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

describe('DepartmentType e2e test', () => {
  const departmentTypePageUrl = '/department-type';
  const departmentTypePageUrlPattern = new RegExp('/department-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const departmentTypeSample = { departmentTypeCode: 'Solutions Chicken brand', departmentType: 'Bahrain purple California' };

  let departmentType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/department-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/department-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/department-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (departmentType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/department-types/${departmentType.id}`,
      }).then(() => {
        departmentType = undefined;
      });
    }
  });

  it('DepartmentTypes menu should load DepartmentTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('department-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DepartmentType').should('exist');
    cy.url().should('match', departmentTypePageUrlPattern);
  });

  describe('DepartmentType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(departmentTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DepartmentType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/department-type/new$'));
        cy.getEntityCreateUpdateHeading('DepartmentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/department-types',
          body: departmentTypeSample,
        }).then(({ body }) => {
          departmentType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/department-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [departmentType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(departmentTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DepartmentType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('departmentType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentTypePageUrlPattern);
      });

      it('edit button click should load edit DepartmentType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DepartmentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentTypePageUrlPattern);
      });

      it('last delete button click should delete instance of DepartmentType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('departmentType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', departmentTypePageUrlPattern);

        departmentType = undefined;
      });
    });
  });

  describe('new DepartmentType page', () => {
    beforeEach(() => {
      cy.visit(`${departmentTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('DepartmentType');
    });

    it('should create an instance of DepartmentType', () => {
      cy.get(`[data-cy="departmentTypeCode"]`).type('enterprise generate').should('have.value', 'enterprise generate');

      cy.get(`[data-cy="departmentType"]`).type('discrete Cambridgeshire Granite').should('have.value', 'discrete Cambridgeshire Granite');

      cy.get(`[data-cy="departmentTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        departmentType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', departmentTypePageUrlPattern);
    });
  });
});
