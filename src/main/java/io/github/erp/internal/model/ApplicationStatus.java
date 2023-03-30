package io.github.erp.internal.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationStatus {

    private String build;

    private String version;

    private String profile;

    private String branch;
}
