public class WheelEntry {
    private Wheel wheel;
    private String name;
    private int weight;

    public int getAngle() {
        return angle;
    }

    private int angle;

    public WheelEntry(Wheel wheel,String name, int weight) {
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
}
