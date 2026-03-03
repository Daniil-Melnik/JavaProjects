package testing.LevelFour;

// класс с тремя типами кастомных диалоговых окон

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class DialogWindows {
    private static final Font font20 = new Font("Arial", Font.BOLD, 20); // основные шрифты для элементов интерфейса
    private static final Font font12 = new Font("Arial", Font.BOLD, 12);

    private static final int COMBO_DIALOG_W = 380; // размеры окна
    private static final int COMBO_DIALOG_H = 110;

    // 3 статических метода получения диалоговых окон

    public static TextAdder getTextAddedDialog(JFrame f, int i) throws IOException {
        return new TextAdder(f);
    }

    public static TextColorChooser getTextColorChooser(JFrame f, boolean t, Color currColor){
        return new TextColorChooser(f, t, currColor);
    }

    public static TextSizeChooser getTextSizeChooser(JFrame f, int i){
        return new TextSizeChooser(f, i);
    }

    public static class TextAdder extends JPanel { // диалоговое окно выбора гарнитуры
        private boolean ok = false; // переменная-результат вызова окна. флаг для вызова геттера получения выбора из класса.
                                    // Нужна для связи диалога и всего приложения. По умолчанию - ложь
        private JFrame owner = null; // владелец диалога, используется в конструировании JDialog
        private JDialog dialog = null; // окно-обертка JDialog
        private JComboBox<String> fontCombo = null; // комбобокс выбора имени гарнитуры

        public TextAdder(JFrame o) throws IOException {
            owner = o;

            fontCombo = new JComboBox<>(getFontsFromFile().toArray(new String[0])); // формирование комбобокса по именам
                                                    // шрифтов из файла (можно заменить на getAvailableFontFamilyNames()
                                                    // из GraphicsEnvironment())

            JPanel btnPanel = new JPanel(); // панель кнопок диалогового окна
            CustomBtn okBtn = new CustomBtn("ок", font12, (e) -> {ok = true; dialog.setVisible(false);});
                // создание кнопки ок, при её нажатии параметр "ок" переводится в true, что значит в основной программе
                // возможность получения результата выбора после вызова showDialog()
            CustomBtn cancelBtn = new CustomBtn("отмена", font12, (e) -> {dialog.setVisible(false);});
                // просто убрать диалог, в программу вернётся false
            setLayout(new BorderLayout());
            btnPanel.setLayout(new FlowLayout());

            fontCombo.setFont(font20);

            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);

            add(btnPanel, BorderLayout.SOUTH);
            add(fontCombo, BorderLayout.CENTER);
        }

        private LinkedList<String> getFontsFromFile() throws IOException { // получение списка шрифтов из файла
            String line = null;
            LinkedList<String> items = new LinkedList<>();
            try (InputStream inputStream = getClass().getResourceAsStream("/fonts.txt");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                // ресурс как поток из /recources/...

                while ((line = reader.readLine()) != null) {
                    items.add(line);
                }
            }

            return items;
        }

        public boolean showDialog(){ // метод для вызова окна из приложения, на возврат - флаг согласия
            if (dialog == null){
                dialog = new JDialog(owner, true); // JDialog - база всех диалоговых окно, подобна JFrame. Модальное
                dialog.add(this); // добавление в базу диалогового окна наполнения из панели
                dialog.setSize(new Dimension(COMBO_DIALOG_W, COMBO_DIALOG_H));
                dialog.setTitle("Выбор шрифта");
                dialog.setIconImage(
                        new ImageIcon(
                                Objects.requireNonNull(
                                        this.getClass().getResource("/fonts.png"))
                        ).getImage());
                dialog.setResizable(false);
            }
            dialog.setVisible(true);
            return ok; // возврат значения ок
            // окно модальное => программа останавливается на месте вызова и ждёт закрытия модального окна
        }

        public String getText(){
            return Objects.requireNonNull(fontCombo.getSelectedItem()).toString();
        } // метод для получения основной программой данных из диалогового окна
    }

    public static class TextSizeChooser extends JPanel{ // окно выбора размера шрифта
        private boolean ok = false;
        private JDialog dialog = null;
        private JFrame owner = null;
        private static ArrayList<Integer> items; // список доступных размеров
        private JComboBox<Integer> sizeCombo = null; // комбобокс размеров

        {
            items = new ArrayList<>(); // блок инициализации списка размеров
            for (int i = 1; i < 72; i++){
                items.add(i);
            }
        }

        public TextSizeChooser(JFrame o, int initIndex){
            owner = o;
            setLayout(new BorderLayout());
            sizeCombo = new JComboBox<>(items.toArray(new Integer[0]));
            sizeCombo.setSelectedIndex(initIndex);
            sizeCombo.setFont(font20);

            JPanel btnPanel = new JPanel();
            btnPanel.setLayout(new FlowLayout());

            JButton okBtn = new CustomBtn("ок", font12, (e) -> {ok = true; dialog.setVisible(false);});
            JButton cancelBtn = new CustomBtn("отмена", font12, (e) -> {dialog.setVisible(false);});

            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);

            add(sizeCombo, BorderLayout.CENTER);
            add(btnPanel, BorderLayout.SOUTH);
        }

        public int getSizeFromCombo(){
            return sizeCombo.getItemAt(sizeCombo.getSelectedIndex());
        } // возврат выбранного размера

        public boolean showDialog(){ // метод для вызова из основного приложения
            if (dialog == null){
                dialog = new JDialog(owner, true);
                dialog.add(this);
                dialog.setSize(COMBO_DIALOG_W, COMBO_DIALOG_H);
                dialog.setIconImage(new ImageIcon(
                        Objects.requireNonNull(
                                this.getClass()
                                        .getResource("/text-width.png"))
                ).getImage());

                dialog.setTitle("Размер текста");
                dialog.setVisible(true);
            }
            return ok; // возврат флага результата
        }
    }

    public static class TextColorChooser extends JPanel{ // окно выбора цвета
        private JDialog dialog = null;
        private JFrame owner = null;
        private boolean ok = false;
        private Color color = Color.BLACK;
        private boolean type = false;

        private final HashMap<String, JSlider> sliders = new HashMap<>(3); // ползунки
        private final LinkedHashMap<String, JLabel> valLabels = new LinkedHashMap<>(3);
                          // отображение из пометок ползунков, связное - для порядка
        private final HashMap<String, JLabel> colorNameLabels = new HashMap<>(3);
                          // отображение пометок-значений

        private static final int[][] colors = { // массив цветов для плитки
                {51,0,0},
                {51, 25, 0},
                {51, 51, 0},
                {0, 51, 0},
                {0, 51, 25},
                {0, 51, 51},
                {0, 0, 51},
                {25, 0, 51},
                {51, 0, 51},
                {0, 0, 0},
                {153, 0, 0},
                {153, 76, 0},
                {153, 153, 0},
                {0, 153, 0},
                {0, 153, 76},
                {0, 153, 153},
                {0, 0, 153},
                {76, 0, 153},
                {153, 0, 153},
                {64, 64, 64},
                {255, 0, 0},
                {255, 128, 0},
                {255, 255, 0},
                {0, 255, 0},
                {0, 255, 128},
                {0, 255, 255},
                {0, 0, 255},
                {127, 0, 255},
                {255, 0, 255},
                {128, 128, 128}
        };

        { // блок инициализации некоторых элементов интерфейса
            valLabels.put("r", new JLabel("100")); // по умолчаию цвет - 100, 100, 100
            valLabels.put("g", new JLabel("100"));
            valLabels.put("b", new JLabel("100"));

            colorNameLabels.put("r", new JLabel("Красный")); // лейблы цветов для ползунков в RGB
            colorNameLabels.put("g", new JLabel("Зелёный"));
            colorNameLabels.put("b", new JLabel("Синий"));

            sliders.put("r", new JSlider(0, 255, 100)); // создание ползунков от 0 до 255
            sliders.put("g", new JSlider(0, 255, 100)); // с установкой значений по умолчанию - 100
            sliders.put("b", new JSlider(0, 255, 100));
        }

        public TextColorChooser(JFrame o, boolean t, Color currColor){

            color = currColor;

            sliders.get("r").setValue(currColor.getRed()); // установка значений ползунков по текущему цвету при вызове
            sliders.get("g").setValue(currColor.getGreen()); // окна
            sliders.get("b").setValue(currColor.getBlue());

            valLabels.get("r").setText(currColor.getRed() + ""); // установка соотсетсвующих числовых индикаторов
            valLabels.get("g").setText(currColor.getGreen() + "");
            valLabels.get("b").setText(currColor.getBlue() + "");


            type = t; // тип - плитка/наборный

            setLayout(new GridBagLayout());
            JPanel colorProbe = new JPanel(); // показатель выбираемого цвета
            colorProbe.setBackground(currColor);

            owner = o;

            JPanel sliderPanel = new JPanel(); // панель с ползунками
            JPanel colorPalettePanel = new JPanel(); // панель с плиткой

            int i = 0;
            if (type) { // формирование панели с ползунками
                sliderPanel.setLayout(new GridBagLayout());
                for (String s : valLabels.keySet()) { // проходка по представлению ключей из подписей ползунков
                                                      // отображение подписаей - связное => ключи упорядочены при вводе
                                                      // R - G - B
                    colorNameLabels.get(s).setFont(font12);
                    valLabels.get(s).setFont(font12);
                    sliders.get(s).addChangeListener((e) -> { // ChangeListener - перехватчик ползунка
                        valLabels.get(s).setText(sliders.get(s).getValue() + "");
                        colorProbe.setBackground(this.getColor());
                        color = new Color(
                                sliders.get("r").getValue(),
                                sliders.get("g").getValue(),
                                sliders.get("b").getValue());
                    });

                    sliderPanel.add(colorNameLabels.get(s), new GBC(0, i, 1, 1)
                            .setWeight(0.2, 0.3)
                            .setAnchor(GBC.EAST)
                            .setInsets(0, 0, 0, 20));
                    sliderPanel.add(sliders.get(s), new GBC(1, i, 7, 1)
                            .setWeight(0.6, 0.3)
                            .setFill(GBC.HORIZONTAL));
                    sliderPanel.add(valLabels.get(s), new GBC(8, i, 2, 1)
                            .setWeight(0.2, 0.3));

                    i++;
                }
            }
            else { // фрмирование панели с плиткой
                colorPalettePanel.setLayout(new GridLayout(3, 10)); // сетка 3*10
                for (int[] c : colors) { // перебор цветов из массива в формате RGB
                    Color cl = new Color(c[0], c[1], c[2]);
                    JButton cBtn = new JButton();
                    cBtn.setBackground(cl);
                    cBtn.setToolTipText(Arrays.toString(c));
                    cBtn.addActionListener((e) -> {
                        colorProbe.setBackground(cl);
                        color = cl;
                    });
                    colorPalettePanel.add(cBtn);
                }
            }



            add(type ? sliderPanel : colorPalettePanel, new GBC(0, 0, 10, 3)
                    .setWeight(1, 0.8).setFill(GridBagConstraints.BOTH));
            add(colorProbe, new GBC(0, 3, 10, 1)
                    .setWeight(1, 0.1).setFill(GBC.BOTH));
        }

        public boolean showDialog(){
            if (dialog == null){
                dialog = new JDialog(owner, true);
                dialog.setLayout(new BorderLayout());
                dialog.add(this, BorderLayout.CENTER);

                CustomBtn okBtn = new CustomBtn("ок", font12, (e) -> {ok = true; dialog.setVisible(false);});
                CustomBtn cancelBtn = new CustomBtn("отмена", font12, (e) -> {dialog.setVisible(false);});

                JPanel btnPanel = new JPanel();
                btnPanel.add(okBtn);
                btnPanel.add(cancelBtn);
                dialog.add(btnPanel, BorderLayout.SOUTH);
                dialog.pack();
                dialog.setResizable(false);
                dialog.setTitle("Выбор цвета: " + (type ? "наборный" : "плитка"));
                dialog.setSize(400, 200);
                dialog.setIconImage(new ImageIcon(
                        Objects.requireNonNull(
                                this.getClass().getResource("/palette.png")
                        )
                ).getImage());
            }
            dialog.setVisible(true);
            return ok;
        }

        public Color getColor(){ // метод для получения выбранного цвета
            return color;
        }
    }

    private static class CustomBtn extends JButton{
        public CustomBtn(String name, Font font, ActionListener listener){
            setText(name);
            addActionListener(listener);
            setFont(font);
        }
    }
}
