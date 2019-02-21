package net.bsoftlab.resource.converter.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.lang.IllegalArgumentException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class StringToDateConverter implements Converter<String, Date> {
    private static final String defaultDatePattern = "yyyy.MM.dd";

    private SimpleDateFormat dateFormat;
    private String datePattern = defaultDatePattern;

    @Autowired(required = false)
    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    @PostConstruct
    public void init() {
        this.dateFormat = new SimpleDateFormat(this.datePattern);
    }
    @PreDestroy
    public void destroy() {}

    @Override
    public Date convert(String dateText) throws IllegalArgumentException {
        try {
            return this.dateFormat.parse(dateText);
        } catch (ParseException parseException) {
            throw new IllegalArgumentException();
        }
    }
}
