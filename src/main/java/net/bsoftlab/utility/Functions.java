package net.bsoftlab.utility;

import java.io.PrintWriter;
import java.io.StringWriter;

public interface Functions {
    static String getPrintStackTrace (Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    static int calculateFirst(int start, int size, int realSize) {
        int first = 0;
        if(start > 0 && size > 0 && start < realSize) {
            first = start - 1;
        }
        return first;
    }

    static int calculateQuantity(int start, int size, int realSize) {
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
