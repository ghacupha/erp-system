package io.github.erp.erp.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

// todo change parameters
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaseContractReportRequisition {

    private long billerId;
    private ZonedDateTime from;
    private ZonedDateTime to;
}
