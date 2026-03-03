package testing;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Главный класс приложения "Пятнашки".
 * Содержит точку входа и статические ссылки на основные компоненты игры.
 * Реализует классическую логику игры "пятнашки" на поле 3x3.
 */
public class Pyatnashka {
    private static final int FRAME_H = 415;
    private static final int FRAME_W = 360;

    private static final int GAME_PAN_H = 360;
    private static final int GAME_PAN_W = 360;

    private static MainFrame mainFrame;
    private static GamePanel gamePanel;
    private static GameLogic gameLogic;

    /**
     * Точка входа в приложение. Запускает главное окно в потоке обработки событий.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String... args){
        EventQueue.invokeLater(() -> {
            mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }

    /**
     * Главное окно приложения.
     * Содержит игровую панель и строку меню.
     */
    private static class MainFrame extends JFrame{
        /**
         * Конструктор главного окна.
         * Устанавливает параметры окна, создает игровую логику и панель, добавляет меню.
         */
        public MainFrame(){
            setLayout(new BorderLayout());
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            setSize(FRAME_W, FRAME_H);
            setTitle("Пятнашки");
            setResizable(false);
            setIconImage(
                    new ImageIcon(
                            Objects.requireNonNull(this.getClass().getResource("/rubik.png"))
                    ).getImage());

            gameLogic = new GameLogic();
            gamePanel = new GamePanel();
            add(gamePanel, BorderLayout.SOUTH);

            setJMenuBar(new MainMenuBar());
        }

    }

    /**
     * Игровая панель с кнопками-костяшками.
     * Отображает текущее состояние игрового поля в виде сетки 3x3.
     */
    private static class GamePanel extends JPanel{
        /**
         * Отображение цифры на кнопке в соответствующий цвет фона.
         * Обеспечивает индивидуальную окраску каждой костяшки.
         */
        public static Map<String, Color> colors = Map.of(
                "1", new Color(85,85,255),
                "2", new Color(254,1,154),
                "3", new Color(57,255,20),
                "4", new Color(188,19,254),
                "5", new Color(4,217,255),
                "6", new Color(248,0,0),
                "7", new Color(204,255,0),
                "8", new Color(255,164,32)
        );

        /**
         * Конструктор игровой панели.
         * Устанавливает сетку 3x3 и выполняет первичную отрисовку кнопок.
         */
        public GamePanel(){
            setLayout(new GridLayout(3, 3));
            rePaintButtons();
        }

        /**
         * Отрисовывает игровое поле на основе текущего состояния массива field.
         * Создает кнопки для цифр и пустые панели для пустоты (*).
         * Полностью перестраивает панель при каждом вызове.
         */
        public void rePaintButtons(){
            int i = 0;

            removeAll();
            revalidate();
            repaint();

            GameButton btn;

            for (String s : gameLogic.getField()){
                if (!s.equals("*")){
                    btn = new GameButton(i / 3, i % 3, s);
                    btn.setFont(new Font("Arial", Font.BOLD, 64));
                    btn.setBackground(colors.get(s));
                    add(btn);
                } else {
                    add(new JPanel());
                }

                i++;
            }
            revalidate();
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(GAME_PAN_H, GAME_PAN_W);
        }
    }

    /**
     * Класс игровой кнопки-костяшки.
     * Хранит свою позицию в сетке и обрабатывает нажатия.
     */
    private static class GameButton extends JButton implements Comparable<GameButton>{
        private int posX;
        private int posY;

        /**
         * Создает кнопку с заданными координатами и значением.
         *
         * @param x координата по горизонтали (строка)
         * @param y координата по вертикали (столбец)
         * @param val текст кнопки (цифра)
         */
        public GameButton(int x, int y, String val){
            super(val);
            posX = x;
            posY = y;
            addActionListener((e) -> {
                gameLogic.move(this);
                gamePanel.rePaintButtons();
                if (gameLogic.isWin()){
                    JOptionPane.showMessageDialog(mainFrame, "Выигрыш!");
                }
            });
        }

        /**
         * Устанавливает новую позицию кнопки в сетке.
         *
         * @param nX новая координата по горизонтали
         * @param nY новая координата по вертикали
         */
        public void setPosition(int nX, int nY){
            posY = nY;
            posX = nX;
        }

        /**
         * Возвращает индекс кнопки в одномерном массиве поля.
         *
         * @return индекс (posX*3 + posY)
         */
        public int getIndex(){
            return posX*3 + posY;
        }

        /**
         * Возвращает текстовое значение кнопки.
         *
         * @return цифра на кнопке
         */
        public String getVal(){
            return super.getText();
        }

        @Override
        public int compareTo(GameButton o) {
            int posObj = o.posX * 3 + o.posY;
            int posThis = this.posX * 3 + this.posY;
            return posThis - posObj;
        }
    }

    /**
     * Реализация игровой логики "Пятнашек".
     * Хранит состояние поля в виде массива строк и управляет перемещением костяшек.
     */
    private static class GameLogic{
        private String[] field = {"1", "2", "3", "4", "5", "6", "7", "*", "8"};

        private static final String[] winField = {"1", "2", "3", "4", "5", "6", "7", "8", "*"};

        private static final HashMap<Integer, HashSet<String>> enabledPositions = new HashMap<>();

        private int zeroIndex = 7;

        /**
         * Конструктор игровой логики.
         * Инициализирует правила допустимых ходов (какие индексы могут перемещаться на место пустоты).
         */
        public GameLogic(){
            enabledPositions.put(0, new HashSet<>(List.of("1", "3")));
            enabledPositions.put(1, new HashSet<>(List.of("0", "2", "4")));
            enabledPositions.put(2, new HashSet<>(List.of("1", "5")));
            enabledPositions.put(3, new HashSet<>(List.of("0", "4", "6")));
            enabledPositions.put(4, new HashSet<>(List.of("1", "3", "5", "7")));
            enabledPositions.put(5, new HashSet<>(List.of("8", "4", "2")));
            enabledPositions.put(6, new HashSet<>(List.of("3", "7")));
            enabledPositions.put(7, new HashSet<>(List.of("6", "4", "8")));
            enabledPositions.put(8, new HashSet<>(List.of("7", "5")));
        }

        /**
         * Выполняет ход указанной кнопкой, если он возможен.
         * Обновляет массив field и позицию пустоты.
         *
         * @param btn нажатая кнопка
         */
        public void move(GameButton btn){
            int btnPos = btn.getIndex();
            if (enabledPositions.get(zeroIndex).contains(btnPos + "")){
                btn.setPosition(zeroIndex / 3, zeroIndex % 3);
                field[zeroIndex] = btn.getVal();
                field[btnPos] = "*";
                zeroIndex = btnPos;
            }
        }

        /**
         * Перемешивает игровое поле для начала новой игры.
         */
        public void shuffleField(){
            zeroIndex = 0;

            Collections.shuffle(Arrays.asList(field));
            setZeroIndexByField();
        }

        /**
         * Проверяет, является ли текущее состояние поля выигрышным.
         *
         * @return true если поле соответствует выигрышной комбинации, иначе false
         */
        public boolean isWin(){
            return Arrays.equals(field, winField);
        }

        /**
         * Возвращает текущее состояние игрового поля.
         *
         * @return массив строк, представляющих поле
         */
        public String[] getField(){
            return field;
        }

        /**
         * Устанавливает новое состояние игрового поля.
         *
         * @param f массив строк для нового поля
         */
        public void setField(String[] f) {
            field = f;
        }

        /**
         * Обновляет индекс пустоты, находя позицию символа "*" в массиве field.
         */
        public void setZeroIndexByField(){
            zeroIndex = 0;
            while (!field[zeroIndex].equals("*")) zeroIndex++;
        }

        /**
         * Проверяет корректность массива поля.
         * Используется для защиты от загрузки некорректных файлов.
         *
         * @param testField массив для проверки
         * @return true если массив содержит правильный набор элементов, иначе false
         */
        public static boolean isCorrectField(String[] testField){
            String[] correctSortedField = {"*", "1", "2", "3", "4", "5", "6", "7", "8"};
            Arrays.sort(testField);
            return Arrays.equals(testField, correctSortedField);
        }
    }

    /**
     * Утилитный класс для работы с файлами.
     * Предоставляет методы сохранения и загрузки состояния игры.
     */
    private static class FileUtil{

        /**
         * Читает состояние поля из файла.
         * Файл должен содержать 9 строк с цифрами или символом "*".
         *
         * @param file файл для чтения
         * @return массив строк, представляющих поле, или текущее поле в случае ошибки
         * @throws FileNotFoundException если файл не найден
         */
        public static String[] getFieldFromFile(File file) throws FileNotFoundException {
            boolean result;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String [] newField = new String[9];

            try (reader){
                for (int i = 0; i < 9; i++){
                    newField[i] = reader.readLine();
                }
            } catch (IOException e){
                JOptionPane.showMessageDialog(mainFrame, "Некорректный файл", "Ошибка файла", JOptionPane.ERROR_MESSAGE);
            }
            result = GameLogic.isCorrectField(Arrays.copyOf(newField, newField.length));
            if (!result) JOptionPane.showMessageDialog(mainFrame, "Некорректный файл", "ошибка файла", JOptionPane.INFORMATION_MESSAGE);
            return result ? newField : gameLogic.getField();
        }

        /**
         * Сохраняет текущее состояние поля в файл.
         * Каждый элемент поля записывается в отдельной строке.
         *
         * @param file файл для сохранения
         * @throws IOException если возникла ошибка записи
         */
        public static void saveFieldToFile(File file) throws IOException {
            BufferedWriter writer = new BufferedWriter((new FileWriter(file)));
            try (writer) {
                for (String s : gameLogic.getField()) writer.write(s + "\n");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainFrame, "Ошибка записи в файл", "Ошибка записи", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    /**
     * Строка главного меню приложения.
     * Содержит меню "Файл" и "Игра" с соответствующими пунктами.
     */
    private static class MainMenuBar extends JMenuBar{

        /**
         * Конструктор строки меню.
         * Создает и настраивает все пункты меню с мнемониками и обработчиками событий.
         */
        public MainMenuBar(){
            JMenu mainMenu = new JMenu("Файл (F)");
            JMenu gameMenu = new JMenu("Игра (G)");

            JMenu newGameMenu = new JMenu("Новая (N)");

            JMenuItem exitItem = new JMenuItem("Выход (E)", 'E');
            JMenuItem aboutItem = new JMenuItem("О программе (A)", 'A');

            JMenuItem newGameItem = new JMenuItem("Случайная (R)", 'R');
            JMenuItem saveGameItem = new JMenuItem("Сохранить (S)", 'S');
            JMenuItem loadGameItem = new JMenuItem("Загрузить (L)", 'L');

            gameMenu.setMnemonic(KeyEvent.VK_G);
            mainMenu.setMnemonic(KeyEvent.VK_F);
            newGameMenu.setMnemonic(KeyEvent.VK_N);

            newGameItem.addActionListener((e) -> {
                gameLogic.shuffleField();
                gamePanel.rePaintButtons();
            });

            exitItem.addActionListener((e) -> System.exit(0));
            aboutItem.addActionListener((e) -> JOptionPane.showMessageDialog(mainFrame, "Игра Пятнашка на 9 клеток"));

            saveGameItem.addActionListener((e) -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Сохранить");
                Date date = new Date();
                chooser.setFileFilter(new FileNameExtensionFilter("game files - txt", "txt"));
                chooser.setSelectedFile(new File(String.format("game_%tY%tm%td_%tH%tM%tS.txt", date, date, date, date, date, date)));

                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION){
                    try {
                        FileUtil.saveFieldToFile(chooser.getSelectedFile());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, "Ошибка записи в файл", "Ошибка записи", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            loadGameItem.addActionListener((e) -> {
                String[] newField;
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setDialogTitle("Загрузить");
                chooser.setFileFilter(new FileNameExtensionFilter("game files", "txt"));
                if (chooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION){
                    try {
                        gameLogic.setField(FileUtil.getFieldFromFile(chooser.getSelectedFile()));
                        gamePanel.rePaintButtons();
                        gameLogic.setZeroIndexByField();
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(mainFrame, "Файл пропал!", "Ошибка файла", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });


            mainMenu.add(exitItem);
            mainMenu.add(aboutItem);

            newGameMenu.add(newGameItem);
            newGameMenu.add(loadGameItem);

            gameMenu.add(newGameMenu);
            gameMenu.addSeparator();
            gameMenu.add(saveGameItem);

            add(mainMenu);
            add(gameMenu);
        }
    }
}