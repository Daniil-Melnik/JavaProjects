package testing;

import java.awt.*;

/**
 * Класс-шаблон для описания расположения компонента в сеточно-контейнерной компоновке (GridBagLayout).
 * Упрощает задание параметров размещения компонентов в GridBagLayout.
 */
public class GBC extends GridBagConstraints {

    /**
     * Конструктор по координатам левого верхнего угла.
     *
     * @param gx столбец левого верхнего угла
     * @param gy строка левого верхнего угла
     */
    public GBC(int gx, int gy){
        this.gridx = gx;
        this.gridy = gy;
    }

    /**
     * Конструктор с указанием ширины и высоты.
     *
     * @param gx столбец левого верхнего угла
     * @param gy строка левого верхнего угла
     * @param w ширина компонента в ячейках
     * @param h высота компонента в ячейках
     */
    public GBC(int gx, int gy, int w, int h){
        this.gridx = gx;
        this.gridy = gy;
        this.gridwidth = w;
        this.gridheight = h;
    }

    /**
     * Устанавливает привязку компонента при наличии свободного места в ячейке.
     *
     * @param a привязка (например, GridBagConstraints.CENTER)
     * @return текущий объект GBC для цепочечных вызовов
     */
    public GBC setAnchor(int a){
        this.anchor = a;
        return this;
    }

    /**
     * Устанавливает, будет ли компонент растягиваться на всё доступное пространство ячейки.
     *
     * @param f тип заполнения (например, GridBagConstraints.BOTH)
     * @return текущий объект GBC для цепочечных вызовов
     */
    public GBC setFill(int f){
        this.fill = f;
        return this;
    }

    /**
     * Устанавливает процент занимаемого компонентом свободного места.
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
     * Устанавливает одинаковые внутренние отступы со всех сторон.
     *
     * @param dist величина отступа во всех направлениях
     * @return текущий объект GBC для цепочечных вызовов
     */
    public GBC setInsets(int dist){
        this.insets = new Insets(dist, dist, dist, dist);
        return this;
    }

    /**
     * Устанавливает внутренние отступы с индивидуальными значениями для каждой стороны.
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
     * Устанавливает дополнительный минимальный размер ячейки (внутренние поля).
     *
     * @param ix внутренний отступ по горизонтали
     * @param iy внутренний отступ по вертикали
     * @return текущий объект GBC для цепочечных вызовов
     */
    public GBC setIpad(int ix, int iy){
        this.ipadx = ix;
        this.ipady = iy;
        return this;
    }
}