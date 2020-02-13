public class WheelEntry {
    String name;
    int weight;

    public WheelEntry(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public double getWeight() {
        return weight*1.0;
    }
}
