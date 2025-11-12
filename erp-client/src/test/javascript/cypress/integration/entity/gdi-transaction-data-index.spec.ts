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

describe('GdiTransactionDataIndex e2e test', () => {
  const gdiTransactionDataIndexPageUrl = '/gdi-transaction-data-index';
  const gdiTransactionDataIndexPageUrlPattern = new RegExp('/gdi-transaction-data-index(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const gdiTransactionDataIndexSample = {
    datasetName: 'Shoes innovate AI',
    databaseName: 'ivory New synthesizing',
    updateFrequency: 'INTRA_DAY',
    datasetBehavior: 'INSERT_AND_UPDATE',
  };

  let gdiTransactionDataIndex: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/gdi-transaction-data-indices+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/gdi-transaction-data-indices').as('postEntityRequest');
    cy.intercept('DELETE', '/api/gdi-transaction-data-indices/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (gdiTransactionDataIndex) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/gdi-transaction-data-indices/${gdiTransactionDataIndex.id}`,
      }).then(() => {
        gdiTransactionDataIndex = undefined;
      });
    }
  });

  it('GdiTransactionDataIndices menu should load GdiTransactionDataIndices page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('gdi-transaction-data-index');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GdiTransactionDataIndex').should('exist');
    cy.url().should('match', gdiTransactionDataIndexPageUrlPattern);
  });

  describe('GdiTransactionDataIndex page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(gdiTransactionDataIndexPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GdiTransactionDataIndex page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/gdi-transaction-data-index/new$'));
        cy.getEntityCreateUpdateHeading('GdiTransactionDataIndex');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', gdiTransactionDataIndexPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/gdi-transaction-data-indices',
          body: gdiTransactionDataIndexSample,
        }).then(({ body }) => {
          gdiTransactionDataIndex = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/gdi-transaction-data-indices+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [gdiTransactionDataIndex],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(gdiTransactionDataIndexPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GdiTransactionDataIndex page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('gdiTransactionDataIndex');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', gdiTransactionDataIndexPageUrlPattern);
      });

      it('edit button click should load edit GdiTransactionDataIndex page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GdiTransactionDataIndex');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', gdiTransactionDataIndexPageUrlPattern);
      });

      it('last delete button click should delete instance of GdiTransactionDataIndex', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('gdiTransactionDataIndex').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', gdiTransactionDataIndexPageUrlPattern);

        gdiTransactionDataIndex = undefined;
      });
    });
  });

  describe('new GdiTransactionDataIndex page', () => {
    beforeEach(() => {
      cy.visit(`${gdiTransactionDataIndexPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('GdiTransactionDataIndex');
    });

    it('should create an instance of GdiTransactionDataIndex', () => {
      cy.get(`[data-cy="datasetName"]`).type('teal Representative Serbia').should('have.value', 'teal Representative Serbia');

      cy.get(`[data-cy="databaseName"]`).type('FTP').should('have.value', 'FTP');

      cy.get(`[data-cy="updateFrequency"]`).select('OTHER');

      cy.get(`[data-cy="datasetBehavior"]`).select('INSERT_AND_UPDATE');

      cy.get(`[data-cy="minimumDatarowsPerRequest"]`).type('67332').should('have.value', '67332');

      cy.get(`[data-cy="maximumDataRowsPerRequest"]`).type('47959').should('have.value', '47959');

      cy.get(`[data-cy="datasetDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.setFieldImageAsBytesOfEntity('dataTemplate', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        gdiTransactionDataIndex = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', gdiTransactionDataIndexPageUrlPattern);
    });
  });
});
