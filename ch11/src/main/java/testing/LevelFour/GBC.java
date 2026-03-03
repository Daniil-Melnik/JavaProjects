package testing.LevelFour;

import java.awt.*;

// класс-шаблон для описания расположения компонента в сеточно-контейнерной компоновке (GridBagLayout)

public class GBC extends GridBagConstraints {
    public GBC(int gx, int gy){ // конструктор по координатам левого верхнего угла
        this.gridx = gx; // столбец ЛВ-угла
        this.gridy = gy; // строка
    }

    public GBC(int gx, int gy, int w, int h){ // конструктор сшириной и высотой
        this.gridx = gx;
        this.gridy = gy;
        this.gridwidth = w;
        this.gridheight = h;
    }

    public GBC setAnchor(int a){ // установка привязки компонента, если вокруг него в ячейке есть свободное место
                                // север, запад, восток, запад, промежутчные
        this.anchor = a;
        return this;
    }

    public GBC setFill(int f){ // растягиваться ли компоненту на пространство в ячейке
        this.fill = f;
        return this;
    }

    public GBC setWeight(double wx, double wy){ // установка процента занимаемого компонентом свободного места
                                                // в ячейке
        this.weightx = wx;
        this.weighty = wy;
        return this;
    }

    public GBC setInsets(int dist){
        this.insets = new Insets(dist, dist, dist, dist); // внутренние отступы от границ ячейки до компонента (padding)
                                                          // во все стороны одно значение
        return this;
    }

    public GBC setInsets(int t, int b, int l, int r){ // padding со значениями для каждой стороны в отдельности
        this.insets = new Insets(t, l, b, r);
        return this;
    }

    public GBC setIpad(int ix, int iy){ // дополнительный минимальный размер ячейки (margin)
        this.ipadx = ix;
        this.ipady = iy;
        return this;
    }
}
