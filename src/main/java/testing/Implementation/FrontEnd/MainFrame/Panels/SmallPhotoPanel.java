package testing.Implementation.FrontEnd.MainFrame.Panels;

import lombok.Getter;
import lombok.Setter;
import testing.Implementation.Constants;
import testing.Interfaces.Compositor;
import testing.Interfaces.Holder;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Малая панель для отображения миниатюры изображения в слайд-панели.
 * Может быть контейнером в компоновщике, содержащим компонент с изображением.
 */
@Getter
@Setter
public class SmallPhotoPanel extends JPanel implements Compositor {

    /** Ссылка на модель данных */
    private Holder holder;

    /** Компонент с миниатюрой изображения */
    private PhotoComponent photoComponent;

    /** Индекс текущей панели в слайд-панели (0-4) */
    private int index;

    /** Коллекция дочерних компонентов */
    Set<Compositor> childs = new HashSet<>(1);

    /**
     * Конструктор малой панели.
     *
     * @param h модель данных
     * @param i индекс панели в слайд-панели
     */
    public SmallPhotoPanel(Holder h, int i) {
        holder = h;
        index = i;
        setLayout(new BorderLayout());
        setBorder(new EtchedBorder());
    }

    /**
     * Возвращает предпочтительный размер панели.
     *
     * @return размер панели
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.SMALL_PHOTO_PANEL_W, Constants.SLIDE_PANEL_H);
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
     * Обновляет панель: изменяет цвет фона, если панель соответствует текущему изображению,
     * и обновляет все дочерние компоненты.
     */
    @Override
    public void refresh() {
        setBackground(index == holder.getFlag() ? Color.GRAY : Constants.GRAY_MAIN);
        if (index == holder.getFlag()) System.out.println(index + " " + holder.getFlag());
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
        this.remove(compositor.getComponent());
        childs.remove(compositor);
    }
}