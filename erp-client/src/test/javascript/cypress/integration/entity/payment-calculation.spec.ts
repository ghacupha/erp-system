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

describe('PaymentCalculation e2e test', () => {
  const paymentCalculationPageUrl = '/payment-calculation';
  const paymentCalculationPageUrlPattern = new RegExp('/payment-calculation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const paymentCalculationSample = {};

  let paymentCalculation: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-calculations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-calculations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-calculations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paymentCalculation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-calculations/${paymentCalculation.id}`,
      }).then(() => {
        paymentCalculation = undefined;
      });
    }
  });

  it('PaymentCalculations menu should load PaymentCalculations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-calculation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentCalculation').should('exist');
    cy.url().should('match', paymentCalculationPageUrlPattern);
  });

  describe('PaymentCalculation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentCalculationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentCalculation page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/payment-calculation/new$'));
        cy.getEntityCreateUpdateHeading('PaymentCalculation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentCalculationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-calculations',
          body: paymentCalculationSample,
        }).then(({ body }) => {
          paymentCalculation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-calculations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [paymentCalculation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentCalculationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentCalculation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentCalculation');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentCalculationPageUrlPattern);
      });

      it('edit button click should load edit PaymentCalculation page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentCalculation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentCalculationPageUrlPattern);
      });

      it('last delete button click should delete instance of PaymentCalculation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paymentCalculation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentCalculationPageUrlPattern);

        paymentCalculation = undefined;
      });
    });
  });

  describe('new PaymentCalculation page', () => {
    beforeEach(() => {
      cy.visit(`${paymentCalculationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('PaymentCalculation');
    });

    it('should create an instance of PaymentCalculation', () => {
      cy.get(`[data-cy="paymentExpense"]`).type('34762').should('have.value', '34762');

      cy.get(`[data-cy="withholdingVAT"]`).type('7746').should('have.value', '7746');

      cy.get(`[data-cy="withholdingTax"]`).type('26736').should('have.value', '26736');

      cy.get(`[data-cy="paymentAmount"]`).type('73037').should('have.value', '73037');

      cy.get(`[data-cy="fileUploadToken"]`).type('Associate Stand-alone').should('have.value', 'Associate Stand-alone');

      cy.get(`[data-cy="compilationToken"]`).type('Dynamic').should('have.value', 'Dynamic');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        paymentCalculation = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', paymentCalculationPageUrlPattern);
    });
  });
});
