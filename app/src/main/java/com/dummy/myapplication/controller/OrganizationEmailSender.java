package com.dummy.myapplication.controller;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;

public class OrganizationEmailSender implements EmailSender {
    @Override
    public Completable sendEmail(String to, String message) {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Throwable {
                // Send email
            }
        }).delay(2, TimeUnit.SECONDS);
    }
}
