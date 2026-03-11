package testing.PhotoViewer;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * Класс, описывающий отдельное окно для просмотра изображения с возможностью масштабирования.
 * Предоставляет функциональность увеличения/уменьшения изображения с помощью кнопок,
 * клавиатуры или щелчков мыши.
 */
public class PhotoScrollFrame extends JFrame {

    /** Изображение для отображения в окне */
    @Getter
    private static Image image;

    /** Обёртка для изображения, хранит масштабированную версию */
    private static ImageIcon iconImage;

    /** Метка, в которую отрисовывается масштабированное изображение */
    private static JLabel imageLabel;

    /** Метка, отображающая текущий коэффициент увеличения */
    private static JLabel scaleLabel;

    /** Истинная ширина изображения для корректного масштабирования */
    private int normalPhotoWidth;

    /** Истинная высота изображения для корректного масштабирования */
    private int normalPhotoHeight;

    /** Название изображения */
    private String photoTitle = "";

    /** Текущий коэффициент увеличения */
    private double zoom = NORMAL_SCALE;

    /** Панель, содержащая изображение */
    @Getter
    private JPanel photoPanel;

    /** Ширина окна с прокруткой */
    private static final int SCROLL_FRAME_W = 1500;
    /** Высота окна с прокруткой */
    private static final int SCROLL_FRAME_H = 920;

    /** Ширина панели с изображением */
    private static final int PHOTO_SCROLL_PANEL_W = 1500;
    /** Высота панели с изображением */
    private static final int PHOTO_SCROLL_PANEL_H = 850;

    /** Ширина панели с кнопками */
    private static final int BTN_PANEL_W = 1500;
    /** Высота панели с кнопками */
    private static final int BTN_PANEL_H = 70;

    /** Флаг увеличения масштаба */
    private static final boolean SCALE_UP = true;
    /** Флаг уменьшения масштаба */
    private static final boolean SCALE_DOWN = false;

    /** Шаг изменения масштаба */
    private static final double SCALE_STEP = 0.1;

    /** Максимальный коэффициент увеличения */
    private static final double MAX_SCALE = 2;
    /** Базовый (нормальный) коэффициент увеличения */
    private static final double NORMAL_SCALE = 1;
    /** Минимальный коэффициент увеличения (с учётом погрешностей чисел с плавающей точкой) */
    private static final double MIN_SCALE = 0.2;

    /** Серый фон окна */
    private static final Color GRAY_BACKGROUND = new Color(238, 238, 238);

    /**
     * Конструктор окна для просмотра изображения.
     *
     * @param img   изображение для отображения
     * @param title название изображения (отображается в заголовке окна)
     */
    public PhotoScrollFrame(Image img, String title) {
        image = img;
        photoTitle = title;

        normalPhotoWidth = image.getWidth(null);
        normalPhotoHeight = image.getHeight(null);

        setLayout(new GridBagLayout());
        setSize(SCROLL_FRAME_W, SCROLL_FRAME_H);

        setIconImage(new ImageIcon(
                Objects.requireNonNull(
                        this.getClass().getResource("/photo.png"))).getImage());
        setTitle("Фотосмотр - " + photoTitle);

        add(createScrollPanel(), new GBC(0, 0, 1, 1));
        add(createButtonPanel(), new GBC(0, 1, 1, 1));
        pack();
    }

    /**
     * Создаёт панель с изображением и добавляет обработчик щелчков мыши для масштабирования.
     *
     * @return панель с изображением
     */
    private JPanel createPhotoPanel() {
        JPanel phPanel = new JPanel(new BorderLayout());
        phPanel.setPreferredSize(new Dimension(normalPhotoWidth, normalPhotoHeight));
        iconImage = new ImageIcon(image);
        imageLabel = new JLabel(iconImage);
        phPanel.add(imageLabel, BorderLayout.CENTER);

        phPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    scalePhoto(SCALE_UP);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    scalePhoto(SCALE_DOWN);
                }
            }
        });

        return phPanel;
    }

    /**
     * Создаёт панель с полосами прокрутки, содержащую изображение.
     * Полосы прокрутки появляются при необходимости, когда изображение превышает размеры области просмотра.
     *
     * @return компонент {@link JScrollPane} с изображением
     */
    private JScrollPane createScrollPanel() {
        photoPanel = createPhotoPanel();

        JScrollPane scrollPane = new JScrollPane(
                photoPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setPreferredSize(new Dimension(PHOTO_SCROLL_PANEL_W, PHOTO_SCROLL_PANEL_H));

        return scrollPane;
    }

    /**
     * Создаёт панель с кнопками управления масштабом.
     * Содержит кнопки увеличения, уменьшения и метку с текущим коэффициентом масштабирования.
     * Поддерживает управление с клавиатуры (клавиши + и -).
     *
     * @return панель с элементами управления
     */
    private JPanel createButtonPanel() {
        JPanel btnPanel = new JPanel(new FlowLayout());

        JButton plusBtn = new JButton(new ImageIcon(
                Objects.requireNonNull(this.getClass()
                        .getResource("/zoom-in.png"))
        ));

        JButton minusBtn = new JButton(new ImageIcon(
                Objects.requireNonNull(this.getClass()
                        .getResource("/zoom-out.png"))
        ));

        plusBtn.setBackground(GRAY_BACKGROUND);
        minusBtn.setBackground(GRAY_BACKGROUND);

        minusBtn.setBorder(null);
        plusBtn.setBorder(null);

        scaleLabel = new JLabel(String.format("%.1f", zoom));
        scaleLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        plusBtn.addActionListener((e) -> {
            scalePhoto(SCALE_UP);
        });

        minusBtn.addActionListener((e) -> {
            scalePhoto(SCALE_DOWN);
        });

        // Добавление реакций на нажатие клавиш клавиатуры
        btnPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ADD"), "zoom_in");
        btnPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SUBTRACT"), "zoom_out");

        btnPanel.getActionMap().put("zoom_in", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scalePhoto(SCALE_UP);
            }
        });
        btnPanel.getActionMap().put("zoom_out", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scalePhoto(SCALE_DOWN);
            }
        });

        btnPanel.setPreferredSize(new Dimension(BTN_PANEL_W, BTN_PANEL_H));

        btnPanel.add(minusBtn);
        btnPanel.add(scaleLabel);
        btnPanel.add(plusBtn);

        return btnPanel;
    }

    /**
     * Изменяет масштаб изображения.
     * При увеличении или уменьшении пересчитывает размеры изображения на основе истинных размеров
     * и текущего коэффициента масштабирования. Обновляет отображение и предпочтительные размеры панели.
     *
     * @param type направление масштабирования:
     *             {@link #SCALE_UP} - увеличение,
     *             {@link #SCALE_DOWN} - уменьшение
     */
    private void scalePhoto(boolean type) {
        if (type && zoom < MAX_SCALE) zoom += SCALE_STEP;
        else if (!type && zoom > MIN_SCALE) zoom -= SCALE_STEP;
        else zoom = zoom;

        photoPanel.remove(imageLabel);
        scaleLabel.setText(String.format("%.1f", zoom));

        int newWidth = (int) (normalPhotoWidth * zoom);
        int newHeight = (int) (normalPhotoHeight * zoom);

        if ((newHeight > 0) && (newWidth > 0)) {
            Image newImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            iconImage.setImage(newImage);
            imageLabel.setIcon(iconImage);
            photoPanel.setPreferredSize(new Dimension(newWidth, newHeight));
            photoPanel.add(imageLabel, BorderLayout.CENTER);

            revalidate();
            repaint();
        }
    }
}