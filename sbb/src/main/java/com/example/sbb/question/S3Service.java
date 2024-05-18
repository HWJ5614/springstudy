package com.example.sbb.question;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

import java.io.File;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3Service {

    private final S3Client s3;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public S3Service() {
        this.s3 = S3Client.builder()
                .region(Region.of("ap-northeast-2"))
                .build();
    }

    public void uploadFile(String key, File file) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl(ObjectCannedACL.PUBLIC_READ) // 퍼블릭 읽기 권한 설정
                    .build();

            PutObjectResponse response = s3.putObject(putObjectRequest, Paths.get(file.getPath()));
        } catch (S3Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileUrl(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
}