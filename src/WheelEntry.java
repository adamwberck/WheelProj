public class WheelEntry {
    private Wheel wheel;
    private String name;
    private int weight;
    private EntryPanel entryPanel;

    public int getAngle() {
        return angle;
    }

    private int angle;

    public WheelEntry(Wheel wheel,EntryPanel entryPanel,String name, int weight) {
        this.entryPanel = entryPanel;
        this.name = name;
        this.weight = weight;
        this.wheel = wheel;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public void notifyEntry() {
        entryPanel.notifyChosen();
    }
}
