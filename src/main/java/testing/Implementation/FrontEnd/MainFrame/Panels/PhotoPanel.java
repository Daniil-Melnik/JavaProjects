package testing.Implementation.FrontEnd.MainFrame.Panels;

import testing.Implementation.Constants;
import testing.Interfaces.Compositor;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Панель-контейнер для отображения основного изображения.
 * Может содержать дочерний компонент с изображением.
 * Реализует интерфейс {@link Compositor} как контейнер в компоновщике.
 */
public class PhotoPanel extends JPanel implements Compositor {

    /** Коллекция дочерних компонентов */
    private Set<Compositor> childs = new HashSet<>(1);

    /**
     * Конструктор панели изображения.
     * Устанавливает макет FlowLayout и фоновый цвет.
     */
    public PhotoPanel() {
        setLayout(new FlowLayout());
        setBackground(new Color(224, 224, 224));
    }

    /**
     * Возвращает предпочтительный размер панели.
     *
     * @return размер панели
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.FRAME_W, Constants.BIG_PHOTO_PANEL_H);
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
     * Обновляет все дочерние компоненты.
     */
    @Override
    public void refresh() {
        for (Compositor c : childs) c.refresh();
    }

    /**
     * Возвращает коллекцию дочерних компонентов.
     *
     * @return множество дочерних компонентов
     */
    @Override
    public Collection<Compositor> getChilds() {
        return childs;
    }

    /**
     * Добавляет дочерний компонент.
     *
     * @param compositor компонент для добавления
     * @param constraints ограничения размещения
     */
    @Override
    public void addComponent(Compositor compositor, Object constraints) {
        this.add(compositor.getComponent(), constraints);
        childs.add(compositor);
    }

    /**
     * Удаляет дочерний компонент.
     *
     * @param compositor компонент для удаления
     */
    @Override
    public void removeComponent(Compositor compositor) {
        childs.remove(compositor);
    }
}