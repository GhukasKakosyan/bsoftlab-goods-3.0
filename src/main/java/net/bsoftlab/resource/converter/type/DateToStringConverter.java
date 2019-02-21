package net.bsoftlab.resource.converter.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DateToStringConverter implements Converter<Date, String> {
    private static final String DefaultDatePattern = "yyyy.MM.dd";

    private SimpleDateFormat dateFormat;
    private String datePattern = DefaultDatePattern;

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
    public String convert(Date date) {
        return this.dateFormat.format(date);
    }
}
