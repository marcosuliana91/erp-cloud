package com.erp.shared.adapter;

/**
 * Generic mapper interface for conversions between types.
 *
 * @param <S> source type
 * @param <T> target type
 */
public interface Mapper<S, T> {

    T map(S source);

    default S reverse(T target) {
        throw new UnsupportedOperationException("Reverse mapping not implemented");
    }
}
