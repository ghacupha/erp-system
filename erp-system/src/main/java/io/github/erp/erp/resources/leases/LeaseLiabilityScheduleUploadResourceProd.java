package io.github.erp.erp.resources.leases;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.erp.leases.liability.schedule.upload.LeaseLiabilityScheduleUploadRequest;
import io.github.erp.erp.leases.liability.schedule.upload.LeaseLiabilityScheduleUploadResponse;
import io.github.erp.erp.leases.liability.schedule.upload.LeaseLiabilityScheduleUploadService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("leaseLiabilityScheduleUploadResourceProd")
@RequestMapping("/api/leases")
public class LeaseLiabilityScheduleUploadResourceProd {

    private static final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleUploadResourceProd.class);

    private final LeaseLiabilityScheduleUploadService uploadService;

    public LeaseLiabilityScheduleUploadResourceProd(LeaseLiabilityScheduleUploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping(value = "/lease-liability-schedule-file-uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LeaseLiabilityScheduleUploadResponse> uploadLeaseLiabilitySchedule(
        @RequestPart("request") @Valid LeaseLiabilityScheduleUploadRequest request,
        @RequestPart("file") MultipartFile file
    ) {
        log.debug("REST request to upload lease liability schedule for liability {}", request.getLeaseLiabilityId());
        LeaseLiabilityScheduleUploadResponse response = uploadService.handleUpload(request, file);
        return ResponseEntity.ok(response);
    }
}

