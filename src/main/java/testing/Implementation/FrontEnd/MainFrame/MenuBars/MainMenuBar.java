package testing.Implementation.FrontEnd.MainFrame.MenuBars;

import testing.Interfaces.Controller;
import testing.Interfaces.Holder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * Строка главного меню приложения.
 * Содержит меню "Файл" и "Текущее" с соответствующими пунктами.
 */
public class MainMenuBar extends JMenuBar {

    private JMenu mainMenu;
    private JMenu photoMenu;

    private JMenuItem setFolderItem;
    private JMenuItem exitItem;
    private final JMenuItem separateWindowItem;
    private final JMenuItem aboutImageItem;

    /**
     * Конструктор главного меню.
     *
     * @param controller контроллер для выполнения действий
     * @param holder модель данных
     */
    public MainMenuBar(Controller controller, Holder holder) {

        mainMenu = new JMenu("Файл (F)");
        photoMenu = new JMenu("Текущее (T)");

        mainMenu.setMnemonic(KeyEvent.VK_F);
        photoMenu.setMnemonic(KeyEvent.VK_T);

        setFolderItem = new JMenuItem("Папка (D)", 'D');
        exitItem = new JMenuItem("Закрыть (C)", 'C');
        separateWindowItem = MenuItemsFabric.getSeparateWindowItem(holder, controller);
        aboutImageItem = MenuItemsFabric.getInfoMenuItem(holder);

        mainMenu.add(setFolderItem);
        mainMenu.addSeparator();
        mainMenu.add(exitItem);

        photoMenu.add(separateWindowItem);
        photoMenu.addSeparator();
        photoMenu.add(aboutImageItem);

        exitItem.addActionListener(e -> System.exit(0));

        setFolderItem.addActionListener((e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    controller.setCurrentDirectory(fileChooser.getSelectedFile());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Ошибка загрузки файлов",
                            "Проблема с директорией",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        add(mainMenu);
        add(photoMenu);
    }

    /**
     * Блокирует или разблокирует некоторые пункты меню.
     *
     * @param t true - блокировать, false - разблокировать
     */
    public void blockMenu(boolean t) {
        separateWindowItem.setEnabled(t);
        aboutImageItem.setEnabled(t);
    }
}