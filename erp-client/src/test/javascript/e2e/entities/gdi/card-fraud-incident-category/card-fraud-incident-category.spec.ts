import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CardFraudIncidentCategoryComponentsPage,
  CardFraudIncidentCategoryDeleteDialog,
  CardFraudIncidentCategoryUpdatePage,
} from './card-fraud-incident-category.page-object';

const expect = chai.expect;

describe('CardFraudIncidentCategory e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardFraudIncidentCategoryComponentsPage: CardFraudIncidentCategoryComponentsPage;
  let cardFraudIncidentCategoryUpdatePage: CardFraudIncidentCategoryUpdatePage;
  let cardFraudIncidentCategoryDeleteDialog: CardFraudIncidentCategoryDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardFraudIncidentCategories', async () => {
    await navBarPage.goToEntity('card-fraud-incident-category');
    cardFraudIncidentCategoryComponentsPage = new CardFraudIncidentCategoryComponentsPage();
    await browser.wait(ec.visibilityOf(cardFraudIncidentCategoryComponentsPage.title), 5000);
    expect(await cardFraudIncidentCategoryComponentsPage.getTitle()).to.eq('Card Fraud Incident Categories');
    await browser.wait(
      ec.or(
        ec.visibilityOf(cardFraudIncidentCategoryComponentsPage.entities),
        ec.visibilityOf(cardFraudIncidentCategoryComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create CardFraudIncidentCategory page', async () => {
    await cardFraudIncidentCategoryComponentsPage.clickOnCreateButton();
    cardFraudIncidentCategoryUpdatePage = new CardFraudIncidentCategoryUpdatePage();
    expect(await cardFraudIncidentCategoryUpdatePage.getPageTitle()).to.eq('Create or edit a Card Fraud Incident Category');
    await cardFraudIncidentCategoryUpdatePage.cancel();
  });

  it('should create and save CardFraudIncidentCategories', async () => {
    const nbButtonsBeforeCreate = await cardFraudIncidentCategoryComponentsPage.countDeleteButtons();

    await cardFraudIncidentCategoryComponentsPage.clickOnCreateButton();

    await promise.all([
      cardFraudIncidentCategoryUpdatePage.setCardFraudCategoryTypeCodeInput('cardFraudCategoryTypeCode'),
      cardFraudIncidentCategoryUpdatePage.setCardFraudCategoryTypeInput('cardFraudCategoryType'),
      cardFraudIncidentCategoryUpdatePage.setCardFraudCategoryTypeDescriptionInput('cardFraudCategoryTypeDescription'),
    ]);

    await cardFraudIncidentCategoryUpdatePage.save();
    expect(await cardFraudIncidentCategoryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cardFraudIncidentCategoryComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CardFraudIncidentCategory', async () => {
    const nbButtonsBeforeDelete = await cardFraudIncidentCategoryComponentsPage.countDeleteButtons();
    await cardFraudIncidentCategoryComponentsPage.clickOnLastDeleteButton();

    cardFraudIncidentCategoryDeleteDialog = new CardFraudIncidentCategoryDeleteDialog();
    expect(await cardFraudIncidentCategoryDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Card Fraud Incident Category?'
    );
    await cardFraudIncidentCategoryDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cardFraudIncidentCategoryComponentsPage.title), 5000);

    expect(await cardFraudIncidentCategoryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
