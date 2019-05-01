package net.bsoftlab.dao.api;

import net.bsoftlab.model.Unitofmsr;

import java.util.List;

public interface UnitofmsrDao {
    void deleteUnitofmsr(Unitofmsr unitofmsr);
    void insertUnitofmsr(Unitofmsr unitofmsr);
    void updateUnitofmsr(Unitofmsr unitofmsr);
    Unitofmsr getUnitofmsr(String code);
    List<Unitofmsr> getUnitofmsrList(int start, int size);
}
