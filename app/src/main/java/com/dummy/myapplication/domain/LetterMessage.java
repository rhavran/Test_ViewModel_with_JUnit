package com.dummy.myapplication.domain;

import java.util.Objects;

public class LetterMessage {
    private String recipientName = "";
    private String recipientAddress = "";
    private String message = "";
    private String authorSignature = "";

    void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    void setMessage(String message) {
        this.message = message;
    }

    void setAuthorSignature(String authorSignature) {
        this.authorSignature = authorSignature;
    }

    void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    String getRecipientName() {
        return recipientName;
    }

    String getRecipientAddress() {
        return recipientAddress;
    }

    String getMessage() {
        return message;
    }

    String getAuthorSignature() {
        return authorSignature;
    }

    public boolean hasEmptyRecipient() {
        return getRecipientName() == null || getRecipientName().trim().isEmpty()
                || getRecipientAddress() == null || getRecipientAddress().trim().isEmpty();
    }

    public boolean hasEmptyMessage() {
        return getMessage() == null || getMessage().trim().isEmpty();
    }

    public String getFormattedLetter() {
        return String.format("%s,\n\n" + // Greetings
                        "%s\n\n" + // Body
                        "%s",// Signature
                getRecipientName().trim(),
                getMessage().trim(),
                getAuthorSignature().trim());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LetterMessage that = (LetterMessage) o;
        return Objects.equals(recipientName, that.recipientName) &&
                Objects.equals(message, that.message) &&
                Objects.equals(authorSignature, that.authorSignature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipientName, message, authorSignature);
    }
}
