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

describe('Dealer e2e test', () => {
  const dealerPageUrl = '/dealer';
  const dealerPageUrlPattern = new RegExp('/dealer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const dealerSample = { dealerName: 'Tuna Kroon' };

  let dealer: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/dealers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/dealers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/dealers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dealer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dealers/${dealer.id}`,
      }).then(() => {
        dealer = undefined;
      });
    }
  });

  it('Dealers menu should load Dealers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('dealer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Dealer').should('exist');
    cy.url().should('match', dealerPageUrlPattern);
  });

  describe('Dealer page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dealerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Dealer page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/dealer/new$'));
        cy.getEntityCreateUpdateHeading('Dealer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dealerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/dealers',
          body: dealerSample,
        }).then(({ body }) => {
          dealer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/dealers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [dealer],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(dealerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Dealer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dealer');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dealerPageUrlPattern);
      });

      it('edit button click should load edit Dealer page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Dealer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dealerPageUrlPattern);
      });

      it('last delete button click should delete instance of Dealer', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('dealer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', dealerPageUrlPattern);

        dealer = undefined;
      });
    });
  });

  describe('new Dealer page', () => {
    beforeEach(() => {
      cy.visit(`${dealerPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('Dealer');
    });

    it('should create an instance of Dealer', () => {
      cy.get(`[data-cy="dealerName"]`).type('white copy').should('have.value', 'white copy');

      cy.get(`[data-cy="taxNumber"]`).type('circuit').should('have.value', 'circuit');

      cy.get(`[data-cy="identificationDocumentNumber"]`).type('Granite Account').should('have.value', 'Granite Account');

      cy.get(`[data-cy="organizationName"]`).type('customized innovate').should('have.value', 'customized innovate');

      cy.get(`[data-cy="department"]`).type('value-added Orchestrator matrix').should('have.value', 'value-added Orchestrator matrix');

      cy.get(`[data-cy="position"]`).type('Garden').should('have.value', 'Garden');

      cy.get(`[data-cy="postalAddress"]`).type('content Gorgeous').should('have.value', 'content Gorgeous');

      cy.get(`[data-cy="physicalAddress"]`).type('Executive').should('have.value', 'Executive');

      cy.get(`[data-cy="accountName"]`).type('Personal Loan Account').should('have.value', 'Personal Loan Account');

      cy.get(`[data-cy="accountNumber"]`).type('Customer').should('have.value', 'Customer');

      cy.get(`[data-cy="bankersName"]`).type('Frozen').should('have.value', 'Frozen');

      cy.get(`[data-cy="bankersBranch"]`).type('blue Lakes Analyst').should('have.value', 'blue Lakes Analyst');

      cy.get(`[data-cy="bankersSwiftCode"]`).type('mobile Grocery').should('have.value', 'mobile Grocery');

      cy.get(`[data-cy="fileUploadToken"]`).type('Practical Argentina Berkshire').should('have.value', 'Practical Argentina Berkshire');

      cy.get(`[data-cy="compilationToken"]`).type('Illinois').should('have.value', 'Illinois');

      cy.get(`[data-cy="remarks"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="otherNames"]`).type('productize reboot').should('have.value', 'productize reboot');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        dealer = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', dealerPageUrlPattern);
    });
  });
});
