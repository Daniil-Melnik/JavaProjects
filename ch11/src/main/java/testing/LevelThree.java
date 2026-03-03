/*
3. Продолжить освоение граничной компоновки на примере игры "пятнашка"
- сформировать фрейм, поместить в него панель 3*3
- заполнить панель 8-ю кнопками, одну клетку оставить свободной
- реализовать логику функционирования игры
- добавить строку меню с возможностью генерации новой обстановки, загрузки и сохранения в файл

= ЛОГИКА
- реализовать проверку возможности хода при нажатии на кнопку
- реализовать перемещение на массиве
- реализовать перерисовку по полю

*/

package testing;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class LevelThree {
    private static final int FRAME_H = 415; // константы размеров элементов интерфейса
    private static final int FRAME_W = 360;

    private static final int GAME_PAN_H = 360;
    private static final int GAME_PAN_W = 360;

    private static MainFrame mainFrame; // поле главного (единственного) окна
    private static GamePanel gamePanel; // поле игровой панели с, непосредственно, кнопками
    private static GameLogic gameLogic; // поле экземпляра реализации игровой логики

    public static void main(String... args){
        EventQueue.invokeLater(() -> {
            mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }



    private static class MainFrame extends JFrame{ // главное окно
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

            gameLogic = new GameLogic(); // создание экземпляров игровой панели и логики
            gamePanel = new GamePanel();
            add(gamePanel, BorderLayout.SOUTH);

            setJMenuBar(new MainMenuBar()); // добавление строки меню
        }

    }

    private static class GamePanel extends JPanel{ // класс описания интерфейса - игровой панели с кнопками
        public static Map<String, Color> colors = Map.of( // отображение "цифра - цвет" для индивидуальной
                "1", new Color(85,85,255),       // окраски костяшек
                "2", new Color(254,1,154),
                "3", new Color(57,255,20),
                "4", new Color(188,19,254),
                "5", new Color(4,217,255),
                "6", new Color(248,0,0),
                "7", new Color(204,255,0),
                "8", new Color(255,164,32)
        );

        public GamePanel(){
            setLayout(new GridLayout(3, 3)); // установка сеточной компоновки
            rePaintButtons(); // первичная отрисовка кнопок
        }

        public void rePaintButtons(){ // метод отрисовк игрового поля состоящего из 8-ми спец. кнопок
            int i = 0;                // и 1 пустого места

            removeAll(); // первичная очистка поля от всего (для повторной отрисовки)
            revalidate(); // обновление интерфейса
            repaint();

            GameButton btn;

            for (String s : gameLogic.getField()){ // перебор поля из экземпляра логики
                if (!s.equals("*")){               // все что не "*" - создание кнопки с установкой её положения
                    btn = new GameButton(i / 3, i % 3, s);
                    btn.setFont(new Font("Arial", Font.BOLD, 64));
                    btn.setBackground(colors.get(s)); // выбор цвета из отображения выше
                    add(btn);
                } else { // если "*" - добавить пустое место в виде панели
                    add(new JPanel());
                }

                i++; // непрерывный на пустоту счётчик для установки координат в кнопке
            }
            revalidate(); // пересчет интерфейса
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(GAME_PAN_H, GAME_PAN_W);
        }
    }

    private static class GameButton extends JButton implements Comparable<GameButton>{ // игровая кнопка
        private int posX; // хранит свою позицию в сетке
        private int posY;

        public GameButton(int x, int y, String val){ // конструирование по тексту
            super(val);
            posX = x;
            posY = y;
            addActionListener((e) -> { // при нажатии выполняется ход
                gameLogic.move(this); // ход по игровой логике
                gamePanel.rePaintButtons(); // перерисовка после хода
                if (gameLogic.isWin()){ // если состояние поля выигрышное - сообщение о выигрыше
                    JOptionPane.showMessageDialog(mainFrame, "Выигрыш!");
                }
            });
        }

        public void setPosition(int nX, int nY){ // установка новой позиции в сетке
            posY = nY;
            posX = nX;
        }

        public int getIndex(){ // получение номера в массиве поля
            return posX*3 + posY;
        }

        public String getVal(){
            return super.getText();
        } // получить тектовое значение - цифру

        @Override
        public int compareTo(GameButton o) {
            int posObj = o.posX * 3 + o.posY;
            int posThis = this.posX * 3 + this.posY;
            return posThis - posObj;
        }
    }

    // игровая логика
    // хранение поля в виде массива строк - цифр и * (пустоты)
    // ходы и отрисовка выполняются строго по состоянию массива

    private static class GameLogic{
        private String[] field = {"1", "2", "3", "4", "5", "6", "7", "*", "8"}; // массив - представление поля
                                                                                // поле инициализируется массивом по умолчанию

        private static final String[] winField = {"1", "2", "3", "4", "5", "6", "7", "8", "*"}; // выигрышное состояние поля

        private static final HashMap<Integer, HashSet<String>> enabledPositions = new HashMap<>(); // отображение правил ходов
                                                 // индекс пустого места - множество разрешенных соседних индексов
                                                 // для занятия пустоты. Индексы в массиве поля.

        private int zeroIndex = 7; // индекс в массиве занимаемый пустотой

        public GameLogic(){ // заполнение отображения правил движения кнопок на пустое место
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

        public void move(GameButton btn){ // выполнение хода по массиву поля
            int btnPos = btn.getIndex(); // вычисление индекса нажатой кнопки в массиве поля
            if (enabledPositions.get(zeroIndex).contains(btnPos + "")){ // если с нажатой кнопки можно пройти в пустоту
                btn.setPosition(zeroIndex / 3, zeroIndex % 3); // установка в кнопку:
                                                              // новая позиция кнопки - старая позиция пустоты
                field[zeroIndex] = btn.getVal(); // в массиве поля на место * - цифру кнопки
                field[btnPos] = "*"; // в массиве поля на место цифры кнопки - *
                zeroIndex = btnPos; // обновление индекса пустого места в массиве поля
            }
        }

        public void shuffleField(){ // перемешивание поля для случайной новой игры
            zeroIndex = 0;

            Collections.shuffle(Arrays.asList(field)); // перетасовка массива поля
            setZeroIndexByField(); // установка индекса пустоты по новому полю
        }

        public boolean isWin(){
            return Arrays.equals(field, winField);
        } // проверка на победу

        public String[] getField(){
            return field;
        }

        public void setField(String[] f) {
            field = f;
        }

        public void setZeroIndexByField(){ // поиск пустоты * в массиве поля
            zeroIndex = 0;
            while (!field[zeroIndex].equals("*")) zeroIndex++;
        }

        public static boolean isCorrectField(String[] testField){ // проверка корректности полученного в массиве поля
                                                                  // костыль - применяется для защиты от чтения не того файла
            String[] correctSortedField = {"*", "1", "2", "3", "4", "5", "6", "7", "8"};
            Arrays.sort(testField);
            return Arrays.equals(testField, correctSortedField);
        }
    }

    private static class FileUtil{ // методы для работы с файлами: сохранением и загрузкой состояния поля

        public static String[] getFieldFromFile(File file) throws FileNotFoundException { // прочитать поле из файла
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

        public static void saveFieldToFile(File file) throws IOException { // сохранить поле в файл
            BufferedWriter writer = new BufferedWriter((new FileWriter(file)));
            try (writer) {
                for (String s : gameLogic.getField()) writer.write(s + "\n");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainFrame, "Ошибка записи в файл", "Ошибка записи", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private static class MainMenuBar extends JMenuBar{ // строка главного меню

        public MainMenuBar(){
            JMenu mainMenu = new JMenu("Файл (F)");
            JMenu gameMenu = new JMenu("Игра (G)");

            JMenu newGameMenu = new JMenu("Новая (N)");

            JMenuItem exitItem = new JMenuItem("Выход (E)", 'E'); // конструирование пунктов меню
            JMenuItem aboutItem = new JMenuItem("О программе (A)", 'A');// с мнемониками

            JMenuItem newGameItem = new JMenuItem("Случайная (R)", 'R');
            JMenuItem saveGameItem = new JMenuItem("Сохранить (S)", 'S');
            JMenuItem loadGameItem = new JMenuItem("Загрузить (L)", 'L');

            gameMenu.setMnemonic(KeyEvent.VK_G); // присвоение мнемоник Alt + __ меню
            mainMenu.setMnemonic(KeyEvent.VK_F);
            newGameMenu.setMnemonic(KeyEvent.VK_N);

            newGameItem.addActionListener((e) -> { // новая случайная игра
                gameLogic.shuffleField(); // перемешать текущее поле
                gamePanel.rePaintButtons(); // перерисовать кнопки в соответсвии с полем
            });

            exitItem.addActionListener((e) -> System.exit(0));
            aboutItem.addActionListener((e) -> JOptionPane.showMessageDialog(mainFrame, "Игра Пятнашка на 9 клеток"));

            saveGameItem.addActionListener((e) -> { // сохранение игры
                JFileChooser chooser = new JFileChooser(); // применение диалога выбора файлов
                chooser.setDialogTitle("Сохранить");
                Date date = new Date(); // объект даты для формирования уникального имени файла сохранения
                chooser.setFileFilter(new FileNameExtensionFilter("game files - txt", "txt"));
                chooser.setSelectedFile(new File(String.format("game_%tY%tm%td_%tH%tM%tS.txt", date, date, date, date, date, date)));
                                    // имя по умолчанию

                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION){
                    try {
                        FileUtil.saveFieldToFile(chooser.getSelectedFile()); // печать состояния поля в файл
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainFrame, "Ошибка записи в файл", "Ошибка записи", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            loadGameItem.addActionListener((e) -> { // загрузка поля из файла
                String[] newField;
                JFileChooser chooser = new JFileChooser(); // применение диалога выбора фйалов
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setDialogTitle("Загрузить");
                chooser.setFileFilter(new FileNameExtensionFilter("game files", "txt")); // фильтр на текстовики
                if (chooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION){
                    try {
                        gameLogic.setField(FileUtil.getFieldFromFile(chooser.getSelectedFile())); // установка нового поля
                        gamePanel.rePaintButtons(); // перерисовка кнопок
                        gameLogic.setZeroIndexByField(); // обноление индекса пустоты
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
