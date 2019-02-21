package net.bsoftlab.dao;

import net.bsoftlab.model.Currency;

import java.util.List;

public interface CurrencyDao {
    void deleteCurrency(Currency currency);
    void insertCurrency(Currency currency);
    void updateCurrency(Currency currency);
    Currency getCurrency(String code);
    List<Currency> getCurrencyList(int start, int size);
}