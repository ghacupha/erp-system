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

describe('CollateralType e2e test', () => {
  const collateralTypePageUrl = '/collateral-type';
  const collateralTypePageUrlPattern = new RegExp('/collateral-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const collateralTypeSample = { collateralTypeCode: 'Analyst Motorway', collateralType: 'Fantastic Global' };

  let collateralType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/collateral-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/collateral-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/collateral-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (collateralType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/collateral-types/${collateralType.id}`,
      }).then(() => {
        collateralType = undefined;
      });
    }
  });

  it('CollateralTypes menu should load CollateralTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('collateral-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CollateralType').should('exist');
    cy.url().should('match', collateralTypePageUrlPattern);
  });

  describe('CollateralType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(collateralTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CollateralType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/collateral-type/new$'));
        cy.getEntityCreateUpdateHeading('CollateralType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', collateralTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/collateral-types',
          body: collateralTypeSample,
        }).then(({ body }) => {
          collateralType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/collateral-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [collateralType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(collateralTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CollateralType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('collateralType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', collateralTypePageUrlPattern);
      });

      it('edit button click should load edit CollateralType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CollateralType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', collateralTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CollateralType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('collateralType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', collateralTypePageUrlPattern);

        collateralType = undefined;
      });
    });
  });

  describe('new CollateralType page', () => {
    beforeEach(() => {
      cy.visit(`${collateralTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CollateralType');
    });

    it('should create an instance of CollateralType', () => {
      cy.get(`[data-cy="collateralTypeCode"]`).type('virtual Music target').should('have.value', 'virtual Music target');

      cy.get(`[data-cy="collateralType"]`).type('AGP Louisiana').should('have.value', 'AGP Louisiana');

      cy.get(`[data-cy="collateralTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        collateralType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', collateralTypePageUrlPattern);
    });
  });
});
