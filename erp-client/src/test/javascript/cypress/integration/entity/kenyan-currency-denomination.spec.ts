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

describe('KenyanCurrencyDenomination e2e test', () => {
  const kenyanCurrencyDenominationPageUrl = '/kenyan-currency-denomination';
  const kenyanCurrencyDenominationPageUrlPattern = new RegExp('/kenyan-currency-denomination(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const kenyanCurrencyDenominationSample = {
    currencyDenominationCode: 'compelling Wooden',
    currencyDenominationType: 'copy Accountability',
  };

  let kenyanCurrencyDenomination: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/kenyan-currency-denominations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/kenyan-currency-denominations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/kenyan-currency-denominations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (kenyanCurrencyDenomination) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/kenyan-currency-denominations/${kenyanCurrencyDenomination.id}`,
      }).then(() => {
        kenyanCurrencyDenomination = undefined;
      });
    }
  });

  it('KenyanCurrencyDenominations menu should load KenyanCurrencyDenominations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('kenyan-currency-denomination');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('KenyanCurrencyDenomination').should('exist');
    cy.url().should('match', kenyanCurrencyDenominationPageUrlPattern);
  });

  describe('KenyanCurrencyDenomination page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(kenyanCurrencyDenominationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create KenyanCurrencyDenomination page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/kenyan-currency-denomination/new$'));
        cy.getEntityCreateUpdateHeading('KenyanCurrencyDenomination');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', kenyanCurrencyDenominationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/kenyan-currency-denominations',
          body: kenyanCurrencyDenominationSample,
        }).then(({ body }) => {
          kenyanCurrencyDenomination = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/kenyan-currency-denominations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [kenyanCurrencyDenomination],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(kenyanCurrencyDenominationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details KenyanCurrencyDenomination page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('kenyanCurrencyDenomination');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', kenyanCurrencyDenominationPageUrlPattern);
      });

      it('edit button click should load edit KenyanCurrencyDenomination page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('KenyanCurrencyDenomination');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', kenyanCurrencyDenominationPageUrlPattern);
      });

      it('last delete button click should delete instance of KenyanCurrencyDenomination', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('kenyanCurrencyDenomination').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', kenyanCurrencyDenominationPageUrlPattern);

        kenyanCurrencyDenomination = undefined;
      });
    });
  });

  describe('new KenyanCurrencyDenomination page', () => {
    beforeEach(() => {
      cy.visit(`${kenyanCurrencyDenominationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('KenyanCurrencyDenomination');
    });

    it('should create an instance of KenyanCurrencyDenomination', () => {
      cy.get(`[data-cy="currencyDenominationCode"]`).type('Brand B2C').should('have.value', 'Brand B2C');

      cy.get(`[data-cy="currencyDenominationType"]`).type('Unbranded').should('have.value', 'Unbranded');

      cy.get(`[data-cy="currencyDenominationTypeDetails"]`).type('Pizza Wells').should('have.value', 'Pizza Wells');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        kenyanCurrencyDenomination = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', kenyanCurrencyDenominationPageUrlPattern);
    });
  });
});
