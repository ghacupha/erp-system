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

describe('BusinessDocument e2e test', () => {
  const businessDocumentPageUrl = '/business-document';
  const businessDocumentPageUrlPattern = new RegExp('/business-document(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const businessDocumentSample = {
    documentTitle: 'capacitor',
    documentSerial: '5c7cf121-6ebf-4920-8c08-008e7261e60c',
    attachmentFilePath: 'multi-byte Island International',
    documentFile: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    documentFileContentType: 'unknown',
    documentFileChecksum: 'backing of',
  };

  let businessDocument: any;
  //let applicationUser: any;
  //let dealer: any;
  //let algorithm: any;
  //let securityClearance: any;

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
      url: '/api/application-users',
      body: {"designation":"517193bc-93e7-4edc-9b0a-44b29aaee8ad","applicationIdentity":"scale SQL"},
    }).then(({ body }) => {
      applicationUser = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/dealers',
      body: {"dealerName":"Data Product Beauty","taxNumber":"bandwidth yellow deposit","identificationDocumentNumber":"Plastic JSON","organizationName":"Fish Credit","department":"JBOD deposit Wooden","position":"Program","postalAddress":"Direct leverage","physicalAddress":"Future dynamic Facilitator","accountName":"Home Loan Account","accountNumber":"invoice Australia Berkshire","bankersName":"bypassing","bankersBranch":"Outdoors Market mobile","bankersSwiftCode":"Buckinghamshire calculating","fileUploadToken":"Exclusive Assurance","compilationToken":"online digital Unbranded","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","otherNames":"Borders PCI"},
    }).then(({ body }) => {
      dealer = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/algorithms',
      body: {"name":"Metal Loan Sleek"},
    }).then(({ body }) => {
      algorithm = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/security-clearances',
      body: {"clearanceLevel":"Bedfordshire"},
    }).then(({ body }) => {
      securityClearance = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/business-documents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/business-documents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/business-documents/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/application-users', {
      statusCode: 200,
      body: [applicationUser],
    });

    cy.intercept('GET', '/api/dealers', {
      statusCode: 200,
      body: [dealer],
    });

    cy.intercept('GET', '/api/universally-unique-mappings', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/placeholders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/algorithms', {
      statusCode: 200,
      body: [algorithm],
    });

    cy.intercept('GET', '/api/security-clearances', {
      statusCode: 200,
      body: [securityClearance],
    });

  });
   */

  afterEach(() => {
    if (businessDocument) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/business-documents/${businessDocument.id}`,
      }).then(() => {
        businessDocument = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (applicationUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/application-users/${applicationUser.id}`,
      }).then(() => {
        applicationUser = undefined;
      });
    }
    if (dealer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dealers/${dealer.id}`,
      }).then(() => {
        dealer = undefined;
      });
    }
    if (algorithm) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/algorithms/${algorithm.id}`,
      }).then(() => {
        algorithm = undefined;
      });
    }
    if (securityClearance) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/security-clearances/${securityClearance.id}`,
      }).then(() => {
        securityClearance = undefined;
      });
    }
  });
   */

  it('BusinessDocuments menu should load BusinessDocuments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('business-document');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BusinessDocument').should('exist');
    cy.url().should('match', businessDocumentPageUrlPattern);
  });

  describe('BusinessDocument page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(businessDocumentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BusinessDocument page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/business-document/new$'));
        cy.getEntityCreateUpdateHeading('BusinessDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', businessDocumentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/business-documents',
  
          body: {
            ...businessDocumentSample,
            createdBy: applicationUser,
            originatingDepartment: dealer,
            fileChecksumAlgorithm: algorithm,
            securityClearance: securityClearance,
          },
        }).then(({ body }) => {
          businessDocument = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/business-documents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [businessDocument],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(businessDocumentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(businessDocumentPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details BusinessDocument page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('businessDocument');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', businessDocumentPageUrlPattern);
      });

      it('edit button click should load edit BusinessDocument page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BusinessDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', businessDocumentPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of BusinessDocument', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('businessDocument').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', businessDocumentPageUrlPattern);

        businessDocument = undefined;
      });
    });
  });

  describe('new BusinessDocument page', () => {
    beforeEach(() => {
      cy.visit(`${businessDocumentPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('BusinessDocument');
    });

    it.skip('should create an instance of BusinessDocument', () => {
      cy.get(`[data-cy="documentTitle"]`).type('Gloves').should('have.value', 'Gloves');

      cy.get(`[data-cy="description"]`).type('Dinar Franc Internal').should('have.value', 'Dinar Franc Internal');

      cy.get(`[data-cy="documentSerial"]`)
        .type('0f8b17ac-d490-4f34-8b78-9b6f595e6921')
        .invoke('val')
        .should('match', new RegExp('0f8b17ac-d490-4f34-8b78-9b6f595e6921'));

      cy.get(`[data-cy="lastModified"]`).type('2022-10-18T15:11').should('have.value', '2022-10-18T15:11');

      cy.get(`[data-cy="attachmentFilePath"]`).type('background').should('have.value', 'background');

      cy.setFieldImageAsBytesOfEntity('documentFile', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="fileTampered"]`).should('not.be.checked');
      cy.get(`[data-cy="fileTampered"]`).click().should('be.checked');

      cy.get(`[data-cy="documentFileChecksum"]`).type('Montana').should('have.value', 'Montana');

      cy.get(`[data-cy="createdBy"]`).select(1);
      cy.get(`[data-cy="originatingDepartment"]`).select(1);
      cy.get(`[data-cy="fileChecksumAlgorithm"]`).select(1);
      cy.get(`[data-cy="securityClearance"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        businessDocument = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', businessDocumentPageUrlPattern);
    });
  });
});
