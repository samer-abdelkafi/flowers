package com.springit.flowers.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class CustomFunctionUtils {


    public static boolean in(Object... array) {

        if (array == null || array.length < 2) {
            return false;
        }

        String element = array[array.length - 1].toString().trim();

        return Arrays.stream(Arrays.copyOfRange(array, 0, array.length-1))
                .map(Object::toString)
                .map(String::trim)
                .anyMatch(element::equals);


    }


}
