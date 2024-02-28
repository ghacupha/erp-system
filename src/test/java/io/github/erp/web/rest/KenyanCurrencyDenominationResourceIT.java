package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.domain.KenyanCurrencyDenomination;
import io.github.erp.repository.KenyanCurrencyDenominationRepository;
import io.github.erp.repository.search.KenyanCurrencyDenominationSearchRepository;
import io.github.erp.service.criteria.KenyanCurrencyDenominationCriteria;
import io.github.erp.service.dto.KenyanCurrencyDenominationDTO;
import io.github.erp.service.mapper.KenyanCurrencyDenominationMapper;
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
 * Integration tests for the {@link KenyanCurrencyDenominationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class KenyanCurrencyDenominationResourceIT {

    private static final String DEFAULT_CURRENCY_DENOMINATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_DENOMINATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_DENOMINATION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_DENOMINATION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/kenyan-currency-denominations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/kenyan-currency-denominations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KenyanCurrencyDenominationRepository kenyanCurrencyDenominationRepository;

    @Autowired
    private KenyanCurrencyDenominationMapper kenyanCurrencyDenominationMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.KenyanCurrencyDenominationSearchRepositoryMockConfiguration
     */
    @Autowired
    private KenyanCurrencyDenominationSearchRepository mockKenyanCurrencyDenominationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKenyanCurrencyDenominationMockMvc;

    private KenyanCurrencyDenomination kenyanCurrencyDenomination;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KenyanCurrencyDenomination createEntity(EntityManager em) {
        KenyanCurrencyDenomination kenyanCurrencyDenomination = new KenyanCurrencyDenomination()
            .currencyDenominationCode(DEFAULT_CURRENCY_DENOMINATION_CODE)
            .currencyDenominationType(DEFAULT_CURRENCY_DENOMINATION_TYPE)
            .currencyDenominationTypeDetails(DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS);
        return kenyanCurrencyDenomination;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KenyanCurrencyDenomination createUpdatedEntity(EntityManager em) {
        KenyanCurrencyDenomination kenyanCurrencyDenomination = new KenyanCurrencyDenomination()
            .currencyDenominationCode(UPDATED_CURRENCY_DENOMINATION_CODE)
            .currencyDenominationType(UPDATED_CURRENCY_DENOMINATION_TYPE)
            .currencyDenominationTypeDetails(UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS);
        return kenyanCurrencyDenomination;
    }

    @BeforeEach
    public void initTest() {
        kenyanCurrencyDenomination = createEntity(em);
    }

    @Test
    @Transactional
    void createKenyanCurrencyDenomination() throws Exception {
        int databaseSizeBeforeCreate = kenyanCurrencyDenominationRepository.findAll().size();
        // Create the KenyanCurrencyDenomination
        KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO = kenyanCurrencyDenominationMapper.toDto(kenyanCurrencyDenomination);
        restKenyanCurrencyDenominationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kenyanCurrencyDenominationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the KenyanCurrencyDenomination in the database
        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeCreate + 1);
        KenyanCurrencyDenomination testKenyanCurrencyDenomination = kenyanCurrencyDenominationList.get(
            kenyanCurrencyDenominationList.size() - 1
        );
        assertThat(testKenyanCurrencyDenomination.getCurrencyDenominationCode()).isEqualTo(DEFAULT_CURRENCY_DENOMINATION_CODE);
        assertThat(testKenyanCurrencyDenomination.getCurrencyDenominationType()).isEqualTo(DEFAULT_CURRENCY_DENOMINATION_TYPE);
        assertThat(testKenyanCurrencyDenomination.getCurrencyDenominationTypeDetails())
            .isEqualTo(DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS);

        // Validate the KenyanCurrencyDenomination in Elasticsearch
        verify(mockKenyanCurrencyDenominationSearchRepository, times(1)).save(testKenyanCurrencyDenomination);
    }

    @Test
    @Transactional
    void createKenyanCurrencyDenominationWithExistingId() throws Exception {
        // Create the KenyanCurrencyDenomination with an existing ID
        kenyanCurrencyDenomination.setId(1L);
        KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO = kenyanCurrencyDenominationMapper.toDto(kenyanCurrencyDenomination);

        int databaseSizeBeforeCreate = kenyanCurrencyDenominationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKenyanCurrencyDenominationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kenyanCurrencyDenominationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KenyanCurrencyDenomination in the database
        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeCreate);

        // Validate the KenyanCurrencyDenomination in Elasticsearch
        verify(mockKenyanCurrencyDenominationSearchRepository, times(0)).save(kenyanCurrencyDenomination);
    }

    @Test
    @Transactional
    void checkCurrencyDenominationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = kenyanCurrencyDenominationRepository.findAll().size();
        // set the field null
        kenyanCurrencyDenomination.setCurrencyDenominationCode(null);

        // Create the KenyanCurrencyDenomination, which fails.
        KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO = kenyanCurrencyDenominationMapper.toDto(kenyanCurrencyDenomination);

        restKenyanCurrencyDenominationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kenyanCurrencyDenominationDTO))
            )
            .andExpect(status().isBadRequest());

        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyDenominationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = kenyanCurrencyDenominationRepository.findAll().size();
        // set the field null
        kenyanCurrencyDenomination.setCurrencyDenominationType(null);

        // Create the KenyanCurrencyDenomination, which fails.
        KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO = kenyanCurrencyDenominationMapper.toDto(kenyanCurrencyDenomination);

        restKenyanCurrencyDenominationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kenyanCurrencyDenominationDTO))
            )
            .andExpect(status().isBadRequest());

        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominations() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList
        restKenyanCurrencyDenominationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kenyanCurrencyDenomination.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyDenominationCode").value(hasItem(DEFAULT_CURRENCY_DENOMINATION_CODE)))
            .andExpect(jsonPath("$.[*].currencyDenominationType").value(hasItem(DEFAULT_CURRENCY_DENOMINATION_TYPE)))
            .andExpect(jsonPath("$.[*].currencyDenominationTypeDetails").value(hasItem(DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS)));
    }

    @Test
    @Transactional
    void getKenyanCurrencyDenomination() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get the kenyanCurrencyDenomination
        restKenyanCurrencyDenominationMockMvc
            .perform(get(ENTITY_API_URL_ID, kenyanCurrencyDenomination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kenyanCurrencyDenomination.getId().intValue()))
            .andExpect(jsonPath("$.currencyDenominationCode").value(DEFAULT_CURRENCY_DENOMINATION_CODE))
            .andExpect(jsonPath("$.currencyDenominationType").value(DEFAULT_CURRENCY_DENOMINATION_TYPE))
            .andExpect(jsonPath("$.currencyDenominationTypeDetails").value(DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS));
    }

    @Test
    @Transactional
    void getKenyanCurrencyDenominationsByIdFiltering() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        Long id = kenyanCurrencyDenomination.getId();

        defaultKenyanCurrencyDenominationShouldBeFound("id.equals=" + id);
        defaultKenyanCurrencyDenominationShouldNotBeFound("id.notEquals=" + id);

        defaultKenyanCurrencyDenominationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultKenyanCurrencyDenominationShouldNotBeFound("id.greaterThan=" + id);

        defaultKenyanCurrencyDenominationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultKenyanCurrencyDenominationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationCode equals to DEFAULT_CURRENCY_DENOMINATION_CODE
        defaultKenyanCurrencyDenominationShouldBeFound("currencyDenominationCode.equals=" + DEFAULT_CURRENCY_DENOMINATION_CODE);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationCode equals to UPDATED_CURRENCY_DENOMINATION_CODE
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationCode.equals=" + UPDATED_CURRENCY_DENOMINATION_CODE);
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationCode not equals to DEFAULT_CURRENCY_DENOMINATION_CODE
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationCode.notEquals=" + DEFAULT_CURRENCY_DENOMINATION_CODE);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationCode not equals to UPDATED_CURRENCY_DENOMINATION_CODE
        defaultKenyanCurrencyDenominationShouldBeFound("currencyDenominationCode.notEquals=" + UPDATED_CURRENCY_DENOMINATION_CODE);
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationCodeIsInShouldWork() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationCode in DEFAULT_CURRENCY_DENOMINATION_CODE or UPDATED_CURRENCY_DENOMINATION_CODE
        defaultKenyanCurrencyDenominationShouldBeFound(
            "currencyDenominationCode.in=" + DEFAULT_CURRENCY_DENOMINATION_CODE + "," + UPDATED_CURRENCY_DENOMINATION_CODE
        );

        // Get all the kenyanCurrencyDenominationList where currencyDenominationCode equals to UPDATED_CURRENCY_DENOMINATION_CODE
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationCode.in=" + UPDATED_CURRENCY_DENOMINATION_CODE);
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationCode is not null
        defaultKenyanCurrencyDenominationShouldBeFound("currencyDenominationCode.specified=true");

        // Get all the kenyanCurrencyDenominationList where currencyDenominationCode is null
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationCode.specified=false");
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationCodeContainsSomething() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationCode contains DEFAULT_CURRENCY_DENOMINATION_CODE
        defaultKenyanCurrencyDenominationShouldBeFound("currencyDenominationCode.contains=" + DEFAULT_CURRENCY_DENOMINATION_CODE);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationCode contains UPDATED_CURRENCY_DENOMINATION_CODE
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationCode.contains=" + UPDATED_CURRENCY_DENOMINATION_CODE);
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationCodeNotContainsSomething() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationCode does not contain DEFAULT_CURRENCY_DENOMINATION_CODE
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationCode.doesNotContain=" + DEFAULT_CURRENCY_DENOMINATION_CODE);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationCode does not contain UPDATED_CURRENCY_DENOMINATION_CODE
        defaultKenyanCurrencyDenominationShouldBeFound("currencyDenominationCode.doesNotContain=" + UPDATED_CURRENCY_DENOMINATION_CODE);
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationType equals to DEFAULT_CURRENCY_DENOMINATION_TYPE
        defaultKenyanCurrencyDenominationShouldBeFound("currencyDenominationType.equals=" + DEFAULT_CURRENCY_DENOMINATION_TYPE);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationType equals to UPDATED_CURRENCY_DENOMINATION_TYPE
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationType.equals=" + UPDATED_CURRENCY_DENOMINATION_TYPE);
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationType not equals to DEFAULT_CURRENCY_DENOMINATION_TYPE
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationType.notEquals=" + DEFAULT_CURRENCY_DENOMINATION_TYPE);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationType not equals to UPDATED_CURRENCY_DENOMINATION_TYPE
        defaultKenyanCurrencyDenominationShouldBeFound("currencyDenominationType.notEquals=" + UPDATED_CURRENCY_DENOMINATION_TYPE);
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationType in DEFAULT_CURRENCY_DENOMINATION_TYPE or UPDATED_CURRENCY_DENOMINATION_TYPE
        defaultKenyanCurrencyDenominationShouldBeFound(
            "currencyDenominationType.in=" + DEFAULT_CURRENCY_DENOMINATION_TYPE + "," + UPDATED_CURRENCY_DENOMINATION_TYPE
        );

        // Get all the kenyanCurrencyDenominationList where currencyDenominationType equals to UPDATED_CURRENCY_DENOMINATION_TYPE
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationType.in=" + UPDATED_CURRENCY_DENOMINATION_TYPE);
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationType is not null
        defaultKenyanCurrencyDenominationShouldBeFound("currencyDenominationType.specified=true");

        // Get all the kenyanCurrencyDenominationList where currencyDenominationType is null
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationType.specified=false");
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationTypeContainsSomething() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationType contains DEFAULT_CURRENCY_DENOMINATION_TYPE
        defaultKenyanCurrencyDenominationShouldBeFound("currencyDenominationType.contains=" + DEFAULT_CURRENCY_DENOMINATION_TYPE);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationType contains UPDATED_CURRENCY_DENOMINATION_TYPE
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationType.contains=" + UPDATED_CURRENCY_DENOMINATION_TYPE);
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationTypeNotContainsSomething() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationType does not contain DEFAULT_CURRENCY_DENOMINATION_TYPE
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationType.doesNotContain=" + DEFAULT_CURRENCY_DENOMINATION_TYPE);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationType does not contain UPDATED_CURRENCY_DENOMINATION_TYPE
        defaultKenyanCurrencyDenominationShouldBeFound("currencyDenominationType.doesNotContain=" + UPDATED_CURRENCY_DENOMINATION_TYPE);
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationTypeDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationTypeDetails equals to DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS
        defaultKenyanCurrencyDenominationShouldBeFound(
            "currencyDenominationTypeDetails.equals=" + DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS
        );

        // Get all the kenyanCurrencyDenominationList where currencyDenominationTypeDetails equals to UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS
        defaultKenyanCurrencyDenominationShouldNotBeFound(
            "currencyDenominationTypeDetails.equals=" + UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS
        );
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationTypeDetailsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationTypeDetails not equals to DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS
        defaultKenyanCurrencyDenominationShouldNotBeFound(
            "currencyDenominationTypeDetails.notEquals=" + DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS
        );

        // Get all the kenyanCurrencyDenominationList where currencyDenominationTypeDetails not equals to UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS
        defaultKenyanCurrencyDenominationShouldBeFound(
            "currencyDenominationTypeDetails.notEquals=" + UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS
        );
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationTypeDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationTypeDetails in DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS or UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS
        defaultKenyanCurrencyDenominationShouldBeFound(
            "currencyDenominationTypeDetails.in=" +
            DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS +
            "," +
            UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS
        );

        // Get all the kenyanCurrencyDenominationList where currencyDenominationTypeDetails equals to UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS
        defaultKenyanCurrencyDenominationShouldNotBeFound(
            "currencyDenominationTypeDetails.in=" + UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS
        );
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationTypeDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationTypeDetails is not null
        defaultKenyanCurrencyDenominationShouldBeFound("currencyDenominationTypeDetails.specified=true");

        // Get all the kenyanCurrencyDenominationList where currencyDenominationTypeDetails is null
        defaultKenyanCurrencyDenominationShouldNotBeFound("currencyDenominationTypeDetails.specified=false");
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationTypeDetailsContainsSomething() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationTypeDetails contains DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS
        defaultKenyanCurrencyDenominationShouldBeFound(
            "currencyDenominationTypeDetails.contains=" + DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS
        );

        // Get all the kenyanCurrencyDenominationList where currencyDenominationTypeDetails contains UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS
        defaultKenyanCurrencyDenominationShouldNotBeFound(
            "currencyDenominationTypeDetails.contains=" + UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS
        );
    }

    @Test
    @Transactional
    void getAllKenyanCurrencyDenominationsByCurrencyDenominationTypeDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        // Get all the kenyanCurrencyDenominationList where currencyDenominationTypeDetails does not contain DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS
        defaultKenyanCurrencyDenominationShouldNotBeFound(
            "currencyDenominationTypeDetails.doesNotContain=" + DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS
        );

        // Get all the kenyanCurrencyDenominationList where currencyDenominationTypeDetails does not contain UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS
        defaultKenyanCurrencyDenominationShouldBeFound(
            "currencyDenominationTypeDetails.doesNotContain=" + UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS
        );
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultKenyanCurrencyDenominationShouldBeFound(String filter) throws Exception {
        restKenyanCurrencyDenominationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kenyanCurrencyDenomination.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyDenominationCode").value(hasItem(DEFAULT_CURRENCY_DENOMINATION_CODE)))
            .andExpect(jsonPath("$.[*].currencyDenominationType").value(hasItem(DEFAULT_CURRENCY_DENOMINATION_TYPE)))
            .andExpect(jsonPath("$.[*].currencyDenominationTypeDetails").value(hasItem(DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS)));

        // Check, that the count call also returns 1
        restKenyanCurrencyDenominationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultKenyanCurrencyDenominationShouldNotBeFound(String filter) throws Exception {
        restKenyanCurrencyDenominationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restKenyanCurrencyDenominationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingKenyanCurrencyDenomination() throws Exception {
        // Get the kenyanCurrencyDenomination
        restKenyanCurrencyDenominationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewKenyanCurrencyDenomination() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        int databaseSizeBeforeUpdate = kenyanCurrencyDenominationRepository.findAll().size();

        // Update the kenyanCurrencyDenomination
        KenyanCurrencyDenomination updatedKenyanCurrencyDenomination = kenyanCurrencyDenominationRepository
            .findById(kenyanCurrencyDenomination.getId())
            .get();
        // Disconnect from session so that the updates on updatedKenyanCurrencyDenomination are not directly saved in db
        em.detach(updatedKenyanCurrencyDenomination);
        updatedKenyanCurrencyDenomination
            .currencyDenominationCode(UPDATED_CURRENCY_DENOMINATION_CODE)
            .currencyDenominationType(UPDATED_CURRENCY_DENOMINATION_TYPE)
            .currencyDenominationTypeDetails(UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS);
        KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO = kenyanCurrencyDenominationMapper.toDto(
            updatedKenyanCurrencyDenomination
        );

        restKenyanCurrencyDenominationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kenyanCurrencyDenominationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kenyanCurrencyDenominationDTO))
            )
            .andExpect(status().isOk());

        // Validate the KenyanCurrencyDenomination in the database
        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeUpdate);
        KenyanCurrencyDenomination testKenyanCurrencyDenomination = kenyanCurrencyDenominationList.get(
            kenyanCurrencyDenominationList.size() - 1
        );
        assertThat(testKenyanCurrencyDenomination.getCurrencyDenominationCode()).isEqualTo(UPDATED_CURRENCY_DENOMINATION_CODE);
        assertThat(testKenyanCurrencyDenomination.getCurrencyDenominationType()).isEqualTo(UPDATED_CURRENCY_DENOMINATION_TYPE);
        assertThat(testKenyanCurrencyDenomination.getCurrencyDenominationTypeDetails())
            .isEqualTo(UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS);

        // Validate the KenyanCurrencyDenomination in Elasticsearch
        verify(mockKenyanCurrencyDenominationSearchRepository).save(testKenyanCurrencyDenomination);
    }

    @Test
    @Transactional
    void putNonExistingKenyanCurrencyDenomination() throws Exception {
        int databaseSizeBeforeUpdate = kenyanCurrencyDenominationRepository.findAll().size();
        kenyanCurrencyDenomination.setId(count.incrementAndGet());

        // Create the KenyanCurrencyDenomination
        KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO = kenyanCurrencyDenominationMapper.toDto(kenyanCurrencyDenomination);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKenyanCurrencyDenominationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kenyanCurrencyDenominationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kenyanCurrencyDenominationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KenyanCurrencyDenomination in the database
        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the KenyanCurrencyDenomination in Elasticsearch
        verify(mockKenyanCurrencyDenominationSearchRepository, times(0)).save(kenyanCurrencyDenomination);
    }

    @Test
    @Transactional
    void putWithIdMismatchKenyanCurrencyDenomination() throws Exception {
        int databaseSizeBeforeUpdate = kenyanCurrencyDenominationRepository.findAll().size();
        kenyanCurrencyDenomination.setId(count.incrementAndGet());

        // Create the KenyanCurrencyDenomination
        KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO = kenyanCurrencyDenominationMapper.toDto(kenyanCurrencyDenomination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKenyanCurrencyDenominationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kenyanCurrencyDenominationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KenyanCurrencyDenomination in the database
        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the KenyanCurrencyDenomination in Elasticsearch
        verify(mockKenyanCurrencyDenominationSearchRepository, times(0)).save(kenyanCurrencyDenomination);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKenyanCurrencyDenomination() throws Exception {
        int databaseSizeBeforeUpdate = kenyanCurrencyDenominationRepository.findAll().size();
        kenyanCurrencyDenomination.setId(count.incrementAndGet());

        // Create the KenyanCurrencyDenomination
        KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO = kenyanCurrencyDenominationMapper.toDto(kenyanCurrencyDenomination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKenyanCurrencyDenominationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kenyanCurrencyDenominationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the KenyanCurrencyDenomination in the database
        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the KenyanCurrencyDenomination in Elasticsearch
        verify(mockKenyanCurrencyDenominationSearchRepository, times(0)).save(kenyanCurrencyDenomination);
    }

    @Test
    @Transactional
    void partialUpdateKenyanCurrencyDenominationWithPatch() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        int databaseSizeBeforeUpdate = kenyanCurrencyDenominationRepository.findAll().size();

        // Update the kenyanCurrencyDenomination using partial update
        KenyanCurrencyDenomination partialUpdatedKenyanCurrencyDenomination = new KenyanCurrencyDenomination();
        partialUpdatedKenyanCurrencyDenomination.setId(kenyanCurrencyDenomination.getId());

        partialUpdatedKenyanCurrencyDenomination
            .currencyDenominationCode(UPDATED_CURRENCY_DENOMINATION_CODE)
            .currencyDenominationTypeDetails(UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS);

        restKenyanCurrencyDenominationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKenyanCurrencyDenomination.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKenyanCurrencyDenomination))
            )
            .andExpect(status().isOk());

        // Validate the KenyanCurrencyDenomination in the database
        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeUpdate);
        KenyanCurrencyDenomination testKenyanCurrencyDenomination = kenyanCurrencyDenominationList.get(
            kenyanCurrencyDenominationList.size() - 1
        );
        assertThat(testKenyanCurrencyDenomination.getCurrencyDenominationCode()).isEqualTo(UPDATED_CURRENCY_DENOMINATION_CODE);
        assertThat(testKenyanCurrencyDenomination.getCurrencyDenominationType()).isEqualTo(DEFAULT_CURRENCY_DENOMINATION_TYPE);
        assertThat(testKenyanCurrencyDenomination.getCurrencyDenominationTypeDetails())
            .isEqualTo(UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateKenyanCurrencyDenominationWithPatch() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        int databaseSizeBeforeUpdate = kenyanCurrencyDenominationRepository.findAll().size();

        // Update the kenyanCurrencyDenomination using partial update
        KenyanCurrencyDenomination partialUpdatedKenyanCurrencyDenomination = new KenyanCurrencyDenomination();
        partialUpdatedKenyanCurrencyDenomination.setId(kenyanCurrencyDenomination.getId());

        partialUpdatedKenyanCurrencyDenomination
            .currencyDenominationCode(UPDATED_CURRENCY_DENOMINATION_CODE)
            .currencyDenominationType(UPDATED_CURRENCY_DENOMINATION_TYPE)
            .currencyDenominationTypeDetails(UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS);

        restKenyanCurrencyDenominationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKenyanCurrencyDenomination.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKenyanCurrencyDenomination))
            )
            .andExpect(status().isOk());

        // Validate the KenyanCurrencyDenomination in the database
        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeUpdate);
        KenyanCurrencyDenomination testKenyanCurrencyDenomination = kenyanCurrencyDenominationList.get(
            kenyanCurrencyDenominationList.size() - 1
        );
        assertThat(testKenyanCurrencyDenomination.getCurrencyDenominationCode()).isEqualTo(UPDATED_CURRENCY_DENOMINATION_CODE);
        assertThat(testKenyanCurrencyDenomination.getCurrencyDenominationType()).isEqualTo(UPDATED_CURRENCY_DENOMINATION_TYPE);
        assertThat(testKenyanCurrencyDenomination.getCurrencyDenominationTypeDetails())
            .isEqualTo(UPDATED_CURRENCY_DENOMINATION_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingKenyanCurrencyDenomination() throws Exception {
        int databaseSizeBeforeUpdate = kenyanCurrencyDenominationRepository.findAll().size();
        kenyanCurrencyDenomination.setId(count.incrementAndGet());

        // Create the KenyanCurrencyDenomination
        KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO = kenyanCurrencyDenominationMapper.toDto(kenyanCurrencyDenomination);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKenyanCurrencyDenominationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kenyanCurrencyDenominationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kenyanCurrencyDenominationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KenyanCurrencyDenomination in the database
        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the KenyanCurrencyDenomination in Elasticsearch
        verify(mockKenyanCurrencyDenominationSearchRepository, times(0)).save(kenyanCurrencyDenomination);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKenyanCurrencyDenomination() throws Exception {
        int databaseSizeBeforeUpdate = kenyanCurrencyDenominationRepository.findAll().size();
        kenyanCurrencyDenomination.setId(count.incrementAndGet());

        // Create the KenyanCurrencyDenomination
        KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO = kenyanCurrencyDenominationMapper.toDto(kenyanCurrencyDenomination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKenyanCurrencyDenominationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kenyanCurrencyDenominationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KenyanCurrencyDenomination in the database
        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the KenyanCurrencyDenomination in Elasticsearch
        verify(mockKenyanCurrencyDenominationSearchRepository, times(0)).save(kenyanCurrencyDenomination);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKenyanCurrencyDenomination() throws Exception {
        int databaseSizeBeforeUpdate = kenyanCurrencyDenominationRepository.findAll().size();
        kenyanCurrencyDenomination.setId(count.incrementAndGet());

        // Create the KenyanCurrencyDenomination
        KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO = kenyanCurrencyDenominationMapper.toDto(kenyanCurrencyDenomination);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKenyanCurrencyDenominationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kenyanCurrencyDenominationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the KenyanCurrencyDenomination in the database
        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the KenyanCurrencyDenomination in Elasticsearch
        verify(mockKenyanCurrencyDenominationSearchRepository, times(0)).save(kenyanCurrencyDenomination);
    }

    @Test
    @Transactional
    void deleteKenyanCurrencyDenomination() throws Exception {
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);

        int databaseSizeBeforeDelete = kenyanCurrencyDenominationRepository.findAll().size();

        // Delete the kenyanCurrencyDenomination
        restKenyanCurrencyDenominationMockMvc
            .perform(delete(ENTITY_API_URL_ID, kenyanCurrencyDenomination.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<KenyanCurrencyDenomination> kenyanCurrencyDenominationList = kenyanCurrencyDenominationRepository.findAll();
        assertThat(kenyanCurrencyDenominationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the KenyanCurrencyDenomination in Elasticsearch
        verify(mockKenyanCurrencyDenominationSearchRepository, times(1)).deleteById(kenyanCurrencyDenomination.getId());
    }

    @Test
    @Transactional
    void searchKenyanCurrencyDenomination() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        kenyanCurrencyDenominationRepository.saveAndFlush(kenyanCurrencyDenomination);
        when(mockKenyanCurrencyDenominationSearchRepository.search("id:" + kenyanCurrencyDenomination.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(kenyanCurrencyDenomination), PageRequest.of(0, 1), 1));

        // Search the kenyanCurrencyDenomination
        restKenyanCurrencyDenominationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + kenyanCurrencyDenomination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kenyanCurrencyDenomination.getId().intValue())))
            .andExpect(jsonPath("$.[*].currencyDenominationCode").value(hasItem(DEFAULT_CURRENCY_DENOMINATION_CODE)))
            .andExpect(jsonPath("$.[*].currencyDenominationType").value(hasItem(DEFAULT_CURRENCY_DENOMINATION_TYPE)))
            .andExpect(jsonPath("$.[*].currencyDenominationTypeDetails").value(hasItem(DEFAULT_CURRENCY_DENOMINATION_TYPE_DETAILS)));
    }
}
