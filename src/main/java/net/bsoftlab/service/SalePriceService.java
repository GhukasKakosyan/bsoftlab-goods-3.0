package net.bsoftlab.service;

import net.bsoftlab.model.SalePrice;

import java.util.Date;
import java.util.List;

public interface SalePriceService {
    void deleteSalePrice(Integer ID);
    void insertSalePrice(SalePrice salePrice);
    void updateSalePrice(SalePrice salePrice);
    SalePrice getSalePrice(Integer ID);
    SalePrice getSalePrice(String departmentCode, String matvalueCode, Date date);
    List<SalePrice> getSalePriceList();
    List<SalePrice> getSalePriceList(String departmentCode, String matvalueCode,
                                     String currencyCode, int start, int size);
}