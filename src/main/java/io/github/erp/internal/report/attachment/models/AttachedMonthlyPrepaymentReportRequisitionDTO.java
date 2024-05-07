package io.github.erp.internal.report.attachment.models;

import io.github.erp.internal.model.HasChecksum;
import io.github.erp.internal.report.attachment.AttachedUnTamperedReport;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.FiscalYearDTO;
import io.github.erp.service.dto.MonthlyPrepaymentReportRequisitionDTO;
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachedMonthlyPrepaymentReportRequisitionDTO  implements AttachedUnTamperedReport<MonthlyPrepaymentReportRequisitionDTO>, HasChecksum {

    private Long id;

    @NotNull
    private String reportName;

    @NotNull
    private ZonedDateTime timeOfRequisition;

    private String fileChecksum;

    private Boolean tampered;

    private UUID filename;

    private String reportParameters;

    @Lob
    private byte[] reportFile;

    private String reportFileContentType;
    private ApplicationUserDTO requestedBy;

    private ApplicationUserDTO lastAccessedBy;

    private FiscalYearDTO fiscalYear;

    @Override
    public void setChecksum(String fileChecksum) {

        this.fileChecksum = fileChecksum;
    }

    @Override
    public AttachedUnTamperedReport<MonthlyPrepaymentReportRequisitionDTO> setReportAttachment(byte[] reportResource) {

        this.reportFile = reportResource;

        return this;
    }

    /**
     * @return Report name as it is saved in the DB
     */
    @Override
    public String getReportName() {
        return reportName;
    }

    /**
     * @return Filename as UUID
     */
    @Override
    public UUID getReportId() {
        return filename;
    }

    @Override
    public void setReportTampered(boolean reportIsTampered) {

        this.tampered = reportIsTampered;
    }

    @Override
    public boolean getReportTampered() {
        return tampered;
    }
}
