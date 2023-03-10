package ru.savenkov.mainFrameAndDBConnection;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RestAdapter;
import retrofit.converter.JacksonConverter;
import ru.savenkov.cbrService.CbrService;
import ru.savenkov.converter.CurrencyExchangeDTOConverter;
import ru.savenkov.dto.ValCurs;
import ru.savenkov.mainWindow.MainFrame;
import ru.savenkov.model.CurrencyExchange;
import ru.savenkov.repository.CurrencyExchangeRepository;
import ru.savenkov.repository.CurrencyExchangeRepositorySqliteImpl;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFrameAndDBConnector implements JTableAndDBConnector, JMenuBarAndDBConnector {
    private static final Logger log = LoggerFactory.getLogger(MainFrameAndDBConnector.class);
    private static MainFrameAndDBConnector instance;
    public static synchronized MainFrameAndDBConnector getInstance(){
        log.info("In method");
        if(instance == null){
            log.info("instance == null");
            instance = new MainFrameAndDBConnector();
            log.info("instance created");
        }
        return instance;
    }
    private MainFrameAndDBConnector(){}

    @Override
    public void deleteAll(MainFrame mainFrame) {
        log.info("In method");
        CurrencyExchangeRepository rep = CurrencyExchangeRepositorySqliteImpl.getInstance();
        rep.deleteAll();
        updateDataInTableFromDB(mainFrame);
        log.info("exit");
    }
    @Override
    public void deleteById(MainFrame mainFrame) {
        log.info("In  MainFrameAndDBConnector.deleteById");
        String inputId = (String)JOptionPane.showInputDialog(
                mainFrame,
                "?????????????? id ????????????:",
                "???????? id",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        //???????????????? ?????????????? ????????????
        if(inputId == null || inputId.isEmpty() || Integer.parseInt(inputId) < 0){
            JOptionPane.showMessageDialog(mainFrame, "Id ???????????? ??????????????????????");
            log.warn("incorrect id");
            return;
        }
        CurrencyExchangeRepository rep = CurrencyExchangeRepositorySqliteImpl.getInstance();

        int deleted = rep.delete(Integer.parseInt(inputId));
        if(deleted == 0){
            JOptionPane.showMessageDialog(mainFrame, "????????????: ???????????? ???? ???????? ??????????????.");
            log.warn("can't delete row");
        }
        else if (deleted > 1){
            JOptionPane.showMessageDialog(mainFrame, "????????????: ?????????????? ???????????? 1 ????????.");
            log.warn("deleted more than 1 row");
        }
        updateDataInTableFromDB(mainFrame);
        log.info("exit");
    }

    @Override
    public void resetTable(MainFrame mainFrame) {
        log.info("In method");
        CurrencyExchangeRepository rep = CurrencyExchangeRepositorySqliteImpl.getInstance();
        rep.resetDbTable();
        updateDataInTableFromDB(mainFrame);
        log.info("exit");
    }
    @Override
    public void appendDataFromCBR(MainFrame mainFrame) {
        log.info("In method");
        String date = (String)JOptionPane.showInputDialog(
                mainFrame,
                "?????????????? ???????? ?? ?????????????? dd/MM/yyyy:",
                "???????? ????????",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        if(!isInputDateCorrect(date)){
            log.warn("data not correct");
            return;
        }


        List<CurrencyExchange> currencyExchanges = getDataFromCBR(date);
        //???????????????????????????? ????
        CurrencyExchangeRepository rep = CurrencyExchangeRepositorySqliteImpl.getInstance();

        for(int i = 0; i < currencyExchanges.size(); i++){
            rep.insert(currencyExchanges.get(i));
        }
        updateDataInTableFromDB(mainFrame);
        log.info("data inserted");
        log.info("exit");

    }

    private boolean isInputDateCorrect(String date) {
        if(date == null || date.isEmpty()){
            return false;
        }
        final String datePatern =
                "(0[1-9]|[12][0-9]|3[01])[/](0?[1-9]|1[012])[/]((19|20)\\d\\d)";
        Pattern pattern = Pattern.compile(datePatern);
        Matcher matcher = pattern.matcher(date);

        if(matcher.matches()){
            matcher.reset();

            if(matcher.find()){
                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));
                LocalDate inputDate = LocalDate.of(year, Integer.parseInt(month), Integer.parseInt(day));
                LocalDate minDate = LocalDate.of(1992, 7, 1);//???????????? ???????? ???????? ???? ???? ?????? ????????????
                if(inputDate.compareTo(LocalDate.now()) > 0 || inputDate.compareTo(minDate) < 0){
                    return false;
                }
                if (day.equals("31") &&
                        (month.equals("4") || month .equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month .equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                }
                else if (month.equals("2") || month.equals("02")) {
                    //???????????????????? ??????
                    if(year % 4==0){
                        if(day.equals("30") || day.equals("31")){
                            return false;
                        }
                        else{
                            return true;
                        }
                    }
                    else{
                        if(day.equals("29")||day.equals("30")||day.equals("31")){
                            return false;
                        }
                        else{
                            return true;
                        }
                    }
                }
                else{
                    return true;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }

    }

    @Override
    public void csvExport(MainFrame mainFrame) {
        log.info("In method");
        JFileChooser chooser = new JFileChooser();
        String csvPath = "";
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                ".csv", "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(mainFrame);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            csvPath = chooser.getSelectedFile().getAbsolutePath() + ".csv";
        }
        else {
            log.info("returnVal not correct");
            return;
        }
        CurrencyExchangeRepository rep = CurrencyExchangeRepositorySqliteImpl.getInstance();
        List<CurrencyExchange> exchanges = rep.findAll();

        File csvFile = new File(csvPath);
        if(!csvFile.exists()){
            try {
                log.info("creating csv file...");
                csvFile.createNewFile();
                log.info("csv file created");
            } catch (IOException e) {
                log.warn("can't create csv file");
                throw new RuntimeException(e);
            }
        }

        //???????????????????? ???????????? ?? csv
        try {
            PrintWriter printWriter = new PrintWriter(csvPath, StandardCharsets.UTF_8);
            CSVPrinter csvPrinter = CSVFormat.DEFAULT.withHeader (
                    CurrencyExchange.ID_FIELD_NAME,
                    CurrencyExchange.VALUE_FIELD_NAME,
                    CurrencyExchange.NOMINAL_FIELD_NAME,
                    CurrencyExchange.CURRENCY_NAME_FIELD_NAME,
                    CurrencyExchange.CURRENCY_CODE_FIELD_NAME,
                    CurrencyExchange.DATE_FIELD_NAME).print(printWriter);
            for (CurrencyExchange exchange : exchanges) {
                csvPrinter.printRecord(
                        exchange.getId(),
                        exchange.getValue(),
                        exchange.getNominal(),
                        exchange.getCurrencyName(),
                        exchange.getCurrencyCode(),
                        exchange.getDate());
            }
            csvPrinter.flush();
            csvPrinter.close();
            JOptionPane.showMessageDialog(mainFrame, "?????????????? ?? CSV ?????????????????? ??????????????");
            log.info("successful export in csv");
        } catch (IOException e) {
            log.warn("can't export in csv");
            throw new RuntimeException(e);
        }

    }

    @Override
    public void jsonExport(MainFrame mainFrame) {
        log.info("In method");
        JFileChooser chooser = new JFileChooser();
        String jsonPath = "";
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                ".json", "json");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(mainFrame);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            jsonPath = chooser.getSelectedFile().getAbsolutePath() + ".json";
        }
        else {
            log.info("returnVal not correct");
            return;
        }

        CurrencyExchangeRepository rep = CurrencyExchangeRepositorySqliteImpl.getInstance();
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        //???????????????? ?????? ???????????? ???? ????
        List<CurrencyExchange> exchanges = rep.findAll();

        // ???????????? ?? ?????????????? JSON ?? ??????????
        File jsonFile = new File(jsonPath);

        //???????? ?????????? ??????, ???? ??????????????
        if(!jsonFile.exists()){
            try {
                jsonFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //???????????????????? ???????????? ?? json
        try {
            mapper.writeValue(new File(jsonPath), exchanges);
            JOptionPane.showMessageDialog(mainFrame, "?????????????? ?? Json ?????????????????? ??????????????");
            log.info("successful export in json");
        } catch (IOException e) {
            log.info("can't export in json");
            throw new RuntimeException(e);
        }
    }
    private List<CurrencyExchange> getDataFromCBR(String inputDate){
        log.info("In method");
        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint("https://www.cbr.ru/")//???????????? ???????????????? ?? ???????????????? ??????
                .setConverter(new JacksonConverter(new XmlMapper())) // ?????????????????????? ?????????????????? ?????? ?????????????????? XML (?????????????????? ???? xml)
                .build();

        CbrService cbrService = retrofit.create(CbrService.class);//?????????????? ?????????? ?????????? cbrService ?? ?????????????? https://www.cbr.ru/scripts/XML_daily.asp

        ValCurs exchange = cbrService.getExchange(inputDate);

        CurrencyExchangeDTOConverter c = new CurrencyExchangeDTOConverter();
        List<CurrencyExchange> currencyExchanges = new ArrayList<>(exchange.getValuteList().size());
        for(int i = 0; i < exchange.getValuteList().size(); i++){//???????????????????????? ???????????? ?? ???????????? CurrencyExchange
            currencyExchanges.add(c.fromValuteToCurrencyExchange(exchange.getValuteList().get(i), exchange.getDate()));
        }
        log.info("exit");
        return currencyExchanges;
    }
    private void updateDataInTableFromDB(MainFrame mainFrame){
        //???????????????????? ?????? ???????????? ???? ????
        log.info("In method");
        CurrencyExchangeRepository rep = CurrencyExchangeRepositorySqliteImpl.getInstance();
        List<CurrencyExchange> exchangesFromDB = rep.findAll();

        //???????????????????? ?????? ???????????? ???????????? ??????????

        String[][] dataFromDB = ((CurrencyExchangeRepositorySqliteImpl)rep).getDataFromDB();

        //?????????????????? ???????????? ?? ??????????????
        mainFrame.setTable(new JTable(dataFromDB, CurrencyExchange.COLUMNS_NAME));
        log.info("exit");
    }
}
