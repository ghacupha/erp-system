package io.github.erp.internal.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NextIntegerFiller {

    public static int fillNext(List<Integer> numbers) {
        if (numbers.isEmpty()) {
            return 0; // If the list is empty, start with 0
        }

        Collections.sort(numbers); // Sort the list of numbers

        int nextNumber = numbers.get(0); // Start with the smallest number

        for (int i = 1; i < numbers.size(); i++) {
            int currentNumber = numbers.get(i);
            if (currentNumber - nextNumber > 1) {
                return nextNumber + 1; // Found a gap, return the next number to fill
            }
            nextNumber = currentNumber;
        }

        // If all numbers are in sequence, return the next number in sequence
        return numbers.get(numbers.size() - 1) + 1;
    }

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(3);
        numbers.add(6);
        numbers.add(1);
        numbers.add(4);
        System.out.println("Next number to fill: " + fillNext(numbers)); // Output: 2
    }
}

