package ru.savenkov.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.savenkov.database.Database;
import ru.savenkov.model.CurrencyExchange;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CurrencyExchangeRepositorySqliteImpl implements CurrencyExchangeRepository {
    private static final Logger log = LoggerFactory.getLogger(CurrencyExchangeRepositorySqliteImpl.class);
    private static CurrencyExchangeRepository instance;
    public static CurrencyExchangeRepository getInstance() {
        if (instance == null) {
            log.info("instance == null");
            instance = new CurrencyExchangeRepositorySqliteImpl();
            log.info("instance initialized");
        }
        log.info("In CurrencyExchangeRepository.getInstance: exit");
        return instance;
    }
    private CurrencyExchangeRepositorySqliteImpl() {
        database = Database.getInstance();
    }
    private final Database database;

    @Override
    public CurrencyExchange findById(Integer id) {
        log.info("In method");
        Connection co = database.getConnection();
        String query = "SELECT * FROM " + database.DB_NAME + " WHERE " + CurrencyExchange.ID_FIELD_NAME +  " = ? ;";

        try (PreparedStatement statement = co.prepareStatement(query)) {
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            CurrencyExchange currencyExchange = new CurrencyExchange();
            while (resultSet.next()) {
                currencyExchange.setId(resultSet.getInt(1));
                currencyExchange.setValue(resultSet.getDouble(2));
                currencyExchange.setNominal(resultSet.getInt(3));
                currencyExchange.setCurrencyName(resultSet.getString(4));
                currencyExchange.setCurrencyCode(resultSet.getString(5));
                currencyExchange.setDate(LocalDate.parse(resultSet.getString(6)));
            }
            resultSet.close();
            return currencyExchange;
        } catch (SQLException e) {
            log.warn("error");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CurrencyExchange> findAll() {
        log.info("In method");
        Connection co = database.getConnection();
        String query = "SELECT * FROM " + database.DB_NAME + " ;";

        try (PreparedStatement statement = co.prepareStatement(query)) {//TRY WITH RESOURCES - в скобках открывается поток, потом он закрывается
            ResultSet resultSet = statement.executeQuery();
            List<CurrencyExchange> currencyExchanges = new ArrayList<>();
            while (resultSet.next()) {
                CurrencyExchange currencyExchange = new CurrencyExchange();
                currencyExchange.setId(resultSet.getInt(1));
                currencyExchange.setValue(resultSet.getDouble(2));
                currencyExchange.setNominal(resultSet.getInt(3));
                currencyExchange.setCurrencyName(resultSet.getString(4));
                currencyExchange.setCurrencyCode(resultSet.getString(5));
                currencyExchange.setDate(LocalDate.parse(resultSet.getString(6)));
                currencyExchanges.add(currencyExchange);
            }
            resultSet.close();
            log.info("exchanges saved");
            return currencyExchanges;
        } catch (SQLException e) {
            log.warn("error");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CurrencyExchange> findAllByCode(String currencyCode) {
        log.info("In method");
        Connection co = database.getConnection();
        String query = "SELECT * " +
                " FROM " + database.DB_NAME +
                " WHERE " +  CurrencyExchange.CURRENCY_CODE_FIELD_NAME + " = ? ;";

        try (PreparedStatement statement = co.prepareStatement(query)) {
            statement.setString(1, currencyCode);
            ResultSet resultSet = statement.executeQuery();
            List<CurrencyExchange> currencyExchanges = new ArrayList<>();

            while (resultSet.next()) {
                CurrencyExchange currencyExchange = new CurrencyExchange();
                currencyExchange.setId(resultSet.getInt(1));
                currencyExchange.setValue(resultSet.getDouble(2));
                currencyExchange.setNominal(resultSet.getInt(3));
                currencyExchange.setCurrencyName(resultSet.getString(4));
                currencyExchange.setCurrencyCode(resultSet.getString(5));
                currencyExchange.setDate(LocalDate.parse(resultSet.getString(6)));
                currencyExchanges.add(currencyExchange);
            }
            resultSet.close();
            return currencyExchanges;
        } catch (SQLException e) {
            log.warn("error");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer delete(Integer id) {
        log.info("In method");
        Connection co = database.getConnection();
        String query = "DELETE FROM " + database.DB_NAME + " WHERE " + CurrencyExchange.ID_FIELD_NAME + " = ? ;";
        Integer changes;

        try (PreparedStatement statement = co.prepareStatement(query)) {
            statement.setInt(1, id);
            changes = statement.executeUpdate();
            if(changes > 1)
            {
                log.warn("changes > 1");
            }

        } catch (SQLException e) {
            log.warn("error");
            throw new RuntimeException(e);
        }
        //Удалить из таблицы
        return changes;
    }

    @Override
    public void deleteAll() {
        log.info("In method");
        Connection co = database.getConnection();
        String query = "DELETE FROM " + database.DB_NAME + " ;";

        try (PreparedStatement statement = co.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            log.warn("error");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer insert(CurrencyExchange currency) {
        log.info("In method");
        Connection co = database.getConnection();

        String query = "INSERT INTO " + database.DB_NAME + " (" +
                CurrencyExchange.VALUE_FIELD_NAME + ", " +
                CurrencyExchange.NOMINAL_FIELD_NAME + ", " +
                CurrencyExchange.CURRENCY_NAME_FIELD_NAME + ", " +
                CurrencyExchange.CURRENCY_CODE_FIELD_NAME + ", " +
                CurrencyExchange.DATE_FIELD_NAME +
        ") VALUES (?, ?, ?, ?, ?);";
        Integer changes;

        try(PreparedStatement statement = co.prepareStatement(query)) {
            statement.setDouble(1, currency.getValue());
            statement.setInt(2, currency.getNominal());
            statement.setString(3, currency.getCurrencyName());
            statement.setString(4, currency.getCurrencyCode());
            statement.setString(5, currency.getDate().toString());
            changes = statement.executeUpdate();

            if(changes > 1)
            {
                log.info("changes > 1");
            }
        }
        catch (SQLException e) {
            log.warn("error");
            throw new RuntimeException(e);
        }
        return changes;
    }

    @Override
    public Integer update(CurrencyExchange currency) {
        log.info("In method");
        Connection co = database.getConnection();

        String query = "UPDATE " + database.DB_NAME + " SET " +
                CurrencyExchange.VALUE_FIELD_NAME + " = ? , " +
                CurrencyExchange.NOMINAL_FIELD_NAME + " = ? , " +
                CurrencyExchange.CURRENCY_NAME_FIELD_NAME + " = ? , " +
                CurrencyExchange.CURRENCY_CODE_FIELD_NAME + " = ? , " +
                CurrencyExchange.DATE_FIELD_NAME + " = ? , " +
                "WHERE " + CurrencyExchange.ID_FIELD_NAME + " = ? ;";
        Integer changes;

        try(PreparedStatement statement = co.prepareStatement(query)) {
            statement.setDouble(1, currency.getValue());
            statement.setInt(2, currency.getNominal());
            statement.setString(3, currency.getCurrencyName());
            statement.setString(4, currency.getCurrencyCode());
            statement.setString(5, currency.getDate().toString());
            statement.setInt(6, currency.getId());
            changes = statement.executeUpdate();
            if(changes > 1)
            {
                log.info("changes > 1");
            }
        }
        catch (SQLException e) {
            log.warn("error");
            throw new RuntimeException(e);
        }
        return changes;
    }

    @Override
    public void resetDbTable() {
        log.info("In method");
        deleteAll();
        Connection co = database.getConnection();
        String query = "UPDATE sqlite_sequence SET seq = 0 WHERE name = ? ;";

        try (PreparedStatement statement = co.prepareStatement(query)) {
            statement.setString(1, database.DB_NAME);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.warn("error");
            throw new RuntimeException(e);
        }
    }

    public String[][] getDataFromDB(){
        log.info("In method");
        CurrencyExchangeRepository rep = CurrencyExchangeRepositorySqliteImpl.getInstance();
        List<CurrencyExchange> currencyExchanges = rep.findAll();

        String[][] dataFromDB = new String[currencyExchanges.size()][6];
        for(int i = 0; i < currencyExchanges.size(); i++){
            dataFromDB[i][0] = currencyExchanges.get(i).getId().toString();
            dataFromDB[i][1] = currencyExchanges.get(i).getValue().toString();
            dataFromDB[i][2] = currencyExchanges.get(i).getNominal().toString();
            dataFromDB[i][3] = currencyExchanges.get(i).getCurrencyName();
            dataFromDB[i][4] = currencyExchanges.get(i).getCurrencyCode();
            dataFromDB[i][5] = currencyExchanges.get(i).getDate().toString();
        }
        log.info("exit");
        return dataFromDB;
    }
}
