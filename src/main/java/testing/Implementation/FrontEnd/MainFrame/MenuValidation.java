package testing.Implementation.FrontEnd.MainFrame;

import lombok.AllArgsConstructor;
import testing.Interfaces.Holder;

/**
 * Класс, отвечающий за контроль необходимости блокировки меню.
 * Определяет, нужно ли блокировать пункты меню в зависимости от состояния модели.
 */
@AllArgsConstructor
public class MenuValidation {

    /** Ссылка на модель данных */
    private Holder holder;

    /**
     * Определяет, нужно ли блокировать меню.
     *
     * @return true если меню должно быть заблокировано (список изображений пуст),
     *         false если меню активно
     */
    public boolean mustBlockMenu(){
        return !holder.getImages().isEmpty();
    }
}