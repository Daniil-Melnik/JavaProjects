package testing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Главный класс приложения "Калькулятор".
 * Содержит точку входа и статические ссылки на основные компоненты интерфейса.
 * Реализует логику отображения и управления калькулятором с поддержкой
 * различных систем счисления.
 */
public class Calculator {
    private static MainFrame mainFrame;
    private static BtnPanel btnPanel;
    private static CalcPanel calcPanel;
    private static ScreenComponent screenComponent;

    private static final int FRAME_W = 300;
    private static final int FRAME_H = 295;

    private static final int BTN_PANEL_W = 300;
    private static final int BTN_PANEL_H = 140;

    private static final int CALC_PANEL_W = 300;
    private static final int CALC_PANEL_H = 90;

    private static final String[] BTN_S = {"1", "2" , "3", "+", "4", "5", "6", "-",
            "7", "8", "9", "/" , "0", "C", "=", "*"};

    private static final String[] KEYSTROKES = {"NUMPAD1", "NUMPAD2", "NUMPAD3", "ADD", "NUMPAD4", "NUMPAD5",
            "NUMPAD6", "SUBTRACT", "NUMPAD7", "NUMPAD8", "NUMPAD9",
            "DIVIDE", "NUMPAD0", "BACK_SPACE", "ENTER", "MULTIPLY"
    };

    private static final String[] BTN_SG = {"=", "/" , "-", "*", "+"};

    private static final int SYMBOLS_PER_STR = 13;

    /**
     * Точка входа в приложение. Запускает главное окно в потоке обработки событий.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }

    /**
     * Внутренний класс, представляющий главное окно приложения.
     * Наследуется от {@link JFrame}. Содержит панель с кнопками, панель экрана и строку меню.
     */
    private static class MainFrame extends JFrame{
        /**
         * Конструктор главного окна. Устанавливает менеджер компоновки,
         * размеры, заголовок, иконку и добавляет дочерние компоненты.
         */
        public MainFrame(){
            setLayout(new BorderLayout());
            setSize(FRAME_W, FRAME_H);
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            setResizable(false);
            setTitle("Калькулятор");
            setIconImage(
                    new ImageIcon(
                            Objects.requireNonNull(this.getClass().getResource("/calculator.png"))
                    ).getImage());

            btnPanel = new BtnPanel();
            add(btnPanel, BorderLayout.SOUTH);

            calcPanel = new CalcPanel();
            add(calcPanel, BorderLayout.NORTH);
            setJMenuBar(new MainMenuBar());
        }
    }

    /**
     * Панель с кнопками калькулятора.
     * Организована в виде сетки 4x4. Поддерживает ввод как с помощью мыши,
     * так и с помощью клавиатуры.
     */
    private static class BtnPanel extends JPanel{
        private static JButton[] btns = new JButton[16];
        private static BtnAction[] actions = new BtnAction[16];

        /**
         * Конструктор панели. Создает кнопки, настраивает их шрифт,
         * устанавливает соответствие действий клавишам и применяет начальную логику.
         */
        public BtnPanel(){
            setLayout(new GridLayout(4, 4));
            int i = 0;

            for (String s : BTN_S) {
                actions[i] = new BtnAction(s);
                btns[i] = new JButton(actions[i]);
                btns[i].setFont(new Font("Courier New", Font.BOLD, 16));
                add(btns[i]);
                i++;
            }
            createKeyIAMap();
            setEnableEqual(false);
        }

        /**
         * Устанавливает доступность кнопки "=".
         *
         * @param b {@code true} - кнопка доступна, {@code false} - недоступна
         */
        public void setEnableEqual(boolean b){
            btns[14].setEnabled(b);
            actions[14].putValue("enabled", b);
        }

        /**
         * Блокирует или разблокирует все кнопки, кроме "C" и "=".
         *
         * @param b {@code true} - все кнопки доступны, {@code false} - заблокированы, кроме "C" и "="
         */
        public void setEnableBtns(boolean b){
            for (int i = 0; i < 16; i++){
                btns[i].setEnabled(b);
                actions[i].putValue("enabled", b);
            }
            btns[13].setEnabled(true);
            btns[14].setEnabled(true);

            actions[13].putValue("enabled", true);
            actions[14].putValue("enabled", true);
        }

        /**
         * Связывает нажатия клавиш на клавиатуре с действиями кнопок.
         * Использует {@link InputMap} и {@link ActionMap}.
         */
        private void createKeyIAMap(){
            ActionMap amap = this.getActionMap();
            InputMap imap = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);

            for (int i = 0; i < 16; i++){
                imap.put(KeyStroke.getKeyStroke(KEYSTROKES[i]), BTN_S[i]);
                amap.put(BTN_S[i], actions[i]);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(BTN_PANEL_W, BTN_PANEL_H);
        }
    }

    /**
     * Панель, отображающая введенное выражение и результат вычисления.
     * Содержит компонент {@link ScreenComponent} для рисования текста.
     */
    private static class CalcPanel extends JPanel{
        /**
         * Конструктор панели. Устанавливает белый фон и добавляет компонент экрана.
         */
        public CalcPanel(){
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            screenComponent = new ScreenComponent();
            add(screenComponent);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(CALC_PANEL_W, CALC_PANEL_H);
        }
    }

    /**
     * Действие, выполняемое при нажатии на кнопку калькулятора.
     * Хранит состояние доступности действия и обновляет строку выражения на экране.
     */
    private static class BtnAction extends AbstractAction{
        /**
         * Создает действие для кнопки.
         *
         * @param s текст на кнопке, используемый как имя действия
         */
        public BtnAction(String s){
            putValue(Action.NAME, s);
            putValue(Action.SHORT_DESCRIPTION, s);
            putValue("enabled", true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                if ((boolean) this.getValue("enabled"))
                    screenComponent.setStr(this.getValue(Action.NAME).toString());
            }catch (MaxExpLengthExeption ex){
                JOptionPane.showMessageDialog(mainFrame, ex.getMessage());
                btnPanel.setEnableBtns(false);
                screenComponent.setK(2);
            }

        }
    }

    /**
     * Компонент для отрисовки строки выражения и результата.
     * Управляет логикой формирования выражения и его вычисления.
     */
    private static class ScreenComponent extends JComponent{
        private String str = "";
        private String strRes = "";
        private boolean lastInSGwM;
        private int k = 1;
        private boolean eqOp = false;
        private int radix = 10;

        @Override
        protected void paintComponent(Graphics g) {
            g.setFont(new Font("Courier New", Font.BOLD, 32 / k));
            g.drawString(str, 0, 25);
            g.setFont(new Font("Courier New", Font.BOLD, 26));
            g.drawString(strRes, 5, 65);
        }

        /**
         * Добавляет символ в строку выражения после валидации.
         *
         * @param s символ для добавления
         * @throws MaxExpLengthExeption если превышена максимальная длина строки
         */
        public void setStr(String s) throws MaxExpLengthExeption {
            validInsert(s);
        }

        /**
         * Устанавливает делитель кегля шрифта для изменения размера текста.
         *
         * @param nk новый делитель кегля
         */
        public void setK(int nk){
            k = nk;
        }

        /**
         * Основная логика обработки ввода.
         * Проверяет корректность ввода, обновляет строку выражения,
         * выполняет вычисление при нажатии "=" и обрабатывает стирание "C".
         *
         * @param s введенный символ
         * @throws MaxExpLengthExeption при превышении лимита длины выражения
         */
        private void validInsert(String s) throws MaxExpLengthExeption{

            k = str.length() / SYMBOLS_PER_STR + 1;

            if (eqOp) {
                str = "";
                strRes = "";
                eqOp = false;
                btnPanel.setEnableEqual(false);
                repaint();
            }

            if ((s.equals("C")) && (!str.isEmpty())) {
                if (k == 3) k = 2;
                str = str.substring(0, str.length()-1);
                btnPanel.setEnableBtns(true);
                repaint();
            }

            else if (k == 3) {
                throw new MaxExpLengthExeption();
            }

            else if (s.equals("=") && !str.isEmpty()){
                try {
                    switch (radix){
                        case 2:
                            strRes = "=b" + Integer.toBinaryString(calculateExpr(getSplitExpression()));
                            break;
                        case 8:
                            strRes = "=o" + Integer.toOctalString(calculateExpr(getSplitExpression()));
                            break;
                        case 10:
                            strRes = "=" + calculateExpr(getSplitExpression());
                            break;
                        case 16:
                            strRes = "=h" + Integer.toHexString(calculateExpr(getSplitExpression()));
                            break;
                    }
                    btnPanel.setEnableBtns(true);
                    eqOp = true;
                    repaint();
                } catch (IndexOutOfBoundsException e){
                    JOptionPane.showMessageDialog(mainFrame, "Некорректное выражение", "Допишите", JOptionPane.ERROR_MESSAGE);
                }

            } else if ((!s.equals("C")) && (str.isEmpty()) && (!Arrays.asList(BTN_SG).contains(s))){
                str += s;
                lastInSGwM = Arrays.asList(BTN_SG).contains(s);
                repaint();
            }
            else if ((!s.equals("C")) &&((!str.isEmpty())) && !(lastInSGwM && Arrays.asList(BTN_SG).contains(s))){
                str += s;
                lastInSGwM = Arrays.asList(BTN_SG).contains(s);
                repaint();
            }

            if (str.length() == 0){
                btnPanel.setEnableEqual(false);
            } else btnPanel.setEnableEqual(true);
        }

        /**
         * Устанавливает систему счисления для отображения результата.
         *
         * @param r основание системы счисления (2, 8, 10, 16)
         */
        private void setRadix(int r){
            radix = r;
        }

        /**
         * Разбивает строку выражения на части по знакам операций.
         *
         * @return список строк, где каждая строка оканчивается знаком операции,
         *         кроме последней
         */
        private ArrayList<String> getSplitExpression(){
            String [] splittedStr = str.split("(?<=[-*/+])");
            return new ArrayList<>(List.of(splittedStr));
        }

        /**
         * Вычисляет значение арифметического выражения с учетом приоритета операций.
         * Поддерживает операции +, -, *, /.
         *
         * @param sA список частей выражения, полученный методом {@link #getSplitExpression()}
         * @return целочисленный результат вычисления
         */
        private int calculateExpr(ArrayList<String> sA){
            String current = "";
            String next = "";
            String nextOp = "";

            int currentI = 0;
            int nextI = 0;
            int res = 0;

            String regex = ".*[-+*/]";
            int l = sA.size();
            int i = 0;
            while (i < l){
                current = sA.get(i);
                char lastChar = current.charAt(current.length()-1);
                if (lastChar == '*' || lastChar == '/'){
                    next = sA.get(i+1);
                    currentI = Integer.parseInt(current.substring(0, current.length()-1));
                    nextI = Integer.parseInt(next.matches(regex) ? next.substring(0, next.length()-1) : next);
                    nextOp = (next.matches(regex) ? next.charAt(next.length()-1) + "" : "");
                    sA.remove(i+1);
                }
                switch (lastChar){
                    case '*':
                        res = currentI * nextI;
                        sA.set(i, res + nextOp);
                        l--;
                        i=0;
                        break;
                    case '/':
                        res = currentI / nextI;
                        sA.set(i, res + nextOp);
                        l--;
                        i=0;
                    default:
                        i++;
                }
            }

            i = 0;
            l = sA.size();
            while (i < l){
                current = sA.get(i);
                char lastChar = current.charAt(current.length()-1);
                if (lastChar == '-' || lastChar == '+'){
                    next = sA.get(i+1);
                    currentI = Integer.parseInt(current.substring(0, current.length()-1));
                    nextI = Integer.parseInt(next.matches(regex) ? next.substring(0, next.length()-1) : next);
                    nextOp = (next.matches(regex) ? next.charAt(next.length()-1) + "" : "");
                    sA.remove(i+1);
                }
                switch (current.charAt(current.length()-1)){
                    case '-':
                        res = currentI - nextI;
                        sA.set(i, res + nextOp);
                        l--;
                        i=0;
                        break;
                    case '+':
                        res = currentI + nextI;
                        sA.set(i, res + nextOp);
                        l--;
                        i=0;
                        break;
                    default:
                        i++;
                }
            }
            return Integer.parseInt(sA.get(0));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(CALC_PANEL_H, CALC_PANEL_W);
        }
    }

    /**
     * Исключение, выбрасываемое при превышении максимальной длины выражения.
     * Используется для контроля размера текста на экране.
     */
    private static class MaxExpLengthExeption extends RuntimeException{
        @Override
        public String getMessage() {
            return "Превышена максимальная длина выражения";
        }
    }

    /**
     * Строка меню приложения. Содержит меню "Файл" и "СисСчис".
     * В меню "Файл" доступны пункты "Закрыть" и "О программе".
     * В меню "СисСчис" можно выбрать систему счисления для результата.
     */
    private static class MainMenuBar extends JMenuBar{

        private final int[] items = {2, 8, 10, 16};

        /**
         * Конструктор строки меню. Создает и настраивает все пункты меню.
         */
        public MainMenuBar(){
            JMenuItem item = null;
            JRadioButtonMenuItem radioItem = null;
            ButtonGroup group = new ButtonGroup();

            JMenu fileMenu = new JMenu("Файл (F)");
            JMenu radixMenu = new JMenu("СисСчис (R)");

            fileMenu.setMnemonic(KeyEvent.VK_F);
            radixMenu.setMnemonic(KeyEvent.VK_R);

            JMenuItem exitItem = new JMenuItem("Закрыть (E)", 'E');
            JMenuItem aboutItem = new JMenuItem("О программе (A)", 'A');

            for (int i : items){

                radioItem = new JRadioButtonMenuItem(Integer.toString(i));
                group.add(radioItem);
                if (i == 10) radioItem.setSelected(true);

                radioItem.addActionListener((e) -> {
                    screenComponent.setRadix(i);
                });
                radixMenu.add(radioItem);
            }

            exitItem.addActionListener((e) -> {
                if (JOptionPane.showConfirmDialog(mainFrame, "Выйти?", "Выход", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                    System.exit(0);
            });

            aboutItem.addActionListener((e) -> {
                JOptionPane.showMessageDialog(mainFrame,
                        "Демонстрация применения граничной компоновки на примере калькулятора с на" +
                                "выполненни арифметических операций в очередном порядке",
                        "О программе", JOptionPane.INFORMATION_MESSAGE);
            });

            fileMenu.add(exitItem);
            fileMenu.add(aboutItem);

            add(fileMenu);
            add(radixMenu);
        }
    }
}