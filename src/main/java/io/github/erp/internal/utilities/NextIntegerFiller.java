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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NextIntegerFiller {

    public static long fillNext(List<Long> numbers) {
        if (numbers.isEmpty()) {
            return 0; // If the list is empty, start with 0
        }

        Collections.sort(numbers); // Sort the list of numbers

        long nextNumber = numbers.get(0); // Start with the smallest number

        for (int i = 1; i < numbers.size(); i++) {
            long currentNumber = numbers.get(i);
            if (currentNumber - nextNumber > 1) {
                return nextNumber + 1; // Found a gap, return the next number to fill
            }
            nextNumber = currentNumber;
        }

        // If all numbers are in sequence, return the next number in sequence
        return numbers.get(numbers.size() - 1) + 1;
    }

    public static void main(String[] args) {
        List<Long> numbers = new ArrayList<>();
        numbers.add(3L);
        numbers.add(6L);
        numbers.add(1L);
        numbers.add(4L);
        System.out.println("Next number to fill: " + fillNext(numbers)); // Output: 2
    }
}

