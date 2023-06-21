package com.debijenkorf.imagery.service.fetcher;

import com.debijenkorf.imagery.constant.image.PredefinedType;
import com.debijenkorf.imagery.service.upstream.S3Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service(PredefinedType.ORIGINAL_)
@RequiredArgsConstructor
public class OriginalImageHandler implements ImageHandler {

    private final S3Service s3Service;
    private final SourceImageHandler sourceImageHandler;

    @Override
    @CircuitBreaker(name = "OriginalImageHandler", fallbackMethod = "originalImageFetcherFallback")
    public ByteArrayResource getImage(String reference) {
        log.info("OriginalImageHandler::getImage, calling s3 to fetch the image.");
        return s3Service.getImage(getS3Key(reference));
    }

    @Override
    public String uploadImage(String reference, ByteArrayResource image) throws InterruptedException {
        return s3Service.uploadImage(getS3Key(reference), image);
    }

    @Override
    public void flushImage(String reference) {
        s3Service.flushImage(getS3Key(reference));
    }

    private ByteArrayResource originalImageFetcherFallback(String reference, Exception e) throws InterruptedException {
        log.info("OriginalImageHandler::originalImageFetcherFallback, trying to get source image : {}", e.getMessage());
        ByteArrayResource image = sourceImageHandler.getImage(reference);
        uploadImage(reference, image);
        return image;
    }

    private String getS3Key(final String reference) {
        return String.join(Strings.EMPTY, PredefinedType.ORIGINAL_, "/", reference);
    }

}
