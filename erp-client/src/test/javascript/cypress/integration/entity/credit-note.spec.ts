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

describe('CreditNote e2e test', () => {
  const creditNotePageUrl = '/credit-note';
  const creditNotePageUrlPattern = new RegExp('/credit-note(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const creditNoteSample = { creditNumber: 'Steel Dram Strategist', creditNoteDate: '2022-03-19', creditAmount: 18946 };

  let creditNote: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/credit-notes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/credit-notes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/credit-notes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (creditNote) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/credit-notes/${creditNote.id}`,
      }).then(() => {
        creditNote = undefined;
      });
    }
  });

  it('CreditNotes menu should load CreditNotes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('credit-note');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CreditNote').should('exist');
    cy.url().should('match', creditNotePageUrlPattern);
  });

  describe('CreditNote page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(creditNotePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CreditNote page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/credit-note/new$'));
        cy.getEntityCreateUpdateHeading('CreditNote');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', creditNotePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/credit-notes',
          body: creditNoteSample,
        }).then(({ body }) => {
          creditNote = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/credit-notes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [creditNote],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(creditNotePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CreditNote page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('creditNote');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', creditNotePageUrlPattern);
      });

      it('edit button click should load edit CreditNote page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CreditNote');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', creditNotePageUrlPattern);
      });

      it('last delete button click should delete instance of CreditNote', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('creditNote').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', creditNotePageUrlPattern);

        creditNote = undefined;
      });
    });
  });

  describe('new CreditNote page', () => {
    beforeEach(() => {
      cy.visit(`${creditNotePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('CreditNote');
    });

    it('should create an instance of CreditNote', () => {
      cy.get(`[data-cy="creditNumber"]`).type('bandwidth platforms Rial').should('have.value', 'bandwidth platforms Rial');

      cy.get(`[data-cy="creditNoteDate"]`).type('2022-03-20').should('have.value', '2022-03-20');

      cy.get(`[data-cy="creditAmount"]`).type('97734').should('have.value', '97734');

      cy.get(`[data-cy="remarks"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        creditNote = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', creditNotePageUrlPattern);
    });
  });
});
