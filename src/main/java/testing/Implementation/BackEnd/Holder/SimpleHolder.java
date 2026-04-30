package testing.Implementation.BackEnd.Holder;

import testing.Implementation.BackEnd.Holder.Components.ImageCollection;
import testing.Implementation.BackEnd.Holder.Components.ImageInfoCalculator;
import testing.Implementation.BackEnd.Holder.Components.ZoomManager;
import testing.Implementation.Constants;
import testing.Interfaces.Holder;
import testing.Interfaces.Publisher;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Реализация модели в MVC архитектуре.
 * Объединяет под одним фасадом несколько компонентов, отвечающих за хранение изображений,
 * управление масштабом и оповещение наблюдателей об изменениях.
 */
public class SimpleHolder implements Holder {

    /** Компонент, отвечающий за операции с набором изображений */
    private ImageCollection imageCollection;

    /** Компонент, отвечающий за масштабирование изображения */
    private ZoomManager zoomManager;

    /** Текущая рабочая директория */
    private File currentDirectory;

    /** Менеджер оповещения об изменениях модели по паттерну Наблюдатель */
    private Publisher publisher;

    /**
     * Конструктор модели.
     *
     * @param publisher менеджер оповещений для уведомления наблюдателей
     */
    public SimpleHolder(Publisher publisher){
        this.publisher = publisher;
        imageCollection = new ImageCollection();
        zoomManager = new ZoomManager();
    }

    /**
     * Возвращает текущее изображение.
     *
     * @return текущее изображение
     */
    @Override
    public Image getCurrentImage() {
        return imageCollection.getCurrentImage();
    }

    /**
     * Возвращает список соседних с текущим изображений.
     *
     * @return список из пяти соседних изображений
     */
    @Override
    public List<Image> getNearImages() {
        return imageCollection.getNearImages();
    }

    /**
     * Возвращает наименование текущего изображения.
     *
     * @return наименование текущего изображения
     */
    @Override
    public String getCurrentImageName() {
        return imageCollection.getCurrentTitle();
    }

    /**
     * Возвращает список наименований соседних изображений.
     *
     * @return список из пяти наименований соседних изображений
     */
    @Override
    public List<String> getNearImagesNames() {
        return imageCollection.getNearTitles();
    }

    /**
     * Возвращает текущий коэффициент масштабирования.
     *
     * @return значение масштаба
     */
    @Override
    public double getZoom() {
        return zoomManager.getZoom();
    }

    /**
     * Возвращает список всех изображений.
     *
     * @return список всех изображений
     */
    @Override
    public List<Image> getImages() {
        return imageCollection.getImageList();
    }

    /**
     * Возвращает список всех наименований.
     *
     * @return список всех наименований
     */
    @Override
    public List<String> getTitles() {
        return imageCollection.getTitleList();
    }

    /**
     * Устанавливает новый список изображений и оповещает всех наблюдателей.
     *
     * @param images новый список изображений
     */
    @Override
    public void setImages(List<Image> images) {
        imageCollection.setImageList(images);
        publisher.notify(this, Constants.ALL);
    }

    /**
     * Устанавливает новый список наименований и оповещает всех наблюдателей.
     *
     * @param names новый список наименований
     */
    @Override
    public void setImageNames(List<String> names) {
        imageCollection.setTitleList(names);
        publisher.notify(this, Constants.ALL);
    }

    /**
     * Устанавливает новый коэффициент масштабирования.
     *
     * @param addOrSub флаг направления изменения (true - увеличение, false - уменьшение)
     * @param zoom значение изменения масштаба
     */
    @Override
    public void setZoom(boolean addOrSub, double zoom) {
        if (addOrSub) zoomManager.addZoom(zoom);
        else zoomManager.subZoom(zoom);
        publisher.notify(this, Constants.ZOOM);
    }

    /**
     * Устанавливает текущую рабочую директорию.
     *
     * @param directory новая директория
     */
    @Override
    public void setCurrentDirectory(File directory) {
        this.currentDirectory = directory;
    }

    /**
     * Переключается на следующее изображение.
     * При успешном переходе сбрасывает масштаб на нормальный.
     */
    @Override
    public void nextImage() {
        if (imageCollection.next()) {
            zoomManager.setNormalZoom();
            publisher.notify(this, Constants.ALL);
        }
    }

    /**
     * Переключается на предыдущее изображение.
     * При успешном переходе сбрасывает масштаб на нормальный.
     */
    @Override
    public void previousImage() {
        if (imageCollection.previous()){
            zoomManager.setNormalZoom();
            publisher.notify(this, Constants.ALL);
        }
    }

    /**
     * Возвращает информацию об изображении по указанному пути.
     *
     * @param title путь к файлу изображения
     * @return отображение с информацией об изображении
     * @throws IOException если возникает ошибка при чтении файла
     */
    @Override
    public HashMap<String, Number> getImageInfo(String title) throws IOException {
        return ImageInfoCalculator.getImageInfo(title);
    }

    /**
     * Возвращает позицию курсора в рамках пяти соседних элементов.
     *
     * @return индекс курсора в диапазоне 0-4
     */
    @Override
    public int getFlag(){
        return imageCollection.getFlag();
    }
}