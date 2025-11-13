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

describe('CreditCardFacility e2e test', () => {
  const creditCardFacilityPageUrl = '/credit-card-facility';
  const creditCardFacilityPageUrlPattern = new RegExp('/credit-card-facility(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const creditCardFacilitySample = {
    reportingDate: '2023-10-03',
    totalNumberOfActiveCreditCards: 10982,
    totalCreditCardLimitsInCCY: 20593,
    totalCreditCardLimitsInLCY: 73664,
    totalCreditCardAmountUtilisedInCCY: 16229,
    totalCreditCardAmountUtilisedInLcy: 73758,
    totalNPACreditCardAmountInFCY: 68263,
    totalNPACreditCardAmountInLCY: 15013,
  };

  let creditCardFacility: any;
  let institutionCode: any;
  let creditCardOwnership: any;
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
        institutionCode: 'withdrawal context-sensitive',
        institutionName: 'Card Soap',
        shortName: 'extend',
        category: 'Money',
        institutionCategory: 'vortals Buckinghamshire',
        institutionOwnership: 'Small',
        dateLicensed: '2022-04-05',
        institutionStatus: 'Frozen Inverse',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/credit-card-ownerships',
      body: {
        creditCardOwnershipCategoryCode: 'Global transmit',
        creditCardOwnershipCategoryType: 'INDIVIDUAL',
        description: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      creditCardOwnership = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/iso-currency-codes',
      body: {
        alphabeticCode: 'transmitting infomediaries',
        numericCode: 'utilize',
        minorUnit: 'Enterprise-wide parse',
        currency: 'Table Franc line',
        country: 'Luxembourg',
      },
    }).then(({ body }) => {
      isoCurrencyCode = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/credit-card-facilities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/credit-card-facilities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/credit-card-facilities/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/institution-codes', {
      statusCode: 200,
      body: [institutionCode],
    });

    cy.intercept('GET', '/api/credit-card-ownerships', {
      statusCode: 200,
      body: [creditCardOwnership],
    });

    cy.intercept('GET', '/api/iso-currency-codes', {
      statusCode: 200,
      body: [isoCurrencyCode],
    });
  });

  afterEach(() => {
    if (creditCardFacility) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/credit-card-facilities/${creditCardFacility.id}`,
      }).then(() => {
        creditCardFacility = undefined;
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
    if (creditCardOwnership) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/credit-card-ownerships/${creditCardOwnership.id}`,
      }).then(() => {
        creditCardOwnership = undefined;
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

  it('CreditCardFacilities menu should load CreditCardFacilities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('credit-card-facility');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CreditCardFacility').should('exist');
    cy.url().should('match', creditCardFacilityPageUrlPattern);
  });

  describe('CreditCardFacility page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(creditCardFacilityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CreditCardFacility page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/credit-card-facility/new$'));
        cy.getEntityCreateUpdateHeading('CreditCardFacility');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', creditCardFacilityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/credit-card-facilities',

          body: {
            ...creditCardFacilitySample,
            bankCode: institutionCode,
            customerCategory: creditCardOwnership,
            currencyCode: isoCurrencyCode,
          },
        }).then(({ body }) => {
          creditCardFacility = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/credit-card-facilities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [creditCardFacility],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(creditCardFacilityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CreditCardFacility page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('creditCardFacility');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', creditCardFacilityPageUrlPattern);
      });

      it('edit button click should load edit CreditCardFacility page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CreditCardFacility');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', creditCardFacilityPageUrlPattern);
      });

      it('last delete button click should delete instance of CreditCardFacility', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('creditCardFacility').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', creditCardFacilityPageUrlPattern);

        creditCardFacility = undefined;
      });
    });
  });

  describe('new CreditCardFacility page', () => {
    beforeEach(() => {
      cy.visit(`${creditCardFacilityPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CreditCardFacility');
    });

    it('should create an instance of CreditCardFacility', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-03').should('have.value', '2023-10-03');

      cy.get(`[data-cy="totalNumberOfActiveCreditCards"]`).type('11529').should('have.value', '11529');

      cy.get(`[data-cy="totalCreditCardLimitsInCCY"]`).type('75697').should('have.value', '75697');

      cy.get(`[data-cy="totalCreditCardLimitsInLCY"]`).type('67853').should('have.value', '67853');

      cy.get(`[data-cy="totalCreditCardAmountUtilisedInCCY"]`).type('60093').should('have.value', '60093');

      cy.get(`[data-cy="totalCreditCardAmountUtilisedInLcy"]`).type('31972').should('have.value', '31972');

      cy.get(`[data-cy="totalNPACreditCardAmountInFCY"]`).type('1507').should('have.value', '1507');

      cy.get(`[data-cy="totalNPACreditCardAmountInLCY"]`).type('57207').should('have.value', '57207');

      cy.get(`[data-cy="bankCode"]`).select(1);
      cy.get(`[data-cy="customerCategory"]`).select(1);
      cy.get(`[data-cy="currencyCode"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        creditCardFacility = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', creditCardFacilityPageUrlPattern);
    });
  });
});
