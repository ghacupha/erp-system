package io.github.erp.internal.utilities;

//import static org.junit.jupiter.api.Assertions.*;
//
//class NextIntegerFillerTest {
//
//}

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NextIntegerFillerTest {

    @Test
    public void testFillNext() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(3);
        numbers.add(6);
        numbers.add(1);
        numbers.add(4);

        int nextNumber = NextIntegerFiller.fillNext(numbers);
        assertEquals(2, nextNumber, "Next number to fill should be 2");
    }

    @Test
    public void testFillNextWithEmptyList() {
        List<Integer> numbers = new ArrayList<>();

        int nextNumber = NextIntegerFiller.fillNext(numbers);
        assertEquals(0, nextNumber, "Next number to fill should be 0 for an empty list");
    }

    @Test
    public void testFillNextWithConsecutiveNumbers() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);

        int nextNumber = NextIntegerFiller.fillNext(numbers);
        assertEquals(5, nextNumber, "Next number to fill should be 5 for consecutive numbers");
    }
}
