import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { RouAccountBalanceReportItemComponentsPage } from './rou-account-balance-report-item.page-object';

const expect = chai.expect;

describe('RouAccountBalanceReportItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouAccountBalanceReportItemComponentsPage: RouAccountBalanceReportItemComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load RouAccountBalanceReportItems', async () => {
    await navBarPage.goToEntity('rou-account-balance-report-item');
    rouAccountBalanceReportItemComponentsPage = new RouAccountBalanceReportItemComponentsPage();
    await browser.wait(ec.visibilityOf(rouAccountBalanceReportItemComponentsPage.title), 5000);
    expect(await rouAccountBalanceReportItemComponentsPage.getTitle()).to.eq('Rou Account Balance Report Items');
    await browser.wait(
      ec.or(
        ec.visibilityOf(rouAccountBalanceReportItemComponentsPage.entities),
        ec.visibilityOf(rouAccountBalanceReportItemComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
