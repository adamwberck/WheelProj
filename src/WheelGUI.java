import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    private JButton Add = new JButton();
    private JPanel AddPanel;


    public WheelGUI() {
        setContentPane(MainPanel);
        MainPanel.setBackground(Color.black);
        //MainPanel.setForeground(Color.black);
        //LeftPanel.setForeground(Color.darkGray);
        LeftPanel.setBackground(Color.darkGray);
        final Font tahoma = new Font("Tahoma", Font.BOLD, 12);
        int w = 0;
        for(int i=0;i<6;i++){
            EntryPanel panel = new EntryPanel(this,Color.WHITE);
            entryPanels.add(panel);
            w = panel.getPreferredSize().width;
            LeftPanel.add(panel);
            adjustHeight(panel,true);
        }

        AddPanel = new JPanel();
        AddPanel.setPreferredSize(new Dimension(w,90));
        AddPanel.setOpaque(false);
        Add.setOpaque(false);
        Add.setBackground(new Color(60,63,65));
        Add.setBorder(BorderFactory.createEmptyBorder());
        Add.setIcon(new ImageIcon("res/add.png"));
        AddPanel.add(Add);
        LeftPanel.add(AddPanel);
        adjustHeight(AddPanel,true);
        
        final WheelGUI wThis = this;
        Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EntryPanel panel = new EntryPanel(wThis,Color.WHITE);
                wThis.entryPanels.add(panel);
                LeftPanel.remove(AddPanel);
                LeftPanel.add(panel);
                adjustHeight(panel,true);
                LeftPanel.add(AddPanel);
                LeftPanel.revalidate();
                LeftPanel.repaint();
            }
        });
        RightPanel.setBackground(Color.gray);
        RightPanel.setPreferredSize(new Dimension(ParLeftPanel.getPreferredSize().height,
                ParLeftPanel.getPreferredSize().height));
        MainPanel.add(RightPanel);

        RightPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                spin();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    private void spin() {
        if(RightPanel.getSpinSpeed()==0){
            //TODO DRAG SPIN
            RightPanel.setSpinSpeed(25);
            RightPanel.setSpinning(true);
        }
    }

    private void adjustHeight(Component panel,boolean increase) {
        int height = panel.getPreferredSize().height;
        Dimension d = LeftPanel.getPreferredSize();
        int dir = increase ? 1 : -1;
        LeftPanel.setPreferredSize(new Dimension(d.width,d.height+height*dir));
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

    public void removeEntry(EntryPanel panelThis) {
        entryPanels.remove(panelThis);
        adjustHeight(panelThis,false);
        LeftPanel.remove(panelThis);
        LeftPanel.revalidate();
        LeftPanel.repaint();
        updateWheel();
    }
}
