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

describe('JobSheet e2e test', () => {
  const jobSheetPageUrl = '/job-sheet';
  const jobSheetPageUrlPattern = new RegExp('/job-sheet(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const jobSheetSample = { serialNumber: 'deposit virtual' };

  let jobSheet: any;
  let dealer: any;

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
      url: '/api/dealers',
      body: {
        dealerName: 'Orchestrator Ergonomic',
        taxNumber: 'Divide needs-based Central',
        identificationDocumentNumber: 'Well',
        organizationName: 'Regional',
        department: 'navigating invoice',
        position: 'Reduced Rupee',
        postalAddress: 'card',
        physicalAddress: 'Japan compress holistic',
        accountName: 'Checking Account',
        accountNumber: 'actuating Identity',
        bankersName: 'leading-edge',
        bankersBranch: 'metrics synthesize',
        bankersSwiftCode: 'Vanuatu',
        fileUploadToken: 'Flats wireless Palestinian',
        compilationToken: 'silver',
        remarks: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        otherNames: 'innovative',
      },
    }).then(({ body }) => {
      dealer = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/job-sheets+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/job-sheets').as('postEntityRequest');
    cy.intercept('DELETE', '/api/job-sheets/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/dealers', {
      statusCode: 200,
      body: [dealer],
    });

    cy.intercept('GET', '/api/business-stamps', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/payment-labels', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/business-documents', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (jobSheet) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/job-sheets/${jobSheet.id}`,
      }).then(() => {
        jobSheet = undefined;
      });
    }
  });

  afterEach(() => {
    if (dealer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dealers/${dealer.id}`,
      }).then(() => {
        dealer = undefined;
      });
    }
  });

  it('JobSheets menu should load JobSheets page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('job-sheet');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('JobSheet').should('exist');
    cy.url().should('match', jobSheetPageUrlPattern);
  });

  describe('JobSheet page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(jobSheetPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create JobSheet page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/job-sheet/new$'));
        cy.getEntityCreateUpdateHeading('JobSheet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', jobSheetPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/job-sheets',

          body: {
            ...jobSheetSample,
            biller: dealer,
          },
        }).then(({ body }) => {
          jobSheet = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/job-sheets+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [jobSheet],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(jobSheetPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details JobSheet page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('jobSheet');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', jobSheetPageUrlPattern);
      });

      it('edit button click should load edit JobSheet page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('JobSheet');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', jobSheetPageUrlPattern);
      });

      it('last delete button click should delete instance of JobSheet', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('jobSheet').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', jobSheetPageUrlPattern);

        jobSheet = undefined;
      });
    });
  });

  describe('new JobSheet page', () => {
    beforeEach(() => {
      cy.visit(`${jobSheetPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('JobSheet');
    });

    it('should create an instance of JobSheet', () => {
      cy.get(`[data-cy="serialNumber"]`).type('deliverables').should('have.value', 'deliverables');

      cy.get(`[data-cy="jobSheetDate"]`).type('2022-03-20').should('have.value', '2022-03-20');

      cy.get(`[data-cy="details"]`).type('e-tailers').should('have.value', 'e-tailers');

      cy.get(`[data-cy="remarks"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="biller"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        jobSheet = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', jobSheetPageUrlPattern);
    });
  });
});
