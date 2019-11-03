package com.dummy.myapplication.data.api;

import com.dummy.myapplication.data.ManagersLocalStorage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class ManagersDbStorage implements ManagersLocalStorage {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private BehaviorSubject<ManagerNameAndEmail> namesSource = BehaviorSubject.createDefault(new ManagerNameAndEmail("", ""));

    @Override
    public Flowable<ManagerNameAndEmail> saveManagersName(ManagerNameAndEmail managerNameAndEmail) {
        namesSource.onNext(managerNameAndEmail);

        return getManager();
    }

    @Override
    public Flowable<ManagerNameAndEmail> getManager() {
        return namesSource.toFlowable(BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.from(executorService))
                .observeOn(Schedulers.from(executorService));
    }
}
