import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CardStatusFlagComponentsPage, CardStatusFlagDeleteDialog, CardStatusFlagUpdatePage } from './card-status-flag.page-object';

const expect = chai.expect;

describe('CardStatusFlag e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardStatusFlagComponentsPage: CardStatusFlagComponentsPage;
  let cardStatusFlagUpdatePage: CardStatusFlagUpdatePage;
  let cardStatusFlagDeleteDialog: CardStatusFlagDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardStatusFlags', async () => {
    await navBarPage.goToEntity('card-status-flag');
    cardStatusFlagComponentsPage = new CardStatusFlagComponentsPage();
    await browser.wait(ec.visibilityOf(cardStatusFlagComponentsPage.title), 5000);
    expect(await cardStatusFlagComponentsPage.getTitle()).to.eq('Card Status Flags');
    await browser.wait(
      ec.or(ec.visibilityOf(cardStatusFlagComponentsPage.entities), ec.visibilityOf(cardStatusFlagComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CardStatusFlag page', async () => {
    await cardStatusFlagComponentsPage.clickOnCreateButton();
    cardStatusFlagUpdatePage = new CardStatusFlagUpdatePage();
    expect(await cardStatusFlagUpdatePage.getPageTitle()).to.eq('Create or edit a Card Status Flag');
    await cardStatusFlagUpdatePage.cancel();
  });

  it('should create and save CardStatusFlags', async () => {
    const nbButtonsBeforeCreate = await cardStatusFlagComponentsPage.countDeleteButtons();

    await cardStatusFlagComponentsPage.clickOnCreateButton();

    await promise.all([
      cardStatusFlagUpdatePage.cardStatusFlagSelectLastOption(),
      cardStatusFlagUpdatePage.setCardStatusFlagDescriptionInput('cardStatusFlagDescription'),
      cardStatusFlagUpdatePage.setCardStatusFlagDetailsInput('cardStatusFlagDetails'),
    ]);

    await cardStatusFlagUpdatePage.save();
    expect(await cardStatusFlagUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cardStatusFlagComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CardStatusFlag', async () => {
    const nbButtonsBeforeDelete = await cardStatusFlagComponentsPage.countDeleteButtons();
    await cardStatusFlagComponentsPage.clickOnLastDeleteButton();

    cardStatusFlagDeleteDialog = new CardStatusFlagDeleteDialog();
    expect(await cardStatusFlagDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Card Status Flag?');
    await cardStatusFlagDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cardStatusFlagComponentsPage.title), 5000);

    expect(await cardStatusFlagComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
