package com.debijenkorf.imagery.service.fetcher;

import com.debijenkorf.imagery.constant.image.PredefinedType;
import com.debijenkorf.imagery.exception.ImageNotFoundException;
import com.debijenkorf.imagery.service.upstream.intershop.IntershopClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service(PredefinedType.SOURCE_)
@RequiredArgsConstructor
public class SourceImageHandler implements ImageHandler {

    private final IntershopClient intershopClient;

    @Override
    public ByteArrayResource getImage(String reference) {
        try {
            log.info("SourceImageHandler::getImage, calling source to fetch the image.");
            Optional<ByteArrayResource> image = intershopClient.getImage(reference);
            if (image.isPresent()) {
                return image.get();
            } else {
                throw new ImageNotFoundException("SOURCE_REFERENCE_NOT_FOUND",
                        String.format("Image not found in the '%s'.", PredefinedType.SOURCE_));
            }
        } catch (Exception e) {
            log.error("SourceImageHandler::getImage, failed to fetch the image {}", e.getMessage());
            throw new ImageNotFoundException("SOURCE_UNREACHABLE",
                    String.format("Image could not be retrieved from '%s'.", PredefinedType.SOURCE_));
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
