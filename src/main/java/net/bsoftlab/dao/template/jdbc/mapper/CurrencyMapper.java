package net.bsoftlab.dao.template.jdbc.mapper;

import net.bsoftlab.model.Currency;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyMapper implements RowMapper<Currency> {
    @Override
    public Currency mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Currency currency = new Currency();
        currency.setCode(resultSet.getString("Code"));
        currency.setShortName(resultSet.getString("ShortName"));
        currency.setLongName(resultSet.getString("LongName"));
        currency.setCountry(resultSet.getString("Country"));
        currency.setAdditionalInformation(resultSet.getString("AdditionalInformation"));
        return currency;
    }
}
