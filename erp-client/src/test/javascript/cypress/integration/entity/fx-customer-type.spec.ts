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

describe('FxCustomerType e2e test', () => {
  const fxCustomerTypePageUrl = '/fx-customer-type';
  const fxCustomerTypePageUrlPattern = new RegExp('/fx-customer-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const fxCustomerTypeSample = { foreignExchangeCustomerTypeCode: 'Regional back-end', foreignCustomerType: 'and Managed Persevering' };

  let fxCustomerType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fx-customer-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fx-customer-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fx-customer-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fxCustomerType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fx-customer-types/${fxCustomerType.id}`,
      }).then(() => {
        fxCustomerType = undefined;
      });
    }
  });

  it('FxCustomerTypes menu should load FxCustomerTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fx-customer-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FxCustomerType').should('exist');
    cy.url().should('match', fxCustomerTypePageUrlPattern);
  });

  describe('FxCustomerType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fxCustomerTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FxCustomerType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/fx-customer-type/new$'));
        cy.getEntityCreateUpdateHeading('FxCustomerType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxCustomerTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fx-customer-types',
          body: fxCustomerTypeSample,
        }).then(({ body }) => {
          fxCustomerType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fx-customer-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fxCustomerType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fxCustomerTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FxCustomerType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fxCustomerType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxCustomerTypePageUrlPattern);
      });

      it('edit button click should load edit FxCustomerType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FxCustomerType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxCustomerTypePageUrlPattern);
      });

      it('last delete button click should delete instance of FxCustomerType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fxCustomerType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', fxCustomerTypePageUrlPattern);

        fxCustomerType = undefined;
      });
    });
  });

  describe('new FxCustomerType page', () => {
    beforeEach(() => {
      cy.visit(`${fxCustomerTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FxCustomerType');
    });

    it('should create an instance of FxCustomerType', () => {
      cy.get(`[data-cy="foreignExchangeCustomerTypeCode"]`)
        .type('infrastructures Rhode Switchable')
        .should('have.value', 'infrastructures Rhode Switchable');

      cy.get(`[data-cy="foreignCustomerType"]`).type('Argentina purple bandwidth').should('have.value', 'Argentina purple bandwidth');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        fxCustomerType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', fxCustomerTypePageUrlPattern);
    });
  });
});
