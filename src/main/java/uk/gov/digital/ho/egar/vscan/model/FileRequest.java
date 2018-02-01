package uk.gov.digital.ho.egar.vscan.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
public class FileRequest {

    @NotNull
    @JsonProperty("file_uuid")
    private UUID fileUuid;

    @NotNull
    @JsonProperty("file_link")
    private String fileLink;
}
