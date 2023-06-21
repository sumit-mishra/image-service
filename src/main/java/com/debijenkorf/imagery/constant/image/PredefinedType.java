package com.debijenkorf.imagery.constant.image;

public enum PredefinedType {
    SOURCE(PredefinedType.SOURCE_),
    ORIGINAL(PredefinedType.ORIGINAL_),
    THUMBNAIL(PredefinedType.ORIGINAL_);

    public static final String SOURCE_ = "source";
    public static final String ORIGINAL_ = "original";
    public static final String THUMBNAIL_ = "thumbnail";

    private final String predefinedType;

    PredefinedType(String predefinedType) {
        this.predefinedType = predefinedType;
    }

}
