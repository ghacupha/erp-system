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

describe('IsoCurrencyCode e2e test', () => {
  const isoCurrencyCodePageUrl = '/iso-currency-code';
  const isoCurrencyCodePageUrlPattern = new RegExp('/iso-currency-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const isoCurrencyCodeSample = {
    alphabeticCode: 'District back California',
    numericCode: 'Car',
    minorUnit: 'grow Account',
    currency: 'invoice New Steel',
  };

  let isoCurrencyCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/iso-currency-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/iso-currency-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/iso-currency-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (isoCurrencyCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/iso-currency-codes/${isoCurrencyCode.id}`,
      }).then(() => {
        isoCurrencyCode = undefined;
      });
    }
  });

  it('IsoCurrencyCodes menu should load IsoCurrencyCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('iso-currency-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IsoCurrencyCode').should('exist');
    cy.url().should('match', isoCurrencyCodePageUrlPattern);
  });

  describe('IsoCurrencyCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(isoCurrencyCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IsoCurrencyCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/iso-currency-code/new$'));
        cy.getEntityCreateUpdateHeading('IsoCurrencyCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', isoCurrencyCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/iso-currency-codes',
          body: isoCurrencyCodeSample,
        }).then(({ body }) => {
          isoCurrencyCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/iso-currency-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [isoCurrencyCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(isoCurrencyCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IsoCurrencyCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('isoCurrencyCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', isoCurrencyCodePageUrlPattern);
      });

      it('edit button click should load edit IsoCurrencyCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IsoCurrencyCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', isoCurrencyCodePageUrlPattern);
      });

      it('last delete button click should delete instance of IsoCurrencyCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('isoCurrencyCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', isoCurrencyCodePageUrlPattern);

        isoCurrencyCode = undefined;
      });
    });
  });

  describe('new IsoCurrencyCode page', () => {
    beforeEach(() => {
      cy.visit(`${isoCurrencyCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('IsoCurrencyCode');
    });

    it('should create an instance of IsoCurrencyCode', () => {
      cy.get(`[data-cy="alphabeticCode"]`).type('Bedfordshire Legacy').should('have.value', 'Bedfordshire Legacy');

      cy.get(`[data-cy="numericCode"]`).type('transparent schemas Tools').should('have.value', 'transparent schemas Tools');

      cy.get(`[data-cy="minorUnit"]`).type('Granite Account').should('have.value', 'Granite Account');

      cy.get(`[data-cy="currency"]`).type('Pizza Bangladesh').should('have.value', 'Pizza Bangladesh');

      cy.get(`[data-cy="country"]`).type('Monaco').should('have.value', 'Monaco');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        isoCurrencyCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', isoCurrencyCodePageUrlPattern);
    });
  });
});
