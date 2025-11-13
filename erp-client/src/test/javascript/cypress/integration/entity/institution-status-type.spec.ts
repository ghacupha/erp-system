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

describe('InstitutionStatusType e2e test', () => {
  const institutionStatusTypePageUrl = '/institution-status-type';
  const institutionStatusTypePageUrlPattern = new RegExp('/institution-status-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const institutionStatusTypeSample = { institutionStatusCode: 'Brunei Producer exploit' };

  let institutionStatusType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/institution-status-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/institution-status-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/institution-status-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (institutionStatusType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/institution-status-types/${institutionStatusType.id}`,
      }).then(() => {
        institutionStatusType = undefined;
      });
    }
  });

  it('InstitutionStatusTypes menu should load InstitutionStatusTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('institution-status-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InstitutionStatusType').should('exist');
    cy.url().should('match', institutionStatusTypePageUrlPattern);
  });

  describe('InstitutionStatusType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(institutionStatusTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InstitutionStatusType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/institution-status-type/new$'));
        cy.getEntityCreateUpdateHeading('InstitutionStatusType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', institutionStatusTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/institution-status-types',
          body: institutionStatusTypeSample,
        }).then(({ body }) => {
          institutionStatusType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/institution-status-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [institutionStatusType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(institutionStatusTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InstitutionStatusType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('institutionStatusType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', institutionStatusTypePageUrlPattern);
      });

      it('edit button click should load edit InstitutionStatusType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InstitutionStatusType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', institutionStatusTypePageUrlPattern);
      });

      it('last delete button click should delete instance of InstitutionStatusType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('institutionStatusType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', institutionStatusTypePageUrlPattern);

        institutionStatusType = undefined;
      });
    });
  });

  describe('new InstitutionStatusType page', () => {
    beforeEach(() => {
      cy.visit(`${institutionStatusTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('InstitutionStatusType');
    });

    it('should create an instance of InstitutionStatusType', () => {
      cy.get(`[data-cy="institutionStatusCode"]`).type('implement schemas Computers').should('have.value', 'implement schemas Computers');

      cy.get(`[data-cy="institutionStatusType"]`).type('demand-driven implement XML').should('have.value', 'demand-driven implement XML');

      cy.get(`[data-cy="insitutionStatusTypeDescription"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        institutionStatusType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', institutionStatusTypePageUrlPattern);
    });
  });
});
