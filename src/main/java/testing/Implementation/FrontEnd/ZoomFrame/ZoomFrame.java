package testing.Implementation.FrontEnd.ZoomFrame;

import testing.GBC;
import testing.Implementation.Constants;
import testing.Implementation.FrontEnd.MainPanel;
import testing.Implementation.FrontEnd.ZoomFrame.Panels.ButtonPanel;
import testing.Implementation.FrontEnd.ZoomFrame.Panels.PhotoPanel;
import testing.Interfaces.Compositor;
import testing.Interfaces.Controller;
import testing.Interfaces.Holder;
import testing.Interfaces.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Отдельное окно для просмотра изображения с возможностью масштабирования.
 * Реализует паттерн Одиночка и интерфейс {@link Observer} для получения уведомлений от модели.
 */
public class ZoomFrame extends JFrame implements Observer {

    /** Название текущего изображения */
    private String photoTitle = "";

    /** Ссылка на модель данных */
    private static Holder holder;

    /** Ссылка на контроллер */
    private static Controller controller;

    /** Единственный экземпляр окна масштабирования (паттерн Одиночка) */
    private static ZoomFrame instance = null;

    /** Корневой компонент компоновщика */
    private Compositor rootComponent;

    /**
     * Возвращает единственный экземпляр окна масштабирования.
     *
     * @param h модель данных
     * @param c контроллер
     * @return экземпляр окна масштабирования
     */
    public static ZoomFrame getInstance(Holder h, Controller c){
        holder = h;
        controller = c;
        if (instance == null){
            instance = new ZoomFrame();
        }
        return instance;
    }

    /**
     * Приватный конструктор для реализации паттерна Одиночка.
     */
    private ZoomFrame(){
        setLayout(new BorderLayout());
        setSize(Constants.SCROLL_FRAME_W, Constants.SCROLL_FRAME_H);
        setIconImage(new ImageIcon(
                Objects.requireNonNull(
                        this.getClass().getResource("/photo.png"))).getImage());
        setTitle("Фотосмотр - " + photoTitle);
        rootComponent = createRootCompositor();
        add(rootComponent.getComponent(), BorderLayout.CENTER);
        pack();
    }

    /**
     * Собирает иерархию компонентов окна по паттерну Компоновщик.
     *
     * @return корневой компонент компоновщика
     */
    private Compositor createRootCompositor(){
        Compositor rootComponent = new MainPanel();
        Compositor photoPanel = new PhotoPanel(controller, holder);
        Compositor buttonPanel = new ButtonPanel(holder, controller);
        rootComponent.addComponent(photoPanel, new GBC(0, 0, 1, 1));
        rootComponent.addComponent(buttonPanel, new GBC(0, 1, 1, 1));

        return rootComponent;
    }

    /**
     * Обновляет окно при получении уведомления от модели.
     * Обновляет заголовок окна и рекурсивно обновляет все компоненты.
     */
    @Override
    public void update() {
        setTitle(holder.getCurrentImageName());
        rootComponent.refresh();
    }
}