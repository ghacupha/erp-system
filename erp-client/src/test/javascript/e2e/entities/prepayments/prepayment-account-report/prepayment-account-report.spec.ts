import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { PrepaymentAccountReportComponentsPage } from './prepayment-account-report.page-object';

const expect = chai.expect;

describe('PrepaymentAccountReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let prepaymentAccountReportComponentsPage: PrepaymentAccountReportComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load PrepaymentAccountReports', async () => {
    await navBarPage.goToEntity('prepayment-account-report');
    prepaymentAccountReportComponentsPage = new PrepaymentAccountReportComponentsPage();
    await browser.wait(ec.visibilityOf(prepaymentAccountReportComponentsPage.title), 5000);
    expect(await prepaymentAccountReportComponentsPage.getTitle()).to.eq('Prepayment Account Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(prepaymentAccountReportComponentsPage.entities),
        ec.visibilityOf(prepaymentAccountReportComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
