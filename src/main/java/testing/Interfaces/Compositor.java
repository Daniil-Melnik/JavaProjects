package testing.Interfaces;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

/**
 * Интерфейс для описания компонента дерева графических панелей в интерфейсе
 * по паттерну проектирования "Компоновщик" (Composite).
 * Позволяет единообразно работать с простыми и составными компонентами интерфейса.
 */
public interface Compositor {

    /**
     * Возвращает Swing-компонент для отображения.
     *
     * @return Swing-компонент
     */
    JComponent getComponent();

    /**
     * Возвращает имя экземпляра компонента.
     *
     * @return имя компонента
     */
    String getName();

    /**
     * Обновляет графический компонент.
     * Вызывается при изменении данных в модели.
     */
    void refresh();

    /**
     * Возвращает коллекцию дочерних компонентов.
     * По умолчанию возвращается пустой список для листовых узлов.
     *
     * @return коллекция дочерних компонентов
     */
    default Collection<Compositor> getChilds() {
        return List.of();
    }

    /**
     * Добавляет дочерний компонент.
     *
     * @param compositor дочерний компонент для добавления
     * @param constraints ограничения размещения компонента
     */
    default void addComponent(Compositor compositor, Object constraints) {
    }

    /**
     * Удаляет дочерний компонент.
     *
     * @param compositor компонент для удаления
     */
    default void removeComponent(Compositor compositor) {
    }
}