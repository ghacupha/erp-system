package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.domain.AccountAttributeMetadata;
import io.github.erp.domain.GdiMasterDataIndex;
import io.github.erp.domain.enumeration.MandatoryFieldFlagTypes;
import io.github.erp.repository.AccountAttributeMetadataRepository;
import io.github.erp.repository.search.AccountAttributeMetadataSearchRepository;
import io.github.erp.service.criteria.AccountAttributeMetadataCriteria;
import io.github.erp.service.dto.AccountAttributeMetadataDTO;
import io.github.erp.service.mapper.AccountAttributeMetadataMapper;
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
 * Integration tests for the {@link AccountAttributeMetadataResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AccountAttributeMetadataResourceIT {

    private static final Integer DEFAULT_PRECEDENCE = 1;
    private static final Integer UPDATED_PRECEDENCE = 2;
    private static final Integer SMALLER_PRECEDENCE = 1 - 1;

    private static final String DEFAULT_COLUMN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILED_DEFINITION = "AAAAAAAAAA";
    private static final String UPDATED_DETAILED_DEFINITION = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_LENGTH = 1;
    private static final Integer UPDATED_LENGTH = 2;
    private static final Integer SMALLER_LENGTH = 1 - 1;

    private static final String DEFAULT_COLUMN_INDEX = "AAAAAAAAAA";
    private static final String UPDATED_COLUMN_INDEX = "BBBBBBBBBB";

    private static final MandatoryFieldFlagTypes DEFAULT_MANDATORY_FIELD_FLAG = MandatoryFieldFlagTypes.Y;
    private static final MandatoryFieldFlagTypes UPDATED_MANDATORY_FIELD_FLAG = MandatoryFieldFlagTypes.N;

    private static final String DEFAULT_BUSINESS_VALIDATION = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_VALIDATION = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNICAL_VALIDATION = "AAAAAAAAAA";
    private static final String UPDATED_TECHNICAL_VALIDATION = "BBBBBBBBBB";

    private static final String DEFAULT_DB_COLUMN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DB_COLUMN_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_METADATA_VERSION = 1;
    private static final Integer UPDATED_METADATA_VERSION = 2;
    private static final Integer SMALLER_METADATA_VERSION = 1 - 1;

    private static final String ENTITY_API_URL = "/api/account-attribute-metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/account-attribute-metadata";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountAttributeMetadataRepository accountAttributeMetadataRepository;

    @Autowired
    private AccountAttributeMetadataMapper accountAttributeMetadataMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AccountAttributeMetadataSearchRepositoryMockConfiguration
     */
    @Autowired
    private AccountAttributeMetadataSearchRepository mockAccountAttributeMetadataSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountAttributeMetadataMockMvc;

    private AccountAttributeMetadata accountAttributeMetadata;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountAttributeMetadata createEntity(EntityManager em) {
        AccountAttributeMetadata accountAttributeMetadata = new AccountAttributeMetadata()
            .precedence(DEFAULT_PRECEDENCE)
            .columnName(DEFAULT_COLUMN_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .detailedDefinition(DEFAULT_DETAILED_DEFINITION)
            .dataType(DEFAULT_DATA_TYPE)
            .length(DEFAULT_LENGTH)
            .columnIndex(DEFAULT_COLUMN_INDEX)
            .mandatoryFieldFlag(DEFAULT_MANDATORY_FIELD_FLAG)
            .businessValidation(DEFAULT_BUSINESS_VALIDATION)
            .technicalValidation(DEFAULT_TECHNICAL_VALIDATION)
            .dbColumnName(DEFAULT_DB_COLUMN_NAME)
            .metadataVersion(DEFAULT_METADATA_VERSION);
        return accountAttributeMetadata;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountAttributeMetadata createUpdatedEntity(EntityManager em) {
        AccountAttributeMetadata accountAttributeMetadata = new AccountAttributeMetadata()
            .precedence(UPDATED_PRECEDENCE)
            .columnName(UPDATED_COLUMN_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .detailedDefinition(UPDATED_DETAILED_DEFINITION)
            .dataType(UPDATED_DATA_TYPE)
            .length(UPDATED_LENGTH)
            .columnIndex(UPDATED_COLUMN_INDEX)
            .mandatoryFieldFlag(UPDATED_MANDATORY_FIELD_FLAG)
            .businessValidation(UPDATED_BUSINESS_VALIDATION)
            .technicalValidation(UPDATED_TECHNICAL_VALIDATION)
            .dbColumnName(UPDATED_DB_COLUMN_NAME)
            .metadataVersion(UPDATED_METADATA_VERSION);
        return accountAttributeMetadata;
    }

    @BeforeEach
    public void initTest() {
        accountAttributeMetadata = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountAttributeMetadata() throws Exception {
        int databaseSizeBeforeCreate = accountAttributeMetadataRepository.findAll().size();
        // Create the AccountAttributeMetadata
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);
        restAccountAttributeMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AccountAttributeMetadata in the database
        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeCreate + 1);
        AccountAttributeMetadata testAccountAttributeMetadata = accountAttributeMetadataList.get(accountAttributeMetadataList.size() - 1);
        assertThat(testAccountAttributeMetadata.getPrecedence()).isEqualTo(DEFAULT_PRECEDENCE);
        assertThat(testAccountAttributeMetadata.getColumnName()).isEqualTo(DEFAULT_COLUMN_NAME);
        assertThat(testAccountAttributeMetadata.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testAccountAttributeMetadata.getDetailedDefinition()).isEqualTo(DEFAULT_DETAILED_DEFINITION);
        assertThat(testAccountAttributeMetadata.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testAccountAttributeMetadata.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testAccountAttributeMetadata.getColumnIndex()).isEqualTo(DEFAULT_COLUMN_INDEX);
        assertThat(testAccountAttributeMetadata.getMandatoryFieldFlag()).isEqualTo(DEFAULT_MANDATORY_FIELD_FLAG);
        assertThat(testAccountAttributeMetadata.getBusinessValidation()).isEqualTo(DEFAULT_BUSINESS_VALIDATION);
        assertThat(testAccountAttributeMetadata.getTechnicalValidation()).isEqualTo(DEFAULT_TECHNICAL_VALIDATION);
        assertThat(testAccountAttributeMetadata.getDbColumnName()).isEqualTo(DEFAULT_DB_COLUMN_NAME);
        assertThat(testAccountAttributeMetadata.getMetadataVersion()).isEqualTo(DEFAULT_METADATA_VERSION);

        // Validate the AccountAttributeMetadata in Elasticsearch
        verify(mockAccountAttributeMetadataSearchRepository, times(1)).save(testAccountAttributeMetadata);
    }

    @Test
    @Transactional
    void createAccountAttributeMetadataWithExistingId() throws Exception {
        // Create the AccountAttributeMetadata with an existing ID
        accountAttributeMetadata.setId(1L);
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);

        int databaseSizeBeforeCreate = accountAttributeMetadataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountAttributeMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountAttributeMetadata in the database
        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeCreate);

        // Validate the AccountAttributeMetadata in Elasticsearch
        verify(mockAccountAttributeMetadataSearchRepository, times(0)).save(accountAttributeMetadata);
    }

    @Test
    @Transactional
    void checkPrecedenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeMetadataRepository.findAll().size();
        // set the field null
        accountAttributeMetadata.setPrecedence(null);

        // Create the AccountAttributeMetadata, which fails.
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);

        restAccountAttributeMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkColumnNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeMetadataRepository.findAll().size();
        // set the field null
        accountAttributeMetadata.setColumnName(null);

        // Create the AccountAttributeMetadata, which fails.
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);

        restAccountAttributeMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeMetadataRepository.findAll().size();
        // set the field null
        accountAttributeMetadata.setShortName(null);

        // Create the AccountAttributeMetadata, which fails.
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);

        restAccountAttributeMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeMetadataRepository.findAll().size();
        // set the field null
        accountAttributeMetadata.setDataType(null);

        // Create the AccountAttributeMetadata, which fails.
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);

        restAccountAttributeMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMandatoryFieldFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountAttributeMetadataRepository.findAll().size();
        // set the field null
        accountAttributeMetadata.setMandatoryFieldFlag(null);

        // Create the AccountAttributeMetadata, which fails.
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);

        restAccountAttributeMetadataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadata() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList
        restAccountAttributeMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountAttributeMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].precedence").value(hasItem(DEFAULT_PRECEDENCE)))
            .andExpect(jsonPath("$.[*].columnName").value(hasItem(DEFAULT_COLUMN_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].detailedDefinition").value(hasItem(DEFAULT_DETAILED_DEFINITION.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE)))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH)))
            .andExpect(jsonPath("$.[*].columnIndex").value(hasItem(DEFAULT_COLUMN_INDEX)))
            .andExpect(jsonPath("$.[*].mandatoryFieldFlag").value(hasItem(DEFAULT_MANDATORY_FIELD_FLAG.toString())))
            .andExpect(jsonPath("$.[*].businessValidation").value(hasItem(DEFAULT_BUSINESS_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].technicalValidation").value(hasItem(DEFAULT_TECHNICAL_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].dbColumnName").value(hasItem(DEFAULT_DB_COLUMN_NAME)))
            .andExpect(jsonPath("$.[*].metadataVersion").value(hasItem(DEFAULT_METADATA_VERSION)));
    }

    @Test
    @Transactional
    void getAccountAttributeMetadata() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get the accountAttributeMetadata
        restAccountAttributeMetadataMockMvc
            .perform(get(ENTITY_API_URL_ID, accountAttributeMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountAttributeMetadata.getId().intValue()))
            .andExpect(jsonPath("$.precedence").value(DEFAULT_PRECEDENCE))
            .andExpect(jsonPath("$.columnName").value(DEFAULT_COLUMN_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.detailedDefinition").value(DEFAULT_DETAILED_DEFINITION.toString()))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH))
            .andExpect(jsonPath("$.columnIndex").value(DEFAULT_COLUMN_INDEX))
            .andExpect(jsonPath("$.mandatoryFieldFlag").value(DEFAULT_MANDATORY_FIELD_FLAG.toString()))
            .andExpect(jsonPath("$.businessValidation").value(DEFAULT_BUSINESS_VALIDATION.toString()))
            .andExpect(jsonPath("$.technicalValidation").value(DEFAULT_TECHNICAL_VALIDATION.toString()))
            .andExpect(jsonPath("$.dbColumnName").value(DEFAULT_DB_COLUMN_NAME))
            .andExpect(jsonPath("$.metadataVersion").value(DEFAULT_METADATA_VERSION));
    }

    @Test
    @Transactional
    void getAccountAttributeMetadataByIdFiltering() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        Long id = accountAttributeMetadata.getId();

        defaultAccountAttributeMetadataShouldBeFound("id.equals=" + id);
        defaultAccountAttributeMetadataShouldNotBeFound("id.notEquals=" + id);

        defaultAccountAttributeMetadataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAccountAttributeMetadataShouldNotBeFound("id.greaterThan=" + id);

        defaultAccountAttributeMetadataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAccountAttributeMetadataShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByPrecedenceIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where precedence equals to DEFAULT_PRECEDENCE
        defaultAccountAttributeMetadataShouldBeFound("precedence.equals=" + DEFAULT_PRECEDENCE);

        // Get all the accountAttributeMetadataList where precedence equals to UPDATED_PRECEDENCE
        defaultAccountAttributeMetadataShouldNotBeFound("precedence.equals=" + UPDATED_PRECEDENCE);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByPrecedenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where precedence not equals to DEFAULT_PRECEDENCE
        defaultAccountAttributeMetadataShouldNotBeFound("precedence.notEquals=" + DEFAULT_PRECEDENCE);

        // Get all the accountAttributeMetadataList where precedence not equals to UPDATED_PRECEDENCE
        defaultAccountAttributeMetadataShouldBeFound("precedence.notEquals=" + UPDATED_PRECEDENCE);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByPrecedenceIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where precedence in DEFAULT_PRECEDENCE or UPDATED_PRECEDENCE
        defaultAccountAttributeMetadataShouldBeFound("precedence.in=" + DEFAULT_PRECEDENCE + "," + UPDATED_PRECEDENCE);

        // Get all the accountAttributeMetadataList where precedence equals to UPDATED_PRECEDENCE
        defaultAccountAttributeMetadataShouldNotBeFound("precedence.in=" + UPDATED_PRECEDENCE);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByPrecedenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where precedence is not null
        defaultAccountAttributeMetadataShouldBeFound("precedence.specified=true");

        // Get all the accountAttributeMetadataList where precedence is null
        defaultAccountAttributeMetadataShouldNotBeFound("precedence.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByPrecedenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where precedence is greater than or equal to DEFAULT_PRECEDENCE
        defaultAccountAttributeMetadataShouldBeFound("precedence.greaterThanOrEqual=" + DEFAULT_PRECEDENCE);

        // Get all the accountAttributeMetadataList where precedence is greater than or equal to UPDATED_PRECEDENCE
        defaultAccountAttributeMetadataShouldNotBeFound("precedence.greaterThanOrEqual=" + UPDATED_PRECEDENCE);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByPrecedenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where precedence is less than or equal to DEFAULT_PRECEDENCE
        defaultAccountAttributeMetadataShouldBeFound("precedence.lessThanOrEqual=" + DEFAULT_PRECEDENCE);

        // Get all the accountAttributeMetadataList where precedence is less than or equal to SMALLER_PRECEDENCE
        defaultAccountAttributeMetadataShouldNotBeFound("precedence.lessThanOrEqual=" + SMALLER_PRECEDENCE);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByPrecedenceIsLessThanSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where precedence is less than DEFAULT_PRECEDENCE
        defaultAccountAttributeMetadataShouldNotBeFound("precedence.lessThan=" + DEFAULT_PRECEDENCE);

        // Get all the accountAttributeMetadataList where precedence is less than UPDATED_PRECEDENCE
        defaultAccountAttributeMetadataShouldBeFound("precedence.lessThan=" + UPDATED_PRECEDENCE);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByPrecedenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where precedence is greater than DEFAULT_PRECEDENCE
        defaultAccountAttributeMetadataShouldNotBeFound("precedence.greaterThan=" + DEFAULT_PRECEDENCE);

        // Get all the accountAttributeMetadataList where precedence is greater than SMALLER_PRECEDENCE
        defaultAccountAttributeMetadataShouldBeFound("precedence.greaterThan=" + SMALLER_PRECEDENCE);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByColumnNameIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where columnName equals to DEFAULT_COLUMN_NAME
        defaultAccountAttributeMetadataShouldBeFound("columnName.equals=" + DEFAULT_COLUMN_NAME);

        // Get all the accountAttributeMetadataList where columnName equals to UPDATED_COLUMN_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("columnName.equals=" + UPDATED_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByColumnNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where columnName not equals to DEFAULT_COLUMN_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("columnName.notEquals=" + DEFAULT_COLUMN_NAME);

        // Get all the accountAttributeMetadataList where columnName not equals to UPDATED_COLUMN_NAME
        defaultAccountAttributeMetadataShouldBeFound("columnName.notEquals=" + UPDATED_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByColumnNameIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where columnName in DEFAULT_COLUMN_NAME or UPDATED_COLUMN_NAME
        defaultAccountAttributeMetadataShouldBeFound("columnName.in=" + DEFAULT_COLUMN_NAME + "," + UPDATED_COLUMN_NAME);

        // Get all the accountAttributeMetadataList where columnName equals to UPDATED_COLUMN_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("columnName.in=" + UPDATED_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByColumnNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where columnName is not null
        defaultAccountAttributeMetadataShouldBeFound("columnName.specified=true");

        // Get all the accountAttributeMetadataList where columnName is null
        defaultAccountAttributeMetadataShouldNotBeFound("columnName.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByColumnNameContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where columnName contains DEFAULT_COLUMN_NAME
        defaultAccountAttributeMetadataShouldBeFound("columnName.contains=" + DEFAULT_COLUMN_NAME);

        // Get all the accountAttributeMetadataList where columnName contains UPDATED_COLUMN_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("columnName.contains=" + UPDATED_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByColumnNameNotContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where columnName does not contain DEFAULT_COLUMN_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("columnName.doesNotContain=" + DEFAULT_COLUMN_NAME);

        // Get all the accountAttributeMetadataList where columnName does not contain UPDATED_COLUMN_NAME
        defaultAccountAttributeMetadataShouldBeFound("columnName.doesNotContain=" + UPDATED_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByShortNameIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where shortName equals to DEFAULT_SHORT_NAME
        defaultAccountAttributeMetadataShouldBeFound("shortName.equals=" + DEFAULT_SHORT_NAME);

        // Get all the accountAttributeMetadataList where shortName equals to UPDATED_SHORT_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("shortName.equals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByShortNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where shortName not equals to DEFAULT_SHORT_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("shortName.notEquals=" + DEFAULT_SHORT_NAME);

        // Get all the accountAttributeMetadataList where shortName not equals to UPDATED_SHORT_NAME
        defaultAccountAttributeMetadataShouldBeFound("shortName.notEquals=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByShortNameIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where shortName in DEFAULT_SHORT_NAME or UPDATED_SHORT_NAME
        defaultAccountAttributeMetadataShouldBeFound("shortName.in=" + DEFAULT_SHORT_NAME + "," + UPDATED_SHORT_NAME);

        // Get all the accountAttributeMetadataList where shortName equals to UPDATED_SHORT_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("shortName.in=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByShortNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where shortName is not null
        defaultAccountAttributeMetadataShouldBeFound("shortName.specified=true");

        // Get all the accountAttributeMetadataList where shortName is null
        defaultAccountAttributeMetadataShouldNotBeFound("shortName.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByShortNameContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where shortName contains DEFAULT_SHORT_NAME
        defaultAccountAttributeMetadataShouldBeFound("shortName.contains=" + DEFAULT_SHORT_NAME);

        // Get all the accountAttributeMetadataList where shortName contains UPDATED_SHORT_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("shortName.contains=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByShortNameNotContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where shortName does not contain DEFAULT_SHORT_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("shortName.doesNotContain=" + DEFAULT_SHORT_NAME);

        // Get all the accountAttributeMetadataList where shortName does not contain UPDATED_SHORT_NAME
        defaultAccountAttributeMetadataShouldBeFound("shortName.doesNotContain=" + UPDATED_SHORT_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByDataTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where dataType equals to DEFAULT_DATA_TYPE
        defaultAccountAttributeMetadataShouldBeFound("dataType.equals=" + DEFAULT_DATA_TYPE);

        // Get all the accountAttributeMetadataList where dataType equals to UPDATED_DATA_TYPE
        defaultAccountAttributeMetadataShouldNotBeFound("dataType.equals=" + UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByDataTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where dataType not equals to DEFAULT_DATA_TYPE
        defaultAccountAttributeMetadataShouldNotBeFound("dataType.notEquals=" + DEFAULT_DATA_TYPE);

        // Get all the accountAttributeMetadataList where dataType not equals to UPDATED_DATA_TYPE
        defaultAccountAttributeMetadataShouldBeFound("dataType.notEquals=" + UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByDataTypeIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where dataType in DEFAULT_DATA_TYPE or UPDATED_DATA_TYPE
        defaultAccountAttributeMetadataShouldBeFound("dataType.in=" + DEFAULT_DATA_TYPE + "," + UPDATED_DATA_TYPE);

        // Get all the accountAttributeMetadataList where dataType equals to UPDATED_DATA_TYPE
        defaultAccountAttributeMetadataShouldNotBeFound("dataType.in=" + UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByDataTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where dataType is not null
        defaultAccountAttributeMetadataShouldBeFound("dataType.specified=true");

        // Get all the accountAttributeMetadataList where dataType is null
        defaultAccountAttributeMetadataShouldNotBeFound("dataType.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByDataTypeContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where dataType contains DEFAULT_DATA_TYPE
        defaultAccountAttributeMetadataShouldBeFound("dataType.contains=" + DEFAULT_DATA_TYPE);

        // Get all the accountAttributeMetadataList where dataType contains UPDATED_DATA_TYPE
        defaultAccountAttributeMetadataShouldNotBeFound("dataType.contains=" + UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByDataTypeNotContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where dataType does not contain DEFAULT_DATA_TYPE
        defaultAccountAttributeMetadataShouldNotBeFound("dataType.doesNotContain=" + DEFAULT_DATA_TYPE);

        // Get all the accountAttributeMetadataList where dataType does not contain UPDATED_DATA_TYPE
        defaultAccountAttributeMetadataShouldBeFound("dataType.doesNotContain=" + UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByLengthIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where length equals to DEFAULT_LENGTH
        defaultAccountAttributeMetadataShouldBeFound("length.equals=" + DEFAULT_LENGTH);

        // Get all the accountAttributeMetadataList where length equals to UPDATED_LENGTH
        defaultAccountAttributeMetadataShouldNotBeFound("length.equals=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByLengthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where length not equals to DEFAULT_LENGTH
        defaultAccountAttributeMetadataShouldNotBeFound("length.notEquals=" + DEFAULT_LENGTH);

        // Get all the accountAttributeMetadataList where length not equals to UPDATED_LENGTH
        defaultAccountAttributeMetadataShouldBeFound("length.notEquals=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByLengthIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where length in DEFAULT_LENGTH or UPDATED_LENGTH
        defaultAccountAttributeMetadataShouldBeFound("length.in=" + DEFAULT_LENGTH + "," + UPDATED_LENGTH);

        // Get all the accountAttributeMetadataList where length equals to UPDATED_LENGTH
        defaultAccountAttributeMetadataShouldNotBeFound("length.in=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByLengthIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where length is not null
        defaultAccountAttributeMetadataShouldBeFound("length.specified=true");

        // Get all the accountAttributeMetadataList where length is null
        defaultAccountAttributeMetadataShouldNotBeFound("length.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByLengthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where length is greater than or equal to DEFAULT_LENGTH
        defaultAccountAttributeMetadataShouldBeFound("length.greaterThanOrEqual=" + DEFAULT_LENGTH);

        // Get all the accountAttributeMetadataList where length is greater than or equal to UPDATED_LENGTH
        defaultAccountAttributeMetadataShouldNotBeFound("length.greaterThanOrEqual=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByLengthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where length is less than or equal to DEFAULT_LENGTH
        defaultAccountAttributeMetadataShouldBeFound("length.lessThanOrEqual=" + DEFAULT_LENGTH);

        // Get all the accountAttributeMetadataList where length is less than or equal to SMALLER_LENGTH
        defaultAccountAttributeMetadataShouldNotBeFound("length.lessThanOrEqual=" + SMALLER_LENGTH);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByLengthIsLessThanSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where length is less than DEFAULT_LENGTH
        defaultAccountAttributeMetadataShouldNotBeFound("length.lessThan=" + DEFAULT_LENGTH);

        // Get all the accountAttributeMetadataList where length is less than UPDATED_LENGTH
        defaultAccountAttributeMetadataShouldBeFound("length.lessThan=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByLengthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where length is greater than DEFAULT_LENGTH
        defaultAccountAttributeMetadataShouldNotBeFound("length.greaterThan=" + DEFAULT_LENGTH);

        // Get all the accountAttributeMetadataList where length is greater than SMALLER_LENGTH
        defaultAccountAttributeMetadataShouldBeFound("length.greaterThan=" + SMALLER_LENGTH);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByColumnIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where columnIndex equals to DEFAULT_COLUMN_INDEX
        defaultAccountAttributeMetadataShouldBeFound("columnIndex.equals=" + DEFAULT_COLUMN_INDEX);

        // Get all the accountAttributeMetadataList where columnIndex equals to UPDATED_COLUMN_INDEX
        defaultAccountAttributeMetadataShouldNotBeFound("columnIndex.equals=" + UPDATED_COLUMN_INDEX);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByColumnIndexIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where columnIndex not equals to DEFAULT_COLUMN_INDEX
        defaultAccountAttributeMetadataShouldNotBeFound("columnIndex.notEquals=" + DEFAULT_COLUMN_INDEX);

        // Get all the accountAttributeMetadataList where columnIndex not equals to UPDATED_COLUMN_INDEX
        defaultAccountAttributeMetadataShouldBeFound("columnIndex.notEquals=" + UPDATED_COLUMN_INDEX);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByColumnIndexIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where columnIndex in DEFAULT_COLUMN_INDEX or UPDATED_COLUMN_INDEX
        defaultAccountAttributeMetadataShouldBeFound("columnIndex.in=" + DEFAULT_COLUMN_INDEX + "," + UPDATED_COLUMN_INDEX);

        // Get all the accountAttributeMetadataList where columnIndex equals to UPDATED_COLUMN_INDEX
        defaultAccountAttributeMetadataShouldNotBeFound("columnIndex.in=" + UPDATED_COLUMN_INDEX);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByColumnIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where columnIndex is not null
        defaultAccountAttributeMetadataShouldBeFound("columnIndex.specified=true");

        // Get all the accountAttributeMetadataList where columnIndex is null
        defaultAccountAttributeMetadataShouldNotBeFound("columnIndex.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByColumnIndexContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where columnIndex contains DEFAULT_COLUMN_INDEX
        defaultAccountAttributeMetadataShouldBeFound("columnIndex.contains=" + DEFAULT_COLUMN_INDEX);

        // Get all the accountAttributeMetadataList where columnIndex contains UPDATED_COLUMN_INDEX
        defaultAccountAttributeMetadataShouldNotBeFound("columnIndex.contains=" + UPDATED_COLUMN_INDEX);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByColumnIndexNotContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where columnIndex does not contain DEFAULT_COLUMN_INDEX
        defaultAccountAttributeMetadataShouldNotBeFound("columnIndex.doesNotContain=" + DEFAULT_COLUMN_INDEX);

        // Get all the accountAttributeMetadataList where columnIndex does not contain UPDATED_COLUMN_INDEX
        defaultAccountAttributeMetadataShouldBeFound("columnIndex.doesNotContain=" + UPDATED_COLUMN_INDEX);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByMandatoryFieldFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where mandatoryFieldFlag equals to DEFAULT_MANDATORY_FIELD_FLAG
        defaultAccountAttributeMetadataShouldBeFound("mandatoryFieldFlag.equals=" + DEFAULT_MANDATORY_FIELD_FLAG);

        // Get all the accountAttributeMetadataList where mandatoryFieldFlag equals to UPDATED_MANDATORY_FIELD_FLAG
        defaultAccountAttributeMetadataShouldNotBeFound("mandatoryFieldFlag.equals=" + UPDATED_MANDATORY_FIELD_FLAG);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByMandatoryFieldFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where mandatoryFieldFlag not equals to DEFAULT_MANDATORY_FIELD_FLAG
        defaultAccountAttributeMetadataShouldNotBeFound("mandatoryFieldFlag.notEquals=" + DEFAULT_MANDATORY_FIELD_FLAG);

        // Get all the accountAttributeMetadataList where mandatoryFieldFlag not equals to UPDATED_MANDATORY_FIELD_FLAG
        defaultAccountAttributeMetadataShouldBeFound("mandatoryFieldFlag.notEquals=" + UPDATED_MANDATORY_FIELD_FLAG);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByMandatoryFieldFlagIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where mandatoryFieldFlag in DEFAULT_MANDATORY_FIELD_FLAG or UPDATED_MANDATORY_FIELD_FLAG
        defaultAccountAttributeMetadataShouldBeFound(
            "mandatoryFieldFlag.in=" + DEFAULT_MANDATORY_FIELD_FLAG + "," + UPDATED_MANDATORY_FIELD_FLAG
        );

        // Get all the accountAttributeMetadataList where mandatoryFieldFlag equals to UPDATED_MANDATORY_FIELD_FLAG
        defaultAccountAttributeMetadataShouldNotBeFound("mandatoryFieldFlag.in=" + UPDATED_MANDATORY_FIELD_FLAG);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByMandatoryFieldFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where mandatoryFieldFlag is not null
        defaultAccountAttributeMetadataShouldBeFound("mandatoryFieldFlag.specified=true");

        // Get all the accountAttributeMetadataList where mandatoryFieldFlag is null
        defaultAccountAttributeMetadataShouldNotBeFound("mandatoryFieldFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByDbColumnNameIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where dbColumnName equals to DEFAULT_DB_COLUMN_NAME
        defaultAccountAttributeMetadataShouldBeFound("dbColumnName.equals=" + DEFAULT_DB_COLUMN_NAME);

        // Get all the accountAttributeMetadataList where dbColumnName equals to UPDATED_DB_COLUMN_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("dbColumnName.equals=" + UPDATED_DB_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByDbColumnNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where dbColumnName not equals to DEFAULT_DB_COLUMN_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("dbColumnName.notEquals=" + DEFAULT_DB_COLUMN_NAME);

        // Get all the accountAttributeMetadataList where dbColumnName not equals to UPDATED_DB_COLUMN_NAME
        defaultAccountAttributeMetadataShouldBeFound("dbColumnName.notEquals=" + UPDATED_DB_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByDbColumnNameIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where dbColumnName in DEFAULT_DB_COLUMN_NAME or UPDATED_DB_COLUMN_NAME
        defaultAccountAttributeMetadataShouldBeFound("dbColumnName.in=" + DEFAULT_DB_COLUMN_NAME + "," + UPDATED_DB_COLUMN_NAME);

        // Get all the accountAttributeMetadataList where dbColumnName equals to UPDATED_DB_COLUMN_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("dbColumnName.in=" + UPDATED_DB_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByDbColumnNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where dbColumnName is not null
        defaultAccountAttributeMetadataShouldBeFound("dbColumnName.specified=true");

        // Get all the accountAttributeMetadataList where dbColumnName is null
        defaultAccountAttributeMetadataShouldNotBeFound("dbColumnName.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByDbColumnNameContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where dbColumnName contains DEFAULT_DB_COLUMN_NAME
        defaultAccountAttributeMetadataShouldBeFound("dbColumnName.contains=" + DEFAULT_DB_COLUMN_NAME);

        // Get all the accountAttributeMetadataList where dbColumnName contains UPDATED_DB_COLUMN_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("dbColumnName.contains=" + UPDATED_DB_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByDbColumnNameNotContainsSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where dbColumnName does not contain DEFAULT_DB_COLUMN_NAME
        defaultAccountAttributeMetadataShouldNotBeFound("dbColumnName.doesNotContain=" + DEFAULT_DB_COLUMN_NAME);

        // Get all the accountAttributeMetadataList where dbColumnName does not contain UPDATED_DB_COLUMN_NAME
        defaultAccountAttributeMetadataShouldBeFound("dbColumnName.doesNotContain=" + UPDATED_DB_COLUMN_NAME);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByMetadataVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where metadataVersion equals to DEFAULT_METADATA_VERSION
        defaultAccountAttributeMetadataShouldBeFound("metadataVersion.equals=" + DEFAULT_METADATA_VERSION);

        // Get all the accountAttributeMetadataList where metadataVersion equals to UPDATED_METADATA_VERSION
        defaultAccountAttributeMetadataShouldNotBeFound("metadataVersion.equals=" + UPDATED_METADATA_VERSION);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByMetadataVersionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where metadataVersion not equals to DEFAULT_METADATA_VERSION
        defaultAccountAttributeMetadataShouldNotBeFound("metadataVersion.notEquals=" + DEFAULT_METADATA_VERSION);

        // Get all the accountAttributeMetadataList where metadataVersion not equals to UPDATED_METADATA_VERSION
        defaultAccountAttributeMetadataShouldBeFound("metadataVersion.notEquals=" + UPDATED_METADATA_VERSION);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByMetadataVersionIsInShouldWork() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where metadataVersion in DEFAULT_METADATA_VERSION or UPDATED_METADATA_VERSION
        defaultAccountAttributeMetadataShouldBeFound("metadataVersion.in=" + DEFAULT_METADATA_VERSION + "," + UPDATED_METADATA_VERSION);

        // Get all the accountAttributeMetadataList where metadataVersion equals to UPDATED_METADATA_VERSION
        defaultAccountAttributeMetadataShouldNotBeFound("metadataVersion.in=" + UPDATED_METADATA_VERSION);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByMetadataVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where metadataVersion is not null
        defaultAccountAttributeMetadataShouldBeFound("metadataVersion.specified=true");

        // Get all the accountAttributeMetadataList where metadataVersion is null
        defaultAccountAttributeMetadataShouldNotBeFound("metadataVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByMetadataVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where metadataVersion is greater than or equal to DEFAULT_METADATA_VERSION
        defaultAccountAttributeMetadataShouldBeFound("metadataVersion.greaterThanOrEqual=" + DEFAULT_METADATA_VERSION);

        // Get all the accountAttributeMetadataList where metadataVersion is greater than or equal to UPDATED_METADATA_VERSION
        defaultAccountAttributeMetadataShouldNotBeFound("metadataVersion.greaterThanOrEqual=" + UPDATED_METADATA_VERSION);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByMetadataVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where metadataVersion is less than or equal to DEFAULT_METADATA_VERSION
        defaultAccountAttributeMetadataShouldBeFound("metadataVersion.lessThanOrEqual=" + DEFAULT_METADATA_VERSION);

        // Get all the accountAttributeMetadataList where metadataVersion is less than or equal to SMALLER_METADATA_VERSION
        defaultAccountAttributeMetadataShouldNotBeFound("metadataVersion.lessThanOrEqual=" + SMALLER_METADATA_VERSION);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByMetadataVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where metadataVersion is less than DEFAULT_METADATA_VERSION
        defaultAccountAttributeMetadataShouldNotBeFound("metadataVersion.lessThan=" + DEFAULT_METADATA_VERSION);

        // Get all the accountAttributeMetadataList where metadataVersion is less than UPDATED_METADATA_VERSION
        defaultAccountAttributeMetadataShouldBeFound("metadataVersion.lessThan=" + UPDATED_METADATA_VERSION);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByMetadataVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        // Get all the accountAttributeMetadataList where metadataVersion is greater than DEFAULT_METADATA_VERSION
        defaultAccountAttributeMetadataShouldNotBeFound("metadataVersion.greaterThan=" + DEFAULT_METADATA_VERSION);

        // Get all the accountAttributeMetadataList where metadataVersion is greater than SMALLER_METADATA_VERSION
        defaultAccountAttributeMetadataShouldBeFound("metadataVersion.greaterThan=" + SMALLER_METADATA_VERSION);
    }

    @Test
    @Transactional
    void getAllAccountAttributeMetadataByStandardInputTemplateIsEqualToSomething() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);
        GdiMasterDataIndex standardInputTemplate;
        if (TestUtil.findAll(em, GdiMasterDataIndex.class).isEmpty()) {
            standardInputTemplate = GdiMasterDataIndexResourceIT.createEntity(em);
            em.persist(standardInputTemplate);
            em.flush();
        } else {
            standardInputTemplate = TestUtil.findAll(em, GdiMasterDataIndex.class).get(0);
        }
        em.persist(standardInputTemplate);
        em.flush();
        accountAttributeMetadata.setStandardInputTemplate(standardInputTemplate);
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);
        Long standardInputTemplateId = standardInputTemplate.getId();

        // Get all the accountAttributeMetadataList where standardInputTemplate equals to standardInputTemplateId
        defaultAccountAttributeMetadataShouldBeFound("standardInputTemplateId.equals=" + standardInputTemplateId);

        // Get all the accountAttributeMetadataList where standardInputTemplate equals to (standardInputTemplateId + 1)
        defaultAccountAttributeMetadataShouldNotBeFound("standardInputTemplateId.equals=" + (standardInputTemplateId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAccountAttributeMetadataShouldBeFound(String filter) throws Exception {
        restAccountAttributeMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountAttributeMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].precedence").value(hasItem(DEFAULT_PRECEDENCE)))
            .andExpect(jsonPath("$.[*].columnName").value(hasItem(DEFAULT_COLUMN_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].detailedDefinition").value(hasItem(DEFAULT_DETAILED_DEFINITION.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE)))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH)))
            .andExpect(jsonPath("$.[*].columnIndex").value(hasItem(DEFAULT_COLUMN_INDEX)))
            .andExpect(jsonPath("$.[*].mandatoryFieldFlag").value(hasItem(DEFAULT_MANDATORY_FIELD_FLAG.toString())))
            .andExpect(jsonPath("$.[*].businessValidation").value(hasItem(DEFAULT_BUSINESS_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].technicalValidation").value(hasItem(DEFAULT_TECHNICAL_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].dbColumnName").value(hasItem(DEFAULT_DB_COLUMN_NAME)))
            .andExpect(jsonPath("$.[*].metadataVersion").value(hasItem(DEFAULT_METADATA_VERSION)));

        // Check, that the count call also returns 1
        restAccountAttributeMetadataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAccountAttributeMetadataShouldNotBeFound(String filter) throws Exception {
        restAccountAttributeMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAccountAttributeMetadataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAccountAttributeMetadata() throws Exception {
        // Get the accountAttributeMetadata
        restAccountAttributeMetadataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccountAttributeMetadata() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        int databaseSizeBeforeUpdate = accountAttributeMetadataRepository.findAll().size();

        // Update the accountAttributeMetadata
        AccountAttributeMetadata updatedAccountAttributeMetadata = accountAttributeMetadataRepository
            .findById(accountAttributeMetadata.getId())
            .get();
        // Disconnect from session so that the updates on updatedAccountAttributeMetadata are not directly saved in db
        em.detach(updatedAccountAttributeMetadata);
        updatedAccountAttributeMetadata
            .precedence(UPDATED_PRECEDENCE)
            .columnName(UPDATED_COLUMN_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .detailedDefinition(UPDATED_DETAILED_DEFINITION)
            .dataType(UPDATED_DATA_TYPE)
            .length(UPDATED_LENGTH)
            .columnIndex(UPDATED_COLUMN_INDEX)
            .mandatoryFieldFlag(UPDATED_MANDATORY_FIELD_FLAG)
            .businessValidation(UPDATED_BUSINESS_VALIDATION)
            .technicalValidation(UPDATED_TECHNICAL_VALIDATION)
            .dbColumnName(UPDATED_DB_COLUMN_NAME)
            .metadataVersion(UPDATED_METADATA_VERSION);
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(updatedAccountAttributeMetadata);

        restAccountAttributeMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountAttributeMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccountAttributeMetadata in the database
        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeUpdate);
        AccountAttributeMetadata testAccountAttributeMetadata = accountAttributeMetadataList.get(accountAttributeMetadataList.size() - 1);
        assertThat(testAccountAttributeMetadata.getPrecedence()).isEqualTo(UPDATED_PRECEDENCE);
        assertThat(testAccountAttributeMetadata.getColumnName()).isEqualTo(UPDATED_COLUMN_NAME);
        assertThat(testAccountAttributeMetadata.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testAccountAttributeMetadata.getDetailedDefinition()).isEqualTo(UPDATED_DETAILED_DEFINITION);
        assertThat(testAccountAttributeMetadata.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testAccountAttributeMetadata.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testAccountAttributeMetadata.getColumnIndex()).isEqualTo(UPDATED_COLUMN_INDEX);
        assertThat(testAccountAttributeMetadata.getMandatoryFieldFlag()).isEqualTo(UPDATED_MANDATORY_FIELD_FLAG);
        assertThat(testAccountAttributeMetadata.getBusinessValidation()).isEqualTo(UPDATED_BUSINESS_VALIDATION);
        assertThat(testAccountAttributeMetadata.getTechnicalValidation()).isEqualTo(UPDATED_TECHNICAL_VALIDATION);
        assertThat(testAccountAttributeMetadata.getDbColumnName()).isEqualTo(UPDATED_DB_COLUMN_NAME);
        assertThat(testAccountAttributeMetadata.getMetadataVersion()).isEqualTo(UPDATED_METADATA_VERSION);

        // Validate the AccountAttributeMetadata in Elasticsearch
        verify(mockAccountAttributeMetadataSearchRepository).save(testAccountAttributeMetadata);
    }

    @Test
    @Transactional
    void putNonExistingAccountAttributeMetadata() throws Exception {
        int databaseSizeBeforeUpdate = accountAttributeMetadataRepository.findAll().size();
        accountAttributeMetadata.setId(count.incrementAndGet());

        // Create the AccountAttributeMetadata
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountAttributeMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountAttributeMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountAttributeMetadata in the database
        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountAttributeMetadata in Elasticsearch
        verify(mockAccountAttributeMetadataSearchRepository, times(0)).save(accountAttributeMetadata);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountAttributeMetadata() throws Exception {
        int databaseSizeBeforeUpdate = accountAttributeMetadataRepository.findAll().size();
        accountAttributeMetadata.setId(count.incrementAndGet());

        // Create the AccountAttributeMetadata
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountAttributeMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountAttributeMetadata in the database
        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountAttributeMetadata in Elasticsearch
        verify(mockAccountAttributeMetadataSearchRepository, times(0)).save(accountAttributeMetadata);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountAttributeMetadata() throws Exception {
        int databaseSizeBeforeUpdate = accountAttributeMetadataRepository.findAll().size();
        accountAttributeMetadata.setId(count.incrementAndGet());

        // Create the AccountAttributeMetadata
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountAttributeMetadataMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountAttributeMetadata in the database
        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountAttributeMetadata in Elasticsearch
        verify(mockAccountAttributeMetadataSearchRepository, times(0)).save(accountAttributeMetadata);
    }

    @Test
    @Transactional
    void partialUpdateAccountAttributeMetadataWithPatch() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        int databaseSizeBeforeUpdate = accountAttributeMetadataRepository.findAll().size();

        // Update the accountAttributeMetadata using partial update
        AccountAttributeMetadata partialUpdatedAccountAttributeMetadata = new AccountAttributeMetadata();
        partialUpdatedAccountAttributeMetadata.setId(accountAttributeMetadata.getId());

        partialUpdatedAccountAttributeMetadata
            .shortName(UPDATED_SHORT_NAME)
            .detailedDefinition(UPDATED_DETAILED_DEFINITION)
            .dataType(UPDATED_DATA_TYPE)
            .length(UPDATED_LENGTH)
            .columnIndex(UPDATED_COLUMN_INDEX)
            .mandatoryFieldFlag(UPDATED_MANDATORY_FIELD_FLAG)
            .businessValidation(UPDATED_BUSINESS_VALIDATION)
            .technicalValidation(UPDATED_TECHNICAL_VALIDATION);

        restAccountAttributeMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountAttributeMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountAttributeMetadata))
            )
            .andExpect(status().isOk());

        // Validate the AccountAttributeMetadata in the database
        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeUpdate);
        AccountAttributeMetadata testAccountAttributeMetadata = accountAttributeMetadataList.get(accountAttributeMetadataList.size() - 1);
        assertThat(testAccountAttributeMetadata.getPrecedence()).isEqualTo(DEFAULT_PRECEDENCE);
        assertThat(testAccountAttributeMetadata.getColumnName()).isEqualTo(DEFAULT_COLUMN_NAME);
        assertThat(testAccountAttributeMetadata.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testAccountAttributeMetadata.getDetailedDefinition()).isEqualTo(UPDATED_DETAILED_DEFINITION);
        assertThat(testAccountAttributeMetadata.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testAccountAttributeMetadata.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testAccountAttributeMetadata.getColumnIndex()).isEqualTo(UPDATED_COLUMN_INDEX);
        assertThat(testAccountAttributeMetadata.getMandatoryFieldFlag()).isEqualTo(UPDATED_MANDATORY_FIELD_FLAG);
        assertThat(testAccountAttributeMetadata.getBusinessValidation()).isEqualTo(UPDATED_BUSINESS_VALIDATION);
        assertThat(testAccountAttributeMetadata.getTechnicalValidation()).isEqualTo(UPDATED_TECHNICAL_VALIDATION);
        assertThat(testAccountAttributeMetadata.getDbColumnName()).isEqualTo(DEFAULT_DB_COLUMN_NAME);
        assertThat(testAccountAttributeMetadata.getMetadataVersion()).isEqualTo(DEFAULT_METADATA_VERSION);
    }

    @Test
    @Transactional
    void fullUpdateAccountAttributeMetadataWithPatch() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        int databaseSizeBeforeUpdate = accountAttributeMetadataRepository.findAll().size();

        // Update the accountAttributeMetadata using partial update
        AccountAttributeMetadata partialUpdatedAccountAttributeMetadata = new AccountAttributeMetadata();
        partialUpdatedAccountAttributeMetadata.setId(accountAttributeMetadata.getId());

        partialUpdatedAccountAttributeMetadata
            .precedence(UPDATED_PRECEDENCE)
            .columnName(UPDATED_COLUMN_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .detailedDefinition(UPDATED_DETAILED_DEFINITION)
            .dataType(UPDATED_DATA_TYPE)
            .length(UPDATED_LENGTH)
            .columnIndex(UPDATED_COLUMN_INDEX)
            .mandatoryFieldFlag(UPDATED_MANDATORY_FIELD_FLAG)
            .businessValidation(UPDATED_BUSINESS_VALIDATION)
            .technicalValidation(UPDATED_TECHNICAL_VALIDATION)
            .dbColumnName(UPDATED_DB_COLUMN_NAME)
            .metadataVersion(UPDATED_METADATA_VERSION);

        restAccountAttributeMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountAttributeMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountAttributeMetadata))
            )
            .andExpect(status().isOk());

        // Validate the AccountAttributeMetadata in the database
        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeUpdate);
        AccountAttributeMetadata testAccountAttributeMetadata = accountAttributeMetadataList.get(accountAttributeMetadataList.size() - 1);
        assertThat(testAccountAttributeMetadata.getPrecedence()).isEqualTo(UPDATED_PRECEDENCE);
        assertThat(testAccountAttributeMetadata.getColumnName()).isEqualTo(UPDATED_COLUMN_NAME);
        assertThat(testAccountAttributeMetadata.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testAccountAttributeMetadata.getDetailedDefinition()).isEqualTo(UPDATED_DETAILED_DEFINITION);
        assertThat(testAccountAttributeMetadata.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testAccountAttributeMetadata.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testAccountAttributeMetadata.getColumnIndex()).isEqualTo(UPDATED_COLUMN_INDEX);
        assertThat(testAccountAttributeMetadata.getMandatoryFieldFlag()).isEqualTo(UPDATED_MANDATORY_FIELD_FLAG);
        assertThat(testAccountAttributeMetadata.getBusinessValidation()).isEqualTo(UPDATED_BUSINESS_VALIDATION);
        assertThat(testAccountAttributeMetadata.getTechnicalValidation()).isEqualTo(UPDATED_TECHNICAL_VALIDATION);
        assertThat(testAccountAttributeMetadata.getDbColumnName()).isEqualTo(UPDATED_DB_COLUMN_NAME);
        assertThat(testAccountAttributeMetadata.getMetadataVersion()).isEqualTo(UPDATED_METADATA_VERSION);
    }

    @Test
    @Transactional
    void patchNonExistingAccountAttributeMetadata() throws Exception {
        int databaseSizeBeforeUpdate = accountAttributeMetadataRepository.findAll().size();
        accountAttributeMetadata.setId(count.incrementAndGet());

        // Create the AccountAttributeMetadata
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountAttributeMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountAttributeMetadataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountAttributeMetadata in the database
        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountAttributeMetadata in Elasticsearch
        verify(mockAccountAttributeMetadataSearchRepository, times(0)).save(accountAttributeMetadata);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountAttributeMetadata() throws Exception {
        int databaseSizeBeforeUpdate = accountAttributeMetadataRepository.findAll().size();
        accountAttributeMetadata.setId(count.incrementAndGet());

        // Create the AccountAttributeMetadata
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountAttributeMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountAttributeMetadata in the database
        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountAttributeMetadata in Elasticsearch
        verify(mockAccountAttributeMetadataSearchRepository, times(0)).save(accountAttributeMetadata);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountAttributeMetadata() throws Exception {
        int databaseSizeBeforeUpdate = accountAttributeMetadataRepository.findAll().size();
        accountAttributeMetadata.setId(count.incrementAndGet());

        // Create the AccountAttributeMetadata
        AccountAttributeMetadataDTO accountAttributeMetadataDTO = accountAttributeMetadataMapper.toDto(accountAttributeMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountAttributeMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountAttributeMetadataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountAttributeMetadata in the database
        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AccountAttributeMetadata in Elasticsearch
        verify(mockAccountAttributeMetadataSearchRepository, times(0)).save(accountAttributeMetadata);
    }

    @Test
    @Transactional
    void deleteAccountAttributeMetadata() throws Exception {
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);

        int databaseSizeBeforeDelete = accountAttributeMetadataRepository.findAll().size();

        // Delete the accountAttributeMetadata
        restAccountAttributeMetadataMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountAttributeMetadata.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountAttributeMetadata> accountAttributeMetadataList = accountAttributeMetadataRepository.findAll();
        assertThat(accountAttributeMetadataList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AccountAttributeMetadata in Elasticsearch
        verify(mockAccountAttributeMetadataSearchRepository, times(1)).deleteById(accountAttributeMetadata.getId());
    }

    @Test
    @Transactional
    void searchAccountAttributeMetadata() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        accountAttributeMetadataRepository.saveAndFlush(accountAttributeMetadata);
        when(mockAccountAttributeMetadataSearchRepository.search("id:" + accountAttributeMetadata.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(accountAttributeMetadata), PageRequest.of(0, 1), 1));

        // Search the accountAttributeMetadata
        restAccountAttributeMetadataMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + accountAttributeMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountAttributeMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].precedence").value(hasItem(DEFAULT_PRECEDENCE)))
            .andExpect(jsonPath("$.[*].columnName").value(hasItem(DEFAULT_COLUMN_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].detailedDefinition").value(hasItem(DEFAULT_DETAILED_DEFINITION.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE)))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH)))
            .andExpect(jsonPath("$.[*].columnIndex").value(hasItem(DEFAULT_COLUMN_INDEX)))
            .andExpect(jsonPath("$.[*].mandatoryFieldFlag").value(hasItem(DEFAULT_MANDATORY_FIELD_FLAG.toString())))
            .andExpect(jsonPath("$.[*].businessValidation").value(hasItem(DEFAULT_BUSINESS_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].technicalValidation").value(hasItem(DEFAULT_TECHNICAL_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].dbColumnName").value(hasItem(DEFAULT_DB_COLUMN_NAME)))
            .andExpect(jsonPath("$.[*].metadataVersion").value(hasItem(DEFAULT_METADATA_VERSION)));
    }
}
