package com.dummy.myapplication.data.api;

import com.dummy.myapplication.data.UsersLocalStorage;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UsersDblStorage implements UsersLocalStorage {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Flowable<String> signatureSource = Flowable.fromCallable(new Callable<String>() {
        @Override
        public String call() {
            return "Jake Hunter, Senior Android Developer";
        }
    });

    @Override
    public Flowable<String> getSignature() {
        return signatureSource
                .subscribeOn(Schedulers.from(executorService));
    }
}
