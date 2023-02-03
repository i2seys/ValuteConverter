package ru.savenkov.mainWindow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.savenkov.MainWindow;
import ru.savenkov.mainFrameAndDBConnection.MainFrameAndDBConnector;
import ru.savenkov.model.CurrencyExchange;
import ru.savenkov.repository.CurrencyExchangeRepository;
import ru.savenkov.repository.CurrencyExchangeRepositorySqliteImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame {
    private static final Logger log = LoggerFactory.getLogger(MainFrame.class);
    public static final String APP_NAME = "Конвертатор валют";
    private final String APP_VERSION = "1.0";
    private final Integer WINDOW_HEIGHT = 750;
    private final Integer WINDOW_WIDTH = 700;
    private final Integer MAX_BUTTONS_PANEL_HEIGHT = 200;
    private final Integer MAX_BUTTONS_PANEL_WIDTH = WINDOW_WIDTH;
    private final Integer PREFFERED_BUTTONS_PANEL_HEIGHT = 100;
    private final Integer PREFFERED_BUTTONS_PANEL_WIDTH =  WINDOW_WIDTH;
    private final Integer MIN_BUTTONS_PANEL_HEIGHT = 60;
    private final Integer MIN_BUTTONS_PANEL_WIDTH = WINDOW_WIDTH;
    private final JTable table;

    public JTable getTable() {
        return table;
    }
    public void setTable(JTable table){
        this.table.setModel(table.getModel());
    }

    public MainFrame() throws HeadlessException {
        CurrencyExchangeRepository rep = CurrencyExchangeRepositorySqliteImpl.getInstance();
        Toolkit tk = Toolkit.getDefaultToolkit();
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            log.warn("UIManager error");
            throw new RuntimeException(e);
        }

        //задаём вид окна так, что все элементы помещаются друг за другом сверху вниз
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(APP_NAME);

        //Панель для кнопок действия c таблицей
        JPanel panelForActions = new JPanel();
        panelForActions.setBorder(BorderFactory.createTitledBorder("Действия"));
        panelForActions.setLayout(new FlowLayout());

        JButton deleteAllBtn = new JButton("Удалить всё");//Удаление
        deleteAllBtn.addActionListener(e -> MainFrameAndDBConnector.getInstance().deleteAll(MainFrame.this));
        JButton deleteByIdBtn = new JButton("Удалить по id");//Удаление по ID
        deleteByIdBtn.addActionListener(e -> MainFrameAndDBConnector.getInstance().deleteById(MainFrame.this));

        JButton resetBtn = new JButton("Обнуление таблицы");//Обновление таблицы
        resetBtn.addActionListener(e -> MainFrameAndDBConnector.getInstance().resetTable(MainFrame.this));
        JButton updateFromCBRBtn = new JButton("Получить данные из ЦБ");//Получение данных с ЦБР
        updateFromCBRBtn.addActionListener(e -> MainFrameAndDBConnector.getInstance().appendDataFromCBR(MainFrame.this));

        panelForActions.add(deleteAllBtn);
        panelForActions.add(deleteByIdBtn);
        panelForActions.add(resetBtn);
        panelForActions.add(updateFromCBRBtn);

        //придаём адекватную форму панельке с кнопками
        panelForActions.setMaximumSize(new Dimension(MAX_BUTTONS_PANEL_WIDTH, MAX_BUTTONS_PANEL_HEIGHT));
        panelForActions.setPreferredSize(new Dimension(PREFFERED_BUTTONS_PANEL_WIDTH, PREFFERED_BUTTONS_PANEL_HEIGHT));
        panelForActions.setMinimumSize(new Dimension(MIN_BUTTONS_PANEL_WIDTH, MIN_BUTTONS_PANEL_HEIGHT));
        this.add(panelForActions);

        //Добавим таблицу
        //Получаем данные из БД
        table = new JTable(((CurrencyExchangeRepositorySqliteImpl)rep).getDataFromDB(), CurrencyExchange.COLUMNS_NAME);//Инициализируем данными из БД
        table.setDefaultEditor(Object.class, null);//запрещаем редактирование таблицы
        table.getTableHeader().setReorderingAllowed( false );//запрещаем перетаскивание столбцов


        table.setFillsViewportHeight(true);
        JScrollPane paneForTable = new JScrollPane(table);
        Border borderForTable= BorderFactory.createTitledBorder("Таблица");
        paneForTable.setBorder(borderForTable);
        this.add(paneForTable);

        try {
            this.setIconImage(ImageIO.read(new File("picture/mysteriousTree.jpg")));
        } catch (IOException e) {
            log.warn("SetIconImage error");
            throw new RuntimeException(e);
        }

        JMenuBar menuBar = new JMenuBar();
        JMenu menuExport = new JMenu("Экспорт"), menuHelp = new JMenu("Помощь");

        //Добавляем действия на кнопки
        JMenuItem exportItem = new JMenuItem("Экспорт в CSV");
        exportItem.addActionListener(e -> MainFrameAndDBConnector.getInstance().csvExport(MainFrame.this));
        menuExport.add(exportItem);
        exportItem = new JMenuItem("Экспорт в JSON");
        exportItem.addActionListener(e -> MainFrameAndDBConnector.getInstance().jsonExport(MainFrame.this));
        menuExport.add(exportItem);

        ImageIcon imageForHelpItem = new ImageIcon("picture\\nyan-cat.gif");
        JMenuItem helpItem = new JMenuItem("Данные о программе");
        JMenuItem exitItem = new JMenuItem("Выход");
        helpItem.addActionListener(e -> JOptionPane.showMessageDialog(MainFrame.this,
                "Версия: " + APP_VERSION + " ",
                "Данные о программе",
                JOptionPane.PLAIN_MESSAGE,
                imageForHelpItem));
        exitItem.addActionListener(e -> {
            MainWindow.onClose();
            System.exit(0);
        });
        menuHelp.add(helpItem);
        menuHelp.add( new JSeparator());
        menuHelp.add(exitItem);


        menuBar.add(menuExport);
        menuBar.add(menuHelp);
        this.setJMenuBar(menuBar);

        this.setLocation(tk.getScreenSize().width / 2 - WINDOW_WIDTH / 2 , tk.getScreenSize().height / 2 - WINDOW_HEIGHT / 2);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        log.info("exit");
    }
}
