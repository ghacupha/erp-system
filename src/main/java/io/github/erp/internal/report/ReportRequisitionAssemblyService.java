package io.github.erp.internal.report;

import io.github.erp.service.dto.ReportRequisitionDTO;
import org.springframework.stereotype.Service;

@Service
public class ReportRequisitionAssemblyService implements ReportAssemblyService<ReportRequisitionDTO>{

    @Override
    public String createReport(ReportRequisitionDTO dto, String fileExtension) {
        return null;
    }
}
