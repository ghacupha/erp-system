package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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
import io.github.erp.domain.CrbGlCode;
import io.github.erp.repository.CrbGlCodeRepository;
import io.github.erp.repository.search.CrbGlCodeSearchRepository;
import io.github.erp.service.criteria.CrbGlCodeCriteria;
import io.github.erp.service.dto.CrbGlCodeDTO;
import io.github.erp.service.mapper.CrbGlCodeMapper;
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
 * Integration tests for the {@link CrbGlCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbGlCodeResourceIT {

    private static final String DEFAULT_GL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_GL_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_GL_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_GL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_GL_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_INSTITUTION_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_INSTITUTION_CATEGORY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-gl-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-gl-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbGlCodeRepository crbGlCodeRepository;

    @Autowired
    private CrbGlCodeMapper crbGlCodeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbGlCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbGlCodeSearchRepository mockCrbGlCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbGlCodeMockMvc;

    private CrbGlCode crbGlCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbGlCode createEntity(EntityManager em) {
        CrbGlCode crbGlCode = new CrbGlCode()
            .glCode(DEFAULT_GL_CODE)
            .glDescription(DEFAULT_GL_DESCRIPTION)
            .glType(DEFAULT_GL_TYPE)
            .institutionCategory(DEFAULT_INSTITUTION_CATEGORY);
        return crbGlCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbGlCode createUpdatedEntity(EntityManager em) {
        CrbGlCode crbGlCode = new CrbGlCode()
            .glCode(UPDATED_GL_CODE)
            .glDescription(UPDATED_GL_DESCRIPTION)
            .glType(UPDATED_GL_TYPE)
            .institutionCategory(UPDATED_INSTITUTION_CATEGORY);
        return crbGlCode;
    }

    @BeforeEach
    public void initTest() {
        crbGlCode = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbGlCode() throws Exception {
        int databaseSizeBeforeCreate = crbGlCodeRepository.findAll().size();
        // Create the CrbGlCode
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(crbGlCode);
        restCrbGlCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO)))
            .andExpect(status().isCreated());

        // Validate the CrbGlCode in the database
        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeCreate + 1);
        CrbGlCode testCrbGlCode = crbGlCodeList.get(crbGlCodeList.size() - 1);
        assertThat(testCrbGlCode.getGlCode()).isEqualTo(DEFAULT_GL_CODE);
        assertThat(testCrbGlCode.getGlDescription()).isEqualTo(DEFAULT_GL_DESCRIPTION);
        assertThat(testCrbGlCode.getGlType()).isEqualTo(DEFAULT_GL_TYPE);
        assertThat(testCrbGlCode.getInstitutionCategory()).isEqualTo(DEFAULT_INSTITUTION_CATEGORY);

        // Validate the CrbGlCode in Elasticsearch
        verify(mockCrbGlCodeSearchRepository, times(1)).save(testCrbGlCode);
    }

    @Test
    @Transactional
    void createCrbGlCodeWithExistingId() throws Exception {
        // Create the CrbGlCode with an existing ID
        crbGlCode.setId(1L);
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(crbGlCode);

        int databaseSizeBeforeCreate = crbGlCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbGlCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CrbGlCode in the database
        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbGlCode in Elasticsearch
        verify(mockCrbGlCodeSearchRepository, times(0)).save(crbGlCode);
    }

    @Test
    @Transactional
    void checkGlCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbGlCodeRepository.findAll().size();
        // set the field null
        crbGlCode.setGlCode(null);

        // Create the CrbGlCode, which fails.
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(crbGlCode);

        restCrbGlCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO)))
            .andExpect(status().isBadRequest());

        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGlDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbGlCodeRepository.findAll().size();
        // set the field null
        crbGlCode.setGlDescription(null);

        // Create the CrbGlCode, which fails.
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(crbGlCode);

        restCrbGlCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO)))
            .andExpect(status().isBadRequest());

        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGlTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbGlCodeRepository.findAll().size();
        // set the field null
        crbGlCode.setGlType(null);

        // Create the CrbGlCode, which fails.
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(crbGlCode);

        restCrbGlCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO)))
            .andExpect(status().isBadRequest());

        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInstitutionCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbGlCodeRepository.findAll().size();
        // set the field null
        crbGlCode.setInstitutionCategory(null);

        // Create the CrbGlCode, which fails.
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(crbGlCode);

        restCrbGlCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO)))
            .andExpect(status().isBadRequest());

        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbGlCodes() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList
        restCrbGlCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbGlCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].glCode").value(hasItem(DEFAULT_GL_CODE)))
            .andExpect(jsonPath("$.[*].glDescription").value(hasItem(DEFAULT_GL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].glType").value(hasItem(DEFAULT_GL_TYPE)))
            .andExpect(jsonPath("$.[*].institutionCategory").value(hasItem(DEFAULT_INSTITUTION_CATEGORY)));
    }

    @Test
    @Transactional
    void getCrbGlCode() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get the crbGlCode
        restCrbGlCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, crbGlCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbGlCode.getId().intValue()))
            .andExpect(jsonPath("$.glCode").value(DEFAULT_GL_CODE))
            .andExpect(jsonPath("$.glDescription").value(DEFAULT_GL_DESCRIPTION))
            .andExpect(jsonPath("$.glType").value(DEFAULT_GL_TYPE))
            .andExpect(jsonPath("$.institutionCategory").value(DEFAULT_INSTITUTION_CATEGORY));
    }

    @Test
    @Transactional
    void getCrbGlCodesByIdFiltering() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        Long id = crbGlCode.getId();

        defaultCrbGlCodeShouldBeFound("id.equals=" + id);
        defaultCrbGlCodeShouldNotBeFound("id.notEquals=" + id);

        defaultCrbGlCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbGlCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbGlCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbGlCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glCode equals to DEFAULT_GL_CODE
        defaultCrbGlCodeShouldBeFound("glCode.equals=" + DEFAULT_GL_CODE);

        // Get all the crbGlCodeList where glCode equals to UPDATED_GL_CODE
        defaultCrbGlCodeShouldNotBeFound("glCode.equals=" + UPDATED_GL_CODE);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glCode not equals to DEFAULT_GL_CODE
        defaultCrbGlCodeShouldNotBeFound("glCode.notEquals=" + DEFAULT_GL_CODE);

        // Get all the crbGlCodeList where glCode not equals to UPDATED_GL_CODE
        defaultCrbGlCodeShouldBeFound("glCode.notEquals=" + UPDATED_GL_CODE);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glCode in DEFAULT_GL_CODE or UPDATED_GL_CODE
        defaultCrbGlCodeShouldBeFound("glCode.in=" + DEFAULT_GL_CODE + "," + UPDATED_GL_CODE);

        // Get all the crbGlCodeList where glCode equals to UPDATED_GL_CODE
        defaultCrbGlCodeShouldNotBeFound("glCode.in=" + UPDATED_GL_CODE);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glCode is not null
        defaultCrbGlCodeShouldBeFound("glCode.specified=true");

        // Get all the crbGlCodeList where glCode is null
        defaultCrbGlCodeShouldNotBeFound("glCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlCodeContainsSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glCode contains DEFAULT_GL_CODE
        defaultCrbGlCodeShouldBeFound("glCode.contains=" + DEFAULT_GL_CODE);

        // Get all the crbGlCodeList where glCode contains UPDATED_GL_CODE
        defaultCrbGlCodeShouldNotBeFound("glCode.contains=" + UPDATED_GL_CODE);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glCode does not contain DEFAULT_GL_CODE
        defaultCrbGlCodeShouldNotBeFound("glCode.doesNotContain=" + DEFAULT_GL_CODE);

        // Get all the crbGlCodeList where glCode does not contain UPDATED_GL_CODE
        defaultCrbGlCodeShouldBeFound("glCode.doesNotContain=" + UPDATED_GL_CODE);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glDescription equals to DEFAULT_GL_DESCRIPTION
        defaultCrbGlCodeShouldBeFound("glDescription.equals=" + DEFAULT_GL_DESCRIPTION);

        // Get all the crbGlCodeList where glDescription equals to UPDATED_GL_DESCRIPTION
        defaultCrbGlCodeShouldNotBeFound("glDescription.equals=" + UPDATED_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glDescription not equals to DEFAULT_GL_DESCRIPTION
        defaultCrbGlCodeShouldNotBeFound("glDescription.notEquals=" + DEFAULT_GL_DESCRIPTION);

        // Get all the crbGlCodeList where glDescription not equals to UPDATED_GL_DESCRIPTION
        defaultCrbGlCodeShouldBeFound("glDescription.notEquals=" + UPDATED_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glDescription in DEFAULT_GL_DESCRIPTION or UPDATED_GL_DESCRIPTION
        defaultCrbGlCodeShouldBeFound("glDescription.in=" + DEFAULT_GL_DESCRIPTION + "," + UPDATED_GL_DESCRIPTION);

        // Get all the crbGlCodeList where glDescription equals to UPDATED_GL_DESCRIPTION
        defaultCrbGlCodeShouldNotBeFound("glDescription.in=" + UPDATED_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glDescription is not null
        defaultCrbGlCodeShouldBeFound("glDescription.specified=true");

        // Get all the crbGlCodeList where glDescription is null
        defaultCrbGlCodeShouldNotBeFound("glDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlDescriptionContainsSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glDescription contains DEFAULT_GL_DESCRIPTION
        defaultCrbGlCodeShouldBeFound("glDescription.contains=" + DEFAULT_GL_DESCRIPTION);

        // Get all the crbGlCodeList where glDescription contains UPDATED_GL_DESCRIPTION
        defaultCrbGlCodeShouldNotBeFound("glDescription.contains=" + UPDATED_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glDescription does not contain DEFAULT_GL_DESCRIPTION
        defaultCrbGlCodeShouldNotBeFound("glDescription.doesNotContain=" + DEFAULT_GL_DESCRIPTION);

        // Get all the crbGlCodeList where glDescription does not contain UPDATED_GL_DESCRIPTION
        defaultCrbGlCodeShouldBeFound("glDescription.doesNotContain=" + UPDATED_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glType equals to DEFAULT_GL_TYPE
        defaultCrbGlCodeShouldBeFound("glType.equals=" + DEFAULT_GL_TYPE);

        // Get all the crbGlCodeList where glType equals to UPDATED_GL_TYPE
        defaultCrbGlCodeShouldNotBeFound("glType.equals=" + UPDATED_GL_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glType not equals to DEFAULT_GL_TYPE
        defaultCrbGlCodeShouldNotBeFound("glType.notEquals=" + DEFAULT_GL_TYPE);

        // Get all the crbGlCodeList where glType not equals to UPDATED_GL_TYPE
        defaultCrbGlCodeShouldBeFound("glType.notEquals=" + UPDATED_GL_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glType in DEFAULT_GL_TYPE or UPDATED_GL_TYPE
        defaultCrbGlCodeShouldBeFound("glType.in=" + DEFAULT_GL_TYPE + "," + UPDATED_GL_TYPE);

        // Get all the crbGlCodeList where glType equals to UPDATED_GL_TYPE
        defaultCrbGlCodeShouldNotBeFound("glType.in=" + UPDATED_GL_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glType is not null
        defaultCrbGlCodeShouldBeFound("glType.specified=true");

        // Get all the crbGlCodeList where glType is null
        defaultCrbGlCodeShouldNotBeFound("glType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlTypeContainsSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glType contains DEFAULT_GL_TYPE
        defaultCrbGlCodeShouldBeFound("glType.contains=" + DEFAULT_GL_TYPE);

        // Get all the crbGlCodeList where glType contains UPDATED_GL_TYPE
        defaultCrbGlCodeShouldNotBeFound("glType.contains=" + UPDATED_GL_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByGlTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where glType does not contain DEFAULT_GL_TYPE
        defaultCrbGlCodeShouldNotBeFound("glType.doesNotContain=" + DEFAULT_GL_TYPE);

        // Get all the crbGlCodeList where glType does not contain UPDATED_GL_TYPE
        defaultCrbGlCodeShouldBeFound("glType.doesNotContain=" + UPDATED_GL_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByInstitutionCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where institutionCategory equals to DEFAULT_INSTITUTION_CATEGORY
        defaultCrbGlCodeShouldBeFound("institutionCategory.equals=" + DEFAULT_INSTITUTION_CATEGORY);

        // Get all the crbGlCodeList where institutionCategory equals to UPDATED_INSTITUTION_CATEGORY
        defaultCrbGlCodeShouldNotBeFound("institutionCategory.equals=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByInstitutionCategoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where institutionCategory not equals to DEFAULT_INSTITUTION_CATEGORY
        defaultCrbGlCodeShouldNotBeFound("institutionCategory.notEquals=" + DEFAULT_INSTITUTION_CATEGORY);

        // Get all the crbGlCodeList where institutionCategory not equals to UPDATED_INSTITUTION_CATEGORY
        defaultCrbGlCodeShouldBeFound("institutionCategory.notEquals=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByInstitutionCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where institutionCategory in DEFAULT_INSTITUTION_CATEGORY or UPDATED_INSTITUTION_CATEGORY
        defaultCrbGlCodeShouldBeFound("institutionCategory.in=" + DEFAULT_INSTITUTION_CATEGORY + "," + UPDATED_INSTITUTION_CATEGORY);

        // Get all the crbGlCodeList where institutionCategory equals to UPDATED_INSTITUTION_CATEGORY
        defaultCrbGlCodeShouldNotBeFound("institutionCategory.in=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByInstitutionCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where institutionCategory is not null
        defaultCrbGlCodeShouldBeFound("institutionCategory.specified=true");

        // Get all the crbGlCodeList where institutionCategory is null
        defaultCrbGlCodeShouldNotBeFound("institutionCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByInstitutionCategoryContainsSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where institutionCategory contains DEFAULT_INSTITUTION_CATEGORY
        defaultCrbGlCodeShouldBeFound("institutionCategory.contains=" + DEFAULT_INSTITUTION_CATEGORY);

        // Get all the crbGlCodeList where institutionCategory contains UPDATED_INSTITUTION_CATEGORY
        defaultCrbGlCodeShouldNotBeFound("institutionCategory.contains=" + UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCrbGlCodesByInstitutionCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        // Get all the crbGlCodeList where institutionCategory does not contain DEFAULT_INSTITUTION_CATEGORY
        defaultCrbGlCodeShouldNotBeFound("institutionCategory.doesNotContain=" + DEFAULT_INSTITUTION_CATEGORY);

        // Get all the crbGlCodeList where institutionCategory does not contain UPDATED_INSTITUTION_CATEGORY
        defaultCrbGlCodeShouldBeFound("institutionCategory.doesNotContain=" + UPDATED_INSTITUTION_CATEGORY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbGlCodeShouldBeFound(String filter) throws Exception {
        restCrbGlCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbGlCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].glCode").value(hasItem(DEFAULT_GL_CODE)))
            .andExpect(jsonPath("$.[*].glDescription").value(hasItem(DEFAULT_GL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].glType").value(hasItem(DEFAULT_GL_TYPE)))
            .andExpect(jsonPath("$.[*].institutionCategory").value(hasItem(DEFAULT_INSTITUTION_CATEGORY)));

        // Check, that the count call also returns 1
        restCrbGlCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbGlCodeShouldNotBeFound(String filter) throws Exception {
        restCrbGlCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbGlCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbGlCode() throws Exception {
        // Get the crbGlCode
        restCrbGlCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbGlCode() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        int databaseSizeBeforeUpdate = crbGlCodeRepository.findAll().size();

        // Update the crbGlCode
        CrbGlCode updatedCrbGlCode = crbGlCodeRepository.findById(crbGlCode.getId()).get();
        // Disconnect from session so that the updates on updatedCrbGlCode are not directly saved in db
        em.detach(updatedCrbGlCode);
        updatedCrbGlCode
            .glCode(UPDATED_GL_CODE)
            .glDescription(UPDATED_GL_DESCRIPTION)
            .glType(UPDATED_GL_TYPE)
            .institutionCategory(UPDATED_INSTITUTION_CATEGORY);
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(updatedCrbGlCode);

        restCrbGlCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbGlCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbGlCode in the database
        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeUpdate);
        CrbGlCode testCrbGlCode = crbGlCodeList.get(crbGlCodeList.size() - 1);
        assertThat(testCrbGlCode.getGlCode()).isEqualTo(UPDATED_GL_CODE);
        assertThat(testCrbGlCode.getGlDescription()).isEqualTo(UPDATED_GL_DESCRIPTION);
        assertThat(testCrbGlCode.getGlType()).isEqualTo(UPDATED_GL_TYPE);
        assertThat(testCrbGlCode.getInstitutionCategory()).isEqualTo(UPDATED_INSTITUTION_CATEGORY);

        // Validate the CrbGlCode in Elasticsearch
        verify(mockCrbGlCodeSearchRepository).save(testCrbGlCode);
    }

    @Test
    @Transactional
    void putNonExistingCrbGlCode() throws Exception {
        int databaseSizeBeforeUpdate = crbGlCodeRepository.findAll().size();
        crbGlCode.setId(count.incrementAndGet());

        // Create the CrbGlCode
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(crbGlCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbGlCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbGlCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbGlCode in the database
        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbGlCode in Elasticsearch
        verify(mockCrbGlCodeSearchRepository, times(0)).save(crbGlCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbGlCode() throws Exception {
        int databaseSizeBeforeUpdate = crbGlCodeRepository.findAll().size();
        crbGlCode.setId(count.incrementAndGet());

        // Create the CrbGlCode
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(crbGlCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbGlCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbGlCode in the database
        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbGlCode in Elasticsearch
        verify(mockCrbGlCodeSearchRepository, times(0)).save(crbGlCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbGlCode() throws Exception {
        int databaseSizeBeforeUpdate = crbGlCodeRepository.findAll().size();
        crbGlCode.setId(count.incrementAndGet());

        // Create the CrbGlCode
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(crbGlCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbGlCodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbGlCode in the database
        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbGlCode in Elasticsearch
        verify(mockCrbGlCodeSearchRepository, times(0)).save(crbGlCode);
    }

    @Test
    @Transactional
    void partialUpdateCrbGlCodeWithPatch() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        int databaseSizeBeforeUpdate = crbGlCodeRepository.findAll().size();

        // Update the crbGlCode using partial update
        CrbGlCode partialUpdatedCrbGlCode = new CrbGlCode();
        partialUpdatedCrbGlCode.setId(crbGlCode.getId());

        partialUpdatedCrbGlCode.institutionCategory(UPDATED_INSTITUTION_CATEGORY);

        restCrbGlCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbGlCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbGlCode))
            )
            .andExpect(status().isOk());

        // Validate the CrbGlCode in the database
        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeUpdate);
        CrbGlCode testCrbGlCode = crbGlCodeList.get(crbGlCodeList.size() - 1);
        assertThat(testCrbGlCode.getGlCode()).isEqualTo(DEFAULT_GL_CODE);
        assertThat(testCrbGlCode.getGlDescription()).isEqualTo(DEFAULT_GL_DESCRIPTION);
        assertThat(testCrbGlCode.getGlType()).isEqualTo(DEFAULT_GL_TYPE);
        assertThat(testCrbGlCode.getInstitutionCategory()).isEqualTo(UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void fullUpdateCrbGlCodeWithPatch() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        int databaseSizeBeforeUpdate = crbGlCodeRepository.findAll().size();

        // Update the crbGlCode using partial update
        CrbGlCode partialUpdatedCrbGlCode = new CrbGlCode();
        partialUpdatedCrbGlCode.setId(crbGlCode.getId());

        partialUpdatedCrbGlCode
            .glCode(UPDATED_GL_CODE)
            .glDescription(UPDATED_GL_DESCRIPTION)
            .glType(UPDATED_GL_TYPE)
            .institutionCategory(UPDATED_INSTITUTION_CATEGORY);

        restCrbGlCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbGlCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbGlCode))
            )
            .andExpect(status().isOk());

        // Validate the CrbGlCode in the database
        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeUpdate);
        CrbGlCode testCrbGlCode = crbGlCodeList.get(crbGlCodeList.size() - 1);
        assertThat(testCrbGlCode.getGlCode()).isEqualTo(UPDATED_GL_CODE);
        assertThat(testCrbGlCode.getGlDescription()).isEqualTo(UPDATED_GL_DESCRIPTION);
        assertThat(testCrbGlCode.getGlType()).isEqualTo(UPDATED_GL_TYPE);
        assertThat(testCrbGlCode.getInstitutionCategory()).isEqualTo(UPDATED_INSTITUTION_CATEGORY);
    }

    @Test
    @Transactional
    void patchNonExistingCrbGlCode() throws Exception {
        int databaseSizeBeforeUpdate = crbGlCodeRepository.findAll().size();
        crbGlCode.setId(count.incrementAndGet());

        // Create the CrbGlCode
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(crbGlCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbGlCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbGlCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbGlCode in the database
        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbGlCode in Elasticsearch
        verify(mockCrbGlCodeSearchRepository, times(0)).save(crbGlCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbGlCode() throws Exception {
        int databaseSizeBeforeUpdate = crbGlCodeRepository.findAll().size();
        crbGlCode.setId(count.incrementAndGet());

        // Create the CrbGlCode
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(crbGlCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbGlCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbGlCode in the database
        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbGlCode in Elasticsearch
        verify(mockCrbGlCodeSearchRepository, times(0)).save(crbGlCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbGlCode() throws Exception {
        int databaseSizeBeforeUpdate = crbGlCodeRepository.findAll().size();
        crbGlCode.setId(count.incrementAndGet());

        // Create the CrbGlCode
        CrbGlCodeDTO crbGlCodeDTO = crbGlCodeMapper.toDto(crbGlCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbGlCodeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(crbGlCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbGlCode in the database
        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbGlCode in Elasticsearch
        verify(mockCrbGlCodeSearchRepository, times(0)).save(crbGlCode);
    }

    @Test
    @Transactional
    void deleteCrbGlCode() throws Exception {
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);

        int databaseSizeBeforeDelete = crbGlCodeRepository.findAll().size();

        // Delete the crbGlCode
        restCrbGlCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbGlCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbGlCode> crbGlCodeList = crbGlCodeRepository.findAll();
        assertThat(crbGlCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbGlCode in Elasticsearch
        verify(mockCrbGlCodeSearchRepository, times(1)).deleteById(crbGlCode.getId());
    }

    @Test
    @Transactional
    void searchCrbGlCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbGlCodeRepository.saveAndFlush(crbGlCode);
        when(mockCrbGlCodeSearchRepository.search("id:" + crbGlCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbGlCode), PageRequest.of(0, 1), 1));

        // Search the crbGlCode
        restCrbGlCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbGlCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbGlCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].glCode").value(hasItem(DEFAULT_GL_CODE)))
            .andExpect(jsonPath("$.[*].glDescription").value(hasItem(DEFAULT_GL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].glType").value(hasItem(DEFAULT_GL_TYPE)))
            .andExpect(jsonPath("$.[*].institutionCategory").value(hasItem(DEFAULT_INSTITUTION_CATEGORY)));
    }
}
