package com.debijenkorf.imagery.service.compression;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ImageCompressionService {

    public ByteArrayResource compress(ByteArrayResource image, String compressionStandard) {
        log.info("ImageCompressionService::compress, compressing the source image to {}", compressionStandard);
        return new ByteArrayResource("mocked-image".getBytes());
    }

}
