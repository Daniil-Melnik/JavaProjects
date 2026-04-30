package testing.Implementation.FrontEnd.ZoomFrame.Panels;

import testing.Implementation.Constants;
import testing.Interfaces.Compositor;
import testing.Interfaces.Controller;
import testing.Interfaces.Holder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Панель с изображением для окна масштабирования.
 * Поддерживает изменение масштаба с помощью прокрутки и кликов мыши.
 * Может использоваться как листовой узел в компоновщике.
 */
public class PhotoPanel extends JPanel implements Compositor{

    /** Обёртка для изображения, хранит изображение с учётом коэффициента увеличения */
    private static ImageIcon iconImage;

    /** Метка с изображением */
    private static JLabel imageLabel;

    /** Истинная ширина изображения для корректного масштабирования */
    private static int normalPhotoWidth;

    /** Истинная высота изображения для корректного масштабирования */
    private static int normalPhotoHeight;

    /** Ссылка на модель данных */
    private Holder holder;

    /** Панель прокрутки для изображения */
    private JScrollPane scrollPane;

    /**
     * Конструктор панели изображения.
     *
     * @param controller контроллер для выполнения масштабирования
     * @param holder модель данных
     */
    public PhotoPanel(Controller controller, Holder holder){
        this.holder = holder;
        setPreferredSize(new Dimension(normalPhotoWidth, normalPhotoHeight));
        setLayout(new BorderLayout());
        iconImage = new ImageIcon();
        imageLabel = new JLabel(iconImage);
        add(imageLabel, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1){
                    controller.addZoom(Constants.SCALE_STEP);
                } else if (e.getButton() == MouseEvent.BUTTON3){
                    controller.subZoom(Constants.SCALE_STEP);
                }
            }
        });

        scrollPane = new JScrollPane(
                this,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setPreferredSize(new Dimension(Constants.PHOTO_SCROLL_PANEL_W, Constants.PHOTO_SCROLL_PANEL_H));
    }

    /**
     * Возвращает Swing-компонент панели (панель прокрутки с изображением).
     *
     * @return панель прокрутки
     */
    @Override
    public JComponent getComponent() {
        return scrollPane;
    }

    /**
     * Обновляет изображение с учётом текущего коэффициента масштаба.
     * Пересчитывает размеры изображения и обновляет интерфейс.
     */
    @Override
    public void refresh() {
        normalPhotoWidth = holder.getCurrentImage().getWidth(null);
        normalPhotoHeight = holder.getCurrentImage().getHeight(null);

        this.remove(imageLabel);

        int newWidth = (int) (normalPhotoWidth * holder.getZoom());
        int newHeight = (int) (normalPhotoHeight * holder.getZoom());

        if ((newHeight > 0) && (newWidth > 0)) {
            Image newImage = holder.getCurrentImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            iconImage.setImage(newImage);
            imageLabel.setIcon(iconImage);
            this.setPreferredSize(new Dimension(newWidth, newHeight));
            this.add(imageLabel, BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }
}