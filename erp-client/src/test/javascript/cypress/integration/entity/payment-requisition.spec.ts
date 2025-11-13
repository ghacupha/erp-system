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

describe('PaymentRequisition e2e test', () => {
  const paymentRequisitionPageUrl = '/payment-requisition';
  const paymentRequisitionPageUrlPattern = new RegExp('/payment-requisition(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const paymentRequisitionSample = {};

  let paymentRequisition: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-requisitions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-requisitions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-requisitions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paymentRequisition) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-requisitions/${paymentRequisition.id}`,
      }).then(() => {
        paymentRequisition = undefined;
      });
    }
  });

  it('PaymentRequisitions menu should load PaymentRequisitions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-requisition');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentRequisition').should('exist');
    cy.url().should('match', paymentRequisitionPageUrlPattern);
  });

  describe('PaymentRequisition page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentRequisitionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentRequisition page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/payment-requisition/new$'));
        cy.getEntityCreateUpdateHeading('PaymentRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentRequisitionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-requisitions',
          body: paymentRequisitionSample,
        }).then(({ body }) => {
          paymentRequisition = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-requisitions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [paymentRequisition],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentRequisitionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentRequisition page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentRequisition');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentRequisitionPageUrlPattern);
      });

      it('edit button click should load edit PaymentRequisition page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentRequisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentRequisitionPageUrlPattern);
      });

      it('last delete button click should delete instance of PaymentRequisition', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paymentRequisition').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentRequisitionPageUrlPattern);

        paymentRequisition = undefined;
      });
    });
  });

  describe('new PaymentRequisition page', () => {
    beforeEach(() => {
      cy.visit(`${paymentRequisitionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('PaymentRequisition');
    });

    it('should create an instance of PaymentRequisition', () => {
      cy.get(`[data-cy="receptionDate"]`).type('2021-07-20').should('have.value', '2021-07-20');

      cy.get(`[data-cy="dealerName"]`).type('synergies').should('have.value', 'synergies');

      cy.get(`[data-cy="briefDescription"]`).type('Cotton Turkey').should('have.value', 'Cotton Turkey');

      cy.get(`[data-cy="requisitionNumber"]`).type('SSL index users').should('have.value', 'SSL index users');

      cy.get(`[data-cy="invoicedAmount"]`).type('87374').should('have.value', '87374');

      cy.get(`[data-cy="disbursementCost"]`).type('76373').should('have.value', '76373');

      cy.get(`[data-cy="taxableAmount"]`).type('86246').should('have.value', '86246');

      cy.get(`[data-cy="requisitionProcessed"]`).should('not.be.checked');
      cy.get(`[data-cy="requisitionProcessed"]`).click().should('be.checked');

      cy.get(`[data-cy="fileUploadToken"]`).type('Israel Kuwaiti').should('have.value', 'Israel Kuwaiti');

      cy.get(`[data-cy="compilationToken"]`).type('overriding Mississippi Algeria').should('have.value', 'overriding Mississippi Algeria');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        paymentRequisition = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', paymentRequisitionPageUrlPattern);
    });
  });
});
