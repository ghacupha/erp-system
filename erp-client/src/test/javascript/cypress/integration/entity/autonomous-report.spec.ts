import { entityItemSelector } from '../../support/commands';
import { entityTableSelector, entityDetailsButtonSelector, entityDetailsBackButtonSelector } from '../../support/entity';

describe('AutonomousReport e2e test', () => {
  const autonomousReportPageUrl = '/autonomous-report';
  const autonomousReportPageUrlPattern = new RegExp('/autonomous-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'admin';
  const password = Cypress.env('E2E_PASSWORD') ?? 'admin';
  const autonomousReportSample = { reportName: 'Integration Agent azure', createdAt: '2023-12-06T23:09:59.655Z' };

  let autonomousReport: any;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });
    cy.visit('');
    cy.login(username, password);
    cy.get(entityItemSelector).should('exist');
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/autonomous-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/autonomous-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/autonomous-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (autonomousReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/autonomous-reports/${autonomousReport.id}`,
      }).then(() => {
        autonomousReport = undefined;
      });
    }
  });

  it('AutonomousReports menu should load AutonomousReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('autonomous-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AutonomousReport').should('exist');
    cy.url().should('match', autonomousReportPageUrlPattern);
  });

  describe('AutonomousReport page', () => {
    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(autonomousReportPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AutonomousReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('autonomousReport');
        cy.get(entityDetailsBackButtonSelector).click({ force: true });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', autonomousReportPageUrlPattern);
      });
    });
  });
});
