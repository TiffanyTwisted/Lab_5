package bsu.rfict.java.group7.lab5.Shirko;





        import java.awt.Toolkit;
        import java.awt.event.ActionEvent;
        import java.io.*;
        import java.util.ArrayList;
        import java.util.Iterator;
        import javax.swing.AbstractAction;
        import javax.swing.Action;
        import javax.swing.JFileChooser;
        import javax.swing.JFrame;
        import javax.swing.JMenu;
        import javax.swing.JMenuBar;
        import javax.swing.JMenuItem;
        import javax.swing.JOptionPane;


public class Main extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private JFileChooser fileChooser = null;
    private JMenuItem resetGraphicsMenuItem;
    private JMenuItem saveGraphicsMenuItem;
    private GraphicsDisplay display = new GraphicsDisplay();
    private boolean fileLoaded = false;

    public Main() {
        super("Обработка событий от мыши");
        this.setSize(700, 500);
        Toolkit kit = Toolkit.getDefaultToolkit();
        this.setLocation((kit.getScreenSize().width - 700) / 2, (kit.getScreenSize().height - 500) / 2);
        this.setExtendedState(6);
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        Action openGraphicsAction = new AbstractAction("Открыть файл с графиком") {
            public void actionPerformed(ActionEvent event) {
                if (Main.this.fileChooser == null) {
                    Main.this.fileChooser = new JFileChooser();
                    Main.this.fileChooser.setCurrentDirectory(new File("."));
                }

                Main.this.fileChooser.showOpenDialog(Main.this);
                Main.this.openGraphics(Main.this.fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(openGraphicsAction);
        Action resetGraphicsAction = new AbstractAction("Отменить все изменения") {
            public void actionPerformed(ActionEvent event) {
                Main.this.display.reset();
            }
        };
        this.resetGraphicsMenuItem = fileMenu.add(resetGraphicsAction);
        this.resetGraphicsMenuItem.setEnabled(false);
        this.getContentPane().add(this.display, "Center");

        Action saveGraphicsAction = new AbstractAction("Сохранить все изменения") {
            public void actionPerformed(ActionEvent event) {
                try {
                    Main.this.saveGraphics(Main.this.fileChooser.getSelectedFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        this.saveGraphicsMenuItem = fileMenu.add(saveGraphicsAction);
        this.saveGraphicsMenuItem.setEnabled(false);
        this.getContentPane().add(this.display, "Center");
    }



    // Считывание данных графика из существующего файла
    protected void openGraphics(File selectedFile) {
        try {
            FileReader fileReader = new FileReader(selectedFile);
            BufferedReader reader = new BufferedReader(fileReader);
            int fileSize = Integer.parseInt(reader.readLine());
            ArrayList graphicsData = new ArrayList(fileSize);
            for (int i = 0; i < fileSize; i++) {
                String line = reader.readLine();
                String[] parts = line.split(" ");
                graphicsData.add(new Double[] {Double.parseDouble(parts[0]), Double.parseDouble(parts[1])});
            }
            if (fileSize > 0) {
                this.fileLoaded = true;
                this.resetGraphicsMenuItem.setEnabled(true);
                this.saveGraphicsMenuItem.setEnabled(true);
                this.display.displayGraphics(graphicsData);
            }
            fileReader.close();
            reader.close();

        } catch (FileNotFoundException var6) {
            JOptionPane.showMessageDialog(this, "Указанный файл не найден", "Ошибка загрузки данных", 2);
        } catch (IOException var7) {
            JOptionPane.showMessageDialog(this, "Ошибка чтения координат точек из файла", "Ошибка загрузки данных", 2);
        }
    }


    protected void saveGraphics(File selectedFile) throws IOException {
        PrintStream out = new PrintStream(selectedFile);
        ArrayList graphicsData = display.getGraphicsData();
        out.println(Integer.toString(graphicsData.size()));
        Iterator var = graphicsData.iterator();
        while(var.hasNext()) {
            Double[] point = (Double[])var.next();
            out.println(Double.toString(point[0]) + " " + Double.toString(point[1]));
        }
    }




    public static void main(String[] args) {
        Main frame = new Main();
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
    }
}