package com.puzzle.controller;

import java.util.List;
import java.util.function.Function;


import static java.util.stream.Collectors.toList;

/**
 * @author ibez
 * @since 2019-06-08
 */
public class ControllerUtils {

    public static <T, R> List<R> toResourceList(List<T> entities, Function<T, R> mapper) {
        return entities.stream()
            .map(mapper)
            .collect(toList());
    }
}
