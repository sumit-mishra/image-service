package com.debijenkorf.imagery.service.upstream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.debijenkorf.imagery.exception.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 s3;
    private final TransferManager transferManager;

    public ByteArrayResource getImage(GetObjectRequest request) {
        try {
            S3Object object = s3.getObject(request);
            return new ByteArrayResource(object.getObjectContent().readAllBytes());
        } catch (Exception e) {
            log.error("S3Service::getImage, could not fetch the image, reference : {}, errorMessage : {}", request.getKey(), e.getMessage());
            throw new ImageNotFoundException("S3_REFERENCE_NOT_FOUND", String.format("Image '%s' not found", request.getKey()));
        }
    }

    public ByteArrayResource getImage(final String reference) {
        try {
            // return new ByteArrayResource("dummy_image".getBytes());
            S3Object object = s3.getObject("", reference);
            return new ByteArrayResource(object.getObjectContent().readAllBytes());
        } catch (Exception e) {
            log.error("S3Service::getImage, could not fetch the image, reference : {}, errorMessage : {}", reference, e.getMessage());
            throw new ImageNotFoundException("S3_REFERENCE_NOT_FOUND", String.format("Image '%s' not found", reference));
        }
    }

    /**
     * This can be an asynchronous method to upload the image for better performance
     */
    public String uploadImage(PutObjectRequest request) throws InterruptedException {
        Upload upload = transferManager.upload(request);
        return upload.waitForUploadResult().getKey();
    }


    /**
     * This can be an asynchronous method to upload the image for better performance
     */
    public String uploadImage(final String key, final ByteArrayResource resource) throws InterruptedException {
        log.info("Uploading {} to s3.", key);
        Upload upload = transferManager.upload("",
                                                key,
                                                new ByteArrayInputStream(resource.getByteArray()),
                                                getObjectMetadata(resource));
        return upload.waitForUploadResult().getKey();
    }

    public void flushImage(final String reference) {
        s3.deleteObject("", reference);
    }

    public void flushImage(DeleteObjectRequest deleteObjectRequest) {
        s3.deleteObject(deleteObjectRequest);
    }

    private ObjectMetadata getObjectMetadata(final ByteArrayResource resource) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(resource.contentLength());
        objectMetadata.setContentType(Mimetypes.MIMETYPE_OCTET_STREAM);
        return objectMetadata;
    }

}
