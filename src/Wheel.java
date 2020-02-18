import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Wheel implements Serializable {
    private List<WheelEntry> entries = new ArrayList(10);
    private double spinAngle = -45;
    private static final long serialVersionUID = 0xFDADABEC;
    private boolean soundOn = false;

    public void clearEntries() {
        entries.clear();
        total = 0;
    }
    private final static Color[] COLORS = {new Color(0xFF002C),new Color(0xFE641A), new Color(0xFEE101),new Color(0x1A94A),
            new Color(0x17AC3),new Color(0x3C3F9E), new Color(0x773798), new Color(0xFE0094),
            new Color(0x00AFE6), new Color(0x7ECB2D),new Color(0xD7E300), new Color(0xFEAF30)};


    private double total = 0;
    public Color[] getColors() {
        return COLORS;
    }

    public WheelEntry getEntry(int i) {
        return entries.get(i);
    }

    public int size(){
        return entries.size();
    }

    public void addEntry(EntryPanel panel,String name, double weight){
        entries.add(new WheelEntry(this,panel,name,weight));
        total+=weight;
    }

    public int getAngle(int i){
        return entries.get(i).getAngle();
    }

    public double getTotal() {
        return total*1.0;
    }

    public void setSpinAngle(double spinAngle) {
        this.spinAngle = spinAngle;
    }

    public double getSpinAngle() {
        return spinAngle;
    }

    public int drawnSize(){
        int total = 0;
        for(var entry : entries){
            total += entry.getWeight() > 0 ? 1 : 0;
        }
        return total;
    }

    public void updateAngles() {
        int totalAngle = 0;
        for(WheelEntry entry : entries){
            int angle = (int) (((entry.getWeight()*1.0) / (total*1.0))*360);
            entry.setAngle(angle);
            totalAngle+=angle;
        }
        int j=0;
        while(totalAngle<360){
            while (entries.get(j).getWeight()==0 && total!=0){
                j=(j+1) % entries.size();
            }
            int angle = entries.get(j).getAngle();
            entries.get(j).setAngle(angle+1);
            totalAngle++;
            j=(j+1) % entries.size();
        }
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    public void setSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }
}
