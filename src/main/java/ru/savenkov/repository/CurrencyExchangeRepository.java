package ru.savenkov.repository;

import ru.savenkov.model.CurrencyExchange;

import java.util.List;

public interface CurrencyExchangeRepository {
    public CurrencyExchange findById(Integer id);
    public List<CurrencyExchange> findAll();
    public List<CurrencyExchange> findAllByCode(String currencyCode);
    public Integer delete(Integer id);
    public void deleteAll();
    public Integer insert(CurrencyExchange currencyExchange);
    public Integer update(CurrencyExchange currencyExchange);
    public void resetDbTable();
}
