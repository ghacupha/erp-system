import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  CollateralInformationComponentsPage,
  /* CollateralInformationDeleteDialog, */
  CollateralInformationUpdatePage,
} from './collateral-information.page-object';

const expect = chai.expect;

describe('CollateralInformation e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let collateralInformationComponentsPage: CollateralInformationComponentsPage;
  let collateralInformationUpdatePage: CollateralInformationUpdatePage;
  /* let collateralInformationDeleteDialog: CollateralInformationDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CollateralInformations', async () => {
    await navBarPage.goToEntity('collateral-information');
    collateralInformationComponentsPage = new CollateralInformationComponentsPage();
    await browser.wait(ec.visibilityOf(collateralInformationComponentsPage.title), 5000);
    expect(await collateralInformationComponentsPage.getTitle()).to.eq('Collateral Informations');
    await browser.wait(
      ec.or(ec.visibilityOf(collateralInformationComponentsPage.entities), ec.visibilityOf(collateralInformationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create CollateralInformation page', async () => {
    await collateralInformationComponentsPage.clickOnCreateButton();
    collateralInformationUpdatePage = new CollateralInformationUpdatePage();
    expect(await collateralInformationUpdatePage.getPageTitle()).to.eq('Create or edit a Collateral Information');
    await collateralInformationUpdatePage.cancel();
  });

  /* it('should create and save CollateralInformations', async () => {
        const nbButtonsBeforeCreate = await collateralInformationComponentsPage.countDeleteButtons();

        await collateralInformationComponentsPage.clickOnCreateButton();

        await promise.all([
            collateralInformationUpdatePage.setReportingDateInput('2000-12-31'),
            collateralInformationUpdatePage.setCollateralIdInput('collateralId'),
            collateralInformationUpdatePage.setLoanContractIdInput('493942639303847'),
            collateralInformationUpdatePage.setCustomerIdInput('customerId'),
            collateralInformationUpdatePage.setRegistrationPropertyNumberInput('registrationPropertyNumber'),
            collateralInformationUpdatePage.setCollateralOMVInCCYInput('5'),
            collateralInformationUpdatePage.setCollateralFSVInLCYInput('5'),
            collateralInformationUpdatePage.setCollateralDiscountedValueInput('5'),
            collateralInformationUpdatePage.setAmountChargedInput('5'),
            collateralInformationUpdatePage.setCollateralDiscountRateInput('5'),
            collateralInformationUpdatePage.setLoanToValueRatioInput('5'),
            collateralInformationUpdatePage.setNameOfPropertyValuerInput('nameOfPropertyValuer'),
            collateralInformationUpdatePage.setCollateralLastValuationDateInput('2000-12-31'),
            collateralInformationUpdatePage.insuredFlagSelectLastOption(),
            collateralInformationUpdatePage.setNameOfInsurerInput('nameOfInsurer'),
            collateralInformationUpdatePage.setAmountInsuredInput('5'),
            collateralInformationUpdatePage.setInsuranceExpiryDateInput('2000-12-31'),
            collateralInformationUpdatePage.setGuaranteeInsurersInput('guaranteeInsurers'),
            collateralInformationUpdatePage.bankCodeSelectLastOption(),
            collateralInformationUpdatePage.branchCodeSelectLastOption(),
            collateralInformationUpdatePage.collateralTypeSelectLastOption(),
            collateralInformationUpdatePage.countyCodeSelectLastOption(),
        ]);

        await collateralInformationUpdatePage.save();
        expect(await collateralInformationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await collateralInformationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last CollateralInformation', async () => {
        const nbButtonsBeforeDelete = await collateralInformationComponentsPage.countDeleteButtons();
        await collateralInformationComponentsPage.clickOnLastDeleteButton();

        collateralInformationDeleteDialog = new CollateralInformationDeleteDialog();
        expect(await collateralInformationDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Collateral Information?');
        await collateralInformationDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(collateralInformationComponentsPage.title), 5000);

        expect(await collateralInformationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
