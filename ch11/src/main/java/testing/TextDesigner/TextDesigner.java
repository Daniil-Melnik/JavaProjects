package testing.TextDesigner;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

/**
 * Главный класс приложения "Дизайнер текста".
 * Позволяет настраивать параметры отображения текста: гарнитуру, размер,
 * начертание, цвет, а также отслеживать метрики строки.
 */
public class TextDesigner {
    private static MainFrame mainFrame;
    private static TextComponent textComponent;
    private static BottomPanel bottomPanel;
    private static TextInfoPanel textInfoPanel;

    private static final int FRAME_W = 800;
    private static final int FRAME_H = 400;

    private static final int TEXT_PANEL_W = 800;
    private static final int TEXT_PANEL_H = 250;

    private static final int MAX_TEXT_W = 750;

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
     * Содержит панель с текстом, нижнюю панель с элементами управления и строку меню.
     */
    private static class MainFrame extends JFrame{
        /**
         * Конструктор главного окна.
         * Устанавливает размеры, заголовок, иконку, менеджер компоновки
         * и добавляет все необходимые панели.
         */
        public MainFrame(){
            setSize(FRAME_W, FRAME_H);
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            setResizable(false);
            setTitle("Просмотр текста");
            setLayout(new BorderLayout());
            setIconImage(
                    new ImageIcon(
                            Objects.requireNonNull(this.getClass().getResource("/text.png"))
                    ).getImage());

            TextPanel textPanel = new TextPanel();
            bottomPanel = new BottomPanel();

            add(textPanel, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);

            setJMenuBar(new MainMenuBar());
        }
    }

    /**
     * Панель-контейнер для текстового компонента.
     * Обеспечивает размещение текста в центре панели.
     */
    private static class TextPanel extends JPanel{
        /**
         * Конструктор панели. Устанавливает белый фон и добавляет текстовый компонент.
         */
        public TextPanel(){
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            textComponent = new TextComponent();

            add(textComponent, BorderLayout.CENTER);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(TEXT_PANEL_W, TEXT_PANEL_H);
        }
    }

    /**
     * Компонент для отображения текста с настраиваемыми параметрами.
     * Хранит информацию о тексте, его шрифте, размере, начертании и цвете.
     * Отрисовывает текст по центру панели.
     */
    private static class TextComponent extends JComponent{
        private String text = "Hello, it's text component";
        private String fontName = "Arial";
        private int fontSize = 26;
        private int fontOutline = Font.BOLD;
        private Font font;
        private Color color = new Color(100, 100, 100);

        /**
         * Конструктор текстового компонента.
         * Инициализирует шрифт с параметрами по умолчанию.
         */
        public TextComponent(){
            font = new Font(fontName, fontOutline, fontSize);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setFont(font);
            g.setColor(color);

            FontMetrics fm = getFontMetrics(font);
            int strWidth = fm.stringWidth(text);
            int strHeight = fm.getHeight();

            g.drawString(text, (TEXT_PANEL_W - strWidth) / 2,(TEXT_PANEL_H - strHeight) / 2);
        }

        /**
         * Возвращает метрики шрифта для текущего текста.
         *
         * @return объект {@link FontMetrics} для текущего шрифта
         */
        public FontMetrics getFM(){
            return getFontMetrics(font);
        }

        /**
         * Устанавливает новый шрифт с текущими параметрами.
         * Проверяет, помещается ли текст на экран при новом шрифте.
         */
        public void setFont() {
            Font newFont = new Font(fontName, fontOutline, fontSize);
            if (getFontMetrics(newFont).stringWidth(text) <= MAX_TEXT_W){
                font = newFont;
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Слишком длинная строка, уменьшите пожалуйста её длину", "Длинная строка", JOptionPane.ERROR_MESSAGE);
            }
        }

        /**
         * Устанавливает имя гарнитуры шрифта.
         *
         * @param fN имя гарнитуры
         */
        public void setFontName(String fN) {
            fontName = fN;
        }

        /**
         * Устанавливает начертание шрифта.
         * Допустимые значения: {@link Font#PLAIN}, {@link Font#BOLD}, {@link Font#ITALIC}.
         *
         * @param fO начертание шрифта
         */
        public void setFontOutline(int fO) {
            fontOutline = (fO >= Font.PLAIN && fO <=Font.ITALIC) ? fO : Font.PLAIN;
        }

        /**
         * Устанавливает размер шрифта.
         *
         * @param fS размер шрифта в пунктах
         */
        public void setFontSize(int fS) {
            fontSize = fS;
        }

        /**
         * Устанавливает текст для отображения.
         * Проверяет, помещается ли текст на панель, и при необходимости
         * ограничивает длину вводимой строки.
         *
         * @param t новый текст
         * @throws BadLocationException если возникает ошибка при работе с документом
         */
        public void setText(String t) throws BadLocationException {
            FontMetrics fm = getFontMetrics(font);
            if (fm.stringWidth(t) > MAX_TEXT_W){
                bottomPanel.getInputTextField().setDocument(new LimitDocument(t.length() - 1));
                bottomPanel.getInputTextField().setText(text);
            }
            else{
                text = t;
                ((LimitDocument) bottomPanel.getInputTextField().getDocument()).updateDocument(text.length() + 1);
            }
        }

        /**
         * Устанавливает цвет текста.
         *
         * @param c новый цвет
         */
        public void setColor(Color c) {
            color = c;
        }

        /**
         * Возвращает текущий цвет текста.
         *
         * @return текущий цвет
         */
        public Color getColor(){
            return color;
        }

        /**
         * Возвращает информацию о текущих настройках шрифта.
         *
         * @return связанный HashMap с парами "параметр-значение"
         */
        public LinkedHashMap<String, String> getFontInfo(){
            LinkedHashMap<String, String> info = new LinkedHashMap<>(4);
            info.put("fontName", fontName);
            info.put("fontOutline", fontOutline == Font.BOLD ? "Bold" : fontOutline == Font.PLAIN ? "Plain" : fontOutline == Font.ITALIC ? "Italic" : "ERROR");
            info.put("fontSize", Integer.valueOf(fontSize).toString());
            info.put("fontColor", String.format("%d, %d, %d", color.getRed(), color.getGreen(), color.getBlue()));
            return info;
        }

        /**
         * Возвращает метрики текущей строки текста.
         *
         * @return связанный HashMap с парами "метрика-значение"
         */
        public LinkedHashMap<String, String> getStringMetrics(){
            LinkedHashMap<String, String> metrics = new LinkedHashMap<>(2);
            metrics.put("strWidth", Integer.toString(getFM().stringWidth(text)));
            metrics.put("strHeight", Integer.toString(getFM().getHeight()));
            return metrics;
        }
    }

    /**
     * Нижняя панель приложения.
     * Содержит поле ввода текста и панель с метриками.
     */
    private static class BottomPanel extends JPanel{
        private JTextField inputTextField;
        private LimitDocument limitDocument;

        /**
         * Конструктор нижней панели.
         * Создает поле ввода с ограничением длины и панель метрик.
         */
        public BottomPanel(){
            limitDocument = new LimitDocument(100);
            setBorder(new EtchedBorder());
            setLayout(new GridBagLayout());

            inputTextField = new JTextField();
            inputTextField.setFont(new Font("Arial", Font.PLAIN, 20));
            inputTextField.setDocument(limitDocument);

            JLabel textLabel = new JLabel("Строка:");
            textLabel.setFont(new Font("Arial", Font.PLAIN, 20));

            add(textLabel, new GBC(0, 0, 2, 1).setWeight(0, 0.1).setAnchor(GBC.EAST).setInsets(0, 0, 10, 0));
            add(inputTextField, new GBC(2, 0, 14, 1).setWeight(1, 0.1).setFill(1).setInsets(10));

            textInfoPanel = new TextInfoPanel();

            add(textInfoPanel, new GBC(0, 1, 16, 2).setWeight(1, 1).setFill(1));
        }

        /**
         * Возвращает поле ввода текста.
         *
         * @return текстовое поле
         */
        public JTextField getInputTextField(){
            return inputTextField;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(TEXT_PANEL_W, FRAME_H - TEXT_PANEL_H);
        }
    }

    /**
     * Панель отображения метрик текста и шрифта.
     * Показывает информацию о гарнитуре, начертании, размере, цвете,
     * а также ширину и высоту строки в пикселях.
     */
    private static class TextInfoPanel extends JPanel{
        private static final Font font12 = new Font("Arial", Font.BOLD, 12);

        LinkedHashMap<String, JLabel> valuesMap = new LinkedHashMap<>(4);
        LinkedHashMap<String, JLabel> metricsMap = new LinkedHashMap<>(2);
        Map<String, JLabel> labelsMap;
        Map<String, JLabel> labelsMetrMap;

        {
            labelsMap = Map.of(
                    "fontName", new JLabel("Название:"),
                    "fontOutline", new JLabel("Начертание:"),
                    "fontSize", new JLabel("Размер"),
                    "fontColor", new JLabel("Цвет:")
            );

            labelsMetrMap = Map.of(
                    "strWidth", new JLabel("Ширина (пкс):"),
                    "strHeight", new JLabel("Высота (пкс):")
            );
        }

        /**
         * Конструктор панели метрик.
         * Создает две подпанели: информация о шрифте и метрики строки.
         */
        public TextInfoPanel(){
            setLayout(new GridLayout(1, 3));

            JPanel fontInfoPanel = new JPanel();
            JPanel fontMetricsPanel = new JPanel();

            fontInfoPanel.setLayout(new GridBagLayout());
            fontMetricsPanel.setLayout(new GridBagLayout());

            for (Map.Entry<String, String> e : textComponent.getFontInfo().entrySet()){
                valuesMap.put(e.getKey(), new JLabel(e.getValue()));
            }

            for (Map.Entry<String, String> e : textComponent.getStringMetrics().entrySet()){
                metricsMap.put(e.getKey(), new JLabel(e.getValue()));
            }

            int i = 0;
            for (String s : labelsMap.keySet()){
                labelsMap.get(s).setFont(font12);
                valuesMap.get(s).setFont(font12);
                fontInfoPanel.add(labelsMap.get(s),
                        new GBC(0, i, 1, 1)
                                .setWeight(0, 0.25)
                                .setInsets(0, 0, 10, 0)
                                .setAnchor(GBC.EAST));
                fontInfoPanel.add(valuesMap.get(s),
                        new GBC(1, i, 1, 1)
                                .setWeight(1, 0.25)
                                .setInsets(0,0,0, 5)
                                .setAnchor(GBC.EAST));
                i++;
            }

            i = 0;

            for (String s : metricsMap.keySet()){
                fontMetricsPanel.add(labelsMetrMap.get(s),
                        new GBC(0, i, 1, 1)
                                .setWeight(0, 0.25)
                                .setAnchor(GBC.EAST)
                                .setInsets(0,0,10,0));
                fontMetricsPanel.add(metricsMap.get(s),
                        new GBC(1, i, 1, 1)
                                .setWeight(1, 0.25)
                                .setAnchor(GBC.EAST)
                                .setInsets(0,0,0,10));
                i++;
            }

            fontMetricsPanel.setBorder(new EtchedBorder());
            fontInfoPanel.setBorder(new EtchedBorder());

            add(fontInfoPanel);
            add(new JPanel());
            add(fontMetricsPanel);
        }

        /**
         * Обновляет значения метрик на панели.
         * Вызывается при изменении текста или параметров шрифта.
         */
        public void updatePanel(){
            for (Map.Entry<String, String> e : textComponent.getFontInfo().entrySet()){
                valuesMap.get(e.getKey()).setText(e.getValue());
            }

            for (Map.Entry<String, String> e : textComponent.getStringMetrics().entrySet()){
                metricsMap.get(e.getKey()).setText(e.getValue());
            }
            repaint();
        }
    }

    /**
     * Строка главного меню приложения.
     * Содержит меню "Файл", "Шрифт" и "Цвет" с соответствующими пунктами.
     */
    private static class MainMenuBar extends JMenuBar{
        private static final boolean PALETTE = false;
        private static final boolean ADDITIVE = true;

        /**
         * Конструктор строки меню.
         * Создает все пункты меню с мнемониками и обработчиками событий.
         */
        public MainMenuBar(){
            JMenu fileMenu = new JMenu("Файл (F)");
            JMenu fontMenu = new JMenu("Шрифт (S)");
            JMenu colorMenu = new JMenu("Цвет (C)");

            JMenu outlineMenu = new JMenu("Начертание (O)");

            outlineMenu.setMnemonic(KeyEvent.VK_O);
            fileMenu.setMnemonic(KeyEvent.VK_F);
            fontMenu.setMnemonic(KeyEvent.VK_S);
            colorMenu.setMnemonic(KeyEvent.VK_C);

            JMenuItem exitItem = new JMenuItem("Выход (E)", 'E');
            JMenuItem aboutItem = new JMenuItem("О программе (A)", 'A');

            exitItem.addActionListener((e) -> {System.exit(0);});

            aboutItem.addActionListener((e) -> {
                JOptionPane.showMessageDialog(mainFrame,
                        "Праграмма подбора дизайна строки по гарнитуре, размеру, начертанию и цвету.",
                        "О программе",
                        JOptionPane.INFORMATION_MESSAGE);
            });

            fileMenu.add(exitItem);
            fileMenu.add(aboutItem);

            JMenuItem shriftItem = new JMenuItem("Гарнитура (G)", 'G');

            ButtonGroup radioGroup = new ButtonGroup();
            JRadioButtonMenuItem outlineItemPlain = new JRadioButtonMenuItem(new OutlineAction("Plain"));
            JRadioButtonMenuItem outlineItemBold = new JRadioButtonMenuItem(new OutlineAction("Bold"));
            JRadioButtonMenuItem outlineItemItalic = new JRadioButtonMenuItem(new OutlineAction("Italic"));
            radioGroup.add(outlineItemPlain);
            radioGroup.add(outlineItemBold);
            radioGroup.add(outlineItemItalic);

            outlineMenu.add(outlineItemPlain);
            outlineMenu.add(outlineItemBold);
            outlineMenu.add(outlineItemItalic);

            shriftItem.addActionListener((e) -> {
                try {
                    DialogWindows.TextAdder tA = DialogWindows.getTextAddedDialog(mainFrame, 0);
                    if (tA.showDialog()){
                        textComponent.setFontName(tA.getText());
                        textComponent.setFont();
                        textComponent.repaint();
                        textInfoPanel.updatePanel();
                    }
                } catch (IOException ex) {System.out.println(ex.getMessage());}

            });

            JMenuItem colorAddsItem = new JMenuItem("Наборный (N)", 'N');
            JMenuItem colorPaletteItem = new JMenuItem("Плитка (P)", 'P');

            colorAddsItem.addActionListener((e) -> {
                DialogWindows.TextColorChooser tC = DialogWindows.getTextColorChooser(mainFrame, ADDITIVE, textComponent.getColor());
                if (tC.showDialog()){
                    textComponent.setColor(tC.getColor());
                    textComponent.repaint();
                    textInfoPanel.updatePanel();
                }
            });

            colorPaletteItem.addActionListener((e) -> {
                DialogWindows.TextColorChooser tC = DialogWindows.getTextColorChooser(mainFrame, PALETTE, textComponent.getColor());
                if (tC.showDialog()){
                    textComponent.setColor(tC.getColor());
                    textComponent.repaint();
                    textInfoPanel.updatePanel();
                }
            });

            JMenuItem textSizeItem = new JMenuItem("Размер (S)", 'S');
            textSizeItem.addActionListener((e) -> {
                DialogWindows.TextSizeChooser tSC = DialogWindows.getTextSizeChooser(mainFrame, Integer.parseInt(textComponent.getFontInfo().get("fontSize")) - 1);
                if (tSC.showDialog()){
                    textComponent.setFontSize(tSC.getSizeFromCombo());
                    textComponent.setFont();
                    textComponent.repaint();
                    textInfoPanel.updatePanel();
                }
            });


            colorMenu.add(colorAddsItem);
            colorMenu.add(colorPaletteItem);

            fontMenu.add(shriftItem);
            fontMenu.add(outlineMenu);
            fontMenu.add(textSizeItem);

            add(fileMenu);
            add(fontMenu);
            add(colorMenu);
        }
    }

    /**
     * Действие для изменения начертания шрифта.
     * Реализует выбор между Plain, Bold и Italic.
     */
    private static class OutlineAction extends AbstractAction{
        HashMap<String, Integer> outlineMap = new HashMap<>(3);

        /**
         * Создает действие с указанным именем начертания.
         *
         * @param name имя начертания ("Plain", "Bold", "Italic")
         */
        public OutlineAction(String name){
            putValue(Action.NAME, name);
            outlineMap.put("Plain", Font.PLAIN);
            outlineMap.put("Bold", Font.BOLD);
            outlineMap.put("Italic", Font.ITALIC);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = this.getValue(Action.NAME).toString();
            int outline = outlineMap.get(name);
            textComponent.setFontOutline(outline);
            textComponent.setFont();
            textComponent.repaint();
            textInfoPanel.updatePanel();
        }
    }

    /**
     * Модифицированный документ с ограничением длины.
     * Реализует модель для текстового поля с контролем максимального
     * количества символов и автоматическим обновлением текстового компонента.
     */
    private static class LimitDocument extends PlainDocument{
        private int limit;

        /**
         * Создает документ с указанным ограничением длины.
         *
         * @param l максимальное количество символов
         */
        public LimitDocument(int l){
            super();
            limit = l;
            addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        textComponent.setText(getText(0, getLength()));
                        textInfoPanel.updatePanel();
                        textComponent.repaint();
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    try {
                        textComponent.setText(getText(0, getLength()));
                        textInfoPanel.updatePanel();
                        textComponent.repaint();
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                }
            });
        }

        /**
         * Обновляет ограничение длины документа.
         *
         * @param l новое ограничение длины
         */
        public void updateDocument(int l){
            limit = l;
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) return;

            if ((getLength() + str.length() <= limit)){
                super.insertString(offs, str, a);
            }
        }
    }
}