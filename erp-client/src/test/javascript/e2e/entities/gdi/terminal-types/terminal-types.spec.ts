import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { TerminalTypesComponentsPage, TerminalTypesDeleteDialog, TerminalTypesUpdatePage } from './terminal-types.page-object';

const expect = chai.expect;

describe('TerminalTypes e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let terminalTypesComponentsPage: TerminalTypesComponentsPage;
  let terminalTypesUpdatePage: TerminalTypesUpdatePage;
  let terminalTypesDeleteDialog: TerminalTypesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TerminalTypes', async () => {
    await navBarPage.goToEntity('terminal-types');
    terminalTypesComponentsPage = new TerminalTypesComponentsPage();
    await browser.wait(ec.visibilityOf(terminalTypesComponentsPage.title), 5000);
    expect(await terminalTypesComponentsPage.getTitle()).to.eq('Terminal Types');
    await browser.wait(
      ec.or(ec.visibilityOf(terminalTypesComponentsPage.entities), ec.visibilityOf(terminalTypesComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TerminalTypes page', async () => {
    await terminalTypesComponentsPage.clickOnCreateButton();
    terminalTypesUpdatePage = new TerminalTypesUpdatePage();
    expect(await terminalTypesUpdatePage.getPageTitle()).to.eq('Create or edit a Terminal Types');
    await terminalTypesUpdatePage.cancel();
  });

  it('should create and save TerminalTypes', async () => {
    const nbButtonsBeforeCreate = await terminalTypesComponentsPage.countDeleteButtons();

    await terminalTypesComponentsPage.clickOnCreateButton();

    await promise.all([
      terminalTypesUpdatePage.setTxnTerminalTypeCodeInput('txnTerminalTypeCode'),
      terminalTypesUpdatePage.setTxnChannelTypeInput('txnChannelType'),
      terminalTypesUpdatePage.setTxnChannelTypeDetailsInput('txnChannelTypeDetails'),
    ]);

    await terminalTypesUpdatePage.save();
    expect(await terminalTypesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await terminalTypesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last TerminalTypes', async () => {
    const nbButtonsBeforeDelete = await terminalTypesComponentsPage.countDeleteButtons();
    await terminalTypesComponentsPage.clickOnLastDeleteButton();

    terminalTypesDeleteDialog = new TerminalTypesDeleteDialog();
    expect(await terminalTypesDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Terminal Types?');
    await terminalTypesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(terminalTypesComponentsPage.title), 5000);

    expect(await terminalTypesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
