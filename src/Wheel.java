import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Wheel {
    public void clearEntries() {
        entries.clear();
        total = 0;
    }

    private List<WheelEntry> entries = new ArrayList(10);
    private Color[] colors = {Color.CYAN,Color.green,Color.ORANGE,Color.PINK,Color.YELLOW,Color.red,Color.green,Color.ORANGE,Color.PINK,Color.YELLOW,Color.red};
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

    public void addEntry(EntryPanel panel,String name, int weight){
        entries.add(new WheelEntry(this,panel,name,weight));
        total+=weight;
    }

    public int getAngle(int i){
        int totalAngle = 0;
        for(WheelEntry entry : entries){
            int angle = (int) (((entry.getWeight()*1.0) / (total*1.0))*360);
            entry.setAngle(angle);
            totalAngle+=angle;
        }
        int j=0;
        while(totalAngle<360){
            int angle = entries.get(j).getAngle();
            entries.get(j).setAngle(angle+1);
            totalAngle++;
            j=(j+1) % entries.size();
        }
        return entries.get(i).getAngle();
    }

    public double getTotal() {
        return total*1.0;
    }
}
