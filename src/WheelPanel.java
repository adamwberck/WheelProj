import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class WheelPanel extends JPanel implements Runnable{
    private static final Font tahoma = new Font("Tahoma", Font.BOLD, 13);
    private static final int SIZE = 450;
    private static final int BORDER_SIZE = (int) (SIZE/12.5);
    private final int INITIAL_X = 25;//(int) (SIZE/7.14287);
    private final int INITIAL_Y = (int) (SIZE/16.6666667);
    private final int DELAY = 25;

    private int x,y;
    private double spinAngle = 0;
    private double spinSpeed = 0;
    private double spinFriction = 0.1;
    private Thread animator;
    private Wheel wheel;
    private boolean isSpinning;

    public WheelPanel(Wheel wheel) {
        initWheelPanel();
        this.wheel = wheel;
    }

    private void initWheelPanel() {
        x = INITIAL_X;
        y = INITIAL_Y;
    }

    @Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawWheel(g);
    }

    private void drawWheel(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_RESOLUTION_VARIANT,RenderingHints.VALUE_RESOLUTION_VARIANT_DPI_FIT);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color b = Color.BLACK;
        Color[] colors = wheel.getColors();
        int angle = (int) ((180+spinAngle) % 360);
        //draw black border
        g2d.setColor(b);
        g2d.fillArc(x, y, SIZE +BORDER_SIZE, SIZE +BORDER_SIZE, 0, 360 );
        ArrayList<Line> lines = new ArrayList<>(wheel.size()*2);
        int ox = x+(SIZE)/2+BORDER_SIZE/2;
        int oy = y+(SIZE)/2+BORDER_SIZE/2;
        for(int i=0;i<wheel.size();i++) {
            WheelEntry entry = wheel.getEntry(i);
            int sAngle = wheel.getAngle(i);
            Color c = colors[ i % colors.length];
            g2d.setColor(c);
            g2d.fillArc(x+BORDER_SIZE/2, y+BORDER_SIZE/2, SIZE, SIZE, angle, sAngle);

            double rads = Math.toRadians(-angle);
            int rx = (int) (ox+Math.cos(rads)*SIZE/2), ry = (int) (oy+Math.sin(rads)*SIZE/2);
            lines.add(Line.drawLineLater(ox,oy, rx,ry));

            rads = Math.toRadians(-angle - sAngle*.5 );
            if(entry.getWeight()>0) {
                drawText(g2d, entry.getName(), ox, oy, rads, Color.BLACK);
            }

            angle += sAngle;
            angle = angle%360;
        }

        g2d.setStroke(new BasicStroke(2));
        if(wheel.size()>1) {
            for (Line line : lines) {
                Line.drawLineNow(g2d, line);
            }
        }

        drawSpoke(g2d, ox, oy);
        drawTriangle(g2d);
    }

    private void drawText(Graphics2D g2d,String text, int ox, int oy, double rads,Color color) {
        AffineTransform orig = g2d.getTransform();
        BufferedImage bi  = new BufferedImage( SIZE/2,50,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(tahoma);
        g2.setColor(color);
        g2.drawString(text,SIZE/2-getFontMetrics(tahoma).stringWidth(text)-SIZE/45,25);
        //g2.drawString(text, (int) (),SIZE/50);
        g2d.rotate(rads,ox,oy);

        g2d.drawImage(bi,ox, (int) (oy-SIZE/22.5),this);

        g2d.setTransform(orig);
    }


    private void drawSpoke(Graphics2D g2d, int ox, int oy) {
        g2d.setColor(Color.BLACK);
        int s1 = (int) (SIZE/11.363636);
        int s2 = (int) (SIZE/13.888888);
        int s3 = (int) (SIZE/20.833333);

        g2d.fillArc(ox-s1/2, oy-s1/2, s1, s1,0, 360);
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillArc(ox-s2/2, oy-s2/2,s2, s2, 0, 360);
        g2d.setColor(Color.BLACK);
        g2d.fillArc(ox-s3/2, oy-s3/2,s3, s3, 0, 360);
    }

    private void drawTriangle(Graphics2D g2d) {
        Polygon triangle = new Polygon();
        int adjy = (int) (SIZE/2.0325203252);
        int adjx = SIZE/10;
        int x1 = (int) (SIZE/19.230769), x2 = (int) (SIZE/9.6153846);
        int y2 = (int) (SIZE/20.8333), y1 = (int) (SIZE/10.41666667);
        g2d.setColor(Color.BLACK);
        triangle.addPoint(x+SIZE+BORDER_SIZE+adjx-x1,y+adjy);
        triangle.addPoint(x+SIZE+BORDER_SIZE+adjx-x1,y+y1+adjy);
        triangle.addPoint(x+SIZE+BORDER_SIZE+adjx-x2,y+y2+adjy);
        g2d.fillPolygon(triangle);
    }

    public void setSpinSpeed(double spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public double getSpinSpeed() {
        return spinSpeed;
    }

    public void setSpinning(boolean isSpinning) {
        this.isSpinning = isSpinning;
    }

    public boolean isSpining() {
        return isSpinning;
    }

    private static class Line{
        int i;int i1;int i2; int i3;

        public Line(int i, int i1, int i2, int i3) {
            this.i = i;
            this.i1 = i1;
            this.i2 = i2;
            this.i3 = i3;
        }

        private static Line drawLineLater(int i, int i1, int i2, int i3) {
            return new Line(i,i1,i2,i3);
        }
        private static void drawLineNow(Graphics2D g2d, Line line) {
            g2d.setColor(Color.BLACK);
            g2d.drawLine(line.i,line.i1,line.i2,line.i3);
        }
    }



    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            spinAngle += spinSpeed;
            spinAngle = spinAngle % 360;
            spinSpeed = Math.max(0, spinSpeed -spinFriction);
            if(spinSpeed == 0 && isSpinning){
                //TODO detect result
            }
            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {

                String msg = String.format("Thread interrupted: %s", e.getMessage());

                JOptionPane.showMessageDialog(this, msg, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            beforeTime = System.currentTimeMillis();
        }
    }
}
