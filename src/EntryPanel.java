import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class EntryPanel extends JPanel {
    private WheelGUI wheelGUI;
    private JTextField tfName = new JTextField();
    private JTextField tfNum = new JTextField();
    private JButton sColor = new JButton();
    private Color color;
    private JButton bRemove = new JButton();
    private boolean isSet = false;
    private String name;
    private int weight;

    public int getWeight() {
        return weight;
    }

    @Override
    public String getName() {
        return name;
    }



    public EntryPanel(WheelGUI wheelGUI,Color c) {
        this.wheelGUI = wheelGUI;
        this.color = c;

        this.add(tfName);
        this.add(tfNum);
        this.add(bRemove);
        this.setOpaque(false);
        tfName.setColumns(15);
        tfNum.setColumns(3);
        tfNum.setHorizontalAlignment(SwingConstants.RIGHT);
        final Font tahoma = new Font("Tahoma", Font.BOLD, 12);
        tfName.setFont(tahoma);
        tfNum.setFont(tahoma);
        bRemove.setOpaque(false);
        bRemove.setIcon(new ImageIcon("res/close.png"));
        bRemove.setBorder(BorderFactory.createEmptyBorder());
        bRemove.setBackground(new Color(60,63,65));
        bRemove.setSize(25,25);
        final EntryPanel panelThis = this;
        bRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wheelGUI.removeEntry(panelThis);
            }
        });
        /*
        this.add(sColor);
        sColor.setOpaque(true);
        sColor.setBorder(BorderFactory.createEmptyBorder());
        sColor.setSize(25,25);
        BufferedImage bi = makeIconSquare(this.color);
        sColor.setIcon(new ImageIcon(bi));
        sColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame();
                frame.add(new JColorChooser(color));
                frame.pack();
                frame.setVisible(true);
            }
        });
        */
        tfName.getDocument().addDocumentListener(new EntryDocumentListener(tfName));
        tfNum.getDocument().addDocumentListener(new EntryDocumentListener(tfNum));
    }

    private BufferedImage makeIconSquare(Color color) {
        BufferedImage bi  = new BufferedImage(20,20,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(color);
        g.fillRect(0,0,20,20);
        return  bi;
    }

    private class EntryDocumentListener implements DocumentListener{
        private JTextField textField;

        public EntryDocumentListener(JTextField textField) {
            this.textField = textField;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            warn();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            warn();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            warn();
        }

        public void warn() {
            name = tfName.getText().trim();
            try {
                weight = Integer.parseInt(tfNum.getText().trim());
                isSet = true;
            }
            catch (NumberFormatException ignored){
            }
            if(tfNum.getText().isEmpty() && tfName.getText().isEmpty()) {
                isSet = false;
            }
            else if(tfNum.getText().isEmpty()){//just num empty assume its 1
                if(textField==tfName){
                    tfNum.setText("1");
                }
                weight = 1;
                isSet = true;
            }
            wheelGUI.updateWheel();
        }
    }

    public boolean isSet() {
        return isSet;
    }
}
