package testing.TextDesigner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Класс-фабрика для создания пользовательских диалоговых окон.
 * Предоставляет три типа диалогов: выбор гарнитуры, выбор размера шрифта
 * и выбор цвета (в двух вариантах: наборный и плиточный).
 */
public class DialogWindows {
    private static final Font font20 = new Font("Arial", Font.BOLD, 20);
    private static final Font font12 = new Font("Arial", Font.BOLD, 12);

    private static final int COMBO_DIALOG_W = 380;
    private static final int COMBO_DIALOG_H = 110;

    /**
     * Создает диалоговое окно для выбора гарнитуры шрифта.
     *
     * @param f родительское окно
     * @param i неиспользуемый параметр (зарезервирован)
     * @return объект диалога {@link TextAdder}
     * @throws IOException если возникает ошибка при чтении файла со списком шрифтов
     */
    public static TextAdder getTextAddedDialog(JFrame f, int i) throws IOException {
        return new TextAdder(f);
    }

    /**
     * Создает диалоговое окно для выбора цвета.
     *
     * @param f родительское окно
     * @param t тип диалога: {@code true} - наборный (ползунки RGB), {@code false} - плитка
     * @param currColor текущий цвет для инициализации
     * @return объект диалога {@link TextColorChooser}
     */
    public static TextColorChooser getTextColorChooser(JFrame f, boolean t, Color currColor){
        return new TextColorChooser(f, t, currColor);
    }

    /**
     * Создает диалоговое окно для выбора размера шрифта.
     *
     * @param f родительское окно
     * @param i индекс начального выбора в комбобоксе
     * @return объект диалога {@link TextSizeChooser}
     */
    public static TextSizeChooser getTextSizeChooser(JFrame f, int i){
        return new TextSizeChooser(f, i);
    }

    /**
     * Диалоговое окно для выбора гарнитуры шрифта из выпадающего списка.
     * Список шрифтов загружается из файла /fonts.txt в ресурсах.
     */
    public static class TextAdder extends JPanel {
        private boolean ok = false;
        private JFrame owner = null;
        private JDialog dialog = null;
        private JComboBox<String> fontCombo = null;

        /**
         * Создает панель выбора гарнитуры.
         *
         * @param o родительское окно
         * @throws IOException если возникает ошибка при чтении файла со шрифтами
         */
        public TextAdder(JFrame o) throws IOException {
            owner = o;

            fontCombo = new JComboBox<>(getFontsFromFile().toArray(new String[0]));

            JPanel btnPanel = new JPanel();
            CustomBtn okBtn = new CustomBtn("ок", font12, (e) -> {ok = true; dialog.setVisible(false);});
            CustomBtn cancelBtn = new CustomBtn("отмена", font12, (e) -> {dialog.setVisible(false);});
            setLayout(new BorderLayout());
            btnPanel.setLayout(new FlowLayout());

            fontCombo.setFont(font20);

            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);

            add(btnPanel, BorderLayout.SOUTH);
            add(fontCombo, BorderLayout.CENTER);
        }

        /**
         * Загружает список доступных шрифтов из файла ресурсов.
         *
         * @return список названий шрифтов
         * @throws IOException если возникает ошибка чтения
         */
        private LinkedList<String> getFontsFromFile() throws IOException {
            String line = null;
            LinkedList<String> items = new LinkedList<>();
            try (InputStream inputStream = getClass().getResourceAsStream("/fonts.txt");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                while ((line = reader.readLine()) != null) {
                    items.add(line);
                }
            }

            return items;
        }

        /**
         * Отображает диалоговое окно и ожидает ответа пользователя.
         *
         * @return {@code true} если пользователь нажал "ок", иначе {@code false}
         */
        public boolean showDialog(){
            if (dialog == null){
                dialog = new JDialog(owner, true);
                dialog.add(this);
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
            return ok;
        }

        /**
         * Возвращает выбранную гарнитуру.
         *
         * @return название выбранного шрифта
         */
        public String getText(){
            return Objects.requireNonNull(fontCombo.getSelectedItem()).toString();
        }
    }

    /**
     * Диалоговое окно для выбора размера шрифта.
     * Предлагает размеры от 1 до 72 в выпадающем списке.
     */
    public static class TextSizeChooser extends JPanel{
        private boolean ok = false;
        private JDialog dialog = null;
        private JFrame owner = null;
        private static ArrayList<Integer> items;
        private JComboBox<Integer> sizeCombo = null;

        {
            items = new ArrayList<>();
            for (int i = 1; i < 72; i++){
                items.add(i);
            }
        }

        /**
         * Создает панель выбора размера.
         *
         * @param o родительское окно
         * @param initIndex индекс для предварительного выбора
         */
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

        /**
         * Возвращает выбранный размер шрифта.
         *
         * @return размер шрифта в пунктах
         */
        public int getSizeFromCombo(){
            return sizeCombo.getItemAt(sizeCombo.getSelectedIndex());
        }

        /**
         * Отображает диалоговое окно и ожидает ответа пользователя.
         *
         * @return {@code true} если пользователь нажал "ок", иначе {@code false}
         */
        public boolean showDialog(){
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
            return ok;
        }
    }

    /**
     * Диалоговое окно для выбора цвета.
     * Поддерживает два режима: наборный (ползунки RGB) и плиточный (выбор из палитры).
     */
    public static class TextColorChooser extends JPanel{
        private JDialog dialog = null;
        private JFrame owner = null;
        private boolean ok = false;
        private Color color = Color.BLACK;
        private boolean type = false;

        private final HashMap<String, JSlider> sliders = new HashMap<>(3);
        private final LinkedHashMap<String, JLabel> valLabels = new LinkedHashMap<>(3);
        private final HashMap<String, JLabel> colorNameLabels = new HashMap<>(3);

        private static final int[][] colors = {
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

        {
            valLabels.put("r", new JLabel("100"));
            valLabels.put("g", new JLabel("100"));
            valLabels.put("b", new JLabel("100"));

            colorNameLabels.put("r", new JLabel("Красный"));
            colorNameLabels.put("g", new JLabel("Зелёный"));
            colorNameLabels.put("b", new JLabel("Синий"));

            sliders.put("r", new JSlider(0, 255, 100));
            sliders.put("g", new JSlider(0, 255, 100));
            sliders.put("b", new JSlider(0, 255, 100));
        }

        /**
         * Создает панель выбора цвета.
         *
         * @param o родительское окно
         * @param t тип диалога: {@code true} - наборный, {@code false} - плитка
         * @param currColor текущий цвет для инициализации
         */
        public TextColorChooser(JFrame o, boolean t, Color currColor){

            color = currColor;

            sliders.get("r").setValue(currColor.getRed());
            sliders.get("g").setValue(currColor.getGreen());
            sliders.get("b").setValue(currColor.getBlue());

            valLabels.get("r").setText(currColor.getRed() + "");
            valLabels.get("g").setText(currColor.getGreen() + "");
            valLabels.get("b").setText(currColor.getBlue() + "");


            type = t;

            setLayout(new GridBagLayout());
            JPanel colorProbe = new JPanel();
            colorProbe.setBackground(currColor);

            owner = o;

            JPanel sliderPanel = new JPanel();
            JPanel colorPalettePanel = new JPanel();

            int i = 0;
            if (type) {
                sliderPanel.setLayout(new GridBagLayout());
                for (String s : valLabels.keySet()) {
                    colorNameLabels.get(s).setFont(font12);
                    valLabels.get(s).setFont(font12);
                    sliders.get(s).addChangeListener((e) -> {
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
            else {
                colorPalettePanel.setLayout(new GridLayout(3, 10));
                for (int[] c : colors) {
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

        /**
         * Отображает диалоговое окно и ожидает ответа пользователя.
         *
         * @return {@code true} если пользователь нажал "ок", иначе {@code false}
         */
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

        /**
         * Возвращает выбранный цвет.
         *
         * @return объект {@link Color}
         */
        public Color getColor(){
            return color;
        }
    }

    /**
     * Вспомогательный класс для создания стандартизированных кнопок.
     * Упрощает создание кнопок с заданным текстом, шрифтом и обработчиком.
     */
    private static class CustomBtn extends JButton{
        /**
         * Создает кнопку с указанными параметрами.
         *
         * @param name текст на кнопке
         * @param font шрифт кнопки
         * @param listener обработчик нажатия
         */
        public CustomBtn(String name, Font font, ActionListener listener){
            setText(name);
            addActionListener(listener);
            setFont(font);
        }
    }
}