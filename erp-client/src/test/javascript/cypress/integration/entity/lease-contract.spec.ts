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

describe('LeaseContract e2e test', () => {
  const leaseContractPageUrl = '/lease-contract';
  const leaseContractPageUrlPattern = new RegExp('/lease-contract(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseContractSample = {
    bookingId: 'Dinar',
    leaseTitle: 'green SDR',
    identifier: '6d30b56c-7c0a-469f-ba89-55580d4d63ab',
    commencementDate: '2023-01-09',
    terminalDate: '2023-01-09',
  };

  let leaseContract: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-contracts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-contracts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-contracts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (leaseContract) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-contracts/${leaseContract.id}`,
      }).then(() => {
        leaseContract = undefined;
      });
    }
  });

  it('LeaseContracts menu should load LeaseContracts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-contract');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseContract').should('exist');
    cy.url().should('match', leaseContractPageUrlPattern);
  });

  describe('LeaseContract page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseContractPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseContract page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-contract/new$'));
        cy.getEntityCreateUpdateHeading('LeaseContract');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseContractPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-contracts',
          body: leaseContractSample,
        }).then(({ body }) => {
          leaseContract = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-contracts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseContract],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseContractPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LeaseContract page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseContract');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseContractPageUrlPattern);
      });

      it('edit button click should load edit LeaseContract page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseContract');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseContractPageUrlPattern);
      });

      it('last delete button click should delete instance of LeaseContract', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseContract').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseContractPageUrlPattern);

        leaseContract = undefined;
      });
    });
  });

  describe('new LeaseContract page', () => {
    beforeEach(() => {
      cy.visit(`${leaseContractPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseContract');
    });

    it('should create an instance of LeaseContract', () => {
      cy.get(`[data-cy="bookingId"]`).type('Lempira Investment').should('have.value', 'Lempira Investment');

      cy.get(`[data-cy="leaseTitle"]`).type('visionary Reunion paradigm').should('have.value', 'visionary Reunion paradigm');

      cy.get(`[data-cy="identifier"]`)
        .type('1f5a9efd-7f80-4b7b-b504-51f707726302')
        .invoke('val')
        .should('match', new RegExp('1f5a9efd-7f80-4b7b-b504-51f707726302'));

      cy.get(`[data-cy="description"]`).type('Sleek XML primary').should('have.value', 'Sleek XML primary');

      cy.get(`[data-cy="commencementDate"]`).type('2023-01-09').should('have.value', '2023-01-09');

      cy.get(`[data-cy="terminalDate"]`).type('2023-01-09').should('have.value', '2023-01-09');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseContract = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseContractPageUrlPattern);
    });
  });
});
