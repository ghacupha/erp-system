import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { CardBrandTypeComponentsPage, CardBrandTypeDeleteDialog, CardBrandTypeUpdatePage } from './card-brand-type.page-object';

const expect = chai.expect;

describe('CardBrandType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardBrandTypeComponentsPage: CardBrandTypeComponentsPage;
  let cardBrandTypeUpdatePage: CardBrandTypeUpdatePage;
  let cardBrandTypeDeleteDialog: CardBrandTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardBrandTypes', async () => {
    await navBarPage.goToEntity('card-brand-type');
    cardBrandTypeComponentsPage = new CardBrandTypeComponentsPage();
    await browser.wait(ec.visibilityOf(cardBrandTypeComponentsPage.title), 5000);
    expect(await cardBrandTypeComponentsPage.getTitle()).to.eq('Card Brand Types');
    await browser.wait(
      ec.or(ec.visibilityOf(cardBrandTypeComponentsPage.entities), ec.visibilityOf(cardBrandTypeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CardBrandType page', async () => {
    await cardBrandTypeComponentsPage.clickOnCreateButton();
    cardBrandTypeUpdatePage = new CardBrandTypeUpdatePage();
    expect(await cardBrandTypeUpdatePage.getPageTitle()).to.eq('Create or edit a Card Brand Type');
    await cardBrandTypeUpdatePage.cancel();
  });

  it('should create and save CardBrandTypes', async () => {
    const nbButtonsBeforeCreate = await cardBrandTypeComponentsPage.countDeleteButtons();

    await cardBrandTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      cardBrandTypeUpdatePage.setCardBrandTypeCodeInput('cardBrandTypeCode'),
      cardBrandTypeUpdatePage.setCardBrandTypeInput('cardBrandType'),
      cardBrandTypeUpdatePage.setCardBrandTypeDetailsInput('cardBrandTypeDetails'),
    ]);

    await cardBrandTypeUpdatePage.save();
    expect(await cardBrandTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cardBrandTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last CardBrandType', async () => {
    const nbButtonsBeforeDelete = await cardBrandTypeComponentsPage.countDeleteButtons();
    await cardBrandTypeComponentsPage.clickOnLastDeleteButton();

    cardBrandTypeDeleteDialog = new CardBrandTypeDeleteDialog();
    expect(await cardBrandTypeDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Card Brand Type?');
    await cardBrandTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cardBrandTypeComponentsPage.title), 5000);

    expect(await cardBrandTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
