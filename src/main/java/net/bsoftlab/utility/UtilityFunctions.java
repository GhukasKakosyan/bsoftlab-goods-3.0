package net.bsoftlab.utility;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UtilityFunctions {

    public String getPrintStackTrace (Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public int calculateFirst(int start, int size, int realSize) {
        int first = 0;
        if(start > 0 && size > 0 && start < realSize) {
            first = start - 1;
        }
        return first;
    }

    public int calculateQuantity(int start, int size, int realSize) {
        int quantity = 0;
        if(start > 0 && size > 0 && start < realSize) {
            if(start + size > realSize) {
                quantity = realSize - start;
            } else {
                quantity = size;
            }
        } else if(start == 0 && size == 0) {
            quantity = realSize;
        }
        return quantity;
    }
}