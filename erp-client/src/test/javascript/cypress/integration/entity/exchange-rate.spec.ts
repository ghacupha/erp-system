///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

describe('ExchangeRate e2e test', () => {
  const exchangeRatePageUrl = '/exchange-rate';
  const exchangeRatePageUrlPattern = new RegExp('/exchange-rate(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const exchangeRateSample = {
    businessReportingDay: '2023-10-02',
    buyingRate: 44428,
    sellingRate: 66030,
    meanRate: 28508,
    closingBidRate: 19541,
    closingOfferRate: 99045,
    usdCrossRate: 82827,
  };

  let exchangeRate: any;
  let institutionCode: any;
  let isoCurrencyCode: any;

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
        institutionCode: 'Personal Minnesota virtual',
        institutionName: 'Gorgeous disintermediate Rupee',
        shortName: 'Electronics dot-com Representative',
        category: 'human-resource Shoal hierarchy',
        institutionCategory: 'Baht',
        institutionOwnership: 'overriding',
        dateLicensed: '2022-04-06',
        institutionStatus: 'Mexico',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/iso-currency-codes',
      body: {
        alphabeticCode: 'Buckinghamshire Iowa interactive',
        numericCode: 'Table',
        minorUnit: 'partnerships Hat',
        currency: 'Island Freeway',
        country: 'Suriname',
      },
    }).then(({ body }) => {
      isoCurrencyCode = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/exchange-rates+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/exchange-rates').as('postEntityRequest');
    cy.intercept('DELETE', '/api/exchange-rates/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/institution-codes', {
      statusCode: 200,
      body: [institutionCode],
    });

    cy.intercept('GET', '/api/iso-currency-codes', {
      statusCode: 200,
      body: [isoCurrencyCode],
    });
  });

  afterEach(() => {
    if (exchangeRate) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/exchange-rates/${exchangeRate.id}`,
      }).then(() => {
        exchangeRate = undefined;
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
    if (isoCurrencyCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/iso-currency-codes/${isoCurrencyCode.id}`,
      }).then(() => {
        isoCurrencyCode = undefined;
      });
    }
  });

  it('ExchangeRates menu should load ExchangeRates page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('exchange-rate');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ExchangeRate').should('exist');
    cy.url().should('match', exchangeRatePageUrlPattern);
  });

  describe('ExchangeRate page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(exchangeRatePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ExchangeRate page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/exchange-rate/new$'));
        cy.getEntityCreateUpdateHeading('ExchangeRate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', exchangeRatePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/exchange-rates',

          body: {
            ...exchangeRateSample,
            institutionCode: institutionCode,
            currencyCode: isoCurrencyCode,
          },
        }).then(({ body }) => {
          exchangeRate = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/exchange-rates+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [exchangeRate],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(exchangeRatePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ExchangeRate page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('exchangeRate');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', exchangeRatePageUrlPattern);
      });

      it('edit button click should load edit ExchangeRate page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ExchangeRate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', exchangeRatePageUrlPattern);
      });

      it('last delete button click should delete instance of ExchangeRate', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('exchangeRate').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', exchangeRatePageUrlPattern);

        exchangeRate = undefined;
      });
    });
  });

  describe('new ExchangeRate page', () => {
    beforeEach(() => {
      cy.visit(`${exchangeRatePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ExchangeRate');
    });

    it('should create an instance of ExchangeRate', () => {
      cy.get(`[data-cy="businessReportingDay"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="buyingRate"]`).type('74203').should('have.value', '74203');

      cy.get(`[data-cy="sellingRate"]`).type('97092').should('have.value', '97092');

      cy.get(`[data-cy="meanRate"]`).type('64438').should('have.value', '64438');

      cy.get(`[data-cy="closingBidRate"]`).type('20192').should('have.value', '20192');

      cy.get(`[data-cy="closingOfferRate"]`).type('72323').should('have.value', '72323');

      cy.get(`[data-cy="usdCrossRate"]`).type('82743').should('have.value', '82743');

      cy.get(`[data-cy="institutionCode"]`).select(1);
      cy.get(`[data-cy="currencyCode"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        exchangeRate = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', exchangeRatePageUrlPattern);
    });
  });
});
