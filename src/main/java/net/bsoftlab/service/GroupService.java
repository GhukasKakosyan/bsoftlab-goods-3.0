package net.bsoftlab.service;

import net.bsoftlab.model.Group;

import java.util.List;

public interface GroupService {
    void deleteGroup(String code);
    void insertGroup(Group group);
    void updateGroup(Group group);
    Group getGroup(String code);
    List<Group> getGroupList();
    List<Group> getGroupList(int start, int size);
}