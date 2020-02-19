import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WheelGUI extends JFrame{
    private Wheel wheel;

    private JPanel MainPanel;
    private JScrollPane ScrollPane;
    private List<EntryPanel> entryPanels = new ArrayList<>(10);
    private JPanel LeftPanel;
    private WheelPanel RightPanel;
    private JPanel ParLeftPanel;
    private JButton Add = new JButton();
    private JPanel AddPanel;

    public final static String PREF_NAME = "SAVE_LOC";
    public final static String FOLDER_NAME = System.getProperty("user.home") + "\\WheelJarSaves";
    public final static String FILE_NAME = FOLDER_NAME + "\\wheel_save.wheel";
    public final static String PREF_FOLDER = System.getProperty("user.home") + "\\WheelPrefs";
    public final static String PREF_FILE = PREF_FOLDER + "\\wheel.pref";


    public WheelGUI(Wheel wheel){
        this.wheel = wheel;
        setLeftPanel();
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

    public boolean isSoundOn() {
        return wheel.isSoundOn();
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
        Add.setIcon(new ImageIcon(WheelGUI.class.getResource("add.png")));
        AddPanel.add(Add);
        LeftPanel.add(AddPanel);
        adjustHeight(AddPanel,true);

        final WheelGUI wThis = this;
        Add.addActionListener(e -> {
            var panel = new EntryPanel(wThis);
            wThis.entryPanels.add(panel);
            LeftPanel.remove(AddPanel);
            LeftPanel.add(panel);
            adjustHeight(panel,true);
            LeftPanel.add(AddPanel);
            LeftPanel.revalidate();
            LeftPanel.repaint();
        });
        RightPanel.setBackground(Color.gray);
        RightPanel.setPreferredSize(new Dimension(ParLeftPanel.getPreferredSize().height,
                ParLeftPanel.getPreferredSize().height));
        MainPanel.add(RightPanel);

        RightPanel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getX()+" "+e.getY());
                int x = e.getX(), y = e.getY();
                if(x > 472 && x <526 && y>32 && y<73){
                    wheel.setSoundOn(!wheel.isSoundOn());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX(), y = e.getY();
                if(!(x > 472 && x <526 && y>32 && y<73)) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        spin();
                        RightPanel.setGrabbed(true);
                        RightPanel.setMousePos(e.getX(), e.getY());
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        quickSpin();
                    }
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
        var r = new Random();
        var minSpeed = 1.5 *
                (WheelPanel.MIN_SPIN_RATE *wheel.drawnSize()+WheelPanel.SPIN_FRICTION  + WheelPanel.MIN_SPIN_CONST);
        RightPanel.setSpinSpeed((r.nextDouble()*(WheelPanel.MAX_SPEED-minSpeed)+minSpeed)
                * (r.nextBoolean() ?  1: -1 ));
        System.out.println(RightPanel.getSpinSpeed());
        spin();
    }


    private void spin() {
        RightPanel.resetChosen();
        for(EntryPanel panel : entryPanels){
            panel.notifyUnChosen();
        }
    }

    private void adjustHeight(Component panel,boolean increase) {
        System.out.println(LeftPanel.getPreferredSize().height);
        int height = panel.getPreferredSize().height;
        var d = LeftPanel.getPreferredSize();
        int dir = increase ? 1 : -1;
        LeftPanel.setPreferredSize(new Dimension(d.width,d.height+height*dir));
        System.out.println(LeftPanel.getPreferredSize().height);
    }

    public static void main(String[] args) {
        System.out.println(FILE_NAME);
        Wheel loadWheel = null;
        try {
            loadWheel = load(getFileNameFromFile());
        } catch (StreamCorruptedException i) {
            showWrongFileError();
        }
        var frame = loadWheel == null ? new WheelGUI() : new WheelGUI(loadWheel)  ;
        frame.setJMenuBar(frame.createMenuBar());
        frame.setTitle("Wheel - " + new File(frame.wheel.getSaveLocation()).getName());

        try {
            BufferedImage icon = ImageIO.read(WheelGUI.class.getResource("iconBIG.png"));
            frame.setIconImage(icon);
        } catch (IOException ignored) { }



        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if(frame.wheel.isAutoSaveOn()) {
                    frame.save();
                }
            }
        });
        final var fThis = frame;
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            private boolean controlDown = false;

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if(e.isControlDown() && e.getID()==KeyEvent.KEY_PRESSED){
                    controlDown = true;
                }else if(e.getID()==KeyEvent.KEY_RELEASED){
                    controlDown = false;
                }
                if(controlDown && e.getKeyCode()==KeyEvent.VK_M && e.getID()==KeyEvent.KEY_PRESSED){
                    fThis.setSetSoundOn(!fThis.isSoundOn());
                }
                if(controlDown && e.getKeyCode()==KeyEvent.VK_S && e.getID()==KeyEvent.KEY_PRESSED){
                    fThis.save();
                }
                return false;
            }
        });
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private static String getFileNameFromFile() {
        try {
            if(!new File(WheelGUI.PREF_FOLDER).exists()){
                new File(WheelGUI.PREF_FOLDER).mkdir();
            }
            return (String) new ObjectInputStream(new FileInputStream(WheelGUI.PREF_FILE)).readObject();
        } catch (FileNotFoundException fe){
            return FILE_NAME;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        var fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        final var autoSaveItem = new JMenuItem(getAutoSaveString(wheel));
        autoSaveItem.addActionListener(e -> {
            wheel.setAutoSaveOn(!wheel.isAutoSaveOn());
            autoSaveItem.setText(getAutoSaveString(wheel));
        });

        var saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> {
            save();
        });
        var saveAsItem = new JMenuItem("Save As");
        saveAsItem.addActionListener(e -> { showSaveLoadDialog(true,autoSaveItem); });

        var loadItem = new JMenuItem("Load");
        loadItem.addActionListener(e -> showSaveLoadDialog(false,autoSaveItem));


        fileMenu.add(autoSaveItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(loadItem);

        return menuBar;
    }

    private void showSaveLoadDialog(boolean isSave,JMenuItem autoSaveItem) {
        //location?
        var fileChooser = new JFileChooser();
        var str = isSave ? "save" : "load";
        fileChooser.setDialogTitle("Choose "+str+" location:");
        fileChooser.setCurrentDirectory(new File(wheel.getFolderLocation()));
        int userSelection;
        if(isSave) {
            userSelection = fileChooser.showSaveDialog(this);
        } else{
            userSelection = fileChooser.showOpenDialog(this);
        }
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            if( !isSave && wheel.isAutoSaveOn()) {
                save();
            }
            File file = fileChooser.getSelectedFile();
            String oldLoc = wheel.getSaveLocation();
            wheel.setSaveLocation(file.getAbsolutePath());
            setTitle("Wheel - " + new File(wheel.getSaveLocation()).getName());
            if (isSave) {
                save();
            } else {
                var loc = wheel.getSaveLocation();
                wheel.setSaveLocation(loc);
                setTitle("Wheel - " + new File(wheel.getSaveLocation()).getName());
                try {
                    wheel = load(loc);
                    autoSaveItem.setText(getAutoSaveString(wheel));
                    LeftPanel.removeAll();
                    var w = entryPanels.get(0).getPreferredSize().width;
                    entryPanels.clear();
                    LeftPanel.setPreferredSize(new Dimension(w,10));
                    setLeftPanel();
                    LeftPanel.add(AddPanel);
                    adjustHeight(AddPanel,true);
                    LeftPanel.revalidate();
                    RightPanel.setWheel(wheel);
                    updateWheel();
                }catch(ClassCastException | NullPointerException | StreamCorruptedException e){
                    wheel.setSaveLocation(oldLoc);
                    setTitle("Wheel - " + new File(wheel.getSaveLocation()).getName());
                    showWrongFileError();
                }
            }
        }
    }

    private static void showWrongFileError() {
        JOptionPane.showMessageDialog(null,"Wrong File or Corrupted");
    }

    private void setLeftPanel() {
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
    }

    private static String getAutoSaveString(Wheel wheel) throws NullPointerException{
        return "Auto Save " + (wheel.isAutoSaveOn() ? "On" : "Off");
    }

    private void setSetSoundOn(boolean b) {
        wheel.setSoundOn(b);
    }

    public void updateWheel() {
        wheel.clearEntries();
        for(EntryPanel panel : entryPanels){
            panel.notifyUnChosen();
            if(panel.isSet()){
                wheel.addEntry(panel,panel.getName(),panel.getWeight());
            }
        }
        if(wheel.size()>0) {
            wheel.updateAngles();
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
    public static Wheel load(String fileLocation) throws ClassCastException, StreamCorruptedException{
        try {
            FileInputStream file = new FileInputStream(fileLocation);
            ObjectInputStream in = new ObjectInputStream(file);
            return (Wheel) in.readObject();
        } catch (FileNotFoundException e) {
            //No File
            return null;
        }catch(StreamCorruptedException e){
            throw  e;
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
            File folder = new File(wheel.getFolderLocation());
            if(!folder.exists()){
                //noinspection ResultOfMethodCallIgnored
                folder.mkdir();
            }
            FileOutputStream file = new FileOutputStream(wheel.getSaveLocation());
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(wheel);
            out.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                JOptionPane.showMessageDialog(null,"Save Failed See log.txt");
                FileWriter file = new FileWriter("log.txt");
                file.write(Arrays.toString(e.getStackTrace()));
                file.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,"Save Failed and Log Failed " +
                        "for Some Reason");
                ex.printStackTrace();
            }

        }
    }
}
