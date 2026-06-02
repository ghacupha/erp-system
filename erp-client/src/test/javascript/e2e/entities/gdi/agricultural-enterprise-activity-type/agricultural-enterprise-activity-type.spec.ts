import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AgriculturalEnterpriseActivityTypeComponentsPage,
  AgriculturalEnterpriseActivityTypeDeleteDialog,
  AgriculturalEnterpriseActivityTypeUpdatePage,
} from './agricultural-enterprise-activity-type.page-object';

const expect = chai.expect;

describe('AgriculturalEnterpriseActivityType e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let agriculturalEnterpriseActivityTypeComponentsPage: AgriculturalEnterpriseActivityTypeComponentsPage;
  let agriculturalEnterpriseActivityTypeUpdatePage: AgriculturalEnterpriseActivityTypeUpdatePage;
  let agriculturalEnterpriseActivityTypeDeleteDialog: AgriculturalEnterpriseActivityTypeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AgriculturalEnterpriseActivityTypes', async () => {
    await navBarPage.goToEntity('agricultural-enterprise-activity-type');
    agriculturalEnterpriseActivityTypeComponentsPage = new AgriculturalEnterpriseActivityTypeComponentsPage();
    await browser.wait(ec.visibilityOf(agriculturalEnterpriseActivityTypeComponentsPage.title), 5000);
    expect(await agriculturalEnterpriseActivityTypeComponentsPage.getTitle()).to.eq('Agricultural Enterprise Activity Types');
    await browser.wait(
      ec.or(
        ec.visibilityOf(agriculturalEnterpriseActivityTypeComponentsPage.entities),
        ec.visibilityOf(agriculturalEnterpriseActivityTypeComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create AgriculturalEnterpriseActivityType page', async () => {
    await agriculturalEnterpriseActivityTypeComponentsPage.clickOnCreateButton();
    agriculturalEnterpriseActivityTypeUpdatePage = new AgriculturalEnterpriseActivityTypeUpdatePage();
    expect(await agriculturalEnterpriseActivityTypeUpdatePage.getPageTitle()).to.eq(
      'Create or edit a Agricultural Enterprise Activity Type'
    );
    await agriculturalEnterpriseActivityTypeUpdatePage.cancel();
  });

  it('should create and save AgriculturalEnterpriseActivityTypes', async () => {
    const nbButtonsBeforeCreate = await agriculturalEnterpriseActivityTypeComponentsPage.countDeleteButtons();

    await agriculturalEnterpriseActivityTypeComponentsPage.clickOnCreateButton();

    await promise.all([
      agriculturalEnterpriseActivityTypeUpdatePage.setAgriculturalEnterpriseActivityTypeCodeInput('agriculturalEnterpriseActivityTypeCode'),
      agriculturalEnterpriseActivityTypeUpdatePage.setAgriculturalEnterpriseActivityTypeInput('agriculturalEnterpriseActivityType'),
      agriculturalEnterpriseActivityTypeUpdatePage.setAgriculturalEnterpriseActivityTypeDescriptionInput(
        'agriculturalEnterpriseActivityTypeDescription'
      ),
    ]);

    await agriculturalEnterpriseActivityTypeUpdatePage.save();
    expect(await agriculturalEnterpriseActivityTypeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await agriculturalEnterpriseActivityTypeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last AgriculturalEnterpriseActivityType', async () => {
    const nbButtonsBeforeDelete = await agriculturalEnterpriseActivityTypeComponentsPage.countDeleteButtons();
    await agriculturalEnterpriseActivityTypeComponentsPage.clickOnLastDeleteButton();

    agriculturalEnterpriseActivityTypeDeleteDialog = new AgriculturalEnterpriseActivityTypeDeleteDialog();
    expect(await agriculturalEnterpriseActivityTypeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Agricultural Enterprise Activity Type?'
    );
    await agriculturalEnterpriseActivityTypeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(agriculturalEnterpriseActivityTypeComponentsPage.title), 5000);

    expect(await agriculturalEnterpriseActivityTypeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
