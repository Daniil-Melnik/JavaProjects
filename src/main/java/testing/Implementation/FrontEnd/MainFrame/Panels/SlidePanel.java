package testing.Implementation.FrontEnd.MainFrame.Panels;

import testing.Implementation.Constants;
import testing.Interfaces.Compositor;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.*;

/**
 * Панель слайдов, отображающая пять миниатюр изображений, соседних с текущим.
 * Может быть контейнером в компоновщике.
 */
public class SlidePanel extends JPanel implements Compositor {

    /** Коллекция дочерних компонентов (малых панелей с миниатюрами) */
    private Set<Compositor> childs = new HashSet<>(5);

    /**
     * Конструктор панели слайдов.
     * Устанавливает сетку 1x5 и рамку.
     */
    public SlidePanel() {
        setLayout(new GridLayout(1, 5));
        setBorder(new EtchedBorder());
    }

    /**
     * Возвращает предпочтительный размер панели.
     *
     * @return размер панели
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.FRAME_W, Constants.SLIDE_PANEL_H);
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
        System.out.println(childs.size());
    }

    /**
     * Удаляет дочерний компонент.
     *
     * @param compositor компонент для удаления
     */
    @Override
    public void removeComponent(Compositor compositor) {
        this.remove(compositor.getComponent());
        childs.remove(compositor);
    }
}