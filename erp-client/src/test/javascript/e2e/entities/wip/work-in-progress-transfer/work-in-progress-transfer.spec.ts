import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  WorkInProgressTransferComponentsPage,
  WorkInProgressTransferDeleteDialog,
  WorkInProgressTransferUpdatePage,
} from './work-in-progress-transfer.page-object';

const expect = chai.expect;

describe('WorkInProgressTransfer e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let workInProgressTransferComponentsPage: WorkInProgressTransferComponentsPage;
  let workInProgressTransferUpdatePage: WorkInProgressTransferUpdatePage;
  let workInProgressTransferDeleteDialog: WorkInProgressTransferDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load WorkInProgressTransfers', async () => {
    await navBarPage.goToEntity('work-in-progress-transfer');
    workInProgressTransferComponentsPage = new WorkInProgressTransferComponentsPage();
    await browser.wait(ec.visibilityOf(workInProgressTransferComponentsPage.title), 5000);
    expect(await workInProgressTransferComponentsPage.getTitle()).to.eq('Work In Progress Transfers');
    await browser.wait(
      ec.or(ec.visibilityOf(workInProgressTransferComponentsPage.entities), ec.visibilityOf(workInProgressTransferComponentsPage.noResult)),
      1000
    );
  });

  it('should load create WorkInProgressTransfer page', async () => {
    await workInProgressTransferComponentsPage.clickOnCreateButton();
    workInProgressTransferUpdatePage = new WorkInProgressTransferUpdatePage();
    expect(await workInProgressTransferUpdatePage.getPageTitle()).to.eq('Create or edit a Work In Progress Transfer');
    await workInProgressTransferUpdatePage.cancel();
  });

  it('should create and save WorkInProgressTransfers', async () => {
    const nbButtonsBeforeCreate = await workInProgressTransferComponentsPage.countDeleteButtons();

    await workInProgressTransferComponentsPage.clickOnCreateButton();

    await promise.all([
      workInProgressTransferUpdatePage.setDescriptionInput('description'),
      workInProgressTransferUpdatePage.setTargetAssetNumberInput('targetAssetNumber'),
      workInProgressTransferUpdatePage.setTransferAmountInput('5'),
      workInProgressTransferUpdatePage.setTransferDateInput('2000-12-31'),
      workInProgressTransferUpdatePage.transferTypeSelectLastOption(),
      // workInProgressTransferUpdatePage.placeholderSelectLastOption(),
      // workInProgressTransferUpdatePage.businessDocumentSelectLastOption(),
      workInProgressTransferUpdatePage.assetCategorySelectLastOption(),
      workInProgressTransferUpdatePage.workInProgressRegistrationSelectLastOption(),
      workInProgressTransferUpdatePage.serviceOutletSelectLastOption(),
      workInProgressTransferUpdatePage.transferSettlementSelectLastOption(),
      workInProgressTransferUpdatePage.originalSettlementSelectLastOption(),
      workInProgressTransferUpdatePage.workProjectRegisterSelectLastOption(),
    ]);

    await workInProgressTransferUpdatePage.save();
    expect(await workInProgressTransferUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await workInProgressTransferComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last WorkInProgressTransfer', async () => {
    const nbButtonsBeforeDelete = await workInProgressTransferComponentsPage.countDeleteButtons();
    await workInProgressTransferComponentsPage.clickOnLastDeleteButton();

    workInProgressTransferDeleteDialog = new WorkInProgressTransferDeleteDialog();
    expect(await workInProgressTransferDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Work In Progress Transfer?'
    );
    await workInProgressTransferDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(workInProgressTransferComponentsPage.title), 5000);

    expect(await workInProgressTransferComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
