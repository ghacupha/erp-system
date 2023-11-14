package io.github.erp.web.rest;

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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.PartyRelationType;
import io.github.erp.repository.PartyRelationTypeRepository;
import io.github.erp.repository.search.PartyRelationTypeSearchRepository;
import io.github.erp.service.criteria.PartyRelationTypeCriteria;
import io.github.erp.service.dto.PartyRelationTypeDTO;
import io.github.erp.service.mapper.PartyRelationTypeMapper;
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
 * Integration tests for the {@link PartyRelationTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PartyRelationTypeResourceIT {

    private static final String DEFAULT_PARTY_RELATION_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PARTY_RELATION_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PARTY_RELATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PARTY_RELATION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PARTY_RELATION_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PARTY_RELATION_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/party-relation-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/party-relation-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartyRelationTypeRepository partyRelationTypeRepository;

    @Autowired
    private PartyRelationTypeMapper partyRelationTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PartyRelationTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private PartyRelationTypeSearchRepository mockPartyRelationTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartyRelationTypeMockMvc;

    private PartyRelationType partyRelationType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyRelationType createEntity(EntityManager em) {
        PartyRelationType partyRelationType = new PartyRelationType()
            .partyRelationTypeCode(DEFAULT_PARTY_RELATION_TYPE_CODE)
            .partyRelationType(DEFAULT_PARTY_RELATION_TYPE)
            .partyRelationTypeDescription(DEFAULT_PARTY_RELATION_TYPE_DESCRIPTION);
        return partyRelationType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PartyRelationType createUpdatedEntity(EntityManager em) {
        PartyRelationType partyRelationType = new PartyRelationType()
            .partyRelationTypeCode(UPDATED_PARTY_RELATION_TYPE_CODE)
            .partyRelationType(UPDATED_PARTY_RELATION_TYPE)
            .partyRelationTypeDescription(UPDATED_PARTY_RELATION_TYPE_DESCRIPTION);
        return partyRelationType;
    }

    @BeforeEach
    public void initTest() {
        partyRelationType = createEntity(em);
    }

    @Test
    @Transactional
    void createPartyRelationType() throws Exception {
        int databaseSizeBeforeCreate = partyRelationTypeRepository.findAll().size();
        // Create the PartyRelationType
        PartyRelationTypeDTO partyRelationTypeDTO = partyRelationTypeMapper.toDto(partyRelationType);
        restPartyRelationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyRelationTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PartyRelationType in the database
        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PartyRelationType testPartyRelationType = partyRelationTypeList.get(partyRelationTypeList.size() - 1);
        assertThat(testPartyRelationType.getPartyRelationTypeCode()).isEqualTo(DEFAULT_PARTY_RELATION_TYPE_CODE);
        assertThat(testPartyRelationType.getPartyRelationType()).isEqualTo(DEFAULT_PARTY_RELATION_TYPE);
        assertThat(testPartyRelationType.getPartyRelationTypeDescription()).isEqualTo(DEFAULT_PARTY_RELATION_TYPE_DESCRIPTION);

        // Validate the PartyRelationType in Elasticsearch
        verify(mockPartyRelationTypeSearchRepository, times(1)).save(testPartyRelationType);
    }

    @Test
    @Transactional
    void createPartyRelationTypeWithExistingId() throws Exception {
        // Create the PartyRelationType with an existing ID
        partyRelationType.setId(1L);
        PartyRelationTypeDTO partyRelationTypeDTO = partyRelationTypeMapper.toDto(partyRelationType);

        int databaseSizeBeforeCreate = partyRelationTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartyRelationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyRelationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationType in the database
        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the PartyRelationType in Elasticsearch
        verify(mockPartyRelationTypeSearchRepository, times(0)).save(partyRelationType);
    }

    @Test
    @Transactional
    void checkPartyRelationTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRelationTypeRepository.findAll().size();
        // set the field null
        partyRelationType.setPartyRelationTypeCode(null);

        // Create the PartyRelationType, which fails.
        PartyRelationTypeDTO partyRelationTypeDTO = partyRelationTypeMapper.toDto(partyRelationType);

        restPartyRelationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyRelationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPartyRelationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = partyRelationTypeRepository.findAll().size();
        // set the field null
        partyRelationType.setPartyRelationType(null);

        // Create the PartyRelationType, which fails.
        PartyRelationTypeDTO partyRelationTypeDTO = partyRelationTypeMapper.toDto(partyRelationType);

        restPartyRelationTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyRelationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPartyRelationTypes() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList
        restPartyRelationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyRelationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].partyRelationTypeCode").value(hasItem(DEFAULT_PARTY_RELATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].partyRelationType").value(hasItem(DEFAULT_PARTY_RELATION_TYPE)))
            .andExpect(jsonPath("$.[*].partyRelationTypeDescription").value(hasItem(DEFAULT_PARTY_RELATION_TYPE_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getPartyRelationType() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get the partyRelationType
        restPartyRelationTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, partyRelationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partyRelationType.getId().intValue()))
            .andExpect(jsonPath("$.partyRelationTypeCode").value(DEFAULT_PARTY_RELATION_TYPE_CODE))
            .andExpect(jsonPath("$.partyRelationType").value(DEFAULT_PARTY_RELATION_TYPE))
            .andExpect(jsonPath("$.partyRelationTypeDescription").value(DEFAULT_PARTY_RELATION_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getPartyRelationTypesByIdFiltering() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        Long id = partyRelationType.getId();

        defaultPartyRelationTypeShouldBeFound("id.equals=" + id);
        defaultPartyRelationTypeShouldNotBeFound("id.notEquals=" + id);

        defaultPartyRelationTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPartyRelationTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultPartyRelationTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPartyRelationTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPartyRelationTypesByPartyRelationTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList where partyRelationTypeCode equals to DEFAULT_PARTY_RELATION_TYPE_CODE
        defaultPartyRelationTypeShouldBeFound("partyRelationTypeCode.equals=" + DEFAULT_PARTY_RELATION_TYPE_CODE);

        // Get all the partyRelationTypeList where partyRelationTypeCode equals to UPDATED_PARTY_RELATION_TYPE_CODE
        defaultPartyRelationTypeShouldNotBeFound("partyRelationTypeCode.equals=" + UPDATED_PARTY_RELATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllPartyRelationTypesByPartyRelationTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList where partyRelationTypeCode not equals to DEFAULT_PARTY_RELATION_TYPE_CODE
        defaultPartyRelationTypeShouldNotBeFound("partyRelationTypeCode.notEquals=" + DEFAULT_PARTY_RELATION_TYPE_CODE);

        // Get all the partyRelationTypeList where partyRelationTypeCode not equals to UPDATED_PARTY_RELATION_TYPE_CODE
        defaultPartyRelationTypeShouldBeFound("partyRelationTypeCode.notEquals=" + UPDATED_PARTY_RELATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllPartyRelationTypesByPartyRelationTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList where partyRelationTypeCode in DEFAULT_PARTY_RELATION_TYPE_CODE or UPDATED_PARTY_RELATION_TYPE_CODE
        defaultPartyRelationTypeShouldBeFound(
            "partyRelationTypeCode.in=" + DEFAULT_PARTY_RELATION_TYPE_CODE + "," + UPDATED_PARTY_RELATION_TYPE_CODE
        );

        // Get all the partyRelationTypeList where partyRelationTypeCode equals to UPDATED_PARTY_RELATION_TYPE_CODE
        defaultPartyRelationTypeShouldNotBeFound("partyRelationTypeCode.in=" + UPDATED_PARTY_RELATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllPartyRelationTypesByPartyRelationTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList where partyRelationTypeCode is not null
        defaultPartyRelationTypeShouldBeFound("partyRelationTypeCode.specified=true");

        // Get all the partyRelationTypeList where partyRelationTypeCode is null
        defaultPartyRelationTypeShouldNotBeFound("partyRelationTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyRelationTypesByPartyRelationTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList where partyRelationTypeCode contains DEFAULT_PARTY_RELATION_TYPE_CODE
        defaultPartyRelationTypeShouldBeFound("partyRelationTypeCode.contains=" + DEFAULT_PARTY_RELATION_TYPE_CODE);

        // Get all the partyRelationTypeList where partyRelationTypeCode contains UPDATED_PARTY_RELATION_TYPE_CODE
        defaultPartyRelationTypeShouldNotBeFound("partyRelationTypeCode.contains=" + UPDATED_PARTY_RELATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllPartyRelationTypesByPartyRelationTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList where partyRelationTypeCode does not contain DEFAULT_PARTY_RELATION_TYPE_CODE
        defaultPartyRelationTypeShouldNotBeFound("partyRelationTypeCode.doesNotContain=" + DEFAULT_PARTY_RELATION_TYPE_CODE);

        // Get all the partyRelationTypeList where partyRelationTypeCode does not contain UPDATED_PARTY_RELATION_TYPE_CODE
        defaultPartyRelationTypeShouldBeFound("partyRelationTypeCode.doesNotContain=" + UPDATED_PARTY_RELATION_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllPartyRelationTypesByPartyRelationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList where partyRelationType equals to DEFAULT_PARTY_RELATION_TYPE
        defaultPartyRelationTypeShouldBeFound("partyRelationType.equals=" + DEFAULT_PARTY_RELATION_TYPE);

        // Get all the partyRelationTypeList where partyRelationType equals to UPDATED_PARTY_RELATION_TYPE
        defaultPartyRelationTypeShouldNotBeFound("partyRelationType.equals=" + UPDATED_PARTY_RELATION_TYPE);
    }

    @Test
    @Transactional
    void getAllPartyRelationTypesByPartyRelationTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList where partyRelationType not equals to DEFAULT_PARTY_RELATION_TYPE
        defaultPartyRelationTypeShouldNotBeFound("partyRelationType.notEquals=" + DEFAULT_PARTY_RELATION_TYPE);

        // Get all the partyRelationTypeList where partyRelationType not equals to UPDATED_PARTY_RELATION_TYPE
        defaultPartyRelationTypeShouldBeFound("partyRelationType.notEquals=" + UPDATED_PARTY_RELATION_TYPE);
    }

    @Test
    @Transactional
    void getAllPartyRelationTypesByPartyRelationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList where partyRelationType in DEFAULT_PARTY_RELATION_TYPE or UPDATED_PARTY_RELATION_TYPE
        defaultPartyRelationTypeShouldBeFound("partyRelationType.in=" + DEFAULT_PARTY_RELATION_TYPE + "," + UPDATED_PARTY_RELATION_TYPE);

        // Get all the partyRelationTypeList where partyRelationType equals to UPDATED_PARTY_RELATION_TYPE
        defaultPartyRelationTypeShouldNotBeFound("partyRelationType.in=" + UPDATED_PARTY_RELATION_TYPE);
    }

    @Test
    @Transactional
    void getAllPartyRelationTypesByPartyRelationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList where partyRelationType is not null
        defaultPartyRelationTypeShouldBeFound("partyRelationType.specified=true");

        // Get all the partyRelationTypeList where partyRelationType is null
        defaultPartyRelationTypeShouldNotBeFound("partyRelationType.specified=false");
    }

    @Test
    @Transactional
    void getAllPartyRelationTypesByPartyRelationTypeContainsSomething() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList where partyRelationType contains DEFAULT_PARTY_RELATION_TYPE
        defaultPartyRelationTypeShouldBeFound("partyRelationType.contains=" + DEFAULT_PARTY_RELATION_TYPE);

        // Get all the partyRelationTypeList where partyRelationType contains UPDATED_PARTY_RELATION_TYPE
        defaultPartyRelationTypeShouldNotBeFound("partyRelationType.contains=" + UPDATED_PARTY_RELATION_TYPE);
    }

    @Test
    @Transactional
    void getAllPartyRelationTypesByPartyRelationTypeNotContainsSomething() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        // Get all the partyRelationTypeList where partyRelationType does not contain DEFAULT_PARTY_RELATION_TYPE
        defaultPartyRelationTypeShouldNotBeFound("partyRelationType.doesNotContain=" + DEFAULT_PARTY_RELATION_TYPE);

        // Get all the partyRelationTypeList where partyRelationType does not contain UPDATED_PARTY_RELATION_TYPE
        defaultPartyRelationTypeShouldBeFound("partyRelationType.doesNotContain=" + UPDATED_PARTY_RELATION_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPartyRelationTypeShouldBeFound(String filter) throws Exception {
        restPartyRelationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyRelationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].partyRelationTypeCode").value(hasItem(DEFAULT_PARTY_RELATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].partyRelationType").value(hasItem(DEFAULT_PARTY_RELATION_TYPE)))
            .andExpect(jsonPath("$.[*].partyRelationTypeDescription").value(hasItem(DEFAULT_PARTY_RELATION_TYPE_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restPartyRelationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPartyRelationTypeShouldNotBeFound(String filter) throws Exception {
        restPartyRelationTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPartyRelationTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPartyRelationType() throws Exception {
        // Get the partyRelationType
        restPartyRelationTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPartyRelationType() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        int databaseSizeBeforeUpdate = partyRelationTypeRepository.findAll().size();

        // Update the partyRelationType
        PartyRelationType updatedPartyRelationType = partyRelationTypeRepository.findById(partyRelationType.getId()).get();
        // Disconnect from session so that the updates on updatedPartyRelationType are not directly saved in db
        em.detach(updatedPartyRelationType);
        updatedPartyRelationType
            .partyRelationTypeCode(UPDATED_PARTY_RELATION_TYPE_CODE)
            .partyRelationType(UPDATED_PARTY_RELATION_TYPE)
            .partyRelationTypeDescription(UPDATED_PARTY_RELATION_TYPE_DESCRIPTION);
        PartyRelationTypeDTO partyRelationTypeDTO = partyRelationTypeMapper.toDto(updatedPartyRelationType);

        restPartyRelationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyRelationTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyRelationTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the PartyRelationType in the database
        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeUpdate);
        PartyRelationType testPartyRelationType = partyRelationTypeList.get(partyRelationTypeList.size() - 1);
        assertThat(testPartyRelationType.getPartyRelationTypeCode()).isEqualTo(UPDATED_PARTY_RELATION_TYPE_CODE);
        assertThat(testPartyRelationType.getPartyRelationType()).isEqualTo(UPDATED_PARTY_RELATION_TYPE);
        assertThat(testPartyRelationType.getPartyRelationTypeDescription()).isEqualTo(UPDATED_PARTY_RELATION_TYPE_DESCRIPTION);

        // Validate the PartyRelationType in Elasticsearch
        verify(mockPartyRelationTypeSearchRepository).save(testPartyRelationType);
    }

    @Test
    @Transactional
    void putNonExistingPartyRelationType() throws Exception {
        int databaseSizeBeforeUpdate = partyRelationTypeRepository.findAll().size();
        partyRelationType.setId(count.incrementAndGet());

        // Create the PartyRelationType
        PartyRelationTypeDTO partyRelationTypeDTO = partyRelationTypeMapper.toDto(partyRelationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyRelationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partyRelationTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyRelationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationType in the database
        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PartyRelationType in Elasticsearch
        verify(mockPartyRelationTypeSearchRepository, times(0)).save(partyRelationType);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartyRelationType() throws Exception {
        int databaseSizeBeforeUpdate = partyRelationTypeRepository.findAll().size();
        partyRelationType.setId(count.incrementAndGet());

        // Create the PartyRelationType
        PartyRelationTypeDTO partyRelationTypeDTO = partyRelationTypeMapper.toDto(partyRelationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRelationTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partyRelationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationType in the database
        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PartyRelationType in Elasticsearch
        verify(mockPartyRelationTypeSearchRepository, times(0)).save(partyRelationType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartyRelationType() throws Exception {
        int databaseSizeBeforeUpdate = partyRelationTypeRepository.findAll().size();
        partyRelationType.setId(count.incrementAndGet());

        // Create the PartyRelationType
        PartyRelationTypeDTO partyRelationTypeDTO = partyRelationTypeMapper.toDto(partyRelationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRelationTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partyRelationTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyRelationType in the database
        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PartyRelationType in Elasticsearch
        verify(mockPartyRelationTypeSearchRepository, times(0)).save(partyRelationType);
    }

    @Test
    @Transactional
    void partialUpdatePartyRelationTypeWithPatch() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        int databaseSizeBeforeUpdate = partyRelationTypeRepository.findAll().size();

        // Update the partyRelationType using partial update
        PartyRelationType partialUpdatedPartyRelationType = new PartyRelationType();
        partialUpdatedPartyRelationType.setId(partyRelationType.getId());

        partialUpdatedPartyRelationType.partyRelationType(UPDATED_PARTY_RELATION_TYPE);

        restPartyRelationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyRelationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartyRelationType))
            )
            .andExpect(status().isOk());

        // Validate the PartyRelationType in the database
        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeUpdate);
        PartyRelationType testPartyRelationType = partyRelationTypeList.get(partyRelationTypeList.size() - 1);
        assertThat(testPartyRelationType.getPartyRelationTypeCode()).isEqualTo(DEFAULT_PARTY_RELATION_TYPE_CODE);
        assertThat(testPartyRelationType.getPartyRelationType()).isEqualTo(UPDATED_PARTY_RELATION_TYPE);
        assertThat(testPartyRelationType.getPartyRelationTypeDescription()).isEqualTo(DEFAULT_PARTY_RELATION_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePartyRelationTypeWithPatch() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        int databaseSizeBeforeUpdate = partyRelationTypeRepository.findAll().size();

        // Update the partyRelationType using partial update
        PartyRelationType partialUpdatedPartyRelationType = new PartyRelationType();
        partialUpdatedPartyRelationType.setId(partyRelationType.getId());

        partialUpdatedPartyRelationType
            .partyRelationTypeCode(UPDATED_PARTY_RELATION_TYPE_CODE)
            .partyRelationType(UPDATED_PARTY_RELATION_TYPE)
            .partyRelationTypeDescription(UPDATED_PARTY_RELATION_TYPE_DESCRIPTION);

        restPartyRelationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartyRelationType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartyRelationType))
            )
            .andExpect(status().isOk());

        // Validate the PartyRelationType in the database
        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeUpdate);
        PartyRelationType testPartyRelationType = partyRelationTypeList.get(partyRelationTypeList.size() - 1);
        assertThat(testPartyRelationType.getPartyRelationTypeCode()).isEqualTo(UPDATED_PARTY_RELATION_TYPE_CODE);
        assertThat(testPartyRelationType.getPartyRelationType()).isEqualTo(UPDATED_PARTY_RELATION_TYPE);
        assertThat(testPartyRelationType.getPartyRelationTypeDescription()).isEqualTo(UPDATED_PARTY_RELATION_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPartyRelationType() throws Exception {
        int databaseSizeBeforeUpdate = partyRelationTypeRepository.findAll().size();
        partyRelationType.setId(count.incrementAndGet());

        // Create the PartyRelationType
        PartyRelationTypeDTO partyRelationTypeDTO = partyRelationTypeMapper.toDto(partyRelationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartyRelationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partyRelationTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partyRelationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationType in the database
        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PartyRelationType in Elasticsearch
        verify(mockPartyRelationTypeSearchRepository, times(0)).save(partyRelationType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartyRelationType() throws Exception {
        int databaseSizeBeforeUpdate = partyRelationTypeRepository.findAll().size();
        partyRelationType.setId(count.incrementAndGet());

        // Create the PartyRelationType
        PartyRelationTypeDTO partyRelationTypeDTO = partyRelationTypeMapper.toDto(partyRelationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRelationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partyRelationTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PartyRelationType in the database
        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PartyRelationType in Elasticsearch
        verify(mockPartyRelationTypeSearchRepository, times(0)).save(partyRelationType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartyRelationType() throws Exception {
        int databaseSizeBeforeUpdate = partyRelationTypeRepository.findAll().size();
        partyRelationType.setId(count.incrementAndGet());

        // Create the PartyRelationType
        PartyRelationTypeDTO partyRelationTypeDTO = partyRelationTypeMapper.toDto(partyRelationType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartyRelationTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partyRelationTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PartyRelationType in the database
        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PartyRelationType in Elasticsearch
        verify(mockPartyRelationTypeSearchRepository, times(0)).save(partyRelationType);
    }

    @Test
    @Transactional
    void deletePartyRelationType() throws Exception {
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);

        int databaseSizeBeforeDelete = partyRelationTypeRepository.findAll().size();

        // Delete the partyRelationType
        restPartyRelationTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, partyRelationType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PartyRelationType> partyRelationTypeList = partyRelationTypeRepository.findAll();
        assertThat(partyRelationTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PartyRelationType in Elasticsearch
        verify(mockPartyRelationTypeSearchRepository, times(1)).deleteById(partyRelationType.getId());
    }

    @Test
    @Transactional
    void searchPartyRelationType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        partyRelationTypeRepository.saveAndFlush(partyRelationType);
        when(mockPartyRelationTypeSearchRepository.search("id:" + partyRelationType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(partyRelationType), PageRequest.of(0, 1), 1));

        // Search the partyRelationType
        restPartyRelationTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + partyRelationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partyRelationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].partyRelationTypeCode").value(hasItem(DEFAULT_PARTY_RELATION_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].partyRelationType").value(hasItem(DEFAULT_PARTY_RELATION_TYPE)))
            .andExpect(jsonPath("$.[*].partyRelationTypeDescription").value(hasItem(DEFAULT_PARTY_RELATION_TYPE_DESCRIPTION.toString())));
    }
}
