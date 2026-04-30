package testing.Interfaces;

import java.io.File;
import java.io.IOException;

/**
 * Интерфейс, описывающий функциональные требования к контроллеру приложения по паттерну MVC.
 * Контроллер обрабатывает пользовательский ввод и управляет моделью.
 */
public interface Controller {

    /**
     * Устанавливает текущую директорию.
     *
     * @param directory новая директория для просмотра
     * @throws IOException если возникает ошибка при чтении директории
     */
    void setCurrentDirectory(File directory) throws IOException;

    /**
     * Переключается на следующее фото.
     */
    void nextPhoto();

    /**
     * Переключается на предыдущее фото.
     */
    void backPhoto();

    /**
     * Открывает окно с масштабированием.
     */
    void createZoomFrame();

    /**
     * Увеличивает коэффициент масштабирования.
     *
     * @param zoom значение, на которое нужно увеличить масштаб
     */
    void addZoom(double zoom);

    /**
     * Уменьшает коэффициент масштабирования.
     *
     * @param zoom значение, на которое нужно уменьшить масштаб
     */
    void subZoom(double zoom);
}