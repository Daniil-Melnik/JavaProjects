package testing.Implementation.BackEnd;

import testing.Interfaces.Holder;
import testing.Interfaces.Observer;
import testing.Interfaces.Publisher;

import java.util.HashSet;
import java.util.Set;

/**
 * Менеджер уведомлений по паттерну Наблюдатель.
 * Управляет подпиской и оповещением наблюдателей об изменениях в модели.
 */
public class SimplePublisher implements Publisher {

    /** Множество всех подписчиков, получающих все типы уведомлений */
    Set<Observer> allObservers = new HashSet<>(2);

    /** Множество подписчиков, получающих уведомления только об изменении масштаба */
    Set<Observer> zoomObservers = new HashSet<>(1);

    /**
     * Оповещает наблюдателей об изменении состояния модели.
     *
     * @param holder модель, в которой произошло изменение
     * @param allOrZoom тип оповещения:
     *                  Constants.ALL - оповещаются все подписчики,
     *                  Constants.ZOOM - оповещаются только подписчики на масштаб
     */
    @Override
    public void notify(Holder holder, boolean allOrZoom) {
        Set<Observer> set = allOrZoom ? allObservers : zoomObservers;
        for (Observer o : set) o.update();
    }

    /**
     * Подписывает наблюдателя на уведомления.
     *
     * @param observer наблюдатель для подписки
     * @param allOrZoom тип подписки:
     *                  Constants.ALL - на все уведомления,
     *                  Constants.ZOOM - на уведомления об изменении масштаба
     */
    @Override
    public void subscribe(Observer observer, boolean allOrZoom) {
        if (allOrZoom){
            allObservers.add(observer);
        } else {
            allObservers.add(observer);
            zoomObservers.add(observer);
        }
    }

    /**
     * Отписывает наблюдателя от уведомлений.
     *
     * @param observer наблюдатель для отписки
     * @param allOrZoom тип подписки, от которой нужно отписать
     */
    @Override
    public void unsubscribe(Observer observer, boolean allOrZoom) {
        if (allOrZoom){
            allObservers.remove(observer);
        } else{
            zoomObservers.remove(observer);
            allObservers.remove(observer);
        }
    }
}