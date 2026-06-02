import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { LeaseLiabilityScheduleReportItemComponentsPage } from './lease-liability-schedule-report-item.page-object';

const expect = chai.expect;

describe('LeaseLiabilityScheduleReportItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseLiabilityScheduleReportItemComponentsPage: LeaseLiabilityScheduleReportItemComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseLiabilityScheduleReportItems', async () => {
    await navBarPage.goToEntity('lease-liability-schedule-report-item');
    leaseLiabilityScheduleReportItemComponentsPage = new LeaseLiabilityScheduleReportItemComponentsPage();
    await browser.wait(ec.visibilityOf(leaseLiabilityScheduleReportItemComponentsPage.title), 5000);
    expect(await leaseLiabilityScheduleReportItemComponentsPage.getTitle()).to.eq('Lease Liability Schedule Report Items');
    await browser.wait(
      ec.or(
        ec.visibilityOf(leaseLiabilityScheduleReportItemComponentsPage.entities),
        ec.visibilityOf(leaseLiabilityScheduleReportItemComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
