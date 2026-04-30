package testing.Implementation;

import java.awt.*;

/**
 * Класс, содержащий константы, используемые во всём приложении.
 * Определяет размеры окон и панелей, цвета, шаги масштабирования и другие настройки.
 */
public class Constants {

    /** Ширина главного окна */
    public static final int FRAME_W = 1000;

    /** Высота главного окна */
    public static final int FRAME_H = 730;

    /** Высота панели с большим изображением */
    public static final int BIG_PHOTO_PANEL_H = 500;

    /** Высота панели с названием */
    public static final int TITLE_PANEL_H = 24;

    /** Высота панели слайдов */
    public static final int SLIDE_PANEL_H = 150;

    /** Высота панели кнопок */
    public static final int BUTTON_PANEL_H = 60;

    /** Ширина малого изображения (миниатюры) */
    public static final int SMALL_PHOTO_W = 180;

    /** Высота малого изображения (миниатюры) */
    public static final int SMALL_PHOTO_H = 140;

    /** Ширина панели малого изображения */
    public static final int SMALL_PHOTO_PANEL_W = 196;

    /** Высота панели малого изображения */
    public static final int SMALL_PHOTO_PANEL_H = 144;

    /** Основной цвет фона */
    public static final Color GRAY_BACKGROUND = new Color(238, 238, 238);

    /** Дополнительный цвет фона */
    public static final Color GRAY_MAIN = new Color(224,224,224);

    /** Флаг для перехода к следующему фото */
    public static final boolean NEXT_PHOTO = true;

    /** Флаг для перехода к предыдущему фото */
    public static final boolean PREVIOUS_PHOTO = false;

    /** Ширина окна масштабирования */
    public static final int SCROLL_FRAME_W = 1500;

    /** Высота окна масштабирования */
    public static final int SCROLL_FRAME_H = 920;

    /** Ширина панели прокрутки изображения в окне масштабирования */
    public static final int PHOTO_SCROLL_PANEL_W = 1500;

    /** Высота панели прокрутки изображения в окне масштабирования */
    public static final int PHOTO_SCROLL_PANEL_H = 850;

    /** Ширина панели кнопок в окне масштабирования */
    public static final int BTN_PANEL_W = 1500;

    /** Высота панели кнопок в окне масштабирования */
    public static final int BTN_PANEL_H = 70;

    /** Флаг увеличения масштаба */
    public static final boolean SCALE_UP = true;

    /** Флаг уменьшения масштаба */
    public static final boolean SCALE_DOWN = false;

    /** Шаг изменения масштаба */
    public static final double SCALE_STEP = 0.1;

    /** Максимальный коэффициент масштаба */
    public static final double MAX_SCALE = 2.0;

    /** Минимальный коэффициент масштаба */
    public static final double MIN_SCALE = 0.2;

    /** Нормальный коэффициент масштаба */
    public static final double NORMAL_SCALE = 1.0;

    /** Флаг оповещения всех наблюдателей */
    public static final boolean ALL = true;

    /** Флаг оповещения только наблюдателей масштаба */
    public static final boolean ZOOM = false;
}