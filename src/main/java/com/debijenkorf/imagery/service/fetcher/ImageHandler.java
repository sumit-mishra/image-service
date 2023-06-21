package com.debijenkorf.imagery.service.fetcher;

import org.springframework.core.io.ByteArrayResource;

public interface ImageHandler {

    ByteArrayResource getImage(String reference);

    String uploadImage(String reference, ByteArrayResource image);

    void flushImage(String reference);

}
