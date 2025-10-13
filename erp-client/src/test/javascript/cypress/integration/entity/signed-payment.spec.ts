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

describe('SignedPayment e2e test', () => {
  const signedPaymentPageUrl = '/signed-payment';
  const signedPaymentPageUrlPattern = new RegExp('/signed-payment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const signedPaymentSample = {
    transactionNumber: 'asymmetric calculating Rubber',
    transactionDate: '2021-09-19',
    transactionCurrency: 'JPY',
    transactionAmount: 49642,
  };

  let signedPayment: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/signed-payments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/signed-payments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/signed-payments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (signedPayment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/signed-payments/${signedPayment.id}`,
      }).then(() => {
        signedPayment = undefined;
      });
    }
  });

  it('SignedPayments menu should load SignedPayments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('signed-payment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SignedPayment').should('exist');
    cy.url().should('match', signedPaymentPageUrlPattern);
  });

  describe('SignedPayment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(signedPaymentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SignedPayment page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/signed-payment/new$'));
        cy.getEntityCreateUpdateHeading('SignedPayment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', signedPaymentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/signed-payments',
          body: signedPaymentSample,
        }).then(({ body }) => {
          signedPayment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/signed-payments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [signedPayment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(signedPaymentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SignedPayment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('signedPayment');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', signedPaymentPageUrlPattern);
      });

      it('edit button click should load edit SignedPayment page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SignedPayment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', signedPaymentPageUrlPattern);
      });

      it('last delete button click should delete instance of SignedPayment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('signedPayment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', signedPaymentPageUrlPattern);

        signedPayment = undefined;
      });
    });
  });

  describe('new SignedPayment page', () => {
    beforeEach(() => {
      cy.visit(`${signedPaymentPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SignedPayment');
    });

    it('should create an instance of SignedPayment', () => {
      cy.get(`[data-cy="transactionNumber"]`).type('Mouse').should('have.value', 'Mouse');

      cy.get(`[data-cy="transactionDate"]`).type('2021-09-20').should('have.value', '2021-09-20');

      cy.get(`[data-cy="transactionCurrency"]`).select('UGX');

      cy.get(`[data-cy="transactionAmount"]`).type('21384').should('have.value', '21384');

      cy.get(`[data-cy="dealerName"]`).type('District Towels Dirham').should('have.value', 'District Towels Dirham');

      cy.get(`[data-cy="fileUploadToken"]`).type('Up-sized Soft').should('have.value', 'Up-sized Soft');

      cy.get(`[data-cy="compilationToken"]`).type('Chile drive infomediaries').should('have.value', 'Chile drive infomediaries');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        signedPayment = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', signedPaymentPageUrlPattern);
    });
  });
});
