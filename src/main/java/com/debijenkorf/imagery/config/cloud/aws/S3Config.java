package com.debijenkorf.imagery.config.cloud.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.internal.Constants;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    private static final long UPLOAD_PART_SIZE_BYTES = 5L * Constants.MB;

    @Value("${debijenkorf.aws.s3.clientRegion}")
    private String clientRegion;

    @Value("${debijenkorf.aws.s3.accessKeyId}")
    private String awsAccessKeyId;

    @Value("${debijenkorf.aws.s3.secretAccessKey}")
    private String awsSecretAccessKey;

    @Value("${debijenkorf.aws.s3.imageBucketName}")
    private String imageBucketName;

    @Bean
    public AmazonS3 s3Client() {
        BasicAWSCredentials credential = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credential))
                .withRegion(clientRegion)
                .build();
    }

    @Bean
    public TransferManager s3TransferManager(AmazonS3 s3Client) {
        return TransferManagerBuilder.standard()
                .withS3Client(s3Client)
                .withMultipartUploadThreshold(UPLOAD_PART_SIZE_BYTES)
                .build();
    }

}
