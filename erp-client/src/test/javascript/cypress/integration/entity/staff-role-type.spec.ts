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

describe('StaffRoleType e2e test', () => {
  const staffRoleTypePageUrl = '/staff-role-type';
  const staffRoleTypePageUrlPattern = new RegExp('/staff-role-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const staffRoleTypeSample = { staffRoleTypeCode: 'withdrawal Director olive', staffRoleType: 'withdrawal Director New' };

  let staffRoleType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/staff-role-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/staff-role-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/staff-role-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (staffRoleType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/staff-role-types/${staffRoleType.id}`,
      }).then(() => {
        staffRoleType = undefined;
      });
    }
  });

  it('StaffRoleTypes menu should load StaffRoleTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('staff-role-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StaffRoleType').should('exist');
    cy.url().should('match', staffRoleTypePageUrlPattern);
  });

  describe('StaffRoleType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(staffRoleTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StaffRoleType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/staff-role-type/new$'));
        cy.getEntityCreateUpdateHeading('StaffRoleType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', staffRoleTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/staff-role-types',
          body: staffRoleTypeSample,
        }).then(({ body }) => {
          staffRoleType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/staff-role-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [staffRoleType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(staffRoleTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StaffRoleType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('staffRoleType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', staffRoleTypePageUrlPattern);
      });

      it('edit button click should load edit StaffRoleType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StaffRoleType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', staffRoleTypePageUrlPattern);
      });

      it('last delete button click should delete instance of StaffRoleType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('staffRoleType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', staffRoleTypePageUrlPattern);

        staffRoleType = undefined;
      });
    });
  });

  describe('new StaffRoleType page', () => {
    beforeEach(() => {
      cy.visit(`${staffRoleTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('StaffRoleType');
    });

    it('should create an instance of StaffRoleType', () => {
      cy.get(`[data-cy="staffRoleTypeCode"]`).type('cross-platform').should('have.value', 'cross-platform');

      cy.get(`[data-cy="staffRoleType"]`).type('asynchronous 24 software').should('have.value', 'asynchronous 24 software');

      cy.get(`[data-cy="staffRoleTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        staffRoleType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', staffRoleTypePageUrlPattern);
    });
  });
});
