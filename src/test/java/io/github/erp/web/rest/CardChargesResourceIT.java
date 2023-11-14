package io.github.erp.web.rest;

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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.CardCharges;
import io.github.erp.repository.CardChargesRepository;
import io.github.erp.repository.search.CardChargesSearchRepository;
import io.github.erp.service.criteria.CardChargesCriteria;
import io.github.erp.service.dto.CardChargesDTO;
import io.github.erp.service.mapper.CardChargesMapper;
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
 * Integration tests for the {@link CardChargesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CardChargesResourceIT {

    private static final String DEFAULT_CARD_CHARGE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CARD_CHARGE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_CHARGE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CARD_CHARGE_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_CHARGE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CARD_CHARGE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/card-charges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/card-charges";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardChargesRepository cardChargesRepository;

    @Autowired
    private CardChargesMapper cardChargesMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardChargesSearchRepositoryMockConfiguration
     */
    @Autowired
    private CardChargesSearchRepository mockCardChargesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardChargesMockMvc;

    private CardCharges cardCharges;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardCharges createEntity(EntityManager em) {
        CardCharges cardCharges = new CardCharges()
            .cardChargeType(DEFAULT_CARD_CHARGE_TYPE)
            .cardChargeTypeName(DEFAULT_CARD_CHARGE_TYPE_NAME)
            .cardChargeDetails(DEFAULT_CARD_CHARGE_DETAILS);
        return cardCharges;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardCharges createUpdatedEntity(EntityManager em) {
        CardCharges cardCharges = new CardCharges()
            .cardChargeType(UPDATED_CARD_CHARGE_TYPE)
            .cardChargeTypeName(UPDATED_CARD_CHARGE_TYPE_NAME)
            .cardChargeDetails(UPDATED_CARD_CHARGE_DETAILS);
        return cardCharges;
    }

    @BeforeEach
    public void initTest() {
        cardCharges = createEntity(em);
    }

    @Test
    @Transactional
    void createCardCharges() throws Exception {
        int databaseSizeBeforeCreate = cardChargesRepository.findAll().size();
        // Create the CardCharges
        CardChargesDTO cardChargesDTO = cardChargesMapper.toDto(cardCharges);
        restCardChargesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardChargesDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CardCharges in the database
        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeCreate + 1);
        CardCharges testCardCharges = cardChargesList.get(cardChargesList.size() - 1);
        assertThat(testCardCharges.getCardChargeType()).isEqualTo(DEFAULT_CARD_CHARGE_TYPE);
        assertThat(testCardCharges.getCardChargeTypeName()).isEqualTo(DEFAULT_CARD_CHARGE_TYPE_NAME);
        assertThat(testCardCharges.getCardChargeDetails()).isEqualTo(DEFAULT_CARD_CHARGE_DETAILS);

        // Validate the CardCharges in Elasticsearch
        verify(mockCardChargesSearchRepository, times(1)).save(testCardCharges);
    }

    @Test
    @Transactional
    void createCardChargesWithExistingId() throws Exception {
        // Create the CardCharges with an existing ID
        cardCharges.setId(1L);
        CardChargesDTO cardChargesDTO = cardChargesMapper.toDto(cardCharges);

        int databaseSizeBeforeCreate = cardChargesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardChargesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardCharges in the database
        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardCharges in Elasticsearch
        verify(mockCardChargesSearchRepository, times(0)).save(cardCharges);
    }

    @Test
    @Transactional
    void checkCardChargeTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardChargesRepository.findAll().size();
        // set the field null
        cardCharges.setCardChargeType(null);

        // Create the CardCharges, which fails.
        CardChargesDTO cardChargesDTO = cardChargesMapper.toDto(cardCharges);

        restCardChargesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardChargesDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardChargeTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardChargesRepository.findAll().size();
        // set the field null
        cardCharges.setCardChargeTypeName(null);

        // Create the CardCharges, which fails.
        CardChargesDTO cardChargesDTO = cardChargesMapper.toDto(cardCharges);

        restCardChargesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardChargesDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardCharges() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList
        restCardChargesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardCharges.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardChargeType").value(hasItem(DEFAULT_CARD_CHARGE_TYPE)))
            .andExpect(jsonPath("$.[*].cardChargeTypeName").value(hasItem(DEFAULT_CARD_CHARGE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].cardChargeDetails").value(hasItem(DEFAULT_CARD_CHARGE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCardCharges() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get the cardCharges
        restCardChargesMockMvc
            .perform(get(ENTITY_API_URL_ID, cardCharges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardCharges.getId().intValue()))
            .andExpect(jsonPath("$.cardChargeType").value(DEFAULT_CARD_CHARGE_TYPE))
            .andExpect(jsonPath("$.cardChargeTypeName").value(DEFAULT_CARD_CHARGE_TYPE_NAME))
            .andExpect(jsonPath("$.cardChargeDetails").value(DEFAULT_CARD_CHARGE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCardChargesByIdFiltering() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        Long id = cardCharges.getId();

        defaultCardChargesShouldBeFound("id.equals=" + id);
        defaultCardChargesShouldNotBeFound("id.notEquals=" + id);

        defaultCardChargesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardChargesShouldNotBeFound("id.greaterThan=" + id);

        defaultCardChargesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardChargesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardChargesByCardChargeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList where cardChargeType equals to DEFAULT_CARD_CHARGE_TYPE
        defaultCardChargesShouldBeFound("cardChargeType.equals=" + DEFAULT_CARD_CHARGE_TYPE);

        // Get all the cardChargesList where cardChargeType equals to UPDATED_CARD_CHARGE_TYPE
        defaultCardChargesShouldNotBeFound("cardChargeType.equals=" + UPDATED_CARD_CHARGE_TYPE);
    }

    @Test
    @Transactional
    void getAllCardChargesByCardChargeTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList where cardChargeType not equals to DEFAULT_CARD_CHARGE_TYPE
        defaultCardChargesShouldNotBeFound("cardChargeType.notEquals=" + DEFAULT_CARD_CHARGE_TYPE);

        // Get all the cardChargesList where cardChargeType not equals to UPDATED_CARD_CHARGE_TYPE
        defaultCardChargesShouldBeFound("cardChargeType.notEquals=" + UPDATED_CARD_CHARGE_TYPE);
    }

    @Test
    @Transactional
    void getAllCardChargesByCardChargeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList where cardChargeType in DEFAULT_CARD_CHARGE_TYPE or UPDATED_CARD_CHARGE_TYPE
        defaultCardChargesShouldBeFound("cardChargeType.in=" + DEFAULT_CARD_CHARGE_TYPE + "," + UPDATED_CARD_CHARGE_TYPE);

        // Get all the cardChargesList where cardChargeType equals to UPDATED_CARD_CHARGE_TYPE
        defaultCardChargesShouldNotBeFound("cardChargeType.in=" + UPDATED_CARD_CHARGE_TYPE);
    }

    @Test
    @Transactional
    void getAllCardChargesByCardChargeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList where cardChargeType is not null
        defaultCardChargesShouldBeFound("cardChargeType.specified=true");

        // Get all the cardChargesList where cardChargeType is null
        defaultCardChargesShouldNotBeFound("cardChargeType.specified=false");
    }

    @Test
    @Transactional
    void getAllCardChargesByCardChargeTypeContainsSomething() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList where cardChargeType contains DEFAULT_CARD_CHARGE_TYPE
        defaultCardChargesShouldBeFound("cardChargeType.contains=" + DEFAULT_CARD_CHARGE_TYPE);

        // Get all the cardChargesList where cardChargeType contains UPDATED_CARD_CHARGE_TYPE
        defaultCardChargesShouldNotBeFound("cardChargeType.contains=" + UPDATED_CARD_CHARGE_TYPE);
    }

    @Test
    @Transactional
    void getAllCardChargesByCardChargeTypeNotContainsSomething() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList where cardChargeType does not contain DEFAULT_CARD_CHARGE_TYPE
        defaultCardChargesShouldNotBeFound("cardChargeType.doesNotContain=" + DEFAULT_CARD_CHARGE_TYPE);

        // Get all the cardChargesList where cardChargeType does not contain UPDATED_CARD_CHARGE_TYPE
        defaultCardChargesShouldBeFound("cardChargeType.doesNotContain=" + UPDATED_CARD_CHARGE_TYPE);
    }

    @Test
    @Transactional
    void getAllCardChargesByCardChargeTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList where cardChargeTypeName equals to DEFAULT_CARD_CHARGE_TYPE_NAME
        defaultCardChargesShouldBeFound("cardChargeTypeName.equals=" + DEFAULT_CARD_CHARGE_TYPE_NAME);

        // Get all the cardChargesList where cardChargeTypeName equals to UPDATED_CARD_CHARGE_TYPE_NAME
        defaultCardChargesShouldNotBeFound("cardChargeTypeName.equals=" + UPDATED_CARD_CHARGE_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllCardChargesByCardChargeTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList where cardChargeTypeName not equals to DEFAULT_CARD_CHARGE_TYPE_NAME
        defaultCardChargesShouldNotBeFound("cardChargeTypeName.notEquals=" + DEFAULT_CARD_CHARGE_TYPE_NAME);

        // Get all the cardChargesList where cardChargeTypeName not equals to UPDATED_CARD_CHARGE_TYPE_NAME
        defaultCardChargesShouldBeFound("cardChargeTypeName.notEquals=" + UPDATED_CARD_CHARGE_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllCardChargesByCardChargeTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList where cardChargeTypeName in DEFAULT_CARD_CHARGE_TYPE_NAME or UPDATED_CARD_CHARGE_TYPE_NAME
        defaultCardChargesShouldBeFound("cardChargeTypeName.in=" + DEFAULT_CARD_CHARGE_TYPE_NAME + "," + UPDATED_CARD_CHARGE_TYPE_NAME);

        // Get all the cardChargesList where cardChargeTypeName equals to UPDATED_CARD_CHARGE_TYPE_NAME
        defaultCardChargesShouldNotBeFound("cardChargeTypeName.in=" + UPDATED_CARD_CHARGE_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllCardChargesByCardChargeTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList where cardChargeTypeName is not null
        defaultCardChargesShouldBeFound("cardChargeTypeName.specified=true");

        // Get all the cardChargesList where cardChargeTypeName is null
        defaultCardChargesShouldNotBeFound("cardChargeTypeName.specified=false");
    }

    @Test
    @Transactional
    void getAllCardChargesByCardChargeTypeNameContainsSomething() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList where cardChargeTypeName contains DEFAULT_CARD_CHARGE_TYPE_NAME
        defaultCardChargesShouldBeFound("cardChargeTypeName.contains=" + DEFAULT_CARD_CHARGE_TYPE_NAME);

        // Get all the cardChargesList where cardChargeTypeName contains UPDATED_CARD_CHARGE_TYPE_NAME
        defaultCardChargesShouldNotBeFound("cardChargeTypeName.contains=" + UPDATED_CARD_CHARGE_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllCardChargesByCardChargeTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        // Get all the cardChargesList where cardChargeTypeName does not contain DEFAULT_CARD_CHARGE_TYPE_NAME
        defaultCardChargesShouldNotBeFound("cardChargeTypeName.doesNotContain=" + DEFAULT_CARD_CHARGE_TYPE_NAME);

        // Get all the cardChargesList where cardChargeTypeName does not contain UPDATED_CARD_CHARGE_TYPE_NAME
        defaultCardChargesShouldBeFound("cardChargeTypeName.doesNotContain=" + UPDATED_CARD_CHARGE_TYPE_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardChargesShouldBeFound(String filter) throws Exception {
        restCardChargesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardCharges.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardChargeType").value(hasItem(DEFAULT_CARD_CHARGE_TYPE)))
            .andExpect(jsonPath("$.[*].cardChargeTypeName").value(hasItem(DEFAULT_CARD_CHARGE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].cardChargeDetails").value(hasItem(DEFAULT_CARD_CHARGE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCardChargesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardChargesShouldNotBeFound(String filter) throws Exception {
        restCardChargesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardChargesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardCharges() throws Exception {
        // Get the cardCharges
        restCardChargesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardCharges() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        int databaseSizeBeforeUpdate = cardChargesRepository.findAll().size();

        // Update the cardCharges
        CardCharges updatedCardCharges = cardChargesRepository.findById(cardCharges.getId()).get();
        // Disconnect from session so that the updates on updatedCardCharges are not directly saved in db
        em.detach(updatedCardCharges);
        updatedCardCharges
            .cardChargeType(UPDATED_CARD_CHARGE_TYPE)
            .cardChargeTypeName(UPDATED_CARD_CHARGE_TYPE_NAME)
            .cardChargeDetails(UPDATED_CARD_CHARGE_DETAILS);
        CardChargesDTO cardChargesDTO = cardChargesMapper.toDto(updatedCardCharges);

        restCardChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardChargesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardChargesDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardCharges in the database
        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeUpdate);
        CardCharges testCardCharges = cardChargesList.get(cardChargesList.size() - 1);
        assertThat(testCardCharges.getCardChargeType()).isEqualTo(UPDATED_CARD_CHARGE_TYPE);
        assertThat(testCardCharges.getCardChargeTypeName()).isEqualTo(UPDATED_CARD_CHARGE_TYPE_NAME);
        assertThat(testCardCharges.getCardChargeDetails()).isEqualTo(UPDATED_CARD_CHARGE_DETAILS);

        // Validate the CardCharges in Elasticsearch
        verify(mockCardChargesSearchRepository).save(testCardCharges);
    }

    @Test
    @Transactional
    void putNonExistingCardCharges() throws Exception {
        int databaseSizeBeforeUpdate = cardChargesRepository.findAll().size();
        cardCharges.setId(count.incrementAndGet());

        // Create the CardCharges
        CardChargesDTO cardChargesDTO = cardChargesMapper.toDto(cardCharges);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardChargesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardCharges in the database
        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardCharges in Elasticsearch
        verify(mockCardChargesSearchRepository, times(0)).save(cardCharges);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardCharges() throws Exception {
        int databaseSizeBeforeUpdate = cardChargesRepository.findAll().size();
        cardCharges.setId(count.incrementAndGet());

        // Create the CardCharges
        CardChargesDTO cardChargesDTO = cardChargesMapper.toDto(cardCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardCharges in the database
        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardCharges in Elasticsearch
        verify(mockCardChargesSearchRepository, times(0)).save(cardCharges);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardCharges() throws Exception {
        int databaseSizeBeforeUpdate = cardChargesRepository.findAll().size();
        cardCharges.setId(count.incrementAndGet());

        // Create the CardCharges
        CardChargesDTO cardChargesDTO = cardChargesMapper.toDto(cardCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardChargesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardChargesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardCharges in the database
        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardCharges in Elasticsearch
        verify(mockCardChargesSearchRepository, times(0)).save(cardCharges);
    }

    @Test
    @Transactional
    void partialUpdateCardChargesWithPatch() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        int databaseSizeBeforeUpdate = cardChargesRepository.findAll().size();

        // Update the cardCharges using partial update
        CardCharges partialUpdatedCardCharges = new CardCharges();
        partialUpdatedCardCharges.setId(cardCharges.getId());

        partialUpdatedCardCharges.cardChargeType(UPDATED_CARD_CHARGE_TYPE).cardChargeTypeName(UPDATED_CARD_CHARGE_TYPE_NAME);

        restCardChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardCharges.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardCharges))
            )
            .andExpect(status().isOk());

        // Validate the CardCharges in the database
        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeUpdate);
        CardCharges testCardCharges = cardChargesList.get(cardChargesList.size() - 1);
        assertThat(testCardCharges.getCardChargeType()).isEqualTo(UPDATED_CARD_CHARGE_TYPE);
        assertThat(testCardCharges.getCardChargeTypeName()).isEqualTo(UPDATED_CARD_CHARGE_TYPE_NAME);
        assertThat(testCardCharges.getCardChargeDetails()).isEqualTo(DEFAULT_CARD_CHARGE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCardChargesWithPatch() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        int databaseSizeBeforeUpdate = cardChargesRepository.findAll().size();

        // Update the cardCharges using partial update
        CardCharges partialUpdatedCardCharges = new CardCharges();
        partialUpdatedCardCharges.setId(cardCharges.getId());

        partialUpdatedCardCharges
            .cardChargeType(UPDATED_CARD_CHARGE_TYPE)
            .cardChargeTypeName(UPDATED_CARD_CHARGE_TYPE_NAME)
            .cardChargeDetails(UPDATED_CARD_CHARGE_DETAILS);

        restCardChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardCharges.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardCharges))
            )
            .andExpect(status().isOk());

        // Validate the CardCharges in the database
        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeUpdate);
        CardCharges testCardCharges = cardChargesList.get(cardChargesList.size() - 1);
        assertThat(testCardCharges.getCardChargeType()).isEqualTo(UPDATED_CARD_CHARGE_TYPE);
        assertThat(testCardCharges.getCardChargeTypeName()).isEqualTo(UPDATED_CARD_CHARGE_TYPE_NAME);
        assertThat(testCardCharges.getCardChargeDetails()).isEqualTo(UPDATED_CARD_CHARGE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCardCharges() throws Exception {
        int databaseSizeBeforeUpdate = cardChargesRepository.findAll().size();
        cardCharges.setId(count.incrementAndGet());

        // Create the CardCharges
        CardChargesDTO cardChargesDTO = cardChargesMapper.toDto(cardCharges);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardChargesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardCharges in the database
        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardCharges in Elasticsearch
        verify(mockCardChargesSearchRepository, times(0)).save(cardCharges);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardCharges() throws Exception {
        int databaseSizeBeforeUpdate = cardChargesRepository.findAll().size();
        cardCharges.setId(count.incrementAndGet());

        // Create the CardCharges
        CardChargesDTO cardChargesDTO = cardChargesMapper.toDto(cardCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardChargesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardCharges in the database
        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardCharges in Elasticsearch
        verify(mockCardChargesSearchRepository, times(0)).save(cardCharges);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardCharges() throws Exception {
        int databaseSizeBeforeUpdate = cardChargesRepository.findAll().size();
        cardCharges.setId(count.incrementAndGet());

        // Create the CardCharges
        CardChargesDTO cardChargesDTO = cardChargesMapper.toDto(cardCharges);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardChargesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cardChargesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardCharges in the database
        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardCharges in Elasticsearch
        verify(mockCardChargesSearchRepository, times(0)).save(cardCharges);
    }

    @Test
    @Transactional
    void deleteCardCharges() throws Exception {
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);

        int databaseSizeBeforeDelete = cardChargesRepository.findAll().size();

        // Delete the cardCharges
        restCardChargesMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardCharges.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardCharges> cardChargesList = cardChargesRepository.findAll();
        assertThat(cardChargesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardCharges in Elasticsearch
        verify(mockCardChargesSearchRepository, times(1)).deleteById(cardCharges.getId());
    }

    @Test
    @Transactional
    void searchCardCharges() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardChargesRepository.saveAndFlush(cardCharges);
        when(mockCardChargesSearchRepository.search("id:" + cardCharges.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardCharges), PageRequest.of(0, 1), 1));

        // Search the cardCharges
        restCardChargesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardCharges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardCharges.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardChargeType").value(hasItem(DEFAULT_CARD_CHARGE_TYPE)))
            .andExpect(jsonPath("$.[*].cardChargeTypeName").value(hasItem(DEFAULT_CARD_CHARGE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].cardChargeDetails").value(hasItem(DEFAULT_CARD_CHARGE_DETAILS.toString())));
    }
}
