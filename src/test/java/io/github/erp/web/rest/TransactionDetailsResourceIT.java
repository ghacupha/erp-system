package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.TransactionAccount;
import io.github.erp.domain.TransactionDetails;
import io.github.erp.repository.TransactionDetailsRepository;
import io.github.erp.repository.search.TransactionDetailsSearchRepository;
import io.github.erp.service.TransactionDetailsService;
import io.github.erp.service.criteria.TransactionDetailsCriteria;
import io.github.erp.service.dto.TransactionDetailsDTO;
import io.github.erp.service.mapper.TransactionDetailsMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link TransactionDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransactionDetailsResourceIT {

    private static final Long DEFAULT_ENTRY_ID = 1L;
    private static final Long UPDATED_ENTRY_ID = 2L;
    private static final Long SMALLER_ENTRY_ID = 1L - 1L;

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final UUID DEFAULT_POSTING_ID = UUID.randomUUID();
    private static final UUID UPDATED_POSTING_ID = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_MODIFIED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MODIFIED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_TRANSACTION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transaction-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/transaction-details";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Mock
    private TransactionDetailsRepository transactionDetailsRepositoryMock;

    @Autowired
    private TransactionDetailsMapper transactionDetailsMapper;

    @Mock
    private TransactionDetailsService transactionDetailsServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TransactionDetailsSearchRepositoryMockConfiguration
     */
    @Autowired
    private TransactionDetailsSearchRepository mockTransactionDetailsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionDetailsMockMvc;

    private TransactionDetails transactionDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionDetails createEntity(EntityManager em) {
        TransactionDetails transactionDetails = new TransactionDetails()
            .entryId(DEFAULT_ENTRY_ID)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .description(DEFAULT_DESCRIPTION)
            .amount(DEFAULT_AMOUNT)
            .isDeleted(DEFAULT_IS_DELETED)
            .postingId(DEFAULT_POSTING_ID)
            .createdAt(DEFAULT_CREATED_AT)
            .modifiedAt(DEFAULT_MODIFIED_AT)
            .transactionType(DEFAULT_TRANSACTION_TYPE);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        transactionDetails.setDebitAccount(transactionAccount);
        // Add required entity
        transactionDetails.setCreditAccount(transactionAccount);
        return transactionDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionDetails createUpdatedEntity(EntityManager em) {
        TransactionDetails transactionDetails = new TransactionDetails()
            .entryId(UPDATED_ENTRY_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .isDeleted(UPDATED_IS_DELETED)
            .postingId(UPDATED_POSTING_ID)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .transactionType(UPDATED_TRANSACTION_TYPE);
        // Add required entity
        TransactionAccount transactionAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transactionAccount = TransactionAccountResourceIT.createUpdatedEntity(em);
            em.persist(transactionAccount);
            em.flush();
        } else {
            transactionAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        transactionDetails.setDebitAccount(transactionAccount);
        // Add required entity
        transactionDetails.setCreditAccount(transactionAccount);
        return transactionDetails;
    }

    @BeforeEach
    public void initTest() {
        transactionDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionDetails() throws Exception {
        int databaseSizeBeforeCreate = transactionDetailsRepository.findAll().size();
        // Create the TransactionDetails
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);
        restTransactionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionDetails testTransactionDetails = transactionDetailsList.get(transactionDetailsList.size() - 1);
        assertThat(testTransactionDetails.getEntryId()).isEqualTo(DEFAULT_ENTRY_ID);
        assertThat(testTransactionDetails.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testTransactionDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransactionDetails.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testTransactionDetails.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testTransactionDetails.getPostingId()).isEqualTo(DEFAULT_POSTING_ID);
        assertThat(testTransactionDetails.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTransactionDetails.getModifiedAt()).isEqualTo(DEFAULT_MODIFIED_AT);
        assertThat(testTransactionDetails.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);

        // Validate the TransactionDetails in Elasticsearch
        verify(mockTransactionDetailsSearchRepository, times(1)).save(testTransactionDetails);
    }

    @Test
    @Transactional
    void createTransactionDetailsWithExistingId() throws Exception {
        // Create the TransactionDetails with an existing ID
        transactionDetails.setId(1L);
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        int databaseSizeBeforeCreate = transactionDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeCreate);

        // Validate the TransactionDetails in Elasticsearch
        verify(mockTransactionDetailsSearchRepository, times(0)).save(transactionDetails);
    }

    @Test
    @Transactional
    void checkEntryIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionDetailsRepository.findAll().size();
        // set the field null
        transactionDetails.setEntryId(null);

        // Create the TransactionDetails, which fails.
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        restTransactionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionDetailsRepository.findAll().size();
        // set the field null
        transactionDetails.setTransactionDate(null);

        // Create the TransactionDetails, which fails.
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        restTransactionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionDetailsRepository.findAll().size();
        // set the field null
        transactionDetails.setAmount(null);

        // Create the TransactionDetails, which fails.
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        restTransactionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionDetailsRepository.findAll().size();
        // set the field null
        transactionDetails.setCreatedAt(null);

        // Create the TransactionDetails, which fails.
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        restTransactionDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionDetails() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList
        restTransactionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].entryId").value(hasItem(DEFAULT_ENTRY_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].postingId").value(hasItem(DEFAULT_POSTING_ID.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(sameInstant(DEFAULT_MODIFIED_AT))))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(transactionDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionDetailsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(transactionDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionDetailsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTransactionDetails() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get the transactionDetails
        restTransactionDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionDetails.getId().intValue()))
            .andExpect(jsonPath("$.entryId").value(DEFAULT_ENTRY_ID.intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.postingId").value(DEFAULT_POSTING_ID.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.modifiedAt").value(sameInstant(DEFAULT_MODIFIED_AT)))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE));
    }

    @Test
    @Transactional
    void getTransactionDetailsByIdFiltering() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        Long id = transactionDetails.getId();

        defaultTransactionDetailsShouldBeFound("id.equals=" + id);
        defaultTransactionDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByEntryIdIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where entryId equals to DEFAULT_ENTRY_ID
        defaultTransactionDetailsShouldBeFound("entryId.equals=" + DEFAULT_ENTRY_ID);

        // Get all the transactionDetailsList where entryId equals to UPDATED_ENTRY_ID
        defaultTransactionDetailsShouldNotBeFound("entryId.equals=" + UPDATED_ENTRY_ID);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByEntryIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where entryId not equals to DEFAULT_ENTRY_ID
        defaultTransactionDetailsShouldNotBeFound("entryId.notEquals=" + DEFAULT_ENTRY_ID);

        // Get all the transactionDetailsList where entryId not equals to UPDATED_ENTRY_ID
        defaultTransactionDetailsShouldBeFound("entryId.notEquals=" + UPDATED_ENTRY_ID);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByEntryIdIsInShouldWork() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where entryId in DEFAULT_ENTRY_ID or UPDATED_ENTRY_ID
        defaultTransactionDetailsShouldBeFound("entryId.in=" + DEFAULT_ENTRY_ID + "," + UPDATED_ENTRY_ID);

        // Get all the transactionDetailsList where entryId equals to UPDATED_ENTRY_ID
        defaultTransactionDetailsShouldNotBeFound("entryId.in=" + UPDATED_ENTRY_ID);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByEntryIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where entryId is not null
        defaultTransactionDetailsShouldBeFound("entryId.specified=true");

        // Get all the transactionDetailsList where entryId is null
        defaultTransactionDetailsShouldNotBeFound("entryId.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByEntryIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where entryId is greater than or equal to DEFAULT_ENTRY_ID
        defaultTransactionDetailsShouldBeFound("entryId.greaterThanOrEqual=" + DEFAULT_ENTRY_ID);

        // Get all the transactionDetailsList where entryId is greater than or equal to UPDATED_ENTRY_ID
        defaultTransactionDetailsShouldNotBeFound("entryId.greaterThanOrEqual=" + UPDATED_ENTRY_ID);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByEntryIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where entryId is less than or equal to DEFAULT_ENTRY_ID
        defaultTransactionDetailsShouldBeFound("entryId.lessThanOrEqual=" + DEFAULT_ENTRY_ID);

        // Get all the transactionDetailsList where entryId is less than or equal to SMALLER_ENTRY_ID
        defaultTransactionDetailsShouldNotBeFound("entryId.lessThanOrEqual=" + SMALLER_ENTRY_ID);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByEntryIdIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where entryId is less than DEFAULT_ENTRY_ID
        defaultTransactionDetailsShouldNotBeFound("entryId.lessThan=" + DEFAULT_ENTRY_ID);

        // Get all the transactionDetailsList where entryId is less than UPDATED_ENTRY_ID
        defaultTransactionDetailsShouldBeFound("entryId.lessThan=" + UPDATED_ENTRY_ID);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByEntryIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where entryId is greater than DEFAULT_ENTRY_ID
        defaultTransactionDetailsShouldNotBeFound("entryId.greaterThan=" + DEFAULT_ENTRY_ID);

        // Get all the transactionDetailsList where entryId is greater than SMALLER_ENTRY_ID
        defaultTransactionDetailsShouldBeFound("entryId.greaterThan=" + SMALLER_ENTRY_ID);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultTransactionDetailsShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the transactionDetailsList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultTransactionDetailsShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionDate not equals to DEFAULT_TRANSACTION_DATE
        defaultTransactionDetailsShouldNotBeFound("transactionDate.notEquals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the transactionDetailsList where transactionDate not equals to UPDATED_TRANSACTION_DATE
        defaultTransactionDetailsShouldBeFound("transactionDate.notEquals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultTransactionDetailsShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the transactionDetailsList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultTransactionDetailsShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionDate is not null
        defaultTransactionDetailsShouldBeFound("transactionDate.specified=true");

        // Get all the transactionDetailsList where transactionDate is null
        defaultTransactionDetailsShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultTransactionDetailsShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the transactionDetailsList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultTransactionDetailsShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultTransactionDetailsShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the transactionDetailsList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultTransactionDetailsShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultTransactionDetailsShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the transactionDetailsList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultTransactionDetailsShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultTransactionDetailsShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the transactionDetailsList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultTransactionDetailsShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where description equals to DEFAULT_DESCRIPTION
        defaultTransactionDetailsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the transactionDetailsList where description equals to UPDATED_DESCRIPTION
        defaultTransactionDetailsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where description not equals to DEFAULT_DESCRIPTION
        defaultTransactionDetailsShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the transactionDetailsList where description not equals to UPDATED_DESCRIPTION
        defaultTransactionDetailsShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTransactionDetailsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the transactionDetailsList where description equals to UPDATED_DESCRIPTION
        defaultTransactionDetailsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where description is not null
        defaultTransactionDetailsShouldBeFound("description.specified=true");

        // Get all the transactionDetailsList where description is null
        defaultTransactionDetailsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where description contains DEFAULT_DESCRIPTION
        defaultTransactionDetailsShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the transactionDetailsList where description contains UPDATED_DESCRIPTION
        defaultTransactionDetailsShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where description does not contain DEFAULT_DESCRIPTION
        defaultTransactionDetailsShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the transactionDetailsList where description does not contain UPDATED_DESCRIPTION
        defaultTransactionDetailsShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where amount equals to DEFAULT_AMOUNT
        defaultTransactionDetailsShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the transactionDetailsList where amount equals to UPDATED_AMOUNT
        defaultTransactionDetailsShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where amount not equals to DEFAULT_AMOUNT
        defaultTransactionDetailsShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the transactionDetailsList where amount not equals to UPDATED_AMOUNT
        defaultTransactionDetailsShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultTransactionDetailsShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the transactionDetailsList where amount equals to UPDATED_AMOUNT
        defaultTransactionDetailsShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where amount is not null
        defaultTransactionDetailsShouldBeFound("amount.specified=true");

        // Get all the transactionDetailsList where amount is null
        defaultTransactionDetailsShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultTransactionDetailsShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the transactionDetailsList where amount is greater than or equal to UPDATED_AMOUNT
        defaultTransactionDetailsShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where amount is less than or equal to DEFAULT_AMOUNT
        defaultTransactionDetailsShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the transactionDetailsList where amount is less than or equal to SMALLER_AMOUNT
        defaultTransactionDetailsShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where amount is less than DEFAULT_AMOUNT
        defaultTransactionDetailsShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the transactionDetailsList where amount is less than UPDATED_AMOUNT
        defaultTransactionDetailsShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where amount is greater than DEFAULT_AMOUNT
        defaultTransactionDetailsShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the transactionDetailsList where amount is greater than SMALLER_AMOUNT
        defaultTransactionDetailsShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where isDeleted equals to DEFAULT_IS_DELETED
        defaultTransactionDetailsShouldBeFound("isDeleted.equals=" + DEFAULT_IS_DELETED);

        // Get all the transactionDetailsList where isDeleted equals to UPDATED_IS_DELETED
        defaultTransactionDetailsShouldNotBeFound("isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByIsDeletedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where isDeleted not equals to DEFAULT_IS_DELETED
        defaultTransactionDetailsShouldNotBeFound("isDeleted.notEquals=" + DEFAULT_IS_DELETED);

        // Get all the transactionDetailsList where isDeleted not equals to UPDATED_IS_DELETED
        defaultTransactionDetailsShouldBeFound("isDeleted.notEquals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where isDeleted in DEFAULT_IS_DELETED or UPDATED_IS_DELETED
        defaultTransactionDetailsShouldBeFound("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED);

        // Get all the transactionDetailsList where isDeleted equals to UPDATED_IS_DELETED
        defaultTransactionDetailsShouldNotBeFound("isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where isDeleted is not null
        defaultTransactionDetailsShouldBeFound("isDeleted.specified=true");

        // Get all the transactionDetailsList where isDeleted is null
        defaultTransactionDetailsShouldNotBeFound("isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByPostingIdIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where postingId equals to DEFAULT_POSTING_ID
        defaultTransactionDetailsShouldBeFound("postingId.equals=" + DEFAULT_POSTING_ID);

        // Get all the transactionDetailsList where postingId equals to UPDATED_POSTING_ID
        defaultTransactionDetailsShouldNotBeFound("postingId.equals=" + UPDATED_POSTING_ID);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByPostingIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where postingId not equals to DEFAULT_POSTING_ID
        defaultTransactionDetailsShouldNotBeFound("postingId.notEquals=" + DEFAULT_POSTING_ID);

        // Get all the transactionDetailsList where postingId not equals to UPDATED_POSTING_ID
        defaultTransactionDetailsShouldBeFound("postingId.notEquals=" + UPDATED_POSTING_ID);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByPostingIdIsInShouldWork() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where postingId in DEFAULT_POSTING_ID or UPDATED_POSTING_ID
        defaultTransactionDetailsShouldBeFound("postingId.in=" + DEFAULT_POSTING_ID + "," + UPDATED_POSTING_ID);

        // Get all the transactionDetailsList where postingId equals to UPDATED_POSTING_ID
        defaultTransactionDetailsShouldNotBeFound("postingId.in=" + UPDATED_POSTING_ID);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByPostingIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where postingId is not null
        defaultTransactionDetailsShouldBeFound("postingId.specified=true");

        // Get all the transactionDetailsList where postingId is null
        defaultTransactionDetailsShouldNotBeFound("postingId.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where createdAt equals to DEFAULT_CREATED_AT
        defaultTransactionDetailsShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the transactionDetailsList where createdAt equals to UPDATED_CREATED_AT
        defaultTransactionDetailsShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where createdAt not equals to DEFAULT_CREATED_AT
        defaultTransactionDetailsShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the transactionDetailsList where createdAt not equals to UPDATED_CREATED_AT
        defaultTransactionDetailsShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultTransactionDetailsShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the transactionDetailsList where createdAt equals to UPDATED_CREATED_AT
        defaultTransactionDetailsShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where createdAt is not null
        defaultTransactionDetailsShouldBeFound("createdAt.specified=true");

        // Get all the transactionDetailsList where createdAt is null
        defaultTransactionDetailsShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where createdAt is greater than or equal to DEFAULT_CREATED_AT
        defaultTransactionDetailsShouldBeFound("createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the transactionDetailsList where createdAt is greater than or equal to UPDATED_CREATED_AT
        defaultTransactionDetailsShouldNotBeFound("createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where createdAt is less than or equal to DEFAULT_CREATED_AT
        defaultTransactionDetailsShouldBeFound("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the transactionDetailsList where createdAt is less than or equal to SMALLER_CREATED_AT
        defaultTransactionDetailsShouldNotBeFound("createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where createdAt is less than DEFAULT_CREATED_AT
        defaultTransactionDetailsShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the transactionDetailsList where createdAt is less than UPDATED_CREATED_AT
        defaultTransactionDetailsShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where createdAt is greater than DEFAULT_CREATED_AT
        defaultTransactionDetailsShouldNotBeFound("createdAt.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the transactionDetailsList where createdAt is greater than SMALLER_CREATED_AT
        defaultTransactionDetailsShouldBeFound("createdAt.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByModifiedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where modifiedAt equals to DEFAULT_MODIFIED_AT
        defaultTransactionDetailsShouldBeFound("modifiedAt.equals=" + DEFAULT_MODIFIED_AT);

        // Get all the transactionDetailsList where modifiedAt equals to UPDATED_MODIFIED_AT
        defaultTransactionDetailsShouldNotBeFound("modifiedAt.equals=" + UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByModifiedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where modifiedAt not equals to DEFAULT_MODIFIED_AT
        defaultTransactionDetailsShouldNotBeFound("modifiedAt.notEquals=" + DEFAULT_MODIFIED_AT);

        // Get all the transactionDetailsList where modifiedAt not equals to UPDATED_MODIFIED_AT
        defaultTransactionDetailsShouldBeFound("modifiedAt.notEquals=" + UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByModifiedAtIsInShouldWork() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where modifiedAt in DEFAULT_MODIFIED_AT or UPDATED_MODIFIED_AT
        defaultTransactionDetailsShouldBeFound("modifiedAt.in=" + DEFAULT_MODIFIED_AT + "," + UPDATED_MODIFIED_AT);

        // Get all the transactionDetailsList where modifiedAt equals to UPDATED_MODIFIED_AT
        defaultTransactionDetailsShouldNotBeFound("modifiedAt.in=" + UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByModifiedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where modifiedAt is not null
        defaultTransactionDetailsShouldBeFound("modifiedAt.specified=true");

        // Get all the transactionDetailsList where modifiedAt is null
        defaultTransactionDetailsShouldNotBeFound("modifiedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByModifiedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where modifiedAt is greater than or equal to DEFAULT_MODIFIED_AT
        defaultTransactionDetailsShouldBeFound("modifiedAt.greaterThanOrEqual=" + DEFAULT_MODIFIED_AT);

        // Get all the transactionDetailsList where modifiedAt is greater than or equal to UPDATED_MODIFIED_AT
        defaultTransactionDetailsShouldNotBeFound("modifiedAt.greaterThanOrEqual=" + UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByModifiedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where modifiedAt is less than or equal to DEFAULT_MODIFIED_AT
        defaultTransactionDetailsShouldBeFound("modifiedAt.lessThanOrEqual=" + DEFAULT_MODIFIED_AT);

        // Get all the transactionDetailsList where modifiedAt is less than or equal to SMALLER_MODIFIED_AT
        defaultTransactionDetailsShouldNotBeFound("modifiedAt.lessThanOrEqual=" + SMALLER_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByModifiedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where modifiedAt is less than DEFAULT_MODIFIED_AT
        defaultTransactionDetailsShouldNotBeFound("modifiedAt.lessThan=" + DEFAULT_MODIFIED_AT);

        // Get all the transactionDetailsList where modifiedAt is less than UPDATED_MODIFIED_AT
        defaultTransactionDetailsShouldBeFound("modifiedAt.lessThan=" + UPDATED_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByModifiedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where modifiedAt is greater than DEFAULT_MODIFIED_AT
        defaultTransactionDetailsShouldNotBeFound("modifiedAt.greaterThan=" + DEFAULT_MODIFIED_AT);

        // Get all the transactionDetailsList where modifiedAt is greater than SMALLER_MODIFIED_AT
        defaultTransactionDetailsShouldBeFound("modifiedAt.greaterThan=" + SMALLER_MODIFIED_AT);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionType equals to DEFAULT_TRANSACTION_TYPE
        defaultTransactionDetailsShouldBeFound("transactionType.equals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the transactionDetailsList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultTransactionDetailsShouldNotBeFound("transactionType.equals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionType not equals to DEFAULT_TRANSACTION_TYPE
        defaultTransactionDetailsShouldNotBeFound("transactionType.notEquals=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the transactionDetailsList where transactionType not equals to UPDATED_TRANSACTION_TYPE
        defaultTransactionDetailsShouldBeFound("transactionType.notEquals=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionType in DEFAULT_TRANSACTION_TYPE or UPDATED_TRANSACTION_TYPE
        defaultTransactionDetailsShouldBeFound("transactionType.in=" + DEFAULT_TRANSACTION_TYPE + "," + UPDATED_TRANSACTION_TYPE);

        // Get all the transactionDetailsList where transactionType equals to UPDATED_TRANSACTION_TYPE
        defaultTransactionDetailsShouldNotBeFound("transactionType.in=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionType is not null
        defaultTransactionDetailsShouldBeFound("transactionType.specified=true");

        // Get all the transactionDetailsList where transactionType is null
        defaultTransactionDetailsShouldNotBeFound("transactionType.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionTypeContainsSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionType contains DEFAULT_TRANSACTION_TYPE
        defaultTransactionDetailsShouldBeFound("transactionType.contains=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the transactionDetailsList where transactionType contains UPDATED_TRANSACTION_TYPE
        defaultTransactionDetailsShouldNotBeFound("transactionType.contains=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByTransactionTypeNotContainsSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        // Get all the transactionDetailsList where transactionType does not contain DEFAULT_TRANSACTION_TYPE
        defaultTransactionDetailsShouldNotBeFound("transactionType.doesNotContain=" + DEFAULT_TRANSACTION_TYPE);

        // Get all the transactionDetailsList where transactionType does not contain UPDATED_TRANSACTION_TYPE
        defaultTransactionDetailsShouldBeFound("transactionType.doesNotContain=" + UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByDebitAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);
        TransactionAccount debitAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            debitAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(debitAccount);
            em.flush();
        } else {
            debitAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(debitAccount);
        em.flush();
        transactionDetails.setDebitAccount(debitAccount);
        transactionDetailsRepository.saveAndFlush(transactionDetails);
        Long debitAccountId = debitAccount.getId();

        // Get all the transactionDetailsList where debitAccount equals to debitAccountId
        defaultTransactionDetailsShouldBeFound("debitAccountId.equals=" + debitAccountId);

        // Get all the transactionDetailsList where debitAccount equals to (debitAccountId + 1)
        defaultTransactionDetailsShouldNotBeFound("debitAccountId.equals=" + (debitAccountId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByCreditAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);
        TransactionAccount creditAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            creditAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(creditAccount);
            em.flush();
        } else {
            creditAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(creditAccount);
        em.flush();
        transactionDetails.setCreditAccount(creditAccount);
        transactionDetailsRepository.saveAndFlush(transactionDetails);
        Long creditAccountId = creditAccount.getId();

        // Get all the transactionDetailsList where creditAccount equals to creditAccountId
        defaultTransactionDetailsShouldBeFound("creditAccountId.equals=" + creditAccountId);

        // Get all the transactionDetailsList where creditAccount equals to (creditAccountId + 1)
        defaultTransactionDetailsShouldNotBeFound("creditAccountId.equals=" + (creditAccountId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);
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
        transactionDetails.addPlaceholder(placeholder);
        transactionDetailsRepository.saveAndFlush(transactionDetails);
        Long placeholderId = placeholder.getId();

        // Get all the transactionDetailsList where placeholder equals to placeholderId
        defaultTransactionDetailsShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the transactionDetailsList where placeholder equals to (placeholderId + 1)
        defaultTransactionDetailsShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionDetailsByPostedByIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);
        ApplicationUser postedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            postedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(postedBy);
            em.flush();
        } else {
            postedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(postedBy);
        em.flush();
        transactionDetails.setPostedBy(postedBy);
        transactionDetailsRepository.saveAndFlush(transactionDetails);
        Long postedById = postedBy.getId();

        // Get all the transactionDetailsList where postedBy equals to postedById
        defaultTransactionDetailsShouldBeFound("postedById.equals=" + postedById);

        // Get all the transactionDetailsList where postedBy equals to (postedById + 1)
        defaultTransactionDetailsShouldNotBeFound("postedById.equals=" + (postedById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionDetailsShouldBeFound(String filter) throws Exception {
        restTransactionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].entryId").value(hasItem(DEFAULT_ENTRY_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].postingId").value(hasItem(DEFAULT_POSTING_ID.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(sameInstant(DEFAULT_MODIFIED_AT))))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE)));

        // Check, that the count call also returns 1
        restTransactionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionDetailsShouldNotBeFound(String filter) throws Exception {
        restTransactionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionDetails() throws Exception {
        // Get the transactionDetails
        restTransactionDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransactionDetails() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        int databaseSizeBeforeUpdate = transactionDetailsRepository.findAll().size();

        // Update the transactionDetails
        TransactionDetails updatedTransactionDetails = transactionDetailsRepository.findById(transactionDetails.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionDetails are not directly saved in db
        em.detach(updatedTransactionDetails);
        updatedTransactionDetails
            .entryId(UPDATED_ENTRY_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .isDeleted(UPDATED_IS_DELETED)
            .postingId(UPDATED_POSTING_ID)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .transactionType(UPDATED_TRANSACTION_TYPE);
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(updatedTransactionDetails);

        restTransactionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate);
        TransactionDetails testTransactionDetails = transactionDetailsList.get(transactionDetailsList.size() - 1);
        assertThat(testTransactionDetails.getEntryId()).isEqualTo(UPDATED_ENTRY_ID);
        assertThat(testTransactionDetails.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testTransactionDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransactionDetails.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTransactionDetails.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testTransactionDetails.getPostingId()).isEqualTo(UPDATED_POSTING_ID);
        assertThat(testTransactionDetails.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTransactionDetails.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
        assertThat(testTransactionDetails.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);

        // Validate the TransactionDetails in Elasticsearch
        verify(mockTransactionDetailsSearchRepository).save(testTransactionDetails);
    }

    @Test
    @Transactional
    void putNonExistingTransactionDetails() throws Exception {
        int databaseSizeBeforeUpdate = transactionDetailsRepository.findAll().size();
        transactionDetails.setId(count.incrementAndGet());

        // Create the TransactionDetails
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionDetails in Elasticsearch
        verify(mockTransactionDetailsSearchRepository, times(0)).save(transactionDetails);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionDetails() throws Exception {
        int databaseSizeBeforeUpdate = transactionDetailsRepository.findAll().size();
        transactionDetails.setId(count.incrementAndGet());

        // Create the TransactionDetails
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionDetails in Elasticsearch
        verify(mockTransactionDetailsSearchRepository, times(0)).save(transactionDetails);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionDetails() throws Exception {
        int databaseSizeBeforeUpdate = transactionDetailsRepository.findAll().size();
        transactionDetails.setId(count.incrementAndGet());

        // Create the TransactionDetails
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionDetails in Elasticsearch
        verify(mockTransactionDetailsSearchRepository, times(0)).save(transactionDetails);
    }

    @Test
    @Transactional
    void partialUpdateTransactionDetailsWithPatch() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        int databaseSizeBeforeUpdate = transactionDetailsRepository.findAll().size();

        // Update the transactionDetails using partial update
        TransactionDetails partialUpdatedTransactionDetails = new TransactionDetails();
        partialUpdatedTransactionDetails.setId(transactionDetails.getId());

        partialUpdatedTransactionDetails
            .entryId(UPDATED_ENTRY_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION)
            .isDeleted(UPDATED_IS_DELETED)
            .postingId(UPDATED_POSTING_ID)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .transactionType(UPDATED_TRANSACTION_TYPE);

        restTransactionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionDetails))
            )
            .andExpect(status().isOk());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate);
        TransactionDetails testTransactionDetails = transactionDetailsList.get(transactionDetailsList.size() - 1);
        assertThat(testTransactionDetails.getEntryId()).isEqualTo(UPDATED_ENTRY_ID);
        assertThat(testTransactionDetails.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testTransactionDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransactionDetails.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testTransactionDetails.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testTransactionDetails.getPostingId()).isEqualTo(UPDATED_POSTING_ID);
        assertThat(testTransactionDetails.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTransactionDetails.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
        assertThat(testTransactionDetails.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateTransactionDetailsWithPatch() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        int databaseSizeBeforeUpdate = transactionDetailsRepository.findAll().size();

        // Update the transactionDetails using partial update
        TransactionDetails partialUpdatedTransactionDetails = new TransactionDetails();
        partialUpdatedTransactionDetails.setId(transactionDetails.getId());

        partialUpdatedTransactionDetails
            .entryId(UPDATED_ENTRY_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION)
            .amount(UPDATED_AMOUNT)
            .isDeleted(UPDATED_IS_DELETED)
            .postingId(UPDATED_POSTING_ID)
            .createdAt(UPDATED_CREATED_AT)
            .modifiedAt(UPDATED_MODIFIED_AT)
            .transactionType(UPDATED_TRANSACTION_TYPE);

        restTransactionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionDetails))
            )
            .andExpect(status().isOk());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate);
        TransactionDetails testTransactionDetails = transactionDetailsList.get(transactionDetailsList.size() - 1);
        assertThat(testTransactionDetails.getEntryId()).isEqualTo(UPDATED_ENTRY_ID);
        assertThat(testTransactionDetails.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testTransactionDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransactionDetails.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testTransactionDetails.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testTransactionDetails.getPostingId()).isEqualTo(UPDATED_POSTING_ID);
        assertThat(testTransactionDetails.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTransactionDetails.getModifiedAt()).isEqualTo(UPDATED_MODIFIED_AT);
        assertThat(testTransactionDetails.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionDetails() throws Exception {
        int databaseSizeBeforeUpdate = transactionDetailsRepository.findAll().size();
        transactionDetails.setId(count.incrementAndGet());

        // Create the TransactionDetails
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionDetails in Elasticsearch
        verify(mockTransactionDetailsSearchRepository, times(0)).save(transactionDetails);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionDetails() throws Exception {
        int databaseSizeBeforeUpdate = transactionDetailsRepository.findAll().size();
        transactionDetails.setId(count.incrementAndGet());

        // Create the TransactionDetails
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionDetails in Elasticsearch
        verify(mockTransactionDetailsSearchRepository, times(0)).save(transactionDetails);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionDetails() throws Exception {
        int databaseSizeBeforeUpdate = transactionDetailsRepository.findAll().size();
        transactionDetails.setId(count.incrementAndGet());

        // Create the TransactionDetails
        TransactionDetailsDTO transactionDetailsDTO = transactionDetailsMapper.toDto(transactionDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionDetails in the database
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TransactionDetails in Elasticsearch
        verify(mockTransactionDetailsSearchRepository, times(0)).save(transactionDetails);
    }

    @Test
    @Transactional
    void deleteTransactionDetails() throws Exception {
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);

        int databaseSizeBeforeDelete = transactionDetailsRepository.findAll().size();

        // Delete the transactionDetails
        restTransactionDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionDetails> transactionDetailsList = transactionDetailsRepository.findAll();
        assertThat(transactionDetailsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TransactionDetails in Elasticsearch
        verify(mockTransactionDetailsSearchRepository, times(1)).deleteById(transactionDetails.getId());
    }

    @Test
    @Transactional
    void searchTransactionDetails() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        transactionDetailsRepository.saveAndFlush(transactionDetails);
        when(mockTransactionDetailsSearchRepository.search("id:" + transactionDetails.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(transactionDetails), PageRequest.of(0, 1), 1));

        // Search the transactionDetails
        restTransactionDetailsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + transactionDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].entryId").value(hasItem(DEFAULT_ENTRY_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].postingId").value(hasItem(DEFAULT_POSTING_ID.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].modifiedAt").value(hasItem(sameInstant(DEFAULT_MODIFIED_AT))))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE)));
    }
}
