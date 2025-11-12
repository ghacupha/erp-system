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

describe('CrbProductServiceFeeType e2e test', () => {
  const crbProductServiceFeeTypePageUrl = '/crb-product-service-fee-type';
  const crbProductServiceFeeTypePageUrlPattern = new RegExp('/crb-product-service-fee-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const crbProductServiceFeeTypeSample = { chargeTypeCode: 'out-of-the-box invoice', chargeTypeCategory: 'Inverse dedicated interface' };

  let crbProductServiceFeeType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crb-product-service-fee-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crb-product-service-fee-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crb-product-service-fee-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crbProductServiceFeeType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crb-product-service-fee-types/${crbProductServiceFeeType.id}`,
      }).then(() => {
        crbProductServiceFeeType = undefined;
      });
    }
  });

  it('CrbProductServiceFeeTypes menu should load CrbProductServiceFeeTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crb-product-service-fee-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CrbProductServiceFeeType').should('exist');
    cy.url().should('match', crbProductServiceFeeTypePageUrlPattern);
  });

  describe('CrbProductServiceFeeType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(crbProductServiceFeeTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CrbProductServiceFeeType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/crb-product-service-fee-type/new$'));
        cy.getEntityCreateUpdateHeading('CrbProductServiceFeeType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbProductServiceFeeTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crb-product-service-fee-types',
          body: crbProductServiceFeeTypeSample,
        }).then(({ body }) => {
          crbProductServiceFeeType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crb-product-service-fee-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crbProductServiceFeeType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(crbProductServiceFeeTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CrbProductServiceFeeType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crbProductServiceFeeType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbProductServiceFeeTypePageUrlPattern);
      });

      it('edit button click should load edit CrbProductServiceFeeType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CrbProductServiceFeeType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbProductServiceFeeTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CrbProductServiceFeeType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('crbProductServiceFeeType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', crbProductServiceFeeTypePageUrlPattern);

        crbProductServiceFeeType = undefined;
      });
    });
  });

  describe('new CrbProductServiceFeeType page', () => {
    beforeEach(() => {
      cy.visit(`${crbProductServiceFeeTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CrbProductServiceFeeType');
    });

    it('should create an instance of CrbProductServiceFeeType', () => {
      cy.get(`[data-cy="chargeTypeCode"]`).type('Direct USB').should('have.value', 'Direct USB');

      cy.get(`[data-cy="chargeTypeDescription"]`).type('cyan implementation').should('have.value', 'cyan implementation');

      cy.get(`[data-cy="chargeTypeCategory"]`).type('bleeding-edge').should('have.value', 'bleeding-edge');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        crbProductServiceFeeType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', crbProductServiceFeeTypePageUrlPattern);
    });
  });
});
