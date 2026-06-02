import { entityItemSelector } from '../../support/commands';
import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('TransactionAccountReportItem e2e test', () => {
  const transactionAccountReportItemPageUrl = '/transaction-account-report-item';
  const transactionAccountReportItemPageUrlPattern = new RegExp('/transaction-account-report-item(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const transactionAccountReportItemSample = {};

  let transactionAccountReportItem: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/transaction-account-report-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transaction-account-report-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transaction-account-report-items/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (transactionAccountReportItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transaction-account-report-items/${transactionAccountReportItem.id}`,
      }).then(() => {
        transactionAccountReportItem = undefined;
      });
    }
  });

  it('TransactionAccountReportItems menu should load TransactionAccountReportItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transaction-account-report-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TransactionAccountReportItem').should('exist');
    cy.url().should('match', transactionAccountReportItemPageUrlPattern);
  });

  describe('TransactionAccountReportItem page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(transactionAccountReportItemPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TransactionAccountReportItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transactionAccountReportItem');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transactionAccountReportItemPageUrlPattern);
      });
    });
  });
});
