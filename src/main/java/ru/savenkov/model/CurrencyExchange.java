package ru.savenkov.model;

import java.time.LocalDate;

public class CurrencyExchange {
    public static final String ID_FIELD_NAME = "id";
    public static final String VALUE_FIELD_NAME = "value";
    public static final String NOMINAL_FIELD_NAME = "nominal";
    public static final String CURRENCY_NAME_FIELD_NAME = "currency_name";
    public static final String CURRENCY_CODE_FIELD_NAME = "currency_code";
    public static final String DATE_FIELD_NAME = "date";
    public static final String[] COLUMNS_NAME = {
            ID_FIELD_NAME,
            VALUE_FIELD_NAME,
            NOMINAL_FIELD_NAME,
            CURRENCY_NAME_FIELD_NAME,
            CURRENCY_CODE_FIELD_NAME,
            DATE_FIELD_NAME
    };
    private Integer id;
    private Double value;
    private Integer nominal;
    private String currencyName;
    private String currencyCode;
    private LocalDate date;

    public CurrencyExchange() {
    }

    public CurrencyExchange(Integer id, Double value, Integer nominal, String currencyName, String currencyCode, LocalDate date) {
        this.id = id;
        this.value = value;
        this.nominal = nominal;
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getNominal() {
        return nominal;
    }

    public void setNominal(Integer nominal) {
        this.nominal = nominal;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CurrencyExchange{");
        sb.append("id=").append(id);
        sb.append(", value=").append(value);
        sb.append(", nominal=").append(nominal);
        sb.append(", currencyName='").append(currencyName).append('\'');
        sb.append(", currencyCode='").append(currencyCode).append('\'');
        sb.append(", date=").append(date);
        sb.append('}');
        return sb.toString();
    }
}
