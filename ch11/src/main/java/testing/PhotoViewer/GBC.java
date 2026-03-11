package testing.PhotoViewer;

import java.awt.*;

/**
 * Вспомогательный класс для упрощения работы с GridBagLayout.
 * Наследуется от {@link GridBagConstraints} и предоставляет методы
 * для удобной настройки параметров размещения компонентов.
 */
public class GBC extends GridBagConstraints {

    /**
     * Создает объект с указанными координатами ячейки.
     *
     * @param gx координата по горизонтали (столбец)
     * @param gy координата по вертикали (строка)
     */
    public GBC(int gx, int gy){
        this.gridx = gx;
        this.gridy = gy;
    }

    /**
     * Создает объект с указанными координатами и размерами ячейки.
     *
     * @param gx координата по горизонтали (столбец)
     * @param gy координата по вертикали (строка)
     * @param w количество занимаемых столбцов
     * @param h количество занимаемых строк
     */
    public GBC(int gx, int gy, int w, int h){
        this.gridx = gx;
        this.gridy = gy;
        this.gridwidth = w;
        this.gridheight = h;
    }

    /**
     * Устанавливает привязку компонента в ячейке.
     * Определяет, где будет расположен компонент, если он меньше ячейки.
     *
     * @param a значение привязки (например, {@link GridBagConstraints#CENTER})
     * @return текущий объект GBC для цепочечных вызовов
     */
    public GBC setAnchor(int a){
        this.anchor = a;
        return this;
    }

    /**
     * Устанавливает режим заполнения ячейки компонентом.
     * Определяет, будет ли компонент растягиваться, чтобы заполнить ячейку.
     *
     * @param f режим заполнения (например, {@link GridBagConstraints#BOTH})
     * @return текущий объект GBC для цепочечных вызовов
     */
    public GBC setFill(int f){
        this.fill = f;
        return this;
    }

    /**
     * Устанавливает весовые коэффициенты для распределения свободного пространства.
     * Определяют, какая часть лишнего пространства будет выделена компоненту
     * при растяжении контейнера.
     *
     * @param wx вес по горизонтали
     * @param wy вес по вертикали
     * @return текущий объект GBC для цепочечных вызовов
     */
    public GBC setWeight(double wx, double wy){
        this.weightx = wx;
        this.weighty = wy;
        return this;
    }

    /**
     * Устанавливает одинаковые отступы со всех сторон.
     * Определяет минимальное расстояние от границ ячейки до компонента.
     *
     * @param dist отступ в пикселях со всех сторон
     * @return текущий объект GBC для цепочечных вызовов
     */
    public GBC setInsets(int dist){
        this.insets = new Insets(dist, dist, dist, dist);
        return this;
    }

    /**
     * Устанавливает индивидуальные отступы для каждой стороны.
     *
     * @param t отступ сверху
     * @param b отступ снизу
     * @param l отступ слева
     * @param r отступ справа
     * @return текущий объект GBC для цепочечных вызовов
     */
    public GBC setInsets(int t, int b, int l, int r){
        this.insets = new Insets(t, l, b, r);
        return this;
    }

    /**
     * Устанавливает минимальные внутренние отступы компонента.
     * Увеличивает предпочтительный размер компонента на указанные значения.
     *
     * @param ix дополнительная ширина
     * @param iy дополнительная высота
     * @return текущий объект GBC для цепочечных вызовов
     */
    public GBC setIpad(int ix, int iy){
        this.ipadx = ix;
        this.ipady = iy;
        return this;
    }
}