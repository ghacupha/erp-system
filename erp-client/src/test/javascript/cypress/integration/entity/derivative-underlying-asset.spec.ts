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

describe('DerivativeUnderlyingAsset e2e test', () => {
  const derivativeUnderlyingAssetPageUrl = '/derivative-underlying-asset';
  const derivativeUnderlyingAssetPageUrlPattern = new RegExp('/derivative-underlying-asset(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const derivativeUnderlyingAssetSample = {
    derivativeUnderlyingAssetTypeCode: 'Rustic projection deposit',
    financialDerivativeUnderlyingAssetType: 'Avon',
  };

  let derivativeUnderlyingAsset: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/derivative-underlying-assets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/derivative-underlying-assets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/derivative-underlying-assets/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (derivativeUnderlyingAsset) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/derivative-underlying-assets/${derivativeUnderlyingAsset.id}`,
      }).then(() => {
        derivativeUnderlyingAsset = undefined;
      });
    }
  });

  it('DerivativeUnderlyingAssets menu should load DerivativeUnderlyingAssets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('derivative-underlying-asset');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DerivativeUnderlyingAsset').should('exist');
    cy.url().should('match', derivativeUnderlyingAssetPageUrlPattern);
  });

  describe('DerivativeUnderlyingAsset page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(derivativeUnderlyingAssetPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DerivativeUnderlyingAsset page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/derivative-underlying-asset/new$'));
        cy.getEntityCreateUpdateHeading('DerivativeUnderlyingAsset');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', derivativeUnderlyingAssetPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/derivative-underlying-assets',
          body: derivativeUnderlyingAssetSample,
        }).then(({ body }) => {
          derivativeUnderlyingAsset = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/derivative-underlying-assets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [derivativeUnderlyingAsset],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(derivativeUnderlyingAssetPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DerivativeUnderlyingAsset page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('derivativeUnderlyingAsset');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', derivativeUnderlyingAssetPageUrlPattern);
      });

      it('edit button click should load edit DerivativeUnderlyingAsset page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DerivativeUnderlyingAsset');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', derivativeUnderlyingAssetPageUrlPattern);
      });

      it('last delete button click should delete instance of DerivativeUnderlyingAsset', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('derivativeUnderlyingAsset').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', derivativeUnderlyingAssetPageUrlPattern);

        derivativeUnderlyingAsset = undefined;
      });
    });
  });

  describe('new DerivativeUnderlyingAsset page', () => {
    beforeEach(() => {
      cy.visit(`${derivativeUnderlyingAssetPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('DerivativeUnderlyingAsset');
    });

    it('should create an instance of DerivativeUnderlyingAsset', () => {
      cy.get(`[data-cy="derivativeUnderlyingAssetTypeCode"]`).type('Strategist').should('have.value', 'Strategist');

      cy.get(`[data-cy="financialDerivativeUnderlyingAssetType"]`).type('Tuna generate').should('have.value', 'Tuna generate');

      cy.get(`[data-cy="derivativeUnderlyingAssetTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        derivativeUnderlyingAsset = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', derivativeUnderlyingAssetPageUrlPattern);
    });
  });
});
