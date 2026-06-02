import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CrbAmountCategoryBandComponentsPage,
  CrbAmountCategoryBandDeleteDialog,
  CrbAmountCategoryBandUpdatePage,
} from './crb-amount-category-band.page-object';

const expect = chai.expect;

describe('CrbAmountCategoryBand e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbAmountCategoryBandComponentsPage: CrbAmountCategoryBandComponentsPage;
  let crbAmountCategoryBandUpdatePage: CrbAmountCategoryBandUpdatePage;
  let crbAmountCategoryBandDeleteDialog: CrbAmountCategoryBandDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbAmountCategoryBands', async () => {
    await navBarPage.goToEntity('crb-amount-category-band');
    crbAmountCategoryBandComponentsPage = new CrbAmountCategoryBandComponentsPage();
    await browser.wait(ec.visibilityOf(crbAmountCategoryBandComponentsPage.title), 5000);
    expect(await crbAmountCategoryBandComponentsPage.getTitle()).to.eq('Crb Amount Category Bands');
    await browser.wait(
      ec.or(ec.visibilityOf(crbAmountCategoryBandComponentsPage.entities), ec.visibilityOf(crbAmountCategoryBandComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbAmountCategoryBand page', async () => {
    await crbAmountCategoryBandComponentsPage.clickOnCreateButton();
    crbAmountCategoryBandUpdatePage = new CrbAmountCategoryBandUpdatePage();
    expect(await crbAmountCategoryBandUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Amount Category Band');
    await crbAmountCategoryBandUpdatePage.cancel();
  });

  it('should create and save CrbAmountCategoryBands', async () => {
    const nbButtonsBeforeCreate = await crbAmountCategoryBandComponentsPage.countDeleteButtons();

    await crbAmountCategoryBandComponentsPage.clickOnCreateButton();

    await promise.all([
      crbAmountCategoryBandUpdatePage.setAmountCategoryBandCodeInput('amountCategoryBandCode'),
      crbAmountCategoryBandUpdatePage.setAmountCategoryBandInput('amountCategoryBand'),
      crbAmountCategoryBandUpdatePage.setAmountCategoryBandDetailsInput('amountCategoryBandDetails'),
    ]);

    await crbAmountCategoryBandUpdatePage.save();
    expect(await crbAmountCategoryBandUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbAmountCategoryBandComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbAmountCategoryBand', async () => {
    const nbButtonsBeforeDelete = await crbAmountCategoryBandComponentsPage.countDeleteButtons();
    await crbAmountCategoryBandComponentsPage.clickOnLastDeleteButton();

    crbAmountCategoryBandDeleteDialog = new CrbAmountCategoryBandDeleteDialog();
    expect(await crbAmountCategoryBandDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Crb Amount Category Band?'
    );
    await crbAmountCategoryBandDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbAmountCategoryBandComponentsPage.title), 5000);

    expect(await crbAmountCategoryBandComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
