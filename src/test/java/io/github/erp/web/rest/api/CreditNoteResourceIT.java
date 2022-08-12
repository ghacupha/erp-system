package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark II No 26 (Baruch Series) Server ver 0.0.6-SNAPSHOT
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
import io.github.erp.repository.CreditNoteRepository;
import io.github.erp.repository.search.CreditNoteSearchRepository;
import io.github.erp.service.CreditNoteService;
import io.github.erp.service.dto.CreditNoteDTO;
import io.github.erp.service.mapper.CreditNoteMapper;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CreditNoteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
public class CreditNoteResourceIT {

    private static final String DEFAULT_CREDIT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREDIT_NOTE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREDIT_NOTE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREDIT_NOTE_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_CREDIT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_CREDIT_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dev/credit-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/credit-notes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CreditNoteRepository creditNoteRepository;

    @Mock
    private CreditNoteRepository creditNoteRepositoryMock;

    @Autowired
    private CreditNoteMapper creditNoteMapper;

    @Mock
    private CreditNoteService creditNoteServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CreditNoteSearchRepositoryMockConfiguration
     */
    @Autowired
    private CreditNoteSearchRepository mockCreditNoteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCreditNoteMockMvc;

    private CreditNote creditNote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditNote createEntity(EntityManager em) {
        CreditNote creditNote = new CreditNote()
            .creditNumber(DEFAULT_CREDIT_NUMBER)
            .creditNoteDate(DEFAULT_CREDIT_NOTE_DATE)
            .creditAmount(DEFAULT_CREDIT_AMOUNT)
            .remarks(DEFAULT_REMARKS);
        return creditNote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditNote createUpdatedEntity(EntityManager em) {
        CreditNote creditNote = new CreditNote()
            .creditNumber(UPDATED_CREDIT_NUMBER)
            .creditNoteDate(UPDATED_CREDIT_NOTE_DATE)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .remarks(UPDATED_REMARKS);
        return creditNote;
    }

    @BeforeEach
    public void initTest() {
        creditNote = createEntity(em);
    }

    @Test
    @Transactional
    void createCreditNote() throws Exception {
        int databaseSizeBeforeCreate = creditNoteRepository.findAll().size();
        // Create the CreditNote
        CreditNoteDTO creditNoteDTO = creditNoteMapper.toDto(creditNote);
        restCreditNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditNoteDTO)))
            .andExpect(status().isCreated());

        // Validate the CreditNote in the database
        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeCreate + 1);
        CreditNote testCreditNote = creditNoteList.get(creditNoteList.size() - 1);
        assertThat(testCreditNote.getCreditNumber()).isEqualTo(DEFAULT_CREDIT_NUMBER);
        assertThat(testCreditNote.getCreditNoteDate()).isEqualTo(DEFAULT_CREDIT_NOTE_DATE);
        assertThat(testCreditNote.getCreditAmount()).isEqualByComparingTo(DEFAULT_CREDIT_AMOUNT);
        assertThat(testCreditNote.getRemarks()).isEqualTo(DEFAULT_REMARKS);

        // Validate the CreditNote in Elasticsearch
        verify(mockCreditNoteSearchRepository, times(1)).save(testCreditNote);
    }

    @Test
    @Transactional
    void createCreditNoteWithExistingId() throws Exception {
        // Create the CreditNote with an existing ID
        creditNote.setId(1L);
        CreditNoteDTO creditNoteDTO = creditNoteMapper.toDto(creditNote);

        int databaseSizeBeforeCreate = creditNoteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditNoteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CreditNote in the database
        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeCreate);

        // Validate the CreditNote in Elasticsearch
        verify(mockCreditNoteSearchRepository, times(0)).save(creditNote);
    }

    @Test
    @Transactional
    void checkCreditNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditNoteRepository.findAll().size();
        // set the field null
        creditNote.setCreditNumber(null);

        // Create the CreditNote, which fails.
        CreditNoteDTO creditNoteDTO = creditNoteMapper.toDto(creditNote);

        restCreditNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditNoteDTO)))
            .andExpect(status().isBadRequest());

        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreditNoteDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditNoteRepository.findAll().size();
        // set the field null
        creditNote.setCreditNoteDate(null);

        // Create the CreditNote, which fails.
        CreditNoteDTO creditNoteDTO = creditNoteMapper.toDto(creditNote);

        restCreditNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditNoteDTO)))
            .andExpect(status().isBadRequest());

        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreditAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditNoteRepository.findAll().size();
        // set the field null
        creditNote.setCreditAmount(null);

        // Create the CreditNote, which fails.
        CreditNoteDTO creditNoteDTO = creditNoteMapper.toDto(creditNote);

        restCreditNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditNoteDTO)))
            .andExpect(status().isBadRequest());

        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCreditNotes() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList
        restCreditNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditNumber").value(hasItem(DEFAULT_CREDIT_NUMBER)))
            .andExpect(jsonPath("$.[*].creditNoteDate").value(hasItem(DEFAULT_CREDIT_NOTE_DATE.toString())))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(sameNumber(DEFAULT_CREDIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCreditNotesWithEagerRelationshipsIsEnabled() throws Exception {
        when(creditNoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCreditNoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(creditNoteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCreditNotesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(creditNoteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCreditNoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(creditNoteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCreditNote() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get the creditNote
        restCreditNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, creditNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(creditNote.getId().intValue()))
            .andExpect(jsonPath("$.creditNumber").value(DEFAULT_CREDIT_NUMBER))
            .andExpect(jsonPath("$.creditNoteDate").value(DEFAULT_CREDIT_NOTE_DATE.toString()))
            .andExpect(jsonPath("$.creditAmount").value(sameNumber(DEFAULT_CREDIT_AMOUNT)))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS.toString()));
    }

    @Test
    @Transactional
    void getCreditNotesByIdFiltering() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        Long id = creditNote.getId();

        defaultCreditNoteShouldBeFound("id.equals=" + id);
        defaultCreditNoteShouldNotBeFound("id.notEquals=" + id);

        defaultCreditNoteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCreditNoteShouldNotBeFound("id.greaterThan=" + id);

        defaultCreditNoteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCreditNoteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNumber equals to DEFAULT_CREDIT_NUMBER
        defaultCreditNoteShouldBeFound("creditNumber.equals=" + DEFAULT_CREDIT_NUMBER);

        // Get all the creditNoteList where creditNumber equals to UPDATED_CREDIT_NUMBER
        defaultCreditNoteShouldNotBeFound("creditNumber.equals=" + UPDATED_CREDIT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNumber not equals to DEFAULT_CREDIT_NUMBER
        defaultCreditNoteShouldNotBeFound("creditNumber.notEquals=" + DEFAULT_CREDIT_NUMBER);

        // Get all the creditNoteList where creditNumber not equals to UPDATED_CREDIT_NUMBER
        defaultCreditNoteShouldBeFound("creditNumber.notEquals=" + UPDATED_CREDIT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNumberIsInShouldWork() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNumber in DEFAULT_CREDIT_NUMBER or UPDATED_CREDIT_NUMBER
        defaultCreditNoteShouldBeFound("creditNumber.in=" + DEFAULT_CREDIT_NUMBER + "," + UPDATED_CREDIT_NUMBER);

        // Get all the creditNoteList where creditNumber equals to UPDATED_CREDIT_NUMBER
        defaultCreditNoteShouldNotBeFound("creditNumber.in=" + UPDATED_CREDIT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNumber is not null
        defaultCreditNoteShouldBeFound("creditNumber.specified=true");

        // Get all the creditNoteList where creditNumber is null
        defaultCreditNoteShouldNotBeFound("creditNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNumberContainsSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNumber contains DEFAULT_CREDIT_NUMBER
        defaultCreditNoteShouldBeFound("creditNumber.contains=" + DEFAULT_CREDIT_NUMBER);

        // Get all the creditNoteList where creditNumber contains UPDATED_CREDIT_NUMBER
        defaultCreditNoteShouldNotBeFound("creditNumber.contains=" + UPDATED_CREDIT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNumberNotContainsSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNumber does not contain DEFAULT_CREDIT_NUMBER
        defaultCreditNoteShouldNotBeFound("creditNumber.doesNotContain=" + DEFAULT_CREDIT_NUMBER);

        // Get all the creditNoteList where creditNumber does not contain UPDATED_CREDIT_NUMBER
        defaultCreditNoteShouldBeFound("creditNumber.doesNotContain=" + UPDATED_CREDIT_NUMBER);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNoteDateIsEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNoteDate equals to DEFAULT_CREDIT_NOTE_DATE
        defaultCreditNoteShouldBeFound("creditNoteDate.equals=" + DEFAULT_CREDIT_NOTE_DATE);

        // Get all the creditNoteList where creditNoteDate equals to UPDATED_CREDIT_NOTE_DATE
        defaultCreditNoteShouldNotBeFound("creditNoteDate.equals=" + UPDATED_CREDIT_NOTE_DATE);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNoteDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNoteDate not equals to DEFAULT_CREDIT_NOTE_DATE
        defaultCreditNoteShouldNotBeFound("creditNoteDate.notEquals=" + DEFAULT_CREDIT_NOTE_DATE);

        // Get all the creditNoteList where creditNoteDate not equals to UPDATED_CREDIT_NOTE_DATE
        defaultCreditNoteShouldBeFound("creditNoteDate.notEquals=" + UPDATED_CREDIT_NOTE_DATE);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNoteDateIsInShouldWork() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNoteDate in DEFAULT_CREDIT_NOTE_DATE or UPDATED_CREDIT_NOTE_DATE
        defaultCreditNoteShouldBeFound("creditNoteDate.in=" + DEFAULT_CREDIT_NOTE_DATE + "," + UPDATED_CREDIT_NOTE_DATE);

        // Get all the creditNoteList where creditNoteDate equals to UPDATED_CREDIT_NOTE_DATE
        defaultCreditNoteShouldNotBeFound("creditNoteDate.in=" + UPDATED_CREDIT_NOTE_DATE);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNoteDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNoteDate is not null
        defaultCreditNoteShouldBeFound("creditNoteDate.specified=true");

        // Get all the creditNoteList where creditNoteDate is null
        defaultCreditNoteShouldNotBeFound("creditNoteDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNoteDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNoteDate is greater than or equal to DEFAULT_CREDIT_NOTE_DATE
        defaultCreditNoteShouldBeFound("creditNoteDate.greaterThanOrEqual=" + DEFAULT_CREDIT_NOTE_DATE);

        // Get all the creditNoteList where creditNoteDate is greater than or equal to UPDATED_CREDIT_NOTE_DATE
        defaultCreditNoteShouldNotBeFound("creditNoteDate.greaterThanOrEqual=" + UPDATED_CREDIT_NOTE_DATE);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNoteDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNoteDate is less than or equal to DEFAULT_CREDIT_NOTE_DATE
        defaultCreditNoteShouldBeFound("creditNoteDate.lessThanOrEqual=" + DEFAULT_CREDIT_NOTE_DATE);

        // Get all the creditNoteList where creditNoteDate is less than or equal to SMALLER_CREDIT_NOTE_DATE
        defaultCreditNoteShouldNotBeFound("creditNoteDate.lessThanOrEqual=" + SMALLER_CREDIT_NOTE_DATE);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNoteDateIsLessThanSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNoteDate is less than DEFAULT_CREDIT_NOTE_DATE
        defaultCreditNoteShouldNotBeFound("creditNoteDate.lessThan=" + DEFAULT_CREDIT_NOTE_DATE);

        // Get all the creditNoteList where creditNoteDate is less than UPDATED_CREDIT_NOTE_DATE
        defaultCreditNoteShouldBeFound("creditNoteDate.lessThan=" + UPDATED_CREDIT_NOTE_DATE);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditNoteDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditNoteDate is greater than DEFAULT_CREDIT_NOTE_DATE
        defaultCreditNoteShouldNotBeFound("creditNoteDate.greaterThan=" + DEFAULT_CREDIT_NOTE_DATE);

        // Get all the creditNoteList where creditNoteDate is greater than SMALLER_CREDIT_NOTE_DATE
        defaultCreditNoteShouldBeFound("creditNoteDate.greaterThan=" + SMALLER_CREDIT_NOTE_DATE);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditAmount equals to DEFAULT_CREDIT_AMOUNT
        defaultCreditNoteShouldBeFound("creditAmount.equals=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the creditNoteList where creditAmount equals to UPDATED_CREDIT_AMOUNT
        defaultCreditNoteShouldNotBeFound("creditAmount.equals=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditAmount not equals to DEFAULT_CREDIT_AMOUNT
        defaultCreditNoteShouldNotBeFound("creditAmount.notEquals=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the creditNoteList where creditAmount not equals to UPDATED_CREDIT_AMOUNT
        defaultCreditNoteShouldBeFound("creditAmount.notEquals=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditAmountIsInShouldWork() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditAmount in DEFAULT_CREDIT_AMOUNT or UPDATED_CREDIT_AMOUNT
        defaultCreditNoteShouldBeFound("creditAmount.in=" + DEFAULT_CREDIT_AMOUNT + "," + UPDATED_CREDIT_AMOUNT);

        // Get all the creditNoteList where creditAmount equals to UPDATED_CREDIT_AMOUNT
        defaultCreditNoteShouldNotBeFound("creditAmount.in=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditAmount is not null
        defaultCreditNoteShouldBeFound("creditAmount.specified=true");

        // Get all the creditNoteList where creditAmount is null
        defaultCreditNoteShouldNotBeFound("creditAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditAmount is greater than or equal to DEFAULT_CREDIT_AMOUNT
        defaultCreditNoteShouldBeFound("creditAmount.greaterThanOrEqual=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the creditNoteList where creditAmount is greater than or equal to UPDATED_CREDIT_AMOUNT
        defaultCreditNoteShouldNotBeFound("creditAmount.greaterThanOrEqual=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditAmount is less than or equal to DEFAULT_CREDIT_AMOUNT
        defaultCreditNoteShouldBeFound("creditAmount.lessThanOrEqual=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the creditNoteList where creditAmount is less than or equal to SMALLER_CREDIT_AMOUNT
        defaultCreditNoteShouldNotBeFound("creditAmount.lessThanOrEqual=" + SMALLER_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditAmount is less than DEFAULT_CREDIT_AMOUNT
        defaultCreditNoteShouldNotBeFound("creditAmount.lessThan=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the creditNoteList where creditAmount is less than UPDATED_CREDIT_AMOUNT
        defaultCreditNoteShouldBeFound("creditAmount.lessThan=" + UPDATED_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCreditNotesByCreditAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        // Get all the creditNoteList where creditAmount is greater than DEFAULT_CREDIT_AMOUNT
        defaultCreditNoteShouldNotBeFound("creditAmount.greaterThan=" + DEFAULT_CREDIT_AMOUNT);

        // Get all the creditNoteList where creditAmount is greater than SMALLER_CREDIT_AMOUNT
        defaultCreditNoteShouldBeFound("creditAmount.greaterThan=" + SMALLER_CREDIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllCreditNotesByPurchaseOrdersIsEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);
        PurchaseOrder purchaseOrders;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrders = PurchaseOrderResourceIT.createEntity(em);
            em.persist(purchaseOrders);
            em.flush();
        } else {
            purchaseOrders = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        em.persist(purchaseOrders);
        em.flush();
        creditNote.addPurchaseOrders(purchaseOrders);
        creditNoteRepository.saveAndFlush(creditNote);
        Long purchaseOrdersId = purchaseOrders.getId();

        // Get all the creditNoteList where purchaseOrders equals to purchaseOrdersId
        defaultCreditNoteShouldBeFound("purchaseOrdersId.equals=" + purchaseOrdersId);

        // Get all the creditNoteList where purchaseOrders equals to (purchaseOrdersId + 1)
        defaultCreditNoteShouldNotBeFound("purchaseOrdersId.equals=" + (purchaseOrdersId + 1));
    }

    @Test
    @Transactional
    void getAllCreditNotesByInvoicesIsEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);
        PaymentInvoice invoices;
        if (TestUtil.findAll(em, PaymentInvoice.class).isEmpty()) {
            invoices = PaymentInvoiceResourceIT.createEntity(em);
            em.persist(invoices);
            em.flush();
        } else {
            invoices = TestUtil.findAll(em, PaymentInvoice.class).get(0);
        }
        em.persist(invoices);
        em.flush();
        creditNote.addInvoices(invoices);
        creditNoteRepository.saveAndFlush(creditNote);
        Long invoicesId = invoices.getId();

        // Get all the creditNoteList where invoices equals to invoicesId
        defaultCreditNoteShouldBeFound("invoicesId.equals=" + invoicesId);

        // Get all the creditNoteList where invoices equals to (invoicesId + 1)
        defaultCreditNoteShouldNotBeFound("invoicesId.equals=" + (invoicesId + 1));
    }

    @Test
    @Transactional
    void getAllCreditNotesByPaymentLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);
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
        creditNote.addPaymentLabel(paymentLabel);
        creditNoteRepository.saveAndFlush(creditNote);
        Long paymentLabelId = paymentLabel.getId();

        // Get all the creditNoteList where paymentLabel equals to paymentLabelId
        defaultCreditNoteShouldBeFound("paymentLabelId.equals=" + paymentLabelId);

        // Get all the creditNoteList where paymentLabel equals to (paymentLabelId + 1)
        defaultCreditNoteShouldNotBeFound("paymentLabelId.equals=" + (paymentLabelId + 1));
    }

    @Test
    @Transactional
    void getAllCreditNotesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);
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
        creditNote.addPlaceholder(placeholder);
        creditNoteRepository.saveAndFlush(creditNote);
        Long placeholderId = placeholder.getId();

        // Get all the creditNoteList where placeholder equals to placeholderId
        defaultCreditNoteShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the creditNoteList where placeholder equals to (placeholderId + 1)
        defaultCreditNoteShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllCreditNotesBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);
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
        creditNote.setSettlementCurrency(settlementCurrency);
        creditNoteRepository.saveAndFlush(creditNote);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the creditNoteList where settlementCurrency equals to settlementCurrencyId
        defaultCreditNoteShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the creditNoteList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultCreditNoteShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCreditNoteShouldBeFound(String filter) throws Exception {
        restCreditNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditNumber").value(hasItem(DEFAULT_CREDIT_NUMBER)))
            .andExpect(jsonPath("$.[*].creditNoteDate").value(hasItem(DEFAULT_CREDIT_NOTE_DATE.toString())))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(sameNumber(DEFAULT_CREDIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));

        // Check, that the count call also returns 1
        restCreditNoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCreditNoteShouldNotBeFound(String filter) throws Exception {
        restCreditNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCreditNoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCreditNote() throws Exception {
        // Get the creditNote
        restCreditNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCreditNote() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        int databaseSizeBeforeUpdate = creditNoteRepository.findAll().size();

        // Update the creditNote
        CreditNote updatedCreditNote = creditNoteRepository.findById(creditNote.getId()).get();
        // Disconnect from session so that the updates on updatedCreditNote are not directly saved in db
        em.detach(updatedCreditNote);
        updatedCreditNote
            .creditNumber(UPDATED_CREDIT_NUMBER)
            .creditNoteDate(UPDATED_CREDIT_NOTE_DATE)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .remarks(UPDATED_REMARKS);
        CreditNoteDTO creditNoteDTO = creditNoteMapper.toDto(updatedCreditNote);

        restCreditNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creditNoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditNoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the CreditNote in the database
        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeUpdate);
        CreditNote testCreditNote = creditNoteList.get(creditNoteList.size() - 1);
        assertThat(testCreditNote.getCreditNumber()).isEqualTo(UPDATED_CREDIT_NUMBER);
        assertThat(testCreditNote.getCreditNoteDate()).isEqualTo(UPDATED_CREDIT_NOTE_DATE);
        assertThat(testCreditNote.getCreditAmount()).isEqualTo(UPDATED_CREDIT_AMOUNT);
        assertThat(testCreditNote.getRemarks()).isEqualTo(UPDATED_REMARKS);

        // Validate the CreditNote in Elasticsearch
        verify(mockCreditNoteSearchRepository).save(testCreditNote);
    }

    @Test
    @Transactional
    void putNonExistingCreditNote() throws Exception {
        int databaseSizeBeforeUpdate = creditNoteRepository.findAll().size();
        creditNote.setId(count.incrementAndGet());

        // Create the CreditNote
        CreditNoteDTO creditNoteDTO = creditNoteMapper.toDto(creditNote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creditNoteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditNote in the database
        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditNote in Elasticsearch
        verify(mockCreditNoteSearchRepository, times(0)).save(creditNote);
    }

    @Test
    @Transactional
    void putWithIdMismatchCreditNote() throws Exception {
        int databaseSizeBeforeUpdate = creditNoteRepository.findAll().size();
        creditNote.setId(count.incrementAndGet());

        // Create the CreditNote
        CreditNoteDTO creditNoteDTO = creditNoteMapper.toDto(creditNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditNote in the database
        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditNote in Elasticsearch
        verify(mockCreditNoteSearchRepository, times(0)).save(creditNote);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCreditNote() throws Exception {
        int databaseSizeBeforeUpdate = creditNoteRepository.findAll().size();
        creditNote.setId(count.incrementAndGet());

        // Create the CreditNote
        CreditNoteDTO creditNoteDTO = creditNoteMapper.toDto(creditNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditNoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditNoteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreditNote in the database
        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditNote in Elasticsearch
        verify(mockCreditNoteSearchRepository, times(0)).save(creditNote);
    }

    @Test
    @Transactional
    void partialUpdateCreditNoteWithPatch() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        int databaseSizeBeforeUpdate = creditNoteRepository.findAll().size();

        // Update the creditNote using partial update
        CreditNote partialUpdatedCreditNote = new CreditNote();
        partialUpdatedCreditNote.setId(creditNote.getId());

        partialUpdatedCreditNote.creditNumber(UPDATED_CREDIT_NUMBER).creditNoteDate(UPDATED_CREDIT_NOTE_DATE).remarks(UPDATED_REMARKS);

        restCreditNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreditNote))
            )
            .andExpect(status().isOk());

        // Validate the CreditNote in the database
        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeUpdate);
        CreditNote testCreditNote = creditNoteList.get(creditNoteList.size() - 1);
        assertThat(testCreditNote.getCreditNumber()).isEqualTo(UPDATED_CREDIT_NUMBER);
        assertThat(testCreditNote.getCreditNoteDate()).isEqualTo(UPDATED_CREDIT_NOTE_DATE);
        assertThat(testCreditNote.getCreditAmount()).isEqualByComparingTo(DEFAULT_CREDIT_AMOUNT);
        assertThat(testCreditNote.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateCreditNoteWithPatch() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        int databaseSizeBeforeUpdate = creditNoteRepository.findAll().size();

        // Update the creditNote using partial update
        CreditNote partialUpdatedCreditNote = new CreditNote();
        partialUpdatedCreditNote.setId(creditNote.getId());

        partialUpdatedCreditNote
            .creditNumber(UPDATED_CREDIT_NUMBER)
            .creditNoteDate(UPDATED_CREDIT_NOTE_DATE)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .remarks(UPDATED_REMARKS);

        restCreditNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreditNote))
            )
            .andExpect(status().isOk());

        // Validate the CreditNote in the database
        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeUpdate);
        CreditNote testCreditNote = creditNoteList.get(creditNoteList.size() - 1);
        assertThat(testCreditNote.getCreditNumber()).isEqualTo(UPDATED_CREDIT_NUMBER);
        assertThat(testCreditNote.getCreditNoteDate()).isEqualTo(UPDATED_CREDIT_NOTE_DATE);
        assertThat(testCreditNote.getCreditAmount()).isEqualByComparingTo(UPDATED_CREDIT_AMOUNT);
        assertThat(testCreditNote.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingCreditNote() throws Exception {
        int databaseSizeBeforeUpdate = creditNoteRepository.findAll().size();
        creditNote.setId(count.incrementAndGet());

        // Create the CreditNote
        CreditNoteDTO creditNoteDTO = creditNoteMapper.toDto(creditNote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, creditNoteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditNote in the database
        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditNote in Elasticsearch
        verify(mockCreditNoteSearchRepository, times(0)).save(creditNote);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCreditNote() throws Exception {
        int databaseSizeBeforeUpdate = creditNoteRepository.findAll().size();
        creditNote.setId(count.incrementAndGet());

        // Create the CreditNote
        CreditNoteDTO creditNoteDTO = creditNoteMapper.toDto(creditNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditNoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditNote in the database
        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditNote in Elasticsearch
        verify(mockCreditNoteSearchRepository, times(0)).save(creditNote);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCreditNote() throws Exception {
        int databaseSizeBeforeUpdate = creditNoteRepository.findAll().size();
        creditNote.setId(count.incrementAndGet());

        // Create the CreditNote
        CreditNoteDTO creditNoteDTO = creditNoteMapper.toDto(creditNote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditNoteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(creditNoteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreditNote in the database
        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditNote in Elasticsearch
        verify(mockCreditNoteSearchRepository, times(0)).save(creditNote);
    }

    @Test
    @Transactional
    void deleteCreditNote() throws Exception {
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);

        int databaseSizeBeforeDelete = creditNoteRepository.findAll().size();

        // Delete the creditNote
        restCreditNoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, creditNote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CreditNote> creditNoteList = creditNoteRepository.findAll();
        assertThat(creditNoteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CreditNote in Elasticsearch
        verify(mockCreditNoteSearchRepository, times(1)).deleteById(creditNote.getId());
    }

    @Test
    @Transactional
    void searchCreditNote() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        creditNoteRepository.saveAndFlush(creditNote);
        when(mockCreditNoteSearchRepository.search("id:" + creditNote.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(creditNote), PageRequest.of(0, 1), 1));

        // Search the creditNote
        restCreditNoteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + creditNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditNumber").value(hasItem(DEFAULT_CREDIT_NUMBER)))
            .andExpect(jsonPath("$.[*].creditNoteDate").value(hasItem(DEFAULT_CREDIT_NOTE_DATE.toString())))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(sameNumber(DEFAULT_CREDIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }
}
