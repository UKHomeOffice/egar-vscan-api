package uk.gov.digital.ho.egar.vscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

@Value
@EqualsAndHashCode
@Builder
public class ScanNotification {
    @JsonProperty("file_uuid")
    private UUID fileUuid;
    @JsonProperty("file_status")
    private ScannedFileStatus fileStatus;
}
