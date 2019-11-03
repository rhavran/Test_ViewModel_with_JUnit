package com.dummy.myapplication.ui.main;

import com.dummy.myapplication.R;
import com.dummy.myapplication.domain.FormatLetterToUsersManagerUseCase;
import com.dummy.myapplication.domain.LetterMessage;
import com.dummy.myapplication.domain.SendLetterToUsersManagerUseCase;
import com.dummy.myapplication.ui.controller.ToastController;
import com.dummy.myapplication.ui.uitls.RxTestSchedulerRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.Callable;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MainViewModelTest {

    @Rule
    public final RxTestSchedulerRule testSchedulerRule = new RxTestSchedulerRule();

    private FormatLetterToUsersManagerUseCase formatLetterToUsersManagerUseCase;
    private SendLetterToUsersManagerUseCase sendLetterToUsersManagerUseCase;
    private ToastController toastManager;
    private MainViewModel mainViewModel;

    @Before
    public void setUp() {
        formatLetterToUsersManagerUseCase = mock(FormatLetterToUsersManagerUseCase.class);
        sendLetterToUsersManagerUseCase = mock(SendLetterToUsersManagerUseCase.class);
        toastManager = mock(ToastController.class);
        mainViewModel = new MainViewModel(formatLetterToUsersManagerUseCase, sendLetterToUsersManagerUseCase, toastManager);
    }

    @Test
    public void shouldShowLoadingTextOnEmptyRecipient() {
        final LetterMessage letterMessage = mock(LetterMessage.class);
        when(formatLetterToUsersManagerUseCase.formatLetterToCurrentManager(eq("")))
                .thenReturn(Flowable.fromCallable(new Callable<LetterMessage>() {
                    @Override
                    public LetterMessage call() {
                        return letterMessage;
                    }
                }));
        when(letterMessage.hasEmptyRecipient())
                .thenReturn(true);

        BehaviorSubject<String> message = mainViewModel.getMessage();

        message.onNext("");

        TestObserver<String> test = mainViewModel.getFormattedText()
                .test();

        testSchedulerRule.getTestScheduler().advanceTimeBy(2, SECONDS);

        test.assertNoErrors();
        test.assertValue("Loading ...");
    }

    @Test
    public void shouldShowPleaseEnterMessageTextOnEmptyMessage() {
        final LetterMessage letterMessage = mock(LetterMessage.class);
        when(formatLetterToUsersManagerUseCase.formatLetterToCurrentManager(eq("")))
                .thenReturn(Flowable.fromCallable(new Callable<LetterMessage>() {
                    @Override
                    public LetterMessage call() {
                        return letterMessage;
                    }
                }));
        when(letterMessage.hasEmptyRecipient())
                .thenReturn(false);
        when(letterMessage.hasEmptyMessage())
                .thenReturn(true);

        BehaviorSubject<String> message = mainViewModel.getMessage();
        message.onNext("");

        TestObserver<String> test = mainViewModel.getFormattedText()
                .test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(2, SECONDS);

        test.assertNoErrors();
        test.assertValue("Please enter your message!");
    }

    @Test
    public void shouldShowFormattedLetterTextOnWithCompleteMessage() {
        final LetterMessage letterMessage = mock(LetterMessage.class);
        String text = "Hello it is me!";

        when(formatLetterToUsersManagerUseCase.formatLetterToCurrentManager(eq(text)))
                .thenReturn(Flowable.fromCallable(new Callable<LetterMessage>() {
                    @Override
                    public LetterMessage call() {
                        return letterMessage;
                    }
                }));
        when(letterMessage.hasEmptyRecipient())
                .thenReturn(false);
        when(letterMessage.hasEmptyMessage())
                .thenReturn(false);
        when(letterMessage.getFormattedLetter())
                .thenReturn(text);

        BehaviorSubject<String> message = mainViewModel.getMessage();
        message.onNext(text);

        TestObserver<String> test = mainViewModel.getFormattedText()
                .test();
        testSchedulerRule.getTestScheduler().advanceTimeBy(2, SECONDS);

        test.assertNoErrors();
        test.assertValue("Letter Sample:\n" +
                text + "\n" +
                "__________________________________________");
    }

    @Test
    public void shouldDisposeEveryThingOnClear() {
        final LetterMessage letterMessage = mock(LetterMessage.class);
        when(formatLetterToUsersManagerUseCase.formatLetterToCurrentManager(anyString()))
                .thenReturn(Flowable.fromCallable(new Callable<LetterMessage>() {
                    @Override
                    public LetterMessage call() {
                        return letterMessage;
                    }
                }));
        when(letterMessage.hasEmptyRecipient())
                .thenReturn(true);

        BehaviorSubject<String> message = mainViewModel.getMessage();
        message.onNext("1");

        testSchedulerRule.getTestScheduler().advanceTimeBy(2, SECONDS);

        mainViewModel.onCleared();

        message.onNext("2");

        testSchedulerRule.getTestScheduler().advanceTimeBy(2, SECONDS);

        verify(formatLetterToUsersManagerUseCase, atLeastOnce()).formatLetterToCurrentManager(eq("1"));
        verify(formatLetterToUsersManagerUseCase, never()).formatLetterToCurrentManager(eq("2"));

        mainViewModel.getFormattedText()
                .test()
                .assertComplete();

        mainViewModel.getMessage()
                .test()
                .assertComplete();
    }

    @Test
    public void shouldShowErrorToastOnError() {
        final LetterMessage letterMessage = mock(LetterMessage.class);
        when(letterMessage.hasEmptyRecipient())
                .thenReturn(true);
        when(formatLetterToUsersManagerUseCase.formatLetterToCurrentManager(anyString()))
                .thenReturn(Flowable.fromCallable(new Callable<LetterMessage>() {
                    @Override
                    public LetterMessage call() {
                        return letterMessage;
                    }
                }));

        mainViewModel.getMessage().onNext("Message");
        testSchedulerRule.getTestScheduler().advanceTimeBy(2, SECONDS);

        when(sendLetterToUsersManagerUseCase.sendLetter(eq("Message")))
                .thenReturn(Single.<String>error(new Exception("Ex")));

        mainViewModel.send();

        verify(toastManager).showToast(eq(R.string.failed));
    }

    @Test
    public void shouldShowToastWithSuccessMessageWhenEmailIsSend() {
        final LetterMessage letterMessage = mock(LetterMessage.class);
        when(letterMessage.hasEmptyRecipient())
                .thenReturn(true);
        when(formatLetterToUsersManagerUseCase.formatLetterToCurrentManager(anyString()))
                .thenReturn(Flowable.fromCallable(new Callable<LetterMessage>() {
                    @Override
                    public LetterMessage call() {
                        return letterMessage;
                    }
                }));

        mainViewModel.getMessage().onNext("Message");
        testSchedulerRule.getTestScheduler().advanceTimeBy(2, SECONDS);

        final String email = "jhon.snow@winter.has.come";
        when(sendLetterToUsersManagerUseCase.sendLetter(eq("Message")))
                .thenReturn(Single.fromCallable(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return email;
                    }
                }));

        mainViewModel.send();

        verify(toastManager).showToast(eq(R.string.email_sent), eq(email));
    }
}