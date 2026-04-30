package testing;

import testing.Implementation.BackEnd.Holder.SimpleHolder;
import testing.Implementation.BackEnd.Controller.SimpleController;
import testing.Implementation.BackEnd.SimplePublisher;
import testing.Implementation.Constants;
import testing.Implementation.FrontEnd.MainFrame.MainFrame;
import testing.Implementation.FrontEnd.ZoomFrame.ZoomFrame;
import testing.Interfaces.Holder;
import testing.Interfaces.Controller;
import testing.Interfaces.Publisher;

import java.awt.*;

/**
 * Главный класс приложения для запуска программы просмотра изображений.
 * Инициализирует компоненты MVC, создаёт окна и запускает приложение.
 */
public class Main {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String ... args){
        Publisher publisher = new SimplePublisher();
        Holder holder = new SimpleHolder(publisher);
        Controller controller = new SimpleController(holder);
        MainFrame mainFrame = new MainFrame(controller, holder);
        ZoomFrame zoomFrame = ZoomFrame.getInstance(holder, controller);

        publisher.subscribe(mainFrame, Constants.ALL);
        publisher.subscribe(zoomFrame, Constants.ZOOM);

        EventQueue.invokeLater(() -> mainFrame.setVisible(true));
    }
}