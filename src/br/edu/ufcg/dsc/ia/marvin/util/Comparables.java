package br.edu.ufcg.dsc.ia.marvin.util;

import java.util.Iterator;

public class Comparables {

    public static <T extends Comparable<T>, I extends Iterable<T>> T minimum(I elements, T defaultReturn) {
        Iterator<T> iterator = elements.iterator();

        T minimum = defaultReturn;

        if (iterator.hasNext()) {
            minimum = iterator.next();
        }

        while (iterator.hasNext()) {
            T other = iterator.next();

            if (minimum.compareTo(other) > 0) {
                minimum = other;
            }
        }

        return minimum;
    }

    public static <T extends Comparable<T>, I extends Iterable<T>> T maximum(I elements, T defaultReturn) {
        Iterator<T> iterator = elements.iterator();

        T maximum = defaultReturn;

        if (iterator.hasNext()) {
            maximum = iterator.next();
        }

        while (iterator.hasNext()) {
            T other = iterator.next();

            if (maximum.compareTo(other) < 0) {
                maximum = other;
            }
        }

        return maximum;
    }
}
