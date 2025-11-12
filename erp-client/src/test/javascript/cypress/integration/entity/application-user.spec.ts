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

describe('ApplicationUser e2e test', () => {
  const applicationUserPageUrl = '/application-user';
  const applicationUserPageUrlPattern = new RegExp('/application-user(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const applicationUserSample = { designation: '3f6df420-3e53-433d-95ab-d86de399bfaa', applicationIdentity: 'Som Buckinghamshire neural' };

  let applicationUser: any;
  //let dealer: any;
  //let securityClearance: any;
  //let user: any;

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
      url: '/api/dealers',
      body: {"dealerName":"Berkshire demand-driven Sports","taxNumber":"matrix Jewelery","identificationDocumentNumber":"wireless Vatu PCI","organizationName":"Mouse","department":"ivory Small convergence","position":"Papua withdrawal","postalAddress":"Berkshire world-class Berkshire","physicalAddress":"maximize Mexico Minnesota","accountName":"Credit Card Account","accountNumber":"Uzbekistan Buckinghamshire dot-com","bankersName":"cross-media payment","bankersBranch":"Refined","bankersSwiftCode":"Cambridgeshire Soap digital","fileUploadToken":"Intelligent","compilationToken":"indexing","remarks":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","otherNames":"Metal"},
    }).then(({ body }) => {
      dealer = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/security-clearances',
      body: {"clearanceLevel":"Licensed Account Credit"},
    }).then(({ body }) => {
      securityClearance = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"bandwidth-monitored red core","firstName":"Molly","lastName":"Becker"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/application-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/application-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/application-users/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/dealers', {
      statusCode: 200,
      body: [dealer],
    });

    cy.intercept('GET', '/api/security-clearances', {
      statusCode: 200,
      body: [securityClearance],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

    cy.intercept('GET', '/api/universally-unique-mappings', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (applicationUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/application-users/${applicationUser.id}`,
      }).then(() => {
        applicationUser = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (dealer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dealers/${dealer.id}`,
      }).then(() => {
        dealer = undefined;
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
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('ApplicationUsers menu should load ApplicationUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('application-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ApplicationUser').should('exist');
    cy.url().should('match', applicationUserPageUrlPattern);
  });

  describe('ApplicationUser page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(applicationUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ApplicationUser page', () => {
        cy.get(entityCreateButtonSelector).click({ force: true });
        cy.url().should('match', new RegExp('/application-user/new$'));
        cy.getEntityCreateUpdateHeading('ApplicationUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationUserPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/application-users',
  
          body: {
            ...applicationUserSample,
            organization: dealer,
            department: dealer,
            securityClearance: securityClearance,
            systemIdentity: user,
            dealerIdentity: dealer,
          },
        }).then(({ body }) => {
          applicationUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/application-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [applicationUser],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(applicationUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(applicationUserPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ApplicationUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('applicationUser');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationUserPageUrlPattern);
      });

      it('edit button click should load edit ApplicationUser page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ApplicationUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationUserPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of ApplicationUser', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('applicationUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationUserPageUrlPattern);

        applicationUser = undefined;
      });
    });
  });

  describe('new ApplicationUser page', () => {
    beforeEach(() => {
      cy.visit(`${applicationUserPageUrl}`);
      cy.get(entityCreateButtonSelector).click({ force: true });
      cy.getEntityCreateUpdateHeading('ApplicationUser');
    });

    it.skip('should create an instance of ApplicationUser', () => {
      cy.get(`[data-cy="designation"]`)
        .type('e7662229-4b8f-4a86-9d22-793d97b57df9')
        .invoke('val')
        .should('match', new RegExp('e7662229-4b8f-4a86-9d22-793d97b57df9'));

      cy.get(`[data-cy="applicationIdentity"]`).type('PCI').should('have.value', 'PCI');

      cy.get(`[data-cy="organization"]`).select(1);
      cy.get(`[data-cy="department"]`).select(1);
      cy.get(`[data-cy="securityClearance"]`).select(1);
      cy.get(`[data-cy="systemIdentity"]`).select(1);
      cy.get(`[data-cy="dealerIdentity"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        applicationUser = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', applicationUserPageUrlPattern);
    });
  });
});
