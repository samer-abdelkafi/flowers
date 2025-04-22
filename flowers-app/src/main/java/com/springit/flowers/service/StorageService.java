package com.springit.flowers.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service
@AllArgsConstructor
public class StorageService {


    public static final String FOLDER_MESSAGE_PATH = "FOLDER_MESSAGE_PATH";
    private final AwsS3Service awsS3Service;

    @SneakyThrows
    public Message<?> writeMessage(Object payload, MessageHeaders headers, TypeMessage typeMessage) {
        String fileName = generateFileName(headers);
        return genericWriteMessage(payload, headers, fileName, typeMessage);
    }


    public  byte[] readMessage(String path) {
        return awsS3Service.read(path);
    }





    private String generateFileName(
            MessageHeaders headers) {
        Date now = new Date();
        DateFormat timeFormat = new SimpleDateFormat("HH'h'mm'm'ss's'");
        String time = timeFormat.format(now);
        String messageId = strip(headers.get("jms_messageId", String.class));
        return time + "_" + messageId;
    }

    private String strip(String s) {
        return s.replaceAll("[:./?<>\\\\*|]", "_");
    }


    protected Message<?> genericWriteMessage(Object payload,
                                             MessageHeaders headers,
                                             String fileName, TypeMessage typeMessage) throws IOException {
        String path = write(payload, fileName, typeMessage);
        return MessageBuilder.withPayload(payload)
                .copyHeaders(headers)
                .setHeader(FOLDER_MESSAGE_PATH, path)
                .build();
    }

    /**
     * Generate date partition for current date
     * @return date partition in S3 convention
     */
    public static String generateS3PartitionDate(){
        return LocalDate.now().format(DateTimeFormatter.ofPattern("'y='yyyy'/m='MM'/d='dd"));
    }


    private String write(Object payload, String fullFileName, TypeMessage typeMessage) throws IOException {
        return awsS3Service.upload(fullFileName, getInputStream(payload), "FLOWS/" + typeMessage);
    }


    private static InputStream getInputStream(Object payload) throws IOException {
        if (payload instanceof String) {
            return new ByteArrayInputStream(((String) payload).getBytes(StandardCharsets.UTF_8));
        } else if (payload instanceof byte[]) {
            return new ByteArrayInputStream((byte[]) payload);
        } else if (payload instanceof Byte[]) {
            return new ByteArrayInputStream(getBytes((Byte[]) payload));
        } else if (payload instanceof File) {
            return Files.newInputStream(((File) payload).toPath());
        } else {
            return new ByteArrayInputStream(payload.toString().getBytes());
        }
    }

    private static byte[] getBytes(Byte[] byteArray) {
        byte[] bytePrimArray = new byte[byteArray.length];
        Arrays.setAll(byteArray, b -> byteArray[b]);
        return bytePrimArray;
    }

    public enum TypeMessage {
        input, output, error
    }

}
