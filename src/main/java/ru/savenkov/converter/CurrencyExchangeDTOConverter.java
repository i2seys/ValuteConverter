package ru.savenkov.converter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.savenkov.dto.Valute;
import ru.savenkov.model.CurrencyExchange;

import java.time.LocalDate;
import java.util.*;

public class CurrencyExchangeDTOConverter  {
    private static final Logger log = LoggerFactory.getLogger(CurrencyExchangeDTOConverter.class);

    public Valute fromCurrencyExchangeToValute(CurrencyExchange currencyExchange){
        log.info("In method");
        return new Valute(
                "",
                0,
                currencyExchange.getCurrencyCode(),
                currencyExchange.getNominal(),
                currencyExchange.getCurrencyName(),
                currencyExchange.getValue());

    }

    public CurrencyExchange fromValuteToCurrencyExchange(Valute valute, Date date){
        log.info("In method");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        //Add one to month {0 - 11}
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new CurrencyExchange(
                0,
                valute.getValue(),
                valute.getNominal(),
                valute.getName(),
                valute.getCharCode(),
                LocalDate.of(year, month, day)
        );
    }
}
