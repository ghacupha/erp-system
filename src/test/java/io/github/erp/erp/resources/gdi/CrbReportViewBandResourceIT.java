package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.CrbReportViewBand;
import io.github.erp.repository.CrbReportViewBandRepository;
import io.github.erp.repository.search.CrbReportViewBandSearchRepository;
import io.github.erp.service.dto.CrbReportViewBandDTO;
import io.github.erp.service.mapper.CrbReportViewBandMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.CrbReportViewBandResource;
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
 * Integration tests for the {@link CrbReportViewBandResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CrbReportViewBandResourceIT {

    private static final String DEFAULT_REPORT_VIEW_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_VIEW_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_REPORT_VIEW_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_VIEW_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_REPORT_VIEW_CATEGORY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_VIEW_CATEGORY_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/crb-report-view-bands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/crb-report-view-bands";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbReportViewBandRepository crbReportViewBandRepository;

    @Autowired
    private CrbReportViewBandMapper crbReportViewBandMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbReportViewBandSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbReportViewBandSearchRepository mockCrbReportViewBandSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbReportViewBandMockMvc;

    private CrbReportViewBand crbReportViewBand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbReportViewBand createEntity(EntityManager em) {
        CrbReportViewBand crbReportViewBand = new CrbReportViewBand()
            .reportViewCode(DEFAULT_REPORT_VIEW_CODE)
            .reportViewCategory(DEFAULT_REPORT_VIEW_CATEGORY)
            .reportViewCategoryDescription(DEFAULT_REPORT_VIEW_CATEGORY_DESCRIPTION);
        return crbReportViewBand;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbReportViewBand createUpdatedEntity(EntityManager em) {
        CrbReportViewBand crbReportViewBand = new CrbReportViewBand()
            .reportViewCode(UPDATED_REPORT_VIEW_CODE)
            .reportViewCategory(UPDATED_REPORT_VIEW_CATEGORY)
            .reportViewCategoryDescription(UPDATED_REPORT_VIEW_CATEGORY_DESCRIPTION);
        return crbReportViewBand;
    }

    @BeforeEach
    public void initTest() {
        crbReportViewBand = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbReportViewBand() throws Exception {
        int databaseSizeBeforeCreate = crbReportViewBandRepository.findAll().size();
        // Create the CrbReportViewBand
        CrbReportViewBandDTO crbReportViewBandDTO = crbReportViewBandMapper.toDto(crbReportViewBand);
        restCrbReportViewBandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportViewBandDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbReportViewBand in the database
        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeCreate + 1);
        CrbReportViewBand testCrbReportViewBand = crbReportViewBandList.get(crbReportViewBandList.size() - 1);
        assertThat(testCrbReportViewBand.getReportViewCode()).isEqualTo(DEFAULT_REPORT_VIEW_CODE);
        assertThat(testCrbReportViewBand.getReportViewCategory()).isEqualTo(DEFAULT_REPORT_VIEW_CATEGORY);
        assertThat(testCrbReportViewBand.getReportViewCategoryDescription()).isEqualTo(DEFAULT_REPORT_VIEW_CATEGORY_DESCRIPTION);

        // Validate the CrbReportViewBand in Elasticsearch
        verify(mockCrbReportViewBandSearchRepository, times(1)).save(testCrbReportViewBand);
    }

    @Test
    @Transactional
    void createCrbReportViewBandWithExistingId() throws Exception {
        // Create the CrbReportViewBand with an existing ID
        crbReportViewBand.setId(1L);
        CrbReportViewBandDTO crbReportViewBandDTO = crbReportViewBandMapper.toDto(crbReportViewBand);

        int databaseSizeBeforeCreate = crbReportViewBandRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbReportViewBandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportViewBandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbReportViewBand in the database
        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbReportViewBand in Elasticsearch
        verify(mockCrbReportViewBandSearchRepository, times(0)).save(crbReportViewBand);
    }

    @Test
    @Transactional
    void checkReportViewCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbReportViewBandRepository.findAll().size();
        // set the field null
        crbReportViewBand.setReportViewCode(null);

        // Create the CrbReportViewBand, which fails.
        CrbReportViewBandDTO crbReportViewBandDTO = crbReportViewBandMapper.toDto(crbReportViewBand);

        restCrbReportViewBandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportViewBandDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportViewCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbReportViewBandRepository.findAll().size();
        // set the field null
        crbReportViewBand.setReportViewCategory(null);

        // Create the CrbReportViewBand, which fails.
        CrbReportViewBandDTO crbReportViewBandDTO = crbReportViewBandMapper.toDto(crbReportViewBand);

        restCrbReportViewBandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportViewBandDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbReportViewBands() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList
        restCrbReportViewBandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbReportViewBand.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportViewCode").value(hasItem(DEFAULT_REPORT_VIEW_CODE)))
            .andExpect(jsonPath("$.[*].reportViewCategory").value(hasItem(DEFAULT_REPORT_VIEW_CATEGORY)))
            .andExpect(jsonPath("$.[*].reportViewCategoryDescription").value(hasItem(DEFAULT_REPORT_VIEW_CATEGORY_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getCrbReportViewBand() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get the crbReportViewBand
        restCrbReportViewBandMockMvc
            .perform(get(ENTITY_API_URL_ID, crbReportViewBand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbReportViewBand.getId().intValue()))
            .andExpect(jsonPath("$.reportViewCode").value(DEFAULT_REPORT_VIEW_CODE))
            .andExpect(jsonPath("$.reportViewCategory").value(DEFAULT_REPORT_VIEW_CATEGORY))
            .andExpect(jsonPath("$.reportViewCategoryDescription").value(DEFAULT_REPORT_VIEW_CATEGORY_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getCrbReportViewBandsByIdFiltering() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        Long id = crbReportViewBand.getId();

        defaultCrbReportViewBandShouldBeFound("id.equals=" + id);
        defaultCrbReportViewBandShouldNotBeFound("id.notEquals=" + id);

        defaultCrbReportViewBandShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbReportViewBandShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbReportViewBandShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbReportViewBandShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbReportViewBandsByReportViewCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList where reportViewCode equals to DEFAULT_REPORT_VIEW_CODE
        defaultCrbReportViewBandShouldBeFound("reportViewCode.equals=" + DEFAULT_REPORT_VIEW_CODE);

        // Get all the crbReportViewBandList where reportViewCode equals to UPDATED_REPORT_VIEW_CODE
        defaultCrbReportViewBandShouldNotBeFound("reportViewCode.equals=" + UPDATED_REPORT_VIEW_CODE);
    }

    @Test
    @Transactional
    void getAllCrbReportViewBandsByReportViewCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList where reportViewCode not equals to DEFAULT_REPORT_VIEW_CODE
        defaultCrbReportViewBandShouldNotBeFound("reportViewCode.notEquals=" + DEFAULT_REPORT_VIEW_CODE);

        // Get all the crbReportViewBandList where reportViewCode not equals to UPDATED_REPORT_VIEW_CODE
        defaultCrbReportViewBandShouldBeFound("reportViewCode.notEquals=" + UPDATED_REPORT_VIEW_CODE);
    }

    @Test
    @Transactional
    void getAllCrbReportViewBandsByReportViewCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList where reportViewCode in DEFAULT_REPORT_VIEW_CODE or UPDATED_REPORT_VIEW_CODE
        defaultCrbReportViewBandShouldBeFound("reportViewCode.in=" + DEFAULT_REPORT_VIEW_CODE + "," + UPDATED_REPORT_VIEW_CODE);

        // Get all the crbReportViewBandList where reportViewCode equals to UPDATED_REPORT_VIEW_CODE
        defaultCrbReportViewBandShouldNotBeFound("reportViewCode.in=" + UPDATED_REPORT_VIEW_CODE);
    }

    @Test
    @Transactional
    void getAllCrbReportViewBandsByReportViewCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList where reportViewCode is not null
        defaultCrbReportViewBandShouldBeFound("reportViewCode.specified=true");

        // Get all the crbReportViewBandList where reportViewCode is null
        defaultCrbReportViewBandShouldNotBeFound("reportViewCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbReportViewBandsByReportViewCodeContainsSomething() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList where reportViewCode contains DEFAULT_REPORT_VIEW_CODE
        defaultCrbReportViewBandShouldBeFound("reportViewCode.contains=" + DEFAULT_REPORT_VIEW_CODE);

        // Get all the crbReportViewBandList where reportViewCode contains UPDATED_REPORT_VIEW_CODE
        defaultCrbReportViewBandShouldNotBeFound("reportViewCode.contains=" + UPDATED_REPORT_VIEW_CODE);
    }

    @Test
    @Transactional
    void getAllCrbReportViewBandsByReportViewCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList where reportViewCode does not contain DEFAULT_REPORT_VIEW_CODE
        defaultCrbReportViewBandShouldNotBeFound("reportViewCode.doesNotContain=" + DEFAULT_REPORT_VIEW_CODE);

        // Get all the crbReportViewBandList where reportViewCode does not contain UPDATED_REPORT_VIEW_CODE
        defaultCrbReportViewBandShouldBeFound("reportViewCode.doesNotContain=" + UPDATED_REPORT_VIEW_CODE);
    }

    @Test
    @Transactional
    void getAllCrbReportViewBandsByReportViewCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList where reportViewCategory equals to DEFAULT_REPORT_VIEW_CATEGORY
        defaultCrbReportViewBandShouldBeFound("reportViewCategory.equals=" + DEFAULT_REPORT_VIEW_CATEGORY);

        // Get all the crbReportViewBandList where reportViewCategory equals to UPDATED_REPORT_VIEW_CATEGORY
        defaultCrbReportViewBandShouldNotBeFound("reportViewCategory.equals=" + UPDATED_REPORT_VIEW_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbReportViewBandsByReportViewCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList where reportViewCategory not equals to DEFAULT_REPORT_VIEW_CATEGORY
        defaultCrbReportViewBandShouldNotBeFound("reportViewCategory.notEquals=" + DEFAULT_REPORT_VIEW_CATEGORY);

        // Get all the crbReportViewBandList where reportViewCategory not equals to UPDATED_REPORT_VIEW_CATEGORY
        defaultCrbReportViewBandShouldBeFound("reportViewCategory.notEquals=" + UPDATED_REPORT_VIEW_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbReportViewBandsByReportViewCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList where reportViewCategory in DEFAULT_REPORT_VIEW_CATEGORY or UPDATED_REPORT_VIEW_CATEGORY
        defaultCrbReportViewBandShouldBeFound("reportViewCategory.in=" + DEFAULT_REPORT_VIEW_CATEGORY + "," + UPDATED_REPORT_VIEW_CATEGORY);

        // Get all the crbReportViewBandList where reportViewCategory equals to UPDATED_REPORT_VIEW_CATEGORY
        defaultCrbReportViewBandShouldNotBeFound("reportViewCategory.in=" + UPDATED_REPORT_VIEW_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbReportViewBandsByReportViewCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList where reportViewCategory is not null
        defaultCrbReportViewBandShouldBeFound("reportViewCategory.specified=true");

        // Get all the crbReportViewBandList where reportViewCategory is null
        defaultCrbReportViewBandShouldNotBeFound("reportViewCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbReportViewBandsByReportViewCategoryContainsSomething() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList where reportViewCategory contains DEFAULT_REPORT_VIEW_CATEGORY
        defaultCrbReportViewBandShouldBeFound("reportViewCategory.contains=" + DEFAULT_REPORT_VIEW_CATEGORY);

        // Get all the crbReportViewBandList where reportViewCategory contains UPDATED_REPORT_VIEW_CATEGORY
        defaultCrbReportViewBandShouldNotBeFound("reportViewCategory.contains=" + UPDATED_REPORT_VIEW_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbReportViewBandsByReportViewCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        // Get all the crbReportViewBandList where reportViewCategory does not contain DEFAULT_REPORT_VIEW_CATEGORY
        defaultCrbReportViewBandShouldNotBeFound("reportViewCategory.doesNotContain=" + DEFAULT_REPORT_VIEW_CATEGORY);

        // Get all the crbReportViewBandList where reportViewCategory does not contain UPDATED_REPORT_VIEW_CATEGORY
        defaultCrbReportViewBandShouldBeFound("reportViewCategory.doesNotContain=" + UPDATED_REPORT_VIEW_CATEGORY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbReportViewBandShouldBeFound(String filter) throws Exception {
        restCrbReportViewBandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbReportViewBand.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportViewCode").value(hasItem(DEFAULT_REPORT_VIEW_CODE)))
            .andExpect(jsonPath("$.[*].reportViewCategory").value(hasItem(DEFAULT_REPORT_VIEW_CATEGORY)))
            .andExpect(jsonPath("$.[*].reportViewCategoryDescription").value(hasItem(DEFAULT_REPORT_VIEW_CATEGORY_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restCrbReportViewBandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbReportViewBandShouldNotBeFound(String filter) throws Exception {
        restCrbReportViewBandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbReportViewBandMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbReportViewBand() throws Exception {
        // Get the crbReportViewBand
        restCrbReportViewBandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbReportViewBand() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        int databaseSizeBeforeUpdate = crbReportViewBandRepository.findAll().size();

        // Update the crbReportViewBand
        CrbReportViewBand updatedCrbReportViewBand = crbReportViewBandRepository.findById(crbReportViewBand.getId()).get();
        // Disconnect from session so that the updates on updatedCrbReportViewBand are not directly saved in db
        em.detach(updatedCrbReportViewBand);
        updatedCrbReportViewBand
            .reportViewCode(UPDATED_REPORT_VIEW_CODE)
            .reportViewCategory(UPDATED_REPORT_VIEW_CATEGORY)
            .reportViewCategoryDescription(UPDATED_REPORT_VIEW_CATEGORY_DESCRIPTION);
        CrbReportViewBandDTO crbReportViewBandDTO = crbReportViewBandMapper.toDto(updatedCrbReportViewBand);

        restCrbReportViewBandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbReportViewBandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportViewBandDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbReportViewBand in the database
        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeUpdate);
        CrbReportViewBand testCrbReportViewBand = crbReportViewBandList.get(crbReportViewBandList.size() - 1);
        assertThat(testCrbReportViewBand.getReportViewCode()).isEqualTo(UPDATED_REPORT_VIEW_CODE);
        assertThat(testCrbReportViewBand.getReportViewCategory()).isEqualTo(UPDATED_REPORT_VIEW_CATEGORY);
        assertThat(testCrbReportViewBand.getReportViewCategoryDescription()).isEqualTo(UPDATED_REPORT_VIEW_CATEGORY_DESCRIPTION);

        // Validate the CrbReportViewBand in Elasticsearch
        verify(mockCrbReportViewBandSearchRepository).save(testCrbReportViewBand);
    }

    @Test
    @Transactional
    void putNonExistingCrbReportViewBand() throws Exception {
        int databaseSizeBeforeUpdate = crbReportViewBandRepository.findAll().size();
        crbReportViewBand.setId(count.incrementAndGet());

        // Create the CrbReportViewBand
        CrbReportViewBandDTO crbReportViewBandDTO = crbReportViewBandMapper.toDto(crbReportViewBand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbReportViewBandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbReportViewBandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportViewBandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbReportViewBand in the database
        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbReportViewBand in Elasticsearch
        verify(mockCrbReportViewBandSearchRepository, times(0)).save(crbReportViewBand);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbReportViewBand() throws Exception {
        int databaseSizeBeforeUpdate = crbReportViewBandRepository.findAll().size();
        crbReportViewBand.setId(count.incrementAndGet());

        // Create the CrbReportViewBand
        CrbReportViewBandDTO crbReportViewBandDTO = crbReportViewBandMapper.toDto(crbReportViewBand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbReportViewBandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbReportViewBandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbReportViewBand in the database
        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbReportViewBand in Elasticsearch
        verify(mockCrbReportViewBandSearchRepository, times(0)).save(crbReportViewBand);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbReportViewBand() throws Exception {
        int databaseSizeBeforeUpdate = crbReportViewBandRepository.findAll().size();
        crbReportViewBand.setId(count.incrementAndGet());

        // Create the CrbReportViewBand
        CrbReportViewBandDTO crbReportViewBandDTO = crbReportViewBandMapper.toDto(crbReportViewBand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbReportViewBandMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbReportViewBandDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbReportViewBand in the database
        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbReportViewBand in Elasticsearch
        verify(mockCrbReportViewBandSearchRepository, times(0)).save(crbReportViewBand);
    }

    @Test
    @Transactional
    void partialUpdateCrbReportViewBandWithPatch() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        int databaseSizeBeforeUpdate = crbReportViewBandRepository.findAll().size();

        // Update the crbReportViewBand using partial update
        CrbReportViewBand partialUpdatedCrbReportViewBand = new CrbReportViewBand();
        partialUpdatedCrbReportViewBand.setId(crbReportViewBand.getId());

        restCrbReportViewBandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbReportViewBand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbReportViewBand))
            )
            .andExpect(status().isOk());

        // Validate the CrbReportViewBand in the database
        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeUpdate);
        CrbReportViewBand testCrbReportViewBand = crbReportViewBandList.get(crbReportViewBandList.size() - 1);
        assertThat(testCrbReportViewBand.getReportViewCode()).isEqualTo(DEFAULT_REPORT_VIEW_CODE);
        assertThat(testCrbReportViewBand.getReportViewCategory()).isEqualTo(DEFAULT_REPORT_VIEW_CATEGORY);
        assertThat(testCrbReportViewBand.getReportViewCategoryDescription()).isEqualTo(DEFAULT_REPORT_VIEW_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCrbReportViewBandWithPatch() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        int databaseSizeBeforeUpdate = crbReportViewBandRepository.findAll().size();

        // Update the crbReportViewBand using partial update
        CrbReportViewBand partialUpdatedCrbReportViewBand = new CrbReportViewBand();
        partialUpdatedCrbReportViewBand.setId(crbReportViewBand.getId());

        partialUpdatedCrbReportViewBand
            .reportViewCode(UPDATED_REPORT_VIEW_CODE)
            .reportViewCategory(UPDATED_REPORT_VIEW_CATEGORY)
            .reportViewCategoryDescription(UPDATED_REPORT_VIEW_CATEGORY_DESCRIPTION);

        restCrbReportViewBandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbReportViewBand.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbReportViewBand))
            )
            .andExpect(status().isOk());

        // Validate the CrbReportViewBand in the database
        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeUpdate);
        CrbReportViewBand testCrbReportViewBand = crbReportViewBandList.get(crbReportViewBandList.size() - 1);
        assertThat(testCrbReportViewBand.getReportViewCode()).isEqualTo(UPDATED_REPORT_VIEW_CODE);
        assertThat(testCrbReportViewBand.getReportViewCategory()).isEqualTo(UPDATED_REPORT_VIEW_CATEGORY);
        assertThat(testCrbReportViewBand.getReportViewCategoryDescription()).isEqualTo(UPDATED_REPORT_VIEW_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCrbReportViewBand() throws Exception {
        int databaseSizeBeforeUpdate = crbReportViewBandRepository.findAll().size();
        crbReportViewBand.setId(count.incrementAndGet());

        // Create the CrbReportViewBand
        CrbReportViewBandDTO crbReportViewBandDTO = crbReportViewBandMapper.toDto(crbReportViewBand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbReportViewBandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbReportViewBandDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbReportViewBandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbReportViewBand in the database
        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbReportViewBand in Elasticsearch
        verify(mockCrbReportViewBandSearchRepository, times(0)).save(crbReportViewBand);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbReportViewBand() throws Exception {
        int databaseSizeBeforeUpdate = crbReportViewBandRepository.findAll().size();
        crbReportViewBand.setId(count.incrementAndGet());

        // Create the CrbReportViewBand
        CrbReportViewBandDTO crbReportViewBandDTO = crbReportViewBandMapper.toDto(crbReportViewBand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbReportViewBandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbReportViewBandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbReportViewBand in the database
        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbReportViewBand in Elasticsearch
        verify(mockCrbReportViewBandSearchRepository, times(0)).save(crbReportViewBand);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbReportViewBand() throws Exception {
        int databaseSizeBeforeUpdate = crbReportViewBandRepository.findAll().size();
        crbReportViewBand.setId(count.incrementAndGet());

        // Create the CrbReportViewBand
        CrbReportViewBandDTO crbReportViewBandDTO = crbReportViewBandMapper.toDto(crbReportViewBand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbReportViewBandMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbReportViewBandDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbReportViewBand in the database
        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbReportViewBand in Elasticsearch
        verify(mockCrbReportViewBandSearchRepository, times(0)).save(crbReportViewBand);
    }

    @Test
    @Transactional
    void deleteCrbReportViewBand() throws Exception {
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);

        int databaseSizeBeforeDelete = crbReportViewBandRepository.findAll().size();

        // Delete the crbReportViewBand
        restCrbReportViewBandMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbReportViewBand.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbReportViewBand> crbReportViewBandList = crbReportViewBandRepository.findAll();
        assertThat(crbReportViewBandList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbReportViewBand in Elasticsearch
        verify(mockCrbReportViewBandSearchRepository, times(1)).deleteById(crbReportViewBand.getId());
    }

    @Test
    @Transactional
    void searchCrbReportViewBand() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbReportViewBandRepository.saveAndFlush(crbReportViewBand);
        when(mockCrbReportViewBandSearchRepository.search("id:" + crbReportViewBand.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbReportViewBand), PageRequest.of(0, 1), 1));

        // Search the crbReportViewBand
        restCrbReportViewBandMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbReportViewBand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbReportViewBand.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportViewCode").value(hasItem(DEFAULT_REPORT_VIEW_CODE)))
            .andExpect(jsonPath("$.[*].reportViewCategory").value(hasItem(DEFAULT_REPORT_VIEW_CATEGORY)))
            .andExpect(jsonPath("$.[*].reportViewCategoryDescription").value(hasItem(DEFAULT_REPORT_VIEW_CATEGORY_DESCRIPTION.toString())));
    }
}
