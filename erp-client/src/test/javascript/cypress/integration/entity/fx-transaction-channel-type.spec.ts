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

describe('FxTransactionChannelType e2e test', () => {
  const fxTransactionChannelTypePageUrl = '/fx-transaction-channel-type';
  const fxTransactionChannelTypePageUrlPattern = new RegExp('/fx-transaction-channel-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fxTransactionChannelTypeSample = { fxTransactionChannelCode: 'Customer alarm B2C', fxTransactionChannelType: 'compress Architect' };

  let fxTransactionChannelType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fx-transaction-channel-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fx-transaction-channel-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fx-transaction-channel-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fxTransactionChannelType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fx-transaction-channel-types/${fxTransactionChannelType.id}`,
      }).then(() => {
        fxTransactionChannelType = undefined;
      });
    }
  });

  it('FxTransactionChannelTypes menu should load FxTransactionChannelTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fx-transaction-channel-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FxTransactionChannelType').should('exist');
    cy.url().should('match', fxTransactionChannelTypePageUrlPattern);
  });

  describe('FxTransactionChannelType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fxTransactionChannelTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FxTransactionChannelType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fx-transaction-channel-type/new$'));
        cy.getEntityCreateUpdateHeading('FxTransactionChannelType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxTransactionChannelTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fx-transaction-channel-types',
          body: fxTransactionChannelTypeSample,
        }).then(({ body }) => {
          fxTransactionChannelType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fx-transaction-channel-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fxTransactionChannelType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fxTransactionChannelTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FxTransactionChannelType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fxTransactionChannelType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxTransactionChannelTypePageUrlPattern);
      });

      it('edit button click should load edit FxTransactionChannelType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FxTransactionChannelType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxTransactionChannelTypePageUrlPattern);
      });

      it('last delete button click should delete instance of FxTransactionChannelType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fxTransactionChannelType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxTransactionChannelTypePageUrlPattern);

        fxTransactionChannelType = undefined;
      });
    });
  });

  describe('new FxTransactionChannelType page', () => {
    beforeEach(() => {
      cy.visit(`${fxTransactionChannelTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FxTransactionChannelType');
    });

    it('should create an instance of FxTransactionChannelType', () => {
      cy.get(`[data-cy="fxTransactionChannelCode"]`).type('Rustic Internal Small').should('have.value', 'Rustic Internal Small');

      cy.get(`[data-cy="fxTransactionChannelType"]`)
        .type('Implemented deposit Incredible')
        .should('have.value', 'Implemented deposit Incredible');

      cy.get(`[data-cy="fxChannelTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fxTransactionChannelType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fxTransactionChannelTypePageUrlPattern);
    });
  });
});
