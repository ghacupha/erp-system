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

describe('CardUsageInformation e2e test', () => {
  const cardUsageInformationPageUrl = '/card-usage-information';
  const cardUsageInformationPageUrlPattern = new RegExp('/card-usage-information(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardUsageInformationSample = {
    reportingDate: '2023-10-03',
    totalNumberOfLiveCards: 88854,
    totalActiveCards: 38336,
    totalNumberOfTransactionsDone: 53146,
    totalValueOfTransactionsDoneInLCY: 51931,
  };

  let cardUsageInformation: any;
  let institutionCode: any;
  let cardTypes: any;
  let cardBrandType: any;
  let cardCategoryType: any;
  let bankTransactionType: any;
  let channelType: any;
  let cardState: any;

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
        institutionCode: 'overriding',
        institutionName: 'Montana',
        shortName: 'invoice',
        category: 'Pines',
        institutionCategory: 'extensible',
        institutionOwnership: 'array',
        dateLicensed: '2022-04-05',
        institutionStatus: 'France ability Gardens',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/card-types',
      body: {
        cardTypeCode: 'Rubber CSS Granite',
        cardType: 'array array Orchard',
        cardTypeDetails: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      cardTypes = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/card-brand-types',
      body: {
        cardBrandTypeCode: 'virtual SDD Afghani',
        cardBrandType: 'parse Direct Specialist',
        cardBrandTypeDetails: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      cardBrandType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/card-category-types',
      body: {
        cardCategoryFlag: 'L',
        cardCategoryDescription: 'bypassing azure grey',
        cardCategoryDetails: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      cardCategoryType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/bank-transaction-types',
      body: { transactionTypeCode: 'benchmark port', transactionTypeDetails: 'index Generic (Malvinas)' },
    }).then(({ body }) => {
      bankTransactionType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/channel-types',
      body: { channelsTypeCode: 'maximize', channelTypes: 'solution', channelTypeDetails: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=' },
    }).then(({ body }) => {
      channelType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/card-states',
      body: { cardStateFlag: 'P', cardStateFlagDetails: 'bandwidth Ridge Shoals', cardStateFlagDescription: 'harness Rubber' },
    }).then(({ body }) => {
      cardState = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-usage-informations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-usage-informations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-usage-informations/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/institution-codes', {
      statusCode: 200,
      body: [institutionCode],
    });

    cy.intercept('GET', '/api/card-types', {
      statusCode: 200,
      body: [cardTypes],
    });

    cy.intercept('GET', '/api/card-brand-types', {
      statusCode: 200,
      body: [cardBrandType],
    });

    cy.intercept('GET', '/api/card-category-types', {
      statusCode: 200,
      body: [cardCategoryType],
    });

    cy.intercept('GET', '/api/bank-transaction-types', {
      statusCode: 200,
      body: [bankTransactionType],
    });

    cy.intercept('GET', '/api/channel-types', {
      statusCode: 200,
      body: [channelType],
    });

    cy.intercept('GET', '/api/card-states', {
      statusCode: 200,
      body: [cardState],
    });
  });

  afterEach(() => {
    if (cardUsageInformation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-usage-informations/${cardUsageInformation.id}`,
      }).then(() => {
        cardUsageInformation = undefined;
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
    if (cardTypes) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-types/${cardTypes.id}`,
      }).then(() => {
        cardTypes = undefined;
      });
    }
    if (cardBrandType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-brand-types/${cardBrandType.id}`,
      }).then(() => {
        cardBrandType = undefined;
      });
    }
    if (cardCategoryType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-category-types/${cardCategoryType.id}`,
      }).then(() => {
        cardCategoryType = undefined;
      });
    }
    if (bankTransactionType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bank-transaction-types/${bankTransactionType.id}`,
      }).then(() => {
        bankTransactionType = undefined;
      });
    }
    if (channelType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/channel-types/${channelType.id}`,
      }).then(() => {
        channelType = undefined;
      });
    }
    if (cardState) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-states/${cardState.id}`,
      }).then(() => {
        cardState = undefined;
      });
    }
  });

  it('CardUsageInformations menu should load CardUsageInformations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-usage-information');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardUsageInformation').should('exist');
    cy.url().should('match', cardUsageInformationPageUrlPattern);
  });

  describe('CardUsageInformation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardUsageInformationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardUsageInformation page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-usage-information/new$'));
        cy.getEntityCreateUpdateHeading('CardUsageInformation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardUsageInformationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-usage-informations',

          body: {
            ...cardUsageInformationSample,
            bankCode: institutionCode,
            cardType: cardTypes,
            cardBrand: cardBrandType,
            cardCategoryType: cardCategoryType,
            transactionType: bankTransactionType,
            channelType: channelType,
            cardState: cardState,
          },
        }).then(({ body }) => {
          cardUsageInformation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-usage-informations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardUsageInformation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardUsageInformationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardUsageInformation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardUsageInformation');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardUsageInformationPageUrlPattern);
      });

      it('edit button click should load edit CardUsageInformation page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardUsageInformation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardUsageInformationPageUrlPattern);
      });

      it('last delete button click should delete instance of CardUsageInformation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardUsageInformation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardUsageInformationPageUrlPattern);

        cardUsageInformation = undefined;
      });
    });
  });

  describe('new CardUsageInformation page', () => {
    beforeEach(() => {
      cy.visit(`${cardUsageInformationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardUsageInformation');
    });

    it('should create an instance of CardUsageInformation', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="totalNumberOfLiveCards"]`).type('30885').should('have.value', '30885');

      cy.get(`[data-cy="totalActiveCards"]`).type('89812').should('have.value', '89812');

      cy.get(`[data-cy="totalNumberOfTransactionsDone"]`).type('74898').should('have.value', '74898');

      cy.get(`[data-cy="totalValueOfTransactionsDoneInLCY"]`).type('47224').should('have.value', '47224');

      cy.get(`[data-cy="bankCode"]`).select(1);
      cy.get(`[data-cy="cardType"]`).select(1);
      cy.get(`[data-cy="cardBrand"]`).select(1);
      cy.get(`[data-cy="cardCategoryType"]`).select(1);
      cy.get(`[data-cy="transactionType"]`).select(1);
      cy.get(`[data-cy="channelType"]`).select(1);
      cy.get(`[data-cy="cardState"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardUsageInformation = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardUsageInformationPageUrlPattern);
    });
  });
});
