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

describe('FixedAssetNetBookValue e2e test', () => {
  const fixedAssetNetBookValuePageUrl = '/fixed-asset-net-book-value';
  const fixedAssetNetBookValuePageUrlPattern = new RegExp('/fixed-asset-net-book-value(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fixedAssetNetBookValueSample = {};

  let fixedAssetNetBookValue: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fixed-asset-net-book-values+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fixed-asset-net-book-values').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fixed-asset-net-book-values/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fixedAssetNetBookValue) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fixed-asset-net-book-values/${fixedAssetNetBookValue.id}`,
      }).then(() => {
        fixedAssetNetBookValue = undefined;
      });
    }
  });

  it('FixedAssetNetBookValues menu should load FixedAssetNetBookValues page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fixed-asset-net-book-value');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FixedAssetNetBookValue').should('exist');
    cy.url().should('match', fixedAssetNetBookValuePageUrlPattern);
  });

  describe('FixedAssetNetBookValue page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fixedAssetNetBookValuePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FixedAssetNetBookValue page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fixed-asset-net-book-value/new$'));
        cy.getEntityCreateUpdateHeading('FixedAssetNetBookValue');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fixedAssetNetBookValuePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fixed-asset-net-book-values',
          body: fixedAssetNetBookValueSample,
        }).then(({ body }) => {
          fixedAssetNetBookValue = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fixed-asset-net-book-values+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fixedAssetNetBookValue],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fixedAssetNetBookValuePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FixedAssetNetBookValue page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fixedAssetNetBookValue');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fixedAssetNetBookValuePageUrlPattern);
      });

      it('edit button click should load edit FixedAssetNetBookValue page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FixedAssetNetBookValue');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fixedAssetNetBookValuePageUrlPattern);
      });

      it('last delete button click should delete instance of FixedAssetNetBookValue', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fixedAssetNetBookValue').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fixedAssetNetBookValuePageUrlPattern);

        fixedAssetNetBookValue = undefined;
      });
    });
  });

  describe('new FixedAssetNetBookValue page', () => {
    beforeEach(() => {
      cy.visit(`${fixedAssetNetBookValuePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FixedAssetNetBookValue');
    });

    it('should create an instance of FixedAssetNetBookValue', () => {
      cy.get(`[data-cy="assetNumber"]`).type('97738').should('have.value', '97738');

      cy.get(`[data-cy="serviceOutletCode"]`).type('optimal Alabama').should('have.value', 'optimal Alabama');

      cy.get(`[data-cy="assetTag"]`).type('Administrator Auto').should('have.value', 'Administrator Auto');

      cy.get(`[data-cy="assetDescription"]`).type('green Director strategize').should('have.value', 'green Director strategize');

      cy.get(`[data-cy="netBookValueDate"]`).type('2021-03-23').should('have.value', '2021-03-23');

      cy.get(`[data-cy="assetCategory"]`).type('Plastic Granite').should('have.value', 'Plastic Granite');

      cy.get(`[data-cy="netBookValue"]`).type('91249').should('have.value', '91249');

      cy.get(`[data-cy="depreciationRegime"]`).select('STRAIGHT_LINE_BASIS');

      cy.get(`[data-cy="fileUploadToken"]`).type('Synergistic').should('have.value', 'Synergistic');

      cy.get(`[data-cy="compilationToken"]`).type('Mill Global Practical').should('have.value', 'Mill Global Practical');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fixedAssetNetBookValue = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fixedAssetNetBookValuePageUrlPattern);
    });
  });
});
