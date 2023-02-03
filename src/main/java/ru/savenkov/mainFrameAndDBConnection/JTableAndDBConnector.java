package ru.savenkov.mainFrameAndDBConnection;

import ru.savenkov.mainWindow.MainFrame;

public interface JTableAndDBConnector {
    public void deleteAll(MainFrame mainFrame);
    public void deleteById(MainFrame mainFrame);
    public void resetTable(MainFrame mainFrame);
    public void appendDataFromCBR(MainFrame mainFrame);
}
