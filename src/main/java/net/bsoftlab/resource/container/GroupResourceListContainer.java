package net.bsoftlab.resource.container;

import net.bsoftlab.resource.GroupResource;

import java.util.List;

public class GroupResourceListContainer {
    private List<GroupResource> groupResourceList = null;

    public List<GroupResource> getGroupResourceList() {
        return this.groupResourceList;
    }
    public void setGroupResourceList(
            List<GroupResource> groupResourceList) {
        this.groupResourceList = groupResourceList;
    }
}
