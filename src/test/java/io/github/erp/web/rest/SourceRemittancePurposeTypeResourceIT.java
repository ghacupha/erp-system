package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
import io.github.erp.domain.SourceRemittancePurposeType;
import io.github.erp.domain.enumeration.SourceOrPurposeOfRemittancFlag;
import io.github.erp.repository.SourceRemittancePurposeTypeRepository;
import io.github.erp.repository.search.SourceRemittancePurposeTypeSearchRepository;
import io.github.erp.service.criteria.SourceRemittancePurposeTypeCriteria;
import io.github.erp.service.dto.SourceRemittancePurposeTypeDTO;
import io.github.erp.service.mapper.SourceRemittancePurposeTypeMapper;
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
 * Integration tests for the {@link SourceRemittancePurposeTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SourceRemittancePurposeTypeResourceIT {

    private static final String DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE = "BBBBBBBBBB";

    private static final SourceOrPurposeOfRemittancFlag DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG =
        SourceOrPurposeOfRemittancFlag.PURPOSE_OF_REMITTANCE;
    private static final SourceOrPurposeOfRemittancFlag UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG =
        SourceOrPurposeOfRemittancFlag.SOURCE_OF_FUNDS;

    private static final String DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_REMITTANCE_PURPOSE_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_REMITTANCE_PURPOSE_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/source-remittance-purpose-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/source-remittance-purpose-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SourceRemittancePurposeTypeRepository sourceRemittancePurposeTypeRepository;

    @Autowired
    private SourceRemittancePurposeTypeMapper sourceRemittancePurposeTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SourceRemittancePurposeTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private SourceRemittancePurposeTypeSearchRepository mockSourceRemittancePurposeTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourceRemittancePurposeTypeMockMvc;

    private SourceRemittancePurposeType sourceRemittancePurposeType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceRemittancePurposeType createEntity(EntityManager em) {
        SourceRemittancePurposeType sourceRemittancePurposeType = new SourceRemittancePurposeType()
            .sourceOrPurposeTypeCode(DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE)
            .sourceOrPurposeOfRemittanceFlag(DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG)
            .sourceOrPurposeOfRemittanceType(DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE)
            .remittancePurposeTypeDetails(DEFAULT_REMITTANCE_PURPOSE_TYPE_DETAILS);
        return sourceRemittancePurposeType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SourceRemittancePurposeType createUpdatedEntity(EntityManager em) {
        SourceRemittancePurposeType sourceRemittancePurposeType = new SourceRemittancePurposeType()
            .sourceOrPurposeTypeCode(UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE)
            .sourceOrPurposeOfRemittanceFlag(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG)
            .sourceOrPurposeOfRemittanceType(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE)
            .remittancePurposeTypeDetails(UPDATED_REMITTANCE_PURPOSE_TYPE_DETAILS);
        return sourceRemittancePurposeType;
    }

    @BeforeEach
    public void initTest() {
        sourceRemittancePurposeType = createEntity(em);
    }

    @Test
    @Transactional
    void createSourceRemittancePurposeType() throws Exception {
        int databaseSizeBeforeCreate = sourceRemittancePurposeTypeRepository.findAll().size();
        // Create the SourceRemittancePurposeType
        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeMapper.toDto(
            sourceRemittancePurposeType
        );
        restSourceRemittancePurposeTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceRemittancePurposeTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SourceRemittancePurposeType in the database
        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SourceRemittancePurposeType testSourceRemittancePurposeType = sourceRemittancePurposeTypeList.get(
            sourceRemittancePurposeTypeList.size() - 1
        );
        assertThat(testSourceRemittancePurposeType.getSourceOrPurposeTypeCode()).isEqualTo(DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE);
        assertThat(testSourceRemittancePurposeType.getSourceOrPurposeOfRemittanceFlag())
            .isEqualTo(DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG);
        assertThat(testSourceRemittancePurposeType.getSourceOrPurposeOfRemittanceType())
            .isEqualTo(DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE);
        assertThat(testSourceRemittancePurposeType.getRemittancePurposeTypeDetails()).isEqualTo(DEFAULT_REMITTANCE_PURPOSE_TYPE_DETAILS);

        // Validate the SourceRemittancePurposeType in Elasticsearch
        verify(mockSourceRemittancePurposeTypeSearchRepository, times(1)).save(testSourceRemittancePurposeType);
    }

    @Test
    @Transactional
    void createSourceRemittancePurposeTypeWithExistingId() throws Exception {
        // Create the SourceRemittancePurposeType with an existing ID
        sourceRemittancePurposeType.setId(1L);
        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeMapper.toDto(
            sourceRemittancePurposeType
        );

        int databaseSizeBeforeCreate = sourceRemittancePurposeTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceRemittancePurposeTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceRemittancePurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceRemittancePurposeType in the database
        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the SourceRemittancePurposeType in Elasticsearch
        verify(mockSourceRemittancePurposeTypeSearchRepository, times(0)).save(sourceRemittancePurposeType);
    }

    @Test
    @Transactional
    void checkSourceOrPurposeTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceRemittancePurposeTypeRepository.findAll().size();
        // set the field null
        sourceRemittancePurposeType.setSourceOrPurposeTypeCode(null);

        // Create the SourceRemittancePurposeType, which fails.
        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeMapper.toDto(
            sourceRemittancePurposeType
        );

        restSourceRemittancePurposeTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceRemittancePurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceOrPurposeOfRemittanceFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceRemittancePurposeTypeRepository.findAll().size();
        // set the field null
        sourceRemittancePurposeType.setSourceOrPurposeOfRemittanceFlag(null);

        // Create the SourceRemittancePurposeType, which fails.
        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeMapper.toDto(
            sourceRemittancePurposeType
        );

        restSourceRemittancePurposeTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceRemittancePurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceOrPurposeOfRemittanceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sourceRemittancePurposeTypeRepository.findAll().size();
        // set the field null
        sourceRemittancePurposeType.setSourceOrPurposeOfRemittanceType(null);

        // Create the SourceRemittancePurposeType, which fails.
        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeMapper.toDto(
            sourceRemittancePurposeType
        );

        restSourceRemittancePurposeTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceRemittancePurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypes() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList
        restSourceRemittancePurposeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceRemittancePurposeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceOrPurposeTypeCode").value(hasItem(DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE)))
            .andExpect(
                jsonPath("$.[*].sourceOrPurposeOfRemittanceFlag").value(hasItem(DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG.toString()))
            )
            .andExpect(jsonPath("$.[*].sourceOrPurposeOfRemittanceType").value(hasItem(DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE)))
            .andExpect(jsonPath("$.[*].remittancePurposeTypeDetails").value(hasItem(DEFAULT_REMITTANCE_PURPOSE_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getSourceRemittancePurposeType() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get the sourceRemittancePurposeType
        restSourceRemittancePurposeTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, sourceRemittancePurposeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sourceRemittancePurposeType.getId().intValue()))
            .andExpect(jsonPath("$.sourceOrPurposeTypeCode").value(DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE))
            .andExpect(jsonPath("$.sourceOrPurposeOfRemittanceFlag").value(DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG.toString()))
            .andExpect(jsonPath("$.sourceOrPurposeOfRemittanceType").value(DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE))
            .andExpect(jsonPath("$.remittancePurposeTypeDetails").value(DEFAULT_REMITTANCE_PURPOSE_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getSourceRemittancePurposeTypesByIdFiltering() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        Long id = sourceRemittancePurposeType.getId();

        defaultSourceRemittancePurposeTypeShouldBeFound("id.equals=" + id);
        defaultSourceRemittancePurposeTypeShouldNotBeFound("id.notEquals=" + id);

        defaultSourceRemittancePurposeTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSourceRemittancePurposeTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultSourceRemittancePurposeTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSourceRemittancePurposeTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeTypeCode equals to DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE
        defaultSourceRemittancePurposeTypeShouldBeFound("sourceOrPurposeTypeCode.equals=" + DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeTypeCode equals to UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE
        defaultSourceRemittancePurposeTypeShouldNotBeFound("sourceOrPurposeTypeCode.equals=" + UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeTypeCode not equals to DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE
        defaultSourceRemittancePurposeTypeShouldNotBeFound("sourceOrPurposeTypeCode.notEquals=" + DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeTypeCode not equals to UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE
        defaultSourceRemittancePurposeTypeShouldBeFound("sourceOrPurposeTypeCode.notEquals=" + UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeTypeCode in DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE or UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE
        defaultSourceRemittancePurposeTypeShouldBeFound(
            "sourceOrPurposeTypeCode.in=" + DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE + "," + UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE
        );

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeTypeCode equals to UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE
        defaultSourceRemittancePurposeTypeShouldNotBeFound("sourceOrPurposeTypeCode.in=" + UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeTypeCode is not null
        defaultSourceRemittancePurposeTypeShouldBeFound("sourceOrPurposeTypeCode.specified=true");

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeTypeCode is null
        defaultSourceRemittancePurposeTypeShouldNotBeFound("sourceOrPurposeTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeTypeCode contains DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE
        defaultSourceRemittancePurposeTypeShouldBeFound("sourceOrPurposeTypeCode.contains=" + DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeTypeCode contains UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE
        defaultSourceRemittancePurposeTypeShouldNotBeFound("sourceOrPurposeTypeCode.contains=" + UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeTypeCode does not contain DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE
        defaultSourceRemittancePurposeTypeShouldNotBeFound("sourceOrPurposeTypeCode.doesNotContain=" + DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeTypeCode does not contain UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE
        defaultSourceRemittancePurposeTypeShouldBeFound("sourceOrPurposeTypeCode.doesNotContain=" + UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeOfRemittanceFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceFlag equals to DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG
        defaultSourceRemittancePurposeTypeShouldBeFound(
            "sourceOrPurposeOfRemittanceFlag.equals=" + DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG
        );

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceFlag equals to UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG
        defaultSourceRemittancePurposeTypeShouldNotBeFound(
            "sourceOrPurposeOfRemittanceFlag.equals=" + UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG
        );
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeOfRemittanceFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceFlag not equals to DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG
        defaultSourceRemittancePurposeTypeShouldNotBeFound(
            "sourceOrPurposeOfRemittanceFlag.notEquals=" + DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG
        );

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceFlag not equals to UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG
        defaultSourceRemittancePurposeTypeShouldBeFound(
            "sourceOrPurposeOfRemittanceFlag.notEquals=" + UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG
        );
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeOfRemittanceFlagIsInShouldWork() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceFlag in DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG or UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG
        defaultSourceRemittancePurposeTypeShouldBeFound(
            "sourceOrPurposeOfRemittanceFlag.in=" +
            DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG +
            "," +
            UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG
        );

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceFlag equals to UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG
        defaultSourceRemittancePurposeTypeShouldNotBeFound(
            "sourceOrPurposeOfRemittanceFlag.in=" + UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG
        );
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeOfRemittanceFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceFlag is not null
        defaultSourceRemittancePurposeTypeShouldBeFound("sourceOrPurposeOfRemittanceFlag.specified=true");

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceFlag is null
        defaultSourceRemittancePurposeTypeShouldNotBeFound("sourceOrPurposeOfRemittanceFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeOfRemittanceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceType equals to DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        defaultSourceRemittancePurposeTypeShouldBeFound(
            "sourceOrPurposeOfRemittanceType.equals=" + DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        );

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceType equals to UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        defaultSourceRemittancePurposeTypeShouldNotBeFound(
            "sourceOrPurposeOfRemittanceType.equals=" + UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeOfRemittanceTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceType not equals to DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        defaultSourceRemittancePurposeTypeShouldNotBeFound(
            "sourceOrPurposeOfRemittanceType.notEquals=" + DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        );

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceType not equals to UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        defaultSourceRemittancePurposeTypeShouldBeFound(
            "sourceOrPurposeOfRemittanceType.notEquals=" + UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeOfRemittanceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceType in DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE or UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        defaultSourceRemittancePurposeTypeShouldBeFound(
            "sourceOrPurposeOfRemittanceType.in=" +
            DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE +
            "," +
            UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        );

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceType equals to UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        defaultSourceRemittancePurposeTypeShouldNotBeFound(
            "sourceOrPurposeOfRemittanceType.in=" + UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeOfRemittanceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceType is not null
        defaultSourceRemittancePurposeTypeShouldBeFound("sourceOrPurposeOfRemittanceType.specified=true");

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceType is null
        defaultSourceRemittancePurposeTypeShouldNotBeFound("sourceOrPurposeOfRemittanceType.specified=false");
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeOfRemittanceTypeContainsSomething() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceType contains DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        defaultSourceRemittancePurposeTypeShouldBeFound(
            "sourceOrPurposeOfRemittanceType.contains=" + DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        );

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceType contains UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        defaultSourceRemittancePurposeTypeShouldNotBeFound(
            "sourceOrPurposeOfRemittanceType.contains=" + UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllSourceRemittancePurposeTypesBySourceOrPurposeOfRemittanceTypeNotContainsSomething() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceType does not contain DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        defaultSourceRemittancePurposeTypeShouldNotBeFound(
            "sourceOrPurposeOfRemittanceType.doesNotContain=" + DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        );

        // Get all the sourceRemittancePurposeTypeList where sourceOrPurposeOfRemittanceType does not contain UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        defaultSourceRemittancePurposeTypeShouldBeFound(
            "sourceOrPurposeOfRemittanceType.doesNotContain=" + UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSourceRemittancePurposeTypeShouldBeFound(String filter) throws Exception {
        restSourceRemittancePurposeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceRemittancePurposeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceOrPurposeTypeCode").value(hasItem(DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE)))
            .andExpect(
                jsonPath("$.[*].sourceOrPurposeOfRemittanceFlag").value(hasItem(DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG.toString()))
            )
            .andExpect(jsonPath("$.[*].sourceOrPurposeOfRemittanceType").value(hasItem(DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE)))
            .andExpect(jsonPath("$.[*].remittancePurposeTypeDetails").value(hasItem(DEFAULT_REMITTANCE_PURPOSE_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restSourceRemittancePurposeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSourceRemittancePurposeTypeShouldNotBeFound(String filter) throws Exception {
        restSourceRemittancePurposeTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSourceRemittancePurposeTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSourceRemittancePurposeType() throws Exception {
        // Get the sourceRemittancePurposeType
        restSourceRemittancePurposeTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSourceRemittancePurposeType() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        int databaseSizeBeforeUpdate = sourceRemittancePurposeTypeRepository.findAll().size();

        // Update the sourceRemittancePurposeType
        SourceRemittancePurposeType updatedSourceRemittancePurposeType = sourceRemittancePurposeTypeRepository
            .findById(sourceRemittancePurposeType.getId())
            .get();
        // Disconnect from session so that the updates on updatedSourceRemittancePurposeType are not directly saved in db
        em.detach(updatedSourceRemittancePurposeType);
        updatedSourceRemittancePurposeType
            .sourceOrPurposeTypeCode(UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE)
            .sourceOrPurposeOfRemittanceFlag(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG)
            .sourceOrPurposeOfRemittanceType(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE)
            .remittancePurposeTypeDetails(UPDATED_REMITTANCE_PURPOSE_TYPE_DETAILS);
        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeMapper.toDto(
            updatedSourceRemittancePurposeType
        );

        restSourceRemittancePurposeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceRemittancePurposeTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceRemittancePurposeTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the SourceRemittancePurposeType in the database
        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeUpdate);
        SourceRemittancePurposeType testSourceRemittancePurposeType = sourceRemittancePurposeTypeList.get(
            sourceRemittancePurposeTypeList.size() - 1
        );
        assertThat(testSourceRemittancePurposeType.getSourceOrPurposeTypeCode()).isEqualTo(UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE);
        assertThat(testSourceRemittancePurposeType.getSourceOrPurposeOfRemittanceFlag())
            .isEqualTo(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG);
        assertThat(testSourceRemittancePurposeType.getSourceOrPurposeOfRemittanceType())
            .isEqualTo(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE);
        assertThat(testSourceRemittancePurposeType.getRemittancePurposeTypeDetails()).isEqualTo(UPDATED_REMITTANCE_PURPOSE_TYPE_DETAILS);

        // Validate the SourceRemittancePurposeType in Elasticsearch
        verify(mockSourceRemittancePurposeTypeSearchRepository).save(testSourceRemittancePurposeType);
    }

    @Test
    @Transactional
    void putNonExistingSourceRemittancePurposeType() throws Exception {
        int databaseSizeBeforeUpdate = sourceRemittancePurposeTypeRepository.findAll().size();
        sourceRemittancePurposeType.setId(count.incrementAndGet());

        // Create the SourceRemittancePurposeType
        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeMapper.toDto(
            sourceRemittancePurposeType
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceRemittancePurposeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceRemittancePurposeTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceRemittancePurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceRemittancePurposeType in the database
        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceRemittancePurposeType in Elasticsearch
        verify(mockSourceRemittancePurposeTypeSearchRepository, times(0)).save(sourceRemittancePurposeType);
    }

    @Test
    @Transactional
    void putWithIdMismatchSourceRemittancePurposeType() throws Exception {
        int databaseSizeBeforeUpdate = sourceRemittancePurposeTypeRepository.findAll().size();
        sourceRemittancePurposeType.setId(count.incrementAndGet());

        // Create the SourceRemittancePurposeType
        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeMapper.toDto(
            sourceRemittancePurposeType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceRemittancePurposeTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceRemittancePurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceRemittancePurposeType in the database
        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceRemittancePurposeType in Elasticsearch
        verify(mockSourceRemittancePurposeTypeSearchRepository, times(0)).save(sourceRemittancePurposeType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSourceRemittancePurposeType() throws Exception {
        int databaseSizeBeforeUpdate = sourceRemittancePurposeTypeRepository.findAll().size();
        sourceRemittancePurposeType.setId(count.incrementAndGet());

        // Create the SourceRemittancePurposeType
        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeMapper.toDto(
            sourceRemittancePurposeType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceRemittancePurposeTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sourceRemittancePurposeTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourceRemittancePurposeType in the database
        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceRemittancePurposeType in Elasticsearch
        verify(mockSourceRemittancePurposeTypeSearchRepository, times(0)).save(sourceRemittancePurposeType);
    }

    @Test
    @Transactional
    void partialUpdateSourceRemittancePurposeTypeWithPatch() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        int databaseSizeBeforeUpdate = sourceRemittancePurposeTypeRepository.findAll().size();

        // Update the sourceRemittancePurposeType using partial update
        SourceRemittancePurposeType partialUpdatedSourceRemittancePurposeType = new SourceRemittancePurposeType();
        partialUpdatedSourceRemittancePurposeType.setId(sourceRemittancePurposeType.getId());

        partialUpdatedSourceRemittancePurposeType
            .sourceOrPurposeTypeCode(UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE)
            .sourceOrPurposeOfRemittanceFlag(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG)
            .sourceOrPurposeOfRemittanceType(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE);

        restSourceRemittancePurposeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourceRemittancePurposeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSourceRemittancePurposeType))
            )
            .andExpect(status().isOk());

        // Validate the SourceRemittancePurposeType in the database
        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeUpdate);
        SourceRemittancePurposeType testSourceRemittancePurposeType = sourceRemittancePurposeTypeList.get(
            sourceRemittancePurposeTypeList.size() - 1
        );
        assertThat(testSourceRemittancePurposeType.getSourceOrPurposeTypeCode()).isEqualTo(UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE);
        assertThat(testSourceRemittancePurposeType.getSourceOrPurposeOfRemittanceFlag())
            .isEqualTo(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG);
        assertThat(testSourceRemittancePurposeType.getSourceOrPurposeOfRemittanceType())
            .isEqualTo(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE);
        assertThat(testSourceRemittancePurposeType.getRemittancePurposeTypeDetails()).isEqualTo(DEFAULT_REMITTANCE_PURPOSE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateSourceRemittancePurposeTypeWithPatch() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        int databaseSizeBeforeUpdate = sourceRemittancePurposeTypeRepository.findAll().size();

        // Update the sourceRemittancePurposeType using partial update
        SourceRemittancePurposeType partialUpdatedSourceRemittancePurposeType = new SourceRemittancePurposeType();
        partialUpdatedSourceRemittancePurposeType.setId(sourceRemittancePurposeType.getId());

        partialUpdatedSourceRemittancePurposeType
            .sourceOrPurposeTypeCode(UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE)
            .sourceOrPurposeOfRemittanceFlag(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG)
            .sourceOrPurposeOfRemittanceType(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE)
            .remittancePurposeTypeDetails(UPDATED_REMITTANCE_PURPOSE_TYPE_DETAILS);

        restSourceRemittancePurposeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSourceRemittancePurposeType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSourceRemittancePurposeType))
            )
            .andExpect(status().isOk());

        // Validate the SourceRemittancePurposeType in the database
        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeUpdate);
        SourceRemittancePurposeType testSourceRemittancePurposeType = sourceRemittancePurposeTypeList.get(
            sourceRemittancePurposeTypeList.size() - 1
        );
        assertThat(testSourceRemittancePurposeType.getSourceOrPurposeTypeCode()).isEqualTo(UPDATED_SOURCE_OR_PURPOSE_TYPE_CODE);
        assertThat(testSourceRemittancePurposeType.getSourceOrPurposeOfRemittanceFlag())
            .isEqualTo(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG);
        assertThat(testSourceRemittancePurposeType.getSourceOrPurposeOfRemittanceType())
            .isEqualTo(UPDATED_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE);
        assertThat(testSourceRemittancePurposeType.getRemittancePurposeTypeDetails()).isEqualTo(UPDATED_REMITTANCE_PURPOSE_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingSourceRemittancePurposeType() throws Exception {
        int databaseSizeBeforeUpdate = sourceRemittancePurposeTypeRepository.findAll().size();
        sourceRemittancePurposeType.setId(count.incrementAndGet());

        // Create the SourceRemittancePurposeType
        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeMapper.toDto(
            sourceRemittancePurposeType
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceRemittancePurposeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sourceRemittancePurposeTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourceRemittancePurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceRemittancePurposeType in the database
        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceRemittancePurposeType in Elasticsearch
        verify(mockSourceRemittancePurposeTypeSearchRepository, times(0)).save(sourceRemittancePurposeType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSourceRemittancePurposeType() throws Exception {
        int databaseSizeBeforeUpdate = sourceRemittancePurposeTypeRepository.findAll().size();
        sourceRemittancePurposeType.setId(count.incrementAndGet());

        // Create the SourceRemittancePurposeType
        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeMapper.toDto(
            sourceRemittancePurposeType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceRemittancePurposeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourceRemittancePurposeTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SourceRemittancePurposeType in the database
        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceRemittancePurposeType in Elasticsearch
        verify(mockSourceRemittancePurposeTypeSearchRepository, times(0)).save(sourceRemittancePurposeType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSourceRemittancePurposeType() throws Exception {
        int databaseSizeBeforeUpdate = sourceRemittancePurposeTypeRepository.findAll().size();
        sourceRemittancePurposeType.setId(count.incrementAndGet());

        // Create the SourceRemittancePurposeType
        SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeMapper.toDto(
            sourceRemittancePurposeType
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceRemittancePurposeTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sourceRemittancePurposeTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SourceRemittancePurposeType in the database
        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SourceRemittancePurposeType in Elasticsearch
        verify(mockSourceRemittancePurposeTypeSearchRepository, times(0)).save(sourceRemittancePurposeType);
    }

    @Test
    @Transactional
    void deleteSourceRemittancePurposeType() throws Exception {
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);

        int databaseSizeBeforeDelete = sourceRemittancePurposeTypeRepository.findAll().size();

        // Delete the sourceRemittancePurposeType
        restSourceRemittancePurposeTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, sourceRemittancePurposeType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SourceRemittancePurposeType> sourceRemittancePurposeTypeList = sourceRemittancePurposeTypeRepository.findAll();
        assertThat(sourceRemittancePurposeTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SourceRemittancePurposeType in Elasticsearch
        verify(mockSourceRemittancePurposeTypeSearchRepository, times(1)).deleteById(sourceRemittancePurposeType.getId());
    }

    @Test
    @Transactional
    void searchSourceRemittancePurposeType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        sourceRemittancePurposeTypeRepository.saveAndFlush(sourceRemittancePurposeType);
        when(mockSourceRemittancePurposeTypeSearchRepository.search("id:" + sourceRemittancePurposeType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(sourceRemittancePurposeType), PageRequest.of(0, 1), 1));

        // Search the sourceRemittancePurposeType
        restSourceRemittancePurposeTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + sourceRemittancePurposeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sourceRemittancePurposeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceOrPurposeTypeCode").value(hasItem(DEFAULT_SOURCE_OR_PURPOSE_TYPE_CODE)))
            .andExpect(
                jsonPath("$.[*].sourceOrPurposeOfRemittanceFlag").value(hasItem(DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_FLAG.toString()))
            )
            .andExpect(jsonPath("$.[*].sourceOrPurposeOfRemittanceType").value(hasItem(DEFAULT_SOURCE_OR_PURPOSE_OF_REMITTANCE_TYPE)))
            .andExpect(jsonPath("$.[*].remittancePurposeTypeDetails").value(hasItem(DEFAULT_REMITTANCE_PURPOSE_TYPE_DETAILS.toString())));
    }
}
