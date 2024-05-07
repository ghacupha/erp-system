package io.github.erp.internal.utilities;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

