package com.dummy.myapplication.data;

import io.reactivex.rxjava3.core.Flowable;

public interface UsersLocalStorage {
    Flowable<String> getSignature();
}
