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

describe('AssetWriteOff e2e test', () => {
  const assetWriteOffPageUrl = '/asset-write-off';
  const assetWriteOffPageUrlPattern = new RegExp('/asset-write-off(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const assetWriteOffSample = { writeOffAmount: 85447, writeOffDate: '2024-03-20' };

  let assetWriteOff: any;
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
      body: {"startDate":"2023-07-04","endDate":"2023-07-04","depreciationPeriodStatus":"OPEN","periodCode":"mindshare","processLocked":true},
    }).then(({ body }) => {
      depreciationPeriod = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/asset-registrations',
      body: {"assetNumber":"bypass","assetTag":"EXE","assetDetails":"Representative","assetCost":91897,"comments":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=","commentsContentType":"unknown","modelNumber":"Market Turnpike","serialNumber":"Account hack","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","capitalizationDate":"2022-04-12","historicalCost":40469,"registrationDate":"2022-04-13"},
    }).then(({ body }) => {
      assetRegistration = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/asset-write-offs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/asset-write-offs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/asset-write-offs/*').as('deleteEntityRequest');
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
    if (assetWriteOff) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/asset-write-offs/${assetWriteOff.id}`,
      }).then(() => {
        assetWriteOff = undefined;
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

  it('AssetWriteOffs menu should load AssetWriteOffs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('asset-write-off');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AssetWriteOff').should('exist');
    cy.url().should('match', assetWriteOffPageUrlPattern);
  });

  describe('AssetWriteOff page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(assetWriteOffPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AssetWriteOff page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/asset-write-off/new$'));
        cy.getEntityCreateUpdateHeading('AssetWriteOff');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetWriteOffPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/asset-write-offs',
  
          body: {
            ...assetWriteOffSample,
            effectivePeriod: depreciationPeriod,
            assetWrittenOff: assetRegistration,
          },
        }).then(({ body }) => {
          assetWriteOff = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/asset-write-offs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [assetWriteOff],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(assetWriteOffPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(assetWriteOffPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AssetWriteOff page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('assetWriteOff');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetWriteOffPageUrlPattern);
      });

      it('edit button click should load edit AssetWriteOff page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AssetWriteOff');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetWriteOffPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of AssetWriteOff', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('assetWriteOff').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', assetWriteOffPageUrlPattern);

        assetWriteOff = undefined;
      });
    });
  });

  describe('new AssetWriteOff page', () => {
    beforeEach(() => {
      cy.visit(`${assetWriteOffPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('AssetWriteOff');
    });

    it.skip('should create an instance of AssetWriteOff', () => {
      cy.get(`[data-cy="description"]`).type('Alabama Reverse-engineered Brand').should('have.value', 'Alabama Reverse-engineered Brand');

      cy.get(`[data-cy="writeOffAmount"]`).type('12421').should('have.value', '12421');

      cy.get(`[data-cy="writeOffDate"]`).type('2024-03-19').should('have.value', '2024-03-19');

      cy.get(`[data-cy="writeOffReferenceId"]`)
        .type('fbfa238d-8fc8-4f5a-97c4-9e4c75721984')
        .invoke('val')
        .should('match', new RegExp('fbfa238d-8fc8-4f5a-97c4-9e4c75721984'));

      cy.get(`[data-cy="effectivePeriod"]`).select(1);
      cy.get(`[data-cy="assetWrittenOff"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        assetWriteOff = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', assetWriteOffPageUrlPattern);
    });
  });
});
