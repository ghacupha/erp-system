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

describe('ReportContentType e2e test', () => {
  const reportContentTypePageUrl = '/report-content-type';
  const reportContentTypePageUrlPattern = new RegExp('/report-content-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const reportContentTypeSample = { reportTypeName: 'Practical', reportFileExtension: 'Keyboard' };

  let reportContentType: any;
  let systemContentType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/system-content-types',
      body: {
        contentTypeName: 'Tasty yellow auxiliary',
        contentTypeHeader: 'Concrete',
        comments: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        availability: 'SUPPORTED',
      },
    }).then(({ body }) => {
      systemContentType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/report-content-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/report-content-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/report-content-types/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/system-content-types', {
      statusCode: 200,
      body: [systemContentType],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (reportContentType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/report-content-types/${reportContentType.id}`,
      }).then(() => {
        reportContentType = undefined;
      });
    }
  });

  afterEach(() => {
    if (systemContentType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/system-content-types/${systemContentType.id}`,
      }).then(() => {
        systemContentType = undefined;
      });
    }
  });

  it('ReportContentTypes menu should load ReportContentTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('report-content-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReportContentType').should('exist');
    cy.url().should('match', reportContentTypePageUrlPattern);
  });

  describe('ReportContentType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reportContentTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReportContentType page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/report-content-type/new$'));
        cy.getEntityCreateUpdateHeading('ReportContentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportContentTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/report-content-types',

          body: {
            ...reportContentTypeSample,
            systemContentType: systemContentType,
          },
        }).then(({ body }) => {
          reportContentType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/report-content-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [reportContentType],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(reportContentTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReportContentType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reportContentType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportContentTypePageUrlPattern);
      });

      it('edit button click should load edit ReportContentType page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReportContentType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportContentTypePageUrlPattern);
      });

      it('last delete button click should delete instance of ReportContentType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reportContentType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', reportContentTypePageUrlPattern);

        reportContentType = undefined;
      });
    });
  });

  describe('new ReportContentType page', () => {
    beforeEach(() => {
      cy.visit(`${reportContentTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ReportContentType');
    });

    it('should create an instance of ReportContentType', () => {
      cy.get(`[data-cy="reportTypeName"]`).type('back-end interactive hack').should('have.value', 'back-end interactive hack');

      cy.get(`[data-cy="reportFileExtension"]`).type('Mouse hack').should('have.value', 'Mouse hack');

      cy.get(`[data-cy="systemContentType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        reportContentType = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', reportContentTypePageUrlPattern);
    });
  });
});
