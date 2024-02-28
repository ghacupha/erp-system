package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.domain.CrbReportRequestReasons;
import io.github.erp.repository.CrbReportRequestReasonsRepository;
import io.github.erp.repository.search.CrbReportRequestReasonsSearchRepository;
import io.github.erp.service.dto.CrbReportRequestReasonsDTO;
import io.github.erp.service.mapper.CrbReportRequestReasonsMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.CrbReportRequestReasonsResource;
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

/**
 * Integration tests for the {@link CrbReportRequestReasonsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CrbReportRequestReasonsResourceIT {

    private static final String DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CREDIT_REPORT_REQUEST_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_REPORT_REQUEST_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/crb-report-request-reasons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/crb-report-request-reasons";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbReportRequestReasonsRepository crbReportRequestReasonsRepository;

    @Autowired
    private CrbReportRequestReasonsMapper crbReportRequestReasonsMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbReportRequestReasonsSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbReportRequestReasonsSearchRepository mockCrbReportRequestReasonsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbReportRequestReasonsMockMvc;

    private CrbReportRequestReasons crbReportRequestReasons;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbReportRequestReasons createEntity(EntityManager em) {
        CrbReportRequestReasons crbReportRequestReasons = new CrbReportRequestReasons()
            .creditReportRequestReasonTypeCode(DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE)
            .creditReportRequestReasonType(DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE)
            .creditReportRequestDetails(DEFAULT_CREDIT_REPORT_REQUEST_DETAILS);
        return crbReportRequestReasons;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbReportRequestReasons createUpdatedEntity(EntityManager em) {
        CrbReportRequestReasons crbReportRequestReasons = new CrbReportRequestReasons()
            .creditReportRequestReasonTypeCode(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE)
            .creditReportRequestReasonType(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE)
            .creditReportRequestDetails(UPDATED_CREDIT_REPORT_REQUEST_DETAILS);
        return crbReportRequestReasons;
    }

    @BeforeEach
    public void initTest() {
        crbReportRequestReasons = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbReportRequestReasons() throws Exception {
        int databaseSizeBeforeCreate = crbReportRequestReasonsRepository.findAll().size();
        // Create the CrbReportRequestReasons
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO = crbReportRequestReasonsMapper.toDto(crbReportRequestReasons);
        restCrbReportRequestReasonsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportRequestReasonsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbReportRequestReasons in the database
        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeCreate + 1);
        CrbReportRequestReasons testCrbReportRequestReasons = crbReportRequestReasonsList.get(crbReportRequestReasonsList.size() - 1);
        assertThat(testCrbReportRequestReasons.getCreditReportRequestReasonTypeCode())
            .isEqualTo(DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE);
        assertThat(testCrbReportRequestReasons.getCreditReportRequestReasonType()).isEqualTo(DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE);
        assertThat(testCrbReportRequestReasons.getCreditReportRequestDetails()).isEqualTo(DEFAULT_CREDIT_REPORT_REQUEST_DETAILS);

        // Validate the CrbReportRequestReasons in Elasticsearch
        verify(mockCrbReportRequestReasonsSearchRepository, times(1)).save(testCrbReportRequestReasons);
    }

    @Test
    @Transactional
    void createCrbReportRequestReasonsWithExistingId() throws Exception {
        // Create the CrbReportRequestReasons with an existing ID
        crbReportRequestReasons.setId(1L);
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO = crbReportRequestReasonsMapper.toDto(crbReportRequestReasons);

        int databaseSizeBeforeCreate = crbReportRequestReasonsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbReportRequestReasonsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportRequestReasonsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbReportRequestReasons in the database
        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbReportRequestReasons in Elasticsearch
        verify(mockCrbReportRequestReasonsSearchRepository, times(0)).save(crbReportRequestReasons);
    }

    @Test
    @Transactional
    void checkCreditReportRequestReasonTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbReportRequestReasonsRepository.findAll().size();
        // set the field null
        crbReportRequestReasons.setCreditReportRequestReasonTypeCode(null);

        // Create the CrbReportRequestReasons, which fails.
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO = crbReportRequestReasonsMapper.toDto(crbReportRequestReasons);

        restCrbReportRequestReasonsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportRequestReasonsDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreditReportRequestReasonTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbReportRequestReasonsRepository.findAll().size();
        // set the field null
        crbReportRequestReasons.setCreditReportRequestReasonType(null);

        // Create the CrbReportRequestReasons, which fails.
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO = crbReportRequestReasonsMapper.toDto(crbReportRequestReasons);

        restCrbReportRequestReasonsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportRequestReasonsDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasons() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList
        restCrbReportRequestReasonsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbReportRequestReasons.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditReportRequestReasonTypeCode").value(hasItem(DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].creditReportRequestReasonType").value(hasItem(DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE)))
            .andExpect(jsonPath("$.[*].creditReportRequestDetails").value(hasItem(DEFAULT_CREDIT_REPORT_REQUEST_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCrbReportRequestReasons() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get the crbReportRequestReasons
        restCrbReportRequestReasonsMockMvc
            .perform(get(ENTITY_API_URL_ID, crbReportRequestReasons.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbReportRequestReasons.getId().intValue()))
            .andExpect(jsonPath("$.creditReportRequestReasonTypeCode").value(DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE))
            .andExpect(jsonPath("$.creditReportRequestReasonType").value(DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE))
            .andExpect(jsonPath("$.creditReportRequestDetails").value(DEFAULT_CREDIT_REPORT_REQUEST_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCrbReportRequestReasonsByIdFiltering() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        Long id = crbReportRequestReasons.getId();

        defaultCrbReportRequestReasonsShouldBeFound("id.equals=" + id);
        defaultCrbReportRequestReasonsShouldNotBeFound("id.notEquals=" + id);

        defaultCrbReportRequestReasonsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbReportRequestReasonsShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbReportRequestReasonsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbReportRequestReasonsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasonsByCreditReportRequestReasonTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonTypeCode equals to DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        defaultCrbReportRequestReasonsShouldBeFound(
            "creditReportRequestReasonTypeCode.equals=" + DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        );

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonTypeCode equals to UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        defaultCrbReportRequestReasonsShouldNotBeFound(
            "creditReportRequestReasonTypeCode.equals=" + UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasonsByCreditReportRequestReasonTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonTypeCode not equals to DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        defaultCrbReportRequestReasonsShouldNotBeFound(
            "creditReportRequestReasonTypeCode.notEquals=" + DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        );

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonTypeCode not equals to UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        defaultCrbReportRequestReasonsShouldBeFound(
            "creditReportRequestReasonTypeCode.notEquals=" + UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasonsByCreditReportRequestReasonTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonTypeCode in DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE or UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        defaultCrbReportRequestReasonsShouldBeFound(
            "creditReportRequestReasonTypeCode.in=" +
            DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE +
            "," +
            UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        );

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonTypeCode equals to UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        defaultCrbReportRequestReasonsShouldNotBeFound(
            "creditReportRequestReasonTypeCode.in=" + UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasonsByCreditReportRequestReasonTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonTypeCode is not null
        defaultCrbReportRequestReasonsShouldBeFound("creditReportRequestReasonTypeCode.specified=true");

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonTypeCode is null
        defaultCrbReportRequestReasonsShouldNotBeFound("creditReportRequestReasonTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasonsByCreditReportRequestReasonTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonTypeCode contains DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        defaultCrbReportRequestReasonsShouldBeFound(
            "creditReportRequestReasonTypeCode.contains=" + DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        );

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonTypeCode contains UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        defaultCrbReportRequestReasonsShouldNotBeFound(
            "creditReportRequestReasonTypeCode.contains=" + UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasonsByCreditReportRequestReasonTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonTypeCode does not contain DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        defaultCrbReportRequestReasonsShouldNotBeFound(
            "creditReportRequestReasonTypeCode.doesNotContain=" + DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        );

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonTypeCode does not contain UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        defaultCrbReportRequestReasonsShouldBeFound(
            "creditReportRequestReasonTypeCode.doesNotContain=" + UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasonsByCreditReportRequestReasonTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonType equals to DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE
        defaultCrbReportRequestReasonsShouldBeFound("creditReportRequestReasonType.equals=" + DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonType equals to UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE
        defaultCrbReportRequestReasonsShouldNotBeFound("creditReportRequestReasonType.equals=" + UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasonsByCreditReportRequestReasonTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonType not equals to DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE
        defaultCrbReportRequestReasonsShouldNotBeFound(
            "creditReportRequestReasonType.notEquals=" + DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE
        );

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonType not equals to UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE
        defaultCrbReportRequestReasonsShouldBeFound("creditReportRequestReasonType.notEquals=" + UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasonsByCreditReportRequestReasonTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonType in DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE or UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE
        defaultCrbReportRequestReasonsShouldBeFound(
            "creditReportRequestReasonType.in=" +
            DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE +
            "," +
            UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE
        );

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonType equals to UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE
        defaultCrbReportRequestReasonsShouldNotBeFound("creditReportRequestReasonType.in=" + UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasonsByCreditReportRequestReasonTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonType is not null
        defaultCrbReportRequestReasonsShouldBeFound("creditReportRequestReasonType.specified=true");

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonType is null
        defaultCrbReportRequestReasonsShouldNotBeFound("creditReportRequestReasonType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasonsByCreditReportRequestReasonTypeContainsSomething() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonType contains DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE
        defaultCrbReportRequestReasonsShouldBeFound("creditReportRequestReasonType.contains=" + DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonType contains UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE
        defaultCrbReportRequestReasonsShouldNotBeFound(
            "creditReportRequestReasonType.contains=" + UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE
        );
    }

    @Test
    @Transactional
    void getAllCrbReportRequestReasonsByCreditReportRequestReasonTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonType does not contain DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE
        defaultCrbReportRequestReasonsShouldNotBeFound(
            "creditReportRequestReasonType.doesNotContain=" + DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE
        );

        // Get all the crbReportRequestReasonsList where creditReportRequestReasonType does not contain UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE
        defaultCrbReportRequestReasonsShouldBeFound(
            "creditReportRequestReasonType.doesNotContain=" + UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbReportRequestReasonsShouldBeFound(String filter) throws Exception {
        restCrbReportRequestReasonsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbReportRequestReasons.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditReportRequestReasonTypeCode").value(hasItem(DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].creditReportRequestReasonType").value(hasItem(DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE)))
            .andExpect(jsonPath("$.[*].creditReportRequestDetails").value(hasItem(DEFAULT_CREDIT_REPORT_REQUEST_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCrbReportRequestReasonsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbReportRequestReasonsShouldNotBeFound(String filter) throws Exception {
        restCrbReportRequestReasonsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbReportRequestReasonsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbReportRequestReasons() throws Exception {
        // Get the crbReportRequestReasons
        restCrbReportRequestReasonsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbReportRequestReasons() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        int databaseSizeBeforeUpdate = crbReportRequestReasonsRepository.findAll().size();

        // Update the crbReportRequestReasons
        CrbReportRequestReasons updatedCrbReportRequestReasons = crbReportRequestReasonsRepository
            .findById(crbReportRequestReasons.getId())
            .get();
        // Disconnect from session so that the updates on updatedCrbReportRequestReasons are not directly saved in db
        em.detach(updatedCrbReportRequestReasons);
        updatedCrbReportRequestReasons
            .creditReportRequestReasonTypeCode(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE)
            .creditReportRequestReasonType(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE)
            .creditReportRequestDetails(UPDATED_CREDIT_REPORT_REQUEST_DETAILS);
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO = crbReportRequestReasonsMapper.toDto(updatedCrbReportRequestReasons);

        restCrbReportRequestReasonsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbReportRequestReasonsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportRequestReasonsDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbReportRequestReasons in the database
        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeUpdate);
        CrbReportRequestReasons testCrbReportRequestReasons = crbReportRequestReasonsList.get(crbReportRequestReasonsList.size() - 1);
        assertThat(testCrbReportRequestReasons.getCreditReportRequestReasonTypeCode())
            .isEqualTo(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE);
        assertThat(testCrbReportRequestReasons.getCreditReportRequestReasonType()).isEqualTo(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE);
        assertThat(testCrbReportRequestReasons.getCreditReportRequestDetails()).isEqualTo(UPDATED_CREDIT_REPORT_REQUEST_DETAILS);

        // Validate the CrbReportRequestReasons in Elasticsearch
        verify(mockCrbReportRequestReasonsSearchRepository).save(testCrbReportRequestReasons);
    }

    @Test
    @Transactional
    void putNonExistingCrbReportRequestReasons() throws Exception {
        int databaseSizeBeforeUpdate = crbReportRequestReasonsRepository.findAll().size();
        crbReportRequestReasons.setId(count.incrementAndGet());

        // Create the CrbReportRequestReasons
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO = crbReportRequestReasonsMapper.toDto(crbReportRequestReasons);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbReportRequestReasonsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbReportRequestReasonsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportRequestReasonsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbReportRequestReasons in the database
        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbReportRequestReasons in Elasticsearch
        verify(mockCrbReportRequestReasonsSearchRepository, times(0)).save(crbReportRequestReasons);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbReportRequestReasons() throws Exception {
        int databaseSizeBeforeUpdate = crbReportRequestReasonsRepository.findAll().size();
        crbReportRequestReasons.setId(count.incrementAndGet());

        // Create the CrbReportRequestReasons
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO = crbReportRequestReasonsMapper.toDto(crbReportRequestReasons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbReportRequestReasonsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportRequestReasonsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbReportRequestReasons in the database
        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbReportRequestReasons in Elasticsearch
        verify(mockCrbReportRequestReasonsSearchRepository, times(0)).save(crbReportRequestReasons);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbReportRequestReasons() throws Exception {
        int databaseSizeBeforeUpdate = crbReportRequestReasonsRepository.findAll().size();
        crbReportRequestReasons.setId(count.incrementAndGet());

        // Create the CrbReportRequestReasons
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO = crbReportRequestReasonsMapper.toDto(crbReportRequestReasons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbReportRequestReasonsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportRequestReasonsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbReportRequestReasons in the database
        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbReportRequestReasons in Elasticsearch
        verify(mockCrbReportRequestReasonsSearchRepository, times(0)).save(crbReportRequestReasons);
    }

    @Test
    @Transactional
    void partialUpdateCrbReportRequestReasonsWithPatch() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        int databaseSizeBeforeUpdate = crbReportRequestReasonsRepository.findAll().size();

        // Update the crbReportRequestReasons using partial update
        CrbReportRequestReasons partialUpdatedCrbReportRequestReasons = new CrbReportRequestReasons();
        partialUpdatedCrbReportRequestReasons.setId(crbReportRequestReasons.getId());

        partialUpdatedCrbReportRequestReasons
            .creditReportRequestReasonTypeCode(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE)
            .creditReportRequestReasonType(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE)
            .creditReportRequestDetails(UPDATED_CREDIT_REPORT_REQUEST_DETAILS);

        restCrbReportRequestReasonsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbReportRequestReasons.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbReportRequestReasons))
            )
            .andExpect(status().isOk());

        // Validate the CrbReportRequestReasons in the database
        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeUpdate);
        CrbReportRequestReasons testCrbReportRequestReasons = crbReportRequestReasonsList.get(crbReportRequestReasonsList.size() - 1);
        assertThat(testCrbReportRequestReasons.getCreditReportRequestReasonTypeCode())
            .isEqualTo(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE);
        assertThat(testCrbReportRequestReasons.getCreditReportRequestReasonType()).isEqualTo(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE);
        assertThat(testCrbReportRequestReasons.getCreditReportRequestDetails()).isEqualTo(UPDATED_CREDIT_REPORT_REQUEST_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCrbReportRequestReasonsWithPatch() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        int databaseSizeBeforeUpdate = crbReportRequestReasonsRepository.findAll().size();

        // Update the crbReportRequestReasons using partial update
        CrbReportRequestReasons partialUpdatedCrbReportRequestReasons = new CrbReportRequestReasons();
        partialUpdatedCrbReportRequestReasons.setId(crbReportRequestReasons.getId());

        partialUpdatedCrbReportRequestReasons
            .creditReportRequestReasonTypeCode(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE)
            .creditReportRequestReasonType(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE)
            .creditReportRequestDetails(UPDATED_CREDIT_REPORT_REQUEST_DETAILS);

        restCrbReportRequestReasonsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbReportRequestReasons.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbReportRequestReasons))
            )
            .andExpect(status().isOk());

        // Validate the CrbReportRequestReasons in the database
        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeUpdate);
        CrbReportRequestReasons testCrbReportRequestReasons = crbReportRequestReasonsList.get(crbReportRequestReasonsList.size() - 1);
        assertThat(testCrbReportRequestReasons.getCreditReportRequestReasonTypeCode())
            .isEqualTo(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE);
        assertThat(testCrbReportRequestReasons.getCreditReportRequestReasonType()).isEqualTo(UPDATED_CREDIT_REPORT_REQUEST_REASON_TYPE);
        assertThat(testCrbReportRequestReasons.getCreditReportRequestDetails()).isEqualTo(UPDATED_CREDIT_REPORT_REQUEST_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCrbReportRequestReasons() throws Exception {
        int databaseSizeBeforeUpdate = crbReportRequestReasonsRepository.findAll().size();
        crbReportRequestReasons.setId(count.incrementAndGet());

        // Create the CrbReportRequestReasons
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO = crbReportRequestReasonsMapper.toDto(crbReportRequestReasons);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbReportRequestReasonsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbReportRequestReasonsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbReportRequestReasonsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbReportRequestReasons in the database
        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbReportRequestReasons in Elasticsearch
        verify(mockCrbReportRequestReasonsSearchRepository, times(0)).save(crbReportRequestReasons);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbReportRequestReasons() throws Exception {
        int databaseSizeBeforeUpdate = crbReportRequestReasonsRepository.findAll().size();
        crbReportRequestReasons.setId(count.incrementAndGet());

        // Create the CrbReportRequestReasons
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO = crbReportRequestReasonsMapper.toDto(crbReportRequestReasons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbReportRequestReasonsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbReportRequestReasonsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbReportRequestReasons in the database
        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbReportRequestReasons in Elasticsearch
        verify(mockCrbReportRequestReasonsSearchRepository, times(0)).save(crbReportRequestReasons);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbReportRequestReasons() throws Exception {
        int databaseSizeBeforeUpdate = crbReportRequestReasonsRepository.findAll().size();
        crbReportRequestReasons.setId(count.incrementAndGet());

        // Create the CrbReportRequestReasons
        CrbReportRequestReasonsDTO crbReportRequestReasonsDTO = crbReportRequestReasonsMapper.toDto(crbReportRequestReasons);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbReportRequestReasonsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbReportRequestReasonsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbReportRequestReasons in the database
        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbReportRequestReasons in Elasticsearch
        verify(mockCrbReportRequestReasonsSearchRepository, times(0)).save(crbReportRequestReasons);
    }

    @Test
    @Transactional
    void deleteCrbReportRequestReasons() throws Exception {
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);

        int databaseSizeBeforeDelete = crbReportRequestReasonsRepository.findAll().size();

        // Delete the crbReportRequestReasons
        restCrbReportRequestReasonsMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbReportRequestReasons.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbReportRequestReasons> crbReportRequestReasonsList = crbReportRequestReasonsRepository.findAll();
        assertThat(crbReportRequestReasonsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbReportRequestReasons in Elasticsearch
        verify(mockCrbReportRequestReasonsSearchRepository, times(1)).deleteById(crbReportRequestReasons.getId());
    }

    @Test
    @Transactional
    void searchCrbReportRequestReasons() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbReportRequestReasonsRepository.saveAndFlush(crbReportRequestReasons);
        when(mockCrbReportRequestReasonsSearchRepository.search("id:" + crbReportRequestReasons.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbReportRequestReasons), PageRequest.of(0, 1), 1));

        // Search the crbReportRequestReasons
        restCrbReportRequestReasonsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbReportRequestReasons.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbReportRequestReasons.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditReportRequestReasonTypeCode").value(hasItem(DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].creditReportRequestReasonType").value(hasItem(DEFAULT_CREDIT_REPORT_REQUEST_REASON_TYPE)))
            .andExpect(jsonPath("$.[*].creditReportRequestDetails").value(hasItem(DEFAULT_CREDIT_REPORT_REQUEST_DETAILS.toString())));
    }
}
