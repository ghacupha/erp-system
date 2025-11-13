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

describe('CurrencyServiceabilityFlag e2e test', () => {
  const currencyServiceabilityFlagPageUrl = '/currency-serviceability-flag';
  const currencyServiceabilityFlagPageUrlPattern = new RegExp('/currency-serviceability-flag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const currencyServiceabilityFlagSample = { currencyServiceabilityFlag: 'Y', currencyServiceability: 'FIT' };

  let currencyServiceabilityFlag: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/currency-serviceability-flags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/currency-serviceability-flags').as('postEntityRequest');
    cy.intercept('DELETE', '/api/currency-serviceability-flags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (currencyServiceabilityFlag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/currency-serviceability-flags/${currencyServiceabilityFlag.id}`,
      }).then(() => {
        currencyServiceabilityFlag = undefined;
      });
    }
  });

  it('CurrencyServiceabilityFlags menu should load CurrencyServiceabilityFlags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('currency-serviceability-flag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CurrencyServiceabilityFlag').should('exist');
    cy.url().should('match', currencyServiceabilityFlagPageUrlPattern);
  });

  describe('CurrencyServiceabilityFlag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(currencyServiceabilityFlagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CurrencyServiceabilityFlag page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/currency-serviceability-flag/new$'));
        cy.getEntityCreateUpdateHeading('CurrencyServiceabilityFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', currencyServiceabilityFlagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/currency-serviceability-flags',
          body: currencyServiceabilityFlagSample,
        }).then(({ body }) => {
          currencyServiceabilityFlag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/currency-serviceability-flags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [currencyServiceabilityFlag],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(currencyServiceabilityFlagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CurrencyServiceabilityFlag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('currencyServiceabilityFlag');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', currencyServiceabilityFlagPageUrlPattern);
      });

      it('edit button click should load edit CurrencyServiceabilityFlag page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CurrencyServiceabilityFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', currencyServiceabilityFlagPageUrlPattern);
      });

      it('last delete button click should delete instance of CurrencyServiceabilityFlag', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('currencyServiceabilityFlag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', currencyServiceabilityFlagPageUrlPattern);

        currencyServiceabilityFlag = undefined;
      });
    });
  });

  describe('new CurrencyServiceabilityFlag page', () => {
    beforeEach(() => {
      cy.visit(`${currencyServiceabilityFlagPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CurrencyServiceabilityFlag');
    });

    it('should create an instance of CurrencyServiceabilityFlag', () => {
      cy.get(`[data-cy="currencyServiceabilityFlag"]`).select('Y');

      cy.get(`[data-cy="currencyServiceability"]`).select('UNFIT');

      cy.get(`[data-cy="currencyServiceabilityFlagDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        currencyServiceabilityFlag = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', currencyServiceabilityFlagPageUrlPattern);
    });
  });
});
