///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

describe('CardIssuerCharges e2e test', () => {
  const cardIssuerChargesPageUrl = '/card-issuer-charges';
  const cardIssuerChargesPageUrlPattern = new RegExp('/card-issuer-charges(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const cardIssuerChargesSample = { reportingDate: '2023-10-04', cardFeeChargeInLCY: 63280 };

  let cardIssuerCharges: any;
  let institutionCode: any;
  let cardCategoryType: any;
  let cardTypes: any;
  let cardBrandType: any;
  let cardClassType: any;
  let cardCharges: any;

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
        institutionCode: 'Metal',
        institutionName: 'Helena Salad Village',
        shortName: 'withdrawal Kids',
        category: 'Account value-added backing',
        institutionCategory: 'brand',
        institutionOwnership: 'Bacon',
        dateLicensed: '2022-04-05',
        institutionStatus: '24/365 index',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/card-category-types',
      body: {
        cardCategoryFlag: 'L',
        cardCategoryDescription: 'Practical',
        cardCategoryDetails: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      cardCategoryType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/card-types',
      body: {
        cardTypeCode: 'Utah alarm',
        cardType: 'orchid Associate transmitting',
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
        cardBrandTypeCode: 'Heights Camp Kids',
        cardBrandType: 'gold',
        cardBrandTypeDetails: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      cardBrandType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/card-class-types',
      body: {
        cardClassTypeCode: 'invoice Dynamic navigate',
        cardClassType: 'EXE Lake',
        cardClassDetails: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      cardClassType = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/card-charges',
      body: {
        cardChargeType: 'Garden Generic Movies',
        cardChargeTypeName: 'bus payment',
        cardChargeDetails: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      cardCharges = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/card-issuer-charges+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/card-issuer-charges').as('postEntityRequest');
    cy.intercept('DELETE', '/api/card-issuer-charges/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/institution-codes', {
      statusCode: 200,
      body: [institutionCode],
    });

    cy.intercept('GET', '/api/card-category-types', {
      statusCode: 200,
      body: [cardCategoryType],
    });

    cy.intercept('GET', '/api/card-types', {
      statusCode: 200,
      body: [cardTypes],
    });

    cy.intercept('GET', '/api/card-brand-types', {
      statusCode: 200,
      body: [cardBrandType],
    });

    cy.intercept('GET', '/api/card-class-types', {
      statusCode: 200,
      body: [cardClassType],
    });

    cy.intercept('GET', '/api/card-charges', {
      statusCode: 200,
      body: [cardCharges],
    });
  });

  afterEach(() => {
    if (cardIssuerCharges) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-issuer-charges/${cardIssuerCharges.id}`,
      }).then(() => {
        cardIssuerCharges = undefined;
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
    if (cardCategoryType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-category-types/${cardCategoryType.id}`,
      }).then(() => {
        cardCategoryType = undefined;
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
    if (cardClassType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-class-types/${cardClassType.id}`,
      }).then(() => {
        cardClassType = undefined;
      });
    }
    if (cardCharges) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/card-charges/${cardCharges.id}`,
      }).then(() => {
        cardCharges = undefined;
      });
    }
  });

  it('CardIssuerCharges menu should load CardIssuerCharges page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('card-issuer-charges');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CardIssuerCharges').should('exist');
    cy.url().should('match', cardIssuerChargesPageUrlPattern);
  });

  describe('CardIssuerCharges page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cardIssuerChargesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CardIssuerCharges page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/card-issuer-charges/new$'));
        cy.getEntityCreateUpdateHeading('CardIssuerCharges');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardIssuerChargesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/card-issuer-charges',

          body: {
            ...cardIssuerChargesSample,
            bankCode: institutionCode,
            cardCategory: cardCategoryType,
            cardType: cardTypes,
            cardBrand: cardBrandType,
            cardClass: cardClassType,
            cardChargeType: cardCharges,
          },
        }).then(({ body }) => {
          cardIssuerCharges = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/card-issuer-charges+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cardIssuerCharges],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cardIssuerChargesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CardIssuerCharges page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cardIssuerCharges');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardIssuerChargesPageUrlPattern);
      });

      it('edit button click should load edit CardIssuerCharges page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CardIssuerCharges');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardIssuerChargesPageUrlPattern);
      });

      it('last delete button click should delete instance of CardIssuerCharges', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cardIssuerCharges').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', cardIssuerChargesPageUrlPattern);

        cardIssuerCharges = undefined;
      });
    });
  });

  describe('new CardIssuerCharges page', () => {
    beforeEach(() => {
      cy.visit(`${cardIssuerChargesPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CardIssuerCharges');
    });

    it('should create an instance of CardIssuerCharges', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="cardFeeChargeInLCY"]`).type('90313').should('have.value', '90313');

      cy.get(`[data-cy="bankCode"]`).select(1);
      cy.get(`[data-cy="cardCategory"]`).select(1);
      cy.get(`[data-cy="cardType"]`).select(1);
      cy.get(`[data-cy="cardBrand"]`).select(1);
      cy.get(`[data-cy="cardClass"]`).select(1);
      cy.get(`[data-cy="cardChargeType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        cardIssuerCharges = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', cardIssuerChargesPageUrlPattern);
    });
  });
});
