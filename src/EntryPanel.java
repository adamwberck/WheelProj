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
    private int weight;

    public int getWeight() {
        return weight;
    }

    @Override
    public String getName() {
        return name;
    }

    private String name;

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
        tfNum.getDocument().addDocumentListener(new DocumentListener() {
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
                try {
                    weight = Integer.parseInt(tfNum.getText().trim());
                    name = tfName.getText().trim();
                    if(!name.isEmpty()){
                        wheelGUI.updateWheel();
                        isSet = true;
                    }else{
                        isSet = false;
                    }
                }
                catch (NumberFormatException ignored){}
                isSet = false;
            }

        });
    }

    public boolean isSet() {
        return isSet;
    }
}