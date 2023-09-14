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
import io.github.erp.domain.QuestionBase;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.domain.enumeration.ControlTypes;
import io.github.erp.repository.QuestionBaseRepository;
import io.github.erp.repository.search.QuestionBaseSearchRepository;
import io.github.erp.service.QuestionBaseService;
import io.github.erp.service.criteria.QuestionBaseCriteria;
import io.github.erp.service.dto.QuestionBaseDTO;
import io.github.erp.service.mapper.QuestionBaseMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link QuestionBaseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuestionBaseResourceIT {

    private static final String DEFAULT_CONTEXT = "AAAAAAAAAA";
    private static final String UPDATED_CONTEXT = "BBBBBBBBBB";

    private static final UUID DEFAULT_SERIAL = UUID.randomUUID();
    private static final UUID UPDATED_SERIAL = UUID.randomUUID();

    private static final String DEFAULT_QUESTION_BASE_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_BASE_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION_BASE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_BASE_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_QUESTION_BASE_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION_BASE_LABEL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REQUIRED = false;
    private static final Boolean UPDATED_REQUIRED = true;

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    private static final ControlTypes DEFAULT_CONTROL_TYPE = ControlTypes.TEXTBOX;
    private static final ControlTypes UPDATED_CONTROL_TYPE = ControlTypes.DATETIME_LOCAL;

    private static final String DEFAULT_PLACEHOLDER = "AAAAAAAAAA";
    private static final String UPDATED_PLACEHOLDER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ITERABLE = false;
    private static final Boolean UPDATED_ITERABLE = true;

    private static final String ENTITY_API_URL = "/api/question-bases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/question-bases";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionBaseRepository questionBaseRepository;

    @Mock
    private QuestionBaseRepository questionBaseRepositoryMock;

    @Autowired
    private QuestionBaseMapper questionBaseMapper;

    @Mock
    private QuestionBaseService questionBaseServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.QuestionBaseSearchRepositoryMockConfiguration
     */
    @Autowired
    private QuestionBaseSearchRepository mockQuestionBaseSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionBaseMockMvc;

    private QuestionBase questionBase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionBase createEntity(EntityManager em) {
        QuestionBase questionBase = new QuestionBase()
            .context(DEFAULT_CONTEXT)
            .serial(DEFAULT_SERIAL)
            .questionBaseValue(DEFAULT_QUESTION_BASE_VALUE)
            .questionBaseKey(DEFAULT_QUESTION_BASE_KEY)
            .questionBaseLabel(DEFAULT_QUESTION_BASE_LABEL)
            .required(DEFAULT_REQUIRED)
            .order(DEFAULT_ORDER)
            .controlType(DEFAULT_CONTROL_TYPE)
            .placeholder(DEFAULT_PLACEHOLDER)
            .iterable(DEFAULT_ITERABLE);
        return questionBase;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionBase createUpdatedEntity(EntityManager em) {
        QuestionBase questionBase = new QuestionBase()
            .context(UPDATED_CONTEXT)
            .serial(UPDATED_SERIAL)
            .questionBaseValue(UPDATED_QUESTION_BASE_VALUE)
            .questionBaseKey(UPDATED_QUESTION_BASE_KEY)
            .questionBaseLabel(UPDATED_QUESTION_BASE_LABEL)
            .required(UPDATED_REQUIRED)
            .order(UPDATED_ORDER)
            .controlType(UPDATED_CONTROL_TYPE)
            .placeholder(UPDATED_PLACEHOLDER)
            .iterable(UPDATED_ITERABLE);
        return questionBase;
    }

    @BeforeEach
    public void initTest() {
        questionBase = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionBase() throws Exception {
        int databaseSizeBeforeCreate = questionBaseRepository.findAll().size();
        // Create the QuestionBase
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);
        restQuestionBaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isCreated());

        // Validate the QuestionBase in the database
        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionBase testQuestionBase = questionBaseList.get(questionBaseList.size() - 1);
        assertThat(testQuestionBase.getContext()).isEqualTo(DEFAULT_CONTEXT);
        assertThat(testQuestionBase.getSerial()).isEqualTo(DEFAULT_SERIAL);
        assertThat(testQuestionBase.getQuestionBaseValue()).isEqualTo(DEFAULT_QUESTION_BASE_VALUE);
        assertThat(testQuestionBase.getQuestionBaseKey()).isEqualTo(DEFAULT_QUESTION_BASE_KEY);
        assertThat(testQuestionBase.getQuestionBaseLabel()).isEqualTo(DEFAULT_QUESTION_BASE_LABEL);
        assertThat(testQuestionBase.getRequired()).isEqualTo(DEFAULT_REQUIRED);
        assertThat(testQuestionBase.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testQuestionBase.getControlType()).isEqualTo(DEFAULT_CONTROL_TYPE);
        assertThat(testQuestionBase.getPlaceholder()).isEqualTo(DEFAULT_PLACEHOLDER);
        assertThat(testQuestionBase.getIterable()).isEqualTo(DEFAULT_ITERABLE);

        // Validate the QuestionBase in Elasticsearch
        verify(mockQuestionBaseSearchRepository, times(1)).save(testQuestionBase);
    }

    @Test
    @Transactional
    void createQuestionBaseWithExistingId() throws Exception {
        // Create the QuestionBase with an existing ID
        questionBase.setId(1L);
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        int databaseSizeBeforeCreate = questionBaseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionBaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionBase in the database
        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeCreate);

        // Validate the QuestionBase in Elasticsearch
        verify(mockQuestionBaseSearchRepository, times(0)).save(questionBase);
    }

    @Test
    @Transactional
    void checkContextIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionBaseRepository.findAll().size();
        // set the field null
        questionBase.setContext(null);

        // Create the QuestionBase, which fails.
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        restQuestionBaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isBadRequest());

        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSerialIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionBaseRepository.findAll().size();
        // set the field null
        questionBase.setSerial(null);

        // Create the QuestionBase, which fails.
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        restQuestionBaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isBadRequest());

        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuestionBaseKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionBaseRepository.findAll().size();
        // set the field null
        questionBase.setQuestionBaseKey(null);

        // Create the QuestionBase, which fails.
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        restQuestionBaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isBadRequest());

        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuestionBaseLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionBaseRepository.findAll().size();
        // set the field null
        questionBase.setQuestionBaseLabel(null);

        // Create the QuestionBase, which fails.
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        restQuestionBaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isBadRequest());

        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionBaseRepository.findAll().size();
        // set the field null
        questionBase.setOrder(null);

        // Create the QuestionBase, which fails.
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        restQuestionBaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isBadRequest());

        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkControlTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionBaseRepository.findAll().size();
        // set the field null
        questionBase.setControlType(null);

        // Create the QuestionBase, which fails.
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        restQuestionBaseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isBadRequest());

        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuestionBases() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList
        restQuestionBaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionBase.getId().intValue())))
            .andExpect(jsonPath("$.[*].context").value(hasItem(DEFAULT_CONTEXT)))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL.toString())))
            .andExpect(jsonPath("$.[*].questionBaseValue").value(hasItem(DEFAULT_QUESTION_BASE_VALUE)))
            .andExpect(jsonPath("$.[*].questionBaseKey").value(hasItem(DEFAULT_QUESTION_BASE_KEY)))
            .andExpect(jsonPath("$.[*].questionBaseLabel").value(hasItem(DEFAULT_QUESTION_BASE_LABEL)))
            .andExpect(jsonPath("$.[*].required").value(hasItem(DEFAULT_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].controlType").value(hasItem(DEFAULT_CONTROL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].placeholder").value(hasItem(DEFAULT_PLACEHOLDER)))
            .andExpect(jsonPath("$.[*].iterable").value(hasItem(DEFAULT_ITERABLE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionBasesWithEagerRelationshipsIsEnabled() throws Exception {
        when(questionBaseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionBaseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionBaseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionBasesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(questionBaseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionBaseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionBaseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getQuestionBase() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get the questionBase
        restQuestionBaseMockMvc
            .perform(get(ENTITY_API_URL_ID, questionBase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionBase.getId().intValue()))
            .andExpect(jsonPath("$.context").value(DEFAULT_CONTEXT))
            .andExpect(jsonPath("$.serial").value(DEFAULT_SERIAL.toString()))
            .andExpect(jsonPath("$.questionBaseValue").value(DEFAULT_QUESTION_BASE_VALUE))
            .andExpect(jsonPath("$.questionBaseKey").value(DEFAULT_QUESTION_BASE_KEY))
            .andExpect(jsonPath("$.questionBaseLabel").value(DEFAULT_QUESTION_BASE_LABEL))
            .andExpect(jsonPath("$.required").value(DEFAULT_REQUIRED.booleanValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER))
            .andExpect(jsonPath("$.controlType").value(DEFAULT_CONTROL_TYPE.toString()))
            .andExpect(jsonPath("$.placeholder").value(DEFAULT_PLACEHOLDER))
            .andExpect(jsonPath("$.iterable").value(DEFAULT_ITERABLE.booleanValue()));
    }

    @Test
    @Transactional
    void getQuestionBasesByIdFiltering() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        Long id = questionBase.getId();

        defaultQuestionBaseShouldBeFound("id.equals=" + id);
        defaultQuestionBaseShouldNotBeFound("id.notEquals=" + id);

        defaultQuestionBaseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestionBaseShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestionBaseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestionBaseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByContextIsEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where context equals to DEFAULT_CONTEXT
        defaultQuestionBaseShouldBeFound("context.equals=" + DEFAULT_CONTEXT);

        // Get all the questionBaseList where context equals to UPDATED_CONTEXT
        defaultQuestionBaseShouldNotBeFound("context.equals=" + UPDATED_CONTEXT);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByContextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where context not equals to DEFAULT_CONTEXT
        defaultQuestionBaseShouldNotBeFound("context.notEquals=" + DEFAULT_CONTEXT);

        // Get all the questionBaseList where context not equals to UPDATED_CONTEXT
        defaultQuestionBaseShouldBeFound("context.notEquals=" + UPDATED_CONTEXT);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByContextIsInShouldWork() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where context in DEFAULT_CONTEXT or UPDATED_CONTEXT
        defaultQuestionBaseShouldBeFound("context.in=" + DEFAULT_CONTEXT + "," + UPDATED_CONTEXT);

        // Get all the questionBaseList where context equals to UPDATED_CONTEXT
        defaultQuestionBaseShouldNotBeFound("context.in=" + UPDATED_CONTEXT);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByContextIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where context is not null
        defaultQuestionBaseShouldBeFound("context.specified=true");

        // Get all the questionBaseList where context is null
        defaultQuestionBaseShouldNotBeFound("context.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionBasesByContextContainsSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where context contains DEFAULT_CONTEXT
        defaultQuestionBaseShouldBeFound("context.contains=" + DEFAULT_CONTEXT);

        // Get all the questionBaseList where context contains UPDATED_CONTEXT
        defaultQuestionBaseShouldNotBeFound("context.contains=" + UPDATED_CONTEXT);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByContextNotContainsSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where context does not contain DEFAULT_CONTEXT
        defaultQuestionBaseShouldNotBeFound("context.doesNotContain=" + DEFAULT_CONTEXT);

        // Get all the questionBaseList where context does not contain UPDATED_CONTEXT
        defaultQuestionBaseShouldBeFound("context.doesNotContain=" + UPDATED_CONTEXT);
    }

    @Test
    @Transactional
    void getAllQuestionBasesBySerialIsEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where serial equals to DEFAULT_SERIAL
        defaultQuestionBaseShouldBeFound("serial.equals=" + DEFAULT_SERIAL);

        // Get all the questionBaseList where serial equals to UPDATED_SERIAL
        defaultQuestionBaseShouldNotBeFound("serial.equals=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllQuestionBasesBySerialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where serial not equals to DEFAULT_SERIAL
        defaultQuestionBaseShouldNotBeFound("serial.notEquals=" + DEFAULT_SERIAL);

        // Get all the questionBaseList where serial not equals to UPDATED_SERIAL
        defaultQuestionBaseShouldBeFound("serial.notEquals=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllQuestionBasesBySerialIsInShouldWork() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where serial in DEFAULT_SERIAL or UPDATED_SERIAL
        defaultQuestionBaseShouldBeFound("serial.in=" + DEFAULT_SERIAL + "," + UPDATED_SERIAL);

        // Get all the questionBaseList where serial equals to UPDATED_SERIAL
        defaultQuestionBaseShouldNotBeFound("serial.in=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllQuestionBasesBySerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where serial is not null
        defaultQuestionBaseShouldBeFound("serial.specified=true");

        // Get all the questionBaseList where serial is null
        defaultQuestionBaseShouldNotBeFound("serial.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseValueIsEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseValue equals to DEFAULT_QUESTION_BASE_VALUE
        defaultQuestionBaseShouldBeFound("questionBaseValue.equals=" + DEFAULT_QUESTION_BASE_VALUE);

        // Get all the questionBaseList where questionBaseValue equals to UPDATED_QUESTION_BASE_VALUE
        defaultQuestionBaseShouldNotBeFound("questionBaseValue.equals=" + UPDATED_QUESTION_BASE_VALUE);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseValue not equals to DEFAULT_QUESTION_BASE_VALUE
        defaultQuestionBaseShouldNotBeFound("questionBaseValue.notEquals=" + DEFAULT_QUESTION_BASE_VALUE);

        // Get all the questionBaseList where questionBaseValue not equals to UPDATED_QUESTION_BASE_VALUE
        defaultQuestionBaseShouldBeFound("questionBaseValue.notEquals=" + UPDATED_QUESTION_BASE_VALUE);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseValueIsInShouldWork() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseValue in DEFAULT_QUESTION_BASE_VALUE or UPDATED_QUESTION_BASE_VALUE
        defaultQuestionBaseShouldBeFound("questionBaseValue.in=" + DEFAULT_QUESTION_BASE_VALUE + "," + UPDATED_QUESTION_BASE_VALUE);

        // Get all the questionBaseList where questionBaseValue equals to UPDATED_QUESTION_BASE_VALUE
        defaultQuestionBaseShouldNotBeFound("questionBaseValue.in=" + UPDATED_QUESTION_BASE_VALUE);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseValue is not null
        defaultQuestionBaseShouldBeFound("questionBaseValue.specified=true");

        // Get all the questionBaseList where questionBaseValue is null
        defaultQuestionBaseShouldNotBeFound("questionBaseValue.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseValueContainsSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseValue contains DEFAULT_QUESTION_BASE_VALUE
        defaultQuestionBaseShouldBeFound("questionBaseValue.contains=" + DEFAULT_QUESTION_BASE_VALUE);

        // Get all the questionBaseList where questionBaseValue contains UPDATED_QUESTION_BASE_VALUE
        defaultQuestionBaseShouldNotBeFound("questionBaseValue.contains=" + UPDATED_QUESTION_BASE_VALUE);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseValueNotContainsSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseValue does not contain DEFAULT_QUESTION_BASE_VALUE
        defaultQuestionBaseShouldNotBeFound("questionBaseValue.doesNotContain=" + DEFAULT_QUESTION_BASE_VALUE);

        // Get all the questionBaseList where questionBaseValue does not contain UPDATED_QUESTION_BASE_VALUE
        defaultQuestionBaseShouldBeFound("questionBaseValue.doesNotContain=" + UPDATED_QUESTION_BASE_VALUE);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseKey equals to DEFAULT_QUESTION_BASE_KEY
        defaultQuestionBaseShouldBeFound("questionBaseKey.equals=" + DEFAULT_QUESTION_BASE_KEY);

        // Get all the questionBaseList where questionBaseKey equals to UPDATED_QUESTION_BASE_KEY
        defaultQuestionBaseShouldNotBeFound("questionBaseKey.equals=" + UPDATED_QUESTION_BASE_KEY);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseKeyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseKey not equals to DEFAULT_QUESTION_BASE_KEY
        defaultQuestionBaseShouldNotBeFound("questionBaseKey.notEquals=" + DEFAULT_QUESTION_BASE_KEY);

        // Get all the questionBaseList where questionBaseKey not equals to UPDATED_QUESTION_BASE_KEY
        defaultQuestionBaseShouldBeFound("questionBaseKey.notEquals=" + UPDATED_QUESTION_BASE_KEY);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseKeyIsInShouldWork() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseKey in DEFAULT_QUESTION_BASE_KEY or UPDATED_QUESTION_BASE_KEY
        defaultQuestionBaseShouldBeFound("questionBaseKey.in=" + DEFAULT_QUESTION_BASE_KEY + "," + UPDATED_QUESTION_BASE_KEY);

        // Get all the questionBaseList where questionBaseKey equals to UPDATED_QUESTION_BASE_KEY
        defaultQuestionBaseShouldNotBeFound("questionBaseKey.in=" + UPDATED_QUESTION_BASE_KEY);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseKey is not null
        defaultQuestionBaseShouldBeFound("questionBaseKey.specified=true");

        // Get all the questionBaseList where questionBaseKey is null
        defaultQuestionBaseShouldNotBeFound("questionBaseKey.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseKeyContainsSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseKey contains DEFAULT_QUESTION_BASE_KEY
        defaultQuestionBaseShouldBeFound("questionBaseKey.contains=" + DEFAULT_QUESTION_BASE_KEY);

        // Get all the questionBaseList where questionBaseKey contains UPDATED_QUESTION_BASE_KEY
        defaultQuestionBaseShouldNotBeFound("questionBaseKey.contains=" + UPDATED_QUESTION_BASE_KEY);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseKeyNotContainsSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseKey does not contain DEFAULT_QUESTION_BASE_KEY
        defaultQuestionBaseShouldNotBeFound("questionBaseKey.doesNotContain=" + DEFAULT_QUESTION_BASE_KEY);

        // Get all the questionBaseList where questionBaseKey does not contain UPDATED_QUESTION_BASE_KEY
        defaultQuestionBaseShouldBeFound("questionBaseKey.doesNotContain=" + UPDATED_QUESTION_BASE_KEY);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseLabel equals to DEFAULT_QUESTION_BASE_LABEL
        defaultQuestionBaseShouldBeFound("questionBaseLabel.equals=" + DEFAULT_QUESTION_BASE_LABEL);

        // Get all the questionBaseList where questionBaseLabel equals to UPDATED_QUESTION_BASE_LABEL
        defaultQuestionBaseShouldNotBeFound("questionBaseLabel.equals=" + UPDATED_QUESTION_BASE_LABEL);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseLabel not equals to DEFAULT_QUESTION_BASE_LABEL
        defaultQuestionBaseShouldNotBeFound("questionBaseLabel.notEquals=" + DEFAULT_QUESTION_BASE_LABEL);

        // Get all the questionBaseList where questionBaseLabel not equals to UPDATED_QUESTION_BASE_LABEL
        defaultQuestionBaseShouldBeFound("questionBaseLabel.notEquals=" + UPDATED_QUESTION_BASE_LABEL);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseLabelIsInShouldWork() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseLabel in DEFAULT_QUESTION_BASE_LABEL or UPDATED_QUESTION_BASE_LABEL
        defaultQuestionBaseShouldBeFound("questionBaseLabel.in=" + DEFAULT_QUESTION_BASE_LABEL + "," + UPDATED_QUESTION_BASE_LABEL);

        // Get all the questionBaseList where questionBaseLabel equals to UPDATED_QUESTION_BASE_LABEL
        defaultQuestionBaseShouldNotBeFound("questionBaseLabel.in=" + UPDATED_QUESTION_BASE_LABEL);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseLabel is not null
        defaultQuestionBaseShouldBeFound("questionBaseLabel.specified=true");

        // Get all the questionBaseList where questionBaseLabel is null
        defaultQuestionBaseShouldNotBeFound("questionBaseLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseLabelContainsSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseLabel contains DEFAULT_QUESTION_BASE_LABEL
        defaultQuestionBaseShouldBeFound("questionBaseLabel.contains=" + DEFAULT_QUESTION_BASE_LABEL);

        // Get all the questionBaseList where questionBaseLabel contains UPDATED_QUESTION_BASE_LABEL
        defaultQuestionBaseShouldNotBeFound("questionBaseLabel.contains=" + UPDATED_QUESTION_BASE_LABEL);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByQuestionBaseLabelNotContainsSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where questionBaseLabel does not contain DEFAULT_QUESTION_BASE_LABEL
        defaultQuestionBaseShouldNotBeFound("questionBaseLabel.doesNotContain=" + DEFAULT_QUESTION_BASE_LABEL);

        // Get all the questionBaseList where questionBaseLabel does not contain UPDATED_QUESTION_BASE_LABEL
        defaultQuestionBaseShouldBeFound("questionBaseLabel.doesNotContain=" + UPDATED_QUESTION_BASE_LABEL);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByRequiredIsEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where required equals to DEFAULT_REQUIRED
        defaultQuestionBaseShouldBeFound("required.equals=" + DEFAULT_REQUIRED);

        // Get all the questionBaseList where required equals to UPDATED_REQUIRED
        defaultQuestionBaseShouldNotBeFound("required.equals=" + UPDATED_REQUIRED);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByRequiredIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where required not equals to DEFAULT_REQUIRED
        defaultQuestionBaseShouldNotBeFound("required.notEquals=" + DEFAULT_REQUIRED);

        // Get all the questionBaseList where required not equals to UPDATED_REQUIRED
        defaultQuestionBaseShouldBeFound("required.notEquals=" + UPDATED_REQUIRED);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByRequiredIsInShouldWork() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where required in DEFAULT_REQUIRED or UPDATED_REQUIRED
        defaultQuestionBaseShouldBeFound("required.in=" + DEFAULT_REQUIRED + "," + UPDATED_REQUIRED);

        // Get all the questionBaseList where required equals to UPDATED_REQUIRED
        defaultQuestionBaseShouldNotBeFound("required.in=" + UPDATED_REQUIRED);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByRequiredIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where required is not null
        defaultQuestionBaseShouldBeFound("required.specified=true");

        // Get all the questionBaseList where required is null
        defaultQuestionBaseShouldNotBeFound("required.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionBasesByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where order equals to DEFAULT_ORDER
        defaultQuestionBaseShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the questionBaseList where order equals to UPDATED_ORDER
        defaultQuestionBaseShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where order not equals to DEFAULT_ORDER
        defaultQuestionBaseShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the questionBaseList where order not equals to UPDATED_ORDER
        defaultQuestionBaseShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultQuestionBaseShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the questionBaseList where order equals to UPDATED_ORDER
        defaultQuestionBaseShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where order is not null
        defaultQuestionBaseShouldBeFound("order.specified=true");

        // Get all the questionBaseList where order is null
        defaultQuestionBaseShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionBasesByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where order is greater than or equal to DEFAULT_ORDER
        defaultQuestionBaseShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the questionBaseList where order is greater than or equal to UPDATED_ORDER
        defaultQuestionBaseShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where order is less than or equal to DEFAULT_ORDER
        defaultQuestionBaseShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the questionBaseList where order is less than or equal to SMALLER_ORDER
        defaultQuestionBaseShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where order is less than DEFAULT_ORDER
        defaultQuestionBaseShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the questionBaseList where order is less than UPDATED_ORDER
        defaultQuestionBaseShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where order is greater than DEFAULT_ORDER
        defaultQuestionBaseShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the questionBaseList where order is greater than SMALLER_ORDER
        defaultQuestionBaseShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByControlTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where controlType equals to DEFAULT_CONTROL_TYPE
        defaultQuestionBaseShouldBeFound("controlType.equals=" + DEFAULT_CONTROL_TYPE);

        // Get all the questionBaseList where controlType equals to UPDATED_CONTROL_TYPE
        defaultQuestionBaseShouldNotBeFound("controlType.equals=" + UPDATED_CONTROL_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByControlTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where controlType not equals to DEFAULT_CONTROL_TYPE
        defaultQuestionBaseShouldNotBeFound("controlType.notEquals=" + DEFAULT_CONTROL_TYPE);

        // Get all the questionBaseList where controlType not equals to UPDATED_CONTROL_TYPE
        defaultQuestionBaseShouldBeFound("controlType.notEquals=" + UPDATED_CONTROL_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByControlTypeIsInShouldWork() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where controlType in DEFAULT_CONTROL_TYPE or UPDATED_CONTROL_TYPE
        defaultQuestionBaseShouldBeFound("controlType.in=" + DEFAULT_CONTROL_TYPE + "," + UPDATED_CONTROL_TYPE);

        // Get all the questionBaseList where controlType equals to UPDATED_CONTROL_TYPE
        defaultQuestionBaseShouldNotBeFound("controlType.in=" + UPDATED_CONTROL_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByControlTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where controlType is not null
        defaultQuestionBaseShouldBeFound("controlType.specified=true");

        // Get all the questionBaseList where controlType is null
        defaultQuestionBaseShouldNotBeFound("controlType.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionBasesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where placeholder equals to DEFAULT_PLACEHOLDER
        defaultQuestionBaseShouldBeFound("placeholder.equals=" + DEFAULT_PLACEHOLDER);

        // Get all the questionBaseList where placeholder equals to UPDATED_PLACEHOLDER
        defaultQuestionBaseShouldNotBeFound("placeholder.equals=" + UPDATED_PLACEHOLDER);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByPlaceholderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where placeholder not equals to DEFAULT_PLACEHOLDER
        defaultQuestionBaseShouldNotBeFound("placeholder.notEquals=" + DEFAULT_PLACEHOLDER);

        // Get all the questionBaseList where placeholder not equals to UPDATED_PLACEHOLDER
        defaultQuestionBaseShouldBeFound("placeholder.notEquals=" + UPDATED_PLACEHOLDER);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByPlaceholderIsInShouldWork() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where placeholder in DEFAULT_PLACEHOLDER or UPDATED_PLACEHOLDER
        defaultQuestionBaseShouldBeFound("placeholder.in=" + DEFAULT_PLACEHOLDER + "," + UPDATED_PLACEHOLDER);

        // Get all the questionBaseList where placeholder equals to UPDATED_PLACEHOLDER
        defaultQuestionBaseShouldNotBeFound("placeholder.in=" + UPDATED_PLACEHOLDER);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByPlaceholderIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where placeholder is not null
        defaultQuestionBaseShouldBeFound("placeholder.specified=true");

        // Get all the questionBaseList where placeholder is null
        defaultQuestionBaseShouldNotBeFound("placeholder.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionBasesByPlaceholderContainsSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where placeholder contains DEFAULT_PLACEHOLDER
        defaultQuestionBaseShouldBeFound("placeholder.contains=" + DEFAULT_PLACEHOLDER);

        // Get all the questionBaseList where placeholder contains UPDATED_PLACEHOLDER
        defaultQuestionBaseShouldNotBeFound("placeholder.contains=" + UPDATED_PLACEHOLDER);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByPlaceholderNotContainsSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where placeholder does not contain DEFAULT_PLACEHOLDER
        defaultQuestionBaseShouldNotBeFound("placeholder.doesNotContain=" + DEFAULT_PLACEHOLDER);

        // Get all the questionBaseList where placeholder does not contain UPDATED_PLACEHOLDER
        defaultQuestionBaseShouldBeFound("placeholder.doesNotContain=" + UPDATED_PLACEHOLDER);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByIterableIsEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where iterable equals to DEFAULT_ITERABLE
        defaultQuestionBaseShouldBeFound("iterable.equals=" + DEFAULT_ITERABLE);

        // Get all the questionBaseList where iterable equals to UPDATED_ITERABLE
        defaultQuestionBaseShouldNotBeFound("iterable.equals=" + UPDATED_ITERABLE);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByIterableIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where iterable not equals to DEFAULT_ITERABLE
        defaultQuestionBaseShouldNotBeFound("iterable.notEquals=" + DEFAULT_ITERABLE);

        // Get all the questionBaseList where iterable not equals to UPDATED_ITERABLE
        defaultQuestionBaseShouldBeFound("iterable.notEquals=" + UPDATED_ITERABLE);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByIterableIsInShouldWork() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where iterable in DEFAULT_ITERABLE or UPDATED_ITERABLE
        defaultQuestionBaseShouldBeFound("iterable.in=" + DEFAULT_ITERABLE + "," + UPDATED_ITERABLE);

        // Get all the questionBaseList where iterable equals to UPDATED_ITERABLE
        defaultQuestionBaseShouldNotBeFound("iterable.in=" + UPDATED_ITERABLE);
    }

    @Test
    @Transactional
    void getAllQuestionBasesByIterableIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        // Get all the questionBaseList where iterable is not null
        defaultQuestionBaseShouldBeFound("iterable.specified=true");

        // Get all the questionBaseList where iterable is null
        defaultQuestionBaseShouldNotBeFound("iterable.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionBasesByParametersIsEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);
        UniversallyUniqueMapping parameters;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            parameters = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(parameters);
            em.flush();
        } else {
            parameters = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(parameters);
        em.flush();
        questionBase.addParameters(parameters);
        questionBaseRepository.saveAndFlush(questionBase);
        Long parametersId = parameters.getId();

        // Get all the questionBaseList where parameters equals to parametersId
        defaultQuestionBaseShouldBeFound("parametersId.equals=" + parametersId);

        // Get all the questionBaseList where parameters equals to (parametersId + 1)
        defaultQuestionBaseShouldNotBeFound("parametersId.equals=" + (parametersId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionBasesByPlaceholderItemIsEqualToSomething() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);
        Placeholder placeholderItem;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholderItem = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholderItem);
            em.flush();
        } else {
            placeholderItem = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholderItem);
        em.flush();
        questionBase.addPlaceholderItem(placeholderItem);
        questionBaseRepository.saveAndFlush(questionBase);
        Long placeholderItemId = placeholderItem.getId();

        // Get all the questionBaseList where placeholderItem equals to placeholderItemId
        defaultQuestionBaseShouldBeFound("placeholderItemId.equals=" + placeholderItemId);

        // Get all the questionBaseList where placeholderItem equals to (placeholderItemId + 1)
        defaultQuestionBaseShouldNotBeFound("placeholderItemId.equals=" + (placeholderItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionBaseShouldBeFound(String filter) throws Exception {
        restQuestionBaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionBase.getId().intValue())))
            .andExpect(jsonPath("$.[*].context").value(hasItem(DEFAULT_CONTEXT)))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL.toString())))
            .andExpect(jsonPath("$.[*].questionBaseValue").value(hasItem(DEFAULT_QUESTION_BASE_VALUE)))
            .andExpect(jsonPath("$.[*].questionBaseKey").value(hasItem(DEFAULT_QUESTION_BASE_KEY)))
            .andExpect(jsonPath("$.[*].questionBaseLabel").value(hasItem(DEFAULT_QUESTION_BASE_LABEL)))
            .andExpect(jsonPath("$.[*].required").value(hasItem(DEFAULT_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].controlType").value(hasItem(DEFAULT_CONTROL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].placeholder").value(hasItem(DEFAULT_PLACEHOLDER)))
            .andExpect(jsonPath("$.[*].iterable").value(hasItem(DEFAULT_ITERABLE.booleanValue())));

        // Check, that the count call also returns 1
        restQuestionBaseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionBaseShouldNotBeFound(String filter) throws Exception {
        restQuestionBaseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionBaseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestionBase() throws Exception {
        // Get the questionBase
        restQuestionBaseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestionBase() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        int databaseSizeBeforeUpdate = questionBaseRepository.findAll().size();

        // Update the questionBase
        QuestionBase updatedQuestionBase = questionBaseRepository.findById(questionBase.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionBase are not directly saved in db
        em.detach(updatedQuestionBase);
        updatedQuestionBase
            .context(UPDATED_CONTEXT)
            .serial(UPDATED_SERIAL)
            .questionBaseValue(UPDATED_QUESTION_BASE_VALUE)
            .questionBaseKey(UPDATED_QUESTION_BASE_KEY)
            .questionBaseLabel(UPDATED_QUESTION_BASE_LABEL)
            .required(UPDATED_REQUIRED)
            .order(UPDATED_ORDER)
            .controlType(UPDATED_CONTROL_TYPE)
            .placeholder(UPDATED_PLACEHOLDER)
            .iterable(UPDATED_ITERABLE);
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(updatedQuestionBase);

        restQuestionBaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionBaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuestionBase in the database
        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeUpdate);
        QuestionBase testQuestionBase = questionBaseList.get(questionBaseList.size() - 1);
        assertThat(testQuestionBase.getContext()).isEqualTo(UPDATED_CONTEXT);
        assertThat(testQuestionBase.getSerial()).isEqualTo(UPDATED_SERIAL);
        assertThat(testQuestionBase.getQuestionBaseValue()).isEqualTo(UPDATED_QUESTION_BASE_VALUE);
        assertThat(testQuestionBase.getQuestionBaseKey()).isEqualTo(UPDATED_QUESTION_BASE_KEY);
        assertThat(testQuestionBase.getQuestionBaseLabel()).isEqualTo(UPDATED_QUESTION_BASE_LABEL);
        assertThat(testQuestionBase.getRequired()).isEqualTo(UPDATED_REQUIRED);
        assertThat(testQuestionBase.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testQuestionBase.getControlType()).isEqualTo(UPDATED_CONTROL_TYPE);
        assertThat(testQuestionBase.getPlaceholder()).isEqualTo(UPDATED_PLACEHOLDER);
        assertThat(testQuestionBase.getIterable()).isEqualTo(UPDATED_ITERABLE);

        // Validate the QuestionBase in Elasticsearch
        verify(mockQuestionBaseSearchRepository).save(testQuestionBase);
    }

    @Test
    @Transactional
    void putNonExistingQuestionBase() throws Exception {
        int databaseSizeBeforeUpdate = questionBaseRepository.findAll().size();
        questionBase.setId(count.incrementAndGet());

        // Create the QuestionBase
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionBaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionBaseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionBase in the database
        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the QuestionBase in Elasticsearch
        verify(mockQuestionBaseSearchRepository, times(0)).save(questionBase);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionBase() throws Exception {
        int databaseSizeBeforeUpdate = questionBaseRepository.findAll().size();
        questionBase.setId(count.incrementAndGet());

        // Create the QuestionBase
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionBaseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionBase in the database
        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the QuestionBase in Elasticsearch
        verify(mockQuestionBaseSearchRepository, times(0)).save(questionBase);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionBase() throws Exception {
        int databaseSizeBeforeUpdate = questionBaseRepository.findAll().size();
        questionBase.setId(count.incrementAndGet());

        // Create the QuestionBase
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionBaseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionBase in the database
        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the QuestionBase in Elasticsearch
        verify(mockQuestionBaseSearchRepository, times(0)).save(questionBase);
    }

    @Test
    @Transactional
    void partialUpdateQuestionBaseWithPatch() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        int databaseSizeBeforeUpdate = questionBaseRepository.findAll().size();

        // Update the questionBase using partial update
        QuestionBase partialUpdatedQuestionBase = new QuestionBase();
        partialUpdatedQuestionBase.setId(questionBase.getId());

        partialUpdatedQuestionBase
            .questionBaseValue(UPDATED_QUESTION_BASE_VALUE)
            .questionBaseLabel(UPDATED_QUESTION_BASE_LABEL)
            .required(UPDATED_REQUIRED)
            .controlType(UPDATED_CONTROL_TYPE)
            .iterable(UPDATED_ITERABLE);

        restQuestionBaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionBase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionBase))
            )
            .andExpect(status().isOk());

        // Validate the QuestionBase in the database
        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeUpdate);
        QuestionBase testQuestionBase = questionBaseList.get(questionBaseList.size() - 1);
        assertThat(testQuestionBase.getContext()).isEqualTo(DEFAULT_CONTEXT);
        assertThat(testQuestionBase.getSerial()).isEqualTo(DEFAULT_SERIAL);
        assertThat(testQuestionBase.getQuestionBaseValue()).isEqualTo(UPDATED_QUESTION_BASE_VALUE);
        assertThat(testQuestionBase.getQuestionBaseKey()).isEqualTo(DEFAULT_QUESTION_BASE_KEY);
        assertThat(testQuestionBase.getQuestionBaseLabel()).isEqualTo(UPDATED_QUESTION_BASE_LABEL);
        assertThat(testQuestionBase.getRequired()).isEqualTo(UPDATED_REQUIRED);
        assertThat(testQuestionBase.getOrder()).isEqualTo(DEFAULT_ORDER);
        assertThat(testQuestionBase.getControlType()).isEqualTo(UPDATED_CONTROL_TYPE);
        assertThat(testQuestionBase.getPlaceholder()).isEqualTo(DEFAULT_PLACEHOLDER);
        assertThat(testQuestionBase.getIterable()).isEqualTo(UPDATED_ITERABLE);
    }

    @Test
    @Transactional
    void fullUpdateQuestionBaseWithPatch() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        int databaseSizeBeforeUpdate = questionBaseRepository.findAll().size();

        // Update the questionBase using partial update
        QuestionBase partialUpdatedQuestionBase = new QuestionBase();
        partialUpdatedQuestionBase.setId(questionBase.getId());

        partialUpdatedQuestionBase
            .context(UPDATED_CONTEXT)
            .serial(UPDATED_SERIAL)
            .questionBaseValue(UPDATED_QUESTION_BASE_VALUE)
            .questionBaseKey(UPDATED_QUESTION_BASE_KEY)
            .questionBaseLabel(UPDATED_QUESTION_BASE_LABEL)
            .required(UPDATED_REQUIRED)
            .order(UPDATED_ORDER)
            .controlType(UPDATED_CONTROL_TYPE)
            .placeholder(UPDATED_PLACEHOLDER)
            .iterable(UPDATED_ITERABLE);

        restQuestionBaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionBase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionBase))
            )
            .andExpect(status().isOk());

        // Validate the QuestionBase in the database
        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeUpdate);
        QuestionBase testQuestionBase = questionBaseList.get(questionBaseList.size() - 1);
        assertThat(testQuestionBase.getContext()).isEqualTo(UPDATED_CONTEXT);
        assertThat(testQuestionBase.getSerial()).isEqualTo(UPDATED_SERIAL);
        assertThat(testQuestionBase.getQuestionBaseValue()).isEqualTo(UPDATED_QUESTION_BASE_VALUE);
        assertThat(testQuestionBase.getQuestionBaseKey()).isEqualTo(UPDATED_QUESTION_BASE_KEY);
        assertThat(testQuestionBase.getQuestionBaseLabel()).isEqualTo(UPDATED_QUESTION_BASE_LABEL);
        assertThat(testQuestionBase.getRequired()).isEqualTo(UPDATED_REQUIRED);
        assertThat(testQuestionBase.getOrder()).isEqualTo(UPDATED_ORDER);
        assertThat(testQuestionBase.getControlType()).isEqualTo(UPDATED_CONTROL_TYPE);
        assertThat(testQuestionBase.getPlaceholder()).isEqualTo(UPDATED_PLACEHOLDER);
        assertThat(testQuestionBase.getIterable()).isEqualTo(UPDATED_ITERABLE);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionBase() throws Exception {
        int databaseSizeBeforeUpdate = questionBaseRepository.findAll().size();
        questionBase.setId(count.incrementAndGet());

        // Create the QuestionBase
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionBaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionBaseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionBase in the database
        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the QuestionBase in Elasticsearch
        verify(mockQuestionBaseSearchRepository, times(0)).save(questionBase);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionBase() throws Exception {
        int databaseSizeBeforeUpdate = questionBaseRepository.findAll().size();
        questionBase.setId(count.incrementAndGet());

        // Create the QuestionBase
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionBaseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuestionBase in the database
        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the QuestionBase in Elasticsearch
        verify(mockQuestionBaseSearchRepository, times(0)).save(questionBase);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionBase() throws Exception {
        int databaseSizeBeforeUpdate = questionBaseRepository.findAll().size();
        questionBase.setId(count.incrementAndGet());

        // Create the QuestionBase
        QuestionBaseDTO questionBaseDTO = questionBaseMapper.toDto(questionBase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionBaseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionBaseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuestionBase in the database
        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the QuestionBase in Elasticsearch
        verify(mockQuestionBaseSearchRepository, times(0)).save(questionBase);
    }

    @Test
    @Transactional
    void deleteQuestionBase() throws Exception {
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);

        int databaseSizeBeforeDelete = questionBaseRepository.findAll().size();

        // Delete the questionBase
        restQuestionBaseMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionBase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestionBase> questionBaseList = questionBaseRepository.findAll();
        assertThat(questionBaseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the QuestionBase in Elasticsearch
        verify(mockQuestionBaseSearchRepository, times(1)).deleteById(questionBase.getId());
    }

    @Test
    @Transactional
    void searchQuestionBase() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        questionBaseRepository.saveAndFlush(questionBase);
        when(mockQuestionBaseSearchRepository.search("id:" + questionBase.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(questionBase), PageRequest.of(0, 1), 1));

        // Search the questionBase
        restQuestionBaseMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + questionBase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionBase.getId().intValue())))
            .andExpect(jsonPath("$.[*].context").value(hasItem(DEFAULT_CONTEXT)))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL.toString())))
            .andExpect(jsonPath("$.[*].questionBaseValue").value(hasItem(DEFAULT_QUESTION_BASE_VALUE)))
            .andExpect(jsonPath("$.[*].questionBaseKey").value(hasItem(DEFAULT_QUESTION_BASE_KEY)))
            .andExpect(jsonPath("$.[*].questionBaseLabel").value(hasItem(DEFAULT_QUESTION_BASE_LABEL)))
            .andExpect(jsonPath("$.[*].required").value(hasItem(DEFAULT_REQUIRED.booleanValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)))
            .andExpect(jsonPath("$.[*].controlType").value(hasItem(DEFAULT_CONTROL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].placeholder").value(hasItem(DEFAULT_PLACEHOLDER)))
            .andExpect(jsonPath("$.[*].iterable").value(hasItem(DEFAULT_ITERABLE.booleanValue())));
    }
}
