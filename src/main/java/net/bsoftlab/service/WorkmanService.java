package net.bsoftlab.service;

import net.bsoftlab.model.Workman;

import java.util.List;

public interface WorkmanService {
    void deleteWorkman(Integer ID);
    void insertWorkman(Workman workman);
    void updateWorkman(Workman workman);
    Workman getWorkman(Integer ID);
    Workman getWorkman(String name);
    List<Workman> getWorkmanList();
}