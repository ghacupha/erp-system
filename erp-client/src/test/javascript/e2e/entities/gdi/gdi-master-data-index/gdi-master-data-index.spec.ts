import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  GdiMasterDataIndexComponentsPage,
  GdiMasterDataIndexDeleteDialog,
  GdiMasterDataIndexUpdatePage,
} from './gdi-master-data-index.page-object';

const expect = chai.expect;

describe('GdiMasterDataIndex e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let gdiMasterDataIndexComponentsPage: GdiMasterDataIndexComponentsPage;
  let gdiMasterDataIndexUpdatePage: GdiMasterDataIndexUpdatePage;
  let gdiMasterDataIndexDeleteDialog: GdiMasterDataIndexDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load GdiMasterDataIndices', async () => {
    await navBarPage.goToEntity('gdi-master-data-index');
    gdiMasterDataIndexComponentsPage = new GdiMasterDataIndexComponentsPage();
    await browser.wait(ec.visibilityOf(gdiMasterDataIndexComponentsPage.title), 5000);
    expect(await gdiMasterDataIndexComponentsPage.getTitle()).to.eq('Gdi Master Data Indices');
    await browser.wait(
      ec.or(ec.visibilityOf(gdiMasterDataIndexComponentsPage.entities), ec.visibilityOf(gdiMasterDataIndexComponentsPage.noResult)),
      1000
    );
  });

  it('should load create GdiMasterDataIndex page', async () => {
    await gdiMasterDataIndexComponentsPage.clickOnCreateButton();
    gdiMasterDataIndexUpdatePage = new GdiMasterDataIndexUpdatePage();
    expect(await gdiMasterDataIndexUpdatePage.getPageTitle()).to.eq('Create or edit a Gdi Master Data Index');
    await gdiMasterDataIndexUpdatePage.cancel();
  });

  it('should create and save GdiMasterDataIndices', async () => {
    const nbButtonsBeforeCreate = await gdiMasterDataIndexComponentsPage.countDeleteButtons();

    await gdiMasterDataIndexComponentsPage.clickOnCreateButton();

    await promise.all([
      gdiMasterDataIndexUpdatePage.setEntityNameInput('entityName'),
      gdiMasterDataIndexUpdatePage.setDatabaseNameInput('databaseName'),
      gdiMasterDataIndexUpdatePage.setBusinessDescriptionInput('businessDescription'),
    ]);

    await gdiMasterDataIndexUpdatePage.save();
    expect(await gdiMasterDataIndexUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await gdiMasterDataIndexComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last GdiMasterDataIndex', async () => {
    const nbButtonsBeforeDelete = await gdiMasterDataIndexComponentsPage.countDeleteButtons();
    await gdiMasterDataIndexComponentsPage.clickOnLastDeleteButton();

    gdiMasterDataIndexDeleteDialog = new GdiMasterDataIndexDeleteDialog();
    expect(await gdiMasterDataIndexDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Gdi Master Data Index?');
    await gdiMasterDataIndexDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(gdiMasterDataIndexComponentsPage.title), 5000);

    expect(await gdiMasterDataIndexComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
