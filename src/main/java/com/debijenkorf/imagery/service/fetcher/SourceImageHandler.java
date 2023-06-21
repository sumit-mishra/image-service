package com.debijenkorf.imagery.service.fetcher;

import com.debijenkorf.imagery.constant.image.PredefinedType;
import com.debijenkorf.imagery.exception.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service(PredefinedType.SOURCE_)
@RequiredArgsConstructor
public class SourceImageHandler implements ImageHandler {

    //TODO : include feign

    @Override
    public ByteArrayResource getImage(String reference) {
        try {
            log.info("SourceImageHandler::getImage, calling source to fetch the image.");
            return new ByteArrayResource("source-image".getBytes());
        } catch (ImageNotFoundException e) {
            log.error("SourceImageHandler::getImage, failed to fetch the image {}", e.getMessage());
            throw new ImageNotFoundException("SOURCE_REFERENCE_NOT_FOUND",
                    String.format("Image not found int the '%s' directory", PredefinedType.SOURCE_));
        }
    }

    @Override
    public String uploadImage(String reference, ByteArrayResource image) {
        return null;
    }

    @Override
    public void flushImage(String reference) {
    }

}
