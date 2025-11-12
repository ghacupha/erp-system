package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.IssuersOfSecurities;
import io.github.erp.repository.IssuersOfSecuritiesRepository;
import io.github.erp.repository.search.IssuersOfSecuritiesSearchRepository;
import io.github.erp.service.criteria.IssuersOfSecuritiesCriteria;
import io.github.erp.service.dto.IssuersOfSecuritiesDTO;
import io.github.erp.service.mapper.IssuersOfSecuritiesMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link IssuersOfSecuritiesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class IssuersOfSecuritiesResourceIT {

    private static final String DEFAULT_ISSUER_OF_SECURITIES_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ISSUER_OF_SECURITIES_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUER_OF_SECURITIES = "AAAAAAAAAA";
    private static final String UPDATED_ISSUER_OF_SECURITIES = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUER_OF_SECURITIES_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ISSUER_OF_SECURITIES_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/issuers-of-securities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/issuers-of-securities";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IssuersOfSecuritiesRepository issuersOfSecuritiesRepository;

    @Autowired
    private IssuersOfSecuritiesMapper issuersOfSecuritiesMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.IssuersOfSecuritiesSearchRepositoryMockConfiguration
     */
    @Autowired
    private IssuersOfSecuritiesSearchRepository mockIssuersOfSecuritiesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIssuersOfSecuritiesMockMvc;

    private IssuersOfSecurities issuersOfSecurities;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IssuersOfSecurities createEntity(EntityManager em) {
        IssuersOfSecurities issuersOfSecurities = new IssuersOfSecurities()
            .issuerOfSecuritiesCode(DEFAULT_ISSUER_OF_SECURITIES_CODE)
            .issuerOfSecurities(DEFAULT_ISSUER_OF_SECURITIES)
            .issuerOfSecuritiesDescription(DEFAULT_ISSUER_OF_SECURITIES_DESCRIPTION);
        return issuersOfSecurities;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IssuersOfSecurities createUpdatedEntity(EntityManager em) {
        IssuersOfSecurities issuersOfSecurities = new IssuersOfSecurities()
            .issuerOfSecuritiesCode(UPDATED_ISSUER_OF_SECURITIES_CODE)
            .issuerOfSecurities(UPDATED_ISSUER_OF_SECURITIES)
            .issuerOfSecuritiesDescription(UPDATED_ISSUER_OF_SECURITIES_DESCRIPTION);
        return issuersOfSecurities;
    }

    @BeforeEach
    public void initTest() {
        issuersOfSecurities = createEntity(em);
    }

    @Test
    @Transactional
    void createIssuersOfSecurities() throws Exception {
        int databaseSizeBeforeCreate = issuersOfSecuritiesRepository.findAll().size();
        // Create the IssuersOfSecurities
        IssuersOfSecuritiesDTO issuersOfSecuritiesDTO = issuersOfSecuritiesMapper.toDto(issuersOfSecurities);
        restIssuersOfSecuritiesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(issuersOfSecuritiesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the IssuersOfSecurities in the database
        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeCreate + 1);
        IssuersOfSecurities testIssuersOfSecurities = issuersOfSecuritiesList.get(issuersOfSecuritiesList.size() - 1);
        assertThat(testIssuersOfSecurities.getIssuerOfSecuritiesCode()).isEqualTo(DEFAULT_ISSUER_OF_SECURITIES_CODE);
        assertThat(testIssuersOfSecurities.getIssuerOfSecurities()).isEqualTo(DEFAULT_ISSUER_OF_SECURITIES);
        assertThat(testIssuersOfSecurities.getIssuerOfSecuritiesDescription()).isEqualTo(DEFAULT_ISSUER_OF_SECURITIES_DESCRIPTION);

        // Validate the IssuersOfSecurities in Elasticsearch
        verify(mockIssuersOfSecuritiesSearchRepository, times(1)).save(testIssuersOfSecurities);
    }

    @Test
    @Transactional
    void createIssuersOfSecuritiesWithExistingId() throws Exception {
        // Create the IssuersOfSecurities with an existing ID
        issuersOfSecurities.setId(1L);
        IssuersOfSecuritiesDTO issuersOfSecuritiesDTO = issuersOfSecuritiesMapper.toDto(issuersOfSecurities);

        int databaseSizeBeforeCreate = issuersOfSecuritiesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssuersOfSecuritiesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(issuersOfSecuritiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IssuersOfSecurities in the database
        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeCreate);

        // Validate the IssuersOfSecurities in Elasticsearch
        verify(mockIssuersOfSecuritiesSearchRepository, times(0)).save(issuersOfSecurities);
    }

    @Test
    @Transactional
    void checkIssuerOfSecuritiesCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = issuersOfSecuritiesRepository.findAll().size();
        // set the field null
        issuersOfSecurities.setIssuerOfSecuritiesCode(null);

        // Create the IssuersOfSecurities, which fails.
        IssuersOfSecuritiesDTO issuersOfSecuritiesDTO = issuersOfSecuritiesMapper.toDto(issuersOfSecurities);

        restIssuersOfSecuritiesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(issuersOfSecuritiesDTO))
            )
            .andExpect(status().isBadRequest());

        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssuerOfSecuritiesIsRequired() throws Exception {
        int databaseSizeBeforeTest = issuersOfSecuritiesRepository.findAll().size();
        // set the field null
        issuersOfSecurities.setIssuerOfSecurities(null);

        // Create the IssuersOfSecurities, which fails.
        IssuersOfSecuritiesDTO issuersOfSecuritiesDTO = issuersOfSecuritiesMapper.toDto(issuersOfSecurities);

        restIssuersOfSecuritiesMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(issuersOfSecuritiesDTO))
            )
            .andExpect(status().isBadRequest());

        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIssuersOfSecurities() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList
        restIssuersOfSecuritiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issuersOfSecurities.getId().intValue())))
            .andExpect(jsonPath("$.[*].issuerOfSecuritiesCode").value(hasItem(DEFAULT_ISSUER_OF_SECURITIES_CODE)))
            .andExpect(jsonPath("$.[*].issuerOfSecurities").value(hasItem(DEFAULT_ISSUER_OF_SECURITIES)))
            .andExpect(jsonPath("$.[*].issuerOfSecuritiesDescription").value(hasItem(DEFAULT_ISSUER_OF_SECURITIES_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getIssuersOfSecurities() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get the issuersOfSecurities
        restIssuersOfSecuritiesMockMvc
            .perform(get(ENTITY_API_URL_ID, issuersOfSecurities.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(issuersOfSecurities.getId().intValue()))
            .andExpect(jsonPath("$.issuerOfSecuritiesCode").value(DEFAULT_ISSUER_OF_SECURITIES_CODE))
            .andExpect(jsonPath("$.issuerOfSecurities").value(DEFAULT_ISSUER_OF_SECURITIES))
            .andExpect(jsonPath("$.issuerOfSecuritiesDescription").value(DEFAULT_ISSUER_OF_SECURITIES_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getIssuersOfSecuritiesByIdFiltering() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        Long id = issuersOfSecurities.getId();

        defaultIssuersOfSecuritiesShouldBeFound("id.equals=" + id);
        defaultIssuersOfSecuritiesShouldNotBeFound("id.notEquals=" + id);

        defaultIssuersOfSecuritiesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultIssuersOfSecuritiesShouldNotBeFound("id.greaterThan=" + id);

        defaultIssuersOfSecuritiesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultIssuersOfSecuritiesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIssuersOfSecuritiesByIssuerOfSecuritiesCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList where issuerOfSecuritiesCode equals to DEFAULT_ISSUER_OF_SECURITIES_CODE
        defaultIssuersOfSecuritiesShouldBeFound("issuerOfSecuritiesCode.equals=" + DEFAULT_ISSUER_OF_SECURITIES_CODE);

        // Get all the issuersOfSecuritiesList where issuerOfSecuritiesCode equals to UPDATED_ISSUER_OF_SECURITIES_CODE
        defaultIssuersOfSecuritiesShouldNotBeFound("issuerOfSecuritiesCode.equals=" + UPDATED_ISSUER_OF_SECURITIES_CODE);
    }

    @Test
    @Transactional
    void getAllIssuersOfSecuritiesByIssuerOfSecuritiesCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList where issuerOfSecuritiesCode not equals to DEFAULT_ISSUER_OF_SECURITIES_CODE
        defaultIssuersOfSecuritiesShouldNotBeFound("issuerOfSecuritiesCode.notEquals=" + DEFAULT_ISSUER_OF_SECURITIES_CODE);

        // Get all the issuersOfSecuritiesList where issuerOfSecuritiesCode not equals to UPDATED_ISSUER_OF_SECURITIES_CODE
        defaultIssuersOfSecuritiesShouldBeFound("issuerOfSecuritiesCode.notEquals=" + UPDATED_ISSUER_OF_SECURITIES_CODE);
    }

    @Test
    @Transactional
    void getAllIssuersOfSecuritiesByIssuerOfSecuritiesCodeIsInShouldWork() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList where issuerOfSecuritiesCode in DEFAULT_ISSUER_OF_SECURITIES_CODE or UPDATED_ISSUER_OF_SECURITIES_CODE
        defaultIssuersOfSecuritiesShouldBeFound(
            "issuerOfSecuritiesCode.in=" + DEFAULT_ISSUER_OF_SECURITIES_CODE + "," + UPDATED_ISSUER_OF_SECURITIES_CODE
        );

        // Get all the issuersOfSecuritiesList where issuerOfSecuritiesCode equals to UPDATED_ISSUER_OF_SECURITIES_CODE
        defaultIssuersOfSecuritiesShouldNotBeFound("issuerOfSecuritiesCode.in=" + UPDATED_ISSUER_OF_SECURITIES_CODE);
    }

    @Test
    @Transactional
    void getAllIssuersOfSecuritiesByIssuerOfSecuritiesCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList where issuerOfSecuritiesCode is not null
        defaultIssuersOfSecuritiesShouldBeFound("issuerOfSecuritiesCode.specified=true");

        // Get all the issuersOfSecuritiesList where issuerOfSecuritiesCode is null
        defaultIssuersOfSecuritiesShouldNotBeFound("issuerOfSecuritiesCode.specified=false");
    }

    @Test
    @Transactional
    void getAllIssuersOfSecuritiesByIssuerOfSecuritiesCodeContainsSomething() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList where issuerOfSecuritiesCode contains DEFAULT_ISSUER_OF_SECURITIES_CODE
        defaultIssuersOfSecuritiesShouldBeFound("issuerOfSecuritiesCode.contains=" + DEFAULT_ISSUER_OF_SECURITIES_CODE);

        // Get all the issuersOfSecuritiesList where issuerOfSecuritiesCode contains UPDATED_ISSUER_OF_SECURITIES_CODE
        defaultIssuersOfSecuritiesShouldNotBeFound("issuerOfSecuritiesCode.contains=" + UPDATED_ISSUER_OF_SECURITIES_CODE);
    }

    @Test
    @Transactional
    void getAllIssuersOfSecuritiesByIssuerOfSecuritiesCodeNotContainsSomething() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList where issuerOfSecuritiesCode does not contain DEFAULT_ISSUER_OF_SECURITIES_CODE
        defaultIssuersOfSecuritiesShouldNotBeFound("issuerOfSecuritiesCode.doesNotContain=" + DEFAULT_ISSUER_OF_SECURITIES_CODE);

        // Get all the issuersOfSecuritiesList where issuerOfSecuritiesCode does not contain UPDATED_ISSUER_OF_SECURITIES_CODE
        defaultIssuersOfSecuritiesShouldBeFound("issuerOfSecuritiesCode.doesNotContain=" + UPDATED_ISSUER_OF_SECURITIES_CODE);
    }

    @Test
    @Transactional
    void getAllIssuersOfSecuritiesByIssuerOfSecuritiesIsEqualToSomething() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList where issuerOfSecurities equals to DEFAULT_ISSUER_OF_SECURITIES
        defaultIssuersOfSecuritiesShouldBeFound("issuerOfSecurities.equals=" + DEFAULT_ISSUER_OF_SECURITIES);

        // Get all the issuersOfSecuritiesList where issuerOfSecurities equals to UPDATED_ISSUER_OF_SECURITIES
        defaultIssuersOfSecuritiesShouldNotBeFound("issuerOfSecurities.equals=" + UPDATED_ISSUER_OF_SECURITIES);
    }

    @Test
    @Transactional
    void getAllIssuersOfSecuritiesByIssuerOfSecuritiesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList where issuerOfSecurities not equals to DEFAULT_ISSUER_OF_SECURITIES
        defaultIssuersOfSecuritiesShouldNotBeFound("issuerOfSecurities.notEquals=" + DEFAULT_ISSUER_OF_SECURITIES);

        // Get all the issuersOfSecuritiesList where issuerOfSecurities not equals to UPDATED_ISSUER_OF_SECURITIES
        defaultIssuersOfSecuritiesShouldBeFound("issuerOfSecurities.notEquals=" + UPDATED_ISSUER_OF_SECURITIES);
    }

    @Test
    @Transactional
    void getAllIssuersOfSecuritiesByIssuerOfSecuritiesIsInShouldWork() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList where issuerOfSecurities in DEFAULT_ISSUER_OF_SECURITIES or UPDATED_ISSUER_OF_SECURITIES
        defaultIssuersOfSecuritiesShouldBeFound(
            "issuerOfSecurities.in=" + DEFAULT_ISSUER_OF_SECURITIES + "," + UPDATED_ISSUER_OF_SECURITIES
        );

        // Get all the issuersOfSecuritiesList where issuerOfSecurities equals to UPDATED_ISSUER_OF_SECURITIES
        defaultIssuersOfSecuritiesShouldNotBeFound("issuerOfSecurities.in=" + UPDATED_ISSUER_OF_SECURITIES);
    }

    @Test
    @Transactional
    void getAllIssuersOfSecuritiesByIssuerOfSecuritiesIsNullOrNotNull() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList where issuerOfSecurities is not null
        defaultIssuersOfSecuritiesShouldBeFound("issuerOfSecurities.specified=true");

        // Get all the issuersOfSecuritiesList where issuerOfSecurities is null
        defaultIssuersOfSecuritiesShouldNotBeFound("issuerOfSecurities.specified=false");
    }

    @Test
    @Transactional
    void getAllIssuersOfSecuritiesByIssuerOfSecuritiesContainsSomething() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList where issuerOfSecurities contains DEFAULT_ISSUER_OF_SECURITIES
        defaultIssuersOfSecuritiesShouldBeFound("issuerOfSecurities.contains=" + DEFAULT_ISSUER_OF_SECURITIES);

        // Get all the issuersOfSecuritiesList where issuerOfSecurities contains UPDATED_ISSUER_OF_SECURITIES
        defaultIssuersOfSecuritiesShouldNotBeFound("issuerOfSecurities.contains=" + UPDATED_ISSUER_OF_SECURITIES);
    }

    @Test
    @Transactional
    void getAllIssuersOfSecuritiesByIssuerOfSecuritiesNotContainsSomething() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        // Get all the issuersOfSecuritiesList where issuerOfSecurities does not contain DEFAULT_ISSUER_OF_SECURITIES
        defaultIssuersOfSecuritiesShouldNotBeFound("issuerOfSecurities.doesNotContain=" + DEFAULT_ISSUER_OF_SECURITIES);

        // Get all the issuersOfSecuritiesList where issuerOfSecurities does not contain UPDATED_ISSUER_OF_SECURITIES
        defaultIssuersOfSecuritiesShouldBeFound("issuerOfSecurities.doesNotContain=" + UPDATED_ISSUER_OF_SECURITIES);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIssuersOfSecuritiesShouldBeFound(String filter) throws Exception {
        restIssuersOfSecuritiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issuersOfSecurities.getId().intValue())))
            .andExpect(jsonPath("$.[*].issuerOfSecuritiesCode").value(hasItem(DEFAULT_ISSUER_OF_SECURITIES_CODE)))
            .andExpect(jsonPath("$.[*].issuerOfSecurities").value(hasItem(DEFAULT_ISSUER_OF_SECURITIES)))
            .andExpect(jsonPath("$.[*].issuerOfSecuritiesDescription").value(hasItem(DEFAULT_ISSUER_OF_SECURITIES_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restIssuersOfSecuritiesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIssuersOfSecuritiesShouldNotBeFound(String filter) throws Exception {
        restIssuersOfSecuritiesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIssuersOfSecuritiesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIssuersOfSecurities() throws Exception {
        // Get the issuersOfSecurities
        restIssuersOfSecuritiesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIssuersOfSecurities() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        int databaseSizeBeforeUpdate = issuersOfSecuritiesRepository.findAll().size();

        // Update the issuersOfSecurities
        IssuersOfSecurities updatedIssuersOfSecurities = issuersOfSecuritiesRepository.findById(issuersOfSecurities.getId()).get();
        // Disconnect from session so that the updates on updatedIssuersOfSecurities are not directly saved in db
        em.detach(updatedIssuersOfSecurities);
        updatedIssuersOfSecurities
            .issuerOfSecuritiesCode(UPDATED_ISSUER_OF_SECURITIES_CODE)
            .issuerOfSecurities(UPDATED_ISSUER_OF_SECURITIES)
            .issuerOfSecuritiesDescription(UPDATED_ISSUER_OF_SECURITIES_DESCRIPTION);
        IssuersOfSecuritiesDTO issuersOfSecuritiesDTO = issuersOfSecuritiesMapper.toDto(updatedIssuersOfSecurities);

        restIssuersOfSecuritiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, issuersOfSecuritiesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(issuersOfSecuritiesDTO))
            )
            .andExpect(status().isOk());

        // Validate the IssuersOfSecurities in the database
        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeUpdate);
        IssuersOfSecurities testIssuersOfSecurities = issuersOfSecuritiesList.get(issuersOfSecuritiesList.size() - 1);
        assertThat(testIssuersOfSecurities.getIssuerOfSecuritiesCode()).isEqualTo(UPDATED_ISSUER_OF_SECURITIES_CODE);
        assertThat(testIssuersOfSecurities.getIssuerOfSecurities()).isEqualTo(UPDATED_ISSUER_OF_SECURITIES);
        assertThat(testIssuersOfSecurities.getIssuerOfSecuritiesDescription()).isEqualTo(UPDATED_ISSUER_OF_SECURITIES_DESCRIPTION);

        // Validate the IssuersOfSecurities in Elasticsearch
        verify(mockIssuersOfSecuritiesSearchRepository).save(testIssuersOfSecurities);
    }

    @Test
    @Transactional
    void putNonExistingIssuersOfSecurities() throws Exception {
        int databaseSizeBeforeUpdate = issuersOfSecuritiesRepository.findAll().size();
        issuersOfSecurities.setId(count.incrementAndGet());

        // Create the IssuersOfSecurities
        IssuersOfSecuritiesDTO issuersOfSecuritiesDTO = issuersOfSecuritiesMapper.toDto(issuersOfSecurities);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIssuersOfSecuritiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, issuersOfSecuritiesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(issuersOfSecuritiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IssuersOfSecurities in the database
        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IssuersOfSecurities in Elasticsearch
        verify(mockIssuersOfSecuritiesSearchRepository, times(0)).save(issuersOfSecurities);
    }

    @Test
    @Transactional
    void putWithIdMismatchIssuersOfSecurities() throws Exception {
        int databaseSizeBeforeUpdate = issuersOfSecuritiesRepository.findAll().size();
        issuersOfSecurities.setId(count.incrementAndGet());

        // Create the IssuersOfSecurities
        IssuersOfSecuritiesDTO issuersOfSecuritiesDTO = issuersOfSecuritiesMapper.toDto(issuersOfSecurities);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIssuersOfSecuritiesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(issuersOfSecuritiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IssuersOfSecurities in the database
        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IssuersOfSecurities in Elasticsearch
        verify(mockIssuersOfSecuritiesSearchRepository, times(0)).save(issuersOfSecurities);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIssuersOfSecurities() throws Exception {
        int databaseSizeBeforeUpdate = issuersOfSecuritiesRepository.findAll().size();
        issuersOfSecurities.setId(count.incrementAndGet());

        // Create the IssuersOfSecurities
        IssuersOfSecuritiesDTO issuersOfSecuritiesDTO = issuersOfSecuritiesMapper.toDto(issuersOfSecurities);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIssuersOfSecuritiesMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(issuersOfSecuritiesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IssuersOfSecurities in the database
        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IssuersOfSecurities in Elasticsearch
        verify(mockIssuersOfSecuritiesSearchRepository, times(0)).save(issuersOfSecurities);
    }

    @Test
    @Transactional
    void partialUpdateIssuersOfSecuritiesWithPatch() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        int databaseSizeBeforeUpdate = issuersOfSecuritiesRepository.findAll().size();

        // Update the issuersOfSecurities using partial update
        IssuersOfSecurities partialUpdatedIssuersOfSecurities = new IssuersOfSecurities();
        partialUpdatedIssuersOfSecurities.setId(issuersOfSecurities.getId());

        partialUpdatedIssuersOfSecurities
            .issuerOfSecuritiesCode(UPDATED_ISSUER_OF_SECURITIES_CODE)
            .issuerOfSecurities(UPDATED_ISSUER_OF_SECURITIES)
            .issuerOfSecuritiesDescription(UPDATED_ISSUER_OF_SECURITIES_DESCRIPTION);

        restIssuersOfSecuritiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIssuersOfSecurities.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIssuersOfSecurities))
            )
            .andExpect(status().isOk());

        // Validate the IssuersOfSecurities in the database
        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeUpdate);
        IssuersOfSecurities testIssuersOfSecurities = issuersOfSecuritiesList.get(issuersOfSecuritiesList.size() - 1);
        assertThat(testIssuersOfSecurities.getIssuerOfSecuritiesCode()).isEqualTo(UPDATED_ISSUER_OF_SECURITIES_CODE);
        assertThat(testIssuersOfSecurities.getIssuerOfSecurities()).isEqualTo(UPDATED_ISSUER_OF_SECURITIES);
        assertThat(testIssuersOfSecurities.getIssuerOfSecuritiesDescription()).isEqualTo(UPDATED_ISSUER_OF_SECURITIES_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateIssuersOfSecuritiesWithPatch() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        int databaseSizeBeforeUpdate = issuersOfSecuritiesRepository.findAll().size();

        // Update the issuersOfSecurities using partial update
        IssuersOfSecurities partialUpdatedIssuersOfSecurities = new IssuersOfSecurities();
        partialUpdatedIssuersOfSecurities.setId(issuersOfSecurities.getId());

        partialUpdatedIssuersOfSecurities
            .issuerOfSecuritiesCode(UPDATED_ISSUER_OF_SECURITIES_CODE)
            .issuerOfSecurities(UPDATED_ISSUER_OF_SECURITIES)
            .issuerOfSecuritiesDescription(UPDATED_ISSUER_OF_SECURITIES_DESCRIPTION);

        restIssuersOfSecuritiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIssuersOfSecurities.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIssuersOfSecurities))
            )
            .andExpect(status().isOk());

        // Validate the IssuersOfSecurities in the database
        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeUpdate);
        IssuersOfSecurities testIssuersOfSecurities = issuersOfSecuritiesList.get(issuersOfSecuritiesList.size() - 1);
        assertThat(testIssuersOfSecurities.getIssuerOfSecuritiesCode()).isEqualTo(UPDATED_ISSUER_OF_SECURITIES_CODE);
        assertThat(testIssuersOfSecurities.getIssuerOfSecurities()).isEqualTo(UPDATED_ISSUER_OF_SECURITIES);
        assertThat(testIssuersOfSecurities.getIssuerOfSecuritiesDescription()).isEqualTo(UPDATED_ISSUER_OF_SECURITIES_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingIssuersOfSecurities() throws Exception {
        int databaseSizeBeforeUpdate = issuersOfSecuritiesRepository.findAll().size();
        issuersOfSecurities.setId(count.incrementAndGet());

        // Create the IssuersOfSecurities
        IssuersOfSecuritiesDTO issuersOfSecuritiesDTO = issuersOfSecuritiesMapper.toDto(issuersOfSecurities);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIssuersOfSecuritiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, issuersOfSecuritiesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(issuersOfSecuritiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IssuersOfSecurities in the database
        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IssuersOfSecurities in Elasticsearch
        verify(mockIssuersOfSecuritiesSearchRepository, times(0)).save(issuersOfSecurities);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIssuersOfSecurities() throws Exception {
        int databaseSizeBeforeUpdate = issuersOfSecuritiesRepository.findAll().size();
        issuersOfSecurities.setId(count.incrementAndGet());

        // Create the IssuersOfSecurities
        IssuersOfSecuritiesDTO issuersOfSecuritiesDTO = issuersOfSecuritiesMapper.toDto(issuersOfSecurities);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIssuersOfSecuritiesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(issuersOfSecuritiesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IssuersOfSecurities in the database
        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IssuersOfSecurities in Elasticsearch
        verify(mockIssuersOfSecuritiesSearchRepository, times(0)).save(issuersOfSecurities);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIssuersOfSecurities() throws Exception {
        int databaseSizeBeforeUpdate = issuersOfSecuritiesRepository.findAll().size();
        issuersOfSecurities.setId(count.incrementAndGet());

        // Create the IssuersOfSecurities
        IssuersOfSecuritiesDTO issuersOfSecuritiesDTO = issuersOfSecuritiesMapper.toDto(issuersOfSecurities);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIssuersOfSecuritiesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(issuersOfSecuritiesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IssuersOfSecurities in the database
        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IssuersOfSecurities in Elasticsearch
        verify(mockIssuersOfSecuritiesSearchRepository, times(0)).save(issuersOfSecurities);
    }

    @Test
    @Transactional
    void deleteIssuersOfSecurities() throws Exception {
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);

        int databaseSizeBeforeDelete = issuersOfSecuritiesRepository.findAll().size();

        // Delete the issuersOfSecurities
        restIssuersOfSecuritiesMockMvc
            .perform(delete(ENTITY_API_URL_ID, issuersOfSecurities.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IssuersOfSecurities> issuersOfSecuritiesList = issuersOfSecuritiesRepository.findAll();
        assertThat(issuersOfSecuritiesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IssuersOfSecurities in Elasticsearch
        verify(mockIssuersOfSecuritiesSearchRepository, times(1)).deleteById(issuersOfSecurities.getId());
    }

    @Test
    @Transactional
    void searchIssuersOfSecurities() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        issuersOfSecuritiesRepository.saveAndFlush(issuersOfSecurities);
        when(mockIssuersOfSecuritiesSearchRepository.search("id:" + issuersOfSecurities.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(issuersOfSecurities), PageRequest.of(0, 1), 1));

        // Search the issuersOfSecurities
        restIssuersOfSecuritiesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + issuersOfSecurities.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issuersOfSecurities.getId().intValue())))
            .andExpect(jsonPath("$.[*].issuerOfSecuritiesCode").value(hasItem(DEFAULT_ISSUER_OF_SECURITIES_CODE)))
            .andExpect(jsonPath("$.[*].issuerOfSecurities").value(hasItem(DEFAULT_ISSUER_OF_SECURITIES)))
            .andExpect(jsonPath("$.[*].issuerOfSecuritiesDescription").value(hasItem(DEFAULT_ISSUER_OF_SECURITIES_DESCRIPTION.toString())));
    }
}
