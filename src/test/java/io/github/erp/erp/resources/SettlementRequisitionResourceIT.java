package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.6
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
import io.github.erp.domain.*;
import io.github.erp.domain.enumeration.PaymentStatus;
import io.github.erp.repository.SettlementRequisitionRepository;
import io.github.erp.repository.search.SettlementRequisitionSearchRepository;
import io.github.erp.service.SettlementRequisitionService;
import io.github.erp.service.dto.SettlementRequisitionDTO;
import io.github.erp.service.mapper.SettlementRequisitionMapper;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the SettlementRequisitionResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"REQUISITION_MANAGER"})
public class SettlementRequisitionResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final UUID DEFAULT_SERIAL_NUMBER = UUID.randomUUID();
    private static final UUID UPDATED_SERIAL_NUMBER = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_TIME_OF_REQUISITION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_REQUISITION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_REQUISITION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_REQUISITION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REQUISITION_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PAYMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final PaymentStatus DEFAULT_PAYMENT_STATUS = PaymentStatus.PROCESSED;
    private static final PaymentStatus UPDATED_PAYMENT_STATUS = PaymentStatus.IN_PROCESS;

    private static final String ENTITY_API_URL = "/api/requisition/settlement-requisitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/requisition/_search/settlement-requisitions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SettlementRequisitionRepository settlementRequisitionRepository;

    @Mock
    private SettlementRequisitionRepository settlementRequisitionRepositoryMock;

    @Autowired
    private SettlementRequisitionMapper settlementRequisitionMapper;

    @Mock
    private SettlementRequisitionService settlementRequisitionServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SettlementRequisitionSearchRepositoryMockConfiguration
     */
    @Autowired
    private SettlementRequisitionSearchRepository mockSettlementRequisitionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSettlementRequisitionMockMvc;

    private SettlementRequisition settlementRequisition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SettlementRequisition createEntity(EntityManager em) {
        SettlementRequisition settlementRequisition = new SettlementRequisition()
            .description(DEFAULT_DESCRIPTION)
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .timeOfRequisition(DEFAULT_TIME_OF_REQUISITION)
            .requisitionNumber(DEFAULT_REQUISITION_NUMBER)
            .paymentAmount(DEFAULT_PAYMENT_AMOUNT)
            .paymentStatus(DEFAULT_PAYMENT_STATUS);
        // Add required entity
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        settlementRequisition.setSettlementCurrency(settlementCurrency);
        // Add required entity
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            applicationUser = ApplicationUserResourceIT.createEntity(em);
            em.persist(applicationUser);
            em.flush();
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        settlementRequisition.setCurrentOwner(applicationUser);
        // Add required entity
        settlementRequisition.setNativeOwner(applicationUser);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        settlementRequisition.setNativeDepartment(dealer);
        // Add required entity
        settlementRequisition.setBiller(dealer);
        return settlementRequisition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SettlementRequisition createUpdatedEntity(EntityManager em) {
        SettlementRequisition settlementRequisition = new SettlementRequisition()
            .description(UPDATED_DESCRIPTION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .requisitionNumber(UPDATED_REQUISITION_NUMBER)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS);
        // Add required entity
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createUpdatedEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        settlementRequisition.setSettlementCurrency(settlementCurrency);
        // Add required entity
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            applicationUser = ApplicationUserResourceIT.createUpdatedEntity(em);
            em.persist(applicationUser);
            em.flush();
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        settlementRequisition.setCurrentOwner(applicationUser);
        // Add required entity
        settlementRequisition.setNativeOwner(applicationUser);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        settlementRequisition.setNativeDepartment(dealer);
        // Add required entity
        settlementRequisition.setBiller(dealer);
        return settlementRequisition;
    }

    @BeforeEach
    public void initTest() {
        settlementRequisition = createEntity(em);
    }

    @Test
    @Transactional
    void createSettlementRequisition() throws Exception {
        int databaseSizeBeforeCreate = settlementRequisitionRepository.findAll().size();
        // Create the SettlementRequisition
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);
        restSettlementRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SettlementRequisition in the database
        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeCreate + 1);
        SettlementRequisition testSettlementRequisition = settlementRequisitionList.get(settlementRequisitionList.size() - 1);
        assertThat(testSettlementRequisition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSettlementRequisition.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testSettlementRequisition.getTimeOfRequisition()).isEqualTo(DEFAULT_TIME_OF_REQUISITION);
        assertThat(testSettlementRequisition.getRequisitionNumber()).isEqualTo(DEFAULT_REQUISITION_NUMBER);
        assertThat(testSettlementRequisition.getPaymentAmount()).isEqualByComparingTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testSettlementRequisition.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);

        // Validate the SettlementRequisition in Elasticsearch
        verify(mockSettlementRequisitionSearchRepository, times(1)).save(testSettlementRequisition);
    }

    @Test
    @Transactional
    void createSettlementRequisitionWithExistingId() throws Exception {
        // Create the SettlementRequisition with an existing ID
        settlementRequisition.setId(1L);
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);

        int databaseSizeBeforeCreate = settlementRequisitionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSettlementRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettlementRequisition in the database
        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeCreate);

        // Validate the SettlementRequisition in Elasticsearch
        verify(mockSettlementRequisitionSearchRepository, times(0)).save(settlementRequisition);
    }

    @Test
    @Transactional
    void checkSerialNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = settlementRequisitionRepository.findAll().size();
        // set the field null
        settlementRequisition.setSerialNumber(null);

        // Create the SettlementRequisition, which fails.
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);

        restSettlementRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimeOfRequisitionIsRequired() throws Exception {
        int databaseSizeBeforeTest = settlementRequisitionRepository.findAll().size();
        // set the field null
        settlementRequisition.setTimeOfRequisition(null);

        // Create the SettlementRequisition, which fails.
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);

        restSettlementRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequisitionNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = settlementRequisitionRepository.findAll().size();
        // set the field null
        settlementRequisition.setRequisitionNumber(null);

        // Create the SettlementRequisition, which fails.
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);

        restSettlementRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = settlementRequisitionRepository.findAll().size();
        // set the field null
        settlementRequisition.setPaymentAmount(null);

        // Create the SettlementRequisition, which fails.
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);

        restSettlementRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaymentStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = settlementRequisitionRepository.findAll().size();
        // set the field null
        settlementRequisition.setPaymentStatus(null);

        // Create the SettlementRequisition, which fails.
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);

        restSettlementRequisitionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitions() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList
        restSettlementRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settlementRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].requisitionNumber").value(hasItem(DEFAULT_REQUISITION_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSettlementRequisitionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(settlementRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSettlementRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(settlementRequisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSettlementRequisitionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(settlementRequisitionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSettlementRequisitionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(settlementRequisitionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSettlementRequisition() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get the settlementRequisition
        restSettlementRequisitionMockMvc
            .perform(get(ENTITY_API_URL_ID, settlementRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(settlementRequisition.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER.toString()))
            .andExpect(jsonPath("$.timeOfRequisition").value(sameInstant(DEFAULT_TIME_OF_REQUISITION)))
            .andExpect(jsonPath("$.requisitionNumber").value(DEFAULT_REQUISITION_NUMBER))
            .andExpect(jsonPath("$.paymentAmount").value(sameNumber(DEFAULT_PAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.paymentStatus").value(DEFAULT_PAYMENT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getSettlementRequisitionsByIdFiltering() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        Long id = settlementRequisition.getId();

        defaultSettlementRequisitionShouldBeFound("id.equals=" + id);
        defaultSettlementRequisitionShouldNotBeFound("id.notEquals=" + id);

        defaultSettlementRequisitionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSettlementRequisitionShouldNotBeFound("id.greaterThan=" + id);

        defaultSettlementRequisitionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSettlementRequisitionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where description equals to DEFAULT_DESCRIPTION
        defaultSettlementRequisitionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the settlementRequisitionList where description equals to UPDATED_DESCRIPTION
        defaultSettlementRequisitionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where description not equals to DEFAULT_DESCRIPTION
        defaultSettlementRequisitionShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the settlementRequisitionList where description not equals to UPDATED_DESCRIPTION
        defaultSettlementRequisitionShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSettlementRequisitionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the settlementRequisitionList where description equals to UPDATED_DESCRIPTION
        defaultSettlementRequisitionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where description is not null
        defaultSettlementRequisitionShouldBeFound("description.specified=true");

        // Get all the settlementRequisitionList where description is null
        defaultSettlementRequisitionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where description contains DEFAULT_DESCRIPTION
        defaultSettlementRequisitionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the settlementRequisitionList where description contains UPDATED_DESCRIPTION
        defaultSettlementRequisitionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where description does not contain DEFAULT_DESCRIPTION
        defaultSettlementRequisitionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the settlementRequisitionList where description does not contain UPDATED_DESCRIPTION
        defaultSettlementRequisitionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where serialNumber equals to DEFAULT_SERIAL_NUMBER
        defaultSettlementRequisitionShouldBeFound("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the settlementRequisitionList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultSettlementRequisitionShouldNotBeFound("serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsBySerialNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where serialNumber not equals to DEFAULT_SERIAL_NUMBER
        defaultSettlementRequisitionShouldNotBeFound("serialNumber.notEquals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the settlementRequisitionList where serialNumber not equals to UPDATED_SERIAL_NUMBER
        defaultSettlementRequisitionShouldBeFound("serialNumber.notEquals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where serialNumber in DEFAULT_SERIAL_NUMBER or UPDATED_SERIAL_NUMBER
        defaultSettlementRequisitionShouldBeFound("serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER);

        // Get all the settlementRequisitionList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultSettlementRequisitionShouldNotBeFound("serialNumber.in=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where serialNumber is not null
        defaultSettlementRequisitionShouldBeFound("serialNumber.specified=true");

        // Get all the settlementRequisitionList where serialNumber is null
        defaultSettlementRequisitionShouldNotBeFound("serialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByTimeOfRequisitionIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where timeOfRequisition equals to DEFAULT_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldBeFound("timeOfRequisition.equals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the settlementRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldNotBeFound("timeOfRequisition.equals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByTimeOfRequisitionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where timeOfRequisition not equals to DEFAULT_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldNotBeFound("timeOfRequisition.notEquals=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the settlementRequisitionList where timeOfRequisition not equals to UPDATED_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldBeFound("timeOfRequisition.notEquals=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByTimeOfRequisitionIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where timeOfRequisition in DEFAULT_TIME_OF_REQUISITION or UPDATED_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldBeFound(
            "timeOfRequisition.in=" + DEFAULT_TIME_OF_REQUISITION + "," + UPDATED_TIME_OF_REQUISITION
        );

        // Get all the settlementRequisitionList where timeOfRequisition equals to UPDATED_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldNotBeFound("timeOfRequisition.in=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByTimeOfRequisitionIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where timeOfRequisition is not null
        defaultSettlementRequisitionShouldBeFound("timeOfRequisition.specified=true");

        // Get all the settlementRequisitionList where timeOfRequisition is null
        defaultSettlementRequisitionShouldNotBeFound("timeOfRequisition.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByTimeOfRequisitionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where timeOfRequisition is greater than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldBeFound("timeOfRequisition.greaterThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the settlementRequisitionList where timeOfRequisition is greater than or equal to UPDATED_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldNotBeFound("timeOfRequisition.greaterThanOrEqual=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByTimeOfRequisitionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where timeOfRequisition is less than or equal to DEFAULT_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldBeFound("timeOfRequisition.lessThanOrEqual=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the settlementRequisitionList where timeOfRequisition is less than or equal to SMALLER_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldNotBeFound("timeOfRequisition.lessThanOrEqual=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByTimeOfRequisitionIsLessThanSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where timeOfRequisition is less than DEFAULT_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldNotBeFound("timeOfRequisition.lessThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the settlementRequisitionList where timeOfRequisition is less than UPDATED_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldBeFound("timeOfRequisition.lessThan=" + UPDATED_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByTimeOfRequisitionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where timeOfRequisition is greater than DEFAULT_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldNotBeFound("timeOfRequisition.greaterThan=" + DEFAULT_TIME_OF_REQUISITION);

        // Get all the settlementRequisitionList where timeOfRequisition is greater than SMALLER_TIME_OF_REQUISITION
        defaultSettlementRequisitionShouldBeFound("timeOfRequisition.greaterThan=" + SMALLER_TIME_OF_REQUISITION);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByRequisitionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where requisitionNumber equals to DEFAULT_REQUISITION_NUMBER
        defaultSettlementRequisitionShouldBeFound("requisitionNumber.equals=" + DEFAULT_REQUISITION_NUMBER);

        // Get all the settlementRequisitionList where requisitionNumber equals to UPDATED_REQUISITION_NUMBER
        defaultSettlementRequisitionShouldNotBeFound("requisitionNumber.equals=" + UPDATED_REQUISITION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByRequisitionNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where requisitionNumber not equals to DEFAULT_REQUISITION_NUMBER
        defaultSettlementRequisitionShouldNotBeFound("requisitionNumber.notEquals=" + DEFAULT_REQUISITION_NUMBER);

        // Get all the settlementRequisitionList where requisitionNumber not equals to UPDATED_REQUISITION_NUMBER
        defaultSettlementRequisitionShouldBeFound("requisitionNumber.notEquals=" + UPDATED_REQUISITION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByRequisitionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where requisitionNumber in DEFAULT_REQUISITION_NUMBER or UPDATED_REQUISITION_NUMBER
        defaultSettlementRequisitionShouldBeFound("requisitionNumber.in=" + DEFAULT_REQUISITION_NUMBER + "," + UPDATED_REQUISITION_NUMBER);

        // Get all the settlementRequisitionList where requisitionNumber equals to UPDATED_REQUISITION_NUMBER
        defaultSettlementRequisitionShouldNotBeFound("requisitionNumber.in=" + UPDATED_REQUISITION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByRequisitionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where requisitionNumber is not null
        defaultSettlementRequisitionShouldBeFound("requisitionNumber.specified=true");

        // Get all the settlementRequisitionList where requisitionNumber is null
        defaultSettlementRequisitionShouldNotBeFound("requisitionNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByRequisitionNumberContainsSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where requisitionNumber contains DEFAULT_REQUISITION_NUMBER
        defaultSettlementRequisitionShouldBeFound("requisitionNumber.contains=" + DEFAULT_REQUISITION_NUMBER);

        // Get all the settlementRequisitionList where requisitionNumber contains UPDATED_REQUISITION_NUMBER
        defaultSettlementRequisitionShouldNotBeFound("requisitionNumber.contains=" + UPDATED_REQUISITION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByRequisitionNumberNotContainsSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where requisitionNumber does not contain DEFAULT_REQUISITION_NUMBER
        defaultSettlementRequisitionShouldNotBeFound("requisitionNumber.doesNotContain=" + DEFAULT_REQUISITION_NUMBER);

        // Get all the settlementRequisitionList where requisitionNumber does not contain UPDATED_REQUISITION_NUMBER
        defaultSettlementRequisitionShouldBeFound("requisitionNumber.doesNotContain=" + UPDATED_REQUISITION_NUMBER);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where paymentAmount equals to DEFAULT_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldBeFound("paymentAmount.equals=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the settlementRequisitionList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldNotBeFound("paymentAmount.equals=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where paymentAmount not equals to DEFAULT_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldNotBeFound("paymentAmount.notEquals=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the settlementRequisitionList where paymentAmount not equals to UPDATED_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldBeFound("paymentAmount.notEquals=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where paymentAmount in DEFAULT_PAYMENT_AMOUNT or UPDATED_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldBeFound("paymentAmount.in=" + DEFAULT_PAYMENT_AMOUNT + "," + UPDATED_PAYMENT_AMOUNT);

        // Get all the settlementRequisitionList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldNotBeFound("paymentAmount.in=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where paymentAmount is not null
        defaultSettlementRequisitionShouldBeFound("paymentAmount.specified=true");

        // Get all the settlementRequisitionList where paymentAmount is null
        defaultSettlementRequisitionShouldNotBeFound("paymentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where paymentAmount is greater than or equal to DEFAULT_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldBeFound("paymentAmount.greaterThanOrEqual=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the settlementRequisitionList where paymentAmount is greater than or equal to UPDATED_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldNotBeFound("paymentAmount.greaterThanOrEqual=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where paymentAmount is less than or equal to DEFAULT_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldBeFound("paymentAmount.lessThanOrEqual=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the settlementRequisitionList where paymentAmount is less than or equal to SMALLER_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldNotBeFound("paymentAmount.lessThanOrEqual=" + SMALLER_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where paymentAmount is less than DEFAULT_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldNotBeFound("paymentAmount.lessThan=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the settlementRequisitionList where paymentAmount is less than UPDATED_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldBeFound("paymentAmount.lessThan=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where paymentAmount is greater than DEFAULT_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldNotBeFound("paymentAmount.greaterThan=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the settlementRequisitionList where paymentAmount is greater than SMALLER_PAYMENT_AMOUNT
        defaultSettlementRequisitionShouldBeFound("paymentAmount.greaterThan=" + SMALLER_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where paymentStatus equals to DEFAULT_PAYMENT_STATUS
        defaultSettlementRequisitionShouldBeFound("paymentStatus.equals=" + DEFAULT_PAYMENT_STATUS);

        // Get all the settlementRequisitionList where paymentStatus equals to UPDATED_PAYMENT_STATUS
        defaultSettlementRequisitionShouldNotBeFound("paymentStatus.equals=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where paymentStatus not equals to DEFAULT_PAYMENT_STATUS
        defaultSettlementRequisitionShouldNotBeFound("paymentStatus.notEquals=" + DEFAULT_PAYMENT_STATUS);

        // Get all the settlementRequisitionList where paymentStatus not equals to UPDATED_PAYMENT_STATUS
        defaultSettlementRequisitionShouldBeFound("paymentStatus.notEquals=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where paymentStatus in DEFAULT_PAYMENT_STATUS or UPDATED_PAYMENT_STATUS
        defaultSettlementRequisitionShouldBeFound("paymentStatus.in=" + DEFAULT_PAYMENT_STATUS + "," + UPDATED_PAYMENT_STATUS);

        // Get all the settlementRequisitionList where paymentStatus equals to UPDATED_PAYMENT_STATUS
        defaultSettlementRequisitionShouldNotBeFound("paymentStatus.in=" + UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        // Get all the settlementRequisitionList where paymentStatus is not null
        defaultSettlementRequisitionShouldBeFound("paymentStatus.specified=true");

        // Get all the settlementRequisitionList where paymentStatus is null
        defaultSettlementRequisitionShouldNotBeFound("paymentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
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
        settlementRequisition.setSettlementCurrency(settlementCurrency);
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the settlementRequisitionList where settlementCurrency equals to settlementCurrencyId
        defaultSettlementRequisitionShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the settlementRequisitionList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultSettlementRequisitionShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByCurrentOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        ApplicationUser currentOwner;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            currentOwner = ApplicationUserResourceIT.createEntity(em);
            em.persist(currentOwner);
            em.flush();
        } else {
            currentOwner = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(currentOwner);
        em.flush();
        settlementRequisition.setCurrentOwner(currentOwner);
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Long currentOwnerId = currentOwner.getId();

        // Get all the settlementRequisitionList where currentOwner equals to currentOwnerId
        defaultSettlementRequisitionShouldBeFound("currentOwnerId.equals=" + currentOwnerId);

        // Get all the settlementRequisitionList where currentOwner equals to (currentOwnerId + 1)
        defaultSettlementRequisitionShouldNotBeFound("currentOwnerId.equals=" + (currentOwnerId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByNativeOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        ApplicationUser nativeOwner;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            nativeOwner = ApplicationUserResourceIT.createEntity(em);
            em.persist(nativeOwner);
            em.flush();
        } else {
            nativeOwner = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(nativeOwner);
        em.flush();
        settlementRequisition.setNativeOwner(nativeOwner);
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Long nativeOwnerId = nativeOwner.getId();

        // Get all the settlementRequisitionList where nativeOwner equals to nativeOwnerId
        defaultSettlementRequisitionShouldBeFound("nativeOwnerId.equals=" + nativeOwnerId);

        // Get all the settlementRequisitionList where nativeOwner equals to (nativeOwnerId + 1)
        defaultSettlementRequisitionShouldNotBeFound("nativeOwnerId.equals=" + (nativeOwnerId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByNativeDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Dealer nativeDepartment;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            nativeDepartment = DealerResourceIT.createEntity(em);
            em.persist(nativeDepartment);
            em.flush();
        } else {
            nativeDepartment = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(nativeDepartment);
        em.flush();
        settlementRequisition.setNativeDepartment(nativeDepartment);
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Long nativeDepartmentId = nativeDepartment.getId();

        // Get all the settlementRequisitionList where nativeDepartment equals to nativeDepartmentId
        defaultSettlementRequisitionShouldBeFound("nativeDepartmentId.equals=" + nativeDepartmentId);

        // Get all the settlementRequisitionList where nativeDepartment equals to (nativeDepartmentId + 1)
        defaultSettlementRequisitionShouldNotBeFound("nativeDepartmentId.equals=" + (nativeDepartmentId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByBillerIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Dealer biller;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            biller = DealerResourceIT.createEntity(em);
            em.persist(biller);
            em.flush();
        } else {
            biller = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(biller);
        em.flush();
        settlementRequisition.setBiller(biller);
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Long billerId = biller.getId();

        // Get all the settlementRequisitionList where biller equals to billerId
        defaultSettlementRequisitionShouldBeFound("billerId.equals=" + billerId);

        // Get all the settlementRequisitionList where biller equals to (billerId + 1)
        defaultSettlementRequisitionShouldNotBeFound("billerId.equals=" + (billerId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPaymentInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        PaymentInvoice paymentInvoice;
        if (TestUtil.findAll(em, PaymentInvoice.class).isEmpty()) {
            paymentInvoice = PaymentInvoiceResourceIT.createEntity(em);
            em.persist(paymentInvoice);
            em.flush();
        } else {
            paymentInvoice = TestUtil.findAll(em, PaymentInvoice.class).get(0);
        }
        em.persist(paymentInvoice);
        em.flush();
        settlementRequisition.addPaymentInvoice(paymentInvoice);
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Long paymentInvoiceId = paymentInvoice.getId();

        // Get all the settlementRequisitionList where paymentInvoice equals to paymentInvoiceId
        defaultSettlementRequisitionShouldBeFound("paymentInvoiceId.equals=" + paymentInvoiceId);

        // Get all the settlementRequisitionList where paymentInvoice equals to (paymentInvoiceId + 1)
        defaultSettlementRequisitionShouldNotBeFound("paymentInvoiceId.equals=" + (paymentInvoiceId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByDeliveryNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        DeliveryNote deliveryNote;
        if (TestUtil.findAll(em, DeliveryNote.class).isEmpty()) {
            deliveryNote = DeliveryNoteResourceIT.createEntity(em);
            em.persist(deliveryNote);
            em.flush();
        } else {
            deliveryNote = TestUtil.findAll(em, DeliveryNote.class).get(0);
        }
        em.persist(deliveryNote);
        em.flush();
        settlementRequisition.addDeliveryNote(deliveryNote);
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Long deliveryNoteId = deliveryNote.getId();

        // Get all the settlementRequisitionList where deliveryNote equals to deliveryNoteId
        defaultSettlementRequisitionShouldBeFound("deliveryNoteId.equals=" + deliveryNoteId);

        // Get all the settlementRequisitionList where deliveryNote equals to (deliveryNoteId + 1)
        defaultSettlementRequisitionShouldNotBeFound("deliveryNoteId.equals=" + (deliveryNoteId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByJobSheetIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        JobSheet jobSheet;
        if (TestUtil.findAll(em, JobSheet.class).isEmpty()) {
            jobSheet = JobSheetResourceIT.createEntity(em);
            em.persist(jobSheet);
            em.flush();
        } else {
            jobSheet = TestUtil.findAll(em, JobSheet.class).get(0);
        }
        em.persist(jobSheet);
        em.flush();
        settlementRequisition.addJobSheet(jobSheet);
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Long jobSheetId = jobSheet.getId();

        // Get all the settlementRequisitionList where jobSheet equals to jobSheetId
        defaultSettlementRequisitionShouldBeFound("jobSheetId.equals=" + jobSheetId);

        // Get all the settlementRequisitionList where jobSheet equals to (jobSheetId + 1)
        defaultSettlementRequisitionShouldNotBeFound("jobSheetId.equals=" + (jobSheetId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsBySignaturesIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Dealer signatures;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            signatures = DealerResourceIT.createEntity(em);
            em.persist(signatures);
            em.flush();
        } else {
            signatures = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(signatures);
        em.flush();
        settlementRequisition.addSignatures(signatures);
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Long signaturesId = signatures.getId();

        // Get all the settlementRequisitionList where signatures equals to signaturesId
        defaultSettlementRequisitionShouldBeFound("signaturesId.equals=" + signaturesId);

        // Get all the settlementRequisitionList where signatures equals to (signaturesId + 1)
        defaultSettlementRequisitionShouldNotBeFound("signaturesId.equals=" + (signaturesId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByBusinessDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        BusinessDocument businessDocument;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            businessDocument = BusinessDocumentResourceIT.createEntity(em);
            em.persist(businessDocument);
            em.flush();
        } else {
            businessDocument = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(businessDocument);
        em.flush();
        settlementRequisition.addBusinessDocument(businessDocument);
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Long businessDocumentId = businessDocument.getId();

        // Get all the settlementRequisitionList where businessDocument equals to businessDocumentId
        defaultSettlementRequisitionShouldBeFound("businessDocumentId.equals=" + businessDocumentId);

        // Get all the settlementRequisitionList where businessDocument equals to (businessDocumentId + 1)
        defaultSettlementRequisitionShouldNotBeFound("businessDocumentId.equals=" + (businessDocumentId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByApplicationMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        UniversallyUniqueMapping applicationMapping;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            applicationMapping = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(applicationMapping);
            em.flush();
        } else {
            applicationMapping = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(applicationMapping);
        em.flush();
        settlementRequisition.addApplicationMapping(applicationMapping);
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Long applicationMappingId = applicationMapping.getId();

        // Get all the settlementRequisitionList where applicationMapping equals to applicationMappingId
        defaultSettlementRequisitionShouldBeFound("applicationMappingId.equals=" + applicationMappingId);

        // Get all the settlementRequisitionList where applicationMapping equals to (applicationMappingId + 1)
        defaultSettlementRequisitionShouldNotBeFound("applicationMappingId.equals=" + (applicationMappingId + 1));
    }

    @Test
    @Transactional
    void getAllSettlementRequisitionsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
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
        settlementRequisition.addPlaceholder(placeholder);
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        Long placeholderId = placeholder.getId();

        // Get all the settlementRequisitionList where placeholder equals to placeholderId
        defaultSettlementRequisitionShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the settlementRequisitionList where placeholder equals to (placeholderId + 1)
        defaultSettlementRequisitionShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSettlementRequisitionShouldBeFound(String filter) throws Exception {
        restSettlementRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settlementRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].requisitionNumber").value(hasItem(DEFAULT_REQUISITION_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())));

        // Check, that the count call also returns 1
        restSettlementRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSettlementRequisitionShouldNotBeFound(String filter) throws Exception {
        restSettlementRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSettlementRequisitionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSettlementRequisition() throws Exception {
        // Get the settlementRequisition
        restSettlementRequisitionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSettlementRequisition() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        int databaseSizeBeforeUpdate = settlementRequisitionRepository.findAll().size();

        // Update the settlementRequisition
        SettlementRequisition updatedSettlementRequisition = settlementRequisitionRepository.findById(settlementRequisition.getId()).get();
        // Disconnect from session so that the updates on updatedSettlementRequisition are not directly saved in db
        em.detach(updatedSettlementRequisition);
        updatedSettlementRequisition
            .description(UPDATED_DESCRIPTION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .requisitionNumber(UPDATED_REQUISITION_NUMBER)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS);
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(updatedSettlementRequisition);

        restSettlementRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settlementRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isOk());

        // Validate the SettlementRequisition in the database
        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeUpdate);
        SettlementRequisition testSettlementRequisition = settlementRequisitionList.get(settlementRequisitionList.size() - 1);
        assertThat(testSettlementRequisition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSettlementRequisition.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testSettlementRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testSettlementRequisition.getRequisitionNumber()).isEqualTo(UPDATED_REQUISITION_NUMBER);
        assertThat(testSettlementRequisition.getPaymentAmount()).isEqualTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testSettlementRequisition.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);

        // Validate the SettlementRequisition in Elasticsearch
        verify(mockSettlementRequisitionSearchRepository).save(testSettlementRequisition);
    }

    @Test
    @Transactional
    void putNonExistingSettlementRequisition() throws Exception {
        int databaseSizeBeforeUpdate = settlementRequisitionRepository.findAll().size();
        settlementRequisition.setId(count.incrementAndGet());

        // Create the SettlementRequisition
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettlementRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settlementRequisitionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettlementRequisition in the database
        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SettlementRequisition in Elasticsearch
        verify(mockSettlementRequisitionSearchRepository, times(0)).save(settlementRequisition);
    }

    @Test
    @Transactional
    void putWithIdMismatchSettlementRequisition() throws Exception {
        int databaseSizeBeforeUpdate = settlementRequisitionRepository.findAll().size();
        settlementRequisition.setId(count.incrementAndGet());

        // Create the SettlementRequisition
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettlementRequisition in the database
        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SettlementRequisition in Elasticsearch
        verify(mockSettlementRequisitionSearchRepository, times(0)).save(settlementRequisition);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSettlementRequisition() throws Exception {
        int databaseSizeBeforeUpdate = settlementRequisitionRepository.findAll().size();
        settlementRequisition.setId(count.incrementAndGet());

        // Create the SettlementRequisition
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementRequisitionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SettlementRequisition in the database
        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SettlementRequisition in Elasticsearch
        verify(mockSettlementRequisitionSearchRepository, times(0)).save(settlementRequisition);
    }

    @Test
    @Transactional
    void partialUpdateSettlementRequisitionWithPatch() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        int databaseSizeBeforeUpdate = settlementRequisitionRepository.findAll().size();

        // Update the settlementRequisition using partial update
        SettlementRequisition partialUpdatedSettlementRequisition = new SettlementRequisition();
        partialUpdatedSettlementRequisition.setId(settlementRequisition.getId());

        partialUpdatedSettlementRequisition
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .requisitionNumber(UPDATED_REQUISITION_NUMBER)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS);

        restSettlementRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSettlementRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSettlementRequisition))
            )
            .andExpect(status().isOk());

        // Validate the SettlementRequisition in the database
        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeUpdate);
        SettlementRequisition testSettlementRequisition = settlementRequisitionList.get(settlementRequisitionList.size() - 1);
        assertThat(testSettlementRequisition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSettlementRequisition.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testSettlementRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testSettlementRequisition.getRequisitionNumber()).isEqualTo(UPDATED_REQUISITION_NUMBER);
        assertThat(testSettlementRequisition.getPaymentAmount()).isEqualByComparingTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testSettlementRequisition.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateSettlementRequisitionWithPatch() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        int databaseSizeBeforeUpdate = settlementRequisitionRepository.findAll().size();

        // Update the settlementRequisition using partial update
        SettlementRequisition partialUpdatedSettlementRequisition = new SettlementRequisition();
        partialUpdatedSettlementRequisition.setId(settlementRequisition.getId());

        partialUpdatedSettlementRequisition
            .description(UPDATED_DESCRIPTION)
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .timeOfRequisition(UPDATED_TIME_OF_REQUISITION)
            .requisitionNumber(UPDATED_REQUISITION_NUMBER)
            .paymentAmount(UPDATED_PAYMENT_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS);

        restSettlementRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSettlementRequisition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSettlementRequisition))
            )
            .andExpect(status().isOk());

        // Validate the SettlementRequisition in the database
        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeUpdate);
        SettlementRequisition testSettlementRequisition = settlementRequisitionList.get(settlementRequisitionList.size() - 1);
        assertThat(testSettlementRequisition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSettlementRequisition.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testSettlementRequisition.getTimeOfRequisition()).isEqualTo(UPDATED_TIME_OF_REQUISITION);
        assertThat(testSettlementRequisition.getRequisitionNumber()).isEqualTo(UPDATED_REQUISITION_NUMBER);
        assertThat(testSettlementRequisition.getPaymentAmount()).isEqualByComparingTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testSettlementRequisition.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingSettlementRequisition() throws Exception {
        int databaseSizeBeforeUpdate = settlementRequisitionRepository.findAll().size();
        settlementRequisition.setId(count.incrementAndGet());

        // Create the SettlementRequisition
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettlementRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, settlementRequisitionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettlementRequisition in the database
        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SettlementRequisition in Elasticsearch
        verify(mockSettlementRequisitionSearchRepository, times(0)).save(settlementRequisition);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSettlementRequisition() throws Exception {
        int databaseSizeBeforeUpdate = settlementRequisitionRepository.findAll().size();
        settlementRequisition.setId(count.incrementAndGet());

        // Create the SettlementRequisition
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettlementRequisition in the database
        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SettlementRequisition in Elasticsearch
        verify(mockSettlementRequisitionSearchRepository, times(0)).save(settlementRequisition);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSettlementRequisition() throws Exception {
        int databaseSizeBeforeUpdate = settlementRequisitionRepository.findAll().size();
        settlementRequisition.setId(count.incrementAndGet());

        // Create the SettlementRequisition
        SettlementRequisitionDTO settlementRequisitionDTO = settlementRequisitionMapper.toDto(settlementRequisition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettlementRequisitionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settlementRequisitionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SettlementRequisition in the database
        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SettlementRequisition in Elasticsearch
        verify(mockSettlementRequisitionSearchRepository, times(0)).save(settlementRequisition);
    }

    @Test
    @Transactional
    void deleteSettlementRequisition() throws Exception {
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);

        int databaseSizeBeforeDelete = settlementRequisitionRepository.findAll().size();

        // Delete the settlementRequisition
        restSettlementRequisitionMockMvc
            .perform(delete(ENTITY_API_URL_ID, settlementRequisition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SettlementRequisition> settlementRequisitionList = settlementRequisitionRepository.findAll();
        assertThat(settlementRequisitionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SettlementRequisition in Elasticsearch
        verify(mockSettlementRequisitionSearchRepository, times(1)).deleteById(settlementRequisition.getId());
    }

    @Test
    @Transactional
    void searchSettlementRequisition() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        settlementRequisitionRepository.saveAndFlush(settlementRequisition);
        when(mockSettlementRequisitionSearchRepository.search("id:" + settlementRequisition.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(settlementRequisition), PageRequest.of(0, 1), 1));

        // Search the settlementRequisition
        restSettlementRequisitionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + settlementRequisition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settlementRequisition.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].timeOfRequisition").value(hasItem(sameInstant(DEFAULT_TIME_OF_REQUISITION))))
            .andExpect(jsonPath("$.[*].requisitionNumber").value(hasItem(DEFAULT_REQUISITION_NUMBER)))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentStatus").value(hasItem(DEFAULT_PAYMENT_STATUS.toString())));
    }
}
