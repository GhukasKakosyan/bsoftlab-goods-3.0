package net.bsoftlab.resource.container;

import net.bsoftlab.resource.MatvalueResource;

import java.util.List;

public class MatvalueResourceListContainer {
    private List<MatvalueResource> matvalueResourceList = null;

    public List<MatvalueResource> getMatvalueResourceList() {
        return this.matvalueResourceList;
    }
    public void setMatvalueResourceList(
            List<MatvalueResource> matvalueResourceList) {
        this.matvalueResourceList = matvalueResourceList;
    }
}
