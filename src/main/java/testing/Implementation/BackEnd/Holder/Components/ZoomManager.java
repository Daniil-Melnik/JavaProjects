package testing.Implementation.BackEnd.Holder.Components;

import lombok.Getter;
import lombok.Setter;
import testing.Implementation.Constants;

/**
 * Компонент модели, управляющий масштабированием изображения.
 * Определяет границы безопасного изменения коэффициента масштаба.
 */
public class ZoomManager {

    /** Текущий коэффициент масштабирования */
    @Getter
    @Setter
    private double zoom = Constants.NORMAL_SCALE;

    /**
     * Устанавливает нормальный коэффициент масштабирования (1.0).
     */
    public void setNormalZoom(){
        zoom = Constants.NORMAL_SCALE;
    }

    /**
     * Безопасно увеличивает коэффициент масштабирования.
     * Увеличение не происходит, если результат превышает максимально допустимое значение.
     *
     * @param addZoom значение, на которое нужно увеличить масштаб
     */
    public void addZoom(double addZoom){
        if (zoom + addZoom < Constants.MAX_SCALE) zoom += addZoom;
    }

    /**
     * Безопасно уменьшает коэффициент масштабирования.
     * Уменьшение не происходит, если результат становится меньше минимально допустимого значения.
     *
     * @param subZoom значение, на которое нужно уменьшить масштаб
     */
    public void subZoom(double subZoom){
        if (zoom - subZoom > Constants.MIN_SCALE) zoom -= subZoom;
    }
}