package io.github.erp.web.rest;

/*-
 * Erp System - Mark III No 16 (Caleb Series) Server ver 1.2.7
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
import io.github.erp.domain.BusinessDocument;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.WorkInProgressRegistration;
import io.github.erp.domain.WorkInProgressTransfer;
import io.github.erp.repository.WorkInProgressTransferRepository;
import io.github.erp.repository.search.WorkInProgressTransferSearchRepository;
import io.github.erp.service.WorkInProgressTransferService;
import io.github.erp.service.criteria.WorkInProgressTransferCriteria;
import io.github.erp.service.dto.WorkInProgressTransferDTO;
import io.github.erp.service.mapper.WorkInProgressTransferMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
 * Integration tests for the {@link WorkInProgressTransferResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkInProgressTransferResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_ASSET_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_ASSET_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/work-in-progress-transfers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/work-in-progress-transfers";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkInProgressTransferRepository workInProgressTransferRepository;

    @Mock
    private WorkInProgressTransferRepository workInProgressTransferRepositoryMock;

    @Autowired
    private WorkInProgressTransferMapper workInProgressTransferMapper;

    @Mock
    private WorkInProgressTransferService workInProgressTransferServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WorkInProgressTransferSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkInProgressTransferSearchRepository mockWorkInProgressTransferSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkInProgressTransferMockMvc;

    private WorkInProgressTransfer workInProgressTransfer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkInProgressTransfer createEntity(EntityManager em) {
        WorkInProgressTransfer workInProgressTransfer = new WorkInProgressTransfer()
            .description(DEFAULT_DESCRIPTION)
            .targetAssetNumber(DEFAULT_TARGET_ASSET_NUMBER);
        return workInProgressTransfer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkInProgressTransfer createUpdatedEntity(EntityManager em) {
        WorkInProgressTransfer workInProgressTransfer = new WorkInProgressTransfer()
            .description(UPDATED_DESCRIPTION)
            .targetAssetNumber(UPDATED_TARGET_ASSET_NUMBER);
        return workInProgressTransfer;
    }

    @BeforeEach
    public void initTest() {
        workInProgressTransfer = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkInProgressTransfer() throws Exception {
        int databaseSizeBeforeCreate = workInProgressTransferRepository.findAll().size();
        // Create the WorkInProgressTransfer
        WorkInProgressTransferDTO workInProgressTransferDTO = workInProgressTransferMapper.toDto(workInProgressTransfer);
        restWorkInProgressTransferMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressTransferDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkInProgressTransfer in the database
        List<WorkInProgressTransfer> workInProgressTransferList = workInProgressTransferRepository.findAll();
        assertThat(workInProgressTransferList).hasSize(databaseSizeBeforeCreate + 1);
        WorkInProgressTransfer testWorkInProgressTransfer = workInProgressTransferList.get(workInProgressTransferList.size() - 1);
        assertThat(testWorkInProgressTransfer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkInProgressTransfer.getTargetAssetNumber()).isEqualTo(DEFAULT_TARGET_ASSET_NUMBER);

        // Validate the WorkInProgressTransfer in Elasticsearch
        verify(mockWorkInProgressTransferSearchRepository, times(1)).save(testWorkInProgressTransfer);
    }

    @Test
    @Transactional
    void createWorkInProgressTransferWithExistingId() throws Exception {
        // Create the WorkInProgressTransfer with an existing ID
        workInProgressTransfer.setId(1L);
        WorkInProgressTransferDTO workInProgressTransferDTO = workInProgressTransferMapper.toDto(workInProgressTransfer);

        int databaseSizeBeforeCreate = workInProgressTransferRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkInProgressTransferMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressTransferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressTransfer in the database
        List<WorkInProgressTransfer> workInProgressTransferList = workInProgressTransferRepository.findAll();
        assertThat(workInProgressTransferList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkInProgressTransfer in Elasticsearch
        verify(mockWorkInProgressTransferSearchRepository, times(0)).save(workInProgressTransfer);
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfers() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList
        restWorkInProgressTransferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].targetAssetNumber").value(hasItem(DEFAULT_TARGET_ASSET_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkInProgressTransfersWithEagerRelationshipsIsEnabled() throws Exception {
        when(workInProgressTransferServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkInProgressTransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workInProgressTransferServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkInProgressTransfersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(workInProgressTransferServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkInProgressTransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workInProgressTransferServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getWorkInProgressTransfer() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get the workInProgressTransfer
        restWorkInProgressTransferMockMvc
            .perform(get(ENTITY_API_URL_ID, workInProgressTransfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workInProgressTransfer.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.targetAssetNumber").value(DEFAULT_TARGET_ASSET_NUMBER));
    }

    @Test
    @Transactional
    void getWorkInProgressTransfersByIdFiltering() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        Long id = workInProgressTransfer.getId();

        defaultWorkInProgressTransferShouldBeFound("id.equals=" + id);
        defaultWorkInProgressTransferShouldNotBeFound("id.notEquals=" + id);

        defaultWorkInProgressTransferShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWorkInProgressTransferShouldNotBeFound("id.greaterThan=" + id);

        defaultWorkInProgressTransferShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWorkInProgressTransferShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList where description equals to DEFAULT_DESCRIPTION
        defaultWorkInProgressTransferShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the workInProgressTransferList where description equals to UPDATED_DESCRIPTION
        defaultWorkInProgressTransferShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList where description not equals to DEFAULT_DESCRIPTION
        defaultWorkInProgressTransferShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the workInProgressTransferList where description not equals to UPDATED_DESCRIPTION
        defaultWorkInProgressTransferShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultWorkInProgressTransferShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the workInProgressTransferList where description equals to UPDATED_DESCRIPTION
        defaultWorkInProgressTransferShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList where description is not null
        defaultWorkInProgressTransferShouldBeFound("description.specified=true");

        // Get all the workInProgressTransferList where description is null
        defaultWorkInProgressTransferShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList where description contains DEFAULT_DESCRIPTION
        defaultWorkInProgressTransferShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the workInProgressTransferList where description contains UPDATED_DESCRIPTION
        defaultWorkInProgressTransferShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList where description does not contain DEFAULT_DESCRIPTION
        defaultWorkInProgressTransferShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the workInProgressTransferList where description does not contain UPDATED_DESCRIPTION
        defaultWorkInProgressTransferShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByTargetAssetNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList where targetAssetNumber equals to DEFAULT_TARGET_ASSET_NUMBER
        defaultWorkInProgressTransferShouldBeFound("targetAssetNumber.equals=" + DEFAULT_TARGET_ASSET_NUMBER);

        // Get all the workInProgressTransferList where targetAssetNumber equals to UPDATED_TARGET_ASSET_NUMBER
        defaultWorkInProgressTransferShouldNotBeFound("targetAssetNumber.equals=" + UPDATED_TARGET_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByTargetAssetNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList where targetAssetNumber not equals to DEFAULT_TARGET_ASSET_NUMBER
        defaultWorkInProgressTransferShouldNotBeFound("targetAssetNumber.notEquals=" + DEFAULT_TARGET_ASSET_NUMBER);

        // Get all the workInProgressTransferList where targetAssetNumber not equals to UPDATED_TARGET_ASSET_NUMBER
        defaultWorkInProgressTransferShouldBeFound("targetAssetNumber.notEquals=" + UPDATED_TARGET_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByTargetAssetNumberIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList where targetAssetNumber in DEFAULT_TARGET_ASSET_NUMBER or UPDATED_TARGET_ASSET_NUMBER
        defaultWorkInProgressTransferShouldBeFound(
            "targetAssetNumber.in=" + DEFAULT_TARGET_ASSET_NUMBER + "," + UPDATED_TARGET_ASSET_NUMBER
        );

        // Get all the workInProgressTransferList where targetAssetNumber equals to UPDATED_TARGET_ASSET_NUMBER
        defaultWorkInProgressTransferShouldNotBeFound("targetAssetNumber.in=" + UPDATED_TARGET_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByTargetAssetNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList where targetAssetNumber is not null
        defaultWorkInProgressTransferShouldBeFound("targetAssetNumber.specified=true");

        // Get all the workInProgressTransferList where targetAssetNumber is null
        defaultWorkInProgressTransferShouldNotBeFound("targetAssetNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByTargetAssetNumberContainsSomething() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList where targetAssetNumber contains DEFAULT_TARGET_ASSET_NUMBER
        defaultWorkInProgressTransferShouldBeFound("targetAssetNumber.contains=" + DEFAULT_TARGET_ASSET_NUMBER);

        // Get all the workInProgressTransferList where targetAssetNumber contains UPDATED_TARGET_ASSET_NUMBER
        defaultWorkInProgressTransferShouldNotBeFound("targetAssetNumber.contains=" + UPDATED_TARGET_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByTargetAssetNumberNotContainsSomething() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        // Get all the workInProgressTransferList where targetAssetNumber does not contain DEFAULT_TARGET_ASSET_NUMBER
        defaultWorkInProgressTransferShouldNotBeFound("targetAssetNumber.doesNotContain=" + DEFAULT_TARGET_ASSET_NUMBER);

        // Get all the workInProgressTransferList where targetAssetNumber does not contain UPDATED_TARGET_ASSET_NUMBER
        defaultWorkInProgressTransferShouldBeFound("targetAssetNumber.doesNotContain=" + UPDATED_TARGET_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByWorkInProgressRegistrationIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);
        WorkInProgressRegistration workInProgressRegistration;
        if (TestUtil.findAll(em, WorkInProgressRegistration.class).isEmpty()) {
            workInProgressRegistration = WorkInProgressRegistrationResourceIT.createEntity(em);
            em.persist(workInProgressRegistration);
            em.flush();
        } else {
            workInProgressRegistration = TestUtil.findAll(em, WorkInProgressRegistration.class).get(0);
        }
        em.persist(workInProgressRegistration);
        em.flush();
        workInProgressTransfer.addWorkInProgressRegistration(workInProgressRegistration);
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);
        Long workInProgressRegistrationId = workInProgressRegistration.getId();

        // Get all the workInProgressTransferList where workInProgressRegistration equals to workInProgressRegistrationId
        defaultWorkInProgressTransferShouldBeFound("workInProgressRegistrationId.equals=" + workInProgressRegistrationId);

        // Get all the workInProgressTransferList where workInProgressRegistration equals to (workInProgressRegistrationId + 1)
        defaultWorkInProgressTransferShouldNotBeFound("workInProgressRegistrationId.equals=" + (workInProgressRegistrationId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);
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
        workInProgressTransfer.addPlaceholder(placeholder);
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);
        Long placeholderId = placeholder.getId();

        // Get all the workInProgressTransferList where placeholder equals to placeholderId
        defaultWorkInProgressTransferShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the workInProgressTransferList where placeholder equals to (placeholderId + 1)
        defaultWorkInProgressTransferShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressTransfersByBusinessDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);
        BusinessDocument businessDocument;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            businessDocument = BusinessDocumentResourceIT.createEntity(em);
            em.persist(businessDocument);
            em.flush();
        } else {
            businessDocument = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(businessDocument);
        em.flush();
        workInProgressTransfer.addBusinessDocument(businessDocument);
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);
        Long businessDocumentId = businessDocument.getId();

        // Get all the workInProgressTransferList where businessDocument equals to businessDocumentId
        defaultWorkInProgressTransferShouldBeFound("businessDocumentId.equals=" + businessDocumentId);

        // Get all the workInProgressTransferList where businessDocument equals to (businessDocumentId + 1)
        defaultWorkInProgressTransferShouldNotBeFound("businessDocumentId.equals=" + (businessDocumentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkInProgressTransferShouldBeFound(String filter) throws Exception {
        restWorkInProgressTransferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].targetAssetNumber").value(hasItem(DEFAULT_TARGET_ASSET_NUMBER)));

        // Check, that the count call also returns 1
        restWorkInProgressTransferMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkInProgressTransferShouldNotBeFound(String filter) throws Exception {
        restWorkInProgressTransferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkInProgressTransferMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkInProgressTransfer() throws Exception {
        // Get the workInProgressTransfer
        restWorkInProgressTransferMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkInProgressTransfer() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        int databaseSizeBeforeUpdate = workInProgressTransferRepository.findAll().size();

        // Update the workInProgressTransfer
        WorkInProgressTransfer updatedWorkInProgressTransfer = workInProgressTransferRepository
            .findById(workInProgressTransfer.getId())
            .get();
        // Disconnect from session so that the updates on updatedWorkInProgressTransfer are not directly saved in db
        em.detach(updatedWorkInProgressTransfer);
        updatedWorkInProgressTransfer.description(UPDATED_DESCRIPTION).targetAssetNumber(UPDATED_TARGET_ASSET_NUMBER);
        WorkInProgressTransferDTO workInProgressTransferDTO = workInProgressTransferMapper.toDto(updatedWorkInProgressTransfer);

        restWorkInProgressTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workInProgressTransferDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressTransferDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkInProgressTransfer in the database
        List<WorkInProgressTransfer> workInProgressTransferList = workInProgressTransferRepository.findAll();
        assertThat(workInProgressTransferList).hasSize(databaseSizeBeforeUpdate);
        WorkInProgressTransfer testWorkInProgressTransfer = workInProgressTransferList.get(workInProgressTransferList.size() - 1);
        assertThat(testWorkInProgressTransfer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkInProgressTransfer.getTargetAssetNumber()).isEqualTo(UPDATED_TARGET_ASSET_NUMBER);

        // Validate the WorkInProgressTransfer in Elasticsearch
        verify(mockWorkInProgressTransferSearchRepository).save(testWorkInProgressTransfer);
    }

    @Test
    @Transactional
    void putNonExistingWorkInProgressTransfer() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressTransferRepository.findAll().size();
        workInProgressTransfer.setId(count.incrementAndGet());

        // Create the WorkInProgressTransfer
        WorkInProgressTransferDTO workInProgressTransferDTO = workInProgressTransferMapper.toDto(workInProgressTransfer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkInProgressTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workInProgressTransferDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressTransferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressTransfer in the database
        List<WorkInProgressTransfer> workInProgressTransferList = workInProgressTransferRepository.findAll();
        assertThat(workInProgressTransferList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressTransfer in Elasticsearch
        verify(mockWorkInProgressTransferSearchRepository, times(0)).save(workInProgressTransfer);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkInProgressTransfer() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressTransferRepository.findAll().size();
        workInProgressTransfer.setId(count.incrementAndGet());

        // Create the WorkInProgressTransfer
        WorkInProgressTransferDTO workInProgressTransferDTO = workInProgressTransferMapper.toDto(workInProgressTransfer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkInProgressTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressTransferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressTransfer in the database
        List<WorkInProgressTransfer> workInProgressTransferList = workInProgressTransferRepository.findAll();
        assertThat(workInProgressTransferList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressTransfer in Elasticsearch
        verify(mockWorkInProgressTransferSearchRepository, times(0)).save(workInProgressTransfer);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkInProgressTransfer() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressTransferRepository.findAll().size();
        workInProgressTransfer.setId(count.incrementAndGet());

        // Create the WorkInProgressTransfer
        WorkInProgressTransferDTO workInProgressTransferDTO = workInProgressTransferMapper.toDto(workInProgressTransfer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkInProgressTransferMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressTransferDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkInProgressTransfer in the database
        List<WorkInProgressTransfer> workInProgressTransferList = workInProgressTransferRepository.findAll();
        assertThat(workInProgressTransferList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressTransfer in Elasticsearch
        verify(mockWorkInProgressTransferSearchRepository, times(0)).save(workInProgressTransfer);
    }

    @Test
    @Transactional
    void partialUpdateWorkInProgressTransferWithPatch() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        int databaseSizeBeforeUpdate = workInProgressTransferRepository.findAll().size();

        // Update the workInProgressTransfer using partial update
        WorkInProgressTransfer partialUpdatedWorkInProgressTransfer = new WorkInProgressTransfer();
        partialUpdatedWorkInProgressTransfer.setId(workInProgressTransfer.getId());

        restWorkInProgressTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkInProgressTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkInProgressTransfer))
            )
            .andExpect(status().isOk());

        // Validate the WorkInProgressTransfer in the database
        List<WorkInProgressTransfer> workInProgressTransferList = workInProgressTransferRepository.findAll();
        assertThat(workInProgressTransferList).hasSize(databaseSizeBeforeUpdate);
        WorkInProgressTransfer testWorkInProgressTransfer = workInProgressTransferList.get(workInProgressTransferList.size() - 1);
        assertThat(testWorkInProgressTransfer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkInProgressTransfer.getTargetAssetNumber()).isEqualTo(DEFAULT_TARGET_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateWorkInProgressTransferWithPatch() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        int databaseSizeBeforeUpdate = workInProgressTransferRepository.findAll().size();

        // Update the workInProgressTransfer using partial update
        WorkInProgressTransfer partialUpdatedWorkInProgressTransfer = new WorkInProgressTransfer();
        partialUpdatedWorkInProgressTransfer.setId(workInProgressTransfer.getId());

        partialUpdatedWorkInProgressTransfer.description(UPDATED_DESCRIPTION).targetAssetNumber(UPDATED_TARGET_ASSET_NUMBER);

        restWorkInProgressTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkInProgressTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkInProgressTransfer))
            )
            .andExpect(status().isOk());

        // Validate the WorkInProgressTransfer in the database
        List<WorkInProgressTransfer> workInProgressTransferList = workInProgressTransferRepository.findAll();
        assertThat(workInProgressTransferList).hasSize(databaseSizeBeforeUpdate);
        WorkInProgressTransfer testWorkInProgressTransfer = workInProgressTransferList.get(workInProgressTransferList.size() - 1);
        assertThat(testWorkInProgressTransfer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkInProgressTransfer.getTargetAssetNumber()).isEqualTo(UPDATED_TARGET_ASSET_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingWorkInProgressTransfer() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressTransferRepository.findAll().size();
        workInProgressTransfer.setId(count.incrementAndGet());

        // Create the WorkInProgressTransfer
        WorkInProgressTransferDTO workInProgressTransferDTO = workInProgressTransferMapper.toDto(workInProgressTransfer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkInProgressTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workInProgressTransferDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressTransferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressTransfer in the database
        List<WorkInProgressTransfer> workInProgressTransferList = workInProgressTransferRepository.findAll();
        assertThat(workInProgressTransferList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressTransfer in Elasticsearch
        verify(mockWorkInProgressTransferSearchRepository, times(0)).save(workInProgressTransfer);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkInProgressTransfer() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressTransferRepository.findAll().size();
        workInProgressTransfer.setId(count.incrementAndGet());

        // Create the WorkInProgressTransfer
        WorkInProgressTransferDTO workInProgressTransferDTO = workInProgressTransferMapper.toDto(workInProgressTransfer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkInProgressTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressTransferDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressTransfer in the database
        List<WorkInProgressTransfer> workInProgressTransferList = workInProgressTransferRepository.findAll();
        assertThat(workInProgressTransferList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressTransfer in Elasticsearch
        verify(mockWorkInProgressTransferSearchRepository, times(0)).save(workInProgressTransfer);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkInProgressTransfer() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressTransferRepository.findAll().size();
        workInProgressTransfer.setId(count.incrementAndGet());

        // Create the WorkInProgressTransfer
        WorkInProgressTransferDTO workInProgressTransferDTO = workInProgressTransferMapper.toDto(workInProgressTransfer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkInProgressTransferMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressTransferDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkInProgressTransfer in the database
        List<WorkInProgressTransfer> workInProgressTransferList = workInProgressTransferRepository.findAll();
        assertThat(workInProgressTransferList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressTransfer in Elasticsearch
        verify(mockWorkInProgressTransferSearchRepository, times(0)).save(workInProgressTransfer);
    }

    @Test
    @Transactional
    void deleteWorkInProgressTransfer() throws Exception {
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);

        int databaseSizeBeforeDelete = workInProgressTransferRepository.findAll().size();

        // Delete the workInProgressTransfer
        restWorkInProgressTransferMockMvc
            .perform(delete(ENTITY_API_URL_ID, workInProgressTransfer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkInProgressTransfer> workInProgressTransferList = workInProgressTransferRepository.findAll();
        assertThat(workInProgressTransferList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkInProgressTransfer in Elasticsearch
        verify(mockWorkInProgressTransferSearchRepository, times(1)).deleteById(workInProgressTransfer.getId());
    }

    @Test
    @Transactional
    void searchWorkInProgressTransfer() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workInProgressTransferRepository.saveAndFlush(workInProgressTransfer);
        when(mockWorkInProgressTransferSearchRepository.search("id:" + workInProgressTransfer.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(workInProgressTransfer), PageRequest.of(0, 1), 1));

        // Search the workInProgressTransfer
        restWorkInProgressTransferMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workInProgressTransfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].targetAssetNumber").value(hasItem(DEFAULT_TARGET_ASSET_NUMBER)));
    }
}
