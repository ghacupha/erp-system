import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { RouDepreciationPostingReportItemComponentsPage } from './rou-depreciation-posting-report-item.page-object';

const expect = chai.expect;

describe('RouDepreciationPostingReportItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouDepreciationPostingReportItemComponentsPage: RouDepreciationPostingReportItemComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load RouDepreciationPostingReportItems', async () => {
    await navBarPage.goToEntity('rou-depreciation-posting-report-item');
    rouDepreciationPostingReportItemComponentsPage = new RouDepreciationPostingReportItemComponentsPage();
    await browser.wait(ec.visibilityOf(rouDepreciationPostingReportItemComponentsPage.title), 5000);
    expect(await rouDepreciationPostingReportItemComponentsPage.getTitle()).to.eq('Rou Depreciation Posting Report Items');
    await browser.wait(
      ec.or(
        ec.visibilityOf(rouDepreciationPostingReportItemComponentsPage.entities),
        ec.visibilityOf(rouDepreciationPostingReportItemComponentsPage.noResult)
      ),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
