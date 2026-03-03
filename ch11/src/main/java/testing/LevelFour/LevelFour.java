/*
4. Освоить примененение компонентов для выбора разных вариантов
- сформировать фрейм, поместить в него компонент с текстом
- добавить в фрейм панели с выбором: шрифта, начертания, размера
  -- шрифт - компобокс
  -- размер - комбобокс
  -- начертание - радио-кнопки
  -- цвет - меню радио кнопок - наборный через комбо
                              - из констант через компбо
  -- изменение текста через поля для ввода
- все панели изменений провести через строку меню
*/

package testing.LevelFour;

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

public class LevelFour {
    private static MainFrame mainFrame; // главное окно
    private static TextComponent textComponent; // копонент с тестовой строкой
    private static BottomPanel bottomPanel; // нижняя панель (строка ввода, панель с метриками)
    private static TextInfoPanel textInfoPanel; // панель с метриками

    private static final int FRAME_W = 800; // константы размеров окна и панелей
    private static final int FRAME_H = 400;

    private static final int TEXT_PANEL_W = 800;
    private static final int TEXT_PANEL_H = 250;

    private static final int MAX_TEXT_W = 750;

    public static void main(String... args){
        EventQueue.invokeLater(() -> {
            mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }

    private static class MainFrame extends JFrame{
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

            TextPanel textPanel = new TextPanel(); // инициализация панели под текстовых компонент
            bottomPanel = new BottomPanel(); // инициализация панели под строку поиска и панель с метриками

            add(textPanel, BorderLayout.CENTER); // именно CENTER (а не NORTH) - занимает только централиьную область
                                                 // и не нарушает панель SOUTH
            add(bottomPanel, BorderLayout.SOUTH);

            setJMenuBar(new MainMenuBar()); // установка панели пеню
        }
    }

    private static class TextPanel extends JPanel{ // панель под текстовый компонент
        public TextPanel(){
            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            textComponent = new TextComponent(); // инициализация текстового компонента

            add(textComponent, BorderLayout.CENTER);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(TEXT_PANEL_W, TEXT_PANEL_H);
        }
    }

    private static class TextComponent extends JComponent{ // текстовый компонент,  содержит информацияю о
        private String text = "Hello, it's text component"; // тексте, который отображает компонент
        private String fontName = "Arial"; // имени гарнитуры надписи
        private int fontSize = 26; // размер шрифта текста
        private int fontOutline = Font.BOLD; // начертание шрифта
        private Font font; // инкапсулированное поле шрифта
        private Color color = new Color(100, 100, 100); // цвет написания ятекста

        public TextComponent(){
            font = new Font(fontName, fontOutline, fontSize);
        }

        @Override
        protected void paintComponent(Graphics g) { // отрисовка компонента
            g.setFont(font);
            g.setColor(color);

            FontMetrics fm = getFontMetrics(font); // объект для расчета размеров строки текста в заданном шрифте
                                                   // расчет координа ЛВ-угла чтобы строка была примерно по центру
            int strWidth = fm.stringWidth(text); // ширина строки
            int strHeight = fm.getHeight();  // высота строки

            g.drawString(text, (TEXT_PANEL_W - strWidth) / 2,(TEXT_PANEL_H - strHeight) / 2);
        }

        public FontMetrics getFM(){
            return getFontMetrics(font);
        }  // быстрое получение экземпляра FontMetrics

        public void setFont() { // установка нового шрифта (при изменении через интерфейс параметров)
            Font newFont = new Font(fontName, fontOutline, fontSize);
            if (getFontMetrics(newFont).stringWidth(text) <= MAX_TEXT_W){ // проверка: помещается ли строка на экран
                font = newFont;
            } else { // если слишком длинная - не обновлять шрифт
                JOptionPane.showMessageDialog(mainFrame, "Слишком длинная строка, уменьшите пожалуйста её длину", "Длинная строка", JOptionPane.ERROR_MESSAGE);
            }
        }

        public void setFontName(String fN) {
            fontName = fN;
        }

        public void setFontOutline(int fO) {
            fontOutline = (fO >= Font.PLAIN && fO <=Font.ITALIC) ? fO : Font.PLAIN; // проверка на принадлежность [PLAIN; ITALIC]
        }

        public void setFontSize(int fS) {
            fontSize = fS;
        }

        public void setText(String t) throws BadLocationException { // установка хранимого текста
            FontMetrics fm = getFontMetrics(font);
            // проверка: помещается ли строка на панель
            // если нет - остановка возможности ввода новых символов: установка нового модифицированного Document
            // с ограничением длины вводимой строки и установкой введённого текста
            // получится только удалить ссимвол перед вводом нового
            if (fm.stringWidth(t) > MAX_TEXT_W){
                bottomPanel.getInputTextField().setDocument(new LimitDocument(t.length() - 1));
                bottomPanel.getInputTextField().setText(text);
            }
            else{ // если текст помещается: обновляется ограничение доины в Document
                text = t;
                ((LimitDocument) bottomPanel.getInputTextField().getDocument()).updateDocument(text.length() + 1);
            }
        }

        public void setColor(Color c) {
            color = c;
        }

        public Color getColor(){
            return color;
        }

        public LinkedHashMap<String, String> getFontInfo(){ // метод для формирования информации о настройках шрифта (для
                                                            // панели метрик)
            LinkedHashMap<String, String> info = new LinkedHashMap<>(4);
            info.put("fontName", fontName);
            info.put("fontOutline", fontOutline == Font.BOLD ? "Bold" : fontOutline == Font.PLAIN ? "Plain" : fontOutline == Font.ITALIC ? "Italic" : "ERROR");
            info.put("fontSize", Integer.valueOf(fontSize).toString());
            info.put("fontColor", String.format("%d, %d, %d", color.getRed(), color.getGreen(), color.getBlue()));
            return info;
        }

        public LinkedHashMap<String, String> getStringMetrics(){ // метод формирования метрик строки (для панели метрик)
            LinkedHashMap<String, String> metrics = new LinkedHashMap<>(2);
            metrics.put("strWidth", Integer.toString(getFM().stringWidth(text)));
            metrics.put("strHeight", Integer.toString(getFM().getHeight()));
            return metrics;
        }
    }

    private static class BottomPanel extends JPanel{
        private JTextField inputTextField;
        private LimitDocument limitDocument;

        public BottomPanel(){ // нижня панель вкючает в себя строку ввода и панель метрик
            limitDocument = new LimitDocument(100);
            setBorder(new EtchedBorder());
            setLayout(new GridBagLayout());

            inputTextField = new JTextField(); // поле для ввода строки
            inputTextField.setFont(new Font("Arial", Font.PLAIN, 20));
            inputTextField.setDocument(limitDocument);

            JLabel textLabel = new JLabel("Строка:");
            textLabel.setFont(new Font("Arial", Font.PLAIN, 20));

            add(textLabel, new GBC(0, 0, 2, 1).setWeight(0, 0.1).setAnchor(GBC.EAST).setInsets(0, 0, 10, 0));
            add(inputTextField, new GBC(2, 0, 14, 1).setWeight(1, 0.1).setFill(1).setInsets(10));

            textInfoPanel = new TextInfoPanel(); // панель с метриками о шрифте и строке

            add(textInfoPanel, new GBC(0, 1, 16, 2).setWeight(1, 1).setFill(1));
        }

        public JTextField getInputTextField(){
            return inputTextField;
        } // доступ к полю ввода строки

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(TEXT_PANEL_W, FRAME_H - TEXT_PANEL_H);
        }
    }

    private static class TextInfoPanel extends JPanel{ // панель с метриками
        private static final Font font12 = new Font("Arial", Font.BOLD, 12);

        LinkedHashMap<String, JLabel> valuesMap = new LinkedHashMap<>(4); // отображение метрик шрифта
        LinkedHashMap<String, JLabel> metricsMap = new LinkedHashMap<>(2); // отображение метрик строки
        Map<String, JLabel> labelsMap; // отображения лейблов для метрик
        Map<String, JLabel> labelsMetrMap;

        { // блок инициализации для отображений пометок для метрик
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

        public TextInfoPanel(){
            setLayout(new GridLayout(1, 3));

            JPanel fontInfoPanel = new JPanel();
            JPanel fontMetricsPanel = new JPanel();

            fontInfoPanel.setLayout(new GridBagLayout());
            fontMetricsPanel.setLayout(new GridBagLayout());

            // первичное заполнение значений метрик yа основе методов текстового компонента и предславлений (k, v)
            for (Map.Entry<String, String> e : textComponent.getFontInfo().entrySet()){
                valuesMap.put(e.getKey(), new JLabel(e.getValue()));
            }

            for (Map.Entry<String, String> e : textComponent.getStringMetrics().entrySet()){
                metricsMap.put(e.getKey(), new JLabel(e.getValue()));
            }

            // нанесение на панель метрик шрифта подписей и значений
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

            // нанесение на панель метрик строки подписей и значений
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

            fontMetricsPanel.setBorder(new EtchedBorder()); // ограничение панелей
            fontInfoPanel.setBorder(new EtchedBorder());

            add(fontInfoPanel);
            add(new JPanel());
            add(fontMetricsPanel);
        }

        public void updatePanel(){ // обновление панели = обновление значений в соответствующих отображениях
            for (Map.Entry<String, String> e : textComponent.getFontInfo().entrySet()){
                valuesMap.get(e.getKey()).setText(e.getValue());
            }

            for (Map.Entry<String, String> e : textComponent.getStringMetrics().entrySet()){
                metricsMap.get(e.getKey()).setText(e.getValue());
            }
            repaint();
        }
    }

    private static class MainMenuBar extends JMenuBar{ // панель меню
        private static final boolean PALETTE = false;
        private static final boolean ADDITIVE = true;

        public MainMenuBar(){
            JMenu fileMenu = new JMenu("Файл (F)"); // к каждому меню присвоена мнемоника
            JMenu fontMenu = new JMenu("Шрифт (S)");
            JMenu colorMenu = new JMenu("Цвет (C)");

            JMenu outlineMenu = new JMenu("Начертание (O)");

            outlineMenu.setMnemonic(KeyEvent.VK_O); // установка мнемоник к меню
            fileMenu.setMnemonic(KeyEvent.VK_F);
            fontMenu.setMnemonic(KeyEvent.VK_S);
            colorMenu.setMnemonic(KeyEvent.VK_C);

            JMenuItem exitItem = new JMenuItem("Выход (E)", 'E'); // конструирование пунктов меню с мнемониками
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

            ButtonGroup radioGroup = new ButtonGroup(); // набор из 3-х радио-кнопок под выбор начертания
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
                try { // получение диалогового окна выбора гарнитуры
                    DialogWindows.TextAdder tA = DialogWindows.getTextAddedDialog(mainFrame, 0);
                    if (tA.showDialog()){ // если в диалоге нажата "ок" - возврат true => можно:
                        textComponent.setFontName(tA.getText()); // получить имя текста по методу из диалога
                        textComponent.setFont(); // обновить шрифт в компоненте и обновить сам компонент
                        textComponent.repaint();
                        textInfoPanel.updatePanel();
                    }
                } catch (IOException ex) {System.out.println(ex.getMessage());}

            });

            JMenuItem colorAddsItem = new JMenuItem("Наборный (N)", 'N');
            JMenuItem colorPaletteItem = new JMenuItem("Плитка (P)", 'P');

            colorAddsItem.addActionListener((e) -> { // наборное диалоговое окно выбора текста
                DialogWindows.TextColorChooser tC = DialogWindows.getTextColorChooser(mainFrame, ADDITIVE, textComponent.getColor());
                if (tC.showDialog()){ // нажата "ок" =>
                    textComponent.setColor(tC.getColor()); // получить из диалога выбранный цвет
                    textComponent.repaint(); // обновить компонент со строкой
                    textInfoPanel.updatePanel();
                }
            });

            colorPaletteItem.addActionListener((e) -> { // плиточное диалоговое окно выбора текста
                DialogWindows.TextColorChooser tC = DialogWindows.getTextColorChooser(mainFrame, PALETTE, textComponent.getColor());
                if (tC.showDialog()){ // аналогично выше
                    textComponent.setColor(tC.getColor());
                    textComponent.repaint();
                    textInfoPanel.updatePanel();
                }
            });

            JMenuItem textSizeItem = new JMenuItem("Размер (S)", 'S');
            textSizeItem.addActionListener((e) -> { // диалоговое окно выбора размера текста
                DialogWindows.TextSizeChooser tSC = DialogWindows.getTextSizeChooser(mainFrame, Integer.parseInt(textComponent.getFontInfo().get("fontSize")) - 1);
                if (tSC.showDialog()){ // нажата "ок" =>
                    textComponent.setFontSize(tSC.getSizeFromCombo()); // получить выбранный размер из диалога
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

    private static class OutlineAction extends AbstractAction{
        HashMap<String, Integer> outlineMap = new HashMap<>(3);

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

    // модифицированный Document (модель текстового компонента по MVC)
    private static class LimitDocument extends PlainDocument{
        private int limit; // ограничение длины строки в символах

        public LimitDocument(int l){
            super();
            limit = l;
            addDocumentListener(new DocumentListener() { // здесь же можно добавить перехватчик обновления строки
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

        public void updateDocument(int l){
            limit = l;
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) return;

            if ((getLength() + str.length() <= limit)){ // переопределение метода вставки строки с учетом ограничения длины
                super.insertString(offs, str, a);
            }
        }
    }
}
