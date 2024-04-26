package io.github.erp.erp.resources.wip;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.WorkInProgressReportREPO;
import io.github.erp.internal.repository.InternalWIPProjectDealerSummaryReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.time.LocalDate;
import java.util.List;

@Deprecated
@RestController
@RequestMapping("/api/fixed-asset")
public class WorkInProgressReportsResourceProd {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressReportsResourceProd.class);



//    public WorkInProgressReportsResourceProd(InternalWIPProjectDealerSummaryReportRepository internalWIPOutstandingReportRepository) {
//        this.internalWIPOutstandingReportRepository = internalWIPOutstandingReportRepository;
//    }


}
