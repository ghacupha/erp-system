package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
import io.github.erp.domain.BankBranchCode;
import io.github.erp.domain.Placeholder;
import io.github.erp.erp.resources.PlaceholderResourceIT;
import io.github.erp.repository.BankBranchCodeRepository;
import io.github.erp.repository.search.BankBranchCodeSearchRepository;
import io.github.erp.service.BankBranchCodeService;
import io.github.erp.service.dto.BankBranchCodeDTO;
import io.github.erp.service.mapper.BankBranchCodeMapper;
import io.github.erp.web.rest.BankBranchCodeResource;
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

import javax.persistence.EntityManager;
import java.util.ArrayList;
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
 * Integration tests for the {@link BankBranchCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER", "FIXED_ASSETS_USER"})
public class BankBranchCodeResourceIT {

    private static final String DEFAULT_BANK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BANK_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/bank-branch-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/bank-branch-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BankBranchCodeRepository bankBranchCodeRepository;

    @Mock
    private BankBranchCodeRepository bankBranchCodeRepositoryMock;

    @Autowired
    private BankBranchCodeMapper bankBranchCodeMapper;

    @Mock
    private BankBranchCodeService bankBranchCodeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.BankBranchCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private BankBranchCodeSearchRepository mockBankBranchCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankBranchCodeMockMvc;

    private BankBranchCode bankBranchCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankBranchCode createEntity(EntityManager em) {
        BankBranchCode bankBranchCode = new BankBranchCode()
            .bankCode(DEFAULT_BANK_CODE)
            .bankName(DEFAULT_BANK_NAME)
            .branchCode(DEFAULT_BRANCH_CODE)
            .branchName(DEFAULT_BRANCH_NAME)
            .notes(DEFAULT_NOTES);
        return bankBranchCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankBranchCode createUpdatedEntity(EntityManager em) {
        BankBranchCode bankBranchCode = new BankBranchCode()
            .bankCode(UPDATED_BANK_CODE)
            .bankName(UPDATED_BANK_NAME)
            .branchCode(UPDATED_BRANCH_CODE)
            .branchName(UPDATED_BRANCH_NAME)
            .notes(UPDATED_NOTES);
        return bankBranchCode;
    }

    @BeforeEach
    public void initTest() {
        bankBranchCode = createEntity(em);
    }

    @Test
    @Transactional
    void createBankBranchCode() throws Exception {
        int databaseSizeBeforeCreate = bankBranchCodeRepository.findAll().size();
        // Create the BankBranchCode
        BankBranchCodeDTO bankBranchCodeDTO = bankBranchCodeMapper.toDto(bankBranchCode);
        restBankBranchCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankBranchCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BankBranchCode in the database
        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeCreate + 1);
        BankBranchCode testBankBranchCode = bankBranchCodeList.get(bankBranchCodeList.size() - 1);
        assertThat(testBankBranchCode.getBankCode()).isEqualTo(DEFAULT_BANK_CODE);
        assertThat(testBankBranchCode.getBankName()).isEqualTo(DEFAULT_BANK_NAME);
        assertThat(testBankBranchCode.getBranchCode()).isEqualTo(DEFAULT_BRANCH_CODE);
        assertThat(testBankBranchCode.getBranchName()).isEqualTo(DEFAULT_BRANCH_NAME);
        assertThat(testBankBranchCode.getNotes()).isEqualTo(DEFAULT_NOTES);

        // Validate the BankBranchCode in Elasticsearch
        verify(mockBankBranchCodeSearchRepository, times(1)).save(testBankBranchCode);
    }

    @Test
    @Transactional
    void createBankBranchCodeWithExistingId() throws Exception {
        // Create the BankBranchCode with an existing ID
        bankBranchCode.setId(1L);
        BankBranchCodeDTO bankBranchCodeDTO = bankBranchCodeMapper.toDto(bankBranchCode);

        int databaseSizeBeforeCreate = bankBranchCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankBranchCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankBranchCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankBranchCode in the database
        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the BankBranchCode in Elasticsearch
        verify(mockBankBranchCodeSearchRepository, times(0)).save(bankBranchCode);
    }

    @Test
    @Transactional
    void checkBankNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankBranchCodeRepository.findAll().size();
        // set the field null
        bankBranchCode.setBankName(null);

        // Create the BankBranchCode, which fails.
        BankBranchCodeDTO bankBranchCodeDTO = bankBranchCodeMapper.toDto(bankBranchCode);

        restBankBranchCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankBranchCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBranchCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankBranchCodeRepository.findAll().size();
        // set the field null
        bankBranchCode.setBranchCode(null);

        // Create the BankBranchCode, which fails.
        BankBranchCodeDTO bankBranchCodeDTO = bankBranchCodeMapper.toDto(bankBranchCode);

        restBankBranchCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankBranchCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBankBranchCodes() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList
        restBankBranchCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankBranchCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].bankCode").value(hasItem(DEFAULT_BANK_CODE)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBankBranchCodesWithEagerRelationshipsIsEnabled() throws Exception {
        when(bankBranchCodeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBankBranchCodeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bankBranchCodeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBankBranchCodesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(bankBranchCodeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBankBranchCodeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(bankBranchCodeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBankBranchCode() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get the bankBranchCode
        restBankBranchCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, bankBranchCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankBranchCode.getId().intValue()))
            .andExpect(jsonPath("$.bankCode").value(DEFAULT_BANK_CODE))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME))
            .andExpect(jsonPath("$.branchCode").value(DEFAULT_BRANCH_CODE))
            .andExpect(jsonPath("$.branchName").value(DEFAULT_BRANCH_NAME))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getBankBranchCodesByIdFiltering() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        Long id = bankBranchCode.getId();

        defaultBankBranchCodeShouldBeFound("id.equals=" + id);
        defaultBankBranchCodeShouldNotBeFound("id.notEquals=" + id);

        defaultBankBranchCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBankBranchCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultBankBranchCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBankBranchCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBankCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where bankCode equals to DEFAULT_BANK_CODE
        defaultBankBranchCodeShouldBeFound("bankCode.equals=" + DEFAULT_BANK_CODE);

        // Get all the bankBranchCodeList where bankCode equals to UPDATED_BANK_CODE
        defaultBankBranchCodeShouldNotBeFound("bankCode.equals=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBankCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where bankCode not equals to DEFAULT_BANK_CODE
        defaultBankBranchCodeShouldNotBeFound("bankCode.notEquals=" + DEFAULT_BANK_CODE);

        // Get all the bankBranchCodeList where bankCode not equals to UPDATED_BANK_CODE
        defaultBankBranchCodeShouldBeFound("bankCode.notEquals=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBankCodeIsInShouldWork() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where bankCode in DEFAULT_BANK_CODE or UPDATED_BANK_CODE
        defaultBankBranchCodeShouldBeFound("bankCode.in=" + DEFAULT_BANK_CODE + "," + UPDATED_BANK_CODE);

        // Get all the bankBranchCodeList where bankCode equals to UPDATED_BANK_CODE
        defaultBankBranchCodeShouldNotBeFound("bankCode.in=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBankCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where bankCode is not null
        defaultBankBranchCodeShouldBeFound("bankCode.specified=true");

        // Get all the bankBranchCodeList where bankCode is null
        defaultBankBranchCodeShouldNotBeFound("bankCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBankCodeContainsSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where bankCode contains DEFAULT_BANK_CODE
        defaultBankBranchCodeShouldBeFound("bankCode.contains=" + DEFAULT_BANK_CODE);

        // Get all the bankBranchCodeList where bankCode contains UPDATED_BANK_CODE
        defaultBankBranchCodeShouldNotBeFound("bankCode.contains=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBankCodeNotContainsSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where bankCode does not contain DEFAULT_BANK_CODE
        defaultBankBranchCodeShouldNotBeFound("bankCode.doesNotContain=" + DEFAULT_BANK_CODE);

        // Get all the bankBranchCodeList where bankCode does not contain UPDATED_BANK_CODE
        defaultBankBranchCodeShouldBeFound("bankCode.doesNotContain=" + UPDATED_BANK_CODE);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBankNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where bankName equals to DEFAULT_BANK_NAME
        defaultBankBranchCodeShouldBeFound("bankName.equals=" + DEFAULT_BANK_NAME);

        // Get all the bankBranchCodeList where bankName equals to UPDATED_BANK_NAME
        defaultBankBranchCodeShouldNotBeFound("bankName.equals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBankNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where bankName not equals to DEFAULT_BANK_NAME
        defaultBankBranchCodeShouldNotBeFound("bankName.notEquals=" + DEFAULT_BANK_NAME);

        // Get all the bankBranchCodeList where bankName not equals to UPDATED_BANK_NAME
        defaultBankBranchCodeShouldBeFound("bankName.notEquals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBankNameIsInShouldWork() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where bankName in DEFAULT_BANK_NAME or UPDATED_BANK_NAME
        defaultBankBranchCodeShouldBeFound("bankName.in=" + DEFAULT_BANK_NAME + "," + UPDATED_BANK_NAME);

        // Get all the bankBranchCodeList where bankName equals to UPDATED_BANK_NAME
        defaultBankBranchCodeShouldNotBeFound("bankName.in=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBankNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where bankName is not null
        defaultBankBranchCodeShouldBeFound("bankName.specified=true");

        // Get all the bankBranchCodeList where bankName is null
        defaultBankBranchCodeShouldNotBeFound("bankName.specified=false");
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBankNameContainsSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where bankName contains DEFAULT_BANK_NAME
        defaultBankBranchCodeShouldBeFound("bankName.contains=" + DEFAULT_BANK_NAME);

        // Get all the bankBranchCodeList where bankName contains UPDATED_BANK_NAME
        defaultBankBranchCodeShouldNotBeFound("bankName.contains=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBankNameNotContainsSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where bankName does not contain DEFAULT_BANK_NAME
        defaultBankBranchCodeShouldNotBeFound("bankName.doesNotContain=" + DEFAULT_BANK_NAME);

        // Get all the bankBranchCodeList where bankName does not contain UPDATED_BANK_NAME
        defaultBankBranchCodeShouldBeFound("bankName.doesNotContain=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBranchCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where branchCode equals to DEFAULT_BRANCH_CODE
        defaultBankBranchCodeShouldBeFound("branchCode.equals=" + DEFAULT_BRANCH_CODE);

        // Get all the bankBranchCodeList where branchCode equals to UPDATED_BRANCH_CODE
        defaultBankBranchCodeShouldNotBeFound("branchCode.equals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBranchCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where branchCode not equals to DEFAULT_BRANCH_CODE
        defaultBankBranchCodeShouldNotBeFound("branchCode.notEquals=" + DEFAULT_BRANCH_CODE);

        // Get all the bankBranchCodeList where branchCode not equals to UPDATED_BRANCH_CODE
        defaultBankBranchCodeShouldBeFound("branchCode.notEquals=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBranchCodeIsInShouldWork() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where branchCode in DEFAULT_BRANCH_CODE or UPDATED_BRANCH_CODE
        defaultBankBranchCodeShouldBeFound("branchCode.in=" + DEFAULT_BRANCH_CODE + "," + UPDATED_BRANCH_CODE);

        // Get all the bankBranchCodeList where branchCode equals to UPDATED_BRANCH_CODE
        defaultBankBranchCodeShouldNotBeFound("branchCode.in=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBranchCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where branchCode is not null
        defaultBankBranchCodeShouldBeFound("branchCode.specified=true");

        // Get all the bankBranchCodeList where branchCode is null
        defaultBankBranchCodeShouldNotBeFound("branchCode.specified=false");
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBranchCodeContainsSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where branchCode contains DEFAULT_BRANCH_CODE
        defaultBankBranchCodeShouldBeFound("branchCode.contains=" + DEFAULT_BRANCH_CODE);

        // Get all the bankBranchCodeList where branchCode contains UPDATED_BRANCH_CODE
        defaultBankBranchCodeShouldNotBeFound("branchCode.contains=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBranchCodeNotContainsSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where branchCode does not contain DEFAULT_BRANCH_CODE
        defaultBankBranchCodeShouldNotBeFound("branchCode.doesNotContain=" + DEFAULT_BRANCH_CODE);

        // Get all the bankBranchCodeList where branchCode does not contain UPDATED_BRANCH_CODE
        defaultBankBranchCodeShouldBeFound("branchCode.doesNotContain=" + UPDATED_BRANCH_CODE);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBranchNameIsEqualToSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where branchName equals to DEFAULT_BRANCH_NAME
        defaultBankBranchCodeShouldBeFound("branchName.equals=" + DEFAULT_BRANCH_NAME);

        // Get all the bankBranchCodeList where branchName equals to UPDATED_BRANCH_NAME
        defaultBankBranchCodeShouldNotBeFound("branchName.equals=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBranchNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where branchName not equals to DEFAULT_BRANCH_NAME
        defaultBankBranchCodeShouldNotBeFound("branchName.notEquals=" + DEFAULT_BRANCH_NAME);

        // Get all the bankBranchCodeList where branchName not equals to UPDATED_BRANCH_NAME
        defaultBankBranchCodeShouldBeFound("branchName.notEquals=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBranchNameIsInShouldWork() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where branchName in DEFAULT_BRANCH_NAME or UPDATED_BRANCH_NAME
        defaultBankBranchCodeShouldBeFound("branchName.in=" + DEFAULT_BRANCH_NAME + "," + UPDATED_BRANCH_NAME);

        // Get all the bankBranchCodeList where branchName equals to UPDATED_BRANCH_NAME
        defaultBankBranchCodeShouldNotBeFound("branchName.in=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBranchNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where branchName is not null
        defaultBankBranchCodeShouldBeFound("branchName.specified=true");

        // Get all the bankBranchCodeList where branchName is null
        defaultBankBranchCodeShouldNotBeFound("branchName.specified=false");
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBranchNameContainsSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where branchName contains DEFAULT_BRANCH_NAME
        defaultBankBranchCodeShouldBeFound("branchName.contains=" + DEFAULT_BRANCH_NAME);

        // Get all the bankBranchCodeList where branchName contains UPDATED_BRANCH_NAME
        defaultBankBranchCodeShouldNotBeFound("branchName.contains=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByBranchNameNotContainsSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where branchName does not contain DEFAULT_BRANCH_NAME
        defaultBankBranchCodeShouldNotBeFound("branchName.doesNotContain=" + DEFAULT_BRANCH_NAME);

        // Get all the bankBranchCodeList where branchName does not contain UPDATED_BRANCH_NAME
        defaultBankBranchCodeShouldBeFound("branchName.doesNotContain=" + UPDATED_BRANCH_NAME);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where notes equals to DEFAULT_NOTES
        defaultBankBranchCodeShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the bankBranchCodeList where notes equals to UPDATED_NOTES
        defaultBankBranchCodeShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where notes not equals to DEFAULT_NOTES
        defaultBankBranchCodeShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the bankBranchCodeList where notes not equals to UPDATED_NOTES
        defaultBankBranchCodeShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultBankBranchCodeShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the bankBranchCodeList where notes equals to UPDATED_NOTES
        defaultBankBranchCodeShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where notes is not null
        defaultBankBranchCodeShouldBeFound("notes.specified=true");

        // Get all the bankBranchCodeList where notes is null
        defaultBankBranchCodeShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByNotesContainsSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where notes contains DEFAULT_NOTES
        defaultBankBranchCodeShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the bankBranchCodeList where notes contains UPDATED_NOTES
        defaultBankBranchCodeShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        // Get all the bankBranchCodeList where notes does not contain DEFAULT_NOTES
        defaultBankBranchCodeShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the bankBranchCodeList where notes does not contain UPDATED_NOTES
        defaultBankBranchCodeShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllBankBranchCodesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);
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
        bankBranchCode.addPlaceholder(placeholder);
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);
        Long placeholderId = placeholder.getId();

        // Get all the bankBranchCodeList where placeholder equals to placeholderId
        defaultBankBranchCodeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the bankBranchCodeList where placeholder equals to (placeholderId + 1)
        defaultBankBranchCodeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBankBranchCodeShouldBeFound(String filter) throws Exception {
        restBankBranchCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankBranchCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].bankCode").value(hasItem(DEFAULT_BANK_CODE)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restBankBranchCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBankBranchCodeShouldNotBeFound(String filter) throws Exception {
        restBankBranchCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBankBranchCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBankBranchCode() throws Exception {
        // Get the bankBranchCode
        restBankBranchCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBankBranchCode() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        int databaseSizeBeforeUpdate = bankBranchCodeRepository.findAll().size();

        // Update the bankBranchCode
        BankBranchCode updatedBankBranchCode = bankBranchCodeRepository.findById(bankBranchCode.getId()).get();
        // Disconnect from session so that the updates on updatedBankBranchCode are not directly saved in db
        em.detach(updatedBankBranchCode);
        updatedBankBranchCode
            .bankCode(UPDATED_BANK_CODE)
            .bankName(UPDATED_BANK_NAME)
            .branchCode(UPDATED_BRANCH_CODE)
            .branchName(UPDATED_BRANCH_NAME)
            .notes(UPDATED_NOTES);
        BankBranchCodeDTO bankBranchCodeDTO = bankBranchCodeMapper.toDto(updatedBankBranchCode);

        restBankBranchCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankBranchCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankBranchCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the BankBranchCode in the database
        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeUpdate);
        BankBranchCode testBankBranchCode = bankBranchCodeList.get(bankBranchCodeList.size() - 1);
        assertThat(testBankBranchCode.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
        assertThat(testBankBranchCode.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testBankBranchCode.getBranchCode()).isEqualTo(UPDATED_BRANCH_CODE);
        assertThat(testBankBranchCode.getBranchName()).isEqualTo(UPDATED_BRANCH_NAME);
        assertThat(testBankBranchCode.getNotes()).isEqualTo(UPDATED_NOTES);

        // Validate the BankBranchCode in Elasticsearch
        verify(mockBankBranchCodeSearchRepository).save(testBankBranchCode);
    }

    @Test
    @Transactional
    void putNonExistingBankBranchCode() throws Exception {
        int databaseSizeBeforeUpdate = bankBranchCodeRepository.findAll().size();
        bankBranchCode.setId(count.incrementAndGet());

        // Create the BankBranchCode
        BankBranchCodeDTO bankBranchCodeDTO = bankBranchCodeMapper.toDto(bankBranchCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankBranchCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankBranchCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankBranchCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankBranchCode in the database
        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BankBranchCode in Elasticsearch
        verify(mockBankBranchCodeSearchRepository, times(0)).save(bankBranchCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchBankBranchCode() throws Exception {
        int databaseSizeBeforeUpdate = bankBranchCodeRepository.findAll().size();
        bankBranchCode.setId(count.incrementAndGet());

        // Create the BankBranchCode
        BankBranchCodeDTO bankBranchCodeDTO = bankBranchCodeMapper.toDto(bankBranchCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankBranchCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bankBranchCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankBranchCode in the database
        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BankBranchCode in Elasticsearch
        verify(mockBankBranchCodeSearchRepository, times(0)).save(bankBranchCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBankBranchCode() throws Exception {
        int databaseSizeBeforeUpdate = bankBranchCodeRepository.findAll().size();
        bankBranchCode.setId(count.incrementAndGet());

        // Create the BankBranchCode
        BankBranchCodeDTO bankBranchCodeDTO = bankBranchCodeMapper.toDto(bankBranchCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankBranchCodeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bankBranchCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankBranchCode in the database
        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BankBranchCode in Elasticsearch
        verify(mockBankBranchCodeSearchRepository, times(0)).save(bankBranchCode);
    }

    @Test
    @Transactional
    void partialUpdateBankBranchCodeWithPatch() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        int databaseSizeBeforeUpdate = bankBranchCodeRepository.findAll().size();

        // Update the bankBranchCode using partial update
        BankBranchCode partialUpdatedBankBranchCode = new BankBranchCode();
        partialUpdatedBankBranchCode.setId(bankBranchCode.getId());

        partialUpdatedBankBranchCode.bankCode(UPDATED_BANK_CODE).bankName(UPDATED_BANK_NAME);

        restBankBranchCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankBranchCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBankBranchCode))
            )
            .andExpect(status().isOk());

        // Validate the BankBranchCode in the database
        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeUpdate);
        BankBranchCode testBankBranchCode = bankBranchCodeList.get(bankBranchCodeList.size() - 1);
        assertThat(testBankBranchCode.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
        assertThat(testBankBranchCode.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testBankBranchCode.getBranchCode()).isEqualTo(DEFAULT_BRANCH_CODE);
        assertThat(testBankBranchCode.getBranchName()).isEqualTo(DEFAULT_BRANCH_NAME);
        assertThat(testBankBranchCode.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateBankBranchCodeWithPatch() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        int databaseSizeBeforeUpdate = bankBranchCodeRepository.findAll().size();

        // Update the bankBranchCode using partial update
        BankBranchCode partialUpdatedBankBranchCode = new BankBranchCode();
        partialUpdatedBankBranchCode.setId(bankBranchCode.getId());

        partialUpdatedBankBranchCode
            .bankCode(UPDATED_BANK_CODE)
            .bankName(UPDATED_BANK_NAME)
            .branchCode(UPDATED_BRANCH_CODE)
            .branchName(UPDATED_BRANCH_NAME)
            .notes(UPDATED_NOTES);

        restBankBranchCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankBranchCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBankBranchCode))
            )
            .andExpect(status().isOk());

        // Validate the BankBranchCode in the database
        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeUpdate);
        BankBranchCode testBankBranchCode = bankBranchCodeList.get(bankBranchCodeList.size() - 1);
        assertThat(testBankBranchCode.getBankCode()).isEqualTo(UPDATED_BANK_CODE);
        assertThat(testBankBranchCode.getBankName()).isEqualTo(UPDATED_BANK_NAME);
        assertThat(testBankBranchCode.getBranchCode()).isEqualTo(UPDATED_BRANCH_CODE);
        assertThat(testBankBranchCode.getBranchName()).isEqualTo(UPDATED_BRANCH_NAME);
        assertThat(testBankBranchCode.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingBankBranchCode() throws Exception {
        int databaseSizeBeforeUpdate = bankBranchCodeRepository.findAll().size();
        bankBranchCode.setId(count.incrementAndGet());

        // Create the BankBranchCode
        BankBranchCodeDTO bankBranchCodeDTO = bankBranchCodeMapper.toDto(bankBranchCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankBranchCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bankBranchCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bankBranchCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankBranchCode in the database
        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BankBranchCode in Elasticsearch
        verify(mockBankBranchCodeSearchRepository, times(0)).save(bankBranchCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBankBranchCode() throws Exception {
        int databaseSizeBeforeUpdate = bankBranchCodeRepository.findAll().size();
        bankBranchCode.setId(count.incrementAndGet());

        // Create the BankBranchCode
        BankBranchCodeDTO bankBranchCodeDTO = bankBranchCodeMapper.toDto(bankBranchCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankBranchCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bankBranchCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankBranchCode in the database
        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BankBranchCode in Elasticsearch
        verify(mockBankBranchCodeSearchRepository, times(0)).save(bankBranchCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBankBranchCode() throws Exception {
        int databaseSizeBeforeUpdate = bankBranchCodeRepository.findAll().size();
        bankBranchCode.setId(count.incrementAndGet());

        // Create the BankBranchCode
        BankBranchCodeDTO bankBranchCodeDTO = bankBranchCodeMapper.toDto(bankBranchCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankBranchCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bankBranchCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankBranchCode in the database
        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BankBranchCode in Elasticsearch
        verify(mockBankBranchCodeSearchRepository, times(0)).save(bankBranchCode);
    }

    @Test
    @Transactional
    void deleteBankBranchCode() throws Exception {
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);

        int databaseSizeBeforeDelete = bankBranchCodeRepository.findAll().size();

        // Delete the bankBranchCode
        restBankBranchCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, bankBranchCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BankBranchCode> bankBranchCodeList = bankBranchCodeRepository.findAll();
        assertThat(bankBranchCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BankBranchCode in Elasticsearch
        verify(mockBankBranchCodeSearchRepository, times(1)).deleteById(bankBranchCode.getId());
    }

    @Test
    @Transactional
    void searchBankBranchCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        bankBranchCodeRepository.saveAndFlush(bankBranchCode);
        when(mockBankBranchCodeSearchRepository.search("id:" + bankBranchCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(bankBranchCode), PageRequest.of(0, 1), 1));

        // Search the bankBranchCode
        restBankBranchCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + bankBranchCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankBranchCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].bankCode").value(hasItem(DEFAULT_BANK_CODE)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].branchCode").value(hasItem(DEFAULT_BRANCH_CODE)))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }
}
