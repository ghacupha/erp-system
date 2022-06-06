package io.github.erp.internal.report;

import io.github.erp.internal.model.AttachedXlsxReportRequisitionDTO;
import lombok.SneakyThrows;

public interface AttachedRepoprtService<AttachedXlsxReportRequisitionDTO> {
    @SneakyThrows
    AttachedXlsxReportRequisitionDTO attachReport(AttachedXlsxReportRequisitionDTO one);
}
