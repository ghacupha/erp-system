import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { PrepaymentReportComponentsPage } from './prepayment-report.page-object';

const expect = chai.expect;

describe('PrepaymentReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let prepaymentReportComponentsPage: PrepaymentReportComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PrepaymentReports', async () => {
    await navBarPage.goToEntity('prepayment-report');
    prepaymentReportComponentsPage = new PrepaymentReportComponentsPage();
    await browser.wait(ec.visibilityOf(prepaymentReportComponentsPage.title), 5000);
    expect(await prepaymentReportComponentsPage.getTitle()).to.eq('Prepayment Reports');
    await browser.wait(
      ec.or(ec.visibilityOf(prepaymentReportComponentsPage.entities), ec.visibilityOf(prepaymentReportComponentsPage.noResult)),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
