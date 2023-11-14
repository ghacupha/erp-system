package io.github.erp.erp.resources.gdi;

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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.GenderType;
import io.github.erp.domain.enumeration.genderTypes;
import io.github.erp.repository.GenderTypeRepository;
import io.github.erp.repository.search.GenderTypeSearchRepository;
import io.github.erp.service.dto.GenderTypeDTO;
import io.github.erp.service.mapper.GenderTypeMapper;
import io.github.erp.web.rest.GenderTypeResource;
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
 * Integration tests for the {@link GenderTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class GenderTypeResourceIT {

    private static final String DEFAULT_GENDER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GENDER_CODE = "BBBBBBBBBB";

    private static final genderTypes DEFAULT_GENDER_TYPE = genderTypes.MALE;
    private static final genderTypes UPDATED_GENDER_TYPE = genderTypes.FEMALE;

    private static final String DEFAULT_GENDER_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_GENDER_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/gender-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/gender-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GenderTypeRepository genderTypeRepository;

    @Autowired
    private GenderTypeMapper genderTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.GenderTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private GenderTypeSearchRepository mockGenderTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGenderTypeMockMvc;

    private GenderType genderType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenderType createEntity(EntityManager em) {
        GenderType genderType = new GenderType()
            .genderCode(DEFAULT_GENDER_CODE)
            .genderType(DEFAULT_GENDER_TYPE)
            .genderDescription(DEFAULT_GENDER_DESCRIPTION);
        return genderType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenderType createUpdatedEntity(EntityManager em) {
        GenderType genderType = new GenderType()
            .genderCode(UPDATED_GENDER_CODE)
            .genderType(UPDATED_GENDER_TYPE)
            .genderDescription(UPDATED_GENDER_DESCRIPTION);
        return genderType;
    }

    @BeforeEach
    public void initTest() {
        genderType = createEntity(em);
    }

    @Test
    @Transactional
    void createGenderType() throws Exception {
        int databaseSizeBeforeCreate = genderTypeRepository.findAll().size();
        // Create the GenderType
        GenderTypeDTO genderTypeDTO = genderTypeMapper.toDto(genderType);
        restGenderTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genderTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the GenderType in the database
        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeCreate + 1);
        GenderType testGenderType = genderTypeList.get(genderTypeList.size() - 1);
        assertThat(testGenderType.getGenderCode()).isEqualTo(DEFAULT_GENDER_CODE);
        assertThat(testGenderType.getGenderType()).isEqualTo(DEFAULT_GENDER_TYPE);
        assertThat(testGenderType.getGenderDescription()).isEqualTo(DEFAULT_GENDER_DESCRIPTION);

        // Validate the GenderType in Elasticsearch
        verify(mockGenderTypeSearchRepository, times(1)).save(testGenderType);
    }

    @Test
    @Transactional
    void createGenderTypeWithExistingId() throws Exception {
        // Create the GenderType with an existing ID
        genderType.setId(1L);
        GenderTypeDTO genderTypeDTO = genderTypeMapper.toDto(genderType);

        int databaseSizeBeforeCreate = genderTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenderTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genderTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GenderType in the database
        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the GenderType in Elasticsearch
        verify(mockGenderTypeSearchRepository, times(0)).save(genderType);
    }

    @Test
    @Transactional
    void checkGenderCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = genderTypeRepository.findAll().size();
        // set the field null
        genderType.setGenderCode(null);

        // Create the GenderType, which fails.
        GenderTypeDTO genderTypeDTO = genderTypeMapper.toDto(genderType);

        restGenderTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genderTypeDTO)))
            .andExpect(status().isBadRequest());

        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = genderTypeRepository.findAll().size();
        // set the field null
        genderType.setGenderType(null);

        // Create the GenderType, which fails.
        GenderTypeDTO genderTypeDTO = genderTypeMapper.toDto(genderType);

        restGenderTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genderTypeDTO)))
            .andExpect(status().isBadRequest());

        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGenderTypes() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        // Get all the genderTypeList
        restGenderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].genderCode").value(hasItem(DEFAULT_GENDER_CODE)))
            .andExpect(jsonPath("$.[*].genderType").value(hasItem(DEFAULT_GENDER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].genderDescription").value(hasItem(DEFAULT_GENDER_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getGenderType() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        // Get the genderType
        restGenderTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, genderType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(genderType.getId().intValue()))
            .andExpect(jsonPath("$.genderCode").value(DEFAULT_GENDER_CODE))
            .andExpect(jsonPath("$.genderType").value(DEFAULT_GENDER_TYPE.toString()))
            .andExpect(jsonPath("$.genderDescription").value(DEFAULT_GENDER_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getGenderTypesByIdFiltering() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        Long id = genderType.getId();

        defaultGenderTypeShouldBeFound("id.equals=" + id);
        defaultGenderTypeShouldNotBeFound("id.notEquals=" + id);

        defaultGenderTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGenderTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultGenderTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGenderTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGenderTypesByGenderCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        // Get all the genderTypeList where genderCode equals to DEFAULT_GENDER_CODE
        defaultGenderTypeShouldBeFound("genderCode.equals=" + DEFAULT_GENDER_CODE);

        // Get all the genderTypeList where genderCode equals to UPDATED_GENDER_CODE
        defaultGenderTypeShouldNotBeFound("genderCode.equals=" + UPDATED_GENDER_CODE);
    }

    @Test
    @Transactional
    void getAllGenderTypesByGenderCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        // Get all the genderTypeList where genderCode not equals to DEFAULT_GENDER_CODE
        defaultGenderTypeShouldNotBeFound("genderCode.notEquals=" + DEFAULT_GENDER_CODE);

        // Get all the genderTypeList where genderCode not equals to UPDATED_GENDER_CODE
        defaultGenderTypeShouldBeFound("genderCode.notEquals=" + UPDATED_GENDER_CODE);
    }

    @Test
    @Transactional
    void getAllGenderTypesByGenderCodeIsInShouldWork() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        // Get all the genderTypeList where genderCode in DEFAULT_GENDER_CODE or UPDATED_GENDER_CODE
        defaultGenderTypeShouldBeFound("genderCode.in=" + DEFAULT_GENDER_CODE + "," + UPDATED_GENDER_CODE);

        // Get all the genderTypeList where genderCode equals to UPDATED_GENDER_CODE
        defaultGenderTypeShouldNotBeFound("genderCode.in=" + UPDATED_GENDER_CODE);
    }

    @Test
    @Transactional
    void getAllGenderTypesByGenderCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        // Get all the genderTypeList where genderCode is not null
        defaultGenderTypeShouldBeFound("genderCode.specified=true");

        // Get all the genderTypeList where genderCode is null
        defaultGenderTypeShouldNotBeFound("genderCode.specified=false");
    }

    @Test
    @Transactional
    void getAllGenderTypesByGenderCodeContainsSomething() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        // Get all the genderTypeList where genderCode contains DEFAULT_GENDER_CODE
        defaultGenderTypeShouldBeFound("genderCode.contains=" + DEFAULT_GENDER_CODE);

        // Get all the genderTypeList where genderCode contains UPDATED_GENDER_CODE
        defaultGenderTypeShouldNotBeFound("genderCode.contains=" + UPDATED_GENDER_CODE);
    }

    @Test
    @Transactional
    void getAllGenderTypesByGenderCodeNotContainsSomething() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        // Get all the genderTypeList where genderCode does not contain DEFAULT_GENDER_CODE
        defaultGenderTypeShouldNotBeFound("genderCode.doesNotContain=" + DEFAULT_GENDER_CODE);

        // Get all the genderTypeList where genderCode does not contain UPDATED_GENDER_CODE
        defaultGenderTypeShouldBeFound("genderCode.doesNotContain=" + UPDATED_GENDER_CODE);
    }

    @Test
    @Transactional
    void getAllGenderTypesByGenderTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        // Get all the genderTypeList where genderType equals to DEFAULT_GENDER_TYPE
        defaultGenderTypeShouldBeFound("genderType.equals=" + DEFAULT_GENDER_TYPE);

        // Get all the genderTypeList where genderType equals to UPDATED_GENDER_TYPE
        defaultGenderTypeShouldNotBeFound("genderType.equals=" + UPDATED_GENDER_TYPE);
    }

    @Test
    @Transactional
    void getAllGenderTypesByGenderTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        // Get all the genderTypeList where genderType not equals to DEFAULT_GENDER_TYPE
        defaultGenderTypeShouldNotBeFound("genderType.notEquals=" + DEFAULT_GENDER_TYPE);

        // Get all the genderTypeList where genderType not equals to UPDATED_GENDER_TYPE
        defaultGenderTypeShouldBeFound("genderType.notEquals=" + UPDATED_GENDER_TYPE);
    }

    @Test
    @Transactional
    void getAllGenderTypesByGenderTypeIsInShouldWork() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        // Get all the genderTypeList where genderType in DEFAULT_GENDER_TYPE or UPDATED_GENDER_TYPE
        defaultGenderTypeShouldBeFound("genderType.in=" + DEFAULT_GENDER_TYPE + "," + UPDATED_GENDER_TYPE);

        // Get all the genderTypeList where genderType equals to UPDATED_GENDER_TYPE
        defaultGenderTypeShouldNotBeFound("genderType.in=" + UPDATED_GENDER_TYPE);
    }

    @Test
    @Transactional
    void getAllGenderTypesByGenderTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        // Get all the genderTypeList where genderType is not null
        defaultGenderTypeShouldBeFound("genderType.specified=true");

        // Get all the genderTypeList where genderType is null
        defaultGenderTypeShouldNotBeFound("genderType.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGenderTypeShouldBeFound(String filter) throws Exception {
        restGenderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].genderCode").value(hasItem(DEFAULT_GENDER_CODE)))
            .andExpect(jsonPath("$.[*].genderType").value(hasItem(DEFAULT_GENDER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].genderDescription").value(hasItem(DEFAULT_GENDER_DESCRIPTION.toString())));

        // Check, that the count call also returns 1
        restGenderTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGenderTypeShouldNotBeFound(String filter) throws Exception {
        restGenderTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGenderTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGenderType() throws Exception {
        // Get the genderType
        restGenderTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGenderType() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        int databaseSizeBeforeUpdate = genderTypeRepository.findAll().size();

        // Update the genderType
        GenderType updatedGenderType = genderTypeRepository.findById(genderType.getId()).get();
        // Disconnect from session so that the updates on updatedGenderType are not directly saved in db
        em.detach(updatedGenderType);
        updatedGenderType.genderCode(UPDATED_GENDER_CODE).genderType(UPDATED_GENDER_TYPE).genderDescription(UPDATED_GENDER_DESCRIPTION);
        GenderTypeDTO genderTypeDTO = genderTypeMapper.toDto(updatedGenderType);

        restGenderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genderTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genderTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the GenderType in the database
        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeUpdate);
        GenderType testGenderType = genderTypeList.get(genderTypeList.size() - 1);
        assertThat(testGenderType.getGenderCode()).isEqualTo(UPDATED_GENDER_CODE);
        assertThat(testGenderType.getGenderType()).isEqualTo(UPDATED_GENDER_TYPE);
        assertThat(testGenderType.getGenderDescription()).isEqualTo(UPDATED_GENDER_DESCRIPTION);

        // Validate the GenderType in Elasticsearch
        verify(mockGenderTypeSearchRepository).save(testGenderType);
    }

    @Test
    @Transactional
    void putNonExistingGenderType() throws Exception {
        int databaseSizeBeforeUpdate = genderTypeRepository.findAll().size();
        genderType.setId(count.incrementAndGet());

        // Create the GenderType
        GenderTypeDTO genderTypeDTO = genderTypeMapper.toDto(genderType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genderTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenderType in the database
        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GenderType in Elasticsearch
        verify(mockGenderTypeSearchRepository, times(0)).save(genderType);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenderType() throws Exception {
        int databaseSizeBeforeUpdate = genderTypeRepository.findAll().size();
        genderType.setId(count.incrementAndGet());

        // Create the GenderType
        GenderTypeDTO genderTypeDTO = genderTypeMapper.toDto(genderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenderTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenderType in the database
        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GenderType in Elasticsearch
        verify(mockGenderTypeSearchRepository, times(0)).save(genderType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenderType() throws Exception {
        int databaseSizeBeforeUpdate = genderTypeRepository.findAll().size();
        genderType.setId(count.incrementAndGet());

        // Create the GenderType
        GenderTypeDTO genderTypeDTO = genderTypeMapper.toDto(genderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenderTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genderTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GenderType in the database
        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GenderType in Elasticsearch
        verify(mockGenderTypeSearchRepository, times(0)).save(genderType);
    }

    @Test
    @Transactional
    void partialUpdateGenderTypeWithPatch() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        int databaseSizeBeforeUpdate = genderTypeRepository.findAll().size();

        // Update the genderType using partial update
        GenderType partialUpdatedGenderType = new GenderType();
        partialUpdatedGenderType.setId(genderType.getId());

        partialUpdatedGenderType.genderType(UPDATED_GENDER_TYPE).genderDescription(UPDATED_GENDER_DESCRIPTION);

        restGenderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenderType))
            )
            .andExpect(status().isOk());

        // Validate the GenderType in the database
        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeUpdate);
        GenderType testGenderType = genderTypeList.get(genderTypeList.size() - 1);
        assertThat(testGenderType.getGenderCode()).isEqualTo(DEFAULT_GENDER_CODE);
        assertThat(testGenderType.getGenderType()).isEqualTo(UPDATED_GENDER_TYPE);
        assertThat(testGenderType.getGenderDescription()).isEqualTo(UPDATED_GENDER_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateGenderTypeWithPatch() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        int databaseSizeBeforeUpdate = genderTypeRepository.findAll().size();

        // Update the genderType using partial update
        GenderType partialUpdatedGenderType = new GenderType();
        partialUpdatedGenderType.setId(genderType.getId());

        partialUpdatedGenderType
            .genderCode(UPDATED_GENDER_CODE)
            .genderType(UPDATED_GENDER_TYPE)
            .genderDescription(UPDATED_GENDER_DESCRIPTION);

        restGenderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenderType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenderType))
            )
            .andExpect(status().isOk());

        // Validate the GenderType in the database
        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeUpdate);
        GenderType testGenderType = genderTypeList.get(genderTypeList.size() - 1);
        assertThat(testGenderType.getGenderCode()).isEqualTo(UPDATED_GENDER_CODE);
        assertThat(testGenderType.getGenderType()).isEqualTo(UPDATED_GENDER_TYPE);
        assertThat(testGenderType.getGenderDescription()).isEqualTo(UPDATED_GENDER_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingGenderType() throws Exception {
        int databaseSizeBeforeUpdate = genderTypeRepository.findAll().size();
        genderType.setId(count.incrementAndGet());

        // Create the GenderType
        GenderTypeDTO genderTypeDTO = genderTypeMapper.toDto(genderType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, genderTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenderType in the database
        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GenderType in Elasticsearch
        verify(mockGenderTypeSearchRepository, times(0)).save(genderType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenderType() throws Exception {
        int databaseSizeBeforeUpdate = genderTypeRepository.findAll().size();
        genderType.setId(count.incrementAndGet());

        // Create the GenderType
        GenderTypeDTO genderTypeDTO = genderTypeMapper.toDto(genderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genderTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenderType in the database
        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GenderType in Elasticsearch
        verify(mockGenderTypeSearchRepository, times(0)).save(genderType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenderType() throws Exception {
        int databaseSizeBeforeUpdate = genderTypeRepository.findAll().size();
        genderType.setId(count.incrementAndGet());

        // Create the GenderType
        GenderTypeDTO genderTypeDTO = genderTypeMapper.toDto(genderType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenderTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(genderTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GenderType in the database
        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GenderType in Elasticsearch
        verify(mockGenderTypeSearchRepository, times(0)).save(genderType);
    }

    @Test
    @Transactional
    void deleteGenderType() throws Exception {
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);

        int databaseSizeBeforeDelete = genderTypeRepository.findAll().size();

        // Delete the genderType
        restGenderTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, genderType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GenderType> genderTypeList = genderTypeRepository.findAll();
        assertThat(genderTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GenderType in Elasticsearch
        verify(mockGenderTypeSearchRepository, times(1)).deleteById(genderType.getId());
    }

    @Test
    @Transactional
    void searchGenderType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        genderTypeRepository.saveAndFlush(genderType);
        when(mockGenderTypeSearchRepository.search("id:" + genderType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(genderType), PageRequest.of(0, 1), 1));

        // Search the genderType
        restGenderTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + genderType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genderType.getId().intValue())))
            .andExpect(jsonPath("$.[*].genderCode").value(hasItem(DEFAULT_GENDER_CODE)))
            .andExpect(jsonPath("$.[*].genderType").value(hasItem(DEFAULT_GENDER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].genderDescription").value(hasItem(DEFAULT_GENDER_DESCRIPTION.toString())));
    }
}
