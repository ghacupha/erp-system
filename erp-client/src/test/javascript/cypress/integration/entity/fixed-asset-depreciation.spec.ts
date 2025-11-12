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

describe('FixedAssetDepreciation e2e test', () => {
  const fixedAssetDepreciationPageUrl = '/fixed-asset-depreciation';
  const fixedAssetDepreciationPageUrlPattern = new RegExp('/fixed-asset-depreciation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fixedAssetDepreciationSample = {};

  let fixedAssetDepreciation: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fixed-asset-depreciations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fixed-asset-depreciations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fixed-asset-depreciations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fixedAssetDepreciation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fixed-asset-depreciations/${fixedAssetDepreciation.id}`,
      }).then(() => {
        fixedAssetDepreciation = undefined;
      });
    }
  });

  it('FixedAssetDepreciations menu should load FixedAssetDepreciations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fixed-asset-depreciation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FixedAssetDepreciation').should('exist');
    cy.url().should('match', fixedAssetDepreciationPageUrlPattern);
  });

  describe('FixedAssetDepreciation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fixedAssetDepreciationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FixedAssetDepreciation page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fixed-asset-depreciation/new$'));
        cy.getEntityCreateUpdateHeading('FixedAssetDepreciation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fixedAssetDepreciationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fixed-asset-depreciations',
          body: fixedAssetDepreciationSample,
        }).then(({ body }) => {
          fixedAssetDepreciation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fixed-asset-depreciations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fixedAssetDepreciation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fixedAssetDepreciationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FixedAssetDepreciation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fixedAssetDepreciation');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fixedAssetDepreciationPageUrlPattern);
      });

      it('edit button click should load edit FixedAssetDepreciation page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FixedAssetDepreciation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fixedAssetDepreciationPageUrlPattern);
      });

      it('last delete button click should delete instance of FixedAssetDepreciation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fixedAssetDepreciation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fixedAssetDepreciationPageUrlPattern);

        fixedAssetDepreciation = undefined;
      });
    });
  });

  describe('new FixedAssetDepreciation page', () => {
    beforeEach(() => {
      cy.visit(`${fixedAssetDepreciationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FixedAssetDepreciation');
    });

    it('should create an instance of FixedAssetDepreciation', () => {
      cy.get(`[data-cy="assetNumber"]`).type('42900').should('have.value', '42900');

      cy.get(`[data-cy="serviceOutletCode"]`).type('Account').should('have.value', 'Account');

      cy.get(`[data-cy="assetTag"]`).type('capability Rhode Function-based').should('have.value', 'capability Rhode Function-based');

      cy.get(`[data-cy="assetDescription"]`).type('rich Balboa overriding').should('have.value', 'rich Balboa overriding');

      cy.get(`[data-cy="depreciationDate"]`).type('2021-03-23').should('have.value', '2021-03-23');

      cy.get(`[data-cy="assetCategory"]`).type('synthesize').should('have.value', 'synthesize');

      cy.get(`[data-cy="depreciationAmount"]`).type('70788').should('have.value', '70788');

      cy.get(`[data-cy="depreciationRegime"]`).select('STRAIGHT_LINE_BASIS');

      cy.get(`[data-cy="fileUploadToken"]`).type('projection Belarus').should('have.value', 'projection Belarus');

      cy.get(`[data-cy="compilationToken"]`).type('wireless').should('have.value', 'wireless');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fixedAssetDepreciation = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fixedAssetDepreciationPageUrlPattern);
    });
  });
});
