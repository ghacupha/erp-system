package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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
import io.github.erp.domain.BankBranchCode;
import io.github.erp.domain.InstitutionCode;
import io.github.erp.domain.PartyRelationType;
import io.github.erp.domain.RelatedPartyRelationship;
import io.github.erp.repository.RelatedPartyRelationshipRepository;
import io.github.erp.repository.search.RelatedPartyRelationshipSearchRepository;
import io.github.erp.service.dto.RelatedPartyRelationshipDTO;
import io.github.erp.service.mapper.RelatedPartyRelationshipMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link RelatedPartyRelationshipResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class RelatedPartyRelationshipResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_RELATED_PARTY_ID = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_PARTY_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/related-party-relationships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/related-party-relationships";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RelatedPartyRelationshipRepository relatedPartyRelationshipRepository;

    @Autowired
    private RelatedPartyRelationshipMapper relatedPartyRelationshipMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.RelatedPartyRelationshipSearchRepositoryMockConfiguration
     */
    @Autowired
    private RelatedPartyRelationshipSearchRepository mockRelatedPartyRelationshipSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRelatedPartyRelationshipMockMvc;

    private RelatedPartyRelationship relatedPartyRelationship;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RelatedPartyRelationship createEntity(EntityManager em) {
        RelatedPartyRelationship relatedPartyRelationship = new RelatedPartyRelationship()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .customerId(DEFAULT_CUSTOMER_ID)
            .relatedPartyId(DEFAULT_RELATED_PARTY_ID);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        relatedPartyRelationship.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        relatedPartyRelationship.setBranchId(bankBranchCode);
        // Add required entity
        PartyRelationType partyRelationType;
        if (TestUtil.findAll(em, PartyRelationType.class).isEmpty()) {
            partyRelationType = PartyRelationTypeResourceIT.createEntity(em);
            em.persist(partyRelationType);
            em.flush();
        } else {
            partyRelationType = TestUtil.findAll(em, PartyRelationType.class).get(0);
        }
        relatedPartyRelationship.setRelationshipType(partyRelationType);
        return relatedPartyRelationship;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RelatedPartyRelationship createUpdatedEntity(EntityManager em) {
        RelatedPartyRelationship relatedPartyRelationship = new RelatedPartyRelationship()
            .reportingDate(UPDATED_REPORTING_DATE)
            .customerId(UPDATED_CUSTOMER_ID)
            .relatedPartyId(UPDATED_RELATED_PARTY_ID);
        // Add required entity
        InstitutionCode institutionCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            institutionCode = InstitutionCodeResourceIT.createUpdatedEntity(em);
            em.persist(institutionCode);
            em.flush();
        } else {
            institutionCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        relatedPartyRelationship.setBankCode(institutionCode);
        // Add required entity
        BankBranchCode bankBranchCode;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            bankBranchCode = BankBranchCodeResourceIT.createUpdatedEntity(em);
            em.persist(bankBranchCode);
            em.flush();
        } else {
            bankBranchCode = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        relatedPartyRelationship.setBranchId(bankBranchCode);
        // Add required entity
        PartyRelationType partyRelationType;
        if (TestUtil.findAll(em, PartyRelationType.class).isEmpty()) {
            partyRelationType = PartyRelationTypeResourceIT.createUpdatedEntity(em);
            em.persist(partyRelationType);
            em.flush();
        } else {
            partyRelationType = TestUtil.findAll(em, PartyRelationType.class).get(0);
        }
        relatedPartyRelationship.setRelationshipType(partyRelationType);
        return relatedPartyRelationship;
    }

    @BeforeEach
    public void initTest() {
        relatedPartyRelationship = createEntity(em);
    }

    @Test
    @Transactional
    void createRelatedPartyRelationship() throws Exception {
        int databaseSizeBeforeCreate = relatedPartyRelationshipRepository.findAll().size();
        // Create the RelatedPartyRelationship
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = relatedPartyRelationshipMapper.toDto(relatedPartyRelationship);
        restRelatedPartyRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatedPartyRelationshipDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RelatedPartyRelationship in the database
        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeCreate + 1);
        RelatedPartyRelationship testRelatedPartyRelationship = relatedPartyRelationshipList.get(relatedPartyRelationshipList.size() - 1);
        assertThat(testRelatedPartyRelationship.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testRelatedPartyRelationship.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testRelatedPartyRelationship.getRelatedPartyId()).isEqualTo(DEFAULT_RELATED_PARTY_ID);

        // Validate the RelatedPartyRelationship in Elasticsearch
        verify(mockRelatedPartyRelationshipSearchRepository, times(1)).save(testRelatedPartyRelationship);
    }

    @Test
    @Transactional
    void createRelatedPartyRelationshipWithExistingId() throws Exception {
        // Create the RelatedPartyRelationship with an existing ID
        relatedPartyRelationship.setId(1L);
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = relatedPartyRelationshipMapper.toDto(relatedPartyRelationship);

        int databaseSizeBeforeCreate = relatedPartyRelationshipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelatedPartyRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatedPartyRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelatedPartyRelationship in the database
        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeCreate);

        // Validate the RelatedPartyRelationship in Elasticsearch
        verify(mockRelatedPartyRelationshipSearchRepository, times(0)).save(relatedPartyRelationship);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedPartyRelationshipRepository.findAll().size();
        // set the field null
        relatedPartyRelationship.setReportingDate(null);

        // Create the RelatedPartyRelationship, which fails.
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = relatedPartyRelationshipMapper.toDto(relatedPartyRelationship);

        restRelatedPartyRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatedPartyRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCustomerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedPartyRelationshipRepository.findAll().size();
        // set the field null
        relatedPartyRelationship.setCustomerId(null);

        // Create the RelatedPartyRelationship, which fails.
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = relatedPartyRelationshipMapper.toDto(relatedPartyRelationship);

        restRelatedPartyRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatedPartyRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelatedPartyIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = relatedPartyRelationshipRepository.findAll().size();
        // set the field null
        relatedPartyRelationship.setRelatedPartyId(null);

        // Create the RelatedPartyRelationship, which fails.
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = relatedPartyRelationshipMapper.toDto(relatedPartyRelationship);

        restRelatedPartyRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatedPartyRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationships() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList
        restRelatedPartyRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatedPartyRelationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].relatedPartyId").value(hasItem(DEFAULT_RELATED_PARTY_ID)));
    }

    @Test
    @Transactional
    void getRelatedPartyRelationship() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get the relatedPartyRelationship
        restRelatedPartyRelationshipMockMvc
            .perform(get(ENTITY_API_URL_ID, relatedPartyRelationship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(relatedPartyRelationship.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
            .andExpect(jsonPath("$.relatedPartyId").value(DEFAULT_RELATED_PARTY_ID));
    }

    @Test
    @Transactional
    void getRelatedPartyRelationshipsByIdFiltering() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        Long id = relatedPartyRelationship.getId();

        defaultRelatedPartyRelationshipShouldBeFound("id.equals=" + id);
        defaultRelatedPartyRelationshipShouldNotBeFound("id.notEquals=" + id);

        defaultRelatedPartyRelationshipShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRelatedPartyRelationshipShouldNotBeFound("id.greaterThan=" + id);

        defaultRelatedPartyRelationshipShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRelatedPartyRelationshipShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the relatedPartyRelationshipList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the relatedPartyRelationshipList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the relatedPartyRelationshipList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where reportingDate is not null
        defaultRelatedPartyRelationshipShouldBeFound("reportingDate.specified=true");

        // Get all the relatedPartyRelationshipList where reportingDate is null
        defaultRelatedPartyRelationshipShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the relatedPartyRelationshipList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the relatedPartyRelationshipList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the relatedPartyRelationshipList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the relatedPartyRelationshipList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultRelatedPartyRelationshipShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultRelatedPartyRelationshipShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the relatedPartyRelationshipList where customerId equals to UPDATED_CUSTOMER_ID
        defaultRelatedPartyRelationshipShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByCustomerIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where customerId not equals to DEFAULT_CUSTOMER_ID
        defaultRelatedPartyRelationshipShouldNotBeFound("customerId.notEquals=" + DEFAULT_CUSTOMER_ID);

        // Get all the relatedPartyRelationshipList where customerId not equals to UPDATED_CUSTOMER_ID
        defaultRelatedPartyRelationshipShouldBeFound("customerId.notEquals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultRelatedPartyRelationshipShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the relatedPartyRelationshipList where customerId equals to UPDATED_CUSTOMER_ID
        defaultRelatedPartyRelationshipShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where customerId is not null
        defaultRelatedPartyRelationshipShouldBeFound("customerId.specified=true");

        // Get all the relatedPartyRelationshipList where customerId is null
        defaultRelatedPartyRelationshipShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByCustomerIdContainsSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where customerId contains DEFAULT_CUSTOMER_ID
        defaultRelatedPartyRelationshipShouldBeFound("customerId.contains=" + DEFAULT_CUSTOMER_ID);

        // Get all the relatedPartyRelationshipList where customerId contains UPDATED_CUSTOMER_ID
        defaultRelatedPartyRelationshipShouldNotBeFound("customerId.contains=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByCustomerIdNotContainsSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where customerId does not contain DEFAULT_CUSTOMER_ID
        defaultRelatedPartyRelationshipShouldNotBeFound("customerId.doesNotContain=" + DEFAULT_CUSTOMER_ID);

        // Get all the relatedPartyRelationshipList where customerId does not contain UPDATED_CUSTOMER_ID
        defaultRelatedPartyRelationshipShouldBeFound("customerId.doesNotContain=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByRelatedPartyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where relatedPartyId equals to DEFAULT_RELATED_PARTY_ID
        defaultRelatedPartyRelationshipShouldBeFound("relatedPartyId.equals=" + DEFAULT_RELATED_PARTY_ID);

        // Get all the relatedPartyRelationshipList where relatedPartyId equals to UPDATED_RELATED_PARTY_ID
        defaultRelatedPartyRelationshipShouldNotBeFound("relatedPartyId.equals=" + UPDATED_RELATED_PARTY_ID);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByRelatedPartyIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where relatedPartyId not equals to DEFAULT_RELATED_PARTY_ID
        defaultRelatedPartyRelationshipShouldNotBeFound("relatedPartyId.notEquals=" + DEFAULT_RELATED_PARTY_ID);

        // Get all the relatedPartyRelationshipList where relatedPartyId not equals to UPDATED_RELATED_PARTY_ID
        defaultRelatedPartyRelationshipShouldBeFound("relatedPartyId.notEquals=" + UPDATED_RELATED_PARTY_ID);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByRelatedPartyIdIsInShouldWork() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where relatedPartyId in DEFAULT_RELATED_PARTY_ID or UPDATED_RELATED_PARTY_ID
        defaultRelatedPartyRelationshipShouldBeFound("relatedPartyId.in=" + DEFAULT_RELATED_PARTY_ID + "," + UPDATED_RELATED_PARTY_ID);

        // Get all the relatedPartyRelationshipList where relatedPartyId equals to UPDATED_RELATED_PARTY_ID
        defaultRelatedPartyRelationshipShouldNotBeFound("relatedPartyId.in=" + UPDATED_RELATED_PARTY_ID);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByRelatedPartyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where relatedPartyId is not null
        defaultRelatedPartyRelationshipShouldBeFound("relatedPartyId.specified=true");

        // Get all the relatedPartyRelationshipList where relatedPartyId is null
        defaultRelatedPartyRelationshipShouldNotBeFound("relatedPartyId.specified=false");
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByRelatedPartyIdContainsSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where relatedPartyId contains DEFAULT_RELATED_PARTY_ID
        defaultRelatedPartyRelationshipShouldBeFound("relatedPartyId.contains=" + DEFAULT_RELATED_PARTY_ID);

        // Get all the relatedPartyRelationshipList where relatedPartyId contains UPDATED_RELATED_PARTY_ID
        defaultRelatedPartyRelationshipShouldNotBeFound("relatedPartyId.contains=" + UPDATED_RELATED_PARTY_ID);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByRelatedPartyIdNotContainsSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        // Get all the relatedPartyRelationshipList where relatedPartyId does not contain DEFAULT_RELATED_PARTY_ID
        defaultRelatedPartyRelationshipShouldNotBeFound("relatedPartyId.doesNotContain=" + DEFAULT_RELATED_PARTY_ID);

        // Get all the relatedPartyRelationshipList where relatedPartyId does not contain UPDATED_RELATED_PARTY_ID
        defaultRelatedPartyRelationshipShouldBeFound("relatedPartyId.doesNotContain=" + UPDATED_RELATED_PARTY_ID);
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);
        InstitutionCode bankCode;
        if (TestUtil.findAll(em, InstitutionCode.class).isEmpty()) {
            bankCode = InstitutionCodeResourceIT.createEntity(em);
            em.persist(bankCode);
            em.flush();
        } else {
            bankCode = TestUtil.findAll(em, InstitutionCode.class).get(0);
        }
        em.persist(bankCode);
        em.flush();
        relatedPartyRelationship.setBankCode(bankCode);
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);
        Long bankCodeId = bankCode.getId();

        // Get all the relatedPartyRelationshipList where bankCode equals to bankCodeId
        defaultRelatedPartyRelationshipShouldBeFound("bankCodeId.equals=" + bankCodeId);

        // Get all the relatedPartyRelationshipList where bankCode equals to (bankCodeId + 1)
        defaultRelatedPartyRelationshipShouldNotBeFound("bankCodeId.equals=" + (bankCodeId + 1));
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);
        BankBranchCode branchId;
        if (TestUtil.findAll(em, BankBranchCode.class).isEmpty()) {
            branchId = BankBranchCodeResourceIT.createEntity(em);
            em.persist(branchId);
            em.flush();
        } else {
            branchId = TestUtil.findAll(em, BankBranchCode.class).get(0);
        }
        em.persist(branchId);
        em.flush();
        relatedPartyRelationship.setBranchId(branchId);
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);
        Long branchIdId = branchId.getId();

        // Get all the relatedPartyRelationshipList where branchId equals to branchIdId
        defaultRelatedPartyRelationshipShouldBeFound("branchIdId.equals=" + branchIdId);

        // Get all the relatedPartyRelationshipList where branchId equals to (branchIdId + 1)
        defaultRelatedPartyRelationshipShouldNotBeFound("branchIdId.equals=" + (branchIdId + 1));
    }

    @Test
    @Transactional
    void getAllRelatedPartyRelationshipsByRelationshipTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);
        PartyRelationType relationshipType;
        if (TestUtil.findAll(em, PartyRelationType.class).isEmpty()) {
            relationshipType = PartyRelationTypeResourceIT.createEntity(em);
            em.persist(relationshipType);
            em.flush();
        } else {
            relationshipType = TestUtil.findAll(em, PartyRelationType.class).get(0);
        }
        em.persist(relationshipType);
        em.flush();
        relatedPartyRelationship.setRelationshipType(relationshipType);
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);
        Long relationshipTypeId = relationshipType.getId();

        // Get all the relatedPartyRelationshipList where relationshipType equals to relationshipTypeId
        defaultRelatedPartyRelationshipShouldBeFound("relationshipTypeId.equals=" + relationshipTypeId);

        // Get all the relatedPartyRelationshipList where relationshipType equals to (relationshipTypeId + 1)
        defaultRelatedPartyRelationshipShouldNotBeFound("relationshipTypeId.equals=" + (relationshipTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRelatedPartyRelationshipShouldBeFound(String filter) throws Exception {
        restRelatedPartyRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatedPartyRelationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].relatedPartyId").value(hasItem(DEFAULT_RELATED_PARTY_ID)));

        // Check, that the count call also returns 1
        restRelatedPartyRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRelatedPartyRelationshipShouldNotBeFound(String filter) throws Exception {
        restRelatedPartyRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRelatedPartyRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRelatedPartyRelationship() throws Exception {
        // Get the relatedPartyRelationship
        restRelatedPartyRelationshipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRelatedPartyRelationship() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        int databaseSizeBeforeUpdate = relatedPartyRelationshipRepository.findAll().size();

        // Update the relatedPartyRelationship
        RelatedPartyRelationship updatedRelatedPartyRelationship = relatedPartyRelationshipRepository
            .findById(relatedPartyRelationship.getId())
            .get();
        // Disconnect from session so that the updates on updatedRelatedPartyRelationship are not directly saved in db
        em.detach(updatedRelatedPartyRelationship);
        updatedRelatedPartyRelationship
            .reportingDate(UPDATED_REPORTING_DATE)
            .customerId(UPDATED_CUSTOMER_ID)
            .relatedPartyId(UPDATED_RELATED_PARTY_ID);
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = relatedPartyRelationshipMapper.toDto(updatedRelatedPartyRelationship);

        restRelatedPartyRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, relatedPartyRelationshipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatedPartyRelationshipDTO))
            )
            .andExpect(status().isOk());

        // Validate the RelatedPartyRelationship in the database
        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeUpdate);
        RelatedPartyRelationship testRelatedPartyRelationship = relatedPartyRelationshipList.get(relatedPartyRelationshipList.size() - 1);
        assertThat(testRelatedPartyRelationship.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testRelatedPartyRelationship.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testRelatedPartyRelationship.getRelatedPartyId()).isEqualTo(UPDATED_RELATED_PARTY_ID);

        // Validate the RelatedPartyRelationship in Elasticsearch
        verify(mockRelatedPartyRelationshipSearchRepository).save(testRelatedPartyRelationship);
    }

    @Test
    @Transactional
    void putNonExistingRelatedPartyRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relatedPartyRelationshipRepository.findAll().size();
        relatedPartyRelationship.setId(count.incrementAndGet());

        // Create the RelatedPartyRelationship
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = relatedPartyRelationshipMapper.toDto(relatedPartyRelationship);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelatedPartyRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, relatedPartyRelationshipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatedPartyRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelatedPartyRelationship in the database
        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RelatedPartyRelationship in Elasticsearch
        verify(mockRelatedPartyRelationshipSearchRepository, times(0)).save(relatedPartyRelationship);
    }

    @Test
    @Transactional
    void putWithIdMismatchRelatedPartyRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relatedPartyRelationshipRepository.findAll().size();
        relatedPartyRelationship.setId(count.incrementAndGet());

        // Create the RelatedPartyRelationship
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = relatedPartyRelationshipMapper.toDto(relatedPartyRelationship);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatedPartyRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatedPartyRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelatedPartyRelationship in the database
        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RelatedPartyRelationship in Elasticsearch
        verify(mockRelatedPartyRelationshipSearchRepository, times(0)).save(relatedPartyRelationship);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRelatedPartyRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relatedPartyRelationshipRepository.findAll().size();
        relatedPartyRelationship.setId(count.incrementAndGet());

        // Create the RelatedPartyRelationship
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = relatedPartyRelationshipMapper.toDto(relatedPartyRelationship);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatedPartyRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatedPartyRelationshipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RelatedPartyRelationship in the database
        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RelatedPartyRelationship in Elasticsearch
        verify(mockRelatedPartyRelationshipSearchRepository, times(0)).save(relatedPartyRelationship);
    }

    @Test
    @Transactional
    void partialUpdateRelatedPartyRelationshipWithPatch() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        int databaseSizeBeforeUpdate = relatedPartyRelationshipRepository.findAll().size();

        // Update the relatedPartyRelationship using partial update
        RelatedPartyRelationship partialUpdatedRelatedPartyRelationship = new RelatedPartyRelationship();
        partialUpdatedRelatedPartyRelationship.setId(relatedPartyRelationship.getId());

        restRelatedPartyRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelatedPartyRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelatedPartyRelationship))
            )
            .andExpect(status().isOk());

        // Validate the RelatedPartyRelationship in the database
        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeUpdate);
        RelatedPartyRelationship testRelatedPartyRelationship = relatedPartyRelationshipList.get(relatedPartyRelationshipList.size() - 1);
        assertThat(testRelatedPartyRelationship.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testRelatedPartyRelationship.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testRelatedPartyRelationship.getRelatedPartyId()).isEqualTo(DEFAULT_RELATED_PARTY_ID);
    }

    @Test
    @Transactional
    void fullUpdateRelatedPartyRelationshipWithPatch() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        int databaseSizeBeforeUpdate = relatedPartyRelationshipRepository.findAll().size();

        // Update the relatedPartyRelationship using partial update
        RelatedPartyRelationship partialUpdatedRelatedPartyRelationship = new RelatedPartyRelationship();
        partialUpdatedRelatedPartyRelationship.setId(relatedPartyRelationship.getId());

        partialUpdatedRelatedPartyRelationship
            .reportingDate(UPDATED_REPORTING_DATE)
            .customerId(UPDATED_CUSTOMER_ID)
            .relatedPartyId(UPDATED_RELATED_PARTY_ID);

        restRelatedPartyRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelatedPartyRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelatedPartyRelationship))
            )
            .andExpect(status().isOk());

        // Validate the RelatedPartyRelationship in the database
        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeUpdate);
        RelatedPartyRelationship testRelatedPartyRelationship = relatedPartyRelationshipList.get(relatedPartyRelationshipList.size() - 1);
        assertThat(testRelatedPartyRelationship.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testRelatedPartyRelationship.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testRelatedPartyRelationship.getRelatedPartyId()).isEqualTo(UPDATED_RELATED_PARTY_ID);
    }

    @Test
    @Transactional
    void patchNonExistingRelatedPartyRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relatedPartyRelationshipRepository.findAll().size();
        relatedPartyRelationship.setId(count.incrementAndGet());

        // Create the RelatedPartyRelationship
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = relatedPartyRelationshipMapper.toDto(relatedPartyRelationship);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelatedPartyRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, relatedPartyRelationshipDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relatedPartyRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelatedPartyRelationship in the database
        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RelatedPartyRelationship in Elasticsearch
        verify(mockRelatedPartyRelationshipSearchRepository, times(0)).save(relatedPartyRelationship);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRelatedPartyRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relatedPartyRelationshipRepository.findAll().size();
        relatedPartyRelationship.setId(count.incrementAndGet());

        // Create the RelatedPartyRelationship
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = relatedPartyRelationshipMapper.toDto(relatedPartyRelationship);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatedPartyRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relatedPartyRelationshipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RelatedPartyRelationship in the database
        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RelatedPartyRelationship in Elasticsearch
        verify(mockRelatedPartyRelationshipSearchRepository, times(0)).save(relatedPartyRelationship);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRelatedPartyRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relatedPartyRelationshipRepository.findAll().size();
        relatedPartyRelationship.setId(count.incrementAndGet());

        // Create the RelatedPartyRelationship
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO = relatedPartyRelationshipMapper.toDto(relatedPartyRelationship);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelatedPartyRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relatedPartyRelationshipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RelatedPartyRelationship in the database
        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RelatedPartyRelationship in Elasticsearch
        verify(mockRelatedPartyRelationshipSearchRepository, times(0)).save(relatedPartyRelationship);
    }

    @Test
    @Transactional
    void deleteRelatedPartyRelationship() throws Exception {
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);

        int databaseSizeBeforeDelete = relatedPartyRelationshipRepository.findAll().size();

        // Delete the relatedPartyRelationship
        restRelatedPartyRelationshipMockMvc
            .perform(delete(ENTITY_API_URL_ID, relatedPartyRelationship.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RelatedPartyRelationship> relatedPartyRelationshipList = relatedPartyRelationshipRepository.findAll();
        assertThat(relatedPartyRelationshipList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RelatedPartyRelationship in Elasticsearch
        verify(mockRelatedPartyRelationshipSearchRepository, times(1)).deleteById(relatedPartyRelationship.getId());
    }

    @Test
    @Transactional
    void searchRelatedPartyRelationship() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        relatedPartyRelationshipRepository.saveAndFlush(relatedPartyRelationship);
        when(mockRelatedPartyRelationshipSearchRepository.search("id:" + relatedPartyRelationship.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(relatedPartyRelationship), PageRequest.of(0, 1), 1));

        // Search the relatedPartyRelationship
        restRelatedPartyRelationshipMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + relatedPartyRelationship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatedPartyRelationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
            .andExpect(jsonPath("$.[*].relatedPartyId").value(hasItem(DEFAULT_RELATED_PARTY_ID)));
    }
}
