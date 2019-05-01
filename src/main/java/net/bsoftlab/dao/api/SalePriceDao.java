package net.bsoftlab.dao.api;

import net.bsoftlab.model.Currency;
import net.bsoftlab.model.Department;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.SalePrice;

import java.util.Date;
import java.util.List;

public interface SalePriceDao {
    void deleteSalePrice(SalePrice salePrice);
    void insertSalePrice(SalePrice salePrice);
    void updateSalePrice(SalePrice salePrice);
    boolean existSalePrice(Currency currency);
    boolean existSalePrice(Department department);
    SalePrice getSalePrice(Integer ID);
    SalePrice getSalePrice(Department department, Matvalue matvalue, Date date, Integer ID);
    List<SalePrice> getSalePriceList(int start, int size);
    List<SalePrice> getSalePriceListCurrency(Currency currency, int start, int size);
    List<SalePrice> getSalePriceListDepartment(Department department, int start, int size);
    List<SalePrice> getSalePriceListMatvalue(Matvalue matvalue, int start, int size);
}