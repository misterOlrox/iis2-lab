import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyTableModel extends AbstractTableModel {

    private List<Mushroom> mushrooms;

    public MyTableModel(List<Mushroom> mushrooms) {
        this.mushrooms = mushrooms;
    }

    @Override
    public int getRowCount() {
        return mushrooms.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int r, int c) {
        switch (c) {
            case 0:
                return mushrooms.get(r).getId();
            case 1:
                return mushrooms.get(r).attrVectStr();
            default:
                return "";
        }
    }

    public Mushroom removeMushroomAt(int r) {
        var mush = mushrooms.remove(r);
        fireTableDataChanged();
        return mush;
    }

    public void addMushroom(Mushroom mushroom) {
        mushrooms.add(mushroom);
        fireTableDataChanged();
    }

    public List<Mushroom> removeMushrooms(int count) {
        List<Mushroom> removed = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int ind = random.nextInt(mushrooms.size());
            removed.add(mushrooms.remove(ind));
        }
        fireTableDataChanged();
        return removed;
    }

    public void addMushrooms(List<Mushroom> mushrooms) {
        this.mushrooms.addAll(mushrooms);
        fireTableDataChanged();
    }

    public List<Mushroom> removeAllMushrooms() {
        List<Mushroom> removed = new ArrayList<>(mushrooms);
        mushrooms.clear();
        fireTableDataChanged();
        return removed;
    }

    public List<Mushroom> getAllMushrooms() {
        return new ArrayList<>(mushrooms);
    }
}
