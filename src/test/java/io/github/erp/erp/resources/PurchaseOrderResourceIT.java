package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 27 (Baruch Series) Server ver 0.0.7-SNAPSHOT
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
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.PurchaseOrder;
import io.github.erp.domain.SettlementCurrency;
import io.github.erp.repository.PurchaseOrderRepository;
import io.github.erp.repository.search.PurchaseOrderSearchRepository;
import io.github.erp.service.PurchaseOrderService;
import io.github.erp.service.dto.PurchaseOrderDTO;
import io.github.erp.service.mapper.PurchaseOrderMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PurchaseOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"PAYMENTS_USER", "FIXED_ASSETS_USER"})
class PurchaseOrderResourceIT {

    private static final String DEFAULT_PURCHASE_ORDER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PURCHASE_ORDER_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PURCHASE_ORDER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PURCHASE_ORDER_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PURCHASE_ORDER_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_PURCHASE_ORDER_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PURCHASE_ORDER_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PURCHASE_ORDER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments/purchase-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/payments/_search/purchase-orders";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private PurchaseOrderRepository purchaseOrderRepositoryMock;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Mock
    private PurchaseOrderService purchaseOrderServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PurchaseOrderSearchRepositoryMockConfiguration
     */
    @Autowired
    private PurchaseOrderSearchRepository mockPurchaseOrderSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseOrderMockMvc;

    private PurchaseOrder purchaseOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrder createEntity(EntityManager em) {
        PurchaseOrder purchaseOrder = new PurchaseOrder()
            .purchaseOrderNumber(DEFAULT_PURCHASE_ORDER_NUMBER)
            .purchaseOrderDate(DEFAULT_PURCHASE_ORDER_DATE)
            .purchaseOrderAmount(DEFAULT_PURCHASE_ORDER_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .notes(DEFAULT_NOTES)
            .fileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN)
            .compilationToken(DEFAULT_COMPILATION_TOKEN);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        purchaseOrder.setVendor(dealer);
        return purchaseOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrder createUpdatedEntity(EntityManager em) {
        PurchaseOrder purchaseOrder = new PurchaseOrder()
            .purchaseOrderNumber(UPDATED_PURCHASE_ORDER_NUMBER)
            .purchaseOrderDate(UPDATED_PURCHASE_ORDER_DATE)
            .purchaseOrderAmount(UPDATED_PURCHASE_ORDER_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        purchaseOrder.setVendor(dealer);
        return purchaseOrder;
    }

    @BeforeEach
    public void initTest() {
        purchaseOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createPurchaseOrder() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderRepository.findAll().size();
        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);
        restPurchaseOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getPurchaseOrderNumber()).isEqualTo(DEFAULT_PURCHASE_ORDER_NUMBER);
        assertThat(testPurchaseOrder.getPurchaseOrderDate()).isEqualTo(DEFAULT_PURCHASE_ORDER_DATE);
        assertThat(testPurchaseOrder.getPurchaseOrderAmount()).isEqualByComparingTo(DEFAULT_PURCHASE_ORDER_AMOUNT);
        assertThat(testPurchaseOrder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPurchaseOrder.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testPurchaseOrder.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testPurchaseOrder.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);

        // Validate the PurchaseOrder in Elasticsearch
        verify(mockPurchaseOrderSearchRepository, times(1)).save(testPurchaseOrder);
    }

    @Test
    @Transactional
    void createPurchaseOrderWithExistingId() throws Exception {
        // Create the PurchaseOrder with an existing ID
        purchaseOrder.setId(1L);
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        int databaseSizeBeforeCreate = purchaseOrderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeCreate);

        // Validate the PurchaseOrder in Elasticsearch
        verify(mockPurchaseOrderSearchRepository, times(0)).save(purchaseOrder);
    }

    @Test
    @Transactional
    void checkPurchaseOrderNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrderRepository.findAll().size();
        // set the field null
        purchaseOrder.setPurchaseOrderNumber(null);

        // Create the PurchaseOrder, which fails.
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        restPurchaseOrderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPurchaseOrders() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].purchaseOrderNumber").value(hasItem(DEFAULT_PURCHASE_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].purchaseOrderDate").value(hasItem(DEFAULT_PURCHASE_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].purchaseOrderAmount").value(hasItem(sameNumber(DEFAULT_PURCHASE_ORDER_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchaseOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(purchaseOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchaseOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(purchaseOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchaseOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(purchaseOrderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPurchaseOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(purchaseOrderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get the purchaseOrder
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrder.getId().intValue()))
            .andExpect(jsonPath("$.purchaseOrderNumber").value(DEFAULT_PURCHASE_ORDER_NUMBER))
            .andExpect(jsonPath("$.purchaseOrderDate").value(DEFAULT_PURCHASE_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.purchaseOrderAmount").value(sameNumber(DEFAULT_PURCHASE_ORDER_AMOUNT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN));
    }

    @Test
    @Transactional
    void getPurchaseOrdersByIdFiltering() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        Long id = purchaseOrder.getId();

        defaultPurchaseOrderShouldBeFound("id.equals=" + id);
        defaultPurchaseOrderShouldNotBeFound("id.notEquals=" + id);

        defaultPurchaseOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPurchaseOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultPurchaseOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPurchaseOrderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderNumber equals to DEFAULT_PURCHASE_ORDER_NUMBER
        defaultPurchaseOrderShouldBeFound("purchaseOrderNumber.equals=" + DEFAULT_PURCHASE_ORDER_NUMBER);

        // Get all the purchaseOrderList where purchaseOrderNumber equals to UPDATED_PURCHASE_ORDER_NUMBER
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderNumber.equals=" + UPDATED_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderNumber not equals to DEFAULT_PURCHASE_ORDER_NUMBER
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderNumber.notEquals=" + DEFAULT_PURCHASE_ORDER_NUMBER);

        // Get all the purchaseOrderList where purchaseOrderNumber not equals to UPDATED_PURCHASE_ORDER_NUMBER
        defaultPurchaseOrderShouldBeFound("purchaseOrderNumber.notEquals=" + UPDATED_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderNumberIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderNumber in DEFAULT_PURCHASE_ORDER_NUMBER or UPDATED_PURCHASE_ORDER_NUMBER
        defaultPurchaseOrderShouldBeFound("purchaseOrderNumber.in=" + DEFAULT_PURCHASE_ORDER_NUMBER + "," + UPDATED_PURCHASE_ORDER_NUMBER);

        // Get all the purchaseOrderList where purchaseOrderNumber equals to UPDATED_PURCHASE_ORDER_NUMBER
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderNumber.in=" + UPDATED_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderNumber is not null
        defaultPurchaseOrderShouldBeFound("purchaseOrderNumber.specified=true");

        // Get all the purchaseOrderList where purchaseOrderNumber is null
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderNumberContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderNumber contains DEFAULT_PURCHASE_ORDER_NUMBER
        defaultPurchaseOrderShouldBeFound("purchaseOrderNumber.contains=" + DEFAULT_PURCHASE_ORDER_NUMBER);

        // Get all the purchaseOrderList where purchaseOrderNumber contains UPDATED_PURCHASE_ORDER_NUMBER
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderNumber.contains=" + UPDATED_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderNumberNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderNumber does not contain DEFAULT_PURCHASE_ORDER_NUMBER
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderNumber.doesNotContain=" + DEFAULT_PURCHASE_ORDER_NUMBER);

        // Get all the purchaseOrderList where purchaseOrderNumber does not contain UPDATED_PURCHASE_ORDER_NUMBER
        defaultPurchaseOrderShouldBeFound("purchaseOrderNumber.doesNotContain=" + UPDATED_PURCHASE_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderDate equals to DEFAULT_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldBeFound("purchaseOrderDate.equals=" + DEFAULT_PURCHASE_ORDER_DATE);

        // Get all the purchaseOrderList where purchaseOrderDate equals to UPDATED_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderDate.equals=" + UPDATED_PURCHASE_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderDate not equals to DEFAULT_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderDate.notEquals=" + DEFAULT_PURCHASE_ORDER_DATE);

        // Get all the purchaseOrderList where purchaseOrderDate not equals to UPDATED_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldBeFound("purchaseOrderDate.notEquals=" + UPDATED_PURCHASE_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderDate in DEFAULT_PURCHASE_ORDER_DATE or UPDATED_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldBeFound("purchaseOrderDate.in=" + DEFAULT_PURCHASE_ORDER_DATE + "," + UPDATED_PURCHASE_ORDER_DATE);

        // Get all the purchaseOrderList where purchaseOrderDate equals to UPDATED_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderDate.in=" + UPDATED_PURCHASE_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderDate is not null
        defaultPurchaseOrderShouldBeFound("purchaseOrderDate.specified=true");

        // Get all the purchaseOrderList where purchaseOrderDate is null
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderDate is greater than or equal to DEFAULT_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldBeFound("purchaseOrderDate.greaterThanOrEqual=" + DEFAULT_PURCHASE_ORDER_DATE);

        // Get all the purchaseOrderList where purchaseOrderDate is greater than or equal to UPDATED_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderDate.greaterThanOrEqual=" + UPDATED_PURCHASE_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderDate is less than or equal to DEFAULT_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldBeFound("purchaseOrderDate.lessThanOrEqual=" + DEFAULT_PURCHASE_ORDER_DATE);

        // Get all the purchaseOrderList where purchaseOrderDate is less than or equal to SMALLER_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderDate.lessThanOrEqual=" + SMALLER_PURCHASE_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderDateIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderDate is less than DEFAULT_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderDate.lessThan=" + DEFAULT_PURCHASE_ORDER_DATE);

        // Get all the purchaseOrderList where purchaseOrderDate is less than UPDATED_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldBeFound("purchaseOrderDate.lessThan=" + UPDATED_PURCHASE_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderDate is greater than DEFAULT_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderDate.greaterThan=" + DEFAULT_PURCHASE_ORDER_DATE);

        // Get all the purchaseOrderList where purchaseOrderDate is greater than SMALLER_PURCHASE_ORDER_DATE
        defaultPurchaseOrderShouldBeFound("purchaseOrderDate.greaterThan=" + SMALLER_PURCHASE_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderAmount equals to DEFAULT_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldBeFound("purchaseOrderAmount.equals=" + DEFAULT_PURCHASE_ORDER_AMOUNT);

        // Get all the purchaseOrderList where purchaseOrderAmount equals to UPDATED_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderAmount.equals=" + UPDATED_PURCHASE_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderAmount not equals to DEFAULT_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderAmount.notEquals=" + DEFAULT_PURCHASE_ORDER_AMOUNT);

        // Get all the purchaseOrderList where purchaseOrderAmount not equals to UPDATED_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldBeFound("purchaseOrderAmount.notEquals=" + UPDATED_PURCHASE_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderAmountIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderAmount in DEFAULT_PURCHASE_ORDER_AMOUNT or UPDATED_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldBeFound("purchaseOrderAmount.in=" + DEFAULT_PURCHASE_ORDER_AMOUNT + "," + UPDATED_PURCHASE_ORDER_AMOUNT);

        // Get all the purchaseOrderList where purchaseOrderAmount equals to UPDATED_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderAmount.in=" + UPDATED_PURCHASE_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderAmount is not null
        defaultPurchaseOrderShouldBeFound("purchaseOrderAmount.specified=true");

        // Get all the purchaseOrderList where purchaseOrderAmount is null
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderAmount is greater than or equal to DEFAULT_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldBeFound("purchaseOrderAmount.greaterThanOrEqual=" + DEFAULT_PURCHASE_ORDER_AMOUNT);

        // Get all the purchaseOrderList where purchaseOrderAmount is greater than or equal to UPDATED_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderAmount.greaterThanOrEqual=" + UPDATED_PURCHASE_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderAmount is less than or equal to DEFAULT_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldBeFound("purchaseOrderAmount.lessThanOrEqual=" + DEFAULT_PURCHASE_ORDER_AMOUNT);

        // Get all the purchaseOrderList where purchaseOrderAmount is less than or equal to SMALLER_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderAmount.lessThanOrEqual=" + SMALLER_PURCHASE_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderAmount is less than DEFAULT_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderAmount.lessThan=" + DEFAULT_PURCHASE_ORDER_AMOUNT);

        // Get all the purchaseOrderList where purchaseOrderAmount is less than UPDATED_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldBeFound("purchaseOrderAmount.lessThan=" + UPDATED_PURCHASE_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPurchaseOrderAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where purchaseOrderAmount is greater than DEFAULT_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldNotBeFound("purchaseOrderAmount.greaterThan=" + DEFAULT_PURCHASE_ORDER_AMOUNT);

        // Get all the purchaseOrderList where purchaseOrderAmount is greater than SMALLER_PURCHASE_ORDER_AMOUNT
        defaultPurchaseOrderShouldBeFound("purchaseOrderAmount.greaterThan=" + SMALLER_PURCHASE_ORDER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where description equals to DEFAULT_DESCRIPTION
        defaultPurchaseOrderShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the purchaseOrderList where description equals to UPDATED_DESCRIPTION
        defaultPurchaseOrderShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where description not equals to DEFAULT_DESCRIPTION
        defaultPurchaseOrderShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the purchaseOrderList where description not equals to UPDATED_DESCRIPTION
        defaultPurchaseOrderShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPurchaseOrderShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the purchaseOrderList where description equals to UPDATED_DESCRIPTION
        defaultPurchaseOrderShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where description is not null
        defaultPurchaseOrderShouldBeFound("description.specified=true");

        // Get all the purchaseOrderList where description is null
        defaultPurchaseOrderShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where description contains DEFAULT_DESCRIPTION
        defaultPurchaseOrderShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the purchaseOrderList where description contains UPDATED_DESCRIPTION
        defaultPurchaseOrderShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where description does not contain DEFAULT_DESCRIPTION
        defaultPurchaseOrderShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the purchaseOrderList where description does not contain UPDATED_DESCRIPTION
        defaultPurchaseOrderShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where notes equals to DEFAULT_NOTES
        defaultPurchaseOrderShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the purchaseOrderList where notes equals to UPDATED_NOTES
        defaultPurchaseOrderShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where notes not equals to DEFAULT_NOTES
        defaultPurchaseOrderShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the purchaseOrderList where notes not equals to UPDATED_NOTES
        defaultPurchaseOrderShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultPurchaseOrderShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the purchaseOrderList where notes equals to UPDATED_NOTES
        defaultPurchaseOrderShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where notes is not null
        defaultPurchaseOrderShouldBeFound("notes.specified=true");

        // Get all the purchaseOrderList where notes is null
        defaultPurchaseOrderShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNotesContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where notes contains DEFAULT_NOTES
        defaultPurchaseOrderShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the purchaseOrderList where notes contains UPDATED_NOTES
        defaultPurchaseOrderShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where notes does not contain DEFAULT_NOTES
        defaultPurchaseOrderShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the purchaseOrderList where notes does not contain UPDATED_NOTES
        defaultPurchaseOrderShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultPurchaseOrderShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the purchaseOrderList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultPurchaseOrderShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultPurchaseOrderShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the purchaseOrderList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultPurchaseOrderShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultPurchaseOrderShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the purchaseOrderList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultPurchaseOrderShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where fileUploadToken is not null
        defaultPurchaseOrderShouldBeFound("fileUploadToken.specified=true");

        // Get all the purchaseOrderList where fileUploadToken is null
        defaultPurchaseOrderShouldNotBeFound("fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultPurchaseOrderShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the purchaseOrderList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultPurchaseOrderShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultPurchaseOrderShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the purchaseOrderList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultPurchaseOrderShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultPurchaseOrderShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the purchaseOrderList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultPurchaseOrderShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultPurchaseOrderShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the purchaseOrderList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultPurchaseOrderShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultPurchaseOrderShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the purchaseOrderList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultPurchaseOrderShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where compilationToken is not null
        defaultPurchaseOrderShouldBeFound("compilationToken.specified=true");

        // Get all the purchaseOrderList where compilationToken is null
        defaultPurchaseOrderShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultPurchaseOrderShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the purchaseOrderList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultPurchaseOrderShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultPurchaseOrderShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the purchaseOrderList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultPurchaseOrderShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
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
        purchaseOrder.setSettlementCurrency(settlementCurrency);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the purchaseOrderList where settlementCurrency equals to settlementCurrencyId
        defaultPurchaseOrderShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the purchaseOrderList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultPurchaseOrderShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
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
        purchaseOrder.addPlaceholder(placeholder);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long placeholderId = placeholder.getId();

        // Get all the purchaseOrderList where placeholder equals to placeholderId
        defaultPurchaseOrderShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the purchaseOrderList where placeholder equals to (placeholderId + 1)
        defaultPurchaseOrderShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersBySignatoriesIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Dealer signatories;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            signatories = DealerResourceIT.createEntity(em);
            em.persist(signatories);
            em.flush();
        } else {
            signatories = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(signatories);
        em.flush();
        purchaseOrder.addSignatories(signatories);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long signatoriesId = signatories.getId();

        // Get all the purchaseOrderList where signatories equals to signatoriesId
        defaultPurchaseOrderShouldBeFound("signatoriesId.equals=" + signatoriesId);

        // Get all the purchaseOrderList where signatories equals to (signatoriesId + 1)
        defaultPurchaseOrderShouldNotBeFound("signatoriesId.equals=" + (signatoriesId + 1));
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByVendorIsEqualToSomething() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Dealer vendor;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            vendor = DealerResourceIT.createEntity(em);
            em.persist(vendor);
            em.flush();
        } else {
            vendor = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(vendor);
        em.flush();
        purchaseOrder.setVendor(vendor);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long vendorId = vendor.getId();

        // Get all the purchaseOrderList where vendor equals to vendorId
        defaultPurchaseOrderShouldBeFound("vendorId.equals=" + vendorId);

        // Get all the purchaseOrderList where vendor equals to (vendorId + 1)
        defaultPurchaseOrderShouldNotBeFound("vendorId.equals=" + (vendorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPurchaseOrderShouldBeFound(String filter) throws Exception {
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].purchaseOrderNumber").value(hasItem(DEFAULT_PURCHASE_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].purchaseOrderDate").value(hasItem(DEFAULT_PURCHASE_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].purchaseOrderAmount").value(hasItem(sameNumber(DEFAULT_PURCHASE_ORDER_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));

        // Check, that the count call also returns 1
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPurchaseOrderShouldNotBeFound(String filter) throws Exception {
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseOrder() throws Exception {
        // Get the purchaseOrder
        restPurchaseOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

        // Update the purchaseOrder
        PurchaseOrder updatedPurchaseOrder = purchaseOrderRepository.findById(purchaseOrder.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseOrder are not directly saved in db
        em.detach(updatedPurchaseOrder);
        updatedPurchaseOrder
            .purchaseOrderNumber(UPDATED_PURCHASE_ORDER_NUMBER)
            .purchaseOrderDate(UPDATED_PURCHASE_ORDER_DATE)
            .purchaseOrderAmount(UPDATED_PURCHASE_ORDER_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(updatedPurchaseOrder);

        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getPurchaseOrderNumber()).isEqualTo(UPDATED_PURCHASE_ORDER_NUMBER);
        assertThat(testPurchaseOrder.getPurchaseOrderDate()).isEqualTo(UPDATED_PURCHASE_ORDER_DATE);
        assertThat(testPurchaseOrder.getPurchaseOrderAmount()).isEqualTo(UPDATED_PURCHASE_ORDER_AMOUNT);
        assertThat(testPurchaseOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPurchaseOrder.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPurchaseOrder.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testPurchaseOrder.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);

        // Validate the PurchaseOrder in Elasticsearch
        verify(mockPurchaseOrderSearchRepository).save(testPurchaseOrder);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PurchaseOrder in Elasticsearch
        verify(mockPurchaseOrderSearchRepository, times(0)).save(purchaseOrder);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PurchaseOrder in Elasticsearch
        verify(mockPurchaseOrderSearchRepository, times(0)).save(purchaseOrder);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PurchaseOrder in Elasticsearch
        verify(mockPurchaseOrderSearchRepository, times(0)).save(purchaseOrder);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseOrderWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

        // Update the purchaseOrder using partial update
        PurchaseOrder partialUpdatedPurchaseOrder = new PurchaseOrder();
        partialUpdatedPurchaseOrder.setId(purchaseOrder.getId());

        partialUpdatedPurchaseOrder
            .purchaseOrderAmount(UPDATED_PURCHASE_ORDER_AMOUNT)
            .notes(UPDATED_NOTES)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN);

        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrder))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getPurchaseOrderNumber()).isEqualTo(DEFAULT_PURCHASE_ORDER_NUMBER);
        assertThat(testPurchaseOrder.getPurchaseOrderDate()).isEqualTo(DEFAULT_PURCHASE_ORDER_DATE);
        assertThat(testPurchaseOrder.getPurchaseOrderAmount()).isEqualByComparingTo(UPDATED_PURCHASE_ORDER_AMOUNT);
        assertThat(testPurchaseOrder.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPurchaseOrder.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPurchaseOrder.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testPurchaseOrder.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdatePurchaseOrderWithPatch() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();

        // Update the purchaseOrder using partial update
        PurchaseOrder partialUpdatedPurchaseOrder = new PurchaseOrder();
        partialUpdatedPurchaseOrder.setId(purchaseOrder.getId());

        partialUpdatedPurchaseOrder
            .purchaseOrderNumber(UPDATED_PURCHASE_ORDER_NUMBER)
            .purchaseOrderDate(UPDATED_PURCHASE_ORDER_DATE)
            .purchaseOrderAmount(UPDATED_PURCHASE_ORDER_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchaseOrder))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrder testPurchaseOrder = purchaseOrderList.get(purchaseOrderList.size() - 1);
        assertThat(testPurchaseOrder.getPurchaseOrderNumber()).isEqualTo(UPDATED_PURCHASE_ORDER_NUMBER);
        assertThat(testPurchaseOrder.getPurchaseOrderDate()).isEqualTo(UPDATED_PURCHASE_ORDER_DATE);
        assertThat(testPurchaseOrder.getPurchaseOrderAmount()).isEqualByComparingTo(UPDATED_PURCHASE_ORDER_AMOUNT);
        assertThat(testPurchaseOrder.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPurchaseOrder.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testPurchaseOrder.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testPurchaseOrder.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseOrderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PurchaseOrder in Elasticsearch
        verify(mockPurchaseOrderSearchRepository, times(0)).save(purchaseOrder);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PurchaseOrder in Elasticsearch
        verify(mockPurchaseOrderSearchRepository, times(0)).save(purchaseOrder);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseOrder() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderRepository.findAll().size();
        purchaseOrder.setId(count.incrementAndGet());

        // Create the PurchaseOrder
        PurchaseOrderDTO purchaseOrderDTO = purchaseOrderMapper.toDto(purchaseOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(purchaseOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrder in the database
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PurchaseOrder in Elasticsearch
        verify(mockPurchaseOrderSearchRepository, times(0)).save(purchaseOrder);
    }

    @Test
    @Transactional
    void deletePurchaseOrder() throws Exception {
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);

        int databaseSizeBeforeDelete = purchaseOrderRepository.findAll().size();

        // Delete the purchaseOrder
        restPurchaseOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findAll();
        assertThat(purchaseOrderList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PurchaseOrder in Elasticsearch
        verify(mockPurchaseOrderSearchRepository, times(1)).deleteById(purchaseOrder.getId());
    }

    @Test
    @Transactional
    void searchPurchaseOrder() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        when(mockPurchaseOrderSearchRepository.search("id:" + purchaseOrder.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(purchaseOrder), PageRequest.of(0, 1), 1));

        // Search the purchaseOrder
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + purchaseOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].purchaseOrderNumber").value(hasItem(DEFAULT_PURCHASE_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].purchaseOrderDate").value(hasItem(DEFAULT_PURCHASE_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].purchaseOrderAmount").value(hasItem(sameNumber(DEFAULT_PURCHASE_ORDER_AMOUNT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }
}
