import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { LeaseLiabilityByAccountReportItemComponentsPage } from './lease-liability-by-account-report-item.page-object';

const expect = chai.expect;

describe('LeaseLiabilityByAccountReportItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseLiabilityByAccountReportItemComponentsPage: LeaseLiabilityByAccountReportItemComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseLiabilityByAccountReportItems', async () => {
    await navBarPage.goToEntity('lease-liability-by-account-report-item');
    leaseLiabilityByAccountReportItemComponentsPage = new LeaseLiabilityByAccountReportItemComponentsPage();
    await browser.wait(ec.visibilityOf(leaseLiabilityByAccountReportItemComponentsPage.title), 5000);
    expect(await leaseLiabilityByAccountReportItemComponentsPage.getTitle()).to.eq('Lease Liability By Account Report Items');
    await browser.wait(
      ec.or(
        ec.visibilityOf(leaseLiabilityByAccountReportItemComponentsPage.entities),
        ec.visibilityOf(leaseLiabilityByAccountReportItemComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
