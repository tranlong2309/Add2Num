package com.add2num.core;

import com.add2num.core.MyBigNumber.AdditionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit 5 unit tests for {@link MyBigNumber#sum(String, String)}.
 */
@DisplayName("MyBigNumber.sum() Tests")
class MyBigNumberTest {

    private MyBigNumber sut;

    @BeforeEach
    void setUp() {
        sut = new MyBigNumber();
    }

    @Test
    @DisplayName("Basic: 1234 + 897 = 2131")
    void sum_basicCase() {
        AdditionResult r = sut.sum("1234", "897");
        assertEquals("2131", r.getResult());
        assertEquals(4, r.getSteps().size());   // 4 digit positions
    }

    @Test
    @DisplayName("Carry propagation: 999 + 1 = 1000")
    void sum_carryPropagation() {
        assertEquals("1000", sut.sum("999", "1").getResult());
    }

    @Test
    @DisplayName("Different lengths: 1 + 999999 = 1000000")
    void sum_differentLengths() {
        assertEquals("1000000", sut.sum("1", "999999").getResult());
    }

    @Test
    @DisplayName("Single digits with carry: 5 + 5 = 10")
    void sum_singleDigitWithCarry() {
        assertEquals("10", sut.sum("5", "5").getResult());
    }

    @Test
    @DisplayName("Very large number beyond long range")
    void sum_veryLargeNumbers() {
        assertEquals(
            "100000000000000000000",
            sut.sum("99999999999999999999", "1").getResult()
        );
    }

    @Test
    @DisplayName("One operand is zero")
    void sum_oneOperandIsZero() {
        assertEquals("123", sut.sum("0", "123").getResult());
    }

    @Test
    @DisplayName("Both operands are zero")
    void sum_bothZero() {
        assertEquals("0", sut.sum("0", "0").getResult());
    }

    @Test
    @DisplayName("Equal large numbers")
    void sum_equalLargeNumbers() {
        assertEquals(
            "1000000000000000000000",
            sut.sum("500000000000000000000", "500000000000000000000").getResult()
        );
    }
}
