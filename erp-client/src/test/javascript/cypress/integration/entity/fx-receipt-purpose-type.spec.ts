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

describe('FxReceiptPurposeType e2e test', () => {
  const fxReceiptPurposeTypePageUrl = '/fx-receipt-purpose-type';
  const fxReceiptPurposeTypePageUrlPattern = new RegExp('/fx-receipt-purpose-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fxReceiptPurposeTypeSample = { itemCode: 'Bedfordshire' };

  let fxReceiptPurposeType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fx-receipt-purpose-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fx-receipt-purpose-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fx-receipt-purpose-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fxReceiptPurposeType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fx-receipt-purpose-types/${fxReceiptPurposeType.id}`,
      }).then(() => {
        fxReceiptPurposeType = undefined;
      });
    }
  });

  it('FxReceiptPurposeTypes menu should load FxReceiptPurposeTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fx-receipt-purpose-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FxReceiptPurposeType').should('exist');
    cy.url().should('match', fxReceiptPurposeTypePageUrlPattern);
  });

  describe('FxReceiptPurposeType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fxReceiptPurposeTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FxReceiptPurposeType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fx-receipt-purpose-type/new$'));
        cy.getEntityCreateUpdateHeading('FxReceiptPurposeType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxReceiptPurposeTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fx-receipt-purpose-types',
          body: fxReceiptPurposeTypeSample,
        }).then(({ body }) => {
          fxReceiptPurposeType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fx-receipt-purpose-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fxReceiptPurposeType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fxReceiptPurposeTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FxReceiptPurposeType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fxReceiptPurposeType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxReceiptPurposeTypePageUrlPattern);
      });

      it('edit button click should load edit FxReceiptPurposeType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FxReceiptPurposeType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxReceiptPurposeTypePageUrlPattern);
      });

      it('last delete button click should delete instance of FxReceiptPurposeType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fxReceiptPurposeType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxReceiptPurposeTypePageUrlPattern);

        fxReceiptPurposeType = undefined;
      });
    });
  });

  describe('new FxReceiptPurposeType page', () => {
    beforeEach(() => {
      cy.visit(`${fxReceiptPurposeTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FxReceiptPurposeType');
    });

    it('should create an instance of FxReceiptPurposeType', () => {
      cy.get(`[data-cy="itemCode"]`).type('GB').should('have.value', 'GB');

      cy.get(`[data-cy="attribute1ReceiptPaymentPurposeCode"]`).type('SDR leading-edge').should('have.value', 'SDR leading-edge');

      cy.get(`[data-cy="attribute1ReceiptPaymentPurposeType"]`)
        .type('Dynamic Handcrafted Bedfordshire')
        .should('have.value', 'Dynamic Handcrafted Bedfordshire');

      cy.get(`[data-cy="attribute2ReceiptPaymentPurposeCode"]`)
        .type('even-keeled withdrawal')
        .should('have.value', 'even-keeled withdrawal');

      cy.get(`[data-cy="attribute2ReceiptPaymentPurposeDescription"]`).type('Fresh Tasty Sleek').should('have.value', 'Fresh Tasty Sleek');

      cy.get(`[data-cy="attribute3ReceiptPaymentPurposeCode"]`)
        .type('modular Director driver')
        .should('have.value', 'modular Director driver');

      cy.get(`[data-cy="attribute3ReceiptPaymentPurposeDescription"]`)
        .type('Accountability Grenada Kentucky')
        .should('have.value', 'Accountability Grenada Kentucky');

      cy.get(`[data-cy="attribute4ReceiptPaymentPurposeCode"]`).type('Investment').should('have.value', 'Investment');

      cy.get(`[data-cy="attribute4ReceiptPaymentPurposeDescription"]`)
        .type('optimizing Principal')
        .should('have.value', 'optimizing Principal');

      cy.get(`[data-cy="attribute5ReceiptPaymentPurposeCode"]`).type('content').should('have.value', 'content');

      cy.get(`[data-cy="attribute5ReceiptPaymentPurposeDescription"]`)
        .type('Borders neural-net Frozen')
        .should('have.value', 'Borders neural-net Frozen');

      cy.get(`[data-cy="lastChild"]`).type('invoice Triple-buffered').should('have.value', 'invoice Triple-buffered');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fxReceiptPurposeType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fxReceiptPurposeTypePageUrlPattern);
    });
  });
});
