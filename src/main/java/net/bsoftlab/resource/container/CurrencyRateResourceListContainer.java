package net.bsoftlab.resource.container;

import net.bsoftlab.resource.CurrencyRateResource;
import java.util.List;

public class CurrencyRateResourceListContainer {
    private List<CurrencyRateResource> currencyRateResourceList = null;

    public List<CurrencyRateResource> getCurrencyRateResourceList() {
        return this.currencyRateResourceList;
    }
    public void setCurrencyRateResourceList(
            List<CurrencyRateResource> currencyRateResourceList) {
        this.currencyRateResourceList = currencyRateResourceList;
    }
}
