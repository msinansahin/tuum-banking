package com.tuum.banking.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PageRequestTest {

    @Test
    void getOffset() {
        assertThat(new PageRequest().setPage(2).setSize(4).getOffset()).isEqualTo(8);
        assertThat(new PageRequest().setPage(0).setSize(4).getOffset()).isZero();
    }
}
