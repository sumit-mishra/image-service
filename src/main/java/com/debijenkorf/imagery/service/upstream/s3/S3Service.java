package com.debijenkorf.imagery.service.upstream.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.debijenkorf.imagery.exception.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3;
    private final TransferManager transferManager;
    @Value("${debijenkorf.aws.s3.imageBucketName}")
    private String productImageBucket;

    public ByteArrayResource getImage(final String key) {
        log.info("S3Service::getImage, getting {} from s3.", key);
        try {
            S3Object object = s3.getObject(productImageBucket, key);
            return new ByteArrayResource(object.getObjectContent().readAllBytes());
        } catch (Exception e) {
            log.error("S3Service::getImage, could not fetch the image, key : {}, errorMessage : {}", key, e.getMessage());
            throw new ImageNotFoundException("S3_REFERENCE_NOT_FOUND", String.format("Image '%s' not found", key));
        }
    }

    /**
     * This can be an asynchronous method to upload the image for better performance
     */
    public String uploadImage(final String key, final ByteArrayResource resource) {
        log.info("S3Service::uploadImage, uploading {} to s3.", key);
        try {
            Upload upload = transferManager.upload(productImageBucket,
                                                   key,
                                                   new ByteArrayInputStream(resource.getByteArray()),
                                                   getObjectMetadata(resource));
            return upload.waitForUploadResult().getKey();
        } catch (Exception e) {
            log.error("S3Service::uploadImage, could not upload the image, key : {}, errorMessage : {}", key, e.getMessage());
            throw new ImageNotFoundException("S3_UPLOAD_FAILED", String.format("Image '%s' could not be uploaded.", key));
        }
    }

    public void flushImage(final String key) {
        log.info("S3Service::flushImage, flushing {} from s3.", key);
        try {
            s3.deleteObject(productImageBucket, key);
        } catch (Exception e) {
            log.error("S3Service::flushImage, could not flush the image, key : {}, errorMessage : {}", key, e.getMessage());
            throw new ImageNotFoundException("S3_FLUSH_FAILED", String.format("Image '%s' could not be flushed.", key));
        }
    }

    private ObjectMetadata getObjectMetadata(final ByteArrayResource resource) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(resource.contentLength());
        objectMetadata.setContentType(Mimetypes.MIMETYPE_OCTET_STREAM);
        return objectMetadata;
    }

}
