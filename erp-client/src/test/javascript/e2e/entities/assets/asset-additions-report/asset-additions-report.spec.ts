import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AssetAdditionsReportComponentsPage,
  AssetAdditionsReportDeleteDialog,
  AssetAdditionsReportUpdatePage,
} from './asset-additions-report.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('AssetAdditionsReport e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let assetAdditionsReportComponentsPage: AssetAdditionsReportComponentsPage;
  let assetAdditionsReportUpdatePage: AssetAdditionsReportUpdatePage;
  let assetAdditionsReportDeleteDialog: AssetAdditionsReportDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AssetAdditionsReports', async () => {
    await navBarPage.goToEntity('asset-additions-report');
    assetAdditionsReportComponentsPage = new AssetAdditionsReportComponentsPage();
    await browser.wait(ec.visibilityOf(assetAdditionsReportComponentsPage.title), 5000);
    expect(await assetAdditionsReportComponentsPage.getTitle()).to.eq('Asset Additions Reports');
    await browser.wait(
      ec.or(ec.visibilityOf(assetAdditionsReportComponentsPage.entities), ec.visibilityOf(assetAdditionsReportComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AssetAdditionsReport page', async () => {
    await assetAdditionsReportComponentsPage.clickOnCreateButton();
    assetAdditionsReportUpdatePage = new AssetAdditionsReportUpdatePage();
    expect(await assetAdditionsReportUpdatePage.getPageTitle()).to.eq('Create or edit a Asset Additions Report');
    await assetAdditionsReportUpdatePage.cancel();
  });

  it('should create and save AssetAdditionsReports', async () => {
    const nbButtonsBeforeCreate = await assetAdditionsReportComponentsPage.countDeleteButtons();

    await assetAdditionsReportComponentsPage.clickOnCreateButton();

    await promise.all([
      assetAdditionsReportUpdatePage.setTimeOfRequestInput('2000-12-31'),
      assetAdditionsReportUpdatePage.setReportStartDateInput('2000-12-31'),
      assetAdditionsReportUpdatePage.setReportEndDateInput('2000-12-31'),
      assetAdditionsReportUpdatePage.setRequestIdInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      assetAdditionsReportUpdatePage.getTamperedInput().click(),
      assetAdditionsReportUpdatePage.setReportParametersInput('reportParameters'),
      assetAdditionsReportUpdatePage.setReportFileInput(absolutePath),
      assetAdditionsReportUpdatePage.requestedBySelectLastOption(),
    ]);

    await assetAdditionsReportUpdatePage.save();
    expect(await assetAdditionsReportUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await assetAdditionsReportComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last AssetAdditionsReport', async () => {
    const nbButtonsBeforeDelete = await assetAdditionsReportComponentsPage.countDeleteButtons();
    await assetAdditionsReportComponentsPage.clickOnLastDeleteButton();

    assetAdditionsReportDeleteDialog = new AssetAdditionsReportDeleteDialog();
    expect(await assetAdditionsReportDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Asset Additions Report?');
    await assetAdditionsReportDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(assetAdditionsReportComponentsPage.title), 5000);

    expect(await assetAdditionsReportComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
