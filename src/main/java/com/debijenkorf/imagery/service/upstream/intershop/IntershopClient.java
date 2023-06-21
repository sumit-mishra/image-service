package com.debijenkorf.imagery.service.upstream.intershop;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * IntershopClient uses OpenFeign to consume upstream service (source image)
 */
@FeignClient(name = "intershop-api", url = "${debijenkorf.source.root.url}")
public interface IntershopClient {

    @GetMapping
    Optional<ByteArrayResource> getImage(@RequestParam(value = "reference") String reference);

}
