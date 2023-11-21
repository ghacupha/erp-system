package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
import io.github.erp.domain.SnaSectorCode;
import io.github.erp.repository.SnaSectorCodeRepository;
import io.github.erp.repository.search.SnaSectorCodeSearchRepository;
import io.github.erp.service.criteria.SnaSectorCodeCriteria;
import io.github.erp.service.dto.SnaSectorCodeDTO;
import io.github.erp.service.mapper.SnaSectorCodeMapper;
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
 * Integration tests for the {@link SnaSectorCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SnaSectorCodeResourceIT {

    private static final String DEFAULT_SECTOR_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SECTOR_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_SECTOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_SECTOR_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_SECTOR_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_SECTOR_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_SECTOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_SECTOR_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_SECTOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUB_SECTOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_SUB_SECTOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_SUB_SECTOR_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_SUB_SECTOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUB_SUB_SECTOR_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sna-sector-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/sna-sector-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SnaSectorCodeRepository snaSectorCodeRepository;

    @Autowired
    private SnaSectorCodeMapper snaSectorCodeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SnaSectorCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private SnaSectorCodeSearchRepository mockSnaSectorCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSnaSectorCodeMockMvc;

    private SnaSectorCode snaSectorCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SnaSectorCode createEntity(EntityManager em) {
        SnaSectorCode snaSectorCode = new SnaSectorCode()
            .sectorTypeCode(DEFAULT_SECTOR_TYPE_CODE)
            .mainSectorCode(DEFAULT_MAIN_SECTOR_CODE)
            .mainSectorTypeName(DEFAULT_MAIN_SECTOR_TYPE_NAME)
            .subSectorCode(DEFAULT_SUB_SECTOR_CODE)
            .subSectorName(DEFAULT_SUB_SECTOR_NAME)
            .subSubSectorCode(DEFAULT_SUB_SUB_SECTOR_CODE)
            .subSubSectorName(DEFAULT_SUB_SUB_SECTOR_NAME);
        return snaSectorCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SnaSectorCode createUpdatedEntity(EntityManager em) {
        SnaSectorCode snaSectorCode = new SnaSectorCode()
            .sectorTypeCode(UPDATED_SECTOR_TYPE_CODE)
            .mainSectorCode(UPDATED_MAIN_SECTOR_CODE)
            .mainSectorTypeName(UPDATED_MAIN_SECTOR_TYPE_NAME)
            .subSectorCode(UPDATED_SUB_SECTOR_CODE)
            .subSectorName(UPDATED_SUB_SECTOR_NAME)
            .subSubSectorCode(UPDATED_SUB_SUB_SECTOR_CODE)
            .subSubSectorName(UPDATED_SUB_SUB_SECTOR_NAME);
        return snaSectorCode;
    }

    @BeforeEach
    public void initTest() {
        snaSectorCode = createEntity(em);
    }

    @Test
    @Transactional
    void createSnaSectorCode() throws Exception {
        int databaseSizeBeforeCreate = snaSectorCodeRepository.findAll().size();
        // Create the SnaSectorCode
        SnaSectorCodeDTO snaSectorCodeDTO = snaSectorCodeMapper.toDto(snaSectorCode);
        restSnaSectorCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snaSectorCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SnaSectorCode in the database
        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeCreate + 1);
        SnaSectorCode testSnaSectorCode = snaSectorCodeList.get(snaSectorCodeList.size() - 1);
        assertThat(testSnaSectorCode.getSectorTypeCode()).isEqualTo(DEFAULT_SECTOR_TYPE_CODE);
        assertThat(testSnaSectorCode.getMainSectorCode()).isEqualTo(DEFAULT_MAIN_SECTOR_CODE);
        assertThat(testSnaSectorCode.getMainSectorTypeName()).isEqualTo(DEFAULT_MAIN_SECTOR_TYPE_NAME);
        assertThat(testSnaSectorCode.getSubSectorCode()).isEqualTo(DEFAULT_SUB_SECTOR_CODE);
        assertThat(testSnaSectorCode.getSubSectorName()).isEqualTo(DEFAULT_SUB_SECTOR_NAME);
        assertThat(testSnaSectorCode.getSubSubSectorCode()).isEqualTo(DEFAULT_SUB_SUB_SECTOR_CODE);
        assertThat(testSnaSectorCode.getSubSubSectorName()).isEqualTo(DEFAULT_SUB_SUB_SECTOR_NAME);

        // Validate the SnaSectorCode in Elasticsearch
        verify(mockSnaSectorCodeSearchRepository, times(1)).save(testSnaSectorCode);
    }

    @Test
    @Transactional
    void createSnaSectorCodeWithExistingId() throws Exception {
        // Create the SnaSectorCode with an existing ID
        snaSectorCode.setId(1L);
        SnaSectorCodeDTO snaSectorCodeDTO = snaSectorCodeMapper.toDto(snaSectorCode);

        int databaseSizeBeforeCreate = snaSectorCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSnaSectorCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snaSectorCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SnaSectorCode in the database
        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the SnaSectorCode in Elasticsearch
        verify(mockSnaSectorCodeSearchRepository, times(0)).save(snaSectorCode);
    }

    @Test
    @Transactional
    void checkSectorTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = snaSectorCodeRepository.findAll().size();
        // set the field null
        snaSectorCode.setSectorTypeCode(null);

        // Create the SnaSectorCode, which fails.
        SnaSectorCodeDTO snaSectorCodeDTO = snaSectorCodeMapper.toDto(snaSectorCode);

        restSnaSectorCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snaSectorCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodes() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList
        restSnaSectorCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(snaSectorCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectorTypeCode").value(hasItem(DEFAULT_SECTOR_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].mainSectorCode").value(hasItem(DEFAULT_MAIN_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].mainSectorTypeName").value(hasItem(DEFAULT_MAIN_SECTOR_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].subSectorCode").value(hasItem(DEFAULT_SUB_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].subSectorName").value(hasItem(DEFAULT_SUB_SECTOR_NAME)))
            .andExpect(jsonPath("$.[*].subSubSectorCode").value(hasItem(DEFAULT_SUB_SUB_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].subSubSectorName").value(hasItem(DEFAULT_SUB_SUB_SECTOR_NAME)));
    }

    @Test
    @Transactional
    void getSnaSectorCode() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get the snaSectorCode
        restSnaSectorCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, snaSectorCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(snaSectorCode.getId().intValue()))
            .andExpect(jsonPath("$.sectorTypeCode").value(DEFAULT_SECTOR_TYPE_CODE))
            .andExpect(jsonPath("$.mainSectorCode").value(DEFAULT_MAIN_SECTOR_CODE))
            .andExpect(jsonPath("$.mainSectorTypeName").value(DEFAULT_MAIN_SECTOR_TYPE_NAME))
            .andExpect(jsonPath("$.subSectorCode").value(DEFAULT_SUB_SECTOR_CODE))
            .andExpect(jsonPath("$.subSectorName").value(DEFAULT_SUB_SECTOR_NAME))
            .andExpect(jsonPath("$.subSubSectorCode").value(DEFAULT_SUB_SUB_SECTOR_CODE))
            .andExpect(jsonPath("$.subSubSectorName").value(DEFAULT_SUB_SUB_SECTOR_NAME));
    }

    @Test
    @Transactional
    void getSnaSectorCodesByIdFiltering() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        Long id = snaSectorCode.getId();

        defaultSnaSectorCodeShouldBeFound("id.equals=" + id);
        defaultSnaSectorCodeShouldNotBeFound("id.notEquals=" + id);

        defaultSnaSectorCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSnaSectorCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultSnaSectorCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSnaSectorCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySectorTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where sectorTypeCode equals to DEFAULT_SECTOR_TYPE_CODE
        defaultSnaSectorCodeShouldBeFound("sectorTypeCode.equals=" + DEFAULT_SECTOR_TYPE_CODE);

        // Get all the snaSectorCodeList where sectorTypeCode equals to UPDATED_SECTOR_TYPE_CODE
        defaultSnaSectorCodeShouldNotBeFound("sectorTypeCode.equals=" + UPDATED_SECTOR_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySectorTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where sectorTypeCode not equals to DEFAULT_SECTOR_TYPE_CODE
        defaultSnaSectorCodeShouldNotBeFound("sectorTypeCode.notEquals=" + DEFAULT_SECTOR_TYPE_CODE);

        // Get all the snaSectorCodeList where sectorTypeCode not equals to UPDATED_SECTOR_TYPE_CODE
        defaultSnaSectorCodeShouldBeFound("sectorTypeCode.notEquals=" + UPDATED_SECTOR_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySectorTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where sectorTypeCode in DEFAULT_SECTOR_TYPE_CODE or UPDATED_SECTOR_TYPE_CODE
        defaultSnaSectorCodeShouldBeFound("sectorTypeCode.in=" + DEFAULT_SECTOR_TYPE_CODE + "," + UPDATED_SECTOR_TYPE_CODE);

        // Get all the snaSectorCodeList where sectorTypeCode equals to UPDATED_SECTOR_TYPE_CODE
        defaultSnaSectorCodeShouldNotBeFound("sectorTypeCode.in=" + UPDATED_SECTOR_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySectorTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where sectorTypeCode is not null
        defaultSnaSectorCodeShouldBeFound("sectorTypeCode.specified=true");

        // Get all the snaSectorCodeList where sectorTypeCode is null
        defaultSnaSectorCodeShouldNotBeFound("sectorTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySectorTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where sectorTypeCode contains DEFAULT_SECTOR_TYPE_CODE
        defaultSnaSectorCodeShouldBeFound("sectorTypeCode.contains=" + DEFAULT_SECTOR_TYPE_CODE);

        // Get all the snaSectorCodeList where sectorTypeCode contains UPDATED_SECTOR_TYPE_CODE
        defaultSnaSectorCodeShouldNotBeFound("sectorTypeCode.contains=" + UPDATED_SECTOR_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySectorTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where sectorTypeCode does not contain DEFAULT_SECTOR_TYPE_CODE
        defaultSnaSectorCodeShouldNotBeFound("sectorTypeCode.doesNotContain=" + DEFAULT_SECTOR_TYPE_CODE);

        // Get all the snaSectorCodeList where sectorTypeCode does not contain UPDATED_SECTOR_TYPE_CODE
        defaultSnaSectorCodeShouldBeFound("sectorTypeCode.doesNotContain=" + UPDATED_SECTOR_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesByMainSectorCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where mainSectorCode equals to DEFAULT_MAIN_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("mainSectorCode.equals=" + DEFAULT_MAIN_SECTOR_CODE);

        // Get all the snaSectorCodeList where mainSectorCode equals to UPDATED_MAIN_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("mainSectorCode.equals=" + UPDATED_MAIN_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesByMainSectorCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where mainSectorCode not equals to DEFAULT_MAIN_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("mainSectorCode.notEquals=" + DEFAULT_MAIN_SECTOR_CODE);

        // Get all the snaSectorCodeList where mainSectorCode not equals to UPDATED_MAIN_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("mainSectorCode.notEquals=" + UPDATED_MAIN_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesByMainSectorCodeIsInShouldWork() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where mainSectorCode in DEFAULT_MAIN_SECTOR_CODE or UPDATED_MAIN_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("mainSectorCode.in=" + DEFAULT_MAIN_SECTOR_CODE + "," + UPDATED_MAIN_SECTOR_CODE);

        // Get all the snaSectorCodeList where mainSectorCode equals to UPDATED_MAIN_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("mainSectorCode.in=" + UPDATED_MAIN_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesByMainSectorCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where mainSectorCode is not null
        defaultSnaSectorCodeShouldBeFound("mainSectorCode.specified=true");

        // Get all the snaSectorCodeList where mainSectorCode is null
        defaultSnaSectorCodeShouldNotBeFound("mainSectorCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesByMainSectorCodeContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where mainSectorCode contains DEFAULT_MAIN_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("mainSectorCode.contains=" + DEFAULT_MAIN_SECTOR_CODE);

        // Get all the snaSectorCodeList where mainSectorCode contains UPDATED_MAIN_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("mainSectorCode.contains=" + UPDATED_MAIN_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesByMainSectorCodeNotContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where mainSectorCode does not contain DEFAULT_MAIN_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("mainSectorCode.doesNotContain=" + DEFAULT_MAIN_SECTOR_CODE);

        // Get all the snaSectorCodeList where mainSectorCode does not contain UPDATED_MAIN_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("mainSectorCode.doesNotContain=" + UPDATED_MAIN_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesByMainSectorTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where mainSectorTypeName equals to DEFAULT_MAIN_SECTOR_TYPE_NAME
        defaultSnaSectorCodeShouldBeFound("mainSectorTypeName.equals=" + DEFAULT_MAIN_SECTOR_TYPE_NAME);

        // Get all the snaSectorCodeList where mainSectorTypeName equals to UPDATED_MAIN_SECTOR_TYPE_NAME
        defaultSnaSectorCodeShouldNotBeFound("mainSectorTypeName.equals=" + UPDATED_MAIN_SECTOR_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesByMainSectorTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where mainSectorTypeName not equals to DEFAULT_MAIN_SECTOR_TYPE_NAME
        defaultSnaSectorCodeShouldNotBeFound("mainSectorTypeName.notEquals=" + DEFAULT_MAIN_SECTOR_TYPE_NAME);

        // Get all the snaSectorCodeList where mainSectorTypeName not equals to UPDATED_MAIN_SECTOR_TYPE_NAME
        defaultSnaSectorCodeShouldBeFound("mainSectorTypeName.notEquals=" + UPDATED_MAIN_SECTOR_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesByMainSectorTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where mainSectorTypeName in DEFAULT_MAIN_SECTOR_TYPE_NAME or UPDATED_MAIN_SECTOR_TYPE_NAME
        defaultSnaSectorCodeShouldBeFound("mainSectorTypeName.in=" + DEFAULT_MAIN_SECTOR_TYPE_NAME + "," + UPDATED_MAIN_SECTOR_TYPE_NAME);

        // Get all the snaSectorCodeList where mainSectorTypeName equals to UPDATED_MAIN_SECTOR_TYPE_NAME
        defaultSnaSectorCodeShouldNotBeFound("mainSectorTypeName.in=" + UPDATED_MAIN_SECTOR_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesByMainSectorTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where mainSectorTypeName is not null
        defaultSnaSectorCodeShouldBeFound("mainSectorTypeName.specified=true");

        // Get all the snaSectorCodeList where mainSectorTypeName is null
        defaultSnaSectorCodeShouldNotBeFound("mainSectorTypeName.specified=false");
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesByMainSectorTypeNameContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where mainSectorTypeName contains DEFAULT_MAIN_SECTOR_TYPE_NAME
        defaultSnaSectorCodeShouldBeFound("mainSectorTypeName.contains=" + DEFAULT_MAIN_SECTOR_TYPE_NAME);

        // Get all the snaSectorCodeList where mainSectorTypeName contains UPDATED_MAIN_SECTOR_TYPE_NAME
        defaultSnaSectorCodeShouldNotBeFound("mainSectorTypeName.contains=" + UPDATED_MAIN_SECTOR_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesByMainSectorTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where mainSectorTypeName does not contain DEFAULT_MAIN_SECTOR_TYPE_NAME
        defaultSnaSectorCodeShouldNotBeFound("mainSectorTypeName.doesNotContain=" + DEFAULT_MAIN_SECTOR_TYPE_NAME);

        // Get all the snaSectorCodeList where mainSectorTypeName does not contain UPDATED_MAIN_SECTOR_TYPE_NAME
        defaultSnaSectorCodeShouldBeFound("mainSectorTypeName.doesNotContain=" + UPDATED_MAIN_SECTOR_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSectorCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSectorCode equals to DEFAULT_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("subSectorCode.equals=" + DEFAULT_SUB_SECTOR_CODE);

        // Get all the snaSectorCodeList where subSectorCode equals to UPDATED_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("subSectorCode.equals=" + UPDATED_SUB_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSectorCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSectorCode not equals to DEFAULT_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("subSectorCode.notEquals=" + DEFAULT_SUB_SECTOR_CODE);

        // Get all the snaSectorCodeList where subSectorCode not equals to UPDATED_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("subSectorCode.notEquals=" + UPDATED_SUB_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSectorCodeIsInShouldWork() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSectorCode in DEFAULT_SUB_SECTOR_CODE or UPDATED_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("subSectorCode.in=" + DEFAULT_SUB_SECTOR_CODE + "," + UPDATED_SUB_SECTOR_CODE);

        // Get all the snaSectorCodeList where subSectorCode equals to UPDATED_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("subSectorCode.in=" + UPDATED_SUB_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSectorCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSectorCode is not null
        defaultSnaSectorCodeShouldBeFound("subSectorCode.specified=true");

        // Get all the snaSectorCodeList where subSectorCode is null
        defaultSnaSectorCodeShouldNotBeFound("subSectorCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSectorCodeContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSectorCode contains DEFAULT_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("subSectorCode.contains=" + DEFAULT_SUB_SECTOR_CODE);

        // Get all the snaSectorCodeList where subSectorCode contains UPDATED_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("subSectorCode.contains=" + UPDATED_SUB_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSectorCodeNotContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSectorCode does not contain DEFAULT_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("subSectorCode.doesNotContain=" + DEFAULT_SUB_SECTOR_CODE);

        // Get all the snaSectorCodeList where subSectorCode does not contain UPDATED_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("subSectorCode.doesNotContain=" + UPDATED_SUB_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSectorNameIsEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSectorName equals to DEFAULT_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldBeFound("subSectorName.equals=" + DEFAULT_SUB_SECTOR_NAME);

        // Get all the snaSectorCodeList where subSectorName equals to UPDATED_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldNotBeFound("subSectorName.equals=" + UPDATED_SUB_SECTOR_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSectorNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSectorName not equals to DEFAULT_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldNotBeFound("subSectorName.notEquals=" + DEFAULT_SUB_SECTOR_NAME);

        // Get all the snaSectorCodeList where subSectorName not equals to UPDATED_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldBeFound("subSectorName.notEquals=" + UPDATED_SUB_SECTOR_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSectorNameIsInShouldWork() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSectorName in DEFAULT_SUB_SECTOR_NAME or UPDATED_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldBeFound("subSectorName.in=" + DEFAULT_SUB_SECTOR_NAME + "," + UPDATED_SUB_SECTOR_NAME);

        // Get all the snaSectorCodeList where subSectorName equals to UPDATED_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldNotBeFound("subSectorName.in=" + UPDATED_SUB_SECTOR_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSectorNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSectorName is not null
        defaultSnaSectorCodeShouldBeFound("subSectorName.specified=true");

        // Get all the snaSectorCodeList where subSectorName is null
        defaultSnaSectorCodeShouldNotBeFound("subSectorName.specified=false");
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSectorNameContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSectorName contains DEFAULT_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldBeFound("subSectorName.contains=" + DEFAULT_SUB_SECTOR_NAME);

        // Get all the snaSectorCodeList where subSectorName contains UPDATED_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldNotBeFound("subSectorName.contains=" + UPDATED_SUB_SECTOR_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSectorNameNotContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSectorName does not contain DEFAULT_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldNotBeFound("subSectorName.doesNotContain=" + DEFAULT_SUB_SECTOR_NAME);

        // Get all the snaSectorCodeList where subSectorName does not contain UPDATED_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldBeFound("subSectorName.doesNotContain=" + UPDATED_SUB_SECTOR_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSubSectorCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSubSectorCode equals to DEFAULT_SUB_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("subSubSectorCode.equals=" + DEFAULT_SUB_SUB_SECTOR_CODE);

        // Get all the snaSectorCodeList where subSubSectorCode equals to UPDATED_SUB_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("subSubSectorCode.equals=" + UPDATED_SUB_SUB_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSubSectorCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSubSectorCode not equals to DEFAULT_SUB_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("subSubSectorCode.notEquals=" + DEFAULT_SUB_SUB_SECTOR_CODE);

        // Get all the snaSectorCodeList where subSubSectorCode not equals to UPDATED_SUB_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("subSubSectorCode.notEquals=" + UPDATED_SUB_SUB_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSubSectorCodeIsInShouldWork() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSubSectorCode in DEFAULT_SUB_SUB_SECTOR_CODE or UPDATED_SUB_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("subSubSectorCode.in=" + DEFAULT_SUB_SUB_SECTOR_CODE + "," + UPDATED_SUB_SUB_SECTOR_CODE);

        // Get all the snaSectorCodeList where subSubSectorCode equals to UPDATED_SUB_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("subSubSectorCode.in=" + UPDATED_SUB_SUB_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSubSectorCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSubSectorCode is not null
        defaultSnaSectorCodeShouldBeFound("subSubSectorCode.specified=true");

        // Get all the snaSectorCodeList where subSubSectorCode is null
        defaultSnaSectorCodeShouldNotBeFound("subSubSectorCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSubSectorCodeContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSubSectorCode contains DEFAULT_SUB_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("subSubSectorCode.contains=" + DEFAULT_SUB_SUB_SECTOR_CODE);

        // Get all the snaSectorCodeList where subSubSectorCode contains UPDATED_SUB_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("subSubSectorCode.contains=" + UPDATED_SUB_SUB_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSubSectorCodeNotContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSubSectorCode does not contain DEFAULT_SUB_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldNotBeFound("subSubSectorCode.doesNotContain=" + DEFAULT_SUB_SUB_SECTOR_CODE);

        // Get all the snaSectorCodeList where subSubSectorCode does not contain UPDATED_SUB_SUB_SECTOR_CODE
        defaultSnaSectorCodeShouldBeFound("subSubSectorCode.doesNotContain=" + UPDATED_SUB_SUB_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSubSectorNameIsEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSubSectorName equals to DEFAULT_SUB_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldBeFound("subSubSectorName.equals=" + DEFAULT_SUB_SUB_SECTOR_NAME);

        // Get all the snaSectorCodeList where subSubSectorName equals to UPDATED_SUB_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldNotBeFound("subSubSectorName.equals=" + UPDATED_SUB_SUB_SECTOR_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSubSectorNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSubSectorName not equals to DEFAULT_SUB_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldNotBeFound("subSubSectorName.notEquals=" + DEFAULT_SUB_SUB_SECTOR_NAME);

        // Get all the snaSectorCodeList where subSubSectorName not equals to UPDATED_SUB_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldBeFound("subSubSectorName.notEquals=" + UPDATED_SUB_SUB_SECTOR_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSubSectorNameIsInShouldWork() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSubSectorName in DEFAULT_SUB_SUB_SECTOR_NAME or UPDATED_SUB_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldBeFound("subSubSectorName.in=" + DEFAULT_SUB_SUB_SECTOR_NAME + "," + UPDATED_SUB_SUB_SECTOR_NAME);

        // Get all the snaSectorCodeList where subSubSectorName equals to UPDATED_SUB_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldNotBeFound("subSubSectorName.in=" + UPDATED_SUB_SUB_SECTOR_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSubSectorNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSubSectorName is not null
        defaultSnaSectorCodeShouldBeFound("subSubSectorName.specified=true");

        // Get all the snaSectorCodeList where subSubSectorName is null
        defaultSnaSectorCodeShouldNotBeFound("subSubSectorName.specified=false");
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSubSectorNameContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSubSectorName contains DEFAULT_SUB_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldBeFound("subSubSectorName.contains=" + DEFAULT_SUB_SUB_SECTOR_NAME);

        // Get all the snaSectorCodeList where subSubSectorName contains UPDATED_SUB_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldNotBeFound("subSubSectorName.contains=" + UPDATED_SUB_SUB_SECTOR_NAME);
    }

    @Test
    @Transactional
    void getAllSnaSectorCodesBySubSubSectorNameNotContainsSomething() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        // Get all the snaSectorCodeList where subSubSectorName does not contain DEFAULT_SUB_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldNotBeFound("subSubSectorName.doesNotContain=" + DEFAULT_SUB_SUB_SECTOR_NAME);

        // Get all the snaSectorCodeList where subSubSectorName does not contain UPDATED_SUB_SUB_SECTOR_NAME
        defaultSnaSectorCodeShouldBeFound("subSubSectorName.doesNotContain=" + UPDATED_SUB_SUB_SECTOR_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSnaSectorCodeShouldBeFound(String filter) throws Exception {
        restSnaSectorCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(snaSectorCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectorTypeCode").value(hasItem(DEFAULT_SECTOR_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].mainSectorCode").value(hasItem(DEFAULT_MAIN_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].mainSectorTypeName").value(hasItem(DEFAULT_MAIN_SECTOR_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].subSectorCode").value(hasItem(DEFAULT_SUB_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].subSectorName").value(hasItem(DEFAULT_SUB_SECTOR_NAME)))
            .andExpect(jsonPath("$.[*].subSubSectorCode").value(hasItem(DEFAULT_SUB_SUB_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].subSubSectorName").value(hasItem(DEFAULT_SUB_SUB_SECTOR_NAME)));

        // Check, that the count call also returns 1
        restSnaSectorCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSnaSectorCodeShouldNotBeFound(String filter) throws Exception {
        restSnaSectorCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSnaSectorCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSnaSectorCode() throws Exception {
        // Get the snaSectorCode
        restSnaSectorCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSnaSectorCode() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        int databaseSizeBeforeUpdate = snaSectorCodeRepository.findAll().size();

        // Update the snaSectorCode
        SnaSectorCode updatedSnaSectorCode = snaSectorCodeRepository.findById(snaSectorCode.getId()).get();
        // Disconnect from session so that the updates on updatedSnaSectorCode are not directly saved in db
        em.detach(updatedSnaSectorCode);
        updatedSnaSectorCode
            .sectorTypeCode(UPDATED_SECTOR_TYPE_CODE)
            .mainSectorCode(UPDATED_MAIN_SECTOR_CODE)
            .mainSectorTypeName(UPDATED_MAIN_SECTOR_TYPE_NAME)
            .subSectorCode(UPDATED_SUB_SECTOR_CODE)
            .subSectorName(UPDATED_SUB_SECTOR_NAME)
            .subSubSectorCode(UPDATED_SUB_SUB_SECTOR_CODE)
            .subSubSectorName(UPDATED_SUB_SUB_SECTOR_NAME);
        SnaSectorCodeDTO snaSectorCodeDTO = snaSectorCodeMapper.toDto(updatedSnaSectorCode);

        restSnaSectorCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, snaSectorCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(snaSectorCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the SnaSectorCode in the database
        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeUpdate);
        SnaSectorCode testSnaSectorCode = snaSectorCodeList.get(snaSectorCodeList.size() - 1);
        assertThat(testSnaSectorCode.getSectorTypeCode()).isEqualTo(UPDATED_SECTOR_TYPE_CODE);
        assertThat(testSnaSectorCode.getMainSectorCode()).isEqualTo(UPDATED_MAIN_SECTOR_CODE);
        assertThat(testSnaSectorCode.getMainSectorTypeName()).isEqualTo(UPDATED_MAIN_SECTOR_TYPE_NAME);
        assertThat(testSnaSectorCode.getSubSectorCode()).isEqualTo(UPDATED_SUB_SECTOR_CODE);
        assertThat(testSnaSectorCode.getSubSectorName()).isEqualTo(UPDATED_SUB_SECTOR_NAME);
        assertThat(testSnaSectorCode.getSubSubSectorCode()).isEqualTo(UPDATED_SUB_SUB_SECTOR_CODE);
        assertThat(testSnaSectorCode.getSubSubSectorName()).isEqualTo(UPDATED_SUB_SUB_SECTOR_NAME);

        // Validate the SnaSectorCode in Elasticsearch
        verify(mockSnaSectorCodeSearchRepository).save(testSnaSectorCode);
    }

    @Test
    @Transactional
    void putNonExistingSnaSectorCode() throws Exception {
        int databaseSizeBeforeUpdate = snaSectorCodeRepository.findAll().size();
        snaSectorCode.setId(count.incrementAndGet());

        // Create the SnaSectorCode
        SnaSectorCodeDTO snaSectorCodeDTO = snaSectorCodeMapper.toDto(snaSectorCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSnaSectorCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, snaSectorCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(snaSectorCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SnaSectorCode in the database
        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SnaSectorCode in Elasticsearch
        verify(mockSnaSectorCodeSearchRepository, times(0)).save(snaSectorCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchSnaSectorCode() throws Exception {
        int databaseSizeBeforeUpdate = snaSectorCodeRepository.findAll().size();
        snaSectorCode.setId(count.incrementAndGet());

        // Create the SnaSectorCode
        SnaSectorCodeDTO snaSectorCodeDTO = snaSectorCodeMapper.toDto(snaSectorCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSnaSectorCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(snaSectorCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SnaSectorCode in the database
        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SnaSectorCode in Elasticsearch
        verify(mockSnaSectorCodeSearchRepository, times(0)).save(snaSectorCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSnaSectorCode() throws Exception {
        int databaseSizeBeforeUpdate = snaSectorCodeRepository.findAll().size();
        snaSectorCode.setId(count.incrementAndGet());

        // Create the SnaSectorCode
        SnaSectorCodeDTO snaSectorCodeDTO = snaSectorCodeMapper.toDto(snaSectorCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSnaSectorCodeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(snaSectorCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SnaSectorCode in the database
        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SnaSectorCode in Elasticsearch
        verify(mockSnaSectorCodeSearchRepository, times(0)).save(snaSectorCode);
    }

    @Test
    @Transactional
    void partialUpdateSnaSectorCodeWithPatch() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        int databaseSizeBeforeUpdate = snaSectorCodeRepository.findAll().size();

        // Update the snaSectorCode using partial update
        SnaSectorCode partialUpdatedSnaSectorCode = new SnaSectorCode();
        partialUpdatedSnaSectorCode.setId(snaSectorCode.getId());

        partialUpdatedSnaSectorCode
            .sectorTypeCode(UPDATED_SECTOR_TYPE_CODE)
            .mainSectorTypeName(UPDATED_MAIN_SECTOR_TYPE_NAME)
            .subSectorCode(UPDATED_SUB_SECTOR_CODE)
            .subSubSectorCode(UPDATED_SUB_SUB_SECTOR_CODE);

        restSnaSectorCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSnaSectorCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSnaSectorCode))
            )
            .andExpect(status().isOk());

        // Validate the SnaSectorCode in the database
        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeUpdate);
        SnaSectorCode testSnaSectorCode = snaSectorCodeList.get(snaSectorCodeList.size() - 1);
        assertThat(testSnaSectorCode.getSectorTypeCode()).isEqualTo(UPDATED_SECTOR_TYPE_CODE);
        assertThat(testSnaSectorCode.getMainSectorCode()).isEqualTo(DEFAULT_MAIN_SECTOR_CODE);
        assertThat(testSnaSectorCode.getMainSectorTypeName()).isEqualTo(UPDATED_MAIN_SECTOR_TYPE_NAME);
        assertThat(testSnaSectorCode.getSubSectorCode()).isEqualTo(UPDATED_SUB_SECTOR_CODE);
        assertThat(testSnaSectorCode.getSubSectorName()).isEqualTo(DEFAULT_SUB_SECTOR_NAME);
        assertThat(testSnaSectorCode.getSubSubSectorCode()).isEqualTo(UPDATED_SUB_SUB_SECTOR_CODE);
        assertThat(testSnaSectorCode.getSubSubSectorName()).isEqualTo(DEFAULT_SUB_SUB_SECTOR_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSnaSectorCodeWithPatch() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        int databaseSizeBeforeUpdate = snaSectorCodeRepository.findAll().size();

        // Update the snaSectorCode using partial update
        SnaSectorCode partialUpdatedSnaSectorCode = new SnaSectorCode();
        partialUpdatedSnaSectorCode.setId(snaSectorCode.getId());

        partialUpdatedSnaSectorCode
            .sectorTypeCode(UPDATED_SECTOR_TYPE_CODE)
            .mainSectorCode(UPDATED_MAIN_SECTOR_CODE)
            .mainSectorTypeName(UPDATED_MAIN_SECTOR_TYPE_NAME)
            .subSectorCode(UPDATED_SUB_SECTOR_CODE)
            .subSectorName(UPDATED_SUB_SECTOR_NAME)
            .subSubSectorCode(UPDATED_SUB_SUB_SECTOR_CODE)
            .subSubSectorName(UPDATED_SUB_SUB_SECTOR_NAME);

        restSnaSectorCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSnaSectorCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSnaSectorCode))
            )
            .andExpect(status().isOk());

        // Validate the SnaSectorCode in the database
        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeUpdate);
        SnaSectorCode testSnaSectorCode = snaSectorCodeList.get(snaSectorCodeList.size() - 1);
        assertThat(testSnaSectorCode.getSectorTypeCode()).isEqualTo(UPDATED_SECTOR_TYPE_CODE);
        assertThat(testSnaSectorCode.getMainSectorCode()).isEqualTo(UPDATED_MAIN_SECTOR_CODE);
        assertThat(testSnaSectorCode.getMainSectorTypeName()).isEqualTo(UPDATED_MAIN_SECTOR_TYPE_NAME);
        assertThat(testSnaSectorCode.getSubSectorCode()).isEqualTo(UPDATED_SUB_SECTOR_CODE);
        assertThat(testSnaSectorCode.getSubSectorName()).isEqualTo(UPDATED_SUB_SECTOR_NAME);
        assertThat(testSnaSectorCode.getSubSubSectorCode()).isEqualTo(UPDATED_SUB_SUB_SECTOR_CODE);
        assertThat(testSnaSectorCode.getSubSubSectorName()).isEqualTo(UPDATED_SUB_SUB_SECTOR_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSnaSectorCode() throws Exception {
        int databaseSizeBeforeUpdate = snaSectorCodeRepository.findAll().size();
        snaSectorCode.setId(count.incrementAndGet());

        // Create the SnaSectorCode
        SnaSectorCodeDTO snaSectorCodeDTO = snaSectorCodeMapper.toDto(snaSectorCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSnaSectorCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, snaSectorCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(snaSectorCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SnaSectorCode in the database
        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SnaSectorCode in Elasticsearch
        verify(mockSnaSectorCodeSearchRepository, times(0)).save(snaSectorCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSnaSectorCode() throws Exception {
        int databaseSizeBeforeUpdate = snaSectorCodeRepository.findAll().size();
        snaSectorCode.setId(count.incrementAndGet());

        // Create the SnaSectorCode
        SnaSectorCodeDTO snaSectorCodeDTO = snaSectorCodeMapper.toDto(snaSectorCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSnaSectorCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(snaSectorCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SnaSectorCode in the database
        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SnaSectorCode in Elasticsearch
        verify(mockSnaSectorCodeSearchRepository, times(0)).save(snaSectorCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSnaSectorCode() throws Exception {
        int databaseSizeBeforeUpdate = snaSectorCodeRepository.findAll().size();
        snaSectorCode.setId(count.incrementAndGet());

        // Create the SnaSectorCode
        SnaSectorCodeDTO snaSectorCodeDTO = snaSectorCodeMapper.toDto(snaSectorCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSnaSectorCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(snaSectorCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SnaSectorCode in the database
        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SnaSectorCode in Elasticsearch
        verify(mockSnaSectorCodeSearchRepository, times(0)).save(snaSectorCode);
    }

    @Test
    @Transactional
    void deleteSnaSectorCode() throws Exception {
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);

        int databaseSizeBeforeDelete = snaSectorCodeRepository.findAll().size();

        // Delete the snaSectorCode
        restSnaSectorCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, snaSectorCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SnaSectorCode> snaSectorCodeList = snaSectorCodeRepository.findAll();
        assertThat(snaSectorCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SnaSectorCode in Elasticsearch
        verify(mockSnaSectorCodeSearchRepository, times(1)).deleteById(snaSectorCode.getId());
    }

    @Test
    @Transactional
    void searchSnaSectorCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        snaSectorCodeRepository.saveAndFlush(snaSectorCode);
        when(mockSnaSectorCodeSearchRepository.search("id:" + snaSectorCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(snaSectorCode), PageRequest.of(0, 1), 1));

        // Search the snaSectorCode
        restSnaSectorCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + snaSectorCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(snaSectorCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectorTypeCode").value(hasItem(DEFAULT_SECTOR_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].mainSectorCode").value(hasItem(DEFAULT_MAIN_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].mainSectorTypeName").value(hasItem(DEFAULT_MAIN_SECTOR_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].subSectorCode").value(hasItem(DEFAULT_SUB_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].subSectorName").value(hasItem(DEFAULT_SUB_SECTOR_NAME)))
            .andExpect(jsonPath("$.[*].subSubSectorCode").value(hasItem(DEFAULT_SUB_SUB_SECTOR_CODE)))
            .andExpect(jsonPath("$.[*].subSubSectorName").value(hasItem(DEFAULT_SUB_SUB_SECTOR_NAME)));
    }
}
