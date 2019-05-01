package net.bsoftlab.dao.api;

import net.bsoftlab.model.Group;

import java.util.List;

public interface GroupDao {
    void deleteGroup(Group group);
    void insertGroup(Group group);
    void updateGroup(Group group);
    Group getGroup(String code);
    List<Group> getGroupList(int start, int size);
}
