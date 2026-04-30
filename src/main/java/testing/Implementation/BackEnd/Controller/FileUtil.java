package testing.Implementation.BackEnd.Controller;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Утилитарный класс для работы с файлами изображений.
 * Предоставляет методы для чтения изображений и получения их путей из массива файлов.
 */
public class FileUtil {

    /**
     * Извлекает и загружает изображения из массива файлов.
     * Фильтрует только файлы с расширениями png, jpg, jpeg.
     *
     * @param files массив файлов для обработки
     * @return список загруженных изображений {@link Image}
     * @throws IOException если возникает ошибка при чтении файла изображения
     */
    public static java.util.List<Image> getImagesFromFileArray(File[] files) throws IOException {
        String fileName;
        java.util.List<Image> result = new ArrayList<>();
        for (File f : files){
            fileName = f.toString().toLowerCase();
            if (f.isFile() &&
                    fileName.endsWith("png") || fileName.endsWith("jpg") || fileName.endsWith("jpeg")) {
                result.add(ImageIO.read(f));
            }
        }
        return result;
    }

    /**
     * Извлекает пути к файлам изображений из массива файлов.
     * Фильтрует только файлы с расширениями png, jpg, jpeg.
     *
     * @param files массив файлов для обработки
     * @return список строк с путями к файлам изображений
     */
    public static java.util.List<String> getTitlesFromFileArray(File[] files){
        String fileName;
        List<String> result = new ArrayList<>();
        for (File f : files){
            fileName = f.toString().toLowerCase();
            if (f.isFile() &&
                    fileName.endsWith("png") || fileName.endsWith("jpg") || fileName.endsWith("jpeg")) {
                result.add(f.toString());
            }
        }
        return result;
    }
}