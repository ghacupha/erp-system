package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.4
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
import io.github.erp.domain.MfbBranchCode;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.MfbBranchCodeRepository;
import io.github.erp.repository.search.MfbBranchCodeSearchRepository;
import io.github.erp.service.MfbBranchCodeService;
import io.github.erp.service.dto.MfbBranchCodeDTO;
import io.github.erp.service.mapper.MfbBranchCodeMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.TestUtil;
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
 * Integration tests for the {@link MfbBranchCodeResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER", "FIXED_ASSETS_USER"})
public class MfbBranchCodeResourceIT {

    private static final String DEFAULT_BANK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BANK_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/mfb-branch-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/mfb-branch-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MfbBranchCodeRepository mfbBranchCodeRepository;

    @Mock
    private MfbBranchCodeRepository mfbBranchCodeRepositoryMock;

    @Autowired
    private MfbBranchCodeMapper mfbBranchCodeMapper;

    @Mock
    private MfbBranchCodeService mfbBranchCodeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.MfbBranchCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private MfbBranchCodeSearchRepository mockMfbBranchCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMfbBranchCodeMockMvc;

    private MfbBranchCode mfbBranchCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MfbBranchCode createEntity(EntityManager em) {
        MfbBranchCode mfbBranchCode = new MfbBranchCode()
            .bankCode(DEFAULT_BANK_CODE)
            .bankName(DEFAULT_BANK_NAME)
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchName(DEFAULT_BRANCH_NAME);
        return mfbBranchCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MfbBranchCode createUpdatedEntity(EntityManager em) {
        MfbBranchCode mfbBranchCode = new MfbBranchCode()
            .bankCode(UPDATED_BANK_CODE)
            .bankName(UPDATED_BANK_NAME)
            .branchCode(UPDATED_BRANCH_CODE)
            .branchName(UPDATED_BRANCH_NAME);
        return mfbBranchCode;
    }

    @BeforeEach
    public void initTest() {
        mfbBranchCode = createEntity(em);
    }

    @Test
    @Transactional
    void createMfbBranchCode() throws Exception {
        int databaseSizeBeforeCreate = mfbBranchCodeRepository.findAll().size();
        // Create the MfbBranchCode
        MfbBranchCodeDTO mfbBranchCodeDTO = mfbBranchCodeMapper.toDto(mfbBranchCode);
        restMfbBranchCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mfbBranchCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MfbBranchCode in the database
        List<MfbBranchCode> mfbBranchCodeList = mfbBranchCodeRepository.findAll();
        assertThat(mfbBranchCodeList).hasSize(databaseSizeBeforeCreate + 1);
        MfbBranchCode testMfbBranchCode = mfbBranchCodeList.get(mfbBranchCodeList.size() - 1);
        assertThat(testMfbBranchCode.getBankCode()).isEqualTo(DEFAULT_BANK_CODE);
        assertThat(testMfbBranchCode.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testMfbBranchCode.getBranchCode()).isEqualTo(DEFAULT_BRANCH_CODE);
        assertThat(testMfbBranchCode.getBranchName()).isEqualTo(DEFAULT_BRANCH_NAME);

        // Validate the MfbBranchCode in Elasticsearch
        verify(mockMfbBranchCodeSearchRepository, times(1)).save(testMfbBranchCode);
    }

    @Test
    @Transactional
    void createMfbBranchCodeWithExistingId() throws Exception {
        // Create the MfbBranchCode with an existing ID
        mfbBranchCode.setId(1L);
        MfbBranchCodeDTO mfbBranchCodeDTO = mfbBranchCodeMapper.toDto(mfbBranchCode);

        int databaseSizeBeforeCreate = mfbBranchCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMfbBranchCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mfbBranchCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MfbBranchCode in the database
        List<MfbBranchCode> mfbBranchCodeList = mfbBranchCodeRepository.findAll();
        assertThat(mfbBranchCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the MfbBranchCode in Elasticsearch
        verify(mockMfbBranchCodeSearchRepository, times(0)).save(mfbBranchCode);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodes() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList
        restMfbBranchCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mfbBranchCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].bankCode").value(hasItem(DEFAULT_BANK_CODE)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMfbBranchCodesWithEagerRelationshipsIsEnabled() throws Exception {
        when(mfbBranchCodeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMfbBranchCodeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mfbBranchCodeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMfbBranchCodesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mfbBranchCodeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMfbBranchCodeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mfbBranchCodeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getMfbBranchCode() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get the mfbBranchCode
        restMfbBranchCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, mfbBranchCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mfbBranchCode.getId().intValue()))
            .andExpect(jsonPath("$.bankCode").value(DEFAULT_BANK_CODE))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchName").value(DEFAULT_BRANCH_NAME));
    }

    @Test
    @Transactional
    void getMfbBranchCodesByIdFiltering() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        Long id = mfbBranchCode.getId();

        defaultMfbBranchCodeShouldBeFound("id.equals=" + id);
        defaultMfbBranchCodeShouldNotBeFound("id.notEquals=" + id);

        defaultMfbBranchCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMfbBranchCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultMfbBranchCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMfbBranchCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where bankCode equals to DEFAULT_BANK_CODE
        defaultMfbBranchCodeShouldBeFound("bankCode.equals=" + DEFAULT_BANK_CODE);

        // Get all the mfbBranchCodeList where bankCode equals to UPDATED_BANK_CODE
        defaultMfbBranchCodeShouldNotBeFound("bankCode.equals=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBankCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where bankCode not equals to DEFAULT_BANK_CODE
        defaultMfbBranchCodeShouldNotBeFound("bankCode.notEquals=" + DEFAULT_BANK_CODE);

        // Get all the mfbBranchCodeList where bankCode not equals to UPDATED_BANK_CODE
        defaultMfbBranchCodeShouldBeFound("bankCode.notEquals=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBankCodeIsInShouldWork() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where bankCode in DEFAULT_BANK_CODE or UPDATED_BANK_CODE
        defaultMfbBranchCodeShouldBeFound("bankCode.in=" + DEFAULT_BANK_CODE + "," + UPDATED_BANK_CODE);

        // Get all the mfbBranchCodeList where bankCode equals to UPDATED_BANK_CODE
        defaultMfbBranchCodeShouldNotBeFound("bankCode.in=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBankCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where bankCode is not null
        defaultMfbBranchCodeShouldBeFound("bankCode.specified=true");

        // Get all the mfbBranchCodeList where bankCode is null
        defaultMfbBranchCodeShouldNotBeFound("bankCode.specified=false");
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBankCodeContainsSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where bankCode contains DEFAULT_BANK_CODE
        defaultMfbBranchCodeShouldBeFound("bankCode.contains=" + DEFAULT_BANK_CODE);

        // Get all the mfbBranchCodeList where bankCode contains UPDATED_BANK_CODE
        defaultMfbBranchCodeShouldNotBeFound("bankCode.contains=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBankCodeNotContainsSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where bankCode does not contain DEFAULT_BANK_CODE
        defaultMfbBranchCodeShouldNotBeFound("bankCode.doesNotContain=" + DEFAULT_BANK_CODE);

        // Get all the mfbBranchCodeList where bankCode does not contain UPDATED_BANK_CODE
        defaultMfbBranchCodeShouldBeFound("bankCode.doesNotContain=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBankNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where bankName equals to DEFAULT_BANK_NAME
        defaultMfbBranchCodeShouldBeFound("bankName.equals=" + DEFAULT_BANK_NAME);

        // Get all the mfbBranchCodeList where bankName equals to UPDATED_BANK_NAME
        defaultMfbBranchCodeShouldNotBeFound("bankName.equals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBankNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where bankName not equals to DEFAULT_BANK_NAME
        defaultMfbBranchCodeShouldNotBeFound("bankName.notEquals=" + DEFAULT_BANK_NAME);

        // Get all the mfbBranchCodeList where bankName not equals to UPDATED_BANK_NAME
        defaultMfbBranchCodeShouldBeFound("bankName.notEquals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBankNameIsInShouldWork() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where bankName in DEFAULT_BANK_NAME or UPDATED_BANK_NAME
        defaultMfbBranchCodeShouldBeFound("bankName.in=" + DEFAULT_BANK_NAME + "," + UPDATED_BANK_NAME);

        // Get all the mfbBranchCodeList where bankName equals to UPDATED_BANK_NAME
        defaultMfbBranchCodeShouldNotBeFound("bankName.in=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBankNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where bankName is not null
        defaultMfbBranchCodeShouldBeFound("bankName.specified=true");

        // Get all the mfbBranchCodeList where bankName is null
        defaultMfbBranchCodeShouldNotBeFound("bankName.specified=false");
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBankNameContainsSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where bankName contains DEFAULT_BANK_NAME
        defaultMfbBranchCodeShouldBeFound("bankName.contains=" + DEFAULT_BANK_NAME);

        // Get all the mfbBranchCodeList where bankName contains UPDATED_BANK_NAME
        defaultMfbBranchCodeShouldNotBeFound("bankName.contains=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBankNameNotContainsSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where bankName does not contain DEFAULT_BANK_NAME
        defaultMfbBranchCodeShouldNotBeFound("bankName.doesNotContain=" + DEFAULT_BANK_NAME);

        // Get all the mfbBranchCodeList where bankName does not contain UPDATED_BANK_NAME
        defaultMfbBranchCodeShouldBeFound("bankName.doesNotContain=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where branchCode equals to DEFAULT_BRANCH_CODE
        defaultMfbBranchCodeShouldBeFound("branchCode.equals=" + DEFAULT_BRANCH_CODE);

        // Get all the mfbBranchCodeList where branchCode equals to UPDATED_BRANCH_CODE
        defaultMfbBranchCodeShouldNotBeFound("branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBranchCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where branchCode not equals to DEFAULT_BRANCH_CODE
        defaultMfbBranchCodeShouldNotBeFound("branchCode.notEquals=" + DEFAULT_BRANCH_CODE);

        // Get all the mfbBranchCodeList where branchCode not equals to UPDATED_BRANCH_CODE
        defaultMfbBranchCodeShouldBeFound("branchCode.notEquals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where branchCode in DEFAULT_BRANCH_CODE or UPDATED_BRANCH_CODE
        defaultMfbBranchCodeShouldBeFound("branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE);

        // Get all the mfbBranchCodeList where branchCode equals to UPDATED_BRANCH_CODE
        defaultMfbBranchCodeShouldNotBeFound("branchCode.in=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where branchCode is not null
        defaultMfbBranchCodeShouldBeFound("branchCode.specified=true");

        // Get all the mfbBranchCodeList where branchCode is null
        defaultMfbBranchCodeShouldNotBeFound("branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where branchCode contains DEFAULT_BRANCH_CODE
        defaultMfbBranchCodeShouldBeFound("branchCode.contains=" + DEFAULT_BRANCH_CODE);

        // Get all the mfbBranchCodeList where branchCode contains UPDATED_BRANCH_CODE
        defaultMfbBranchCodeShouldNotBeFound("branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where branchCode does not contain DEFAULT_BRANCH_CODE
        defaultMfbBranchCodeShouldNotBeFound("branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE);

        // Get all the mfbBranchCodeList where branchCode does not contain UPDATED_BRANCH_CODE
        defaultMfbBranchCodeShouldBeFound("branchCode.doesNotContain=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBranchNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where branchName equals to DEFAULT_BRANCH_NAME
        defaultMfbBranchCodeShouldBeFound("branchName.equals=" + DEFAULT_BRANCH_NAME);

        // Get all the mfbBranchCodeList where branchName equals to UPDATED_BRANCH_NAME
        defaultMfbBranchCodeShouldNotBeFound("branchName.equals=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBranchNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where branchName not equals to DEFAULT_BRANCH_NAME
        defaultMfbBranchCodeShouldNotBeFound("branchName.notEquals=" + DEFAULT_BRANCH_NAME);

        // Get all the mfbBranchCodeList where branchName not equals to UPDATED_BRANCH_NAME
        defaultMfbBranchCodeShouldBeFound("branchName.notEquals=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBranchNameIsInShouldWork() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where branchName in DEFAULT_BRANCH_NAME or UPDATED_BRANCH_NAME
        defaultMfbBranchCodeShouldBeFound("branchName.in=" + DEFAULT_BRANCH_NAME + "," + UPDATED_BRANCH_NAME);

        // Get all the mfbBranchCodeList where branchName equals to UPDATED_BRANCH_NAME
        defaultMfbBranchCodeShouldNotBeFound("branchName.in=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBranchNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where branchName is not null
        defaultMfbBranchCodeShouldBeFound("branchName.specified=true");

        // Get all the mfbBranchCodeList where branchName is null
        defaultMfbBranchCodeShouldNotBeFound("branchName.specified=false");
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBranchNameContainsSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where branchName contains DEFAULT_BRANCH_NAME
        defaultMfbBranchCodeShouldBeFound("branchName.contains=" + DEFAULT_BRANCH_NAME);

        // Get all the mfbBranchCodeList where branchName contains UPDATED_BRANCH_NAME
        defaultMfbBranchCodeShouldNotBeFound("branchName.contains=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByBranchNameNotContainsSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        // Get all the mfbBranchCodeList where branchName does not contain DEFAULT_BRANCH_NAME
        defaultMfbBranchCodeShouldNotBeFound("branchName.doesNotContain=" + DEFAULT_BRANCH_NAME);

        // Get all the mfbBranchCodeList where branchName does not contain UPDATED_BRANCH_NAME
        defaultMfbBranchCodeShouldBeFound("branchName.doesNotContain=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllMfbBranchCodesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        mfbBranchCode.addPlaceholder(placeholder);
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);
        Long placeholderId = placeholder.getId();

        // Get all the mfbBranchCodeList where placeholder equals to placeholderId
        defaultMfbBranchCodeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the mfbBranchCodeList where placeholder equals to (placeholderId + 1)
        defaultMfbBranchCodeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMfbBranchCodeShouldBeFound(String filter) throws Exception {
        restMfbBranchCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mfbBranchCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].bankCode").value(hasItem(DEFAULT_BANK_CODE)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)));

        // Check, that the count call also returns 1
        restMfbBranchCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMfbBranchCodeShouldNotBeFound(String filter) throws Exception {
        restMfbBranchCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMfbBranchCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMfbBranchCode() throws Exception {
        // Get the mfbBranchCode
        restMfbBranchCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMfbBranchCode() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        int databaseSizeBeforeUpdate = mfbBranchCodeRepository.findAll().size();

        // Update the mfbBranchCode
        MfbBranchCode updatedMfbBranchCode = mfbBranchCodeRepository.findById(mfbBranchCode.getId()).get();
        // Disconnect from session so that the updates on updatedMfbBranchCode are not directly saved in db
        em.detach(updatedMfbBranchCode);
        updatedMfbBranchCode
            .bankCode(UPDATED_BANK_CODE)
            .bankName(UPDATED_BANK_NAME)
            .branchCode(UPDATED_BRANCH_CODE)
            .branchName(UPDATED_BRANCH_NAME);
        MfbBranchCodeDTO mfbBranchCodeDTO = mfbBranchCodeMapper.toDto(updatedMfbBranchCode);

        restMfbBranchCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mfbBranchCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mfbBranchCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the MfbBranchCode in the database
        List<MfbBranchCode> mfbBranchCodeList = mfbBranchCodeRepository.findAll();
        assertThat(mfbBranchCodeList).hasSize(databaseSizeBeforeUpdate);
        MfbBranchCode testMfbBranchCode = mfbBranchCodeList.get(mfbBranchCodeList.size() - 1);
        assertThat(testMfbBranchCode.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
        assertThat(testMfbBranchCode.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testMfbBranchCode.getBranchCode()).isEqualTo(UPDATED_BRANCH_CODE);
        assertThat(testMfbBranchCode.getBranchName()).isEqualTo(UPDATED_BRANCH_NAME);

        // Validate the MfbBranchCode in Elasticsearch
        verify(mockMfbBranchCodeSearchRepository).save(testMfbBranchCode);
    }

    @Test
    @Transactional
    void putNonExistingMfbBranchCode() throws Exception {
        int databaseSizeBeforeUpdate = mfbBranchCodeRepository.findAll().size();
        mfbBranchCode.setId(count.incrementAndGet());

        // Create the MfbBranchCode
        MfbBranchCodeDTO mfbBranchCodeDTO = mfbBranchCodeMapper.toDto(mfbBranchCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMfbBranchCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mfbBranchCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mfbBranchCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MfbBranchCode in the database
        List<MfbBranchCode> mfbBranchCodeList = mfbBranchCodeRepository.findAll();
        assertThat(mfbBranchCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MfbBranchCode in Elasticsearch
        verify(mockMfbBranchCodeSearchRepository, times(0)).save(mfbBranchCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchMfbBranchCode() throws Exception {
        int databaseSizeBeforeUpdate = mfbBranchCodeRepository.findAll().size();
        mfbBranchCode.setId(count.incrementAndGet());

        // Create the MfbBranchCode
        MfbBranchCodeDTO mfbBranchCodeDTO = mfbBranchCodeMapper.toDto(mfbBranchCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMfbBranchCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mfbBranchCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MfbBranchCode in the database
        List<MfbBranchCode> mfbBranchCodeList = mfbBranchCodeRepository.findAll();
        assertThat(mfbBranchCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MfbBranchCode in Elasticsearch
        verify(mockMfbBranchCodeSearchRepository, times(0)).save(mfbBranchCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMfbBranchCode() throws Exception {
        int databaseSizeBeforeUpdate = mfbBranchCodeRepository.findAll().size();
        mfbBranchCode.setId(count.incrementAndGet());

        // Create the MfbBranchCode
        MfbBranchCodeDTO mfbBranchCodeDTO = mfbBranchCodeMapper.toDto(mfbBranchCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMfbBranchCodeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mfbBranchCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MfbBranchCode in the database
        List<MfbBranchCode> mfbBranchCodeList = mfbBranchCodeRepository.findAll();
        assertThat(mfbBranchCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MfbBranchCode in Elasticsearch
        verify(mockMfbBranchCodeSearchRepository, times(0)).save(mfbBranchCode);
    }

    @Test
    @Transactional
    void partialUpdateMfbBranchCodeWithPatch() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        int databaseSizeBeforeUpdate = mfbBranchCodeRepository.findAll().size();

        // Update the mfbBranchCode using partial update
        MfbBranchCode partialUpdatedMfbBranchCode = new MfbBranchCode();
        partialUpdatedMfbBranchCode.setId(mfbBranchCode.getId());

        partialUpdatedMfbBranchCode
            .bankCode(UPDATED_BANK_CODE)
            .bankName(UPDATED_BANK_NAME)
            .branchCode(UPDATED_BRANCH_CODE)
            .branchName(UPDATED_BRANCH_NAME);

        restMfbBranchCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMfbBranchCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMfbBranchCode))
            )
            .andExpect(status().isOk());

        // Validate the MfbBranchCode in the database
        List<MfbBranchCode> mfbBranchCodeList = mfbBranchCodeRepository.findAll();
        assertThat(mfbBranchCodeList).hasSize(databaseSizeBeforeUpdate);
        MfbBranchCode testMfbBranchCode = mfbBranchCodeList.get(mfbBranchCodeList.size() - 1);
        assertThat(testMfbBranchCode.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
        assertThat(testMfbBranchCode.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testMfbBranchCode.getBranchCode()).isEqualTo(UPDATED_BRANCH_CODE);
        assertThat(testMfbBranchCode.getBranchName()).isEqualTo(UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void fullUpdateMfbBranchCodeWithPatch() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        int databaseSizeBeforeUpdate = mfbBranchCodeRepository.findAll().size();

        // Update the mfbBranchCode using partial update
        MfbBranchCode partialUpdatedMfbBranchCode = new MfbBranchCode();
        partialUpdatedMfbBranchCode.setId(mfbBranchCode.getId());

        partialUpdatedMfbBranchCode
            .bankCode(UPDATED_BANK_CODE)
            .bankName(UPDATED_BANK_NAME)
            .branchCode(UPDATED_BRANCH_CODE)
            .branchName(UPDATED_BRANCH_NAME);

        restMfbBranchCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMfbBranchCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMfbBranchCode))
            )
            .andExpect(status().isOk());

        // Validate the MfbBranchCode in the database
        List<MfbBranchCode> mfbBranchCodeList = mfbBranchCodeRepository.findAll();
        assertThat(mfbBranchCodeList).hasSize(databaseSizeBeforeUpdate);
        MfbBranchCode testMfbBranchCode = mfbBranchCodeList.get(mfbBranchCodeList.size() - 1);
        assertThat(testMfbBranchCode.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
        assertThat(testMfbBranchCode.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testMfbBranchCode.getBranchCode()).isEqualTo(UPDATED_BRANCH_CODE);
        assertThat(testMfbBranchCode.getBranchName()).isEqualTo(UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingMfbBranchCode() throws Exception {
        int databaseSizeBeforeUpdate = mfbBranchCodeRepository.findAll().size();
        mfbBranchCode.setId(count.incrementAndGet());

        // Create the MfbBranchCode
        MfbBranchCodeDTO mfbBranchCodeDTO = mfbBranchCodeMapper.toDto(mfbBranchCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMfbBranchCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mfbBranchCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mfbBranchCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MfbBranchCode in the database
        List<MfbBranchCode> mfbBranchCodeList = mfbBranchCodeRepository.findAll();
        assertThat(mfbBranchCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MfbBranchCode in Elasticsearch
        verify(mockMfbBranchCodeSearchRepository, times(0)).save(mfbBranchCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMfbBranchCode() throws Exception {
        int databaseSizeBeforeUpdate = mfbBranchCodeRepository.findAll().size();
        mfbBranchCode.setId(count.incrementAndGet());

        // Create the MfbBranchCode
        MfbBranchCodeDTO mfbBranchCodeDTO = mfbBranchCodeMapper.toDto(mfbBranchCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMfbBranchCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mfbBranchCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MfbBranchCode in the database
        List<MfbBranchCode> mfbBranchCodeList = mfbBranchCodeRepository.findAll();
        assertThat(mfbBranchCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MfbBranchCode in Elasticsearch
        verify(mockMfbBranchCodeSearchRepository, times(0)).save(mfbBranchCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMfbBranchCode() throws Exception {
        int databaseSizeBeforeUpdate = mfbBranchCodeRepository.findAll().size();
        mfbBranchCode.setId(count.incrementAndGet());

        // Create the MfbBranchCode
        MfbBranchCodeDTO mfbBranchCodeDTO = mfbBranchCodeMapper.toDto(mfbBranchCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMfbBranchCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mfbBranchCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MfbBranchCode in the database
        List<MfbBranchCode> mfbBranchCodeList = mfbBranchCodeRepository.findAll();
        assertThat(mfbBranchCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MfbBranchCode in Elasticsearch
        verify(mockMfbBranchCodeSearchRepository, times(0)).save(mfbBranchCode);
    }

    @Test
    @Transactional
    void deleteMfbBranchCode() throws Exception {
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);

        int databaseSizeBeforeDelete = mfbBranchCodeRepository.findAll().size();

        // Delete the mfbBranchCode
        restMfbBranchCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, mfbBranchCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MfbBranchCode> mfbBranchCodeList = mfbBranchCodeRepository.findAll();
        assertThat(mfbBranchCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MfbBranchCode in Elasticsearch
        verify(mockMfbBranchCodeSearchRepository, times(1)).deleteById(mfbBranchCode.getId());
    }

    @Test
    @Transactional
    void searchMfbBranchCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        mfbBranchCodeRepository.saveAndFlush(mfbBranchCode);
        when(mockMfbBranchCodeSearchRepository.search("id:" + mfbBranchCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(mfbBranchCode), PageRequest.of(0, 1), 1));

        // Search the mfbBranchCode
        restMfbBranchCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + mfbBranchCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mfbBranchCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].bankCode").value(hasItem(DEFAULT_BANK_CODE)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)));
    }
}
