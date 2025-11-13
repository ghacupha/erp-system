///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('WeeklyCashHolding e2e test', () => {
  const weeklyCashHoldingPageUrl = '/weekly-cash-holding';
  const weeklyCashHoldingPageUrlPattern = new RegExp('/weekly-cash-holding(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const weeklyCashHoldingSample = { reportingDate: '2023-10-03', fitUnits: 80916, unfitUnits: 87787 };

  let weeklyCashHolding: any;
  let institutionCode: any;
  let bankBranchCode: any;
  let countySubCountyCode: any;
  let kenyanCurrencyDenomination: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/institution-codes',
      body: {
        institutionCode: 'mesh',
        institutionName: 'incentivize orchestrate Chicken',
        shortName: 'Incredible pink invoice',
        category: 'compelling Granite Future',
        institutionCategory: 'strategy',
        institutionOwnership: 'Tactics Product',
        dateLicensed: '2022-04-05',
        institutionStatus: 'grow structure Generic',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/bank-branch-codes',
      body: {
        bankCode: 'quantify Investment hub',
        bankName: 'deposit',
        branchCode: 'Avon matrix',
        branchName: 'Real Avon',
        notes: 'Gibraltar Grocery',
      },
    }).then(({ body }) => {
      bankBranchCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/county-sub-county-codes',
      body: { subCountyCode: '7535', subCountyName: 'Product Delaware Chips', countyCode: '35', countyName: 'encoding yellow target' },
    }).then(({ body }) => {
      countySubCountyCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/kenyan-currency-denominations',
      body: {
        currencyDenominationCode: 'compressing Ports',
        currencyDenominationType: 'lime purple',
        currencyDenominationTypeDetails: 'Grocery',
      },
    }).then(({ body }) => {
      kenyanCurrencyDenomination = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/weekly-cash-holdings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/weekly-cash-holdings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/weekly-cash-holdings/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/institution-codes', {
      statusCode: 200,
      body: [institutionCode],
    });

    cy.intercept('GET', '/api/bank-branch-codes', {
      statusCode: 200,
      body: [bankBranchCode],
    });

    cy.intercept('GET', '/api/county-sub-county-codes', {
      statusCode: 200,
      body: [countySubCountyCode],
    });

    cy.intercept('GET', '/api/kenyan-currency-denominations', {
      statusCode: 200,
      body: [kenyanCurrencyDenomination],
    });
  });

  afterEach(() => {
    if (weeklyCashHolding) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/weekly-cash-holdings/${weeklyCashHolding.id}`,
      }).then(() => {
        weeklyCashHolding = undefined;
      });
    }
  });

  afterEach(() => {
    if (institutionCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/institution-codes/${institutionCode.id}`,
      }).then(() => {
        institutionCode = undefined;
      });
    }
    if (bankBranchCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bank-branch-codes/${bankBranchCode.id}`,
      }).then(() => {
        bankBranchCode = undefined;
      });
    }
    if (countySubCountyCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/county-sub-county-codes/${countySubCountyCode.id}`,
      }).then(() => {
        countySubCountyCode = undefined;
      });
    }
    if (kenyanCurrencyDenomination) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/kenyan-currency-denominations/${kenyanCurrencyDenomination.id}`,
      }).then(() => {
        kenyanCurrencyDenomination = undefined;
      });
    }
  });

  it('WeeklyCashHoldings menu should load WeeklyCashHoldings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('weekly-cash-holding');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WeeklyCashHolding').should('exist');
    cy.url().should('match', weeklyCashHoldingPageUrlPattern);
  });

  describe('WeeklyCashHolding page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(weeklyCashHoldingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WeeklyCashHolding page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/weekly-cash-holding/new$'));
        cy.getEntityCreateUpdateHeading('WeeklyCashHolding');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weeklyCashHoldingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/weekly-cash-holdings',

          body: {
            ...weeklyCashHoldingSample,
            bankCode: institutionCode,
            branchId: bankBranchCode,
            subCountyCode: countySubCountyCode,
            denomination: kenyanCurrencyDenomination,
          },
        }).then(({ body }) => {
          weeklyCashHolding = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/weekly-cash-holdings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [weeklyCashHolding],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(weeklyCashHoldingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WeeklyCashHolding page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('weeklyCashHolding');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weeklyCashHoldingPageUrlPattern);
      });

      it('edit button click should load edit WeeklyCashHolding page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WeeklyCashHolding');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weeklyCashHoldingPageUrlPattern);
      });

      it('last delete button click should delete instance of WeeklyCashHolding', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('weeklyCashHolding').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weeklyCashHoldingPageUrlPattern);

        weeklyCashHolding = undefined;
      });
    });
  });

  describe('new WeeklyCashHolding page', () => {
    beforeEach(() => {
      cy.visit(`${weeklyCashHoldingPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('WeeklyCashHolding');
    });

    it('should create an instance of WeeklyCashHolding', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-04').should('have.value', '2023-10-04');

      cy.get(`[data-cy="fitUnits"]`).type('9103').should('have.value', '9103');

      cy.get(`[data-cy="unfitUnits"]`).type('69121').should('have.value', '69121');

      cy.get(`[data-cy="bankCode"]`).select(1);
      cy.get(`[data-cy="branchId"]`).select(1);
      cy.get(`[data-cy="subCountyCode"]`).select(1);
      cy.get(`[data-cy="denomination"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        weeklyCashHolding = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', weeklyCashHoldingPageUrlPattern);
    });
  });
});
