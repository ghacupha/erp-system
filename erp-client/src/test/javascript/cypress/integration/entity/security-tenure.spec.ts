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

describe('SecurityTenure e2e test', () => {
  const securityTenurePageUrl = '/security-tenure';
  const securityTenurePageUrlPattern = new RegExp('/security-tenure(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const securityTenureSample = { securityTenureCode: 'Carolina', securityTenureType: 'Prairie PNG' };

  let securityTenure: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/security-tenures+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/security-tenures').as('postEntityRequest');
    cy.intercept('DELETE', '/api/security-tenures/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (securityTenure) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/security-tenures/${securityTenure.id}`,
      }).then(() => {
        securityTenure = undefined;
      });
    }
  });

  it('SecurityTenures menu should load SecurityTenures page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('security-tenure');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SecurityTenure').should('exist');
    cy.url().should('match', securityTenurePageUrlPattern);
  });

  describe('SecurityTenure page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(securityTenurePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SecurityTenure page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/security-tenure/new$'));
        cy.getEntityCreateUpdateHeading('SecurityTenure');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityTenurePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/security-tenures',
          body: securityTenureSample,
        }).then(({ body }) => {
          securityTenure = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/security-tenures+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [securityTenure],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(securityTenurePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SecurityTenure page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('securityTenure');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityTenurePageUrlPattern);
      });

      it('edit button click should load edit SecurityTenure page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SecurityTenure');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityTenurePageUrlPattern);
      });

      it('last delete button click should delete instance of SecurityTenure', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('securityTenure').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', securityTenurePageUrlPattern);

        securityTenure = undefined;
      });
    });
  });

  describe('new SecurityTenure page', () => {
    beforeEach(() => {
      cy.visit(`${securityTenurePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('SecurityTenure');
    });

    it('should create an instance of SecurityTenure', () => {
      cy.get(`[data-cy="securityTenureCode"]`).type('connecting Market Global').should('have.value', 'connecting Market Global');

      cy.get(`[data-cy="securityTenureType"]`).type('enterprise').should('have.value', 'enterprise');

      cy.get(`[data-cy="securityTenureDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        securityTenure = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', securityTenurePageUrlPattern);
    });
  });
});
