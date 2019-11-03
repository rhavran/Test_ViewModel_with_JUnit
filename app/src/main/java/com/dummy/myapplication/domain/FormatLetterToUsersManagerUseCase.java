package com.dummy.myapplication.domain;

import com.dummy.myapplication.data.ManagersLocalStorage;
import com.dummy.myapplication.data.OrganizationsCloudStorage;
import com.dummy.myapplication.data.UsersLocalStorage;
import com.dummy.myapplication.data.api.ManagerNameAndEmail;

import org.reactivestreams.Publisher;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Function;

public class FormatLetterToUsersManagerUseCase {

    private final OrganizationsCloudStorage organizationNetworkManager;
    private final UsersLocalStorage userLocalManager;
    private final ManagersLocalStorage managerLocalStorage;

    public FormatLetterToUsersManagerUseCase(OrganizationsCloudStorage organizationNetworkManager,
                                             UsersLocalStorage userLocalManager,
                                             ManagersLocalStorage managerLocalStorage) {
        this.organizationNetworkManager = organizationNetworkManager;
        this.userLocalManager = userLocalManager;
        this.managerLocalStorage = managerLocalStorage;
    }

    public Flowable<LetterMessage> formatLetterToCurrentManager(final String textOfTheMessage) {
        final LetterMessage letterMessage = new LetterMessage();

        getManagersNameFromNetAndSyncWithDb()
                .subscribe();

        return managerLocalStorage.getManager()
                .map(new Function<ManagerNameAndEmail, LetterMessage>() {
                    @Override
                    public LetterMessage apply(ManagerNameAndEmail manager) {

                        letterMessage.setRecipientName(manager.getName());
                        letterMessage.setRecipientAddress(manager.getEmail());

                        return letterMessage;
                    }
                })
                .flatMap(new Function<LetterMessage, Publisher<LetterMessage>>() {
                    @Override
                    public Publisher<LetterMessage> apply(final LetterMessage letterMessage) {
                        return userLocalManager.getSignature()
                                .map(new Function<String, LetterMessage>() {
                                    @Override
                                    public LetterMessage apply(String signature) {
                                        letterMessage.setAuthorSignature("Sincerely yours,\n\n" + signature);
                                        return letterMessage;
                                    }
                                });
                    }
                })
                .map(new Function<LetterMessage, LetterMessage>() {
                    @Override
                    public LetterMessage apply(LetterMessage letterMessage) {
                        letterMessage.setMessage(textOfTheMessage);
                        return letterMessage;
                    }
                });
    }

    private Flowable<ManagerNameAndEmail> getManagersNameFromNetAndSyncWithDb() {
        return organizationNetworkManager.getManager()
                .flatMapPublisher(new Function<ManagerNameAndEmail, Publisher<ManagerNameAndEmail>>() {
                    @Override
                    public Publisher<ManagerNameAndEmail> apply(ManagerNameAndEmail managersNameFromNetwork) {
                        return managerLocalStorage.saveManagersName(managersNameFromNetwork);
                    }
                });
    }
}
