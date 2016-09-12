package com.jforex.programming.order;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;
import java.util.function.Function;

import com.dukascopy.api.IOrder;
import com.dukascopy.api.Instrument;
import com.jforex.programming.order.command.CloseCommand;
import com.jforex.programming.order.command.CommonCommand;
import com.jforex.programming.order.command.MergeCommand;
import com.jforex.programming.order.command.option.CloseOption;
import com.jforex.programming.order.command.option.MergeOption;
import com.jforex.programming.order.command.option.SetAmountOption;
import com.jforex.programming.order.command.option.SetGTTOption;
import com.jforex.programming.order.command.option.SetLabelOption;
import com.jforex.programming.order.command.option.SetOpenPriceOption;
import com.jforex.programming.order.command.option.SetSLOption;
import com.jforex.programming.order.command.option.SetTPOption;
import com.jforex.programming.order.command.option.SubmitOption;
import com.jforex.programming.position.PositionOrders;
import com.jforex.programming.position.PositionUtil;

import rx.Completable;

public final class OrderUtil {

    private final OrderUtilBuilder orderUtilBuilder;
    private final PositionUtil positionUtil;
    private final OrderUtilCompletable orderUtilCompletable;

    public OrderUtil(final OrderUtilBuilder orderUtilBuilder,
                     final PositionUtil positionUtil,
                     final OrderUtilCompletable orderUtilCompletable) {
        this.orderUtilBuilder = orderUtilBuilder;
        this.positionUtil = positionUtil;
        this.orderUtilCompletable = orderUtilCompletable;
    }

    public final SubmitOption submitBuilder(final OrderParams orderParams) {
        return orderUtilBuilder.submitBuilder(checkNotNull(orderParams));
    }

    public final MergeOption mergeBuilder(final String mergeOrderLabel,
                                          final Set<IOrder> toMergeOrders) {
        return orderUtilBuilder.mergeBuilder(checkNotNull(mergeOrderLabel),
                                             checkNotNull(toMergeOrders));
    }

    public final CloseOption closeBuilder(final IOrder orderToClose) {
        return orderUtilBuilder.closeBuilder(checkNotNull(orderToClose));
    }

    public final SetLabelOption setLabelBuilder(final IOrder order,
                                                final String newLabel) {
        return orderUtilBuilder.setLabelBuilder(checkNotNull(order),
                                                checkNotNull(newLabel));
    }

    public final SetGTTOption setGTTBuilder(final IOrder order,
                                            final long newGTT) {
        return orderUtilBuilder.setGTTBuilder(checkNotNull(order), newGTT);
    }

    public final SetAmountOption setAmountBuilder(final IOrder order,
                                                  final double newAmount) {
        return orderUtilBuilder.setAmountBuilder(checkNotNull(order), newAmount);
    }

    public final SetOpenPriceOption setOpenPriceBuilder(final IOrder order,
                                                        final double newPrice) {
        return orderUtilBuilder.setOpenPriceBuilder(checkNotNull(order), newPrice);
    }

    public final SetSLOption setSLBuilder(final IOrder order,
                                          final double newSL) {
        return orderUtilBuilder.setSLBuilder(checkNotNull(order), newSL);
    }

    public final SetTPOption setTPBuilder(final IOrder order,
                                          final double newTP) {
        return orderUtilBuilder.setTPBuilder(checkNotNull(order), newTP);
    }

    public final Completable mergePosition(final Instrument instrument,
                                           final Function<Set<IOrder>, MergeCommand> mergeCommandFactory) {
        return positionUtil.merge(checkNotNull(instrument),
                                  checkNotNull(mergeCommandFactory));
    }

    public final Completable mergeAllPositions(final Function<Set<IOrder>, MergeCommand> mergeCommandFactory) {
        return positionUtil.mergeAll(checkNotNull(mergeCommandFactory));
    }

    public final Completable closePosition(final Instrument instrument,
                                           final Function<Set<IOrder>, MergeCommand> mergeCommandFactory,
                                           final Function<IOrder, CloseCommand> closeCommandFactory) {
        return positionUtil.close(checkNotNull(instrument),
                                  checkNotNull(mergeCommandFactory),
                                  checkNotNull(closeCommandFactory));
    }

    public final Completable closeAllPositions(final Function<Set<IOrder>, MergeCommand> mergeCommandFactory,
                                               final Function<IOrder, CloseCommand> closeCommandFactory) {
        return positionUtil.closeAll(checkNotNull(mergeCommandFactory),
                                     checkNotNull(closeCommandFactory));
    }

    public final Completable commandToCompletable(final CommonCommand command) {
        return orderUtilCompletable.commandToCompletable(checkNotNull(command));
    }

    public final PositionOrders positionOrders(final Instrument instrument) {
        return positionUtil.positionOrders(checkNotNull(instrument));
    }
}
