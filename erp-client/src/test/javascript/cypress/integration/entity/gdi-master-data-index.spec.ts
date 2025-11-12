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

describe('GdiMasterDataIndex e2e test', () => {
  const gdiMasterDataIndexPageUrl = '/gdi-master-data-index';
  const gdiMasterDataIndexPageUrlPattern = new RegExp('/gdi-master-data-index(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const gdiMasterDataIndexSample = { entityName: 'Fresh', databaseName: 'Island' };

  let gdiMasterDataIndex: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/gdi-master-data-indices+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/gdi-master-data-indices').as('postEntityRequest');
    cy.intercept('DELETE', '/api/gdi-master-data-indices/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (gdiMasterDataIndex) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/gdi-master-data-indices/${gdiMasterDataIndex.id}`,
      }).then(() => {
        gdiMasterDataIndex = undefined;
      });
    }
  });

  it('GdiMasterDataIndices menu should load GdiMasterDataIndices page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('gdi-master-data-index');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GdiMasterDataIndex').should('exist');
    cy.url().should('match', gdiMasterDataIndexPageUrlPattern);
  });

  describe('GdiMasterDataIndex page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(gdiMasterDataIndexPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GdiMasterDataIndex page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/gdi-master-data-index/new$'));
        cy.getEntityCreateUpdateHeading('GdiMasterDataIndex');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', gdiMasterDataIndexPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/gdi-master-data-indices',
          body: gdiMasterDataIndexSample,
        }).then(({ body }) => {
          gdiMasterDataIndex = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/gdi-master-data-indices+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [gdiMasterDataIndex],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(gdiMasterDataIndexPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GdiMasterDataIndex page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('gdiMasterDataIndex');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', gdiMasterDataIndexPageUrlPattern);
      });

      it('edit button click should load edit GdiMasterDataIndex page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GdiMasterDataIndex');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', gdiMasterDataIndexPageUrlPattern);
      });

      it('last delete button click should delete instance of GdiMasterDataIndex', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('gdiMasterDataIndex').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', gdiMasterDataIndexPageUrlPattern);

        gdiMasterDataIndex = undefined;
      });
    });
  });

  describe('new GdiMasterDataIndex page', () => {
    beforeEach(() => {
      cy.visit(`${gdiMasterDataIndexPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('GdiMasterDataIndex');
    });

    it('should create an instance of GdiMasterDataIndex', () => {
      cy.get(`[data-cy="entityName"]`).type('Practical').should('have.value', 'Practical');

      cy.get(`[data-cy="databaseName"]`).type('Account black monitor').should('have.value', 'Account black monitor');

      cy.get(`[data-cy="businessDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        gdiMasterDataIndex = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', gdiMasterDataIndexPageUrlPattern);
    });
  });
});
