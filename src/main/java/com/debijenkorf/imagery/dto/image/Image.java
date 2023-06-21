package com.debijenkorf.imagery.dto.image;

public interface Image {

    /**
     * @return the height of the image as int value.
     */
    int getHeight();

    /**
     * @return the width of the image as int value.
     */
    int getWidth();

    /**
     * @return the compression quality from as an int value.
     * The quality represents a int value ranging between 0 - 100
     */
    int getQuality();

    /**
     * @return the type i.e. how is the image resized.
     */
    ScaleType getScaleType();

    /**
     * @return a hex value of a color, used when the Scale-type is set to Fill.
     */
    String getFillColor();

    /**
     * @return should represent the base url of the source image
     */
    String getSourceName();

}
