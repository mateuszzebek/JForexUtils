package com.jforex.programming.connection;

import java.util.concurrent.TimeUnit;

import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dukascopy.api.system.IClient;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.jforex.programming.misc.RxUtil;
import com.jforex.programming.settings.PlatformSettings;

import rx.Completable;
import rx.Completable.CompletableSubscriber;
import rx.Observable;

public final class ConnectionKeeper {

    private final IClient client;
    private Completable reconnectCompletable;
    private final AuthentificationUtil authentificationUtil;
    private final StateMachineConfig<FSMState, ConnectionState> fsmConfig = new StateMachineConfig<>();
    private final StateMachine<FSMState, ConnectionState> fsm = new StateMachine<>(FSMState.IDLE, fsmConfig);

    private enum FSMState {
        IDLE,
        RECONNECTING
    }

    private final static PlatformSettings platformSettings = ConfigFactory.create(PlatformSettings.class);
    private final static Logger logger = LogManager.getLogger(ConnectionKeeper.class);

    public ConnectionKeeper(final IClient client,
                            final Observable<ConnectionState> connectionStateObs,
                            final AuthentificationUtil authentificationUtil) {
        this.client = client;
        this.authentificationUtil = authentificationUtil;

        intObservables(connectionStateObs);
        configureFSM();
    }

    private final void intObservables(final Observable<ConnectionState> connectionStateObs) {
        connectionStateObs.subscribe(this::onConnectionStateUpdate);

        reconnectCompletable = Completable.create(subscriber -> {
            logger.debug("Try to do a light reconnection...");
            client.reconnect();
            initNextConnectionStateObs(connectionStateObs, subscriber);
        }).retryWhen(errors -> RxUtil.retryWithDelay(errors,
                                                     platformSettings.logintimeoutseconds(),
                                                     TimeUnit.SECONDS));
    }

    private void initNextConnectionStateObs(final Observable<ConnectionState> connectionStateObs,
                                            final CompletableSubscriber subscriber) {
        connectionStateObs.subscribe(connectionState -> {
            if (connectionState == ConnectionState.CONNECTED || client.isConnected())
                subscriber.onCompleted();
            else
                subscriber.onError(new ConnectException());
        });
    }

    private final void configureFSM() {
        fsmConfig.configure(FSMState.IDLE)
                .permitDynamic(ConnectionState.DISCONNECTED, () -> {
                    return authentificationUtil.loginState() == LoginState.LOGGED_IN
                            ? FSMState.RECONNECTING
                            : FSMState.IDLE;
                })
                .ignore(ConnectionState.CONNECTED);

        fsmConfig.configure(FSMState.RECONNECTING)
                .onEntry(() -> startReconnectStrategy())
                .permit(ConnectionState.CONNECTED, FSMState.IDLE)
                .ignore(ConnectionState.DISCONNECTED);
    }

    private final void onConnectionStateUpdate(final ConnectionState connectionState) {
        logger.debug(connectionState + " message received!");
        fsm.fire(connectionState);
    }

    private final void startReconnectStrategy() {
        reconnectCompletable
                .subscribe(exc -> logger.debug("Light reconnection error: " + exc.getMessage()),
                           () -> logger.debug("Light reconnect successful!"));
    }
}
