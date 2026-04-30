package testing.Implementation.FrontEnd.ZoomFrame.Panels;

import testing.Implementation.Constants;
import testing.Interfaces.Compositor;
import testing.Interfaces.Controller;
import testing.Interfaces.Holder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * Панель с кнопками управления масштабом для окна увеличения.
 * Содержит кнопки увеличения, уменьшения и индикатор текущего масштаба.
 * Может использоваться как листовой узел компоновщика.
 */
public class ButtonPanel extends JPanel implements Compositor {

    /** Метка для отображения текущего коэффициента масштаба */
    private JLabel scaleLabel;

    /** Ссылка на модель данных */
    private Holder holder;

    /**
     * Конструктор панели кнопок масштабирования.
     *
     * @param holder модель данных
     * @param controller контроллер для выполнения масштабирования
     */
    public ButtonPanel(Holder holder, Controller controller){
        this.holder = holder;

        JButton plusBtn = new JButton(new ImageIcon(
                Objects.requireNonNull(this.getClass().getResource("/zoom-in.png"))
        ));

        JButton minusBtn = new JButton(new ImageIcon(
                Objects.requireNonNull(this.getClass().getResource("/zoom-out.png"))
        ));

        plusBtn.setBackground(Constants.GRAY_BACKGROUND);
        minusBtn.setBackground(Constants.GRAY_BACKGROUND);

        minusBtn.setBorder(null);
        plusBtn.setBorder(null);

        scaleLabel = new JLabel(String.format("%.1f", holder.getZoom()));
        scaleLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        plusBtn.addActionListener((e) -> {
            controller.addZoom(Constants.SCALE_STEP);
        });

        minusBtn.addActionListener((e) -> {
            controller.subZoom(Constants.SCALE_STEP);
        });

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ADD"), "zoom_in");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SUBTRACT"), "zoom_out");

        getActionMap().put("zoom_in", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.addZoom(Constants.SCALE_STEP);
            }
        });
        getActionMap().put("zoom_out", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.subZoom(Constants.SCALE_STEP);
            }
        });

        add(minusBtn);
        add(scaleLabel);
        add(plusBtn);
    }

    /**
     * Возвращает предпочтительный размер панели.
     *
     * @return размер панели
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Constants.BTN_PANEL_W, Constants.BTN_PANEL_H);
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
     * Обновляет отображение текущего коэффициента масштаба из модели.
     */
    @Override
    public void refresh() {
        scaleLabel.setText(String.format("%.1f", holder.getZoom()));
        revalidate();
        repaint();
    }
}