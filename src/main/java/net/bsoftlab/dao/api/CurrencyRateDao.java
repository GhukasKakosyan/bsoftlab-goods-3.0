package net.bsoftlab.dao.api;

import net.bsoftlab.model.Currency;
import net.bsoftlab.model.CurrencyRate;

import java.util.Date;
import java.util.List;

public interface CurrencyRateDao {
    void deleteCurrencyRate(CurrencyRate currencyRate);
    void insertCurrencyRate(CurrencyRate currencyRate);
    void updateCurrencyRate(CurrencyRate currencyRate);
    boolean existCurrencyRate(Currency currency);
    CurrencyRate getCurrencyRate(Integer ID);
    CurrencyRate getCurrencyRate(Currency currency, Date date, Integer ID);
    List<CurrencyRate> getCurrencyRateList(int start, int size);
    List<CurrencyRate> getCurrencyRateList(Currency currency, int start, int size);
}