package com.dummy.myapplication.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dummy.myapplication.controller.OrganizationEmailSender;
import com.dummy.myapplication.data.api.ManagersDbStorage;
import com.dummy.myapplication.data.api.OrganizationsRESTfulApi;
import com.dummy.myapplication.data.api.UsersDblStorage;
import com.dummy.myapplication.domain.FormatLetterToUsersManagerUseCase;
import com.dummy.myapplication.domain.SendLetterToUsersManagerUseCase;
import com.dummy.myapplication.ui.controller.ToastController;

public class MainViewFactory implements ViewModelProvider.Factory {
    private final Context context;

    public MainViewFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        FormatLetterToUsersManagerUseCase formatter = new FormatLetterToUsersManagerUseCase(
                new OrganizationsRESTfulApi(),
                new UsersDblStorage(),
                new ManagersDbStorage()

        );
        SendLetterToUsersManagerUseCase sendUseCase = new SendLetterToUsersManagerUseCase(formatter, new OrganizationEmailSender());

        return (T) new MainViewModel(formatter, sendUseCase, new ToastController(context));
    }
}
