import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WheelGUI extends JFrame{
    private Wheel wheel;

    private JPanel MainPanel;
    private JScrollPane ScrollPane;
    private List<EntryPanel> entryPanels = new ArrayList(10);
    private JPanel LeftPanel;
    private WheelPanel RightPanel;
    private JPanel ParLeftPanel;
    private JButton Add = new JButton();
    private JPanel AddPanel;

    private final static String FILE_NAME = "wheel_save.wheel";;

    public WheelGUI(Wheel wheel){
        this.wheel = wheel;
        for(int i=0;i<wheel.size();i++){
            EntryPanel panel = wheel.getEntry(i).getPanel();
            panel.initListeners(this);
            entryPanels.add(panel);
            LeftPanel.add(panel);
            adjustHeight(panel,true);
        }
        if(wheel.size()==0){
            EntryPanel panel = new EntryPanel(this);
            panel.setWeight(1);
            entryPanels.add(panel);
            LeftPanel.add(panel);
            adjustHeight(panel,true);
        }
        initWheelGUI();
    }
    public WheelGUI() {
        wheel = new Wheel();
        for(int i=0;i<6;i++){
            EntryPanel panel = new EntryPanel(this);
            panel.setWeight(1);
            entryPanels.add(panel);
            LeftPanel.add(panel);
            adjustHeight(panel,true);
        }
        updateWheel();
        initWheelGUI();
    }

    private void initWheelGUI() {
        setContentPane(MainPanel);
        RightPanel = new WheelPanel(wheel);
        MainPanel.setBackground(Color.black);
        LeftPanel.setBackground(Color.darkGray);
        AddPanel = new JPanel();
        AddPanel.setPreferredSize(new Dimension(entryPanels.get(0).getPreferredSize().width,90));
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
                EntryPanel panel = new EntryPanel(wThis);
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
                if(SwingUtilities.isLeftMouseButton(e)) {
                    spin();
                    RightPanel.setGrabbed(true);
                    RightPanel.setMousePos(e.getX(), e.getY());
                }else if(SwingUtilities.isRightMouseButton(e)){
                    quickSpin();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                RightPanel.setGrabbed(false);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                RightPanel.setGrabbed(false);
            }
        });
    }


    private void quickSpin(){
        RightPanel.setSpinSpeed((new Random().nextDouble()*35+10)*(new Random().nextBoolean() ?  1: -1 ));
        spin();
    }


    private void spin() {
        RightPanel.resetChosen();
        for(EntryPanel panel : entryPanels){
            panel.notifyUnChosen();
        }
    }

    private void adjustHeight(Component panel,boolean increase) {
        int height = panel.getPreferredSize().height;
        Dimension d = LeftPanel.getPreferredSize();
        int dir = increase ? 1 : -1;
        LeftPanel.setPreferredSize(new Dimension(d.width,d.height+height*dir));
    }

    public static void main(String[] args) {
        Wheel wheel = load();
        WheelGUI frame = wheel == null ? new WheelGUI() : new WheelGUI(wheel)  ;

        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                frame.save();
            }
        });

        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void updateWheel() {
        wheel.clearEntries();
        for(EntryPanel panel : entryPanels){
            panel.notifyUnChosen();
            if(panel.isSet()){
                wheel.addEntry(panel,panel.getName(),panel.getWeight());
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
    public static Wheel load(){
        try {
            FileInputStream file = new FileInputStream(FILE_NAME);
            ObjectInputStream in = new ObjectInputStream(file);
            return (Wheel) in.readObject();
        } catch (FileNotFoundException e) {
            //No File
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Error on File Load");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save() {
        try {

            FileOutputStream file = new FileOutputStream(FILE_NAME);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(wheel);
            out.close();

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Save Failed for Some Reason");
        }
    }
}
