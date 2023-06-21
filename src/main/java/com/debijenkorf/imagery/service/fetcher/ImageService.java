package com.debijenkorf.imagery.service.fetcher;

import com.debijenkorf.imagery.constant.image.PredefinedType;
import com.debijenkorf.imagery.exception.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final Map<String, ImageHandler> imageHandlers;

    public ByteArrayResource getImage(final String predefinedTypeName, final String reference) {
        log.debug("ImageService::getImage reference {}", reference);
        ImageHandler imageHandler = imageHandlers.get(predefinedTypeName);
        if (nonNull(imageHandler)) {
            return imageHandler.getImage(reference);
        } else {
            log.info("ImageService::getImage, requested predefined image type does not exist. {}", reference);
            throw new ImageNotFoundException("INVALID_PREDEFINED_TYPE", "The requested predefined image type does not exist.");
        }
    }

    public void flushImage(final String predefinedTypeName, final String reference) {
        log.debug("ImageService::flushImage reference {}", reference);
        if (PredefinedType.ORIGINAL_.equals(predefinedTypeName)) {
            imageHandlers.values().forEach(imageHandler -> imageHandler.flushImage(reference));
        } else {
            ImageHandler imageHandler = imageHandlers.get(predefinedTypeName);
            imageHandler.flushImage(reference);
        }
    }

}
