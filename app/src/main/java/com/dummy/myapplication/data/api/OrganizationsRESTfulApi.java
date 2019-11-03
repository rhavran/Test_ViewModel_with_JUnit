package com.dummy.myapplication.data.api;

import com.dummy.myapplication.data.OrganizationsCloudStorage;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class OrganizationsRESTfulApi implements OrganizationsCloudStorage {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public Single<ManagerNameAndEmail> getManager() {
        return Single.fromCallable(new Callable<ManagerNameAndEmail>() {
            @Override
            public ManagerNameAndEmail call() {
                return new ManagerNameAndEmail("john.snow@winter.is.comming", "Mr. John Snow");
            }
        }).subscribeOn(Schedulers.from(executorService))
                .delay(6, TimeUnit.SECONDS);
    }
}
