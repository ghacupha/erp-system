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

describe('AssetAdditionsReport e2e test', () => {
  const assetAdditionsReportPageUrl = '/asset-additions-report';
  const assetAdditionsReportPageUrlPattern = new RegExp('/asset-additions-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const assetAdditionsReportSample = {};

  let assetAdditionsReport: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-additions-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-additions-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-additions-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (assetAdditionsReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-additions-reports/${assetAdditionsReport.id}`,
      }).then(() => {
        assetAdditionsReport = undefined;
      });
    }
  });

  it('AssetAdditionsReports menu should load AssetAdditionsReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-additions-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetAdditionsReport').should('exist');
    cy.url().should('match', assetAdditionsReportPageUrlPattern);
  });

  describe('AssetAdditionsReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetAdditionsReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetAdditionsReport page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/asset-additions-report/new$'));
        cy.getEntityCreateUpdateHeading('AssetAdditionsReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetAdditionsReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-additions-reports',
          body: assetAdditionsReportSample,
        }).then(({ body }) => {
          assetAdditionsReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-additions-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [assetAdditionsReport],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetAdditionsReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AssetAdditionsReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetAdditionsReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetAdditionsReportPageUrlPattern);
      });

      it('edit button click should load edit AssetAdditionsReport page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetAdditionsReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetAdditionsReportPageUrlPattern);
      });

      it('last delete button click should delete instance of AssetAdditionsReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetAdditionsReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetAdditionsReportPageUrlPattern);

        assetAdditionsReport = undefined;
      });
    });
  });

  describe('new AssetAdditionsReport page', () => {
    beforeEach(() => {
      cy.visit(`${assetAdditionsReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AssetAdditionsReport');
    });

    it('should create an instance of AssetAdditionsReport', () => {
      cy.get(`[data-cy="timeOfRequest"]`).type('2024-01-25').should('have.value', '2024-01-25');

      cy.get(`[data-cy="reportStartDate"]`).type('2024-01-26').should('have.value', '2024-01-26');

      cy.get(`[data-cy="reportEndDate"]`).type('2024-01-25').should('have.value', '2024-01-25');

      cy.get(`[data-cy="requestId"]`)
        .type('15c2c285-a924-4ad7-99fd-c2fdbc0ff3e6')
        .invoke('val')
        .should('match', new RegExp('15c2c285-a924-4ad7-99fd-c2fdbc0ff3e6'));

      cy.get(`[data-cy="tampered"]`).should('not.be.checked');
      cy.get(`[data-cy="tampered"]`).click().should('be.checked');

      cy.get(`[data-cy="reportParameters"]`).type('sky Borders Avon').should('have.value', 'sky Borders Avon');

      cy.setFieldImageAsBytesOfEntity('reportFile', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        assetAdditionsReport = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', assetAdditionsReportPageUrlPattern);
    });
  });
});
