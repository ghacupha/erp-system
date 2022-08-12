package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark II No 26 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.ProcessStatus;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.ProcessStatusRepository;
import io.github.erp.repository.search.ProcessStatusSearchRepository;
import io.github.erp.service.ProcessStatusService;
import io.github.erp.service.dto.ProcessStatusDTO;
import io.github.erp.service.mapper.ProcessStatusMapper;
//import io.github.erp.web.rest.ProcessStatusResource;
import io.github.erp.web.rest.TestUtil;
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

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProcessStatusResourceDev} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
class ProcessStatusResourceIT {

    private static final String DEFAULT_STATUS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dev/process-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/process-statuses";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessStatusRepository processStatusRepository;

    @Mock
    private ProcessStatusRepository processStatusRepositoryMock;

    @Autowired
    private ProcessStatusMapper processStatusMapper;

    @Mock
    private ProcessStatusService processStatusServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ProcessStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProcessStatusSearchRepository mockProcessStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessStatusMockMvc;

    private ProcessStatus processStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessStatus createEntity(EntityManager em) {
        ProcessStatus processStatus = new ProcessStatus().statusCode(DEFAULT_STATUS_CODE).description(DEFAULT_DESCRIPTION);
        return processStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessStatus createUpdatedEntity(EntityManager em) {
        ProcessStatus processStatus = new ProcessStatus().statusCode(UPDATED_STATUS_CODE).description(UPDATED_DESCRIPTION);
        return processStatus;
    }

    @BeforeEach
    public void initTest() {
        processStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createProcessStatus() throws Exception {
        int databaseSizeBeforeCreate = processStatusRepository.findAll().size();
        // Create the ProcessStatus
        ProcessStatusDTO processStatusDTO = processStatusMapper.toDto(processStatus);
        restProcessStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProcessStatus in the database
        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessStatus testProcessStatus = processStatusList.get(processStatusList.size() - 1);
        assertThat(testProcessStatus.getStatusCode()).isEqualTo(DEFAULT_STATUS_CODE);
        assertThat(testProcessStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the ProcessStatus in Elasticsearch
        verify(mockProcessStatusSearchRepository, times(1)).save(testProcessStatus);
    }

    @Test
    @Transactional
    void createProcessStatusWithExistingId() throws Exception {
        // Create the ProcessStatus with an existing ID
        processStatus.setId(1L);
        ProcessStatusDTO processStatusDTO = processStatusMapper.toDto(processStatus);

        int databaseSizeBeforeCreate = processStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessStatus in the database
        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the ProcessStatus in Elasticsearch
        verify(mockProcessStatusSearchRepository, times(0)).save(processStatus);
    }

    @Test
    @Transactional
    void checkStatusCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = processStatusRepository.findAll().size();
        // set the field null
        processStatus.setStatusCode(null);

        // Create the ProcessStatus, which fails.
        ProcessStatusDTO processStatusDTO = processStatusMapper.toDto(processStatus);

        restProcessStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = processStatusRepository.findAll().size();
        // set the field null
        processStatus.setDescription(null);

        // Create the ProcessStatus, which fails.
        ProcessStatusDTO processStatusDTO = processStatusMapper.toDto(processStatus);

        restProcessStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProcessStatuses() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList
        restProcessStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProcessStatusesWithEagerRelationshipsIsEnabled() throws Exception {
        when(processStatusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProcessStatusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(processStatusServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProcessStatusesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(processStatusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProcessStatusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(processStatusServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProcessStatus() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get the processStatus
        restProcessStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, processStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(processStatus.getId().intValue()))
            .andExpect(jsonPath("$.statusCode").value(DEFAULT_STATUS_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getProcessStatusesByIdFiltering() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        Long id = processStatus.getId();

        defaultProcessStatusShouldBeFound("id.equals=" + id);
        defaultProcessStatusShouldNotBeFound("id.notEquals=" + id);

        defaultProcessStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProcessStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultProcessStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProcessStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProcessStatusesByStatusCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList where statusCode equals to DEFAULT_STATUS_CODE
        defaultProcessStatusShouldBeFound("statusCode.equals=" + DEFAULT_STATUS_CODE);

        // Get all the processStatusList where statusCode equals to UPDATED_STATUS_CODE
        defaultProcessStatusShouldNotBeFound("statusCode.equals=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllProcessStatusesByStatusCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList where statusCode not equals to DEFAULT_STATUS_CODE
        defaultProcessStatusShouldNotBeFound("statusCode.notEquals=" + DEFAULT_STATUS_CODE);

        // Get all the processStatusList where statusCode not equals to UPDATED_STATUS_CODE
        defaultProcessStatusShouldBeFound("statusCode.notEquals=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllProcessStatusesByStatusCodeIsInShouldWork() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList where statusCode in DEFAULT_STATUS_CODE or UPDATED_STATUS_CODE
        defaultProcessStatusShouldBeFound("statusCode.in=" + DEFAULT_STATUS_CODE + "," + UPDATED_STATUS_CODE);

        // Get all the processStatusList where statusCode equals to UPDATED_STATUS_CODE
        defaultProcessStatusShouldNotBeFound("statusCode.in=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllProcessStatusesByStatusCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList where statusCode is not null
        defaultProcessStatusShouldBeFound("statusCode.specified=true");

        // Get all the processStatusList where statusCode is null
        defaultProcessStatusShouldNotBeFound("statusCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessStatusesByStatusCodeContainsSomething() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList where statusCode contains DEFAULT_STATUS_CODE
        defaultProcessStatusShouldBeFound("statusCode.contains=" + DEFAULT_STATUS_CODE);

        // Get all the processStatusList where statusCode contains UPDATED_STATUS_CODE
        defaultProcessStatusShouldNotBeFound("statusCode.contains=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllProcessStatusesByStatusCodeNotContainsSomething() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList where statusCode does not contain DEFAULT_STATUS_CODE
        defaultProcessStatusShouldNotBeFound("statusCode.doesNotContain=" + DEFAULT_STATUS_CODE);

        // Get all the processStatusList where statusCode does not contain UPDATED_STATUS_CODE
        defaultProcessStatusShouldBeFound("statusCode.doesNotContain=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    void getAllProcessStatusesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList where description equals to DEFAULT_DESCRIPTION
        defaultProcessStatusShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the processStatusList where description equals to UPDATED_DESCRIPTION
        defaultProcessStatusShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProcessStatusesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList where description not equals to DEFAULT_DESCRIPTION
        defaultProcessStatusShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the processStatusList where description not equals to UPDATED_DESCRIPTION
        defaultProcessStatusShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProcessStatusesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProcessStatusShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the processStatusList where description equals to UPDATED_DESCRIPTION
        defaultProcessStatusShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProcessStatusesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList where description is not null
        defaultProcessStatusShouldBeFound("description.specified=true");

        // Get all the processStatusList where description is null
        defaultProcessStatusShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessStatusesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList where description contains DEFAULT_DESCRIPTION
        defaultProcessStatusShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the processStatusList where description contains UPDATED_DESCRIPTION
        defaultProcessStatusShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProcessStatusesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        // Get all the processStatusList where description does not contain DEFAULT_DESCRIPTION
        defaultProcessStatusShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the processStatusList where description does not contain UPDATED_DESCRIPTION
        defaultProcessStatusShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProcessStatusesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);
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
        processStatus.addPlaceholder(placeholder);
        processStatusRepository.saveAndFlush(processStatus);
        Long placeholderId = placeholder.getId();

        // Get all the processStatusList where placeholder equals to placeholderId
        defaultProcessStatusShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the processStatusList where placeholder equals to (placeholderId + 1)
        defaultProcessStatusShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllProcessStatusesByParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);
        UniversallyUniqueMapping parameters;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            parameters = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(parameters);
            em.flush();
        } else {
            parameters = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(parameters);
        em.flush();
        processStatus.addParameters(parameters);
        processStatusRepository.saveAndFlush(processStatus);
        Long parametersId = parameters.getId();

        // Get all the processStatusList where parameters equals to parametersId
        defaultProcessStatusShouldBeFound("parametersId.equals=" + parametersId);

        // Get all the processStatusList where parameters equals to (parametersId + 1)
        defaultProcessStatusShouldNotBeFound("parametersId.equals=" + (parametersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProcessStatusShouldBeFound(String filter) throws Exception {
        restProcessStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restProcessStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProcessStatusShouldNotBeFound(String filter) throws Exception {
        restProcessStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProcessStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProcessStatus() throws Exception {
        // Get the processStatus
        restProcessStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProcessStatus() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        int databaseSizeBeforeUpdate = processStatusRepository.findAll().size();

        // Update the processStatus
        ProcessStatus updatedProcessStatus = processStatusRepository.findById(processStatus.getId()).get();
        // Disconnect from session so that the updates on updatedProcessStatus are not directly saved in db
        em.detach(updatedProcessStatus);
        updatedProcessStatus.statusCode(UPDATED_STATUS_CODE).description(UPDATED_DESCRIPTION);
        ProcessStatusDTO processStatusDTO = processStatusMapper.toDto(updatedProcessStatus);

        restProcessStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProcessStatus in the database
        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeUpdate);
        ProcessStatus testProcessStatus = processStatusList.get(processStatusList.size() - 1);
        assertThat(testProcessStatus.getStatusCode()).isEqualTo(UPDATED_STATUS_CODE);
        assertThat(testProcessStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the ProcessStatus in Elasticsearch
        verify(mockProcessStatusSearchRepository).save(testProcessStatus);
    }

    @Test
    @Transactional
    void putNonExistingProcessStatus() throws Exception {
        int databaseSizeBeforeUpdate = processStatusRepository.findAll().size();
        processStatus.setId(count.incrementAndGet());

        // Create the ProcessStatus
        ProcessStatusDTO processStatusDTO = processStatusMapper.toDto(processStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessStatus in the database
        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProcessStatus in Elasticsearch
        verify(mockProcessStatusSearchRepository, times(0)).save(processStatus);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcessStatus() throws Exception {
        int databaseSizeBeforeUpdate = processStatusRepository.findAll().size();
        processStatus.setId(count.incrementAndGet());

        // Create the ProcessStatus
        ProcessStatusDTO processStatusDTO = processStatusMapper.toDto(processStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessStatus in the database
        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProcessStatus in Elasticsearch
        verify(mockProcessStatusSearchRepository, times(0)).save(processStatus);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcessStatus() throws Exception {
        int databaseSizeBeforeUpdate = processStatusRepository.findAll().size();
        processStatus.setId(count.incrementAndGet());

        // Create the ProcessStatus
        ProcessStatusDTO processStatusDTO = processStatusMapper.toDto(processStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(processStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessStatus in the database
        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProcessStatus in Elasticsearch
        verify(mockProcessStatusSearchRepository, times(0)).save(processStatus);
    }

    @Test
    @Transactional
    void partialUpdateProcessStatusWithPatch() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        int databaseSizeBeforeUpdate = processStatusRepository.findAll().size();

        // Update the processStatus using partial update
        ProcessStatus partialUpdatedProcessStatus = new ProcessStatus();
        partialUpdatedProcessStatus.setId(processStatus.getId());

        restProcessStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessStatus))
            )
            .andExpect(status().isOk());

        // Validate the ProcessStatus in the database
        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeUpdate);
        ProcessStatus testProcessStatus = processStatusList.get(processStatusList.size() - 1);
        assertThat(testProcessStatus.getStatusCode()).isEqualTo(DEFAULT_STATUS_CODE);
        assertThat(testProcessStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateProcessStatusWithPatch() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        int databaseSizeBeforeUpdate = processStatusRepository.findAll().size();

        // Update the processStatus using partial update
        ProcessStatus partialUpdatedProcessStatus = new ProcessStatus();
        partialUpdatedProcessStatus.setId(processStatus.getId());

        partialUpdatedProcessStatus.statusCode(UPDATED_STATUS_CODE).description(UPDATED_DESCRIPTION);

        restProcessStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcessStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcessStatus))
            )
            .andExpect(status().isOk());

        // Validate the ProcessStatus in the database
        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeUpdate);
        ProcessStatus testProcessStatus = processStatusList.get(processStatusList.size() - 1);
        assertThat(testProcessStatus.getStatusCode()).isEqualTo(UPDATED_STATUS_CODE);
        assertThat(testProcessStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingProcessStatus() throws Exception {
        int databaseSizeBeforeUpdate = processStatusRepository.findAll().size();
        processStatus.setId(count.incrementAndGet());

        // Create the ProcessStatus
        ProcessStatusDTO processStatusDTO = processStatusMapper.toDto(processStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessStatus in the database
        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProcessStatus in Elasticsearch
        verify(mockProcessStatusSearchRepository, times(0)).save(processStatus);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcessStatus() throws Exception {
        int databaseSizeBeforeUpdate = processStatusRepository.findAll().size();
        processStatus.setId(count.incrementAndGet());

        // Create the ProcessStatus
        ProcessStatusDTO processStatusDTO = processStatusMapper.toDto(processStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProcessStatus in the database
        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProcessStatus in Elasticsearch
        verify(mockProcessStatusSearchRepository, times(0)).save(processStatus);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcessStatus() throws Exception {
        int databaseSizeBeforeUpdate = processStatusRepository.findAll().size();
        processStatus.setId(count.incrementAndGet());

        // Create the ProcessStatus
        ProcessStatusDTO processStatusDTO = processStatusMapper.toDto(processStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProcessStatus in the database
        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ProcessStatus in Elasticsearch
        verify(mockProcessStatusSearchRepository, times(0)).save(processStatus);
    }

    @Test
    @Transactional
    void deleteProcessStatus() throws Exception {
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);

        int databaseSizeBeforeDelete = processStatusRepository.findAll().size();

        // Delete the processStatus
        restProcessStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, processStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessStatus> processStatusList = processStatusRepository.findAll();
        assertThat(processStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ProcessStatus in Elasticsearch
        verify(mockProcessStatusSearchRepository, times(1)).deleteById(processStatus.getId());
    }

    @Test
    @Transactional
    void searchProcessStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        processStatusRepository.saveAndFlush(processStatus);
        when(mockProcessStatusSearchRepository.search("id:" + processStatus.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(processStatus), PageRequest.of(0, 1), 1));

        // Search the processStatus
        restProcessStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + processStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
