package com.debijenkorf.imagery.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {
    private String type;
    private String errorMessage;
}
