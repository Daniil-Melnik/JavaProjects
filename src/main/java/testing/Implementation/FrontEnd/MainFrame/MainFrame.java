package testing.Implementation.FrontEnd.MainFrame;

import testing.GBC;
import testing.Implementation.Constants;
import testing.Implementation.FrontEnd.MainFrame.MenuBars.MainMenuBar;
import testing.Implementation.FrontEnd.MainFrame.MenuBars.PhotoJPopupMenu;
import testing.Implementation.FrontEnd.MainFrame.Panels.*;
import testing.Implementation.FrontEnd.MainPanel;
import testing.Interfaces.Compositor;
import testing.Interfaces.Controller;
import testing.Interfaces.Holder;
import testing.Interfaces.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Главное окно приложения для просмотра изображений.
 * Реализует интерфейс {@link Observer} для получения уведомлений от модели.
 *
 * <p>Функциональные требования:
 * <ul>
 *   <li>Отображение текущего изображения</li>
 *   <li>Панель с лентой окружающих изображений</li>
 *   <li>Отображение имени просматриваемого файла и его метаинформации</li>
 *   <li>Кнопки пролистывания с привязкой клавиш</li>
 *   <li>Выбор директории через главное меню</li>
 * </ul>
 */
public class MainFrame extends JFrame implements Observer {

    /** Всплывающее меню для панели с изображением */
    private static PhotoJPopupMenu photoJPopupMenu;

    /** Строка-панель меню */
    private static MainMenuBar mainMenuBar;

    /** Компоновщик главной панели окна */
    private static Compositor rootCompositor;

    /** Компонент валидации блокировки меню */
    private static MenuValidation menuValidation;

    /**
     * Конструктор главного окна.
     *
     * @param controller контроллер для выполнения действий
     * @param holder модель данных
     */
    public MainFrame(Controller controller, Holder holder) {
        setLayout(new BorderLayout());
        setSize(Constants.FRAME_W, Constants.FRAME_H);
        setIconImage(new ImageIcon(
                Objects.requireNonNull(this.getClass().getResource("/photo.png"))).getImage()
        );
        setTitle("Фотосмотр");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainMenuBar = new MainMenuBar(controller, holder);
        photoJPopupMenu = new PhotoJPopupMenu(holder, controller);
        menuValidation = new MenuValidation(holder);

        this.setJMenuBar(mainMenuBar);

        rootCompositor = createRootComponent(holder, controller);
        add(rootCompositor.getComponent());

        pack();
    }

    /**
     * Обновление интерфейса по паттерну Наблюдатель.
     * Блокирует/разблокирует меню и рекурсивно обновляет всю иерархию интерфейса.
     */
    @Override
    public void update() {
        boolean mustBlock = menuValidation.mustBlockMenu();
        mainMenuBar.blockMenu(mustBlock);
        photoJPopupMenu.blockMenu(mustBlock);

        rootCompositor.refresh();
    }

    /**
     * Собирает иерархию компонентов интерфейса по паттерну Компоновщик.
     *
     * @param holder модель данных
     * @param controller контроллер для выполнения действий
     * @return корневой компонент компоновщика
     */
    private Compositor createRootComponent(Holder holder, Controller controller) {
        Compositor photoPanel;
        Compositor photoComponent;
        Compositor titlePanel;
        Compositor slidePanel;
        Compositor buttonPanel;
        Compositor mainPanel;

        buttonPanel = new ButtonPanel(controller);
        photoPanel = new PhotoPanel();
        mainPanel = new MainPanel();

        photoComponent = new PhotoComponent(false, holder, -1);
        photoPanel.addComponent(photoComponent, null);

        titlePanel = new TitlePanel(holder);
        slidePanel = new SlidePanel();
        for (int i = 0; i < 5; i++){
            Compositor smallPhotoComponent = new PhotoComponent(true, holder, i);
            Compositor smallPhotoPanel = new SmallPhotoPanel(holder, i);
            smallPhotoPanel.addComponent(smallPhotoComponent, BorderLayout.CENTER);
            slidePanel.addComponent(smallPhotoPanel, null);
        }

        photoComponent.getComponent().setComponentPopupMenu(photoJPopupMenu);

        mainPanel.addComponent(photoPanel, new GBC(0, 0, 1, 1));
        mainPanel.addComponent(titlePanel, new GBC(0, 1, 1, 1).setInsets(3, 0, 0, 0));
        mainPanel.addComponent(slidePanel, new GBC(0, 2, 1, 1));
        mainPanel.addComponent(buttonPanel, new GBC(0, 3, 1, 1));

        mainMenuBar.blockMenu(false);
        photoJPopupMenu.blockMenu(false);

        return mainPanel;
    }
}