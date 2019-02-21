package net.bsoftlab.resource.container;

import net.bsoftlab.resource.CurrencyResource;

import java.util.List;

public class CurrencyResourceListContainer {
    private List<CurrencyResource> currencyResourceList = null;

    public List<CurrencyResource> getCurrencyResourceList() {
        return this.currencyResourceList;
    }
    public void setCurrencyResourceList(
            List<CurrencyResource> currencyResourceList) {
        this.currencyResourceList = currencyResourceList;
    }
}
