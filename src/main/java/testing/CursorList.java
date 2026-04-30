package testing;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, описывающий список с курсором на основе списочного массива.
 * Поддерживает навигацию по элементам и получение пяти элементов, соседних с текущим.
 *
 * @param <T> тип элементов, хранящихся в списке
 */
public class CursorList<T> extends ArrayList<T> {

    /** Текущая позиция курсора */
    private int currentIndex = 0;

    /** Указатель на курсор в рамках пяти соседних от курсора элементов */
    @Getter
    private int flag = 0;

    /**
     * Конструктор по умолчанию.
     */
    public CursorList(){
        super();
    }

    /**
     * Конструктор с указанием начальной ёмкости.
     *
     * @param cap начальная ёмкость списка
     */
    public CursorList(int cap){
        super(cap);
    }

    /**
     * Возвращает позицию курсора в рамках пяти соседних элементов.
     *
     * @return индекс от 0 до 4, указывающий позицию курсора
     */
    public int getFiveElementsFlag(){
        int f = 0;
        if (this.size() >= 5){
            if (currentIndex == 0) f = 0;
            else if (currentIndex == 1) f = 1;
            else if (currentIndex == this.size() - 2) f = 3;
            else if (currentIndex == this.size() - 1) f = 4;
            else f = 2;
        } else f = currentIndex;

        return f;
    }

    /**
     * Возвращает текущий элемент по позиции курсора.
     *
     * @return текущий элемент или null, если список пуст или курсор вне границ
     */
    public T getCurrent(){
        T result = null;
        if (!this.isEmpty() && (currentIndex < this.size())){
            result = this.get(currentIndex);
        }
        return result;
    }

    /**
     * Увеличивает курсор на 1, если это возможно.
     */
    public void incrCurrentIndex(){
        if (currentIndex < this.size()) currentIndex++;
    }

    /**
     * Уменьшает курсор на 1, если это возможно.
     */
    public void decrCurrentIndex(){
        if (currentIndex > 0) currentIndex--;
    }

    /**
     * Устанавливает курсор на указанную позицию.
     *
     * @param cI новая позиция курсора (должна быть в границах списка)
     */
    public void setCurrentIndex(int cI){
        if ((cI >=0) && (cI < this.size())){
            currentIndex = cI;
        }
    }

    /**
     * Конструктор на основе существующего списка.
     *
     * @param list существующий список для инициализации
     */
    public CursorList(List<T> list){
        super(list);
    }

    /**
     * Возвращает пять элементов, соседних с текущим.
     * В зависимости от позиции курсора возвращает различные подсписки.
     *
     * @return список из пяти (или менее) соседних элементов
     */
    public List<T> getFiveNearElements(){
        int nearCapacity = 5;
        List<T> result = new ArrayList<>(nearCapacity);

        if ((currentIndex - nearCapacity / 2 >= 0) && (currentIndex + nearCapacity / 2 <= this.size() - 1)){
            result = this.subList(currentIndex - 2, currentIndex + 2 + 1);
            flag = 2;
        }
        else if((currentIndex == 0) || (currentIndex == 1)){
            if (this.size() < nearCapacity){
                result = this;
            }
            else {
                result = this.subList(0, nearCapacity);
            }
            flag = (currentIndex == 0) ? 0 : 1;
        }
        else if((currentIndex == this.size() - 1) || (currentIndex == this.size() - 2)){
            if (this.size() < nearCapacity){
                result = this;
            }
            else {
                result = this.subList(this.size() - nearCapacity, this.size());
            }
            flag = (this.size() == 3 && currentIndex == 2) ? 2 : (currentIndex == this.size() - 1) ?
                    nearCapacity - 1:
                    nearCapacity - 2;
        }
        return result;
    }
}