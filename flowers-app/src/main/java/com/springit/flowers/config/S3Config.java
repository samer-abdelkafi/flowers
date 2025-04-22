package com.springit.flowers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class S3Config {

    @Bean
    public S3Client getS3Client(Environment env) throws URISyntaxException {
        try {
            return getS3ClientWithCloudConfigProperty(env);
        } catch (AssertionError e) {
            return getS3ClientWithSystemProperty();
        }

    }

    private static S3Client getS3ClientWithCloudConfigProperty(Environment env) throws URISyntaxException {
        String awsRegion = env.getProperty("aws.region");
        String s3Endpoint = env.getProperty("aws.endpoint");
        String accessKeyId = env.getProperty("aws.accessKeyId");
        String secretAccessKey = env.getProperty("aws.secretAccessKey");
        assert awsRegion != null;
        assert s3Endpoint != null;

        return S3Client.builder()
                .region(Region.of(awsRegion))
                .endpointOverride(new URI(s3Endpoint))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                        )
                )
                .forcePathStyle(true).build();
    }

    public static S3Client getS3ClientWithSystemProperty() throws URISyntaxException {
        String awsRegion = System.getenv("AWS_REGION");
        String s3Endpoint = System.getenv("AWS_ENDPOINT");
        return S3Client.builder().region(Region.of(awsRegion)).endpointOverride(new URI(s3Endpoint)).forcePathStyle(true).build();
    }



}
