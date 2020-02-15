import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EntryPanel extends JPanel {
    private WheelGUI wheelGUI;
    private JTextField tfName = new JTextField();
    private JTextField tfNum = new JTextField();
    private JButton remove = new JButton();
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



    public EntryPanel(WheelGUI wheelGUI) {
        this.wheelGUI = wheelGUI;

        this.add(tfName);
        this.add(tfNum);
        this.add(remove);
        this.setOpaque(false);
        tfName.setColumns(15);
        tfNum.setColumns(5);
        tfNum.setHorizontalAlignment(SwingConstants.RIGHT);
        final Font tahoma = new Font("Tahoma", Font.BOLD, 12);
        tfName.setFont(tahoma);
        tfNum.setFont(tahoma);
        remove.setOpaque(false);
        remove.setFont(tahoma);
        remove.setIcon(new ImageIcon("res/close.png"));
        remove.setBorder(BorderFactory.createEmptyBorder());
        remove.setBackground(new Color(60,63,65));
        remove.setSize(25,25);
        tfName.getDocument().addDocumentListener(new EntryDocumentListener());
        tfNum.getDocument().addDocumentListener(new EntryDocumentListener());
    }

    private class EntryDocumentListener implements DocumentListener{

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
