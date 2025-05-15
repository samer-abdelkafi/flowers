package com.springit.flowers.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Slf4j
public class AwsS3Service {


    public static final String RAW = "raw";

    private final String s3BucketName= "";


//    public AwsS3Service(/*@Value("${s3.bucket.name}")*/ String s3BucketName) {
//        this.s3BucketName = s3BucketName;
//
//    }

    @SneakyThrows
    public String upload(String fileName, final InputStream inputStream, final String folderTypeMessage) {
        return null;
    }



    public byte[] read(String path) {
        return null;
    }

    /**
     *
     * @param datePartition like y=yyyy/m=MM/d=dd
     * @param fileName like {PROCESS_ID}{ENV}{NUMBER}
     * @param typeMessage message incoming = input or message outgoing = output
     * @return S3 key to locate file
     */
    private String getS3KeyFromAudit(String datePartition, String fileName, StorageService.TypeMessage typeMessage){
        return String.format("%s/%s/%s/%s/%s", RAW, "FLOW", typeMessage,datePartition, fileName);
    }


}
