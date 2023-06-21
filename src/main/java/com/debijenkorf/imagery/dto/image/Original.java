package com.debijenkorf.imagery.dto.image;

public class Original implements Image {
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
        return null;
    }
}
