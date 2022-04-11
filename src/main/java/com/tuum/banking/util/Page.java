package com.tuum.banking.util;

import java.util.List;

import lombok.Data;

@Data
public class Page<T> {
    private List<T> content;
    private Long totalCount;
    private int currentPage;
    private int pageSize;
}
