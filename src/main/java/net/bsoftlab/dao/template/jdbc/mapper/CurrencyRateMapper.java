package net.bsoftlab.dao.template.jdbc.mapper;

import net.bsoftlab.model.Currency;
import net.bsoftlab.model.CurrencyRate;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyRateMapper implements RowMapper<CurrencyRate> {
    @Override
    public CurrencyRate mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setID(resultSet.getInt("ID"));
        currencyRate.setCurrency(new Currency());
        currencyRate.getCurrency().setCode(resultSet.getString("CurCode"));
        currencyRate.getCurrency().setShortName(resultSet.getString("CurShortName"));
        currencyRate.getCurrency().setLongName(resultSet.getString("CurLongName"));
        currencyRate.getCurrency().setCountry(resultSet.getString("CurCountry"));
        currencyRate.getCurrency().setAdditionalInformation(resultSet.getString("CurAdditionalInformation"));
        currencyRate.setDate(resultSet.getDate("Date"));
        currencyRate.setRate(resultSet.getDouble("Rate"));
        currencyRate.setQuantity(resultSet.getDouble("Quantity"));
        return currencyRate;
    }
}
