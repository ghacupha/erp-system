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

describe('FinancialDerivativeTypeCode e2e test', () => {
  const financialDerivativeTypeCodePageUrl = '/financial-derivative-type-code';
  const financialDerivativeTypeCodePageUrlPattern = new RegExp('/financial-derivative-type-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const financialDerivativeTypeCodeSample = { financialDerivativeTypeCode: 'Avon parse haptic', financialDerivativeType: 'transmitting' };

  let financialDerivativeTypeCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/financial-derivative-type-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/financial-derivative-type-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/financial-derivative-type-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (financialDerivativeTypeCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/financial-derivative-type-codes/${financialDerivativeTypeCode.id}`,
      }).then(() => {
        financialDerivativeTypeCode = undefined;
      });
    }
  });

  it('FinancialDerivativeTypeCodes menu should load FinancialDerivativeTypeCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('financial-derivative-type-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FinancialDerivativeTypeCode').should('exist');
    cy.url().should('match', financialDerivativeTypeCodePageUrlPattern);
  });

  describe('FinancialDerivativeTypeCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(financialDerivativeTypeCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FinancialDerivativeTypeCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/financial-derivative-type-code/new$'));
        cy.getEntityCreateUpdateHeading('FinancialDerivativeTypeCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', financialDerivativeTypeCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/financial-derivative-type-codes',
          body: financialDerivativeTypeCodeSample,
        }).then(({ body }) => {
          financialDerivativeTypeCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/financial-derivative-type-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [financialDerivativeTypeCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(financialDerivativeTypeCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FinancialDerivativeTypeCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('financialDerivativeTypeCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', financialDerivativeTypeCodePageUrlPattern);
      });

      it('edit button click should load edit FinancialDerivativeTypeCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FinancialDerivativeTypeCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', financialDerivativeTypeCodePageUrlPattern);
      });

      it('last delete button click should delete instance of FinancialDerivativeTypeCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('financialDerivativeTypeCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', financialDerivativeTypeCodePageUrlPattern);

        financialDerivativeTypeCode = undefined;
      });
    });
  });

  describe('new FinancialDerivativeTypeCode page', () => {
    beforeEach(() => {
      cy.visit(`${financialDerivativeTypeCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('FinancialDerivativeTypeCode');
    });

    it('should create an instance of FinancialDerivativeTypeCode', () => {
      cy.get(`[data-cy="financialDerivativeTypeCode"]`).type('Alaska 24/365').should('have.value', 'Alaska 24/365');

      cy.get(`[data-cy="financialDerivativeType"]`).type('Mississippi product silver').should('have.value', 'Mississippi product silver');

      cy.get(`[data-cy="financialDerivativeTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        financialDerivativeTypeCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', financialDerivativeTypeCodePageUrlPattern);
    });
  });
});
