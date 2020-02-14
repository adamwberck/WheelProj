import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class WheelPanel extends JPanel implements Runnable{
    private static final Font tahoma = new Font("Tahoma", Font.BOLD, 12);
    private static final int SIZE = 250;
    private static final int BORDER_SIZE = 20;
    private final int B_WIDTH = 350;
    private final int B_HEIGHT = 350;
    private final int INITIAL_X = 35;
    private final int INITIAL_Y = 15;
    private final int DELAY = 25;

    private int x,y;
    private double spinAngle = 0;
    private double spinSpd = 0;
    private Thread animator;
    private Wheel wheel;

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
        int angle = 180;
        //draw background
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
            lines.add(drawLineLater(ox,oy, rx,ry));

            rads = Math.toRadians(-angle - sAngle*.5 );
            int cx = x+(SIZE)/2+BORDER_SIZE/2-5;
            int cy = y+(SIZE)/2+BORDER_SIZE/2-5;
            rx = (int) (cx+Math.cos(rads)*25); ry = (int) (cy+Math.sin(rads)*25);
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(rads, 0, 0);
            Font rotatedFont = tahoma.deriveFont(affineTransform);
            g2d.setFont(rotatedFont);
            g2d.setColor(Color.RED);
            g2d.drawString("A String",rx,ry);

            angle += sAngle;
            angle = angle%360;
        }
        g2d.setStroke(new BasicStroke(2));
        if(wheel.size()>1) {
            for (Line line : lines) {
                drawLineNow(g2d, line);
            }
        }

        drawSpoke(g2d, ox, oy);

        int cx = x+(SIZE)/2+BORDER_SIZE/2-5;
        int cy = y+(SIZE)/2+BORDER_SIZE/2-5;
        g2d.setColor(Color.RED);
        g2d.fillOval(cx,cy,10,10);

        g2d.setColor(b);
        drawTriangle(g2d);





        //for(int i=-1000;i<1000;i+=50) {
        /*
            drawText(g2d, 200, 155, "zero", 0);
            drawText(g2d, -125, 172, "ninety", 90);
            drawText(g2d, 50, 230, "forty5", 45);
            drawText(g2d, -145, -150, "one80", 180);
            drawText(g2d, 175, -165, "270", 270);
            drawText(g2d, 250, -15, "315", 315);
        //}

         */
    }

    private void drawText(Graphics2D g2d, int ox, int oy, String text, int angle) {
        angle = -angle;
        double rads;
        int rx;
        int ry;
        rads = Math.toRadians(45);
        rx = (int) (ox+Math.cos(rads)*25);
        ry = (int) (oy+Math.sin(rads)*25);
        g2d.setColor(Color.RED);
        AffineTransform orig = g2d.getTransform();
        rads = Math.toRadians(angle);
        g2d.rotate(rads);
        g2d.setFont(tahoma);
        g2d.drawString(text,ox,oy);
        g2d.setTransform(orig);
    }

    private void drawSpoke(Graphics2D g2d, int ox, int oy) {
        g2d.setColor(Color.BLACK);
        g2d.fillArc(ox-11, oy-11, 22, 22,0, 360);
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillArc(ox-9, oy-9,18, 18, 0, 360);
        g2d.setColor(Color.BLACK);
        g2d.fillArc(ox-6, oy-6,12, 12, 0, 360);
    }

    private void drawTriangle(Graphics2D g2d) {
        Polygon triangle = new Polygon();
        int adjy = 120;
        int adjx = 25;
        g2d.setColor(Color.RED);
        triangle.addPoint(x+SIZE+BORDER_SIZE+adjx-13,y+adjy);
        triangle.addPoint(x+SIZE+BORDER_SIZE+adjx-13,y+24+adjy);
        triangle.addPoint(x+SIZE+BORDER_SIZE+adjx-26,y+12+adjy);
        g2d.fillPolygon(triangle);
    }

    public static void drawRotate(Graphics2D g2d, double x, double y, int angle, String text)
    {
        Font font = new Font(null, Font.PLAIN, 10);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(45), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g2d.setFont(rotatedFont);
        g2d.drawString(text,0,0);
    }

    private static class Line{
        int i;int i1;int i2; int i3;

        public Line(int i, int i1, int i2, int i3) {
            this.i = i;
            this.i1 = i1;
            this.i2 = i2;
            this.i3 = i3;
        }
    }
    private static Line drawLineLater(int i, int i1, int i2, int i3) {
        return new Line(i,i1,i2,i3);
    }
    private static void drawLineNow(Graphics2D g2d, Line line) {
        g2d.setColor(Color.BLACK);
        g2d.drawLine(line.i,line.i1,line.i2,line.i3);
    }



    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

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
