package com.dummy.myapplication.ui.uitls;

import com.dummy.myapplication.ui.utils.RxAndroidPlugins;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.Callable;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.schedulers.TestScheduler;

public class RxTestSchedulerRule implements TestRule {
    private final TestScheduler testScheduler = new TestScheduler();

    public TestScheduler getTestScheduler() {
        return testScheduler;
    }

    @Override
    public Statement apply(final Statement base, Description d) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxAndroidPlugins.reset();

                RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override
                    public Scheduler apply(Scheduler scheduler) {
                        return testScheduler;
                    }
                });
                RxJavaPlugins.setComputationSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override
                    public Scheduler apply(Scheduler scheduler) {
                        return testScheduler;
                    }
                });
                RxJavaPlugins.setNewThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override
                    public Scheduler apply(Scheduler scheduler) {
                        return testScheduler;
                    }
                });
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
                    @Override
                    public Scheduler apply(Callable<Scheduler> schedulerCallable) {
                        return Schedulers.trampoline();
                    }
                });
                RxAndroidPlugins.initMainThreadScheduler(new Callable<Scheduler>() {
                    @Override
                    public Scheduler call() {
                        return Schedulers.trampoline();
                    }
                });
                RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
                    @Override
                    public Scheduler apply(Scheduler scheduler) {
                        return Schedulers.trampoline();
                    }
                });

                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }
        };
    }
}