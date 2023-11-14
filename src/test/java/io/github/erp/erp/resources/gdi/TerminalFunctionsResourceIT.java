package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import io.github.erp.domain.TerminalFunctions;
import io.github.erp.repository.TerminalFunctionsRepository;
import io.github.erp.repository.search.TerminalFunctionsSearchRepository;
import io.github.erp.service.dto.TerminalFunctionsDTO;
import io.github.erp.service.mapper.TerminalFunctionsMapper;
import io.github.erp.web.rest.TerminalFunctionsResource;
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
 * Integration tests for the {@link TerminalFunctionsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class TerminalFunctionsResourceIT {

    private static final String DEFAULT_FUNCTION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FUNCTION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TERMINAL_FUNCTIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_TERMINAL_FUNCTIONALITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/terminal-functions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/terminal-functions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TerminalFunctionsRepository terminalFunctionsRepository;

    @Autowired
    private TerminalFunctionsMapper terminalFunctionsMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TerminalFunctionsSearchRepositoryMockConfiguration
     */
    @Autowired
    private TerminalFunctionsSearchRepository mockTerminalFunctionsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTerminalFunctionsMockMvc;

    private TerminalFunctions terminalFunctions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TerminalFunctions createEntity(EntityManager em) {
        TerminalFunctions terminalFunctions = new TerminalFunctions()
            .functionCode(DEFAULT_FUNCTION_CODE)
            .terminalFunctionality(DEFAULT_TERMINAL_FUNCTIONALITY);
        return terminalFunctions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TerminalFunctions createUpdatedEntity(EntityManager em) {
        TerminalFunctions terminalFunctions = new TerminalFunctions()
            .functionCode(UPDATED_FUNCTION_CODE)
            .terminalFunctionality(UPDATED_TERMINAL_FUNCTIONALITY);
        return terminalFunctions;
    }

    @BeforeEach
    public void initTest() {
        terminalFunctions = createEntity(em);
    }

    @Test
    @Transactional
    void createTerminalFunctions() throws Exception {
        int databaseSizeBeforeCreate = terminalFunctionsRepository.findAll().size();
        // Create the TerminalFunctions
        TerminalFunctionsDTO terminalFunctionsDTO = terminalFunctionsMapper.toDto(terminalFunctions);
        restTerminalFunctionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalFunctionsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TerminalFunctions in the database
        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeCreate + 1);
        TerminalFunctions testTerminalFunctions = terminalFunctionsList.get(terminalFunctionsList.size() - 1);
        assertThat(testTerminalFunctions.getFunctionCode()).isEqualTo(DEFAULT_FUNCTION_CODE);
        assertThat(testTerminalFunctions.getTerminalFunctionality()).isEqualTo(DEFAULT_TERMINAL_FUNCTIONALITY);

        // Validate the TerminalFunctions in Elasticsearch
        verify(mockTerminalFunctionsSearchRepository, times(1)).save(testTerminalFunctions);
    }

    @Test
    @Transactional
    void createTerminalFunctionsWithExistingId() throws Exception {
        // Create the TerminalFunctions with an existing ID
        terminalFunctions.setId(1L);
        TerminalFunctionsDTO terminalFunctionsDTO = terminalFunctionsMapper.toDto(terminalFunctions);

        int databaseSizeBeforeCreate = terminalFunctionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTerminalFunctionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalFunctionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalFunctions in the database
        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeCreate);

        // Validate the TerminalFunctions in Elasticsearch
        verify(mockTerminalFunctionsSearchRepository, times(0)).save(terminalFunctions);
    }

    @Test
    @Transactional
    void checkFunctionCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalFunctionsRepository.findAll().size();
        // set the field null
        terminalFunctions.setFunctionCode(null);

        // Create the TerminalFunctions, which fails.
        TerminalFunctionsDTO terminalFunctionsDTO = terminalFunctionsMapper.toDto(terminalFunctions);

        restTerminalFunctionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalFunctionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTerminalFunctionalityIsRequired() throws Exception {
        int databaseSizeBeforeTest = terminalFunctionsRepository.findAll().size();
        // set the field null
        terminalFunctions.setTerminalFunctionality(null);

        // Create the TerminalFunctions, which fails.
        TerminalFunctionsDTO terminalFunctionsDTO = terminalFunctionsMapper.toDto(terminalFunctions);

        restTerminalFunctionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalFunctionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTerminalFunctions() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList
        restTerminalFunctionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminalFunctions.getId().intValue())))
            .andExpect(jsonPath("$.[*].functionCode").value(hasItem(DEFAULT_FUNCTION_CODE)))
            .andExpect(jsonPath("$.[*].terminalFunctionality").value(hasItem(DEFAULT_TERMINAL_FUNCTIONALITY)));
    }

    @Test
    @Transactional
    void getTerminalFunctions() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get the terminalFunctions
        restTerminalFunctionsMockMvc
            .perform(get(ENTITY_API_URL_ID, terminalFunctions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(terminalFunctions.getId().intValue()))
            .andExpect(jsonPath("$.functionCode").value(DEFAULT_FUNCTION_CODE))
            .andExpect(jsonPath("$.terminalFunctionality").value(DEFAULT_TERMINAL_FUNCTIONALITY));
    }

    @Test
    @Transactional
    void getTerminalFunctionsByIdFiltering() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        Long id = terminalFunctions.getId();

        defaultTerminalFunctionsShouldBeFound("id.equals=" + id);
        defaultTerminalFunctionsShouldNotBeFound("id.notEquals=" + id);

        defaultTerminalFunctionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTerminalFunctionsShouldNotBeFound("id.greaterThan=" + id);

        defaultTerminalFunctionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTerminalFunctionsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTerminalFunctionsByFunctionCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList where functionCode equals to DEFAULT_FUNCTION_CODE
        defaultTerminalFunctionsShouldBeFound("functionCode.equals=" + DEFAULT_FUNCTION_CODE);

        // Get all the terminalFunctionsList where functionCode equals to UPDATED_FUNCTION_CODE
        defaultTerminalFunctionsShouldNotBeFound("functionCode.equals=" + UPDATED_FUNCTION_CODE);
    }

    @Test
    @Transactional
    void getAllTerminalFunctionsByFunctionCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList where functionCode not equals to DEFAULT_FUNCTION_CODE
        defaultTerminalFunctionsShouldNotBeFound("functionCode.notEquals=" + DEFAULT_FUNCTION_CODE);

        // Get all the terminalFunctionsList where functionCode not equals to UPDATED_FUNCTION_CODE
        defaultTerminalFunctionsShouldBeFound("functionCode.notEquals=" + UPDATED_FUNCTION_CODE);
    }

    @Test
    @Transactional
    void getAllTerminalFunctionsByFunctionCodeIsInShouldWork() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList where functionCode in DEFAULT_FUNCTION_CODE or UPDATED_FUNCTION_CODE
        defaultTerminalFunctionsShouldBeFound("functionCode.in=" + DEFAULT_FUNCTION_CODE + "," + UPDATED_FUNCTION_CODE);

        // Get all the terminalFunctionsList where functionCode equals to UPDATED_FUNCTION_CODE
        defaultTerminalFunctionsShouldNotBeFound("functionCode.in=" + UPDATED_FUNCTION_CODE);
    }

    @Test
    @Transactional
    void getAllTerminalFunctionsByFunctionCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList where functionCode is not null
        defaultTerminalFunctionsShouldBeFound("functionCode.specified=true");

        // Get all the terminalFunctionsList where functionCode is null
        defaultTerminalFunctionsShouldNotBeFound("functionCode.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalFunctionsByFunctionCodeContainsSomething() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList where functionCode contains DEFAULT_FUNCTION_CODE
        defaultTerminalFunctionsShouldBeFound("functionCode.contains=" + DEFAULT_FUNCTION_CODE);

        // Get all the terminalFunctionsList where functionCode contains UPDATED_FUNCTION_CODE
        defaultTerminalFunctionsShouldNotBeFound("functionCode.contains=" + UPDATED_FUNCTION_CODE);
    }

    @Test
    @Transactional
    void getAllTerminalFunctionsByFunctionCodeNotContainsSomething() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList where functionCode does not contain DEFAULT_FUNCTION_CODE
        defaultTerminalFunctionsShouldNotBeFound("functionCode.doesNotContain=" + DEFAULT_FUNCTION_CODE);

        // Get all the terminalFunctionsList where functionCode does not contain UPDATED_FUNCTION_CODE
        defaultTerminalFunctionsShouldBeFound("functionCode.doesNotContain=" + UPDATED_FUNCTION_CODE);
    }

    @Test
    @Transactional
    void getAllTerminalFunctionsByTerminalFunctionalityIsEqualToSomething() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList where terminalFunctionality equals to DEFAULT_TERMINAL_FUNCTIONALITY
        defaultTerminalFunctionsShouldBeFound("terminalFunctionality.equals=" + DEFAULT_TERMINAL_FUNCTIONALITY);

        // Get all the terminalFunctionsList where terminalFunctionality equals to UPDATED_TERMINAL_FUNCTIONALITY
        defaultTerminalFunctionsShouldNotBeFound("terminalFunctionality.equals=" + UPDATED_TERMINAL_FUNCTIONALITY);
    }

    @Test
    @Transactional
    void getAllTerminalFunctionsByTerminalFunctionalityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList where terminalFunctionality not equals to DEFAULT_TERMINAL_FUNCTIONALITY
        defaultTerminalFunctionsShouldNotBeFound("terminalFunctionality.notEquals=" + DEFAULT_TERMINAL_FUNCTIONALITY);

        // Get all the terminalFunctionsList where terminalFunctionality not equals to UPDATED_TERMINAL_FUNCTIONALITY
        defaultTerminalFunctionsShouldBeFound("terminalFunctionality.notEquals=" + UPDATED_TERMINAL_FUNCTIONALITY);
    }

    @Test
    @Transactional
    void getAllTerminalFunctionsByTerminalFunctionalityIsInShouldWork() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList where terminalFunctionality in DEFAULT_TERMINAL_FUNCTIONALITY or UPDATED_TERMINAL_FUNCTIONALITY
        defaultTerminalFunctionsShouldBeFound(
            "terminalFunctionality.in=" + DEFAULT_TERMINAL_FUNCTIONALITY + "," + UPDATED_TERMINAL_FUNCTIONALITY
        );

        // Get all the terminalFunctionsList where terminalFunctionality equals to UPDATED_TERMINAL_FUNCTIONALITY
        defaultTerminalFunctionsShouldNotBeFound("terminalFunctionality.in=" + UPDATED_TERMINAL_FUNCTIONALITY);
    }

    @Test
    @Transactional
    void getAllTerminalFunctionsByTerminalFunctionalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList where terminalFunctionality is not null
        defaultTerminalFunctionsShouldBeFound("terminalFunctionality.specified=true");

        // Get all the terminalFunctionsList where terminalFunctionality is null
        defaultTerminalFunctionsShouldNotBeFound("terminalFunctionality.specified=false");
    }

    @Test
    @Transactional
    void getAllTerminalFunctionsByTerminalFunctionalityContainsSomething() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList where terminalFunctionality contains DEFAULT_TERMINAL_FUNCTIONALITY
        defaultTerminalFunctionsShouldBeFound("terminalFunctionality.contains=" + DEFAULT_TERMINAL_FUNCTIONALITY);

        // Get all the terminalFunctionsList where terminalFunctionality contains UPDATED_TERMINAL_FUNCTIONALITY
        defaultTerminalFunctionsShouldNotBeFound("terminalFunctionality.contains=" + UPDATED_TERMINAL_FUNCTIONALITY);
    }

    @Test
    @Transactional
    void getAllTerminalFunctionsByTerminalFunctionalityNotContainsSomething() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        // Get all the terminalFunctionsList where terminalFunctionality does not contain DEFAULT_TERMINAL_FUNCTIONALITY
        defaultTerminalFunctionsShouldNotBeFound("terminalFunctionality.doesNotContain=" + DEFAULT_TERMINAL_FUNCTIONALITY);

        // Get all the terminalFunctionsList where terminalFunctionality does not contain UPDATED_TERMINAL_FUNCTIONALITY
        defaultTerminalFunctionsShouldBeFound("terminalFunctionality.doesNotContain=" + UPDATED_TERMINAL_FUNCTIONALITY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTerminalFunctionsShouldBeFound(String filter) throws Exception {
        restTerminalFunctionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminalFunctions.getId().intValue())))
            .andExpect(jsonPath("$.[*].functionCode").value(hasItem(DEFAULT_FUNCTION_CODE)))
            .andExpect(jsonPath("$.[*].terminalFunctionality").value(hasItem(DEFAULT_TERMINAL_FUNCTIONALITY)));

        // Check, that the count call also returns 1
        restTerminalFunctionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTerminalFunctionsShouldNotBeFound(String filter) throws Exception {
        restTerminalFunctionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTerminalFunctionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTerminalFunctions() throws Exception {
        // Get the terminalFunctions
        restTerminalFunctionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTerminalFunctions() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        int databaseSizeBeforeUpdate = terminalFunctionsRepository.findAll().size();

        // Update the terminalFunctions
        TerminalFunctions updatedTerminalFunctions = terminalFunctionsRepository.findById(terminalFunctions.getId()).get();
        // Disconnect from session so that the updates on updatedTerminalFunctions are not directly saved in db
        em.detach(updatedTerminalFunctions);
        updatedTerminalFunctions.functionCode(UPDATED_FUNCTION_CODE).terminalFunctionality(UPDATED_TERMINAL_FUNCTIONALITY);
        TerminalFunctionsDTO terminalFunctionsDTO = terminalFunctionsMapper.toDto(updatedTerminalFunctions);

        restTerminalFunctionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terminalFunctionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalFunctionsDTO))
            )
            .andExpect(status().isOk());

        // Validate the TerminalFunctions in the database
        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeUpdate);
        TerminalFunctions testTerminalFunctions = terminalFunctionsList.get(terminalFunctionsList.size() - 1);
        assertThat(testTerminalFunctions.getFunctionCode()).isEqualTo(UPDATED_FUNCTION_CODE);
        assertThat(testTerminalFunctions.getTerminalFunctionality()).isEqualTo(UPDATED_TERMINAL_FUNCTIONALITY);

        // Validate the TerminalFunctions in Elasticsearch
        verify(mockTerminalFunctionsSearchRepository).save(testTerminalFunctions);
    }

    @Test
    @Transactional
    void putNonExistingTerminalFunctions() throws Exception {
        int databaseSizeBeforeUpdate = terminalFunctionsRepository.findAll().size();
        terminalFunctions.setId(count.incrementAndGet());

        // Create the TerminalFunctions
        TerminalFunctionsDTO terminalFunctionsDTO = terminalFunctionsMapper.toDto(terminalFunctions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalFunctionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, terminalFunctionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalFunctionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalFunctions in the database
        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalFunctions in Elasticsearch
        verify(mockTerminalFunctionsSearchRepository, times(0)).save(terminalFunctions);
    }

    @Test
    @Transactional
    void putWithIdMismatchTerminalFunctions() throws Exception {
        int databaseSizeBeforeUpdate = terminalFunctionsRepository.findAll().size();
        terminalFunctions.setId(count.incrementAndGet());

        // Create the TerminalFunctions
        TerminalFunctionsDTO terminalFunctionsDTO = terminalFunctionsMapper.toDto(terminalFunctions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalFunctionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(terminalFunctionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalFunctions in the database
        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalFunctions in Elasticsearch
        verify(mockTerminalFunctionsSearchRepository, times(0)).save(terminalFunctions);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTerminalFunctions() throws Exception {
        int databaseSizeBeforeUpdate = terminalFunctionsRepository.findAll().size();
        terminalFunctions.setId(count.incrementAndGet());

        // Create the TerminalFunctions
        TerminalFunctionsDTO terminalFunctionsDTO = terminalFunctionsMapper.toDto(terminalFunctions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalFunctionsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(terminalFunctionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TerminalFunctions in the database
        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalFunctions in Elasticsearch
        verify(mockTerminalFunctionsSearchRepository, times(0)).save(terminalFunctions);
    }

    @Test
    @Transactional
    void partialUpdateTerminalFunctionsWithPatch() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        int databaseSizeBeforeUpdate = terminalFunctionsRepository.findAll().size();

        // Update the terminalFunctions using partial update
        TerminalFunctions partialUpdatedTerminalFunctions = new TerminalFunctions();
        partialUpdatedTerminalFunctions.setId(terminalFunctions.getId());

        restTerminalFunctionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerminalFunctions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTerminalFunctions))
            )
            .andExpect(status().isOk());

        // Validate the TerminalFunctions in the database
        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeUpdate);
        TerminalFunctions testTerminalFunctions = terminalFunctionsList.get(terminalFunctionsList.size() - 1);
        assertThat(testTerminalFunctions.getFunctionCode()).isEqualTo(DEFAULT_FUNCTION_CODE);
        assertThat(testTerminalFunctions.getTerminalFunctionality()).isEqualTo(DEFAULT_TERMINAL_FUNCTIONALITY);
    }

    @Test
    @Transactional
    void fullUpdateTerminalFunctionsWithPatch() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        int databaseSizeBeforeUpdate = terminalFunctionsRepository.findAll().size();

        // Update the terminalFunctions using partial update
        TerminalFunctions partialUpdatedTerminalFunctions = new TerminalFunctions();
        partialUpdatedTerminalFunctions.setId(terminalFunctions.getId());

        partialUpdatedTerminalFunctions.functionCode(UPDATED_FUNCTION_CODE).terminalFunctionality(UPDATED_TERMINAL_FUNCTIONALITY);

        restTerminalFunctionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTerminalFunctions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTerminalFunctions))
            )
            .andExpect(status().isOk());

        // Validate the TerminalFunctions in the database
        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeUpdate);
        TerminalFunctions testTerminalFunctions = terminalFunctionsList.get(terminalFunctionsList.size() - 1);
        assertThat(testTerminalFunctions.getFunctionCode()).isEqualTo(UPDATED_FUNCTION_CODE);
        assertThat(testTerminalFunctions.getTerminalFunctionality()).isEqualTo(UPDATED_TERMINAL_FUNCTIONALITY);
    }

    @Test
    @Transactional
    void patchNonExistingTerminalFunctions() throws Exception {
        int databaseSizeBeforeUpdate = terminalFunctionsRepository.findAll().size();
        terminalFunctions.setId(count.incrementAndGet());

        // Create the TerminalFunctions
        TerminalFunctionsDTO terminalFunctionsDTO = terminalFunctionsMapper.toDto(terminalFunctions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalFunctionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, terminalFunctionsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalFunctionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalFunctions in the database
        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalFunctions in Elasticsearch
        verify(mockTerminalFunctionsSearchRepository, times(0)).save(terminalFunctions);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTerminalFunctions() throws Exception {
        int databaseSizeBeforeUpdate = terminalFunctionsRepository.findAll().size();
        terminalFunctions.setId(count.incrementAndGet());

        // Create the TerminalFunctions
        TerminalFunctionsDTO terminalFunctionsDTO = terminalFunctionsMapper.toDto(terminalFunctions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalFunctionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalFunctionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TerminalFunctions in the database
        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalFunctions in Elasticsearch
        verify(mockTerminalFunctionsSearchRepository, times(0)).save(terminalFunctions);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTerminalFunctions() throws Exception {
        int databaseSizeBeforeUpdate = terminalFunctionsRepository.findAll().size();
        terminalFunctions.setId(count.incrementAndGet());

        // Create the TerminalFunctions
        TerminalFunctionsDTO terminalFunctionsDTO = terminalFunctionsMapper.toDto(terminalFunctions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTerminalFunctionsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(terminalFunctionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TerminalFunctions in the database
        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TerminalFunctions in Elasticsearch
        verify(mockTerminalFunctionsSearchRepository, times(0)).save(terminalFunctions);
    }

    @Test
    @Transactional
    void deleteTerminalFunctions() throws Exception {
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);

        int databaseSizeBeforeDelete = terminalFunctionsRepository.findAll().size();

        // Delete the terminalFunctions
        restTerminalFunctionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, terminalFunctions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TerminalFunctions> terminalFunctionsList = terminalFunctionsRepository.findAll();
        assertThat(terminalFunctionsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TerminalFunctions in Elasticsearch
        verify(mockTerminalFunctionsSearchRepository, times(1)).deleteById(terminalFunctions.getId());
    }

    @Test
    @Transactional
    void searchTerminalFunctions() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        terminalFunctionsRepository.saveAndFlush(terminalFunctions);
        when(mockTerminalFunctionsSearchRepository.search("id:" + terminalFunctions.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(terminalFunctions), PageRequest.of(0, 1), 1));

        // Search the terminalFunctions
        restTerminalFunctionsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + terminalFunctions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminalFunctions.getId().intValue())))
            .andExpect(jsonPath("$.[*].functionCode").value(hasItem(DEFAULT_FUNCTION_CODE)))
            .andExpect(jsonPath("$.[*].terminalFunctionality").value(hasItem(DEFAULT_TERMINAL_FUNCTIONALITY)));
    }
}
