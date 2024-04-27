package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.ReasonsForBouncedCheque;
import io.github.erp.repository.ReasonsForBouncedChequeRepository;
import io.github.erp.repository.search.ReasonsForBouncedChequeSearchRepository;
import io.github.erp.service.criteria.ReasonsForBouncedChequeCriteria;
import io.github.erp.service.dto.ReasonsForBouncedChequeDTO;
import io.github.erp.service.mapper.ReasonsForBouncedChequeMapper;
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

/**
 * Integration tests for the {@link ReasonsForBouncedChequeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReasonsForBouncedChequeResourceIT {

    private static final String DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BOUNCED_CHEQUE_REASONS_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reasons-for-bounced-cheques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/reasons-for-bounced-cheques";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReasonsForBouncedChequeRepository reasonsForBouncedChequeRepository;

    @Autowired
    private ReasonsForBouncedChequeMapper reasonsForBouncedChequeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ReasonsForBouncedChequeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReasonsForBouncedChequeSearchRepository mockReasonsForBouncedChequeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReasonsForBouncedChequeMockMvc;

    private ReasonsForBouncedCheque reasonsForBouncedCheque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReasonsForBouncedCheque createEntity(EntityManager em) {
        ReasonsForBouncedCheque reasonsForBouncedCheque = new ReasonsForBouncedCheque()
            .bouncedChequeReasonsTypeCode(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE)
            .bouncedChequeReasonsType(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE);
        return reasonsForBouncedCheque;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReasonsForBouncedCheque createUpdatedEntity(EntityManager em) {
        ReasonsForBouncedCheque reasonsForBouncedCheque = new ReasonsForBouncedCheque()
            .bouncedChequeReasonsTypeCode(UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE)
            .bouncedChequeReasonsType(UPDATED_BOUNCED_CHEQUE_REASONS_TYPE);
        return reasonsForBouncedCheque;
    }

    @BeforeEach
    public void initTest() {
        reasonsForBouncedCheque = createEntity(em);
    }

    @Test
    @Transactional
    void createReasonsForBouncedCheque() throws Exception {
        int databaseSizeBeforeCreate = reasonsForBouncedChequeRepository.findAll().size();
        // Create the ReasonsForBouncedCheque
        ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO = reasonsForBouncedChequeMapper.toDto(reasonsForBouncedCheque);
        restReasonsForBouncedChequeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reasonsForBouncedChequeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ReasonsForBouncedCheque in the database
        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeCreate + 1);
        ReasonsForBouncedCheque testReasonsForBouncedCheque = reasonsForBouncedChequeList.get(reasonsForBouncedChequeList.size() - 1);
        assertThat(testReasonsForBouncedCheque.getBouncedChequeReasonsTypeCode()).isEqualTo(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE);
        assertThat(testReasonsForBouncedCheque.getBouncedChequeReasonsType()).isEqualTo(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE);

        // Validate the ReasonsForBouncedCheque in Elasticsearch
        verify(mockReasonsForBouncedChequeSearchRepository, times(1)).save(testReasonsForBouncedCheque);
    }

    @Test
    @Transactional
    void createReasonsForBouncedChequeWithExistingId() throws Exception {
        // Create the ReasonsForBouncedCheque with an existing ID
        reasonsForBouncedCheque.setId(1L);
        ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO = reasonsForBouncedChequeMapper.toDto(reasonsForBouncedCheque);

        int databaseSizeBeforeCreate = reasonsForBouncedChequeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReasonsForBouncedChequeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reasonsForBouncedChequeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReasonsForBouncedCheque in the database
        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReasonsForBouncedCheque in Elasticsearch
        verify(mockReasonsForBouncedChequeSearchRepository, times(0)).save(reasonsForBouncedCheque);
    }

    @Test
    @Transactional
    void checkBouncedChequeReasonsTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = reasonsForBouncedChequeRepository.findAll().size();
        // set the field null
        reasonsForBouncedCheque.setBouncedChequeReasonsTypeCode(null);

        // Create the ReasonsForBouncedCheque, which fails.
        ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO = reasonsForBouncedChequeMapper.toDto(reasonsForBouncedCheque);

        restReasonsForBouncedChequeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reasonsForBouncedChequeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedCheques() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList
        restReasonsForBouncedChequeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reasonsForBouncedCheque.getId().intValue())))
            .andExpect(jsonPath("$.[*].bouncedChequeReasonsTypeCode").value(hasItem(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].bouncedChequeReasonsType").value(hasItem(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE)));
    }

    @Test
    @Transactional
    void getReasonsForBouncedCheque() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get the reasonsForBouncedCheque
        restReasonsForBouncedChequeMockMvc
            .perform(get(ENTITY_API_URL_ID, reasonsForBouncedCheque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reasonsForBouncedCheque.getId().intValue()))
            .andExpect(jsonPath("$.bouncedChequeReasonsTypeCode").value(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE))
            .andExpect(jsonPath("$.bouncedChequeReasonsType").value(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE));
    }

    @Test
    @Transactional
    void getReasonsForBouncedChequesByIdFiltering() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        Long id = reasonsForBouncedCheque.getId();

        defaultReasonsForBouncedChequeShouldBeFound("id.equals=" + id);
        defaultReasonsForBouncedChequeShouldNotBeFound("id.notEquals=" + id);

        defaultReasonsForBouncedChequeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReasonsForBouncedChequeShouldNotBeFound("id.greaterThan=" + id);

        defaultReasonsForBouncedChequeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReasonsForBouncedChequeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedChequesByBouncedChequeReasonsTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsTypeCode equals to DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        defaultReasonsForBouncedChequeShouldBeFound("bouncedChequeReasonsTypeCode.equals=" + DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsTypeCode equals to UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        defaultReasonsForBouncedChequeShouldNotBeFound("bouncedChequeReasonsTypeCode.equals=" + UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedChequesByBouncedChequeReasonsTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsTypeCode not equals to DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        defaultReasonsForBouncedChequeShouldNotBeFound(
            "bouncedChequeReasonsTypeCode.notEquals=" + DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        );

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsTypeCode not equals to UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        defaultReasonsForBouncedChequeShouldBeFound("bouncedChequeReasonsTypeCode.notEquals=" + UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedChequesByBouncedChequeReasonsTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsTypeCode in DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE or UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        defaultReasonsForBouncedChequeShouldBeFound(
            "bouncedChequeReasonsTypeCode.in=" + DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE + "," + UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        );

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsTypeCode equals to UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        defaultReasonsForBouncedChequeShouldNotBeFound("bouncedChequeReasonsTypeCode.in=" + UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedChequesByBouncedChequeReasonsTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsTypeCode is not null
        defaultReasonsForBouncedChequeShouldBeFound("bouncedChequeReasonsTypeCode.specified=true");

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsTypeCode is null
        defaultReasonsForBouncedChequeShouldNotBeFound("bouncedChequeReasonsTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedChequesByBouncedChequeReasonsTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsTypeCode contains DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        defaultReasonsForBouncedChequeShouldBeFound("bouncedChequeReasonsTypeCode.contains=" + DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsTypeCode contains UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        defaultReasonsForBouncedChequeShouldNotBeFound("bouncedChequeReasonsTypeCode.contains=" + UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedChequesByBouncedChequeReasonsTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsTypeCode does not contain DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        defaultReasonsForBouncedChequeShouldNotBeFound(
            "bouncedChequeReasonsTypeCode.doesNotContain=" + DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        );

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsTypeCode does not contain UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        defaultReasonsForBouncedChequeShouldBeFound(
            "bouncedChequeReasonsTypeCode.doesNotContain=" + UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE
        );
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedChequesByBouncedChequeReasonsTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsType equals to DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE
        defaultReasonsForBouncedChequeShouldBeFound("bouncedChequeReasonsType.equals=" + DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsType equals to UPDATED_BOUNCED_CHEQUE_REASONS_TYPE
        defaultReasonsForBouncedChequeShouldNotBeFound("bouncedChequeReasonsType.equals=" + UPDATED_BOUNCED_CHEQUE_REASONS_TYPE);
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedChequesByBouncedChequeReasonsTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsType not equals to DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE
        defaultReasonsForBouncedChequeShouldNotBeFound("bouncedChequeReasonsType.notEquals=" + DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsType not equals to UPDATED_BOUNCED_CHEQUE_REASONS_TYPE
        defaultReasonsForBouncedChequeShouldBeFound("bouncedChequeReasonsType.notEquals=" + UPDATED_BOUNCED_CHEQUE_REASONS_TYPE);
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedChequesByBouncedChequeReasonsTypeIsInShouldWork() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsType in DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE or UPDATED_BOUNCED_CHEQUE_REASONS_TYPE
        defaultReasonsForBouncedChequeShouldBeFound(
            "bouncedChequeReasonsType.in=" + DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE + "," + UPDATED_BOUNCED_CHEQUE_REASONS_TYPE
        );

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsType equals to UPDATED_BOUNCED_CHEQUE_REASONS_TYPE
        defaultReasonsForBouncedChequeShouldNotBeFound("bouncedChequeReasonsType.in=" + UPDATED_BOUNCED_CHEQUE_REASONS_TYPE);
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedChequesByBouncedChequeReasonsTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsType is not null
        defaultReasonsForBouncedChequeShouldBeFound("bouncedChequeReasonsType.specified=true");

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsType is null
        defaultReasonsForBouncedChequeShouldNotBeFound("bouncedChequeReasonsType.specified=false");
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedChequesByBouncedChequeReasonsTypeContainsSomething() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsType contains DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE
        defaultReasonsForBouncedChequeShouldBeFound("bouncedChequeReasonsType.contains=" + DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsType contains UPDATED_BOUNCED_CHEQUE_REASONS_TYPE
        defaultReasonsForBouncedChequeShouldNotBeFound("bouncedChequeReasonsType.contains=" + UPDATED_BOUNCED_CHEQUE_REASONS_TYPE);
    }

    @Test
    @Transactional
    void getAllReasonsForBouncedChequesByBouncedChequeReasonsTypeNotContainsSomething() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsType does not contain DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE
        defaultReasonsForBouncedChequeShouldNotBeFound("bouncedChequeReasonsType.doesNotContain=" + DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE);

        // Get all the reasonsForBouncedChequeList where bouncedChequeReasonsType does not contain UPDATED_BOUNCED_CHEQUE_REASONS_TYPE
        defaultReasonsForBouncedChequeShouldBeFound("bouncedChequeReasonsType.doesNotContain=" + UPDATED_BOUNCED_CHEQUE_REASONS_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReasonsForBouncedChequeShouldBeFound(String filter) throws Exception {
        restReasonsForBouncedChequeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reasonsForBouncedCheque.getId().intValue())))
            .andExpect(jsonPath("$.[*].bouncedChequeReasonsTypeCode").value(hasItem(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].bouncedChequeReasonsType").value(hasItem(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE)));

        // Check, that the count call also returns 1
        restReasonsForBouncedChequeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReasonsForBouncedChequeShouldNotBeFound(String filter) throws Exception {
        restReasonsForBouncedChequeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReasonsForBouncedChequeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReasonsForBouncedCheque() throws Exception {
        // Get the reasonsForBouncedCheque
        restReasonsForBouncedChequeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReasonsForBouncedCheque() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        int databaseSizeBeforeUpdate = reasonsForBouncedChequeRepository.findAll().size();

        // Update the reasonsForBouncedCheque
        ReasonsForBouncedCheque updatedReasonsForBouncedCheque = reasonsForBouncedChequeRepository
            .findById(reasonsForBouncedCheque.getId())
            .get();
        // Disconnect from session so that the updates on updatedReasonsForBouncedCheque are not directly saved in db
        em.detach(updatedReasonsForBouncedCheque);
        updatedReasonsForBouncedCheque
            .bouncedChequeReasonsTypeCode(UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE)
            .bouncedChequeReasonsType(UPDATED_BOUNCED_CHEQUE_REASONS_TYPE);
        ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO = reasonsForBouncedChequeMapper.toDto(updatedReasonsForBouncedCheque);

        restReasonsForBouncedChequeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reasonsForBouncedChequeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reasonsForBouncedChequeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReasonsForBouncedCheque in the database
        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeUpdate);
        ReasonsForBouncedCheque testReasonsForBouncedCheque = reasonsForBouncedChequeList.get(reasonsForBouncedChequeList.size() - 1);
        assertThat(testReasonsForBouncedCheque.getBouncedChequeReasonsTypeCode()).isEqualTo(UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE);
        assertThat(testReasonsForBouncedCheque.getBouncedChequeReasonsType()).isEqualTo(UPDATED_BOUNCED_CHEQUE_REASONS_TYPE);

        // Validate the ReasonsForBouncedCheque in Elasticsearch
        verify(mockReasonsForBouncedChequeSearchRepository).save(testReasonsForBouncedCheque);
    }

    @Test
    @Transactional
    void putNonExistingReasonsForBouncedCheque() throws Exception {
        int databaseSizeBeforeUpdate = reasonsForBouncedChequeRepository.findAll().size();
        reasonsForBouncedCheque.setId(count.incrementAndGet());

        // Create the ReasonsForBouncedCheque
        ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO = reasonsForBouncedChequeMapper.toDto(reasonsForBouncedCheque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReasonsForBouncedChequeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reasonsForBouncedChequeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reasonsForBouncedChequeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReasonsForBouncedCheque in the database
        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReasonsForBouncedCheque in Elasticsearch
        verify(mockReasonsForBouncedChequeSearchRepository, times(0)).save(reasonsForBouncedCheque);
    }

    @Test
    @Transactional
    void putWithIdMismatchReasonsForBouncedCheque() throws Exception {
        int databaseSizeBeforeUpdate = reasonsForBouncedChequeRepository.findAll().size();
        reasonsForBouncedCheque.setId(count.incrementAndGet());

        // Create the ReasonsForBouncedCheque
        ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO = reasonsForBouncedChequeMapper.toDto(reasonsForBouncedCheque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReasonsForBouncedChequeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reasonsForBouncedChequeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReasonsForBouncedCheque in the database
        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReasonsForBouncedCheque in Elasticsearch
        verify(mockReasonsForBouncedChequeSearchRepository, times(0)).save(reasonsForBouncedCheque);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReasonsForBouncedCheque() throws Exception {
        int databaseSizeBeforeUpdate = reasonsForBouncedChequeRepository.findAll().size();
        reasonsForBouncedCheque.setId(count.incrementAndGet());

        // Create the ReasonsForBouncedCheque
        ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO = reasonsForBouncedChequeMapper.toDto(reasonsForBouncedCheque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReasonsForBouncedChequeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reasonsForBouncedChequeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReasonsForBouncedCheque in the database
        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReasonsForBouncedCheque in Elasticsearch
        verify(mockReasonsForBouncedChequeSearchRepository, times(0)).save(reasonsForBouncedCheque);
    }

    @Test
    @Transactional
    void partialUpdateReasonsForBouncedChequeWithPatch() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        int databaseSizeBeforeUpdate = reasonsForBouncedChequeRepository.findAll().size();

        // Update the reasonsForBouncedCheque using partial update
        ReasonsForBouncedCheque partialUpdatedReasonsForBouncedCheque = new ReasonsForBouncedCheque();
        partialUpdatedReasonsForBouncedCheque.setId(reasonsForBouncedCheque.getId());

        restReasonsForBouncedChequeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReasonsForBouncedCheque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReasonsForBouncedCheque))
            )
            .andExpect(status().isOk());

        // Validate the ReasonsForBouncedCheque in the database
        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeUpdate);
        ReasonsForBouncedCheque testReasonsForBouncedCheque = reasonsForBouncedChequeList.get(reasonsForBouncedChequeList.size() - 1);
        assertThat(testReasonsForBouncedCheque.getBouncedChequeReasonsTypeCode()).isEqualTo(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE);
        assertThat(testReasonsForBouncedCheque.getBouncedChequeReasonsType()).isEqualTo(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateReasonsForBouncedChequeWithPatch() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        int databaseSizeBeforeUpdate = reasonsForBouncedChequeRepository.findAll().size();

        // Update the reasonsForBouncedCheque using partial update
        ReasonsForBouncedCheque partialUpdatedReasonsForBouncedCheque = new ReasonsForBouncedCheque();
        partialUpdatedReasonsForBouncedCheque.setId(reasonsForBouncedCheque.getId());

        partialUpdatedReasonsForBouncedCheque
            .bouncedChequeReasonsTypeCode(UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE)
            .bouncedChequeReasonsType(UPDATED_BOUNCED_CHEQUE_REASONS_TYPE);

        restReasonsForBouncedChequeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReasonsForBouncedCheque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReasonsForBouncedCheque))
            )
            .andExpect(status().isOk());

        // Validate the ReasonsForBouncedCheque in the database
        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeUpdate);
        ReasonsForBouncedCheque testReasonsForBouncedCheque = reasonsForBouncedChequeList.get(reasonsForBouncedChequeList.size() - 1);
        assertThat(testReasonsForBouncedCheque.getBouncedChequeReasonsTypeCode()).isEqualTo(UPDATED_BOUNCED_CHEQUE_REASONS_TYPE_CODE);
        assertThat(testReasonsForBouncedCheque.getBouncedChequeReasonsType()).isEqualTo(UPDATED_BOUNCED_CHEQUE_REASONS_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingReasonsForBouncedCheque() throws Exception {
        int databaseSizeBeforeUpdate = reasonsForBouncedChequeRepository.findAll().size();
        reasonsForBouncedCheque.setId(count.incrementAndGet());

        // Create the ReasonsForBouncedCheque
        ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO = reasonsForBouncedChequeMapper.toDto(reasonsForBouncedCheque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReasonsForBouncedChequeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reasonsForBouncedChequeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reasonsForBouncedChequeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReasonsForBouncedCheque in the database
        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReasonsForBouncedCheque in Elasticsearch
        verify(mockReasonsForBouncedChequeSearchRepository, times(0)).save(reasonsForBouncedCheque);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReasonsForBouncedCheque() throws Exception {
        int databaseSizeBeforeUpdate = reasonsForBouncedChequeRepository.findAll().size();
        reasonsForBouncedCheque.setId(count.incrementAndGet());

        // Create the ReasonsForBouncedCheque
        ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO = reasonsForBouncedChequeMapper.toDto(reasonsForBouncedCheque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReasonsForBouncedChequeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reasonsForBouncedChequeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReasonsForBouncedCheque in the database
        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReasonsForBouncedCheque in Elasticsearch
        verify(mockReasonsForBouncedChequeSearchRepository, times(0)).save(reasonsForBouncedCheque);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReasonsForBouncedCheque() throws Exception {
        int databaseSizeBeforeUpdate = reasonsForBouncedChequeRepository.findAll().size();
        reasonsForBouncedCheque.setId(count.incrementAndGet());

        // Create the ReasonsForBouncedCheque
        ReasonsForBouncedChequeDTO reasonsForBouncedChequeDTO = reasonsForBouncedChequeMapper.toDto(reasonsForBouncedCheque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReasonsForBouncedChequeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reasonsForBouncedChequeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReasonsForBouncedCheque in the database
        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReasonsForBouncedCheque in Elasticsearch
        verify(mockReasonsForBouncedChequeSearchRepository, times(0)).save(reasonsForBouncedCheque);
    }

    @Test
    @Transactional
    void deleteReasonsForBouncedCheque() throws Exception {
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);

        int databaseSizeBeforeDelete = reasonsForBouncedChequeRepository.findAll().size();

        // Delete the reasonsForBouncedCheque
        restReasonsForBouncedChequeMockMvc
            .perform(delete(ENTITY_API_URL_ID, reasonsForBouncedCheque.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReasonsForBouncedCheque> reasonsForBouncedChequeList = reasonsForBouncedChequeRepository.findAll();
        assertThat(reasonsForBouncedChequeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReasonsForBouncedCheque in Elasticsearch
        verify(mockReasonsForBouncedChequeSearchRepository, times(1)).deleteById(reasonsForBouncedCheque.getId());
    }

    @Test
    @Transactional
    void searchReasonsForBouncedCheque() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        reasonsForBouncedChequeRepository.saveAndFlush(reasonsForBouncedCheque);
        when(mockReasonsForBouncedChequeSearchRepository.search("id:" + reasonsForBouncedCheque.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reasonsForBouncedCheque), PageRequest.of(0, 1), 1));

        // Search the reasonsForBouncedCheque
        restReasonsForBouncedChequeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + reasonsForBouncedCheque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reasonsForBouncedCheque.getId().intValue())))
            .andExpect(jsonPath("$.[*].bouncedChequeReasonsTypeCode").value(hasItem(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].bouncedChequeReasonsType").value(hasItem(DEFAULT_BOUNCED_CHEQUE_REASONS_TYPE)));
    }
}
