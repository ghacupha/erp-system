import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { MonthlyPrepaymentOutstandingReportItemComponentsPage } from './monthly-prepayment-outstanding-report-item.page-object';

const expect = chai.expect;

describe('MonthlyPrepaymentOutstandingReportItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let monthlyPrepaymentOutstandingReportItemComponentsPage: MonthlyPrepaymentOutstandingReportItemComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load MonthlyPrepaymentOutstandingReportItems', async () => {
    await navBarPage.goToEntity('monthly-prepayment-outstanding-report-item');
    monthlyPrepaymentOutstandingReportItemComponentsPage = new MonthlyPrepaymentOutstandingReportItemComponentsPage();
    await browser.wait(ec.visibilityOf(monthlyPrepaymentOutstandingReportItemComponentsPage.title), 5000);
    expect(await monthlyPrepaymentOutstandingReportItemComponentsPage.getTitle()).to.eq('Monthly Prepayment Outstanding Report Items');
    await browser.wait(
      ec.or(
        ec.visibilityOf(monthlyPrepaymentOutstandingReportItemComponentsPage.entities),
        ec.visibilityOf(monthlyPrepaymentOutstandingReportItemComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
