package io.github.erp.erp.resources;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import io.github.erp.domain.TaxRule;
import io.github.erp.repository.TaxRuleRepository;
import io.github.erp.repository.search.TaxRuleSearchRepository;
import io.github.erp.service.TaxRuleService;
import io.github.erp.service.dto.TaxRuleDTO;
import io.github.erp.service.mapper.TaxRuleMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

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

/**
 * Integration tests for the {@link TaxRuleResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"TAX_MODULE_USER"})
class TaxRuleResourceIT {

    private static final Double DEFAULT_TELCO_EXCISE_DUTY = 1D;
    private static final Double UPDATED_TELCO_EXCISE_DUTY = 2D;
    private static final Double SMALLER_TELCO_EXCISE_DUTY = 1D - 1D;

    private static final Double DEFAULT_VALUE_ADDED_TAX = 1D;
    private static final Double UPDATED_VALUE_ADDED_TAX = 2D;
    private static final Double SMALLER_VALUE_ADDED_TAX = 1D - 1D;

    private static final Double DEFAULT_WITHHOLDING_VAT = 1D;
    private static final Double UPDATED_WITHHOLDING_VAT = 2D;
    private static final Double SMALLER_WITHHOLDING_VAT = 1D - 1D;

    private static final Double DEFAULT_WITHHOLDING_TAX_CONSULTANCY = 1D;
    private static final Double UPDATED_WITHHOLDING_TAX_CONSULTANCY = 2D;
    private static final Double SMALLER_WITHHOLDING_TAX_CONSULTANCY = 1D - 1D;

    private static final Double DEFAULT_WITHHOLDING_TAX_RENT = 1D;
    private static final Double UPDATED_WITHHOLDING_TAX_RENT = 2D;
    private static final Double SMALLER_WITHHOLDING_TAX_RENT = 1D - 1D;

    private static final Double DEFAULT_CATERING_LEVY = 1D;
    private static final Double UPDATED_CATERING_LEVY = 2D;
    private static final Double SMALLER_CATERING_LEVY = 1D - 1D;

    private static final Double DEFAULT_SERVICE_CHARGE = 1D;
    private static final Double UPDATED_SERVICE_CHARGE = 2D;
    private static final Double SMALLER_SERVICE_CHARGE = 1D - 1D;

    private static final Double DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE = 1D;
    private static final Double UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE = 2D;
    private static final Double SMALLER_WITHHOLDING_TAX_IMPORTED_SERVICE = 1D - 1D;

    private static final String DEFAULT_FILE_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FILE_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_COMPILATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_COMPILATION_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/taxes/tax-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/taxes/_search/tax-rules";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaxRuleRepository taxRuleRepository;

    @Mock
    private TaxRuleRepository taxRuleRepositoryMock;

    @Autowired
    private TaxRuleMapper taxRuleMapper;

    @Mock
    private TaxRuleService taxRuleServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.TaxRuleSearchRepositoryMockConfiguration
     */
    @Autowired
    private TaxRuleSearchRepository mockTaxRuleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaxRuleMockMvc;

    private TaxRule taxRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxRule createEntity(EntityManager em) {
        TaxRule taxRule = new TaxRule()
            .telcoExciseDuty(DEFAULT_TELCO_EXCISE_DUTY)
            .valueAddedTax(DEFAULT_VALUE_ADDED_TAX)
            .withholdingVAT(DEFAULT_WITHHOLDING_VAT)
            .withholdingTaxConsultancy(DEFAULT_WITHHOLDING_TAX_CONSULTANCY)
            .withholdingTaxRent(DEFAULT_WITHHOLDING_TAX_RENT)
            .cateringLevy(DEFAULT_CATERING_LEVY)
            .serviceCharge(DEFAULT_SERVICE_CHARGE)
            .withholdingTaxImportedService(DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE)
            .fileUploadToken(DEFAULT_FILE_UPLOAD_TOKEN)
            .compilationToken(DEFAULT_COMPILATION_TOKEN);
        return taxRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxRule createUpdatedEntity(EntityManager em) {
        TaxRule taxRule = new TaxRule()
            .telcoExciseDuty(UPDATED_TELCO_EXCISE_DUTY)
            .valueAddedTax(UPDATED_VALUE_ADDED_TAX)
            .withholdingVAT(UPDATED_WITHHOLDING_VAT)
            .withholdingTaxConsultancy(UPDATED_WITHHOLDING_TAX_CONSULTANCY)
            .withholdingTaxRent(UPDATED_WITHHOLDING_TAX_RENT)
            .cateringLevy(UPDATED_CATERING_LEVY)
            .serviceCharge(UPDATED_SERVICE_CHARGE)
            .withholdingTaxImportedService(UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        return taxRule;
    }

    @BeforeEach
    public void initTest() {
        taxRule = createEntity(em);
    }

    @Test
    @Transactional
    void createTaxRule() throws Exception {
        int databaseSizeBeforeCreate = taxRuleRepository.findAll().size();
        // Create the TaxRule
        TaxRuleDTO taxRuleDTO = taxRuleMapper.toDto(taxRule);
        restTaxRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxRuleDTO)))
            .andExpect(status().isCreated());

        // Validate the TaxRule in the database
        List<TaxRule> taxRuleList = taxRuleRepository.findAll();
        assertThat(taxRuleList).hasSize(databaseSizeBeforeCreate + 1);
        TaxRule testTaxRule = taxRuleList.get(taxRuleList.size() - 1);
        assertThat(testTaxRule.getTelcoExciseDuty()).isEqualTo(DEFAULT_TELCO_EXCISE_DUTY);
        assertThat(testTaxRule.getValueAddedTax()).isEqualTo(DEFAULT_VALUE_ADDED_TAX);
        assertThat(testTaxRule.getWithholdingVAT()).isEqualTo(DEFAULT_WITHHOLDING_VAT);
        assertThat(testTaxRule.getWithholdingTaxConsultancy()).isEqualTo(DEFAULT_WITHHOLDING_TAX_CONSULTANCY);
        assertThat(testTaxRule.getWithholdingTaxRent()).isEqualTo(DEFAULT_WITHHOLDING_TAX_RENT);
        assertThat(testTaxRule.getCateringLevy()).isEqualTo(DEFAULT_CATERING_LEVY);
        assertThat(testTaxRule.getServiceCharge()).isEqualTo(DEFAULT_SERVICE_CHARGE);
        assertThat(testTaxRule.getWithholdingTaxImportedService()).isEqualTo(DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE);
        assertThat(testTaxRule.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testTaxRule.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);

        // Validate the TaxRule in Elasticsearch
        verify(mockTaxRuleSearchRepository, times(1)).save(testTaxRule);
    }

    @Test
    @Transactional
    void createTaxRuleWithExistingId() throws Exception {
        // Create the TaxRule with an existing ID
        taxRule.setId(1L);
        TaxRuleDTO taxRuleDTO = taxRuleMapper.toDto(taxRule);

        int databaseSizeBeforeCreate = taxRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaxRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxRuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TaxRule in the database
        List<TaxRule> taxRuleList = taxRuleRepository.findAll();
        assertThat(taxRuleList).hasSize(databaseSizeBeforeCreate);

        // Validate the TaxRule in Elasticsearch
        verify(mockTaxRuleSearchRepository, times(0)).save(taxRule);
    }

    @Test
    @Transactional
    void getAllTaxRules() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList
        restTaxRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].telcoExciseDuty").value(hasItem(DEFAULT_TELCO_EXCISE_DUTY.doubleValue())))
            .andExpect(jsonPath("$.[*].valueAddedTax").value(hasItem(DEFAULT_VALUE_ADDED_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].withholdingVAT").value(hasItem(DEFAULT_WITHHOLDING_VAT.doubleValue())))
            .andExpect(jsonPath("$.[*].withholdingTaxConsultancy").value(hasItem(DEFAULT_WITHHOLDING_TAX_CONSULTANCY.doubleValue())))
            .andExpect(jsonPath("$.[*].withholdingTaxRent").value(hasItem(DEFAULT_WITHHOLDING_TAX_RENT.doubleValue())))
            .andExpect(jsonPath("$.[*].cateringLevy").value(hasItem(DEFAULT_CATERING_LEVY.doubleValue())))
            .andExpect(jsonPath("$.[*].serviceCharge").value(hasItem(DEFAULT_SERVICE_CHARGE.doubleValue())))
            .andExpect(
                jsonPath("$.[*].withholdingTaxImportedService").value(hasItem(DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE.doubleValue()))
            )
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxRulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(taxRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTaxRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(taxRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxRulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(taxRuleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTaxRuleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(taxRuleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTaxRule() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get the taxRule
        restTaxRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, taxRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(taxRule.getId().intValue()))
            .andExpect(jsonPath("$.telcoExciseDuty").value(DEFAULT_TELCO_EXCISE_DUTY.doubleValue()))
            .andExpect(jsonPath("$.valueAddedTax").value(DEFAULT_VALUE_ADDED_TAX.doubleValue()))
            .andExpect(jsonPath("$.withholdingVAT").value(DEFAULT_WITHHOLDING_VAT.doubleValue()))
            .andExpect(jsonPath("$.withholdingTaxConsultancy").value(DEFAULT_WITHHOLDING_TAX_CONSULTANCY.doubleValue()))
            .andExpect(jsonPath("$.withholdingTaxRent").value(DEFAULT_WITHHOLDING_TAX_RENT.doubleValue()))
            .andExpect(jsonPath("$.cateringLevy").value(DEFAULT_CATERING_LEVY.doubleValue()))
            .andExpect(jsonPath("$.serviceCharge").value(DEFAULT_SERVICE_CHARGE.doubleValue()))
            .andExpect(jsonPath("$.withholdingTaxImportedService").value(DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE.doubleValue()))
            .andExpect(jsonPath("$.fileUploadToken").value(DEFAULT_FILE_UPLOAD_TOKEN))
            .andExpect(jsonPath("$.compilationToken").value(DEFAULT_COMPILATION_TOKEN));
    }

    @Test
    @Transactional
    void getTaxRulesByIdFiltering() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        Long id = taxRule.getId();

        defaultTaxRuleShouldBeFound("id.equals=" + id);
        defaultTaxRuleShouldNotBeFound("id.notEquals=" + id);

        defaultTaxRuleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaxRuleShouldNotBeFound("id.greaterThan=" + id);

        defaultTaxRuleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaxRuleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTaxRulesByTelcoExciseDutyIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where telcoExciseDuty equals to DEFAULT_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldBeFound("telcoExciseDuty.equals=" + DEFAULT_TELCO_EXCISE_DUTY);

        // Get all the taxRuleList where telcoExciseDuty equals to UPDATED_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldNotBeFound("telcoExciseDuty.equals=" + UPDATED_TELCO_EXCISE_DUTY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByTelcoExciseDutyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where telcoExciseDuty not equals to DEFAULT_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldNotBeFound("telcoExciseDuty.notEquals=" + DEFAULT_TELCO_EXCISE_DUTY);

        // Get all the taxRuleList where telcoExciseDuty not equals to UPDATED_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldBeFound("telcoExciseDuty.notEquals=" + UPDATED_TELCO_EXCISE_DUTY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByTelcoExciseDutyIsInShouldWork() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where telcoExciseDuty in DEFAULT_TELCO_EXCISE_DUTY or UPDATED_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldBeFound("telcoExciseDuty.in=" + DEFAULT_TELCO_EXCISE_DUTY + "," + UPDATED_TELCO_EXCISE_DUTY);

        // Get all the taxRuleList where telcoExciseDuty equals to UPDATED_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldNotBeFound("telcoExciseDuty.in=" + UPDATED_TELCO_EXCISE_DUTY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByTelcoExciseDutyIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where telcoExciseDuty is not null
        defaultTaxRuleShouldBeFound("telcoExciseDuty.specified=true");

        // Get all the taxRuleList where telcoExciseDuty is null
        defaultTaxRuleShouldNotBeFound("telcoExciseDuty.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRulesByTelcoExciseDutyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where telcoExciseDuty is greater than or equal to DEFAULT_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldBeFound("telcoExciseDuty.greaterThanOrEqual=" + DEFAULT_TELCO_EXCISE_DUTY);

        // Get all the taxRuleList where telcoExciseDuty is greater than or equal to UPDATED_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldNotBeFound("telcoExciseDuty.greaterThanOrEqual=" + UPDATED_TELCO_EXCISE_DUTY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByTelcoExciseDutyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where telcoExciseDuty is less than or equal to DEFAULT_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldBeFound("telcoExciseDuty.lessThanOrEqual=" + DEFAULT_TELCO_EXCISE_DUTY);

        // Get all the taxRuleList where telcoExciseDuty is less than or equal to SMALLER_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldNotBeFound("telcoExciseDuty.lessThanOrEqual=" + SMALLER_TELCO_EXCISE_DUTY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByTelcoExciseDutyIsLessThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where telcoExciseDuty is less than DEFAULT_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldNotBeFound("telcoExciseDuty.lessThan=" + DEFAULT_TELCO_EXCISE_DUTY);

        // Get all the taxRuleList where telcoExciseDuty is less than UPDATED_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldBeFound("telcoExciseDuty.lessThan=" + UPDATED_TELCO_EXCISE_DUTY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByTelcoExciseDutyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where telcoExciseDuty is greater than DEFAULT_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldNotBeFound("telcoExciseDuty.greaterThan=" + DEFAULT_TELCO_EXCISE_DUTY);

        // Get all the taxRuleList where telcoExciseDuty is greater than SMALLER_TELCO_EXCISE_DUTY
        defaultTaxRuleShouldBeFound("telcoExciseDuty.greaterThan=" + SMALLER_TELCO_EXCISE_DUTY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByValueAddedTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where valueAddedTax equals to DEFAULT_VALUE_ADDED_TAX
        defaultTaxRuleShouldBeFound("valueAddedTax.equals=" + DEFAULT_VALUE_ADDED_TAX);

        // Get all the taxRuleList where valueAddedTax equals to UPDATED_VALUE_ADDED_TAX
        defaultTaxRuleShouldNotBeFound("valueAddedTax.equals=" + UPDATED_VALUE_ADDED_TAX);
    }

    @Test
    @Transactional
    void getAllTaxRulesByValueAddedTaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where valueAddedTax not equals to DEFAULT_VALUE_ADDED_TAX
        defaultTaxRuleShouldNotBeFound("valueAddedTax.notEquals=" + DEFAULT_VALUE_ADDED_TAX);

        // Get all the taxRuleList where valueAddedTax not equals to UPDATED_VALUE_ADDED_TAX
        defaultTaxRuleShouldBeFound("valueAddedTax.notEquals=" + UPDATED_VALUE_ADDED_TAX);
    }

    @Test
    @Transactional
    void getAllTaxRulesByValueAddedTaxIsInShouldWork() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where valueAddedTax in DEFAULT_VALUE_ADDED_TAX or UPDATED_VALUE_ADDED_TAX
        defaultTaxRuleShouldBeFound("valueAddedTax.in=" + DEFAULT_VALUE_ADDED_TAX + "," + UPDATED_VALUE_ADDED_TAX);

        // Get all the taxRuleList where valueAddedTax equals to UPDATED_VALUE_ADDED_TAX
        defaultTaxRuleShouldNotBeFound("valueAddedTax.in=" + UPDATED_VALUE_ADDED_TAX);
    }

    @Test
    @Transactional
    void getAllTaxRulesByValueAddedTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where valueAddedTax is not null
        defaultTaxRuleShouldBeFound("valueAddedTax.specified=true");

        // Get all the taxRuleList where valueAddedTax is null
        defaultTaxRuleShouldNotBeFound("valueAddedTax.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRulesByValueAddedTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where valueAddedTax is greater than or equal to DEFAULT_VALUE_ADDED_TAX
        defaultTaxRuleShouldBeFound("valueAddedTax.greaterThanOrEqual=" + DEFAULT_VALUE_ADDED_TAX);

        // Get all the taxRuleList where valueAddedTax is greater than or equal to UPDATED_VALUE_ADDED_TAX
        defaultTaxRuleShouldNotBeFound("valueAddedTax.greaterThanOrEqual=" + UPDATED_VALUE_ADDED_TAX);
    }

    @Test
    @Transactional
    void getAllTaxRulesByValueAddedTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where valueAddedTax is less than or equal to DEFAULT_VALUE_ADDED_TAX
        defaultTaxRuleShouldBeFound("valueAddedTax.lessThanOrEqual=" + DEFAULT_VALUE_ADDED_TAX);

        // Get all the taxRuleList where valueAddedTax is less than or equal to SMALLER_VALUE_ADDED_TAX
        defaultTaxRuleShouldNotBeFound("valueAddedTax.lessThanOrEqual=" + SMALLER_VALUE_ADDED_TAX);
    }

    @Test
    @Transactional
    void getAllTaxRulesByValueAddedTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where valueAddedTax is less than DEFAULT_VALUE_ADDED_TAX
        defaultTaxRuleShouldNotBeFound("valueAddedTax.lessThan=" + DEFAULT_VALUE_ADDED_TAX);

        // Get all the taxRuleList where valueAddedTax is less than UPDATED_VALUE_ADDED_TAX
        defaultTaxRuleShouldBeFound("valueAddedTax.lessThan=" + UPDATED_VALUE_ADDED_TAX);
    }

    @Test
    @Transactional
    void getAllTaxRulesByValueAddedTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where valueAddedTax is greater than DEFAULT_VALUE_ADDED_TAX
        defaultTaxRuleShouldNotBeFound("valueAddedTax.greaterThan=" + DEFAULT_VALUE_ADDED_TAX);

        // Get all the taxRuleList where valueAddedTax is greater than SMALLER_VALUE_ADDED_TAX
        defaultTaxRuleShouldBeFound("valueAddedTax.greaterThan=" + SMALLER_VALUE_ADDED_TAX);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingVATIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingVAT equals to DEFAULT_WITHHOLDING_VAT
        defaultTaxRuleShouldBeFound("withholdingVAT.equals=" + DEFAULT_WITHHOLDING_VAT);

        // Get all the taxRuleList where withholdingVAT equals to UPDATED_WITHHOLDING_VAT
        defaultTaxRuleShouldNotBeFound("withholdingVAT.equals=" + UPDATED_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingVATIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingVAT not equals to DEFAULT_WITHHOLDING_VAT
        defaultTaxRuleShouldNotBeFound("withholdingVAT.notEquals=" + DEFAULT_WITHHOLDING_VAT);

        // Get all the taxRuleList where withholdingVAT not equals to UPDATED_WITHHOLDING_VAT
        defaultTaxRuleShouldBeFound("withholdingVAT.notEquals=" + UPDATED_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingVATIsInShouldWork() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingVAT in DEFAULT_WITHHOLDING_VAT or UPDATED_WITHHOLDING_VAT
        defaultTaxRuleShouldBeFound("withholdingVAT.in=" + DEFAULT_WITHHOLDING_VAT + "," + UPDATED_WITHHOLDING_VAT);

        // Get all the taxRuleList where withholdingVAT equals to UPDATED_WITHHOLDING_VAT
        defaultTaxRuleShouldNotBeFound("withholdingVAT.in=" + UPDATED_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingVATIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingVAT is not null
        defaultTaxRuleShouldBeFound("withholdingVAT.specified=true");

        // Get all the taxRuleList where withholdingVAT is null
        defaultTaxRuleShouldNotBeFound("withholdingVAT.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingVATIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingVAT is greater than or equal to DEFAULT_WITHHOLDING_VAT
        defaultTaxRuleShouldBeFound("withholdingVAT.greaterThanOrEqual=" + DEFAULT_WITHHOLDING_VAT);

        // Get all the taxRuleList where withholdingVAT is greater than or equal to UPDATED_WITHHOLDING_VAT
        defaultTaxRuleShouldNotBeFound("withholdingVAT.greaterThanOrEqual=" + UPDATED_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingVATIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingVAT is less than or equal to DEFAULT_WITHHOLDING_VAT
        defaultTaxRuleShouldBeFound("withholdingVAT.lessThanOrEqual=" + DEFAULT_WITHHOLDING_VAT);

        // Get all the taxRuleList where withholdingVAT is less than or equal to SMALLER_WITHHOLDING_VAT
        defaultTaxRuleShouldNotBeFound("withholdingVAT.lessThanOrEqual=" + SMALLER_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingVATIsLessThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingVAT is less than DEFAULT_WITHHOLDING_VAT
        defaultTaxRuleShouldNotBeFound("withholdingVAT.lessThan=" + DEFAULT_WITHHOLDING_VAT);

        // Get all the taxRuleList where withholdingVAT is less than UPDATED_WITHHOLDING_VAT
        defaultTaxRuleShouldBeFound("withholdingVAT.lessThan=" + UPDATED_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingVATIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingVAT is greater than DEFAULT_WITHHOLDING_VAT
        defaultTaxRuleShouldNotBeFound("withholdingVAT.greaterThan=" + DEFAULT_WITHHOLDING_VAT);

        // Get all the taxRuleList where withholdingVAT is greater than SMALLER_WITHHOLDING_VAT
        defaultTaxRuleShouldBeFound("withholdingVAT.greaterThan=" + SMALLER_WITHHOLDING_VAT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxConsultancyIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxConsultancy equals to DEFAULT_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldBeFound("withholdingTaxConsultancy.equals=" + DEFAULT_WITHHOLDING_TAX_CONSULTANCY);

        // Get all the taxRuleList where withholdingTaxConsultancy equals to UPDATED_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldNotBeFound("withholdingTaxConsultancy.equals=" + UPDATED_WITHHOLDING_TAX_CONSULTANCY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxConsultancyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxConsultancy not equals to DEFAULT_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldNotBeFound("withholdingTaxConsultancy.notEquals=" + DEFAULT_WITHHOLDING_TAX_CONSULTANCY);

        // Get all the taxRuleList where withholdingTaxConsultancy not equals to UPDATED_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldBeFound("withholdingTaxConsultancy.notEquals=" + UPDATED_WITHHOLDING_TAX_CONSULTANCY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxConsultancyIsInShouldWork() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxConsultancy in DEFAULT_WITHHOLDING_TAX_CONSULTANCY or UPDATED_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldBeFound(
            "withholdingTaxConsultancy.in=" + DEFAULT_WITHHOLDING_TAX_CONSULTANCY + "," + UPDATED_WITHHOLDING_TAX_CONSULTANCY
        );

        // Get all the taxRuleList where withholdingTaxConsultancy equals to UPDATED_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldNotBeFound("withholdingTaxConsultancy.in=" + UPDATED_WITHHOLDING_TAX_CONSULTANCY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxConsultancyIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxConsultancy is not null
        defaultTaxRuleShouldBeFound("withholdingTaxConsultancy.specified=true");

        // Get all the taxRuleList where withholdingTaxConsultancy is null
        defaultTaxRuleShouldNotBeFound("withholdingTaxConsultancy.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxConsultancyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxConsultancy is greater than or equal to DEFAULT_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldBeFound("withholdingTaxConsultancy.greaterThanOrEqual=" + DEFAULT_WITHHOLDING_TAX_CONSULTANCY);

        // Get all the taxRuleList where withholdingTaxConsultancy is greater than or equal to UPDATED_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldNotBeFound("withholdingTaxConsultancy.greaterThanOrEqual=" + UPDATED_WITHHOLDING_TAX_CONSULTANCY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxConsultancyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxConsultancy is less than or equal to DEFAULT_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldBeFound("withholdingTaxConsultancy.lessThanOrEqual=" + DEFAULT_WITHHOLDING_TAX_CONSULTANCY);

        // Get all the taxRuleList where withholdingTaxConsultancy is less than or equal to SMALLER_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldNotBeFound("withholdingTaxConsultancy.lessThanOrEqual=" + SMALLER_WITHHOLDING_TAX_CONSULTANCY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxConsultancyIsLessThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxConsultancy is less than DEFAULT_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldNotBeFound("withholdingTaxConsultancy.lessThan=" + DEFAULT_WITHHOLDING_TAX_CONSULTANCY);

        // Get all the taxRuleList where withholdingTaxConsultancy is less than UPDATED_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldBeFound("withholdingTaxConsultancy.lessThan=" + UPDATED_WITHHOLDING_TAX_CONSULTANCY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxConsultancyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxConsultancy is greater than DEFAULT_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldNotBeFound("withholdingTaxConsultancy.greaterThan=" + DEFAULT_WITHHOLDING_TAX_CONSULTANCY);

        // Get all the taxRuleList where withholdingTaxConsultancy is greater than SMALLER_WITHHOLDING_TAX_CONSULTANCY
        defaultTaxRuleShouldBeFound("withholdingTaxConsultancy.greaterThan=" + SMALLER_WITHHOLDING_TAX_CONSULTANCY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxRentIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxRent equals to DEFAULT_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldBeFound("withholdingTaxRent.equals=" + DEFAULT_WITHHOLDING_TAX_RENT);

        // Get all the taxRuleList where withholdingTaxRent equals to UPDATED_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldNotBeFound("withholdingTaxRent.equals=" + UPDATED_WITHHOLDING_TAX_RENT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxRentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxRent not equals to DEFAULT_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldNotBeFound("withholdingTaxRent.notEquals=" + DEFAULT_WITHHOLDING_TAX_RENT);

        // Get all the taxRuleList where withholdingTaxRent not equals to UPDATED_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldBeFound("withholdingTaxRent.notEquals=" + UPDATED_WITHHOLDING_TAX_RENT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxRentIsInShouldWork() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxRent in DEFAULT_WITHHOLDING_TAX_RENT or UPDATED_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldBeFound("withholdingTaxRent.in=" + DEFAULT_WITHHOLDING_TAX_RENT + "," + UPDATED_WITHHOLDING_TAX_RENT);

        // Get all the taxRuleList where withholdingTaxRent equals to UPDATED_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldNotBeFound("withholdingTaxRent.in=" + UPDATED_WITHHOLDING_TAX_RENT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxRentIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxRent is not null
        defaultTaxRuleShouldBeFound("withholdingTaxRent.specified=true");

        // Get all the taxRuleList where withholdingTaxRent is null
        defaultTaxRuleShouldNotBeFound("withholdingTaxRent.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxRentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxRent is greater than or equal to DEFAULT_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldBeFound("withholdingTaxRent.greaterThanOrEqual=" + DEFAULT_WITHHOLDING_TAX_RENT);

        // Get all the taxRuleList where withholdingTaxRent is greater than or equal to UPDATED_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldNotBeFound("withholdingTaxRent.greaterThanOrEqual=" + UPDATED_WITHHOLDING_TAX_RENT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxRentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxRent is less than or equal to DEFAULT_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldBeFound("withholdingTaxRent.lessThanOrEqual=" + DEFAULT_WITHHOLDING_TAX_RENT);

        // Get all the taxRuleList where withholdingTaxRent is less than or equal to SMALLER_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldNotBeFound("withholdingTaxRent.lessThanOrEqual=" + SMALLER_WITHHOLDING_TAX_RENT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxRentIsLessThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxRent is less than DEFAULT_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldNotBeFound("withholdingTaxRent.lessThan=" + DEFAULT_WITHHOLDING_TAX_RENT);

        // Get all the taxRuleList where withholdingTaxRent is less than UPDATED_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldBeFound("withholdingTaxRent.lessThan=" + UPDATED_WITHHOLDING_TAX_RENT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxRentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxRent is greater than DEFAULT_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldNotBeFound("withholdingTaxRent.greaterThan=" + DEFAULT_WITHHOLDING_TAX_RENT);

        // Get all the taxRuleList where withholdingTaxRent is greater than SMALLER_WITHHOLDING_TAX_RENT
        defaultTaxRuleShouldBeFound("withholdingTaxRent.greaterThan=" + SMALLER_WITHHOLDING_TAX_RENT);
    }

    @Test
    @Transactional
    void getAllTaxRulesByCateringLevyIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where cateringLevy equals to DEFAULT_CATERING_LEVY
        defaultTaxRuleShouldBeFound("cateringLevy.equals=" + DEFAULT_CATERING_LEVY);

        // Get all the taxRuleList where cateringLevy equals to UPDATED_CATERING_LEVY
        defaultTaxRuleShouldNotBeFound("cateringLevy.equals=" + UPDATED_CATERING_LEVY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByCateringLevyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where cateringLevy not equals to DEFAULT_CATERING_LEVY
        defaultTaxRuleShouldNotBeFound("cateringLevy.notEquals=" + DEFAULT_CATERING_LEVY);

        // Get all the taxRuleList where cateringLevy not equals to UPDATED_CATERING_LEVY
        defaultTaxRuleShouldBeFound("cateringLevy.notEquals=" + UPDATED_CATERING_LEVY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByCateringLevyIsInShouldWork() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where cateringLevy in DEFAULT_CATERING_LEVY or UPDATED_CATERING_LEVY
        defaultTaxRuleShouldBeFound("cateringLevy.in=" + DEFAULT_CATERING_LEVY + "," + UPDATED_CATERING_LEVY);

        // Get all the taxRuleList where cateringLevy equals to UPDATED_CATERING_LEVY
        defaultTaxRuleShouldNotBeFound("cateringLevy.in=" + UPDATED_CATERING_LEVY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByCateringLevyIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where cateringLevy is not null
        defaultTaxRuleShouldBeFound("cateringLevy.specified=true");

        // Get all the taxRuleList where cateringLevy is null
        defaultTaxRuleShouldNotBeFound("cateringLevy.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRulesByCateringLevyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where cateringLevy is greater than or equal to DEFAULT_CATERING_LEVY
        defaultTaxRuleShouldBeFound("cateringLevy.greaterThanOrEqual=" + DEFAULT_CATERING_LEVY);

        // Get all the taxRuleList where cateringLevy is greater than or equal to UPDATED_CATERING_LEVY
        defaultTaxRuleShouldNotBeFound("cateringLevy.greaterThanOrEqual=" + UPDATED_CATERING_LEVY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByCateringLevyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where cateringLevy is less than or equal to DEFAULT_CATERING_LEVY
        defaultTaxRuleShouldBeFound("cateringLevy.lessThanOrEqual=" + DEFAULT_CATERING_LEVY);

        // Get all the taxRuleList where cateringLevy is less than or equal to SMALLER_CATERING_LEVY
        defaultTaxRuleShouldNotBeFound("cateringLevy.lessThanOrEqual=" + SMALLER_CATERING_LEVY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByCateringLevyIsLessThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where cateringLevy is less than DEFAULT_CATERING_LEVY
        defaultTaxRuleShouldNotBeFound("cateringLevy.lessThan=" + DEFAULT_CATERING_LEVY);

        // Get all the taxRuleList where cateringLevy is less than UPDATED_CATERING_LEVY
        defaultTaxRuleShouldBeFound("cateringLevy.lessThan=" + UPDATED_CATERING_LEVY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByCateringLevyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where cateringLevy is greater than DEFAULT_CATERING_LEVY
        defaultTaxRuleShouldNotBeFound("cateringLevy.greaterThan=" + DEFAULT_CATERING_LEVY);

        // Get all the taxRuleList where cateringLevy is greater than SMALLER_CATERING_LEVY
        defaultTaxRuleShouldBeFound("cateringLevy.greaterThan=" + SMALLER_CATERING_LEVY);
    }

    @Test
    @Transactional
    void getAllTaxRulesByServiceChargeIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where serviceCharge equals to DEFAULT_SERVICE_CHARGE
        defaultTaxRuleShouldBeFound("serviceCharge.equals=" + DEFAULT_SERVICE_CHARGE);

        // Get all the taxRuleList where serviceCharge equals to UPDATED_SERVICE_CHARGE
        defaultTaxRuleShouldNotBeFound("serviceCharge.equals=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByServiceChargeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where serviceCharge not equals to DEFAULT_SERVICE_CHARGE
        defaultTaxRuleShouldNotBeFound("serviceCharge.notEquals=" + DEFAULT_SERVICE_CHARGE);

        // Get all the taxRuleList where serviceCharge not equals to UPDATED_SERVICE_CHARGE
        defaultTaxRuleShouldBeFound("serviceCharge.notEquals=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByServiceChargeIsInShouldWork() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where serviceCharge in DEFAULT_SERVICE_CHARGE or UPDATED_SERVICE_CHARGE
        defaultTaxRuleShouldBeFound("serviceCharge.in=" + DEFAULT_SERVICE_CHARGE + "," + UPDATED_SERVICE_CHARGE);

        // Get all the taxRuleList where serviceCharge equals to UPDATED_SERVICE_CHARGE
        defaultTaxRuleShouldNotBeFound("serviceCharge.in=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByServiceChargeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where serviceCharge is not null
        defaultTaxRuleShouldBeFound("serviceCharge.specified=true");

        // Get all the taxRuleList where serviceCharge is null
        defaultTaxRuleShouldNotBeFound("serviceCharge.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRulesByServiceChargeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where serviceCharge is greater than or equal to DEFAULT_SERVICE_CHARGE
        defaultTaxRuleShouldBeFound("serviceCharge.greaterThanOrEqual=" + DEFAULT_SERVICE_CHARGE);

        // Get all the taxRuleList where serviceCharge is greater than or equal to UPDATED_SERVICE_CHARGE
        defaultTaxRuleShouldNotBeFound("serviceCharge.greaterThanOrEqual=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByServiceChargeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where serviceCharge is less than or equal to DEFAULT_SERVICE_CHARGE
        defaultTaxRuleShouldBeFound("serviceCharge.lessThanOrEqual=" + DEFAULT_SERVICE_CHARGE);

        // Get all the taxRuleList where serviceCharge is less than or equal to SMALLER_SERVICE_CHARGE
        defaultTaxRuleShouldNotBeFound("serviceCharge.lessThanOrEqual=" + SMALLER_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByServiceChargeIsLessThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where serviceCharge is less than DEFAULT_SERVICE_CHARGE
        defaultTaxRuleShouldNotBeFound("serviceCharge.lessThan=" + DEFAULT_SERVICE_CHARGE);

        // Get all the taxRuleList where serviceCharge is less than UPDATED_SERVICE_CHARGE
        defaultTaxRuleShouldBeFound("serviceCharge.lessThan=" + UPDATED_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByServiceChargeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where serviceCharge is greater than DEFAULT_SERVICE_CHARGE
        defaultTaxRuleShouldNotBeFound("serviceCharge.greaterThan=" + DEFAULT_SERVICE_CHARGE);

        // Get all the taxRuleList where serviceCharge is greater than SMALLER_SERVICE_CHARGE
        defaultTaxRuleShouldBeFound("serviceCharge.greaterThan=" + SMALLER_SERVICE_CHARGE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxImportedServiceIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxImportedService equals to DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldBeFound("withholdingTaxImportedService.equals=" + DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE);

        // Get all the taxRuleList where withholdingTaxImportedService equals to UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldNotBeFound("withholdingTaxImportedService.equals=" + UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxImportedServiceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxImportedService not equals to DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldNotBeFound("withholdingTaxImportedService.notEquals=" + DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE);

        // Get all the taxRuleList where withholdingTaxImportedService not equals to UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldBeFound("withholdingTaxImportedService.notEquals=" + UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxImportedServiceIsInShouldWork() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxImportedService in DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE or UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldBeFound(
            "withholdingTaxImportedService.in=" + DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE + "," + UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE
        );

        // Get all the taxRuleList where withholdingTaxImportedService equals to UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldNotBeFound("withholdingTaxImportedService.in=" + UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxImportedServiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxImportedService is not null
        defaultTaxRuleShouldBeFound("withholdingTaxImportedService.specified=true");

        // Get all the taxRuleList where withholdingTaxImportedService is null
        defaultTaxRuleShouldNotBeFound("withholdingTaxImportedService.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxImportedServiceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxImportedService is greater than or equal to DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldBeFound("withholdingTaxImportedService.greaterThanOrEqual=" + DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE);

        // Get all the taxRuleList where withholdingTaxImportedService is greater than or equal to UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldNotBeFound("withholdingTaxImportedService.greaterThanOrEqual=" + UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxImportedServiceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxImportedService is less than or equal to DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldBeFound("withholdingTaxImportedService.lessThanOrEqual=" + DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE);

        // Get all the taxRuleList where withholdingTaxImportedService is less than or equal to SMALLER_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldNotBeFound("withholdingTaxImportedService.lessThanOrEqual=" + SMALLER_WITHHOLDING_TAX_IMPORTED_SERVICE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxImportedServiceIsLessThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxImportedService is less than DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldNotBeFound("withholdingTaxImportedService.lessThan=" + DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE);

        // Get all the taxRuleList where withholdingTaxImportedService is less than UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldBeFound("withholdingTaxImportedService.lessThan=" + UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByWithholdingTaxImportedServiceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where withholdingTaxImportedService is greater than DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldNotBeFound("withholdingTaxImportedService.greaterThan=" + DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE);

        // Get all the taxRuleList where withholdingTaxImportedService is greater than SMALLER_WITHHOLDING_TAX_IMPORTED_SERVICE
        defaultTaxRuleShouldBeFound("withholdingTaxImportedService.greaterThan=" + SMALLER_WITHHOLDING_TAX_IMPORTED_SERVICE);
    }

    @Test
    @Transactional
    void getAllTaxRulesByFileUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where fileUploadToken equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultTaxRuleShouldBeFound("fileUploadToken.equals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the taxRuleList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultTaxRuleShouldNotBeFound("fileUploadToken.equals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxRulesByFileUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where fileUploadToken not equals to DEFAULT_FILE_UPLOAD_TOKEN
        defaultTaxRuleShouldNotBeFound("fileUploadToken.notEquals=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the taxRuleList where fileUploadToken not equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultTaxRuleShouldBeFound("fileUploadToken.notEquals=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxRulesByFileUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where fileUploadToken in DEFAULT_FILE_UPLOAD_TOKEN or UPDATED_FILE_UPLOAD_TOKEN
        defaultTaxRuleShouldBeFound("fileUploadToken.in=" + DEFAULT_FILE_UPLOAD_TOKEN + "," + UPDATED_FILE_UPLOAD_TOKEN);

        // Get all the taxRuleList where fileUploadToken equals to UPDATED_FILE_UPLOAD_TOKEN
        defaultTaxRuleShouldNotBeFound("fileUploadToken.in=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxRulesByFileUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where fileUploadToken is not null
        defaultTaxRuleShouldBeFound("fileUploadToken.specified=true");

        // Get all the taxRuleList where fileUploadToken is null
        defaultTaxRuleShouldNotBeFound("fileUploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRulesByFileUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where fileUploadToken contains DEFAULT_FILE_UPLOAD_TOKEN
        defaultTaxRuleShouldBeFound("fileUploadToken.contains=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the taxRuleList where fileUploadToken contains UPDATED_FILE_UPLOAD_TOKEN
        defaultTaxRuleShouldNotBeFound("fileUploadToken.contains=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxRulesByFileUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where fileUploadToken does not contain DEFAULT_FILE_UPLOAD_TOKEN
        defaultTaxRuleShouldNotBeFound("fileUploadToken.doesNotContain=" + DEFAULT_FILE_UPLOAD_TOKEN);

        // Get all the taxRuleList where fileUploadToken does not contain UPDATED_FILE_UPLOAD_TOKEN
        defaultTaxRuleShouldBeFound("fileUploadToken.doesNotContain=" + UPDATED_FILE_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxRulesByCompilationTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where compilationToken equals to DEFAULT_COMPILATION_TOKEN
        defaultTaxRuleShouldBeFound("compilationToken.equals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the taxRuleList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultTaxRuleShouldNotBeFound("compilationToken.equals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxRulesByCompilationTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where compilationToken not equals to DEFAULT_COMPILATION_TOKEN
        defaultTaxRuleShouldNotBeFound("compilationToken.notEquals=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the taxRuleList where compilationToken not equals to UPDATED_COMPILATION_TOKEN
        defaultTaxRuleShouldBeFound("compilationToken.notEquals=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxRulesByCompilationTokenIsInShouldWork() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where compilationToken in DEFAULT_COMPILATION_TOKEN or UPDATED_COMPILATION_TOKEN
        defaultTaxRuleShouldBeFound("compilationToken.in=" + DEFAULT_COMPILATION_TOKEN + "," + UPDATED_COMPILATION_TOKEN);

        // Get all the taxRuleList where compilationToken equals to UPDATED_COMPILATION_TOKEN
        defaultTaxRuleShouldNotBeFound("compilationToken.in=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxRulesByCompilationTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where compilationToken is not null
        defaultTaxRuleShouldBeFound("compilationToken.specified=true");

        // Get all the taxRuleList where compilationToken is null
        defaultTaxRuleShouldNotBeFound("compilationToken.specified=false");
    }

    @Test
    @Transactional
    void getAllTaxRulesByCompilationTokenContainsSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where compilationToken contains DEFAULT_COMPILATION_TOKEN
        defaultTaxRuleShouldBeFound("compilationToken.contains=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the taxRuleList where compilationToken contains UPDATED_COMPILATION_TOKEN
        defaultTaxRuleShouldNotBeFound("compilationToken.contains=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxRulesByCompilationTokenNotContainsSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        // Get all the taxRuleList where compilationToken does not contain DEFAULT_COMPILATION_TOKEN
        defaultTaxRuleShouldNotBeFound("compilationToken.doesNotContain=" + DEFAULT_COMPILATION_TOKEN);

        // Get all the taxRuleList where compilationToken does not contain UPDATED_COMPILATION_TOKEN
        defaultTaxRuleShouldBeFound("compilationToken.doesNotContain=" + UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void getAllTaxRulesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);
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
        taxRule.addPlaceholder(placeholder);
        taxRuleRepository.saveAndFlush(taxRule);
        Long placeholderId = placeholder.getId();

        // Get all the taxRuleList where placeholder equals to placeholderId
        defaultTaxRuleShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the taxRuleList where placeholder equals to (placeholderId + 1)
        defaultTaxRuleShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaxRuleShouldBeFound(String filter) throws Exception {
        restTaxRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].telcoExciseDuty").value(hasItem(DEFAULT_TELCO_EXCISE_DUTY.doubleValue())))
            .andExpect(jsonPath("$.[*].valueAddedTax").value(hasItem(DEFAULT_VALUE_ADDED_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].withholdingVAT").value(hasItem(DEFAULT_WITHHOLDING_VAT.doubleValue())))
            .andExpect(jsonPath("$.[*].withholdingTaxConsultancy").value(hasItem(DEFAULT_WITHHOLDING_TAX_CONSULTANCY.doubleValue())))
            .andExpect(jsonPath("$.[*].withholdingTaxRent").value(hasItem(DEFAULT_WITHHOLDING_TAX_RENT.doubleValue())))
            .andExpect(jsonPath("$.[*].cateringLevy").value(hasItem(DEFAULT_CATERING_LEVY.doubleValue())))
            .andExpect(jsonPath("$.[*].serviceCharge").value(hasItem(DEFAULT_SERVICE_CHARGE.doubleValue())))
            .andExpect(
                jsonPath("$.[*].withholdingTaxImportedService").value(hasItem(DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE.doubleValue()))
            )
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));

        // Check, that the count call also returns 1
        restTaxRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaxRuleShouldNotBeFound(String filter) throws Exception {
        restTaxRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaxRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTaxRule() throws Exception {
        // Get the taxRule
        restTaxRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTaxRule() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        int databaseSizeBeforeUpdate = taxRuleRepository.findAll().size();

        // Update the taxRule
        TaxRule updatedTaxRule = taxRuleRepository.findById(taxRule.getId()).get();
        // Disconnect from session so that the updates on updatedTaxRule are not directly saved in db
        em.detach(updatedTaxRule);
        updatedTaxRule
            .telcoExciseDuty(UPDATED_TELCO_EXCISE_DUTY)
            .valueAddedTax(UPDATED_VALUE_ADDED_TAX)
            .withholdingVAT(UPDATED_WITHHOLDING_VAT)
            .withholdingTaxConsultancy(UPDATED_WITHHOLDING_TAX_CONSULTANCY)
            .withholdingTaxRent(UPDATED_WITHHOLDING_TAX_RENT)
            .cateringLevy(UPDATED_CATERING_LEVY)
            .serviceCharge(UPDATED_SERVICE_CHARGE)
            .withholdingTaxImportedService(UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);
        TaxRuleDTO taxRuleDTO = taxRuleMapper.toDto(updatedTaxRule);

        restTaxRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the TaxRule in the database
        List<TaxRule> taxRuleList = taxRuleRepository.findAll();
        assertThat(taxRuleList).hasSize(databaseSizeBeforeUpdate);
        TaxRule testTaxRule = taxRuleList.get(taxRuleList.size() - 1);
        assertThat(testTaxRule.getTelcoExciseDuty()).isEqualTo(UPDATED_TELCO_EXCISE_DUTY);
        assertThat(testTaxRule.getValueAddedTax()).isEqualTo(UPDATED_VALUE_ADDED_TAX);
        assertThat(testTaxRule.getWithholdingVAT()).isEqualTo(UPDATED_WITHHOLDING_VAT);
        assertThat(testTaxRule.getWithholdingTaxConsultancy()).isEqualTo(UPDATED_WITHHOLDING_TAX_CONSULTANCY);
        assertThat(testTaxRule.getWithholdingTaxRent()).isEqualTo(UPDATED_WITHHOLDING_TAX_RENT);
        assertThat(testTaxRule.getCateringLevy()).isEqualTo(UPDATED_CATERING_LEVY);
        assertThat(testTaxRule.getServiceCharge()).isEqualTo(UPDATED_SERVICE_CHARGE);
        assertThat(testTaxRule.getWithholdingTaxImportedService()).isEqualTo(UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE);
        assertThat(testTaxRule.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testTaxRule.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);

        // Validate the TaxRule in Elasticsearch
        verify(mockTaxRuleSearchRepository).save(testTaxRule);
    }

    @Test
    @Transactional
    void putNonExistingTaxRule() throws Exception {
        int databaseSizeBeforeUpdate = taxRuleRepository.findAll().size();
        taxRule.setId(count.incrementAndGet());

        // Create the TaxRule
        TaxRuleDTO taxRuleDTO = taxRuleMapper.toDto(taxRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, taxRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxRule in the database
        List<TaxRule> taxRuleList = taxRuleRepository.findAll();
        assertThat(taxRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxRule in Elasticsearch
        verify(mockTaxRuleSearchRepository, times(0)).save(taxRule);
    }

    @Test
    @Transactional
    void putWithIdMismatchTaxRule() throws Exception {
        int databaseSizeBeforeUpdate = taxRuleRepository.findAll().size();
        taxRule.setId(count.incrementAndGet());

        // Create the TaxRule
        TaxRuleDTO taxRuleDTO = taxRuleMapper.toDto(taxRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(taxRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxRule in the database
        List<TaxRule> taxRuleList = taxRuleRepository.findAll();
        assertThat(taxRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxRule in Elasticsearch
        verify(mockTaxRuleSearchRepository, times(0)).save(taxRule);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTaxRule() throws Exception {
        int databaseSizeBeforeUpdate = taxRuleRepository.findAll().size();
        taxRule.setId(count.incrementAndGet());

        // Create the TaxRule
        TaxRuleDTO taxRuleDTO = taxRuleMapper.toDto(taxRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(taxRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxRule in the database
        List<TaxRule> taxRuleList = taxRuleRepository.findAll();
        assertThat(taxRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxRule in Elasticsearch
        verify(mockTaxRuleSearchRepository, times(0)).save(taxRule);
    }

    @Test
    @Transactional
    void partialUpdateTaxRuleWithPatch() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        int databaseSizeBeforeUpdate = taxRuleRepository.findAll().size();

        // Update the taxRule using partial update
        TaxRule partialUpdatedTaxRule = new TaxRule();
        partialUpdatedTaxRule.setId(taxRule.getId());

        partialUpdatedTaxRule
            .telcoExciseDuty(UPDATED_TELCO_EXCISE_DUTY)
            .valueAddedTax(UPDATED_VALUE_ADDED_TAX)
            .withholdingTaxImportedService(UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE);

        restTaxRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxRule))
            )
            .andExpect(status().isOk());

        // Validate the TaxRule in the database
        List<TaxRule> taxRuleList = taxRuleRepository.findAll();
        assertThat(taxRuleList).hasSize(databaseSizeBeforeUpdate);
        TaxRule testTaxRule = taxRuleList.get(taxRuleList.size() - 1);
        assertThat(testTaxRule.getTelcoExciseDuty()).isEqualTo(UPDATED_TELCO_EXCISE_DUTY);
        assertThat(testTaxRule.getValueAddedTax()).isEqualTo(UPDATED_VALUE_ADDED_TAX);
        assertThat(testTaxRule.getWithholdingVAT()).isEqualTo(DEFAULT_WITHHOLDING_VAT);
        assertThat(testTaxRule.getWithholdingTaxConsultancy()).isEqualTo(DEFAULT_WITHHOLDING_TAX_CONSULTANCY);
        assertThat(testTaxRule.getWithholdingTaxRent()).isEqualTo(DEFAULT_WITHHOLDING_TAX_RENT);
        assertThat(testTaxRule.getCateringLevy()).isEqualTo(DEFAULT_CATERING_LEVY);
        assertThat(testTaxRule.getServiceCharge()).isEqualTo(DEFAULT_SERVICE_CHARGE);
        assertThat(testTaxRule.getWithholdingTaxImportedService()).isEqualTo(UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE);
        assertThat(testTaxRule.getFileUploadToken()).isEqualTo(DEFAULT_FILE_UPLOAD_TOKEN);
        assertThat(testTaxRule.getCompilationToken()).isEqualTo(DEFAULT_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateTaxRuleWithPatch() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        int databaseSizeBeforeUpdate = taxRuleRepository.findAll().size();

        // Update the taxRule using partial update
        TaxRule partialUpdatedTaxRule = new TaxRule();
        partialUpdatedTaxRule.setId(taxRule.getId());

        partialUpdatedTaxRule
            .telcoExciseDuty(UPDATED_TELCO_EXCISE_DUTY)
            .valueAddedTax(UPDATED_VALUE_ADDED_TAX)
            .withholdingVAT(UPDATED_WITHHOLDING_VAT)
            .withholdingTaxConsultancy(UPDATED_WITHHOLDING_TAX_CONSULTANCY)
            .withholdingTaxRent(UPDATED_WITHHOLDING_TAX_RENT)
            .cateringLevy(UPDATED_CATERING_LEVY)
            .serviceCharge(UPDATED_SERVICE_CHARGE)
            .withholdingTaxImportedService(UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE)
            .fileUploadToken(UPDATED_FILE_UPLOAD_TOKEN)
            .compilationToken(UPDATED_COMPILATION_TOKEN);

        restTaxRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTaxRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxRule))
            )
            .andExpect(status().isOk());

        // Validate the TaxRule in the database
        List<TaxRule> taxRuleList = taxRuleRepository.findAll();
        assertThat(taxRuleList).hasSize(databaseSizeBeforeUpdate);
        TaxRule testTaxRule = taxRuleList.get(taxRuleList.size() - 1);
        assertThat(testTaxRule.getTelcoExciseDuty()).isEqualTo(UPDATED_TELCO_EXCISE_DUTY);
        assertThat(testTaxRule.getValueAddedTax()).isEqualTo(UPDATED_VALUE_ADDED_TAX);
        assertThat(testTaxRule.getWithholdingVAT()).isEqualTo(UPDATED_WITHHOLDING_VAT);
        assertThat(testTaxRule.getWithholdingTaxConsultancy()).isEqualTo(UPDATED_WITHHOLDING_TAX_CONSULTANCY);
        assertThat(testTaxRule.getWithholdingTaxRent()).isEqualTo(UPDATED_WITHHOLDING_TAX_RENT);
        assertThat(testTaxRule.getCateringLevy()).isEqualTo(UPDATED_CATERING_LEVY);
        assertThat(testTaxRule.getServiceCharge()).isEqualTo(UPDATED_SERVICE_CHARGE);
        assertThat(testTaxRule.getWithholdingTaxImportedService()).isEqualTo(UPDATED_WITHHOLDING_TAX_IMPORTED_SERVICE);
        assertThat(testTaxRule.getFileUploadToken()).isEqualTo(UPDATED_FILE_UPLOAD_TOKEN);
        assertThat(testTaxRule.getCompilationToken()).isEqualTo(UPDATED_COMPILATION_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingTaxRule() throws Exception {
        int databaseSizeBeforeUpdate = taxRuleRepository.findAll().size();
        taxRule.setId(count.incrementAndGet());

        // Create the TaxRule
        TaxRuleDTO taxRuleDTO = taxRuleMapper.toDto(taxRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaxRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, taxRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxRule in the database
        List<TaxRule> taxRuleList = taxRuleRepository.findAll();
        assertThat(taxRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxRule in Elasticsearch
        verify(mockTaxRuleSearchRepository, times(0)).save(taxRule);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTaxRule() throws Exception {
        int databaseSizeBeforeUpdate = taxRuleRepository.findAll().size();
        taxRule.setId(count.incrementAndGet());

        // Create the TaxRule
        TaxRuleDTO taxRuleDTO = taxRuleMapper.toDto(taxRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(taxRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TaxRule in the database
        List<TaxRule> taxRuleList = taxRuleRepository.findAll();
        assertThat(taxRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxRule in Elasticsearch
        verify(mockTaxRuleSearchRepository, times(0)).save(taxRule);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTaxRule() throws Exception {
        int databaseSizeBeforeUpdate = taxRuleRepository.findAll().size();
        taxRule.setId(count.incrementAndGet());

        // Create the TaxRule
        TaxRuleDTO taxRuleDTO = taxRuleMapper.toDto(taxRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTaxRuleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(taxRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TaxRule in the database
        List<TaxRule> taxRuleList = taxRuleRepository.findAll();
        assertThat(taxRuleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TaxRule in Elasticsearch
        verify(mockTaxRuleSearchRepository, times(0)).save(taxRule);
    }

    @Test
    @Transactional
    void deleteTaxRule() throws Exception {
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);

        int databaseSizeBeforeDelete = taxRuleRepository.findAll().size();

        // Delete the taxRule
        restTaxRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, taxRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaxRule> taxRuleList = taxRuleRepository.findAll();
        assertThat(taxRuleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TaxRule in Elasticsearch
        verify(mockTaxRuleSearchRepository, times(1)).deleteById(taxRule.getId());
    }

    @Test
    @Transactional
    void searchTaxRule() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        taxRuleRepository.saveAndFlush(taxRule);
        when(mockTaxRuleSearchRepository.search("id:" + taxRule.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(taxRule), PageRequest.of(0, 1), 1));

        // Search the taxRule
        restTaxRuleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + taxRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taxRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].telcoExciseDuty").value(hasItem(DEFAULT_TELCO_EXCISE_DUTY.doubleValue())))
            .andExpect(jsonPath("$.[*].valueAddedTax").value(hasItem(DEFAULT_VALUE_ADDED_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].withholdingVAT").value(hasItem(DEFAULT_WITHHOLDING_VAT.doubleValue())))
            .andExpect(jsonPath("$.[*].withholdingTaxConsultancy").value(hasItem(DEFAULT_WITHHOLDING_TAX_CONSULTANCY.doubleValue())))
            .andExpect(jsonPath("$.[*].withholdingTaxRent").value(hasItem(DEFAULT_WITHHOLDING_TAX_RENT.doubleValue())))
            .andExpect(jsonPath("$.[*].cateringLevy").value(hasItem(DEFAULT_CATERING_LEVY.doubleValue())))
            .andExpect(jsonPath("$.[*].serviceCharge").value(hasItem(DEFAULT_SERVICE_CHARGE.doubleValue())))
            .andExpect(
                jsonPath("$.[*].withholdingTaxImportedService").value(hasItem(DEFAULT_WITHHOLDING_TAX_IMPORTED_SERVICE.doubleValue()))
            )
            .andExpect(jsonPath("$.[*].fileUploadToken").value(hasItem(DEFAULT_FILE_UPLOAD_TOKEN)))
            .andExpect(jsonPath("$.[*].compilationToken").value(hasItem(DEFAULT_COMPILATION_TOKEN)));
    }
}
