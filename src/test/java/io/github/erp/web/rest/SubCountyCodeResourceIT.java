package io.github.erp.web.rest;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.SubCountyCode;
import io.github.erp.repository.SubCountyCodeRepository;
import io.github.erp.repository.search.SubCountyCodeSearchRepository;
import io.github.erp.service.SubCountyCodeService;
import io.github.erp.service.criteria.SubCountyCodeCriteria;
import io.github.erp.service.dto.SubCountyCodeDTO;
import io.github.erp.service.mapper.SubCountyCodeMapper;
import java.util.ArrayList;
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

/**
 * Integration tests for the {@link SubCountyCodeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SubCountyCodeResourceIT {

    private static final String DEFAULT_COUNTY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_COUNTY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_COUNTY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_COUNTY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUB_COUNTY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sub-county-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/sub-county-codes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubCountyCodeRepository subCountyCodeRepository;

    @Mock
    private SubCountyCodeRepository subCountyCodeRepositoryMock;

    @Autowired
    private SubCountyCodeMapper subCountyCodeMapper;

    @Mock
    private SubCountyCodeService subCountyCodeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.SubCountyCodeSearchRepositoryMockConfiguration
     */
    @Autowired
    private SubCountyCodeSearchRepository mockSubCountyCodeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubCountyCodeMockMvc;

    private SubCountyCode subCountyCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCountyCode createEntity(EntityManager em) {
        SubCountyCode subCountyCode = new SubCountyCode()
            .countyCode(DEFAULT_COUNTY_CODE)
            .countyName(DEFAULT_COUNTY_NAME)
            .subCountyCode(DEFAULT_SUB_COUNTY_CODE)
            .subCountyName(DEFAULT_SUB_COUNTY_NAME);
        return subCountyCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCountyCode createUpdatedEntity(EntityManager em) {
        SubCountyCode subCountyCode = new SubCountyCode()
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME)
            .subCountyCode(UPDATED_SUB_COUNTY_CODE)
            .subCountyName(UPDATED_SUB_COUNTY_NAME);
        return subCountyCode;
    }

    @BeforeEach
    public void initTest() {
        subCountyCode = createEntity(em);
    }

    @Test
    @Transactional
    void createSubCountyCode() throws Exception {
        int databaseSizeBeforeCreate = subCountyCodeRepository.findAll().size();
        // Create the SubCountyCode
        SubCountyCodeDTO subCountyCodeDTO = subCountyCodeMapper.toDto(subCountyCode);
        restSubCountyCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCountyCodeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SubCountyCode in the database
        List<SubCountyCode> subCountyCodeList = subCountyCodeRepository.findAll();
        assertThat(subCountyCodeList).hasSize(databaseSizeBeforeCreate + 1);
        SubCountyCode testSubCountyCode = subCountyCodeList.get(subCountyCodeList.size() - 1);
        assertThat(testSubCountyCode.getCountyCode()).isEqualTo(DEFAULT_COUNTY_CODE);
        assertThat(testSubCountyCode.getCountyName()).isEqualTo(DEFAULT_COUNTY_NAME);
        assertThat(testSubCountyCode.getSubCountyCode()).isEqualTo(DEFAULT_SUB_COUNTY_CODE);
        assertThat(testSubCountyCode.getSubCountyName()).isEqualTo(DEFAULT_SUB_COUNTY_NAME);

        // Validate the SubCountyCode in Elasticsearch
        verify(mockSubCountyCodeSearchRepository, times(1)).save(testSubCountyCode);
    }

    @Test
    @Transactional
    void createSubCountyCodeWithExistingId() throws Exception {
        // Create the SubCountyCode with an existing ID
        subCountyCode.setId(1L);
        SubCountyCodeDTO subCountyCodeDTO = subCountyCodeMapper.toDto(subCountyCode);

        int databaseSizeBeforeCreate = subCountyCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubCountyCodeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCountyCode in the database
        List<SubCountyCode> subCountyCodeList = subCountyCodeRepository.findAll();
        assertThat(subCountyCodeList).hasSize(databaseSizeBeforeCreate);

        // Validate the SubCountyCode in Elasticsearch
        verify(mockSubCountyCodeSearchRepository, times(0)).save(subCountyCode);
    }

    @Test
    @Transactional
    void getAllSubCountyCodes() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList
        restSubCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCountyCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].subCountyCode").value(hasItem(DEFAULT_SUB_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].subCountyName").value(hasItem(DEFAULT_SUB_COUNTY_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubCountyCodesWithEagerRelationshipsIsEnabled() throws Exception {
        when(subCountyCodeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubCountyCodeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(subCountyCodeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubCountyCodesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(subCountyCodeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubCountyCodeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(subCountyCodeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSubCountyCode() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get the subCountyCode
        restSubCountyCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, subCountyCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subCountyCode.getId().intValue()))
            .andExpect(jsonPath("$.countyCode").value(DEFAULT_COUNTY_CODE))
            .andExpect(jsonPath("$.countyName").value(DEFAULT_COUNTY_NAME))
            .andExpect(jsonPath("$.subCountyCode").value(DEFAULT_SUB_COUNTY_CODE))
            .andExpect(jsonPath("$.subCountyName").value(DEFAULT_SUB_COUNTY_NAME));
    }

    @Test
    @Transactional
    void getSubCountyCodesByIdFiltering() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        Long id = subCountyCode.getId();

        defaultSubCountyCodeShouldBeFound("id.equals=" + id);
        defaultSubCountyCodeShouldNotBeFound("id.notEquals=" + id);

        defaultSubCountyCodeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSubCountyCodeShouldNotBeFound("id.greaterThan=" + id);

        defaultSubCountyCodeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSubCountyCodeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByCountyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where countyCode equals to DEFAULT_COUNTY_CODE
        defaultSubCountyCodeShouldBeFound("countyCode.equals=" + DEFAULT_COUNTY_CODE);

        // Get all the subCountyCodeList where countyCode equals to UPDATED_COUNTY_CODE
        defaultSubCountyCodeShouldNotBeFound("countyCode.equals=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByCountyCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where countyCode not equals to DEFAULT_COUNTY_CODE
        defaultSubCountyCodeShouldNotBeFound("countyCode.notEquals=" + DEFAULT_COUNTY_CODE);

        // Get all the subCountyCodeList where countyCode not equals to UPDATED_COUNTY_CODE
        defaultSubCountyCodeShouldBeFound("countyCode.notEquals=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByCountyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where countyCode in DEFAULT_COUNTY_CODE or UPDATED_COUNTY_CODE
        defaultSubCountyCodeShouldBeFound("countyCode.in=" + DEFAULT_COUNTY_CODE + "," + UPDATED_COUNTY_CODE);

        // Get all the subCountyCodeList where countyCode equals to UPDATED_COUNTY_CODE
        defaultSubCountyCodeShouldNotBeFound("countyCode.in=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByCountyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where countyCode is not null
        defaultSubCountyCodeShouldBeFound("countyCode.specified=true");

        // Get all the subCountyCodeList where countyCode is null
        defaultSubCountyCodeShouldNotBeFound("countyCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByCountyCodeContainsSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where countyCode contains DEFAULT_COUNTY_CODE
        defaultSubCountyCodeShouldBeFound("countyCode.contains=" + DEFAULT_COUNTY_CODE);

        // Get all the subCountyCodeList where countyCode contains UPDATED_COUNTY_CODE
        defaultSubCountyCodeShouldNotBeFound("countyCode.contains=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByCountyCodeNotContainsSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where countyCode does not contain DEFAULT_COUNTY_CODE
        defaultSubCountyCodeShouldNotBeFound("countyCode.doesNotContain=" + DEFAULT_COUNTY_CODE);

        // Get all the subCountyCodeList where countyCode does not contain UPDATED_COUNTY_CODE
        defaultSubCountyCodeShouldBeFound("countyCode.doesNotContain=" + UPDATED_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByCountyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where countyName equals to DEFAULT_COUNTY_NAME
        defaultSubCountyCodeShouldBeFound("countyName.equals=" + DEFAULT_COUNTY_NAME);

        // Get all the subCountyCodeList where countyName equals to UPDATED_COUNTY_NAME
        defaultSubCountyCodeShouldNotBeFound("countyName.equals=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByCountyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where countyName not equals to DEFAULT_COUNTY_NAME
        defaultSubCountyCodeShouldNotBeFound("countyName.notEquals=" + DEFAULT_COUNTY_NAME);

        // Get all the subCountyCodeList where countyName not equals to UPDATED_COUNTY_NAME
        defaultSubCountyCodeShouldBeFound("countyName.notEquals=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByCountyNameIsInShouldWork() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where countyName in DEFAULT_COUNTY_NAME or UPDATED_COUNTY_NAME
        defaultSubCountyCodeShouldBeFound("countyName.in=" + DEFAULT_COUNTY_NAME + "," + UPDATED_COUNTY_NAME);

        // Get all the subCountyCodeList where countyName equals to UPDATED_COUNTY_NAME
        defaultSubCountyCodeShouldNotBeFound("countyName.in=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByCountyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where countyName is not null
        defaultSubCountyCodeShouldBeFound("countyName.specified=true");

        // Get all the subCountyCodeList where countyName is null
        defaultSubCountyCodeShouldNotBeFound("countyName.specified=false");
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByCountyNameContainsSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where countyName contains DEFAULT_COUNTY_NAME
        defaultSubCountyCodeShouldBeFound("countyName.contains=" + DEFAULT_COUNTY_NAME);

        // Get all the subCountyCodeList where countyName contains UPDATED_COUNTY_NAME
        defaultSubCountyCodeShouldNotBeFound("countyName.contains=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByCountyNameNotContainsSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where countyName does not contain DEFAULT_COUNTY_NAME
        defaultSubCountyCodeShouldNotBeFound("countyName.doesNotContain=" + DEFAULT_COUNTY_NAME);

        // Get all the subCountyCodeList where countyName does not contain UPDATED_COUNTY_NAME
        defaultSubCountyCodeShouldBeFound("countyName.doesNotContain=" + UPDATED_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesBySubCountyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where subCountyCode equals to DEFAULT_SUB_COUNTY_CODE
        defaultSubCountyCodeShouldBeFound("subCountyCode.equals=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the subCountyCodeList where subCountyCode equals to UPDATED_SUB_COUNTY_CODE
        defaultSubCountyCodeShouldNotBeFound("subCountyCode.equals=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesBySubCountyCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where subCountyCode not equals to DEFAULT_SUB_COUNTY_CODE
        defaultSubCountyCodeShouldNotBeFound("subCountyCode.notEquals=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the subCountyCodeList where subCountyCode not equals to UPDATED_SUB_COUNTY_CODE
        defaultSubCountyCodeShouldBeFound("subCountyCode.notEquals=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesBySubCountyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where subCountyCode in DEFAULT_SUB_COUNTY_CODE or UPDATED_SUB_COUNTY_CODE
        defaultSubCountyCodeShouldBeFound("subCountyCode.in=" + DEFAULT_SUB_COUNTY_CODE + "," + UPDATED_SUB_COUNTY_CODE);

        // Get all the subCountyCodeList where subCountyCode equals to UPDATED_SUB_COUNTY_CODE
        defaultSubCountyCodeShouldNotBeFound("subCountyCode.in=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesBySubCountyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where subCountyCode is not null
        defaultSubCountyCodeShouldBeFound("subCountyCode.specified=true");

        // Get all the subCountyCodeList where subCountyCode is null
        defaultSubCountyCodeShouldNotBeFound("subCountyCode.specified=false");
    }

    @Test
    @Transactional
    void getAllSubCountyCodesBySubCountyCodeContainsSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where subCountyCode contains DEFAULT_SUB_COUNTY_CODE
        defaultSubCountyCodeShouldBeFound("subCountyCode.contains=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the subCountyCodeList where subCountyCode contains UPDATED_SUB_COUNTY_CODE
        defaultSubCountyCodeShouldNotBeFound("subCountyCode.contains=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesBySubCountyCodeNotContainsSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where subCountyCode does not contain DEFAULT_SUB_COUNTY_CODE
        defaultSubCountyCodeShouldNotBeFound("subCountyCode.doesNotContain=" + DEFAULT_SUB_COUNTY_CODE);

        // Get all the subCountyCodeList where subCountyCode does not contain UPDATED_SUB_COUNTY_CODE
        defaultSubCountyCodeShouldBeFound("subCountyCode.doesNotContain=" + UPDATED_SUB_COUNTY_CODE);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesBySubCountyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where subCountyName equals to DEFAULT_SUB_COUNTY_NAME
        defaultSubCountyCodeShouldBeFound("subCountyName.equals=" + DEFAULT_SUB_COUNTY_NAME);

        // Get all the subCountyCodeList where subCountyName equals to UPDATED_SUB_COUNTY_NAME
        defaultSubCountyCodeShouldNotBeFound("subCountyName.equals=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesBySubCountyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where subCountyName not equals to DEFAULT_SUB_COUNTY_NAME
        defaultSubCountyCodeShouldNotBeFound("subCountyName.notEquals=" + DEFAULT_SUB_COUNTY_NAME);

        // Get all the subCountyCodeList where subCountyName not equals to UPDATED_SUB_COUNTY_NAME
        defaultSubCountyCodeShouldBeFound("subCountyName.notEquals=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesBySubCountyNameIsInShouldWork() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where subCountyName in DEFAULT_SUB_COUNTY_NAME or UPDATED_SUB_COUNTY_NAME
        defaultSubCountyCodeShouldBeFound("subCountyName.in=" + DEFAULT_SUB_COUNTY_NAME + "," + UPDATED_SUB_COUNTY_NAME);

        // Get all the subCountyCodeList where subCountyName equals to UPDATED_SUB_COUNTY_NAME
        defaultSubCountyCodeShouldNotBeFound("subCountyName.in=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesBySubCountyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where subCountyName is not null
        defaultSubCountyCodeShouldBeFound("subCountyName.specified=true");

        // Get all the subCountyCodeList where subCountyName is null
        defaultSubCountyCodeShouldNotBeFound("subCountyName.specified=false");
    }

    @Test
    @Transactional
    void getAllSubCountyCodesBySubCountyNameContainsSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where subCountyName contains DEFAULT_SUB_COUNTY_NAME
        defaultSubCountyCodeShouldBeFound("subCountyName.contains=" + DEFAULT_SUB_COUNTY_NAME);

        // Get all the subCountyCodeList where subCountyName contains UPDATED_SUB_COUNTY_NAME
        defaultSubCountyCodeShouldNotBeFound("subCountyName.contains=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesBySubCountyNameNotContainsSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        // Get all the subCountyCodeList where subCountyName does not contain DEFAULT_SUB_COUNTY_NAME
        defaultSubCountyCodeShouldNotBeFound("subCountyName.doesNotContain=" + DEFAULT_SUB_COUNTY_NAME);

        // Get all the subCountyCodeList where subCountyName does not contain UPDATED_SUB_COUNTY_NAME
        defaultSubCountyCodeShouldBeFound("subCountyName.doesNotContain=" + UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void getAllSubCountyCodesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);
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
        subCountyCode.addPlaceholder(placeholder);
        subCountyCodeRepository.saveAndFlush(subCountyCode);
        Long placeholderId = placeholder.getId();

        // Get all the subCountyCodeList where placeholder equals to placeholderId
        defaultSubCountyCodeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the subCountyCodeList where placeholder equals to (placeholderId + 1)
        defaultSubCountyCodeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubCountyCodeShouldBeFound(String filter) throws Exception {
        restSubCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCountyCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].subCountyCode").value(hasItem(DEFAULT_SUB_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].subCountyName").value(hasItem(DEFAULT_SUB_COUNTY_NAME)));

        // Check, that the count call also returns 1
        restSubCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubCountyCodeShouldNotBeFound(String filter) throws Exception {
        restSubCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubCountyCodeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubCountyCode() throws Exception {
        // Get the subCountyCode
        restSubCountyCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubCountyCode() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        int databaseSizeBeforeUpdate = subCountyCodeRepository.findAll().size();

        // Update the subCountyCode
        SubCountyCode updatedSubCountyCode = subCountyCodeRepository.findById(subCountyCode.getId()).get();
        // Disconnect from session so that the updates on updatedSubCountyCode are not directly saved in db
        em.detach(updatedSubCountyCode);
        updatedSubCountyCode
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME)
            .subCountyCode(UPDATED_SUB_COUNTY_CODE)
            .subCountyName(UPDATED_SUB_COUNTY_NAME);
        SubCountyCodeDTO subCountyCodeDTO = subCountyCodeMapper.toDto(updatedSubCountyCode);

        restSubCountyCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subCountyCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subCountyCodeDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubCountyCode in the database
        List<SubCountyCode> subCountyCodeList = subCountyCodeRepository.findAll();
        assertThat(subCountyCodeList).hasSize(databaseSizeBeforeUpdate);
        SubCountyCode testSubCountyCode = subCountyCodeList.get(subCountyCodeList.size() - 1);
        assertThat(testSubCountyCode.getCountyCode()).isEqualTo(UPDATED_COUNTY_CODE);
        assertThat(testSubCountyCode.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testSubCountyCode.getSubCountyCode()).isEqualTo(UPDATED_SUB_COUNTY_CODE);
        assertThat(testSubCountyCode.getSubCountyName()).isEqualTo(UPDATED_SUB_COUNTY_NAME);

        // Validate the SubCountyCode in Elasticsearch
        verify(mockSubCountyCodeSearchRepository).save(testSubCountyCode);
    }

    @Test
    @Transactional
    void putNonExistingSubCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = subCountyCodeRepository.findAll().size();
        subCountyCode.setId(count.incrementAndGet());

        // Create the SubCountyCode
        SubCountyCodeDTO subCountyCodeDTO = subCountyCodeMapper.toDto(subCountyCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCountyCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subCountyCodeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCountyCode in the database
        List<SubCountyCode> subCountyCodeList = subCountyCodeRepository.findAll();
        assertThat(subCountyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SubCountyCode in Elasticsearch
        verify(mockSubCountyCodeSearchRepository, times(0)).save(subCountyCode);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = subCountyCodeRepository.findAll().size();
        subCountyCode.setId(count.incrementAndGet());

        // Create the SubCountyCode
        SubCountyCodeDTO subCountyCodeDTO = subCountyCodeMapper.toDto(subCountyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCountyCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCountyCode in the database
        List<SubCountyCode> subCountyCodeList = subCountyCodeRepository.findAll();
        assertThat(subCountyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SubCountyCode in Elasticsearch
        verify(mockSubCountyCodeSearchRepository, times(0)).save(subCountyCode);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = subCountyCodeRepository.findAll().size();
        subCountyCode.setId(count.incrementAndGet());

        // Create the SubCountyCode
        SubCountyCodeDTO subCountyCodeDTO = subCountyCodeMapper.toDto(subCountyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCountyCodeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCountyCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubCountyCode in the database
        List<SubCountyCode> subCountyCodeList = subCountyCodeRepository.findAll();
        assertThat(subCountyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SubCountyCode in Elasticsearch
        verify(mockSubCountyCodeSearchRepository, times(0)).save(subCountyCode);
    }

    @Test
    @Transactional
    void partialUpdateSubCountyCodeWithPatch() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        int databaseSizeBeforeUpdate = subCountyCodeRepository.findAll().size();

        // Update the subCountyCode using partial update
        SubCountyCode partialUpdatedSubCountyCode = new SubCountyCode();
        partialUpdatedSubCountyCode.setId(subCountyCode.getId());

        partialUpdatedSubCountyCode.countyName(UPDATED_COUNTY_NAME).subCountyCode(UPDATED_SUB_COUNTY_CODE);

        restSubCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubCountyCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubCountyCode))
            )
            .andExpect(status().isOk());

        // Validate the SubCountyCode in the database
        List<SubCountyCode> subCountyCodeList = subCountyCodeRepository.findAll();
        assertThat(subCountyCodeList).hasSize(databaseSizeBeforeUpdate);
        SubCountyCode testSubCountyCode = subCountyCodeList.get(subCountyCodeList.size() - 1);
        assertThat(testSubCountyCode.getCountyCode()).isEqualTo(DEFAULT_COUNTY_CODE);
        assertThat(testSubCountyCode.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testSubCountyCode.getSubCountyCode()).isEqualTo(UPDATED_SUB_COUNTY_CODE);
        assertThat(testSubCountyCode.getSubCountyName()).isEqualTo(DEFAULT_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSubCountyCodeWithPatch() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        int databaseSizeBeforeUpdate = subCountyCodeRepository.findAll().size();

        // Update the subCountyCode using partial update
        SubCountyCode partialUpdatedSubCountyCode = new SubCountyCode();
        partialUpdatedSubCountyCode.setId(subCountyCode.getId());

        partialUpdatedSubCountyCode
            .countyCode(UPDATED_COUNTY_CODE)
            .countyName(UPDATED_COUNTY_NAME)
            .subCountyCode(UPDATED_SUB_COUNTY_CODE)
            .subCountyName(UPDATED_SUB_COUNTY_NAME);

        restSubCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubCountyCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubCountyCode))
            )
            .andExpect(status().isOk());

        // Validate the SubCountyCode in the database
        List<SubCountyCode> subCountyCodeList = subCountyCodeRepository.findAll();
        assertThat(subCountyCodeList).hasSize(databaseSizeBeforeUpdate);
        SubCountyCode testSubCountyCode = subCountyCodeList.get(subCountyCodeList.size() - 1);
        assertThat(testSubCountyCode.getCountyCode()).isEqualTo(UPDATED_COUNTY_CODE);
        assertThat(testSubCountyCode.getCountyName()).isEqualTo(UPDATED_COUNTY_NAME);
        assertThat(testSubCountyCode.getSubCountyCode()).isEqualTo(UPDATED_SUB_COUNTY_CODE);
        assertThat(testSubCountyCode.getSubCountyName()).isEqualTo(UPDATED_SUB_COUNTY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSubCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = subCountyCodeRepository.findAll().size();
        subCountyCode.setId(count.incrementAndGet());

        // Create the SubCountyCode
        SubCountyCodeDTO subCountyCodeDTO = subCountyCodeMapper.toDto(subCountyCode);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subCountyCodeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCountyCode in the database
        List<SubCountyCode> subCountyCodeList = subCountyCodeRepository.findAll();
        assertThat(subCountyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SubCountyCode in Elasticsearch
        verify(mockSubCountyCodeSearchRepository, times(0)).save(subCountyCode);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = subCountyCodeRepository.findAll().size();
        subCountyCode.setId(count.incrementAndGet());

        // Create the SubCountyCode
        SubCountyCodeDTO subCountyCodeDTO = subCountyCodeMapper.toDto(subCountyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subCountyCodeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCountyCode in the database
        List<SubCountyCode> subCountyCodeList = subCountyCodeRepository.findAll();
        assertThat(subCountyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SubCountyCode in Elasticsearch
        verify(mockSubCountyCodeSearchRepository, times(0)).save(subCountyCode);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubCountyCode() throws Exception {
        int databaseSizeBeforeUpdate = subCountyCodeRepository.findAll().size();
        subCountyCode.setId(count.incrementAndGet());

        // Create the SubCountyCode
        SubCountyCodeDTO subCountyCodeDTO = subCountyCodeMapper.toDto(subCountyCode);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCountyCodeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subCountyCodeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubCountyCode in the database
        List<SubCountyCode> subCountyCodeList = subCountyCodeRepository.findAll();
        assertThat(subCountyCodeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SubCountyCode in Elasticsearch
        verify(mockSubCountyCodeSearchRepository, times(0)).save(subCountyCode);
    }

    @Test
    @Transactional
    void deleteSubCountyCode() throws Exception {
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);

        int databaseSizeBeforeDelete = subCountyCodeRepository.findAll().size();

        // Delete the subCountyCode
        restSubCountyCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, subCountyCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubCountyCode> subCountyCodeList = subCountyCodeRepository.findAll();
        assertThat(subCountyCodeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SubCountyCode in Elasticsearch
        verify(mockSubCountyCodeSearchRepository, times(1)).deleteById(subCountyCode.getId());
    }

    @Test
    @Transactional
    void searchSubCountyCode() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        subCountyCodeRepository.saveAndFlush(subCountyCode);
        when(mockSubCountyCodeSearchRepository.search("id:" + subCountyCode.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(subCountyCode), PageRequest.of(0, 1), 1));

        // Search the subCountyCode
        restSubCountyCodeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + subCountyCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCountyCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].countyCode").value(hasItem(DEFAULT_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].countyName").value(hasItem(DEFAULT_COUNTY_NAME)))
            .andExpect(jsonPath("$.[*].subCountyCode").value(hasItem(DEFAULT_SUB_COUNTY_CODE)))
            .andExpect(jsonPath("$.[*].subCountyName").value(hasItem(DEFAULT_SUB_COUNTY_NAME)));
    }
}
