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

describe('AssetDisposal e2e test', () => {
  const assetDisposalPageUrl = '/asset-disposal';
  const assetDisposalPageUrlPattern = new RegExp('/asset-disposal(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const assetDisposalSample = { accruedDepreciation: 38979, netBookValue: 35406, disposalDate: '2024-03-20' };

  let assetDisposal: any;
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
      body: {"startDate":"2023-07-04","endDate":"2023-07-04","depreciationPeriodStatus":"CLOSED","periodCode":"multi-tasking payment Argentine","processLocked":false},
    }).then(({ body }) => {
      depreciationPeriod = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/asset-registrations',
      body: {"assetNumber":"Re-contextualized firewall enterprise","assetTag":"Malagasy Division Cambridgeshire","assetDetails":"Persistent Beauty","assetCost":35952,"comments":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","commentsContentType":"unknown","modelNumber":"Roads and","serialNumber":"Optional Ergonomic client-server","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","capitalizationDate":"2022-04-13","historicalCost":65172,"registrationDate":"2022-04-13"},
    }).then(({ body }) => {
      assetRegistration = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-disposals+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-disposals').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-disposals/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/depreciation-periods', {
      statusCode: 200,
      body: [depreciationPeriod],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/asset-registrations', {
      statusCode: 200,
      body: [assetRegistration],
    });

  });
   */

  afterEach(() => {
    if (assetDisposal) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-disposals/${assetDisposal.id}`,
      }).then(() => {
        assetDisposal = undefined;
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

  it('AssetDisposals menu should load AssetDisposals page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-disposal');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetDisposal').should('exist');
    cy.url().should('match', assetDisposalPageUrlPattern);
  });

  describe('AssetDisposal page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetDisposalPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetDisposal page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/asset-disposal/new$'));
        cy.getEntityCreateUpdateHeading('AssetDisposal');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetDisposalPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-disposals',
  
          body: {
            ...assetDisposalSample,
            effectivePeriod: depreciationPeriod,
            assetDisposed: assetRegistration,
          },
        }).then(({ body }) => {
          assetDisposal = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-disposals+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [assetDisposal],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetDisposalPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(assetDisposalPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AssetDisposal page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetDisposal');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetDisposalPageUrlPattern);
      });

      it('edit button click should load edit AssetDisposal page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetDisposal');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetDisposalPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of AssetDisposal', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetDisposal').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetDisposalPageUrlPattern);

        assetDisposal = undefined;
      });
    });
  });

  describe('new AssetDisposal page', () => {
    beforeEach(() => {
      cy.visit(`${assetDisposalPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AssetDisposal');
    });

    it.skip('should create an instance of AssetDisposal', () => {
      cy.get(`[data-cy="assetDisposalReference"]`)
        .type('1cb990ce-ad5a-4f0f-b3c3-bd0a7f3218c6')
        .invoke('val')
        .should('match', new RegExp('1cb990ce-ad5a-4f0f-b3c3-bd0a7f3218c6'));

      cy.get(`[data-cy="description"]`).type('Overpass People&#39;s Gloves').should('have.value', 'Overpass People&#39;s Gloves');

      cy.get(`[data-cy="assetCost"]`).type('96622').should('have.value', '96622');

      cy.get(`[data-cy="historicalCost"]`).type('97689').should('have.value', '97689');

      cy.get(`[data-cy="accruedDepreciation"]`).type('87944').should('have.value', '87944');

      cy.get(`[data-cy="netBookValue"]`).type('1659').should('have.value', '1659');

      cy.get(`[data-cy="decommissioningDate"]`).type('2024-03-20').should('have.value', '2024-03-20');

      cy.get(`[data-cy="disposalDate"]`).type('2024-03-20').should('have.value', '2024-03-20');

      cy.get(`[data-cy="dormant"]`).should('not.be.checked');
      cy.get(`[data-cy="dormant"]`).click().should('be.checked');

      cy.get(`[data-cy="effectivePeriod"]`).select(1);
      cy.get(`[data-cy="assetDisposed"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        assetDisposal = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', assetDisposalPageUrlPattern);
    });
  });
});
