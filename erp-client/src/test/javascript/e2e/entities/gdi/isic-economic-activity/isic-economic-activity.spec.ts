import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  IsicEconomicActivityComponentsPage,
  IsicEconomicActivityDeleteDialog,
  IsicEconomicActivityUpdatePage,
} from './isic-economic-activity.page-object';

const expect = chai.expect;

describe('IsicEconomicActivity e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let isicEconomicActivityComponentsPage: IsicEconomicActivityComponentsPage;
  let isicEconomicActivityUpdatePage: IsicEconomicActivityUpdatePage;
  let isicEconomicActivityDeleteDialog: IsicEconomicActivityDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load IsicEconomicActivities', async () => {
    await navBarPage.goToEntity('isic-economic-activity');
    isicEconomicActivityComponentsPage = new IsicEconomicActivityComponentsPage();
    await browser.wait(ec.visibilityOf(isicEconomicActivityComponentsPage.title), 5000);
    expect(await isicEconomicActivityComponentsPage.getTitle()).to.eq('Isic Economic Activities');
    await browser.wait(
      ec.or(ec.visibilityOf(isicEconomicActivityComponentsPage.entities), ec.visibilityOf(isicEconomicActivityComponentsPage.noResult)),
      1000
    );
  });

  it('should load create IsicEconomicActivity page', async () => {
    await isicEconomicActivityComponentsPage.clickOnCreateButton();
    isicEconomicActivityUpdatePage = new IsicEconomicActivityUpdatePage();
    expect(await isicEconomicActivityUpdatePage.getPageTitle()).to.eq('Create or edit a Isic Economic Activity');
    await isicEconomicActivityUpdatePage.cancel();
  });

  it('should create and save IsicEconomicActivities', async () => {
    const nbButtonsBeforeCreate = await isicEconomicActivityComponentsPage.countDeleteButtons();

    await isicEconomicActivityComponentsPage.clickOnCreateButton();

    await promise.all([
      isicEconomicActivityUpdatePage.setBusinessEconomicActivityCodeInput('businessEconomicActivityCode'),
      isicEconomicActivityUpdatePage.setSectionInput('section'),
      isicEconomicActivityUpdatePage.setSectionLabelInput('sectionLabel'),
      isicEconomicActivityUpdatePage.setDivisionInput('division'),
      isicEconomicActivityUpdatePage.setDivisionLabelInput('divisionLabel'),
      isicEconomicActivityUpdatePage.setGroupCodeInput('groupCode'),
      isicEconomicActivityUpdatePage.setGroupLabelInput('groupLabel'),
      isicEconomicActivityUpdatePage.setClassCodeInput('classCode'),
      isicEconomicActivityUpdatePage.setBusinessEconomicActivityTypeInput('businessEconomicActivityType'),
      isicEconomicActivityUpdatePage.setBusinessEconomicActivityTypeDescriptionInput('businessEconomicActivityTypeDescription'),
    ]);

    await isicEconomicActivityUpdatePage.save();
    expect(await isicEconomicActivityUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await isicEconomicActivityComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last IsicEconomicActivity', async () => {
    const nbButtonsBeforeDelete = await isicEconomicActivityComponentsPage.countDeleteButtons();
    await isicEconomicActivityComponentsPage.clickOnLastDeleteButton();

    isicEconomicActivityDeleteDialog = new IsicEconomicActivityDeleteDialog();
    expect(await isicEconomicActivityDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Isic Economic Activity?');
    await isicEconomicActivityDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(isicEconomicActivityComponentsPage.title), 5000);

    expect(await isicEconomicActivityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
