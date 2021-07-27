package io.github.erp.web.rest;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.github.erp.ErpServiceApp;
import io.github.erp.config.SecurityBeanOverrideConfiguration;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.Payment;
import io.github.erp.repository.DealerRepository;
import io.github.erp.repository.search.DealerSearchRepository;
import io.github.erp.service.DealerService;
import io.github.erp.service.dto.DealerDTO;
import io.github.erp.service.mapper.DealerMapper;
import io.github.erp.service.dto.DealerCriteria;
import io.github.erp.service.DealerQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DealerResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, ErpServiceApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DealerResourceIT {

    private static final String DEFAULT_DEALER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEALER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TAX_NUMBER = "BBBBBBBBBB";

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

    @Autowired
    private DealerRepository dealerRepository;

    @Mock
    private DealerRepository dealerRepositoryMock;

    @Autowired
    private DealerMapper dealerMapper;

    @Mock
    private DealerService dealerServiceMock;

    @Autowired
    private DealerService dealerService;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DealerSearchRepositoryMockConfiguration
     */
    @Autowired
    private DealerSearchRepository mockDealerSearchRepository;

    @Autowired
    private DealerQueryService dealerQueryService;

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
            .postalAddress(DEFAULT_POSTAL_ADDRESS)
            .physicalAddress(DEFAULT_PHYSICAL_ADDRESS)
            .accountName(DEFAULT_ACCOUNT_NAME)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .bankersName(DEFAULT_BANKERS_NAME)
            .bankersBranch(DEFAULT_BANKERS_BRANCH)
            .bankersSwiftCode(DEFAULT_BANKERS_SWIFT_CODE);
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
            .postalAddress(UPDATED_POSTAL_ADDRESS)
            .physicalAddress(UPDATED_PHYSICAL_ADDRESS)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankersName(UPDATED_BANKERS_NAME)
            .bankersBranch(UPDATED_BANKERS_BRANCH)
            .bankersSwiftCode(UPDATED_BANKERS_SWIFT_CODE);
        return dealer;
    }

    @BeforeEach
    public void initTest() {
        dealer = createEntity(em);
    }

    @Test
    @Transactional
    public void createDealer() throws Exception {
        int databaseSizeBeforeCreate = dealerRepository.findAll().size();
        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);
        restDealerMockMvc.perform(post("/api/dealers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isCreated());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeCreate + 1);
        Dealer testDealer = dealerList.get(dealerList.size() - 1);
        assertThat(testDealer.getDealerName()).isEqualTo(DEFAULT_DEALER_NAME);
        assertThat(testDealer.getTaxNumber()).isEqualTo(DEFAULT_TAX_NUMBER);
        assertThat(testDealer.getPostalAddress()).isEqualTo(DEFAULT_POSTAL_ADDRESS);
        assertThat(testDealer.getPhysicalAddress()).isEqualTo(DEFAULT_PHYSICAL_ADDRESS);
        assertThat(testDealer.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testDealer.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testDealer.getBankersName()).isEqualTo(DEFAULT_BANKERS_NAME);
        assertThat(testDealer.getBankersBranch()).isEqualTo(DEFAULT_BANKERS_BRANCH);
        assertThat(testDealer.getBankersSwiftCode()).isEqualTo(DEFAULT_BANKERS_SWIFT_CODE);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(1)).save(testDealer);
    }

    @Test
    @Transactional
    public void createDealerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dealerRepository.findAll().size();

        // Create the Dealer with an existing ID
        dealer.setId(1L);
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDealerMockMvc.perform(post("/api/dealers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(0)).save(dealer);
    }


    @Test
    @Transactional
    public void getAllDealers() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList
        restDealerMockMvc.perform(get("/api/dealers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].physicalAddress").value(hasItem(DEFAULT_PHYSICAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankersName").value(hasItem(DEFAULT_BANKERS_NAME)))
            .andExpect(jsonPath("$.[*].bankersBranch").value(hasItem(DEFAULT_BANKERS_BRANCH)))
            .andExpect(jsonPath("$.[*].bankersSwiftCode").value(hasItem(DEFAULT_BANKERS_SWIFT_CODE)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllDealersWithEagerRelationshipsIsEnabled() throws Exception {
        when(dealerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDealerMockMvc.perform(get("/api/dealers?eagerload=true"))
            .andExpect(status().isOk());

        verify(dealerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDealersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dealerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDealerMockMvc.perform(get("/api/dealers?eagerload=true"))
            .andExpect(status().isOk());

        verify(dealerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get the dealer
        restDealerMockMvc.perform(get("/api/dealers/{id}", dealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dealer.getId().intValue()))
            .andExpect(jsonPath("$.dealerName").value(DEFAULT_DEALER_NAME))
            .andExpect(jsonPath("$.taxNumber").value(DEFAULT_TAX_NUMBER))
            .andExpect(jsonPath("$.postalAddress").value(DEFAULT_POSTAL_ADDRESS))
            .andExpect(jsonPath("$.physicalAddress").value(DEFAULT_PHYSICAL_ADDRESS))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.bankersName").value(DEFAULT_BANKERS_NAME))
            .andExpect(jsonPath("$.bankersBranch").value(DEFAULT_BANKERS_BRANCH))
            .andExpect(jsonPath("$.bankersSwiftCode").value(DEFAULT_BANKERS_SWIFT_CODE));
    }


    @Test
    @Transactional
    public void getDealersByIdFiltering() throws Exception {
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
    public void getAllDealersByDealerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName equals to DEFAULT_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.equals=" + DEFAULT_DEALER_NAME);

        // Get all the dealerList where dealerName equals to UPDATED_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.equals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName not equals to DEFAULT_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.notEquals=" + DEFAULT_DEALER_NAME);

        // Get all the dealerList where dealerName not equals to UPDATED_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.notEquals=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerNameIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName in DEFAULT_DEALER_NAME or UPDATED_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.in=" + DEFAULT_DEALER_NAME + "," + UPDATED_DEALER_NAME);

        // Get all the dealerList where dealerName equals to UPDATED_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.in=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName is not null
        defaultDealerShouldBeFound("dealerName.specified=true");

        // Get all the dealerList where dealerName is null
        defaultDealerShouldNotBeFound("dealerName.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByDealerNameContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName contains DEFAULT_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.contains=" + DEFAULT_DEALER_NAME);

        // Get all the dealerList where dealerName contains UPDATED_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.contains=" + UPDATED_DEALER_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByDealerNameNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where dealerName does not contain DEFAULT_DEALER_NAME
        defaultDealerShouldNotBeFound("dealerName.doesNotContain=" + DEFAULT_DEALER_NAME);

        // Get all the dealerList where dealerName does not contain UPDATED_DEALER_NAME
        defaultDealerShouldBeFound("dealerName.doesNotContain=" + UPDATED_DEALER_NAME);
    }


    @Test
    @Transactional
    public void getAllDealersByTaxNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber equals to DEFAULT_TAX_NUMBER
        defaultDealerShouldBeFound("taxNumber.equals=" + DEFAULT_TAX_NUMBER);

        // Get all the dealerList where taxNumber equals to UPDATED_TAX_NUMBER
        defaultDealerShouldNotBeFound("taxNumber.equals=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByTaxNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber not equals to DEFAULT_TAX_NUMBER
        defaultDealerShouldNotBeFound("taxNumber.notEquals=" + DEFAULT_TAX_NUMBER);

        // Get all the dealerList where taxNumber not equals to UPDATED_TAX_NUMBER
        defaultDealerShouldBeFound("taxNumber.notEquals=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByTaxNumberIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber in DEFAULT_TAX_NUMBER or UPDATED_TAX_NUMBER
        defaultDealerShouldBeFound("taxNumber.in=" + DEFAULT_TAX_NUMBER + "," + UPDATED_TAX_NUMBER);

        // Get all the dealerList where taxNumber equals to UPDATED_TAX_NUMBER
        defaultDealerShouldNotBeFound("taxNumber.in=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByTaxNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber is not null
        defaultDealerShouldBeFound("taxNumber.specified=true");

        // Get all the dealerList where taxNumber is null
        defaultDealerShouldNotBeFound("taxNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByTaxNumberContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber contains DEFAULT_TAX_NUMBER
        defaultDealerShouldBeFound("taxNumber.contains=" + DEFAULT_TAX_NUMBER);

        // Get all the dealerList where taxNumber contains UPDATED_TAX_NUMBER
        defaultDealerShouldNotBeFound("taxNumber.contains=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByTaxNumberNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where taxNumber does not contain DEFAULT_TAX_NUMBER
        defaultDealerShouldNotBeFound("taxNumber.doesNotContain=" + DEFAULT_TAX_NUMBER);

        // Get all the dealerList where taxNumber does not contain UPDATED_TAX_NUMBER
        defaultDealerShouldBeFound("taxNumber.doesNotContain=" + UPDATED_TAX_NUMBER);
    }


    @Test
    @Transactional
    public void getAllDealersByPostalAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress equals to DEFAULT_POSTAL_ADDRESS
        defaultDealerShouldBeFound("postalAddress.equals=" + DEFAULT_POSTAL_ADDRESS);

        // Get all the dealerList where postalAddress equals to UPDATED_POSTAL_ADDRESS
        defaultDealerShouldNotBeFound("postalAddress.equals=" + UPDATED_POSTAL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByPostalAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress not equals to DEFAULT_POSTAL_ADDRESS
        defaultDealerShouldNotBeFound("postalAddress.notEquals=" + DEFAULT_POSTAL_ADDRESS);

        // Get all the dealerList where postalAddress not equals to UPDATED_POSTAL_ADDRESS
        defaultDealerShouldBeFound("postalAddress.notEquals=" + UPDATED_POSTAL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByPostalAddressIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress in DEFAULT_POSTAL_ADDRESS or UPDATED_POSTAL_ADDRESS
        defaultDealerShouldBeFound("postalAddress.in=" + DEFAULT_POSTAL_ADDRESS + "," + UPDATED_POSTAL_ADDRESS);

        // Get all the dealerList where postalAddress equals to UPDATED_POSTAL_ADDRESS
        defaultDealerShouldNotBeFound("postalAddress.in=" + UPDATED_POSTAL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByPostalAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress is not null
        defaultDealerShouldBeFound("postalAddress.specified=true");

        // Get all the dealerList where postalAddress is null
        defaultDealerShouldNotBeFound("postalAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByPostalAddressContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress contains DEFAULT_POSTAL_ADDRESS
        defaultDealerShouldBeFound("postalAddress.contains=" + DEFAULT_POSTAL_ADDRESS);

        // Get all the dealerList where postalAddress contains UPDATED_POSTAL_ADDRESS
        defaultDealerShouldNotBeFound("postalAddress.contains=" + UPDATED_POSTAL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByPostalAddressNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where postalAddress does not contain DEFAULT_POSTAL_ADDRESS
        defaultDealerShouldNotBeFound("postalAddress.doesNotContain=" + DEFAULT_POSTAL_ADDRESS);

        // Get all the dealerList where postalAddress does not contain UPDATED_POSTAL_ADDRESS
        defaultDealerShouldBeFound("postalAddress.doesNotContain=" + UPDATED_POSTAL_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllDealersByPhysicalAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress equals to DEFAULT_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("physicalAddress.equals=" + DEFAULT_PHYSICAL_ADDRESS);

        // Get all the dealerList where physicalAddress equals to UPDATED_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("physicalAddress.equals=" + UPDATED_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByPhysicalAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress not equals to DEFAULT_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("physicalAddress.notEquals=" + DEFAULT_PHYSICAL_ADDRESS);

        // Get all the dealerList where physicalAddress not equals to UPDATED_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("physicalAddress.notEquals=" + UPDATED_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByPhysicalAddressIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress in DEFAULT_PHYSICAL_ADDRESS or UPDATED_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("physicalAddress.in=" + DEFAULT_PHYSICAL_ADDRESS + "," + UPDATED_PHYSICAL_ADDRESS);

        // Get all the dealerList where physicalAddress equals to UPDATED_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("physicalAddress.in=" + UPDATED_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByPhysicalAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress is not null
        defaultDealerShouldBeFound("physicalAddress.specified=true");

        // Get all the dealerList where physicalAddress is null
        defaultDealerShouldNotBeFound("physicalAddress.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByPhysicalAddressContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress contains DEFAULT_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("physicalAddress.contains=" + DEFAULT_PHYSICAL_ADDRESS);

        // Get all the dealerList where physicalAddress contains UPDATED_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("physicalAddress.contains=" + UPDATED_PHYSICAL_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllDealersByPhysicalAddressNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where physicalAddress does not contain DEFAULT_PHYSICAL_ADDRESS
        defaultDealerShouldNotBeFound("physicalAddress.doesNotContain=" + DEFAULT_PHYSICAL_ADDRESS);

        // Get all the dealerList where physicalAddress does not contain UPDATED_PHYSICAL_ADDRESS
        defaultDealerShouldBeFound("physicalAddress.doesNotContain=" + UPDATED_PHYSICAL_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllDealersByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName equals to DEFAULT_ACCOUNT_NAME
        defaultDealerShouldBeFound("accountName.equals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the dealerList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultDealerShouldNotBeFound("accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByAccountNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName not equals to DEFAULT_ACCOUNT_NAME
        defaultDealerShouldNotBeFound("accountName.notEquals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the dealerList where accountName not equals to UPDATED_ACCOUNT_NAME
        defaultDealerShouldBeFound("accountName.notEquals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName in DEFAULT_ACCOUNT_NAME or UPDATED_ACCOUNT_NAME
        defaultDealerShouldBeFound("accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME);

        // Get all the dealerList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultDealerShouldNotBeFound("accountName.in=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName is not null
        defaultDealerShouldBeFound("accountName.specified=true");

        // Get all the dealerList where accountName is null
        defaultDealerShouldNotBeFound("accountName.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByAccountNameContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName contains DEFAULT_ACCOUNT_NAME
        defaultDealerShouldBeFound("accountName.contains=" + DEFAULT_ACCOUNT_NAME);

        // Get all the dealerList where accountName contains UPDATED_ACCOUNT_NAME
        defaultDealerShouldNotBeFound("accountName.contains=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountName does not contain DEFAULT_ACCOUNT_NAME
        defaultDealerShouldNotBeFound("accountName.doesNotContain=" + DEFAULT_ACCOUNT_NAME);

        // Get all the dealerList where accountName does not contain UPDATED_ACCOUNT_NAME
        defaultDealerShouldBeFound("accountName.doesNotContain=" + UPDATED_ACCOUNT_NAME);
    }


    @Test
    @Transactional
    public void getAllDealersByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber equals to DEFAULT_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the dealerList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber not equals to DEFAULT_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("accountNumber.notEquals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the dealerList where accountNumber not equals to UPDATED_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("accountNumber.notEquals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber in DEFAULT_ACCOUNT_NUMBER or UPDATED_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER);

        // Get all the dealerList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("accountNumber.in=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber is not null
        defaultDealerShouldBeFound("accountNumber.specified=true");

        // Get all the dealerList where accountNumber is null
        defaultDealerShouldNotBeFound("accountNumber.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber contains DEFAULT_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("accountNumber.contains=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the dealerList where accountNumber contains UPDATED_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("accountNumber.contains=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDealersByAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where accountNumber does not contain DEFAULT_ACCOUNT_NUMBER
        defaultDealerShouldNotBeFound("accountNumber.doesNotContain=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the dealerList where accountNumber does not contain UPDATED_ACCOUNT_NUMBER
        defaultDealerShouldBeFound("accountNumber.doesNotContain=" + UPDATED_ACCOUNT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllDealersByBankersNameIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName equals to DEFAULT_BANKERS_NAME
        defaultDealerShouldBeFound("bankersName.equals=" + DEFAULT_BANKERS_NAME);

        // Get all the dealerList where bankersName equals to UPDATED_BANKERS_NAME
        defaultDealerShouldNotBeFound("bankersName.equals=" + UPDATED_BANKERS_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByBankersNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName not equals to DEFAULT_BANKERS_NAME
        defaultDealerShouldNotBeFound("bankersName.notEquals=" + DEFAULT_BANKERS_NAME);

        // Get all the dealerList where bankersName not equals to UPDATED_BANKERS_NAME
        defaultDealerShouldBeFound("bankersName.notEquals=" + UPDATED_BANKERS_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByBankersNameIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName in DEFAULT_BANKERS_NAME or UPDATED_BANKERS_NAME
        defaultDealerShouldBeFound("bankersName.in=" + DEFAULT_BANKERS_NAME + "," + UPDATED_BANKERS_NAME);

        // Get all the dealerList where bankersName equals to UPDATED_BANKERS_NAME
        defaultDealerShouldNotBeFound("bankersName.in=" + UPDATED_BANKERS_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByBankersNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName is not null
        defaultDealerShouldBeFound("bankersName.specified=true");

        // Get all the dealerList where bankersName is null
        defaultDealerShouldNotBeFound("bankersName.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByBankersNameContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName contains DEFAULT_BANKERS_NAME
        defaultDealerShouldBeFound("bankersName.contains=" + DEFAULT_BANKERS_NAME);

        // Get all the dealerList where bankersName contains UPDATED_BANKERS_NAME
        defaultDealerShouldNotBeFound("bankersName.contains=" + UPDATED_BANKERS_NAME);
    }

    @Test
    @Transactional
    public void getAllDealersByBankersNameNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersName does not contain DEFAULT_BANKERS_NAME
        defaultDealerShouldNotBeFound("bankersName.doesNotContain=" + DEFAULT_BANKERS_NAME);

        // Get all the dealerList where bankersName does not contain UPDATED_BANKERS_NAME
        defaultDealerShouldBeFound("bankersName.doesNotContain=" + UPDATED_BANKERS_NAME);
    }


    @Test
    @Transactional
    public void getAllDealersByBankersBranchIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch equals to DEFAULT_BANKERS_BRANCH
        defaultDealerShouldBeFound("bankersBranch.equals=" + DEFAULT_BANKERS_BRANCH);

        // Get all the dealerList where bankersBranch equals to UPDATED_BANKERS_BRANCH
        defaultDealerShouldNotBeFound("bankersBranch.equals=" + UPDATED_BANKERS_BRANCH);
    }

    @Test
    @Transactional
    public void getAllDealersByBankersBranchIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch not equals to DEFAULT_BANKERS_BRANCH
        defaultDealerShouldNotBeFound("bankersBranch.notEquals=" + DEFAULT_BANKERS_BRANCH);

        // Get all the dealerList where bankersBranch not equals to UPDATED_BANKERS_BRANCH
        defaultDealerShouldBeFound("bankersBranch.notEquals=" + UPDATED_BANKERS_BRANCH);
    }

    @Test
    @Transactional
    public void getAllDealersByBankersBranchIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch in DEFAULT_BANKERS_BRANCH or UPDATED_BANKERS_BRANCH
        defaultDealerShouldBeFound("bankersBranch.in=" + DEFAULT_BANKERS_BRANCH + "," + UPDATED_BANKERS_BRANCH);

        // Get all the dealerList where bankersBranch equals to UPDATED_BANKERS_BRANCH
        defaultDealerShouldNotBeFound("bankersBranch.in=" + UPDATED_BANKERS_BRANCH);
    }

    @Test
    @Transactional
    public void getAllDealersByBankersBranchIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch is not null
        defaultDealerShouldBeFound("bankersBranch.specified=true");

        // Get all the dealerList where bankersBranch is null
        defaultDealerShouldNotBeFound("bankersBranch.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByBankersBranchContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch contains DEFAULT_BANKERS_BRANCH
        defaultDealerShouldBeFound("bankersBranch.contains=" + DEFAULT_BANKERS_BRANCH);

        // Get all the dealerList where bankersBranch contains UPDATED_BANKERS_BRANCH
        defaultDealerShouldNotBeFound("bankersBranch.contains=" + UPDATED_BANKERS_BRANCH);
    }

    @Test
    @Transactional
    public void getAllDealersByBankersBranchNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersBranch does not contain DEFAULT_BANKERS_BRANCH
        defaultDealerShouldNotBeFound("bankersBranch.doesNotContain=" + DEFAULT_BANKERS_BRANCH);

        // Get all the dealerList where bankersBranch does not contain UPDATED_BANKERS_BRANCH
        defaultDealerShouldBeFound("bankersBranch.doesNotContain=" + UPDATED_BANKERS_BRANCH);
    }


    @Test
    @Transactional
    public void getAllDealersByBankersSwiftCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode equals to DEFAULT_BANKERS_SWIFT_CODE
        defaultDealerShouldBeFound("bankersSwiftCode.equals=" + DEFAULT_BANKERS_SWIFT_CODE);

        // Get all the dealerList where bankersSwiftCode equals to UPDATED_BANKERS_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankersSwiftCode.equals=" + UPDATED_BANKERS_SWIFT_CODE);
    }

    @Test
    @Transactional
    public void getAllDealersByBankersSwiftCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode not equals to DEFAULT_BANKERS_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankersSwiftCode.notEquals=" + DEFAULT_BANKERS_SWIFT_CODE);

        // Get all the dealerList where bankersSwiftCode not equals to UPDATED_BANKERS_SWIFT_CODE
        defaultDealerShouldBeFound("bankersSwiftCode.notEquals=" + UPDATED_BANKERS_SWIFT_CODE);
    }

    @Test
    @Transactional
    public void getAllDealersByBankersSwiftCodeIsInShouldWork() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode in DEFAULT_BANKERS_SWIFT_CODE or UPDATED_BANKERS_SWIFT_CODE
        defaultDealerShouldBeFound("bankersSwiftCode.in=" + DEFAULT_BANKERS_SWIFT_CODE + "," + UPDATED_BANKERS_SWIFT_CODE);

        // Get all the dealerList where bankersSwiftCode equals to UPDATED_BANKERS_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankersSwiftCode.in=" + UPDATED_BANKERS_SWIFT_CODE);
    }

    @Test
    @Transactional
    public void getAllDealersByBankersSwiftCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode is not null
        defaultDealerShouldBeFound("bankersSwiftCode.specified=true");

        // Get all the dealerList where bankersSwiftCode is null
        defaultDealerShouldNotBeFound("bankersSwiftCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllDealersByBankersSwiftCodeContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode contains DEFAULT_BANKERS_SWIFT_CODE
        defaultDealerShouldBeFound("bankersSwiftCode.contains=" + DEFAULT_BANKERS_SWIFT_CODE);

        // Get all the dealerList where bankersSwiftCode contains UPDATED_BANKERS_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankersSwiftCode.contains=" + UPDATED_BANKERS_SWIFT_CODE);
    }

    @Test
    @Transactional
    public void getAllDealersByBankersSwiftCodeNotContainsSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList where bankersSwiftCode does not contain DEFAULT_BANKERS_SWIFT_CODE
        defaultDealerShouldNotBeFound("bankersSwiftCode.doesNotContain=" + DEFAULT_BANKERS_SWIFT_CODE);

        // Get all the dealerList where bankersSwiftCode does not contain UPDATED_BANKERS_SWIFT_CODE
        defaultDealerShouldBeFound("bankersSwiftCode.doesNotContain=" + UPDATED_BANKERS_SWIFT_CODE);
    }


    @Test
    @Transactional
    public void getAllDealersByPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);
        Payment payment = PaymentResourceIT.createEntity(em);
        em.persist(payment);
        em.flush();
        dealer.addPayment(payment);
        dealerRepository.saveAndFlush(dealer);
        Long paymentId = payment.getId();

        // Get all the dealerList where payment equals to paymentId
        defaultDealerShouldBeFound("paymentId.equals=" + paymentId);

        // Get all the dealerList where payment equals to paymentId + 1
        defaultDealerShouldNotBeFound("paymentId.equals=" + (paymentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDealerShouldBeFound(String filter) throws Exception {
        restDealerMockMvc.perform(get("/api/dealers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].physicalAddress").value(hasItem(DEFAULT_PHYSICAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankersName").value(hasItem(DEFAULT_BANKERS_NAME)))
            .andExpect(jsonPath("$.[*].bankersBranch").value(hasItem(DEFAULT_BANKERS_BRANCH)))
            .andExpect(jsonPath("$.[*].bankersSwiftCode").value(hasItem(DEFAULT_BANKERS_SWIFT_CODE)));

        // Check, that the count call also returns 1
        restDealerMockMvc.perform(get("/api/dealers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDealerShouldNotBeFound(String filter) throws Exception {
        restDealerMockMvc.perform(get("/api/dealers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDealerMockMvc.perform(get("/api/dealers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingDealer() throws Exception {
        // Get the dealer
        restDealerMockMvc.perform(get("/api/dealers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDealer() throws Exception {
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
            .postalAddress(UPDATED_POSTAL_ADDRESS)
            .physicalAddress(UPDATED_PHYSICAL_ADDRESS)
            .accountName(UPDATED_ACCOUNT_NAME)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankersName(UPDATED_BANKERS_NAME)
            .bankersBranch(UPDATED_BANKERS_BRANCH)
            .bankersSwiftCode(UPDATED_BANKERS_SWIFT_CODE);
        DealerDTO dealerDTO = dealerMapper.toDto(updatedDealer);

        restDealerMockMvc.perform(put("/api/dealers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isOk());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);
        Dealer testDealer = dealerList.get(dealerList.size() - 1);
        assertThat(testDealer.getDealerName()).isEqualTo(UPDATED_DEALER_NAME);
        assertThat(testDealer.getTaxNumber()).isEqualTo(UPDATED_TAX_NUMBER);
        assertThat(testDealer.getPostalAddress()).isEqualTo(UPDATED_POSTAL_ADDRESS);
        assertThat(testDealer.getPhysicalAddress()).isEqualTo(UPDATED_PHYSICAL_ADDRESS);
        assertThat(testDealer.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testDealer.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testDealer.getBankersName()).isEqualTo(UPDATED_BANKERS_NAME);
        assertThat(testDealer.getBankersBranch()).isEqualTo(UPDATED_BANKERS_BRANCH);
        assertThat(testDealer.getBankersSwiftCode()).isEqualTo(UPDATED_BANKERS_SWIFT_CODE);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(1)).save(testDealer);
    }

    @Test
    @Transactional
    public void updateNonExistingDealer() throws Exception {
        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();

        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealerMockMvc.perform(put("/api/dealers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(0)).save(dealer);
    }

    @Test
    @Transactional
    public void deleteDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        int databaseSizeBeforeDelete = dealerRepository.findAll().size();

        // Delete the dealer
        restDealerMockMvc.perform(delete("/api/dealers/{id}", dealer.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Dealer in Elasticsearch
        verify(mockDealerSearchRepository, times(1)).deleteById(dealer.getId());
    }

    @Test
    @Transactional
    public void searchDealer() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);
        when(mockDealerSearchRepository.search(queryStringQuery("id:" + dealer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dealer), PageRequest.of(0, 1), 1));

        // Search the dealer
        restDealerMockMvc.perform(get("/api/_search/dealers?query=id:" + dealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].dealerName").value(hasItem(DEFAULT_DEALER_NAME)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].postalAddress").value(hasItem(DEFAULT_POSTAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].physicalAddress").value(hasItem(DEFAULT_PHYSICAL_ADDRESS)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankersName").value(hasItem(DEFAULT_BANKERS_NAME)))
            .andExpect(jsonPath("$.[*].bankersBranch").value(hasItem(DEFAULT_BANKERS_BRANCH)))
            .andExpect(jsonPath("$.[*].bankersSwiftCode").value(hasItem(DEFAULT_BANKERS_SWIFT_CODE)));
    }
}
