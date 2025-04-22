package com.springit.flowers.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@UtilityClass
public class StreamUtils {

    /**
     * Closes an InputStream quietly, without throwing any IOException.
     * Exceptions are caught, but not rethrown.
     * 
     * @param inputStream the InputStream to be closed.
     */
    public static void closeQuietly(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ex) {
                log.warn("failed to close input stream");
            }
        }
    }
}
