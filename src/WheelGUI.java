import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WheelGUI extends JFrame{
    private Wheel wheel = new Wheel();

    private JPanel MainPanel;
    private JScrollPane ScrollPane;
    private List<EntryPanel> entryPanels = new ArrayList(10);
    private JPanel LeftPanel;
    private WheelPanel RightPanel = new WheelPanel(wheel);
    private JPanel ParLeftPanel;
    private JButton Add;
    private JPanel ButtonPanel;


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
        RightPanel.setPreferredSize(new Dimension(ParLeftPanel.getPreferredSize().height,
                ParLeftPanel.getPreferredSize().height));
        MainPanel.add(RightPanel);
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
    }
}
