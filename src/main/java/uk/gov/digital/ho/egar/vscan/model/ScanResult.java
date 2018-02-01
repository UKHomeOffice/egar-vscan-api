package uk.gov.digital.ho.egar.vscan.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScanResult {

    private boolean clean;
    
    private LocalDate date;

}
