import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CrbNatureOfInformationComponentsPage,
  CrbNatureOfInformationDeleteDialog,
  CrbNatureOfInformationUpdatePage,
} from './crb-nature-of-information.page-object';

const expect = chai.expect;

describe('CrbNatureOfInformation e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbNatureOfInformationComponentsPage: CrbNatureOfInformationComponentsPage;
  let crbNatureOfInformationUpdatePage: CrbNatureOfInformationUpdatePage;
  let crbNatureOfInformationDeleteDialog: CrbNatureOfInformationDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbNatureOfInformations', async () => {
    await navBarPage.goToEntity('crb-nature-of-information');
    crbNatureOfInformationComponentsPage = new CrbNatureOfInformationComponentsPage();
    await browser.wait(ec.visibilityOf(crbNatureOfInformationComponentsPage.title), 5000);
    expect(await crbNatureOfInformationComponentsPage.getTitle()).to.eq('Crb Nature Of Informations');
    await browser.wait(
      ec.or(ec.visibilityOf(crbNatureOfInformationComponentsPage.entities), ec.visibilityOf(crbNatureOfInformationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CrbNatureOfInformation page', async () => {
    await crbNatureOfInformationComponentsPage.clickOnCreateButton();
    crbNatureOfInformationUpdatePage = new CrbNatureOfInformationUpdatePage();
    expect(await crbNatureOfInformationUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Nature Of Information');
    await crbNatureOfInformationUpdatePage.cancel();
  });

  it('should create and save CrbNatureOfInformations', async () => {
    const nbButtonsBeforeCreate = await crbNatureOfInformationComponentsPage.countDeleteButtons();

    await crbNatureOfInformationComponentsPage.clickOnCreateButton();

    await promise.all([
      crbNatureOfInformationUpdatePage.setNatureOfInformationTypeCodeInput('natureOfInformationTypeCode'),
      crbNatureOfInformationUpdatePage.setNatureOfInformationTypeInput('natureOfInformationType'),
      crbNatureOfInformationUpdatePage.setNatureOfInformationTypeDescriptionInput('natureOfInformationTypeDescription'),
    ]);

    await crbNatureOfInformationUpdatePage.save();
    expect(await crbNatureOfInformationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbNatureOfInformationComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbNatureOfInformation', async () => {
    const nbButtonsBeforeDelete = await crbNatureOfInformationComponentsPage.countDeleteButtons();
    await crbNatureOfInformationComponentsPage.clickOnLastDeleteButton();

    crbNatureOfInformationDeleteDialog = new CrbNatureOfInformationDeleteDialog();
    expect(await crbNatureOfInformationDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Crb Nature Of Information?'
    );
    await crbNatureOfInformationDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbNatureOfInformationComponentsPage.title), 5000);

    expect(await crbNatureOfInformationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
