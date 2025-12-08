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

import io.github.erp.erp.leases.payments.upload.LeasePaymentUploadRequest;
import io.github.erp.erp.leases.payments.upload.LeasePaymentUploadResponse;
import io.github.erp.erp.leases.payments.upload.LeasePaymentUploadService;
import io.github.erp.service.dto.LeasePaymentUploadDTO;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController("leasePaymentUploadResourceProd")
@RequestMapping("/api/leases")
public class LeasePaymentUploadResourceProd {

    private static final Logger log = LoggerFactory.getLogger(LeasePaymentUploadResourceProd.class);

    private final LeasePaymentUploadService uploadService;

    public LeasePaymentUploadResourceProd(LeasePaymentUploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping(value = "/lease-payment-uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LeasePaymentUploadResponse> uploadLeasePayments(
        @RequestPart("request") @Valid LeasePaymentUploadRequest request,
        @RequestPart("file") MultipartFile file
    ) {
        log.debug("REST request to upload lease payments for contract {}", request.getLeaseContractId());
        LeasePaymentUploadResponse response = uploadService.handleUpload(request, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lease-payment-uploads")
    public ResponseEntity<List<LeasePaymentUploadDTO>> getAllUploads(
        @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<LeasePaymentUploadDTO> page = uploadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/_search/lease-payment-uploads")
    public ResponseEntity<List<LeasePaymentUploadDTO>> searchUploads(
        @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
        @RequestParam(name = "query", required = false, defaultValue = "") String query,
        @RequestParam(name = "leaseContractId", required = false) Long leaseContractId
    ) {
        Page<LeasePaymentUploadDTO> page = uploadService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/lease-payment-uploads/{id}/deactivate")
    public ResponseEntity<LeasePaymentUploadDTO> deactivateUpload(@PathVariable Long id) {
        return ResponseEntity.ok(uploadService.deactivateUpload(id));
    }

    @PostMapping("/lease-payment-uploads/{id}/activate")
    public ResponseEntity<LeasePaymentUploadDTO> activateUpload(@PathVariable Long id) {
        return ResponseEntity.ok(uploadService.activateUpload(id));
    }
}
