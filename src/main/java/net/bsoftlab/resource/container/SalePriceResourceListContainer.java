package net.bsoftlab.resource.container;

import net.bsoftlab.resource.SalePriceResource;

import java.util.List;

public class SalePriceResourceListContainer {
    private List<SalePriceResource> salePriceResourceList = null;

    public List<SalePriceResource> getSalePriceResourceList() {
        return this.salePriceResourceList;
    }
    public void setSalePriceResourceList(
            List<SalePriceResource> salePriceResourceList) {
        this.salePriceResourceList = salePriceResourceList;
    }
}
