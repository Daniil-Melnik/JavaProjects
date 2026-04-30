package testing.Implementation.FrontEnd;

import testing.Interfaces.Compositor;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Главная панель, которая служит корневым контейнером для всех компонентов интерфейса.
 * Может применяться как контейнер в компоновщике в обоих представлениях.
 */
public class MainPanel extends JPanel implements Compositor {

    /** Коллекция дочерних компонентов */
    private Set<Compositor> childs = new HashSet<>(4);

    /**
     * Конструктор главной панели.
     * Устанавливает менеджер компоновки GridBagLayout.
     */
    public MainPanel(){
        setLayout(new GridBagLayout());
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
        this.remove(compositor.getComponent());
    }
}