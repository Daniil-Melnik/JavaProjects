package testing.Implementation.BackEnd.Controller;

import testing.CursorList;
import testing.Implementation.Constants;
import testing.Implementation.FrontEnd.ZoomFrame.ZoomFrame;
import testing.Interfaces.Controller;
import testing.Interfaces.Holder;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Реализация контроллера в MVC архитектуре приложения.
 * Обрабатывает пользовательский ввод и управляет моделью.
 */
public class SimpleController implements Controller {

    /** Ссылка на модель данных */
    private Holder holder;

    /**
     * Конструктор контроллера.
     *
     * @param holder модель данных, которой будет управлять контроллер
     */
    public SimpleController(Holder holder){
        this.holder = holder;
    }

    /**
     * Устанавливает новую текущую директорию и загружает из неё изображения.
     *
     * @param directory новая директория для просмотра
     * @throws IOException если возникает ошибка при загрузке файлов
     */
    @Override
    public void setCurrentDirectory(File directory) throws IOException {
        holder.setCurrentDirectory(directory);
        holder.setImages(new CursorList<>(FileUtil.getImagesFromFileArray(directory.listFiles())));
        holder.setImageNames(new CursorList<>(FileUtil.getTitlesFromFileArray(directory.listFiles())));
    }

    /**
     * Переключается на следующее изображение.
     */
    @Override
    public void nextPhoto() {
        holder.nextImage();
    }

    /**
     * Переключается на предыдущее изображение.
     */
    @Override
    public void backPhoto() {
        holder.previousImage();
    }

    /**
     * Создаёт и отображает окно с возможностью масштабирования изображения.
     */
    @Override
    public void createZoomFrame() {
        ZoomFrame.getInstance(holder, this).setVisible(true);
    }

    /**
     * Увеличивает коэффициент масштабирования текущего изображения.
     *
     * @param addZoom значение, на которое нужно увеличить масштаб
     */
    @Override
    public void addZoom(double addZoom){
        holder.setZoom(Constants.SCALE_UP, addZoom);
    }

    /**
     * Уменьшает коэффициент масштабирования текущего изображения.
     *
     * @param subZoom значение, на которое нужно уменьшить масштаб
     */
    @Override
    public void subZoom(double subZoom){
        holder.setZoom(Constants.SCALE_DOWN, subZoom);
    }
}