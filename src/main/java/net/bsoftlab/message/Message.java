package net.bsoftlab.message;

public class Message {

    private String code = null;
    private Integer httpStatus = null;
    private String armenianText = null;
    private String englishText = null;

    public Message() {}

    public String getCode() {
        return this.code;
    }
    public Integer getHttpStatus() {
        return this.httpStatus;
    }
    public String getArmenianText() {
        return this.armenianText;
    }
    public String getEnglishText() {
        return this.englishText;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }
    public void setArmenianText(String armenianText) {
        this.armenianText = armenianText;
    }
    public void setEnglishText(String englishText) {
        this.englishText = englishText;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Message) {
            Message message = (Message)object;
            return this.code.equals(message.getCode()) &&
                    this.httpStatus.equals(message.getHttpStatus()) &&
                    this.armenianText.equals(message.getArmenianText()) &&
                    this.englishText.equals(message.getEnglishText());
        } else {
            return false;
        }
    }

    @Override
    public String toString () {
        return this.code + ", " +
                this.httpStatus + ", " +
                this.englishText;
    }
}