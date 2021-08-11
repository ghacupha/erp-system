package io.github.erp.internal.resource;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
