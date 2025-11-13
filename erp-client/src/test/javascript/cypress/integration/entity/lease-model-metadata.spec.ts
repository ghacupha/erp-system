///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

describe('LeaseModelMetadata e2e test', () => {
  const leaseModelMetadataPageUrl = '/lease-model-metadata';
  const leaseModelMetadataPageUrlPattern = new RegExp('/lease-model-metadata(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const leaseModelMetadataSample = {
    modelTitle: 'Shores copying',
    modelVersion: 58094,
    annualDiscountingRate: 20161,
    commencementDate: '2023-03-28',
    terminalDate: '2023-03-28',
  };

  let leaseModelMetadata: any;
  let leaseContract: any;
  let settlementCurrency: any;

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
      url: '/api/lease-contracts',
      body: {
        bookingId: 'Central',
        leaseTitle: 'Tuna Mouse application',
        identifier: 'd42126d2-932f-46ac-9613-a6d4fa51d51f',
        description: 'Enhanced',
        commencementDate: '2023-01-09',
        terminalDate: '2023-01-08',
      },
    }).then(({ body }) => {
      leaseContract = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/settlement-currencies',
      body: {
        iso4217CurrencyCode: 'Gen',
        currencyName: 'Trinidad and Tobago Dollar',
        country: 'French Polynesia',
        numericCode: 'magenta',
        minorUnit: 'Unbranded complexity state',
        fileUploadToken: 'attitude',
        compilationToken: 'Kenya Dynamic Books',
      },
    }).then(({ body }) => {
      settlementCurrency = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/lease-model-metadata+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/lease-model-metadata').as('postEntityRequest');
    cy.intercept('DELETE', '/api/lease-model-metadata/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/universally-unique-mappings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/lease-contracts', {
      statusCode: 200,
      body: [leaseContract],
    });

    cy.intercept('GET', '/api/lease-model-metadata', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/settlement-currencies', {
      statusCode: 200,
      body: [settlementCurrency],
    });

    cy.intercept('GET', '/api/business-documents', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/security-clearances', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/transaction-accounts', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (leaseModelMetadata) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-model-metadata/${leaseModelMetadata.id}`,
      }).then(() => {
        leaseModelMetadata = undefined;
      });
    }
  });

  afterEach(() => {
    if (leaseContract) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/lease-contracts/${leaseContract.id}`,
      }).then(() => {
        leaseContract = undefined;
      });
    }
    if (settlementCurrency) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/settlement-currencies/${settlementCurrency.id}`,
      }).then(() => {
        settlementCurrency = undefined;
      });
    }
  });

  it('LeaseModelMetadata menu should load LeaseModelMetadata page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('lease-model-metadata');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LeaseModelMetadata').should('exist');
    cy.url().should('match', leaseModelMetadataPageUrlPattern);
  });

  describe('LeaseModelMetadata page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(leaseModelMetadataPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LeaseModelMetadata page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/lease-model-metadata/new$'));
        cy.getEntityCreateUpdateHeading('LeaseModelMetadata');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseModelMetadataPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/lease-model-metadata',

          body: {
            ...leaseModelMetadataSample,
            leaseContract: leaseContract,
            liabilityCurrency: settlementCurrency,
            rouAssetCurrency: settlementCurrency,
          },
        }).then(({ body }) => {
          leaseModelMetadata = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/lease-model-metadata+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [leaseModelMetadata],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(leaseModelMetadataPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LeaseModelMetadata page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('leaseModelMetadata');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseModelMetadataPageUrlPattern);
      });

      it('edit button click should load edit LeaseModelMetadata page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LeaseModelMetadata');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseModelMetadataPageUrlPattern);
      });

      it('last delete button click should delete instance of LeaseModelMetadata', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('leaseModelMetadata').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', leaseModelMetadataPageUrlPattern);

        leaseModelMetadata = undefined;
      });
    });
  });

  describe('new LeaseModelMetadata page', () => {
    beforeEach(() => {
      cy.visit(`${leaseModelMetadataPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('LeaseModelMetadata');
    });

    it('should create an instance of LeaseModelMetadata', () => {
      cy.get(`[data-cy="modelTitle"]`).type('Sausages boliviano Idaho').should('have.value', 'Sausages boliviano Idaho');

      cy.get(`[data-cy="modelVersion"]`).type('76844').should('have.value', '76844');

      cy.get(`[data-cy="description"]`).type('Forges Generic Sweden').should('have.value', 'Forges Generic Sweden');

      cy.setFieldImageAsBytesOfEntity('modelNotes', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="annualDiscountingRate"]`).type('60438').should('have.value', '60438');

      cy.get(`[data-cy="commencementDate"]`).type('2023-03-28').should('have.value', '2023-03-28');

      cy.get(`[data-cy="terminalDate"]`).type('2023-03-27').should('have.value', '2023-03-27');

      cy.get(`[data-cy="totalReportingPeriods"]`).type('8627').should('have.value', '8627');

      cy.get(`[data-cy="reportingPeriodsPerYear"]`).type('54982').should('have.value', '54982');

      cy.get(`[data-cy="settlementPeriodsPerYear"]`).type('24690').should('have.value', '24690');

      cy.get(`[data-cy="initialLiabilityAmount"]`).type('26418').should('have.value', '26418');

      cy.get(`[data-cy="initialROUAmount"]`).type('94130').should('have.value', '94130');

      cy.get(`[data-cy="totalDepreciationPeriods"]`).type('37080').should('have.value', '37080');

      cy.get(`[data-cy="leaseContract"]`).select(1);
      cy.get(`[data-cy="liabilityCurrency"]`).select(1);
      cy.get(`[data-cy="rouAssetCurrency"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        leaseModelMetadata = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', leaseModelMetadataPageUrlPattern);
    });
  });
});
