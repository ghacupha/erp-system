package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.CreditCardOwnership;
import io.github.erp.domain.enumeration.CreditCardOwnershipTypes;
import io.github.erp.repository.CreditCardOwnershipRepository;
import io.github.erp.repository.search.CreditCardOwnershipSearchRepository;
import io.github.erp.service.dto.CreditCardOwnershipDTO;
import io.github.erp.service.mapper.CreditCardOwnershipMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.CreditCardOwnershipResource;
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

/**
 * Integration tests for the {@link CreditCardOwnershipResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CreditCardOwnershipResourceIT {

    private static final String DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE = "BBBBBBBBBB";

    private static final CreditCardOwnershipTypes DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE = CreditCardOwnershipTypes.INDIVIDUAL;
    private static final CreditCardOwnershipTypes UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE = CreditCardOwnershipTypes.CORPORATE;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/credit-card-ownerships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/credit-card-ownerships";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CreditCardOwnershipRepository creditCardOwnershipRepository;

    @Autowired
    private CreditCardOwnershipMapper creditCardOwnershipMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CreditCardOwnershipSearchRepositoryMockConfiguration
     */
    @Autowired
    private CreditCardOwnershipSearchRepository mockCreditCardOwnershipSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCreditCardOwnershipMockMvc;

    private CreditCardOwnership creditCardOwnership;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditCardOwnership createEntity(EntityManager em) {
        CreditCardOwnership creditCardOwnership = new CreditCardOwnership()
            .creditCardOwnershipCategoryCode(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE)
            .creditCardOwnershipCategoryType(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return creditCardOwnership;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditCardOwnership createUpdatedEntity(EntityManager em) {
        CreditCardOwnership creditCardOwnership = new CreditCardOwnership()
            .creditCardOwnershipCategoryCode(UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE)
            .creditCardOwnershipCategoryType(UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE)
            .description(UPDATED_DESCRIPTION);
        return creditCardOwnership;
    }

    @BeforeEach
    public void initTest() {
        creditCardOwnership = createEntity(em);
    }

    @Test
    @Transactional
    void createCreditCardOwnership() throws Exception {
        int databaseSizeBeforeCreate = creditCardOwnershipRepository.findAll().size();
        // Create the CreditCardOwnership
        CreditCardOwnershipDTO creditCardOwnershipDTO = creditCardOwnershipMapper.toDto(creditCardOwnership);
        restCreditCardOwnershipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardOwnershipDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CreditCardOwnership in the database
        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeCreate + 1);
        CreditCardOwnership testCreditCardOwnership = creditCardOwnershipList.get(creditCardOwnershipList.size() - 1);
        assertThat(testCreditCardOwnership.getCreditCardOwnershipCategoryCode()).isEqualTo(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE);
        assertThat(testCreditCardOwnership.getCreditCardOwnershipCategoryType()).isEqualTo(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE);
        assertThat(testCreditCardOwnership.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the CreditCardOwnership in Elasticsearch
        verify(mockCreditCardOwnershipSearchRepository, times(1)).save(testCreditCardOwnership);
    }

    @Test
    @Transactional
    void createCreditCardOwnershipWithExistingId() throws Exception {
        // Create the CreditCardOwnership with an existing ID
        creditCardOwnership.setId(1L);
        CreditCardOwnershipDTO creditCardOwnershipDTO = creditCardOwnershipMapper.toDto(creditCardOwnership);

        int databaseSizeBeforeCreate = creditCardOwnershipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditCardOwnershipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardOwnershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCardOwnership in the database
        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeCreate);

        // Validate the CreditCardOwnership in Elasticsearch
        verify(mockCreditCardOwnershipSearchRepository, times(0)).save(creditCardOwnership);
    }

    @Test
    @Transactional
    void checkCreditCardOwnershipCategoryCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardOwnershipRepository.findAll().size();
        // set the field null
        creditCardOwnership.setCreditCardOwnershipCategoryCode(null);

        // Create the CreditCardOwnership, which fails.
        CreditCardOwnershipDTO creditCardOwnershipDTO = creditCardOwnershipMapper.toDto(creditCardOwnership);

        restCreditCardOwnershipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardOwnershipDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreditCardOwnershipCategoryTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = creditCardOwnershipRepository.findAll().size();
        // set the field null
        creditCardOwnership.setCreditCardOwnershipCategoryType(null);

        // Create the CreditCardOwnership, which fails.
        CreditCardOwnershipDTO creditCardOwnershipDTO = creditCardOwnershipMapper.toDto(creditCardOwnership);

        restCreditCardOwnershipMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardOwnershipDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCreditCardOwnerships() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        // Get all the creditCardOwnershipList
        restCreditCardOwnershipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditCardOwnership.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditCardOwnershipCategoryCode").value(hasItem(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE)))
            .andExpect(
                jsonPath("$.[*].creditCardOwnershipCategoryType").value(hasItem(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE.toString()))
            )
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getCreditCardOwnership() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        // Get the creditCardOwnership
        restCreditCardOwnershipMockMvc
            .perform(get(ENTITY_API_URL_ID, creditCardOwnership.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(creditCardOwnership.getId().intValue()))
            .andExpect(jsonPath("$.creditCardOwnershipCategoryCode").value(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE))
            .andExpect(jsonPath("$.creditCardOwnershipCategoryType").value(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getCreditCardOwnershipsByIdFiltering() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        Long id = creditCardOwnership.getId();

        defaultCreditCardOwnershipShouldBeFound("id.equals=" + id);
        defaultCreditCardOwnershipShouldNotBeFound("id.notEquals=" + id);

        defaultCreditCardOwnershipShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCreditCardOwnershipShouldNotBeFound("id.greaterThan=" + id);

        defaultCreditCardOwnershipShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCreditCardOwnershipShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCreditCardOwnershipsByCreditCardOwnershipCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryCode equals to DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        defaultCreditCardOwnershipShouldBeFound("creditCardOwnershipCategoryCode.equals=" + DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryCode equals to UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        defaultCreditCardOwnershipShouldNotBeFound("creditCardOwnershipCategoryCode.equals=" + UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCreditCardOwnershipsByCreditCardOwnershipCategoryCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryCode not equals to DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        defaultCreditCardOwnershipShouldNotBeFound(
            "creditCardOwnershipCategoryCode.notEquals=" + DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        );

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryCode not equals to UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        defaultCreditCardOwnershipShouldBeFound("creditCardOwnershipCategoryCode.notEquals=" + UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCreditCardOwnershipsByCreditCardOwnershipCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryCode in DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE or UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        defaultCreditCardOwnershipShouldBeFound(
            "creditCardOwnershipCategoryCode.in=" +
            DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE +
            "," +
            UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        );

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryCode equals to UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        defaultCreditCardOwnershipShouldNotBeFound("creditCardOwnershipCategoryCode.in=" + UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllCreditCardOwnershipsByCreditCardOwnershipCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryCode is not null
        defaultCreditCardOwnershipShouldBeFound("creditCardOwnershipCategoryCode.specified=true");

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryCode is null
        defaultCreditCardOwnershipShouldNotBeFound("creditCardOwnershipCategoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCreditCardOwnershipsByCreditCardOwnershipCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryCode contains DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        defaultCreditCardOwnershipShouldBeFound("creditCardOwnershipCategoryCode.contains=" + DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryCode contains UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        defaultCreditCardOwnershipShouldNotBeFound(
            "creditCardOwnershipCategoryCode.contains=" + UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllCreditCardOwnershipsByCreditCardOwnershipCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryCode does not contain DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        defaultCreditCardOwnershipShouldNotBeFound(
            "creditCardOwnershipCategoryCode.doesNotContain=" + DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        );

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryCode does not contain UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        defaultCreditCardOwnershipShouldBeFound(
            "creditCardOwnershipCategoryCode.doesNotContain=" + UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE
        );
    }

    @Test
    @Transactional
    void getAllCreditCardOwnershipsByCreditCardOwnershipCategoryTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryType equals to DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE
        defaultCreditCardOwnershipShouldBeFound("creditCardOwnershipCategoryType.equals=" + DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryType equals to UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE
        defaultCreditCardOwnershipShouldNotBeFound("creditCardOwnershipCategoryType.equals=" + UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllCreditCardOwnershipsByCreditCardOwnershipCategoryTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryType not equals to DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE
        defaultCreditCardOwnershipShouldNotBeFound(
            "creditCardOwnershipCategoryType.notEquals=" + DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE
        );

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryType not equals to UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE
        defaultCreditCardOwnershipShouldBeFound("creditCardOwnershipCategoryType.notEquals=" + UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllCreditCardOwnershipsByCreditCardOwnershipCategoryTypeIsInShouldWork() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryType in DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE or UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE
        defaultCreditCardOwnershipShouldBeFound(
            "creditCardOwnershipCategoryType.in=" +
            DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE +
            "," +
            UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE
        );

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryType equals to UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE
        defaultCreditCardOwnershipShouldNotBeFound("creditCardOwnershipCategoryType.in=" + UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllCreditCardOwnershipsByCreditCardOwnershipCategoryTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryType is not null
        defaultCreditCardOwnershipShouldBeFound("creditCardOwnershipCategoryType.specified=true");

        // Get all the creditCardOwnershipList where creditCardOwnershipCategoryType is null
        defaultCreditCardOwnershipShouldNotBeFound("creditCardOwnershipCategoryType.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCreditCardOwnershipShouldBeFound(String filter) throws Exception {
        restCreditCardOwnershipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditCardOwnership.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditCardOwnershipCategoryCode").value(hasItem(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE)))
            .andExpect(
                jsonPath("$.[*].creditCardOwnershipCategoryType").value(hasItem(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE.toString()))
            )
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restCreditCardOwnershipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCreditCardOwnershipShouldNotBeFound(String filter) throws Exception {
        restCreditCardOwnershipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCreditCardOwnershipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCreditCardOwnership() throws Exception {
        // Get the creditCardOwnership
        restCreditCardOwnershipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCreditCardOwnership() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        int databaseSizeBeforeUpdate = creditCardOwnershipRepository.findAll().size();

        // Update the creditCardOwnership
        CreditCardOwnership updatedCreditCardOwnership = creditCardOwnershipRepository.findById(creditCardOwnership.getId()).get();
        // Disconnect from session so that the updates on updatedCreditCardOwnership are not directly saved in db
        em.detach(updatedCreditCardOwnership);
        updatedCreditCardOwnership
            .creditCardOwnershipCategoryCode(UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE)
            .creditCardOwnershipCategoryType(UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE)
            .description(UPDATED_DESCRIPTION);
        CreditCardOwnershipDTO creditCardOwnershipDTO = creditCardOwnershipMapper.toDto(updatedCreditCardOwnership);

        restCreditCardOwnershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creditCardOwnershipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardOwnershipDTO))
            )
            .andExpect(status().isOk());

        // Validate the CreditCardOwnership in the database
        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeUpdate);
        CreditCardOwnership testCreditCardOwnership = creditCardOwnershipList.get(creditCardOwnershipList.size() - 1);
        assertThat(testCreditCardOwnership.getCreditCardOwnershipCategoryCode()).isEqualTo(UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE);
        assertThat(testCreditCardOwnership.getCreditCardOwnershipCategoryType()).isEqualTo(UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE);
        assertThat(testCreditCardOwnership.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the CreditCardOwnership in Elasticsearch
        verify(mockCreditCardOwnershipSearchRepository).save(testCreditCardOwnership);
    }

    @Test
    @Transactional
    void putNonExistingCreditCardOwnership() throws Exception {
        int databaseSizeBeforeUpdate = creditCardOwnershipRepository.findAll().size();
        creditCardOwnership.setId(count.incrementAndGet());

        // Create the CreditCardOwnership
        CreditCardOwnershipDTO creditCardOwnershipDTO = creditCardOwnershipMapper.toDto(creditCardOwnership);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditCardOwnershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creditCardOwnershipDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardOwnershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCardOwnership in the database
        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCardOwnership in Elasticsearch
        verify(mockCreditCardOwnershipSearchRepository, times(0)).save(creditCardOwnership);
    }

    @Test
    @Transactional
    void putWithIdMismatchCreditCardOwnership() throws Exception {
        int databaseSizeBeforeUpdate = creditCardOwnershipRepository.findAll().size();
        creditCardOwnership.setId(count.incrementAndGet());

        // Create the CreditCardOwnership
        CreditCardOwnershipDTO creditCardOwnershipDTO = creditCardOwnershipMapper.toDto(creditCardOwnership);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardOwnershipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardOwnershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCardOwnership in the database
        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCardOwnership in Elasticsearch
        verify(mockCreditCardOwnershipSearchRepository, times(0)).save(creditCardOwnership);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCreditCardOwnership() throws Exception {
        int databaseSizeBeforeUpdate = creditCardOwnershipRepository.findAll().size();
        creditCardOwnership.setId(count.incrementAndGet());

        // Create the CreditCardOwnership
        CreditCardOwnershipDTO creditCardOwnershipDTO = creditCardOwnershipMapper.toDto(creditCardOwnership);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardOwnershipMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardOwnershipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreditCardOwnership in the database
        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCardOwnership in Elasticsearch
        verify(mockCreditCardOwnershipSearchRepository, times(0)).save(creditCardOwnership);
    }

    @Test
    @Transactional
    void partialUpdateCreditCardOwnershipWithPatch() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        int databaseSizeBeforeUpdate = creditCardOwnershipRepository.findAll().size();

        // Update the creditCardOwnership using partial update
        CreditCardOwnership partialUpdatedCreditCardOwnership = new CreditCardOwnership();
        partialUpdatedCreditCardOwnership.setId(creditCardOwnership.getId());

        partialUpdatedCreditCardOwnership
            .creditCardOwnershipCategoryCode(UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE)
            .description(UPDATED_DESCRIPTION);

        restCreditCardOwnershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditCardOwnership.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreditCardOwnership))
            )
            .andExpect(status().isOk());

        // Validate the CreditCardOwnership in the database
        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeUpdate);
        CreditCardOwnership testCreditCardOwnership = creditCardOwnershipList.get(creditCardOwnershipList.size() - 1);
        assertThat(testCreditCardOwnership.getCreditCardOwnershipCategoryCode()).isEqualTo(UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE);
        assertThat(testCreditCardOwnership.getCreditCardOwnershipCategoryType()).isEqualTo(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE);
        assertThat(testCreditCardOwnership.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCreditCardOwnershipWithPatch() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        int databaseSizeBeforeUpdate = creditCardOwnershipRepository.findAll().size();

        // Update the creditCardOwnership using partial update
        CreditCardOwnership partialUpdatedCreditCardOwnership = new CreditCardOwnership();
        partialUpdatedCreditCardOwnership.setId(creditCardOwnership.getId());

        partialUpdatedCreditCardOwnership
            .creditCardOwnershipCategoryCode(UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE)
            .creditCardOwnershipCategoryType(UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE)
            .description(UPDATED_DESCRIPTION);

        restCreditCardOwnershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditCardOwnership.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreditCardOwnership))
            )
            .andExpect(status().isOk());

        // Validate the CreditCardOwnership in the database
        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeUpdate);
        CreditCardOwnership testCreditCardOwnership = creditCardOwnershipList.get(creditCardOwnershipList.size() - 1);
        assertThat(testCreditCardOwnership.getCreditCardOwnershipCategoryCode()).isEqualTo(UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE);
        assertThat(testCreditCardOwnership.getCreditCardOwnershipCategoryType()).isEqualTo(UPDATED_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE);
        assertThat(testCreditCardOwnership.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCreditCardOwnership() throws Exception {
        int databaseSizeBeforeUpdate = creditCardOwnershipRepository.findAll().size();
        creditCardOwnership.setId(count.incrementAndGet());

        // Create the CreditCardOwnership
        CreditCardOwnershipDTO creditCardOwnershipDTO = creditCardOwnershipMapper.toDto(creditCardOwnership);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditCardOwnershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, creditCardOwnershipDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditCardOwnershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCardOwnership in the database
        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCardOwnership in Elasticsearch
        verify(mockCreditCardOwnershipSearchRepository, times(0)).save(creditCardOwnership);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCreditCardOwnership() throws Exception {
        int databaseSizeBeforeUpdate = creditCardOwnershipRepository.findAll().size();
        creditCardOwnership.setId(count.incrementAndGet());

        // Create the CreditCardOwnership
        CreditCardOwnershipDTO creditCardOwnershipDTO = creditCardOwnershipMapper.toDto(creditCardOwnership);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardOwnershipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditCardOwnershipDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCardOwnership in the database
        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCardOwnership in Elasticsearch
        verify(mockCreditCardOwnershipSearchRepository, times(0)).save(creditCardOwnership);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCreditCardOwnership() throws Exception {
        int databaseSizeBeforeUpdate = creditCardOwnershipRepository.findAll().size();
        creditCardOwnership.setId(count.incrementAndGet());

        // Create the CreditCardOwnership
        CreditCardOwnershipDTO creditCardOwnershipDTO = creditCardOwnershipMapper.toDto(creditCardOwnership);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardOwnershipMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditCardOwnershipDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreditCardOwnership in the database
        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CreditCardOwnership in Elasticsearch
        verify(mockCreditCardOwnershipSearchRepository, times(0)).save(creditCardOwnership);
    }

    @Test
    @Transactional
    void deleteCreditCardOwnership() throws Exception {
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);

        int databaseSizeBeforeDelete = creditCardOwnershipRepository.findAll().size();

        // Delete the creditCardOwnership
        restCreditCardOwnershipMockMvc
            .perform(delete(ENTITY_API_URL_ID, creditCardOwnership.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CreditCardOwnership> creditCardOwnershipList = creditCardOwnershipRepository.findAll();
        assertThat(creditCardOwnershipList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CreditCardOwnership in Elasticsearch
        verify(mockCreditCardOwnershipSearchRepository, times(1)).deleteById(creditCardOwnership.getId());
    }

    @Test
    @Transactional
    void searchCreditCardOwnership() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        creditCardOwnershipRepository.saveAndFlush(creditCardOwnership);
        when(mockCreditCardOwnershipSearchRepository.search("id:" + creditCardOwnership.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(creditCardOwnership), PageRequest.of(0, 1), 1));

        // Search the creditCardOwnership
        restCreditCardOwnershipMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + creditCardOwnership.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditCardOwnership.getId().intValue())))
            .andExpect(jsonPath("$.[*].creditCardOwnershipCategoryCode").value(hasItem(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_CODE)))
            .andExpect(
                jsonPath("$.[*].creditCardOwnershipCategoryType").value(hasItem(DEFAULT_CREDIT_CARD_OWNERSHIP_CATEGORY_TYPE.toString()))
            )
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
