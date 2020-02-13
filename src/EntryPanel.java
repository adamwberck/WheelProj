import javax.swing.*;
import java.awt.*;

public class EntryPanel extends JPanel {
    private JTextField tfName = new JTextField();
    private JTextField tfNum = new JTextField();

    public EntryPanel() {
        this.add(tfName);
        this.add(tfNum);
        this.setOpaque(false);
        tfName.setColumns(15);
        tfNum.setColumns(5);
        tfNum.setHorizontalAlignment(SwingConstants.RIGHT);
        final Font tahoma = new Font("Tahoma", Font.BOLD, 12);
        tfName.setFont(tahoma);
        tfNum.setFont(tahoma);
    }
}
