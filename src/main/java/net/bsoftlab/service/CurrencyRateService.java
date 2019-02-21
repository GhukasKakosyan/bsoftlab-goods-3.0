package net.bsoftlab.service;

import net.bsoftlab.model.CurrencyRate;

import java.util.Date;
import java.util.List;

public interface CurrencyRateService {
    void deleteCurrencyRate(Integer ID);
    void insertCurrencyRate(CurrencyRate currencyRate);
    void updateCurrencyRate(CurrencyRate currencyRate);
    CurrencyRate getCurrencyRate(Integer ID);
    CurrencyRate getCurrencyRate(String currencyCode, Date date);
    List<CurrencyRate> getCurrencyRateList();
    List<CurrencyRate> getCurrencyRateList(String currencyCode, int start, int size);
}