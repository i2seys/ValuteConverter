package ru.savenkov;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.savenkov.database.Database;
import ru.savenkov.mainWindow.MainFrame;

import javax.swing.*;


public class MainWindow {

    private static final Logger log = LoggerFactory.getLogger(MainWindow.class);

    public static void main(String[] args) {
        log.info("{} started", MainFrame.APP_NAME);
        JFrame mainWindow = new MainFrame();
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                onClose();
            }
        });
    }

    public static void onClose() {
        log.info("closing connection...");
        Database.getInstance().closeConnection();
        log.info("connection closed");
    }
}
