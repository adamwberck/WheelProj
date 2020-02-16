import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;

public class EntryPanel extends JPanel {
    transient private WheelGUI wheelGUI;
    private JTextField tfName = new JTextField();
    private JTextField tfNum = new JTextField();
    private JButton sColor = new JButton();
    private Color color;
    private JButton bRemove = new JButton();
    private boolean isSet = false;
    private boolean chosen = false;
    private String name;
    private double weight;

    public double getWeight() {
        return weight;
    }

    @Override
    public String getName() {
        return name;
    }



    public EntryPanel(WheelGUI wheelGUI) {
        this.wheelGUI = wheelGUI;

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
        bRemove.setFocusable(false);
        bRemove.setIcon(new ImageIcon(WheelGUI.class.getResource("close.png")));
        bRemove.setBorder(BorderFactory.createEmptyBorder());
        bRemove.setBackground(new Color(60,63,65));
        bRemove.setSize(25,25);
        initListeners(wheelGUI);
    }

    public void initListeners(WheelGUI wheelGUI) {
        final EntryPanel panelThis = this;
        tfName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                tfName.setScrollOffset(0);
            }
        });
        tfNum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                tfNum.setScrollOffset(0);
            }
        });
        bRemove.addActionListener(e -> wheelGUI.removeEntry(panelThis));
        initDocumentListeners(wheelGUI);
    }

    private void initDocumentListeners(WheelGUI wheelGUI) {
        tfName.getDocument().addDocumentListener(new EntryDocumentListener(tfName));
        tfNum.getDocument().addDocumentListener(new EntryDocumentListener(tfNum));
        this.wheelGUI = wheelGUI;
    }

    private BufferedImage makeIconSquare(Color color) {
        BufferedImage bi  = new BufferedImage(20,20,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(color);
        g.fillRect(0,0,20,20);
        return  bi;
    }

    public void setText(String s) {
        tfName.setText(s);
    }

    public void notifyChosen() {
        chosen = true;
        tfName.setBackground(Color.YELLOW);
        tfNum.setBackground(Color.YELLOW);
        tfName.revalidate();
        tfName.repaint();
        tfNum.revalidate();
        tfNum.repaint();
    }

    public void notifyUnChosen() {
        chosen = false;
        tfName.setBackground(Color.WHITE);
        tfNum.setBackground(Color.WHITE);
        tfName.revalidate();
        tfName.repaint();
        tfNum.revalidate();
        tfNum.repaint();
    }

    public void setWeight(int i) {
        tfNum.setText(""+i);
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
            System.out.println(tfName.getScrollOffset());
            name = tfName.getText().trim();
            try {
                weight = Double.parseDouble(tfNum.getText().trim());
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
