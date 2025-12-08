package io.github.erp.erp.leases.payments.upload;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.CsvFileUpload;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeasePayment;
import io.github.erp.domain.LeasePaymentUpload;
import io.github.erp.erp.resources.LeasePaymentResourceIT;
import io.github.erp.erp.resources.IFRS16LeaseContractResourceIT;
import io.github.erp.repository.CsvFileUploadRepository;
import io.github.erp.repository.LeasePaymentRepository;
import io.github.erp.repository.LeasePaymentUploadRepository;
import io.github.erp.repository.search.LeasePaymentSearchRepository;
import io.github.erp.repository.search.LeasePaymentSearchRepositoryMockConfiguration;
import io.github.erp.service.dto.LeasePaymentUploadDTO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.StreamSupport;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@IntegrationTest
@Import(LeasePaymentSearchRepositoryMockConfiguration.class)
@EmbeddedKafka(topics = "lease-payment-reindex", bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class LeasePaymentUploadServiceIT {

    @Autowired
    private LeasePaymentUploadService leasePaymentUploadService;

    @Autowired
    private LeasePaymentUploadRepository leasePaymentUploadRepository;

    @Autowired
    private LeasePaymentRepository leasePaymentRepository;

    @Autowired
    private CsvFileUploadRepository csvFileUploadRepository;

    @Autowired
    private LeasePaymentSearchRepository mockLeasePaymentSearchRepository;

    @Autowired
    private EntityManager em;

    private Map<Long, LeasePayment> indexedPayments;

    @BeforeEach
    void setUp() {
        leasePaymentRepository.deleteAll();
        leasePaymentUploadRepository.deleteAll();
        csvFileUploadRepository.deleteAll();
        em.flush();

        indexedPayments = new ConcurrentHashMap<>();

        when(mockLeasePaymentSearchRepository.save(any(LeasePayment.class))).thenAnswer(invocation -> {
            LeasePayment leasePayment = invocation.getArgument(0);
            indexedPayments.put(leasePayment.getId(), leasePayment);
            return leasePayment;
        });

        when(mockLeasePaymentSearchRepository.saveAll(any(Iterable.class))).thenAnswer(invocation -> {
            Iterable<LeasePayment> payments = invocation.getArgument(0);
            payments.forEach(payment -> indexedPayments.put(payment.getId(), payment));
            return payments;
        });

        when(mockLeasePaymentSearchRepository.search(anyString(), any())).thenAnswer(invocation -> {
            String query = invocation.getArgument(0);
            List<LeasePayment> results = new ArrayList<>();
            if (query.startsWith("id:")) {
                String idValue = query.substring("id:".length());
                Long id = Long.valueOf(idValue);
                LeasePayment leasePayment = indexedPayments.get(id);
                if (leasePayment != null) {
                    results.add(leasePayment);
                }
            } else {
                results.addAll(indexedPayments.values());
            }
            int pageSize = Math.max(results.size(), 1);
            return new PageImpl<>(results, PageRequest.of(0, pageSize), results.size());
        });
    }

    @Test
    void deactivateUploadReindexesLeasePaymentsThroughQueue() {
        IFRS16LeaseContract contract = IFRS16LeaseContractResourceIT.createEntity(em);
        em.persist(contract);
        em.flush();

        CsvFileUpload csvFileUpload = new CsvFileUpload()
            .originalFileName("payments.csv")
            .storedFileName("payments-" + UUID.randomUUID() + ".csv")
            .filePath("/tmp/payments.csv")
            .fileSize(20L)
            .contentType("text/csv")
            .uploadedAt(Instant.now())
            .processed(Boolean.TRUE)
            .checksum("checksum");
        csvFileUploadRepository.saveAndFlush(csvFileUpload);

        LeasePaymentUpload upload = new LeasePaymentUpload()
            .leaseContract(contract)
            .csvFileUpload(csvFileUpload)
            .uploadStatus("ACTIVE")
            .active(Boolean.TRUE)
            .createdAt(Instant.now());
        csvFileUpload.setLeasePaymentUpload(upload);
        upload = leasePaymentUploadRepository.saveAndFlush(upload);

        LeasePayment firstPayment = LeasePaymentResourceIT.createEntity(em);
        firstPayment.setLeaseContract(contract);
        firstPayment.setLeasePaymentUpload(upload);

        LeasePayment secondPayment = LeasePaymentResourceIT.createEntity(em);
        secondPayment.setLeaseContract(contract);
        secondPayment.setLeasePaymentUpload(upload);

        leasePaymentRepository.saveAll(List.of(firstPayment, secondPayment));

        LeasePaymentUploadDTO response = leasePaymentUploadService.deactivateUpload(upload.getId());

        assertThat(response.getUploadStatus()).isEqualTo("DEACTIVATED");
        assertThat(response.getActive()).isFalse();

        verify(mockLeasePaymentSearchRepository, timeout(5000))
            .saveAll(argThat(iterable ->
                StreamSupport
                    .stream(((Iterable<LeasePayment>) iterable).spliterator(), false)
                    .allMatch(payment -> Boolean.FALSE.equals(payment.getActive()))
            ));

        List<LeasePayment> updated = leasePaymentRepository.findAllByLeasePaymentUploadId(upload.getId());
        assertThat(updated).hasSize(2);
        assertThat(updated).allSatisfy(payment -> assertThat(payment.getActive()).isFalse());

        Page<LeasePayment> searchPage = mockLeasePaymentSearchRepository.search("id:" + updated.get(0).getId(), PageRequest.of(0, 5));
        assertThat(searchPage.getContent()).hasSize(1);
        assertThat(searchPage.getContent().get(0).getActive()).isFalse();
    }
}
