package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import io.github.erp.domain.LoanPerformanceClassification;
import io.github.erp.repository.LoanPerformanceClassificationRepository;
import io.github.erp.repository.search.LoanPerformanceClassificationSearchRepository;
import io.github.erp.service.dto.LoanPerformanceClassificationDTO;
import io.github.erp.service.mapper.LoanPerformanceClassificationMapper;
import io.github.erp.web.rest.LoanPerformanceClassificationResource;
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
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LoanPerformanceClassificationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class LoanPerformanceClassificationResourceIT {

    private static final String DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMERCIAL_BANK_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_COMMERCIAL_BANK_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MICROFINANCE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_MICROFINANCE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/loan-performance-classifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/loan-performance-classifications";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LoanPerformanceClassificationRepository loanPerformanceClassificationRepository;

    @Autowired
    private LoanPerformanceClassificationMapper loanPerformanceClassificationMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LoanPerformanceClassificationSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoanPerformanceClassificationSearchRepository mockLoanPerformanceClassificationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoanPerformanceClassificationMockMvc;

    private LoanPerformanceClassification loanPerformanceClassification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanPerformanceClassification createEntity(EntityManager em) {
        LoanPerformanceClassification loanPerformanceClassification = new LoanPerformanceClassification()
            .loanPerformanceClassificationCode(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE)
            .loanPerformanceClassificationType(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE)
            .commercialBankDescription(DEFAULT_COMMERCIAL_BANK_DESCRIPTION)
            .microfinanceDescription(DEFAULT_MICROFINANCE_DESCRIPTION);
        return loanPerformanceClassification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanPerformanceClassification createUpdatedEntity(EntityManager em) {
        LoanPerformanceClassification loanPerformanceClassification = new LoanPerformanceClassification()
            .loanPerformanceClassificationCode(UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE)
            .loanPerformanceClassificationType(UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE)
            .commercialBankDescription(UPDATED_COMMERCIAL_BANK_DESCRIPTION)
            .microfinanceDescription(UPDATED_MICROFINANCE_DESCRIPTION);
        return loanPerformanceClassification;
    }

    @BeforeEach
    public void initTest() {
        loanPerformanceClassification = createEntity(em);
    }

    @Test
    @Transactional
    void createLoanPerformanceClassification() throws Exception {
        int databaseSizeBeforeCreate = loanPerformanceClassificationRepository.findAll().size();
        // Create the LoanPerformanceClassification
        LoanPerformanceClassificationDTO loanPerformanceClassificationDTO = loanPerformanceClassificationMapper.toDto(
            loanPerformanceClassification
        );
        restLoanPerformanceClassificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanPerformanceClassificationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LoanPerformanceClassification in the database
        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeCreate + 1);
        LoanPerformanceClassification testLoanPerformanceClassification = loanPerformanceClassificationList.get(
            loanPerformanceClassificationList.size() - 1
        );
        assertThat(testLoanPerformanceClassification.getLoanPerformanceClassificationCode())
            .isEqualTo(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE);
        assertThat(testLoanPerformanceClassification.getLoanPerformanceClassificationType())
            .isEqualTo(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE);
        assertThat(testLoanPerformanceClassification.getCommercialBankDescription()).isEqualTo(DEFAULT_COMMERCIAL_BANK_DESCRIPTION);
        assertThat(testLoanPerformanceClassification.getMicrofinanceDescription()).isEqualTo(DEFAULT_MICROFINANCE_DESCRIPTION);

        // Validate the LoanPerformanceClassification in Elasticsearch
        verify(mockLoanPerformanceClassificationSearchRepository, times(1)).save(testLoanPerformanceClassification);
    }

    @Test
    @Transactional
    void createLoanPerformanceClassificationWithExistingId() throws Exception {
        // Create the LoanPerformanceClassification with an existing ID
        loanPerformanceClassification.setId(1L);
        LoanPerformanceClassificationDTO loanPerformanceClassificationDTO = loanPerformanceClassificationMapper.toDto(
            loanPerformanceClassification
        );

        int databaseSizeBeforeCreate = loanPerformanceClassificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanPerformanceClassificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanPerformanceClassificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanPerformanceClassification in the database
        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoanPerformanceClassification in Elasticsearch
        verify(mockLoanPerformanceClassificationSearchRepository, times(0)).save(loanPerformanceClassification);
    }

    @Test
    @Transactional
    void checkLoanPerformanceClassificationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanPerformanceClassificationRepository.findAll().size();
        // set the field null
        loanPerformanceClassification.setLoanPerformanceClassificationCode(null);

        // Create the LoanPerformanceClassification, which fails.
        LoanPerformanceClassificationDTO loanPerformanceClassificationDTO = loanPerformanceClassificationMapper.toDto(
            loanPerformanceClassification
        );

        restLoanPerformanceClassificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanPerformanceClassificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoanPerformanceClassificationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanPerformanceClassificationRepository.findAll().size();
        // set the field null
        loanPerformanceClassification.setLoanPerformanceClassificationType(null);

        // Create the LoanPerformanceClassification, which fails.
        LoanPerformanceClassificationDTO loanPerformanceClassificationDTO = loanPerformanceClassificationMapper.toDto(
            loanPerformanceClassification
        );

        restLoanPerformanceClassificationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanPerformanceClassificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassifications() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList
        restLoanPerformanceClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanPerformanceClassification.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanPerformanceClassificationCode").value(hasItem(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE)))
            .andExpect(jsonPath("$.[*].loanPerformanceClassificationType").value(hasItem(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE)))
            .andExpect(jsonPath("$.[*].commercialBankDescription").value(hasItem(DEFAULT_COMMERCIAL_BANK_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].microfinanceDescription").value(hasItem(DEFAULT_MICROFINANCE_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getLoanPerformanceClassification() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get the loanPerformanceClassification
        restLoanPerformanceClassificationMockMvc
            .perform(get(ENTITY_API_URL_ID, loanPerformanceClassification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loanPerformanceClassification.getId().intValue()))
            .andExpect(jsonPath("$.loanPerformanceClassificationCode").value(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE))
            .andExpect(jsonPath("$.loanPerformanceClassificationType").value(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE))
            .andExpect(jsonPath("$.commercialBankDescription").value(DEFAULT_COMMERCIAL_BANK_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.microfinanceDescription").value(DEFAULT_MICROFINANCE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getLoanPerformanceClassificationsByIdFiltering() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        Long id = loanPerformanceClassification.getId();

        defaultLoanPerformanceClassificationShouldBeFound("id.equals=" + id);
        defaultLoanPerformanceClassificationShouldNotBeFound("id.notEquals=" + id);

        defaultLoanPerformanceClassificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoanPerformanceClassificationShouldNotBeFound("id.greaterThan=" + id);

        defaultLoanPerformanceClassificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoanPerformanceClassificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassificationsByLoanPerformanceClassificationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationCode equals to DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        defaultLoanPerformanceClassificationShouldBeFound(
            "loanPerformanceClassificationCode.equals=" + DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        );

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationCode equals to UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        defaultLoanPerformanceClassificationShouldNotBeFound(
            "loanPerformanceClassificationCode.equals=" + UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        );
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassificationsByLoanPerformanceClassificationCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationCode not equals to DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        defaultLoanPerformanceClassificationShouldNotBeFound(
            "loanPerformanceClassificationCode.notEquals=" + DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        );

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationCode not equals to UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        defaultLoanPerformanceClassificationShouldBeFound(
            "loanPerformanceClassificationCode.notEquals=" + UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        );
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassificationsByLoanPerformanceClassificationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationCode in DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE or UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        defaultLoanPerformanceClassificationShouldBeFound(
            "loanPerformanceClassificationCode.in=" +
            DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE +
            "," +
            UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        );

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationCode equals to UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        defaultLoanPerformanceClassificationShouldNotBeFound(
            "loanPerformanceClassificationCode.in=" + UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        );
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassificationsByLoanPerformanceClassificationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationCode is not null
        defaultLoanPerformanceClassificationShouldBeFound("loanPerformanceClassificationCode.specified=true");

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationCode is null
        defaultLoanPerformanceClassificationShouldNotBeFound("loanPerformanceClassificationCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassificationsByLoanPerformanceClassificationCodeContainsSomething() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationCode contains DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        defaultLoanPerformanceClassificationShouldBeFound(
            "loanPerformanceClassificationCode.contains=" + DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        );

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationCode contains UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        defaultLoanPerformanceClassificationShouldNotBeFound(
            "loanPerformanceClassificationCode.contains=" + UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        );
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassificationsByLoanPerformanceClassificationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationCode does not contain DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        defaultLoanPerformanceClassificationShouldNotBeFound(
            "loanPerformanceClassificationCode.doesNotContain=" + DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        );

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationCode does not contain UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        defaultLoanPerformanceClassificationShouldBeFound(
            "loanPerformanceClassificationCode.doesNotContain=" + UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE
        );
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassificationsByLoanPerformanceClassificationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationType equals to DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        defaultLoanPerformanceClassificationShouldBeFound(
            "loanPerformanceClassificationType.equals=" + DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        );

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationType equals to UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        defaultLoanPerformanceClassificationShouldNotBeFound(
            "loanPerformanceClassificationType.equals=" + UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassificationsByLoanPerformanceClassificationTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationType not equals to DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        defaultLoanPerformanceClassificationShouldNotBeFound(
            "loanPerformanceClassificationType.notEquals=" + DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        );

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationType not equals to UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        defaultLoanPerformanceClassificationShouldBeFound(
            "loanPerformanceClassificationType.notEquals=" + UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassificationsByLoanPerformanceClassificationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationType in DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE or UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        defaultLoanPerformanceClassificationShouldBeFound(
            "loanPerformanceClassificationType.in=" +
            DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE +
            "," +
            UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        );

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationType equals to UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        defaultLoanPerformanceClassificationShouldNotBeFound(
            "loanPerformanceClassificationType.in=" + UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassificationsByLoanPerformanceClassificationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationType is not null
        defaultLoanPerformanceClassificationShouldBeFound("loanPerformanceClassificationType.specified=true");

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationType is null
        defaultLoanPerformanceClassificationShouldNotBeFound("loanPerformanceClassificationType.specified=false");
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassificationsByLoanPerformanceClassificationTypeContainsSomething() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationType contains DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        defaultLoanPerformanceClassificationShouldBeFound(
            "loanPerformanceClassificationType.contains=" + DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        );

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationType contains UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        defaultLoanPerformanceClassificationShouldNotBeFound(
            "loanPerformanceClassificationType.contains=" + UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllLoanPerformanceClassificationsByLoanPerformanceClassificationTypeNotContainsSomething() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationType does not contain DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        defaultLoanPerformanceClassificationShouldNotBeFound(
            "loanPerformanceClassificationType.doesNotContain=" + DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        );

        // Get all the loanPerformanceClassificationList where loanPerformanceClassificationType does not contain UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        defaultLoanPerformanceClassificationShouldBeFound(
            "loanPerformanceClassificationType.doesNotContain=" + UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoanPerformanceClassificationShouldBeFound(String filter) throws Exception {
        restLoanPerformanceClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanPerformanceClassification.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanPerformanceClassificationCode").value(hasItem(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE)))
            .andExpect(jsonPath("$.[*].loanPerformanceClassificationType").value(hasItem(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE)))
            .andExpect(jsonPath("$.[*].commercialBankDescription").value(hasItem(DEFAULT_COMMERCIAL_BANK_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].microfinanceDescription").value(hasItem(DEFAULT_MICROFINANCE_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restLoanPerformanceClassificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoanPerformanceClassificationShouldNotBeFound(String filter) throws Exception {
        restLoanPerformanceClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoanPerformanceClassificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLoanPerformanceClassification() throws Exception {
        // Get the loanPerformanceClassification
        restLoanPerformanceClassificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLoanPerformanceClassification() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        int databaseSizeBeforeUpdate = loanPerformanceClassificationRepository.findAll().size();

        // Update the loanPerformanceClassification
        LoanPerformanceClassification updatedLoanPerformanceClassification = loanPerformanceClassificationRepository
            .findById(loanPerformanceClassification.getId())
            .get();
        // Disconnect from session so that the updates on updatedLoanPerformanceClassification are not directly saved in db
        em.detach(updatedLoanPerformanceClassification);
        updatedLoanPerformanceClassification
            .loanPerformanceClassificationCode(UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE)
            .loanPerformanceClassificationType(UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE)
            .commercialBankDescription(UPDATED_COMMERCIAL_BANK_DESCRIPTION)
            .microfinanceDescription(UPDATED_MICROFINANCE_DESCRIPTION);
        LoanPerformanceClassificationDTO loanPerformanceClassificationDTO = loanPerformanceClassificationMapper.toDto(
            updatedLoanPerformanceClassification
        );

        restLoanPerformanceClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanPerformanceClassificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanPerformanceClassificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the LoanPerformanceClassification in the database
        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeUpdate);
        LoanPerformanceClassification testLoanPerformanceClassification = loanPerformanceClassificationList.get(
            loanPerformanceClassificationList.size() - 1
        );
        assertThat(testLoanPerformanceClassification.getLoanPerformanceClassificationCode())
            .isEqualTo(UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE);
        assertThat(testLoanPerformanceClassification.getLoanPerformanceClassificationType())
            .isEqualTo(UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE);
        assertThat(testLoanPerformanceClassification.getCommercialBankDescription()).isEqualTo(UPDATED_COMMERCIAL_BANK_DESCRIPTION);
        assertThat(testLoanPerformanceClassification.getMicrofinanceDescription()).isEqualTo(UPDATED_MICROFINANCE_DESCRIPTION);

        // Validate the LoanPerformanceClassification in Elasticsearch
        verify(mockLoanPerformanceClassificationSearchRepository).save(testLoanPerformanceClassification);
    }

    @Test
    @Transactional
    void putNonExistingLoanPerformanceClassification() throws Exception {
        int databaseSizeBeforeUpdate = loanPerformanceClassificationRepository.findAll().size();
        loanPerformanceClassification.setId(count.incrementAndGet());

        // Create the LoanPerformanceClassification
        LoanPerformanceClassificationDTO loanPerformanceClassificationDTO = loanPerformanceClassificationMapper.toDto(
            loanPerformanceClassification
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanPerformanceClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, loanPerformanceClassificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanPerformanceClassificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanPerformanceClassification in the database
        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanPerformanceClassification in Elasticsearch
        verify(mockLoanPerformanceClassificationSearchRepository, times(0)).save(loanPerformanceClassification);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoanPerformanceClassification() throws Exception {
        int databaseSizeBeforeUpdate = loanPerformanceClassificationRepository.findAll().size();
        loanPerformanceClassification.setId(count.incrementAndGet());

        // Create the LoanPerformanceClassification
        LoanPerformanceClassificationDTO loanPerformanceClassificationDTO = loanPerformanceClassificationMapper.toDto(
            loanPerformanceClassification
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanPerformanceClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanPerformanceClassificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanPerformanceClassification in the database
        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanPerformanceClassification in Elasticsearch
        verify(mockLoanPerformanceClassificationSearchRepository, times(0)).save(loanPerformanceClassification);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoanPerformanceClassification() throws Exception {
        int databaseSizeBeforeUpdate = loanPerformanceClassificationRepository.findAll().size();
        loanPerformanceClassification.setId(count.incrementAndGet());

        // Create the LoanPerformanceClassification
        LoanPerformanceClassificationDTO loanPerformanceClassificationDTO = loanPerformanceClassificationMapper.toDto(
            loanPerformanceClassification
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanPerformanceClassificationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(loanPerformanceClassificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanPerformanceClassification in the database
        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanPerformanceClassification in Elasticsearch
        verify(mockLoanPerformanceClassificationSearchRepository, times(0)).save(loanPerformanceClassification);
    }

    @Test
    @Transactional
    void partialUpdateLoanPerformanceClassificationWithPatch() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        int databaseSizeBeforeUpdate = loanPerformanceClassificationRepository.findAll().size();

        // Update the loanPerformanceClassification using partial update
        LoanPerformanceClassification partialUpdatedLoanPerformanceClassification = new LoanPerformanceClassification();
        partialUpdatedLoanPerformanceClassification.setId(loanPerformanceClassification.getId());

        partialUpdatedLoanPerformanceClassification
            .loanPerformanceClassificationCode(UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE)
            .commercialBankDescription(UPDATED_COMMERCIAL_BANK_DESCRIPTION);

        restLoanPerformanceClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanPerformanceClassification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanPerformanceClassification))
            )
            .andExpect(status().isOk());

        // Validate the LoanPerformanceClassification in the database
        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeUpdate);
        LoanPerformanceClassification testLoanPerformanceClassification = loanPerformanceClassificationList.get(
            loanPerformanceClassificationList.size() - 1
        );
        assertThat(testLoanPerformanceClassification.getLoanPerformanceClassificationCode())
            .isEqualTo(UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE);
        assertThat(testLoanPerformanceClassification.getLoanPerformanceClassificationType())
            .isEqualTo(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE);
        assertThat(testLoanPerformanceClassification.getCommercialBankDescription()).isEqualTo(UPDATED_COMMERCIAL_BANK_DESCRIPTION);
        assertThat(testLoanPerformanceClassification.getMicrofinanceDescription()).isEqualTo(DEFAULT_MICROFINANCE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateLoanPerformanceClassificationWithPatch() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        int databaseSizeBeforeUpdate = loanPerformanceClassificationRepository.findAll().size();

        // Update the loanPerformanceClassification using partial update
        LoanPerformanceClassification partialUpdatedLoanPerformanceClassification = new LoanPerformanceClassification();
        partialUpdatedLoanPerformanceClassification.setId(loanPerformanceClassification.getId());

        partialUpdatedLoanPerformanceClassification
            .loanPerformanceClassificationCode(UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE)
            .loanPerformanceClassificationType(UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE)
            .commercialBankDescription(UPDATED_COMMERCIAL_BANK_DESCRIPTION)
            .microfinanceDescription(UPDATED_MICROFINANCE_DESCRIPTION);

        restLoanPerformanceClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoanPerformanceClassification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLoanPerformanceClassification))
            )
            .andExpect(status().isOk());

        // Validate the LoanPerformanceClassification in the database
        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeUpdate);
        LoanPerformanceClassification testLoanPerformanceClassification = loanPerformanceClassificationList.get(
            loanPerformanceClassificationList.size() - 1
        );
        assertThat(testLoanPerformanceClassification.getLoanPerformanceClassificationCode())
            .isEqualTo(UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_CODE);
        assertThat(testLoanPerformanceClassification.getLoanPerformanceClassificationType())
            .isEqualTo(UPDATED_LOAN_PERFORMANCE_CLASSIFICATION_TYPE);
        assertThat(testLoanPerformanceClassification.getCommercialBankDescription()).isEqualTo(UPDATED_COMMERCIAL_BANK_DESCRIPTION);
        assertThat(testLoanPerformanceClassification.getMicrofinanceDescription()).isEqualTo(UPDATED_MICROFINANCE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingLoanPerformanceClassification() throws Exception {
        int databaseSizeBeforeUpdate = loanPerformanceClassificationRepository.findAll().size();
        loanPerformanceClassification.setId(count.incrementAndGet());

        // Create the LoanPerformanceClassification
        LoanPerformanceClassificationDTO loanPerformanceClassificationDTO = loanPerformanceClassificationMapper.toDto(
            loanPerformanceClassification
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanPerformanceClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loanPerformanceClassificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanPerformanceClassificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanPerformanceClassification in the database
        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanPerformanceClassification in Elasticsearch
        verify(mockLoanPerformanceClassificationSearchRepository, times(0)).save(loanPerformanceClassification);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoanPerformanceClassification() throws Exception {
        int databaseSizeBeforeUpdate = loanPerformanceClassificationRepository.findAll().size();
        loanPerformanceClassification.setId(count.incrementAndGet());

        // Create the LoanPerformanceClassification
        LoanPerformanceClassificationDTO loanPerformanceClassificationDTO = loanPerformanceClassificationMapper.toDto(
            loanPerformanceClassification
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanPerformanceClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanPerformanceClassificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoanPerformanceClassification in the database
        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanPerformanceClassification in Elasticsearch
        verify(mockLoanPerformanceClassificationSearchRepository, times(0)).save(loanPerformanceClassification);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoanPerformanceClassification() throws Exception {
        int databaseSizeBeforeUpdate = loanPerformanceClassificationRepository.findAll().size();
        loanPerformanceClassification.setId(count.incrementAndGet());

        // Create the LoanPerformanceClassification
        LoanPerformanceClassificationDTO loanPerformanceClassificationDTO = loanPerformanceClassificationMapper.toDto(
            loanPerformanceClassification
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoanPerformanceClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(loanPerformanceClassificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoanPerformanceClassification in the database
        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanPerformanceClassification in Elasticsearch
        verify(mockLoanPerformanceClassificationSearchRepository, times(0)).save(loanPerformanceClassification);
    }

    @Test
    @Transactional
    void deleteLoanPerformanceClassification() throws Exception {
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);

        int databaseSizeBeforeDelete = loanPerformanceClassificationRepository.findAll().size();

        // Delete the loanPerformanceClassification
        restLoanPerformanceClassificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, loanPerformanceClassification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LoanPerformanceClassification> loanPerformanceClassificationList = loanPerformanceClassificationRepository.findAll();
        assertThat(loanPerformanceClassificationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoanPerformanceClassification in Elasticsearch
        verify(mockLoanPerformanceClassificationSearchRepository, times(1)).deleteById(loanPerformanceClassification.getId());
    }

    @Test
    @Transactional
    void searchLoanPerformanceClassification() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        loanPerformanceClassificationRepository.saveAndFlush(loanPerformanceClassification);
        when(mockLoanPerformanceClassificationSearchRepository.search("id:" + loanPerformanceClassification.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(loanPerformanceClassification), PageRequest.of(0, 1), 1));

        // Search the loanPerformanceClassification
        restLoanPerformanceClassificationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + loanPerformanceClassification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanPerformanceClassification.getId().intValue())))
            .andExpect(jsonPath("$.[*].loanPerformanceClassificationCode").value(hasItem(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_CODE)))
            .andExpect(jsonPath("$.[*].loanPerformanceClassificationType").value(hasItem(DEFAULT_LOAN_PERFORMANCE_CLASSIFICATION_TYPE)))
            .andExpect(jsonPath("$.[*].commercialBankDescription").value(hasItem(DEFAULT_COMMERCIAL_BANK_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].microfinanceDescription").value(hasItem(DEFAULT_MICROFINANCE_DESCRIPTION.toString())));
    }
}
