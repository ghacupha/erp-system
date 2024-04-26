package io.github.erp.internal.utilities;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
