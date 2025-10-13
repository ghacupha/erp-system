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

describe('ReportingEntity e2e test', () => {
  const reportingEntityPageUrl = '/reporting-entity';
  const reportingEntityPageUrlPattern = new RegExp('/reporting-entity(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const reportingEntitySample = { entityName: 'wireless monetize Plastic' };

  let reportingEntity: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/reporting-entities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/reporting-entities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/reporting-entities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (reportingEntity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/reporting-entities/${reportingEntity.id}`,
      }).then(() => {
        reportingEntity = undefined;
      });
    }
  });

  it('ReportingEntities menu should load ReportingEntities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('reporting-entity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportingEntity').should('exist');
    cy.url().should('match', reportingEntityPageUrlPattern);
  });

  describe('ReportingEntity page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportingEntityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportingEntity page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/reporting-entity/new$'));
        cy.getEntityCreateUpdateHeading('ReportingEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportingEntityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/reporting-entities',
          body: reportingEntitySample,
        }).then(({ body }) => {
          reportingEntity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/reporting-entities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reportingEntity],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportingEntityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReportingEntity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportingEntity');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportingEntityPageUrlPattern);
      });

      it('edit button click should load edit ReportingEntity page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportingEntity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportingEntityPageUrlPattern);
      });

      it('last delete button click should delete instance of ReportingEntity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportingEntity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportingEntityPageUrlPattern);

        reportingEntity = undefined;
      });
    });
  });

  describe('new ReportingEntity page', () => {
    beforeEach(() => {
      cy.visit(`${reportingEntityPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ReportingEntity');
    });

    it('should create an instance of ReportingEntity', () => {
      cy.get(`[data-cy="entityName"]`).type('Ameliorated bluetooth').should('have.value', 'Ameliorated bluetooth');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        reportingEntity = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', reportingEntityPageUrlPattern);
    });
  });
});
