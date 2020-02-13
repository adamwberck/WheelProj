import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WheelGUI extends JFrame{
    private JPanel MainPanel;
    private JScrollPane ScrollPane;
    private List<EntryPanel> entryPanels = new ArrayList(10);
    private JPanel LeftPanel;
    private JPanel RightPanel;
    private JPanel ParLeftPanel;
    private JButton Add;
    private JPanel ButtonPanel;
    private Wheel wheel = new Wheel();
    private WheelCanvas canvas = new WheelCanvas(wheel);

    public WheelGUI() {
        setContentPane(MainPanel);
        MainPanel.setBackground(Color.black);
        //MainPanel.setForeground(Color.black);
        //LeftPanel.setForeground(Color.darkGray);
        LeftPanel.setBackground(Color.darkGray);
        final Font tahoma = new Font("Tahoma", Font.BOLD, 12);

        for(int i=0;i<6;i++){
            EntryPanel panel = new EntryPanel(this);
            entryPanels.add(panel);
            LeftPanel.add(panel);

        }
        Add.setBackground( new Color(59,149,182));
        Add.setForeground( Color.BLACK);
        Add.setFont(tahoma);
        canvas.setSize(350,300);
        RightPanel.add(canvas);
    }

    public static void main(String[] args) {
        WheelGUI frame = new WheelGUI();
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public void updateWheel() {
        wheel.clearEntries();
        for(EntryPanel panel : entryPanels){
            if(panel.isSet()){
                wheel.addEntry(panel.getName(),panel.getWeight());
            }
        }

        drawWheel();
    }

    private void drawWheel() {
        canvas.repaint();
    }

    private class WheelCanvas extends Canvas{
        Wheel wheel;
        public WheelCanvas(Wheel wheel) {
            super();
            this.wheel = wheel;
        }
        @Override
        public void repaint(){
            Graphics g = getGraphics();
            paint(g);
            super.repaint();
            validate();
        }


        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHints(rh);
            Color b = Color.BLACK;
            g2d.setColor(b);

            Color[] colors = wheel.getColors();
            double angle = 0;
            g2d.setColor(Color.black);
            g2d.fillArc(50, 25, 250, 250, (int)angle, (int) (angle + 90) );

            for(int i=0;i<wheel.size();i++) {
                WheelEntry entry = wheel.getEntry(i);
                double frac = entry.getWeight()/wheel.getTotal();
                double sAngle = frac*360.0;
                Color c = colors[ i % colors.length];
                g2d.setColor(Color.black);
                g2d.fillArc(50, 25, 250, 250, (int)angle, (int) (angle + sAngle) );
                g2d.setColor(c);
                g2d.fillArc(50, 25, 225, 225, (int)angle, (int) (angle + sAngle) );
            }
        }

    }
}
