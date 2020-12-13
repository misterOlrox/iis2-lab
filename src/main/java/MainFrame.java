import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private final static String dataPath = "./src/main/resources/mushrooms.txt";

    private JPanel rootPanel;
    private JTextField textField1;
    private JTable table1;
    private JTable table2;
    private JTable table3;
    private JButton добавитьВОбучающуюButton;
    private JButton добавитьВКонтрольнуюButton;
    private JButton добавитьNВОбучButton;
    private JButton добавитьNВКонтрButton;
    private JTextField textField2;
    private JButton обучитьButton;
    private JButton контрольButton;
    private JButton очиститьButton;
    private JButton очиститьButton1;
    private JButton убратьButton;
    private JButton убратьButton1;

    private List<Mushroom> X;
    private RecognitionAlgorithm recognitionAlgorithm = new RecognitionAlgorithm();

    public MainFrame() {
        super("");
        setContentPane(rootPanel);

        System.out.println("Program started");
        System.out.println("Parsing file " + dataPath);
        X = MushroomDataParser.parse(dataPath);

        textField1.setText("Количество данных в датасете = " + X.size());

        MyTableModel myTableModel1 = new MyTableModel(new ArrayList<>(X));
        table1.setModel(myTableModel1);
        MyTableModel myTableModel2 = new MyTableModel(new ArrayList<>());
        table2.setModel(myTableModel2);
        MyTableModel myTableModel3 = new MyTableModel(new ArrayList<>());
        table3.setModel(myTableModel3);

        добавитьВОбучающуюButton.addActionListener(l -> {
            int row = table1.getSelectedRow();
            if (row != -1) {
                Mushroom mushroom = myTableModel1.removeMushroomAt(row);
                myTableModel2.addMushroom(mushroom);
            }
        });
        добавитьВКонтрольнуюButton.addActionListener(l -> {
            int row = table1.getSelectedRow();
            if (row != -1) {
                Mushroom mushroom = myTableModel1.removeMushroomAt(row);
                myTableModel3.addMushroom(mushroom);
            }
        });
        добавитьNВОбучButton.addActionListener(l -> {
            int count = Integer.parseInt(textField2.getText());
            if (count > 0) {
                List<Mushroom> mushrooms = myTableModel1.removeMushrooms(count);
                myTableModel2.addMushrooms(mushrooms);
            }
        });
        добавитьNВКонтрButton.addActionListener(l -> {
            int count = Integer.parseInt(textField2.getText());
            if (count > 0) {
                List<Mushroom> mushrooms = myTableModel1.removeMushrooms(count);
                myTableModel3.addMushrooms(mushrooms);
            }
        });
        очиститьButton.addActionListener(l -> {
            myTableModel1.addMushrooms(myTableModel2.removeAllMushrooms());
        });
        очиститьButton1.addActionListener(l -> {
            myTableModel1.addMushrooms(myTableModel3.removeAllMushrooms());
        });
        убратьButton.addActionListener(l -> {
            int row = table2.getSelectedRow();
            if (row != -1) {
                Mushroom mushroom = myTableModel2.removeMushroomAt(row);
                myTableModel1.addMushroom(mushroom);
            }
        });
        убратьButton1.addActionListener(l -> {
            int row = table3.getSelectedRow();
            if (row != -1) {
                Mushroom mushroom = myTableModel3.removeMushroomAt(row);
                myTableModel1.addMushroom(mushroom);
            }
        });

        обучитьButton.addActionListener(l -> {
            recognitionAlgorithm.train(myTableModel2.getAllMushrooms());
        });

        контрольButton.addActionListener(l -> {
            String result = recognitionAlgorithm.control(myTableModel3.getAllMushrooms());
            JOptionPane.showMessageDialog(this, result);
        });

        setMinimumSize(new Dimension(800, 400));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
