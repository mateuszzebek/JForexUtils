package com.jforex.programming.order.event;

public enum OrderEventType {

    NOTIFICATION,
    SUBMIT_OK,
    SUBMIT_CONDITIONAL_OK,
    PARTIAL_FILL_OK,
    FULL_FILL_OK,
    SL_CHANGE_OK,
    TP_CHANGE_OK,
    MERGE_OK,
    MERGE_CLOSE_OK,
    CLOSE_OK,
    PARTIAL_CLOSE_OK,
    CLOSED_BY_MERGE,
    CLOSED_BY_SL,
    CLOSED_BY_TP,
    LABEL_CHANGE_OK,
    AMOUNT_CHANGE_OK,
    OPENPRICE_CHANGE_OK,
    GTT_CHANGE_OK,
    SUBMIT_REJECTED,
    FILL_REJECTED,
    CHANGE_REJECTED,
    MERGE_REJECTED,
    CLOSE_REJECTED,
    CHANGE_SL_REJECTED,
    CHANGE_TP_REJECTED,
    CHANGE_GTT_REJECTED,
    CHANGE_LABEL_REJECTED,
    CHANGE_AMOUNT_REJECTED,
    CHANGE_OPENPRICE_REJECTED,
}
