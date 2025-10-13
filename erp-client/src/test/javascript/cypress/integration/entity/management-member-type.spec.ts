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

describe('ManagementMemberType e2e test', () => {
  const managementMemberTypePageUrl = '/management-member-type';
  const managementMemberTypePageUrlPattern = new RegExp('/management-member-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const managementMemberTypeSample = { managementMemberTypeCode: 'directional Home', managementMemberType: 'Lodge' };

  let managementMemberType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/management-member-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/management-member-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/management-member-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (managementMemberType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/management-member-types/${managementMemberType.id}`,
      }).then(() => {
        managementMemberType = undefined;
      });
    }
  });

  it('ManagementMemberTypes menu should load ManagementMemberTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('management-member-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ManagementMemberType').should('exist');
    cy.url().should('match', managementMemberTypePageUrlPattern);
  });

  describe('ManagementMemberType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(managementMemberTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ManagementMemberType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/management-member-type/new$'));
        cy.getEntityCreateUpdateHeading('ManagementMemberType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', managementMemberTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/management-member-types',
          body: managementMemberTypeSample,
        }).then(({ body }) => {
          managementMemberType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/management-member-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [managementMemberType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(managementMemberTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ManagementMemberType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('managementMemberType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', managementMemberTypePageUrlPattern);
      });

      it('edit button click should load edit ManagementMemberType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ManagementMemberType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', managementMemberTypePageUrlPattern);
      });

      it('last delete button click should delete instance of ManagementMemberType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('managementMemberType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', managementMemberTypePageUrlPattern);

        managementMemberType = undefined;
      });
    });
  });

  describe('new ManagementMemberType page', () => {
    beforeEach(() => {
      cy.visit(`${managementMemberTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ManagementMemberType');
    });

    it('should create an instance of ManagementMemberType', () => {
      cy.get(`[data-cy="managementMemberTypeCode"]`).type('Health Multi-lateral').should('have.value', 'Health Multi-lateral');

      cy.get(`[data-cy="managementMemberType"]`).type('Future-proofed').should('have.value', 'Future-proofed');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        managementMemberType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', managementMemberTypePageUrlPattern);
    });
  });
});
