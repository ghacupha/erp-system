package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.1-SNAPSHOT
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

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.SecurityClearance;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.SecurityClearanceRepository;
import io.github.erp.repository.search.SecurityClearanceSearchRepository;
import io.github.erp.service.SecurityClearanceService;
import io.github.erp.service.dto.SecurityClearanceDTO;
import io.github.erp.service.mapper.SecurityClearanceMapper;
import io.github.erp.web.rest.SecurityClearanceResource;
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
 * Integration tests for the {@link SecurityClearanceResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SecurityClearanceResourceIT {

    private static final String DEFAULT_CLEARANCE_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_CLEARANCE_LEVEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/security-clearances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/security-clearances";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SecurityClearanceRepository securityClearanceRepository;

    @Mock
    private SecurityClearanceRepository securityClearanceRepositoryMock;

    @Autowired
    private SecurityClearanceMapper securityClearanceMapper;

    @Mock
    private SecurityClearanceService securityClearanceServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SecurityClearanceSearchRepositoryMockConfiguration
     */
    @Autowired
    private SecurityClearanceSearchRepository mockSecurityClearanceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSecurityClearanceMockMvc;

    private SecurityClearance securityClearance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityClearance createEntity(EntityManager em) {
        SecurityClearance securityClearance = new SecurityClearance().clearanceLevel(DEFAULT_CLEARANCE_LEVEL);
        return securityClearance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityClearance createUpdatedEntity(EntityManager em) {
        SecurityClearance securityClearance = new SecurityClearance().clearanceLevel(UPDATED_CLEARANCE_LEVEL);
        return securityClearance;
    }

    @BeforeEach
    public void initTest() {
        securityClearance = createEntity(em);
    }

    @Test
    @Transactional
    void createSecurityClearance() throws Exception {
        int databaseSizeBeforeCreate = securityClearanceRepository.findAll().size();
        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);
        restSecurityClearanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeCreate + 1);
        SecurityClearance testSecurityClearance = securityClearanceList.get(securityClearanceList.size() - 1);
        assertThat(testSecurityClearance.getClearanceLevel()).isEqualTo(DEFAULT_CLEARANCE_LEVEL);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(1)).save(testSecurityClearance);
    }

    @Test
    @Transactional
    void createSecurityClearanceWithExistingId() throws Exception {
        // Create the SecurityClearance with an existing ID
        securityClearance.setId(1L);
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        int databaseSizeBeforeCreate = securityClearanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecurityClearanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeCreate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void checkClearanceLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityClearanceRepository.findAll().size();
        // set the field null
        securityClearance.setClearanceLevel(null);

        // Create the SecurityClearance, which fails.
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        restSecurityClearanceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSecurityClearances() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityClearance.getId().intValue())))
            .andExpect(jsonPath("$.[*].clearanceLevel").value(hasItem(DEFAULT_CLEARANCE_LEVEL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSecurityClearancesWithEagerRelationshipsIsEnabled() throws Exception {
        when(securityClearanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSecurityClearanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(securityClearanceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSecurityClearancesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(securityClearanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSecurityClearanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(securityClearanceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSecurityClearance() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        // Get the securityClearance
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL_ID, securityClearance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(securityClearance.getId().intValue()))
            .andExpect(jsonPath("$.clearanceLevel").value(DEFAULT_CLEARANCE_LEVEL));
    }

    @Test
    @Transactional
    void getSecurityClearancesByIdFiltering() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        Long id = securityClearance.getId();

        defaultSecurityClearanceShouldBeFound("id.equals=" + id);
        defaultSecurityClearanceShouldNotBeFound("id.notEquals=" + id);

        defaultSecurityClearanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSecurityClearanceShouldNotBeFound("id.greaterThan=" + id);

        defaultSecurityClearanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSecurityClearanceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByClearanceLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where clearanceLevel equals to DEFAULT_CLEARANCE_LEVEL
        defaultSecurityClearanceShouldBeFound("clearanceLevel.equals=" + DEFAULT_CLEARANCE_LEVEL);

        // Get all the securityClearanceList where clearanceLevel equals to UPDATED_CLEARANCE_LEVEL
        defaultSecurityClearanceShouldNotBeFound("clearanceLevel.equals=" + UPDATED_CLEARANCE_LEVEL);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByClearanceLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where clearanceLevel not equals to DEFAULT_CLEARANCE_LEVEL
        defaultSecurityClearanceShouldNotBeFound("clearanceLevel.notEquals=" + DEFAULT_CLEARANCE_LEVEL);

        // Get all the securityClearanceList where clearanceLevel not equals to UPDATED_CLEARANCE_LEVEL
        defaultSecurityClearanceShouldBeFound("clearanceLevel.notEquals=" + UPDATED_CLEARANCE_LEVEL);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByClearanceLevelIsInShouldWork() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where clearanceLevel in DEFAULT_CLEARANCE_LEVEL or UPDATED_CLEARANCE_LEVEL
        defaultSecurityClearanceShouldBeFound("clearanceLevel.in=" + DEFAULT_CLEARANCE_LEVEL + "," + UPDATED_CLEARANCE_LEVEL);

        // Get all the securityClearanceList where clearanceLevel equals to UPDATED_CLEARANCE_LEVEL
        defaultSecurityClearanceShouldNotBeFound("clearanceLevel.in=" + UPDATED_CLEARANCE_LEVEL);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByClearanceLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where clearanceLevel is not null
        defaultSecurityClearanceShouldBeFound("clearanceLevel.specified=true");

        // Get all the securityClearanceList where clearanceLevel is null
        defaultSecurityClearanceShouldNotBeFound("clearanceLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByClearanceLevelContainsSomething() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where clearanceLevel contains DEFAULT_CLEARANCE_LEVEL
        defaultSecurityClearanceShouldBeFound("clearanceLevel.contains=" + DEFAULT_CLEARANCE_LEVEL);

        // Get all the securityClearanceList where clearanceLevel contains UPDATED_CLEARANCE_LEVEL
        defaultSecurityClearanceShouldNotBeFound("clearanceLevel.contains=" + UPDATED_CLEARANCE_LEVEL);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByClearanceLevelNotContainsSomething() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        // Get all the securityClearanceList where clearanceLevel does not contain DEFAULT_CLEARANCE_LEVEL
        defaultSecurityClearanceShouldNotBeFound("clearanceLevel.doesNotContain=" + DEFAULT_CLEARANCE_LEVEL);

        // Get all the securityClearanceList where clearanceLevel does not contain UPDATED_CLEARANCE_LEVEL
        defaultSecurityClearanceShouldBeFound("clearanceLevel.doesNotContain=" + UPDATED_CLEARANCE_LEVEL);
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByGrantedClearancesIsEqualToSomething() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);
        SecurityClearance grantedClearances;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            grantedClearances = SecurityClearanceResourceIT.createEntity(em);
            em.persist(grantedClearances);
            em.flush();
        } else {
            grantedClearances = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        em.persist(grantedClearances);
        em.flush();
        securityClearance.addGrantedClearances(grantedClearances);
        securityClearanceRepository.saveAndFlush(securityClearance);
        Long grantedClearancesId = grantedClearances.getId();

        // Get all the securityClearanceList where grantedClearances equals to grantedClearancesId
        // TODO This leads to stackoverflow error as you are creating a parent within a child and when that child
        // TODO becomes a parent it leads to an infinite insertion loop. It's strange we can still create those in Java
        // defaultSecurityClearanceShouldBeFound("grantedClearancesId.equals=" + grantedClearancesId);

        // Get all the securityClearanceList where grantedClearances equals to (grantedClearancesId + 1)
        defaultSecurityClearanceShouldNotBeFound("grantedClearancesId.equals=" + (grantedClearancesId + 1));
    }

    @Test
    @Transactional
    void getAllSecurityClearancesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);
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
        securityClearance.addPlaceholder(placeholder);
        securityClearanceRepository.saveAndFlush(securityClearance);
        Long placeholderId = placeholder.getId();

        // Get all the securityClearanceList where placeholder equals to placeholderId
        defaultSecurityClearanceShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the securityClearanceList where placeholder equals to (placeholderId + 1)
        defaultSecurityClearanceShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllSecurityClearancesBySystemParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);
        UniversallyUniqueMapping systemParameters;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            systemParameters = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(systemParameters);
            em.flush();
        } else {
            systemParameters = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(systemParameters);
        em.flush();
        securityClearance.addSystemParameters(systemParameters);
        securityClearanceRepository.saveAndFlush(securityClearance);
        Long systemParametersId = systemParameters.getId();

        // Get all the securityClearanceList where systemParameters equals to systemParametersId
        defaultSecurityClearanceShouldBeFound("systemParametersId.equals=" + systemParametersId);

        // Get all the securityClearanceList where systemParameters equals to (systemParametersId + 1)
        defaultSecurityClearanceShouldNotBeFound("systemParametersId.equals=" + (systemParametersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSecurityClearanceShouldBeFound(String filter) throws Exception {
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityClearance.getId().intValue())))
            .andExpect(jsonPath("$.[*].clearanceLevel").value(hasItem(DEFAULT_CLEARANCE_LEVEL)));

        // Check, that the count call also returns 1
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSecurityClearanceShouldNotBeFound(String filter) throws Exception {
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSecurityClearance() throws Exception {
        // Get the securityClearance
        restSecurityClearanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSecurityClearance() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();

        // Update the securityClearance
        SecurityClearance updatedSecurityClearance = securityClearanceRepository.findById(securityClearance.getId()).get();
        // Disconnect from session so that the updates on updatedSecurityClearance are not directly saved in db
        em.detach(updatedSecurityClearance);
        updatedSecurityClearance.clearanceLevel(UPDATED_CLEARANCE_LEVEL);
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(updatedSecurityClearance);

        restSecurityClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityClearanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);
        SecurityClearance testSecurityClearance = securityClearanceList.get(securityClearanceList.size() - 1);
        assertThat(testSecurityClearance.getClearanceLevel()).isEqualTo(UPDATED_CLEARANCE_LEVEL);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository).save(testSecurityClearance);
    }

    @Test
    @Transactional
    void putNonExistingSecurityClearance() throws Exception {
        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();
        securityClearance.setId(count.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityClearanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void putWithIdMismatchSecurityClearance() throws Exception {
        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();
        securityClearance.setId(count.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSecurityClearance() throws Exception {
        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();
        securityClearance.setId(count.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void partialUpdateSecurityClearanceWithPatch() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();

        // Update the securityClearance using partial update
        SecurityClearance partialUpdatedSecurityClearance = new SecurityClearance();
        partialUpdatedSecurityClearance.setId(securityClearance.getId());

        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityClearance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityClearance))
            )
            .andExpect(status().isOk());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);
        SecurityClearance testSecurityClearance = securityClearanceList.get(securityClearanceList.size() - 1);
        assertThat(testSecurityClearance.getClearanceLevel()).isEqualTo(DEFAULT_CLEARANCE_LEVEL);
    }

    @Test
    @Transactional
    void fullUpdateSecurityClearanceWithPatch() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();

        // Update the securityClearance using partial update
        SecurityClearance partialUpdatedSecurityClearance = new SecurityClearance();
        partialUpdatedSecurityClearance.setId(securityClearance.getId());

        partialUpdatedSecurityClearance.clearanceLevel(UPDATED_CLEARANCE_LEVEL);

        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityClearance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityClearance))
            )
            .andExpect(status().isOk());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);
        SecurityClearance testSecurityClearance = securityClearanceList.get(securityClearanceList.size() - 1);
        assertThat(testSecurityClearance.getClearanceLevel()).isEqualTo(UPDATED_CLEARANCE_LEVEL);
    }

    @Test
    @Transactional
    void patchNonExistingSecurityClearance() throws Exception {
        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();
        securityClearance.setId(count.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, securityClearanceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSecurityClearance() throws Exception {
        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();
        securityClearance.setId(count.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSecurityClearance() throws Exception {
        int databaseSizeBeforeUpdate = securityClearanceRepository.findAll().size();
        securityClearance.setId(count.incrementAndGet());

        // Create the SecurityClearance
        SecurityClearanceDTO securityClearanceDTO = securityClearanceMapper.toDto(securityClearance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityClearanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityClearance in the database
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(0)).save(securityClearance);
    }

    @Test
    @Transactional
    void deleteSecurityClearance() throws Exception {
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);

        int databaseSizeBeforeDelete = securityClearanceRepository.findAll().size();

        // Delete the securityClearance
        restSecurityClearanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, securityClearance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SecurityClearance> securityClearanceList = securityClearanceRepository.findAll();
        assertThat(securityClearanceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SecurityClearance in Elasticsearch
        verify(mockSecurityClearanceSearchRepository, times(1)).deleteById(securityClearance.getId());
    }

    @Test
    @Transactional
    void searchSecurityClearance() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        securityClearanceRepository.saveAndFlush(securityClearance);
        when(mockSecurityClearanceSearchRepository.search("id:" + securityClearance.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(securityClearance), PageRequest.of(0, 1), 1));

        // Search the securityClearance
        restSecurityClearanceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + securityClearance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityClearance.getId().intValue())))
            .andExpect(jsonPath("$.[*].clearanceLevel").value(hasItem(DEFAULT_CLEARANCE_LEVEL)));
    }
}
