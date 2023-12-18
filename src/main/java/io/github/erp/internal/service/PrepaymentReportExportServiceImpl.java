package io.github.erp.internal.service;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import com.hazelcast.map.IMap;
import io.github.erp.domain.PrepaymentReportTuple;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.repository.InternalPrepaymentReportRepository;
import io.github.erp.service.AutonomousReportService;
import io.github.erp.service.dto.AutonomousReportDTO;
import io.github.erp.service.dto.PrepaymentReportDTO;
import io.github.erp.service.mapper.ApplicationUserMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Transactional
@Service("prepaymentReportExportService")
public class PrepaymentReportExportServiceImpl extends ReportListCSVExportService<PrepaymentReportDTO, String> implements DatedReportExportService {

    private final InternalPrepaymentReportRepository prepaymentReportRepository;

    public final IMap<String, String> prepaymentsReportCache;

    private final AutonomousReportService autonomousReportService;
    private final InternalUserDetailService userDetailService;
    private final ApplicationUserMapper applicationUserMapper;

    private final FileStorageService fileStorageService;

    public PrepaymentReportExportServiceImpl(ReportsProperties reportsProperties,
                                             InternalPrepaymentReportRepository prepaymentReportRepository,
                                             IMap<String, String> prepaymentsReportCache,
                                             AutonomousReportService autonomousReportService,
                                             InternalUserDetailService userDetailService,
                                             ApplicationUserMapper applicationUserMapper,
                                             @Qualifier("reportsFSStorageService") FileStorageService fileStorageService) {
        super(reportsProperties);
        this.prepaymentReportRepository = prepaymentReportRepository;
        this.prepaymentsReportCache = prepaymentsReportCache;
        this.autonomousReportService = autonomousReportService;
        this.userDetailService = userDetailService;
        this.applicationUserMapper = applicationUserMapper;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public void exportReportByDate(LocalDate reportDate, String reportName) throws IOException {

        String cacheKey = reportDate.format(DateTimeFormatter.ISO_DATE) + "-" + reportName;

        String cachedReport = prepaymentsReportCache.get(cacheKey);

        if (cachedReport == null) {
            executeReport(reportDate, java.util.UUID.randomUUID().toString(), reportName);
            return;
        }

        if (!cachedReport.equalsIgnoreCase(reportName)) {

            executeReport(reportDate, java.util.UUID.randomUUID().toString(), reportName);
        }

    }

    private void executeReport(LocalDate reportDate, String fileName, String reportName) throws IOException {

        Page<PrepaymentReportDTO> result = prepaymentReportRepository.findAllByReportDate(reportDate, PageRequest.of(0, Integer.MAX_VALUE))
            .map(PrepaymentReportExportServiceImpl::mapPrepaymentReport);

        super.exportToCSVFile(fileName, result.getContent());

        super.cacheReport(reportDate, reportName);

        String fileChecksum = fileStorageService.calculateSha512CheckSum(fileName + ".csv");

        if (userDetailService.getCurrentApplicationUser().isPresent()) {
            AutonomousReportDTO autoReport = new AutonomousReportDTO();
            autoReport.setReportName(reportName);
            autoReport.setReportParameters("Report Date: " + reportDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
            autoReport.setCreatedAt(ZonedDateTime.now());
            autoReport.setReportFilename(UUID.fromString(fileName));
            autoReport.setReportFileContentType("text/csv");
            autoReport.setCreatedBy(
                applicationUserMapper.toDto(
                    userDetailService.getCurrentApplicationUser().get()));
            autoReport.setFileChecksum(fileChecksum);
            autoReport.setReportTampered(false);
            // Save report
            autonomousReportService.save(autoReport);
        } else {
            throw new ApplicationUserNotFoundException("Could not retrieve user from security framework");
        }
    }

    private static PrepaymentReportDTO mapPrepaymentReport(PrepaymentReportTuple prepaymentReportTuple) {

        PrepaymentReportDTO report = new PrepaymentReportDTO();
        report.setId(prepaymentReportTuple.getId());
        report.setCatalogueNumber(prepaymentReportTuple.getCatalogueNumber());
        report.setParticulars(prepaymentReportTuple.getParticulars());
        report.setDealerName(prepaymentReportTuple.getDealerName());
        report.setPaymentNumber(prepaymentReportTuple.getPaymentNumber());
        report.setPaymentDate(prepaymentReportTuple.getPaymentDate());
        report.setCurrencyCode(prepaymentReportTuple.getCurrencyCode());
        report.setPrepaymentAmount(prepaymentReportTuple.getPrepaymentAmount());
        report.setAmortisedAmount(prepaymentReportTuple.getAmortisedAmount());
        report.setOutstandingAmount(prepaymentReportTuple.getOutstandingAmount());

        return report;
    }

    public IMap<String, String> getHazelcastInstanceMap() {
        return prepaymentsReportCache;
    }
}
