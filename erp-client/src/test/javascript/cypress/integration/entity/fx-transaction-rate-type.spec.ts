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

describe('FxTransactionRateType e2e test', () => {
  const fxTransactionRateTypePageUrl = '/fx-transaction-rate-type';
  const fxTransactionRateTypePageUrlPattern = new RegExp('/fx-transaction-rate-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fxTransactionRateTypeSample = { fxTransactionRateTypeCode: 'user-centric AI', fxTransactionRateType: 'embrace' };

  let fxTransactionRateType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fx-transaction-rate-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fx-transaction-rate-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fx-transaction-rate-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fxTransactionRateType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fx-transaction-rate-types/${fxTransactionRateType.id}`,
      }).then(() => {
        fxTransactionRateType = undefined;
      });
    }
  });

  it('FxTransactionRateTypes menu should load FxTransactionRateTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fx-transaction-rate-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FxTransactionRateType').should('exist');
    cy.url().should('match', fxTransactionRateTypePageUrlPattern);
  });

  describe('FxTransactionRateType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fxTransactionRateTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FxTransactionRateType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fx-transaction-rate-type/new$'));
        cy.getEntityCreateUpdateHeading('FxTransactionRateType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxTransactionRateTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fx-transaction-rate-types',
          body: fxTransactionRateTypeSample,
        }).then(({ body }) => {
          fxTransactionRateType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fx-transaction-rate-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fxTransactionRateType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fxTransactionRateTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FxTransactionRateType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fxTransactionRateType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxTransactionRateTypePageUrlPattern);
      });

      it('edit button click should load edit FxTransactionRateType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FxTransactionRateType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxTransactionRateTypePageUrlPattern);
      });

      it('last delete button click should delete instance of FxTransactionRateType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fxTransactionRateType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxTransactionRateTypePageUrlPattern);

        fxTransactionRateType = undefined;
      });
    });
  });

  describe('new FxTransactionRateType page', () => {
    beforeEach(() => {
      cy.visit(`${fxTransactionRateTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FxTransactionRateType');
    });

    it('should create an instance of FxTransactionRateType', () => {
      cy.get(`[data-cy="fxTransactionRateTypeCode"]`).type('Analyst silver Fresh').should('have.value', 'Analyst silver Fresh');

      cy.get(`[data-cy="fxTransactionRateType"]`).type('metrics web-enabled Belarus').should('have.value', 'metrics web-enabled Belarus');

      cy.get(`[data-cy="fxTransactionRateTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fxTransactionRateType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fxTransactionRateTypePageUrlPattern);
    });
  });
});
