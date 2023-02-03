package ru.savenkov.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


//Singleton
public class Database {
    private static final Logger log = LoggerFactory.getLogger(Database.class);
    public final Path DB_PATH = Paths.get("src\\main\\java\\ru\\savenkov\\database\\sqlite\\currencyExchange.db");
    public final String DB_NAME = "currency_exchange";
    private final String SCHEMA_SQL = """
			CREATE TABLE IF NOT EXISTS currency_exchange (
			    id INTEGER PRIMARY KEY AUTOINCREMENT,
			    "value" REAL NOT NULL,
			    nominal INTEGER NOT NULL,
			    currency_name VARCHAR(100) NOT NULL,
			    currency_code VARCHAR(3) NOT NULL,
			    date DATE
			);""";
    private Database(){}
    private static Database instance;
    public static synchronized Database getInstance(){
        log.info("In method");
        if(instance == null){
            log.info("instance == null");
            instance = new Database();
            log.info("instance initialized");
        }
        return instance;
    }

    private Connection connection; //объект соединения с нашей бд

    public Connection getConnection() {
        log.info("In method");
        if (connection == null) {
            try {
                prepareDirectory();
                connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
                initDb();
                log.info("Connection initialized");
            } catch (SQLException e) {
                log.error("Can't get connection");
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
    private void prepareDirectory(){
        //подготавливает директорию для хранения бд(создать папку и т.д)\
        //если файл не создан, то создаём
        log.info("In method");
        if (!Files.exists(DB_PATH)) {
            try {
                File file = new File(DB_PATH.toString());
                if(file.createNewFile()){
                    log.info("file created");
                } else {
                    log.info("file is already created");
                }
            } catch (IOException e) {
                log.info("can't create file");
                throw new RuntimeException(e);
            }
        }
    }
    private void initDb(){
        //отвечает за создание таблиц в бд
        log.info("In method");
        try (PreparedStatement statement = connection.prepareStatement(SCHEMA_SQL)) {
            statement.execute();
            System.out.println("Таблица в бд создана или уже была создана");
            log.info("Database initialized successfully ");
        } catch (SQLException e) {
            log.warn("can't init database");
            throw new RuntimeException(e);
        }
    }
    public void closeConnection(){
        //когда прога завершается, то коннекшн закрывается тоже
        if (connection != null) {
            log.info("close connection...");
            try {
                connection.close();
                log.info("connection closed");
            } catch (SQLException e) {
                log.warn("can't close connection, repeat...");
                try {
                    // Finally
                    connection.close();
                    log.info("connection closed");
                } catch (SQLException ex) {
                    log.warn("can't close connection.");
                    throw new RuntimeException(ex);
                }

                throw new RuntimeException(e);
            }
            connection = null;
            log.info("connection = null");
        }
    }
}
