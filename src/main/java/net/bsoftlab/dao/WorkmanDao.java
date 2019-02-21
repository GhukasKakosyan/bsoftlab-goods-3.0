package net.bsoftlab.dao;

import net.bsoftlab.model.Workman;

import java.util.List;

public interface WorkmanDao {
    void deleteWorkman(Workman workman);
    void insertWorkman(Workman workman);
    void updateWorkman(Workman workman);
    Workman getWorkman(Integer ID);
    Workman getWorkman(Integer ID, String name);
    List<Workman> getWorkmanList();
}