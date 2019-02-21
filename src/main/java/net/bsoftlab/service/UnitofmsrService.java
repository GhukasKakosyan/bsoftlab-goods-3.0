package net.bsoftlab.service;

import net.bsoftlab.model.Unitofmsr;

import java.util.List;

public interface UnitofmsrService {
    void deleteUnitofmsr(String code);
    void insertUnitofmsr(Unitofmsr unitofmsr);
    void updateUnitofmsr(Unitofmsr unitofmsr);
    Unitofmsr getUnitofmsr(String code);
    List<Unitofmsr> getUnitofmsrList();
    List<Unitofmsr> getUnitofmsrList(int start, int size);
}