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

describe('TerminalFunctions e2e test', () => {
  const terminalFunctionsPageUrl = '/terminal-functions';
  const terminalFunctionsPageUrlPattern = new RegExp('/terminal-functions(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const terminalFunctionsSample = { functionCode: 'logistical Rest', terminalFunctionality: 'Music' };

  let terminalFunctions: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/terminal-functions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/terminal-functions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/terminal-functions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (terminalFunctions) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/terminal-functions/${terminalFunctions.id}`,
      }).then(() => {
        terminalFunctions = undefined;
      });
    }
  });

  it('TerminalFunctions menu should load TerminalFunctions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('terminal-functions');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TerminalFunctions').should('exist');
    cy.url().should('match', terminalFunctionsPageUrlPattern);
  });

  describe('TerminalFunctions page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(terminalFunctionsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TerminalFunctions page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/terminal-functions/new$'));
        cy.getEntityCreateUpdateHeading('TerminalFunctions');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', terminalFunctionsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/terminal-functions',
          body: terminalFunctionsSample,
        }).then(({ body }) => {
          terminalFunctions = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/terminal-functions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [terminalFunctions],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(terminalFunctionsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TerminalFunctions page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('terminalFunctions');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', terminalFunctionsPageUrlPattern);
      });

      it('edit button click should load edit TerminalFunctions page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TerminalFunctions');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', terminalFunctionsPageUrlPattern);
      });

      it('last delete button click should delete instance of TerminalFunctions', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('terminalFunctions').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', terminalFunctionsPageUrlPattern);

        terminalFunctions = undefined;
      });
    });
  });

  describe('new TerminalFunctions page', () => {
    beforeEach(() => {
      cy.visit(`${terminalFunctionsPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('TerminalFunctions');
    });

    it('should create an instance of TerminalFunctions', () => {
      cy.get(`[data-cy="functionCode"]`).type('Rubber repurpose').should('have.value', 'Rubber repurpose');

      cy.get(`[data-cy="terminalFunctionality"]`).type('Intelligent parsing').should('have.value', 'Intelligent parsing');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        terminalFunctions = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', terminalFunctionsPageUrlPattern);
    });
  });
});
