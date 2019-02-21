package net.bsoftlab.resource.converter.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.text.DecimalFormat;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DoubleToStringConverter implements Converter <Double, String> {
    private static final String DefaultAmountFormatPattern = "#.000";

    private DecimalFormat decimalFormat;
    private String decimalFormatPattern = DefaultAmountFormatPattern;

    @Autowired(required = false)
    public void setDecimalFormatPattern(
            String decimalFormatPattern) {
        this.decimalFormatPattern = decimalFormatPattern;
    }

    @PostConstruct
    public void init() {
        this.decimalFormat = new DecimalFormat(this.decimalFormatPattern);
    }
    @PreDestroy
    public void destroy() {}

    @Override
    public String convert(Double source) {
        return this.decimalFormat.format(source);
    }
}
