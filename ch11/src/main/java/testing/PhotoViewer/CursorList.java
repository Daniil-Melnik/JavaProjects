package testing.PhotoViewer;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий список с курсором (указателем текущей позиции).
 * Расширяет {@link ArrayList} и добавляет функциональность навигации по элементам.
 *
 * @param <T> тип элементов, хранящихся в списке
 */
public class CursorList<T> extends ArrayList<T> {

    /** Текущая позиция курсора в списке */
    private int currentIndex = 0;

    /**
     * Указатель на позицию курсора в рамках пяти соседних элементов.
     * Используется для определения, какой из пяти отображаемых элементов является текущим.
     */
    @Getter
    private int flag = 0;

    /**
     * Конструктор по умолчанию.
     * Создает пустой список с курсором в позиции 0.
     */
    public CursorList() {
        super();
    }

    /**
     * Конструктор с заданной начальной емкостью.
     *
     * @param cap начальная емкость списка
     */
    public CursorList(int cap) {
        super(cap);
    }

    /**
     * Конструктор, создающий список на основе существующей коллекции.
     *
     * @param list список, элементы которого будут добавлены в новый CursorList
     */
    public CursorList(List<T> list) {
        super(list);
    }

    /**
     * Возвращает текущий элемент списка, на который указывает курсор.
     *
     * @return элемент в позиции курсора, или null если список пуст или курсор вне допустимого диапазона
     */
    public T getCurrent() {
        T result = null;
        if (!this.isEmpty() && (currentIndex < this.size())) {
            result = this.get(currentIndex);
        }
        return result;
    }

    /**
     * Увеличивает значение курсора на 1, если это не выходит за пределы списка.
     */
    public void incrCurrentIndex() {
        if (currentIndex < this.size()) currentIndex++;
    }

    /**
     * Уменьшает значение курсора на 1, если это не выходит за пределы списка.
     */
    public void decrCurrentIndex() {
        if (currentIndex > 0) currentIndex--;
    }

    /**
     * Устанавливает курсор в указанную позицию.
     *
     * @param cI новая позиция курсора (должна быть в диапазоне от 0 до size()-1)
     */
    public void setCurrentIndex(int cI) {
        if ((cI >= 0) && (cI < this.size())) {
            currentIndex = cI;
        }
    }

    /**
     * Возвращает список из пяти элементов, соседних с текущей позицией курсора.
     * Метод определяет, как сгруппировать элементы вокруг курсора, и корректно
     * обрабатывает краевые случаи (начало и конец списка).
     *
     * @return список из 5 элементов (или меньше, если общий размер списка меньше 5),
     *         окружающих текущую позицию курсора
     */
    public List<T> getFiveNearElements() {
        int nearCapacity = 5;
        List<T> result = new ArrayList<>(nearCapacity);

        // Случай 1: курсор находится в середине (есть по 2 элемента с каждой стороны)
        if ((currentIndex - nearCapacity / 2 >= 0) && (currentIndex + nearCapacity / 2 <= this.size() - 1)) {
            result = this.subList(currentIndex - 2, currentIndex + 2 + 1);
            flag = 2; // курсор в списке соседей - середина
        }
        // Случай 2: курсор в начале списка (позиция 0 или 1)
        else if ((currentIndex == 0) || (currentIndex == 1)) {
            if (this.size() < nearCapacity) {
                result = this; // в списке меньше пяти элементов
            } else {
                result = this.subList(0, nearCapacity); // берем первые пять элементов
            }
            flag = (currentIndex == 0) ? 0 : 1;
        }
        // Случай 3: курсор в конце списка (последняя или предпоследняя позиция)
        else if ((currentIndex == this.size() - 1) || (currentIndex == this.size() - 2)) {
            if (this.size() < nearCapacity) {
                result = this;
            } else {
                result = this.subList(this.size() - nearCapacity, this.size()); // берем последние пять элементов
            }

            // Сложная логика определения флага для краевых случаев
            flag = (this.size() == 3 && currentIndex == 2) ? 2 :
                    (currentIndex == this.size() - 1) ? nearCapacity - 1 : nearCapacity - 2;
        }
        return result;
    }
}
