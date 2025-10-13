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

describe('ReportTemplate e2e test', () => {
  const reportTemplatePageUrl = '/report-template';
  const reportTemplatePageUrlPattern = new RegExp('/report-template(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const reportTemplateSample = { catalogueNumber: 'Games' };

  let reportTemplate: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/report-templates+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/report-templates').as('postEntityRequest');
    cy.intercept('DELETE', '/api/report-templates/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (reportTemplate) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-templates/${reportTemplate.id}`,
      }).then(() => {
        reportTemplate = undefined;
      });
    }
  });

  it('ReportTemplates menu should load ReportTemplates page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report-template');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportTemplate').should('exist');
    cy.url().should('match', reportTemplatePageUrlPattern);
  });

  describe('ReportTemplate page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportTemplatePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportTemplate page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/report-template/new$'));
        cy.getEntityCreateUpdateHeading('ReportTemplate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportTemplatePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/report-templates',
          body: reportTemplateSample,
        }).then(({ body }) => {
          reportTemplate = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/report-templates+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reportTemplate],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportTemplatePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReportTemplate page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportTemplate');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportTemplatePageUrlPattern);
      });

      it('edit button click should load edit ReportTemplate page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportTemplate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportTemplatePageUrlPattern);
      });

      it('last delete button click should delete instance of ReportTemplate', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportTemplate').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportTemplatePageUrlPattern);

        reportTemplate = undefined;
      });
    });
  });

  describe('new ReportTemplate page', () => {
    beforeEach(() => {
      cy.visit(`${reportTemplatePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ReportTemplate');
    });

    it('should create an instance of ReportTemplate', () => {
      cy.get(`[data-cy="catalogueNumber"]`).type('deposit Cheese').should('have.value', 'deposit Cheese');

      cy.get(`[data-cy="description"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.setFieldImageAsBytesOfEntity('notes', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      cy.setFieldImageAsBytesOfEntity('compileReportFile', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        reportTemplate = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', reportTemplatePageUrlPattern);
    });
  });
});
