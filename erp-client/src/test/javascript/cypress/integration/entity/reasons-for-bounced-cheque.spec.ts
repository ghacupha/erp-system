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

describe('ReasonsForBouncedCheque e2e test', () => {
  const reasonsForBouncedChequePageUrl = '/reasons-for-bounced-cheque';
  const reasonsForBouncedChequePageUrlPattern = new RegExp('/reasons-for-bounced-cheque(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const reasonsForBouncedChequeSample = { bouncedChequeReasonsTypeCode: 'supply-chains Kentucky Markets' };

  let reasonsForBouncedCheque: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/reasons-for-bounced-cheques+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/reasons-for-bounced-cheques').as('postEntityRequest');
    cy.intercept('DELETE', '/api/reasons-for-bounced-cheques/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (reasonsForBouncedCheque) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/reasons-for-bounced-cheques/${reasonsForBouncedCheque.id}`,
      }).then(() => {
        reasonsForBouncedCheque = undefined;
      });
    }
  });

  it('ReasonsForBouncedCheques menu should load ReasonsForBouncedCheques page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('reasons-for-bounced-cheque');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReasonsForBouncedCheque').should('exist');
    cy.url().should('match', reasonsForBouncedChequePageUrlPattern);
  });

  describe('ReasonsForBouncedCheque page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reasonsForBouncedChequePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReasonsForBouncedCheque page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/reasons-for-bounced-cheque/new$'));
        cy.getEntityCreateUpdateHeading('ReasonsForBouncedCheque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reasonsForBouncedChequePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/reasons-for-bounced-cheques',
          body: reasonsForBouncedChequeSample,
        }).then(({ body }) => {
          reasonsForBouncedCheque = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/reasons-for-bounced-cheques+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reasonsForBouncedCheque],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(reasonsForBouncedChequePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReasonsForBouncedCheque page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reasonsForBouncedCheque');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reasonsForBouncedChequePageUrlPattern);
      });

      it('edit button click should load edit ReasonsForBouncedCheque page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReasonsForBouncedCheque');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reasonsForBouncedChequePageUrlPattern);
      });

      it('last delete button click should delete instance of ReasonsForBouncedCheque', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reasonsForBouncedCheque').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reasonsForBouncedChequePageUrlPattern);

        reasonsForBouncedCheque = undefined;
      });
    });
  });

  describe('new ReasonsForBouncedCheque page', () => {
    beforeEach(() => {
      cy.visit(`${reasonsForBouncedChequePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ReasonsForBouncedCheque');
    });

    it('should create an instance of ReasonsForBouncedCheque', () => {
      cy.get(`[data-cy="bouncedChequeReasonsTypeCode"]`)
        .type('Nebraska port architectures')
        .should('have.value', 'Nebraska port architectures');

      cy.get(`[data-cy="bouncedChequeReasonsType"]`).type('sky').should('have.value', 'sky');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        reasonsForBouncedCheque = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', reasonsForBouncedChequePageUrlPattern);
    });
  });
});
