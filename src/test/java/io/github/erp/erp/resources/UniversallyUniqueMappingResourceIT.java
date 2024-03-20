package io.github.erp.erp.resources;

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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.UniversallyUniqueMappingRepository;
import io.github.erp.repository.search.UniversallyUniqueMappingSearchRepository;
import io.github.erp.service.UniversallyUniqueMappingService;
import io.github.erp.service.dto.UniversallyUniqueMappingDTO;
import io.github.erp.service.mapper.UniversallyUniqueMappingMapper;
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
 * Integration tests for the {@link UniversallyUniqueMappingResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class UniversallyUniqueMappingResourceIT {

    private static final String DEFAULT_UNIVERSAL_KEY = "AAAAAAAAAA";
    private static final String UPDATED_UNIVERSAL_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_MAPPED_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_MAPPED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/universally-unique-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/universally-unique-mappings";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UniversallyUniqueMappingRepository universallyUniqueMappingRepository;

    @Mock
    private UniversallyUniqueMappingRepository universallyUniqueMappingRepositoryMock;

    @Autowired
    private UniversallyUniqueMappingMapper universallyUniqueMappingMapper;

    @Mock
    private UniversallyUniqueMappingService universallyUniqueMappingServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.UniversallyUniqueMappingSearchRepositoryMockConfiguration
     */
    @Autowired
    private UniversallyUniqueMappingSearchRepository mockUniversallyUniqueMappingSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUniversallyUniqueMappingMockMvc;

    private UniversallyUniqueMapping universallyUniqueMapping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UniversallyUniqueMapping createEntity(EntityManager em) {
        UniversallyUniqueMapping universallyUniqueMapping = new UniversallyUniqueMapping()
            .universalKey(DEFAULT_UNIVERSAL_KEY)
            .mappedValue(DEFAULT_MAPPED_VALUE);
        return universallyUniqueMapping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UniversallyUniqueMapping createUpdatedEntity(EntityManager em) {
        UniversallyUniqueMapping universallyUniqueMapping = new UniversallyUniqueMapping()
            .universalKey(UPDATED_UNIVERSAL_KEY)
            .mappedValue(UPDATED_MAPPED_VALUE);
        return universallyUniqueMapping;
    }

    @BeforeEach
    public void initTest() {
        universallyUniqueMapping = createEntity(em);
    }

    @Test
    @Transactional
    void createUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeCreate = universallyUniqueMappingRepository.findAll().size();
        // Create the UniversallyUniqueMapping
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO = universallyUniqueMappingMapper.toDto(universallyUniqueMapping);
        restUniversallyUniqueMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMappingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeCreate + 1);
        UniversallyUniqueMapping testUniversallyUniqueMapping = universallyUniqueMappingList.get(universallyUniqueMappingList.size() - 1);
        assertThat(testUniversallyUniqueMapping.getUniversalKey()).isEqualTo(DEFAULT_UNIVERSAL_KEY);
        assertThat(testUniversallyUniqueMapping.getMappedValue()).isEqualTo(DEFAULT_MAPPED_VALUE);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(1)).save(testUniversallyUniqueMapping);
    }

    @Test
    @Transactional
    void createUniversallyUniqueMappingWithExistingId() throws Exception {
        // Create the UniversallyUniqueMapping with an existing ID
        universallyUniqueMapping.setId(1L);
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO = universallyUniqueMappingMapper.toDto(universallyUniqueMapping);

        int databaseSizeBeforeCreate = universallyUniqueMappingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUniversallyUniqueMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeCreate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void checkUniversalKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = universallyUniqueMappingRepository.findAll().size();
        // set the field null
        universallyUniqueMapping.setUniversalKey(null);

        // Create the UniversallyUniqueMapping, which fails.
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO = universallyUniqueMappingMapper.toDto(universallyUniqueMapping);

        restUniversallyUniqueMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMappingDTO))
            )
            .andExpect(status().isBadRequest());

        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappings() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList
        restUniversallyUniqueMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(universallyUniqueMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].universalKey").value(hasItem(DEFAULT_UNIVERSAL_KEY)))
            .andExpect(jsonPath("$.[*].mappedValue").value(hasItem(DEFAULT_MAPPED_VALUE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUniversallyUniqueMappingsWithEagerRelationshipsIsEnabled() throws Exception {
        when(universallyUniqueMappingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUniversallyUniqueMappingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(universallyUniqueMappingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUniversallyUniqueMappingsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(universallyUniqueMappingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUniversallyUniqueMappingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(universallyUniqueMappingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUniversallyUniqueMapping() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get the universallyUniqueMapping
        restUniversallyUniqueMappingMockMvc
            .perform(get(ENTITY_API_URL_ID, universallyUniqueMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(universallyUniqueMapping.getId().intValue()))
            .andExpect(jsonPath("$.universalKey").value(DEFAULT_UNIVERSAL_KEY))
            .andExpect(jsonPath("$.mappedValue").value(DEFAULT_MAPPED_VALUE));
    }

    @Test
    @Transactional
    void getUniversallyUniqueMappingsByIdFiltering() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        Long id = universallyUniqueMapping.getId();

        // todo defaultUniversallyUniqueMappingShouldBeFound("id.equals=" + id);
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("id.notEquals=" + id);

        // todo defaultUniversallyUniqueMappingShouldBeFound("id.greaterThanOrEqual=" + id);
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("id.greaterThan=" + id);

        // todo defaultUniversallyUniqueMappingShouldBeFound("id.lessThanOrEqual=" + id);
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByUniversalKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList where universalKey equals to DEFAULT_UNIVERSAL_KEY
        // todo defaultUniversallyUniqueMappingShouldBeFound("universalKey.equals=" + DEFAULT_UNIVERSAL_KEY);

        // Get all the universallyUniqueMappingList where universalKey equals to UPDATED_UNIVERSAL_KEY
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("universalKey.equals=" + UPDATED_UNIVERSAL_KEY);
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByUniversalKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList where universalKey not equals to DEFAULT_UNIVERSAL_KEY
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("universalKey.notEquals=" + DEFAULT_UNIVERSAL_KEY);

        // Get all the universallyUniqueMappingList where universalKey not equals to UPDATED_UNIVERSAL_KEY
        // todo defaultUniversallyUniqueMappingShouldBeFound("universalKey.notEquals=" + UPDATED_UNIVERSAL_KEY);
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByUniversalKeyIsInShouldWork() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList where universalKey in DEFAULT_UNIVERSAL_KEY or UPDATED_UNIVERSAL_KEY
        // todo defaultUniversallyUniqueMappingShouldBeFound("universalKey.in=" + DEFAULT_UNIVERSAL_KEY + "," + UPDATED_UNIVERSAL_KEY);

        // Get all the universallyUniqueMappingList where universalKey equals to UPDATED_UNIVERSAL_KEY
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("universalKey.in=" + UPDATED_UNIVERSAL_KEY);
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByUniversalKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList where universalKey is not null
        // todo defaultUniversallyUniqueMappingShouldBeFound("universalKey.specified=true");

        // Get all the universallyUniqueMappingList where universalKey is null
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("universalKey.specified=false");
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByUniversalKeyContainsSomething() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList where universalKey contains DEFAULT_UNIVERSAL_KEY
        // todo defaultUniversallyUniqueMappingShouldBeFound("universalKey.contains=" + DEFAULT_UNIVERSAL_KEY);

        // Get all the universallyUniqueMappingList where universalKey contains UPDATED_UNIVERSAL_KEY
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("universalKey.contains=" + UPDATED_UNIVERSAL_KEY);
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByUniversalKeyNotContainsSomething() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList where universalKey does not contain DEFAULT_UNIVERSAL_KEY
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("universalKey.doesNotContain=" + DEFAULT_UNIVERSAL_KEY);

        // Get all the universallyUniqueMappingList where universalKey does not contain UPDATED_UNIVERSAL_KEY
        // todo defaultUniversallyUniqueMappingShouldBeFound("universalKey.doesNotContain=" + UPDATED_UNIVERSAL_KEY);
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByMappedValueIsEqualToSomething() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList where mappedValue equals to DEFAULT_MAPPED_VALUE
        // todo defaultUniversallyUniqueMappingShouldBeFound("mappedValue.equals=" + DEFAULT_MAPPED_VALUE);

        // Get all the universallyUniqueMappingList where mappedValue equals to UPDATED_MAPPED_VALUE
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("mappedValue.equals=" + UPDATED_MAPPED_VALUE);
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByMappedValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList where mappedValue not equals to DEFAULT_MAPPED_VALUE
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("mappedValue.notEquals=" + DEFAULT_MAPPED_VALUE);

        // Get all the universallyUniqueMappingList where mappedValue not equals to UPDATED_MAPPED_VALUE
        // todo defaultUniversallyUniqueMappingShouldBeFound("mappedValue.notEquals=" + UPDATED_MAPPED_VALUE);
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByMappedValueIsInShouldWork() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList where mappedValue in DEFAULT_MAPPED_VALUE or UPDATED_MAPPED_VALUE
        // todo defaultUniversallyUniqueMappingShouldBeFound("mappedValue.in=" + DEFAULT_MAPPED_VALUE + "," + UPDATED_MAPPED_VALUE);

        // Get all the universallyUniqueMappingList where mappedValue equals to UPDATED_MAPPED_VALUE
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("mappedValue.in=" + UPDATED_MAPPED_VALUE);
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByMappedValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList where mappedValue is not null
        // todo defaultUniversallyUniqueMappingShouldBeFound("mappedValue.specified=true");

        // Get all the universallyUniqueMappingList where mappedValue is null
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("mappedValue.specified=false");
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByMappedValueContainsSomething() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList where mappedValue contains DEFAULT_MAPPED_VALUE
        // todo defaultUniversallyUniqueMappingShouldBeFound("mappedValue.contains=" + DEFAULT_MAPPED_VALUE);

        // Get all the universallyUniqueMappingList where mappedValue contains UPDATED_MAPPED_VALUE
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("mappedValue.contains=" + UPDATED_MAPPED_VALUE);
    }

    // todo @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByMappedValueNotContainsSomething() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        // Get all the universallyUniqueMappingList where mappedValue does not contain DEFAULT_MAPPED_VALUE
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("mappedValue.doesNotContain=" + DEFAULT_MAPPED_VALUE);

        // Get all the universallyUniqueMappingList where mappedValue does not contain UPDATED_MAPPED_VALUE
        // todo defaultUniversallyUniqueMappingShouldBeFound("mappedValue.doesNotContain=" + UPDATED_MAPPED_VALUE);
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByParentMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);
        UniversallyUniqueMapping parentMapping;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            parentMapping = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(parentMapping);
            em.flush();
        } else {
            parentMapping = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(parentMapping);
        em.flush();
        universallyUniqueMapping.setParentMapping(parentMapping);
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);
        Long parentMappingId = parentMapping.getId();

        // Get all the universallyUniqueMappingList where parentMapping equals to parentMappingId
        // todo defaultUniversallyUniqueMappingShouldBeFound("parentMappingId.equals=" + parentMappingId);

        // Get all the universallyUniqueMappingList where parentMapping equals to (parentMappingId + 1)
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("parentMappingId.equals=" + (parentMappingId + 1));
    }

    @Test
    @Transactional
    void getAllUniversallyUniqueMappingsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);
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
        universallyUniqueMapping.addPlaceholder(placeholder);
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);
        Long placeholderId = placeholder.getId();

        // Get all the universallyUniqueMappingList where placeholder equals to placeholderId
        // todo defaultUniversallyUniqueMappingShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the universallyUniqueMappingList where placeholder equals to (placeholderId + 1)
        // todo defaultUniversallyUniqueMappingShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUniversallyUniqueMappingShouldBeFound(String filter) throws Exception {
        restUniversallyUniqueMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(universallyUniqueMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].universalKey").value(hasItem(DEFAULT_UNIVERSAL_KEY)))
            .andExpect(jsonPath("$.[*].mappedValue").value(hasItem(DEFAULT_MAPPED_VALUE)));

        // Check, that the count call also returns 1
        restUniversallyUniqueMappingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUniversallyUniqueMappingShouldNotBeFound(String filter) throws Exception {
        restUniversallyUniqueMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUniversallyUniqueMappingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUniversallyUniqueMapping() throws Exception {
        // Get the universallyUniqueMapping
        restUniversallyUniqueMappingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUniversallyUniqueMapping() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();

        // Update the universallyUniqueMapping
        UniversallyUniqueMapping updatedUniversallyUniqueMapping = universallyUniqueMappingRepository
            .findById(universallyUniqueMapping.getId())
            .get();
        // Disconnect from session so that the updates on updatedUniversallyUniqueMapping are not directly saved in db
        em.detach(updatedUniversallyUniqueMapping);
        updatedUniversallyUniqueMapping.universalKey(UPDATED_UNIVERSAL_KEY).mappedValue(UPDATED_MAPPED_VALUE);
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO = universallyUniqueMappingMapper.toDto(updatedUniversallyUniqueMapping);

        restUniversallyUniqueMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, universallyUniqueMappingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMappingDTO))
            )
            .andExpect(status().isOk());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);
        UniversallyUniqueMapping testUniversallyUniqueMapping = universallyUniqueMappingList.get(universallyUniqueMappingList.size() - 1);
        assertThat(testUniversallyUniqueMapping.getUniversalKey()).isEqualTo(UPDATED_UNIVERSAL_KEY);
        assertThat(testUniversallyUniqueMapping.getMappedValue()).isEqualTo(UPDATED_MAPPED_VALUE);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository).save(testUniversallyUniqueMapping);
    }

    @Test
    @Transactional
    void putNonExistingUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();
        universallyUniqueMapping.setId(count.incrementAndGet());

        // Create the UniversallyUniqueMapping
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO = universallyUniqueMappingMapper.toDto(universallyUniqueMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniversallyUniqueMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, universallyUniqueMappingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void putWithIdMismatchUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();
        universallyUniqueMapping.setId(count.incrementAndGet());

        // Create the UniversallyUniqueMapping
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO = universallyUniqueMappingMapper.toDto(universallyUniqueMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversallyUniqueMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();
        universallyUniqueMapping.setId(count.incrementAndGet());

        // Create the UniversallyUniqueMapping
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO = universallyUniqueMappingMapper.toDto(universallyUniqueMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversallyUniqueMappingMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMappingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void partialUpdateUniversallyUniqueMappingWithPatch() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();

        // Update the universallyUniqueMapping using partial update
        UniversallyUniqueMapping partialUpdatedUniversallyUniqueMapping = new UniversallyUniqueMapping();
        partialUpdatedUniversallyUniqueMapping.setId(universallyUniqueMapping.getId());

        restUniversallyUniqueMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUniversallyUniqueMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUniversallyUniqueMapping))
            )
            .andExpect(status().isOk());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);
        UniversallyUniqueMapping testUniversallyUniqueMapping = universallyUniqueMappingList.get(universallyUniqueMappingList.size() - 1);
        assertThat(testUniversallyUniqueMapping.getUniversalKey()).isEqualTo(DEFAULT_UNIVERSAL_KEY);
        assertThat(testUniversallyUniqueMapping.getMappedValue()).isEqualTo(DEFAULT_MAPPED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateUniversallyUniqueMappingWithPatch() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();

        // Update the universallyUniqueMapping using partial update
        UniversallyUniqueMapping partialUpdatedUniversallyUniqueMapping = new UniversallyUniqueMapping();
        partialUpdatedUniversallyUniqueMapping.setId(universallyUniqueMapping.getId());

        partialUpdatedUniversallyUniqueMapping.universalKey(UPDATED_UNIVERSAL_KEY).mappedValue(UPDATED_MAPPED_VALUE);

        restUniversallyUniqueMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUniversallyUniqueMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUniversallyUniqueMapping))
            )
            .andExpect(status().isOk());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);
        UniversallyUniqueMapping testUniversallyUniqueMapping = universallyUniqueMappingList.get(universallyUniqueMappingList.size() - 1);
        assertThat(testUniversallyUniqueMapping.getUniversalKey()).isEqualTo(UPDATED_UNIVERSAL_KEY);
        assertThat(testUniversallyUniqueMapping.getMappedValue()).isEqualTo(UPDATED_MAPPED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();
        universallyUniqueMapping.setId(count.incrementAndGet());

        // Create the UniversallyUniqueMapping
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO = universallyUniqueMappingMapper.toDto(universallyUniqueMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniversallyUniqueMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, universallyUniqueMappingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();
        universallyUniqueMapping.setId(count.incrementAndGet());

        // Create the UniversallyUniqueMapping
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO = universallyUniqueMappingMapper.toDto(universallyUniqueMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversallyUniqueMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUniversallyUniqueMapping() throws Exception {
        int databaseSizeBeforeUpdate = universallyUniqueMappingRepository.findAll().size();
        universallyUniqueMapping.setId(count.incrementAndGet());

        // Create the UniversallyUniqueMapping
        UniversallyUniqueMappingDTO universallyUniqueMappingDTO = universallyUniqueMappingMapper.toDto(universallyUniqueMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniversallyUniqueMappingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(universallyUniqueMappingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UniversallyUniqueMapping in the database
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(0)).save(universallyUniqueMapping);
    }

    @Test
    @Transactional
    void deleteUniversallyUniqueMapping() throws Exception {
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);

        int databaseSizeBeforeDelete = universallyUniqueMappingRepository.findAll().size();

        // Delete the universallyUniqueMapping
        restUniversallyUniqueMappingMockMvc
            .perform(delete(ENTITY_API_URL_ID, universallyUniqueMapping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UniversallyUniqueMapping> universallyUniqueMappingList = universallyUniqueMappingRepository.findAll();
        assertThat(universallyUniqueMappingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the UniversallyUniqueMapping in Elasticsearch
        verify(mockUniversallyUniqueMappingSearchRepository, times(1)).deleteById(universallyUniqueMapping.getId());
    }

    @Test
    @Transactional
    void searchUniversallyUniqueMapping() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        universallyUniqueMappingRepository.saveAndFlush(universallyUniqueMapping);
        when(mockUniversallyUniqueMappingSearchRepository.search("id:" + universallyUniqueMapping.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(universallyUniqueMapping), PageRequest.of(0, 1), 1));

        // Search the universallyUniqueMapping
        restUniversallyUniqueMappingMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + universallyUniqueMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(universallyUniqueMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].universalKey").value(hasItem(DEFAULT_UNIVERSAL_KEY)))
            .andExpect(jsonPath("$.[*].mappedValue").value(hasItem(DEFAULT_MAPPED_VALUE)));
    }
}
