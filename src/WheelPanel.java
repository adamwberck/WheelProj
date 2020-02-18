import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class WheelPanel extends JPanel implements Runnable{
    private static final int SIZE = 450;
    private static final int BORDER_SIZE = (int) (SIZE/12.5);
    private final int INITIAL_X = 25;//(int) (SIZE/7.14287);
    private final int INITIAL_Y = (int) (SIZE/16.6666667);
    private final int INITIAL_RY = (int) (SIZE*1.2);
    private final int DELAY = 25;

    private int x,y;
    private double spinAngle;
    private double spinSpeed = 0;
    final public static double SPIN_FRICTION = 0.025;
    private Thread animator;
    private Wheel wheel;
    private boolean isSpinning;
    private WheelEntry chosen = null;
    private int resultY = INITIAL_RY;
    private boolean grabbed;
    private int mouseX;
    private int mouseY;
    private WheelEntry lastEntry;
    final public static double MIN_SPIN_RATE = .225;
    final public static double MIN_SPIN_CONST = 4.25-MIN_SPIN_RATE;
    final public static int MAX_SPEED = 35;


    public WheelPanel(Wheel wheel) {
        initWheelPanel();
        this.wheel = wheel;
        spinAngle = wheel.getSpinAngle();
        lastEntry = getChosen();
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
        if(chosen!=null){
            chosen.notifyEntry();
        }
    }


    private void drawWheel(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        g2D.setRenderingHint(RenderingHints.KEY_RESOLUTION_VARIANT,RenderingHints.VALUE_RESOLUTION_VARIANT_DPI_FIT);
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color b = Color.BLACK;
        Color[] colors = wheel.getColors();
        int angle = (int) ((0+spinAngle) % 360);
        //draw black border
        g2D.setColor(b);
        g2D.fillArc(x, y, SIZE +BORDER_SIZE, SIZE +BORDER_SIZE, 0, 360 );
        ArrayList<Line> lines = new ArrayList<>(wheel.size()*2);
        int ox = x+(SIZE)/2+BORDER_SIZE/2;
        int oy = y+(SIZE)/2+BORDER_SIZE/2;
        for(int i=0;i<wheel.size();i++) {
            WheelEntry entry = wheel.getEntry(i);
            int sAngle = wheel.getAngle(i);
            Color c = colors[ i % colors.length];
            if(i == wheel.size()-1 && wheel.size()%colors.length == 1){
                c = colors[ (i+1) % colors.length];
            }
            g2D.setColor(c);
            g2D.fillArc(x+BORDER_SIZE/2, y+BORDER_SIZE/2, SIZE, SIZE, angle, sAngle);

            double rads = Math.toRadians(-angle);
            int rx = (int) (ox+Math.cos(rads)*SIZE/2), ry = (int) (oy+Math.sin(rads)*SIZE/2);
            lines.add(Line.drawLineLater(ox,oy, rx,ry));

            rads = Math.toRadians(-angle - sAngle*.5 );
            if(entry.getWeight()>0) {
                drawText(g2D, entry.getName(), ox, oy, rads, Color.BLACK,sAngle);
            }

            angle += sAngle;
            angle = angle%360;
        }

        g2D.setStroke(new BasicStroke(2));
        if(wheel.size()>1) {
            for (Line line : lines) {
                Line.drawLineNow(g2D, line);
            }
        }

        drawSpoke(g2D, ox, oy);
        drawTriangle(g2D);
    }

    private void drawText(Graphics2D g2d, String text, int ox, int oy, double rads, Color color, int angle) {
        AffineTransform orig = g2d.getTransform();
        BufferedImage bi  = new BufferedImage( SIZE/2,50,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


        g2.setColor(color);
        int fontSize = angle<15 ? 10 : 14;
        Font tahoma = new Font("Tahoma", Font.BOLD, fontSize);
        int width = getFontMetrics(tahoma).stringWidth(text);
        int charStrip = 0;

        while (width > SIZE/2 - SIZE/14){
            if(fontSize>10) {//reduce size
                tahoma = new Font("Tahoma", Font.BOLD, --fontSize);
            }else{//reduce string
                text = text.substring(0,text.length()-charStrip++-1);
                if(text.charAt(text.length()-1)!='…'){
                    text = text+"…";
                }
            }
            width = getFontMetrics(tahoma).stringWidth(text);
        }

        g2.setFont(tahoma);
        g2.drawString(text,SIZE/2 - width - SIZE/45,25);
        //g2.drawString(text, (int) (),SIZE/50);
        g2d.rotate(rads,ox,oy);

        g2d.drawImage(bi,ox, (int) (oy-SIZE/22.5),this);

        g2d.setTransform(orig);
    }


    private void drawSpoke(Graphics2D g2d, int ox, int oy) {
        g2d.setColor(Color.BLACK);
        int s1 = (int) (SIZE/10.363636);
        int s2 = (int) (SIZE/11.888888);

        g2d.fillArc(ox-s1/2, oy-s1/2, s1, s1,0, 360);
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillArc(ox-s2/2, oy-s2/2,s2, s2, 0, 360);
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


    public void resetChosen() {
        chosen = null;
    }

    public void setGrabbed(boolean b) {
        grabbed = b;
    }

    public void setMousePos(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
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



    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            spinAngle += spinSpeed;
            if(Math.abs(spinAngle)>360){
                spinAngle-=360*Math.signum(spinAngle);
            }
            spinSpeed = spinSpeed > 0 ? Math.max(0, spinSpeed-SPIN_FRICTION) : Math.min(0, spinSpeed+SPIN_FRICTION);
            if(spinSpeed == 0 && isSpinning){
                chosen = getChosen();
                isSpinning = false;
            }
            wheel.setSpinAngle(spinAngle);

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


            if(grabbed){
                handleGrabbed();
            }
            else if(Math.abs(spinSpeed) > MIN_SPIN_RATE *wheel.drawnSize()+MIN_SPIN_CONST){
                isSpinning = true;
            }

            if(Math.abs(spinSpeed)>0){
                var entry = getChosen();
                if(lastEntry!=entry){
                    if(!grabbed) {
                        System.out.println("hit "+spinSpeed);
                        if (Math.abs(spinSpeed) < .8) {
                            spinSpeed *= -1;
                            spinAngle += Math.signum(spinSpeed);
                            spinAngle = spinAngle % 360;
                            entry = getChosen();
                        }
                        spinSpeed -= .25 * Math.signum(spinSpeed);
                    }
                    lastEntry = entry;
                    playSound("hit2.wav",wheel.isSoundOn());
                }
            }


            beforeTime = System.currentTimeMillis();
        }
    }

    public static synchronized void playSound(final String url,boolean isSoundOn) {
        if(isSoundOn) {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            new Thread(() -> {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            WheelGUI.class.getResourceAsStream(url));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }).start();
        }
    }

    private void handleGrabbed() {
        isSpinning = false;
        int newMouseX = MouseInfo.getPointerInfo().getLocation().x-getLocationOnScreen().x;
        int newMouseY = MouseInfo.getPointerInfo().getLocation().y-getLocationOnScreen().y;
        int ox = x+(SIZE)/2+BORDER_SIZE/2;
        int oy = y+(SIZE)/2+BORDER_SIZE/2;

        double sy =  mouseY-oy;
        double sx =  mouseX-ox;
        double ey = newMouseY-oy;
        double ex = newMouseX-ox;
        double sDir = Math.toDegrees(Math.atan(sy/sx));
        double eDir = Math.toDegrees(Math.atan(ey/ex));
        double speed;
        speed = sDir - eDir;
        if(Math.abs(speed)>90){//dumb check
            speed=0;
        }
        System.out.println(speed);
        double maxSpeed = (MAX_SPEED >> 2)*3 + new Random().nextDouble()*(MAX_SPEED >> 2);
        spinSpeed = speed > 0 ? Math.min(speed,maxSpeed) : Math.max(speed,-maxSpeed);
        System.out.println(spinSpeed);
        mouseX=newMouseX;
        mouseY=newMouseY;
    }

    private WheelEntry getChosen() {
        int trueAngle = spinAngle>0  ? (int) -(spinAngle-360) : (int) -(spinAngle);
        int testAngle = 0;
        for(int i=0;i<wheel.size();i++){
            WheelEntry entry = wheel.getEntry(i);
            testAngle+= entry.getAngle();
            if(testAngle>trueAngle){
                resultY=INITIAL_RY;
                return entry;
            }
        }
        return null;
    }
}
