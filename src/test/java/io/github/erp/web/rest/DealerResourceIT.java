package io.github.erp.web.rest;

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
import io.github.erp.domain.Dealer;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.PaymentLabel;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.DealerRepository;
import io.github.erp.repository.search.DealerSearchRepository;
import io.github.erp.service.DealerService;
import io.github.erp.service.criteria.DealerCriteria;
import io.github.erp.service.dto.DealerDTO;
import io.github.erp.service.mapper.DealerMapper;
import java.util.ArrayList;
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
 * Integration tests for the {@link DealerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DealerResourceIT {

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TAX_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATION_DOCUMENT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANIZATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORGANIZATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHYSICAL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PHYSICAL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BANKERS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANKERS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BANKERS_BRANCH = "AAAAAAAAAA";
    private static final String UPDATED_BANKERS_BRANCH = "BBBBBBBBBB";

    private static final String DEFAULT_BANKERS_SWIFT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BANKERS_SWIFT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_NAMES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dealers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/dealers";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DealerRepository dealerRepository;

    @Mock
    private DealerRepository dealerRepositoryMock;

    @Autowired
    private DealerMapper dealerMapper;

    @Mock
    private DealerService dealerServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DealerSearchRepositoryMockConfiguration
     */
    @Autowired
    private DealerSearchRepository mockDealerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDealerMockMvc;

    private Dealer dealer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dealer createEntity(EntityManager em) {
        Dealer dealer = new Dealer()
            .dealerName(DEFAULT_DEALER_NAME)
            .taxNumber(DEFAULT_TAX_NUMBER)
            .identificationDocumentNumber(DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER)
            .organizationName(DEFAULT_ORGANIZATION_NAME)
            .department(DEFAULT_DEPARTMENT)
            .position(DEFAULT_POSITION)
            .postalAddress(DEFAULT_POSTAL_ADDRESS)
            .physicalAddress(DEFAULT_PHYSICAL_ADDRESS)
            .accountName(DEFAULT_ACCOUNT_NAME)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .bankersName(DEFAULT_BANKERS_NAME)
            .bankersBranch(DEFAULT_BANKERS_BRANCH)
            .bankersSwiftCode(DEFAULT_BANKERS_SWIFT_CODE)
            .fileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN)
            .compilationToken(DEFAULT_COMPILATION_TOKEN)
            .remarks(DEFAULT_REMARKS)
            .otherNames(DEFAULT_OTHER_NAMES);
        return dealer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dealer createUpdatedEntity(EntityManager em) {
        Dealer dealer = new Dealer()
            .dealerName(UPDATED_DEALER_NAME)
            .taxNumber(UPDATED_TAX_NUMBER)
            .identificationDocumentNumber(UPDATED_IDENTIFICATION_DOCUMENT_NUMBER)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .department(UPDATED_DEPARTMENT)
            .position(UPDATED_POSITION)
            .postalAddress(UPDATED_POSTAL_ADDRESS)
            .physicalAddress(UPDATED_PHYSICAL_ADDRESS)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankersName(UPDATED_BANKERS_NAME)
            .bankersBranch(UPDATED_BANKERS_BRANCH)
            .bankersSwiftCode(UPDATED_BANKERS_SWIFT_CODE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN)
            .remarks(UPDATED_REMARKS)
            .otherNames(UPDATED_OTHER_NAMES);
        return dealer;
    }

    @BeforeEach
    public void initTest() {
        dealer = createEntity(em);
    }

    @Test
    @Transactional
    void createDealer() throws Exception {
        int databaseSizeBeforeCreate = dealerRepository.findAll().size();
        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);
        restDealerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isCreated());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeCreate + 1);
        Dealer testDealer = dealerList.get(dealerList.size() - 1);
        assertThat(testDealer.getDealerName()).isEqualTo(DEFAULT_DEALER_NAME);
        assertThat(testDealer.getTaxNumber()).isEqualTo(DEFAULT_TAX_NUMBER);
        assertThat(testDealer.getIdentificationDocumentNumber()).isEqualTo(DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER);
        assertThat(testDealer.getOrganizationName()).isEqualTo(DEFAULT_ORGANIZATION_NAME);
        assertThat(testDealer.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
        assertThat(testDealer.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testDealer.getPostalAddress()).isEqualTo(DEFAULT_POSTAL_ADDRESS);
        assertThat(testDealer.getPhysicalAddress()).isEqualTo(DEFAULT_PHYSICAL_ADDRESS);
        assertThat(testDealer.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testDealer.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testDealer.getBankersName()).isEqualTo(DEFAULT_BANKERS_NAME);
        assertThat(testDealer.getBankersBranch()).isEqualTo(DEFAULT_BANKERS_BRANCH);
        assertThat(testDealer.getBankersSwiftCode()).isEqualTo(DEFAULT_BANKERS_SWIFT_CODE);
        assertThat(testDealer.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testDealer.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);
        assertThat(testDealer.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testDealer.getOtherNames()).isEqualTo(DEFAULT_OTHER_NAMES);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(1)).save(testDealer);
    }

    @Test
    @Transactional
    void createDealerWithExistingId() throws Exception {
        // Create the Dealer with an existing ID
        dealer.setId(1L);
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        int databaseSizeBeforeCreate = dealerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDealerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(0)).save(dealer);
    }

    @Test
    @Transactional
    void checkDealerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealerRepository.findAll().size();
        // set the field null
        dealer.setDealerName(null);

        // Create the Dealer, which fails.
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        restDealerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isBadRequest());

        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDealers() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList
        restDealerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].identificationDocumentNumber").value(hasItem(DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].physicalAddress").value(hasItem(DEFAULT_PHYSICAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankersName").value(hasItem(DEFAULT_BANKERS_NAME)))
            .andExpect(jsonPath("$.[*].bankersBranch").value(hasItem(DEFAULT_BANKERS_BRANCH)))
            .andExpect(jsonPath("$.[*].bankersSwiftCode").value(hasItem(DEFAULT_BANKERS_SWIFT_CODE)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())))
            .andExpect(jsonPath("$.[*].otherNames").value(hasItem(DEFAULT_OTHER_NAMES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDealersWithEagerRelationshipsIsEnabled() throws Exception {
        when(dealerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDealerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dealerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDealersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dealerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDealerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dealerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get the dealer
        restDealerMockMvc
            .perform(get(ENTITY_API_URL_ID, dealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dealer.getId().intValue()))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME))
            .andExpect(jsonPath("$.taxNumber").value(DEFAULT_TAX_NUMBER))
            .andExpect(jsonPath("$.identificationDocumentNumber").value(DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER))
            .andExpect(jsonPath("$.organizationName").value(DEFAULT_ORGANIZATION_NAME))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.postalAddress").value(DEFAULT_POSTAL_ADDRESS))
            .andExpect(jsonPath("$.physicalAddress").value(DEFAULT_PHYSICAL_ADDRESS))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.bankersName").value(DEFAULT_BANKERS_NAME))
            .andExpect(jsonPath("$.bankersBranch").value(DEFAULT_BANKERS_BRANCH))
            .andExpect(jsonPath("$.bankersSwiftCode").value(DEFAULT_BANKERS_SWIFT_CODE))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS.toString()))
            .andExpect(jsonPath("$.otherNames").value(DEFAULT_OTHER_NAMES));
    }

    @Test
    @Transactional
    void getDealersByIdFiltering() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        Long id = dealer.getId();

        defaultDealerShouldBeFound("id.equals=" + id);
        defaultDealerShouldNotBeFound("id.notEquals=" + id);

        defaultDealerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDealerShouldNotBeFound("id.greaterThan=" + id);

        defaultDealerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDealerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDealersByDealerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName equals to DEFAULT_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.equals=" + DEFAULT_DEALER_NAME);

        // Get all the dealerList where dealerName equals to UPDATED_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.equals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByDealerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName not equals to DEFAULT_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.notEquals=" + DEFAULT_DEALER_NAME);

        // Get all the dealerList where dealerName not equals to UPDATED_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.notEquals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByDealerNameIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName in DEFAULT_DEALER_NAME or UPDATED_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.in=" + DEFAULT_DEALER_NAME + "," + UPDATED_DEALER_NAME);

        // Get all the dealerList where dealerName equals to UPDATED_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.in=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByDealerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName is not null
        defaultDealerShouldBeFound("dealerName.specified=true");

        // Get all the dealerList where dealerName is null
        defaultDealerShouldNotBeFound("dealerName.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByDealerNameContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName contains DEFAULT_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.contains=" + DEFAULT_DEALER_NAME);

        // Get all the dealerList where dealerName contains UPDATED_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.contains=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByDealerNameNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName does not contain DEFAULT_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.doesNotContain=" + DEFAULT_DEALER_NAME);

        // Get all the dealerList where dealerName does not contain UPDATED_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.doesNotContain=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByTaxNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber equals to DEFAULT_TAX_NUMBER
        defaultDealerShouldBeFound("taxNumber.equals=" + DEFAULT_TAX_NUMBER);

        // Get all the dealerList where taxNumber equals to UPDATED_TAX_NUMBER
        defaultDealerShouldNotBeFound("taxNumber.equals=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByTaxNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber not equals to DEFAULT_TAX_NUMBER
        defaultDealerShouldNotBeFound("taxNumber.notEquals=" + DEFAULT_TAX_NUMBER);

        // Get all the dealerList where taxNumber not equals to UPDATED_TAX_NUMBER
        defaultDealerShouldBeFound("taxNumber.notEquals=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByTaxNumberIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber in DEFAULT_TAX_NUMBER or UPDATED_TAX_NUMBER
        defaultDealerShouldBeFound("taxNumber.in=" + DEFAULT_TAX_NUMBER + "," + UPDATED_TAX_NUMBER);

        // Get all the dealerList where taxNumber equals to UPDATED_TAX_NUMBER
        defaultDealerShouldNotBeFound("taxNumber.in=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByTaxNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber is not null
        defaultDealerShouldBeFound("taxNumber.specified=true");

        // Get all the dealerList where taxNumber is null
        defaultDealerShouldNotBeFound("taxNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByTaxNumberContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber contains DEFAULT_TAX_NUMBER
        defaultDealerShouldBeFound("taxNumber.contains=" + DEFAULT_TAX_NUMBER);

        // Get all the dealerList where taxNumber contains UPDATED_TAX_NUMBER
        defaultDealerShouldNotBeFound("taxNumber.contains=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByTaxNumberNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber does not contain DEFAULT_TAX_NUMBER
        defaultDealerShouldNotBeFound("taxNumber.doesNotContain=" + DEFAULT_TAX_NUMBER);

        // Get all the dealerList where taxNumber does not contain UPDATED_TAX_NUMBER
        defaultDealerShouldBeFound("taxNumber.doesNotContain=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByIdentificationDocumentNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where identificationDocumentNumber equals to DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER
        defaultDealerShouldBeFound("identificationDocumentNumber.equals=" + DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER);

        // Get all the dealerList where identificationDocumentNumber equals to UPDATED_IDENTIFICATION_DOCUMENT_NUMBER
        defaultDealerShouldNotBeFound("identificationDocumentNumber.equals=" + UPDATED_IDENTIFICATION_DOCUMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByIdentificationDocumentNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where identificationDocumentNumber not equals to DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER
        defaultDealerShouldNotBeFound("identificationDocumentNumber.notEquals=" + DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER);

        // Get all the dealerList where identificationDocumentNumber not equals to UPDATED_IDENTIFICATION_DOCUMENT_NUMBER
        defaultDealerShouldBeFound("identificationDocumentNumber.notEquals=" + UPDATED_IDENTIFICATION_DOCUMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByIdentificationDocumentNumberIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where identificationDocumentNumber in DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER or UPDATED_IDENTIFICATION_DOCUMENT_NUMBER
        defaultDealerShouldBeFound(
            "identificationDocumentNumber.in=" + DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER + "," + UPDATED_IDENTIFICATION_DOCUMENT_NUMBER
        );

        // Get all the dealerList where identificationDocumentNumber equals to UPDATED_IDENTIFICATION_DOCUMENT_NUMBER
        defaultDealerShouldNotBeFound("identificationDocumentNumber.in=" + UPDATED_IDENTIFICATION_DOCUMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByIdentificationDocumentNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where identificationDocumentNumber is not null
        defaultDealerShouldBeFound("identificationDocumentNumber.specified=true");

        // Get all the dealerList where identificationDocumentNumber is null
        defaultDealerShouldNotBeFound("identificationDocumentNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByIdentificationDocumentNumberContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where identificationDocumentNumber contains DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER
        defaultDealerShouldBeFound("identificationDocumentNumber.contains=" + DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER);

        // Get all the dealerList where identificationDocumentNumber contains UPDATED_IDENTIFICATION_DOCUMENT_NUMBER
        defaultDealerShouldNotBeFound("identificationDocumentNumber.contains=" + UPDATED_IDENTIFICATION_DOCUMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByIdentificationDocumentNumberNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where identificationDocumentNumber does not contain DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER
        defaultDealerShouldNotBeFound("identificationDocumentNumber.doesNotContain=" + DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER);

        // Get all the dealerList where identificationDocumentNumber does not contain UPDATED_IDENTIFICATION_DOCUMENT_NUMBER
        defaultDealerShouldBeFound("identificationDocumentNumber.doesNotContain=" + UPDATED_IDENTIFICATION_DOCUMENT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByOrganizationNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where organizationName equals to DEFAULT_ORGANIZATION_NAME
        defaultDealerShouldBeFound("organizationName.equals=" + DEFAULT_ORGANIZATION_NAME);

        // Get all the dealerList where organizationName equals to UPDATED_ORGANIZATION_NAME
        defaultDealerShouldNotBeFound("organizationName.equals=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByOrganizationNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where organizationName not equals to DEFAULT_ORGANIZATION_NAME
        defaultDealerShouldNotBeFound("organizationName.notEquals=" + DEFAULT_ORGANIZATION_NAME);

        // Get all the dealerList where organizationName not equals to UPDATED_ORGANIZATION_NAME
        defaultDealerShouldBeFound("organizationName.notEquals=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByOrganizationNameIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where organizationName in DEFAULT_ORGANIZATION_NAME or UPDATED_ORGANIZATION_NAME
        defaultDealerShouldBeFound("organizationName.in=" + DEFAULT_ORGANIZATION_NAME + "," + UPDATED_ORGANIZATION_NAME);

        // Get all the dealerList where organizationName equals to UPDATED_ORGANIZATION_NAME
        defaultDealerShouldNotBeFound("organizationName.in=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByOrganizationNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where organizationName is not null
        defaultDealerShouldBeFound("organizationName.specified=true");

        // Get all the dealerList where organizationName is null
        defaultDealerShouldNotBeFound("organizationName.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByOrganizationNameContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where organizationName contains DEFAULT_ORGANIZATION_NAME
        defaultDealerShouldBeFound("organizationName.contains=" + DEFAULT_ORGANIZATION_NAME);

        // Get all the dealerList where organizationName contains UPDATED_ORGANIZATION_NAME
        defaultDealerShouldNotBeFound("organizationName.contains=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByOrganizationNameNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where organizationName does not contain DEFAULT_ORGANIZATION_NAME
        defaultDealerShouldNotBeFound("organizationName.doesNotContain=" + DEFAULT_ORGANIZATION_NAME);

        // Get all the dealerList where organizationName does not contain UPDATED_ORGANIZATION_NAME
        defaultDealerShouldBeFound("organizationName.doesNotContain=" + UPDATED_ORGANIZATION_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where department equals to DEFAULT_DEPARTMENT
        defaultDealerShouldBeFound("department.equals=" + DEFAULT_DEPARTMENT);

        // Get all the dealerList where department equals to UPDATED_DEPARTMENT
        defaultDealerShouldNotBeFound("department.equals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllDealersByDepartmentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where department not equals to DEFAULT_DEPARTMENT
        defaultDealerShouldNotBeFound("department.notEquals=" + DEFAULT_DEPARTMENT);

        // Get all the dealerList where department not equals to UPDATED_DEPARTMENT
        defaultDealerShouldBeFound("department.notEquals=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllDealersByDepartmentIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where department in DEFAULT_DEPARTMENT or UPDATED_DEPARTMENT
        defaultDealerShouldBeFound("department.in=" + DEFAULT_DEPARTMENT + "," + UPDATED_DEPARTMENT);

        // Get all the dealerList where department equals to UPDATED_DEPARTMENT
        defaultDealerShouldNotBeFound("department.in=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllDealersByDepartmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where department is not null
        defaultDealerShouldBeFound("department.specified=true");

        // Get all the dealerList where department is null
        defaultDealerShouldNotBeFound("department.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByDepartmentContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where department contains DEFAULT_DEPARTMENT
        defaultDealerShouldBeFound("department.contains=" + DEFAULT_DEPARTMENT);

        // Get all the dealerList where department contains UPDATED_DEPARTMENT
        defaultDealerShouldNotBeFound("department.contains=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllDealersByDepartmentNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where department does not contain DEFAULT_DEPARTMENT
        defaultDealerShouldNotBeFound("department.doesNotContain=" + DEFAULT_DEPARTMENT);

        // Get all the dealerList where department does not contain UPDATED_DEPARTMENT
        defaultDealerShouldBeFound("department.doesNotContain=" + UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void getAllDealersByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where position equals to DEFAULT_POSITION
        defaultDealerShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the dealerList where position equals to UPDATED_POSITION
        defaultDealerShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllDealersByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where position not equals to DEFAULT_POSITION
        defaultDealerShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the dealerList where position not equals to UPDATED_POSITION
        defaultDealerShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllDealersByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultDealerShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the dealerList where position equals to UPDATED_POSITION
        defaultDealerShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllDealersByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where position is not null
        defaultDealerShouldBeFound("position.specified=true");

        // Get all the dealerList where position is null
        defaultDealerShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByPositionContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where position contains DEFAULT_POSITION
        defaultDealerShouldBeFound("position.contains=" + DEFAULT_POSITION);

        // Get all the dealerList where position contains UPDATED_POSITION
        defaultDealerShouldNotBeFound("position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllDealersByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where position does not contain DEFAULT_POSITION
        defaultDealerShouldNotBeFound("position.doesNotContain=" + DEFAULT_POSITION);

        // Get all the dealerList where position does not contain UPDATED_POSITION
        defaultDealerShouldBeFound("position.doesNotContain=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllDealersByPostalAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress equals to DEFAULT_POSTAL_ADDRESS
        defaultDealerShouldBeFound("postalAddress.equals=" + DEFAULT_POSTAL_ADDRESS);

        // Get all the dealerList where postalAddress equals to UPDATED_POSTAL_ADDRESS
        defaultDealerShouldNotBeFound("postalAddress.equals=" + UPDATED_POSTAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByPostalAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress not equals to DEFAULT_POSTAL_ADDRESS
        defaultDealerShouldNotBeFound("postalAddress.notEquals=" + DEFAULT_POSTAL_ADDRESS);

        // Get all the dealerList where postalAddress not equals to UPDATED_POSTAL_ADDRESS
        defaultDealerShouldBeFound("postalAddress.notEquals=" + UPDATED_POSTAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByPostalAddressIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress in DEFAULT_POSTAL_ADDRESS or UPDATED_POSTAL_ADDRESS
        defaultDealerShouldBeFound("postalAddress.in=" + DEFAULT_POSTAL_ADDRESS + "," + UPDATED_POSTAL_ADDRESS);

        // Get all the dealerList where postalAddress equals to UPDATED_POSTAL_ADDRESS
        defaultDealerShouldNotBeFound("postalAddress.in=" + UPDATED_POSTAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByPostalAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress is not null
        defaultDealerShouldBeFound("postalAddress.specified=true");

        // Get all the dealerList where postalAddress is null
        defaultDealerShouldNotBeFound("postalAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByPostalAddressContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress contains DEFAULT_POSTAL_ADDRESS
        defaultDealerShouldBeFound("postalAddress.contains=" + DEFAULT_POSTAL_ADDRESS);

        // Get all the dealerList where postalAddress contains UPDATED_POSTAL_ADDRESS
        defaultDealerShouldNotBeFound("postalAddress.contains=" + UPDATED_POSTAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByPostalAddressNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress does not contain DEFAULT_POSTAL_ADDRESS
        defaultDealerShouldNotBeFound("postalAddress.doesNotContain=" + DEFAULT_POSTAL_ADDRESS);

        // Get all the dealerList where postalAddress does not contain UPDATED_POSTAL_ADDRESS
        defaultDealerShouldBeFound("postalAddress.doesNotContain=" + UPDATED_POSTAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByPhysicalAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress equals to DEFAULT_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("physicalAddress.equals=" + DEFAULT_PHYSICAL_ADDRESS);

        // Get all the dealerList where physicalAddress equals to UPDATED_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("physicalAddress.equals=" + UPDATED_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByPhysicalAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress not equals to DEFAULT_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("physicalAddress.notEquals=" + DEFAULT_PHYSICAL_ADDRESS);

        // Get all the dealerList where physicalAddress not equals to UPDATED_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("physicalAddress.notEquals=" + UPDATED_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByPhysicalAddressIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress in DEFAULT_PHYSICAL_ADDRESS or UPDATED_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("physicalAddress.in=" + DEFAULT_PHYSICAL_ADDRESS + "," + UPDATED_PHYSICAL_ADDRESS);

        // Get all the dealerList where physicalAddress equals to UPDATED_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("physicalAddress.in=" + UPDATED_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByPhysicalAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress is not null
        defaultDealerShouldBeFound("physicalAddress.specified=true");

        // Get all the dealerList where physicalAddress is null
        defaultDealerShouldNotBeFound("physicalAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByPhysicalAddressContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress contains DEFAULT_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("physicalAddress.contains=" + DEFAULT_PHYSICAL_ADDRESS);

        // Get all the dealerList where physicalAddress contains UPDATED_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("physicalAddress.contains=" + UPDATED_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByPhysicalAddressNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress does not contain DEFAULT_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("physicalAddress.doesNotContain=" + DEFAULT_PHYSICAL_ADDRESS);

        // Get all the dealerList where physicalAddress does not contain UPDATED_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("physicalAddress.doesNotContain=" + UPDATED_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName equals to DEFAULT_ACCOUNT_NAME
        defaultDealerShouldBeFound("accountName.equals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the dealerList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultDealerShouldNotBeFound("accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName not equals to DEFAULT_ACCOUNT_NAME
        defaultDealerShouldNotBeFound("accountName.notEquals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the dealerList where accountName not equals to UPDATED_ACCOUNT_NAME
        defaultDealerShouldBeFound("accountName.notEquals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName in DEFAULT_ACCOUNT_NAME or UPDATED_ACCOUNT_NAME
        defaultDealerShouldBeFound("accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME);

        // Get all the dealerList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultDealerShouldNotBeFound("accountName.in=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName is not null
        defaultDealerShouldBeFound("accountName.specified=true");

        // Get all the dealerList where accountName is null
        defaultDealerShouldNotBeFound("accountName.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByAccountNameContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName contains DEFAULT_ACCOUNT_NAME
        defaultDealerShouldBeFound("accountName.contains=" + DEFAULT_ACCOUNT_NAME);

        // Get all the dealerList where accountName contains UPDATED_ACCOUNT_NAME
        defaultDealerShouldNotBeFound("accountName.contains=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName does not contain DEFAULT_ACCOUNT_NAME
        defaultDealerShouldNotBeFound("accountName.doesNotContain=" + DEFAULT_ACCOUNT_NAME);

        // Get all the dealerList where accountName does not contain UPDATED_ACCOUNT_NAME
        defaultDealerShouldBeFound("accountName.doesNotContain=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber equals to DEFAULT_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the dealerList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber not equals to DEFAULT_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("accountNumber.notEquals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the dealerList where accountNumber not equals to UPDATED_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("accountNumber.notEquals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber in DEFAULT_ACCOUNT_NUMBER or UPDATED_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER);

        // Get all the dealerList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("accountNumber.in=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber is not null
        defaultDealerShouldBeFound("accountNumber.specified=true");

        // Get all the dealerList where accountNumber is null
        defaultDealerShouldNotBeFound("accountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber contains DEFAULT_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("accountNumber.contains=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the dealerList where accountNumber contains UPDATED_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("accountNumber.contains=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber does not contain DEFAULT_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("accountNumber.doesNotContain=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the dealerList where accountNumber does not contain UPDATED_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("accountNumber.doesNotContain=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllDealersByBankersNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName equals to DEFAULT_BANKERS_NAME
        defaultDealerShouldBeFound("bankersName.equals=" + DEFAULT_BANKERS_NAME);

        // Get all the dealerList where bankersName equals to UPDATED_BANKERS_NAME
        defaultDealerShouldNotBeFound("bankersName.equals=" + UPDATED_BANKERS_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByBankersNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName not equals to DEFAULT_BANKERS_NAME
        defaultDealerShouldNotBeFound("bankersName.notEquals=" + DEFAULT_BANKERS_NAME);

        // Get all the dealerList where bankersName not equals to UPDATED_BANKERS_NAME
        defaultDealerShouldBeFound("bankersName.notEquals=" + UPDATED_BANKERS_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByBankersNameIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName in DEFAULT_BANKERS_NAME or UPDATED_BANKERS_NAME
        defaultDealerShouldBeFound("bankersName.in=" + DEFAULT_BANKERS_NAME + "," + UPDATED_BANKERS_NAME);

        // Get all the dealerList where bankersName equals to UPDATED_BANKERS_NAME
        defaultDealerShouldNotBeFound("bankersName.in=" + UPDATED_BANKERS_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByBankersNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName is not null
        defaultDealerShouldBeFound("bankersName.specified=true");

        // Get all the dealerList where bankersName is null
        defaultDealerShouldNotBeFound("bankersName.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByBankersNameContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName contains DEFAULT_BANKERS_NAME
        defaultDealerShouldBeFound("bankersName.contains=" + DEFAULT_BANKERS_NAME);

        // Get all the dealerList where bankersName contains UPDATED_BANKERS_NAME
        defaultDealerShouldNotBeFound("bankersName.contains=" + UPDATED_BANKERS_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByBankersNameNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName does not contain DEFAULT_BANKERS_NAME
        defaultDealerShouldNotBeFound("bankersName.doesNotContain=" + DEFAULT_BANKERS_NAME);

        // Get all the dealerList where bankersName does not contain UPDATED_BANKERS_NAME
        defaultDealerShouldBeFound("bankersName.doesNotContain=" + UPDATED_BANKERS_NAME);
    }

    @Test
    @Transactional
    void getAllDealersByBankersBranchIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch equals to DEFAULT_BANKERS_BRANCH
        defaultDealerShouldBeFound("bankersBranch.equals=" + DEFAULT_BANKERS_BRANCH);

        // Get all the dealerList where bankersBranch equals to UPDATED_BANKERS_BRANCH
        defaultDealerShouldNotBeFound("bankersBranch.equals=" + UPDATED_BANKERS_BRANCH);
    }

    @Test
    @Transactional
    void getAllDealersByBankersBranchIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch not equals to DEFAULT_BANKERS_BRANCH
        defaultDealerShouldNotBeFound("bankersBranch.notEquals=" + DEFAULT_BANKERS_BRANCH);

        // Get all the dealerList where bankersBranch not equals to UPDATED_BANKERS_BRANCH
        defaultDealerShouldBeFound("bankersBranch.notEquals=" + UPDATED_BANKERS_BRANCH);
    }

    @Test
    @Transactional
    void getAllDealersByBankersBranchIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch in DEFAULT_BANKERS_BRANCH or UPDATED_BANKERS_BRANCH
        defaultDealerShouldBeFound("bankersBranch.in=" + DEFAULT_BANKERS_BRANCH + "," + UPDATED_BANKERS_BRANCH);

        // Get all the dealerList where bankersBranch equals to UPDATED_BANKERS_BRANCH
        defaultDealerShouldNotBeFound("bankersBranch.in=" + UPDATED_BANKERS_BRANCH);
    }

    @Test
    @Transactional
    void getAllDealersByBankersBranchIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch is not null
        defaultDealerShouldBeFound("bankersBranch.specified=true");

        // Get all the dealerList where bankersBranch is null
        defaultDealerShouldNotBeFound("bankersBranch.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByBankersBranchContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch contains DEFAULT_BANKERS_BRANCH
        defaultDealerShouldBeFound("bankersBranch.contains=" + DEFAULT_BANKERS_BRANCH);

        // Get all the dealerList where bankersBranch contains UPDATED_BANKERS_BRANCH
        defaultDealerShouldNotBeFound("bankersBranch.contains=" + UPDATED_BANKERS_BRANCH);
    }

    @Test
    @Transactional
    void getAllDealersByBankersBranchNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch does not contain DEFAULT_BANKERS_BRANCH
        defaultDealerShouldNotBeFound("bankersBranch.doesNotContain=" + DEFAULT_BANKERS_BRANCH);

        // Get all the dealerList where bankersBranch does not contain UPDATED_BANKERS_BRANCH
        defaultDealerShouldBeFound("bankersBranch.doesNotContain=" + UPDATED_BANKERS_BRANCH);
    }

    @Test
    @Transactional
    void getAllDealersByBankersSwiftCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode equals to DEFAULT_BANKERS_SWIFT_CODE
        defaultDealerShouldBeFound("bankersSwiftCode.equals=" + DEFAULT_BANKERS_SWIFT_CODE);

        // Get all the dealerList where bankersSwiftCode equals to UPDATED_BANKERS_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankersSwiftCode.equals=" + UPDATED_BANKERS_SWIFT_CODE);
    }

    @Test
    @Transactional
    void getAllDealersByBankersSwiftCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode not equals to DEFAULT_BANKERS_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankersSwiftCode.notEquals=" + DEFAULT_BANKERS_SWIFT_CODE);

        // Get all the dealerList where bankersSwiftCode not equals to UPDATED_BANKERS_SWIFT_CODE
        defaultDealerShouldBeFound("bankersSwiftCode.notEquals=" + UPDATED_BANKERS_SWIFT_CODE);
    }

    @Test
    @Transactional
    void getAllDealersByBankersSwiftCodeIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode in DEFAULT_BANKERS_SWIFT_CODE or UPDATED_BANKERS_SWIFT_CODE
        defaultDealerShouldBeFound("bankersSwiftCode.in=" + DEFAULT_BANKERS_SWIFT_CODE + "," + UPDATED_BANKERS_SWIFT_CODE);

        // Get all the dealerList where bankersSwiftCode equals to UPDATED_BANKERS_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankersSwiftCode.in=" + UPDATED_BANKERS_SWIFT_CODE);
    }

    @Test
    @Transactional
    void getAllDealersByBankersSwiftCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode is not null
        defaultDealerShouldBeFound("bankersSwiftCode.specified=true");

        // Get all the dealerList where bankersSwiftCode is null
        defaultDealerShouldNotBeFound("bankersSwiftCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByBankersSwiftCodeContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode contains DEFAULT_BANKERS_SWIFT_CODE
        defaultDealerShouldBeFound("bankersSwiftCode.contains=" + DEFAULT_BANKERS_SWIFT_CODE);

        // Get all the dealerList where bankersSwiftCode contains UPDATED_BANKERS_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankersSwiftCode.contains=" + UPDATED_BANKERS_SWIFT_CODE);
    }

    @Test
    @Transactional
    void getAllDealersByBankersSwiftCodeNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode does not contain DEFAULT_BANKERS_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankersSwiftCode.doesNotContain=" + DEFAULT_BANKERS_SWIFT_CODE);

        // Get all the dealerList where bankersSwiftCode does not contain UPDATED_BANKERS_SWIFT_CODE
        defaultDealerShouldBeFound("bankersSwiftCode.doesNotContain=" + UPDATED_BANKERS_SWIFT_CODE);
    }

    @Test
    @Transactional
    void getAllDealersByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultDealerShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the dealerList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultDealerShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllDealersByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultDealerShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the dealerList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultDealerShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllDealersByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultDealerShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the dealerList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultDealerShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllDealersByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where fileUploadToken is not null
        defaultDealerShouldBeFound("fileUploadToken.specified=true");

        // Get all the dealerList where fileUploadToken is null
        defaultDealerShouldNotBeFound("fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultDealerShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the dealerList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultDealerShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllDealersByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultDealerShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the dealerList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultDealerShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllDealersByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultDealerShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the dealerList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultDealerShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllDealersByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultDealerShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the dealerList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultDealerShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllDealersByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultDealerShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the dealerList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultDealerShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllDealersByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where compilationToken is not null
        defaultDealerShouldBeFound("compilationToken.specified=true");

        // Get all the dealerList where compilationToken is null
        defaultDealerShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultDealerShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the dealerList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultDealerShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllDealersByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultDealerShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the dealerList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultDealerShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllDealersByOtherNamesIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where otherNames equals to DEFAULT_OTHER_NAMES
        defaultDealerShouldBeFound("otherNames.equals=" + DEFAULT_OTHER_NAMES);

        // Get all the dealerList where otherNames equals to UPDATED_OTHER_NAMES
        defaultDealerShouldNotBeFound("otherNames.equals=" + UPDATED_OTHER_NAMES);
    }

    @Test
    @Transactional
    void getAllDealersByOtherNamesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where otherNames not equals to DEFAULT_OTHER_NAMES
        defaultDealerShouldNotBeFound("otherNames.notEquals=" + DEFAULT_OTHER_NAMES);

        // Get all the dealerList where otherNames not equals to UPDATED_OTHER_NAMES
        defaultDealerShouldBeFound("otherNames.notEquals=" + UPDATED_OTHER_NAMES);
    }

    @Test
    @Transactional
    void getAllDealersByOtherNamesIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where otherNames in DEFAULT_OTHER_NAMES or UPDATED_OTHER_NAMES
        defaultDealerShouldBeFound("otherNames.in=" + DEFAULT_OTHER_NAMES + "," + UPDATED_OTHER_NAMES);

        // Get all the dealerList where otherNames equals to UPDATED_OTHER_NAMES
        defaultDealerShouldNotBeFound("otherNames.in=" + UPDATED_OTHER_NAMES);
    }

    @Test
    @Transactional
    void getAllDealersByOtherNamesIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where otherNames is not null
        defaultDealerShouldBeFound("otherNames.specified=true");

        // Get all the dealerList where otherNames is null
        defaultDealerShouldNotBeFound("otherNames.specified=false");
    }

    @Test
    @Transactional
    void getAllDealersByOtherNamesContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where otherNames contains DEFAULT_OTHER_NAMES
        defaultDealerShouldBeFound("otherNames.contains=" + DEFAULT_OTHER_NAMES);

        // Get all the dealerList where otherNames contains UPDATED_OTHER_NAMES
        defaultDealerShouldNotBeFound("otherNames.contains=" + UPDATED_OTHER_NAMES);
    }

    @Test
    @Transactional
    void getAllDealersByOtherNamesNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where otherNames does not contain DEFAULT_OTHER_NAMES
        defaultDealerShouldNotBeFound("otherNames.doesNotContain=" + DEFAULT_OTHER_NAMES);

        // Get all the dealerList where otherNames does not contain UPDATED_OTHER_NAMES
        defaultDealerShouldBeFound("otherNames.doesNotContain=" + UPDATED_OTHER_NAMES);
    }

    @Test
    @Transactional
    void getAllDealersByPaymentLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);
        PaymentLabel paymentLabel;
        if (TestUtil.findAll(em, PaymentLabel.class).isEmpty()) {
            paymentLabel = PaymentLabelResourceIT.createEntity(em);
            em.persist(paymentLabel);
            em.flush();
        } else {
            paymentLabel = TestUtil.findAll(em, PaymentLabel.class).get(0);
        }
        em.persist(paymentLabel);
        em.flush();
        dealer.addPaymentLabel(paymentLabel);
        dealerRepository.saveAndFlush(dealer);
        Long paymentLabelId = paymentLabel.getId();

        // Get all the dealerList where paymentLabel equals to paymentLabelId
        defaultDealerShouldBeFound("paymentLabelId.equals=" + paymentLabelId);

        // Get all the dealerList where paymentLabel equals to (paymentLabelId + 1)
        defaultDealerShouldNotBeFound("paymentLabelId.equals=" + (paymentLabelId + 1));
    }

    @Test
    @Transactional
    void getAllDealersByDealerGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);
        Dealer dealerGroup;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealerGroup = DealerResourceIT.createEntity(em);
            em.persist(dealerGroup);
            em.flush();
        } else {
            dealerGroup = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(dealerGroup);
        em.flush();
        dealer.setDealerGroup(dealerGroup);
        dealerRepository.saveAndFlush(dealer);
        Long dealerGroupId = dealerGroup.getId();

        // Get all the dealerList where dealerGroup equals to dealerGroupId
        defaultDealerShouldBeFound("dealerGroupId.equals=" + dealerGroupId);

        // Get all the dealerList where dealerGroup equals to (dealerGroupId + 1)
        defaultDealerShouldNotBeFound("dealerGroupId.equals=" + (dealerGroupId + 1));
    }

    @Test
    @Transactional
    void getAllDealersByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);
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
        dealer.addPlaceholder(placeholder);
        dealerRepository.saveAndFlush(dealer);
        Long placeholderId = placeholder.getId();

        // Get all the dealerList where placeholder equals to placeholderId
        defaultDealerShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the dealerList where placeholder equals to (placeholderId + 1)
        defaultDealerShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDealerShouldBeFound(String filter) throws Exception {
        restDealerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].identificationDocumentNumber").value(hasItem(DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].physicalAddress").value(hasItem(DEFAULT_PHYSICAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankersName").value(hasItem(DEFAULT_BANKERS_NAME)))
            .andExpect(jsonPath("$.[*].bankersBranch").value(hasItem(DEFAULT_BANKERS_BRANCH)))
            .andExpect(jsonPath("$.[*].bankersSwiftCode").value(hasItem(DEFAULT_BANKERS_SWIFT_CODE)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())))
            .andExpect(jsonPath("$.[*].otherNames").value(hasItem(DEFAULT_OTHER_NAMES)));

        // Check, that the count call also returns 1
        restDealerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDealerShouldNotBeFound(String filter) throws Exception {
        restDealerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDealerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDealer() throws Exception {
        // Get the dealer
        restDealerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();

        // Update the dealer
        Dealer updatedDealer = dealerRepository.findById(dealer.getId()).get();
        // Disconnect from session so that the updates on updatedDealer are not directly saved in db
        em.detach(updatedDealer);
        updatedDealer
            .dealerName(UPDATED_DEALER_NAME)
            .taxNumber(UPDATED_TAX_NUMBER)
            .identificationDocumentNumber(UPDATED_IDENTIFICATION_DOCUMENT_NUMBER)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .department(UPDATED_DEPARTMENT)
            .position(UPDATED_POSITION)
            .postalAddress(UPDATED_POSTAL_ADDRESS)
            .physicalAddress(UPDATED_PHYSICAL_ADDRESS)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankersName(UPDATED_BANKERS_NAME)
            .bankersBranch(UPDATED_BANKERS_BRANCH)
            .bankersSwiftCode(UPDATED_BANKERS_SWIFT_CODE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN)
            .remarks(UPDATED_REMARKS)
            .otherNames(UPDATED_OTHER_NAMES);
        DealerDTO dealerDTO = dealerMapper.toDto(updatedDealer);

        restDealerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dealerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dealerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);
        Dealer testDealer = dealerList.get(dealerList.size() - 1);
        assertThat(testDealer.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testDealer.getTaxNumber()).isEqualTo(UPDATED_TAX_NUMBER);
        assertThat(testDealer.getIdentificationDocumentNumber()).isEqualTo(UPDATED_IDENTIFICATION_DOCUMENT_NUMBER);
        assertThat(testDealer.getOrganizationName()).isEqualTo(UPDATED_ORGANIZATION_NAME);
        assertThat(testDealer.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testDealer.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testDealer.getPostalAddress()).isEqualTo(UPDATED_POSTAL_ADDRESS);
        assertThat(testDealer.getPhysicalAddress()).isEqualTo(UPDATED_PHYSICAL_ADDRESS);
        assertThat(testDealer.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testDealer.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testDealer.getBankersName()).isEqualTo(UPDATED_BANKERS_NAME);
        assertThat(testDealer.getBankersBranch()).isEqualTo(UPDATED_BANKERS_BRANCH);
        assertThat(testDealer.getBankersSwiftCode()).isEqualTo(UPDATED_BANKERS_SWIFT_CODE);
        assertThat(testDealer.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testDealer.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
        assertThat(testDealer.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testDealer.getOtherNames()).isEqualTo(UPDATED_OTHER_NAMES);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository).save(testDealer);
    }

    @Test
    @Transactional
    void putNonExistingDealer() throws Exception {
        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();
        dealer.setId(count.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dealerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dealerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(0)).save(dealer);
    }

    @Test
    @Transactional
    void putWithIdMismatchDealer() throws Exception {
        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();
        dealer.setId(count.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dealerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(0)).save(dealer);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDealer() throws Exception {
        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();
        dealer.setId(count.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(0)).save(dealer);
    }

    @Test
    @Transactional
    void partialUpdateDealerWithPatch() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();

        // Update the dealer using partial update
        Dealer partialUpdatedDealer = new Dealer();
        partialUpdatedDealer.setId(dealer.getId());

        partialUpdatedDealer
            .taxNumber(UPDATED_TAX_NUMBER)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .department(UPDATED_DEPARTMENT)
            .position(UPDATED_POSITION)
            .physicalAddress(UPDATED_PHYSICAL_ADDRESS)
            .accountName(UPDATED_ACCOUNT_NAME)
            .bankersName(UPDATED_BANKERS_NAME)
            .remarks(UPDATED_REMARKS);

        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDealer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDealer))
            )
            .andExpect(status().isOk());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);
        Dealer testDealer = dealerList.get(dealerList.size() - 1);
        assertThat(testDealer.getDealerName()).isEqualTo(DEFAULT_DEALER_NAME);
        assertThat(testDealer.getTaxNumber()).isEqualTo(UPDATED_TAX_NUMBER);
        assertThat(testDealer.getIdentificationDocumentNumber()).isEqualTo(DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER);
        assertThat(testDealer.getOrganizationName()).isEqualTo(UPDATED_ORGANIZATION_NAME);
        assertThat(testDealer.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testDealer.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testDealer.getPostalAddress()).isEqualTo(DEFAULT_POSTAL_ADDRESS);
        assertThat(testDealer.getPhysicalAddress()).isEqualTo(UPDATED_PHYSICAL_ADDRESS);
        assertThat(testDealer.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testDealer.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testDealer.getBankersName()).isEqualTo(UPDATED_BANKERS_NAME);
        assertThat(testDealer.getBankersBranch()).isEqualTo(DEFAULT_BANKERS_BRANCH);
        assertThat(testDealer.getBankersSwiftCode()).isEqualTo(DEFAULT_BANKERS_SWIFT_CODE);
        assertThat(testDealer.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testDealer.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);
        assertThat(testDealer.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testDealer.getOtherNames()).isEqualTo(DEFAULT_OTHER_NAMES);
    }

    @Test
    @Transactional
    void fullUpdateDealerWithPatch() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();

        // Update the dealer using partial update
        Dealer partialUpdatedDealer = new Dealer();
        partialUpdatedDealer.setId(dealer.getId());

        partialUpdatedDealer
            .dealerName(UPDATED_DEALER_NAME)
            .taxNumber(UPDATED_TAX_NUMBER)
            .identificationDocumentNumber(UPDATED_IDENTIFICATION_DOCUMENT_NUMBER)
            .organizationName(UPDATED_ORGANIZATION_NAME)
            .department(UPDATED_DEPARTMENT)
            .position(UPDATED_POSITION)
            .postalAddress(UPDATED_POSTAL_ADDRESS)
            .physicalAddress(UPDATED_PHYSICAL_ADDRESS)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankersName(UPDATED_BANKERS_NAME)
            .bankersBranch(UPDATED_BANKERS_BRANCH)
            .bankersSwiftCode(UPDATED_BANKERS_SWIFT_CODE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN)
            .remarks(UPDATED_REMARKS)
            .otherNames(UPDATED_OTHER_NAMES);

        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDealer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDealer))
            )
            .andExpect(status().isOk());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);
        Dealer testDealer = dealerList.get(dealerList.size() - 1);
        assertThat(testDealer.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testDealer.getTaxNumber()).isEqualTo(UPDATED_TAX_NUMBER);
        assertThat(testDealer.getIdentificationDocumentNumber()).isEqualTo(UPDATED_IDENTIFICATION_DOCUMENT_NUMBER);
        assertThat(testDealer.getOrganizationName()).isEqualTo(UPDATED_ORGANIZATION_NAME);
        assertThat(testDealer.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
        assertThat(testDealer.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testDealer.getPostalAddress()).isEqualTo(UPDATED_POSTAL_ADDRESS);
        assertThat(testDealer.getPhysicalAddress()).isEqualTo(UPDATED_PHYSICAL_ADDRESS);
        assertThat(testDealer.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testDealer.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testDealer.getBankersName()).isEqualTo(UPDATED_BANKERS_NAME);
        assertThat(testDealer.getBankersBranch()).isEqualTo(UPDATED_BANKERS_BRANCH);
        assertThat(testDealer.getBankersSwiftCode()).isEqualTo(UPDATED_BANKERS_SWIFT_CODE);
        assertThat(testDealer.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testDealer.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
        assertThat(testDealer.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testDealer.getOtherNames()).isEqualTo(UPDATED_OTHER_NAMES);
    }

    @Test
    @Transactional
    void patchNonExistingDealer() throws Exception {
        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();
        dealer.setId(count.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dealerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dealerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(0)).save(dealer);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDealer() throws Exception {
        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();
        dealer.setId(count.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dealerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(0)).save(dealer);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDealer() throws Exception {
        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();
        dealer.setId(count.incrementAndGet());

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dealerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(0)).save(dealer);
    }

    @Test
    @Transactional
    void deleteDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        int databaseSizeBeforeDelete = dealerRepository.findAll().size();

        // Delete the dealer
        restDealerMockMvc
            .perform(delete(ENTITY_API_URL_ID, dealer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(1)).deleteById(dealer.getId());
    }

    @Test
    @Transactional
    void searchDealer() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);
        when(mockDealerSearchRepository.search("id:" + dealer.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dealer), PageRequest.of(0, 1), 1));

        // Search the dealer
        restDealerMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + dealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].identificationDocumentNumber").value(hasItem(DEFAULT_IDENTIFICATION_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].organizationName").value(hasItem(DEFAULT_ORGANIZATION_NAME)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].physicalAddress").value(hasItem(DEFAULT_PHYSICAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankersName").value(hasItem(DEFAULT_BANKERS_NAME)))
            .andExpect(jsonPath("$.[*].bankersBranch").value(hasItem(DEFAULT_BANKERS_BRANCH)))
            .andExpect(jsonPath("$.[*].bankersSwiftCode").value(hasItem(DEFAULT_BANKERS_SWIFT_CODE)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())))
            .andExpect(jsonPath("$.[*].otherNames").value(hasItem(DEFAULT_OTHER_NAMES)));
    }
}
