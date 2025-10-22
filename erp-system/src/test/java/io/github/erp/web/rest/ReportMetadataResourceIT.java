package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.ReportMetadata;
import io.github.erp.repository.ReportMetadataRepository;
import io.github.erp.repository.search.ReportMetadataSearchRepository;
import io.github.erp.service.dto.ReportMetadataDTO;
import io.github.erp.service.mapper.ReportMetadataMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReportMetadataResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReportMetadataResourceIT {

    private static final String DEFAULT_REPORT_TITLE = "Dynamic summary";
    private static final String UPDATED_REPORT_TITLE = "Updated summary";

    private static final String DEFAULT_DESCRIPTION = "Preview of aggregated data";
    private static final String UPDATED_DESCRIPTION = "Updated preview of aggregated data";

    private static final String DEFAULT_MODULE = "Leases";
    private static final String UPDATED_MODULE = "Assets";

    private static final String DEFAULT_PAGE_PATH = "reports/view/test-default";
    private static final String UPDATED_PAGE_PATH = "reports/view/test-updated";

    private static final String DEFAULT_BACKEND_API = "api/test-default-endpoint";
    private static final String UPDATED_BACKEND_API = "api/test-updated-endpoint";

    private static final Boolean DEFAULT_ACTIVE = true;
    private static final Boolean UPDATED_ACTIVE = false;

    private static final Boolean DEFAULT_DISPLAY_LEASE_PERIOD = true;
    private static final Boolean UPDATED_DISPLAY_LEASE_PERIOD = false;

    private static final Boolean DEFAULT_DISPLAY_LEASE_CONTRACT = false;
    private static final Boolean UPDATED_DISPLAY_LEASE_CONTRACT = true;

    private static final String DEFAULT_LEASE_PERIOD_QUERY_PARAM = "leasePeriodId.equals";
    private static final String UPDATED_LEASE_PERIOD_QUERY_PARAM = "fiscalPeriodEndDate.equals";

    private static final String DEFAULT_LEASE_CONTRACT_QUERY_PARAM = "leaseLiabilityId.equals";
    private static final String UPDATED_LEASE_CONTRACT_QUERY_PARAM = "leaseContractId.equals";

    private static final String ENTITY_API_URL = "/api/report-metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/report-metadata";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportMetadataRepository reportMetadataRepository;

    @Autowired
    private ReportMetadataMapper reportMetadataMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ReportMetadataSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReportMetadataSearchRepository mockReportMetadataSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportMetadataMockMvc;

    private ReportMetadata reportMetadata;

    public static ReportMetadata createEntity(EntityManager em) {
        ReportMetadata reportMetadata = new ReportMetadata()
            .reportTitle(DEFAULT_REPORT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .module(DEFAULT_MODULE)
            .pagePath(DEFAULT_PAGE_PATH)
            .backendApi(DEFAULT_BACKEND_API)
            .active(DEFAULT_ACTIVE)
            .displayLeasePeriod(DEFAULT_DISPLAY_LEASE_PERIOD)
            .displayLeaseContract(DEFAULT_DISPLAY_LEASE_CONTRACT)
            .leasePeriodQueryParam(DEFAULT_LEASE_PERIOD_QUERY_PARAM)
            .leaseContractQueryParam(DEFAULT_LEASE_CONTRACT_QUERY_PARAM);
        return reportMetadata;
    }

    public static ReportMetadata createUpdatedEntity(EntityManager em) {
        ReportMetadata reportMetadata = new ReportMetadata()
            .reportTitle(UPDATED_REPORT_TITLE)
            .description(UPDATED_DESCRIPTION)
            .module(UPDATED_MODULE)
            .pagePath(UPDATED_PAGE_PATH)
            .backendApi(UPDATED_BACKEND_API)
            .active(UPDATED_ACTIVE)
            .displayLeasePeriod(UPDATED_DISPLAY_LEASE_PERIOD)
            .displayLeaseContract(UPDATED_DISPLAY_LEASE_CONTRACT)
            .leasePeriodQueryParam(UPDATED_LEASE_PERIOD_QUERY_PARAM)
            .leaseContractQueryParam(UPDATED_LEASE_CONTRACT_QUERY_PARAM);
        return reportMetadata;
    }

    @BeforeEach
    public void initTest() {
        reset(mockReportMetadataSearchRepository);
        reportMetadata = createEntity(em);
    }

    @Test
    @Transactional
    void createReportMetadata() throws Exception {
        int databaseSizeBeforeCreate = reportMetadataRepository.findAll().size();
        // Create the ReportMetadata
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);
        restReportMetadataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportMetadataDTO)))
            .andExpect(status().isCreated());

        // Validate the ReportMetadata in the database
        List<ReportMetadata> reportMetadataList = reportMetadataRepository.findAll();
        assertThat(reportMetadataList).hasSize(databaseSizeBeforeCreate + 1);
        ReportMetadata testReportMetadata = reportMetadataList.get(reportMetadataList.size() - 1);
        assertThat(testReportMetadata.getReportTitle()).isEqualTo(DEFAULT_REPORT_TITLE);
        assertThat(testReportMetadata.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReportMetadata.getModule()).isEqualTo(DEFAULT_MODULE);
        assertThat(testReportMetadata.getPagePath()).isEqualTo(DEFAULT_PAGE_PATH);
        assertThat(testReportMetadata.getBackendApi()).isEqualTo(DEFAULT_BACKEND_API);
        assertThat(testReportMetadata.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testReportMetadata.getDisplayLeasePeriod()).isEqualTo(DEFAULT_DISPLAY_LEASE_PERIOD);
        assertThat(testReportMetadata.getDisplayLeaseContract()).isEqualTo(DEFAULT_DISPLAY_LEASE_CONTRACT);
        assertThat(testReportMetadata.getLeasePeriodQueryParam()).isEqualTo(DEFAULT_LEASE_PERIOD_QUERY_PARAM);
        assertThat(testReportMetadata.getLeaseContractQueryParam()).isEqualTo(DEFAULT_LEASE_CONTRACT_QUERY_PARAM);

        // Validate the ReportMetadata in Elasticsearch
        verify(mockReportMetadataSearchRepository, times(1)).save(testReportMetadata);
    }

    @Test
    @Transactional
    void createReportMetadataWithExistingId() throws Exception {
        // Create the ReportMetadata with an existing ID
        reportMetadata.setId(1L);
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        int databaseSizeBeforeCreate = reportMetadataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportMetadataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportMetadataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportMetadata in the database
        List<ReportMetadata> reportMetadataList = reportMetadataRepository.findAll();
        assertThat(reportMetadataList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReportMetadata in Elasticsearch
        verify(mockReportMetadataSearchRepository, times(0)).save(reportMetadata);
    }

    @Test
    @Transactional
    void getAllReportMetadata() throws Exception {
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        restReportMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportTitle").value(hasItem(DEFAULT_REPORT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].module").value(hasItem(DEFAULT_MODULE)))
            .andExpect(jsonPath("$.[*].pagePath").value(hasItem(DEFAULT_PAGE_PATH)))
            .andExpect(jsonPath("$.[*].backendApi").value(hasItem(DEFAULT_BACKEND_API)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].displayLeasePeriod").value(hasItem(DEFAULT_DISPLAY_LEASE_PERIOD.booleanValue())))
            .andExpect(jsonPath("$.[*].displayLeaseContract").value(hasItem(DEFAULT_DISPLAY_LEASE_CONTRACT.booleanValue())))
            .andExpect(jsonPath("$.[*].leasePeriodQueryParam").value(hasItem(DEFAULT_LEASE_PERIOD_QUERY_PARAM)))
            .andExpect(jsonPath("$.[*].leaseContractQueryParam").value(hasItem(DEFAULT_LEASE_CONTRACT_QUERY_PARAM)));
    }

    @Test
    @Transactional
    void getReportMetadata() throws Exception {
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        restReportMetadataMockMvc
            .perform(get(ENTITY_API_URL_ID, reportMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportMetadata.getId().intValue()))
            .andExpect(jsonPath("$.reportTitle").value(DEFAULT_REPORT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.module").value(DEFAULT_MODULE))
            .andExpect(jsonPath("$.pagePath").value(DEFAULT_PAGE_PATH))
            .andExpect(jsonPath("$.backendApi").value(DEFAULT_BACKEND_API))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.displayLeasePeriod").value(DEFAULT_DISPLAY_LEASE_PERIOD.booleanValue()))
            .andExpect(jsonPath("$.displayLeaseContract").value(DEFAULT_DISPLAY_LEASE_CONTRACT.booleanValue()))
            .andExpect(jsonPath("$.leasePeriodQueryParam").value(DEFAULT_LEASE_PERIOD_QUERY_PARAM))
            .andExpect(jsonPath("$.leaseContractQueryParam").value(DEFAULT_LEASE_CONTRACT_QUERY_PARAM));
    }

    @Test
    @Transactional
    void getNonExistingReportMetadata() throws Exception {
        restReportMetadataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReportMetadata() throws Exception {
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        int databaseSizeBeforeUpdate = reportMetadataRepository.findAll().size();

        // Update the reportMetadata
        ReportMetadata updatedReportMetadata = reportMetadataRepository.findById(reportMetadata.getId()).get();
        em.detach(updatedReportMetadata);
        updatedReportMetadata
            .reportTitle(UPDATED_REPORT_TITLE)
            .description(UPDATED_DESCRIPTION)
            .module(UPDATED_MODULE)
            .pagePath(UPDATED_PAGE_PATH)
            .backendApi(UPDATED_BACKEND_API)
            .active(UPDATED_ACTIVE)
            .displayLeasePeriod(UPDATED_DISPLAY_LEASE_PERIOD)
            .displayLeaseContract(UPDATED_DISPLAY_LEASE_CONTRACT)
            .leasePeriodQueryParam(UPDATED_LEASE_PERIOD_QUERY_PARAM)
            .leaseContractQueryParam(UPDATED_LEASE_CONTRACT_QUERY_PARAM);
        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(updatedReportMetadata);

        restReportMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportMetadataDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportMetadata in the database
        List<ReportMetadata> reportMetadataList = reportMetadataRepository.findAll();
        assertThat(reportMetadataList).hasSize(databaseSizeBeforeUpdate);
        ReportMetadata testReportMetadata = reportMetadataList.get(reportMetadataList.size() - 1);
        assertThat(testReportMetadata.getReportTitle()).isEqualTo(UPDATED_REPORT_TITLE);
        assertThat(testReportMetadata.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReportMetadata.getModule()).isEqualTo(UPDATED_MODULE);
        assertThat(testReportMetadata.getPagePath()).isEqualTo(UPDATED_PAGE_PATH);
        assertThat(testReportMetadata.getBackendApi()).isEqualTo(UPDATED_BACKEND_API);
        assertThat(testReportMetadata.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testReportMetadata.getDisplayLeasePeriod()).isEqualTo(UPDATED_DISPLAY_LEASE_PERIOD);
        assertThat(testReportMetadata.getDisplayLeaseContract()).isEqualTo(UPDATED_DISPLAY_LEASE_CONTRACT);
        assertThat(testReportMetadata.getLeasePeriodQueryParam()).isEqualTo(UPDATED_LEASE_PERIOD_QUERY_PARAM);
        assertThat(testReportMetadata.getLeaseContractQueryParam()).isEqualTo(UPDATED_LEASE_CONTRACT_QUERY_PARAM);

        // Validate the ReportMetadata in Elasticsearch
        verify(mockReportMetadataSearchRepository).save(testReportMetadata);
    }

    @Test
    @Transactional
    void putNonExistingReportMetadata() throws Exception {
        int databaseSizeBeforeUpdate = reportMetadataRepository.findAll().size();
        reportMetadata.setId(count.incrementAndGet());

        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        restReportMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportMetadata> reportMetadataList = reportMetadataRepository.findAll();
        assertThat(reportMetadataList).hasSize(databaseSizeBeforeUpdate);

        verify(mockReportMetadataSearchRepository, times(0)).save(reportMetadata);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportMetadata() throws Exception {
        int databaseSizeBeforeUpdate = reportMetadataRepository.findAll().size();
        reportMetadata.setId(count.incrementAndGet());

        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        restReportMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportMetadata> reportMetadataList = reportMetadataRepository.findAll();
        assertThat(reportMetadataList).hasSize(databaseSizeBeforeUpdate);

        verify(mockReportMetadataSearchRepository, times(0)).save(reportMetadata);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportMetadata() throws Exception {
        int databaseSizeBeforeUpdate = reportMetadataRepository.findAll().size();
        reportMetadata.setId(count.incrementAndGet());

        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        restReportMetadataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportMetadataDTO)))
            .andExpect(status().isMethodNotAllowed());

        List<ReportMetadata> reportMetadataList = reportMetadataRepository.findAll();
        assertThat(reportMetadataList).hasSize(databaseSizeBeforeUpdate);

        verify(mockReportMetadataSearchRepository, times(0)).save(reportMetadata);
    }

    @Test
    @Transactional
    void partialUpdateReportMetadataWithPatch() throws Exception {
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        int databaseSizeBeforeUpdate = reportMetadataRepository.findAll().size();

        // Update the reportMetadata using partial update
        ReportMetadata partialUpdatedReportMetadata = new ReportMetadata();
        partialUpdatedReportMetadata.setId(reportMetadata.getId());

        partialUpdatedReportMetadata
            .description(UPDATED_DESCRIPTION)
            .module(UPDATED_MODULE)
            .displayLeaseContract(UPDATED_DISPLAY_LEASE_CONTRACT)
            .leaseContractQueryParam(UPDATED_LEASE_CONTRACT_QUERY_PARAM);

        restReportMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportMetadata))
            )
            .andExpect(status().isOk());

        List<ReportMetadata> reportMetadataList = reportMetadataRepository.findAll();
        assertThat(reportMetadataList).hasSize(databaseSizeBeforeUpdate);
        ReportMetadata testReportMetadata = reportMetadataList.get(reportMetadataList.size() - 1);
        assertThat(testReportMetadata.getReportTitle()).isEqualTo(DEFAULT_REPORT_TITLE);
        assertThat(testReportMetadata.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReportMetadata.getModule()).isEqualTo(UPDATED_MODULE);
        assertThat(testReportMetadata.getPagePath()).isEqualTo(DEFAULT_PAGE_PATH);
        assertThat(testReportMetadata.getBackendApi()).isEqualTo(DEFAULT_BACKEND_API);
        assertThat(testReportMetadata.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testReportMetadata.getDisplayLeasePeriod()).isEqualTo(DEFAULT_DISPLAY_LEASE_PERIOD);
        assertThat(testReportMetadata.getDisplayLeaseContract()).isEqualTo(UPDATED_DISPLAY_LEASE_CONTRACT);
        assertThat(testReportMetadata.getLeasePeriodQueryParam()).isEqualTo(DEFAULT_LEASE_PERIOD_QUERY_PARAM);
        assertThat(testReportMetadata.getLeaseContractQueryParam()).isEqualTo(UPDATED_LEASE_CONTRACT_QUERY_PARAM);
    }

    @Test
    @Transactional
    void fullUpdateReportMetadataWithPatch() throws Exception {
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        int databaseSizeBeforeUpdate = reportMetadataRepository.findAll().size();

        ReportMetadata partialUpdatedReportMetadata = new ReportMetadata();
        partialUpdatedReportMetadata.setId(reportMetadata.getId());

        partialUpdatedReportMetadata
            .reportTitle(UPDATED_REPORT_TITLE)
            .description(UPDATED_DESCRIPTION)
            .module(UPDATED_MODULE)
            .pagePath(UPDATED_PAGE_PATH)
            .backendApi(UPDATED_BACKEND_API)
            .active(UPDATED_ACTIVE)
            .displayLeasePeriod(UPDATED_DISPLAY_LEASE_PERIOD)
            .displayLeaseContract(UPDATED_DISPLAY_LEASE_CONTRACT)
            .leasePeriodQueryParam(UPDATED_LEASE_PERIOD_QUERY_PARAM)
            .leaseContractQueryParam(UPDATED_LEASE_CONTRACT_QUERY_PARAM);

        restReportMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportMetadata))
            )
            .andExpect(status().isOk());

        List<ReportMetadata> reportMetadataList = reportMetadataRepository.findAll();
        assertThat(reportMetadataList).hasSize(databaseSizeBeforeUpdate);
        ReportMetadata testReportMetadata = reportMetadataList.get(reportMetadataList.size() - 1);
        assertThat(testReportMetadata.getReportTitle()).isEqualTo(UPDATED_REPORT_TITLE);
        assertThat(testReportMetadata.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReportMetadata.getModule()).isEqualTo(UPDATED_MODULE);
        assertThat(testReportMetadata.getPagePath()).isEqualTo(UPDATED_PAGE_PATH);
        assertThat(testReportMetadata.getBackendApi()).isEqualTo(UPDATED_BACKEND_API);
        assertThat(testReportMetadata.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testReportMetadata.getDisplayLeasePeriod()).isEqualTo(UPDATED_DISPLAY_LEASE_PERIOD);
        assertThat(testReportMetadata.getDisplayLeaseContract()).isEqualTo(UPDATED_DISPLAY_LEASE_CONTRACT);
        assertThat(testReportMetadata.getLeasePeriodQueryParam()).isEqualTo(UPDATED_LEASE_PERIOD_QUERY_PARAM);
        assertThat(testReportMetadata.getLeaseContractQueryParam()).isEqualTo(UPDATED_LEASE_CONTRACT_QUERY_PARAM);
    }

    @Test
    @Transactional
    void patchNonExistingReportMetadata() throws Exception {
        int databaseSizeBeforeUpdate = reportMetadataRepository.findAll().size();
        reportMetadata.setId(count.incrementAndGet());

        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        restReportMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportMetadataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportMetadata> reportMetadataList = reportMetadataRepository.findAll();
        assertThat(reportMetadataList).hasSize(databaseSizeBeforeUpdate);

        verify(mockReportMetadataSearchRepository, times(0)).save(reportMetadata);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportMetadata() throws Exception {
        int databaseSizeBeforeUpdate = reportMetadataRepository.findAll().size();
        reportMetadata.setId(count.incrementAndGet());

        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        restReportMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReportMetadata> reportMetadataList = reportMetadataRepository.findAll();
        assertThat(reportMetadataList).hasSize(databaseSizeBeforeUpdate);

        verify(mockReportMetadataSearchRepository, times(0)).save(reportMetadata);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportMetadata() throws Exception {
        int databaseSizeBeforeUpdate = reportMetadataRepository.findAll().size();
        reportMetadata.setId(count.incrementAndGet());

        ReportMetadataDTO reportMetadataDTO = reportMetadataMapper.toDto(reportMetadata);

        restReportMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportMetadataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        List<ReportMetadata> reportMetadataList = reportMetadataRepository.findAll();
        assertThat(reportMetadataList).hasSize(databaseSizeBeforeUpdate);

        verify(mockReportMetadataSearchRepository, times(0)).save(reportMetadata);
    }

    @Test
    @Transactional
    void deleteReportMetadata() throws Exception {
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        int databaseSizeBeforeDelete = reportMetadataRepository.findAll().size();

        restReportMetadataMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportMetadata.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        List<ReportMetadata> reportMetadataList = reportMetadataRepository.findAll();
        assertThat(reportMetadataList).hasSize(databaseSizeBeforeDelete - 1);

        verify(mockReportMetadataSearchRepository, times(1)).deleteById(reportMetadata.getId());
    }

    @Test
    @Transactional
    void searchReportMetadata() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        reportMetadataRepository.saveAndFlush(reportMetadata);

        when(mockReportMetadataSearchRepository.search("id:" + reportMetadata.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reportMetadata), PageRequest.of(0, 1), 1));

        restReportMetadataMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reportMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportTitle").value(hasItem(DEFAULT_REPORT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].module").value(hasItem(DEFAULT_MODULE)))
            .andExpect(jsonPath("$.[*].pagePath").value(hasItem(DEFAULT_PAGE_PATH)))
            .andExpect(jsonPath("$.[*].backendApi").value(hasItem(DEFAULT_BACKEND_API)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
}
