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
import io.github.erp.domain.CrbAmountCategoryBand;
import io.github.erp.repository.CrbAmountCategoryBandRepository;
import io.github.erp.repository.search.CrbAmountCategoryBandSearchRepository;
import io.github.erp.service.criteria.CrbAmountCategoryBandCriteria;
import io.github.erp.service.dto.CrbAmountCategoryBandDTO;
import io.github.erp.service.mapper.CrbAmountCategoryBandMapper;
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
 * Integration tests for the {@link CrbAmountCategoryBandResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbAmountCategoryBandResourceIT {

    private static final String DEFAULT_AMOUNT_CATEGORY_BAND_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AMOUNT_CATEGORY_BAND_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_AMOUNT_CATEGORY_BAND = "AAAAAAAAAA";
    private static final String UPDATED_AMOUNT_CATEGORY_BAND = "BBBBBBBBBB";

    private static final String DEFAULT_AMOUNT_CATEGORY_BAND_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_AMOUNT_CATEGORY_BAND_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-amount-category-bands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-amount-category-bands";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbAmountCategoryBandRepository crbAmountCategoryBandRepository;

    @Autowired
    private CrbAmountCategoryBandMapper crbAmountCategoryBandMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbAmountCategoryBandSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbAmountCategoryBandSearchRepository mockCrbAmountCategoryBandSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbAmountCategoryBandMockMvc;

    private CrbAmountCategoryBand crbAmountCategoryBand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbAmountCategoryBand createEntity(EntityManager em) {
        CrbAmountCategoryBand crbAmountCategoryBand = new CrbAmountCategoryBand()
            .amountCategoryBandCode(DEFAULT_AMOUNT_CATEGORY_BAND_CODE)
            .amountCategoryBand(DEFAULT_AMOUNT_CATEGORY_BAND)
            .amountCategoryBandDetails(DEFAULT_AMOUNT_CATEGORY_BAND_DETAILS);
        return crbAmountCategoryBand;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbAmountCategoryBand createUpdatedEntity(EntityManager em) {
        CrbAmountCategoryBand crbAmountCategoryBand = new CrbAmountCategoryBand()
            .amountCategoryBandCode(UPDATED_AMOUNT_CATEGORY_BAND_CODE)
            .amountCategoryBand(UPDATED_AMOUNT_CATEGORY_BAND)
            .amountCategoryBandDetails(UPDATED_AMOUNT_CATEGORY_BAND_DETAILS);
        return crbAmountCategoryBand;
    }

    @BeforeEach
    public void initTest() {
        crbAmountCategoryBand = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbAmountCategoryBand() throws Exception {
        int databaseSizeBeforeCreate = crbAmountCategoryBandRepository.findAll().size();
        // Create the CrbAmountCategoryBand
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO = crbAmountCategoryBandMapper.toDto(crbAmountCategoryBand);
        restCrbAmountCategoryBandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAmountCategoryBandDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbAmountCategoryBand in the database
        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeCreate + 1);
        CrbAmountCategoryBand testCrbAmountCategoryBand = crbAmountCategoryBandList.get(crbAmountCategoryBandList.size() - 1);
        assertThat(testCrbAmountCategoryBand.getAmountCategoryBandCode()).isEqualTo(DEFAULT_AMOUNT_CATEGORY_BAND_CODE);
        assertThat(testCrbAmountCategoryBand.getAmountCategoryBand()).isEqualTo(DEFAULT_AMOUNT_CATEGORY_BAND);
        assertThat(testCrbAmountCategoryBand.getAmountCategoryBandDetails()).isEqualTo(DEFAULT_AMOUNT_CATEGORY_BAND_DETAILS);

        // Validate the CrbAmountCategoryBand in Elasticsearch
        verify(mockCrbAmountCategoryBandSearchRepository, times(1)).save(testCrbAmountCategoryBand);
    }

    @Test
    @Transactional
    void createCrbAmountCategoryBandWithExistingId() throws Exception {
        // Create the CrbAmountCategoryBand with an existing ID
        crbAmountCategoryBand.setId(1L);
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO = crbAmountCategoryBandMapper.toDto(crbAmountCategoryBand);

        int databaseSizeBeforeCreate = crbAmountCategoryBandRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbAmountCategoryBandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAmountCategoryBandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAmountCategoryBand in the database
        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbAmountCategoryBand in Elasticsearch
        verify(mockCrbAmountCategoryBandSearchRepository, times(0)).save(crbAmountCategoryBand);
    }

    @Test
    @Transactional
    void checkAmountCategoryBandCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbAmountCategoryBandRepository.findAll().size();
        // set the field null
        crbAmountCategoryBand.setAmountCategoryBandCode(null);

        // Create the CrbAmountCategoryBand, which fails.
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO = crbAmountCategoryBandMapper.toDto(crbAmountCategoryBand);

        restCrbAmountCategoryBandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAmountCategoryBandDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountCategoryBandIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbAmountCategoryBandRepository.findAll().size();
        // set the field null
        crbAmountCategoryBand.setAmountCategoryBand(null);

        // Create the CrbAmountCategoryBand, which fails.
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO = crbAmountCategoryBandMapper.toDto(crbAmountCategoryBand);

        restCrbAmountCategoryBandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAmountCategoryBandDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBands() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList
        restCrbAmountCategoryBandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAmountCategoryBand.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountCategoryBandCode").value(hasItem(DEFAULT_AMOUNT_CATEGORY_BAND_CODE)))
            .andExpect(jsonPath("$.[*].amountCategoryBand").value(hasItem(DEFAULT_AMOUNT_CATEGORY_BAND)))
            .andExpect(jsonPath("$.[*].amountCategoryBandDetails").value(hasItem(DEFAULT_AMOUNT_CATEGORY_BAND_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCrbAmountCategoryBand() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get the crbAmountCategoryBand
        restCrbAmountCategoryBandMockMvc
            .perform(get(ENTITY_API_URL_ID, crbAmountCategoryBand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbAmountCategoryBand.getId().intValue()))
            .andExpect(jsonPath("$.amountCategoryBandCode").value(DEFAULT_AMOUNT_CATEGORY_BAND_CODE))
            .andExpect(jsonPath("$.amountCategoryBand").value(DEFAULT_AMOUNT_CATEGORY_BAND))
            .andExpect(jsonPath("$.amountCategoryBandDetails").value(DEFAULT_AMOUNT_CATEGORY_BAND_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCrbAmountCategoryBandsByIdFiltering() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        Long id = crbAmountCategoryBand.getId();

        defaultCrbAmountCategoryBandShouldBeFound("id.equals=" + id);
        defaultCrbAmountCategoryBandShouldNotBeFound("id.notEquals=" + id);

        defaultCrbAmountCategoryBandShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbAmountCategoryBandShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbAmountCategoryBandShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbAmountCategoryBandShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBandsByAmountCategoryBandCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList where amountCategoryBandCode equals to DEFAULT_AMOUNT_CATEGORY_BAND_CODE
        defaultCrbAmountCategoryBandShouldBeFound("amountCategoryBandCode.equals=" + DEFAULT_AMOUNT_CATEGORY_BAND_CODE);

        // Get all the crbAmountCategoryBandList where amountCategoryBandCode equals to UPDATED_AMOUNT_CATEGORY_BAND_CODE
        defaultCrbAmountCategoryBandShouldNotBeFound("amountCategoryBandCode.equals=" + UPDATED_AMOUNT_CATEGORY_BAND_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBandsByAmountCategoryBandCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList where amountCategoryBandCode not equals to DEFAULT_AMOUNT_CATEGORY_BAND_CODE
        defaultCrbAmountCategoryBandShouldNotBeFound("amountCategoryBandCode.notEquals=" + DEFAULT_AMOUNT_CATEGORY_BAND_CODE);

        // Get all the crbAmountCategoryBandList where amountCategoryBandCode not equals to UPDATED_AMOUNT_CATEGORY_BAND_CODE
        defaultCrbAmountCategoryBandShouldBeFound("amountCategoryBandCode.notEquals=" + UPDATED_AMOUNT_CATEGORY_BAND_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBandsByAmountCategoryBandCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList where amountCategoryBandCode in DEFAULT_AMOUNT_CATEGORY_BAND_CODE or UPDATED_AMOUNT_CATEGORY_BAND_CODE
        defaultCrbAmountCategoryBandShouldBeFound(
            "amountCategoryBandCode.in=" + DEFAULT_AMOUNT_CATEGORY_BAND_CODE + "," + UPDATED_AMOUNT_CATEGORY_BAND_CODE
        );

        // Get all the crbAmountCategoryBandList where amountCategoryBandCode equals to UPDATED_AMOUNT_CATEGORY_BAND_CODE
        defaultCrbAmountCategoryBandShouldNotBeFound("amountCategoryBandCode.in=" + UPDATED_AMOUNT_CATEGORY_BAND_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBandsByAmountCategoryBandCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList where amountCategoryBandCode is not null
        defaultCrbAmountCategoryBandShouldBeFound("amountCategoryBandCode.specified=true");

        // Get all the crbAmountCategoryBandList where amountCategoryBandCode is null
        defaultCrbAmountCategoryBandShouldNotBeFound("amountCategoryBandCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBandsByAmountCategoryBandCodeContainsSomething() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList where amountCategoryBandCode contains DEFAULT_AMOUNT_CATEGORY_BAND_CODE
        defaultCrbAmountCategoryBandShouldBeFound("amountCategoryBandCode.contains=" + DEFAULT_AMOUNT_CATEGORY_BAND_CODE);

        // Get all the crbAmountCategoryBandList where amountCategoryBandCode contains UPDATED_AMOUNT_CATEGORY_BAND_CODE
        defaultCrbAmountCategoryBandShouldNotBeFound("amountCategoryBandCode.contains=" + UPDATED_AMOUNT_CATEGORY_BAND_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBandsByAmountCategoryBandCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList where amountCategoryBandCode does not contain DEFAULT_AMOUNT_CATEGORY_BAND_CODE
        defaultCrbAmountCategoryBandShouldNotBeFound("amountCategoryBandCode.doesNotContain=" + DEFAULT_AMOUNT_CATEGORY_BAND_CODE);

        // Get all the crbAmountCategoryBandList where amountCategoryBandCode does not contain UPDATED_AMOUNT_CATEGORY_BAND_CODE
        defaultCrbAmountCategoryBandShouldBeFound("amountCategoryBandCode.doesNotContain=" + UPDATED_AMOUNT_CATEGORY_BAND_CODE);
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBandsByAmountCategoryBandIsEqualToSomething() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList where amountCategoryBand equals to DEFAULT_AMOUNT_CATEGORY_BAND
        defaultCrbAmountCategoryBandShouldBeFound("amountCategoryBand.equals=" + DEFAULT_AMOUNT_CATEGORY_BAND);

        // Get all the crbAmountCategoryBandList where amountCategoryBand equals to UPDATED_AMOUNT_CATEGORY_BAND
        defaultCrbAmountCategoryBandShouldNotBeFound("amountCategoryBand.equals=" + UPDATED_AMOUNT_CATEGORY_BAND);
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBandsByAmountCategoryBandIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList where amountCategoryBand not equals to DEFAULT_AMOUNT_CATEGORY_BAND
        defaultCrbAmountCategoryBandShouldNotBeFound("amountCategoryBand.notEquals=" + DEFAULT_AMOUNT_CATEGORY_BAND);

        // Get all the crbAmountCategoryBandList where amountCategoryBand not equals to UPDATED_AMOUNT_CATEGORY_BAND
        defaultCrbAmountCategoryBandShouldBeFound("amountCategoryBand.notEquals=" + UPDATED_AMOUNT_CATEGORY_BAND);
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBandsByAmountCategoryBandIsInShouldWork() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList where amountCategoryBand in DEFAULT_AMOUNT_CATEGORY_BAND or UPDATED_AMOUNT_CATEGORY_BAND
        defaultCrbAmountCategoryBandShouldBeFound(
            "amountCategoryBand.in=" + DEFAULT_AMOUNT_CATEGORY_BAND + "," + UPDATED_AMOUNT_CATEGORY_BAND
        );

        // Get all the crbAmountCategoryBandList where amountCategoryBand equals to UPDATED_AMOUNT_CATEGORY_BAND
        defaultCrbAmountCategoryBandShouldNotBeFound("amountCategoryBand.in=" + UPDATED_AMOUNT_CATEGORY_BAND);
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBandsByAmountCategoryBandIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList where amountCategoryBand is not null
        defaultCrbAmountCategoryBandShouldBeFound("amountCategoryBand.specified=true");

        // Get all the crbAmountCategoryBandList where amountCategoryBand is null
        defaultCrbAmountCategoryBandShouldNotBeFound("amountCategoryBand.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBandsByAmountCategoryBandContainsSomething() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList where amountCategoryBand contains DEFAULT_AMOUNT_CATEGORY_BAND
        defaultCrbAmountCategoryBandShouldBeFound("amountCategoryBand.contains=" + DEFAULT_AMOUNT_CATEGORY_BAND);

        // Get all the crbAmountCategoryBandList where amountCategoryBand contains UPDATED_AMOUNT_CATEGORY_BAND
        defaultCrbAmountCategoryBandShouldNotBeFound("amountCategoryBand.contains=" + UPDATED_AMOUNT_CATEGORY_BAND);
    }

    @Test
    @Transactional
    void getAllCrbAmountCategoryBandsByAmountCategoryBandNotContainsSomething() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        // Get all the crbAmountCategoryBandList where amountCategoryBand does not contain DEFAULT_AMOUNT_CATEGORY_BAND
        defaultCrbAmountCategoryBandShouldNotBeFound("amountCategoryBand.doesNotContain=" + DEFAULT_AMOUNT_CATEGORY_BAND);

        // Get all the crbAmountCategoryBandList where amountCategoryBand does not contain UPDATED_AMOUNT_CATEGORY_BAND
        defaultCrbAmountCategoryBandShouldBeFound("amountCategoryBand.doesNotContain=" + UPDATED_AMOUNT_CATEGORY_BAND);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbAmountCategoryBandShouldBeFound(String filter) throws Exception {
        restCrbAmountCategoryBandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAmountCategoryBand.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountCategoryBandCode").value(hasItem(DEFAULT_AMOUNT_CATEGORY_BAND_CODE)))
            .andExpect(jsonPath("$.[*].amountCategoryBand").value(hasItem(DEFAULT_AMOUNT_CATEGORY_BAND)))
            .andExpect(jsonPath("$.[*].amountCategoryBandDetails").value(hasItem(DEFAULT_AMOUNT_CATEGORY_BAND_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCrbAmountCategoryBandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbAmountCategoryBandShouldNotBeFound(String filter) throws Exception {
        restCrbAmountCategoryBandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbAmountCategoryBandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbAmountCategoryBand() throws Exception {
        // Get the crbAmountCategoryBand
        restCrbAmountCategoryBandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbAmountCategoryBand() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        int databaseSizeBeforeUpdate = crbAmountCategoryBandRepository.findAll().size();

        // Update the crbAmountCategoryBand
        CrbAmountCategoryBand updatedCrbAmountCategoryBand = crbAmountCategoryBandRepository.findById(crbAmountCategoryBand.getId()).get();
        // Disconnect from session so that the updates on updatedCrbAmountCategoryBand are not directly saved in db
        em.detach(updatedCrbAmountCategoryBand);
        updatedCrbAmountCategoryBand
            .amountCategoryBandCode(UPDATED_AMOUNT_CATEGORY_BAND_CODE)
            .amountCategoryBand(UPDATED_AMOUNT_CATEGORY_BAND)
            .amountCategoryBandDetails(UPDATED_AMOUNT_CATEGORY_BAND_DETAILS);
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO = crbAmountCategoryBandMapper.toDto(updatedCrbAmountCategoryBand);

        restCrbAmountCategoryBandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbAmountCategoryBandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAmountCategoryBandDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbAmountCategoryBand in the database
        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeUpdate);
        CrbAmountCategoryBand testCrbAmountCategoryBand = crbAmountCategoryBandList.get(crbAmountCategoryBandList.size() - 1);
        assertThat(testCrbAmountCategoryBand.getAmountCategoryBandCode()).isEqualTo(UPDATED_AMOUNT_CATEGORY_BAND_CODE);
        assertThat(testCrbAmountCategoryBand.getAmountCategoryBand()).isEqualTo(UPDATED_AMOUNT_CATEGORY_BAND);
        assertThat(testCrbAmountCategoryBand.getAmountCategoryBandDetails()).isEqualTo(UPDATED_AMOUNT_CATEGORY_BAND_DETAILS);

        // Validate the CrbAmountCategoryBand in Elasticsearch
        verify(mockCrbAmountCategoryBandSearchRepository).save(testCrbAmountCategoryBand);
    }

    @Test
    @Transactional
    void putNonExistingCrbAmountCategoryBand() throws Exception {
        int databaseSizeBeforeUpdate = crbAmountCategoryBandRepository.findAll().size();
        crbAmountCategoryBand.setId(count.incrementAndGet());

        // Create the CrbAmountCategoryBand
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO = crbAmountCategoryBandMapper.toDto(crbAmountCategoryBand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbAmountCategoryBandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbAmountCategoryBandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAmountCategoryBandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAmountCategoryBand in the database
        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAmountCategoryBand in Elasticsearch
        verify(mockCrbAmountCategoryBandSearchRepository, times(0)).save(crbAmountCategoryBand);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbAmountCategoryBand() throws Exception {
        int databaseSizeBeforeUpdate = crbAmountCategoryBandRepository.findAll().size();
        crbAmountCategoryBand.setId(count.incrementAndGet());

        // Create the CrbAmountCategoryBand
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO = crbAmountCategoryBandMapper.toDto(crbAmountCategoryBand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAmountCategoryBandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAmountCategoryBandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAmountCategoryBand in the database
        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAmountCategoryBand in Elasticsearch
        verify(mockCrbAmountCategoryBandSearchRepository, times(0)).save(crbAmountCategoryBand);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbAmountCategoryBand() throws Exception {
        int databaseSizeBeforeUpdate = crbAmountCategoryBandRepository.findAll().size();
        crbAmountCategoryBand.setId(count.incrementAndGet());

        // Create the CrbAmountCategoryBand
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO = crbAmountCategoryBandMapper.toDto(crbAmountCategoryBand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAmountCategoryBandMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbAmountCategoryBandDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbAmountCategoryBand in the database
        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAmountCategoryBand in Elasticsearch
        verify(mockCrbAmountCategoryBandSearchRepository, times(0)).save(crbAmountCategoryBand);
    }

    @Test
    @Transactional
    void partialUpdateCrbAmountCategoryBandWithPatch() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        int databaseSizeBeforeUpdate = crbAmountCategoryBandRepository.findAll().size();

        // Update the crbAmountCategoryBand using partial update
        CrbAmountCategoryBand partialUpdatedCrbAmountCategoryBand = new CrbAmountCategoryBand();
        partialUpdatedCrbAmountCategoryBand.setId(crbAmountCategoryBand.getId());

        partialUpdatedCrbAmountCategoryBand.amountCategoryBand(UPDATED_AMOUNT_CATEGORY_BAND);

        restCrbAmountCategoryBandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbAmountCategoryBand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbAmountCategoryBand))
            )
            .andExpect(status().isOk());

        // Validate the CrbAmountCategoryBand in the database
        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeUpdate);
        CrbAmountCategoryBand testCrbAmountCategoryBand = crbAmountCategoryBandList.get(crbAmountCategoryBandList.size() - 1);
        assertThat(testCrbAmountCategoryBand.getAmountCategoryBandCode()).isEqualTo(DEFAULT_AMOUNT_CATEGORY_BAND_CODE);
        assertThat(testCrbAmountCategoryBand.getAmountCategoryBand()).isEqualTo(UPDATED_AMOUNT_CATEGORY_BAND);
        assertThat(testCrbAmountCategoryBand.getAmountCategoryBandDetails()).isEqualTo(DEFAULT_AMOUNT_CATEGORY_BAND_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCrbAmountCategoryBandWithPatch() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        int databaseSizeBeforeUpdate = crbAmountCategoryBandRepository.findAll().size();

        // Update the crbAmountCategoryBand using partial update
        CrbAmountCategoryBand partialUpdatedCrbAmountCategoryBand = new CrbAmountCategoryBand();
        partialUpdatedCrbAmountCategoryBand.setId(crbAmountCategoryBand.getId());

        partialUpdatedCrbAmountCategoryBand
            .amountCategoryBandCode(UPDATED_AMOUNT_CATEGORY_BAND_CODE)
            .amountCategoryBand(UPDATED_AMOUNT_CATEGORY_BAND)
            .amountCategoryBandDetails(UPDATED_AMOUNT_CATEGORY_BAND_DETAILS);

        restCrbAmountCategoryBandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbAmountCategoryBand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbAmountCategoryBand))
            )
            .andExpect(status().isOk());

        // Validate the CrbAmountCategoryBand in the database
        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeUpdate);
        CrbAmountCategoryBand testCrbAmountCategoryBand = crbAmountCategoryBandList.get(crbAmountCategoryBandList.size() - 1);
        assertThat(testCrbAmountCategoryBand.getAmountCategoryBandCode()).isEqualTo(UPDATED_AMOUNT_CATEGORY_BAND_CODE);
        assertThat(testCrbAmountCategoryBand.getAmountCategoryBand()).isEqualTo(UPDATED_AMOUNT_CATEGORY_BAND);
        assertThat(testCrbAmountCategoryBand.getAmountCategoryBandDetails()).isEqualTo(UPDATED_AMOUNT_CATEGORY_BAND_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCrbAmountCategoryBand() throws Exception {
        int databaseSizeBeforeUpdate = crbAmountCategoryBandRepository.findAll().size();
        crbAmountCategoryBand.setId(count.incrementAndGet());

        // Create the CrbAmountCategoryBand
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO = crbAmountCategoryBandMapper.toDto(crbAmountCategoryBand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbAmountCategoryBandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbAmountCategoryBandDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAmountCategoryBandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAmountCategoryBand in the database
        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAmountCategoryBand in Elasticsearch
        verify(mockCrbAmountCategoryBandSearchRepository, times(0)).save(crbAmountCategoryBand);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbAmountCategoryBand() throws Exception {
        int databaseSizeBeforeUpdate = crbAmountCategoryBandRepository.findAll().size();
        crbAmountCategoryBand.setId(count.incrementAndGet());

        // Create the CrbAmountCategoryBand
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO = crbAmountCategoryBandMapper.toDto(crbAmountCategoryBand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAmountCategoryBandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAmountCategoryBandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbAmountCategoryBand in the database
        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAmountCategoryBand in Elasticsearch
        verify(mockCrbAmountCategoryBandSearchRepository, times(0)).save(crbAmountCategoryBand);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbAmountCategoryBand() throws Exception {
        int databaseSizeBeforeUpdate = crbAmountCategoryBandRepository.findAll().size();
        crbAmountCategoryBand.setId(count.incrementAndGet());

        // Create the CrbAmountCategoryBand
        CrbAmountCategoryBandDTO crbAmountCategoryBandDTO = crbAmountCategoryBandMapper.toDto(crbAmountCategoryBand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbAmountCategoryBandMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbAmountCategoryBandDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbAmountCategoryBand in the database
        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbAmountCategoryBand in Elasticsearch
        verify(mockCrbAmountCategoryBandSearchRepository, times(0)).save(crbAmountCategoryBand);
    }

    @Test
    @Transactional
    void deleteCrbAmountCategoryBand() throws Exception {
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);

        int databaseSizeBeforeDelete = crbAmountCategoryBandRepository.findAll().size();

        // Delete the crbAmountCategoryBand
        restCrbAmountCategoryBandMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbAmountCategoryBand.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbAmountCategoryBand> crbAmountCategoryBandList = crbAmountCategoryBandRepository.findAll();
        assertThat(crbAmountCategoryBandList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbAmountCategoryBand in Elasticsearch
        verify(mockCrbAmountCategoryBandSearchRepository, times(1)).deleteById(crbAmountCategoryBand.getId());
    }

    @Test
    @Transactional
    void searchCrbAmountCategoryBand() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbAmountCategoryBandRepository.saveAndFlush(crbAmountCategoryBand);
        when(mockCrbAmountCategoryBandSearchRepository.search("id:" + crbAmountCategoryBand.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbAmountCategoryBand), PageRequest.of(0, 1), 1));

        // Search the crbAmountCategoryBand
        restCrbAmountCategoryBandMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbAmountCategoryBand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbAmountCategoryBand.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountCategoryBandCode").value(hasItem(DEFAULT_AMOUNT_CATEGORY_BAND_CODE)))
            .andExpect(jsonPath("$.[*].amountCategoryBand").value(hasItem(DEFAULT_AMOUNT_CATEGORY_BAND)))
            .andExpect(jsonPath("$.[*].amountCategoryBandDetails").value(hasItem(DEFAULT_AMOUNT_CATEGORY_BAND_DETAILS.toString())));
    }
}
