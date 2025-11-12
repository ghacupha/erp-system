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

describe('FxTransactionType e2e test', () => {
  const fxTransactionTypePageUrl = '/fx-transaction-type';
  const fxTransactionTypePageUrlPattern = new RegExp('/fx-transaction-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fxTransactionTypeSample = { fxTransactionTypeCode: 'Frozen', fxTransactionType: 'real-time networks Wooden' };

  let fxTransactionType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fx-transaction-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fx-transaction-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fx-transaction-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fxTransactionType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fx-transaction-types/${fxTransactionType.id}`,
      }).then(() => {
        fxTransactionType = undefined;
      });
    }
  });

  it('FxTransactionTypes menu should load FxTransactionTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fx-transaction-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FxTransactionType').should('exist');
    cy.url().should('match', fxTransactionTypePageUrlPattern);
  });

  describe('FxTransactionType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fxTransactionTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FxTransactionType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fx-transaction-type/new$'));
        cy.getEntityCreateUpdateHeading('FxTransactionType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxTransactionTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fx-transaction-types',
          body: fxTransactionTypeSample,
        }).then(({ body }) => {
          fxTransactionType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fx-transaction-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fxTransactionType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fxTransactionTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FxTransactionType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fxTransactionType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxTransactionTypePageUrlPattern);
      });

      it('edit button click should load edit FxTransactionType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FxTransactionType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxTransactionTypePageUrlPattern);
      });

      it('last delete button click should delete instance of FxTransactionType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fxTransactionType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxTransactionTypePageUrlPattern);

        fxTransactionType = undefined;
      });
    });
  });

  describe('new FxTransactionType page', () => {
    beforeEach(() => {
      cy.visit(`${fxTransactionTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FxTransactionType');
    });

    it('should create an instance of FxTransactionType', () => {
      cy.get(`[data-cy="fxTransactionTypeCode"]`).type('neural JSON cyan').should('have.value', 'neural JSON cyan');

      cy.get(`[data-cy="fxTransactionType"]`).type('Georgia hack').should('have.value', 'Georgia hack');

      cy.get(`[data-cy="fxTransactionTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fxTransactionType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fxTransactionTypePageUrlPattern);
    });
  });
});
