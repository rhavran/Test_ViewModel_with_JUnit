package com.dummy.myapplication.data;

import com.dummy.myapplication.data.api.ManagerNameAndEmail;

import io.reactivex.rxjava3.core.Flowable;

public interface ManagersLocalStorage {
    Flowable<ManagerNameAndEmail> saveManagersName(ManagerNameAndEmail manager);

    Flowable<ManagerNameAndEmail> getManager();
}
