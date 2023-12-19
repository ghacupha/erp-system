package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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
import io.github.erp.domain.NatureOfCustomerComplaints;
import io.github.erp.repository.NatureOfCustomerComplaintsRepository;
import io.github.erp.repository.search.NatureOfCustomerComplaintsSearchRepository;
import io.github.erp.service.dto.NatureOfCustomerComplaintsDTO;
import io.github.erp.service.mapper.NatureOfCustomerComplaintsMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.NatureOfCustomerComplaintsResource;
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
 * Integration tests for the {@link NatureOfCustomerComplaintsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class NatureOfCustomerComplaintsResourceIT {

    private static final String DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NATURE_OF_COMPLAINT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_NATURE_OF_COMPLAINT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NATURE_OF_COMPLAINT_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_NATURE_OF_COMPLAINT_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/nature-of-customer-complaints";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/nature-of-customer-complaints";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NatureOfCustomerComplaintsRepository natureOfCustomerComplaintsRepository;

    @Autowired
    private NatureOfCustomerComplaintsMapper natureOfCustomerComplaintsMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.NatureOfCustomerComplaintsSearchRepositoryMockConfiguration
     */
    @Autowired
    private NatureOfCustomerComplaintsSearchRepository mockNatureOfCustomerComplaintsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNatureOfCustomerComplaintsMockMvc;

    private NatureOfCustomerComplaints natureOfCustomerComplaints;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NatureOfCustomerComplaints createEntity(EntityManager em) {
        NatureOfCustomerComplaints natureOfCustomerComplaints = new NatureOfCustomerComplaints()
            .natureOfComplaintTypeCode(DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE)
            .natureOfComplaintType(DEFAULT_NATURE_OF_COMPLAINT_TYPE)
            .natureOfComplaintTypeDetails(DEFAULT_NATURE_OF_COMPLAINT_TYPE_DETAILS);
        return natureOfCustomerComplaints;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NatureOfCustomerComplaints createUpdatedEntity(EntityManager em) {
        NatureOfCustomerComplaints natureOfCustomerComplaints = new NatureOfCustomerComplaints()
            .natureOfComplaintTypeCode(UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE)
            .natureOfComplaintType(UPDATED_NATURE_OF_COMPLAINT_TYPE)
            .natureOfComplaintTypeDetails(UPDATED_NATURE_OF_COMPLAINT_TYPE_DETAILS);
        return natureOfCustomerComplaints;
    }

    @BeforeEach
    public void initTest() {
        natureOfCustomerComplaints = createEntity(em);
    }

    @Test
    @Transactional
    void createNatureOfCustomerComplaints() throws Exception {
        int databaseSizeBeforeCreate = natureOfCustomerComplaintsRepository.findAll().size();
        // Create the NatureOfCustomerComplaints
        NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO = natureOfCustomerComplaintsMapper.toDto(natureOfCustomerComplaints);
        restNatureOfCustomerComplaintsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureOfCustomerComplaintsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the NatureOfCustomerComplaints in the database
        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeCreate + 1);
        NatureOfCustomerComplaints testNatureOfCustomerComplaints = natureOfCustomerComplaintsList.get(
            natureOfCustomerComplaintsList.size() - 1
        );
        assertThat(testNatureOfCustomerComplaints.getNatureOfComplaintTypeCode()).isEqualTo(DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE);
        assertThat(testNatureOfCustomerComplaints.getNatureOfComplaintType()).isEqualTo(DEFAULT_NATURE_OF_COMPLAINT_TYPE);
        assertThat(testNatureOfCustomerComplaints.getNatureOfComplaintTypeDetails()).isEqualTo(DEFAULT_NATURE_OF_COMPLAINT_TYPE_DETAILS);

        // Validate the NatureOfCustomerComplaints in Elasticsearch
        verify(mockNatureOfCustomerComplaintsSearchRepository, times(1)).save(testNatureOfCustomerComplaints);
    }

    @Test
    @Transactional
    void createNatureOfCustomerComplaintsWithExistingId() throws Exception {
        // Create the NatureOfCustomerComplaints with an existing ID
        natureOfCustomerComplaints.setId(1L);
        NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO = natureOfCustomerComplaintsMapper.toDto(natureOfCustomerComplaints);

        int databaseSizeBeforeCreate = natureOfCustomerComplaintsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNatureOfCustomerComplaintsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureOfCustomerComplaintsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureOfCustomerComplaints in the database
        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeCreate);

        // Validate the NatureOfCustomerComplaints in Elasticsearch
        verify(mockNatureOfCustomerComplaintsSearchRepository, times(0)).save(natureOfCustomerComplaints);
    }

    @Test
    @Transactional
    void checkNatureOfComplaintTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = natureOfCustomerComplaintsRepository.findAll().size();
        // set the field null
        natureOfCustomerComplaints.setNatureOfComplaintTypeCode(null);

        // Create the NatureOfCustomerComplaints, which fails.
        NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO = natureOfCustomerComplaintsMapper.toDto(natureOfCustomerComplaints);

        restNatureOfCustomerComplaintsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureOfCustomerComplaintsDTO))
            )
            .andExpect(status().isBadRequest());

        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNatureOfComplaintTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = natureOfCustomerComplaintsRepository.findAll().size();
        // set the field null
        natureOfCustomerComplaints.setNatureOfComplaintType(null);

        // Create the NatureOfCustomerComplaints, which fails.
        NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO = natureOfCustomerComplaintsMapper.toDto(natureOfCustomerComplaints);

        restNatureOfCustomerComplaintsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureOfCustomerComplaintsDTO))
            )
            .andExpect(status().isBadRequest());

        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaints() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList
        restNatureOfCustomerComplaintsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(natureOfCustomerComplaints.getId().intValue())))
            .andExpect(jsonPath("$.[*].natureOfComplaintTypeCode").value(hasItem(DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].natureOfComplaintType").value(hasItem(DEFAULT_NATURE_OF_COMPLAINT_TYPE)))
            .andExpect(jsonPath("$.[*].natureOfComplaintTypeDetails").value(hasItem(DEFAULT_NATURE_OF_COMPLAINT_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getNatureOfCustomerComplaints() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get the natureOfCustomerComplaints
        restNatureOfCustomerComplaintsMockMvc
            .perform(get(ENTITY_API_URL_ID, natureOfCustomerComplaints.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(natureOfCustomerComplaints.getId().intValue()))
            .andExpect(jsonPath("$.natureOfComplaintTypeCode").value(DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE))
            .andExpect(jsonPath("$.natureOfComplaintType").value(DEFAULT_NATURE_OF_COMPLAINT_TYPE))
            .andExpect(jsonPath("$.natureOfComplaintTypeDetails").value(DEFAULT_NATURE_OF_COMPLAINT_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getNatureOfCustomerComplaintsByIdFiltering() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        Long id = natureOfCustomerComplaints.getId();

        defaultNatureOfCustomerComplaintsShouldBeFound("id.equals=" + id);
        defaultNatureOfCustomerComplaintsShouldNotBeFound("id.notEquals=" + id);

        defaultNatureOfCustomerComplaintsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNatureOfCustomerComplaintsShouldNotBeFound("id.greaterThan=" + id);

        defaultNatureOfCustomerComplaintsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNatureOfCustomerComplaintsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaintsByNatureOfComplaintTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintTypeCode equals to DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE
        defaultNatureOfCustomerComplaintsShouldBeFound("natureOfComplaintTypeCode.equals=" + DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintTypeCode equals to UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE
        defaultNatureOfCustomerComplaintsShouldNotBeFound("natureOfComplaintTypeCode.equals=" + UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaintsByNatureOfComplaintTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintTypeCode not equals to DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE
        defaultNatureOfCustomerComplaintsShouldNotBeFound("natureOfComplaintTypeCode.notEquals=" + DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintTypeCode not equals to UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE
        defaultNatureOfCustomerComplaintsShouldBeFound("natureOfComplaintTypeCode.notEquals=" + UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaintsByNatureOfComplaintTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintTypeCode in DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE or UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE
        defaultNatureOfCustomerComplaintsShouldBeFound(
            "natureOfComplaintTypeCode.in=" + DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE + "," + UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE
        );

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintTypeCode equals to UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE
        defaultNatureOfCustomerComplaintsShouldNotBeFound("natureOfComplaintTypeCode.in=" + UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaintsByNatureOfComplaintTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintTypeCode is not null
        defaultNatureOfCustomerComplaintsShouldBeFound("natureOfComplaintTypeCode.specified=true");

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintTypeCode is null
        defaultNatureOfCustomerComplaintsShouldNotBeFound("natureOfComplaintTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaintsByNatureOfComplaintTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintTypeCode contains DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE
        defaultNatureOfCustomerComplaintsShouldBeFound("natureOfComplaintTypeCode.contains=" + DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintTypeCode contains UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE
        defaultNatureOfCustomerComplaintsShouldNotBeFound("natureOfComplaintTypeCode.contains=" + UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaintsByNatureOfComplaintTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintTypeCode does not contain DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE
        defaultNatureOfCustomerComplaintsShouldNotBeFound(
            "natureOfComplaintTypeCode.doesNotContain=" + DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE
        );

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintTypeCode does not contain UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE
        defaultNatureOfCustomerComplaintsShouldBeFound("natureOfComplaintTypeCode.doesNotContain=" + UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaintsByNatureOfComplaintTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintType equals to DEFAULT_NATURE_OF_COMPLAINT_TYPE
        defaultNatureOfCustomerComplaintsShouldBeFound("natureOfComplaintType.equals=" + DEFAULT_NATURE_OF_COMPLAINT_TYPE);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintType equals to UPDATED_NATURE_OF_COMPLAINT_TYPE
        defaultNatureOfCustomerComplaintsShouldNotBeFound("natureOfComplaintType.equals=" + UPDATED_NATURE_OF_COMPLAINT_TYPE);
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaintsByNatureOfComplaintTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintType not equals to DEFAULT_NATURE_OF_COMPLAINT_TYPE
        defaultNatureOfCustomerComplaintsShouldNotBeFound("natureOfComplaintType.notEquals=" + DEFAULT_NATURE_OF_COMPLAINT_TYPE);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintType not equals to UPDATED_NATURE_OF_COMPLAINT_TYPE
        defaultNatureOfCustomerComplaintsShouldBeFound("natureOfComplaintType.notEquals=" + UPDATED_NATURE_OF_COMPLAINT_TYPE);
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaintsByNatureOfComplaintTypeIsInShouldWork() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintType in DEFAULT_NATURE_OF_COMPLAINT_TYPE or UPDATED_NATURE_OF_COMPLAINT_TYPE
        defaultNatureOfCustomerComplaintsShouldBeFound(
            "natureOfComplaintType.in=" + DEFAULT_NATURE_OF_COMPLAINT_TYPE + "," + UPDATED_NATURE_OF_COMPLAINT_TYPE
        );

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintType equals to UPDATED_NATURE_OF_COMPLAINT_TYPE
        defaultNatureOfCustomerComplaintsShouldNotBeFound("natureOfComplaintType.in=" + UPDATED_NATURE_OF_COMPLAINT_TYPE);
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaintsByNatureOfComplaintTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintType is not null
        defaultNatureOfCustomerComplaintsShouldBeFound("natureOfComplaintType.specified=true");

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintType is null
        defaultNatureOfCustomerComplaintsShouldNotBeFound("natureOfComplaintType.specified=false");
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaintsByNatureOfComplaintTypeContainsSomething() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintType contains DEFAULT_NATURE_OF_COMPLAINT_TYPE
        defaultNatureOfCustomerComplaintsShouldBeFound("natureOfComplaintType.contains=" + DEFAULT_NATURE_OF_COMPLAINT_TYPE);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintType contains UPDATED_NATURE_OF_COMPLAINT_TYPE
        defaultNatureOfCustomerComplaintsShouldNotBeFound("natureOfComplaintType.contains=" + UPDATED_NATURE_OF_COMPLAINT_TYPE);
    }

    @Test
    @Transactional
    void getAllNatureOfCustomerComplaintsByNatureOfComplaintTypeNotContainsSomething() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintType does not contain DEFAULT_NATURE_OF_COMPLAINT_TYPE
        defaultNatureOfCustomerComplaintsShouldNotBeFound("natureOfComplaintType.doesNotContain=" + DEFAULT_NATURE_OF_COMPLAINT_TYPE);

        // Get all the natureOfCustomerComplaintsList where natureOfComplaintType does not contain UPDATED_NATURE_OF_COMPLAINT_TYPE
        defaultNatureOfCustomerComplaintsShouldBeFound("natureOfComplaintType.doesNotContain=" + UPDATED_NATURE_OF_COMPLAINT_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNatureOfCustomerComplaintsShouldBeFound(String filter) throws Exception {
        restNatureOfCustomerComplaintsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(natureOfCustomerComplaints.getId().intValue())))
            .andExpect(jsonPath("$.[*].natureOfComplaintTypeCode").value(hasItem(DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].natureOfComplaintType").value(hasItem(DEFAULT_NATURE_OF_COMPLAINT_TYPE)))
            .andExpect(jsonPath("$.[*].natureOfComplaintTypeDetails").value(hasItem(DEFAULT_NATURE_OF_COMPLAINT_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restNatureOfCustomerComplaintsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNatureOfCustomerComplaintsShouldNotBeFound(String filter) throws Exception {
        restNatureOfCustomerComplaintsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNatureOfCustomerComplaintsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNatureOfCustomerComplaints() throws Exception {
        // Get the natureOfCustomerComplaints
        restNatureOfCustomerComplaintsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNatureOfCustomerComplaints() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        int databaseSizeBeforeUpdate = natureOfCustomerComplaintsRepository.findAll().size();

        // Update the natureOfCustomerComplaints
        NatureOfCustomerComplaints updatedNatureOfCustomerComplaints = natureOfCustomerComplaintsRepository
            .findById(natureOfCustomerComplaints.getId())
            .get();
        // Disconnect from session so that the updates on updatedNatureOfCustomerComplaints are not directly saved in db
        em.detach(updatedNatureOfCustomerComplaints);
        updatedNatureOfCustomerComplaints
            .natureOfComplaintTypeCode(UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE)
            .natureOfComplaintType(UPDATED_NATURE_OF_COMPLAINT_TYPE)
            .natureOfComplaintTypeDetails(UPDATED_NATURE_OF_COMPLAINT_TYPE_DETAILS);
        NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO = natureOfCustomerComplaintsMapper.toDto(
            updatedNatureOfCustomerComplaints
        );

        restNatureOfCustomerComplaintsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, natureOfCustomerComplaintsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureOfCustomerComplaintsDTO))
            )
            .andExpect(status().isOk());

        // Validate the NatureOfCustomerComplaints in the database
        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeUpdate);
        NatureOfCustomerComplaints testNatureOfCustomerComplaints = natureOfCustomerComplaintsList.get(
            natureOfCustomerComplaintsList.size() - 1
        );
        assertThat(testNatureOfCustomerComplaints.getNatureOfComplaintTypeCode()).isEqualTo(UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE);
        assertThat(testNatureOfCustomerComplaints.getNatureOfComplaintType()).isEqualTo(UPDATED_NATURE_OF_COMPLAINT_TYPE);
        assertThat(testNatureOfCustomerComplaints.getNatureOfComplaintTypeDetails()).isEqualTo(UPDATED_NATURE_OF_COMPLAINT_TYPE_DETAILS);

        // Validate the NatureOfCustomerComplaints in Elasticsearch
        verify(mockNatureOfCustomerComplaintsSearchRepository).save(testNatureOfCustomerComplaints);
    }

    @Test
    @Transactional
    void putNonExistingNatureOfCustomerComplaints() throws Exception {
        int databaseSizeBeforeUpdate = natureOfCustomerComplaintsRepository.findAll().size();
        natureOfCustomerComplaints.setId(count.incrementAndGet());

        // Create the NatureOfCustomerComplaints
        NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO = natureOfCustomerComplaintsMapper.toDto(natureOfCustomerComplaints);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNatureOfCustomerComplaintsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, natureOfCustomerComplaintsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureOfCustomerComplaintsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureOfCustomerComplaints in the database
        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NatureOfCustomerComplaints in Elasticsearch
        verify(mockNatureOfCustomerComplaintsSearchRepository, times(0)).save(natureOfCustomerComplaints);
    }

    @Test
    @Transactional
    void putWithIdMismatchNatureOfCustomerComplaints() throws Exception {
        int databaseSizeBeforeUpdate = natureOfCustomerComplaintsRepository.findAll().size();
        natureOfCustomerComplaints.setId(count.incrementAndGet());

        // Create the NatureOfCustomerComplaints
        NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO = natureOfCustomerComplaintsMapper.toDto(natureOfCustomerComplaints);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNatureOfCustomerComplaintsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureOfCustomerComplaintsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureOfCustomerComplaints in the database
        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NatureOfCustomerComplaints in Elasticsearch
        verify(mockNatureOfCustomerComplaintsSearchRepository, times(0)).save(natureOfCustomerComplaints);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNatureOfCustomerComplaints() throws Exception {
        int databaseSizeBeforeUpdate = natureOfCustomerComplaintsRepository.findAll().size();
        natureOfCustomerComplaints.setId(count.incrementAndGet());

        // Create the NatureOfCustomerComplaints
        NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO = natureOfCustomerComplaintsMapper.toDto(natureOfCustomerComplaints);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNatureOfCustomerComplaintsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(natureOfCustomerComplaintsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NatureOfCustomerComplaints in the database
        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NatureOfCustomerComplaints in Elasticsearch
        verify(mockNatureOfCustomerComplaintsSearchRepository, times(0)).save(natureOfCustomerComplaints);
    }

    @Test
    @Transactional
    void partialUpdateNatureOfCustomerComplaintsWithPatch() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        int databaseSizeBeforeUpdate = natureOfCustomerComplaintsRepository.findAll().size();

        // Update the natureOfCustomerComplaints using partial update
        NatureOfCustomerComplaints partialUpdatedNatureOfCustomerComplaints = new NatureOfCustomerComplaints();
        partialUpdatedNatureOfCustomerComplaints.setId(natureOfCustomerComplaints.getId());

        partialUpdatedNatureOfCustomerComplaints
            .natureOfComplaintType(UPDATED_NATURE_OF_COMPLAINT_TYPE)
            .natureOfComplaintTypeDetails(UPDATED_NATURE_OF_COMPLAINT_TYPE_DETAILS);

        restNatureOfCustomerComplaintsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNatureOfCustomerComplaints.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNatureOfCustomerComplaints))
            )
            .andExpect(status().isOk());

        // Validate the NatureOfCustomerComplaints in the database
        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeUpdate);
        NatureOfCustomerComplaints testNatureOfCustomerComplaints = natureOfCustomerComplaintsList.get(
            natureOfCustomerComplaintsList.size() - 1
        );
        assertThat(testNatureOfCustomerComplaints.getNatureOfComplaintTypeCode()).isEqualTo(DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE);
        assertThat(testNatureOfCustomerComplaints.getNatureOfComplaintType()).isEqualTo(UPDATED_NATURE_OF_COMPLAINT_TYPE);
        assertThat(testNatureOfCustomerComplaints.getNatureOfComplaintTypeDetails()).isEqualTo(UPDATED_NATURE_OF_COMPLAINT_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateNatureOfCustomerComplaintsWithPatch() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        int databaseSizeBeforeUpdate = natureOfCustomerComplaintsRepository.findAll().size();

        // Update the natureOfCustomerComplaints using partial update
        NatureOfCustomerComplaints partialUpdatedNatureOfCustomerComplaints = new NatureOfCustomerComplaints();
        partialUpdatedNatureOfCustomerComplaints.setId(natureOfCustomerComplaints.getId());

        partialUpdatedNatureOfCustomerComplaints
            .natureOfComplaintTypeCode(UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE)
            .natureOfComplaintType(UPDATED_NATURE_OF_COMPLAINT_TYPE)
            .natureOfComplaintTypeDetails(UPDATED_NATURE_OF_COMPLAINT_TYPE_DETAILS);

        restNatureOfCustomerComplaintsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNatureOfCustomerComplaints.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNatureOfCustomerComplaints))
            )
            .andExpect(status().isOk());

        // Validate the NatureOfCustomerComplaints in the database
        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeUpdate);
        NatureOfCustomerComplaints testNatureOfCustomerComplaints = natureOfCustomerComplaintsList.get(
            natureOfCustomerComplaintsList.size() - 1
        );
        assertThat(testNatureOfCustomerComplaints.getNatureOfComplaintTypeCode()).isEqualTo(UPDATED_NATURE_OF_COMPLAINT_TYPE_CODE);
        assertThat(testNatureOfCustomerComplaints.getNatureOfComplaintType()).isEqualTo(UPDATED_NATURE_OF_COMPLAINT_TYPE);
        assertThat(testNatureOfCustomerComplaints.getNatureOfComplaintTypeDetails()).isEqualTo(UPDATED_NATURE_OF_COMPLAINT_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingNatureOfCustomerComplaints() throws Exception {
        int databaseSizeBeforeUpdate = natureOfCustomerComplaintsRepository.findAll().size();
        natureOfCustomerComplaints.setId(count.incrementAndGet());

        // Create the NatureOfCustomerComplaints
        NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO = natureOfCustomerComplaintsMapper.toDto(natureOfCustomerComplaints);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNatureOfCustomerComplaintsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, natureOfCustomerComplaintsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(natureOfCustomerComplaintsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureOfCustomerComplaints in the database
        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NatureOfCustomerComplaints in Elasticsearch
        verify(mockNatureOfCustomerComplaintsSearchRepository, times(0)).save(natureOfCustomerComplaints);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNatureOfCustomerComplaints() throws Exception {
        int databaseSizeBeforeUpdate = natureOfCustomerComplaintsRepository.findAll().size();
        natureOfCustomerComplaints.setId(count.incrementAndGet());

        // Create the NatureOfCustomerComplaints
        NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO = natureOfCustomerComplaintsMapper.toDto(natureOfCustomerComplaints);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNatureOfCustomerComplaintsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(natureOfCustomerComplaintsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NatureOfCustomerComplaints in the database
        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NatureOfCustomerComplaints in Elasticsearch
        verify(mockNatureOfCustomerComplaintsSearchRepository, times(0)).save(natureOfCustomerComplaints);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNatureOfCustomerComplaints() throws Exception {
        int databaseSizeBeforeUpdate = natureOfCustomerComplaintsRepository.findAll().size();
        natureOfCustomerComplaints.setId(count.incrementAndGet());

        // Create the NatureOfCustomerComplaints
        NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO = natureOfCustomerComplaintsMapper.toDto(natureOfCustomerComplaints);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNatureOfCustomerComplaintsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(natureOfCustomerComplaintsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NatureOfCustomerComplaints in the database
        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the NatureOfCustomerComplaints in Elasticsearch
        verify(mockNatureOfCustomerComplaintsSearchRepository, times(0)).save(natureOfCustomerComplaints);
    }

    @Test
    @Transactional
    void deleteNatureOfCustomerComplaints() throws Exception {
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);

        int databaseSizeBeforeDelete = natureOfCustomerComplaintsRepository.findAll().size();

        // Delete the natureOfCustomerComplaints
        restNatureOfCustomerComplaintsMockMvc
            .perform(delete(ENTITY_API_URL_ID, natureOfCustomerComplaints.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NatureOfCustomerComplaints> natureOfCustomerComplaintsList = natureOfCustomerComplaintsRepository.findAll();
        assertThat(natureOfCustomerComplaintsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the NatureOfCustomerComplaints in Elasticsearch
        verify(mockNatureOfCustomerComplaintsSearchRepository, times(1)).deleteById(natureOfCustomerComplaints.getId());
    }

    @Test
    @Transactional
    void searchNatureOfCustomerComplaints() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        natureOfCustomerComplaintsRepository.saveAndFlush(natureOfCustomerComplaints);
        when(mockNatureOfCustomerComplaintsSearchRepository.search("id:" + natureOfCustomerComplaints.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(natureOfCustomerComplaints), PageRequest.of(0, 1), 1));

        // Search the natureOfCustomerComplaints
        restNatureOfCustomerComplaintsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + natureOfCustomerComplaints.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(natureOfCustomerComplaints.getId().intValue())))
            .andExpect(jsonPath("$.[*].natureOfComplaintTypeCode").value(hasItem(DEFAULT_NATURE_OF_COMPLAINT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].natureOfComplaintType").value(hasItem(DEFAULT_NATURE_OF_COMPLAINT_TYPE)))
            .andExpect(jsonPath("$.[*].natureOfComplaintTypeDetails").value(hasItem(DEFAULT_NATURE_OF_COMPLAINT_TYPE_DETAILS.toString())));
    }
}
