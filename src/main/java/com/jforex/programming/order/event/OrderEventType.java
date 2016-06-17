package com.jforex.programming.order.event;

public enum OrderEventType {

    NOTIFICATION,
    SUBMIT_OK,
    SUBMIT_CONDITIONAL_OK,
    PARTIAL_FILL_OK,
    FULLY_FILLED,
    CHANGED_SL,
    CHANGED_TP,
    MERGE_OK,
    MERGE_CLOSE_OK,
    CLOSE_OK,
    PARTIAL_CLOSE_OK,
    CLOSED_BY_MERGE,
    CLOSED_BY_SL,
    CLOSED_BY_TP,
    CHANGED_LABEL,
    CHANGED_AMOUNT,
    CHANGED_PRICE,
    CHANGED_GTT,
    SUBMIT_REJECTED,
    FILL_REJECTED,
    CHANGED_REJECTED,
    MERGE_REJECTED,
    CLOSE_REJECTED,
    CHANGE_SL_REJECTED,
    CHANGE_TP_REJECTED,
    CHANGE_GTT_REJECTED,
    CHANGE_LABEL_REJECTED,
    CHANGE_AMOUNT_REJECTED,
    CHANGE_OPENPRICE_REJECTED,
}
