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

describe('PaymentLabel e2e test', () => {
  const paymentLabelPageUrl = '/payment-label';
  const paymentLabelPageUrlPattern = new RegExp('/payment-label(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const paymentLabelSample = { description: 'Chicken' };

  let paymentLabel: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/payment-labels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/payment-labels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/payment-labels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paymentLabel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/payment-labels/${paymentLabel.id}`,
      }).then(() => {
        paymentLabel = undefined;
      });
    }
  });

  it('PaymentLabels menu should load PaymentLabels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('payment-label');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaymentLabel').should('exist');
    cy.url().should('match', paymentLabelPageUrlPattern);
  });

  describe('PaymentLabel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paymentLabelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaymentLabel page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/payment-label/new$'));
        cy.getEntityCreateUpdateHeading('PaymentLabel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentLabelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/payment-labels',
          body: paymentLabelSample,
        }).then(({ body }) => {
          paymentLabel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/payment-labels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [paymentLabel],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(paymentLabelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaymentLabel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paymentLabel');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentLabelPageUrlPattern);
      });

      it('edit button click should load edit PaymentLabel page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaymentLabel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentLabelPageUrlPattern);
      });

      it('last delete button click should delete instance of PaymentLabel', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paymentLabel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paymentLabelPageUrlPattern);

        paymentLabel = undefined;
      });
    });
  });

  describe('new PaymentLabel page', () => {
    beforeEach(() => {
      cy.visit(`${paymentLabelPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('PaymentLabel');
    });

    it('should create an instance of PaymentLabel', () => {
      cy.get(`[data-cy="description"]`).type('relationships Points').should('have.value', 'relationships Points');

      cy.get(`[data-cy="comments"]`).type('Crest open-source Mexico').should('have.value', 'Crest open-source Mexico');

      cy.get(`[data-cy="fileUploadToken"]`).type('Outdoors').should('have.value', 'Outdoors');

      cy.get(`[data-cy="compilationToken"]`).type('Function-based viral').should('have.value', 'Function-based viral');

      cy.get(`[data-cy="remarks"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        paymentLabel = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', paymentLabelPageUrlPattern);
    });
  });
});
