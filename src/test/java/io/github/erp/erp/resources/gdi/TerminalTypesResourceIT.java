package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.TerminalTypes;
import io.github.erp.repository.TerminalTypesRepository;
import io.github.erp.repository.search.TerminalTypesSearchRepository;
import io.github.erp.service.dto.TerminalTypesDTO;
import io.github.erp.service.mapper.TerminalTypesMapper;
import io.github.erp.web.rest.TerminalTypesResource;
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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TerminalTypesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class TerminalTypesResourceIT {

    private static final String DEFAULT_TXN_TERMINAL_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TXN_TERMINAL_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TXN_CHANNEL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TXN_CHANNEL_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_TXN_CHANNEL_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_TXN_CHANNEL_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/terminal-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/terminal-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TerminalTypesRepository terminalTypesRepository;

    @Autowired
    private TerminalTypesMapper terminalTypesMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TerminalTypesSearchRepositoryMockConfiguration
     */
    @Autowired
    private TerminalTypesSearchRepository mockTerminalTypesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTerminalTypesMockMvc;

    private TerminalTypes terminalTypes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TerminalTypes createEntity(EntityManager em) {
        TerminalTypes terminalTypes = new TerminalTypes()
            .txnTerminalTypeCode(DEFAULT_TXN_TERMINAL_TYPE_CODE)
            .txnChannelType(DEFAULT_TXN_CHANNEL_TYPE)
            .txnChannelTypeDetails(DEFAULT_TXN_CHANNEL_TYPE_DETAILS);
        return terminalTypes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TerminalTypes createUpdatedEntity(EntityManager em) {
        TerminalTypes terminalTypes = new TerminalTypes()
            .txnTerminalTypeCode(UPDATED_TXN_TERMINAL_TYPE_CODE)
            .txnChannelType(UPDATED_TXN_CHANNEL_TYPE)
            .txnChannelTypeDetails(UPDATED_TXN_CHANNEL_TYPE_DETAILS);
        return terminalTypes;
    }

    @BeforeEach
    public void initTest() {
        terminalTypes = createEntity(em);
    }

    @Test
    @Transactional
    void createTerminalTypes() throws Exception {
        int databaseSizeBeforeCreate = terminalTypesRepository.findAll().size();
        // Create the TerminalTypes
        TerminalTypesDTO terminalTypesDTO = terminalTypesMapper.toDto(terminalTypes);
        restTerminalTypesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalTypesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TerminalTypes in the database
        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeCreate + 1);
        TerminalTypes testTerminalTypes = terminalTypesList.get(terminalTypesList.size() - 1);
        assertThat(testTerminalTypes.getTxnTerminalTypeCode()).isEqualTo(DEFAULT_TXN_TERMINAL_TYPE_CODE);
        assertThat(testTerminalTypes.getTxnChannelType()).isEqualTo(DEFAULT_TXN_CHANNEL_TYPE);
        assertThat(testTerminalTypes.getTxnChannelTypeDetails()).isEqualTo(DEFAULT_TXN_CHANNEL_TYPE_DETAILS);

        // Validate the TerminalTypes in Elasticsearch
        verify(mockTerminalTypesSearchRepository, times(1)).save(testTerminalTypes);
    }

    @Test
    @Transactional
    void createTerminalTypesWithExistingId() throws Exception {
        // Create the TerminalTypes with an existing ID
        terminalTypes.setId(1L);
        TerminalTypesDTO terminalTypesDTO = terminalTypesMapper.toDto(terminalTypes);

        int databaseSizeBeforeCreate = terminalTypesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTerminalTypesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalTypes in the database
        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeCreate);

        // Validate the TerminalTypes in Elasticsearch
        verify(mockTerminalTypesSearchRepository, times(0)).save(terminalTypes);
    }

    @Test
    @Transactional
    void checkTxnTerminalTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalTypesRepository.findAll().size();
        // set the field null
        terminalTypes.setTxnTerminalTypeCode(null);

        // Create the TerminalTypes, which fails.
        TerminalTypesDTO terminalTypesDTO = terminalTypesMapper.toDto(terminalTypes);

        restTerminalTypesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalTypesDTO))
            )
            .andExpect(status().isBadRequest());

        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTxnChannelTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalTypesRepository.findAll().size();
        // set the field null
        terminalTypes.setTxnChannelType(null);

        // Create the TerminalTypes, which fails.
        TerminalTypesDTO terminalTypesDTO = terminalTypesMapper.toDto(terminalTypes);

        restTerminalTypesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalTypesDTO))
            )
            .andExpect(status().isBadRequest());

        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTerminalTypes() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList
        restTerminalTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminalTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].txnTerminalTypeCode").value(hasItem(DEFAULT_TXN_TERMINAL_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].txnChannelType").value(hasItem(DEFAULT_TXN_CHANNEL_TYPE)))
            .andExpect(jsonPath("$.[*].txnChannelTypeDetails").value(hasItem(DEFAULT_TXN_CHANNEL_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getTerminalTypes() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get the terminalTypes
        restTerminalTypesMockMvc
            .perform(get(ENTITY_API_URL_ID, terminalTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(terminalTypes.getId().intValue()))
            .andExpect(jsonPath("$.txnTerminalTypeCode").value(DEFAULT_TXN_TERMINAL_TYPE_CODE))
            .andExpect(jsonPath("$.txnChannelType").value(DEFAULT_TXN_CHANNEL_TYPE))
            .andExpect(jsonPath("$.txnChannelTypeDetails").value(DEFAULT_TXN_CHANNEL_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getTerminalTypesByIdFiltering() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        Long id = terminalTypes.getId();

        defaultTerminalTypesShouldBeFound("id.equals=" + id);
        defaultTerminalTypesShouldNotBeFound("id.notEquals=" + id);

        defaultTerminalTypesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTerminalTypesShouldNotBeFound("id.greaterThan=" + id);

        defaultTerminalTypesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTerminalTypesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTerminalTypesByTxnTerminalTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList where txnTerminalTypeCode equals to DEFAULT_TXN_TERMINAL_TYPE_CODE
        defaultTerminalTypesShouldBeFound("txnTerminalTypeCode.equals=" + DEFAULT_TXN_TERMINAL_TYPE_CODE);

        // Get all the terminalTypesList where txnTerminalTypeCode equals to UPDATED_TXN_TERMINAL_TYPE_CODE
        defaultTerminalTypesShouldNotBeFound("txnTerminalTypeCode.equals=" + UPDATED_TXN_TERMINAL_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllTerminalTypesByTxnTerminalTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList where txnTerminalTypeCode not equals to DEFAULT_TXN_TERMINAL_TYPE_CODE
        defaultTerminalTypesShouldNotBeFound("txnTerminalTypeCode.notEquals=" + DEFAULT_TXN_TERMINAL_TYPE_CODE);

        // Get all the terminalTypesList where txnTerminalTypeCode not equals to UPDATED_TXN_TERMINAL_TYPE_CODE
        defaultTerminalTypesShouldBeFound("txnTerminalTypeCode.notEquals=" + UPDATED_TXN_TERMINAL_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllTerminalTypesByTxnTerminalTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList where txnTerminalTypeCode in DEFAULT_TXN_TERMINAL_TYPE_CODE or UPDATED_TXN_TERMINAL_TYPE_CODE
        defaultTerminalTypesShouldBeFound(
            "txnTerminalTypeCode.in=" + DEFAULT_TXN_TERMINAL_TYPE_CODE + "," + UPDATED_TXN_TERMINAL_TYPE_CODE
        );

        // Get all the terminalTypesList where txnTerminalTypeCode equals to UPDATED_TXN_TERMINAL_TYPE_CODE
        defaultTerminalTypesShouldNotBeFound("txnTerminalTypeCode.in=" + UPDATED_TXN_TERMINAL_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllTerminalTypesByTxnTerminalTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList where txnTerminalTypeCode is not null
        defaultTerminalTypesShouldBeFound("txnTerminalTypeCode.specified=true");

        // Get all the terminalTypesList where txnTerminalTypeCode is null
        defaultTerminalTypesShouldNotBeFound("txnTerminalTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalTypesByTxnTerminalTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList where txnTerminalTypeCode contains DEFAULT_TXN_TERMINAL_TYPE_CODE
        defaultTerminalTypesShouldBeFound("txnTerminalTypeCode.contains=" + DEFAULT_TXN_TERMINAL_TYPE_CODE);

        // Get all the terminalTypesList where txnTerminalTypeCode contains UPDATED_TXN_TERMINAL_TYPE_CODE
        defaultTerminalTypesShouldNotBeFound("txnTerminalTypeCode.contains=" + UPDATED_TXN_TERMINAL_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllTerminalTypesByTxnTerminalTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList where txnTerminalTypeCode does not contain DEFAULT_TXN_TERMINAL_TYPE_CODE
        defaultTerminalTypesShouldNotBeFound("txnTerminalTypeCode.doesNotContain=" + DEFAULT_TXN_TERMINAL_TYPE_CODE);

        // Get all the terminalTypesList where txnTerminalTypeCode does not contain UPDATED_TXN_TERMINAL_TYPE_CODE
        defaultTerminalTypesShouldBeFound("txnTerminalTypeCode.doesNotContain=" + UPDATED_TXN_TERMINAL_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllTerminalTypesByTxnChannelTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList where txnChannelType equals to DEFAULT_TXN_CHANNEL_TYPE
        defaultTerminalTypesShouldBeFound("txnChannelType.equals=" + DEFAULT_TXN_CHANNEL_TYPE);

        // Get all the terminalTypesList where txnChannelType equals to UPDATED_TXN_CHANNEL_TYPE
        defaultTerminalTypesShouldNotBeFound("txnChannelType.equals=" + UPDATED_TXN_CHANNEL_TYPE);
    }

    @Test
    @Transactional
    void getAllTerminalTypesByTxnChannelTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList where txnChannelType not equals to DEFAULT_TXN_CHANNEL_TYPE
        defaultTerminalTypesShouldNotBeFound("txnChannelType.notEquals=" + DEFAULT_TXN_CHANNEL_TYPE);

        // Get all the terminalTypesList where txnChannelType not equals to UPDATED_TXN_CHANNEL_TYPE
        defaultTerminalTypesShouldBeFound("txnChannelType.notEquals=" + UPDATED_TXN_CHANNEL_TYPE);
    }

    @Test
    @Transactional
    void getAllTerminalTypesByTxnChannelTypeIsInShouldWork() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList where txnChannelType in DEFAULT_TXN_CHANNEL_TYPE or UPDATED_TXN_CHANNEL_TYPE
        defaultTerminalTypesShouldBeFound("txnChannelType.in=" + DEFAULT_TXN_CHANNEL_TYPE + "," + UPDATED_TXN_CHANNEL_TYPE);

        // Get all the terminalTypesList where txnChannelType equals to UPDATED_TXN_CHANNEL_TYPE
        defaultTerminalTypesShouldNotBeFound("txnChannelType.in=" + UPDATED_TXN_CHANNEL_TYPE);
    }

    @Test
    @Transactional
    void getAllTerminalTypesByTxnChannelTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList where txnChannelType is not null
        defaultTerminalTypesShouldBeFound("txnChannelType.specified=true");

        // Get all the terminalTypesList where txnChannelType is null
        defaultTerminalTypesShouldNotBeFound("txnChannelType.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalTypesByTxnChannelTypeContainsSomething() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList where txnChannelType contains DEFAULT_TXN_CHANNEL_TYPE
        defaultTerminalTypesShouldBeFound("txnChannelType.contains=" + DEFAULT_TXN_CHANNEL_TYPE);

        // Get all the terminalTypesList where txnChannelType contains UPDATED_TXN_CHANNEL_TYPE
        defaultTerminalTypesShouldNotBeFound("txnChannelType.contains=" + UPDATED_TXN_CHANNEL_TYPE);
    }

    @Test
    @Transactional
    void getAllTerminalTypesByTxnChannelTypeNotContainsSomething() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        // Get all the terminalTypesList where txnChannelType does not contain DEFAULT_TXN_CHANNEL_TYPE
        defaultTerminalTypesShouldNotBeFound("txnChannelType.doesNotContain=" + DEFAULT_TXN_CHANNEL_TYPE);

        // Get all the terminalTypesList where txnChannelType does not contain UPDATED_TXN_CHANNEL_TYPE
        defaultTerminalTypesShouldBeFound("txnChannelType.doesNotContain=" + UPDATED_TXN_CHANNEL_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTerminalTypesShouldBeFound(String filter) throws Exception {
        restTerminalTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminalTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].txnTerminalTypeCode").value(hasItem(DEFAULT_TXN_TERMINAL_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].txnChannelType").value(hasItem(DEFAULT_TXN_CHANNEL_TYPE)))
            .andExpect(jsonPath("$.[*].txnChannelTypeDetails").value(hasItem(DEFAULT_TXN_CHANNEL_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restTerminalTypesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTerminalTypesShouldNotBeFound(String filter) throws Exception {
        restTerminalTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTerminalTypesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTerminalTypes() throws Exception {
        // Get the terminalTypes
        restTerminalTypesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTerminalTypes() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        int databaseSizeBeforeUpdate = terminalTypesRepository.findAll().size();

        // Update the terminalTypes
        TerminalTypes updatedTerminalTypes = terminalTypesRepository.findById(terminalTypes.getId()).get();
        // Disconnect from session so that the updates on updatedTerminalTypes are not directly saved in db
        em.detach(updatedTerminalTypes);
        updatedTerminalTypes
            .txnTerminalTypeCode(UPDATED_TXN_TERMINAL_TYPE_CODE)
            .txnChannelType(UPDATED_TXN_CHANNEL_TYPE)
            .txnChannelTypeDetails(UPDATED_TXN_CHANNEL_TYPE_DETAILS);
        TerminalTypesDTO terminalTypesDTO = terminalTypesMapper.toDto(updatedTerminalTypes);

        restTerminalTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terminalTypesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalTypesDTO))
            )
            .andExpect(status().isOk());

        // Validate the TerminalTypes in the database
        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeUpdate);
        TerminalTypes testTerminalTypes = terminalTypesList.get(terminalTypesList.size() - 1);
        assertThat(testTerminalTypes.getTxnTerminalTypeCode()).isEqualTo(UPDATED_TXN_TERMINAL_TYPE_CODE);
        assertThat(testTerminalTypes.getTxnChannelType()).isEqualTo(UPDATED_TXN_CHANNEL_TYPE);
        assertThat(testTerminalTypes.getTxnChannelTypeDetails()).isEqualTo(UPDATED_TXN_CHANNEL_TYPE_DETAILS);

        // Validate the TerminalTypes in Elasticsearch
        verify(mockTerminalTypesSearchRepository).save(testTerminalTypes);
    }

    @Test
    @Transactional
    void putNonExistingTerminalTypes() throws Exception {
        int databaseSizeBeforeUpdate = terminalTypesRepository.findAll().size();
        terminalTypes.setId(count.incrementAndGet());

        // Create the TerminalTypes
        TerminalTypesDTO terminalTypesDTO = terminalTypesMapper.toDto(terminalTypes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terminalTypesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalTypes in the database
        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalTypes in Elasticsearch
        verify(mockTerminalTypesSearchRepository, times(0)).save(terminalTypes);
    }

    @Test
    @Transactional
    void putWithIdMismatchTerminalTypes() throws Exception {
        int databaseSizeBeforeUpdate = terminalTypesRepository.findAll().size();
        terminalTypes.setId(count.incrementAndGet());

        // Create the TerminalTypes
        TerminalTypesDTO terminalTypesDTO = terminalTypesMapper.toDto(terminalTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalTypes in the database
        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalTypes in Elasticsearch
        verify(mockTerminalTypesSearchRepository, times(0)).save(terminalTypes);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTerminalTypes() throws Exception {
        int databaseSizeBeforeUpdate = terminalTypesRepository.findAll().size();
        terminalTypes.setId(count.incrementAndGet());

        // Create the TerminalTypes
        TerminalTypesDTO terminalTypesDTO = terminalTypesMapper.toDto(terminalTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalTypesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalTypesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TerminalTypes in the database
        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalTypes in Elasticsearch
        verify(mockTerminalTypesSearchRepository, times(0)).save(terminalTypes);
    }

    @Test
    @Transactional
    void partialUpdateTerminalTypesWithPatch() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        int databaseSizeBeforeUpdate = terminalTypesRepository.findAll().size();

        // Update the terminalTypes using partial update
        TerminalTypes partialUpdatedTerminalTypes = new TerminalTypes();
        partialUpdatedTerminalTypes.setId(terminalTypes.getId());

        partialUpdatedTerminalTypes.txnChannelType(UPDATED_TXN_CHANNEL_TYPE);

        restTerminalTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerminalTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTerminalTypes))
            )
            .andExpect(status().isOk());

        // Validate the TerminalTypes in the database
        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeUpdate);
        TerminalTypes testTerminalTypes = terminalTypesList.get(terminalTypesList.size() - 1);
        assertThat(testTerminalTypes.getTxnTerminalTypeCode()).isEqualTo(DEFAULT_TXN_TERMINAL_TYPE_CODE);
        assertThat(testTerminalTypes.getTxnChannelType()).isEqualTo(UPDATED_TXN_CHANNEL_TYPE);
        assertThat(testTerminalTypes.getTxnChannelTypeDetails()).isEqualTo(DEFAULT_TXN_CHANNEL_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateTerminalTypesWithPatch() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        int databaseSizeBeforeUpdate = terminalTypesRepository.findAll().size();

        // Update the terminalTypes using partial update
        TerminalTypes partialUpdatedTerminalTypes = new TerminalTypes();
        partialUpdatedTerminalTypes.setId(terminalTypes.getId());

        partialUpdatedTerminalTypes
            .txnTerminalTypeCode(UPDATED_TXN_TERMINAL_TYPE_CODE)
            .txnChannelType(UPDATED_TXN_CHANNEL_TYPE)
            .txnChannelTypeDetails(UPDATED_TXN_CHANNEL_TYPE_DETAILS);

        restTerminalTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerminalTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTerminalTypes))
            )
            .andExpect(status().isOk());

        // Validate the TerminalTypes in the database
        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeUpdate);
        TerminalTypes testTerminalTypes = terminalTypesList.get(terminalTypesList.size() - 1);
        assertThat(testTerminalTypes.getTxnTerminalTypeCode()).isEqualTo(UPDATED_TXN_TERMINAL_TYPE_CODE);
        assertThat(testTerminalTypes.getTxnChannelType()).isEqualTo(UPDATED_TXN_CHANNEL_TYPE);
        assertThat(testTerminalTypes.getTxnChannelTypeDetails()).isEqualTo(UPDATED_TXN_CHANNEL_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingTerminalTypes() throws Exception {
        int databaseSizeBeforeUpdate = terminalTypesRepository.findAll().size();
        terminalTypes.setId(count.incrementAndGet());

        // Create the TerminalTypes
        TerminalTypesDTO terminalTypesDTO = terminalTypesMapper.toDto(terminalTypes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, terminalTypesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalTypes in the database
        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalTypes in Elasticsearch
        verify(mockTerminalTypesSearchRepository, times(0)).save(terminalTypes);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTerminalTypes() throws Exception {
        int databaseSizeBeforeUpdate = terminalTypesRepository.findAll().size();
        terminalTypes.setId(count.incrementAndGet());

        // Create the TerminalTypes
        TerminalTypesDTO terminalTypesDTO = terminalTypesMapper.toDto(terminalTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalTypes in the database
        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalTypes in Elasticsearch
        verify(mockTerminalTypesSearchRepository, times(0)).save(terminalTypes);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTerminalTypes() throws Exception {
        int databaseSizeBeforeUpdate = terminalTypesRepository.findAll().size();
        terminalTypes.setId(count.incrementAndGet());

        // Create the TerminalTypes
        TerminalTypesDTO terminalTypesDTO = terminalTypesMapper.toDto(terminalTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalTypesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalTypesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TerminalTypes in the database
        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalTypes in Elasticsearch
        verify(mockTerminalTypesSearchRepository, times(0)).save(terminalTypes);
    }

    @Test
    @Transactional
    void deleteTerminalTypes() throws Exception {
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);

        int databaseSizeBeforeDelete = terminalTypesRepository.findAll().size();

        // Delete the terminalTypes
        restTerminalTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, terminalTypes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TerminalTypes> terminalTypesList = terminalTypesRepository.findAll();
        assertThat(terminalTypesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TerminalTypes in Elasticsearch
        verify(mockTerminalTypesSearchRepository, times(1)).deleteById(terminalTypes.getId());
    }

    @Test
    @Transactional
    void searchTerminalTypes() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        terminalTypesRepository.saveAndFlush(terminalTypes);
        when(mockTerminalTypesSearchRepository.search("id:" + terminalTypes.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(terminalTypes), PageRequest.of(0, 1), 1));

        // Search the terminalTypes
        restTerminalTypesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + terminalTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminalTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].txnTerminalTypeCode").value(hasItem(DEFAULT_TXN_TERMINAL_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].txnChannelType").value(hasItem(DEFAULT_TXN_CHANNEL_TYPE)))
            .andExpect(jsonPath("$.[*].txnChannelTypeDetails").value(hasItem(DEFAULT_TXN_CHANNEL_TYPE_DETAILS.toString())));
    }
}
