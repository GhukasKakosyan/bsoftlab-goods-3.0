package net.bsoftlab.service;

import net.bsoftlab.model.Matvalue;

import java.util.List;

public interface MatvalueService {
    void deleteMatvalue(String code);
    void insertMatvalue(Matvalue matvalue);
    void updateMatvalue(Matvalue matvalue);
    Matvalue getMatvalue(String code);
    List<Matvalue> getMatvalueList();
    List<Matvalue> getMatvalueList(String groupCode, String unitofmsrCode, int start, int size);
}