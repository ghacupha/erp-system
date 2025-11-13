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

describe('RelatedPartyRelationship e2e test', () => {
  const relatedPartyRelationshipPageUrl = '/related-party-relationship';
  const relatedPartyRelationshipPageUrlPattern = new RegExp('/related-party-relationship(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const relatedPartyRelationshipSample = {
    reportingDate: '2023-10-04',
    customerId: 'help-desk',
    relatedPartyId: 'mesh Bahrain Accountability',
  };

  let relatedPartyRelationship: any;
  let institutionCode: any;
  let bankBranchCode: any;
  let partyRelationType: any;

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
      url: '/api/institution-codes',
      body: {
        institutionCode: 'matrix',
        institutionName: 'Games Parks Concrete',
        shortName: 'Cheese',
        category: 'Buckinghamshire Down-sized optical',
        institutionCategory: 'Loan',
        institutionOwnership: 'wireless extend',
        dateLicensed: '2022-04-05',
        institutionStatus: 'Pants Strategist',
      },
    }).then(({ body }) => {
      institutionCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/bank-branch-codes',
      body: {
        bankCode: 'Concrete intelligence Intelligent',
        bankName: 'mobile',
        branchCode: 'Practical solutions',
        branchName: 'extranet gold generating',
        notes: 'Dalasi programming Avenue',
      },
    }).then(({ body }) => {
      bankBranchCode = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/party-relation-types',
      body: {
        partyRelationTypeCode: 'cyan Assistant',
        partyRelationType: 'Bedfordshire Program Pennsylvania',
        partyRelationTypeDescription: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      partyRelationType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/related-party-relationships+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/related-party-relationships').as('postEntityRequest');
    cy.intercept('DELETE', '/api/related-party-relationships/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/institution-codes', {
      statusCode: 200,
      body: [institutionCode],
    });

    cy.intercept('GET', '/api/bank-branch-codes', {
      statusCode: 200,
      body: [bankBranchCode],
    });

    cy.intercept('GET', '/api/party-relation-types', {
      statusCode: 200,
      body: [partyRelationType],
    });
  });

  afterEach(() => {
    if (relatedPartyRelationship) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/related-party-relationships/${relatedPartyRelationship.id}`,
      }).then(() => {
        relatedPartyRelationship = undefined;
      });
    }
  });

  afterEach(() => {
    if (institutionCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/institution-codes/${institutionCode.id}`,
      }).then(() => {
        institutionCode = undefined;
      });
    }
    if (bankBranchCode) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bank-branch-codes/${bankBranchCode.id}`,
      }).then(() => {
        bankBranchCode = undefined;
      });
    }
    if (partyRelationType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-relation-types/${partyRelationType.id}`,
      }).then(() => {
        partyRelationType = undefined;
      });
    }
  });

  it('RelatedPartyRelationships menu should load RelatedPartyRelationships page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('related-party-relationship');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RelatedPartyRelationship').should('exist');
    cy.url().should('match', relatedPartyRelationshipPageUrlPattern);
  });

  describe('RelatedPartyRelationship page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(relatedPartyRelationshipPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RelatedPartyRelationship page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/related-party-relationship/new$'));
        cy.getEntityCreateUpdateHeading('RelatedPartyRelationship');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', relatedPartyRelationshipPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/related-party-relationships',

          body: {
            ...relatedPartyRelationshipSample,
            bankCode: institutionCode,
            branchId: bankBranchCode,
            relationshipType: partyRelationType,
          },
        }).then(({ body }) => {
          relatedPartyRelationship = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/related-party-relationships+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [relatedPartyRelationship],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(relatedPartyRelationshipPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RelatedPartyRelationship page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('relatedPartyRelationship');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', relatedPartyRelationshipPageUrlPattern);
      });

      it('edit button click should load edit RelatedPartyRelationship page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RelatedPartyRelationship');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', relatedPartyRelationshipPageUrlPattern);
      });

      it('last delete button click should delete instance of RelatedPartyRelationship', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('relatedPartyRelationship').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', relatedPartyRelationshipPageUrlPattern);

        relatedPartyRelationship = undefined;
      });
    });
  });

  describe('new RelatedPartyRelationship page', () => {
    beforeEach(() => {
      cy.visit(`${relatedPartyRelationshipPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('RelatedPartyRelationship');
    });

    it('should create an instance of RelatedPartyRelationship', () => {
      cy.get(`[data-cy="reportingDate"]`).type('2023-10-04').should('have.value', '2023-10-04');

      cy.get(`[data-cy="customerId"]`).type('Rupee Plastic optical').should('have.value', 'Rupee Plastic optical');

      cy.get(`[data-cy="relatedPartyId"]`).type('real-time Australian').should('have.value', 'real-time Australian');

      cy.get(`[data-cy="bankCode"]`).select(1);
      cy.get(`[data-cy="branchId"]`).select(1);
      cy.get(`[data-cy="relationshipType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        relatedPartyRelationship = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', relatedPartyRelationshipPageUrlPattern);
    });
  });
});
