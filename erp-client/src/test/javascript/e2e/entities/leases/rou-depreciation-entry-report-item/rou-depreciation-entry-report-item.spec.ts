import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { RouDepreciationEntryReportItemComponentsPage } from './rou-depreciation-entry-report-item.page-object';

const expect = chai.expect;

describe('RouDepreciationEntryReportItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouDepreciationEntryReportItemComponentsPage: RouDepreciationEntryReportItemComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load RouDepreciationEntryReportItems', async () => {
    await navBarPage.goToEntity('rou-depreciation-entry-report-item');
    rouDepreciationEntryReportItemComponentsPage = new RouDepreciationEntryReportItemComponentsPage();
    await browser.wait(ec.visibilityOf(rouDepreciationEntryReportItemComponentsPage.title), 5000);
    expect(await rouDepreciationEntryReportItemComponentsPage.getTitle()).to.eq('Rou Depreciation Entry Report Items');
    await browser.wait(
      ec.or(
        ec.visibilityOf(rouDepreciationEntryReportItemComponentsPage.entities),
        ec.visibilityOf(rouDepreciationEntryReportItemComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
