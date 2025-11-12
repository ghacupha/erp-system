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

describe('StaffCurrentEmploymentStatus e2e test', () => {
  const staffCurrentEmploymentStatusPageUrl = '/staff-current-employment-status';
  const staffCurrentEmploymentStatusPageUrlPattern = new RegExp('/staff-current-employment-status(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const staffCurrentEmploymentStatusSample = {
    staffCurrentEmploymentStatusTypeCode: 'productize',
    staffCurrentEmploymentStatusType: 'Knolls Investment',
  };

  let staffCurrentEmploymentStatus: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/staff-current-employment-statuses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/staff-current-employment-statuses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/staff-current-employment-statuses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (staffCurrentEmploymentStatus) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/staff-current-employment-statuses/${staffCurrentEmploymentStatus.id}`,
      }).then(() => {
        staffCurrentEmploymentStatus = undefined;
      });
    }
  });

  it('StaffCurrentEmploymentStatuses menu should load StaffCurrentEmploymentStatuses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('staff-current-employment-status');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StaffCurrentEmploymentStatus').should('exist');
    cy.url().should('match', staffCurrentEmploymentStatusPageUrlPattern);
  });

  describe('StaffCurrentEmploymentStatus page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(staffCurrentEmploymentStatusPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StaffCurrentEmploymentStatus page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/staff-current-employment-status/new$'));
        cy.getEntityCreateUpdateHeading('StaffCurrentEmploymentStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', staffCurrentEmploymentStatusPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/staff-current-employment-statuses',
          body: staffCurrentEmploymentStatusSample,
        }).then(({ body }) => {
          staffCurrentEmploymentStatus = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/staff-current-employment-statuses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [staffCurrentEmploymentStatus],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(staffCurrentEmploymentStatusPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StaffCurrentEmploymentStatus page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('staffCurrentEmploymentStatus');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', staffCurrentEmploymentStatusPageUrlPattern);
      });

      it('edit button click should load edit StaffCurrentEmploymentStatus page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StaffCurrentEmploymentStatus');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', staffCurrentEmploymentStatusPageUrlPattern);
      });

      it('last delete button click should delete instance of StaffCurrentEmploymentStatus', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('staffCurrentEmploymentStatus').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', staffCurrentEmploymentStatusPageUrlPattern);

        staffCurrentEmploymentStatus = undefined;
      });
    });
  });

  describe('new StaffCurrentEmploymentStatus page', () => {
    beforeEach(() => {
      cy.visit(`${staffCurrentEmploymentStatusPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('StaffCurrentEmploymentStatus');
    });

    it('should create an instance of StaffCurrentEmploymentStatus', () => {
      cy.get(`[data-cy="staffCurrentEmploymentStatusTypeCode"]`).type('Granite').should('have.value', 'Granite');

      cy.get(`[data-cy="staffCurrentEmploymentStatusType"]`).type('Up-sized').should('have.value', 'Up-sized');

      cy.get(`[data-cy="staffCurrentEmploymentStatusTypeDetails"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        staffCurrentEmploymentStatus = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', staffCurrentEmploymentStatusPageUrlPattern);
    });
  });
});
