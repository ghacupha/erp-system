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

describe('WorkInProgressRegistration e2e test', () => {
  const workInProgressRegistrationPageUrl = '/work-in-progress-registration';
  const workInProgressRegistrationPageUrlPattern = new RegExp('/work-in-progress-registration(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const workInProgressRegistrationSample = { sequenceNumber: 'Village web-enabled Consultant' };

  let workInProgressRegistration: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/work-in-progress-registrations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/work-in-progress-registrations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/work-in-progress-registrations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (workInProgressRegistration) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/work-in-progress-registrations/${workInProgressRegistration.id}`,
      }).then(() => {
        workInProgressRegistration = undefined;
      });
    }
  });

  it('WorkInProgressRegistrations menu should load WorkInProgressRegistrations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('work-in-progress-registration');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WorkInProgressRegistration').should('exist');
    cy.url().should('match', workInProgressRegistrationPageUrlPattern);
  });

  describe('WorkInProgressRegistration page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(workInProgressRegistrationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WorkInProgressRegistration page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/work-in-progress-registration/new$'));
        cy.getEntityCreateUpdateHeading('WorkInProgressRegistration');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workInProgressRegistrationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/work-in-progress-registrations',
          body: workInProgressRegistrationSample,
        }).then(({ body }) => {
          workInProgressRegistration = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/work-in-progress-registrations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [workInProgressRegistration],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(workInProgressRegistrationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WorkInProgressRegistration page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('workInProgressRegistration');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workInProgressRegistrationPageUrlPattern);
      });

      it('edit button click should load edit WorkInProgressRegistration page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WorkInProgressRegistration');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workInProgressRegistrationPageUrlPattern);
      });

      it('last delete button click should delete instance of WorkInProgressRegistration', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('workInProgressRegistration').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', workInProgressRegistrationPageUrlPattern);

        workInProgressRegistration = undefined;
      });
    });
  });

  describe('new WorkInProgressRegistration page', () => {
    beforeEach(() => {
      cy.visit(`${workInProgressRegistrationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('WorkInProgressRegistration');
    });

    it('should create an instance of WorkInProgressRegistration', () => {
      cy.get(`[data-cy="sequenceNumber"]`).type('Convertible').should('have.value', 'Convertible');

      cy.get(`[data-cy="particulars"]`).type('San Plastic').should('have.value', 'San Plastic');

      cy.get(`[data-cy="instalmentDate"]`).type('2022-04-13').should('have.value', '2022-04-13');

      cy.get(`[data-cy="instalmentAmount"]`).type('52173').should('have.value', '52173');

      cy.setFieldImageAsBytesOfEntity('comments', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="levelOfCompletion"]`).type('18794').should('have.value', '18794');

      cy.get(`[data-cy="completed"]`).should('not.be.checked');
      cy.get(`[data-cy="completed"]`).click().should('be.checked');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        workInProgressRegistration = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', workInProgressRegistrationPageUrlPattern);
    });
  });
});
