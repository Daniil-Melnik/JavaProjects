package testing.PhotoViewer;

import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Класс-прослойка между хранящими списками изображений и графическим интерфейсом.
 * Обеспечивает синхронизацию списков изображений и их названий,
 * а также предоставляет методы для навигации и получения информации о текущем изображении.
 */
@Getter
public class PhotoHolder {
    /** Список изображений с возможностью навигации по курсору */
    private CursorList<Image> imageList;

    /** Список названий файлов изображений, синхронизированный со списком изображений */
    private CursorList<String> titleList;

    /**
     * Конструктор по умолчанию.
     * Инициализирует пустые списки с начальной емкостью 100 элементов.
     */
    public PhotoHolder() {
        imageList = new CursorList<>(100);
        titleList = new CursorList<>(100);
    }

    /**
     * Устанавливает списки изображений и названий на основе массива файлов.
     * Производит фильтрацию файлов по расширениям (png, jpg, jpeg).
     *
     * @param files массив файлов для загрузки
     * @throws IOException если возникает ошибка при чтении файлов изображений
     */
    public void setImageList(File[] files) throws IOException {
        imageList = new CursorList<>(getImagesFromFileArray(files));
        titleList = new CursorList<>(getTitlesFromFileArray(files));
    }

    /**
     * Увеличивает курсоры в обоих списках одновременно.
     * Обеспечивает синхронную навигацию по изображениям и их названиям.
     */
    public void incrCursorInLists() {
        imageList.incrCurrentIndex();
        titleList.incrCurrentIndex();
    }

    /**
     * Уменьшает курсоры в обоих списках одновременно.
     * Обеспечивает синхронную навигацию по изображениям и их названиям.
     */
    public void decrCursorInLists() {
        imageList.decrCurrentIndex();
        titleList.decrCurrentIndex();
    }

    /**
     * Возвращает размеры текущего изображения.
     *
     * @return объект {@link Dimension} с шириной и высотой изображения,
     *         или null если список изображений пуст
     */
    private Dimension getPhotoDimension() {
        Dimension result = null;
        if (!imageList.isEmpty()) {
            result = new Dimension(
                    imageList.getCurrent().getWidth(null),
                    imageList.getCurrent().getHeight(null));
        }
        return result;
    }

    /**
     * Преобразует массив файлов в список изображений.
     * Фильтрует файлы по расширениям (png, jpg, jpeg) и загружает их.
     *
     * @param files массив файлов для обработки
     * @return список загруженных изображений
     * @throws IOException если возникает ошибка при чтении файлов
     */
    private List<Image> getImagesFromFileArray(File[] files) throws IOException {
        String fileName;
        List<Image> result = new ArrayList<>();
        for (File f : files) {
            fileName = f.toString().toLowerCase();
            if (f.isFile() &&
                    fileName.endsWith("png") || fileName.endsWith("jpg") || fileName.endsWith("jpeg") ) {
                result.add(ImageIO.read(f)); // синхронное чтение
            }
        }
        return result;
    }

    /**
     * Преобразует массив файлов в список названий.
     * Фильтрует файлы по расширениям (png, jpg, jpeg).
     *
     * @param files массив файлов для обработки
     * @return список названий файлов изображений
     */
    private List<String> getTitlesFromFileArray(File[] files) {
        String fileName;
        List<String> result = new ArrayList<>();
        for (File f : files) {
            fileName = f.toString().toLowerCase();
            if (f.isFile() &&
                    fileName.endsWith("png") || fileName.endsWith("jpg") || fileName.endsWith("jpeg")) {
                result.add(f.toString());
            }
        }
        return result;
    }

    /**
     * Формирует отображение с информацией о текущем изображении.
     *
     * @return {@link HashMap} содержащий:
     *         <ul>
     *           <li>"w" - ширина изображения в пикселях</li>
     *           <li>"h" - высота изображения в пикселях</li>
     *           <li>"vol" - размер файла в байтах</li>
     *         </ul>
     * @throws IOException если возникает ошибка при получении размера файла
     */
    public HashMap<String, Number> getCurrentImageInfo() throws IOException {
        HashMap<String, Number> result = new HashMap<>(3);
        result.put("w", getPhotoDimension().getWidth());
        result.put("h", getPhotoDimension().getHeight());
        result.put("vol", Files.size(Paths.get(titleList.getCurrent())));

        return result;
    }
}