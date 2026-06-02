import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  TerminalFunctionsComponentsPage,
  TerminalFunctionsDeleteDialog,
  TerminalFunctionsUpdatePage,
} from './terminal-functions.page-object';

const expect = chai.expect;

describe('TerminalFunctions e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let terminalFunctionsComponentsPage: TerminalFunctionsComponentsPage;
  let terminalFunctionsUpdatePage: TerminalFunctionsUpdatePage;
  let terminalFunctionsDeleteDialog: TerminalFunctionsDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TerminalFunctions', async () => {
    await navBarPage.goToEntity('terminal-functions');
    terminalFunctionsComponentsPage = new TerminalFunctionsComponentsPage();
    await browser.wait(ec.visibilityOf(terminalFunctionsComponentsPage.title), 5000);
    expect(await terminalFunctionsComponentsPage.getTitle()).to.eq('Terminal Functions');
    await browser.wait(
      ec.or(ec.visibilityOf(terminalFunctionsComponentsPage.entities), ec.visibilityOf(terminalFunctionsComponentsPage.noResult)),
      1000
    );
  });

  it('should load create TerminalFunctions page', async () => {
    await terminalFunctionsComponentsPage.clickOnCreateButton();
    terminalFunctionsUpdatePage = new TerminalFunctionsUpdatePage();
    expect(await terminalFunctionsUpdatePage.getPageTitle()).to.eq('Create or edit a Terminal Functions');
    await terminalFunctionsUpdatePage.cancel();
  });

  it('should create and save TerminalFunctions', async () => {
    const nbButtonsBeforeCreate = await terminalFunctionsComponentsPage.countDeleteButtons();

    await terminalFunctionsComponentsPage.clickOnCreateButton();

    await promise.all([
      terminalFunctionsUpdatePage.setFunctionCodeInput('functionCode'),
      terminalFunctionsUpdatePage.setTerminalFunctionalityInput('terminalFunctionality'),
    ]);

    await terminalFunctionsUpdatePage.save();
    expect(await terminalFunctionsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await terminalFunctionsComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last TerminalFunctions', async () => {
    const nbButtonsBeforeDelete = await terminalFunctionsComponentsPage.countDeleteButtons();
    await terminalFunctionsComponentsPage.clickOnLastDeleteButton();

    terminalFunctionsDeleteDialog = new TerminalFunctionsDeleteDialog();
    expect(await terminalFunctionsDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Terminal Functions?');
    await terminalFunctionsDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(terminalFunctionsComponentsPage.title), 5000);

    expect(await terminalFunctionsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
