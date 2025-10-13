package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.AcquiringIssuingFlag;
import io.github.erp.repository.AcquiringIssuingFlagRepository;
import io.github.erp.repository.search.AcquiringIssuingFlagSearchRepository;
import io.github.erp.service.dto.AcquiringIssuingFlagDTO;
import io.github.erp.service.mapper.AcquiringIssuingFlagMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

import io.github.erp.web.rest.AcquiringIssuingFlagResource;
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
 * Integration tests for the {@link AcquiringIssuingFlagResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class AcquiringIssuingFlagResourceIT {

    private static final String DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_ACQUIRING_ISSUING_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CARD_ACQUIRING_ISSUING_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/acquiring-issuing-flags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/acquiring-issuing-flags";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcquiringIssuingFlagRepository acquiringIssuingFlagRepository;

    @Autowired
    private AcquiringIssuingFlagMapper acquiringIssuingFlagMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AcquiringIssuingFlagSearchRepositoryMockConfiguration
     */
    @Autowired
    private AcquiringIssuingFlagSearchRepository mockAcquiringIssuingFlagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcquiringIssuingFlagMockMvc;

    private AcquiringIssuingFlag acquiringIssuingFlag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcquiringIssuingFlag createEntity(EntityManager em) {
        AcquiringIssuingFlag acquiringIssuingFlag = new AcquiringIssuingFlag()
            .cardAcquiringIssuingFlagCode(DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE)
            .cardAcquiringIssuingDescription(DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION)
            .cardAcquiringIssuingDetails(DEFAULT_CARD_ACQUIRING_ISSUING_DETAILS);
        return acquiringIssuingFlag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcquiringIssuingFlag createUpdatedEntity(EntityManager em) {
        AcquiringIssuingFlag acquiringIssuingFlag = new AcquiringIssuingFlag()
            .cardAcquiringIssuingFlagCode(UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE)
            .cardAcquiringIssuingDescription(UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION)
            .cardAcquiringIssuingDetails(UPDATED_CARD_ACQUIRING_ISSUING_DETAILS);
        return acquiringIssuingFlag;
    }

    @BeforeEach
    public void initTest() {
        acquiringIssuingFlag = createEntity(em);
    }

    @Test
    @Transactional
    void createAcquiringIssuingFlag() throws Exception {
        int databaseSizeBeforeCreate = acquiringIssuingFlagRepository.findAll().size();
        // Create the AcquiringIssuingFlag
        AcquiringIssuingFlagDTO acquiringIssuingFlagDTO = acquiringIssuingFlagMapper.toDto(acquiringIssuingFlag);
        restAcquiringIssuingFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acquiringIssuingFlagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AcquiringIssuingFlag in the database
        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeCreate + 1);
        AcquiringIssuingFlag testAcquiringIssuingFlag = acquiringIssuingFlagList.get(acquiringIssuingFlagList.size() - 1);
        assertThat(testAcquiringIssuingFlag.getCardAcquiringIssuingFlagCode()).isEqualTo(DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE);
        assertThat(testAcquiringIssuingFlag.getCardAcquiringIssuingDescription()).isEqualTo(DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION);
        assertThat(testAcquiringIssuingFlag.getCardAcquiringIssuingDetails()).isEqualTo(DEFAULT_CARD_ACQUIRING_ISSUING_DETAILS);

        // Validate the AcquiringIssuingFlag in Elasticsearch
        verify(mockAcquiringIssuingFlagSearchRepository, times(1)).save(testAcquiringIssuingFlag);
    }

    @Test
    @Transactional
    void createAcquiringIssuingFlagWithExistingId() throws Exception {
        // Create the AcquiringIssuingFlag with an existing ID
        acquiringIssuingFlag.setId(1L);
        AcquiringIssuingFlagDTO acquiringIssuingFlagDTO = acquiringIssuingFlagMapper.toDto(acquiringIssuingFlag);

        int databaseSizeBeforeCreate = acquiringIssuingFlagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcquiringIssuingFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acquiringIssuingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcquiringIssuingFlag in the database
        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeCreate);

        // Validate the AcquiringIssuingFlag in Elasticsearch
        verify(mockAcquiringIssuingFlagSearchRepository, times(0)).save(acquiringIssuingFlag);
    }

    @Test
    @Transactional
    void checkCardAcquiringIssuingFlagCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = acquiringIssuingFlagRepository.findAll().size();
        // set the field null
        acquiringIssuingFlag.setCardAcquiringIssuingFlagCode(null);

        // Create the AcquiringIssuingFlag, which fails.
        AcquiringIssuingFlagDTO acquiringIssuingFlagDTO = acquiringIssuingFlagMapper.toDto(acquiringIssuingFlag);

        restAcquiringIssuingFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acquiringIssuingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardAcquiringIssuingDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = acquiringIssuingFlagRepository.findAll().size();
        // set the field null
        acquiringIssuingFlag.setCardAcquiringIssuingDescription(null);

        // Create the AcquiringIssuingFlag, which fails.
        AcquiringIssuingFlagDTO acquiringIssuingFlagDTO = acquiringIssuingFlagMapper.toDto(acquiringIssuingFlag);

        restAcquiringIssuingFlagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acquiringIssuingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlags() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList
        restAcquiringIssuingFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acquiringIssuingFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardAcquiringIssuingFlagCode").value(hasItem(DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE)))
            .andExpect(jsonPath("$.[*].cardAcquiringIssuingDescription").value(hasItem(DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cardAcquiringIssuingDetails").value(hasItem(DEFAULT_CARD_ACQUIRING_ISSUING_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getAcquiringIssuingFlag() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get the acquiringIssuingFlag
        restAcquiringIssuingFlagMockMvc
            .perform(get(ENTITY_API_URL_ID, acquiringIssuingFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(acquiringIssuingFlag.getId().intValue()))
            .andExpect(jsonPath("$.cardAcquiringIssuingFlagCode").value(DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE))
            .andExpect(jsonPath("$.cardAcquiringIssuingDescription").value(DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION))
            .andExpect(jsonPath("$.cardAcquiringIssuingDetails").value(DEFAULT_CARD_ACQUIRING_ISSUING_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getAcquiringIssuingFlagsByIdFiltering() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        Long id = acquiringIssuingFlag.getId();

        defaultAcquiringIssuingFlagShouldBeFound("id.equals=" + id);
        defaultAcquiringIssuingFlagShouldNotBeFound("id.notEquals=" + id);

        defaultAcquiringIssuingFlagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAcquiringIssuingFlagShouldNotBeFound("id.greaterThan=" + id);

        defaultAcquiringIssuingFlagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAcquiringIssuingFlagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlagsByCardAcquiringIssuingFlagCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingFlagCode equals to DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE
        defaultAcquiringIssuingFlagShouldBeFound("cardAcquiringIssuingFlagCode.equals=" + DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingFlagCode equals to UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE
        defaultAcquiringIssuingFlagShouldNotBeFound("cardAcquiringIssuingFlagCode.equals=" + UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE);
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlagsByCardAcquiringIssuingFlagCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingFlagCode not equals to DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE
        defaultAcquiringIssuingFlagShouldNotBeFound("cardAcquiringIssuingFlagCode.notEquals=" + DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingFlagCode not equals to UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE
        defaultAcquiringIssuingFlagShouldBeFound("cardAcquiringIssuingFlagCode.notEquals=" + UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE);
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlagsByCardAcquiringIssuingFlagCodeIsInShouldWork() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingFlagCode in DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE or UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE
        defaultAcquiringIssuingFlagShouldBeFound(
            "cardAcquiringIssuingFlagCode.in=" + DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE + "," + UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE
        );

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingFlagCode equals to UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE
        defaultAcquiringIssuingFlagShouldNotBeFound("cardAcquiringIssuingFlagCode.in=" + UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE);
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlagsByCardAcquiringIssuingFlagCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingFlagCode is not null
        defaultAcquiringIssuingFlagShouldBeFound("cardAcquiringIssuingFlagCode.specified=true");

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingFlagCode is null
        defaultAcquiringIssuingFlagShouldNotBeFound("cardAcquiringIssuingFlagCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlagsByCardAcquiringIssuingFlagCodeContainsSomething() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingFlagCode contains DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE
        defaultAcquiringIssuingFlagShouldBeFound("cardAcquiringIssuingFlagCode.contains=" + DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingFlagCode contains UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE
        defaultAcquiringIssuingFlagShouldNotBeFound("cardAcquiringIssuingFlagCode.contains=" + UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE);
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlagsByCardAcquiringIssuingFlagCodeNotContainsSomething() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingFlagCode does not contain DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE
        defaultAcquiringIssuingFlagShouldNotBeFound(
            "cardAcquiringIssuingFlagCode.doesNotContain=" + DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE
        );

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingFlagCode does not contain UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE
        defaultAcquiringIssuingFlagShouldBeFound("cardAcquiringIssuingFlagCode.doesNotContain=" + UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE);
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlagsByCardAcquiringIssuingDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingDescription equals to DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION
        defaultAcquiringIssuingFlagShouldBeFound("cardAcquiringIssuingDescription.equals=" + DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingDescription equals to UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION
        defaultAcquiringIssuingFlagShouldNotBeFound("cardAcquiringIssuingDescription.equals=" + UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlagsByCardAcquiringIssuingDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingDescription not equals to DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION
        defaultAcquiringIssuingFlagShouldNotBeFound(
            "cardAcquiringIssuingDescription.notEquals=" + DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION
        );

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingDescription not equals to UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION
        defaultAcquiringIssuingFlagShouldBeFound("cardAcquiringIssuingDescription.notEquals=" + UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlagsByCardAcquiringIssuingDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingDescription in DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION or UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION
        defaultAcquiringIssuingFlagShouldBeFound(
            "cardAcquiringIssuingDescription.in=" +
            DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION +
            "," +
            UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION
        );

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingDescription equals to UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION
        defaultAcquiringIssuingFlagShouldNotBeFound("cardAcquiringIssuingDescription.in=" + UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlagsByCardAcquiringIssuingDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingDescription is not null
        defaultAcquiringIssuingFlagShouldBeFound("cardAcquiringIssuingDescription.specified=true");

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingDescription is null
        defaultAcquiringIssuingFlagShouldNotBeFound("cardAcquiringIssuingDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlagsByCardAcquiringIssuingDescriptionContainsSomething() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingDescription contains DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION
        defaultAcquiringIssuingFlagShouldBeFound("cardAcquiringIssuingDescription.contains=" + DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingDescription contains UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION
        defaultAcquiringIssuingFlagShouldNotBeFound(
            "cardAcquiringIssuingDescription.contains=" + UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAcquiringIssuingFlagsByCardAcquiringIssuingDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingDescription does not contain DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION
        defaultAcquiringIssuingFlagShouldNotBeFound(
            "cardAcquiringIssuingDescription.doesNotContain=" + DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION
        );

        // Get all the acquiringIssuingFlagList where cardAcquiringIssuingDescription does not contain UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION
        defaultAcquiringIssuingFlagShouldBeFound(
            "cardAcquiringIssuingDescription.doesNotContain=" + UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAcquiringIssuingFlagShouldBeFound(String filter) throws Exception {
        restAcquiringIssuingFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acquiringIssuingFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardAcquiringIssuingFlagCode").value(hasItem(DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE)))
            .andExpect(jsonPath("$.[*].cardAcquiringIssuingDescription").value(hasItem(DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cardAcquiringIssuingDetails").value(hasItem(DEFAULT_CARD_ACQUIRING_ISSUING_DETAILS.toString())));

        // Check, that the count call also returns 1
        restAcquiringIssuingFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAcquiringIssuingFlagShouldNotBeFound(String filter) throws Exception {
        restAcquiringIssuingFlagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAcquiringIssuingFlagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAcquiringIssuingFlag() throws Exception {
        // Get the acquiringIssuingFlag
        restAcquiringIssuingFlagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAcquiringIssuingFlag() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        int databaseSizeBeforeUpdate = acquiringIssuingFlagRepository.findAll().size();

        // Update the acquiringIssuingFlag
        AcquiringIssuingFlag updatedAcquiringIssuingFlag = acquiringIssuingFlagRepository.findById(acquiringIssuingFlag.getId()).get();
        // Disconnect from session so that the updates on updatedAcquiringIssuingFlag are not directly saved in db
        em.detach(updatedAcquiringIssuingFlag);
        updatedAcquiringIssuingFlag
            .cardAcquiringIssuingFlagCode(UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE)
            .cardAcquiringIssuingDescription(UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION)
            .cardAcquiringIssuingDetails(UPDATED_CARD_ACQUIRING_ISSUING_DETAILS);
        AcquiringIssuingFlagDTO acquiringIssuingFlagDTO = acquiringIssuingFlagMapper.toDto(updatedAcquiringIssuingFlag);

        restAcquiringIssuingFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acquiringIssuingFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acquiringIssuingFlagDTO))
            )
            .andExpect(status().isOk());

        // Validate the AcquiringIssuingFlag in the database
        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeUpdate);
        AcquiringIssuingFlag testAcquiringIssuingFlag = acquiringIssuingFlagList.get(acquiringIssuingFlagList.size() - 1);
        assertThat(testAcquiringIssuingFlag.getCardAcquiringIssuingFlagCode()).isEqualTo(UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE);
        assertThat(testAcquiringIssuingFlag.getCardAcquiringIssuingDescription()).isEqualTo(UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION);
        assertThat(testAcquiringIssuingFlag.getCardAcquiringIssuingDetails()).isEqualTo(UPDATED_CARD_ACQUIRING_ISSUING_DETAILS);

        // Validate the AcquiringIssuingFlag in Elasticsearch
        verify(mockAcquiringIssuingFlagSearchRepository).save(testAcquiringIssuingFlag);
    }

    @Test
    @Transactional
    void putNonExistingAcquiringIssuingFlag() throws Exception {
        int databaseSizeBeforeUpdate = acquiringIssuingFlagRepository.findAll().size();
        acquiringIssuingFlag.setId(count.incrementAndGet());

        // Create the AcquiringIssuingFlag
        AcquiringIssuingFlagDTO acquiringIssuingFlagDTO = acquiringIssuingFlagMapper.toDto(acquiringIssuingFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcquiringIssuingFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acquiringIssuingFlagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acquiringIssuingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcquiringIssuingFlag in the database
        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcquiringIssuingFlag in Elasticsearch
        verify(mockAcquiringIssuingFlagSearchRepository, times(0)).save(acquiringIssuingFlag);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcquiringIssuingFlag() throws Exception {
        int databaseSizeBeforeUpdate = acquiringIssuingFlagRepository.findAll().size();
        acquiringIssuingFlag.setId(count.incrementAndGet());

        // Create the AcquiringIssuingFlag
        AcquiringIssuingFlagDTO acquiringIssuingFlagDTO = acquiringIssuingFlagMapper.toDto(acquiringIssuingFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcquiringIssuingFlagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acquiringIssuingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcquiringIssuingFlag in the database
        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcquiringIssuingFlag in Elasticsearch
        verify(mockAcquiringIssuingFlagSearchRepository, times(0)).save(acquiringIssuingFlag);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcquiringIssuingFlag() throws Exception {
        int databaseSizeBeforeUpdate = acquiringIssuingFlagRepository.findAll().size();
        acquiringIssuingFlag.setId(count.incrementAndGet());

        // Create the AcquiringIssuingFlag
        AcquiringIssuingFlagDTO acquiringIssuingFlagDTO = acquiringIssuingFlagMapper.toDto(acquiringIssuingFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcquiringIssuingFlagMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acquiringIssuingFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcquiringIssuingFlag in the database
        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcquiringIssuingFlag in Elasticsearch
        verify(mockAcquiringIssuingFlagSearchRepository, times(0)).save(acquiringIssuingFlag);
    }

    @Test
    @Transactional
    void partialUpdateAcquiringIssuingFlagWithPatch() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        int databaseSizeBeforeUpdate = acquiringIssuingFlagRepository.findAll().size();

        // Update the acquiringIssuingFlag using partial update
        AcquiringIssuingFlag partialUpdatedAcquiringIssuingFlag = new AcquiringIssuingFlag();
        partialUpdatedAcquiringIssuingFlag.setId(acquiringIssuingFlag.getId());

        restAcquiringIssuingFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcquiringIssuingFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcquiringIssuingFlag))
            )
            .andExpect(status().isOk());

        // Validate the AcquiringIssuingFlag in the database
        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeUpdate);
        AcquiringIssuingFlag testAcquiringIssuingFlag = acquiringIssuingFlagList.get(acquiringIssuingFlagList.size() - 1);
        assertThat(testAcquiringIssuingFlag.getCardAcquiringIssuingFlagCode()).isEqualTo(DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE);
        assertThat(testAcquiringIssuingFlag.getCardAcquiringIssuingDescription()).isEqualTo(DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION);
        assertThat(testAcquiringIssuingFlag.getCardAcquiringIssuingDetails()).isEqualTo(DEFAULT_CARD_ACQUIRING_ISSUING_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateAcquiringIssuingFlagWithPatch() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        int databaseSizeBeforeUpdate = acquiringIssuingFlagRepository.findAll().size();

        // Update the acquiringIssuingFlag using partial update
        AcquiringIssuingFlag partialUpdatedAcquiringIssuingFlag = new AcquiringIssuingFlag();
        partialUpdatedAcquiringIssuingFlag.setId(acquiringIssuingFlag.getId());

        partialUpdatedAcquiringIssuingFlag
            .cardAcquiringIssuingFlagCode(UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE)
            .cardAcquiringIssuingDescription(UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION)
            .cardAcquiringIssuingDetails(UPDATED_CARD_ACQUIRING_ISSUING_DETAILS);

        restAcquiringIssuingFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcquiringIssuingFlag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcquiringIssuingFlag))
            )
            .andExpect(status().isOk());

        // Validate the AcquiringIssuingFlag in the database
        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeUpdate);
        AcquiringIssuingFlag testAcquiringIssuingFlag = acquiringIssuingFlagList.get(acquiringIssuingFlagList.size() - 1);
        assertThat(testAcquiringIssuingFlag.getCardAcquiringIssuingFlagCode()).isEqualTo(UPDATED_CARD_ACQUIRING_ISSUING_FLAG_CODE);
        assertThat(testAcquiringIssuingFlag.getCardAcquiringIssuingDescription()).isEqualTo(UPDATED_CARD_ACQUIRING_ISSUING_DESCRIPTION);
        assertThat(testAcquiringIssuingFlag.getCardAcquiringIssuingDetails()).isEqualTo(UPDATED_CARD_ACQUIRING_ISSUING_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingAcquiringIssuingFlag() throws Exception {
        int databaseSizeBeforeUpdate = acquiringIssuingFlagRepository.findAll().size();
        acquiringIssuingFlag.setId(count.incrementAndGet());

        // Create the AcquiringIssuingFlag
        AcquiringIssuingFlagDTO acquiringIssuingFlagDTO = acquiringIssuingFlagMapper.toDto(acquiringIssuingFlag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcquiringIssuingFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, acquiringIssuingFlagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acquiringIssuingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcquiringIssuingFlag in the database
        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcquiringIssuingFlag in Elasticsearch
        verify(mockAcquiringIssuingFlagSearchRepository, times(0)).save(acquiringIssuingFlag);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcquiringIssuingFlag() throws Exception {
        int databaseSizeBeforeUpdate = acquiringIssuingFlagRepository.findAll().size();
        acquiringIssuingFlag.setId(count.incrementAndGet());

        // Create the AcquiringIssuingFlag
        AcquiringIssuingFlagDTO acquiringIssuingFlagDTO = acquiringIssuingFlagMapper.toDto(acquiringIssuingFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcquiringIssuingFlagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acquiringIssuingFlagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcquiringIssuingFlag in the database
        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcquiringIssuingFlag in Elasticsearch
        verify(mockAcquiringIssuingFlagSearchRepository, times(0)).save(acquiringIssuingFlag);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcquiringIssuingFlag() throws Exception {
        int databaseSizeBeforeUpdate = acquiringIssuingFlagRepository.findAll().size();
        acquiringIssuingFlag.setId(count.incrementAndGet());

        // Create the AcquiringIssuingFlag
        AcquiringIssuingFlagDTO acquiringIssuingFlagDTO = acquiringIssuingFlagMapper.toDto(acquiringIssuingFlag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcquiringIssuingFlagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acquiringIssuingFlagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcquiringIssuingFlag in the database
        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcquiringIssuingFlag in Elasticsearch
        verify(mockAcquiringIssuingFlagSearchRepository, times(0)).save(acquiringIssuingFlag);
    }

    @Test
    @Transactional
    void deleteAcquiringIssuingFlag() throws Exception {
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);

        int databaseSizeBeforeDelete = acquiringIssuingFlagRepository.findAll().size();

        // Delete the acquiringIssuingFlag
        restAcquiringIssuingFlagMockMvc
            .perform(delete(ENTITY_API_URL_ID, acquiringIssuingFlag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AcquiringIssuingFlag> acquiringIssuingFlagList = acquiringIssuingFlagRepository.findAll();
        assertThat(acquiringIssuingFlagList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AcquiringIssuingFlag in Elasticsearch
        verify(mockAcquiringIssuingFlagSearchRepository, times(1)).deleteById(acquiringIssuingFlag.getId());
    }

    @Test
    @Transactional
    void searchAcquiringIssuingFlag() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        acquiringIssuingFlagRepository.saveAndFlush(acquiringIssuingFlag);
        when(mockAcquiringIssuingFlagSearchRepository.search("id:" + acquiringIssuingFlag.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(acquiringIssuingFlag), PageRequest.of(0, 1), 1));

        // Search the acquiringIssuingFlag
        restAcquiringIssuingFlagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + acquiringIssuingFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acquiringIssuingFlag.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardAcquiringIssuingFlagCode").value(hasItem(DEFAULT_CARD_ACQUIRING_ISSUING_FLAG_CODE)))
            .andExpect(jsonPath("$.[*].cardAcquiringIssuingDescription").value(hasItem(DEFAULT_CARD_ACQUIRING_ISSUING_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cardAcquiringIssuingDetails").value(hasItem(DEFAULT_CARD_ACQUIRING_ISSUING_DETAILS.toString())));
    }
}
