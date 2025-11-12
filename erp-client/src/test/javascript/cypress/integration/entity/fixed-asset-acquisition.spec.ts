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

describe('FixedAssetAcquisition e2e test', () => {
  const fixedAssetAcquisitionPageUrl = '/fixed-asset-acquisition';
  const fixedAssetAcquisitionPageUrlPattern = new RegExp('/fixed-asset-acquisition(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fixedAssetAcquisitionSample = {};

  let fixedAssetAcquisition: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fixed-asset-acquisitions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fixed-asset-acquisitions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fixed-asset-acquisitions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fixedAssetAcquisition) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fixed-asset-acquisitions/${fixedAssetAcquisition.id}`,
      }).then(() => {
        fixedAssetAcquisition = undefined;
      });
    }
  });

  it('FixedAssetAcquisitions menu should load FixedAssetAcquisitions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fixed-asset-acquisition');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FixedAssetAcquisition').should('exist');
    cy.url().should('match', fixedAssetAcquisitionPageUrlPattern);
  });

  describe('FixedAssetAcquisition page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fixedAssetAcquisitionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FixedAssetAcquisition page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fixed-asset-acquisition/new$'));
        cy.getEntityCreateUpdateHeading('FixedAssetAcquisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fixedAssetAcquisitionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fixed-asset-acquisitions',
          body: fixedAssetAcquisitionSample,
        }).then(({ body }) => {
          fixedAssetAcquisition = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fixed-asset-acquisitions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fixedAssetAcquisition],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fixedAssetAcquisitionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FixedAssetAcquisition page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fixedAssetAcquisition');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fixedAssetAcquisitionPageUrlPattern);
      });

      it('edit button click should load edit FixedAssetAcquisition page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FixedAssetAcquisition');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fixedAssetAcquisitionPageUrlPattern);
      });

      it('last delete button click should delete instance of FixedAssetAcquisition', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fixedAssetAcquisition').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fixedAssetAcquisitionPageUrlPattern);

        fixedAssetAcquisition = undefined;
      });
    });
  });

  describe('new FixedAssetAcquisition page', () => {
    beforeEach(() => {
      cy.visit(`${fixedAssetAcquisitionPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FixedAssetAcquisition');
    });

    it('should create an instance of FixedAssetAcquisition', () => {
      cy.get(`[data-cy="assetNumber"]`).type('12452').should('have.value', '12452');

      cy.get(`[data-cy="serviceOutletCode"]`).type('Jamaican').should('have.value', 'Jamaican');

      cy.get(`[data-cy="assetTag"]`).type('Pre-emptive rich Avon').should('have.value', 'Pre-emptive rich Avon');

      cy.get(`[data-cy="assetDescription"]`).type('Health').should('have.value', 'Health');

      cy.get(`[data-cy="purchaseDate"]`).type('2021-03-22').should('have.value', '2021-03-22');

      cy.get(`[data-cy="assetCategory"]`).type('Computer RAM').should('have.value', 'Computer RAM');

      cy.get(`[data-cy="purchasePrice"]`).type('75521').should('have.value', '75521');

      cy.get(`[data-cy="fileUploadToken"]`).type('bypassing').should('have.value', 'bypassing');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fixedAssetAcquisition = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fixedAssetAcquisitionPageUrlPattern);
    });
  });
});
