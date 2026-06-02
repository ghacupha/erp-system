import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  BouncedChequeCategoriesComponentsPage,
  BouncedChequeCategoriesDeleteDialog,
  BouncedChequeCategoriesUpdatePage,
} from './bounced-cheque-categories.page-object';

const expect = chai.expect;

describe('BouncedChequeCategories e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let bouncedChequeCategoriesComponentsPage: BouncedChequeCategoriesComponentsPage;
  let bouncedChequeCategoriesUpdatePage: BouncedChequeCategoriesUpdatePage;
  let bouncedChequeCategoriesDeleteDialog: BouncedChequeCategoriesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load BouncedChequeCategories', async () => {
    await navBarPage.goToEntity('bounced-cheque-categories');
    bouncedChequeCategoriesComponentsPage = new BouncedChequeCategoriesComponentsPage();
    await browser.wait(ec.visibilityOf(bouncedChequeCategoriesComponentsPage.title), 5000);
    expect(await bouncedChequeCategoriesComponentsPage.getTitle()).to.eq('Bounced Cheque Categories');
    await browser.wait(
      ec.or(
        ec.visibilityOf(bouncedChequeCategoriesComponentsPage.entities),
        ec.visibilityOf(bouncedChequeCategoriesComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create BouncedChequeCategories page', async () => {
    await bouncedChequeCategoriesComponentsPage.clickOnCreateButton();
    bouncedChequeCategoriesUpdatePage = new BouncedChequeCategoriesUpdatePage();
    expect(await bouncedChequeCategoriesUpdatePage.getPageTitle()).to.eq('Create or edit a Bounced Cheque Categories');
    await bouncedChequeCategoriesUpdatePage.cancel();
  });

  it('should create and save BouncedChequeCategories', async () => {
    const nbButtonsBeforeCreate = await bouncedChequeCategoriesComponentsPage.countDeleteButtons();

    await bouncedChequeCategoriesComponentsPage.clickOnCreateButton();

    await promise.all([
      bouncedChequeCategoriesUpdatePage.setBouncedChequeCategoryTypeCodeInput('bouncedChequeCategoryTypeCode'),
      bouncedChequeCategoriesUpdatePage.setBouncedChequeCategoryTypeInput('bouncedChequeCategoryType'),
    ]);

    await bouncedChequeCategoriesUpdatePage.save();
    expect(await bouncedChequeCategoriesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await bouncedChequeCategoriesComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last BouncedChequeCategories', async () => {
    const nbButtonsBeforeDelete = await bouncedChequeCategoriesComponentsPage.countDeleteButtons();
    await bouncedChequeCategoriesComponentsPage.clickOnLastDeleteButton();

    bouncedChequeCategoriesDeleteDialog = new BouncedChequeCategoriesDeleteDialog();
    expect(await bouncedChequeCategoriesDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Bounced Cheque Categories?'
    );
    await bouncedChequeCategoriesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(bouncedChequeCategoriesComponentsPage.title), 5000);

    expect(await bouncedChequeCategoriesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
