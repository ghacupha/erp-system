import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { AmortizationPostingReportComponentsPage } from './amortization-posting-report.page-object';

const expect = chai.expect;

describe('AmortizationPostingReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let amortizationPostingReportComponentsPage: AmortizationPostingReportComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AmortizationPostingReports', async () => {
    await navBarPage.goToEntity('amortization-posting-report');
    amortizationPostingReportComponentsPage = new AmortizationPostingReportComponentsPage();
    await browser.wait(ec.visibilityOf(amortizationPostingReportComponentsPage.title), 5000);
    expect(await amortizationPostingReportComponentsPage.getTitle()).to.eq('Amortization Posting Reports');
    await browser.wait(
      ec.or(
        ec.visibilityOf(amortizationPostingReportComponentsPage.entities),
        ec.visibilityOf(amortizationPostingReportComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
