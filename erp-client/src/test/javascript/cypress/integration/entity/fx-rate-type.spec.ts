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

describe('FxRateType e2e test', () => {
  const fxRateTypePageUrl = '/fx-rate-type';
  const fxRateTypePageUrlPattern = new RegExp('/fx-rate-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fxRateTypeSample = { fxRateCode: 'Legacy Innovative Silver', fxRateType: 'FTP' };

  let fxRateType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fx-rate-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fx-rate-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fx-rate-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fxRateType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fx-rate-types/${fxRateType.id}`,
      }).then(() => {
        fxRateType = undefined;
      });
    }
  });

  it('FxRateTypes menu should load FxRateTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fx-rate-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FxRateType').should('exist');
    cy.url().should('match', fxRateTypePageUrlPattern);
  });

  describe('FxRateType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fxRateTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FxRateType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fx-rate-type/new$'));
        cy.getEntityCreateUpdateHeading('FxRateType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxRateTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fx-rate-types',
          body: fxRateTypeSample,
        }).then(({ body }) => {
          fxRateType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fx-rate-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fxRateType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fxRateTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FxRateType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fxRateType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxRateTypePageUrlPattern);
      });

      it('edit button click should load edit FxRateType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FxRateType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxRateTypePageUrlPattern);
      });

      it('last delete button click should delete instance of FxRateType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fxRateType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxRateTypePageUrlPattern);

        fxRateType = undefined;
      });
    });
  });

  describe('new FxRateType page', () => {
    beforeEach(() => {
      cy.visit(`${fxRateTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FxRateType');
    });

    it('should create an instance of FxRateType', () => {
      cy.get(`[data-cy="fxRateCode"]`).type('Albania Dong').should('have.value', 'Albania Dong');

      cy.get(`[data-cy="fxRateType"]`)
        .type('Functionality strategic leading-edge')
        .should('have.value', 'Functionality strategic leading-edge');

      cy.get(`[data-cy="fxRateDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fxRateType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fxRateTypePageUrlPattern);
    });
  });
});
