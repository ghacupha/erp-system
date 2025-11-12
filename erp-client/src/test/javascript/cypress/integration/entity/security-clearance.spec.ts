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

describe('SecurityClearance e2e test', () => {
  const securityClearancePageUrl = '/security-clearance';
  const securityClearancePageUrlPattern = new RegExp('/security-clearance(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const securityClearanceSample = { clearanceLevel: 'Infrastructure' };

  let securityClearance: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/security-clearances+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/security-clearances').as('postEntityRequest');
    cy.intercept('DELETE', '/api/security-clearances/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (securityClearance) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/security-clearances/${securityClearance.id}`,
      }).then(() => {
        securityClearance = undefined;
      });
    }
  });

  it('SecurityClearances menu should load SecurityClearances page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('security-clearance');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SecurityClearance').should('exist');
    cy.url().should('match', securityClearancePageUrlPattern);
  });

  describe('SecurityClearance page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(securityClearancePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SecurityClearance page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/security-clearance/new$'));
        cy.getEntityCreateUpdateHeading('SecurityClearance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityClearancePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/security-clearances',
          body: securityClearanceSample,
        }).then(({ body }) => {
          securityClearance = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/security-clearances+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [securityClearance],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(securityClearancePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SecurityClearance page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('securityClearance');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityClearancePageUrlPattern);
      });

      it('edit button click should load edit SecurityClearance page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SecurityClearance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityClearancePageUrlPattern);
      });

      it('last delete button click should delete instance of SecurityClearance', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('securityClearance').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityClearancePageUrlPattern);

        securityClearance = undefined;
      });
    });
  });

  describe('new SecurityClearance page', () => {
    beforeEach(() => {
      cy.visit(`${securityClearancePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SecurityClearance');
    });

    it('should create an instance of SecurityClearance', () => {
      cy.get(`[data-cy="clearanceLevel"]`).type('Internal').should('have.value', 'Internal');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        securityClearance = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', securityClearancePageUrlPattern);
    });
  });
});
