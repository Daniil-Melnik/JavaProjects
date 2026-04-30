package testing.Implementation.FrontEnd.MainFrame.Panels;

import testing.Implementation.Constants;
import testing.Interfaces.Compositor;
import testing.Interfaces.Holder;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Панель для отображения наименования текущего изображения.
 * Может быть листовым узлом в компоновщике.
 */
public class TitlePanel extends JPanel implements Compositor {

    /** Метка для отображения названия изображения */
    JLabel titleLabel = new JLabel();

    /** Ссылка на модель данных */
    Holder holder;

    /**
     * Конструктор панели названия.
     *
     * @param h модель данных
     */
    public TitlePanel(Holder h) {
        holder = h;
        setLayout(new BorderLayout());
        setBorder(new EtchedBorder());
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(titleLabel, BorderLayout.WEST);
    }

    /**
     * Обновляет текст с наименованием изображения.
     *
     * @param newTitle новое наименование
     */
    public void setPhotoTitle(String newTitle) {
        titleLabel.setText(newTitle);
    }

    /**
     * Возвращает предпочтительный размер панели.
     *
     * @return размер панели
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.FRAME_W, Constants.TITLE_PANEL_H);
    }

    /**
     * Возвращает Swing-компонент панели.
     *
     * @return текущая панель
     */
    @Override
    public JComponent getComponent() {
        return this;
    }

    /**
     * Обновляет панель, устанавливая наименование текущего изображения из модели.
     */
    @Override
    public void refresh() {
        setPhotoTitle(holder.getCurrentImageName());
        repaint();
    }
}