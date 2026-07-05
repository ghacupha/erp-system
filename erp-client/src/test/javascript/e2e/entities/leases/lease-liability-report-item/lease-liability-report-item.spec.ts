import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { LeaseLiabilityReportItemComponentsPage } from './lease-liability-report-item.page-object';

const expect = chai.expect;

describe('LeaseLiabilityReportItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseLiabilityReportItemComponentsPage: LeaseLiabilityReportItemComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseLiabilityReportItems', async () => {
    await navBarPage.goToEntity('lease-liability-report-item');
    leaseLiabilityReportItemComponentsPage = new LeaseLiabilityReportItemComponentsPage();
    await browser.wait(ec.visibilityOf(leaseLiabilityReportItemComponentsPage.title), 5000);
    expect(await leaseLiabilityReportItemComponentsPage.getTitle()).to.eq('Lease Liability Report Items');
    await browser.wait(
      ec.or(
        ec.visibilityOf(leaseLiabilityReportItemComponentsPage.entities),
        ec.visibilityOf(leaseLiabilityReportItemComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
