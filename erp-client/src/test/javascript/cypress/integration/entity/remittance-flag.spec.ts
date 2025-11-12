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

describe('RemittanceFlag e2e test', () => {
  const remittanceFlagPageUrl = '/remittance-flag';
  const remittanceFlagPageUrlPattern = new RegExp('/remittance-flag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const remittanceFlagSample = { remittanceTypeFlag: 'RMTOUT', remittanceType: 'OUTFLOWS' };

  let remittanceFlag: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/remittance-flags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/remittance-flags').as('postEntityRequest');
    cy.intercept('DELETE', '/api/remittance-flags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (remittanceFlag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/remittance-flags/${remittanceFlag.id}`,
      }).then(() => {
        remittanceFlag = undefined;
      });
    }
  });

  it('RemittanceFlags menu should load RemittanceFlags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('remittance-flag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RemittanceFlag').should('exist');
    cy.url().should('match', remittanceFlagPageUrlPattern);
  });

  describe('RemittanceFlag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(remittanceFlagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RemittanceFlag page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/remittance-flag/new$'));
        cy.getEntityCreateUpdateHeading('RemittanceFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', remittanceFlagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/remittance-flags',
          body: remittanceFlagSample,
        }).then(({ body }) => {
          remittanceFlag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/remittance-flags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [remittanceFlag],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(remittanceFlagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RemittanceFlag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('remittanceFlag');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', remittanceFlagPageUrlPattern);
      });

      it('edit button click should load edit RemittanceFlag page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RemittanceFlag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', remittanceFlagPageUrlPattern);
      });

      it('last delete button click should delete instance of RemittanceFlag', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('remittanceFlag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', remittanceFlagPageUrlPattern);

        remittanceFlag = undefined;
      });
    });
  });

  describe('new RemittanceFlag page', () => {
    beforeEach(() => {
      cy.visit(`${remittanceFlagPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RemittanceFlag');
    });

    it('should create an instance of RemittanceFlag', () => {
      cy.get(`[data-cy="remittanceTypeFlag"]`).select('RMTIN');

      cy.get(`[data-cy="remittanceType"]`).select('OUTFLOWS');

      cy.get(`[data-cy="remittanceTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        remittanceFlag = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', remittanceFlagPageUrlPattern);
    });
  });
});
