package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import io.github.erp.IntegrationTest;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeaseAmortizationSchedule;
import io.github.erp.domain.LeaseLiability;
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.repository.LeaseAmortizationScheduleRepository;
import io.github.erp.repository.search.LeaseAmortizationScheduleSearchRepository;
import io.github.erp.service.dto.LeaseAmortizationScheduleDTO;
import io.github.erp.service.mapper.LeaseAmortizationScheduleMapper;
import io.github.erp.web.rest.TestUtil;
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

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the LeaseAmortizationScheduleResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class LeaseAmortizationScheduleResourceIT {

    private static final UUID DEFAULT_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/leases/lease-amortization-schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/lease-amortization-schedules";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaseAmortizationScheduleRepository leaseAmortizationScheduleRepository;

    @Autowired
    private LeaseAmortizationScheduleMapper leaseAmortizationScheduleMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeaseAmortizationScheduleSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeaseAmortizationScheduleSearchRepository mockLeaseAmortizationScheduleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaseAmortizationScheduleMockMvc;

    private LeaseAmortizationSchedule leaseAmortizationSchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseAmortizationSchedule createEntity(EntityManager em) {
        LeaseAmortizationSchedule leaseAmortizationSchedule = new LeaseAmortizationSchedule().identifier(DEFAULT_IDENTIFIER);
        // Add required entity
        LeaseLiability leaseLiability;
        if (TestUtil.findAll(em, LeaseLiability.class).isEmpty()) {
            leaseLiability = LeaseLiabilityResourceIT.createEntity(em);
            em.persist(leaseLiability);
            em.flush();
        } else {
            leaseLiability = TestUtil.findAll(em, LeaseLiability.class).get(0);
        }
        leaseAmortizationSchedule.setLeaseLiability(leaseLiability);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        leaseAmortizationSchedule.setLeaseContract(iFRS16LeaseContract);
        return leaseAmortizationSchedule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaseAmortizationSchedule createUpdatedEntity(EntityManager em) {
        LeaseAmortizationSchedule leaseAmortizationSchedule = new LeaseAmortizationSchedule().identifier(UPDATED_IDENTIFIER);
        // Add required entity
        LeaseLiability leaseLiability;
        if (TestUtil.findAll(em, LeaseLiability.class).isEmpty()) {
            leaseLiability = LeaseLiabilityResourceIT.createUpdatedEntity(em);
            em.persist(leaseLiability);
            em.flush();
        } else {
            leaseLiability = TestUtil.findAll(em, LeaseLiability.class).get(0);
        }
        leaseAmortizationSchedule.setLeaseLiability(leaseLiability);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        leaseAmortizationSchedule.setLeaseContract(iFRS16LeaseContract);
        return leaseAmortizationSchedule;
    }

    @BeforeEach
    public void initTest() {
        leaseAmortizationSchedule = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaseAmortizationSchedule() throws Exception {
        int databaseSizeBeforeCreate = leaseAmortizationScheduleRepository.findAll().size();
        // Create the LeaseAmortizationSchedule
        LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO = leaseAmortizationScheduleMapper.toDto(leaseAmortizationSchedule);
        restLeaseAmortizationScheduleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationScheduleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaseAmortizationSchedule in the database
        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        LeaseAmortizationSchedule testLeaseAmortizationSchedule = leaseAmortizationScheduleList.get(
            leaseAmortizationScheduleList.size() - 1
        );
        assertThat(testLeaseAmortizationSchedule.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);

        // Validate the LeaseAmortizationSchedule in Elasticsearch
        verify(mockLeaseAmortizationScheduleSearchRepository, times(1)).save(testLeaseAmortizationSchedule);
    }

    @Test
    @Transactional
    void createLeaseAmortizationScheduleWithExistingId() throws Exception {
        // Create the LeaseAmortizationSchedule with an existing ID
        leaseAmortizationSchedule.setId(1L);
        LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO = leaseAmortizationScheduleMapper.toDto(leaseAmortizationSchedule);

        int databaseSizeBeforeCreate = leaseAmortizationScheduleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseAmortizationScheduleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseAmortizationSchedule in the database
        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeaseAmortizationSchedule in Elasticsearch
        verify(mockLeaseAmortizationScheduleSearchRepository, times(0)).save(leaseAmortizationSchedule);
    }

    @Test
    @Transactional
    void checkIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseAmortizationScheduleRepository.findAll().size();
        // set the field null
        leaseAmortizationSchedule.setIdentifier(null);

        // Create the LeaseAmortizationSchedule, which fails.
        LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO = leaseAmortizationScheduleMapper.toDto(leaseAmortizationSchedule);

        restLeaseAmortizationScheduleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationSchedules() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);

        // Get all the leaseAmortizationScheduleList
        restLeaseAmortizationScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseAmortizationSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }

    @Test
    @Transactional
    void getLeaseAmortizationSchedule() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);

        // Get the leaseAmortizationSchedule
        restLeaseAmortizationScheduleMockMvc
            .perform(get(ENTITY_API_URL_ID, leaseAmortizationSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaseAmortizationSchedule.getId().intValue()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getLeaseAmortizationSchedulesByIdFiltering() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);

        Long id = leaseAmortizationSchedule.getId();

        defaultLeaseAmortizationScheduleShouldBeFound("id.equals=" + id);
        defaultLeaseAmortizationScheduleShouldNotBeFound("id.notEquals=" + id);

        defaultLeaseAmortizationScheduleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaseAmortizationScheduleShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaseAmortizationScheduleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaseAmortizationScheduleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationSchedulesByIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);

        // Get all the leaseAmortizationScheduleList where identifier equals to DEFAULT_IDENTIFIER
        defaultLeaseAmortizationScheduleShouldBeFound("identifier.equals=" + DEFAULT_IDENTIFIER);

        // Get all the leaseAmortizationScheduleList where identifier equals to UPDATED_IDENTIFIER
        defaultLeaseAmortizationScheduleShouldNotBeFound("identifier.equals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationSchedulesByIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);

        // Get all the leaseAmortizationScheduleList where identifier not equals to DEFAULT_IDENTIFIER
        defaultLeaseAmortizationScheduleShouldNotBeFound("identifier.notEquals=" + DEFAULT_IDENTIFIER);

        // Get all the leaseAmortizationScheduleList where identifier not equals to UPDATED_IDENTIFIER
        defaultLeaseAmortizationScheduleShouldBeFound("identifier.notEquals=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationSchedulesByIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);

        // Get all the leaseAmortizationScheduleList where identifier in DEFAULT_IDENTIFIER or UPDATED_IDENTIFIER
        defaultLeaseAmortizationScheduleShouldBeFound("identifier.in=" + DEFAULT_IDENTIFIER + "," + UPDATED_IDENTIFIER);

        // Get all the leaseAmortizationScheduleList where identifier equals to UPDATED_IDENTIFIER
        defaultLeaseAmortizationScheduleShouldNotBeFound("identifier.in=" + UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationSchedulesByIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);

        // Get all the leaseAmortizationScheduleList where identifier is not null
        defaultLeaseAmortizationScheduleShouldBeFound("identifier.specified=true");

        // Get all the leaseAmortizationScheduleList where identifier is null
        defaultLeaseAmortizationScheduleShouldNotBeFound("identifier.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationSchedulesByLeaseLiabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);
        LeaseLiability leaseLiability;
        if (TestUtil.findAll(em, LeaseLiability.class).isEmpty()) {
            leaseLiability = LeaseLiabilityResourceIT.createEntity(em);
            em.persist(leaseLiability);
            em.flush();
        } else {
            leaseLiability = TestUtil.findAll(em, LeaseLiability.class).get(0);
        }
        em.persist(leaseLiability);
        em.flush();
        leaseAmortizationSchedule.setLeaseLiability(leaseLiability);
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);
        Long leaseLiabilityId = leaseLiability.getId();

        // Get all the leaseAmortizationScheduleList where leaseLiability equals to leaseLiabilityId
        defaultLeaseAmortizationScheduleShouldBeFound("leaseLiabilityId.equals=" + leaseLiabilityId);

        // Get all the leaseAmortizationScheduleList where leaseLiability equals to (leaseLiabilityId + 1)
        defaultLeaseAmortizationScheduleShouldNotBeFound("leaseLiabilityId.equals=" + (leaseLiabilityId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationSchedulesByLeaseLiabilityScheduleItemIsEqualToSomething() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);
        LeaseLiabilityScheduleItem leaseLiabilityScheduleItem;
        if (TestUtil.findAll(em, LeaseLiabilityScheduleItem.class).isEmpty()) {
            leaseLiabilityScheduleItem = LeaseLiabilityScheduleItemResourceIT.createEntity(em);
            em.persist(leaseLiabilityScheduleItem);
            em.flush();
        } else {
            leaseLiabilityScheduleItem = TestUtil.findAll(em, LeaseLiabilityScheduleItem.class).get(0);
        }
        em.persist(leaseLiabilityScheduleItem);
        em.flush();
        leaseAmortizationSchedule.addLeaseLiabilityScheduleItem(leaseLiabilityScheduleItem);
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);
        Long leaseLiabilityScheduleItemId = leaseLiabilityScheduleItem.getId();

        // Get all the leaseAmortizationScheduleList where leaseLiabilityScheduleItem equals to leaseLiabilityScheduleItemId
        defaultLeaseAmortizationScheduleShouldBeFound("leaseLiabilityScheduleItemId.equals=" + leaseLiabilityScheduleItemId);

        // Get all the leaseAmortizationScheduleList where leaseLiabilityScheduleItem equals to (leaseLiabilityScheduleItemId + 1)
        defaultLeaseAmortizationScheduleShouldNotBeFound("leaseLiabilityScheduleItemId.equals=" + (leaseLiabilityScheduleItemId + 1));
    }

    @Test
    @Transactional
    void getAllLeaseAmortizationSchedulesByLeaseContractIsEqualToSomething() throws Exception {
        // Get already existing entity
        IFRS16LeaseContract leaseContract = leaseAmortizationSchedule.getLeaseContract();
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);
        Long leaseContractId = leaseContract.getId();

        // Get all the leaseAmortizationScheduleList where leaseContract equals to leaseContractId
        defaultLeaseAmortizationScheduleShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the leaseAmortizationScheduleList where leaseContract equals to (leaseContractId + 1)
        defaultLeaseAmortizationScheduleShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaseAmortizationScheduleShouldBeFound(String filter) throws Exception {
        restLeaseAmortizationScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseAmortizationSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));

        // Check, that the count call also returns 1
        restLeaseAmortizationScheduleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaseAmortizationScheduleShouldNotBeFound(String filter) throws Exception {
        restLeaseAmortizationScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaseAmortizationScheduleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaseAmortizationSchedule() throws Exception {
        // Get the leaseAmortizationSchedule
        restLeaseAmortizationScheduleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaseAmortizationSchedule() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);

        int databaseSizeBeforeUpdate = leaseAmortizationScheduleRepository.findAll().size();

        // Update the leaseAmortizationSchedule
        LeaseAmortizationSchedule updatedLeaseAmortizationSchedule = leaseAmortizationScheduleRepository
            .findById(leaseAmortizationSchedule.getId())
            .get();
        // Disconnect from session so that the updates on updatedLeaseAmortizationSchedule are not directly saved in db
        em.detach(updatedLeaseAmortizationSchedule);
        updatedLeaseAmortizationSchedule.identifier(UPDATED_IDENTIFIER);
        LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO = leaseAmortizationScheduleMapper.toDto(updatedLeaseAmortizationSchedule);

        restLeaseAmortizationScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseAmortizationScheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationScheduleDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaseAmortizationSchedule in the database
        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeUpdate);
        LeaseAmortizationSchedule testLeaseAmortizationSchedule = leaseAmortizationScheduleList.get(
            leaseAmortizationScheduleList.size() - 1
        );
        assertThat(testLeaseAmortizationSchedule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);

        // Validate the LeaseAmortizationSchedule in Elasticsearch
        verify(mockLeaseAmortizationScheduleSearchRepository).save(testLeaseAmortizationSchedule);
    }

    @Test
    @Transactional
    void putNonExistingLeaseAmortizationSchedule() throws Exception {
        int databaseSizeBeforeUpdate = leaseAmortizationScheduleRepository.findAll().size();
        leaseAmortizationSchedule.setId(count.incrementAndGet());

        // Create the LeaseAmortizationSchedule
        LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO = leaseAmortizationScheduleMapper.toDto(leaseAmortizationSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseAmortizationScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaseAmortizationScheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseAmortizationSchedule in the database
        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseAmortizationSchedule in Elasticsearch
        verify(mockLeaseAmortizationScheduleSearchRepository, times(0)).save(leaseAmortizationSchedule);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaseAmortizationSchedule() throws Exception {
        int databaseSizeBeforeUpdate = leaseAmortizationScheduleRepository.findAll().size();
        leaseAmortizationSchedule.setId(count.incrementAndGet());

        // Create the LeaseAmortizationSchedule
        LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO = leaseAmortizationScheduleMapper.toDto(leaseAmortizationSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseAmortizationScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseAmortizationSchedule in the database
        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseAmortizationSchedule in Elasticsearch
        verify(mockLeaseAmortizationScheduleSearchRepository, times(0)).save(leaseAmortizationSchedule);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaseAmortizationSchedule() throws Exception {
        int databaseSizeBeforeUpdate = leaseAmortizationScheduleRepository.findAll().size();
        leaseAmortizationSchedule.setId(count.incrementAndGet());

        // Create the LeaseAmortizationSchedule
        LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO = leaseAmortizationScheduleMapper.toDto(leaseAmortizationSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseAmortizationScheduleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationScheduleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseAmortizationSchedule in the database
        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseAmortizationSchedule in Elasticsearch
        verify(mockLeaseAmortizationScheduleSearchRepository, times(0)).save(leaseAmortizationSchedule);
    }

    @Test
    @Transactional
    void partialUpdateLeaseAmortizationScheduleWithPatch() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);

        int databaseSizeBeforeUpdate = leaseAmortizationScheduleRepository.findAll().size();

        // Update the leaseAmortizationSchedule using partial update
        LeaseAmortizationSchedule partialUpdatedLeaseAmortizationSchedule = new LeaseAmortizationSchedule();
        partialUpdatedLeaseAmortizationSchedule.setId(leaseAmortizationSchedule.getId());

        restLeaseAmortizationScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseAmortizationSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseAmortizationSchedule))
            )
            .andExpect(status().isOk());

        // Validate the LeaseAmortizationSchedule in the database
        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeUpdate);
        LeaseAmortizationSchedule testLeaseAmortizationSchedule = leaseAmortizationScheduleList.get(
            leaseAmortizationScheduleList.size() - 1
        );
        assertThat(testLeaseAmortizationSchedule.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateLeaseAmortizationScheduleWithPatch() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);

        int databaseSizeBeforeUpdate = leaseAmortizationScheduleRepository.findAll().size();

        // Update the leaseAmortizationSchedule using partial update
        LeaseAmortizationSchedule partialUpdatedLeaseAmortizationSchedule = new LeaseAmortizationSchedule();
        partialUpdatedLeaseAmortizationSchedule.setId(leaseAmortizationSchedule.getId());

        partialUpdatedLeaseAmortizationSchedule.identifier(UPDATED_IDENTIFIER);

        restLeaseAmortizationScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaseAmortizationSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaseAmortizationSchedule))
            )
            .andExpect(status().isOk());

        // Validate the LeaseAmortizationSchedule in the database
        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeUpdate);
        LeaseAmortizationSchedule testLeaseAmortizationSchedule = leaseAmortizationScheduleList.get(
            leaseAmortizationScheduleList.size() - 1
        );
        assertThat(testLeaseAmortizationSchedule.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingLeaseAmortizationSchedule() throws Exception {
        int databaseSizeBeforeUpdate = leaseAmortizationScheduleRepository.findAll().size();
        leaseAmortizationSchedule.setId(count.incrementAndGet());

        // Create the LeaseAmortizationSchedule
        LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO = leaseAmortizationScheduleMapper.toDto(leaseAmortizationSchedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaseAmortizationScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaseAmortizationScheduleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseAmortizationSchedule in the database
        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseAmortizationSchedule in Elasticsearch
        verify(mockLeaseAmortizationScheduleSearchRepository, times(0)).save(leaseAmortizationSchedule);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaseAmortizationSchedule() throws Exception {
        int databaseSizeBeforeUpdate = leaseAmortizationScheduleRepository.findAll().size();
        leaseAmortizationSchedule.setId(count.incrementAndGet());

        // Create the LeaseAmortizationSchedule
        LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO = leaseAmortizationScheduleMapper.toDto(leaseAmortizationSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseAmortizationScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationScheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaseAmortizationSchedule in the database
        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseAmortizationSchedule in Elasticsearch
        verify(mockLeaseAmortizationScheduleSearchRepository, times(0)).save(leaseAmortizationSchedule);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaseAmortizationSchedule() throws Exception {
        int databaseSizeBeforeUpdate = leaseAmortizationScheduleRepository.findAll().size();
        leaseAmortizationSchedule.setId(count.incrementAndGet());

        // Create the LeaseAmortizationSchedule
        LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO = leaseAmortizationScheduleMapper.toDto(leaseAmortizationSchedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaseAmortizationScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaseAmortizationScheduleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaseAmortizationSchedule in the database
        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeaseAmortizationSchedule in Elasticsearch
        verify(mockLeaseAmortizationScheduleSearchRepository, times(0)).save(leaseAmortizationSchedule);
    }

    @Test
    @Transactional
    void deleteLeaseAmortizationSchedule() throws Exception {
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);

        int databaseSizeBeforeDelete = leaseAmortizationScheduleRepository.findAll().size();

        // Delete the leaseAmortizationSchedule
        restLeaseAmortizationScheduleMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaseAmortizationSchedule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaseAmortizationSchedule> leaseAmortizationScheduleList = leaseAmortizationScheduleRepository.findAll();
        assertThat(leaseAmortizationScheduleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeaseAmortizationSchedule in Elasticsearch
        verify(mockLeaseAmortizationScheduleSearchRepository, times(1)).deleteById(leaseAmortizationSchedule.getId());
    }

    @Test
    @Transactional
    void searchLeaseAmortizationSchedule() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leaseAmortizationScheduleRepository.saveAndFlush(leaseAmortizationSchedule);
        when(mockLeaseAmortizationScheduleSearchRepository.search("id:" + leaseAmortizationSchedule.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leaseAmortizationSchedule), PageRequest.of(0, 1), 1));

        // Search the leaseAmortizationSchedule
        restLeaseAmortizationScheduleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leaseAmortizationSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaseAmortizationSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())));
    }
}
