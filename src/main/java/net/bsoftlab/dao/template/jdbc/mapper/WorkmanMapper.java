package net.bsoftlab.dao.template.jdbc.mapper;

import net.bsoftlab.model.Address;
import net.bsoftlab.model.Workman;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkmanMapper implements RowMapper<Workman> {
    @Override
    public Workman mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Workman workman = new Workman();
        workman.setID(resultSet.getInt("ID"));
        workman.setName(resultSet.getString("Name"));
        workman.setPassword(resultSet.getString("Password"));
        workman.setFirstName(resultSet.getString("FirstName"));
        workman.setLastName(resultSet.getString("LastName"));
        workman.setPhones(resultSet.getString("Phones"));
        workman.setAddress(new Address());
        workman.getAddress().setStreet(resultSet.getString("Street"));
        workman.getAddress().setPincode(resultSet.getString("Pincode"));
        workman.getAddress().setCity(resultSet.getString("City"));
        workman.getAddress().setState(resultSet.getString("State"));
        workman.getAddress().setCountry(resultSet.getString("Country"));
        return workman;
    }
}
