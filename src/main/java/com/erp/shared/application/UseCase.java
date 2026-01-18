package com.erp.shared.application;

/**
 * Marker interface for use cases.
 * Each use case represents a single action in the application.
 *
 * @param <I> input type
 * @param <O> output type
 */
public interface UseCase<I, O> {

    O execute(I input);
}
