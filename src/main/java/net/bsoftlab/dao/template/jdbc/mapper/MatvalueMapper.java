package net.bsoftlab.dao.template.jdbc.mapper;

import net.bsoftlab.model.Group;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.Unitofmsr;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MatvalueMapper implements RowMapper<Matvalue> {
    @Override
    public Matvalue mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Matvalue matvalue = new Matvalue();
        matvalue.setCode(resultSet.getString("Code"));
        matvalue.setName(resultSet.getString("Name"));
        matvalue.setUnitofmsr(new Unitofmsr());
        matvalue.getUnitofmsr().setCode(resultSet.getString("MsrCode"));
        matvalue.getUnitofmsr().setShortName(resultSet.getString("MsrShortName"));
        matvalue.getUnitofmsr().setLongName(resultSet.getString("MsrLongName"));
        matvalue.setGroup(new Group());
        matvalue.getGroup().setCode(resultSet.getString("GrpCode"));
        matvalue.getGroup().setName(resultSet.getString("GrpName"));
        matvalue.getGroup().setEnhancedName(resultSet.getString("GrpEnhancedName"));
        return matvalue;
    }
}
