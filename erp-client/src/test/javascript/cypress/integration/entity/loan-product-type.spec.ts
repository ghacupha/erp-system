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

describe('LoanProductType e2e test', () => {
  const loanProductTypePageUrl = '/loan-product-type';
  const loanProductTypePageUrlPattern = new RegExp('/loan-product-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const loanProductTypeSample = { productCode: 'Concrete', productType: 'reboot value-added Hawaii' };

  let loanProductType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/loan-product-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/loan-product-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/loan-product-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (loanProductType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/loan-product-types/${loanProductType.id}`,
      }).then(() => {
        loanProductType = undefined;
      });
    }
  });

  it('LoanProductTypes menu should load LoanProductTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('loan-product-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LoanProductType').should('exist');
    cy.url().should('match', loanProductTypePageUrlPattern);
  });

  describe('LoanProductType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(loanProductTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LoanProductType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/loan-product-type/new$'));
        cy.getEntityCreateUpdateHeading('LoanProductType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanProductTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/loan-product-types',
          body: loanProductTypeSample,
        }).then(({ body }) => {
          loanProductType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/loan-product-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [loanProductType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(loanProductTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LoanProductType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('loanProductType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanProductTypePageUrlPattern);
      });

      it('edit button click should load edit LoanProductType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LoanProductType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanProductTypePageUrlPattern);
      });

      it('last delete button click should delete instance of LoanProductType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('loanProductType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', loanProductTypePageUrlPattern);

        loanProductType = undefined;
      });
    });
  });

  describe('new LoanProductType page', () => {
    beforeEach(() => {
      cy.visit(`${loanProductTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LoanProductType');
    });

    it('should create an instance of LoanProductType', () => {
      cy.get(`[data-cy="productCode"]`).type('Shoes Greece back-end').should('have.value', 'Shoes Greece back-end');

      cy.get(`[data-cy="productType"]`).type('optical deploy').should('have.value', 'optical deploy');

      cy.get(`[data-cy="productTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        loanProductType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', loanProductTypePageUrlPattern);
    });
  });
});
