package com.debijenkorf.imagery.service.fetcher;

import com.debijenkorf.imagery.constant.image.PredefinedType;
import com.debijenkorf.imagery.service.compression.ImageCompressionService;
import com.debijenkorf.imagery.service.upstream.S3Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service(PredefinedType.THUMBNAIL_)
@RequiredArgsConstructor
public class ThumbnailImageHandler implements ImageHandler {

    private final S3Service s3Service;
    private final OriginalImageHandler originalImageHandler;
    private final ImageCompressionService compressionService;

    @Override
    @CircuitBreaker(name = "ThumbnailImageHandler", fallbackMethod = "thumbnailImageFetcherFallback")
    public ByteArrayResource getImage(String reference) {
        log.info("ThumbnailImageHandler::getImage, calling s3 to fetch the image.");
        return s3Service.getImage(getS3Key(reference));
    }

    @Override
    public String uploadImage(String reference, ByteArrayResource image) throws InterruptedException {
        return s3Service.uploadImage(reference, image);
    }

    @Override
    public void flushImage(String reference) {
        s3Service.flushImage(reference);
    }

    private ByteArrayResource thumbnailImageFetcherFallback(String reference, Exception e) throws InterruptedException {
        log.info("ThumbnailImageHandler::thumbnailImageFetcherFallback, trying to get original image : {}", e.getMessage());
        ByteArrayResource image = originalImageHandler.getImage(reference);
        image = compressionService.compress(image, PredefinedType.THUMBNAIL_);
        uploadToS3ThumbnailDirectory(reference, image);
        return image;
    }

    private void uploadToS3ThumbnailDirectory(String reference, ByteArrayResource image) throws InterruptedException {
        uploadImage(getS3Key(reference), image);
    }

    private String getS3Key(final String reference) {
        return String.join(Strings.EMPTY, PredefinedType.THUMBNAIL_, "/", reference);
    }

}
