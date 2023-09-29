package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.ProcessStatus;
import io.github.erp.domain.ReportStatus;
import io.github.erp.repository.ReportStatusRepository;
import io.github.erp.repository.search.ReportStatusSearchRepository;
import io.github.erp.service.ReportStatusService;
import io.github.erp.service.criteria.ReportStatusCriteria;
import io.github.erp.service.dto.ReportStatusDTO;
import io.github.erp.service.mapper.ReportStatusMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
 * Integration tests for the {@link ReportStatusResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReportStatusResourceIT {

    private static final String DEFAULT_REPORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_NAME = "BBBBBBBBBB";

    private static final UUID DEFAULT_REPORT_ID = UUID.randomUUID();
    private static final UUID UPDATED_REPORT_ID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/report-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/report-statuses";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportStatusRepository reportStatusRepository;

    @Mock
    private ReportStatusRepository reportStatusRepositoryMock;

    @Autowired
    private ReportStatusMapper reportStatusMapper;

    @Mock
    private ReportStatusService reportStatusServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ReportStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReportStatusSearchRepository mockReportStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportStatusMockMvc;

    private ReportStatus reportStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportStatus createEntity(EntityManager em) {
        ReportStatus reportStatus = new ReportStatus().reportName(DEFAULT_REPORT_NAME).reportId(DEFAULT_REPORT_ID);
        return reportStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportStatus createUpdatedEntity(EntityManager em) {
        ReportStatus reportStatus = new ReportStatus().reportName(UPDATED_REPORT_NAME).reportId(UPDATED_REPORT_ID);
        return reportStatus;
    }

    @BeforeEach
    public void initTest() {
        reportStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createReportStatus() throws Exception {
        int databaseSizeBeforeCreate = reportStatusRepository.findAll().size();
        // Create the ReportStatus
        ReportStatusDTO reportStatusDTO = reportStatusMapper.toDto(reportStatus);
        restReportStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ReportStatus in the database
        List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        assertThat(reportStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ReportStatus testReportStatus = reportStatusList.get(reportStatusList.size() - 1);
        assertThat(testReportStatus.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testReportStatus.getReportId()).isEqualTo(DEFAULT_REPORT_ID);

        // Validate the ReportStatus in Elasticsearch
        verify(mockReportStatusSearchRepository, times(1)).save(testReportStatus);
    }

    @Test
    @Transactional
    void createReportStatusWithExistingId() throws Exception {
        // Create the ReportStatus with an existing ID
        reportStatus.setId(1L);
        ReportStatusDTO reportStatusDTO = reportStatusMapper.toDto(reportStatus);

        int databaseSizeBeforeCreate = reportStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportStatus in the database
        List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        assertThat(reportStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReportStatus in Elasticsearch
        verify(mockReportStatusSearchRepository, times(0)).save(reportStatus);
    }

    @Test
    @Transactional
    void getAllReportStatuses() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        // Get all the reportStatusList
        restReportStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportStatusesWithEagerRelationshipsIsEnabled() throws Exception {
        when(reportStatusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportStatusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportStatusServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReportStatusesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reportStatusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReportStatusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reportStatusServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getReportStatus() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        // Get the reportStatus
        restReportStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, reportStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportStatus.getId().intValue()))
            .andExpect(jsonPath("$.reportName").value(DEFAULT_REPORT_NAME))
            .andExpect(jsonPath("$.reportId").value(DEFAULT_REPORT_ID.toString()));
    }

    @Test
    @Transactional
    void getReportStatusesByIdFiltering() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        Long id = reportStatus.getId();

        defaultReportStatusShouldBeFound("id.equals=" + id);
        defaultReportStatusShouldNotBeFound("id.notEquals=" + id);

        defaultReportStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReportStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultReportStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReportStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportStatusesByReportNameIsEqualToSomething() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        // Get all the reportStatusList where reportName equals to DEFAULT_REPORT_NAME
        defaultReportStatusShouldBeFound("reportName.equals=" + DEFAULT_REPORT_NAME);

        // Get all the reportStatusList where reportName equals to UPDATED_REPORT_NAME
        defaultReportStatusShouldNotBeFound("reportName.equals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllReportStatusesByReportNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        // Get all the reportStatusList where reportName not equals to DEFAULT_REPORT_NAME
        defaultReportStatusShouldNotBeFound("reportName.notEquals=" + DEFAULT_REPORT_NAME);

        // Get all the reportStatusList where reportName not equals to UPDATED_REPORT_NAME
        defaultReportStatusShouldBeFound("reportName.notEquals=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllReportStatusesByReportNameIsInShouldWork() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        // Get all the reportStatusList where reportName in DEFAULT_REPORT_NAME or UPDATED_REPORT_NAME
        defaultReportStatusShouldBeFound("reportName.in=" + DEFAULT_REPORT_NAME + "," + UPDATED_REPORT_NAME);

        // Get all the reportStatusList where reportName equals to UPDATED_REPORT_NAME
        defaultReportStatusShouldNotBeFound("reportName.in=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllReportStatusesByReportNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        // Get all the reportStatusList where reportName is not null
        defaultReportStatusShouldBeFound("reportName.specified=true");

        // Get all the reportStatusList where reportName is null
        defaultReportStatusShouldNotBeFound("reportName.specified=false");
    }

    @Test
    @Transactional
    void getAllReportStatusesByReportNameContainsSomething() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        // Get all the reportStatusList where reportName contains DEFAULT_REPORT_NAME
        defaultReportStatusShouldBeFound("reportName.contains=" + DEFAULT_REPORT_NAME);

        // Get all the reportStatusList where reportName contains UPDATED_REPORT_NAME
        defaultReportStatusShouldNotBeFound("reportName.contains=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllReportStatusesByReportNameNotContainsSomething() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        // Get all the reportStatusList where reportName does not contain DEFAULT_REPORT_NAME
        defaultReportStatusShouldNotBeFound("reportName.doesNotContain=" + DEFAULT_REPORT_NAME);

        // Get all the reportStatusList where reportName does not contain UPDATED_REPORT_NAME
        defaultReportStatusShouldBeFound("reportName.doesNotContain=" + UPDATED_REPORT_NAME);
    }

    @Test
    @Transactional
    void getAllReportStatusesByReportIdIsEqualToSomething() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        // Get all the reportStatusList where reportId equals to DEFAULT_REPORT_ID
        defaultReportStatusShouldBeFound("reportId.equals=" + DEFAULT_REPORT_ID);

        // Get all the reportStatusList where reportId equals to UPDATED_REPORT_ID
        defaultReportStatusShouldNotBeFound("reportId.equals=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllReportStatusesByReportIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        // Get all the reportStatusList where reportId not equals to DEFAULT_REPORT_ID
        defaultReportStatusShouldNotBeFound("reportId.notEquals=" + DEFAULT_REPORT_ID);

        // Get all the reportStatusList where reportId not equals to UPDATED_REPORT_ID
        defaultReportStatusShouldBeFound("reportId.notEquals=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllReportStatusesByReportIdIsInShouldWork() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        // Get all the reportStatusList where reportId in DEFAULT_REPORT_ID or UPDATED_REPORT_ID
        defaultReportStatusShouldBeFound("reportId.in=" + DEFAULT_REPORT_ID + "," + UPDATED_REPORT_ID);

        // Get all the reportStatusList where reportId equals to UPDATED_REPORT_ID
        defaultReportStatusShouldNotBeFound("reportId.in=" + UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void getAllReportStatusesByReportIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        // Get all the reportStatusList where reportId is not null
        defaultReportStatusShouldBeFound("reportId.specified=true");

        // Get all the reportStatusList where reportId is null
        defaultReportStatusShouldNotBeFound("reportId.specified=false");
    }

    @Test
    @Transactional
    void getAllReportStatusesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        reportStatus.addPlaceholder(placeholder);
        reportStatusRepository.saveAndFlush(reportStatus);
        Long placeholderId = placeholder.getId();

        // Get all the reportStatusList where placeholder equals to placeholderId
        defaultReportStatusShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the reportStatusList where placeholder equals to (placeholderId + 1)
        defaultReportStatusShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllReportStatusesByProcessStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);
        ProcessStatus processStatus;
        if (TestUtil.findAll(em, ProcessStatus.class).isEmpty()) {
            processStatus = ProcessStatusResourceIT.createEntity(em);
            em.persist(processStatus);
            em.flush();
        } else {
            processStatus = TestUtil.findAll(em, ProcessStatus.class).get(0);
        }
        em.persist(processStatus);
        em.flush();
        reportStatus.setProcessStatus(processStatus);
        reportStatusRepository.saveAndFlush(reportStatus);
        Long processStatusId = processStatus.getId();

        // Get all the reportStatusList where processStatus equals to processStatusId
        defaultReportStatusShouldBeFound("processStatusId.equals=" + processStatusId);

        // Get all the reportStatusList where processStatus equals to (processStatusId + 1)
        defaultReportStatusShouldNotBeFound("processStatusId.equals=" + (processStatusId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportStatusShouldBeFound(String filter) throws Exception {
        restReportStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));

        // Check, that the count call also returns 1
        restReportStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportStatusShouldNotBeFound(String filter) throws Exception {
        restReportStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportStatus() throws Exception {
        // Get the reportStatus
        restReportStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReportStatus() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        int databaseSizeBeforeUpdate = reportStatusRepository.findAll().size();

        // Update the reportStatus
        ReportStatus updatedReportStatus = reportStatusRepository.findById(reportStatus.getId()).get();
        // Disconnect from session so that the updates on updatedReportStatus are not directly saved in db
        em.detach(updatedReportStatus);
        updatedReportStatus.reportName(UPDATED_REPORT_NAME).reportId(UPDATED_REPORT_ID);
        ReportStatusDTO reportStatusDTO = reportStatusMapper.toDto(updatedReportStatus);

        restReportStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportStatus in the database
        List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        assertThat(reportStatusList).hasSize(databaseSizeBeforeUpdate);
        ReportStatus testReportStatus = reportStatusList.get(reportStatusList.size() - 1);
        assertThat(testReportStatus.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testReportStatus.getReportId()).isEqualTo(UPDATED_REPORT_ID);

        // Validate the ReportStatus in Elasticsearch
        verify(mockReportStatusSearchRepository).save(testReportStatus);
    }

    @Test
    @Transactional
    void putNonExistingReportStatus() throws Exception {
        int databaseSizeBeforeUpdate = reportStatusRepository.findAll().size();
        reportStatus.setId(count.incrementAndGet());

        // Create the ReportStatus
        ReportStatusDTO reportStatusDTO = reportStatusMapper.toDto(reportStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportStatus in the database
        List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        assertThat(reportStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportStatus in Elasticsearch
        verify(mockReportStatusSearchRepository, times(0)).save(reportStatus);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportStatus() throws Exception {
        int databaseSizeBeforeUpdate = reportStatusRepository.findAll().size();
        reportStatus.setId(count.incrementAndGet());

        // Create the ReportStatus
        ReportStatusDTO reportStatusDTO = reportStatusMapper.toDto(reportStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportStatus in the database
        List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        assertThat(reportStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportStatus in Elasticsearch
        verify(mockReportStatusSearchRepository, times(0)).save(reportStatus);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportStatus() throws Exception {
        int databaseSizeBeforeUpdate = reportStatusRepository.findAll().size();
        reportStatus.setId(count.incrementAndGet());

        // Create the ReportStatus
        ReportStatusDTO reportStatusDTO = reportStatusMapper.toDto(reportStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportStatus in the database
        List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        assertThat(reportStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportStatus in Elasticsearch
        verify(mockReportStatusSearchRepository, times(0)).save(reportStatus);
    }

    @Test
    @Transactional
    void partialUpdateReportStatusWithPatch() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        int databaseSizeBeforeUpdate = reportStatusRepository.findAll().size();

        // Update the reportStatus using partial update
        ReportStatus partialUpdatedReportStatus = new ReportStatus();
        partialUpdatedReportStatus.setId(reportStatus.getId());

        restReportStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportStatus))
            )
            .andExpect(status().isOk());

        // Validate the ReportStatus in the database
        List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        assertThat(reportStatusList).hasSize(databaseSizeBeforeUpdate);
        ReportStatus testReportStatus = reportStatusList.get(reportStatusList.size() - 1);
        assertThat(testReportStatus.getReportName()).isEqualTo(DEFAULT_REPORT_NAME);
        assertThat(testReportStatus.getReportId()).isEqualTo(DEFAULT_REPORT_ID);
    }

    @Test
    @Transactional
    void fullUpdateReportStatusWithPatch() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        int databaseSizeBeforeUpdate = reportStatusRepository.findAll().size();

        // Update the reportStatus using partial update
        ReportStatus partialUpdatedReportStatus = new ReportStatus();
        partialUpdatedReportStatus.setId(reportStatus.getId());

        partialUpdatedReportStatus.reportName(UPDATED_REPORT_NAME).reportId(UPDATED_REPORT_ID);

        restReportStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReportStatus))
            )
            .andExpect(status().isOk());

        // Validate the ReportStatus in the database
        List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        assertThat(reportStatusList).hasSize(databaseSizeBeforeUpdate);
        ReportStatus testReportStatus = reportStatusList.get(reportStatusList.size() - 1);
        assertThat(testReportStatus.getReportName()).isEqualTo(UPDATED_REPORT_NAME);
        assertThat(testReportStatus.getReportId()).isEqualTo(UPDATED_REPORT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingReportStatus() throws Exception {
        int databaseSizeBeforeUpdate = reportStatusRepository.findAll().size();
        reportStatus.setId(count.incrementAndGet());

        // Create the ReportStatus
        ReportStatusDTO reportStatusDTO = reportStatusMapper.toDto(reportStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportStatus in the database
        List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        assertThat(reportStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportStatus in Elasticsearch
        verify(mockReportStatusSearchRepository, times(0)).save(reportStatus);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportStatus() throws Exception {
        int databaseSizeBeforeUpdate = reportStatusRepository.findAll().size();
        reportStatus.setId(count.incrementAndGet());

        // Create the ReportStatus
        ReportStatusDTO reportStatusDTO = reportStatusMapper.toDto(reportStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportStatus in the database
        List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        assertThat(reportStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportStatus in Elasticsearch
        verify(mockReportStatusSearchRepository, times(0)).save(reportStatus);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportStatus() throws Exception {
        int databaseSizeBeforeUpdate = reportStatusRepository.findAll().size();
        reportStatus.setId(count.incrementAndGet());

        // Create the ReportStatus
        ReportStatusDTO reportStatusDTO = reportStatusMapper.toDto(reportStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportStatus in the database
        List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        assertThat(reportStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReportStatus in Elasticsearch
        verify(mockReportStatusSearchRepository, times(0)).save(reportStatus);
    }

    @Test
    @Transactional
    void deleteReportStatus() throws Exception {
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);

        int databaseSizeBeforeDelete = reportStatusRepository.findAll().size();

        // Delete the reportStatus
        restReportStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReportStatus> reportStatusList = reportStatusRepository.findAll();
        assertThat(reportStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReportStatus in Elasticsearch
        verify(mockReportStatusSearchRepository, times(1)).deleteById(reportStatus.getId());
    }

    @Test
    @Transactional
    void searchReportStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        reportStatusRepository.saveAndFlush(reportStatus);
        when(mockReportStatusSearchRepository.search("id:" + reportStatus.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reportStatus), PageRequest.of(0, 1), 1));

        // Search the reportStatus
        restReportStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reportStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportName").value(hasItem(DEFAULT_REPORT_NAME)))
            .andExpect(jsonPath("$.[*].reportId").value(hasItem(DEFAULT_REPORT_ID.toString())));
    }
}
