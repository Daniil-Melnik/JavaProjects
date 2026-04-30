package testing.Implementation.BackEnd.Holder.Components;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Компонент модели, предоставляющий методы для вычисления характеристик изображений.
 * Позволяет получить размеры и объём файла изображения.
 */
public class ImageInfoCalculator {

    /**
     * Возвращает размеры изображения.
     *
     * @param image изображение для анализа
     * @return объект {@link Dimension} с шириной и высотой изображения,
     *         или null если изображение не задано
     */
    private static Dimension getPhotoDimension(Image image){
        Dimension result = null;
        if (image != null){
            result = new Dimension(
                    image.getWidth(null),
                    image.getHeight(null));
        }
        return result;
    }

    /**
     * Формирует отображение с информацией об изображении по указанному пути.
     * Включает ширину ("w"), высоту ("h") и объём в байтах ("vol").
     *
     * @param title путь к файлу изображения
     * @return отображение с информацией об изображении
     * @throws IOException если возникает ошибка при чтении файла
     */
    public static HashMap<String, Number> getImageInfo(String title) throws IOException {
        HashMap<String, Number> result = new HashMap<>(3);
        Image image = ImageIO.read(new File(title));
        if (image != null) {
            result.put("w", getPhotoDimension(image).getWidth());
            result.put("h", getPhotoDimension(image).getHeight());
            result.put("vol", Files.size(Paths.get(title)));
        }
        return result;
    }
}