import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CardTypesComponentsPage, CardTypesDeleteDialog, CardTypesUpdatePage } from './card-types.page-object';

const expect = chai.expect;

describe('CardTypes e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardTypesComponentsPage: CardTypesComponentsPage;
  let cardTypesUpdatePage: CardTypesUpdatePage;
  let cardTypesDeleteDialog: CardTypesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardTypes', async () => {
    await navBarPage.goToEntity('card-types');
    cardTypesComponentsPage = new CardTypesComponentsPage();
    await browser.wait(ec.visibilityOf(cardTypesComponentsPage.title), 5000);
    expect(await cardTypesComponentsPage.getTitle()).to.eq('Card Types');
    await browser.wait(ec.or(ec.visibilityOf(cardTypesComponentsPage.entities), ec.visibilityOf(cardTypesComponentsPage.noResult)), 1000);
  });

  it('should load create CardTypes page', async () => {
    await cardTypesComponentsPage.clickOnCreateButton();
    cardTypesUpdatePage = new CardTypesUpdatePage();
    expect(await cardTypesUpdatePage.getPageTitle()).to.eq('Create or edit a Card Types');
    await cardTypesUpdatePage.cancel();
  });

  it('should create and save CardTypes', async () => {
    const nbButtonsBeforeCreate = await cardTypesComponentsPage.countDeleteButtons();

    await cardTypesComponentsPage.clickOnCreateButton();

    await promise.all([
      cardTypesUpdatePage.setCardTypeCodeInput('cardTypeCode'),
      cardTypesUpdatePage.setCardTypeInput('cardType'),
      cardTypesUpdatePage.setCardTypeDetailsInput('cardTypeDetails'),
    ]);

    await cardTypesUpdatePage.save();
    expect(await cardTypesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cardTypesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CardTypes', async () => {
    const nbButtonsBeforeDelete = await cardTypesComponentsPage.countDeleteButtons();
    await cardTypesComponentsPage.clickOnLastDeleteButton();

    cardTypesDeleteDialog = new CardTypesDeleteDialog();
    expect(await cardTypesDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Card Types?');
    await cardTypesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cardTypesComponentsPage.title), 5000);

    expect(await cardTypesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
