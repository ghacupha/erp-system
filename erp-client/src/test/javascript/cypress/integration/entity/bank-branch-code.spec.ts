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

describe('BankBranchCode e2e test', () => {
  const bankBranchCodePageUrl = '/bank-branch-code';
  const bankBranchCodePageUrlPattern = new RegExp('/bank-branch-code(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const bankBranchCodeSample = { bankName: 'Pizza Barthelemy vortals', branchCode: 'Guatemala impactful' };

  let bankBranchCode: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/bank-branch-codes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bank-branch-codes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bank-branch-codes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (bankBranchCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bank-branch-codes/${bankBranchCode.id}`,
      }).then(() => {
        bankBranchCode = undefined;
      });
    }
  });

  it('BankBranchCodes menu should load BankBranchCodes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bank-branch-code');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BankBranchCode').should('exist');
    cy.url().should('match', bankBranchCodePageUrlPattern);
  });

  describe('BankBranchCode page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bankBranchCodePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BankBranchCode page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/bank-branch-code/new$'));
        cy.getEntityCreateUpdateHeading('BankBranchCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bankBranchCodePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bank-branch-codes',
          body: bankBranchCodeSample,
        }).then(({ body }) => {
          bankBranchCode = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bank-branch-codes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [bankBranchCode],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(bankBranchCodePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BankBranchCode page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('bankBranchCode');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bankBranchCodePageUrlPattern);
      });

      it('edit button click should load edit BankBranchCode page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BankBranchCode');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bankBranchCodePageUrlPattern);
      });

      it('last delete button click should delete instance of BankBranchCode', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('bankBranchCode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', bankBranchCodePageUrlPattern);

        bankBranchCode = undefined;
      });
    });
  });

  describe('new BankBranchCode page', () => {
    beforeEach(() => {
      cy.visit(`${bankBranchCodePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('BankBranchCode');
    });

    it('should create an instance of BankBranchCode', () => {
      cy.get(`[data-cy="bankCode"]`).type('compressing reboot').should('have.value', 'compressing reboot');

      cy.get(`[data-cy="bankName"]`).type('capability Plastic').should('have.value', 'capability Plastic');

      cy.get(`[data-cy="branchCode"]`).type('Nicaragua Group').should('have.value', 'Nicaragua Group');

      cy.get(`[data-cy="branchName"]`).type('Wisconsin Account').should('have.value', 'Wisconsin Account');

      cy.get(`[data-cy="notes"]`).type('Dollar) Beauty Licensed').should('have.value', 'Dollar) Beauty Licensed');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        bankBranchCode = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', bankBranchCodePageUrlPattern);
    });
  });
});
