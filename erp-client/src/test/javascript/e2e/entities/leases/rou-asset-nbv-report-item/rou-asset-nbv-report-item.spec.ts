import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { RouAssetNBVReportItemComponentsPage } from './rou-asset-nbv-report-item.page-object';

const expect = chai.expect;

describe('RouAssetNBVReportItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rouAssetNBVReportItemComponentsPage: RouAssetNBVReportItemComponentsPage;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load RouAssetNBVReportItems', async () => {
    await navBarPage.goToEntity('rou-asset-nbv-report-item');
    rouAssetNBVReportItemComponentsPage = new RouAssetNBVReportItemComponentsPage();
    await browser.wait(ec.visibilityOf(rouAssetNBVReportItemComponentsPage.title), 5000);
    expect(await rouAssetNBVReportItemComponentsPage.getTitle()).to.eq('Rou Asset NBV Report Items');
    await browser.wait(
      ec.or(ec.visibilityOf(rouAssetNBVReportItemComponentsPage.entities), ec.visibilityOf(rouAssetNBVReportItemComponentsPage.noResult)),
      1000
    );
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
