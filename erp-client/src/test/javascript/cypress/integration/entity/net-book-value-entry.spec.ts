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

describe('NetBookValueEntry e2e test', () => {
  const netBookValueEntryPageUrl = '/net-book-value-entry';
  const netBookValueEntryPageUrlPattern = new RegExp('/net-book-value-entry(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const netBookValueEntrySample = { nbvIdentifier: '4622a5a7-a3dd-4c02-8f33-8b72e17043b2' };

  let netBookValueEntry: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/net-book-value-entries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/net-book-value-entries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/net-book-value-entries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (netBookValueEntry) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/net-book-value-entries/${netBookValueEntry.id}`,
      }).then(() => {
        netBookValueEntry = undefined;
      });
    }
  });

  it('NetBookValueEntries menu should load NetBookValueEntries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('net-book-value-entry');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('NetBookValueEntry').should('exist');
    cy.url().should('match', netBookValueEntryPageUrlPattern);
  });

  describe('NetBookValueEntry page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(netBookValueEntryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create NetBookValueEntry page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/net-book-value-entry/new$'));
        cy.getEntityCreateUpdateHeading('NetBookValueEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', netBookValueEntryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/net-book-value-entries',
          body: netBookValueEntrySample,
        }).then(({ body }) => {
          netBookValueEntry = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/net-book-value-entries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [netBookValueEntry],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(netBookValueEntryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details NetBookValueEntry page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('netBookValueEntry');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', netBookValueEntryPageUrlPattern);
      });

      it('edit button click should load edit NetBookValueEntry page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NetBookValueEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', netBookValueEntryPageUrlPattern);
      });

      it('last delete button click should delete instance of NetBookValueEntry', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('netBookValueEntry').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', netBookValueEntryPageUrlPattern);

        netBookValueEntry = undefined;
      });
    });
  });

  describe('new NetBookValueEntry page', () => {
    beforeEach(() => {
      cy.visit(`${netBookValueEntryPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('NetBookValueEntry');
    });

    it('should create an instance of NetBookValueEntry', () => {
      cy.get(`[data-cy="assetNumber"]`).type('iterate Beauty disintermediate').should('have.value', 'iterate Beauty disintermediate');

      cy.get(`[data-cy="assetTag"]`).type('Unbranded bi-directional').should('have.value', 'Unbranded bi-directional');

      cy.get(`[data-cy="assetDescription"]`).type('Soap Account Dynamic').should('have.value', 'Soap Account Dynamic');

      cy.get(`[data-cy="nbvIdentifier"]`)
        .type('adaad1af-4d05-413d-878d-f8f6787c206c')
        .invoke('val')
        .should('match', new RegExp('adaad1af-4d05-413d-878d-f8f6787c206c'));

      cy.get(`[data-cy="compilationJobIdentifier"]`)
        .type('76db33c4-c564-4cfe-bc95-871f4612a38d')
        .invoke('val')
        .should('match', new RegExp('76db33c4-c564-4cfe-bc95-871f4612a38d'));

      cy.get(`[data-cy="compilationBatchIdentifier"]`)
        .type('9a6ff1ea-26d7-48d5-bf81-c6cf2205fc72')
        .invoke('val')
        .should('match', new RegExp('9a6ff1ea-26d7-48d5-bf81-c6cf2205fc72'));

      cy.get(`[data-cy="elapsedMonths"]`).type('81550').should('have.value', '81550');

      cy.get(`[data-cy="priorMonths"]`).type('10233').should('have.value', '10233');

      cy.get(`[data-cy="usefulLifeYears"]`).type('24753').should('have.value', '24753');

      cy.get(`[data-cy="netBookValueAmount"]`).type('31520').should('have.value', '31520');

      cy.get(`[data-cy="previousNetBookValueAmount"]`).type('25295').should('have.value', '25295');

      cy.get(`[data-cy="historicalCost"]`).type('870').should('have.value', '870');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        netBookValueEntry = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', netBookValueEntryPageUrlPattern);
    });
  });
});
