package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import io.github.erp.domain.InterbankSectorCode;
import io.github.erp.repository.InterbankSectorCodeRepository;
import io.github.erp.repository.search.InterbankSectorCodeSearchRepository;
import io.github.erp.service.dto.InterbankSectorCodeDTO;
import io.github.erp.service.mapper.InterbankSectorCodeMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.InterbankSectorCodeResource;
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
 * Integration tests for the {@link InterbankSectorCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class InterbankSectorCodeResourceIT {

    private static final String DEFAULT_INTERBANK_SECTOR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_INTERBANK_SECTOR_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INTERBANK_SECTOR_CODE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_INTERBANK_SECTOR_CODE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/interbank-sector-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/interbank-sector-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterbankSectorCodeRepository interbankSectorCodeRepository;

    @Autowired
    private InterbankSectorCodeMapper interbankSectorCodeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.InterbankSectorCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private InterbankSectorCodeSearchRepository mockInterbankSectorCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterbankSectorCodeMockMvc;

    private InterbankSectorCode interbankSectorCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterbankSectorCode createEntity(EntityManager em) {
        InterbankSectorCode interbankSectorCode = new InterbankSectorCode()
            .interbankSectorCode(DEFAULT_INTERBANK_SECTOR_CODE)
            .interbankSectorCodeDescription(DEFAULT_INTERBANK_SECTOR_CODE_DESCRIPTION);
        return interbankSectorCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterbankSectorCode createUpdatedEntity(EntityManager em) {
        InterbankSectorCode interbankSectorCode = new InterbankSectorCode()
            .interbankSectorCode(UPDATED_INTERBANK_SECTOR_CODE)
            .interbankSectorCodeDescription(UPDATED_INTERBANK_SECTOR_CODE_DESCRIPTION);
        return interbankSectorCode;
    }

    @BeforeEach
    public void initTest() {
        interbankSectorCode = createEntity(em);
    }

    @Test
    @Transactional
    void createInterbankSectorCode() throws Exception {
        int databaseSizeBeforeCreate = interbankSectorCodeRepository.findAll().size();
        // Create the InterbankSectorCode
        InterbankSectorCodeDTO interbankSectorCodeDTO = interbankSectorCodeMapper.toDto(interbankSectorCode);
        restInterbankSectorCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interbankSectorCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InterbankSectorCode in the database
        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeCreate + 1);
        InterbankSectorCode testInterbankSectorCode = interbankSectorCodeList.get(interbankSectorCodeList.size() - 1);
        assertThat(testInterbankSectorCode.getInterbankSectorCode()).isEqualTo(DEFAULT_INTERBANK_SECTOR_CODE);
        assertThat(testInterbankSectorCode.getInterbankSectorCodeDescription()).isEqualTo(DEFAULT_INTERBANK_SECTOR_CODE_DESCRIPTION);

        // Validate the InterbankSectorCode in Elasticsearch
        verify(mockInterbankSectorCodeSearchRepository, times(1)).save(testInterbankSectorCode);
    }

    @Test
    @Transactional
    void createInterbankSectorCodeWithExistingId() throws Exception {
        // Create the InterbankSectorCode with an existing ID
        interbankSectorCode.setId(1L);
        InterbankSectorCodeDTO interbankSectorCodeDTO = interbankSectorCodeMapper.toDto(interbankSectorCode);

        int databaseSizeBeforeCreate = interbankSectorCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterbankSectorCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interbankSectorCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterbankSectorCode in the database
        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the InterbankSectorCode in Elasticsearch
        verify(mockInterbankSectorCodeSearchRepository, times(0)).save(interbankSectorCode);
    }

    @Test
    @Transactional
    void checkInterbankSectorCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = interbankSectorCodeRepository.findAll().size();
        // set the field null
        interbankSectorCode.setInterbankSectorCode(null);

        // Create the InterbankSectorCode, which fails.
        InterbankSectorCodeDTO interbankSectorCodeDTO = interbankSectorCodeMapper.toDto(interbankSectorCode);

        restInterbankSectorCodeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interbankSectorCodeDTO))
            )
            .andExpect(status().isBadRequest());

        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInterbankSectorCodes() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        // Get all the interbankSectorCodeList
        restInterbankSectorCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interbankSectorCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].interbankSectorCode").value(hasItem(DEFAULT_INTERBANK_SECTOR_CODE)))
            .andExpect(
                jsonPath("$.[*].interbankSectorCodeDescription").value(hasItem(DEFAULT_INTERBANK_SECTOR_CODE_DESCRIPTION.toString()))
            );
    }

    @Test
    @Transactional
    void getInterbankSectorCode() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        // Get the interbankSectorCode
        restInterbankSectorCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, interbankSectorCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interbankSectorCode.getId().intValue()))
            .andExpect(jsonPath("$.interbankSectorCode").value(DEFAULT_INTERBANK_SECTOR_CODE))
            .andExpect(jsonPath("$.interbankSectorCodeDescription").value(DEFAULT_INTERBANK_SECTOR_CODE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getInterbankSectorCodesByIdFiltering() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        Long id = interbankSectorCode.getId();

        defaultInterbankSectorCodeShouldBeFound("id.equals=" + id);
        defaultInterbankSectorCodeShouldNotBeFound("id.notEquals=" + id);

        defaultInterbankSectorCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInterbankSectorCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultInterbankSectorCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInterbankSectorCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInterbankSectorCodesByInterbankSectorCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        // Get all the interbankSectorCodeList where interbankSectorCode equals to DEFAULT_INTERBANK_SECTOR_CODE
        defaultInterbankSectorCodeShouldBeFound("interbankSectorCode.equals=" + DEFAULT_INTERBANK_SECTOR_CODE);

        // Get all the interbankSectorCodeList where interbankSectorCode equals to UPDATED_INTERBANK_SECTOR_CODE
        defaultInterbankSectorCodeShouldNotBeFound("interbankSectorCode.equals=" + UPDATED_INTERBANK_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllInterbankSectorCodesByInterbankSectorCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        // Get all the interbankSectorCodeList where interbankSectorCode not equals to DEFAULT_INTERBANK_SECTOR_CODE
        defaultInterbankSectorCodeShouldNotBeFound("interbankSectorCode.notEquals=" + DEFAULT_INTERBANK_SECTOR_CODE);

        // Get all the interbankSectorCodeList where interbankSectorCode not equals to UPDATED_INTERBANK_SECTOR_CODE
        defaultInterbankSectorCodeShouldBeFound("interbankSectorCode.notEquals=" + UPDATED_INTERBANK_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllInterbankSectorCodesByInterbankSectorCodeIsInShouldWork() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        // Get all the interbankSectorCodeList where interbankSectorCode in DEFAULT_INTERBANK_SECTOR_CODE or UPDATED_INTERBANK_SECTOR_CODE
        defaultInterbankSectorCodeShouldBeFound(
            "interbankSectorCode.in=" + DEFAULT_INTERBANK_SECTOR_CODE + "," + UPDATED_INTERBANK_SECTOR_CODE
        );

        // Get all the interbankSectorCodeList where interbankSectorCode equals to UPDATED_INTERBANK_SECTOR_CODE
        defaultInterbankSectorCodeShouldNotBeFound("interbankSectorCode.in=" + UPDATED_INTERBANK_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllInterbankSectorCodesByInterbankSectorCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        // Get all the interbankSectorCodeList where interbankSectorCode is not null
        defaultInterbankSectorCodeShouldBeFound("interbankSectorCode.specified=true");

        // Get all the interbankSectorCodeList where interbankSectorCode is null
        defaultInterbankSectorCodeShouldNotBeFound("interbankSectorCode.specified=false");
    }

    @Test
    @Transactional
    void getAllInterbankSectorCodesByInterbankSectorCodeContainsSomething() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        // Get all the interbankSectorCodeList where interbankSectorCode contains DEFAULT_INTERBANK_SECTOR_CODE
        defaultInterbankSectorCodeShouldBeFound("interbankSectorCode.contains=" + DEFAULT_INTERBANK_SECTOR_CODE);

        // Get all the interbankSectorCodeList where interbankSectorCode contains UPDATED_INTERBANK_SECTOR_CODE
        defaultInterbankSectorCodeShouldNotBeFound("interbankSectorCode.contains=" + UPDATED_INTERBANK_SECTOR_CODE);
    }

    @Test
    @Transactional
    void getAllInterbankSectorCodesByInterbankSectorCodeNotContainsSomething() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        // Get all the interbankSectorCodeList where interbankSectorCode does not contain DEFAULT_INTERBANK_SECTOR_CODE
        defaultInterbankSectorCodeShouldNotBeFound("interbankSectorCode.doesNotContain=" + DEFAULT_INTERBANK_SECTOR_CODE);

        // Get all the interbankSectorCodeList where interbankSectorCode does not contain UPDATED_INTERBANK_SECTOR_CODE
        defaultInterbankSectorCodeShouldBeFound("interbankSectorCode.doesNotContain=" + UPDATED_INTERBANK_SECTOR_CODE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInterbankSectorCodeShouldBeFound(String filter) throws Exception {
        restInterbankSectorCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interbankSectorCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].interbankSectorCode").value(hasItem(DEFAULT_INTERBANK_SECTOR_CODE)))
            .andExpect(
                jsonPath("$.[*].interbankSectorCodeDescription").value(hasItem(DEFAULT_INTERBANK_SECTOR_CODE_DESCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restInterbankSectorCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInterbankSectorCodeShouldNotBeFound(String filter) throws Exception {
        restInterbankSectorCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInterbankSectorCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInterbankSectorCode() throws Exception {
        // Get the interbankSectorCode
        restInterbankSectorCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInterbankSectorCode() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        int databaseSizeBeforeUpdate = interbankSectorCodeRepository.findAll().size();

        // Update the interbankSectorCode
        InterbankSectorCode updatedInterbankSectorCode = interbankSectorCodeRepository.findById(interbankSectorCode.getId()).get();
        // Disconnect from session so that the updates on updatedInterbankSectorCode are not directly saved in db
        em.detach(updatedInterbankSectorCode);
        updatedInterbankSectorCode
            .interbankSectorCode(UPDATED_INTERBANK_SECTOR_CODE)
            .interbankSectorCodeDescription(UPDATED_INTERBANK_SECTOR_CODE_DESCRIPTION);
        InterbankSectorCodeDTO interbankSectorCodeDTO = interbankSectorCodeMapper.toDto(updatedInterbankSectorCode);

        restInterbankSectorCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interbankSectorCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interbankSectorCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the InterbankSectorCode in the database
        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeUpdate);
        InterbankSectorCode testInterbankSectorCode = interbankSectorCodeList.get(interbankSectorCodeList.size() - 1);
        assertThat(testInterbankSectorCode.getInterbankSectorCode()).isEqualTo(UPDATED_INTERBANK_SECTOR_CODE);
        assertThat(testInterbankSectorCode.getInterbankSectorCodeDescription()).isEqualTo(UPDATED_INTERBANK_SECTOR_CODE_DESCRIPTION);

        // Validate the InterbankSectorCode in Elasticsearch
        verify(mockInterbankSectorCodeSearchRepository).save(testInterbankSectorCode);
    }

    @Test
    @Transactional
    void putNonExistingInterbankSectorCode() throws Exception {
        int databaseSizeBeforeUpdate = interbankSectorCodeRepository.findAll().size();
        interbankSectorCode.setId(count.incrementAndGet());

        // Create the InterbankSectorCode
        InterbankSectorCodeDTO interbankSectorCodeDTO = interbankSectorCodeMapper.toDto(interbankSectorCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterbankSectorCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interbankSectorCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interbankSectorCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterbankSectorCode in the database
        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterbankSectorCode in Elasticsearch
        verify(mockInterbankSectorCodeSearchRepository, times(0)).save(interbankSectorCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchInterbankSectorCode() throws Exception {
        int databaseSizeBeforeUpdate = interbankSectorCodeRepository.findAll().size();
        interbankSectorCode.setId(count.incrementAndGet());

        // Create the InterbankSectorCode
        InterbankSectorCodeDTO interbankSectorCodeDTO = interbankSectorCodeMapper.toDto(interbankSectorCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterbankSectorCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interbankSectorCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterbankSectorCode in the database
        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterbankSectorCode in Elasticsearch
        verify(mockInterbankSectorCodeSearchRepository, times(0)).save(interbankSectorCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInterbankSectorCode() throws Exception {
        int databaseSizeBeforeUpdate = interbankSectorCodeRepository.findAll().size();
        interbankSectorCode.setId(count.incrementAndGet());

        // Create the InterbankSectorCode
        InterbankSectorCodeDTO interbankSectorCodeDTO = interbankSectorCodeMapper.toDto(interbankSectorCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterbankSectorCodeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interbankSectorCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterbankSectorCode in the database
        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterbankSectorCode in Elasticsearch
        verify(mockInterbankSectorCodeSearchRepository, times(0)).save(interbankSectorCode);
    }

    @Test
    @Transactional
    void partialUpdateInterbankSectorCodeWithPatch() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        int databaseSizeBeforeUpdate = interbankSectorCodeRepository.findAll().size();

        // Update the interbankSectorCode using partial update
        InterbankSectorCode partialUpdatedInterbankSectorCode = new InterbankSectorCode();
        partialUpdatedInterbankSectorCode.setId(interbankSectorCode.getId());

        partialUpdatedInterbankSectorCode.interbankSectorCodeDescription(UPDATED_INTERBANK_SECTOR_CODE_DESCRIPTION);

        restInterbankSectorCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterbankSectorCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterbankSectorCode))
            )
            .andExpect(status().isOk());

        // Validate the InterbankSectorCode in the database
        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeUpdate);
        InterbankSectorCode testInterbankSectorCode = interbankSectorCodeList.get(interbankSectorCodeList.size() - 1);
        assertThat(testInterbankSectorCode.getInterbankSectorCode()).isEqualTo(DEFAULT_INTERBANK_SECTOR_CODE);
        assertThat(testInterbankSectorCode.getInterbankSectorCodeDescription()).isEqualTo(UPDATED_INTERBANK_SECTOR_CODE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateInterbankSectorCodeWithPatch() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        int databaseSizeBeforeUpdate = interbankSectorCodeRepository.findAll().size();

        // Update the interbankSectorCode using partial update
        InterbankSectorCode partialUpdatedInterbankSectorCode = new InterbankSectorCode();
        partialUpdatedInterbankSectorCode.setId(interbankSectorCode.getId());

        partialUpdatedInterbankSectorCode
            .interbankSectorCode(UPDATED_INTERBANK_SECTOR_CODE)
            .interbankSectorCodeDescription(UPDATED_INTERBANK_SECTOR_CODE_DESCRIPTION);

        restInterbankSectorCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterbankSectorCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterbankSectorCode))
            )
            .andExpect(status().isOk());

        // Validate the InterbankSectorCode in the database
        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeUpdate);
        InterbankSectorCode testInterbankSectorCode = interbankSectorCodeList.get(interbankSectorCodeList.size() - 1);
        assertThat(testInterbankSectorCode.getInterbankSectorCode()).isEqualTo(UPDATED_INTERBANK_SECTOR_CODE);
        assertThat(testInterbankSectorCode.getInterbankSectorCodeDescription()).isEqualTo(UPDATED_INTERBANK_SECTOR_CODE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingInterbankSectorCode() throws Exception {
        int databaseSizeBeforeUpdate = interbankSectorCodeRepository.findAll().size();
        interbankSectorCode.setId(count.incrementAndGet());

        // Create the InterbankSectorCode
        InterbankSectorCodeDTO interbankSectorCodeDTO = interbankSectorCodeMapper.toDto(interbankSectorCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterbankSectorCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interbankSectorCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interbankSectorCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterbankSectorCode in the database
        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterbankSectorCode in Elasticsearch
        verify(mockInterbankSectorCodeSearchRepository, times(0)).save(interbankSectorCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInterbankSectorCode() throws Exception {
        int databaseSizeBeforeUpdate = interbankSectorCodeRepository.findAll().size();
        interbankSectorCode.setId(count.incrementAndGet());

        // Create the InterbankSectorCode
        InterbankSectorCodeDTO interbankSectorCodeDTO = interbankSectorCodeMapper.toDto(interbankSectorCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterbankSectorCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interbankSectorCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterbankSectorCode in the database
        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterbankSectorCode in Elasticsearch
        verify(mockInterbankSectorCodeSearchRepository, times(0)).save(interbankSectorCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInterbankSectorCode() throws Exception {
        int databaseSizeBeforeUpdate = interbankSectorCodeRepository.findAll().size();
        interbankSectorCode.setId(count.incrementAndGet());

        // Create the InterbankSectorCode
        InterbankSectorCodeDTO interbankSectorCodeDTO = interbankSectorCodeMapper.toDto(interbankSectorCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterbankSectorCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interbankSectorCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterbankSectorCode in the database
        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InterbankSectorCode in Elasticsearch
        verify(mockInterbankSectorCodeSearchRepository, times(0)).save(interbankSectorCode);
    }

    @Test
    @Transactional
    void deleteInterbankSectorCode() throws Exception {
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);

        int databaseSizeBeforeDelete = interbankSectorCodeRepository.findAll().size();

        // Delete the interbankSectorCode
        restInterbankSectorCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, interbankSectorCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InterbankSectorCode> interbankSectorCodeList = interbankSectorCodeRepository.findAll();
        assertThat(interbankSectorCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InterbankSectorCode in Elasticsearch
        verify(mockInterbankSectorCodeSearchRepository, times(1)).deleteById(interbankSectorCode.getId());
    }

    @Test
    @Transactional
    void searchInterbankSectorCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        interbankSectorCodeRepository.saveAndFlush(interbankSectorCode);
        when(mockInterbankSectorCodeSearchRepository.search("id:" + interbankSectorCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(interbankSectorCode), PageRequest.of(0, 1), 1));

        // Search the interbankSectorCode
        restInterbankSectorCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + interbankSectorCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interbankSectorCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].interbankSectorCode").value(hasItem(DEFAULT_INTERBANK_SECTOR_CODE)))
            .andExpect(
                jsonPath("$.[*].interbankSectorCodeDescription").value(hasItem(DEFAULT_INTERBANK_SECTOR_CODE_DESCRIPTION.toString()))
            );
    }
}
