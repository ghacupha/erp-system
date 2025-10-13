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

describe('DepreciationEntry e2e test', () => {
  const depreciationEntryPageUrl = '/depreciation-entry';
  const depreciationEntryPageUrlPattern = new RegExp('/depreciation-entry(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const depreciationEntrySample = {};

  let depreciationEntry: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/depreciation-entries+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/depreciation-entries').as('postEntityRequest');
    cy.intercept('DELETE', '/api/depreciation-entries/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (depreciationEntry) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/depreciation-entries/${depreciationEntry.id}`,
      }).then(() => {
        depreciationEntry = undefined;
      });
    }
  });

  it('DepreciationEntries menu should load DepreciationEntries page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('depreciation-entry');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DepreciationEntry').should('exist');
    cy.url().should('match', depreciationEntryPageUrlPattern);
  });

  describe('DepreciationEntry page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(depreciationEntryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DepreciationEntry page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/depreciation-entry/new$'));
        cy.getEntityCreateUpdateHeading('DepreciationEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationEntryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/depreciation-entries',
          body: depreciationEntrySample,
        }).then(({ body }) => {
          depreciationEntry = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/depreciation-entries+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [depreciationEntry],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(depreciationEntryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DepreciationEntry page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('depreciationEntry');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationEntryPageUrlPattern);
      });

      it('edit button click should load edit DepreciationEntry page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DepreciationEntry');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationEntryPageUrlPattern);
      });

      it('last delete button click should delete instance of DepreciationEntry', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('depreciationEntry').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', depreciationEntryPageUrlPattern);

        depreciationEntry = undefined;
      });
    });
  });

  describe('new DepreciationEntry page', () => {
    beforeEach(() => {
      cy.visit(`${depreciationEntryPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('DepreciationEntry');
    });

    it('should create an instance of DepreciationEntry', () => {
      cy.get(`[data-cy="postedAt"]`).type('2023-07-03T18:02').should('have.value', '2023-07-03T18:02');

      cy.get(`[data-cy="depreciationAmount"]`).type('27043').should('have.value', '27043');

      cy.get(`[data-cy="assetNumber"]`).type('31619').should('have.value', '31619');

      cy.get(`[data-cy="batchSequenceNumber"]`).type('4626').should('have.value', '4626');

      cy.get(`[data-cy="processedItems"]`).type('Analyst').should('have.value', 'Analyst');

      cy.get(`[data-cy="totalItemsProcessed"]`).type('3035').should('have.value', '3035');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        depreciationEntry = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', depreciationEntryPageUrlPattern);
    });
  });
});
