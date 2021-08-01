import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { MessageTokenComponentsPage, MessageTokenDeleteDialog, MessageTokenUpdatePage } from './message-token.page-object';

const expect = chai.expect;

describe('MessageToken e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let messageTokenComponentsPage: MessageTokenComponentsPage;
  let messageTokenUpdatePage: MessageTokenUpdatePage;
  let messageTokenDeleteDialog: MessageTokenDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load MessageTokens', async () => {
    await navBarPage.goToEntity('message-token');
    messageTokenComponentsPage = new MessageTokenComponentsPage();
    await browser.wait(ec.visibilityOf(messageTokenComponentsPage.title), 5000);
    expect(await messageTokenComponentsPage.getTitle()).to.eq('Message Tokens');
    await browser.wait(
      ec.or(ec.visibilityOf(messageTokenComponentsPage.entities), ec.visibilityOf(messageTokenComponentsPage.noResult)),
      1000
    );
  });

  it('should load create MessageToken page', async () => {
    await messageTokenComponentsPage.clickOnCreateButton();
    messageTokenUpdatePage = new MessageTokenUpdatePage();
    expect(await messageTokenUpdatePage.getPageTitle()).to.eq('Create or edit a Message Token');
    await messageTokenUpdatePage.cancel();
  });

  it('should create and save MessageTokens', async () => {
    const nbButtonsBeforeCreate = await messageTokenComponentsPage.countDeleteButtons();

    await messageTokenComponentsPage.clickOnCreateButton();

    await promise.all([
      messageTokenUpdatePage.setDescriptionInput('description'),
      messageTokenUpdatePage.setTimeSentInput('5'),
      messageTokenUpdatePage.setTokenValueInput('tokenValue'),
      messageTokenUpdatePage.getReceivedInput().click(),
      messageTokenUpdatePage.getActionedInput().click(),
      messageTokenUpdatePage.getContentFullyEnqueuedInput().click(),
    ]);

    await messageTokenUpdatePage.save();
    expect(await messageTokenUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await messageTokenComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last MessageToken', async () => {
    const nbButtonsBeforeDelete = await messageTokenComponentsPage.countDeleteButtons();
    await messageTokenComponentsPage.clickOnLastDeleteButton();

    messageTokenDeleteDialog = new MessageTokenDeleteDialog();
    expect(await messageTokenDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Message Token?');
    await messageTokenDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(messageTokenComponentsPage.title), 5000);

    expect(await messageTokenComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
