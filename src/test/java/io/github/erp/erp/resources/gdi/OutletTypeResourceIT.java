package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.OutletType;
import io.github.erp.domain.Placeholder;
import io.github.erp.erp.resources.PlaceholderResourceIT;
import io.github.erp.repository.OutletTypeRepository;
import io.github.erp.repository.search.OutletTypeSearchRepository;
import io.github.erp.service.OutletTypeService;
import io.github.erp.service.dto.OutletTypeDTO;
import io.github.erp.service.mapper.OutletTypeMapper;
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
import java.util.ArrayList;
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
 * Integration tests for the OutletTypeResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER", "FIXED_ASSETS_USER"})
public class OutletTypeResourceIT {

    private static final String DEFAULT_OUTLET_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_OUTLET_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_OUTLET_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_OUTLET_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_OUTLET_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_OUTLET_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/outlet-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/outlet-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OutletTypeRepository outletTypeRepository;

    @Mock
    private OutletTypeRepository outletTypeRepositoryMock;

    @Autowired
    private OutletTypeMapper outletTypeMapper;

    @Mock
    private OutletTypeService outletTypeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.OutletTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private OutletTypeSearchRepository mockOutletTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOutletTypeMockMvc;

    private OutletType outletType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutletType createEntity(EntityManager em) {
        OutletType outletType = new OutletType()
            .outletTypeCode(DEFAULT_OUTLET_TYPE_CODE)
            .outletType(DEFAULT_OUTLET_TYPE)
            .outletTypeDetails(DEFAULT_OUTLET_TYPE_DETAILS);
        return outletType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutletType createUpdatedEntity(EntityManager em) {
        OutletType outletType = new OutletType()
            .outletTypeCode(UPDATED_OUTLET_TYPE_CODE)
            .outletType(UPDATED_OUTLET_TYPE)
            .outletTypeDetails(UPDATED_OUTLET_TYPE_DETAILS);
        return outletType;
    }

    @BeforeEach
    public void initTest() {
        outletType = createEntity(em);
    }

    @Test
    @Transactional
    void createOutletType() throws Exception {
        int databaseSizeBeforeCreate = outletTypeRepository.findAll().size();
        // Create the OutletType
        OutletTypeDTO outletTypeDTO = outletTypeMapper.toDto(outletType);
        restOutletTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outletTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the OutletType in the database
        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeCreate + 1);
        OutletType testOutletType = outletTypeList.get(outletTypeList.size() - 1);
        assertThat(testOutletType.getOutletTypeCode()).isEqualTo(DEFAULT_OUTLET_TYPE_CODE);
        assertThat(testOutletType.getOutletType()).isEqualTo(DEFAULT_OUTLET_TYPE);
        assertThat(testOutletType.getOutletTypeDetails()).isEqualTo(DEFAULT_OUTLET_TYPE_DETAILS);

        // Validate the OutletType in Elasticsearch
        verify(mockOutletTypeSearchRepository, times(1)).save(testOutletType);
    }

    @Test
    @Transactional
    void createOutletTypeWithExistingId() throws Exception {
        // Create the OutletType with an existing ID
        outletType.setId(1L);
        OutletTypeDTO outletTypeDTO = outletTypeMapper.toDto(outletType);

        int databaseSizeBeforeCreate = outletTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutletTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outletTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OutletType in the database
        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the OutletType in Elasticsearch
        verify(mockOutletTypeSearchRepository, times(0)).save(outletType);
    }

    @Test
    @Transactional
    void checkOutletTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = outletTypeRepository.findAll().size();
        // set the field null
        outletType.setOutletTypeCode(null);

        // Create the OutletType, which fails.
        OutletTypeDTO outletTypeDTO = outletTypeMapper.toDto(outletType);

        restOutletTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outletTypeDTO)))
            .andExpect(status().isBadRequest());

        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOutletTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = outletTypeRepository.findAll().size();
        // set the field null
        outletType.setOutletType(null);

        // Create the OutletType, which fails.
        OutletTypeDTO outletTypeDTO = outletTypeMapper.toDto(outletType);

        restOutletTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outletTypeDTO)))
            .andExpect(status().isBadRequest());

        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOutletTypes() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList
        restOutletTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outletType.getId().intValue())))
            .andExpect(jsonPath("$.[*].outletTypeCode").value(hasItem(DEFAULT_OUTLET_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].outletType").value(hasItem(DEFAULT_OUTLET_TYPE)))
            .andExpect(jsonPath("$.[*].outletTypeDetails").value(hasItem(DEFAULT_OUTLET_TYPE_DETAILS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOutletTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(outletTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOutletTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(outletTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOutletTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(outletTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOutletTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(outletTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getOutletType() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get the outletType
        restOutletTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, outletType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(outletType.getId().intValue()))
            .andExpect(jsonPath("$.outletTypeCode").value(DEFAULT_OUTLET_TYPE_CODE))
            .andExpect(jsonPath("$.outletType").value(DEFAULT_OUTLET_TYPE))
            .andExpect(jsonPath("$.outletTypeDetails").value(DEFAULT_OUTLET_TYPE_DETAILS));
    }

    @Test
    @Transactional
    void getOutletTypesByIdFiltering() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        Long id = outletType.getId();

        defaultOutletTypeShouldBeFound("id.equals=" + id);
        defaultOutletTypeShouldNotBeFound("id.notEquals=" + id);

        defaultOutletTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOutletTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultOutletTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOutletTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletTypeCode equals to DEFAULT_OUTLET_TYPE_CODE
        defaultOutletTypeShouldBeFound("outletTypeCode.equals=" + DEFAULT_OUTLET_TYPE_CODE);

        // Get all the outletTypeList where outletTypeCode equals to UPDATED_OUTLET_TYPE_CODE
        defaultOutletTypeShouldNotBeFound("outletTypeCode.equals=" + UPDATED_OUTLET_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletTypeCode not equals to DEFAULT_OUTLET_TYPE_CODE
        defaultOutletTypeShouldNotBeFound("outletTypeCode.notEquals=" + DEFAULT_OUTLET_TYPE_CODE);

        // Get all the outletTypeList where outletTypeCode not equals to UPDATED_OUTLET_TYPE_CODE
        defaultOutletTypeShouldBeFound("outletTypeCode.notEquals=" + UPDATED_OUTLET_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletTypeCode in DEFAULT_OUTLET_TYPE_CODE or UPDATED_OUTLET_TYPE_CODE
        defaultOutletTypeShouldBeFound("outletTypeCode.in=" + DEFAULT_OUTLET_TYPE_CODE + "," + UPDATED_OUTLET_TYPE_CODE);

        // Get all the outletTypeList where outletTypeCode equals to UPDATED_OUTLET_TYPE_CODE
        defaultOutletTypeShouldNotBeFound("outletTypeCode.in=" + UPDATED_OUTLET_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletTypeCode is not null
        defaultOutletTypeShouldBeFound("outletTypeCode.specified=true");

        // Get all the outletTypeList where outletTypeCode is null
        defaultOutletTypeShouldNotBeFound("outletTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletTypeCode contains DEFAULT_OUTLET_TYPE_CODE
        defaultOutletTypeShouldBeFound("outletTypeCode.contains=" + DEFAULT_OUTLET_TYPE_CODE);

        // Get all the outletTypeList where outletTypeCode contains UPDATED_OUTLET_TYPE_CODE
        defaultOutletTypeShouldNotBeFound("outletTypeCode.contains=" + UPDATED_OUTLET_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletTypeCode does not contain DEFAULT_OUTLET_TYPE_CODE
        defaultOutletTypeShouldNotBeFound("outletTypeCode.doesNotContain=" + DEFAULT_OUTLET_TYPE_CODE);

        // Get all the outletTypeList where outletTypeCode does not contain UPDATED_OUTLET_TYPE_CODE
        defaultOutletTypeShouldBeFound("outletTypeCode.doesNotContain=" + UPDATED_OUTLET_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletType equals to DEFAULT_OUTLET_TYPE
        defaultOutletTypeShouldBeFound("outletType.equals=" + DEFAULT_OUTLET_TYPE);

        // Get all the outletTypeList where outletType equals to UPDATED_OUTLET_TYPE
        defaultOutletTypeShouldNotBeFound("outletType.equals=" + UPDATED_OUTLET_TYPE);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletType not equals to DEFAULT_OUTLET_TYPE
        defaultOutletTypeShouldNotBeFound("outletType.notEquals=" + DEFAULT_OUTLET_TYPE);

        // Get all the outletTypeList where outletType not equals to UPDATED_OUTLET_TYPE
        defaultOutletTypeShouldBeFound("outletType.notEquals=" + UPDATED_OUTLET_TYPE);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeIsInShouldWork() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletType in DEFAULT_OUTLET_TYPE or UPDATED_OUTLET_TYPE
        defaultOutletTypeShouldBeFound("outletType.in=" + DEFAULT_OUTLET_TYPE + "," + UPDATED_OUTLET_TYPE);

        // Get all the outletTypeList where outletType equals to UPDATED_OUTLET_TYPE
        defaultOutletTypeShouldNotBeFound("outletType.in=" + UPDATED_OUTLET_TYPE);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletType is not null
        defaultOutletTypeShouldBeFound("outletType.specified=true");

        // Get all the outletTypeList where outletType is null
        defaultOutletTypeShouldNotBeFound("outletType.specified=false");
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeContainsSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletType contains DEFAULT_OUTLET_TYPE
        defaultOutletTypeShouldBeFound("outletType.contains=" + DEFAULT_OUTLET_TYPE);

        // Get all the outletTypeList where outletType contains UPDATED_OUTLET_TYPE
        defaultOutletTypeShouldNotBeFound("outletType.contains=" + UPDATED_OUTLET_TYPE);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeNotContainsSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletType does not contain DEFAULT_OUTLET_TYPE
        defaultOutletTypeShouldNotBeFound("outletType.doesNotContain=" + DEFAULT_OUTLET_TYPE);

        // Get all the outletTypeList where outletType does not contain UPDATED_OUTLET_TYPE
        defaultOutletTypeShouldBeFound("outletType.doesNotContain=" + UPDATED_OUTLET_TYPE);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletTypeDetails equals to DEFAULT_OUTLET_TYPE_DETAILS
        defaultOutletTypeShouldBeFound("outletTypeDetails.equals=" + DEFAULT_OUTLET_TYPE_DETAILS);

        // Get all the outletTypeList where outletTypeDetails equals to UPDATED_OUTLET_TYPE_DETAILS
        defaultOutletTypeShouldNotBeFound("outletTypeDetails.equals=" + UPDATED_OUTLET_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletTypeDetails not equals to DEFAULT_OUTLET_TYPE_DETAILS
        defaultOutletTypeShouldNotBeFound("outletTypeDetails.notEquals=" + DEFAULT_OUTLET_TYPE_DETAILS);

        // Get all the outletTypeList where outletTypeDetails not equals to UPDATED_OUTLET_TYPE_DETAILS
        defaultOutletTypeShouldBeFound("outletTypeDetails.notEquals=" + UPDATED_OUTLET_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletTypeDetails in DEFAULT_OUTLET_TYPE_DETAILS or UPDATED_OUTLET_TYPE_DETAILS
        defaultOutletTypeShouldBeFound("outletTypeDetails.in=" + DEFAULT_OUTLET_TYPE_DETAILS + "," + UPDATED_OUTLET_TYPE_DETAILS);

        // Get all the outletTypeList where outletTypeDetails equals to UPDATED_OUTLET_TYPE_DETAILS
        defaultOutletTypeShouldNotBeFound("outletTypeDetails.in=" + UPDATED_OUTLET_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletTypeDetails is not null
        defaultOutletTypeShouldBeFound("outletTypeDetails.specified=true");

        // Get all the outletTypeList where outletTypeDetails is null
        defaultOutletTypeShouldNotBeFound("outletTypeDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeDetailsContainsSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletTypeDetails contains DEFAULT_OUTLET_TYPE_DETAILS
        defaultOutletTypeShouldBeFound("outletTypeDetails.contains=" + DEFAULT_OUTLET_TYPE_DETAILS);

        // Get all the outletTypeList where outletTypeDetails contains UPDATED_OUTLET_TYPE_DETAILS
        defaultOutletTypeShouldNotBeFound("outletTypeDetails.contains=" + UPDATED_OUTLET_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllOutletTypesByOutletTypeDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        // Get all the outletTypeList where outletTypeDetails does not contain DEFAULT_OUTLET_TYPE_DETAILS
        defaultOutletTypeShouldNotBeFound("outletTypeDetails.doesNotContain=" + DEFAULT_OUTLET_TYPE_DETAILS);

        // Get all the outletTypeList where outletTypeDetails does not contain UPDATED_OUTLET_TYPE_DETAILS
        defaultOutletTypeShouldBeFound("outletTypeDetails.doesNotContain=" + UPDATED_OUTLET_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void getAllOutletTypesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);
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
        outletType.addPlaceholder(placeholder);
        outletTypeRepository.saveAndFlush(outletType);
        Long placeholderId = placeholder.getId();

        // Get all the outletTypeList where placeholder equals to placeholderId
        defaultOutletTypeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the outletTypeList where placeholder equals to (placeholderId + 1)
        defaultOutletTypeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOutletTypeShouldBeFound(String filter) throws Exception {
        restOutletTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outletType.getId().intValue())))
            .andExpect(jsonPath("$.[*].outletTypeCode").value(hasItem(DEFAULT_OUTLET_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].outletType").value(hasItem(DEFAULT_OUTLET_TYPE)))
            .andExpect(jsonPath("$.[*].outletTypeDetails").value(hasItem(DEFAULT_OUTLET_TYPE_DETAILS)));

        // Check, that the count call also returns 1
        restOutletTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOutletTypeShouldNotBeFound(String filter) throws Exception {
        restOutletTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOutletTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOutletType() throws Exception {
        // Get the outletType
        restOutletTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOutletType() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        int databaseSizeBeforeUpdate = outletTypeRepository.findAll().size();

        // Update the outletType
        OutletType updatedOutletType = outletTypeRepository.findById(outletType.getId()).get();
        // Disconnect from session so that the updates on updatedOutletType are not directly saved in db
        em.detach(updatedOutletType);
        updatedOutletType
            .outletTypeCode(UPDATED_OUTLET_TYPE_CODE)
            .outletType(UPDATED_OUTLET_TYPE)
            .outletTypeDetails(UPDATED_OUTLET_TYPE_DETAILS);
        OutletTypeDTO outletTypeDTO = outletTypeMapper.toDto(updatedOutletType);

        restOutletTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outletTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outletTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the OutletType in the database
        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeUpdate);
        OutletType testOutletType = outletTypeList.get(outletTypeList.size() - 1);
        assertThat(testOutletType.getOutletTypeCode()).isEqualTo(UPDATED_OUTLET_TYPE_CODE);
        assertThat(testOutletType.getOutletType()).isEqualTo(UPDATED_OUTLET_TYPE);
        assertThat(testOutletType.getOutletTypeDetails()).isEqualTo(UPDATED_OUTLET_TYPE_DETAILS);

        // Validate the OutletType in Elasticsearch
        verify(mockOutletTypeSearchRepository).save(testOutletType);
    }

    @Test
    @Transactional
    void putNonExistingOutletType() throws Exception {
        int databaseSizeBeforeUpdate = outletTypeRepository.findAll().size();
        outletType.setId(count.incrementAndGet());

        // Create the OutletType
        OutletTypeDTO outletTypeDTO = outletTypeMapper.toDto(outletType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutletTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outletTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outletTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutletType in the database
        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutletType in Elasticsearch
        verify(mockOutletTypeSearchRepository, times(0)).save(outletType);
    }

    @Test
    @Transactional
    void putWithIdMismatchOutletType() throws Exception {
        int databaseSizeBeforeUpdate = outletTypeRepository.findAll().size();
        outletType.setId(count.incrementAndGet());

        // Create the OutletType
        OutletTypeDTO outletTypeDTO = outletTypeMapper.toDto(outletType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutletTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outletTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutletType in the database
        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutletType in Elasticsearch
        verify(mockOutletTypeSearchRepository, times(0)).save(outletType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOutletType() throws Exception {
        int databaseSizeBeforeUpdate = outletTypeRepository.findAll().size();
        outletType.setId(count.incrementAndGet());

        // Create the OutletType
        OutletTypeDTO outletTypeDTO = outletTypeMapper.toDto(outletType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutletTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outletTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OutletType in the database
        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutletType in Elasticsearch
        verify(mockOutletTypeSearchRepository, times(0)).save(outletType);
    }

    @Test
    @Transactional
    void partialUpdateOutletTypeWithPatch() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        int databaseSizeBeforeUpdate = outletTypeRepository.findAll().size();

        // Update the outletType using partial update
        OutletType partialUpdatedOutletType = new OutletType();
        partialUpdatedOutletType.setId(outletType.getId());

        partialUpdatedOutletType.outletTypeCode(UPDATED_OUTLET_TYPE_CODE);

        restOutletTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutletType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOutletType))
            )
            .andExpect(status().isOk());

        // Validate the OutletType in the database
        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeUpdate);
        OutletType testOutletType = outletTypeList.get(outletTypeList.size() - 1);
        assertThat(testOutletType.getOutletTypeCode()).isEqualTo(UPDATED_OUTLET_TYPE_CODE);
        assertThat(testOutletType.getOutletType()).isEqualTo(DEFAULT_OUTLET_TYPE);
        assertThat(testOutletType.getOutletTypeDetails()).isEqualTo(DEFAULT_OUTLET_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateOutletTypeWithPatch() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        int databaseSizeBeforeUpdate = outletTypeRepository.findAll().size();

        // Update the outletType using partial update
        OutletType partialUpdatedOutletType = new OutletType();
        partialUpdatedOutletType.setId(outletType.getId());

        partialUpdatedOutletType
            .outletTypeCode(UPDATED_OUTLET_TYPE_CODE)
            .outletType(UPDATED_OUTLET_TYPE)
            .outletTypeDetails(UPDATED_OUTLET_TYPE_DETAILS);

        restOutletTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutletType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOutletType))
            )
            .andExpect(status().isOk());

        // Validate the OutletType in the database
        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeUpdate);
        OutletType testOutletType = outletTypeList.get(outletTypeList.size() - 1);
        assertThat(testOutletType.getOutletTypeCode()).isEqualTo(UPDATED_OUTLET_TYPE_CODE);
        assertThat(testOutletType.getOutletType()).isEqualTo(UPDATED_OUTLET_TYPE);
        assertThat(testOutletType.getOutletTypeDetails()).isEqualTo(UPDATED_OUTLET_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingOutletType() throws Exception {
        int databaseSizeBeforeUpdate = outletTypeRepository.findAll().size();
        outletType.setId(count.incrementAndGet());

        // Create the OutletType
        OutletTypeDTO outletTypeDTO = outletTypeMapper.toDto(outletType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutletTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, outletTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outletTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutletType in the database
        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutletType in Elasticsearch
        verify(mockOutletTypeSearchRepository, times(0)).save(outletType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOutletType() throws Exception {
        int databaseSizeBeforeUpdate = outletTypeRepository.findAll().size();
        outletType.setId(count.incrementAndGet());

        // Create the OutletType
        OutletTypeDTO outletTypeDTO = outletTypeMapper.toDto(outletType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutletTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outletTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutletType in the database
        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutletType in Elasticsearch
        verify(mockOutletTypeSearchRepository, times(0)).save(outletType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOutletType() throws Exception {
        int databaseSizeBeforeUpdate = outletTypeRepository.findAll().size();
        outletType.setId(count.incrementAndGet());

        // Create the OutletType
        OutletTypeDTO outletTypeDTO = outletTypeMapper.toDto(outletType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutletTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(outletTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OutletType in the database
        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OutletType in Elasticsearch
        verify(mockOutletTypeSearchRepository, times(0)).save(outletType);
    }

    @Test
    @Transactional
    void deleteOutletType() throws Exception {
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);

        int databaseSizeBeforeDelete = outletTypeRepository.findAll().size();

        // Delete the outletType
        restOutletTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, outletType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OutletType> outletTypeList = outletTypeRepository.findAll();
        assertThat(outletTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OutletType in Elasticsearch
        verify(mockOutletTypeSearchRepository, times(1)).deleteById(outletType.getId());
    }

    @Test
    @Transactional
    void searchOutletType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        outletTypeRepository.saveAndFlush(outletType);
        when(mockOutletTypeSearchRepository.search("id:" + outletType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(outletType), PageRequest.of(0, 1), 1));

        // Search the outletType
        restOutletTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + outletType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outletType.getId().intValue())))
            .andExpect(jsonPath("$.[*].outletTypeCode").value(hasItem(DEFAULT_OUTLET_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].outletType").value(hasItem(DEFAULT_OUTLET_TYPE)))
            .andExpect(jsonPath("$.[*].outletTypeDetails").value(hasItem(DEFAULT_OUTLET_TYPE_DETAILS)));
    }
}
