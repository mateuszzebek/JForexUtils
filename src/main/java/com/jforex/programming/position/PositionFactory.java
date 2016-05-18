package com.jforex.programming.position;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dukascopy.api.Instrument;
import com.jforex.programming.order.event.OrderEvent;

import rx.Observable;

public class PositionFactory {

    private final PositionTask positionTask;
    private final Observable<OrderEvent> orderEventObservable;
    private final Map<Instrument, Position> positionByInstrument = new ConcurrentHashMap<>();

    public PositionFactory(final PositionTask positionTask,
                           final Observable<OrderEvent> orderEventObservable) {
        this.positionTask = positionTask;
        this.orderEventObservable = orderEventObservable;
    }

    public Position createNew(final Instrument instrument,
                              final RestoreSLTPPolicy restoreSLTPPolicy) {
        return new Position(instrument,
                            positionTask,
                            orderEventObservable,
                            restoreSLTPPolicy);
    }

    public Position forInstrument(final Instrument instrument,
                                  final RestoreSLTPPolicy restoreSLTPPolicy) {
        return positionByInstrument.computeIfAbsent(instrument,
                                                    inst -> createNew(inst, restoreSLTPPolicy));
    }

    public Position forInstrument(final Instrument instrument) {
        return forInstrument(instrument, new NoRestorePolicy());
    }

    public Collection<Position> all() {
        return positionByInstrument.values();
    }
}
