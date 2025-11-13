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

describe('AssetRevaluation e2e test', () => {
  const assetRevaluationPageUrl = '/asset-revaluation';
  const assetRevaluationPageUrlPattern = new RegExp('/asset-revaluation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const assetRevaluationSample = { devaluationAmount: 68612, revaluationDate: '2024-04-11' };

  let assetRevaluation: any;
  //let depreciationPeriod: any;
  //let assetRegistration: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/depreciation-periods',
      body: {"startDate":"2023-07-04","endDate":"2023-07-04","depreciationPeriodStatus":"PROCESSING","periodCode":"Concrete Developer PCI","processLocked":true},
    }).then(({ body }) => {
      depreciationPeriod = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/asset-registrations',
      body: {"assetNumber":"Producer","assetTag":"Row","assetDetails":"generating","assetCost":2602,"comments":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","commentsContentType":"unknown","modelNumber":"Applications","serialNumber":"bus Savings methodical","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","capitalizationDate":"2022-04-13","historicalCost":97900,"registrationDate":"2022-04-12"},
    }).then(({ body }) => {
      assetRegistration = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-revaluations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-revaluations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-revaluations/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/dealers', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/depreciation-periods', {
      statusCode: 200,
      body: [depreciationPeriod],
    });

    cy.intercept('GET', '/api/asset-registrations', {
      statusCode: 200,
      body: [assetRegistration],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (assetRevaluation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-revaluations/${assetRevaluation.id}`,
      }).then(() => {
        assetRevaluation = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (depreciationPeriod) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/depreciation-periods/${depreciationPeriod.id}`,
      }).then(() => {
        depreciationPeriod = undefined;
      });
    }
    if (assetRegistration) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-registrations/${assetRegistration.id}`,
      }).then(() => {
        assetRegistration = undefined;
      });
    }
  });
   */

  it('AssetRevaluations menu should load AssetRevaluations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-revaluation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetRevaluation').should('exist');
    cy.url().should('match', assetRevaluationPageUrlPattern);
  });

  describe('AssetRevaluation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetRevaluationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetRevaluation page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/asset-revaluation/new$'));
        cy.getEntityCreateUpdateHeading('AssetRevaluation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRevaluationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-revaluations',
  
          body: {
            ...assetRevaluationSample,
            effectivePeriod: depreciationPeriod,
            revaluedAsset: assetRegistration,
          },
        }).then(({ body }) => {
          assetRevaluation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-revaluations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [assetRevaluation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetRevaluationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(assetRevaluationPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AssetRevaluation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetRevaluation');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRevaluationPageUrlPattern);
      });

      it('edit button click should load edit AssetRevaluation page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetRevaluation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRevaluationPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of AssetRevaluation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetRevaluation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetRevaluationPageUrlPattern);

        assetRevaluation = undefined;
      });
    });
  });

  describe('new AssetRevaluation page', () => {
    beforeEach(() => {
      cy.visit(`${assetRevaluationPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AssetRevaluation');
    });

    it.skip('should create an instance of AssetRevaluation', () => {
      cy.get(`[data-cy="description"]`).type('Jewelery').should('have.value', 'Jewelery');

      cy.get(`[data-cy="devaluationAmount"]`).type('9759').should('have.value', '9759');

      cy.get(`[data-cy="revaluationDate"]`).type('2024-04-11').should('have.value', '2024-04-11');

      cy.get(`[data-cy="revaluationReferenceId"]`)
        .type('8a0f92a3-de96-4887-ab8b-e9f893f21a17')
        .invoke('val')
        .should('match', new RegExp('8a0f92a3-de96-4887-ab8b-e9f893f21a17'));

      cy.get(`[data-cy="timeOfCreation"]`).type('2024-04-12T00:42').should('have.value', '2024-04-12T00:42');

      cy.get(`[data-cy="effectivePeriod"]`).select(1);
      cy.get(`[data-cy="revaluedAsset"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        assetRevaluation = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', assetRevaluationPageUrlPattern);
    });
  });
});
