package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.0.8-SNAPSHOT
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
import io.github.erp.domain.OutletStatus;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.enumeration.BranchStatusType;
import io.github.erp.repository.OutletStatusRepository;
import io.github.erp.repository.search.OutletStatusSearchRepository;
import io.github.erp.service.OutletStatusService;
import io.github.erp.service.dto.OutletStatusDTO;
import io.github.erp.service.mapper.OutletStatusMapper;
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
 * Integration tests for the OutletStatusResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER", "FIXED_ASSETS_USER"})
class OutletStatusResourceIT {

    private static final String DEFAULT_BRANCH_STATUS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_STATUS_TYPE_CODE = "BBBBBBBBBB";

    private static final BranchStatusType DEFAULT_BRANCH_STATUS_TYPE = BranchStatusType.ACTIVE;
    private static final BranchStatusType UPDATED_BRANCH_STATUS_TYPE = BranchStatusType.INACTIVE;

    private static final String DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/outlet-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/outlet-statuses";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OutletStatusRepository outletStatusRepository;

    @Mock
    private OutletStatusRepository outletStatusRepositoryMock;

    @Autowired
    private OutletStatusMapper outletStatusMapper;

    @Mock
    private OutletStatusService outletStatusServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.OutletStatusSearchRepositoryMockConfiguration
     */
    @Autowired
    private OutletStatusSearchRepository mockOutletStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOutletStatusMockMvc;

    private OutletStatus outletStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutletStatus createEntity(EntityManager em) {
        OutletStatus outletStatus = new OutletStatus()
            .branchStatusTypeCode(DEFAULT_BRANCH_STATUS_TYPE_CODE)
            .branchStatusType(DEFAULT_BRANCH_STATUS_TYPE)
            .branchStatusTypeDescription(DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION);
        return outletStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutletStatus createUpdatedEntity(EntityManager em) {
        OutletStatus outletStatus = new OutletStatus()
            .branchStatusTypeCode(UPDATED_BRANCH_STATUS_TYPE_CODE)
            .branchStatusType(UPDATED_BRANCH_STATUS_TYPE)
            .branchStatusTypeDescription(UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION);
        return outletStatus;
    }

    @BeforeEach
    public void initTest() {
        outletStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createOutletStatus() throws Exception {
        int databaseSizeBeforeCreate = outletStatusRepository.findAll().size();
        // Create the OutletStatus
        OutletStatusDTO outletStatusDTO = outletStatusMapper.toDto(outletStatus);
        restOutletStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outletStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OutletStatus in the database
        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeCreate + 1);
        OutletStatus testOutletStatus = outletStatusList.get(outletStatusList.size() - 1);
        assertThat(testOutletStatus.getBranchStatusTypeCode()).isEqualTo(DEFAULT_BRANCH_STATUS_TYPE_CODE);
        assertThat(testOutletStatus.getBranchStatusType()).isEqualTo(DEFAULT_BRANCH_STATUS_TYPE);
        assertThat(testOutletStatus.getBranchStatusTypeDescription()).isEqualTo(DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION);

        // Validate the OutletStatus in Elasticsearch
        verify(mockOutletStatusSearchRepository, times(1)).save(testOutletStatus);
    }

    @Test
    @Transactional
    void createOutletStatusWithExistingId() throws Exception {
        // Create the OutletStatus with an existing ID
        outletStatus.setId(1L);
        OutletStatusDTO outletStatusDTO = outletStatusMapper.toDto(outletStatus);

        int databaseSizeBeforeCreate = outletStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutletStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outletStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutletStatus in the database
        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeCreate);

        // Validate the OutletStatus in Elasticsearch
        verify(mockOutletStatusSearchRepository, times(0)).save(outletStatus);
    }

    @Test
    @Transactional
    void checkBranchStatusTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = outletStatusRepository.findAll().size();
        // set the field null
        outletStatus.setBranchStatusTypeCode(null);

        // Create the OutletStatus, which fails.
        OutletStatusDTO outletStatusDTO = outletStatusMapper.toDto(outletStatus);

        restOutletStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outletStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBranchStatusTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = outletStatusRepository.findAll().size();
        // set the field null
        outletStatus.setBranchStatusType(null);

        // Create the OutletStatus, which fails.
        OutletStatusDTO outletStatusDTO = outletStatusMapper.toDto(outletStatus);

        restOutletStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outletStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOutletStatuses() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList
        restOutletStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outletStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchStatusTypeCode").value(hasItem(DEFAULT_BRANCH_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].branchStatusType").value(hasItem(DEFAULT_BRANCH_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].branchStatusTypeDescription").value(hasItem(DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOutletStatusesWithEagerRelationshipsIsEnabled() throws Exception {
        when(outletStatusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOutletStatusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(outletStatusServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOutletStatusesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(outletStatusServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOutletStatusMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(outletStatusServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getOutletStatus() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get the outletStatus
        restOutletStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, outletStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(outletStatus.getId().intValue()))
            .andExpect(jsonPath("$.branchStatusTypeCode").value(DEFAULT_BRANCH_STATUS_TYPE_CODE))
            .andExpect(jsonPath("$.branchStatusType").value(DEFAULT_BRANCH_STATUS_TYPE.toString()))
            .andExpect(jsonPath("$.branchStatusTypeDescription").value(DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION));
    }

    @Test
    @Transactional
    void getOutletStatusesByIdFiltering() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        Long id = outletStatus.getId();

        defaultOutletStatusShouldBeFound("id.equals=" + id);
        defaultOutletStatusShouldNotBeFound("id.notEquals=" + id);

        defaultOutletStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOutletStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultOutletStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOutletStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusTypeCode equals to DEFAULT_BRANCH_STATUS_TYPE_CODE
        defaultOutletStatusShouldBeFound("branchStatusTypeCode.equals=" + DEFAULT_BRANCH_STATUS_TYPE_CODE);

        // Get all the outletStatusList where branchStatusTypeCode equals to UPDATED_BRANCH_STATUS_TYPE_CODE
        defaultOutletStatusShouldNotBeFound("branchStatusTypeCode.equals=" + UPDATED_BRANCH_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusTypeCode not equals to DEFAULT_BRANCH_STATUS_TYPE_CODE
        defaultOutletStatusShouldNotBeFound("branchStatusTypeCode.notEquals=" + DEFAULT_BRANCH_STATUS_TYPE_CODE);

        // Get all the outletStatusList where branchStatusTypeCode not equals to UPDATED_BRANCH_STATUS_TYPE_CODE
        defaultOutletStatusShouldBeFound("branchStatusTypeCode.notEquals=" + UPDATED_BRANCH_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusTypeCode in DEFAULT_BRANCH_STATUS_TYPE_CODE or UPDATED_BRANCH_STATUS_TYPE_CODE
        defaultOutletStatusShouldBeFound(
            "branchStatusTypeCode.in=" + DEFAULT_BRANCH_STATUS_TYPE_CODE + "," + UPDATED_BRANCH_STATUS_TYPE_CODE
        );

        // Get all the outletStatusList where branchStatusTypeCode equals to UPDATED_BRANCH_STATUS_TYPE_CODE
        defaultOutletStatusShouldNotBeFound("branchStatusTypeCode.in=" + UPDATED_BRANCH_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusTypeCode is not null
        defaultOutletStatusShouldBeFound("branchStatusTypeCode.specified=true");

        // Get all the outletStatusList where branchStatusTypeCode is null
        defaultOutletStatusShouldNotBeFound("branchStatusTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusTypeCode contains DEFAULT_BRANCH_STATUS_TYPE_CODE
        defaultOutletStatusShouldBeFound("branchStatusTypeCode.contains=" + DEFAULT_BRANCH_STATUS_TYPE_CODE);

        // Get all the outletStatusList where branchStatusTypeCode contains UPDATED_BRANCH_STATUS_TYPE_CODE
        defaultOutletStatusShouldNotBeFound("branchStatusTypeCode.contains=" + UPDATED_BRANCH_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusTypeCode does not contain DEFAULT_BRANCH_STATUS_TYPE_CODE
        defaultOutletStatusShouldNotBeFound("branchStatusTypeCode.doesNotContain=" + DEFAULT_BRANCH_STATUS_TYPE_CODE);

        // Get all the outletStatusList where branchStatusTypeCode does not contain UPDATED_BRANCH_STATUS_TYPE_CODE
        defaultOutletStatusShouldBeFound("branchStatusTypeCode.doesNotContain=" + UPDATED_BRANCH_STATUS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusType equals to DEFAULT_BRANCH_STATUS_TYPE
        defaultOutletStatusShouldBeFound("branchStatusType.equals=" + DEFAULT_BRANCH_STATUS_TYPE);

        // Get all the outletStatusList where branchStatusType equals to UPDATED_BRANCH_STATUS_TYPE
        defaultOutletStatusShouldNotBeFound("branchStatusType.equals=" + UPDATED_BRANCH_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusType not equals to DEFAULT_BRANCH_STATUS_TYPE
        defaultOutletStatusShouldNotBeFound("branchStatusType.notEquals=" + DEFAULT_BRANCH_STATUS_TYPE);

        // Get all the outletStatusList where branchStatusType not equals to UPDATED_BRANCH_STATUS_TYPE
        defaultOutletStatusShouldBeFound("branchStatusType.notEquals=" + UPDATED_BRANCH_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeIsInShouldWork() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusType in DEFAULT_BRANCH_STATUS_TYPE or UPDATED_BRANCH_STATUS_TYPE
        defaultOutletStatusShouldBeFound("branchStatusType.in=" + DEFAULT_BRANCH_STATUS_TYPE + "," + UPDATED_BRANCH_STATUS_TYPE);

        // Get all the outletStatusList where branchStatusType equals to UPDATED_BRANCH_STATUS_TYPE
        defaultOutletStatusShouldNotBeFound("branchStatusType.in=" + UPDATED_BRANCH_STATUS_TYPE);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusType is not null
        defaultOutletStatusShouldBeFound("branchStatusType.specified=true");

        // Get all the outletStatusList where branchStatusType is null
        defaultOutletStatusShouldNotBeFound("branchStatusType.specified=false");
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusTypeDescription equals to DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION
        defaultOutletStatusShouldBeFound("branchStatusTypeDescription.equals=" + DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION);

        // Get all the outletStatusList where branchStatusTypeDescription equals to UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION
        defaultOutletStatusShouldNotBeFound("branchStatusTypeDescription.equals=" + UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusTypeDescription not equals to DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION
        defaultOutletStatusShouldNotBeFound("branchStatusTypeDescription.notEquals=" + DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION);

        // Get all the outletStatusList where branchStatusTypeDescription not equals to UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION
        defaultOutletStatusShouldBeFound("branchStatusTypeDescription.notEquals=" + UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusTypeDescription in DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION or UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION
        defaultOutletStatusShouldBeFound(
            "branchStatusTypeDescription.in=" + DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION + "," + UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION
        );

        // Get all the outletStatusList where branchStatusTypeDescription equals to UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION
        defaultOutletStatusShouldNotBeFound("branchStatusTypeDescription.in=" + UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusTypeDescription is not null
        defaultOutletStatusShouldBeFound("branchStatusTypeDescription.specified=true");

        // Get all the outletStatusList where branchStatusTypeDescription is null
        defaultOutletStatusShouldNotBeFound("branchStatusTypeDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusTypeDescription contains DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION
        defaultOutletStatusShouldBeFound("branchStatusTypeDescription.contains=" + DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION);

        // Get all the outletStatusList where branchStatusTypeDescription contains UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION
        defaultOutletStatusShouldNotBeFound("branchStatusTypeDescription.contains=" + UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByBranchStatusTypeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        // Get all the outletStatusList where branchStatusTypeDescription does not contain DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION
        defaultOutletStatusShouldNotBeFound("branchStatusTypeDescription.doesNotContain=" + DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION);

        // Get all the outletStatusList where branchStatusTypeDescription does not contain UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION
        defaultOutletStatusShouldBeFound("branchStatusTypeDescription.doesNotContain=" + UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOutletStatusesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);
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
        outletStatus.addPlaceholder(placeholder);
        outletStatusRepository.saveAndFlush(outletStatus);
        Long placeholderId = placeholder.getId();

        // Get all the outletStatusList where placeholder equals to placeholderId
        defaultOutletStatusShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the outletStatusList where placeholder equals to (placeholderId + 1)
        defaultOutletStatusShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOutletStatusShouldBeFound(String filter) throws Exception {
        restOutletStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outletStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchStatusTypeCode").value(hasItem(DEFAULT_BRANCH_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].branchStatusType").value(hasItem(DEFAULT_BRANCH_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].branchStatusTypeDescription").value(hasItem(DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION)));

        // Check, that the count call also returns 1
        restOutletStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOutletStatusShouldNotBeFound(String filter) throws Exception {
        restOutletStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOutletStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOutletStatus() throws Exception {
        // Get the outletStatus
        restOutletStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOutletStatus() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        int databaseSizeBeforeUpdate = outletStatusRepository.findAll().size();

        // Update the outletStatus
        OutletStatus updatedOutletStatus = outletStatusRepository.findById(outletStatus.getId()).get();
        // Disconnect from session so that the updates on updatedOutletStatus are not directly saved in db
        em.detach(updatedOutletStatus);
        updatedOutletStatus
            .branchStatusTypeCode(UPDATED_BRANCH_STATUS_TYPE_CODE)
            .branchStatusType(UPDATED_BRANCH_STATUS_TYPE)
            .branchStatusTypeDescription(UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION);
        OutletStatusDTO outletStatusDTO = outletStatusMapper.toDto(updatedOutletStatus);

        restOutletStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outletStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outletStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the OutletStatus in the database
        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeUpdate);
        OutletStatus testOutletStatus = outletStatusList.get(outletStatusList.size() - 1);
        assertThat(testOutletStatus.getBranchStatusTypeCode()).isEqualTo(UPDATED_BRANCH_STATUS_TYPE_CODE);
        assertThat(testOutletStatus.getBranchStatusType()).isEqualTo(UPDATED_BRANCH_STATUS_TYPE);
        assertThat(testOutletStatus.getBranchStatusTypeDescription()).isEqualTo(UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION);

        // Validate the OutletStatus in Elasticsearch
        verify(mockOutletStatusSearchRepository).save(testOutletStatus);
    }

    @Test
    @Transactional
    void putNonExistingOutletStatus() throws Exception {
        int databaseSizeBeforeUpdate = outletStatusRepository.findAll().size();
        outletStatus.setId(count.incrementAndGet());

        // Create the OutletStatus
        OutletStatusDTO outletStatusDTO = outletStatusMapper.toDto(outletStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutletStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outletStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outletStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutletStatus in the database
        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutletStatus in Elasticsearch
        verify(mockOutletStatusSearchRepository, times(0)).save(outletStatus);
    }

    @Test
    @Transactional
    void putWithIdMismatchOutletStatus() throws Exception {
        int databaseSizeBeforeUpdate = outletStatusRepository.findAll().size();
        outletStatus.setId(count.incrementAndGet());

        // Create the OutletStatus
        OutletStatusDTO outletStatusDTO = outletStatusMapper.toDto(outletStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutletStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outletStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutletStatus in the database
        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutletStatus in Elasticsearch
        verify(mockOutletStatusSearchRepository, times(0)).save(outletStatus);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOutletStatus() throws Exception {
        int databaseSizeBeforeUpdate = outletStatusRepository.findAll().size();
        outletStatus.setId(count.incrementAndGet());

        // Create the OutletStatus
        OutletStatusDTO outletStatusDTO = outletStatusMapper.toDto(outletStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutletStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outletStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OutletStatus in the database
        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutletStatus in Elasticsearch
        verify(mockOutletStatusSearchRepository, times(0)).save(outletStatus);
    }

    @Test
    @Transactional
    void partialUpdateOutletStatusWithPatch() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        int databaseSizeBeforeUpdate = outletStatusRepository.findAll().size();

        // Update the outletStatus using partial update
        OutletStatus partialUpdatedOutletStatus = new OutletStatus();
        partialUpdatedOutletStatus.setId(outletStatus.getId());

        partialUpdatedOutletStatus.branchStatusType(UPDATED_BRANCH_STATUS_TYPE);

        restOutletStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutletStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOutletStatus))
            )
            .andExpect(status().isOk());

        // Validate the OutletStatus in the database
        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeUpdate);
        OutletStatus testOutletStatus = outletStatusList.get(outletStatusList.size() - 1);
        assertThat(testOutletStatus.getBranchStatusTypeCode()).isEqualTo(DEFAULT_BRANCH_STATUS_TYPE_CODE);
        assertThat(testOutletStatus.getBranchStatusType()).isEqualTo(UPDATED_BRANCH_STATUS_TYPE);
        assertThat(testOutletStatus.getBranchStatusTypeDescription()).isEqualTo(DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateOutletStatusWithPatch() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        int databaseSizeBeforeUpdate = outletStatusRepository.findAll().size();

        // Update the outletStatus using partial update
        OutletStatus partialUpdatedOutletStatus = new OutletStatus();
        partialUpdatedOutletStatus.setId(outletStatus.getId());

        partialUpdatedOutletStatus
            .branchStatusTypeCode(UPDATED_BRANCH_STATUS_TYPE_CODE)
            .branchStatusType(UPDATED_BRANCH_STATUS_TYPE)
            .branchStatusTypeDescription(UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION);

        restOutletStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutletStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOutletStatus))
            )
            .andExpect(status().isOk());

        // Validate the OutletStatus in the database
        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeUpdate);
        OutletStatus testOutletStatus = outletStatusList.get(outletStatusList.size() - 1);
        assertThat(testOutletStatus.getBranchStatusTypeCode()).isEqualTo(UPDATED_BRANCH_STATUS_TYPE_CODE);
        assertThat(testOutletStatus.getBranchStatusType()).isEqualTo(UPDATED_BRANCH_STATUS_TYPE);
        assertThat(testOutletStatus.getBranchStatusTypeDescription()).isEqualTo(UPDATED_BRANCH_STATUS_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingOutletStatus() throws Exception {
        int databaseSizeBeforeUpdate = outletStatusRepository.findAll().size();
        outletStatus.setId(count.incrementAndGet());

        // Create the OutletStatus
        OutletStatusDTO outletStatusDTO = outletStatusMapper.toDto(outletStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutletStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, outletStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outletStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutletStatus in the database
        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutletStatus in Elasticsearch
        verify(mockOutletStatusSearchRepository, times(0)).save(outletStatus);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOutletStatus() throws Exception {
        int databaseSizeBeforeUpdate = outletStatusRepository.findAll().size();
        outletStatus.setId(count.incrementAndGet());

        // Create the OutletStatus
        OutletStatusDTO outletStatusDTO = outletStatusMapper.toDto(outletStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutletStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outletStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutletStatus in the database
        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutletStatus in Elasticsearch
        verify(mockOutletStatusSearchRepository, times(0)).save(outletStatus);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOutletStatus() throws Exception {
        int databaseSizeBeforeUpdate = outletStatusRepository.findAll().size();
        outletStatus.setId(count.incrementAndGet());

        // Create the OutletStatus
        OutletStatusDTO outletStatusDTO = outletStatusMapper.toDto(outletStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutletStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outletStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OutletStatus in the database
        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutletStatus in Elasticsearch
        verify(mockOutletStatusSearchRepository, times(0)).save(outletStatus);
    }

    @Test
    @Transactional
    void deleteOutletStatus() throws Exception {
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);

        int databaseSizeBeforeDelete = outletStatusRepository.findAll().size();

        // Delete the outletStatus
        restOutletStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, outletStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OutletStatus> outletStatusList = outletStatusRepository.findAll();
        assertThat(outletStatusList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OutletStatus in Elasticsearch
        verify(mockOutletStatusSearchRepository, times(1)).deleteById(outletStatus.getId());
    }

    @Test
    @Transactional
    void searchOutletStatus() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        outletStatusRepository.saveAndFlush(outletStatus);
        when(mockOutletStatusSearchRepository.search("id:" + outletStatus.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(outletStatus), PageRequest.of(0, 1), 1));

        // Search the outletStatus
        restOutletStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + outletStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outletStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchStatusTypeCode").value(hasItem(DEFAULT_BRANCH_STATUS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].branchStatusType").value(hasItem(DEFAULT_BRANCH_STATUS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].branchStatusTypeDescription").value(hasItem(DEFAULT_BRANCH_STATUS_TYPE_DESCRIPTION)));
    }
}
