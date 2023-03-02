package io.github.erp.erp.reports;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementBillerReportRequisitionDTO {
    private long biller_id;
    private ZonedDateTime from;
    private ZonedDateTime to;
}
