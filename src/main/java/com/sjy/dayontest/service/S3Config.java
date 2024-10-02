package com.sjy.dayontest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.util.List;

@Configuration
public class S3Config {

    @Value("${aws.endpoint}")
    String awsEndpoint;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return AwsCredentialsProviderChain.builder()
                .reuseLastProviderEnabled(true)
                .credentialsProviders(List.of(
                        DefaultCredentialsProvider.create(),
                        StaticCredentialsProvider.create(AwsBasicCredentials.create("foo", "bar"))
                ))
                .build();
    }
    // AwsCredentialsProvider 는 awssdk 에서 aws 를 사용하기 위해 별도의 인증정보를 제공하는 클래스

    // s3 클라이언트는 aws s3 서비스를 실제 사용하기 위한 클라이언트 모듈

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(awsCredentialsProvider())
                .region(Region.AP_NORTHEAST_2)
                .endpointOverride(URI.create(awsEndpoint))
                .build();
    }
}
