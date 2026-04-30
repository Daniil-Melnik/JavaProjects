package testing.Implementation.BackEnd.Holder.Components;

import lombok.Getter;
import testing.CursorList;

import java.awt.Image;
import java.util.List;

/**
 * Компонент модели, ответственный за хранение коллекций изображений и их наименований.
 * Обеспечивает навигацию по коллекциям и получение соседних элементов.
 */
@Getter
public class ImageCollection {

    /** Список изображений с курсором */
    private CursorList<Image> imageList;

    /** Список наименований изображений с курсором */
    private CursorList<String> titleList;

    /**
     * Устанавливает список изображений.
     *
     * @param images список изображений для установки
     */
    public void setImageList(List<Image> images){
        imageList = new CursorList<>(images);
    }

    /**
     * Устанавливает список наименований.
     *
     * @param titles список наименований для установки
     */
    public void setTitleList(List<String> titles) {
        titleList = new CursorList<>(titles);
    }

    /**
     * Конструктор по умолчанию.
     * Создаёт пустые коллекции.
     */
    public ImageCollection(){
        imageList = new CursorList<>();
        titleList = new CursorList<>();
    }

    /**
     * Конструктор с инициализацией коллекций.
     *
     * @param images список изображений
     * @param titles список наименований
     */
    public ImageCollection(List<Image> images, List<String> titles){
        imageList = new CursorList<>(images);
        titleList = new CursorList<>(titles);
    }

    /**
     * Возвращает пять изображений, соседних с текущим.
     *
     * @return список из пяти соседних изображений
     */
    public List<Image> getNearImages(){
        return imageList.getFiveNearElements();
    }

    /**
     * Возвращает пять наименований, соседних с текущим.
     *
     * @return список из пяти соседних наименований
     */
    public List<String> getNearTitles(){
        return titleList.getFiveNearElements();
    }

    /**
     * Возвращает текущее изображение.
     *
     * @return текущее изображение или null, если список пуст
     */
    public Image getCurrentImage(){
        return imageList.getCurrent();
    }

    /**
     * Возвращает наименование текущего изображения.
     *
     * @return текущее наименование или null, если список пуст
     */
    public String getCurrentTitle(){
        return titleList.getCurrent();
    }

    /**
     * Выполняет безопасный переход на следующее изображение.
     *
     * @return true если переход выполнен успешно, false если достигнут конец списка
     */
    public boolean next(){
        boolean result = false;
        imageList.incrCurrentIndex();
        if (imageList.getCurrent() == null){
            imageList.decrCurrentIndex();
        } else {
            titleList.incrCurrentIndex();
            result = true;
        }
        return result;
    }

    /**
     * Выполняет безопасный переход на предыдущее изображение.
     *
     * @return true если переход выполнен успешно, false если достигнуто начало списка
     */
    public boolean previous(){
        boolean result = false;
        imageList.decrCurrentIndex();
        if (imageList.getCurrent() == null){
            imageList.incrCurrentIndex();
        } else {
            titleList.decrCurrentIndex();
            result = true;
        }
        return result;
    }

    /**
     * Возвращает позицию курсора в рамках пяти соседних элементов.
     *
     * @return индекс курсора в диапазоне 0-4
     */
    public int getFlag(){
        return imageList.getFiveElementsFlag();
    }
}