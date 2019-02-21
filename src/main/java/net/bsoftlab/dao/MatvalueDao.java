package net.bsoftlab.dao;

import net.bsoftlab.model.Group;
import net.bsoftlab.model.Matvalue;
import net.bsoftlab.model.Unitofmsr;

import java.util.List;

public interface MatvalueDao {
    void deleteMatvalue(Matvalue matvalue);
    void insertMatvalue(Matvalue matvalue);
    void updateMatvalue(Matvalue matvalue);
    boolean existMatvalue(Group group);
    boolean existMatvalue(Unitofmsr unitofmsr);
    Matvalue getMatvalue(String code);
    List<Matvalue> getMatvalueList(int start, int size);
    List<Matvalue> getMatvalueListGroup(Group group, int start, int size);
    List<Matvalue> getMatvalueListUnitofmsr(Unitofmsr unitofmsr, int start, int size);
}
