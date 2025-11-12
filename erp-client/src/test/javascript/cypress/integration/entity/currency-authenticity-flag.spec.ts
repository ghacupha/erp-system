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

describe('CurrencyAuthenticityFlag e2e test', () => {
  const currencyAuthenticityFlagPageUrl = '/currency-authenticity-flag';
  const currencyAuthenticityFlagPageUrlPattern = new RegExp('/currency-authenticity-flag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const currencyAuthenticityFlagSample = { currencyAuthenticityFlag: 'Y', currencyAuthenticityType: 'GENUINE' };

  let currencyAuthenticityFlag: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/currency-authenticity-flags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/currency-authenticity-flags').as('postEntityRequest');
    cy.intercept('DELETE', '/api/currency-authenticity-flags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (currencyAuthenticityFlag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/currency-authenticity-flags/${currencyAuthenticityFlag.id}`,
      }).then(() => {
        currencyAuthenticityFlag = undefined;
      });
    }
  });

  it('CurrencyAuthenticityFlags menu should load CurrencyAuthenticityFlags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('currency-authenticity-flag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CurrencyAuthenticityFlag').should('exist');
    cy.url().should('match', currencyAuthenticityFlagPageUrlPattern);
  });

  describe('CurrencyAuthenticityFlag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(currencyAuthenticityFlagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CurrencyAuthenticityFlag page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/currency-authenticity-flag/new$'));
        cy.getEntityCreateUpdateHeading('CurrencyAuthenticityFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', currencyAuthenticityFlagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/currency-authenticity-flags',
          body: currencyAuthenticityFlagSample,
        }).then(({ body }) => {
          currencyAuthenticityFlag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/currency-authenticity-flags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [currencyAuthenticityFlag],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(currencyAuthenticityFlagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CurrencyAuthenticityFlag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('currencyAuthenticityFlag');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', currencyAuthenticityFlagPageUrlPattern);
      });

      it('edit button click should load edit CurrencyAuthenticityFlag page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CurrencyAuthenticityFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', currencyAuthenticityFlagPageUrlPattern);
      });

      it('last delete button click should delete instance of CurrencyAuthenticityFlag', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('currencyAuthenticityFlag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', currencyAuthenticityFlagPageUrlPattern);

        currencyAuthenticityFlag = undefined;
      });
    });
  });

  describe('new CurrencyAuthenticityFlag page', () => {
    beforeEach(() => {
      cy.visit(`${currencyAuthenticityFlagPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CurrencyAuthenticityFlag');
    });

    it('should create an instance of CurrencyAuthenticityFlag', () => {
      cy.get(`[data-cy="currencyAuthenticityFlag"]`).select('N');

      cy.get(`[data-cy="currencyAuthenticityType"]`).select('GENUINE');

      cy.get(`[data-cy="currencyAuthenticityTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        currencyAuthenticityFlag = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', currencyAuthenticityFlagPageUrlPattern);
    });
  });
});
