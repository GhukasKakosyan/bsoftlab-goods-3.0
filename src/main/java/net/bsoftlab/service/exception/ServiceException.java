package net.bsoftlab.service.exception;

import net.bsoftlab.message.Message;

@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {
    private Message errorMessage = null;

    public ServiceException(String messageCode) {
        super(messageCode);
    }
    public ServiceException(Message errorMessage) {
        super(errorMessage.getEnglishText());
        this.errorMessage = errorMessage;
    }
    public Message getErrorMessage() {
        return this.errorMessage;
    }
}
