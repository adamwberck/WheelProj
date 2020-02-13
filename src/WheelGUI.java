import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class WheelGUI extends JFrame{
    private JPanel MainPanel;
    private JScrollPane ScrollPane;
    private JTextField tfName1;
    private JTextField tfNum1;
    private JPanel Entry1;
    private JPanel Entry2;
    private JTextField tfName2;
    private JTextField tfNum2;
    private JPanel LeftPanel;
    private JPanel RightPanel;
    private JPanel ParLeftPanel;
    private JButton Add;
    private JPanel ButtonPanel;


    public WheelGUI() {
        setContentPane(MainPanel);
        MainPanel.setBackground(Color.black);
        //MainPanel.setForeground(Color.black);
        //LeftPanel.setForeground(Color.darkGray);
        LeftPanel.setBackground(Color.darkGray);
        tfNum1.setHorizontalAlignment(SwingConstants.RIGHT);
        tfNum2.setHorizontalAlignment(SwingConstants.RIGHT);
        final Font tahoma = new Font("Tahoma", Font.BOLD, 12);
        tfName1.setFont(tahoma);
        tfName2.setFont(tahoma);
        tfNum1.setFont(tahoma);

        EntryPanel entry1 = new EntryPanel();
        LeftPanel.add(entry1);
        tfNum2.setFont(tahoma);
        Add.setBackground( new Color(59,149,182));
        Add.setForeground( Color.BLACK);
        Add.setFont(tahoma);
    }

    public static void main(String[] args) {
        WheelGUI frame = new WheelGUI();
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
