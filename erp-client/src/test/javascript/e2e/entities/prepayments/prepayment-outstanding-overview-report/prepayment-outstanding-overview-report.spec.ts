import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { PrepaymentOutstandingOverviewReportComponentsPage } from './prepayment-outstanding-overview-report.page-object';

const expect = chai.expect;

describe('PrepaymentOutstandingOverviewReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let prepaymentOutstandingOverviewReportComponentsPage: PrepaymentOutstandingOverviewReportComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PrepaymentOutstandingOverviewReports', async () => {
    await navBarPage.goToEntity('prepayment-outstanding-overview-report');
    prepaymentOutstandingOverviewReportComponentsPage = new PrepaymentOutstandingOverviewReportComponentsPage();
    await browser.wait(ec.visibilityOf(prepaymentOutstandingOverviewReportComponentsPage.title), 5000);
    expect(await prepaymentOutstandingOverviewReportComponentsPage.getTitle()).to.eq('Prepayment Outstanding Overview Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(prepaymentOutstandingOverviewReportComponentsPage.entities),
        ec.visibilityOf(prepaymentOutstandingOverviewReportComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
