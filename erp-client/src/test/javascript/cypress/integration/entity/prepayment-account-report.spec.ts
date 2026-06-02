import { entityItemSelector } from '../../support/commands';
import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('PrepaymentAccountReport e2e test', () => {
  const prepaymentAccountReportPageUrl = '/prepayment-account-report';
  const prepaymentAccountReportPageUrlPattern = new RegExp('/prepayment-account-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const prepaymentAccountReportSample = {};

  let prepaymentAccountReport: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/prepayment-account-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/prepayment-account-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/prepayment-account-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (prepaymentAccountReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/prepayment-account-reports/${prepaymentAccountReport.id}`,
      }).then(() => {
        prepaymentAccountReport = undefined;
      });
    }
  });

  it('PrepaymentAccountReports menu should load PrepaymentAccountReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('prepayment-account-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PrepaymentAccountReport').should('exist');
    cy.url().should('match', prepaymentAccountReportPageUrlPattern);
  });

  describe('PrepaymentAccountReport page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(prepaymentAccountReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details PrepaymentAccountReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('prepaymentAccountReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prepaymentAccountReportPageUrlPattern);
      });
    });
  });
});
