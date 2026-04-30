package testing.Implementation.FrontEnd.MainFrame.Panels;

import testing.Implementation.Constants;
import testing.Interfaces.Compositor;
import testing.Interfaces.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * Панель с кнопками навигации.
 * Содержит кнопки "Назад" и "Далее", а также обрабатывает нажатия клавиш-стрелок.
 * Реализует интерфейс {@link Compositor} для использования в компоновщике.
 */
public class ButtonPanel extends JPanel implements Compositor {

    /**
     * Конструктор панели кнопок.
     *
     * @param controller контроллер для выполнения навигации
     */
    public ButtonPanel(Controller controller) {
        setLayout(new GridLayout(1, 2));

        NextBackAction nextAction = new NextBackAction(Constants.NEXT_PHOTO, controller);
        NextBackAction backAction = new NextBackAction(Constants.PREVIOUS_PHOTO, controller);

        JButton nextBtn = new NextBackButton(nextAction);
        JButton backBtn = new NextBackButton(backAction);

        this.getActionMap().put("next", nextAction);
        this.getActionMap().put("back", backAction);

        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "next");
        this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "back");

        add(backBtn);
        add(nextBtn);
    }

    /**
     * Возвращает Swing-компонент панели.
     *
     * @return текущая панель
     */
    @Override
    public JComponent getComponent() {
        return this;
    }

    /**
     * Обновляет состояние панели (без действий, так как кнопки не требуют обновления).
     */
    @Override
    public void refresh() {

    }

    /**
     * Действие для навигации (вперёд/назад).
     * Используется как для кнопок, так и для клавиатурных сокращений.
     */
    private class NextBackAction extends AbstractAction {

        /** Тип действия: true - вперёд, false - назад */
        private boolean type;

        private Controller controller;

        /**
         * Конструктор действия навигации.
         *
         * @param t тип действия (true - вперёд, false - назад)
         * @param controller контроллер для выполнения навигации
         */
        public NextBackAction(boolean t, Controller controller) {
                        String iconSource = t ? "/next.png" : "/back.png";
            String name = t ? "далее" : "назад";
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(NextBackAction.class.getResource(iconSource)));

            super("", icon);

            type = t;
            this.controller = controller;

            putValue(Action.SHORT_DESCRIPTION, name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (type) {
                controller.nextPhoto();
            } else {
                controller.backPhoto();
            }
        }
    }

    /**
     * Кнопка навигации с предустановленным стилем.
     */
    private class NextBackButton extends JButton {

        /**
         * Конструктор кнопки на основе действия.
         *
         * @param action действие, выполняемое при нажатии
         */
        public NextBackButton(NextBackAction action) {
            super(action);
            setBackground(Constants.GRAY_BACKGROUND);
            setBorder(null);
        }
    }

    /**
     * Возвращает предпочтительный размер панели.
     *
     * @return размер панели
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.FRAME_W, Constants.BUTTON_PANEL_H);
    }
}