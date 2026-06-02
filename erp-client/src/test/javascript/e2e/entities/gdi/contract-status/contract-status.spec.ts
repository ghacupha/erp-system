import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { ContractStatusComponentsPage, ContractStatusDeleteDialog, ContractStatusUpdatePage } from './contract-status.page-object';

const expect = chai.expect;

describe('ContractStatus e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let contractStatusComponentsPage: ContractStatusComponentsPage;
  let contractStatusUpdatePage: ContractStatusUpdatePage;
  let contractStatusDeleteDialog: ContractStatusDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ContractStatuses', async () => {
    await navBarPage.goToEntity('contract-status');
    contractStatusComponentsPage = new ContractStatusComponentsPage();
    await browser.wait(ec.visibilityOf(contractStatusComponentsPage.title), 5000);
    expect(await contractStatusComponentsPage.getTitle()).to.eq('Contract Statuses');
    await browser.wait(
      ec.or(ec.visibilityOf(contractStatusComponentsPage.entities), ec.visibilityOf(contractStatusComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ContractStatus page', async () => {
    await contractStatusComponentsPage.clickOnCreateButton();
    contractStatusUpdatePage = new ContractStatusUpdatePage();
    expect(await contractStatusUpdatePage.getPageTitle()).to.eq('Create or edit a Contract Status');
    await contractStatusUpdatePage.cancel();
  });

  it('should create and save ContractStatuses', async () => {
    const nbButtonsBeforeCreate = await contractStatusComponentsPage.countDeleteButtons();

    await contractStatusComponentsPage.clickOnCreateButton();

    await promise.all([
      contractStatusUpdatePage.setContractStatusCodeInput('contractStatusCode'),
      contractStatusUpdatePage.setContractStatusTypeInput('contractStatusType'),
      contractStatusUpdatePage.setContractStatusTypeDescriptionInput('contractStatusTypeDescription'),
    ]);

    await contractStatusUpdatePage.save();
    expect(await contractStatusUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await contractStatusComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ContractStatus', async () => {
    const nbButtonsBeforeDelete = await contractStatusComponentsPage.countDeleteButtons();
    await contractStatusComponentsPage.clickOnLastDeleteButton();

    contractStatusDeleteDialog = new ContractStatusDeleteDialog();
    expect(await contractStatusDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Contract Status?');
    await contractStatusDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(contractStatusComponentsPage.title), 5000);

    expect(await contractStatusComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
