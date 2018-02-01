package uk.gov.digital.ho.egar.vscan.model;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ScannedFileStatus {
    CLEAN,
    INFECTED;

    @JsonCreator
    public static ScannedFileStatus forValue(String value) {
        if (value == null) {
            return null;
        }

        return ScannedFileStatus.valueOf(StringUtils.upperCase(value));
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
