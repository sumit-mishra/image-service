package com.debijenkorf.imagery.controller;

import com.debijenkorf.imagery.service.fetcher.ImageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final ImageService imageService;

    @GetMapping({"/show/{predefined-type-name}", "/show/{predefined-type-name}/{seo-name}"})
    public ByteArrayResource getImage(@PathVariable("predefined-type-name") @Valid @NotBlank String predefinedTypeName,
                                      @RequestParam @Valid @NotBlank String reference) {
        log.debug("ImageController::getImage reference {}", reference);
        return imageService.getImage(predefinedTypeName, reference);
    }

    @DeleteMapping("/flush/{predefined-type-name}")
    public void flushImage(@PathVariable("predefined-type-name") @Valid @NotBlank String predefinedTypeName,
                                             @RequestParam @Valid @NotBlank String reference) {
        log.debug("ImageController::flushImage reference {}", reference);
        imageService.flushImage(predefinedTypeName, reference);
    }

}
