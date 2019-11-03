package com.dummy.myapplication.ui.main;

import androidx.databinding.Bindable;

import com.dummy.myapplication.R;
import com.dummy.myapplication.domain.FormatLetterToUsersManagerUseCase;
import com.dummy.myapplication.domain.LetterMessage;
import com.dummy.myapplication.domain.SendLetterToUsersManagerUseCase;
import com.dummy.myapplication.ui.controller.ToastController;
import com.dummy.myapplication.ui.utils.AndroidSchedulers;
import com.dummy.myapplication.ui.utils.ObservableViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class MainViewModel extends ObservableViewModel {
    private final CompositeDisposable bag = new CompositeDisposable();

    private final SendLetterToUsersManagerUseCase sendUseCase;
    private BehaviorSubject<String> formattedLetter = BehaviorSubject.create();
    private BehaviorSubject<String> message = BehaviorSubject.createDefault("");
    private ToastController toastManager;

    MainViewModel(final FormatLetterToUsersManagerUseCase useCase,
                  SendLetterToUsersManagerUseCase sendUseCase,
                  ToastController toastManager) {
        this.sendUseCase = sendUseCase;
        this.toastManager = toastManager;

        bag.add(message
                .debounce(250, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String message) {
                        requestLetter(useCase, message);
                    }
                }));

        notifyChange();
    }

    @Bindable
    public BehaviorSubject<String> getFormattedText() {
        return formattedLetter;
    }

    @Bindable
    public BehaviorSubject<String> getMessage() {
        return message;
    }

    public void send() {
        bag.add(sendUseCase.sendLetter(message.getValue())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String receiver) {
                        toastManager.showToast(R.string.email_sent, receiver);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        toastManager.showToast(R.string.failed);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        bag.dispose();
        message.onComplete();
        formattedLetter.onComplete();
    }

    private void requestLetter(FormatLetterToUsersManagerUseCase useCase, String message) {
        bag.add(useCase.formatLetterToCurrentManager(message)
                .map(new Function<LetterMessage, String>() {
                    @Override
                    public String apply(LetterMessage letterMessage) {
                        if (letterMessage.hasEmptyRecipient()) {
                            return "Loading ...";
                        } else if (letterMessage.hasEmptyMessage()) {
                            return "Please enter your message!";
                        }

                        return String.format("Letter Sample:\n" +
                                        "%s\n" +
                                        "__________________________________________",
                                letterMessage.getFormattedLetter());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        formattedLetter.onNext(s);
                        notifyPropertyChanged(com.dummy.myapplication.BR.formattedText);
                    }
                }));
    }
}
