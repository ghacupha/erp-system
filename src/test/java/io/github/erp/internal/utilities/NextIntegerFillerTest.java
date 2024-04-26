package io.github.erp.internal.utilities;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
        List<Long> numbers = new ArrayList<>();
        numbers.add(3L);
        numbers.add(6L);
        numbers.add(1L);
        numbers.add(4L);

        long nextNumber = NextIntegerFiller.fillNext(numbers);
        assertEquals(2, nextNumber, "Next number to fill should be 2");
    }

    @Test
    public void testFillNextWithEmptyList() {
        List<Long> numbers = new ArrayList<>();

        long nextNumber = NextIntegerFiller.fillNext(numbers);
        assertEquals(0, nextNumber, "Next number to fill should be 0 for an empty list");
    }

    @Test
    public void testFillNextWithConsecutiveNumbers() {
        List<Long> numbers = new ArrayList<>();
        numbers.add(1L);
        numbers.add(2L);
        numbers.add(3L);
        numbers.add(4L);

        long nextNumber = NextIntegerFiller.fillNext(numbers);
        assertEquals(5, nextNumber, "Next number to fill should be 5 for consecutive numbers");
    }
}
