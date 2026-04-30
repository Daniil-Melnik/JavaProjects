package testing.Implementation.FrontEnd.MainFrame.MenuBars;

import testing.Interfaces.Controller;
import testing.Interfaces.Holder;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * Фабрика для создания пунктов меню.
 * Поставляет стандартизированные пункты меню для использования в различных меню приложения.
 */
public class MenuItemsFabric {

    /**
     * Создаёт пункт меню "В отдельном окне".
     *
     * @param holder модель данных
     * @param controller контроллер для создания окна масштабирования
     * @return настроенный пункт меню
     */
    public static JMenuItem getSeparateWindowItem(Holder holder, Controller controller) {
        JMenuItem item = new JMenuItem("В отдельном окне (S)", 'S');
        item.addActionListener(e -> {
            Image currImg = holder.getCurrentImage();
            String currTitle = holder.getCurrentImageName();
            if (currImg != null) {
                controller.createZoomFrame();
            }

        });
        return item;
    }

    /**
     * Создаёт пункт меню "О фото" для отображения информации об изображении.
     *
     * @param holder модель данных
     * @return настроенный пункт меню
     */
    public static JMenuItem getInfoMenuItem(Holder holder) {
        JMenuItem item = new JMenuItem("О фото (A)", 'A');
        item.addActionListener(e -> {
            try {
                HashMap<String, Number> imageInfo = (HashMap<String, Number>) holder.getImageInfo(holder.getCurrentImageName());
                JOptionPane.showMessageDialog(null,
                        String.format("%.1f x %.1f пикселей \n %.2f КБайт",
                                imageInfo.get("w").doubleValue(),
                                imageInfo.get("h").doubleValue(),
                                imageInfo.get("vol").doubleValue() / 1024),
                        "Размер и объём",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IOException ex) {
                System.err.println("Ошибка файла");
            }
        });
        return item;
    }
}