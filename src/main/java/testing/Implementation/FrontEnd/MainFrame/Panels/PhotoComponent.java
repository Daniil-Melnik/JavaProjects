package testing.Implementation.FrontEnd.MainFrame.Panels;

import lombok.Setter;
import testing.Implementation.Constants;
import testing.Interfaces.Compositor;
import testing.Interfaces.Holder;

import javax.swing.*;
import java.awt.*;

/**
 * Компонент для отображения изображения.
 * Может использоваться как для главного изображения, так и для миниатюр в слайд-панели.
 * Реализует интерфейс {@link Compositor} как листовой узел в компоновщике.
 */
public class PhotoComponent extends JComponent implements Compositor {

    /** Текущее изображение для отрисовки */
    @Setter
    Image currentImage;

    /** Тип компонента: true - маленький (миниатюра), false - большой */
    boolean type;

    /** Стандартная ширина, выделяемая под изображение */
    int standartWidth;

    /** Стандартная высота, выделяемая под изображение */
    int standartHeight;

    /** Ссылка на модель данных */
    Holder holder;

    /** Наименование изображения для отрисовки */
    @Setter
    String currentTitle;

    /** Индекс в списке соседних изображений (-1 для главного изображения) */
    int index = -1;

    /**
     * Конструктор с прямым указанием изображения и названия.
     *
     * @param cI изображение
     * @param t тип компонента (true - миниатюра, false - главное)
     * @param iT наименование изображения
     */
    public PhotoComponent(Image cI, boolean t, String iT) {
        currentImage = cI;
        type = t;
        standartWidth = t ? Constants.SMALL_PHOTO_W : Constants.FRAME_W;
        standartHeight = t ? Constants.SMALL_PHOTO_H : Constants.BIG_PHOTO_PANEL_H;
        currentTitle = iT;

        setToolTipText(currentTitle);
    }

    /**
     * Конструктор с привязкой к модели для динамического обновления.
     *
     * @param t тип компонента (true - миниатюра, false - главное)
     * @param h модель данных
     * @param i индекс в списке соседних изображений
     */
    public PhotoComponent(boolean t, Holder h, int i) {
        currentImage = null;
        type = t;
        standartWidth = t ? Constants.SMALL_PHOTO_W : Constants.FRAME_W;
        standartHeight = t ? Constants.SMALL_PHOTO_H : Constants.BIG_PHOTO_PANEL_H;
        currentTitle = "";
        holder = h;
        index = i;
    }

    /**
     * Устанавливает в компонент изображение и его наименование.
     *
     * @param img изображение
     * @param title наименование изображения
     */
    public void setCurrentImageTitle(Image img, String title) {
        setCurrentImage(img);
        setCurrentTitle(title);
    }

    /**
     * Отрисовывает изображение с учётом размеров панели.
     *
     * @param g графический контекст для отрисовки
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (currentImage != null) {
            g.drawImage(
                    currentImage,
                    (int) getPhotoPoint().getX(),
                    (int) getPhotoPoint().getY(),
                    (int) getPhotoDimension().getWidth(),
                    (int) getPhotoDimension().getHeight(),
                    null);
        }
    }

    /**
     * Возвращает предпочтительный размер компонента.
     *
     * @return размер компонента в зависимости от типа
     */
    @Override
    public Dimension getPreferredSize() {
        return (type ? new Dimension(Constants.SMALL_PHOTO_PANEL_W, Constants.SLIDE_PANEL_H)
                : new Dimension(Constants.FRAME_W, Constants.BIG_PHOTO_PANEL_H));
    }

    /**
     * Рассчитывает размеры изображения в рамках отведённой области.
     *
     * @return размеры изображения для отрисовки
     */
    private Dimension getPhotoDimension() {
        int imgWidth = currentImage.getWidth(null);
        int imgHeight = currentImage.getHeight(null);

        double ratioWidthHeight = (double) imgWidth / imgHeight;

        if (imgWidth > standartWidth) {
            imgWidth = standartWidth;
            imgHeight = (int) (imgWidth * (1 / ratioWidthHeight));
        }

        if (imgHeight > standartHeight) {
            imgHeight = standartHeight;
            imgWidth = (int) (imgHeight * ratioWidthHeight);
        }
        return new Dimension(imgWidth, imgHeight);
    }

    /**
     * Рассчитывает координаты левого верхнего угла для отрисовки изображения.
     *
     * @return точка для размещения изображения
     */
    private Point getPhotoPoint() {
        int standardPanelWidth = type ? Constants.SMALL_PHOTO_PANEL_W : Constants.FRAME_W;
        int standardPanelHeight = type ? Constants.SMALL_PHOTO_PANEL_H : Constants.BIG_PHOTO_PANEL_H;

        int x = (int) ((standardPanelWidth - getPhotoDimension().getWidth()) / 2);
        int y = (int) ((standardPanelHeight - getPhotoDimension().getHeight()) / 2);

        return new Point(x, y);
    }

    /**
     * Возвращает Swing-компонент.
     *
     * @return текущий компонент
     */
    @Override
    public JComponent getComponent() {
        return this;
    }

    /**
     * Обновляет изображение в компоненте на основе текущих данных модели.
     * Полное обновление листового узла компоновщика.
     */
    @Override
    public void refresh() {
        setCurrentImageTitle(null, null);
        if (!(holder.getNearImages().isEmpty()) && !(holder.getNearImagesNames().isEmpty())
                && (index < holder.getNearImages().size())){
            Image newImage = index == -1 ? holder.getCurrentImage() : holder.getNearImages().get(index);
            String newTitle = index == -1 ? holder.getCurrentImageName() : holder.getNearImagesNames().get(index);
            setCurrentImageTitle(newImage, newTitle);
            this.setToolTipText(newTitle);
        }
        this.revalidate();
        this.repaint();
    }
}