package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import io.github.erp.IntegrationTest;
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.domain.LeasePayment;
import io.github.erp.erp.resources.leases.LeasePaymentResourceProd;
import io.github.erp.repository.LeasePaymentRepository;
import io.github.erp.repository.search.LeasePaymentSearchRepository;
import io.github.erp.service.dto.LeasePaymentDTO;
import io.github.erp.service.mapper.LeasePaymentMapper;
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
 * Integration tests for the {@link LeasePaymentResourceProd } REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"LEASE_MANAGER"})
class LeasePaymentResourceIT {

    private static final BigDecimal DEFAULT_PAYMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAYMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PAYMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PAYMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/leases/lease-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/leases/_search/lease-payments";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeasePaymentRepository leasePaymentRepository;

    @Autowired
    private LeasePaymentMapper leasePaymentMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.LeasePaymentSearchRepositoryMockConfiguration
     */
    @Autowired
    private LeasePaymentSearchRepository mockLeasePaymentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeasePaymentMockMvc;

    private LeasePayment leasePayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeasePayment createEntity(EntityManager em) {
        LeasePayment leasePayment = new LeasePayment().paymentAmount(DEFAULT_PAYMENT_AMOUNT).paymentDate(DEFAULT_PAYMENT_DATE);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        leasePayment.setLeaseContract(iFRS16LeaseContract);
        return leasePayment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeasePayment createUpdatedEntity(EntityManager em) {
        LeasePayment leasePayment = new LeasePayment().paymentAmount(UPDATED_PAYMENT_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE);
        // Add required entity
        IFRS16LeaseContract iFRS16LeaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            iFRS16LeaseContract = IFRS16LeaseContractResourceIT.createUpdatedEntity(em);
            em.persist(iFRS16LeaseContract);
            em.flush();
        } else {
            iFRS16LeaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        leasePayment.setLeaseContract(iFRS16LeaseContract);
        return leasePayment;
    }

    @BeforeEach
    public void initTest() {
        leasePayment = createEntity(em);
    }

    @Test
    @Transactional
    void createLeasePayment() throws Exception {
        int databaseSizeBeforeCreate = leasePaymentRepository.findAll().size();
        // Create the LeasePayment
        LeasePaymentDTO leasePaymentDTO = leasePaymentMapper.toDto(leasePayment);
        restLeasePaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leasePaymentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeasePayment in the database
        List<LeasePayment> leasePaymentList = leasePaymentRepository.findAll();
        assertThat(leasePaymentList).hasSize(databaseSizeBeforeCreate + 1);
        LeasePayment testLeasePayment = leasePaymentList.get(leasePaymentList.size() - 1);
        assertThat(testLeasePayment.getPaymentAmount()).isEqualByComparingTo(DEFAULT_PAYMENT_AMOUNT);
        assertThat(testLeasePayment.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);

        // Validate the LeasePayment in Elasticsearch
        verify(mockLeasePaymentSearchRepository, times(1)).save(testLeasePayment);
    }

    @Test
    @Transactional
    void createLeasePaymentWithExistingId() throws Exception {
        // Create the LeasePayment with an existing ID
        leasePayment.setId(1L);
        LeasePaymentDTO leasePaymentDTO = leasePaymentMapper.toDto(leasePayment);

        int databaseSizeBeforeCreate = leasePaymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeasePaymentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leasePaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeasePayment in the database
        List<LeasePayment> leasePaymentList = leasePaymentRepository.findAll();
        assertThat(leasePaymentList).hasSize(databaseSizeBeforeCreate);

        // Validate the LeasePayment in Elasticsearch
        verify(mockLeasePaymentSearchRepository, times(0)).save(leasePayment);
    }

    @Test
    @Transactional
    void getAllLeasePayments() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList
        restLeasePaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasePayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())));
    }

    @Test
    @Transactional
    void getLeasePayment() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get the leasePayment
        restLeasePaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, leasePayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leasePayment.getId().intValue()))
            .andExpect(jsonPath("$.paymentAmount").value(sameNumber(DEFAULT_PAYMENT_AMOUNT)))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()));
    }

    @Test
    @Transactional
    void getLeasePaymentsByIdFiltering() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        Long id = leasePayment.getId();

        defaultLeasePaymentShouldBeFound("id.equals=" + id);
        defaultLeasePaymentShouldNotBeFound("id.notEquals=" + id);

        defaultLeasePaymentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeasePaymentShouldNotBeFound("id.greaterThan=" + id);

        defaultLeasePaymentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeasePaymentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentAmount equals to DEFAULT_PAYMENT_AMOUNT
        defaultLeasePaymentShouldBeFound("paymentAmount.equals=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the leasePaymentList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultLeasePaymentShouldNotBeFound("paymentAmount.equals=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentAmount not equals to DEFAULT_PAYMENT_AMOUNT
        defaultLeasePaymentShouldNotBeFound("paymentAmount.notEquals=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the leasePaymentList where paymentAmount not equals to UPDATED_PAYMENT_AMOUNT
        defaultLeasePaymentShouldBeFound("paymentAmount.notEquals=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentAmount in DEFAULT_PAYMENT_AMOUNT or UPDATED_PAYMENT_AMOUNT
        defaultLeasePaymentShouldBeFound("paymentAmount.in=" + DEFAULT_PAYMENT_AMOUNT + "," + UPDATED_PAYMENT_AMOUNT);

        // Get all the leasePaymentList where paymentAmount equals to UPDATED_PAYMENT_AMOUNT
        defaultLeasePaymentShouldNotBeFound("paymentAmount.in=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentAmount is not null
        defaultLeasePaymentShouldBeFound("paymentAmount.specified=true");

        // Get all the leasePaymentList where paymentAmount is null
        defaultLeasePaymentShouldNotBeFound("paymentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentAmount is greater than or equal to DEFAULT_PAYMENT_AMOUNT
        defaultLeasePaymentShouldBeFound("paymentAmount.greaterThanOrEqual=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the leasePaymentList where paymentAmount is greater than or equal to UPDATED_PAYMENT_AMOUNT
        defaultLeasePaymentShouldNotBeFound("paymentAmount.greaterThanOrEqual=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentAmount is less than or equal to DEFAULT_PAYMENT_AMOUNT
        defaultLeasePaymentShouldBeFound("paymentAmount.lessThanOrEqual=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the leasePaymentList where paymentAmount is less than or equal to SMALLER_PAYMENT_AMOUNT
        defaultLeasePaymentShouldNotBeFound("paymentAmount.lessThanOrEqual=" + SMALLER_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentAmount is less than DEFAULT_PAYMENT_AMOUNT
        defaultLeasePaymentShouldNotBeFound("paymentAmount.lessThan=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the leasePaymentList where paymentAmount is less than UPDATED_PAYMENT_AMOUNT
        defaultLeasePaymentShouldBeFound("paymentAmount.lessThan=" + UPDATED_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentAmount is greater than DEFAULT_PAYMENT_AMOUNT
        defaultLeasePaymentShouldNotBeFound("paymentAmount.greaterThan=" + DEFAULT_PAYMENT_AMOUNT);

        // Get all the leasePaymentList where paymentAmount is greater than SMALLER_PAYMENT_AMOUNT
        defaultLeasePaymentShouldBeFound("paymentAmount.greaterThan=" + SMALLER_PAYMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentDate equals to DEFAULT_PAYMENT_DATE
        defaultLeasePaymentShouldBeFound("paymentDate.equals=" + DEFAULT_PAYMENT_DATE);

        // Get all the leasePaymentList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultLeasePaymentShouldNotBeFound("paymentDate.equals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentDate not equals to DEFAULT_PAYMENT_DATE
        defaultLeasePaymentShouldNotBeFound("paymentDate.notEquals=" + DEFAULT_PAYMENT_DATE);

        // Get all the leasePaymentList where paymentDate not equals to UPDATED_PAYMENT_DATE
        defaultLeasePaymentShouldBeFound("paymentDate.notEquals=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentDateIsInShouldWork() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentDate in DEFAULT_PAYMENT_DATE or UPDATED_PAYMENT_DATE
        defaultLeasePaymentShouldBeFound("paymentDate.in=" + DEFAULT_PAYMENT_DATE + "," + UPDATED_PAYMENT_DATE);

        // Get all the leasePaymentList where paymentDate equals to UPDATED_PAYMENT_DATE
        defaultLeasePaymentShouldNotBeFound("paymentDate.in=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentDate is not null
        defaultLeasePaymentShouldBeFound("paymentDate.specified=true");

        // Get all the leasePaymentList where paymentDate is null
        defaultLeasePaymentShouldNotBeFound("paymentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentDate is greater than or equal to DEFAULT_PAYMENT_DATE
        defaultLeasePaymentShouldBeFound("paymentDate.greaterThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the leasePaymentList where paymentDate is greater than or equal to UPDATED_PAYMENT_DATE
        defaultLeasePaymentShouldNotBeFound("paymentDate.greaterThanOrEqual=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentDate is less than or equal to DEFAULT_PAYMENT_DATE
        defaultLeasePaymentShouldBeFound("paymentDate.lessThanOrEqual=" + DEFAULT_PAYMENT_DATE);

        // Get all the leasePaymentList where paymentDate is less than or equal to SMALLER_PAYMENT_DATE
        defaultLeasePaymentShouldNotBeFound("paymentDate.lessThanOrEqual=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentDate is less than DEFAULT_PAYMENT_DATE
        defaultLeasePaymentShouldNotBeFound("paymentDate.lessThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the leasePaymentList where paymentDate is less than UPDATED_PAYMENT_DATE
        defaultLeasePaymentShouldBeFound("paymentDate.lessThan=" + UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByPaymentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        // Get all the leasePaymentList where paymentDate is greater than DEFAULT_PAYMENT_DATE
        defaultLeasePaymentShouldNotBeFound("paymentDate.greaterThan=" + DEFAULT_PAYMENT_DATE);

        // Get all the leasePaymentList where paymentDate is greater than SMALLER_PAYMENT_DATE
        defaultLeasePaymentShouldBeFound("paymentDate.greaterThan=" + SMALLER_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void getAllLeasePaymentsByLeaseContractIsEqualToSomething() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);
        IFRS16LeaseContract leaseContract;
        if (TestUtil.findAll(em, IFRS16LeaseContract.class).isEmpty()) {
            leaseContract = IFRS16LeaseContractResourceIT.createEntity(em);
            em.persist(leaseContract);
            em.flush();
        } else {
            leaseContract = TestUtil.findAll(em, IFRS16LeaseContract.class).get(0);
        }
        em.persist(leaseContract);
        em.flush();
        leasePayment.setLeaseContract(leaseContract);
        leasePaymentRepository.saveAndFlush(leasePayment);
        Long leaseContractId = leaseContract.getId();

        // Get all the leasePaymentList where leaseContract equals to leaseContractId
        defaultLeasePaymentShouldBeFound("leaseContractId.equals=" + leaseContractId);

        // Get all the leasePaymentList where leaseContract equals to (leaseContractId + 1)
        defaultLeasePaymentShouldNotBeFound("leaseContractId.equals=" + (leaseContractId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeasePaymentShouldBeFound(String filter) throws Exception {
        restLeasePaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasePayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())));

        // Check, that the count call also returns 1
        restLeasePaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeasePaymentShouldNotBeFound(String filter) throws Exception {
        restLeasePaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeasePaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeasePayment() throws Exception {
        // Get the leasePayment
        restLeasePaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeasePayment() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        int databaseSizeBeforeUpdate = leasePaymentRepository.findAll().size();

        // Update the leasePayment
        LeasePayment updatedLeasePayment = leasePaymentRepository.findById(leasePayment.getId()).get();
        // Disconnect from session so that the updates on updatedLeasePayment are not directly saved in db
        em.detach(updatedLeasePayment);
        updatedLeasePayment.paymentAmount(UPDATED_PAYMENT_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE);
        LeasePaymentDTO leasePaymentDTO = leasePaymentMapper.toDto(updatedLeasePayment);

        restLeasePaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leasePaymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leasePaymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeasePayment in the database
        List<LeasePayment> leasePaymentList = leasePaymentRepository.findAll();
        assertThat(leasePaymentList).hasSize(databaseSizeBeforeUpdate);
        LeasePayment testLeasePayment = leasePaymentList.get(leasePaymentList.size() - 1);
        assertThat(testLeasePayment.getPaymentAmount()).isEqualTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testLeasePayment.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);

        // Validate the LeasePayment in Elasticsearch
        // verify(mockLeasePaymentSearchRepository).save(testLeasePayment);
    }

    @Test
    @Transactional
    void putNonExistingLeasePayment() throws Exception {
        int databaseSizeBeforeUpdate = leasePaymentRepository.findAll().size();
        leasePayment.setId(count.incrementAndGet());

        // Create the LeasePayment
        LeasePaymentDTO leasePaymentDTO = leasePaymentMapper.toDto(leasePayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeasePaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leasePaymentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leasePaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeasePayment in the database
        List<LeasePayment> leasePaymentList = leasePaymentRepository.findAll();
        assertThat(leasePaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasePayment in Elasticsearch
        verify(mockLeasePaymentSearchRepository, times(0)).save(leasePayment);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeasePayment() throws Exception {
        int databaseSizeBeforeUpdate = leasePaymentRepository.findAll().size();
        leasePayment.setId(count.incrementAndGet());

        // Create the LeasePayment
        LeasePaymentDTO leasePaymentDTO = leasePaymentMapper.toDto(leasePayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeasePaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leasePaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeasePayment in the database
        List<LeasePayment> leasePaymentList = leasePaymentRepository.findAll();
        assertThat(leasePaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasePayment in Elasticsearch
        verify(mockLeasePaymentSearchRepository, times(0)).save(leasePayment);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeasePayment() throws Exception {
        int databaseSizeBeforeUpdate = leasePaymentRepository.findAll().size();
        leasePayment.setId(count.incrementAndGet());

        // Create the LeasePayment
        LeasePaymentDTO leasePaymentDTO = leasePaymentMapper.toDto(leasePayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeasePaymentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(leasePaymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeasePayment in the database
        List<LeasePayment> leasePaymentList = leasePaymentRepository.findAll();
        assertThat(leasePaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasePayment in Elasticsearch
        verify(mockLeasePaymentSearchRepository, times(0)).save(leasePayment);
    }

    @Test
    @Transactional
    void partialUpdateLeasePaymentWithPatch() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        int databaseSizeBeforeUpdate = leasePaymentRepository.findAll().size();

        // Update the leasePayment using partial update
        LeasePayment partialUpdatedLeasePayment = new LeasePayment();
        partialUpdatedLeasePayment.setId(leasePayment.getId());

        partialUpdatedLeasePayment.paymentAmount(UPDATED_PAYMENT_AMOUNT);

        restLeasePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeasePayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeasePayment))
            )
            .andExpect(status().isOk());

        // Validate the LeasePayment in the database
        List<LeasePayment> leasePaymentList = leasePaymentRepository.findAll();
        assertThat(leasePaymentList).hasSize(databaseSizeBeforeUpdate);
        LeasePayment testLeasePayment = leasePaymentList.get(leasePaymentList.size() - 1);
        assertThat(testLeasePayment.getPaymentAmount()).isEqualByComparingTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testLeasePayment.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateLeasePaymentWithPatch() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        int databaseSizeBeforeUpdate = leasePaymentRepository.findAll().size();

        // Update the leasePayment using partial update
        LeasePayment partialUpdatedLeasePayment = new LeasePayment();
        partialUpdatedLeasePayment.setId(leasePayment.getId());

        partialUpdatedLeasePayment.paymentAmount(UPDATED_PAYMENT_AMOUNT).paymentDate(UPDATED_PAYMENT_DATE);

        restLeasePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeasePayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeasePayment))
            )
            .andExpect(status().isOk());

        // Validate the LeasePayment in the database
        List<LeasePayment> leasePaymentList = leasePaymentRepository.findAll();
        assertThat(leasePaymentList).hasSize(databaseSizeBeforeUpdate);
        LeasePayment testLeasePayment = leasePaymentList.get(leasePaymentList.size() - 1);
        assertThat(testLeasePayment.getPaymentAmount()).isEqualByComparingTo(UPDATED_PAYMENT_AMOUNT);
        assertThat(testLeasePayment.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingLeasePayment() throws Exception {
        int databaseSizeBeforeUpdate = leasePaymentRepository.findAll().size();
        leasePayment.setId(count.incrementAndGet());

        // Create the LeasePayment
        LeasePaymentDTO leasePaymentDTO = leasePaymentMapper.toDto(leasePayment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeasePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leasePaymentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leasePaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeasePayment in the database
        List<LeasePayment> leasePaymentList = leasePaymentRepository.findAll();
        assertThat(leasePaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasePayment in Elasticsearch
        verify(mockLeasePaymentSearchRepository, times(0)).save(leasePayment);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeasePayment() throws Exception {
        int databaseSizeBeforeUpdate = leasePaymentRepository.findAll().size();
        leasePayment.setId(count.incrementAndGet());

        // Create the LeasePayment
        LeasePaymentDTO leasePaymentDTO = leasePaymentMapper.toDto(leasePayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeasePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leasePaymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeasePayment in the database
        List<LeasePayment> leasePaymentList = leasePaymentRepository.findAll();
        assertThat(leasePaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasePayment in Elasticsearch
        verify(mockLeasePaymentSearchRepository, times(0)).save(leasePayment);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeasePayment() throws Exception {
        int databaseSizeBeforeUpdate = leasePaymentRepository.findAll().size();
        leasePayment.setId(count.incrementAndGet());

        // Create the LeasePayment
        LeasePaymentDTO leasePaymentDTO = leasePaymentMapper.toDto(leasePayment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeasePaymentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leasePaymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeasePayment in the database
        List<LeasePayment> leasePaymentList = leasePaymentRepository.findAll();
        assertThat(leasePaymentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LeasePayment in Elasticsearch
        verify(mockLeasePaymentSearchRepository, times(0)).save(leasePayment);
    }

    @Test
    @Transactional
    void deleteLeasePayment() throws Exception {
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);

        int databaseSizeBeforeDelete = leasePaymentRepository.findAll().size();

        // Delete the leasePayment
        restLeasePaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, leasePayment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeasePayment> leasePaymentList = leasePaymentRepository.findAll();
        assertThat(leasePaymentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LeasePayment in Elasticsearch
        verify(mockLeasePaymentSearchRepository, times(1)).deleteById(leasePayment.getId());
    }

    @Test
    @Transactional
    void searchLeasePayment() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        leasePaymentRepository.saveAndFlush(leasePayment);
        when(mockLeasePaymentSearchRepository.search("id:" + leasePayment.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(leasePayment), PageRequest.of(0, 1), 1));

        // Search the leasePayment
        restLeasePaymentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + leasePayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leasePayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].paymentAmount").value(hasItem(sameNumber(DEFAULT_PAYMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())));
    }
}
