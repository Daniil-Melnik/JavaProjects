package testing.Interfaces;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Интерфейс, описывающий модель приложения по паттерну MVC.
 * Модель хранит данные о:
 * <ul>
 *   <li>рабочей директории</li>
 *   <li>хранящихся в ней изображениях и их наименованиях</li>
 *   <li>текущем изображении с его наименованием</li>
 *   <li>пяти соседних изображениях с их наименованиями</li>
 *   <li>коэффициенте масштабирования</li>
 *   <li>средствах получения информации о фото</li>
 * </ul>
 */
public interface Holder {

    /**
     * Возвращает текущее изображение.
     *
     * @return текущее изображение
     */
    Image getCurrentImage();

    /**
     * Возвращает список соседних изображений.
     *
     * @return список из пяти соседних изображений
     */
    List<Image> getNearImages();

    /**
     * Возвращает наименование текущего изображения.
     *
     * @return наименование текущего изображения
     */
    String getCurrentImageName();

    /**
     * Возвращает список наименований соседних изображений.
     *
     * @return список из пяти наименований
     */
    List<String> getNearImagesNames();

    /**
     * Возвращает текущий коэффициент масштабирования.
     *
     * @return значение масштаба
     */
    double getZoom();

    /**
     * Возвращает список всех изображений.
     *
     * @return список всех изображений
     */
    List<Image> getImages();

    /**
     * Возвращает список всех наименований.
     *
     * @return список всех наименований
     */
    List<String> getTitles();

    /**
     * Возвращает флаг позиции курсора.
     *
     * @return флаг курсора
     */
    int getFlag();

    /**
     * Возвращает информацию об изображении.
     *
     * @param title путь к файлу изображения
     * @return отображение с информацией об изображении
     * @throws IOException если возникает ошибка при чтении файла
     */
    Map<String, Number> getImageInfo(String title) throws IOException;

    /**
     * Устанавливает текущее изображение.
     *
     * @param Image новое текущее изображение
     */
    default void setCurrentImage(Image Image) {};

    /**
     * Устанавливает наименование текущего изображения.
     *
     * @param ImageName новое наименование
     */
    default void setCurrentImageName(String ImageName){};

    /**
     * Устанавливает список соседних изображений.
     *
     * @param Images список соседних изображений
     */
    default void setNearImages(List<Image> Images){};

    /**
     * Устанавливает список наименований соседних изображений.
     *
     * @param ImageNames список наименований
     */
    default void setNearImagesNames(List<String> ImageNames){};

    /**
     * Устанавливает список всех изображений.
     *
     * @param images список изображений
     */
    void setImages(List<Image> images);

    /**
     * Устанавливает список всех наименований.
     *
     * @param names список наименований
     */
    void setImageNames(List<String> names);

    /**
     * Устанавливает коэффициент масштабирования.
     *
     * @param addOrSub флаг направления изменения
     * @param zoom значение изменения
     */
    void setZoom(boolean addOrSub, double zoom);

    /**
     * Устанавливает текущую директорию.
     *
     * @param directory новая директория
     */
    void setCurrentDirectory(File directory);

    /**
     * Переключается на следующее изображение.
     */
    default void nextImage(){};

    /**
     * Переключается на предыдущее изображение.
     */
    default void previousImage(){};
}