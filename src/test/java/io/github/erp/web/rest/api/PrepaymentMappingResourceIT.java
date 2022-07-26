package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark II No 21 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.PrepaymentMapping;
import io.github.erp.repository.PrepaymentMappingRepository;
import io.github.erp.repository.search.PrepaymentMappingSearchRepository;
import io.github.erp.service.PrepaymentMappingService;
import io.github.erp.service.dto.PrepaymentMappingDTO;
import io.github.erp.service.mapper.PrepaymentMappingMapper;
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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link PrepaymentMappingResourceDev} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PrepaymentMappingResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final UUID DEFAULT_GUID = UUID.randomUUID();
    private static final UUID UPDATED_GUID = UUID.randomUUID();

    private static final String DEFAULT_PARAMETER = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/prepayment-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/prepayment-mappings";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrepaymentMappingRepository prepaymentMappingRepository;

    @Mock
    private PrepaymentMappingRepository prepaymentMappingRepositoryMock;

    @Autowired
    private PrepaymentMappingMapper prepaymentMappingMapper;

    @Mock
    private PrepaymentMappingService prepaymentMappingServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.PrepaymentMappingSearchRepositoryMockConfiguration
     */
    @Autowired
    private PrepaymentMappingSearchRepository mockPrepaymentMappingSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrepaymentMappingMockMvc;

    private PrepaymentMapping prepaymentMapping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentMapping createEntity(EntityManager em) {
        PrepaymentMapping prepaymentMapping = new PrepaymentMapping().key(DEFAULT_KEY).guid(DEFAULT_GUID).parameter(DEFAULT_PARAMETER);
        return prepaymentMapping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrepaymentMapping createUpdatedEntity(EntityManager em) {
        PrepaymentMapping prepaymentMapping = new PrepaymentMapping().key(UPDATED_KEY).guid(UPDATED_GUID).parameter(UPDATED_PARAMETER);
        return prepaymentMapping;
    }

    @BeforeEach
    public void initTest() {
        prepaymentMapping = createEntity(em);
    }

    @Test
    @Transactional
    void createPrepaymentMapping() throws Exception {
        int databaseSizeBeforeCreate = prepaymentMappingRepository.findAll().size();
        // Create the PrepaymentMapping
        PrepaymentMappingDTO prepaymentMappingDTO = prepaymentMappingMapper.toDto(prepaymentMapping);
        restPrepaymentMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMappingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PrepaymentMapping in the database
        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeCreate + 1);
        PrepaymentMapping testPrepaymentMapping = prepaymentMappingList.get(prepaymentMappingList.size() - 1);
        assertThat(testPrepaymentMapping.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testPrepaymentMapping.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testPrepaymentMapping.getParameter()).isEqualTo(DEFAULT_PARAMETER);

        // Validate the PrepaymentMapping in Elasticsearch
        verify(mockPrepaymentMappingSearchRepository, times(1)).save(testPrepaymentMapping);
    }

    @Test
    @Transactional
    void createPrepaymentMappingWithExistingId() throws Exception {
        // Create the PrepaymentMapping with an existing ID
        prepaymentMapping.setId(1L);
        PrepaymentMappingDTO prepaymentMappingDTO = prepaymentMappingMapper.toDto(prepaymentMapping);

        int databaseSizeBeforeCreate = prepaymentMappingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrepaymentMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentMapping in the database
        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeCreate);

        // Validate the PrepaymentMapping in Elasticsearch
        verify(mockPrepaymentMappingSearchRepository, times(0)).save(prepaymentMapping);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentMappingRepository.findAll().size();
        // set the field null
        prepaymentMapping.setKey(null);

        // Create the PrepaymentMapping, which fails.
        PrepaymentMappingDTO prepaymentMappingDTO = prepaymentMappingMapper.toDto(prepaymentMapping);

        restPrepaymentMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMappingDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentMappingRepository.findAll().size();
        // set the field null
        prepaymentMapping.setGuid(null);

        // Create the PrepaymentMapping, which fails.
        PrepaymentMappingDTO prepaymentMappingDTO = prepaymentMappingMapper.toDto(prepaymentMapping);

        restPrepaymentMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMappingDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkParameterIsRequired() throws Exception {
        int databaseSizeBeforeTest = prepaymentMappingRepository.findAll().size();
        // set the field null
        prepaymentMapping.setParameter(null);

        // Create the PrepaymentMapping, which fails.
        PrepaymentMappingDTO prepaymentMappingDTO = prepaymentMappingMapper.toDto(prepaymentMapping);

        restPrepaymentMappingMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMappingDTO))
            )
            .andExpect(status().isBadRequest());

        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappings() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList
        restPrepaymentMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID.toString())))
            .andExpect(jsonPath("$.[*].parameter").value(hasItem(DEFAULT_PARAMETER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrepaymentMappingsWithEagerRelationshipsIsEnabled() throws Exception {
        when(prepaymentMappingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrepaymentMappingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prepaymentMappingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPrepaymentMappingsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(prepaymentMappingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPrepaymentMappingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(prepaymentMappingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPrepaymentMapping() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get the prepaymentMapping
        restPrepaymentMappingMockMvc
            .perform(get(ENTITY_API_URL_ID, prepaymentMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prepaymentMapping.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID.toString()))
            .andExpect(jsonPath("$.parameter").value(DEFAULT_PARAMETER));
    }

    @Test
    @Transactional
    void getPrepaymentMappingsByIdFiltering() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        Long id = prepaymentMapping.getId();

        defaultPrepaymentMappingShouldBeFound("id.equals=" + id);
        defaultPrepaymentMappingShouldNotBeFound("id.notEquals=" + id);

        defaultPrepaymentMappingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrepaymentMappingShouldNotBeFound("id.greaterThan=" + id);

        defaultPrepaymentMappingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrepaymentMappingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where key equals to DEFAULT_KEY
        defaultPrepaymentMappingShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the prepaymentMappingList where key equals to UPDATED_KEY
        defaultPrepaymentMappingShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where key not equals to DEFAULT_KEY
        defaultPrepaymentMappingShouldNotBeFound("key.notEquals=" + DEFAULT_KEY);

        // Get all the prepaymentMappingList where key not equals to UPDATED_KEY
        defaultPrepaymentMappingShouldBeFound("key.notEquals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where key in DEFAULT_KEY or UPDATED_KEY
        defaultPrepaymentMappingShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the prepaymentMappingList where key equals to UPDATED_KEY
        defaultPrepaymentMappingShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where key is not null
        defaultPrepaymentMappingShouldBeFound("key.specified=true");

        // Get all the prepaymentMappingList where key is null
        defaultPrepaymentMappingShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByKeyContainsSomething() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where key contains DEFAULT_KEY
        defaultPrepaymentMappingShouldBeFound("key.contains=" + DEFAULT_KEY);

        // Get all the prepaymentMappingList where key contains UPDATED_KEY
        defaultPrepaymentMappingShouldNotBeFound("key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where key does not contain DEFAULT_KEY
        defaultPrepaymentMappingShouldNotBeFound("key.doesNotContain=" + DEFAULT_KEY);

        // Get all the prepaymentMappingList where key does not contain UPDATED_KEY
        defaultPrepaymentMappingShouldBeFound("key.doesNotContain=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByGuidIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where guid equals to DEFAULT_GUID
        defaultPrepaymentMappingShouldBeFound("guid.equals=" + DEFAULT_GUID);

        // Get all the prepaymentMappingList where guid equals to UPDATED_GUID
        defaultPrepaymentMappingShouldNotBeFound("guid.equals=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByGuidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where guid not equals to DEFAULT_GUID
        defaultPrepaymentMappingShouldNotBeFound("guid.notEquals=" + DEFAULT_GUID);

        // Get all the prepaymentMappingList where guid not equals to UPDATED_GUID
        defaultPrepaymentMappingShouldBeFound("guid.notEquals=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByGuidIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where guid in DEFAULT_GUID or UPDATED_GUID
        defaultPrepaymentMappingShouldBeFound("guid.in=" + DEFAULT_GUID + "," + UPDATED_GUID);

        // Get all the prepaymentMappingList where guid equals to UPDATED_GUID
        defaultPrepaymentMappingShouldNotBeFound("guid.in=" + UPDATED_GUID);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByGuidIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where guid is not null
        defaultPrepaymentMappingShouldBeFound("guid.specified=true");

        // Get all the prepaymentMappingList where guid is null
        defaultPrepaymentMappingShouldNotBeFound("guid.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByParameterIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where parameter equals to DEFAULT_PARAMETER
        defaultPrepaymentMappingShouldBeFound("parameter.equals=" + DEFAULT_PARAMETER);

        // Get all the prepaymentMappingList where parameter equals to UPDATED_PARAMETER
        defaultPrepaymentMappingShouldNotBeFound("parameter.equals=" + UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByParameterIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where parameter not equals to DEFAULT_PARAMETER
        defaultPrepaymentMappingShouldNotBeFound("parameter.notEquals=" + DEFAULT_PARAMETER);

        // Get all the prepaymentMappingList where parameter not equals to UPDATED_PARAMETER
        defaultPrepaymentMappingShouldBeFound("parameter.notEquals=" + UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByParameterIsInShouldWork() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where parameter in DEFAULT_PARAMETER or UPDATED_PARAMETER
        defaultPrepaymentMappingShouldBeFound("parameter.in=" + DEFAULT_PARAMETER + "," + UPDATED_PARAMETER);

        // Get all the prepaymentMappingList where parameter equals to UPDATED_PARAMETER
        defaultPrepaymentMappingShouldNotBeFound("parameter.in=" + UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByParameterIsNullOrNotNull() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where parameter is not null
        defaultPrepaymentMappingShouldBeFound("parameter.specified=true");

        // Get all the prepaymentMappingList where parameter is null
        defaultPrepaymentMappingShouldNotBeFound("parameter.specified=false");
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByParameterContainsSomething() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where parameter contains DEFAULT_PARAMETER
        defaultPrepaymentMappingShouldBeFound("parameter.contains=" + DEFAULT_PARAMETER);

        // Get all the prepaymentMappingList where parameter contains UPDATED_PARAMETER
        defaultPrepaymentMappingShouldNotBeFound("parameter.contains=" + UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByParameterNotContainsSomething() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        // Get all the prepaymentMappingList where parameter does not contain DEFAULT_PARAMETER
        defaultPrepaymentMappingShouldNotBeFound("parameter.doesNotContain=" + DEFAULT_PARAMETER);

        // Get all the prepaymentMappingList where parameter does not contain UPDATED_PARAMETER
        defaultPrepaymentMappingShouldBeFound("parameter.doesNotContain=" + UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void getAllPrepaymentMappingsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);
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
        prepaymentMapping.addPlaceholder(placeholder);
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);
        Long placeholderId = placeholder.getId();

        // Get all the prepaymentMappingList where placeholder equals to placeholderId
        defaultPrepaymentMappingShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the prepaymentMappingList where placeholder equals to (placeholderId + 1)
        defaultPrepaymentMappingShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrepaymentMappingShouldBeFound(String filter) throws Exception {
        restPrepaymentMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID.toString())))
            .andExpect(jsonPath("$.[*].parameter").value(hasItem(DEFAULT_PARAMETER)));

        // Check, that the count call also returns 1
        restPrepaymentMappingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrepaymentMappingShouldNotBeFound(String filter) throws Exception {
        restPrepaymentMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrepaymentMappingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrepaymentMapping() throws Exception {
        // Get the prepaymentMapping
        restPrepaymentMappingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrepaymentMapping() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        int databaseSizeBeforeUpdate = prepaymentMappingRepository.findAll().size();

        // Update the prepaymentMapping
        PrepaymentMapping updatedPrepaymentMapping = prepaymentMappingRepository.findById(prepaymentMapping.getId()).get();
        // Disconnect from session so that the updates on updatedPrepaymentMapping are not directly saved in db
        em.detach(updatedPrepaymentMapping);
        updatedPrepaymentMapping.key(UPDATED_KEY).guid(UPDATED_GUID).parameter(UPDATED_PARAMETER);
        PrepaymentMappingDTO prepaymentMappingDTO = prepaymentMappingMapper.toDto(updatedPrepaymentMapping);

        restPrepaymentMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentMappingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMappingDTO))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentMapping in the database
        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentMapping testPrepaymentMapping = prepaymentMappingList.get(prepaymentMappingList.size() - 1);
        assertThat(testPrepaymentMapping.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testPrepaymentMapping.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testPrepaymentMapping.getParameter()).isEqualTo(UPDATED_PARAMETER);

        // Validate the PrepaymentMapping in Elasticsearch
        verify(mockPrepaymentMappingSearchRepository).save(testPrepaymentMapping);
    }

    @Test
    @Transactional
    void putNonExistingPrepaymentMapping() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentMappingRepository.findAll().size();
        prepaymentMapping.setId(count.incrementAndGet());

        // Create the PrepaymentMapping
        PrepaymentMappingDTO prepaymentMappingDTO = prepaymentMappingMapper.toDto(prepaymentMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prepaymentMappingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentMapping in the database
        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentMapping in Elasticsearch
        verify(mockPrepaymentMappingSearchRepository, times(0)).save(prepaymentMapping);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrepaymentMapping() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentMappingRepository.findAll().size();
        prepaymentMapping.setId(count.incrementAndGet());

        // Create the PrepaymentMapping
        PrepaymentMappingDTO prepaymentMappingDTO = prepaymentMappingMapper.toDto(prepaymentMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentMapping in the database
        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentMapping in Elasticsearch
        verify(mockPrepaymentMappingSearchRepository, times(0)).save(prepaymentMapping);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrepaymentMapping() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentMappingRepository.findAll().size();
        prepaymentMapping.setId(count.incrementAndGet());

        // Create the PrepaymentMapping
        PrepaymentMappingDTO prepaymentMappingDTO = prepaymentMappingMapper.toDto(prepaymentMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentMappingMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prepaymentMappingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentMapping in the database
        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentMapping in Elasticsearch
        verify(mockPrepaymentMappingSearchRepository, times(0)).save(prepaymentMapping);
    }

    @Test
    @Transactional
    void partialUpdatePrepaymentMappingWithPatch() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        int databaseSizeBeforeUpdate = prepaymentMappingRepository.findAll().size();

        // Update the prepaymentMapping using partial update
        PrepaymentMapping partialUpdatedPrepaymentMapping = new PrepaymentMapping();
        partialUpdatedPrepaymentMapping.setId(prepaymentMapping.getId());

        partialUpdatedPrepaymentMapping.key(UPDATED_KEY);

        restPrepaymentMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentMapping))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentMapping in the database
        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentMapping testPrepaymentMapping = prepaymentMappingList.get(prepaymentMappingList.size() - 1);
        assertThat(testPrepaymentMapping.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testPrepaymentMapping.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testPrepaymentMapping.getParameter()).isEqualTo(DEFAULT_PARAMETER);
    }

    @Test
    @Transactional
    void fullUpdatePrepaymentMappingWithPatch() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        int databaseSizeBeforeUpdate = prepaymentMappingRepository.findAll().size();

        // Update the prepaymentMapping using partial update
        PrepaymentMapping partialUpdatedPrepaymentMapping = new PrepaymentMapping();
        partialUpdatedPrepaymentMapping.setId(prepaymentMapping.getId());

        partialUpdatedPrepaymentMapping.key(UPDATED_KEY).guid(UPDATED_GUID).parameter(UPDATED_PARAMETER);

        restPrepaymentMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrepaymentMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrepaymentMapping))
            )
            .andExpect(status().isOk());

        // Validate the PrepaymentMapping in the database
        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeUpdate);
        PrepaymentMapping testPrepaymentMapping = prepaymentMappingList.get(prepaymentMappingList.size() - 1);
        assertThat(testPrepaymentMapping.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testPrepaymentMapping.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testPrepaymentMapping.getParameter()).isEqualTo(UPDATED_PARAMETER);
    }

    @Test
    @Transactional
    void patchNonExistingPrepaymentMapping() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentMappingRepository.findAll().size();
        prepaymentMapping.setId(count.incrementAndGet());

        // Create the PrepaymentMapping
        PrepaymentMappingDTO prepaymentMappingDTO = prepaymentMappingMapper.toDto(prepaymentMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrepaymentMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prepaymentMappingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentMapping in the database
        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentMapping in Elasticsearch
        verify(mockPrepaymentMappingSearchRepository, times(0)).save(prepaymentMapping);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrepaymentMapping() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentMappingRepository.findAll().size();
        prepaymentMapping.setId(count.incrementAndGet());

        // Create the PrepaymentMapping
        PrepaymentMappingDTO prepaymentMappingDTO = prepaymentMappingMapper.toDto(prepaymentMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PrepaymentMapping in the database
        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentMapping in Elasticsearch
        verify(mockPrepaymentMappingSearchRepository, times(0)).save(prepaymentMapping);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrepaymentMapping() throws Exception {
        int databaseSizeBeforeUpdate = prepaymentMappingRepository.findAll().size();
        prepaymentMapping.setId(count.incrementAndGet());

        // Create the PrepaymentMapping
        PrepaymentMappingDTO prepaymentMappingDTO = prepaymentMappingMapper.toDto(prepaymentMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrepaymentMappingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prepaymentMappingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PrepaymentMapping in the database
        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the PrepaymentMapping in Elasticsearch
        verify(mockPrepaymentMappingSearchRepository, times(0)).save(prepaymentMapping);
    }

    @Test
    @Transactional
    void deletePrepaymentMapping() throws Exception {
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);

        int databaseSizeBeforeDelete = prepaymentMappingRepository.findAll().size();

        // Delete the prepaymentMapping
        restPrepaymentMappingMockMvc
            .perform(delete(ENTITY_API_URL_ID, prepaymentMapping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrepaymentMapping> prepaymentMappingList = prepaymentMappingRepository.findAll();
        assertThat(prepaymentMappingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the PrepaymentMapping in Elasticsearch
        verify(mockPrepaymentMappingSearchRepository, times(1)).deleteById(prepaymentMapping.getId());
    }

    @Test
    @Transactional
    void searchPrepaymentMapping() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        prepaymentMappingRepository.saveAndFlush(prepaymentMapping);
        when(mockPrepaymentMappingSearchRepository.search("id:" + prepaymentMapping.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(prepaymentMapping), PageRequest.of(0, 1), 1));

        // Search the prepaymentMapping
        restPrepaymentMappingMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + prepaymentMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prepaymentMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID.toString())))
            .andExpect(jsonPath("$.[*].parameter").value(hasItem(DEFAULT_PARAMETER)));
    }
}
