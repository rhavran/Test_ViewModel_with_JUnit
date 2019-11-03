package com.dummy.myapplication.controller;

import io.reactivex.rxjava3.core.Completable;

public interface EmailSender {
    Completable sendEmail(String to, String message);
}
