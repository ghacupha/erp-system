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

describe('AssetGeneralAdjustment e2e test', () => {
  const assetGeneralAdjustmentPageUrl = '/asset-general-adjustment';
  const assetGeneralAdjustmentPageUrlPattern = new RegExp('/asset-general-adjustment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const assetGeneralAdjustmentSample = {
    devaluationAmount: 61642,
    adjustmentDate: '2024-04-11',
    timeOfCreation: '2024-04-12T05:25:55.958Z',
    adjustmentReferenceId: 'a5938478-1fdf-4509-8774-73326ac556f8',
  };

  let assetGeneralAdjustment: any;
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
      body: {"startDate":"2023-07-04","endDate":"2023-07-03","depreciationPeriodStatus":"OPEN","periodCode":"JSON Court Brand","processLocked":true},
    }).then(({ body }) => {
      depreciationPeriod = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/asset-registrations',
      body: {"assetNumber":"payment","assetTag":"Group Highway","assetDetails":"one-to-one","assetCost":26930,"comments":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","commentsContentType":"unknown","modelNumber":"Investor dynamic access","serialNumber":"Agent Berkshire","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","capitalizationDate":"2022-04-12","historicalCost":49180,"registrationDate":"2022-04-13"},
    }).then(({ body }) => {
      assetRegistration = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-general-adjustments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-general-adjustments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-general-adjustments/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/depreciation-periods', {
      statusCode: 200,
      body: [depreciationPeriod],
    });

    cy.intercept('GET', '/api/asset-registrations', {
      statusCode: 200,
      body: [assetRegistration],
    });

    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (assetGeneralAdjustment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-general-adjustments/${assetGeneralAdjustment.id}`,
      }).then(() => {
        assetGeneralAdjustment = undefined;
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

  it('AssetGeneralAdjustments menu should load AssetGeneralAdjustments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-general-adjustment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetGeneralAdjustment').should('exist');
    cy.url().should('match', assetGeneralAdjustmentPageUrlPattern);
  });

  describe('AssetGeneralAdjustment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetGeneralAdjustmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetGeneralAdjustment page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/asset-general-adjustment/new$'));
        cy.getEntityCreateUpdateHeading('AssetGeneralAdjustment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetGeneralAdjustmentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-general-adjustments',
  
          body: {
            ...assetGeneralAdjustmentSample,
            effectivePeriod: depreciationPeriod,
            assetRegistration: assetRegistration,
          },
        }).then(({ body }) => {
          assetGeneralAdjustment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-general-adjustments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [assetGeneralAdjustment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetGeneralAdjustmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(assetGeneralAdjustmentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AssetGeneralAdjustment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetGeneralAdjustment');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetGeneralAdjustmentPageUrlPattern);
      });

      it('edit button click should load edit AssetGeneralAdjustment page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetGeneralAdjustment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetGeneralAdjustmentPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of AssetGeneralAdjustment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetGeneralAdjustment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetGeneralAdjustmentPageUrlPattern);

        assetGeneralAdjustment = undefined;
      });
    });
  });

  describe('new AssetGeneralAdjustment page', () => {
    beforeEach(() => {
      cy.visit(`${assetGeneralAdjustmentPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AssetGeneralAdjustment');
    });

    it.skip('should create an instance of AssetGeneralAdjustment', () => {
      cy.get(`[data-cy="description"]`).type('Antilles').should('have.value', 'Antilles');

      cy.get(`[data-cy="devaluationAmount"]`).type('34677').should('have.value', '34677');

      cy.get(`[data-cy="adjustmentDate"]`).type('2024-04-11').should('have.value', '2024-04-11');

      cy.get(`[data-cy="timeOfCreation"]`).type('2024-04-12T02:06').should('have.value', '2024-04-12T02:06');

      cy.get(`[data-cy="adjustmentReferenceId"]`)
        .type('5efbd613-0ee7-4acd-b8a3-20fe2bec1bb7')
        .invoke('val')
        .should('match', new RegExp('5efbd613-0ee7-4acd-b8a3-20fe2bec1bb7'));

      cy.get(`[data-cy="effectivePeriod"]`).select(1);
      cy.get(`[data-cy="assetRegistration"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        assetGeneralAdjustment = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', assetGeneralAdjustmentPageUrlPattern);
    });
  });
});
