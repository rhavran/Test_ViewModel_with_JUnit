package com.dummy.myapplication.data;

import com.dummy.myapplication.data.api.ManagerNameAndEmail;

import io.reactivex.rxjava3.core.Single;

public interface OrganizationsCloudStorage {
    Single<ManagerNameAndEmail> getManager();
}
