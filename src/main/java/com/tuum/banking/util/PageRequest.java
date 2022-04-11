package com.tuum.banking.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class PageRequest {
    private int page;
    private int size;
    @JsonIgnore
    public int getOffset() {
        return page * size;
    }
}
