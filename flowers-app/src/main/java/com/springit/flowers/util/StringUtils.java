package com.springit.flowers.util;

import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class StringUtils {

    public static Set<String> getSetFromStringCsv(String filterTypesStringFormat) {
        return Optional.ofNullable(filterTypesStringFormat)
                .map(attributes -> attributes.split(";"))
                .stream()
                .flatMap(Stream::of)
                .filter(str -> !str.isBlank())
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
