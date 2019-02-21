package net.bsoftlab.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MessageFactory {

    private MessageSource messageSource = null;

    @Autowired
    public void setMessageSource(
            MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Message getInternalServerErrorMessage(String error) {
        Message message = new Message();
        message.setCode("internal.server.error");
        message.setHttpStatus(500);
        message.setArmenianText("Եթե այդքան շատ ես ուզում, ապա թարգմանիր ինքդ ...");
        message.setEnglishText(error);
        return message;
    }

    public Message getMessage(String code) {
        String httpStatusCode = code + ".http.status";
        String armenianTextCode = code + ".armenian";
        String englishTextCode = code + ".english";

        int httpStatus;
        try {
            httpStatus = Integer.valueOf(
                    this.messageSource.getMessage(httpStatusCode, null,
                            "Http status is not defined for this code: " + code,
                            Locale.getDefault()));
        } catch(NumberFormatException numberFormatException) {
            httpStatus = 200;
        }
        String armenianText =
                this.messageSource.getMessage(armenianTextCode, null,
                        "Armenian text is not defined for code: " + code,
                        Locale.getDefault());
        String englishText =
                this.messageSource.getMessage(englishTextCode, null,
                        "English text is not defined for code: " + code,
                        Locale.getDefault());

        Message message = new Message();
        message.setCode(code);
        message.setHttpStatus(httpStatus);
        message.setArmenianText(armenianText);
        message.setEnglishText(englishText);
        return message;
    }
}