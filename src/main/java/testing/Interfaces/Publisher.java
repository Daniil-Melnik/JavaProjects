package testing.Interfaces;

/**
 * Интерфейс менеджера оповещений из паттерна "Наблюдатель" (Observer).
 * Управляет подпиской и рассылкой уведомлений наблюдателям.
 */
public interface Publisher {

    /**
     * Отправляет уведомление наблюдателям об изменении состояния модели.
     *
     * @param holder модель, в которой произошло изменение
     * @param allOrZoom тип уведомления (всем или только подписчикам на масштаб)
     */
    void notify(Holder holder, boolean allOrZoom);

    /**
     * Подписывает наблюдателя на уведомления.
     *
     * @param observer наблюдатель для подписки
     * @param allOrZoom тип подписки (на все уведомления или только на масштаб)
     */
    void subscribe(Observer observer, boolean allOrZoom);

    /**
     * Отписывает наблюдателя от уведомлений.
     *
     * @param observer наблюдатель для отписки
     * @param allOrZoom тип подписки, от которой нужно отписать
     */
    void unsubscribe(Observer observer, boolean allOrZoom);
}