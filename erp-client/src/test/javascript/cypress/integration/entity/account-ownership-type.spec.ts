import { entityItemSelector } from '../../support/commands';
import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('AccountOwnershipType e2e test', () => {
  const accountOwnershipTypePageUrl = '/account-ownership-type';
  const accountOwnershipTypePageUrlPattern = new RegExp('/account-ownership-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const accountOwnershipTypeSample = {
    accountOwnershipTypeCode: 'withdrawal De-engineered payment',
    accountOwnershipType: 'Trail Outdoors Fantastic',
  };

  let accountOwnershipType: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/account-ownership-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/account-ownership-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/account-ownership-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (accountOwnershipType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/account-ownership-types/${accountOwnershipType.id}`,
      }).then(() => {
        accountOwnershipType = undefined;
      });
    }
  });

  it('AccountOwnershipTypes menu should load AccountOwnershipTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('account-ownership-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AccountOwnershipType').should('exist');
    cy.url().should('match', accountOwnershipTypePageUrlPattern);
  });

  describe('AccountOwnershipType page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(accountOwnershipTypePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AccountOwnershipType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('accountOwnershipType');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', accountOwnershipTypePageUrlPattern);
      });
    });
  });
});
