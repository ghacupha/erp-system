package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark III No 7 (Caleb Series) Server ver 0.3.0-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.*;
import io.github.erp.repository.PrepaymentAccountRepository;
import io.github.erp.repository.search.PrepaymentAccountSearchRepository;
import io.github.erp.service.PrepaymentAccountService;
import io.github.erp.service.dto.PrepaymentAccountDTO;
import io.github.erp.service.mapper.PrepaymentAccountMapper;
import io.github.erp.web.rest.utils.TestUtil;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.utils.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the PrepaymentAccountResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
public class PrepaymentAccountResourceIT {

    private static final String DEFAULT_CATALOGUE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CATALOGUE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PARTICULARS = "AAAAAAAAAA";
    private static final String UPDATED_PARTICULARS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PREPAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PREPAYMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PREPAYMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final UUID DEFAULT_PREPAYMENT_GUID = UUID.randomUUID();
    private static final UUID UPDATED_PREPAYMENT_GUID = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/dev/prepayment-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/prepayment-accounts";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrepaymentAccountRepository prepaymentAccountRepository;

    @Mock
    private PrepaymentAccountRepository prepaymentAccountRepositoryMock;

    @Autowired
    private PrepaymentAccountMapper prepaymentAccountMapper;

    @Mock
    private PrepaymentAccountService prepaymentAccountServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PrepaymentAccountSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrepaymentAccountSearchRepository mockPrepaymentAccountSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrepaymentAccountMockMvc;

    private PrepaymentAccount prepaymentAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentAccount createEntity(EntityManager em) {
        PrepaymentAccount prepaymentAccount = new PrepaymentAccount()
            .catalogueNumber(DEFAULT_CATALOGUE_NUMBER)
            .particulars(DEFAULT_PARTICULARS)
            .notes(DEFAULT_NOTES)
            .prepaymentAmount(DEFAULT_PREPAYMENT_AMOUNT)
            .prepaymentGuid(DEFAULT_PREPAYMENT_GUID);
        return prepaymentAccount;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentAccount createUpdatedEntity(EntityManager em) {
        PrepaymentAccount prepaymentAccount = new PrepaymentAccount()
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .notes(UPDATED_NOTES)
            .prepaymentAmount(UPDATED_PREPAYMENT_AMOUNT)
            .prepaymentGuid(UPDATED_PREPAYMENT_GUID);
        return prepaymentAccount;
    }

    @BeforeEach
    public void initTest() {
        prepaymentAccount = createEntity(em);
    }

    @Test
    @Transactional
    void createPrepaymentAccount() throws Exception {
        int databaseSizeBeforeCreate = prepaymentAccountRepository.findAll().size();
        // Create the PrepaymentAccount
        PrepaymentAccountDTO prepaymentAccountDTO = prepaymentAccountMapper.toDto(prepaymentAccount);
        restPrepaymentAccountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PrepaymentAccount in the database
        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeCreate + 1);
        PrepaymentAccount testPrepaymentAccount = prepaymentAccountList.get(prepaymentAccountList.size() - 1);
        assertThat(testPrepaymentAccount.getCatalogueNumber()).isEqualTo(DEFAULT_CATALOGUE_NUMBER);
        assertThat(testPrepaymentAccount.getParticulars()).isEqualTo(DEFAULT_PARTICULARS);
        assertThat(testPrepaymentAccount.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testPrepaymentAccount.getPrepaymentAmount()).isEqualByComparingTo(DEFAULT_PREPAYMENT_AMOUNT);
        assertThat(testPrepaymentAccount.getPrepaymentGuid()).isEqualTo(DEFAULT_PREPAYMENT_GUID);

        // Validate the PrepaymentAccount in Elasticsearch
        verify(mockPrepaymentAccountSearchRepository, times(1)).save(testPrepaymentAccount);
    }

    @Test
    @Transactional
    void createPrepaymentAccountWithExistingId() throws Exception {
        // Create the PrepaymentAccount with an existing ID
        prepaymentAccount.setId(1L);
        PrepaymentAccountDTO prepaymentAccountDTO = prepaymentAccountMapper.toDto(prepaymentAccount);

        int databaseSizeBeforeCreate = prepaymentAccountRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrepaymentAccountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAccount in the database
        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeCreate);

        // Validate the PrepaymentAccount in Elasticsearch
        verify(mockPrepaymentAccountSearchRepository, times(0)).save(prepaymentAccount);
    }

    @Test
    @Transactional
    void checkCatalogueNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentAccountRepository.findAll().size();
        // set the field null
        prepaymentAccount.setCatalogueNumber(null);

        // Create the PrepaymentAccount, which fails.
        PrepaymentAccountDTO prepaymentAccountDTO = prepaymentAccountMapper.toDto(prepaymentAccount);

        restPrepaymentAccountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkParticularsIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentAccountRepository.findAll().size();
        // set the field null
        prepaymentAccount.setParticulars(null);

        // Create the PrepaymentAccount, which fails.
        PrepaymentAccountDTO prepaymentAccountDTO = prepaymentAccountMapper.toDto(prepaymentAccount);

        restPrepaymentAccountMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccounts() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList
        restPrepaymentAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].prepaymentAmount").value(hasItem(sameNumber(DEFAULT_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].prepaymentGuid").value(hasItem(DEFAULT_PREPAYMENT_GUID.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrepaymentAccountsWithEagerRelationshipsIsEnabled() throws Exception {
        when(prepaymentAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrepaymentAccountMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prepaymentAccountServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrepaymentAccountsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prepaymentAccountServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrepaymentAccountMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prepaymentAccountServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPrepaymentAccount() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get the prepaymentAccount
        restPrepaymentAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, prepaymentAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prepaymentAccount.getId().intValue()))
            .andExpect(jsonPath("$.catalogueNumber").value(DEFAULT_CATALOGUE_NUMBER))
            .andExpect(jsonPath("$.particulars").value(DEFAULT_PARTICULARS))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.prepaymentAmount").value(sameNumber(DEFAULT_PREPAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.prepaymentGuid").value(DEFAULT_PREPAYMENT_GUID.toString()));
    }

    @Test
    @Transactional
    void getPrepaymentAccountsByIdFiltering() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        Long id = prepaymentAccount.getId();

        defaultPrepaymentAccountShouldBeFound("id.equals=" + id);
        defaultPrepaymentAccountShouldNotBeFound("id.notEquals=" + id);

        defaultPrepaymentAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrepaymentAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultPrepaymentAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrepaymentAccountShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByCatalogueNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where catalogueNumber equals to DEFAULT_CATALOGUE_NUMBER
        defaultPrepaymentAccountShouldBeFound("catalogueNumber.equals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the prepaymentAccountList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultPrepaymentAccountShouldNotBeFound("catalogueNumber.equals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByCatalogueNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where catalogueNumber not equals to DEFAULT_CATALOGUE_NUMBER
        defaultPrepaymentAccountShouldNotBeFound("catalogueNumber.notEquals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the prepaymentAccountList where catalogueNumber not equals to UPDATED_CATALOGUE_NUMBER
        defaultPrepaymentAccountShouldBeFound("catalogueNumber.notEquals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByCatalogueNumberIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where catalogueNumber in DEFAULT_CATALOGUE_NUMBER or UPDATED_CATALOGUE_NUMBER
        defaultPrepaymentAccountShouldBeFound("catalogueNumber.in=" + DEFAULT_CATALOGUE_NUMBER + "," + UPDATED_CATALOGUE_NUMBER);

        // Get all the prepaymentAccountList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultPrepaymentAccountShouldNotBeFound("catalogueNumber.in=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByCatalogueNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where catalogueNumber is not null
        defaultPrepaymentAccountShouldBeFound("catalogueNumber.specified=true");

        // Get all the prepaymentAccountList where catalogueNumber is null
        defaultPrepaymentAccountShouldNotBeFound("catalogueNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByCatalogueNumberContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where catalogueNumber contains DEFAULT_CATALOGUE_NUMBER
        defaultPrepaymentAccountShouldBeFound("catalogueNumber.contains=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the prepaymentAccountList where catalogueNumber contains UPDATED_CATALOGUE_NUMBER
        defaultPrepaymentAccountShouldNotBeFound("catalogueNumber.contains=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByCatalogueNumberNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where catalogueNumber does not contain DEFAULT_CATALOGUE_NUMBER
        defaultPrepaymentAccountShouldNotBeFound("catalogueNumber.doesNotContain=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the prepaymentAccountList where catalogueNumber does not contain UPDATED_CATALOGUE_NUMBER
        defaultPrepaymentAccountShouldBeFound("catalogueNumber.doesNotContain=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByParticularsIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where particulars equals to DEFAULT_PARTICULARS
        defaultPrepaymentAccountShouldBeFound("particulars.equals=" + DEFAULT_PARTICULARS);

        // Get all the prepaymentAccountList where particulars equals to UPDATED_PARTICULARS
        defaultPrepaymentAccountShouldNotBeFound("particulars.equals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByParticularsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where particulars not equals to DEFAULT_PARTICULARS
        defaultPrepaymentAccountShouldNotBeFound("particulars.notEquals=" + DEFAULT_PARTICULARS);

        // Get all the prepaymentAccountList where particulars not equals to UPDATED_PARTICULARS
        defaultPrepaymentAccountShouldBeFound("particulars.notEquals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByParticularsIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where particulars in DEFAULT_PARTICULARS or UPDATED_PARTICULARS
        defaultPrepaymentAccountShouldBeFound("particulars.in=" + DEFAULT_PARTICULARS + "," + UPDATED_PARTICULARS);

        // Get all the prepaymentAccountList where particulars equals to UPDATED_PARTICULARS
        defaultPrepaymentAccountShouldNotBeFound("particulars.in=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByParticularsIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where particulars is not null
        defaultPrepaymentAccountShouldBeFound("particulars.specified=true");

        // Get all the prepaymentAccountList where particulars is null
        defaultPrepaymentAccountShouldNotBeFound("particulars.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByParticularsContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where particulars contains DEFAULT_PARTICULARS
        defaultPrepaymentAccountShouldBeFound("particulars.contains=" + DEFAULT_PARTICULARS);

        // Get all the prepaymentAccountList where particulars contains UPDATED_PARTICULARS
        defaultPrepaymentAccountShouldNotBeFound("particulars.contains=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByParticularsNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where particulars does not contain DEFAULT_PARTICULARS
        defaultPrepaymentAccountShouldNotBeFound("particulars.doesNotContain=" + DEFAULT_PARTICULARS);

        // Get all the prepaymentAccountList where particulars does not contain UPDATED_PARTICULARS
        defaultPrepaymentAccountShouldBeFound("particulars.doesNotContain=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where prepaymentAmount equals to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldBeFound("prepaymentAmount.equals=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountList where prepaymentAmount equals to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldNotBeFound("prepaymentAmount.equals=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where prepaymentAmount not equals to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldNotBeFound("prepaymentAmount.notEquals=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountList where prepaymentAmount not equals to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldBeFound("prepaymentAmount.notEquals=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where prepaymentAmount in DEFAULT_PREPAYMENT_AMOUNT or UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldBeFound("prepaymentAmount.in=" + DEFAULT_PREPAYMENT_AMOUNT + "," + UPDATED_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountList where prepaymentAmount equals to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldNotBeFound("prepaymentAmount.in=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where prepaymentAmount is not null
        defaultPrepaymentAccountShouldBeFound("prepaymentAmount.specified=true");

        // Get all the prepaymentAccountList where prepaymentAmount is null
        defaultPrepaymentAccountShouldNotBeFound("prepaymentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where prepaymentAmount is greater than or equal to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldBeFound("prepaymentAmount.greaterThanOrEqual=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountList where prepaymentAmount is greater than or equal to UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldNotBeFound("prepaymentAmount.greaterThanOrEqual=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where prepaymentAmount is less than or equal to DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldBeFound("prepaymentAmount.lessThanOrEqual=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountList where prepaymentAmount is less than or equal to SMALLER_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldNotBeFound("prepaymentAmount.lessThanOrEqual=" + SMALLER_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where prepaymentAmount is less than DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldNotBeFound("prepaymentAmount.lessThan=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountList where prepaymentAmount is less than UPDATED_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldBeFound("prepaymentAmount.lessThan=" + UPDATED_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where prepaymentAmount is greater than DEFAULT_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldNotBeFound("prepaymentAmount.greaterThan=" + DEFAULT_PREPAYMENT_AMOUNT);

        // Get all the prepaymentAccountList where prepaymentAmount is greater than SMALLER_PREPAYMENT_AMOUNT
        defaultPrepaymentAccountShouldBeFound("prepaymentAmount.greaterThan=" + SMALLER_PREPAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where prepaymentGuid equals to DEFAULT_PREPAYMENT_GUID
        defaultPrepaymentAccountShouldBeFound("prepaymentGuid.equals=" + DEFAULT_PREPAYMENT_GUID);

        // Get all the prepaymentAccountList where prepaymentGuid equals to UPDATED_PREPAYMENT_GUID
        defaultPrepaymentAccountShouldNotBeFound("prepaymentGuid.equals=" + UPDATED_PREPAYMENT_GUID);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentGuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where prepaymentGuid not equals to DEFAULT_PREPAYMENT_GUID
        defaultPrepaymentAccountShouldNotBeFound("prepaymentGuid.notEquals=" + DEFAULT_PREPAYMENT_GUID);

        // Get all the prepaymentAccountList where prepaymentGuid not equals to UPDATED_PREPAYMENT_GUID
        defaultPrepaymentAccountShouldBeFound("prepaymentGuid.notEquals=" + UPDATED_PREPAYMENT_GUID);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentGuidIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where prepaymentGuid in DEFAULT_PREPAYMENT_GUID or UPDATED_PREPAYMENT_GUID
        defaultPrepaymentAccountShouldBeFound("prepaymentGuid.in=" + DEFAULT_PREPAYMENT_GUID + "," + UPDATED_PREPAYMENT_GUID);

        // Get all the prepaymentAccountList where prepaymentGuid equals to UPDATED_PREPAYMENT_GUID
        defaultPrepaymentAccountShouldNotBeFound("prepaymentGuid.in=" + UPDATED_PREPAYMENT_GUID);
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        // Get all the prepaymentAccountList where prepaymentGuid is not null
        defaultPrepaymentAccountShouldBeFound("prepaymentGuid.specified=true");

        // Get all the prepaymentAccountList where prepaymentGuid is null
        defaultPrepaymentAccountShouldNotBeFound("prepaymentGuid.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        em.persist(settlementCurrency);
        em.flush();
        prepaymentAccount.setSettlementCurrency(settlementCurrency);
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the prepaymentAccountList where settlementCurrency equals to settlementCurrencyId
        defaultPrepaymentAccountShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the prepaymentAccountList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultPrepaymentAccountShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        Settlement prepaymentTransaction;
        if (TestUtil.findAll(em, Settlement.class).isEmpty()) {
            prepaymentTransaction = SettlementResourceIT.createEntity(em);
            em.persist(prepaymentTransaction);
            em.flush();
        } else {
            prepaymentTransaction = TestUtil.findAll(em, Settlement.class).get(0);
        }
        em.persist(prepaymentTransaction);
        em.flush();
        prepaymentAccount.setPrepaymentTransaction(prepaymentTransaction);
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        Long prepaymentTransactionId = prepaymentTransaction.getId();

        // Get all the prepaymentAccountList where prepaymentTransaction equals to prepaymentTransactionId
        defaultPrepaymentAccountShouldBeFound("prepaymentTransactionId.equals=" + prepaymentTransactionId);

        // Get all the prepaymentAccountList where prepaymentTransaction equals to (prepaymentTransactionId + 1)
        defaultPrepaymentAccountShouldNotBeFound("prepaymentTransactionId.equals=" + (prepaymentTransactionId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        em.persist(serviceOutlet);
        em.flush();
        prepaymentAccount.setServiceOutlet(serviceOutlet);
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        Long serviceOutletId = serviceOutlet.getId();

        // Get all the prepaymentAccountList where serviceOutlet equals to serviceOutletId
        defaultPrepaymentAccountShouldBeFound("serviceOutletId.equals=" + serviceOutletId);

        // Get all the prepaymentAccountList where serviceOutlet equals to (serviceOutletId + 1)
        defaultPrepaymentAccountShouldNotBeFound("serviceOutletId.equals=" + (serviceOutletId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByDealerIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(dealer);
        em.flush();
        prepaymentAccount.setDealer(dealer);
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        Long dealerId = dealer.getId();

        // Get all the prepaymentAccountList where dealer equals to dealerId
        defaultPrepaymentAccountShouldBeFound("dealerId.equals=" + dealerId);

        // Get all the prepaymentAccountList where dealer equals to (dealerId + 1)
        defaultPrepaymentAccountShouldNotBeFound("dealerId.equals=" + (dealerId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByDebitAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
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
        prepaymentAccount.setDebitAccount(debitAccount);
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        Long debitAccountId = debitAccount.getId();

        // Get all the prepaymentAccountList where debitAccount equals to debitAccountId
        defaultPrepaymentAccountShouldBeFound("debitAccountId.equals=" + debitAccountId);

        // Get all the prepaymentAccountList where debitAccount equals to (debitAccountId + 1)
        defaultPrepaymentAccountShouldNotBeFound("debitAccountId.equals=" + (debitAccountId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByTransferAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        TransactionAccount transferAccount;
        if (TestUtil.findAll(em, TransactionAccount.class).isEmpty()) {
            transferAccount = TransactionAccountResourceIT.createEntity(em);
            em.persist(transferAccount);
            em.flush();
        } else {
            transferAccount = TestUtil.findAll(em, TransactionAccount.class).get(0);
        }
        em.persist(transferAccount);
        em.flush();
        prepaymentAccount.setTransferAccount(transferAccount);
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        Long transferAccountId = transferAccount.getId();

        // Get all the prepaymentAccountList where transferAccount equals to transferAccountId
        defaultPrepaymentAccountShouldBeFound("transferAccountId.equals=" + transferAccountId);

        // Get all the prepaymentAccountList where transferAccount equals to (transferAccountId + 1)
        defaultPrepaymentAccountShouldNotBeFound("transferAccountId.equals=" + (transferAccountId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
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
        prepaymentAccount.addPlaceholder(placeholder);
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        Long placeholderId = placeholder.getId();

        // Get all the prepaymentAccountList where placeholder equals to placeholderId
        defaultPrepaymentAccountShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the prepaymentAccountList where placeholder equals to (placeholderId + 1)
        defaultPrepaymentAccountShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByGeneralParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        UniversallyUniqueMapping generalParameters;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            generalParameters = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(generalParameters);
            em.flush();
        } else {
            generalParameters = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(generalParameters);
        em.flush();
        prepaymentAccount.addGeneralParameters(generalParameters);
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        Long generalParametersId = generalParameters.getId();

        // Get all the prepaymentAccountList where generalParameters equals to generalParametersId
        defaultPrepaymentAccountShouldBeFound("generalParametersId.equals=" + generalParametersId);

        // Get all the prepaymentAccountList where generalParameters equals to (generalParametersId + 1)
        defaultPrepaymentAccountShouldNotBeFound("generalParametersId.equals=" + (generalParametersId + 1));
    }

    @Test
    @Transactional
    void getAllPrepaymentAccountsByPrepaymentParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        PrepaymentMapping prepaymentParameters;
        if (TestUtil.findAll(em, PrepaymentMapping.class).isEmpty()) {
            prepaymentParameters = PrepaymentMappingResourceIT.createEntity(em);
            em.persist(prepaymentParameters);
            em.flush();
        } else {
            prepaymentParameters = TestUtil.findAll(em, PrepaymentMapping.class).get(0);
        }
        em.persist(prepaymentParameters);
        em.flush();
        prepaymentAccount.addPrepaymentParameters(prepaymentParameters);
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        Long prepaymentParametersId = prepaymentParameters.getId();

        // Get all the prepaymentAccountList where prepaymentParameters equals to prepaymentParametersId
        defaultPrepaymentAccountShouldBeFound("prepaymentParametersId.equals=" + prepaymentParametersId);

        // Get all the prepaymentAccountList where prepaymentParameters equals to (prepaymentParametersId + 1)
        defaultPrepaymentAccountShouldNotBeFound("prepaymentParametersId.equals=" + (prepaymentParametersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrepaymentAccountShouldBeFound(String filter) throws Exception {
        restPrepaymentAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].prepaymentAmount").value(hasItem(sameNumber(DEFAULT_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].prepaymentGuid").value(hasItem(DEFAULT_PREPAYMENT_GUID.toString())));

        // Check, that the count call also returns 1
        restPrepaymentAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrepaymentAccountShouldNotBeFound(String filter) throws Exception {
        restPrepaymentAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrepaymentAccountMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrepaymentAccount() throws Exception {
        // Get the prepaymentAccount
        restPrepaymentAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrepaymentAccount() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        int databaseSizeBeforeUpdate = prepaymentAccountRepository.findAll().size();

        // Update the prepaymentAccount
        PrepaymentAccount updatedPrepaymentAccount = prepaymentAccountRepository.findById(prepaymentAccount.getId()).get();
        // Disconnect from session so that the updates on updatedPrepaymentAccount are not directly saved in db
        em.detach(updatedPrepaymentAccount);
        updatedPrepaymentAccount
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .notes(UPDATED_NOTES)
            .prepaymentAmount(UPDATED_PREPAYMENT_AMOUNT)
            .prepaymentGuid(UPDATED_PREPAYMENT_GUID);
        PrepaymentAccountDTO prepaymentAccountDTO = prepaymentAccountMapper.toDto(updatedPrepaymentAccount);

        restPrepaymentAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentAccount in the database
        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentAccount testPrepaymentAccount = prepaymentAccountList.get(prepaymentAccountList.size() - 1);
        assertThat(testPrepaymentAccount.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testPrepaymentAccount.getParticulars()).isEqualTo(UPDATED_PARTICULARS);
        assertThat(testPrepaymentAccount.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPrepaymentAccount.getPrepaymentAmount()).isEqualTo(UPDATED_PREPAYMENT_AMOUNT);
        assertThat(testPrepaymentAccount.getPrepaymentGuid()).isEqualTo(UPDATED_PREPAYMENT_GUID);

        // Validate the PrepaymentAccount in Elasticsearch
        verify(mockPrepaymentAccountSearchRepository).save(testPrepaymentAccount);
    }

    @Test
    @Transactional
    void putNonExistingPrepaymentAccount() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAccountRepository.findAll().size();
        prepaymentAccount.setId(count.incrementAndGet());

        // Create the PrepaymentAccount
        PrepaymentAccountDTO prepaymentAccountDTO = prepaymentAccountMapper.toDto(prepaymentAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAccount in the database
        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAccount in Elasticsearch
        verify(mockPrepaymentAccountSearchRepository, times(0)).save(prepaymentAccount);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrepaymentAccount() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAccountRepository.findAll().size();
        prepaymentAccount.setId(count.incrementAndGet());

        // Create the PrepaymentAccount
        PrepaymentAccountDTO prepaymentAccountDTO = prepaymentAccountMapper.toDto(prepaymentAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAccount in the database
        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAccount in Elasticsearch
        verify(mockPrepaymentAccountSearchRepository, times(0)).save(prepaymentAccount);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrepaymentAccount() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAccountRepository.findAll().size();
        prepaymentAccount.setId(count.incrementAndGet());

        // Create the PrepaymentAccount
        PrepaymentAccountDTO prepaymentAccountDTO = prepaymentAccountMapper.toDto(prepaymentAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentAccountMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prepaymentAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentAccount in the database
        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAccount in Elasticsearch
        verify(mockPrepaymentAccountSearchRepository, times(0)).save(prepaymentAccount);
    }

    @Test
    @Transactional
    void partialUpdatePrepaymentAccountWithPatch() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        int databaseSizeBeforeUpdate = prepaymentAccountRepository.findAll().size();

        // Update the prepaymentAccount using partial update
        PrepaymentAccount partialUpdatedPrepaymentAccount = new PrepaymentAccount();
        partialUpdatedPrepaymentAccount.setId(prepaymentAccount.getId());

        partialUpdatedPrepaymentAccount
            .notes(UPDATED_NOTES)
            .prepaymentAmount(UPDATED_PREPAYMENT_AMOUNT)
            .prepaymentGuid(UPDATED_PREPAYMENT_GUID);

        restPrepaymentAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentAccount))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentAccount in the database
        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentAccount testPrepaymentAccount = prepaymentAccountList.get(prepaymentAccountList.size() - 1);
        assertThat(testPrepaymentAccount.getCatalogueNumber()).isEqualTo(DEFAULT_CATALOGUE_NUMBER);
        assertThat(testPrepaymentAccount.getParticulars()).isEqualTo(DEFAULT_PARTICULARS);
        assertThat(testPrepaymentAccount.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPrepaymentAccount.getPrepaymentAmount()).isEqualByComparingTo(UPDATED_PREPAYMENT_AMOUNT);
        assertThat(testPrepaymentAccount.getPrepaymentGuid()).isEqualTo(UPDATED_PREPAYMENT_GUID);
    }

    @Test
    @Transactional
    void fullUpdatePrepaymentAccountWithPatch() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        int databaseSizeBeforeUpdate = prepaymentAccountRepository.findAll().size();

        // Update the prepaymentAccount using partial update
        PrepaymentAccount partialUpdatedPrepaymentAccount = new PrepaymentAccount();
        partialUpdatedPrepaymentAccount.setId(prepaymentAccount.getId());

        partialUpdatedPrepaymentAccount
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .notes(UPDATED_NOTES)
            .prepaymentAmount(UPDATED_PREPAYMENT_AMOUNT)
            .prepaymentGuid(UPDATED_PREPAYMENT_GUID);

        restPrepaymentAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentAccount))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentAccount in the database
        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentAccount testPrepaymentAccount = prepaymentAccountList.get(prepaymentAccountList.size() - 1);
        assertThat(testPrepaymentAccount.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testPrepaymentAccount.getParticulars()).isEqualTo(UPDATED_PARTICULARS);
        assertThat(testPrepaymentAccount.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPrepaymentAccount.getPrepaymentAmount()).isEqualByComparingTo(UPDATED_PREPAYMENT_AMOUNT);
        assertThat(testPrepaymentAccount.getPrepaymentGuid()).isEqualTo(UPDATED_PREPAYMENT_GUID);
    }

    @Test
    @Transactional
    void patchNonExistingPrepaymentAccount() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAccountRepository.findAll().size();
        prepaymentAccount.setId(count.incrementAndGet());

        // Create the PrepaymentAccount
        PrepaymentAccountDTO prepaymentAccountDTO = prepaymentAccountMapper.toDto(prepaymentAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prepaymentAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAccount in the database
        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAccount in Elasticsearch
        verify(mockPrepaymentAccountSearchRepository, times(0)).save(prepaymentAccount);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrepaymentAccount() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAccountRepository.findAll().size();
        prepaymentAccount.setId(count.incrementAndGet());

        // Create the PrepaymentAccount
        PrepaymentAccountDTO prepaymentAccountDTO = prepaymentAccountMapper.toDto(prepaymentAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentAccount in the database
        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAccount in Elasticsearch
        verify(mockPrepaymentAccountSearchRepository, times(0)).save(prepaymentAccount);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrepaymentAccount() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentAccountRepository.findAll().size();
        prepaymentAccount.setId(count.incrementAndGet());

        // Create the PrepaymentAccount
        PrepaymentAccountDTO prepaymentAccountDTO = prepaymentAccountMapper.toDto(prepaymentAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentAccountMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentAccountDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentAccount in the database
        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentAccount in Elasticsearch
        verify(mockPrepaymentAccountSearchRepository, times(0)).save(prepaymentAccount);
    }

    @Test
    @Transactional
    void deletePrepaymentAccount() throws Exception {
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);

        int databaseSizeBeforeDelete = prepaymentAccountRepository.findAll().size();

        // Delete the prepaymentAccount
        restPrepaymentAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, prepaymentAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrepaymentAccount> prepaymentAccountList = prepaymentAccountRepository.findAll();
        assertThat(prepaymentAccountList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PrepaymentAccount in Elasticsearch
        verify(mockPrepaymentAccountSearchRepository, times(1)).deleteById(prepaymentAccount.getId());
    }

    @Test
    @Transactional
    void searchPrepaymentAccount() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        prepaymentAccountRepository.saveAndFlush(prepaymentAccount);
        when(mockPrepaymentAccountSearchRepository.search("id:" + prepaymentAccount.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prepaymentAccount), PageRequest.of(0, 1), 1));

        // Search the prepaymentAccount
        restPrepaymentAccountMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prepaymentAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].prepaymentAmount").value(hasItem(sameNumber(DEFAULT_PREPAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].prepaymentGuid").value(hasItem(DEFAULT_PREPAYMENT_GUID.toString())));
    }
}
