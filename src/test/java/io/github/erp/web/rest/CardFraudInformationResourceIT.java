package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.CardFraudInformation;
import io.github.erp.repository.CardFraudInformationRepository;
import io.github.erp.repository.search.CardFraudInformationSearchRepository;
import io.github.erp.service.criteria.CardFraudInformationCriteria;
import io.github.erp.service.dto.CardFraudInformationDTO;
import io.github.erp.service.mapper.CardFraudInformationMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CardFraudInformationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CardFraudInformationResourceIT {

    private static final LocalDate DEFAULT_REPORTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REPORTING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REPORTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS = 0;
    private static final Integer UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS = 1;
    private static final Integer SMALLER_TOTAL_NUMBER_OF_FRAUD_INCIDENTS = 0 - 1;

    private static final Integer DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY = 0;
    private static final Integer UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY = 1;
    private static final Integer SMALLER_VALUE_OF_FRAUD_INCEDENTS_IN_LCY = 0 - 1;

    private static final String ENTITY_API_URL = "/api/card-fraud-informations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/card-fraud-informations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardFraudInformationRepository cardFraudInformationRepository;

    @Autowired
    private CardFraudInformationMapper cardFraudInformationMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardFraudInformationSearchRepositoryMockConfiguration
     */
    @Autowired
    private CardFraudInformationSearchRepository mockCardFraudInformationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardFraudInformationMockMvc;

    private CardFraudInformation cardFraudInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardFraudInformation createEntity(EntityManager em) {
        CardFraudInformation cardFraudInformation = new CardFraudInformation()
            .reportingDate(DEFAULT_REPORTING_DATE)
            .totalNumberOfFraudIncidents(DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS)
            .valueOfFraudIncedentsInLCY(DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);
        return cardFraudInformation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardFraudInformation createUpdatedEntity(EntityManager em) {
        CardFraudInformation cardFraudInformation = new CardFraudInformation()
            .reportingDate(UPDATED_REPORTING_DATE)
            .totalNumberOfFraudIncidents(UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS)
            .valueOfFraudIncedentsInLCY(UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);
        return cardFraudInformation;
    }

    @BeforeEach
    public void initTest() {
        cardFraudInformation = createEntity(em);
    }

    @Test
    @Transactional
    void createCardFraudInformation() throws Exception {
        int databaseSizeBeforeCreate = cardFraudInformationRepository.findAll().size();
        // Create the CardFraudInformation
        CardFraudInformationDTO cardFraudInformationDTO = cardFraudInformationMapper.toDto(cardFraudInformation);
        restCardFraudInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudInformationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CardFraudInformation in the database
        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeCreate + 1);
        CardFraudInformation testCardFraudInformation = cardFraudInformationList.get(cardFraudInformationList.size() - 1);
        assertThat(testCardFraudInformation.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testCardFraudInformation.getTotalNumberOfFraudIncidents()).isEqualTo(DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);
        assertThat(testCardFraudInformation.getValueOfFraudIncedentsInLCY()).isEqualTo(DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);

        // Validate the CardFraudInformation in Elasticsearch
        verify(mockCardFraudInformationSearchRepository, times(1)).save(testCardFraudInformation);
    }

    @Test
    @Transactional
    void createCardFraudInformationWithExistingId() throws Exception {
        // Create the CardFraudInformation with an existing ID
        cardFraudInformation.setId(1L);
        CardFraudInformationDTO cardFraudInformationDTO = cardFraudInformationMapper.toDto(cardFraudInformation);

        int databaseSizeBeforeCreate = cardFraudInformationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardFraudInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardFraudInformation in the database
        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardFraudInformation in Elasticsearch
        verify(mockCardFraudInformationSearchRepository, times(0)).save(cardFraudInformation);
    }

    @Test
    @Transactional
    void checkReportingDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardFraudInformationRepository.findAll().size();
        // set the field null
        cardFraudInformation.setReportingDate(null);

        // Create the CardFraudInformation, which fails.
        CardFraudInformationDTO cardFraudInformationDTO = cardFraudInformationMapper.toDto(cardFraudInformation);

        restCardFraudInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalNumberOfFraudIncidentsIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardFraudInformationRepository.findAll().size();
        // set the field null
        cardFraudInformation.setTotalNumberOfFraudIncidents(null);

        // Create the CardFraudInformation, which fails.
        CardFraudInformationDTO cardFraudInformationDTO = cardFraudInformationMapper.toDto(cardFraudInformation);

        restCardFraudInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueOfFraudIncedentsInLCYIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardFraudInformationRepository.findAll().size();
        // set the field null
        cardFraudInformation.setValueOfFraudIncedentsInLCY(null);

        // Create the CardFraudInformation, which fails.
        CardFraudInformationDTO cardFraudInformationDTO = cardFraudInformationMapper.toDto(cardFraudInformation);

        restCardFraudInformationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudInformationDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardFraudInformations() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList
        restCardFraudInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardFraudInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalNumberOfFraudIncidents").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS)))
            .andExpect(jsonPath("$.[*].valueOfFraudIncedentsInLCY").value(hasItem(DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY)));
    }

    @Test
    @Transactional
    void getCardFraudInformation() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get the cardFraudInformation
        restCardFraudInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, cardFraudInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardFraudInformation.getId().intValue()))
            .andExpect(jsonPath("$.reportingDate").value(DEFAULT_REPORTING_DATE.toString()))
            .andExpect(jsonPath("$.totalNumberOfFraudIncidents").value(DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS))
            .andExpect(jsonPath("$.valueOfFraudIncedentsInLCY").value(DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY));
    }

    @Test
    @Transactional
    void getCardFraudInformationsByIdFiltering() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        Long id = cardFraudInformation.getId();

        defaultCardFraudInformationShouldBeFound("id.equals=" + id);
        defaultCardFraudInformationShouldNotBeFound("id.notEquals=" + id);

        defaultCardFraudInformationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardFraudInformationShouldNotBeFound("id.greaterThan=" + id);

        defaultCardFraudInformationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardFraudInformationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByReportingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where reportingDate equals to DEFAULT_REPORTING_DATE
        defaultCardFraudInformationShouldBeFound("reportingDate.equals=" + DEFAULT_REPORTING_DATE);

        // Get all the cardFraudInformationList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultCardFraudInformationShouldNotBeFound("reportingDate.equals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByReportingDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where reportingDate not equals to DEFAULT_REPORTING_DATE
        defaultCardFraudInformationShouldNotBeFound("reportingDate.notEquals=" + DEFAULT_REPORTING_DATE);

        // Get all the cardFraudInformationList where reportingDate not equals to UPDATED_REPORTING_DATE
        defaultCardFraudInformationShouldBeFound("reportingDate.notEquals=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByReportingDateIsInShouldWork() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where reportingDate in DEFAULT_REPORTING_DATE or UPDATED_REPORTING_DATE
        defaultCardFraudInformationShouldBeFound("reportingDate.in=" + DEFAULT_REPORTING_DATE + "," + UPDATED_REPORTING_DATE);

        // Get all the cardFraudInformationList where reportingDate equals to UPDATED_REPORTING_DATE
        defaultCardFraudInformationShouldNotBeFound("reportingDate.in=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByReportingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where reportingDate is not null
        defaultCardFraudInformationShouldBeFound("reportingDate.specified=true");

        // Get all the cardFraudInformationList where reportingDate is null
        defaultCardFraudInformationShouldNotBeFound("reportingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByReportingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where reportingDate is greater than or equal to DEFAULT_REPORTING_DATE
        defaultCardFraudInformationShouldBeFound("reportingDate.greaterThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the cardFraudInformationList where reportingDate is greater than or equal to UPDATED_REPORTING_DATE
        defaultCardFraudInformationShouldNotBeFound("reportingDate.greaterThanOrEqual=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByReportingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where reportingDate is less than or equal to DEFAULT_REPORTING_DATE
        defaultCardFraudInformationShouldBeFound("reportingDate.lessThanOrEqual=" + DEFAULT_REPORTING_DATE);

        // Get all the cardFraudInformationList where reportingDate is less than or equal to SMALLER_REPORTING_DATE
        defaultCardFraudInformationShouldNotBeFound("reportingDate.lessThanOrEqual=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByReportingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where reportingDate is less than DEFAULT_REPORTING_DATE
        defaultCardFraudInformationShouldNotBeFound("reportingDate.lessThan=" + DEFAULT_REPORTING_DATE);

        // Get all the cardFraudInformationList where reportingDate is less than UPDATED_REPORTING_DATE
        defaultCardFraudInformationShouldBeFound("reportingDate.lessThan=" + UPDATED_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByReportingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where reportingDate is greater than DEFAULT_REPORTING_DATE
        defaultCardFraudInformationShouldNotBeFound("reportingDate.greaterThan=" + DEFAULT_REPORTING_DATE);

        // Get all the cardFraudInformationList where reportingDate is greater than SMALLER_REPORTING_DATE
        defaultCardFraudInformationShouldBeFound("reportingDate.greaterThan=" + SMALLER_REPORTING_DATE);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByTotalNumberOfFraudIncidentsIsEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents equals to DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldBeFound("totalNumberOfFraudIncidents.equals=" + DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents equals to UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldNotBeFound("totalNumberOfFraudIncidents.equals=" + UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByTotalNumberOfFraudIncidentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents not equals to DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldNotBeFound("totalNumberOfFraudIncidents.notEquals=" + DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents not equals to UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldBeFound("totalNumberOfFraudIncidents.notEquals=" + UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByTotalNumberOfFraudIncidentsIsInShouldWork() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents in DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS or UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldBeFound(
            "totalNumberOfFraudIncidents.in=" + DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS + "," + UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        );

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents equals to UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldNotBeFound("totalNumberOfFraudIncidents.in=" + UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByTotalNumberOfFraudIncidentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents is not null
        defaultCardFraudInformationShouldBeFound("totalNumberOfFraudIncidents.specified=true");

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents is null
        defaultCardFraudInformationShouldNotBeFound("totalNumberOfFraudIncidents.specified=false");
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByTotalNumberOfFraudIncidentsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents is greater than or equal to DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldBeFound(
            "totalNumberOfFraudIncidents.greaterThanOrEqual=" + DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        );

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents is greater than or equal to UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldNotBeFound(
            "totalNumberOfFraudIncidents.greaterThanOrEqual=" + UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        );
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByTotalNumberOfFraudIncidentsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents is less than or equal to DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldBeFound("totalNumberOfFraudIncidents.lessThanOrEqual=" + DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents is less than or equal to SMALLER_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldNotBeFound(
            "totalNumberOfFraudIncidents.lessThanOrEqual=" + SMALLER_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        );
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByTotalNumberOfFraudIncidentsIsLessThanSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents is less than DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldNotBeFound("totalNumberOfFraudIncidents.lessThan=" + DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents is less than UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldBeFound("totalNumberOfFraudIncidents.lessThan=" + UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByTotalNumberOfFraudIncidentsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents is greater than DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldNotBeFound("totalNumberOfFraudIncidents.greaterThan=" + DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);

        // Get all the cardFraudInformationList where totalNumberOfFraudIncidents is greater than SMALLER_TOTAL_NUMBER_OF_FRAUD_INCIDENTS
        defaultCardFraudInformationShouldBeFound("totalNumberOfFraudIncidents.greaterThan=" + SMALLER_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByValueOfFraudIncedentsInLCYIsEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY equals to DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldBeFound("valueOfFraudIncedentsInLCY.equals=" + DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY equals to UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldNotBeFound("valueOfFraudIncedentsInLCY.equals=" + UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByValueOfFraudIncedentsInLCYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY not equals to DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldNotBeFound("valueOfFraudIncedentsInLCY.notEquals=" + DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY not equals to UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldBeFound("valueOfFraudIncedentsInLCY.notEquals=" + UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByValueOfFraudIncedentsInLCYIsInShouldWork() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY in DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY or UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldBeFound(
            "valueOfFraudIncedentsInLCY.in=" + DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY + "," + UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        );

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY equals to UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldNotBeFound("valueOfFraudIncedentsInLCY.in=" + UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByValueOfFraudIncedentsInLCYIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY is not null
        defaultCardFraudInformationShouldBeFound("valueOfFraudIncedentsInLCY.specified=true");

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY is null
        defaultCardFraudInformationShouldNotBeFound("valueOfFraudIncedentsInLCY.specified=false");
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByValueOfFraudIncedentsInLCYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY is greater than or equal to DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldBeFound(
            "valueOfFraudIncedentsInLCY.greaterThanOrEqual=" + DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        );

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY is greater than or equal to UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldNotBeFound(
            "valueOfFraudIncedentsInLCY.greaterThanOrEqual=" + UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByValueOfFraudIncedentsInLCYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY is less than or equal to DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldBeFound("valueOfFraudIncedentsInLCY.lessThanOrEqual=" + DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY is less than or equal to SMALLER_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldNotBeFound(
            "valueOfFraudIncedentsInLCY.lessThanOrEqual=" + SMALLER_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        );
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByValueOfFraudIncedentsInLCYIsLessThanSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY is less than DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldNotBeFound("valueOfFraudIncedentsInLCY.lessThan=" + DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY is less than UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldBeFound("valueOfFraudIncedentsInLCY.lessThan=" + UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);
    }

    @Test
    @Transactional
    void getAllCardFraudInformationsByValueOfFraudIncedentsInLCYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY is greater than DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldNotBeFound("valueOfFraudIncedentsInLCY.greaterThan=" + DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);

        // Get all the cardFraudInformationList where valueOfFraudIncedentsInLCY is greater than SMALLER_VALUE_OF_FRAUD_INCEDENTS_IN_LCY
        defaultCardFraudInformationShouldBeFound("valueOfFraudIncedentsInLCY.greaterThan=" + SMALLER_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardFraudInformationShouldBeFound(String filter) throws Exception {
        restCardFraudInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardFraudInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalNumberOfFraudIncidents").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS)))
            .andExpect(jsonPath("$.[*].valueOfFraudIncedentsInLCY").value(hasItem(DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY)));

        // Check, that the count call also returns 1
        restCardFraudInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardFraudInformationShouldNotBeFound(String filter) throws Exception {
        restCardFraudInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardFraudInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardFraudInformation() throws Exception {
        // Get the cardFraudInformation
        restCardFraudInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardFraudInformation() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        int databaseSizeBeforeUpdate = cardFraudInformationRepository.findAll().size();

        // Update the cardFraudInformation
        CardFraudInformation updatedCardFraudInformation = cardFraudInformationRepository.findById(cardFraudInformation.getId()).get();
        // Disconnect from session so that the updates on updatedCardFraudInformation are not directly saved in db
        em.detach(updatedCardFraudInformation);
        updatedCardFraudInformation
            .reportingDate(UPDATED_REPORTING_DATE)
            .totalNumberOfFraudIncidents(UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS)
            .valueOfFraudIncedentsInLCY(UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);
        CardFraudInformationDTO cardFraudInformationDTO = cardFraudInformationMapper.toDto(updatedCardFraudInformation);

        restCardFraudInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardFraudInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudInformationDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardFraudInformation in the database
        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeUpdate);
        CardFraudInformation testCardFraudInformation = cardFraudInformationList.get(cardFraudInformationList.size() - 1);
        assertThat(testCardFraudInformation.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCardFraudInformation.getTotalNumberOfFraudIncidents()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);
        assertThat(testCardFraudInformation.getValueOfFraudIncedentsInLCY()).isEqualTo(UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);

        // Validate the CardFraudInformation in Elasticsearch
        verify(mockCardFraudInformationSearchRepository).save(testCardFraudInformation);
    }

    @Test
    @Transactional
    void putNonExistingCardFraudInformation() throws Exception {
        int databaseSizeBeforeUpdate = cardFraudInformationRepository.findAll().size();
        cardFraudInformation.setId(count.incrementAndGet());

        // Create the CardFraudInformation
        CardFraudInformationDTO cardFraudInformationDTO = cardFraudInformationMapper.toDto(cardFraudInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardFraudInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardFraudInformationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardFraudInformation in the database
        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardFraudInformation in Elasticsearch
        verify(mockCardFraudInformationSearchRepository, times(0)).save(cardFraudInformation);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardFraudInformation() throws Exception {
        int databaseSizeBeforeUpdate = cardFraudInformationRepository.findAll().size();
        cardFraudInformation.setId(count.incrementAndGet());

        // Create the CardFraudInformation
        CardFraudInformationDTO cardFraudInformationDTO = cardFraudInformationMapper.toDto(cardFraudInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardFraudInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardFraudInformation in the database
        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardFraudInformation in Elasticsearch
        verify(mockCardFraudInformationSearchRepository, times(0)).save(cardFraudInformation);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardFraudInformation() throws Exception {
        int databaseSizeBeforeUpdate = cardFraudInformationRepository.findAll().size();
        cardFraudInformation.setId(count.incrementAndGet());

        // Create the CardFraudInformation
        CardFraudInformationDTO cardFraudInformationDTO = cardFraudInformationMapper.toDto(cardFraudInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardFraudInformationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardFraudInformation in the database
        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardFraudInformation in Elasticsearch
        verify(mockCardFraudInformationSearchRepository, times(0)).save(cardFraudInformation);
    }

    @Test
    @Transactional
    void partialUpdateCardFraudInformationWithPatch() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        int databaseSizeBeforeUpdate = cardFraudInformationRepository.findAll().size();

        // Update the cardFraudInformation using partial update
        CardFraudInformation partialUpdatedCardFraudInformation = new CardFraudInformation();
        partialUpdatedCardFraudInformation.setId(cardFraudInformation.getId());

        partialUpdatedCardFraudInformation
            .totalNumberOfFraudIncidents(UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS)
            .valueOfFraudIncedentsInLCY(UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);

        restCardFraudInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardFraudInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardFraudInformation))
            )
            .andExpect(status().isOk());

        // Validate the CardFraudInformation in the database
        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeUpdate);
        CardFraudInformation testCardFraudInformation = cardFraudInformationList.get(cardFraudInformationList.size() - 1);
        assertThat(testCardFraudInformation.getReportingDate()).isEqualTo(DEFAULT_REPORTING_DATE);
        assertThat(testCardFraudInformation.getTotalNumberOfFraudIncidents()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);
        assertThat(testCardFraudInformation.getValueOfFraudIncedentsInLCY()).isEqualTo(UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);
    }

    @Test
    @Transactional
    void fullUpdateCardFraudInformationWithPatch() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        int databaseSizeBeforeUpdate = cardFraudInformationRepository.findAll().size();

        // Update the cardFraudInformation using partial update
        CardFraudInformation partialUpdatedCardFraudInformation = new CardFraudInformation();
        partialUpdatedCardFraudInformation.setId(cardFraudInformation.getId());

        partialUpdatedCardFraudInformation
            .reportingDate(UPDATED_REPORTING_DATE)
            .totalNumberOfFraudIncidents(UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS)
            .valueOfFraudIncedentsInLCY(UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);

        restCardFraudInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardFraudInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardFraudInformation))
            )
            .andExpect(status().isOk());

        // Validate the CardFraudInformation in the database
        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeUpdate);
        CardFraudInformation testCardFraudInformation = cardFraudInformationList.get(cardFraudInformationList.size() - 1);
        assertThat(testCardFraudInformation.getReportingDate()).isEqualTo(UPDATED_REPORTING_DATE);
        assertThat(testCardFraudInformation.getTotalNumberOfFraudIncidents()).isEqualTo(UPDATED_TOTAL_NUMBER_OF_FRAUD_INCIDENTS);
        assertThat(testCardFraudInformation.getValueOfFraudIncedentsInLCY()).isEqualTo(UPDATED_VALUE_OF_FRAUD_INCEDENTS_IN_LCY);
    }

    @Test
    @Transactional
    void patchNonExistingCardFraudInformation() throws Exception {
        int databaseSizeBeforeUpdate = cardFraudInformationRepository.findAll().size();
        cardFraudInformation.setId(count.incrementAndGet());

        // Create the CardFraudInformation
        CardFraudInformationDTO cardFraudInformationDTO = cardFraudInformationMapper.toDto(cardFraudInformation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardFraudInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardFraudInformationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardFraudInformation in the database
        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardFraudInformation in Elasticsearch
        verify(mockCardFraudInformationSearchRepository, times(0)).save(cardFraudInformation);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardFraudInformation() throws Exception {
        int databaseSizeBeforeUpdate = cardFraudInformationRepository.findAll().size();
        cardFraudInformation.setId(count.incrementAndGet());

        // Create the CardFraudInformation
        CardFraudInformationDTO cardFraudInformationDTO = cardFraudInformationMapper.toDto(cardFraudInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardFraudInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudInformationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardFraudInformation in the database
        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardFraudInformation in Elasticsearch
        verify(mockCardFraudInformationSearchRepository, times(0)).save(cardFraudInformation);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardFraudInformation() throws Exception {
        int databaseSizeBeforeUpdate = cardFraudInformationRepository.findAll().size();
        cardFraudInformation.setId(count.incrementAndGet());

        // Create the CardFraudInformation
        CardFraudInformationDTO cardFraudInformationDTO = cardFraudInformationMapper.toDto(cardFraudInformation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardFraudInformationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudInformationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardFraudInformation in the database
        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardFraudInformation in Elasticsearch
        verify(mockCardFraudInformationSearchRepository, times(0)).save(cardFraudInformation);
    }

    @Test
    @Transactional
    void deleteCardFraudInformation() throws Exception {
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);

        int databaseSizeBeforeDelete = cardFraudInformationRepository.findAll().size();

        // Delete the cardFraudInformation
        restCardFraudInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardFraudInformation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardFraudInformation> cardFraudInformationList = cardFraudInformationRepository.findAll();
        assertThat(cardFraudInformationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardFraudInformation in Elasticsearch
        verify(mockCardFraudInformationSearchRepository, times(1)).deleteById(cardFraudInformation.getId());
    }

    @Test
    @Transactional
    void searchCardFraudInformation() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardFraudInformationRepository.saveAndFlush(cardFraudInformation);
        when(mockCardFraudInformationSearchRepository.search("id:" + cardFraudInformation.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardFraudInformation), PageRequest.of(0, 1), 1));

        // Search the cardFraudInformation
        restCardFraudInformationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardFraudInformation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardFraudInformation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reportingDate").value(hasItem(DEFAULT_REPORTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalNumberOfFraudIncidents").value(hasItem(DEFAULT_TOTAL_NUMBER_OF_FRAUD_INCIDENTS)))
            .andExpect(jsonPath("$.[*].valueOfFraudIncedentsInLCY").value(hasItem(DEFAULT_VALUE_OF_FRAUD_INCEDENTS_IN_LCY)));
    }
}
