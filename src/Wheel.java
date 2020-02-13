import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Wheel {
    public void clearEntries() {
        entries.clear();
        total = 0;
    }

    private List<WheelEntry> entries = new ArrayList(10);
    private Color[] colors = {Color.CYAN,Color.green,Color.ORANGE,Color.PINK,Color.YELLOW,Color.red};
    private int total = 0;
    public Color[] getColors() {
        return colors;
    }

    public WheelEntry getEntry(int i) {
        return entries.get(i);
    }

    public int size(){
        return entries.size();
    }

    public void addEntry(String name, int weight){
        entries.add(new WheelEntry(name,weight));
        total+=weight;
    }

    public double getTotal() {
        return total*1.0;
    }
}
