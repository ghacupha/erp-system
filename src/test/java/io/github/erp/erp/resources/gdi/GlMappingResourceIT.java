package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
import io.github.erp.domain.GlMapping;
import io.github.erp.repository.GlMappingRepository;
import io.github.erp.repository.search.GlMappingSearchRepository;
import io.github.erp.service.dto.GlMappingDTO;
import io.github.erp.service.mapper.GlMappingMapper;
import io.github.erp.web.rest.GlMappingResource;
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
 * Integration tests for the {@link GlMappingResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class GlMappingResourceIT {

    private static final String DEFAULT_SUB_GL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUB_GL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_GL_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SUB_GL_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_GL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_GL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_GL_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_GL_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_GL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_GL_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/gl-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/gl-mappings";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GlMappingRepository glMappingRepository;

    @Autowired
    private GlMappingMapper glMappingMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.GlMappingSearchRepositoryMockConfiguration
     */
    @Autowired
    private GlMappingSearchRepository mockGlMappingSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGlMappingMockMvc;

    private GlMapping glMapping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlMapping createEntity(EntityManager em) {
        GlMapping glMapping = new GlMapping()
            .subGLCode(DEFAULT_SUB_GL_CODE)
            .subGLDescription(DEFAULT_SUB_GL_DESCRIPTION)
            .mainGLCode(DEFAULT_MAIN_GL_CODE)
            .mainGLDescription(DEFAULT_MAIN_GL_DESCRIPTION)
            .glType(DEFAULT_GL_TYPE);
        return glMapping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlMapping createUpdatedEntity(EntityManager em) {
        GlMapping glMapping = new GlMapping()
            .subGLCode(UPDATED_SUB_GL_CODE)
            .subGLDescription(UPDATED_SUB_GL_DESCRIPTION)
            .mainGLCode(UPDATED_MAIN_GL_CODE)
            .mainGLDescription(UPDATED_MAIN_GL_DESCRIPTION)
            .glType(UPDATED_GL_TYPE);
        return glMapping;
    }

    @BeforeEach
    public void initTest() {
        glMapping = createEntity(em);
    }

    @Test
    @Transactional
    void createGlMapping() throws Exception {
        int databaseSizeBeforeCreate = glMappingRepository.findAll().size();
        // Create the GlMapping
        GlMappingDTO glMappingDTO = glMappingMapper.toDto(glMapping);
        restGlMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(glMappingDTO)))
            .andExpect(status().isCreated());

        // Validate the GlMapping in the database
        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeCreate + 1);
        GlMapping testGlMapping = glMappingList.get(glMappingList.size() - 1);
        assertThat(testGlMapping.getSubGLCode()).isEqualTo(DEFAULT_SUB_GL_CODE);
        assertThat(testGlMapping.getSubGLDescription()).isEqualTo(DEFAULT_SUB_GL_DESCRIPTION);
        assertThat(testGlMapping.getMainGLCode()).isEqualTo(DEFAULT_MAIN_GL_CODE);
        assertThat(testGlMapping.getMainGLDescription()).isEqualTo(DEFAULT_MAIN_GL_DESCRIPTION);
        assertThat(testGlMapping.getGlType()).isEqualTo(DEFAULT_GL_TYPE);

        // Validate the GlMapping in Elasticsearch
        verify(mockGlMappingSearchRepository, times(1)).save(testGlMapping);
    }

    @Test
    @Transactional
    void createGlMappingWithExistingId() throws Exception {
        // Create the GlMapping with an existing ID
        glMapping.setId(1L);
        GlMappingDTO glMappingDTO = glMappingMapper.toDto(glMapping);

        int databaseSizeBeforeCreate = glMappingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGlMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(glMappingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GlMapping in the database
        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeCreate);

        // Validate the GlMapping in Elasticsearch
        verify(mockGlMappingSearchRepository, times(0)).save(glMapping);
    }

    @Test
    @Transactional
    void checkSubGLCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = glMappingRepository.findAll().size();
        // set the field null
        glMapping.setSubGLCode(null);

        // Create the GlMapping, which fails.
        GlMappingDTO glMappingDTO = glMappingMapper.toDto(glMapping);

        restGlMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(glMappingDTO)))
            .andExpect(status().isBadRequest());

        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMainGLCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = glMappingRepository.findAll().size();
        // set the field null
        glMapping.setMainGLCode(null);

        // Create the GlMapping, which fails.
        GlMappingDTO glMappingDTO = glMappingMapper.toDto(glMapping);

        restGlMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(glMappingDTO)))
            .andExpect(status().isBadRequest());

        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGlTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = glMappingRepository.findAll().size();
        // set the field null
        glMapping.setGlType(null);

        // Create the GlMapping, which fails.
        GlMappingDTO glMappingDTO = glMappingMapper.toDto(glMapping);

        restGlMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(glMappingDTO)))
            .andExpect(status().isBadRequest());

        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGlMappings() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList
        restGlMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(glMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].subGLCode").value(hasItem(DEFAULT_SUB_GL_CODE)))
            .andExpect(jsonPath("$.[*].subGLDescription").value(hasItem(DEFAULT_SUB_GL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].mainGLCode").value(hasItem(DEFAULT_MAIN_GL_CODE)))
            .andExpect(jsonPath("$.[*].mainGLDescription").value(hasItem(DEFAULT_MAIN_GL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].glType").value(hasItem(DEFAULT_GL_TYPE)));
    }

    @Test
    @Transactional
    void getGlMapping() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get the glMapping
        restGlMappingMockMvc
            .perform(get(ENTITY_API_URL_ID, glMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(glMapping.getId().intValue()))
            .andExpect(jsonPath("$.subGLCode").value(DEFAULT_SUB_GL_CODE))
            .andExpect(jsonPath("$.subGLDescription").value(DEFAULT_SUB_GL_DESCRIPTION))
            .andExpect(jsonPath("$.mainGLCode").value(DEFAULT_MAIN_GL_CODE))
            .andExpect(jsonPath("$.mainGLDescription").value(DEFAULT_MAIN_GL_DESCRIPTION))
            .andExpect(jsonPath("$.glType").value(DEFAULT_GL_TYPE));
    }

    @Test
    @Transactional
    void getGlMappingsByIdFiltering() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        Long id = glMapping.getId();

        defaultGlMappingShouldBeFound("id.equals=" + id);
        defaultGlMappingShouldNotBeFound("id.notEquals=" + id);

        defaultGlMappingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGlMappingShouldNotBeFound("id.greaterThan=" + id);

        defaultGlMappingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGlMappingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGlMappingsBySubGLCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where subGLCode equals to DEFAULT_SUB_GL_CODE
        defaultGlMappingShouldBeFound("subGLCode.equals=" + DEFAULT_SUB_GL_CODE);

        // Get all the glMappingList where subGLCode equals to UPDATED_SUB_GL_CODE
        defaultGlMappingShouldNotBeFound("subGLCode.equals=" + UPDATED_SUB_GL_CODE);
    }

    @Test
    @Transactional
    void getAllGlMappingsBySubGLCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where subGLCode not equals to DEFAULT_SUB_GL_CODE
        defaultGlMappingShouldNotBeFound("subGLCode.notEquals=" + DEFAULT_SUB_GL_CODE);

        // Get all the glMappingList where subGLCode not equals to UPDATED_SUB_GL_CODE
        defaultGlMappingShouldBeFound("subGLCode.notEquals=" + UPDATED_SUB_GL_CODE);
    }

    @Test
    @Transactional
    void getAllGlMappingsBySubGLCodeIsInShouldWork() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where subGLCode in DEFAULT_SUB_GL_CODE or UPDATED_SUB_GL_CODE
        defaultGlMappingShouldBeFound("subGLCode.in=" + DEFAULT_SUB_GL_CODE + "," + UPDATED_SUB_GL_CODE);

        // Get all the glMappingList where subGLCode equals to UPDATED_SUB_GL_CODE
        defaultGlMappingShouldNotBeFound("subGLCode.in=" + UPDATED_SUB_GL_CODE);
    }

    @Test
    @Transactional
    void getAllGlMappingsBySubGLCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where subGLCode is not null
        defaultGlMappingShouldBeFound("subGLCode.specified=true");

        // Get all the glMappingList where subGLCode is null
        defaultGlMappingShouldNotBeFound("subGLCode.specified=false");
    }

    @Test
    @Transactional
    void getAllGlMappingsBySubGLCodeContainsSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where subGLCode contains DEFAULT_SUB_GL_CODE
        defaultGlMappingShouldBeFound("subGLCode.contains=" + DEFAULT_SUB_GL_CODE);

        // Get all the glMappingList where subGLCode contains UPDATED_SUB_GL_CODE
        defaultGlMappingShouldNotBeFound("subGLCode.contains=" + UPDATED_SUB_GL_CODE);
    }

    @Test
    @Transactional
    void getAllGlMappingsBySubGLCodeNotContainsSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where subGLCode does not contain DEFAULT_SUB_GL_CODE
        defaultGlMappingShouldNotBeFound("subGLCode.doesNotContain=" + DEFAULT_SUB_GL_CODE);

        // Get all the glMappingList where subGLCode does not contain UPDATED_SUB_GL_CODE
        defaultGlMappingShouldBeFound("subGLCode.doesNotContain=" + UPDATED_SUB_GL_CODE);
    }

    @Test
    @Transactional
    void getAllGlMappingsBySubGLDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where subGLDescription equals to DEFAULT_SUB_GL_DESCRIPTION
        defaultGlMappingShouldBeFound("subGLDescription.equals=" + DEFAULT_SUB_GL_DESCRIPTION);

        // Get all the glMappingList where subGLDescription equals to UPDATED_SUB_GL_DESCRIPTION
        defaultGlMappingShouldNotBeFound("subGLDescription.equals=" + UPDATED_SUB_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGlMappingsBySubGLDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where subGLDescription not equals to DEFAULT_SUB_GL_DESCRIPTION
        defaultGlMappingShouldNotBeFound("subGLDescription.notEquals=" + DEFAULT_SUB_GL_DESCRIPTION);

        // Get all the glMappingList where subGLDescription not equals to UPDATED_SUB_GL_DESCRIPTION
        defaultGlMappingShouldBeFound("subGLDescription.notEquals=" + UPDATED_SUB_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGlMappingsBySubGLDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where subGLDescription in DEFAULT_SUB_GL_DESCRIPTION or UPDATED_SUB_GL_DESCRIPTION
        defaultGlMappingShouldBeFound("subGLDescription.in=" + DEFAULT_SUB_GL_DESCRIPTION + "," + UPDATED_SUB_GL_DESCRIPTION);

        // Get all the glMappingList where subGLDescription equals to UPDATED_SUB_GL_DESCRIPTION
        defaultGlMappingShouldNotBeFound("subGLDescription.in=" + UPDATED_SUB_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGlMappingsBySubGLDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where subGLDescription is not null
        defaultGlMappingShouldBeFound("subGLDescription.specified=true");

        // Get all the glMappingList where subGLDescription is null
        defaultGlMappingShouldNotBeFound("subGLDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllGlMappingsBySubGLDescriptionContainsSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where subGLDescription contains DEFAULT_SUB_GL_DESCRIPTION
        defaultGlMappingShouldBeFound("subGLDescription.contains=" + DEFAULT_SUB_GL_DESCRIPTION);

        // Get all the glMappingList where subGLDescription contains UPDATED_SUB_GL_DESCRIPTION
        defaultGlMappingShouldNotBeFound("subGLDescription.contains=" + UPDATED_SUB_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGlMappingsBySubGLDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where subGLDescription does not contain DEFAULT_SUB_GL_DESCRIPTION
        defaultGlMappingShouldNotBeFound("subGLDescription.doesNotContain=" + DEFAULT_SUB_GL_DESCRIPTION);

        // Get all the glMappingList where subGLDescription does not contain UPDATED_SUB_GL_DESCRIPTION
        defaultGlMappingShouldBeFound("subGLDescription.doesNotContain=" + UPDATED_SUB_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGlMappingsByMainGLCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where mainGLCode equals to DEFAULT_MAIN_GL_CODE
        defaultGlMappingShouldBeFound("mainGLCode.equals=" + DEFAULT_MAIN_GL_CODE);

        // Get all the glMappingList where mainGLCode equals to UPDATED_MAIN_GL_CODE
        defaultGlMappingShouldNotBeFound("mainGLCode.equals=" + UPDATED_MAIN_GL_CODE);
    }

    @Test
    @Transactional
    void getAllGlMappingsByMainGLCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where mainGLCode not equals to DEFAULT_MAIN_GL_CODE
        defaultGlMappingShouldNotBeFound("mainGLCode.notEquals=" + DEFAULT_MAIN_GL_CODE);

        // Get all the glMappingList where mainGLCode not equals to UPDATED_MAIN_GL_CODE
        defaultGlMappingShouldBeFound("mainGLCode.notEquals=" + UPDATED_MAIN_GL_CODE);
    }

    @Test
    @Transactional
    void getAllGlMappingsByMainGLCodeIsInShouldWork() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where mainGLCode in DEFAULT_MAIN_GL_CODE or UPDATED_MAIN_GL_CODE
        defaultGlMappingShouldBeFound("mainGLCode.in=" + DEFAULT_MAIN_GL_CODE + "," + UPDATED_MAIN_GL_CODE);

        // Get all the glMappingList where mainGLCode equals to UPDATED_MAIN_GL_CODE
        defaultGlMappingShouldNotBeFound("mainGLCode.in=" + UPDATED_MAIN_GL_CODE);
    }

    @Test
    @Transactional
    void getAllGlMappingsByMainGLCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where mainGLCode is not null
        defaultGlMappingShouldBeFound("mainGLCode.specified=true");

        // Get all the glMappingList where mainGLCode is null
        defaultGlMappingShouldNotBeFound("mainGLCode.specified=false");
    }

    @Test
    @Transactional
    void getAllGlMappingsByMainGLCodeContainsSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where mainGLCode contains DEFAULT_MAIN_GL_CODE
        defaultGlMappingShouldBeFound("mainGLCode.contains=" + DEFAULT_MAIN_GL_CODE);

        // Get all the glMappingList where mainGLCode contains UPDATED_MAIN_GL_CODE
        defaultGlMappingShouldNotBeFound("mainGLCode.contains=" + UPDATED_MAIN_GL_CODE);
    }

    @Test
    @Transactional
    void getAllGlMappingsByMainGLCodeNotContainsSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where mainGLCode does not contain DEFAULT_MAIN_GL_CODE
        defaultGlMappingShouldNotBeFound("mainGLCode.doesNotContain=" + DEFAULT_MAIN_GL_CODE);

        // Get all the glMappingList where mainGLCode does not contain UPDATED_MAIN_GL_CODE
        defaultGlMappingShouldBeFound("mainGLCode.doesNotContain=" + UPDATED_MAIN_GL_CODE);
    }

    @Test
    @Transactional
    void getAllGlMappingsByMainGLDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where mainGLDescription equals to DEFAULT_MAIN_GL_DESCRIPTION
        defaultGlMappingShouldBeFound("mainGLDescription.equals=" + DEFAULT_MAIN_GL_DESCRIPTION);

        // Get all the glMappingList where mainGLDescription equals to UPDATED_MAIN_GL_DESCRIPTION
        defaultGlMappingShouldNotBeFound("mainGLDescription.equals=" + UPDATED_MAIN_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGlMappingsByMainGLDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where mainGLDescription not equals to DEFAULT_MAIN_GL_DESCRIPTION
        defaultGlMappingShouldNotBeFound("mainGLDescription.notEquals=" + DEFAULT_MAIN_GL_DESCRIPTION);

        // Get all the glMappingList where mainGLDescription not equals to UPDATED_MAIN_GL_DESCRIPTION
        defaultGlMappingShouldBeFound("mainGLDescription.notEquals=" + UPDATED_MAIN_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGlMappingsByMainGLDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where mainGLDescription in DEFAULT_MAIN_GL_DESCRIPTION or UPDATED_MAIN_GL_DESCRIPTION
        defaultGlMappingShouldBeFound("mainGLDescription.in=" + DEFAULT_MAIN_GL_DESCRIPTION + "," + UPDATED_MAIN_GL_DESCRIPTION);

        // Get all the glMappingList where mainGLDescription equals to UPDATED_MAIN_GL_DESCRIPTION
        defaultGlMappingShouldNotBeFound("mainGLDescription.in=" + UPDATED_MAIN_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGlMappingsByMainGLDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where mainGLDescription is not null
        defaultGlMappingShouldBeFound("mainGLDescription.specified=true");

        // Get all the glMappingList where mainGLDescription is null
        defaultGlMappingShouldNotBeFound("mainGLDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllGlMappingsByMainGLDescriptionContainsSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where mainGLDescription contains DEFAULT_MAIN_GL_DESCRIPTION
        defaultGlMappingShouldBeFound("mainGLDescription.contains=" + DEFAULT_MAIN_GL_DESCRIPTION);

        // Get all the glMappingList where mainGLDescription contains UPDATED_MAIN_GL_DESCRIPTION
        defaultGlMappingShouldNotBeFound("mainGLDescription.contains=" + UPDATED_MAIN_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGlMappingsByMainGLDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where mainGLDescription does not contain DEFAULT_MAIN_GL_DESCRIPTION
        defaultGlMappingShouldNotBeFound("mainGLDescription.doesNotContain=" + DEFAULT_MAIN_GL_DESCRIPTION);

        // Get all the glMappingList where mainGLDescription does not contain UPDATED_MAIN_GL_DESCRIPTION
        defaultGlMappingShouldBeFound("mainGLDescription.doesNotContain=" + UPDATED_MAIN_GL_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllGlMappingsByGlTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where glType equals to DEFAULT_GL_TYPE
        defaultGlMappingShouldBeFound("glType.equals=" + DEFAULT_GL_TYPE);

        // Get all the glMappingList where glType equals to UPDATED_GL_TYPE
        defaultGlMappingShouldNotBeFound("glType.equals=" + UPDATED_GL_TYPE);
    }

    @Test
    @Transactional
    void getAllGlMappingsByGlTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where glType not equals to DEFAULT_GL_TYPE
        defaultGlMappingShouldNotBeFound("glType.notEquals=" + DEFAULT_GL_TYPE);

        // Get all the glMappingList where glType not equals to UPDATED_GL_TYPE
        defaultGlMappingShouldBeFound("glType.notEquals=" + UPDATED_GL_TYPE);
    }

    @Test
    @Transactional
    void getAllGlMappingsByGlTypeIsInShouldWork() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where glType in DEFAULT_GL_TYPE or UPDATED_GL_TYPE
        defaultGlMappingShouldBeFound("glType.in=" + DEFAULT_GL_TYPE + "," + UPDATED_GL_TYPE);

        // Get all the glMappingList where glType equals to UPDATED_GL_TYPE
        defaultGlMappingShouldNotBeFound("glType.in=" + UPDATED_GL_TYPE);
    }

    @Test
    @Transactional
    void getAllGlMappingsByGlTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where glType is not null
        defaultGlMappingShouldBeFound("glType.specified=true");

        // Get all the glMappingList where glType is null
        defaultGlMappingShouldNotBeFound("glType.specified=false");
    }

    @Test
    @Transactional
    void getAllGlMappingsByGlTypeContainsSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where glType contains DEFAULT_GL_TYPE
        defaultGlMappingShouldBeFound("glType.contains=" + DEFAULT_GL_TYPE);

        // Get all the glMappingList where glType contains UPDATED_GL_TYPE
        defaultGlMappingShouldNotBeFound("glType.contains=" + UPDATED_GL_TYPE);
    }

    @Test
    @Transactional
    void getAllGlMappingsByGlTypeNotContainsSomething() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        // Get all the glMappingList where glType does not contain DEFAULT_GL_TYPE
        defaultGlMappingShouldNotBeFound("glType.doesNotContain=" + DEFAULT_GL_TYPE);

        // Get all the glMappingList where glType does not contain UPDATED_GL_TYPE
        defaultGlMappingShouldBeFound("glType.doesNotContain=" + UPDATED_GL_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGlMappingShouldBeFound(String filter) throws Exception {
        restGlMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(glMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].subGLCode").value(hasItem(DEFAULT_SUB_GL_CODE)))
            .andExpect(jsonPath("$.[*].subGLDescription").value(hasItem(DEFAULT_SUB_GL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].mainGLCode").value(hasItem(DEFAULT_MAIN_GL_CODE)))
            .andExpect(jsonPath("$.[*].mainGLDescription").value(hasItem(DEFAULT_MAIN_GL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].glType").value(hasItem(DEFAULT_GL_TYPE)));

        // Check, that the count call also returns 1
        restGlMappingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGlMappingShouldNotBeFound(String filter) throws Exception {
        restGlMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGlMappingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGlMapping() throws Exception {
        // Get the glMapping
        restGlMappingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGlMapping() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        int databaseSizeBeforeUpdate = glMappingRepository.findAll().size();

        // Update the glMapping
        GlMapping updatedGlMapping = glMappingRepository.findById(glMapping.getId()).get();
        // Disconnect from session so that the updates on updatedGlMapping are not directly saved in db
        em.detach(updatedGlMapping);
        updatedGlMapping
            .subGLCode(UPDATED_SUB_GL_CODE)
            .subGLDescription(UPDATED_SUB_GL_DESCRIPTION)
            .mainGLCode(UPDATED_MAIN_GL_CODE)
            .mainGLDescription(UPDATED_MAIN_GL_DESCRIPTION)
            .glType(UPDATED_GL_TYPE);
        GlMappingDTO glMappingDTO = glMappingMapper.toDto(updatedGlMapping);

        restGlMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, glMappingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(glMappingDTO))
            )
            .andExpect(status().isOk());

        // Validate the GlMapping in the database
        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeUpdate);
        GlMapping testGlMapping = glMappingList.get(glMappingList.size() - 1);
        assertThat(testGlMapping.getSubGLCode()).isEqualTo(UPDATED_SUB_GL_CODE);
        assertThat(testGlMapping.getSubGLDescription()).isEqualTo(UPDATED_SUB_GL_DESCRIPTION);
        assertThat(testGlMapping.getMainGLCode()).isEqualTo(UPDATED_MAIN_GL_CODE);
        assertThat(testGlMapping.getMainGLDescription()).isEqualTo(UPDATED_MAIN_GL_DESCRIPTION);
        assertThat(testGlMapping.getGlType()).isEqualTo(UPDATED_GL_TYPE);

        // Validate the GlMapping in Elasticsearch
        verify(mockGlMappingSearchRepository).save(testGlMapping);
    }

    @Test
    @Transactional
    void putNonExistingGlMapping() throws Exception {
        int databaseSizeBeforeUpdate = glMappingRepository.findAll().size();
        glMapping.setId(count.incrementAndGet());

        // Create the GlMapping
        GlMappingDTO glMappingDTO = glMappingMapper.toDto(glMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, glMappingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(glMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlMapping in the database
        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GlMapping in Elasticsearch
        verify(mockGlMappingSearchRepository, times(0)).save(glMapping);
    }

    @Test
    @Transactional
    void putWithIdMismatchGlMapping() throws Exception {
        int databaseSizeBeforeUpdate = glMappingRepository.findAll().size();
        glMapping.setId(count.incrementAndGet());

        // Create the GlMapping
        GlMappingDTO glMappingDTO = glMappingMapper.toDto(glMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(glMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlMapping in the database
        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GlMapping in Elasticsearch
        verify(mockGlMappingSearchRepository, times(0)).save(glMapping);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGlMapping() throws Exception {
        int databaseSizeBeforeUpdate = glMappingRepository.findAll().size();
        glMapping.setId(count.incrementAndGet());

        // Create the GlMapping
        GlMappingDTO glMappingDTO = glMappingMapper.toDto(glMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlMappingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(glMappingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GlMapping in the database
        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GlMapping in Elasticsearch
        verify(mockGlMappingSearchRepository, times(0)).save(glMapping);
    }

    @Test
    @Transactional
    void partialUpdateGlMappingWithPatch() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        int databaseSizeBeforeUpdate = glMappingRepository.findAll().size();

        // Update the glMapping using partial update
        GlMapping partialUpdatedGlMapping = new GlMapping();
        partialUpdatedGlMapping.setId(glMapping.getId());

        partialUpdatedGlMapping
            .subGLDescription(UPDATED_SUB_GL_DESCRIPTION)
            .mainGLCode(UPDATED_MAIN_GL_CODE)
            .mainGLDescription(UPDATED_MAIN_GL_DESCRIPTION);

        restGlMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGlMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGlMapping))
            )
            .andExpect(status().isOk());

        // Validate the GlMapping in the database
        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeUpdate);
        GlMapping testGlMapping = glMappingList.get(glMappingList.size() - 1);
        assertThat(testGlMapping.getSubGLCode()).isEqualTo(DEFAULT_SUB_GL_CODE);
        assertThat(testGlMapping.getSubGLDescription()).isEqualTo(UPDATED_SUB_GL_DESCRIPTION);
        assertThat(testGlMapping.getMainGLCode()).isEqualTo(UPDATED_MAIN_GL_CODE);
        assertThat(testGlMapping.getMainGLDescription()).isEqualTo(UPDATED_MAIN_GL_DESCRIPTION);
        assertThat(testGlMapping.getGlType()).isEqualTo(DEFAULT_GL_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateGlMappingWithPatch() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        int databaseSizeBeforeUpdate = glMappingRepository.findAll().size();

        // Update the glMapping using partial update
        GlMapping partialUpdatedGlMapping = new GlMapping();
        partialUpdatedGlMapping.setId(glMapping.getId());

        partialUpdatedGlMapping
            .subGLCode(UPDATED_SUB_GL_CODE)
            .subGLDescription(UPDATED_SUB_GL_DESCRIPTION)
            .mainGLCode(UPDATED_MAIN_GL_CODE)
            .mainGLDescription(UPDATED_MAIN_GL_DESCRIPTION)
            .glType(UPDATED_GL_TYPE);

        restGlMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGlMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGlMapping))
            )
            .andExpect(status().isOk());

        // Validate the GlMapping in the database
        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeUpdate);
        GlMapping testGlMapping = glMappingList.get(glMappingList.size() - 1);
        assertThat(testGlMapping.getSubGLCode()).isEqualTo(UPDATED_SUB_GL_CODE);
        assertThat(testGlMapping.getSubGLDescription()).isEqualTo(UPDATED_SUB_GL_DESCRIPTION);
        assertThat(testGlMapping.getMainGLCode()).isEqualTo(UPDATED_MAIN_GL_CODE);
        assertThat(testGlMapping.getMainGLDescription()).isEqualTo(UPDATED_MAIN_GL_DESCRIPTION);
        assertThat(testGlMapping.getGlType()).isEqualTo(UPDATED_GL_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingGlMapping() throws Exception {
        int databaseSizeBeforeUpdate = glMappingRepository.findAll().size();
        glMapping.setId(count.incrementAndGet());

        // Create the GlMapping
        GlMappingDTO glMappingDTO = glMappingMapper.toDto(glMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, glMappingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(glMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlMapping in the database
        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GlMapping in Elasticsearch
        verify(mockGlMappingSearchRepository, times(0)).save(glMapping);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGlMapping() throws Exception {
        int databaseSizeBeforeUpdate = glMappingRepository.findAll().size();
        glMapping.setId(count.incrementAndGet());

        // Create the GlMapping
        GlMappingDTO glMappingDTO = glMappingMapper.toDto(glMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(glMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlMapping in the database
        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GlMapping in Elasticsearch
        verify(mockGlMappingSearchRepository, times(0)).save(glMapping);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGlMapping() throws Exception {
        int databaseSizeBeforeUpdate = glMappingRepository.findAll().size();
        glMapping.setId(count.incrementAndGet());

        // Create the GlMapping
        GlMappingDTO glMappingDTO = glMappingMapper.toDto(glMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlMappingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(glMappingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GlMapping in the database
        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the GlMapping in Elasticsearch
        verify(mockGlMappingSearchRepository, times(0)).save(glMapping);
    }

    @Test
    @Transactional
    void deleteGlMapping() throws Exception {
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);

        int databaseSizeBeforeDelete = glMappingRepository.findAll().size();

        // Delete the glMapping
        restGlMappingMockMvc
            .perform(delete(ENTITY_API_URL_ID, glMapping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GlMapping> glMappingList = glMappingRepository.findAll();
        assertThat(glMappingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the GlMapping in Elasticsearch
        verify(mockGlMappingSearchRepository, times(1)).deleteById(glMapping.getId());
    }

    @Test
    @Transactional
    void searchGlMapping() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        glMappingRepository.saveAndFlush(glMapping);
        when(mockGlMappingSearchRepository.search("id:" + glMapping.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(glMapping), PageRequest.of(0, 1), 1));

        // Search the glMapping
        restGlMappingMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + glMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(glMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].subGLCode").value(hasItem(DEFAULT_SUB_GL_CODE)))
            .andExpect(jsonPath("$.[*].subGLDescription").value(hasItem(DEFAULT_SUB_GL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].mainGLCode").value(hasItem(DEFAULT_MAIN_GL_CODE)))
            .andExpect(jsonPath("$.[*].mainGLDescription").value(hasItem(DEFAULT_MAIN_GL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].glType").value(hasItem(DEFAULT_GL_TYPE)));
    }
}
