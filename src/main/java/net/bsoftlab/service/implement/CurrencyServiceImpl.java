package net.bsoftlab.service.implement;

import net.bsoftlab.dao.CurrencyRateDao;
import net.bsoftlab.dao.SalePriceDao;
import net.bsoftlab.dao.CurrencyDao;
import net.bsoftlab.model.Currency;
import net.bsoftlab.service.CurrencyService;
import net.bsoftlab.service.exception.ServiceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyServiceImpl implements CurrencyService {

    private static final String CurrencyNotExistMessageCode = "currency.not.exist";
    private static final String CurrencyPrimaryKeyInvalidMessageCode = "currency.primaryKey.invalid";
    private static final String CurrencyUsedInCurrencyRatesMessageCode = "currency.used.in.currencyRates";
    private static final String CurrencyUsedInSalePricesMessageCode = "currency.used.in.salePrices";

    private CurrencyDao currencyDao = null;
    private CurrencyRateDao currencyRateDao = null;
    private SalePriceDao salePriceDao = null;

    @PreDestroy
    public void destroy(){}
    @PostConstruct
    public void init(){}

    @Autowired
    @Qualifier("currencyDaoJdbcTemplate")
    public void setCurrencyDao(
            CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }
    @Autowired
    @Qualifier("currencyRateDaoJdbcTemplate")
    public void setCurrencyRateDao(
            CurrencyRateDao currencyRateDao) {
        this.currencyRateDao = currencyRateDao;
    }
    @Autowired
    @Qualifier("salePriceDaoJdbcTemplate")
    public void setSalePriceDao(
            SalePriceDao salePriceDao) {
        this.salePriceDao = salePriceDao;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void deleteCurrency(String code) throws ServiceException {
        Currency currencyPersistent = this.currencyDao.getCurrency(code);
        if (currencyPersistent == null) {
            throw new ServiceException(CurrencyNotExistMessageCode);
        }
        if (this.currencyRateDao.existCurrencyRate(currencyPersistent)) {
            throw new ServiceException(CurrencyUsedInCurrencyRatesMessageCode);
        }
        if (this.salePriceDao.existSalePrice(currencyPersistent)) {
            throw new ServiceException(CurrencyUsedInSalePricesMessageCode);
        }
        this.currencyDao.deleteCurrency(currencyPersistent);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void insertCurrency(Currency currency) throws ServiceException {
        Currency currencyPersistent = this.currencyDao.getCurrency(currency.getCode());
        if (currencyPersistent != null) {
            throw new ServiceException(CurrencyPrimaryKeyInvalidMessageCode);
        }
        this.currencyDao.insertCurrency(currency);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void updateCurrency(Currency currency) throws ServiceException {
        Currency currencyPersistent = this.currencyDao.getCurrency(currency.getCode());
        if (currencyPersistent == null) {
            throw new ServiceException(CurrencyNotExistMessageCode);
        }
        this.currencyDao.updateCurrency(currency);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public Currency getCurrency(String code) {
        return this.currencyDao.getCurrency(code);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Currency> getCurrencyList() {
        return this.getCurrencyList(0, 0);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<Currency> getCurrencyList(int start, int size) {
        if(start > 0 && size > 0) {
            return this.currencyDao.getCurrencyList(start, size);
        } else if(start == 0 && size == 0) {
            return this.currencyDao.getCurrencyList(start, size);
        } else {
            return null;
        }
    }
}