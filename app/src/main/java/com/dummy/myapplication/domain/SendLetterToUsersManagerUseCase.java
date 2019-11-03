package com.dummy.myapplication.domain;

import com.dummy.myapplication.controller.EmailSender;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Supplier;

public class SendLetterToUsersManagerUseCase {

    private FormatLetterToUsersManagerUseCase formatLetterToUsersManagerUseCase;
    private EmailSender sender;

    public SendLetterToUsersManagerUseCase(FormatLetterToUsersManagerUseCase formatLetterToUsersManagerUseCase, EmailSender emailSender) {
        this.formatLetterToUsersManagerUseCase = formatLetterToUsersManagerUseCase;
        this.sender = emailSender;
    }

    public Single<String> sendLetter(final String textOfTheMessage) {
        return Single.fromPublisher(
                formatLetterToUsersManagerUseCase.formatLetterToCurrentManager(textOfTheMessage)
                        .take(1)
                        .flatMapSingle(new Function<LetterMessage, SingleSource<String>>() {
                            @Override
                            public SingleSource<String> apply(final LetterMessage letterMessage) {
                                if (letterMessage.hasEmptyMessage() || letterMessage.hasEmptyRecipient()) {
                                    return Single.error(new Exception("hasEmptyRecipient || hasEmptyMessage"));
                                }

                                return sender.sendEmail(letterMessage.getRecipientAddress(), letterMessage.getFormattedLetter())
                                        .toSingle(new Supplier<String>() {
                                            @Override
                                            public String get() {
                                                return letterMessage.getRecipientName();
                                            }
                                        });
                            }
                        })
        );
    }
}
