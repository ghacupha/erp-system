import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { OutletStatusComponentsPage, OutletStatusDeleteDialog, OutletStatusUpdatePage } from './outlet-status.page-object';

const expect = chai.expect;

describe('OutletStatus e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let outletStatusComponentsPage: OutletStatusComponentsPage;
  let outletStatusUpdatePage: OutletStatusUpdatePage;
  let outletStatusDeleteDialog: OutletStatusDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load OutletStatuses', async () => {
    await navBarPage.goToEntity('outlet-status');
    outletStatusComponentsPage = new OutletStatusComponentsPage();
    await browser.wait(ec.visibilityOf(outletStatusComponentsPage.title), 5000);
    expect(await outletStatusComponentsPage.getTitle()).to.eq('Outlet Statuses');
    await browser.wait(
      ec.or(ec.visibilityOf(outletStatusComponentsPage.entities), ec.visibilityOf(outletStatusComponentsPage.noResult)),
      1000
    );
  });

  it('should load create OutletStatus page', async () => {
    await outletStatusComponentsPage.clickOnCreateButton();
    outletStatusUpdatePage = new OutletStatusUpdatePage();
    expect(await outletStatusUpdatePage.getPageTitle()).to.eq('Create or edit a Outlet Status');
    await outletStatusUpdatePage.cancel();
  });

  it('should create and save OutletStatuses', async () => {
    const nbButtonsBeforeCreate = await outletStatusComponentsPage.countDeleteButtons();

    await outletStatusComponentsPage.clickOnCreateButton();

    await promise.all([
      outletStatusUpdatePage.setBranchStatusTypeCodeInput('branchStatusTypeCode'),
      outletStatusUpdatePage.branchStatusTypeSelectLastOption(),
      outletStatusUpdatePage.setBranchStatusTypeDescriptionInput('branchStatusTypeDescription'),
      // outletStatusUpdatePage.placeholderSelectLastOption(),
    ]);

    await outletStatusUpdatePage.save();
    expect(await outletStatusUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await outletStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last OutletStatus', async () => {
    const nbButtonsBeforeDelete = await outletStatusComponentsPage.countDeleteButtons();
    await outletStatusComponentsPage.clickOnLastDeleteButton();

    outletStatusDeleteDialog = new OutletStatusDeleteDialog();
    expect(await outletStatusDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Outlet Status?');
    await outletStatusDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(outletStatusComponentsPage.title), 5000);

    expect(await outletStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
