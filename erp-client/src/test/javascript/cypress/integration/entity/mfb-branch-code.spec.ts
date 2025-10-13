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

describe('MfbBranchCode e2e test', () => {
  const mfbBranchCodePageUrl = '/mfb-branch-code';
  const mfbBranchCodePageUrlPattern = new RegExp('/mfb-branch-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const mfbBranchCodeSample = {};

  let mfbBranchCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/mfb-branch-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/mfb-branch-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/mfb-branch-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (mfbBranchCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/mfb-branch-codes/${mfbBranchCode.id}`,
      }).then(() => {
        mfbBranchCode = undefined;
      });
    }
  });

  it('MfbBranchCodes menu should load MfbBranchCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('mfb-branch-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MfbBranchCode').should('exist');
    cy.url().should('match', mfbBranchCodePageUrlPattern);
  });

  describe('MfbBranchCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(mfbBranchCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MfbBranchCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/mfb-branch-code/new$'));
        cy.getEntityCreateUpdateHeading('MfbBranchCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', mfbBranchCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/mfb-branch-codes',
          body: mfbBranchCodeSample,
        }).then(({ body }) => {
          mfbBranchCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/mfb-branch-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [mfbBranchCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(mfbBranchCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MfbBranchCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('mfbBranchCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', mfbBranchCodePageUrlPattern);
      });

      it('edit button click should load edit MfbBranchCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MfbBranchCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', mfbBranchCodePageUrlPattern);
      });

      it('last delete button click should delete instance of MfbBranchCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('mfbBranchCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', mfbBranchCodePageUrlPattern);

        mfbBranchCode = undefined;
      });
    });
  });

  describe('new MfbBranchCode page', () => {
    beforeEach(() => {
      cy.visit(`${mfbBranchCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('MfbBranchCode');
    });

    it('should create an instance of MfbBranchCode', () => {
      cy.get(`[data-cy="bankCode"]`).type('Generic').should('have.value', 'Generic');

      cy.get(`[data-cy="bankName"]`).type('Plastic').should('have.value', 'Plastic');

      cy.get(`[data-cy="branchCode"]`).type('deposit').should('have.value', 'deposit');

      cy.get(`[data-cy="branchName"]`).type('Rubber').should('have.value', 'Rubber');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        mfbBranchCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', mfbBranchCodePageUrlPattern);
    });
  });
});
