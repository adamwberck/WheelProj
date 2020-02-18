import java.io.Serializable;

public class WheelEntry implements Serializable {
    private Wheel wheel;
    private String name;
    private double weight;
    private EntryPanel entryPanel;
    private static final long serialVersionUID = 0xADABEC95;

    public int getAngle() {
        return angle;
    }

    private int angle;

    public WheelEntry(Wheel wheel,EntryPanel entryPanel,String name, double weight) {
        this.entryPanel = entryPanel;
        this.name = name;
        this.weight = weight;
        this.wheel = wheel;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public double getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public void notifyEntry() {
        entryPanel.notifyChosen();
    }

    public EntryPanel getPanel() {
        return entryPanel;
    }

}
