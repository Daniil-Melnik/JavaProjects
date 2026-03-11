package testing.PhotoViewer;

/**
 * Учебный проект для отработки навыков работы с сеточно-контейнерной компоновкой.
 * Приложение для просмотра изображений в выбранной директории.
 * <p>
 * Основные возможности:
 * <ul>
 *   <li>Просмотр изображений в главном окне</li>
 *   <li>Лента окружающих изображений</li>
 *   <li>Отображение имени файла и метаинформации</li>
 *   <li>Пролистывание кнопками и клавишами</li>
 *   <li>Выбор директории через главное меню</li>
 * </ul>
 *
 * @version 1.0
 */

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
        import javax.swing.border.EtchedBorder;
import java.awt.*;
        import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.*;
        import java.util.List;

/**
 * Главный класс приложения, описывающий основное окно программы.
 * Содержит статические компоненты интерфейса и точку входа в приложение.
 */
public class PhotoViewer {
    /** Главное окно приложения */
    private static JFrame mainFrame;

    /** Ширина главного окна */
    private static final int FRAME_W = 1000;
    /** Высота главного окна */
    private static final int FRAME_H = 730;

    /** Высота панели с большим изображением */
    private static final int BIG_PHOTO_PANEL_H = 500;

    /** Высота панели с названием файла */
    private static final int TITLE_PANEL_H = 24;

    /** Высота панели с лентой окружающих изображений */
    private static final int SLIDE_PANEL_H = 150;

    /** Высота панели с кнопками пролистывания */
    private static final int BUTTON_PANEL_H = 60;

    /** Ширина маленького изображения в ленте */
    private static final int SMALL_PHOTO_W = 180;
    /** Высота маленького изображения в ленте */
    private static final int SMALL_PHOTO_H = 140;

    /** Ширина панели маленького изображения */
    private static final int SMALL_PHOTO_PANEL_W = 196;
    /** Высота панели маленького изображения */
    private static final int SMALL_PHOTO_PANEL_H = 144;

    /** Основной цвет фона окна */
    private static final Color GRAY_BACKGROUND = new Color(238, 238, 238);

    /** Константа для направления пролистывания "вперед" */
    private static final boolean NEXT_PHOTO = true;
    /** Константа для направления пролистывания "назад" */
    private static final boolean PREVIOUS_PHOTO = false;

    /** Экземпляр класса, управляющего положением в просмотрщике фотографий */
    private static PhotoHolder holder = new PhotoHolder();

    /** Панель с основным изображением */
    private static PhotoPanel photoPanel;
    /** Панель с именем текущего изображения */
    private static TitlePanel titlePanel;
    /** Панель с лентой окружающих изображений */
    private static PhotoSlidePanel slidePanel;
    /** Панель с кнопками пролистывания */
    private static ButtonPanel buttonPanel;

    /** Компонент для отрисовки изображения */
    private static PhotoComponent photoComponent;

    /** Всплывающее меню для панели с изображением */
    @Getter
    private static PhotoJPopumMenu photoJPopupMenu;
    /** Строка главного меню */
    @Getter
    private static MainMenuBar mainMenuBar;

    /**
     * Точка входа в приложение.
     * Запускает создание главного окна в потоке обработки событий.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }

    /**
     * Внутренний класс, представляющий главное окно приложения.
     * Отвечает за компоновку всех основных панелей интерфейса.
     */
    private static class MainFrame extends JFrame {
        /**
         * Конструктор главного окна.
         * Настраивает параметры окна и размещает все панели интерфейса.
         */
        public MainFrame() {
            setLayout(new GridBagLayout());
            setSize(FRAME_W, FRAME_H);
            setIconImage(new ImageIcon(
                    Objects.requireNonNull(PhotoViewer.class.getResource("/photo.png"))).getImage()
            );
            setTitle("ФотоПросмотр");
            setResizable(false);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            buttonPanel = new ButtonPanel();
            photoPanel = new PhotoPanel();
            titlePanel = new TitlePanel();
            slidePanel = new PhotoSlidePanel();

            photoJPopupMenu = new PhotoJPopumMenu();
            mainMenuBar = new MainMenuBar();

            mainMenuBar.blockMenu(false);
            photoJPopupMenu.blockMenu(false);

            photoPanel.setComponentPopupMenu(photoJPopupMenu);
            setJMenuBar(mainMenuBar);

            add(photoPanel, new GBC(0, 0, 1, 1));
            add(titlePanel, new GBC(0, 1, 1, 1).setInsets(3, 0, 0, 0));
            add(slidePanel, new GBC(0, 2, 1, 1));
            add(buttonPanel, new GBC(0, 3, 1, 1));

            pack();
        }
    }

    /**
     * Панель для отображения основного изображения.
     * Содержит компонент PhotoComponent для отрисовки большого изображения.
     */
    private static class PhotoPanel extends JPanel {
        /**
         * Конструктор панели с изображением.
         * Создает и добавляет компонент для отрисовки большого изображения.
         */
        public PhotoPanel() {
            setLayout(new FlowLayout());
            setBackground(new Color(224, 224, 224));
            photoComponent = new PhotoComponent(false);
            add(photoComponent);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(FRAME_W, BIG_PHOTO_PANEL_H);
        }
    }

    /**
     * Панель с лентой окружающих изображений.
     * Отображает до 5 маленьких изображений, соседних с текущим.
     */
    private static class PhotoSlidePanel extends JPanel {
        /** Карта для хранения пяти маленьких панелей с изображениями */
        private LinkedHashMap<Integer, SmallPhotoPanel> photoComponentsMap = new LinkedHashMap<>(5);

        /**
         * Конструктор панели слайдов.
         * Создает и размещает 5 маленьких панелей для отображения соседних изображений.
         */
        public PhotoSlidePanel() {
            setLayout(new GridLayout(1, 5));
            setBorder(new EtchedBorder());
            for (int i = 0; i < 5; i++) {
                photoComponentsMap.put(i, new SmallPhotoPanel());
            }
            for (SmallPhotoPanel p : photoComponentsMap.values()) add(p);
        }

        /**
         * Обновляет содержимое панели слайдов.
         * Устанавливает изображения и названия для всех маленьких панелей.
         *
         * @param nearImages список из 5 изображений-соседей
         * @param nearTitles список из 5 названий файлов-соседей
         */
        public void setSlidePanel(List<Image> nearImages, List<String> nearTitles) {
            for (Map.Entry<Integer, SmallPhotoPanel> e : photoComponentsMap.entrySet()) {
                if (e.getKey() < holder.getImageList().size()) {
                    e.getValue()
                            .getPhotoComponent()
                            .setCurrentImageTitle(
                                    nearImages.get(e.getKey()),
                                    nearTitles.get(e.getKey())
                            );
                    e.getValue().setToolTipText(nearTitles.get(e.getKey()));
                    if (e.getKey() == holder.getImageList().getFlag()) {
                        e.getValue().setBackground(Color.DARK_GRAY);
                    } else e.getValue().setBackground(new Color(224, 224, 224));
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(FRAME_W, SLIDE_PANEL_H);
        }

        /**
         * Внутренний класс, представляющий маленькую панель с изображением для ленты.
         */
        @Getter
        @Setter
        private static class SmallPhotoPanel extends JPanel {
            /** Компонент для отрисовки маленького изображения */
            PhotoComponent photoComponent = new PhotoComponent(true);

            /**
             * Конструктор маленькой панели.
             * Создает и размещает компонент для отображения маленького изображения.
             */
            public SmallPhotoPanel() {
                setLayout(new BorderLayout());
                setBorder(new EtchedBorder());
                add(photoComponent, BorderLayout.CENTER);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(SMALL_PHOTO_PANEL_W, SLIDE_PANEL_H);
            }
        }
    }

    /**
     * Панель для отображения названия текущего изображения.
     */
    private static class TitlePanel extends JPanel {
        /** Метка для отображения названия файла */
        JLabel titleLabel = new JLabel();

        /**
         * Конструктор панели названия.
         * Создает и настраивает метку для отображения названия изображения.
         */
        public TitlePanel() {
            setLayout(new BorderLayout());
            setBorder(new EtchedBorder());
            titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            add(titleLabel, BorderLayout.WEST);
        }

        /**
         * Обновляет текст с названием изображения.
         *
         * @param newTitle новое название изображения
         */
        public void setPhotoTitle(String newTitle) {
            titleLabel.setText(newTitle);
            revalidate();
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(FRAME_W, TITLE_PANEL_H);
        }
    }

    /**
     * Панель с кнопками пролистывания изображений.
     * Содержит кнопки "вперед" и "назад", а также обработку нажатий клавиш.
     */
    private static class ButtonPanel extends JPanel {
        /**
         * Конструктор панели кнопок.
         * Создает кнопки пролистывания и настраивает обработку клавиш.
         */
        public ButtonPanel() {
            setLayout(new GridLayout(1, 2));

            NextBackAction nextAction = new NextBackAction(NEXT_PHOTO);
            NextBackAction backAction = new NextBackAction(PREVIOUS_PHOTO);

            JButton nextBtn = new NextBackButton(nextAction);
            JButton backBtn = new NextBackButton(backAction);

            this.getActionMap().put("next", nextAction);
            this.getActionMap().put("back", backAction);

            this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "next");
            this.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "back");

            add(backBtn);
            add(nextBtn);
        }

        /**
         * Внутренний класс, представляющий действие пролистывания.
         * Используется для создания кнопок и обработки нажатий клавиш.
         */
        private class NextBackAction extends AbstractAction {
            /** Тип действия: true - вперед, false - назад */
            private boolean type;

            /**
             * Конструктор действия пролистывания.
             *
             * @param t true для пролистывания вперед, false для пролистывания назад
             */
            public NextBackAction(boolean t) {
                type = t;

                String iconSource = t ? "/next.png" : "/back.png";
                String name = t ? "далее" : "назад";
                ImageIcon icon = new ImageIcon(Objects.requireNonNull(NextBackAction.class.getResource(iconSource)));

                super("", icon);
                putValue(Action.SHORT_DESCRIPTION, name);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (type) {
                    holder.incrCursorInLists();
                } else {
                    holder.decrCursorInLists();
                }

                Image newImg = holder.getImageList().getCurrent();
                String newTitle = holder.getTitleList().getCurrent();
                if (newImg != null) {
                    photoComponent.setCurrentImageTitle(newImg, newTitle);
                    titlePanel.setPhotoTitle(newTitle);
                    slidePanel.setSlidePanel(
                            holder.getImageList().getFiveNearElements(),
                            holder.getTitleList().getFiveNearElements());
                    slidePanel.revalidate();
                    slidePanel.repaint();
                    photoComponent.revalidate();
                    photoComponent.repaint();
                } else {
                    if (type) {
                        holder.decrCursorInLists();
                    } else holder.incrCursorInLists();
                }
            }
        }

        /**
         * Внутренний класс, представляющий кнопку пролистывания.
         */
        private class NextBackButton extends JButton {
            /**
             * Конструктор кнопки пролистывания.
             *
             * @param action действие, которое будет выполняться при нажатии кнопки
             */
            public NextBackButton(NextBackAction action) {
                super(action);
                setBackground(GRAY_BACKGROUND);
                setBorder(null);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(FRAME_W, BUTTON_PANEL_H);
        }
    }

    /**
     * Компонент для отрисовки изображения.
     * Поддерживает два режима: большой (для основного окна) и маленький (для ленты).
     */
    private static class PhotoComponent extends JComponent {
        /** Текущее изображение для отрисовки */
        @Setter
        Image currentImage;
        /** Тип компонента: true - маленький, false - большой */
        boolean type;
        /** Стандартная ширина, выделяемая под изображение */
        int standartWidth;
        /** Стандартная высота, выделяемая под изображение */
        int standartHeight;
        /** Название текущего изображения */
        @Setter
        String currentTitle;

        /**
         * Конструктор компонента с заданным изображением и типом.
         *
         * @param cI   изображение для отображения
         * @param t    тип компонента (true - маленький, false - большой)
         * @param iT   название изображения
         */
        public PhotoComponent(Image cI, boolean t, String iT) {
            currentImage = cI;
            type = t;
            standartWidth = t ? SMALL_PHOTO_W : FRAME_W;
            standartHeight = t ? SMALL_PHOTO_H : BIG_PHOTO_PANEL_H;
            currentTitle = iT;

            setToolTipText(currentTitle);
        }

        /**
         * Конструктор компонента только с указанием типа.
         *
         * @param t тип компонента (true - маленький, false - большой)
         */
        public PhotoComponent(boolean t) {
            currentImage = null;
            type = t;
            standartWidth = t ? SMALL_PHOTO_W : FRAME_W;
            standartHeight = t ? SMALL_PHOTO_H : BIG_PHOTO_PANEL_H;
            currentTitle = "";
        }

        /**
         * Устанавливает изображение и его название в компонент.
         *
         * @param img   изображение для отображения
         * @param title название изображения
         */
        public void setCurrentImageTitle(Image img, String title) {
            setCurrentImage(img);
            setCurrentTitle(title);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (currentImage != null) {
                g.drawImage(
                        currentImage,
                        (int) getPhotoPoint().getX(),
                        (int) getPhotoPoint().getY(),
                        (int) getPhotoDimension().getWidth(),
                        (int) getPhotoDimension().getHeight(),
                        null);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return (type ? new Dimension(SMALL_PHOTO_PANEL_W, SLIDE_PANEL_H) : new Dimension(FRAME_W, BIG_PHOTO_PANEL_H));
        }

        /**
         * Рассчитывает оптимальные размеры изображения для отображения в рамках панели.
         * Сохраняет пропорции изображения.
         *
         * @return размеры изображения для отрисовки
         */
        private Dimension getPhotoDimension() {
            int imgWidth = currentImage.getWidth(null);
            int imgHeight = currentImage.getHeight(null);

            double ratioWidthHeight = (double) imgWidth / imgHeight;

            if (imgWidth > standartWidth) {
                imgWidth = standartWidth;
                imgHeight = (int) (imgWidth * (1 / ratioWidthHeight));
            }

            if (imgHeight > standartHeight) {
                imgHeight = standartHeight;
                imgWidth = (int) (imgHeight * ratioWidthHeight);
            }
            return new Dimension(imgWidth, imgHeight);
        }

        /**
         * Рассчитывает координаты левого верхнего угла для центрирования изображения.
         *
         * @return точка для отрисовки изображения
         */
        private Point getPhotoPoint() {
            int standardPanelWidth = type ? SMALL_PHOTO_PANEL_W : FRAME_W;
            int standardPanelHeight = type ? SMALL_PHOTO_PANEL_H : BIG_PHOTO_PANEL_H;

            int x = (int) ((standardPanelWidth - getPhotoDimension().getWidth()) / 2);
            int y = (int) ((standardPanelHeight - getPhotoDimension().getHeight()) / 2);

            return new Point(x, y);
        }
    }

    /**
     * Главное меню приложения.
     * Содержит пункты для выбора директории, закрытия приложения и работы с текущим изображением.
     */
    private static class MainMenuBar extends JMenuBar {
        /** Меню "Файл" */
        private JMenu mainMenu;
        /** Меню "Текущее" */
        private JMenu photoMenu;

        /** Пункт меню для выбора папки */
        private JMenuItem setFolderItem;
        /** Пункт меню для выхода из приложения */
        private JMenuItem exitItem;
        /** Пункт меню для открытия изображения в отдельном окне */
        private final JMenuItem separateWindowItem;
        /** Пункт меню для отображения информации об изображении */
        private final JMenuItem aboutImageItem;

        /**
         * Конструктор главного меню.
         * Создает и настраивает все пункты меню.
         */
        public MainMenuBar() {
            mainMenu = new JMenu("Файл (F)");
            photoMenu = new JMenu("Текущее (T)");

            mainMenu.setMnemonic(KeyEvent.VK_F);
            photoMenu.setMnemonic(KeyEvent.VK_T);

            setFolderItem = new JMenuItem("Папка (D)", 'D');
            exitItem = new JMenuItem("Закрыть (C)", 'C');
            separateWindowItem = getSeparateWindowItem();
            aboutImageItem = getInfoMenuItem();

            mainMenu.add(setFolderItem);
            mainMenu.addSeparator();
            mainMenu.add(exitItem);

            photoMenu.add(separateWindowItem);
            photoMenu.addSeparator();
            photoMenu.add(aboutImageItem);

            exitItem.addActionListener(e -> System.exit(0));

            setFolderItem.addActionListener((e) -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                    try {
                        holder.setImageList(fileChooser.getSelectedFile().listFiles());
                        photoComponent.setCurrentImageTitle(
                                holder.getImageList().getCurrent(),
                                holder.getTitleList().getCurrent());
                        titlePanel.setPhotoTitle(holder.getTitleList().getCurrent());
                        slidePanel.setSlidePanel(
                                holder.getImageList().getFiveNearElements(),
                                holder.getTitleList().getFiveNearElements());

                        photoComponent.repaint();
                        photoComponent.revalidate();
                        slidePanel.repaint();
                        slidePanel.revalidate();

                        mainMenuBar.blockMenu(true);
                        photoJPopupMenu.blockMenu(true);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(
                                mainFrame,
                                "Ошибка загрузки файлов",
                                "Проблема с директорией",
                                JOptionPane.ERROR_MESSAGE
                        );

                        photoJPopupMenu.blockMenu(false);
                        mainMenuBar.blockMenu(false);
                    }
                }
            });

            add(mainMenu);
            add(photoMenu);
        }

        /**
         * Блокирует или разблокирует пункты меню, связанные с текущим изображением.
         *
         * @param t true - разблокировать, false - заблокировать
         */
        public void blockMenu(boolean t) {
            separateWindowItem.setEnabled(t);
            aboutImageItem.setEnabled(t);
        }
    }

    /**
     * Всплывающее меню для панели с изображением.
     * Содержит пункты для работы с текущим изображением.
     */
    private static class PhotoJPopumMenu extends JPopupMenu {
        /** Пункт для открытия изображения в отдельном окне */
        private JMenuItem separateWindowItem;
        /** Пункт для отображения информации об изображении */
        private JMenuItem infoItem;

        /**
         * Конструктор всплывающего меню.
         * Создает и добавляет пункты меню.
         */
        public PhotoJPopumMenu() {
            separateWindowItem = getSeparateWindowItem();
            infoItem = getInfoMenuItem();

            add(separateWindowItem);
            addSeparator();
            add(infoItem);
        }

        /**
         * Блокирует или разблокирует пункты всплывающего меню.
         *
         * @param t true - разблокировать, false - заблокировать
         */
        public void blockMenu(boolean t) {
            separateWindowItem.setEnabled(t);
            infoItem.setEnabled(t);
        }
    }

    /**
     * Фабричный метод для создания пункта меню с информацией о изображении.
     *
     * @return настроенный пункт меню "О фото"
     */
    private static JMenuItem getInfoMenuItem() {
        JMenuItem item = new JMenuItem("О фото (A)", 'A');
        item.addActionListener(e -> {
            try {
                HashMap<String, Number> imageInfo = holder.getCurrentImageInfo();
                JOptionPane.showMessageDialog(mainFrame,
                        String.format("%.1f x %.1f пикселей \n %.2f КБайт",
                                imageInfo.get("w").doubleValue(),
                                imageInfo.get("h").doubleValue(),
                                imageInfo.get("vol").doubleValue() / 1024),
                        "Размер и объём",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IOException ex) {
                System.err.println("Ошибка файла");
            }
        });
        return item;
    }

    /**
     * Фабричный метод для создания пункта меню "В отдельном окне".
     *
     * @return настроенный пункт меню для открытия изображения в новом окне
     */
    private static JMenuItem getSeparateWindowItem() {
        JMenuItem item = new JMenuItem("В отдельном окне (S)", 'S');
        item.addActionListener(e -> {
            Image currImg = holder.getImageList().getCurrent();
            String currTitle = holder.getTitleList().getCurrent();
            if (currImg != null) {
                PhotoScrollFrame photoFrame = new PhotoScrollFrame(currImg, currTitle);
                photoFrame.setVisible(true);
            }
        });
        return item;
    }
}
