import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { LeaseLiabilityPostingReportItemComponentsPage } from './lease-liability-posting-report-item.page-object';

const expect = chai.expect;

describe('LeaseLiabilityPostingReportItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseLiabilityPostingReportItemComponentsPage: LeaseLiabilityPostingReportItemComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseLiabilityPostingReportItems', async () => {
    await navBarPage.goToEntity('lease-liability-posting-report-item');
    leaseLiabilityPostingReportItemComponentsPage = new LeaseLiabilityPostingReportItemComponentsPage();
    await browser.wait(ec.visibilityOf(leaseLiabilityPostingReportItemComponentsPage.title), 5000);
    expect(await leaseLiabilityPostingReportItemComponentsPage.getTitle()).to.eq('Lease Liability Posting Report Items');
    await browser.wait(
      ec.or(
        ec.visibilityOf(leaseLiabilityPostingReportItemComponentsPage.entities),
        ec.visibilityOf(leaseLiabilityPostingReportItemComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
