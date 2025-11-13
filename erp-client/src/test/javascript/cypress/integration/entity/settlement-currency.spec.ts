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

describe('SettlementCurrency e2e test', () => {
  const settlementCurrencyPageUrl = '/settlement-currency';
  const settlementCurrencyPageUrlPattern = new RegExp('/settlement-currency(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const settlementCurrencySample = {
    iso4217CurrencyCode: 'Ele',
    currencyName: 'European Unit of Account 17(E.U.A.-17)',
    country: 'Hungary',
  };

  let settlementCurrency: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/settlement-currencies+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/settlement-currencies').as('postEntityRequest');
    cy.intercept('DELETE', '/api/settlement-currencies/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (settlementCurrency) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/settlement-currencies/${settlementCurrency.id}`,
      }).then(() => {
        settlementCurrency = undefined;
      });
    }
  });

  it('SettlementCurrencies menu should load SettlementCurrencies page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('settlement-currency');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SettlementCurrency').should('exist');
    cy.url().should('match', settlementCurrencyPageUrlPattern);
  });

  describe('SettlementCurrency page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(settlementCurrencyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SettlementCurrency page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/settlement-currency/new$'));
        cy.getEntityCreateUpdateHeading('SettlementCurrency');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', settlementCurrencyPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/settlement-currencies',
          body: settlementCurrencySample,
        }).then(({ body }) => {
          settlementCurrency = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/settlement-currencies+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [settlementCurrency],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(settlementCurrencyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SettlementCurrency page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('settlementCurrency');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', settlementCurrencyPageUrlPattern);
      });

      it('edit button click should load edit SettlementCurrency page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SettlementCurrency');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', settlementCurrencyPageUrlPattern);
      });

      it('last delete button click should delete instance of SettlementCurrency', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('settlementCurrency').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', settlementCurrencyPageUrlPattern);

        settlementCurrency = undefined;
      });
    });
  });

  describe('new SettlementCurrency page', () => {
    beforeEach(() => {
      cy.visit(`${settlementCurrencyPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SettlementCurrency');
    });

    it('should create an instance of SettlementCurrency', () => {
      cy.get(`[data-cy="iso4217CurrencyCode"]`).type('wit').should('have.value', 'wit');

      cy.get(`[data-cy="currencyName"]`).type('Rufiyaa').should('have.value', 'Rufiyaa');

      cy.get(`[data-cy="country"]`).type('Liechtenstein').should('have.value', 'Liechtenstein');

      cy.get(`[data-cy="numericCode"]`).type('Dynamic Terrace').should('have.value', 'Dynamic Terrace');

      cy.get(`[data-cy="minorUnit"]`).type('Marketing Soap Awesome').should('have.value', 'Marketing Soap Awesome');

      cy.get(`[data-cy="fileUploadToken"]`).type('maroon Senior').should('have.value', 'maroon Senior');

      cy.get(`[data-cy="compilationToken"]`).type('Pants program optimal').should('have.value', 'Pants program optimal');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        settlementCurrency = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', settlementCurrencyPageUrlPattern);
    });
  });
});
