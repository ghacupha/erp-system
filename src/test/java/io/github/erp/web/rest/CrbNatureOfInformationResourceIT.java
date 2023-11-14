package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.CrbNatureOfInformation;
import io.github.erp.repository.CrbNatureOfInformationRepository;
import io.github.erp.repository.search.CrbNatureOfInformationSearchRepository;
import io.github.erp.service.criteria.CrbNatureOfInformationCriteria;
import io.github.erp.service.dto.CrbNatureOfInformationDTO;
import io.github.erp.service.mapper.CrbNatureOfInformationMapper;
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
 * Integration tests for the {@link CrbNatureOfInformationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CrbNatureOfInformationResourceIT {

    private static final String DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_NATURE_OF_INFORMATION_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NATURE_OF_INFORMATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_NATURE_OF_INFORMATION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NATURE_OF_INFORMATION_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_NATURE_OF_INFORMATION_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crb-nature-of-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/crb-nature-of-informations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrbNatureOfInformationRepository crbNatureOfInformationRepository;

    @Autowired
    private CrbNatureOfInformationMapper crbNatureOfInformationMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CrbNatureOfInformationSearchRepositoryMockConfiguration
     */
    @Autowired
    private CrbNatureOfInformationSearchRepository mockCrbNatureOfInformationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrbNatureOfInformationMockMvc;

    private CrbNatureOfInformation crbNatureOfInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbNatureOfInformation createEntity(EntityManager em) {
        CrbNatureOfInformation crbNatureOfInformation = new CrbNatureOfInformation()
            .natureOfInformationTypeCode(DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE)
            .natureOfInformationType(DEFAULT_NATURE_OF_INFORMATION_TYPE)
            .natureOfInformationTypeDescription(DEFAULT_NATURE_OF_INFORMATION_TYPE_DESCRIPTION);
        return crbNatureOfInformation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CrbNatureOfInformation createUpdatedEntity(EntityManager em) {
        CrbNatureOfInformation crbNatureOfInformation = new CrbNatureOfInformation()
            .natureOfInformationTypeCode(UPDATED_NATURE_OF_INFORMATION_TYPE_CODE)
            .natureOfInformationType(UPDATED_NATURE_OF_INFORMATION_TYPE)
            .natureOfInformationTypeDescription(UPDATED_NATURE_OF_INFORMATION_TYPE_DESCRIPTION);
        return crbNatureOfInformation;
    }

    @BeforeEach
    public void initTest() {
        crbNatureOfInformation = createEntity(em);
    }

    @Test
    @Transactional
    void createCrbNatureOfInformation() throws Exception {
        int databaseSizeBeforeCreate = crbNatureOfInformationRepository.findAll().size();
        // Create the CrbNatureOfInformation
        CrbNatureOfInformationDTO crbNatureOfInformationDTO = crbNatureOfInformationMapper.toDto(crbNatureOfInformation);
        restCrbNatureOfInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbNatureOfInformationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CrbNatureOfInformation in the database
        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeCreate + 1);
        CrbNatureOfInformation testCrbNatureOfInformation = crbNatureOfInformationList.get(crbNatureOfInformationList.size() - 1);
        assertThat(testCrbNatureOfInformation.getNatureOfInformationTypeCode()).isEqualTo(DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE);
        assertThat(testCrbNatureOfInformation.getNatureOfInformationType()).isEqualTo(DEFAULT_NATURE_OF_INFORMATION_TYPE);
        assertThat(testCrbNatureOfInformation.getNatureOfInformationTypeDescription())
            .isEqualTo(DEFAULT_NATURE_OF_INFORMATION_TYPE_DESCRIPTION);

        // Validate the CrbNatureOfInformation in Elasticsearch
        verify(mockCrbNatureOfInformationSearchRepository, times(1)).save(testCrbNatureOfInformation);
    }

    @Test
    @Transactional
    void createCrbNatureOfInformationWithExistingId() throws Exception {
        // Create the CrbNatureOfInformation with an existing ID
        crbNatureOfInformation.setId(1L);
        CrbNatureOfInformationDTO crbNatureOfInformationDTO = crbNatureOfInformationMapper.toDto(crbNatureOfInformation);

        int databaseSizeBeforeCreate = crbNatureOfInformationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrbNatureOfInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbNatureOfInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbNatureOfInformation in the database
        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeCreate);

        // Validate the CrbNatureOfInformation in Elasticsearch
        verify(mockCrbNatureOfInformationSearchRepository, times(0)).save(crbNatureOfInformation);
    }

    @Test
    @Transactional
    void checkNatureOfInformationTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbNatureOfInformationRepository.findAll().size();
        // set the field null
        crbNatureOfInformation.setNatureOfInformationTypeCode(null);

        // Create the CrbNatureOfInformation, which fails.
        CrbNatureOfInformationDTO crbNatureOfInformationDTO = crbNatureOfInformationMapper.toDto(crbNatureOfInformation);

        restCrbNatureOfInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbNatureOfInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNatureOfInformationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = crbNatureOfInformationRepository.findAll().size();
        // set the field null
        crbNatureOfInformation.setNatureOfInformationType(null);

        // Create the CrbNatureOfInformation, which fails.
        CrbNatureOfInformationDTO crbNatureOfInformationDTO = crbNatureOfInformationMapper.toDto(crbNatureOfInformation);

        restCrbNatureOfInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbNatureOfInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformations() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList
        restCrbNatureOfInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbNatureOfInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].natureOfInformationTypeCode").value(hasItem(DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].natureOfInformationType").value(hasItem(DEFAULT_NATURE_OF_INFORMATION_TYPE)))
            .andExpect(
                jsonPath("$.[*].natureOfInformationTypeDescription")
                    .value(hasItem(DEFAULT_NATURE_OF_INFORMATION_TYPE_DESCRIPTION.toString()))
            );
    }

    @Test
    @Transactional
    void getCrbNatureOfInformation() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get the crbNatureOfInformation
        restCrbNatureOfInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, crbNatureOfInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crbNatureOfInformation.getId().intValue()))
            .andExpect(jsonPath("$.natureOfInformationTypeCode").value(DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE))
            .andExpect(jsonPath("$.natureOfInformationType").value(DEFAULT_NATURE_OF_INFORMATION_TYPE))
            .andExpect(jsonPath("$.natureOfInformationTypeDescription").value(DEFAULT_NATURE_OF_INFORMATION_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getCrbNatureOfInformationsByIdFiltering() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        Long id = crbNatureOfInformation.getId();

        defaultCrbNatureOfInformationShouldBeFound("id.equals=" + id);
        defaultCrbNatureOfInformationShouldNotBeFound("id.notEquals=" + id);

        defaultCrbNatureOfInformationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrbNatureOfInformationShouldNotBeFound("id.greaterThan=" + id);

        defaultCrbNatureOfInformationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrbNatureOfInformationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformationsByNatureOfInformationTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList where natureOfInformationTypeCode equals to DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE
        defaultCrbNatureOfInformationShouldBeFound("natureOfInformationTypeCode.equals=" + DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE);

        // Get all the crbNatureOfInformationList where natureOfInformationTypeCode equals to UPDATED_NATURE_OF_INFORMATION_TYPE_CODE
        defaultCrbNatureOfInformationShouldNotBeFound("natureOfInformationTypeCode.equals=" + UPDATED_NATURE_OF_INFORMATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformationsByNatureOfInformationTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList where natureOfInformationTypeCode not equals to DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE
        defaultCrbNatureOfInformationShouldNotBeFound("natureOfInformationTypeCode.notEquals=" + DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE);

        // Get all the crbNatureOfInformationList where natureOfInformationTypeCode not equals to UPDATED_NATURE_OF_INFORMATION_TYPE_CODE
        defaultCrbNatureOfInformationShouldBeFound("natureOfInformationTypeCode.notEquals=" + UPDATED_NATURE_OF_INFORMATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformationsByNatureOfInformationTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList where natureOfInformationTypeCode in DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE or UPDATED_NATURE_OF_INFORMATION_TYPE_CODE
        defaultCrbNatureOfInformationShouldBeFound(
            "natureOfInformationTypeCode.in=" + DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE + "," + UPDATED_NATURE_OF_INFORMATION_TYPE_CODE
        );

        // Get all the crbNatureOfInformationList where natureOfInformationTypeCode equals to UPDATED_NATURE_OF_INFORMATION_TYPE_CODE
        defaultCrbNatureOfInformationShouldNotBeFound("natureOfInformationTypeCode.in=" + UPDATED_NATURE_OF_INFORMATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformationsByNatureOfInformationTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList where natureOfInformationTypeCode is not null
        defaultCrbNatureOfInformationShouldBeFound("natureOfInformationTypeCode.specified=true");

        // Get all the crbNatureOfInformationList where natureOfInformationTypeCode is null
        defaultCrbNatureOfInformationShouldNotBeFound("natureOfInformationTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformationsByNatureOfInformationTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList where natureOfInformationTypeCode contains DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE
        defaultCrbNatureOfInformationShouldBeFound("natureOfInformationTypeCode.contains=" + DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE);

        // Get all the crbNatureOfInformationList where natureOfInformationTypeCode contains UPDATED_NATURE_OF_INFORMATION_TYPE_CODE
        defaultCrbNatureOfInformationShouldNotBeFound("natureOfInformationTypeCode.contains=" + UPDATED_NATURE_OF_INFORMATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformationsByNatureOfInformationTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList where natureOfInformationTypeCode does not contain DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE
        defaultCrbNatureOfInformationShouldNotBeFound(
            "natureOfInformationTypeCode.doesNotContain=" + DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE
        );

        // Get all the crbNatureOfInformationList where natureOfInformationTypeCode does not contain UPDATED_NATURE_OF_INFORMATION_TYPE_CODE
        defaultCrbNatureOfInformationShouldBeFound("natureOfInformationTypeCode.doesNotContain=" + UPDATED_NATURE_OF_INFORMATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformationsByNatureOfInformationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList where natureOfInformationType equals to DEFAULT_NATURE_OF_INFORMATION_TYPE
        defaultCrbNatureOfInformationShouldBeFound("natureOfInformationType.equals=" + DEFAULT_NATURE_OF_INFORMATION_TYPE);

        // Get all the crbNatureOfInformationList where natureOfInformationType equals to UPDATED_NATURE_OF_INFORMATION_TYPE
        defaultCrbNatureOfInformationShouldNotBeFound("natureOfInformationType.equals=" + UPDATED_NATURE_OF_INFORMATION_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformationsByNatureOfInformationTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList where natureOfInformationType not equals to DEFAULT_NATURE_OF_INFORMATION_TYPE
        defaultCrbNatureOfInformationShouldNotBeFound("natureOfInformationType.notEquals=" + DEFAULT_NATURE_OF_INFORMATION_TYPE);

        // Get all the crbNatureOfInformationList where natureOfInformationType not equals to UPDATED_NATURE_OF_INFORMATION_TYPE
        defaultCrbNatureOfInformationShouldBeFound("natureOfInformationType.notEquals=" + UPDATED_NATURE_OF_INFORMATION_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformationsByNatureOfInformationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList where natureOfInformationType in DEFAULT_NATURE_OF_INFORMATION_TYPE or UPDATED_NATURE_OF_INFORMATION_TYPE
        defaultCrbNatureOfInformationShouldBeFound(
            "natureOfInformationType.in=" + DEFAULT_NATURE_OF_INFORMATION_TYPE + "," + UPDATED_NATURE_OF_INFORMATION_TYPE
        );

        // Get all the crbNatureOfInformationList where natureOfInformationType equals to UPDATED_NATURE_OF_INFORMATION_TYPE
        defaultCrbNatureOfInformationShouldNotBeFound("natureOfInformationType.in=" + UPDATED_NATURE_OF_INFORMATION_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformationsByNatureOfInformationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList where natureOfInformationType is not null
        defaultCrbNatureOfInformationShouldBeFound("natureOfInformationType.specified=true");

        // Get all the crbNatureOfInformationList where natureOfInformationType is null
        defaultCrbNatureOfInformationShouldNotBeFound("natureOfInformationType.specified=false");
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformationsByNatureOfInformationTypeContainsSomething() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList where natureOfInformationType contains DEFAULT_NATURE_OF_INFORMATION_TYPE
        defaultCrbNatureOfInformationShouldBeFound("natureOfInformationType.contains=" + DEFAULT_NATURE_OF_INFORMATION_TYPE);

        // Get all the crbNatureOfInformationList where natureOfInformationType contains UPDATED_NATURE_OF_INFORMATION_TYPE
        defaultCrbNatureOfInformationShouldNotBeFound("natureOfInformationType.contains=" + UPDATED_NATURE_OF_INFORMATION_TYPE);
    }

    @Test
    @Transactional
    void getAllCrbNatureOfInformationsByNatureOfInformationTypeNotContainsSomething() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        // Get all the crbNatureOfInformationList where natureOfInformationType does not contain DEFAULT_NATURE_OF_INFORMATION_TYPE
        defaultCrbNatureOfInformationShouldNotBeFound("natureOfInformationType.doesNotContain=" + DEFAULT_NATURE_OF_INFORMATION_TYPE);

        // Get all the crbNatureOfInformationList where natureOfInformationType does not contain UPDATED_NATURE_OF_INFORMATION_TYPE
        defaultCrbNatureOfInformationShouldBeFound("natureOfInformationType.doesNotContain=" + UPDATED_NATURE_OF_INFORMATION_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrbNatureOfInformationShouldBeFound(String filter) throws Exception {
        restCrbNatureOfInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbNatureOfInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].natureOfInformationTypeCode").value(hasItem(DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].natureOfInformationType").value(hasItem(DEFAULT_NATURE_OF_INFORMATION_TYPE)))
            .andExpect(
                jsonPath("$.[*].natureOfInformationTypeDescription")
                    .value(hasItem(DEFAULT_NATURE_OF_INFORMATION_TYPE_DESCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restCrbNatureOfInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrbNatureOfInformationShouldNotBeFound(String filter) throws Exception {
        restCrbNatureOfInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrbNatureOfInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrbNatureOfInformation() throws Exception {
        // Get the crbNatureOfInformation
        restCrbNatureOfInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrbNatureOfInformation() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        int databaseSizeBeforeUpdate = crbNatureOfInformationRepository.findAll().size();

        // Update the crbNatureOfInformation
        CrbNatureOfInformation updatedCrbNatureOfInformation = crbNatureOfInformationRepository
            .findById(crbNatureOfInformation.getId())
            .get();
        // Disconnect from session so that the updates on updatedCrbNatureOfInformation are not directly saved in db
        em.detach(updatedCrbNatureOfInformation);
        updatedCrbNatureOfInformation
            .natureOfInformationTypeCode(UPDATED_NATURE_OF_INFORMATION_TYPE_CODE)
            .natureOfInformationType(UPDATED_NATURE_OF_INFORMATION_TYPE)
            .natureOfInformationTypeDescription(UPDATED_NATURE_OF_INFORMATION_TYPE_DESCRIPTION);
        CrbNatureOfInformationDTO crbNatureOfInformationDTO = crbNatureOfInformationMapper.toDto(updatedCrbNatureOfInformation);

        restCrbNatureOfInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbNatureOfInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbNatureOfInformationDTO))
            )
            .andExpect(status().isOk());

        // Validate the CrbNatureOfInformation in the database
        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeUpdate);
        CrbNatureOfInformation testCrbNatureOfInformation = crbNatureOfInformationList.get(crbNatureOfInformationList.size() - 1);
        assertThat(testCrbNatureOfInformation.getNatureOfInformationTypeCode()).isEqualTo(UPDATED_NATURE_OF_INFORMATION_TYPE_CODE);
        assertThat(testCrbNatureOfInformation.getNatureOfInformationType()).isEqualTo(UPDATED_NATURE_OF_INFORMATION_TYPE);
        assertThat(testCrbNatureOfInformation.getNatureOfInformationTypeDescription())
            .isEqualTo(UPDATED_NATURE_OF_INFORMATION_TYPE_DESCRIPTION);

        // Validate the CrbNatureOfInformation in Elasticsearch
        verify(mockCrbNatureOfInformationSearchRepository).save(testCrbNatureOfInformation);
    }

    @Test
    @Transactional
    void putNonExistingCrbNatureOfInformation() throws Exception {
        int databaseSizeBeforeUpdate = crbNatureOfInformationRepository.findAll().size();
        crbNatureOfInformation.setId(count.incrementAndGet());

        // Create the CrbNatureOfInformation
        CrbNatureOfInformationDTO crbNatureOfInformationDTO = crbNatureOfInformationMapper.toDto(crbNatureOfInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbNatureOfInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crbNatureOfInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbNatureOfInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbNatureOfInformation in the database
        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbNatureOfInformation in Elasticsearch
        verify(mockCrbNatureOfInformationSearchRepository, times(0)).save(crbNatureOfInformation);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrbNatureOfInformation() throws Exception {
        int databaseSizeBeforeUpdate = crbNatureOfInformationRepository.findAll().size();
        crbNatureOfInformation.setId(count.incrementAndGet());

        // Create the CrbNatureOfInformation
        CrbNatureOfInformationDTO crbNatureOfInformationDTO = crbNatureOfInformationMapper.toDto(crbNatureOfInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbNatureOfInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbNatureOfInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbNatureOfInformation in the database
        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbNatureOfInformation in Elasticsearch
        verify(mockCrbNatureOfInformationSearchRepository, times(0)).save(crbNatureOfInformation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrbNatureOfInformation() throws Exception {
        int databaseSizeBeforeUpdate = crbNatureOfInformationRepository.findAll().size();
        crbNatureOfInformation.setId(count.incrementAndGet());

        // Create the CrbNatureOfInformation
        CrbNatureOfInformationDTO crbNatureOfInformationDTO = crbNatureOfInformationMapper.toDto(crbNatureOfInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbNatureOfInformationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crbNatureOfInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbNatureOfInformation in the database
        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbNatureOfInformation in Elasticsearch
        verify(mockCrbNatureOfInformationSearchRepository, times(0)).save(crbNatureOfInformation);
    }

    @Test
    @Transactional
    void partialUpdateCrbNatureOfInformationWithPatch() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        int databaseSizeBeforeUpdate = crbNatureOfInformationRepository.findAll().size();

        // Update the crbNatureOfInformation using partial update
        CrbNatureOfInformation partialUpdatedCrbNatureOfInformation = new CrbNatureOfInformation();
        partialUpdatedCrbNatureOfInformation.setId(crbNatureOfInformation.getId());

        restCrbNatureOfInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbNatureOfInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbNatureOfInformation))
            )
            .andExpect(status().isOk());

        // Validate the CrbNatureOfInformation in the database
        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeUpdate);
        CrbNatureOfInformation testCrbNatureOfInformation = crbNatureOfInformationList.get(crbNatureOfInformationList.size() - 1);
        assertThat(testCrbNatureOfInformation.getNatureOfInformationTypeCode()).isEqualTo(DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE);
        assertThat(testCrbNatureOfInformation.getNatureOfInformationType()).isEqualTo(DEFAULT_NATURE_OF_INFORMATION_TYPE);
        assertThat(testCrbNatureOfInformation.getNatureOfInformationTypeDescription())
            .isEqualTo(DEFAULT_NATURE_OF_INFORMATION_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCrbNatureOfInformationWithPatch() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        int databaseSizeBeforeUpdate = crbNatureOfInformationRepository.findAll().size();

        // Update the crbNatureOfInformation using partial update
        CrbNatureOfInformation partialUpdatedCrbNatureOfInformation = new CrbNatureOfInformation();
        partialUpdatedCrbNatureOfInformation.setId(crbNatureOfInformation.getId());

        partialUpdatedCrbNatureOfInformation
            .natureOfInformationTypeCode(UPDATED_NATURE_OF_INFORMATION_TYPE_CODE)
            .natureOfInformationType(UPDATED_NATURE_OF_INFORMATION_TYPE)
            .natureOfInformationTypeDescription(UPDATED_NATURE_OF_INFORMATION_TYPE_DESCRIPTION);

        restCrbNatureOfInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrbNatureOfInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrbNatureOfInformation))
            )
            .andExpect(status().isOk());

        // Validate the CrbNatureOfInformation in the database
        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeUpdate);
        CrbNatureOfInformation testCrbNatureOfInformation = crbNatureOfInformationList.get(crbNatureOfInformationList.size() - 1);
        assertThat(testCrbNatureOfInformation.getNatureOfInformationTypeCode()).isEqualTo(UPDATED_NATURE_OF_INFORMATION_TYPE_CODE);
        assertThat(testCrbNatureOfInformation.getNatureOfInformationType()).isEqualTo(UPDATED_NATURE_OF_INFORMATION_TYPE);
        assertThat(testCrbNatureOfInformation.getNatureOfInformationTypeDescription())
            .isEqualTo(UPDATED_NATURE_OF_INFORMATION_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCrbNatureOfInformation() throws Exception {
        int databaseSizeBeforeUpdate = crbNatureOfInformationRepository.findAll().size();
        crbNatureOfInformation.setId(count.incrementAndGet());

        // Create the CrbNatureOfInformation
        CrbNatureOfInformationDTO crbNatureOfInformationDTO = crbNatureOfInformationMapper.toDto(crbNatureOfInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrbNatureOfInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crbNatureOfInformationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbNatureOfInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbNatureOfInformation in the database
        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbNatureOfInformation in Elasticsearch
        verify(mockCrbNatureOfInformationSearchRepository, times(0)).save(crbNatureOfInformation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrbNatureOfInformation() throws Exception {
        int databaseSizeBeforeUpdate = crbNatureOfInformationRepository.findAll().size();
        crbNatureOfInformation.setId(count.incrementAndGet());

        // Create the CrbNatureOfInformation
        CrbNatureOfInformationDTO crbNatureOfInformationDTO = crbNatureOfInformationMapper.toDto(crbNatureOfInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbNatureOfInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbNatureOfInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CrbNatureOfInformation in the database
        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbNatureOfInformation in Elasticsearch
        verify(mockCrbNatureOfInformationSearchRepository, times(0)).save(crbNatureOfInformation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrbNatureOfInformation() throws Exception {
        int databaseSizeBeforeUpdate = crbNatureOfInformationRepository.findAll().size();
        crbNatureOfInformation.setId(count.incrementAndGet());

        // Create the CrbNatureOfInformation
        CrbNatureOfInformationDTO crbNatureOfInformationDTO = crbNatureOfInformationMapper.toDto(crbNatureOfInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrbNatureOfInformationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crbNatureOfInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CrbNatureOfInformation in the database
        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CrbNatureOfInformation in Elasticsearch
        verify(mockCrbNatureOfInformationSearchRepository, times(0)).save(crbNatureOfInformation);
    }

    @Test
    @Transactional
    void deleteCrbNatureOfInformation() throws Exception {
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);

        int databaseSizeBeforeDelete = crbNatureOfInformationRepository.findAll().size();

        // Delete the crbNatureOfInformation
        restCrbNatureOfInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, crbNatureOfInformation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CrbNatureOfInformation> crbNatureOfInformationList = crbNatureOfInformationRepository.findAll();
        assertThat(crbNatureOfInformationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CrbNatureOfInformation in Elasticsearch
        verify(mockCrbNatureOfInformationSearchRepository, times(1)).deleteById(crbNatureOfInformation.getId());
    }

    @Test
    @Transactional
    void searchCrbNatureOfInformation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        crbNatureOfInformationRepository.saveAndFlush(crbNatureOfInformation);
        when(mockCrbNatureOfInformationSearchRepository.search("id:" + crbNatureOfInformation.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(crbNatureOfInformation), PageRequest.of(0, 1), 1));

        // Search the crbNatureOfInformation
        restCrbNatureOfInformationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + crbNatureOfInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crbNatureOfInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].natureOfInformationTypeCode").value(hasItem(DEFAULT_NATURE_OF_INFORMATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].natureOfInformationType").value(hasItem(DEFAULT_NATURE_OF_INFORMATION_TYPE)))
            .andExpect(
                jsonPath("$.[*].natureOfInformationTypeDescription")
                    .value(hasItem(DEFAULT_NATURE_OF_INFORMATION_TYPE_DESCRIPTION.toString()))
            );
    }
}
