package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.domain.SecurityTenure;
import io.github.erp.repository.SecurityTenureRepository;
import io.github.erp.repository.search.SecurityTenureSearchRepository;
import io.github.erp.service.criteria.SecurityTenureCriteria;
import io.github.erp.service.dto.SecurityTenureDTO;
import io.github.erp.service.mapper.SecurityTenureMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link SecurityTenureResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SecurityTenureResourceIT {

    private static final String DEFAULT_SECURITY_TENURE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SECURITY_TENURE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SECURITY_TENURE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SECURITY_TENURE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SECURITY_TENURE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_SECURITY_TENURE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/security-tenures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/security-tenures";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SecurityTenureRepository securityTenureRepository;

    @Autowired
    private SecurityTenureMapper securityTenureMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SecurityTenureSearchRepositoryMockConfiguration
     */
    @Autowired
    private SecurityTenureSearchRepository mockSecurityTenureSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSecurityTenureMockMvc;

    private SecurityTenure securityTenure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityTenure createEntity(EntityManager em) {
        SecurityTenure securityTenure = new SecurityTenure()
            .securityTenureCode(DEFAULT_SECURITY_TENURE_CODE)
            .securityTenureType(DEFAULT_SECURITY_TENURE_TYPE)
            .securityTenureDetails(DEFAULT_SECURITY_TENURE_DETAILS);
        return securityTenure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityTenure createUpdatedEntity(EntityManager em) {
        SecurityTenure securityTenure = new SecurityTenure()
            .securityTenureCode(UPDATED_SECURITY_TENURE_CODE)
            .securityTenureType(UPDATED_SECURITY_TENURE_TYPE)
            .securityTenureDetails(UPDATED_SECURITY_TENURE_DETAILS);
        return securityTenure;
    }

    @BeforeEach
    public void initTest() {
        securityTenure = createEntity(em);
    }

    @Test
    @Transactional
    void createSecurityTenure() throws Exception {
        int databaseSizeBeforeCreate = securityTenureRepository.findAll().size();
        // Create the SecurityTenure
        SecurityTenureDTO securityTenureDTO = securityTenureMapper.toDto(securityTenure);
        restSecurityTenureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityTenureDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SecurityTenure in the database
        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeCreate + 1);
        SecurityTenure testSecurityTenure = securityTenureList.get(securityTenureList.size() - 1);
        assertThat(testSecurityTenure.getSecurityTenureCode()).isEqualTo(DEFAULT_SECURITY_TENURE_CODE);
        assertThat(testSecurityTenure.getSecurityTenureType()).isEqualTo(DEFAULT_SECURITY_TENURE_TYPE);
        assertThat(testSecurityTenure.getSecurityTenureDetails()).isEqualTo(DEFAULT_SECURITY_TENURE_DETAILS);

        // Validate the SecurityTenure in Elasticsearch
        verify(mockSecurityTenureSearchRepository, times(1)).save(testSecurityTenure);
    }

    @Test
    @Transactional
    void createSecurityTenureWithExistingId() throws Exception {
        // Create the SecurityTenure with an existing ID
        securityTenure.setId(1L);
        SecurityTenureDTO securityTenureDTO = securityTenureMapper.toDto(securityTenure);

        int databaseSizeBeforeCreate = securityTenureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecurityTenureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityTenureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityTenure in the database
        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeCreate);

        // Validate the SecurityTenure in Elasticsearch
        verify(mockSecurityTenureSearchRepository, times(0)).save(securityTenure);
    }

    @Test
    @Transactional
    void checkSecurityTenureCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityTenureRepository.findAll().size();
        // set the field null
        securityTenure.setSecurityTenureCode(null);

        // Create the SecurityTenure, which fails.
        SecurityTenureDTO securityTenureDTO = securityTenureMapper.toDto(securityTenure);

        restSecurityTenureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityTenureDTO))
            )
            .andExpect(status().isBadRequest());

        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSecurityTenureTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = securityTenureRepository.findAll().size();
        // set the field null
        securityTenure.setSecurityTenureType(null);

        // Create the SecurityTenure, which fails.
        SecurityTenureDTO securityTenureDTO = securityTenureMapper.toDto(securityTenure);

        restSecurityTenureMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityTenureDTO))
            )
            .andExpect(status().isBadRequest());

        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSecurityTenures() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList
        restSecurityTenureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityTenure.getId().intValue())))
            .andExpect(jsonPath("$.[*].securityTenureCode").value(hasItem(DEFAULT_SECURITY_TENURE_CODE)))
            .andExpect(jsonPath("$.[*].securityTenureType").value(hasItem(DEFAULT_SECURITY_TENURE_TYPE)))
            .andExpect(jsonPath("$.[*].securityTenureDetails").value(hasItem(DEFAULT_SECURITY_TENURE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getSecurityTenure() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get the securityTenure
        restSecurityTenureMockMvc
            .perform(get(ENTITY_API_URL_ID, securityTenure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(securityTenure.getId().intValue()))
            .andExpect(jsonPath("$.securityTenureCode").value(DEFAULT_SECURITY_TENURE_CODE))
            .andExpect(jsonPath("$.securityTenureType").value(DEFAULT_SECURITY_TENURE_TYPE))
            .andExpect(jsonPath("$.securityTenureDetails").value(DEFAULT_SECURITY_TENURE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getSecurityTenuresByIdFiltering() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        Long id = securityTenure.getId();

        defaultSecurityTenureShouldBeFound("id.equals=" + id);
        defaultSecurityTenureShouldNotBeFound("id.notEquals=" + id);

        defaultSecurityTenureShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSecurityTenureShouldNotBeFound("id.greaterThan=" + id);

        defaultSecurityTenureShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSecurityTenureShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSecurityTenuresBySecurityTenureCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList where securityTenureCode equals to DEFAULT_SECURITY_TENURE_CODE
        defaultSecurityTenureShouldBeFound("securityTenureCode.equals=" + DEFAULT_SECURITY_TENURE_CODE);

        // Get all the securityTenureList where securityTenureCode equals to UPDATED_SECURITY_TENURE_CODE
        defaultSecurityTenureShouldNotBeFound("securityTenureCode.equals=" + UPDATED_SECURITY_TENURE_CODE);
    }

    @Test
    @Transactional
    void getAllSecurityTenuresBySecurityTenureCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList where securityTenureCode not equals to DEFAULT_SECURITY_TENURE_CODE
        defaultSecurityTenureShouldNotBeFound("securityTenureCode.notEquals=" + DEFAULT_SECURITY_TENURE_CODE);

        // Get all the securityTenureList where securityTenureCode not equals to UPDATED_SECURITY_TENURE_CODE
        defaultSecurityTenureShouldBeFound("securityTenureCode.notEquals=" + UPDATED_SECURITY_TENURE_CODE);
    }

    @Test
    @Transactional
    void getAllSecurityTenuresBySecurityTenureCodeIsInShouldWork() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList where securityTenureCode in DEFAULT_SECURITY_TENURE_CODE or UPDATED_SECURITY_TENURE_CODE
        defaultSecurityTenureShouldBeFound("securityTenureCode.in=" + DEFAULT_SECURITY_TENURE_CODE + "," + UPDATED_SECURITY_TENURE_CODE);

        // Get all the securityTenureList where securityTenureCode equals to UPDATED_SECURITY_TENURE_CODE
        defaultSecurityTenureShouldNotBeFound("securityTenureCode.in=" + UPDATED_SECURITY_TENURE_CODE);
    }

    @Test
    @Transactional
    void getAllSecurityTenuresBySecurityTenureCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList where securityTenureCode is not null
        defaultSecurityTenureShouldBeFound("securityTenureCode.specified=true");

        // Get all the securityTenureList where securityTenureCode is null
        defaultSecurityTenureShouldNotBeFound("securityTenureCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityTenuresBySecurityTenureCodeContainsSomething() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList where securityTenureCode contains DEFAULT_SECURITY_TENURE_CODE
        defaultSecurityTenureShouldBeFound("securityTenureCode.contains=" + DEFAULT_SECURITY_TENURE_CODE);

        // Get all the securityTenureList where securityTenureCode contains UPDATED_SECURITY_TENURE_CODE
        defaultSecurityTenureShouldNotBeFound("securityTenureCode.contains=" + UPDATED_SECURITY_TENURE_CODE);
    }

    @Test
    @Transactional
    void getAllSecurityTenuresBySecurityTenureCodeNotContainsSomething() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList where securityTenureCode does not contain DEFAULT_SECURITY_TENURE_CODE
        defaultSecurityTenureShouldNotBeFound("securityTenureCode.doesNotContain=" + DEFAULT_SECURITY_TENURE_CODE);

        // Get all the securityTenureList where securityTenureCode does not contain UPDATED_SECURITY_TENURE_CODE
        defaultSecurityTenureShouldBeFound("securityTenureCode.doesNotContain=" + UPDATED_SECURITY_TENURE_CODE);
    }

    @Test
    @Transactional
    void getAllSecurityTenuresBySecurityTenureTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList where securityTenureType equals to DEFAULT_SECURITY_TENURE_TYPE
        defaultSecurityTenureShouldBeFound("securityTenureType.equals=" + DEFAULT_SECURITY_TENURE_TYPE);

        // Get all the securityTenureList where securityTenureType equals to UPDATED_SECURITY_TENURE_TYPE
        defaultSecurityTenureShouldNotBeFound("securityTenureType.equals=" + UPDATED_SECURITY_TENURE_TYPE);
    }

    @Test
    @Transactional
    void getAllSecurityTenuresBySecurityTenureTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList where securityTenureType not equals to DEFAULT_SECURITY_TENURE_TYPE
        defaultSecurityTenureShouldNotBeFound("securityTenureType.notEquals=" + DEFAULT_SECURITY_TENURE_TYPE);

        // Get all the securityTenureList where securityTenureType not equals to UPDATED_SECURITY_TENURE_TYPE
        defaultSecurityTenureShouldBeFound("securityTenureType.notEquals=" + UPDATED_SECURITY_TENURE_TYPE);
    }

    @Test
    @Transactional
    void getAllSecurityTenuresBySecurityTenureTypeIsInShouldWork() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList where securityTenureType in DEFAULT_SECURITY_TENURE_TYPE or UPDATED_SECURITY_TENURE_TYPE
        defaultSecurityTenureShouldBeFound("securityTenureType.in=" + DEFAULT_SECURITY_TENURE_TYPE + "," + UPDATED_SECURITY_TENURE_TYPE);

        // Get all the securityTenureList where securityTenureType equals to UPDATED_SECURITY_TENURE_TYPE
        defaultSecurityTenureShouldNotBeFound("securityTenureType.in=" + UPDATED_SECURITY_TENURE_TYPE);
    }

    @Test
    @Transactional
    void getAllSecurityTenuresBySecurityTenureTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList where securityTenureType is not null
        defaultSecurityTenureShouldBeFound("securityTenureType.specified=true");

        // Get all the securityTenureList where securityTenureType is null
        defaultSecurityTenureShouldNotBeFound("securityTenureType.specified=false");
    }

    @Test
    @Transactional
    void getAllSecurityTenuresBySecurityTenureTypeContainsSomething() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList where securityTenureType contains DEFAULT_SECURITY_TENURE_TYPE
        defaultSecurityTenureShouldBeFound("securityTenureType.contains=" + DEFAULT_SECURITY_TENURE_TYPE);

        // Get all the securityTenureList where securityTenureType contains UPDATED_SECURITY_TENURE_TYPE
        defaultSecurityTenureShouldNotBeFound("securityTenureType.contains=" + UPDATED_SECURITY_TENURE_TYPE);
    }

    @Test
    @Transactional
    void getAllSecurityTenuresBySecurityTenureTypeNotContainsSomething() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        // Get all the securityTenureList where securityTenureType does not contain DEFAULT_SECURITY_TENURE_TYPE
        defaultSecurityTenureShouldNotBeFound("securityTenureType.doesNotContain=" + DEFAULT_SECURITY_TENURE_TYPE);

        // Get all the securityTenureList where securityTenureType does not contain UPDATED_SECURITY_TENURE_TYPE
        defaultSecurityTenureShouldBeFound("securityTenureType.doesNotContain=" + UPDATED_SECURITY_TENURE_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSecurityTenureShouldBeFound(String filter) throws Exception {
        restSecurityTenureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityTenure.getId().intValue())))
            .andExpect(jsonPath("$.[*].securityTenureCode").value(hasItem(DEFAULT_SECURITY_TENURE_CODE)))
            .andExpect(jsonPath("$.[*].securityTenureType").value(hasItem(DEFAULT_SECURITY_TENURE_TYPE)))
            .andExpect(jsonPath("$.[*].securityTenureDetails").value(hasItem(DEFAULT_SECURITY_TENURE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restSecurityTenureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSecurityTenureShouldNotBeFound(String filter) throws Exception {
        restSecurityTenureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSecurityTenureMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSecurityTenure() throws Exception {
        // Get the securityTenure
        restSecurityTenureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSecurityTenure() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        int databaseSizeBeforeUpdate = securityTenureRepository.findAll().size();

        // Update the securityTenure
        SecurityTenure updatedSecurityTenure = securityTenureRepository.findById(securityTenure.getId()).get();
        // Disconnect from session so that the updates on updatedSecurityTenure are not directly saved in db
        em.detach(updatedSecurityTenure);
        updatedSecurityTenure
            .securityTenureCode(UPDATED_SECURITY_TENURE_CODE)
            .securityTenureType(UPDATED_SECURITY_TENURE_TYPE)
            .securityTenureDetails(UPDATED_SECURITY_TENURE_DETAILS);
        SecurityTenureDTO securityTenureDTO = securityTenureMapper.toDto(updatedSecurityTenure);

        restSecurityTenureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityTenureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityTenureDTO))
            )
            .andExpect(status().isOk());

        // Validate the SecurityTenure in the database
        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeUpdate);
        SecurityTenure testSecurityTenure = securityTenureList.get(securityTenureList.size() - 1);
        assertThat(testSecurityTenure.getSecurityTenureCode()).isEqualTo(UPDATED_SECURITY_TENURE_CODE);
        assertThat(testSecurityTenure.getSecurityTenureType()).isEqualTo(UPDATED_SECURITY_TENURE_TYPE);
        assertThat(testSecurityTenure.getSecurityTenureDetails()).isEqualTo(UPDATED_SECURITY_TENURE_DETAILS);

        // Validate the SecurityTenure in Elasticsearch
        verify(mockSecurityTenureSearchRepository).save(testSecurityTenure);
    }

    @Test
    @Transactional
    void putNonExistingSecurityTenure() throws Exception {
        int databaseSizeBeforeUpdate = securityTenureRepository.findAll().size();
        securityTenure.setId(count.incrementAndGet());

        // Create the SecurityTenure
        SecurityTenureDTO securityTenureDTO = securityTenureMapper.toDto(securityTenure);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityTenureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityTenureDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityTenureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityTenure in the database
        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityTenure in Elasticsearch
        verify(mockSecurityTenureSearchRepository, times(0)).save(securityTenure);
    }

    @Test
    @Transactional
    void putWithIdMismatchSecurityTenure() throws Exception {
        int databaseSizeBeforeUpdate = securityTenureRepository.findAll().size();
        securityTenure.setId(count.incrementAndGet());

        // Create the SecurityTenure
        SecurityTenureDTO securityTenureDTO = securityTenureMapper.toDto(securityTenure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityTenureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(securityTenureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityTenure in the database
        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityTenure in Elasticsearch
        verify(mockSecurityTenureSearchRepository, times(0)).save(securityTenure);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSecurityTenure() throws Exception {
        int databaseSizeBeforeUpdate = securityTenureRepository.findAll().size();
        securityTenure.setId(count.incrementAndGet());

        // Create the SecurityTenure
        SecurityTenureDTO securityTenureDTO = securityTenureMapper.toDto(securityTenure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityTenureMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(securityTenureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityTenure in the database
        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityTenure in Elasticsearch
        verify(mockSecurityTenureSearchRepository, times(0)).save(securityTenure);
    }

    @Test
    @Transactional
    void partialUpdateSecurityTenureWithPatch() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        int databaseSizeBeforeUpdate = securityTenureRepository.findAll().size();

        // Update the securityTenure using partial update
        SecurityTenure partialUpdatedSecurityTenure = new SecurityTenure();
        partialUpdatedSecurityTenure.setId(securityTenure.getId());

        partialUpdatedSecurityTenure.securityTenureCode(UPDATED_SECURITY_TENURE_CODE);

        restSecurityTenureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityTenure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityTenure))
            )
            .andExpect(status().isOk());

        // Validate the SecurityTenure in the database
        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeUpdate);
        SecurityTenure testSecurityTenure = securityTenureList.get(securityTenureList.size() - 1);
        assertThat(testSecurityTenure.getSecurityTenureCode()).isEqualTo(UPDATED_SECURITY_TENURE_CODE);
        assertThat(testSecurityTenure.getSecurityTenureType()).isEqualTo(DEFAULT_SECURITY_TENURE_TYPE);
        assertThat(testSecurityTenure.getSecurityTenureDetails()).isEqualTo(DEFAULT_SECURITY_TENURE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateSecurityTenureWithPatch() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        int databaseSizeBeforeUpdate = securityTenureRepository.findAll().size();

        // Update the securityTenure using partial update
        SecurityTenure partialUpdatedSecurityTenure = new SecurityTenure();
        partialUpdatedSecurityTenure.setId(securityTenure.getId());

        partialUpdatedSecurityTenure
            .securityTenureCode(UPDATED_SECURITY_TENURE_CODE)
            .securityTenureType(UPDATED_SECURITY_TENURE_TYPE)
            .securityTenureDetails(UPDATED_SECURITY_TENURE_DETAILS);

        restSecurityTenureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityTenure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSecurityTenure))
            )
            .andExpect(status().isOk());

        // Validate the SecurityTenure in the database
        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeUpdate);
        SecurityTenure testSecurityTenure = securityTenureList.get(securityTenureList.size() - 1);
        assertThat(testSecurityTenure.getSecurityTenureCode()).isEqualTo(UPDATED_SECURITY_TENURE_CODE);
        assertThat(testSecurityTenure.getSecurityTenureType()).isEqualTo(UPDATED_SECURITY_TENURE_TYPE);
        assertThat(testSecurityTenure.getSecurityTenureDetails()).isEqualTo(UPDATED_SECURITY_TENURE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingSecurityTenure() throws Exception {
        int databaseSizeBeforeUpdate = securityTenureRepository.findAll().size();
        securityTenure.setId(count.incrementAndGet());

        // Create the SecurityTenure
        SecurityTenureDTO securityTenureDTO = securityTenureMapper.toDto(securityTenure);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityTenureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, securityTenureDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityTenureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityTenure in the database
        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityTenure in Elasticsearch
        verify(mockSecurityTenureSearchRepository, times(0)).save(securityTenure);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSecurityTenure() throws Exception {
        int databaseSizeBeforeUpdate = securityTenureRepository.findAll().size();
        securityTenure.setId(count.incrementAndGet());

        // Create the SecurityTenure
        SecurityTenureDTO securityTenureDTO = securityTenureMapper.toDto(securityTenure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityTenureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityTenureDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityTenure in the database
        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityTenure in Elasticsearch
        verify(mockSecurityTenureSearchRepository, times(0)).save(securityTenure);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSecurityTenure() throws Exception {
        int databaseSizeBeforeUpdate = securityTenureRepository.findAll().size();
        securityTenure.setId(count.incrementAndGet());

        // Create the SecurityTenure
        SecurityTenureDTO securityTenureDTO = securityTenureMapper.toDto(securityTenure);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityTenureMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(securityTenureDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityTenure in the database
        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SecurityTenure in Elasticsearch
        verify(mockSecurityTenureSearchRepository, times(0)).save(securityTenure);
    }

    @Test
    @Transactional
    void deleteSecurityTenure() throws Exception {
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);

        int databaseSizeBeforeDelete = securityTenureRepository.findAll().size();

        // Delete the securityTenure
        restSecurityTenureMockMvc
            .perform(delete(ENTITY_API_URL_ID, securityTenure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SecurityTenure> securityTenureList = securityTenureRepository.findAll();
        assertThat(securityTenureList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SecurityTenure in Elasticsearch
        verify(mockSecurityTenureSearchRepository, times(1)).deleteById(securityTenure.getId());
    }

    @Test
    @Transactional
    void searchSecurityTenure() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        securityTenureRepository.saveAndFlush(securityTenure);
        when(mockSecurityTenureSearchRepository.search("id:" + securityTenure.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(securityTenure), PageRequest.of(0, 1), 1));

        // Search the securityTenure
        restSecurityTenureMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + securityTenure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityTenure.getId().intValue())))
            .andExpect(jsonPath("$.[*].securityTenureCode").value(hasItem(DEFAULT_SECURITY_TENURE_CODE)))
            .andExpect(jsonPath("$.[*].securityTenureType").value(hasItem(DEFAULT_SECURITY_TENURE_TYPE)))
            .andExpect(jsonPath("$.[*].securityTenureDetails").value(hasItem(DEFAULT_SECURITY_TENURE_DETAILS.toString())));
    }
}
