import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CardPerformanceFlagComponentsPage,
  CardPerformanceFlagDeleteDialog,
  CardPerformanceFlagUpdatePage,
} from './card-performance-flag.page-object';

const expect = chai.expect;

describe('CardPerformanceFlag e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let cardPerformanceFlagComponentsPage: CardPerformanceFlagComponentsPage;
  let cardPerformanceFlagUpdatePage: CardPerformanceFlagUpdatePage;
  let cardPerformanceFlagDeleteDialog: CardPerformanceFlagDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CardPerformanceFlags', async () => {
    await navBarPage.goToEntity('card-performance-flag');
    cardPerformanceFlagComponentsPage = new CardPerformanceFlagComponentsPage();
    await browser.wait(ec.visibilityOf(cardPerformanceFlagComponentsPage.title), 5000);
    expect(await cardPerformanceFlagComponentsPage.getTitle()).to.eq('Card Performance Flags');
    await browser.wait(
      ec.or(ec.visibilityOf(cardPerformanceFlagComponentsPage.entities), ec.visibilityOf(cardPerformanceFlagComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CardPerformanceFlag page', async () => {
    await cardPerformanceFlagComponentsPage.clickOnCreateButton();
    cardPerformanceFlagUpdatePage = new CardPerformanceFlagUpdatePage();
    expect(await cardPerformanceFlagUpdatePage.getPageTitle()).to.eq('Create or edit a Card Performance Flag');
    await cardPerformanceFlagUpdatePage.cancel();
  });

  it('should create and save CardPerformanceFlags', async () => {
    const nbButtonsBeforeCreate = await cardPerformanceFlagComponentsPage.countDeleteButtons();

    await cardPerformanceFlagComponentsPage.clickOnCreateButton();

    await promise.all([
      cardPerformanceFlagUpdatePage.cardPerformanceFlagSelectLastOption(),
      cardPerformanceFlagUpdatePage.setCardPerformanceFlagDescriptionInput('cardPerformanceFlagDescription'),
      cardPerformanceFlagUpdatePage.setCardPerformanceFlagDetailsInput('cardPerformanceFlagDetails'),
    ]);

    await cardPerformanceFlagUpdatePage.save();
    expect(await cardPerformanceFlagUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await cardPerformanceFlagComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CardPerformanceFlag', async () => {
    const nbButtonsBeforeDelete = await cardPerformanceFlagComponentsPage.countDeleteButtons();
    await cardPerformanceFlagComponentsPage.clickOnLastDeleteButton();

    cardPerformanceFlagDeleteDialog = new CardPerformanceFlagDeleteDialog();
    expect(await cardPerformanceFlagDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Card Performance Flag?');
    await cardPerformanceFlagDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(cardPerformanceFlagComponentsPage.title), 5000);

    expect(await cardPerformanceFlagComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
