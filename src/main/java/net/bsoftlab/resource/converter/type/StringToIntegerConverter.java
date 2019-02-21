package net.bsoftlab.resource.converter.type;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Integer convert(String source) throws IllegalArgumentException {
        try {
            return Integer.valueOf(source);
        } catch (NumberFormatException numberFormatException) {
            throw new IllegalArgumentException();
        }
    }
}
