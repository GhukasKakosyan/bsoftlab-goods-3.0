package net.bsoftlab.service.implement;

import net.bsoftlab.dao.CurrencyDao;
import net.bsoftlab.dao.CurrencyRateDao;
import net.bsoftlab.model.Currency;
import net.bsoftlab.model.CurrencyRate;
import net.bsoftlab.service.CurrencyRateService;
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

import java.util.Date;
import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private static final String CurrencyNotExistMessageCode = "currency.not.exist";
    private static final String CurrencyRateNotExistMessageCode = "currencyRate.not.exist";
    private static final String CurrencyRateUniqueKeyInvalidMessageCode = "currencyRate.uniqueKey.invalid";

    private CurrencyDao currencyDao = null;
    private CurrencyRateDao currencyRateDao = null;

    @PreDestroy
    public void destroy() {}
    @PostConstruct
    public void init() {}

    @Autowired
    @Qualifier("currencyDaoJpaTemplate")
    public void setCurrencyDao(CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }
    @Autowired
    @Qualifier("currencyRateDaoJpaTemplate")
    public void setCurrencyRateDao(CurrencyRateDao currencyRateDao) {
        this.currencyRateDao = currencyRateDao;
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void deleteCurrencyRate(Integer ID) throws ServiceException {

        CurrencyRate currencyRatePersistent = this.currencyRateDao.getCurrencyRate(ID);
        if (currencyRatePersistent == null) {
            throw new ServiceException(CurrencyRateNotExistMessageCode);
        }
        this.currencyRateDao.deleteCurrencyRate(currencyRatePersistent);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void insertCurrencyRate(CurrencyRate currencyRate) throws ServiceException {

        Currency currencyPersistent =
                this.currencyDao.getCurrency(currencyRate.getCurrency().getCode());
        if(currencyPersistent == null ||
                !currencyPersistent.equals(currencyRate.getCurrency())) {
            throw new ServiceException(CurrencyNotExistMessageCode);
        }

        CurrencyRate currencyRatePersistent = this.currencyRateDao.getCurrencyRate(
                currencyRate.getCurrency(),
                currencyRate.getDate(),
                currencyRate.getID());
        if (currencyRatePersistent != null) {
            throw new ServiceException(CurrencyRateUniqueKeyInvalidMessageCode);
        }
        this.currencyRateDao.insertCurrencyRate(currencyRate);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void updateCurrencyRate(CurrencyRate currencyRate) throws ServiceException {

        CurrencyRate currencyRatePersistent =
                this.currencyRateDao.getCurrencyRate(currencyRate.getID());
        if(currencyRatePersistent == null) {
            throw new ServiceException(CurrencyRateNotExistMessageCode);
        }

        Currency currencyPersistent =
                this.currencyDao.getCurrency(currencyRate.getCurrency().getCode());
        if(currencyPersistent == null ||
                !currencyPersistent.equals(currencyRate.getCurrency())) {
            throw new ServiceException(CurrencyNotExistMessageCode);
        }

        CurrencyRate duplicateCurrencyRate = this.currencyRateDao.getCurrencyRate(
                currencyRate.getCurrency(),
                currencyRate.getDate(),
                currencyRate.getID());
        if (duplicateCurrencyRate != null) {
            throw new ServiceException(CurrencyRateUniqueKeyInvalidMessageCode);
        }
        this.currencyRateDao.updateCurrencyRate(currencyRate);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public CurrencyRate getCurrencyRate(Integer ID) {
        return this.currencyRateDao.getCurrencyRate(ID);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public CurrencyRate getCurrencyRate (String currencyCode, Date date)
            throws ServiceException {

        Currency currencyPersistent = this.currencyDao.getCurrency(currencyCode);
        if(currencyPersistent == null) {
            throw new ServiceException(CurrencyNotExistMessageCode);
        }
        return this.currencyRateDao.getCurrencyRate(currencyPersistent, date, 0);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<CurrencyRate> getCurrencyRateList() {
        return this.currencyRateDao.getCurrencyRateList(0, 0);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<CurrencyRate> getCurrencyRateList(String currencyCode, int start, int size)
            throws ServiceException {

        Currency currencyPersistent = null;
        if (currencyCode != null && !currencyCode.isEmpty()) {
            currencyPersistent = this.currencyDao.getCurrency(currencyCode);
            if (currencyPersistent == null) {
                throw new ServiceException(CurrencyNotExistMessageCode);
            }
        }

        if((currencyCode != null && !currencyCode.isEmpty())
                && (start > 0 && size > 0)) {
            return this.currencyRateDao.getCurrencyRateList(currencyPersistent, start, size);
        } else if((currencyCode != null && !currencyCode.isEmpty())
                && (start == 0 && size == 0)) {
            return this.currencyRateDao.getCurrencyRateList(currencyPersistent, start, size);
        } else if((currencyCode == null || currencyCode.isEmpty())
                && (start > 0 && size > 0)) {
            return this.currencyRateDao.getCurrencyRateList(start, size);
        } else if((currencyCode == null || currencyCode.isEmpty())
                && (start == 0 && size == 0)) {
            return this.currencyRateDao.getCurrencyRateList(start, size);
        } else {
            return null;
        }
    }
}