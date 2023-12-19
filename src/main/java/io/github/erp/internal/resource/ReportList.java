package io.github.erp.internal.resource;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * This is a general interface that gives a report but summarised as a
 * <p/>
 * list of pre-designated measures, and served as a resource.
 * <p/>
 *  Check the DTO used for each implementation. Strictly speaking this has nothing
 * <p/>
 * to do with file processing, but might be the only available way you will have for checking
 * <p/>
 * that your data has been uploaded for a given entity
 * <p/>
 * A typical implementation might look like so:
 * <p/>
 * {@code
 *
        @Slf4j
        @RestController
        @RequestMapping("/api/app")
        public class SummaryDpfReportListResource implements ReportList<DPFSummary, LocalDate> {

            private final Report<List<DPFSummary>, DPFSummaryParameters> dpfSummaryReport;

            public SummaryDpfReportListResource(final Report<List<DPFSummary>, DPFSummaryParameters> dpfSummaryReport) {
                this.dpfSummaryReport = dpfSummaryReport;
            }

            @Override
            @GetMapping("/summary/dpf")
            public ResponseEntity<List<DPFSummary>> getEntityList(@RequestParam LocalDate monthOfStudy) {
                log.info("Request for date : {} received...", monthOfStudy.format(DATETIME_FORMATTER));
                return ResponseEntity.ok(dpfSummaryReport.createReport(new DPFSummaryParameters(monthOfStudy)));
            }
        }
 *
 * }
 *
 */
public interface ReportList<Entity, Parameter> {

    ResponseEntity<List<Entity>> getEntityList(Parameter parameters);
}
