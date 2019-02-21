package net.bsoftlab.service;

import net.bsoftlab.model.Currency;

import java.util.List;

public interface CurrencyService {
    void deleteCurrency(String code);
    void insertCurrency(Currency currency);
    void updateCurrency(Currency currency);
    Currency getCurrency(String code);
    List<Currency> getCurrencyList();
    List<Currency> getCurrencyList(int start, int size);
}
