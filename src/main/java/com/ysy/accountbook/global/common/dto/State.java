package com.ysy.accountbook.global.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum State {
    success("success"), fail("fail"), error("error");

    final String value;

    @JsonCreator
    State(String value) {
        this.value = value;
    }
}
