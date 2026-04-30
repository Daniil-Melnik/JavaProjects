package testing.Implementation.FrontEnd.MainFrame.MenuBars;

import testing.Interfaces.Controller;
import testing.Interfaces.Holder;

import javax.swing.*;

/**
 * Всплывающее контекстное меню для панели с изображением.
 * Появляется при нажатии правой кнопки мыши на изображении.
 */
public class PhotoJPopupMenu extends JPopupMenu {

    private JMenuItem separateWindowItem;
    private JMenuItem infoItem;

    /**
     * Конструктор всплывающего меню.
     *
     * @param holder модель данных
     * @param controller контроллер для выполнения действий
     */
    public PhotoJPopupMenu(Holder holder, Controller controller) {

        separateWindowItem = MenuItemsFabric.getSeparateWindowItem(holder, controller);
        infoItem = MenuItemsFabric.getInfoMenuItem(holder);

        add(separateWindowItem);
        addSeparator();
        add(infoItem);
    }

    /**
     * Блокирует или разблокирует пункты меню.
     *
     * @param t true - блокировать, false - разблокировать
     */
    public void blockMenu(boolean t) {
        separateWindowItem.setEnabled(t);
        infoItem.setEnabled(t);
    }
}