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

describe('ContractStatus e2e test', () => {
  const contractStatusPageUrl = '/contract-status';
  const contractStatusPageUrlPattern = new RegExp('/contract-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const contractStatusSample = { contractStatusCode: 'Associate composite', contractStatusType: 'Games Health' };

  let contractStatus: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/contract-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contract-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contract-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (contractStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contract-statuses/${contractStatus.id}`,
      }).then(() => {
        contractStatus = undefined;
      });
    }
  });

  it('ContractStatuses menu should load ContractStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contract-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContractStatus').should('exist');
    cy.url().should('match', contractStatusPageUrlPattern);
  });

  describe('ContractStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contractStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContractStatus page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/contract-status/new$'));
        cy.getEntityCreateUpdateHeading('ContractStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', contractStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contract-statuses',
          body: contractStatusSample,
        }).then(({ body }) => {
          contractStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contract-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [contractStatus],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(contractStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ContractStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contractStatus');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', contractStatusPageUrlPattern);
      });

      it('edit button click should load edit ContractStatus page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContractStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', contractStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of ContractStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contractStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', contractStatusPageUrlPattern);

        contractStatus = undefined;
      });
    });
  });

  describe('new ContractStatus page', () => {
    beforeEach(() => {
      cy.visit(`${contractStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ContractStatus');
    });

    it('should create an instance of ContractStatus', () => {
      cy.get(`[data-cy="contractStatusCode"]`).type('RSS').should('have.value', 'RSS');

      cy.get(`[data-cy="contractStatusType"]`).type('leading-edge Salad eyeballs').should('have.value', 'leading-edge Salad eyeballs');

      cy.get(`[data-cy="contractStatusTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        contractStatus = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', contractStatusPageUrlPattern);
    });
  });
});
