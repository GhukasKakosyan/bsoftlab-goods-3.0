package net.bsoftlab.dao.template.jdbc.mapper;

import net.bsoftlab.model.Address;
import net.bsoftlab.model.Currency;
import net.bsoftlab.model.Department;
import net.bsoftlab.model.Group;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.SalePrice;
import net.bsoftlab.model.Unitofmsr;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SalePriceMapper implements RowMapper<SalePrice> {
    @Override
    public SalePrice mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        SalePrice salePrice = new SalePrice();
        salePrice.setID(resultSet.getInt("ID"));
        salePrice.setMatvalue(new Matvalue());
        salePrice.getMatvalue().setCode(resultSet.getString("MatCode"));
        salePrice.getMatvalue().setName(resultSet.getString("MatName"));
        salePrice.getMatvalue().setUnitofmsr(new Unitofmsr());
        salePrice.getMatvalue().getUnitofmsr().setCode(resultSet.getString("MsrCode"));
        salePrice.getMatvalue().getUnitofmsr().setShortName(resultSet.getString("MsrShortName"));
        salePrice.getMatvalue().getUnitofmsr().setLongName(resultSet.getString("MsrLongName"));
        salePrice.getMatvalue().setGroup(new Group());
        salePrice.getMatvalue().getGroup().setCode(resultSet.getString("GrpCode"));
        salePrice.getMatvalue().getGroup().setName(resultSet.getString("GrpName"));
        salePrice.getMatvalue().getGroup().setEnhancedName(resultSet.getString("GrpEnhancedName"));
        salePrice.setDepartment(new Department());
        salePrice.getDepartment().setCode(resultSet.getString("DptCode"));
        salePrice.getDepartment().setName(resultSet.getString("DptName"));
        salePrice.getDepartment().setAddress(new Address());
        salePrice.getDepartment().getAddress().setStreet(resultSet.getString("DptStreet"));
        salePrice.getDepartment().getAddress().setPincode(resultSet.getString("DptPincode"));
        salePrice.getDepartment().getAddress().setCity(resultSet.getString("DptCity"));
        salePrice.getDepartment().getAddress().setState(resultSet.getString("DptState"));
        salePrice.getDepartment().getAddress().setCountry(resultSet.getString("DptCountry"));
        salePrice.getDepartment().setPhones(resultSet.getString("DptPhones"));
        salePrice.getDepartment().setFaxes(resultSet.getString("DptFaxes"));
        salePrice.getDepartment().setWebSite(resultSet.getString("DptWebSite"));
        salePrice.getDepartment().setEmailAddress(resultSet.getString("DptEmailAddress"));
        salePrice.getDepartment().setAdditionalInformation(resultSet.getString("DptAdditionalInformation"));
        salePrice.setCurrency(new Currency());
        salePrice.getCurrency().setCode(resultSet.getString("CurCode"));
        salePrice.getCurrency().setShortName(resultSet.getString("CurShortName"));
        salePrice.getCurrency().setLongName(resultSet.getString("CurLongName"));
        salePrice.getCurrency().setCountry(resultSet.getString("CurCountry"));
        salePrice.getCurrency().setAdditionalInformation(resultSet.getString("CurAdditionalInformation"));
        salePrice.setDate(resultSet.getDate("Date"));
        salePrice.setPrice(resultSet.getDouble("Price"));
        salePrice.setQuantity(resultSet.getDouble("Quantity"));
        return salePrice;
    }
}
