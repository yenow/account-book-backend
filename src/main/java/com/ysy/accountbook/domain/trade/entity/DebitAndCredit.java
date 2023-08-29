package com.ysy.accountbook.domain.trade.entity;

public enum DebitAndCredit {
    debit("차변"), credit("대변");

    public final String name;

    DebitAndCredit(String name) {
        this.name = name;
    }
}
