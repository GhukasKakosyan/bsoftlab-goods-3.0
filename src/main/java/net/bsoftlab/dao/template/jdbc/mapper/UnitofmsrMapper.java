package net.bsoftlab.dao.template.jdbc.mapper;

import net.bsoftlab.model.Unitofmsr;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UnitofmsrMapper implements RowMapper<Unitofmsr> {
    @Override
    public Unitofmsr mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Unitofmsr unitofmsr = new Unitofmsr();
        unitofmsr.setCode(resultSet.getString("Code"));
        unitofmsr.setShortName(resultSet.getString("ShortName"));
        unitofmsr.setLongName(resultSet.getString("LongName"));
        return unitofmsr;
    }
}
