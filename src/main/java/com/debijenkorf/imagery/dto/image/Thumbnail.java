package com.debijenkorf.imagery.dto.image;

import org.springframework.beans.factory.annotation.Value;

public class Thumbnail implements Image {

    @Value("${debijenkorf.ecom.image.base}")
    private String sourceName;

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getQuality() {
        return 0;
    }

    @Override
    public ScaleType getScaleType() {
        return null;
    }

    @Override
    public String getFillColor() {
        return null;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }
}
