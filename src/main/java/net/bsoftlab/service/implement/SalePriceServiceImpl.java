package net.bsoftlab.service.implement;

import net.bsoftlab.dao.CurrencyDao;
import net.bsoftlab.dao.DepartmentDao;
import net.bsoftlab.dao.MatvalueDao;
import net.bsoftlab.dao.SalePriceDao;
import net.bsoftlab.model.Currency;
import net.bsoftlab.model.Department;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.SalePrice;
import net.bsoftlab.service.SalePriceService;
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
public class SalePriceServiceImpl implements SalePriceService {

    private static final String SalePriceNotExistMessageCode = "salePrice.not.exist";
    private static final String CurrencyNotExistMessageCode = "currency.not.exist";
    private static final String DepartmentNotExistMessageCode = "department.not.exist";
    private static final String MatvalueNotExistMessageCode = "matvalue.not.exist";
    private static final String SalePriceUniqueKeyInvalidMessageCode = "salePrice.uniqueKey.invalid";

    private CurrencyDao currencyDao = null;
    private DepartmentDao departmentDao = null;
    private MatvalueDao matvalueDao = null;
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
    @Qualifier("departmentDaoJdbcTemplate")
    public void setDepartmentDao(
            DepartmentDao departmentDao) {
        this.departmentDao = departmentDao;
    }
    @Autowired
    @Qualifier("matvalueDaoJdbcTemplate")
    public void setMatvalueDao(
            MatvalueDao matvalueDao) {
        this.matvalueDao = matvalueDao;
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
    public void deleteSalePrice(Integer ID) throws ServiceException {
        SalePrice salePricePersistent = this.salePriceDao.getSalePrice(ID);
        if(salePricePersistent == null) {
            throw new ServiceException(SalePriceNotExistMessageCode);
        }
        this.salePriceDao.deleteSalePrice(salePricePersistent);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void insertSalePrice(SalePrice salePrice) throws ServiceException {
        String currencyCode = salePrice.getCurrency().getCode();
        Currency currencyPersistent = this.currencyDao.getCurrency(currencyCode);
        if(currencyPersistent == null ||
                !currencyPersistent.equals(salePrice.getCurrency())) {
            throw new ServiceException(CurrencyNotExistMessageCode);
        }

        String departmentCode = salePrice.getDepartment().getCode();
        Department departmentPersistent = this.departmentDao.getDepartment(departmentCode);
        if(departmentPersistent == null ||
                !departmentPersistent.equals(salePrice.getDepartment())) {
            throw new ServiceException(DepartmentNotExistMessageCode);
        }

        String matvalueCode = salePrice.getMatvalue().getCode();
        Matvalue matvaluePersistent = this.matvalueDao.getMatvalue(matvalueCode);
        if(matvaluePersistent == null ||
                !matvaluePersistent.equals(salePrice.getMatvalue())) {
            throw new ServiceException(MatvalueNotExistMessageCode);
        }

        SalePrice salePricePersistent = this.salePriceDao.getSalePrice(
                salePrice.getDepartment(),
                salePrice.getMatvalue(),
                salePrice.getDate(),
                salePrice.getID());
        if (salePricePersistent != null) {
            throw new ServiceException(SalePriceUniqueKeyInvalidMessageCode);
        }

        this.salePriceDao.insertSalePrice(salePrice);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class, timeout = 10)
    public void updateSalePrice(SalePrice salePrice) throws ServiceException {

        SalePrice salePricePersistent = this.salePriceDao.getSalePrice(salePrice.getID());
        if (salePricePersistent == null) {
            throw new ServiceException(SalePriceNotExistMessageCode);
        }

        String currencyCode = salePrice.getCurrency().getCode();
        Currency currencyPersistent = this.currencyDao.getCurrency(currencyCode);
        if(currencyPersistent == null ||
                !currencyPersistent.equals(salePrice.getCurrency())) {
            throw new ServiceException(CurrencyNotExistMessageCode);
        }

        String departmentCode = salePrice.getDepartment().getCode();
        Department departmentPersistent = this.departmentDao.getDepartment(departmentCode);
        if(departmentPersistent == null ||
                !departmentPersistent.equals(salePrice.getDepartment())) {
            throw new ServiceException(DepartmentNotExistMessageCode);
        }

        String matvalueCode = salePrice.getMatvalue().getCode();
        Matvalue matvaluePersistent = this.matvalueDao.getMatvalue(matvalueCode);
        if(matvaluePersistent == null ||
                !matvaluePersistent.equals(salePrice.getMatvalue())) {
            throw new ServiceException(MatvalueNotExistMessageCode);
        }

        SalePrice duplicateSalePrice = this.salePriceDao.getSalePrice(
                salePrice.getDepartment(),
                salePrice.getMatvalue(),
                salePrice.getDate(),
                salePrice.getID());
        if (duplicateSalePrice != null) {
            throw new ServiceException(SalePriceUniqueKeyInvalidMessageCode);
        }
        this.salePriceDao.updateSalePrice(salePrice);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public SalePrice getSalePrice(Integer ID) {
        return this.salePriceDao.getSalePrice(ID);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public SalePrice getSalePrice(String departmentCode, String matvalueCode, Date date)
            throws ServiceException {

        Department departmentPersistent =
                this.departmentDao.getDepartment(departmentCode);
        if(departmentPersistent == null) {
            throw new ServiceException(DepartmentNotExistMessageCode);
        }

        Matvalue matvaluePersistent = this.matvalueDao.getMatvalue(matvalueCode);
        if(matvaluePersistent == null) {
            throw new ServiceException(MatvalueNotExistMessageCode);
        }
        return this.salePriceDao.getSalePrice(
                departmentPersistent, matvaluePersistent, date, 0);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<SalePrice> getSalePriceList() {
        return this.salePriceDao.getSalePriceList(0, 0);
    }

    @Override
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            readOnly = true, rollbackFor = ServiceException.class, timeout = 10)
    public List<SalePrice> getSalePriceList(
            String departmentCode, String matvalueCode,
            String currencyCode, int start, int size) throws ServiceException {

        Department departmentPersistent = null;
        if (departmentCode != null && !departmentCode.isEmpty()) {
            departmentPersistent = this.departmentDao.getDepartment(departmentCode);
            if (departmentPersistent == null) {
                throw new ServiceException(DepartmentNotExistMessageCode);
            }
        }
        Matvalue matvaluePersistent = null;
        if (matvalueCode != null && !matvalueCode.isEmpty()) {
            matvaluePersistent = this.matvalueDao.getMatvalue(matvalueCode);
            if (matvaluePersistent == null) {
                throw new ServiceException(MatvalueNotExistMessageCode);
            }
        }
        Currency currencyPersistent = null;
        if (currencyCode != null && !currencyCode.isEmpty()) {
            currencyPersistent = this.currencyDao.getCurrency(currencyCode);
            if(currencyPersistent == null) {
                throw new ServiceException(CurrencyNotExistMessageCode);
            }
        }

        if((currencyCode != null && !currencyCode.isEmpty())
                && (departmentCode == null || departmentCode.isEmpty())
                && (matvalueCode == null || matvalueCode.isEmpty())
                && (start > 0 && size > 0)) {
            return this.salePriceDao.getSalePriceListCurrency(currencyPersistent, start, size);
        } else if((currencyCode != null && !currencyCode.isEmpty())
                && (departmentCode == null || departmentCode.isEmpty())
                && (matvalueCode == null || matvalueCode.isEmpty())
                && (start == 0 && size == 0)) {
            return this.salePriceDao.getSalePriceListCurrency(currencyPersistent, start, size);
        } else if((currencyCode == null || currencyCode.isEmpty())
                && (departmentCode != null && !departmentCode.isEmpty())
                && (matvalueCode == null || matvalueCode.isEmpty()) && (start > 0 && size > 0)) {
            return this.salePriceDao.getSalePriceListDepartment(departmentPersistent, start, size);
        } else if((currencyCode == null || currencyCode.isEmpty())
                && (departmentCode != null && !departmentCode.isEmpty())
                && (matvalueCode == null || matvalueCode.isEmpty()) && (start == 0 && size == 0)) {
            return this.salePriceDao.getSalePriceListDepartment(departmentPersistent, start, size);
        } else if((currencyCode == null || currencyCode.isEmpty())
                && (departmentCode == null || departmentCode.isEmpty())
                && (matvalueCode != null && !matvalueCode.isEmpty())
                && (start > 0 && size > 0)) {
            return this.salePriceDao.getSalePriceListMatvalue(matvaluePersistent, start, size);
        } else if((currencyCode == null || currencyCode.isEmpty())
                && (departmentCode == null || departmentCode.isEmpty())
                && (matvalueCode != null && !matvalueCode.isEmpty())
                && (start == 0 && size == 0)) {
            return this.salePriceDao.getSalePriceListMatvalue(matvaluePersistent, start, size);
        } else if((currencyCode == null || currencyCode.isEmpty())
                && (departmentCode == null || departmentCode.isEmpty())
                && (matvalueCode == null || matvalueCode.isEmpty())
                && (start > 0 && size > 0)) {
            return this.salePriceDao.getSalePriceList(start, size);
        } else if((currencyCode == null || currencyCode.isEmpty())
                && (departmentCode == null || departmentCode.isEmpty())
                && (matvalueCode == null || matvalueCode.isEmpty())
                && (start == 0 && size == 0)) {
            return this.salePriceDao.getSalePriceList(start, size);
        } else {
            return null;
        }
    }
}