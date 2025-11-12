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

describe('ProductType e2e test', () => {
  const productTypePageUrl = '/product-type';
  const productTypePageUrlPattern = new RegExp('/product-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const productTypeSample = { productCode: 'toolset' };

  let productType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-types/${productType.id}`,
      }).then(() => {
        productType = undefined;
      });
    }
  });

  it('ProductTypes menu should load ProductTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductType').should('exist');
    cy.url().should('match', productTypePageUrlPattern);
  });

  describe('ProductType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/product-type/new$'));
        cy.getEntityCreateUpdateHeading('ProductType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', productTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-types',
          body: productTypeSample,
        }).then(({ body }) => {
          productType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(productTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', productTypePageUrlPattern);
      });

      it('edit button click should load edit ProductType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', productTypePageUrlPattern);
      });

      it('last delete button click should delete instance of ProductType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', productTypePageUrlPattern);

        productType = undefined;
      });
    });
  });

  describe('new ProductType page', () => {
    beforeEach(() => {
      cy.visit(`${productTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ProductType');
    });

    it('should create an instance of ProductType', () => {
      cy.get(`[data-cy="productCode"]`).type('Gloves').should('have.value', 'Gloves');

      cy.get(`[data-cy="productType"]`).type('Gardens').should('have.value', 'Gardens');

      cy.get(`[data-cy="productTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        productType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', productTypePageUrlPattern);
    });
  });
});
