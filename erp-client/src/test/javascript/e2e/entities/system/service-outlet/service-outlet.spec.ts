///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { ServiceOutletComponentsPage, ServiceOutletDeleteDialog, ServiceOutletUpdatePage } from './service-outlet.page-object';

const expect = chai.expect;

describe('ServiceOutlet e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let serviceOutletComponentsPage: ServiceOutletComponentsPage;
  let serviceOutletUpdatePage: ServiceOutletUpdatePage;
  let serviceOutletDeleteDialog: ServiceOutletDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ServiceOutlets', async () => {
    await navBarPage.goToEntity('service-outlet');
    serviceOutletComponentsPage = new ServiceOutletComponentsPage();
    await browser.wait(ec.visibilityOf(serviceOutletComponentsPage.title), 5000);
    expect(await serviceOutletComponentsPage.getTitle()).to.eq('Service Outlets');
    await browser.wait(
      ec.or(ec.visibilityOf(serviceOutletComponentsPage.entities), ec.visibilityOf(serviceOutletComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ServiceOutlet page', async () => {
    await serviceOutletComponentsPage.clickOnCreateButton();
    serviceOutletUpdatePage = new ServiceOutletUpdatePage();
    expect(await serviceOutletUpdatePage.getPageTitle()).to.eq('Create or edit a Service Outlet');
    await serviceOutletUpdatePage.cancel();
  });

  it('should create and save ServiceOutlets', async () => {
    const nbButtonsBeforeCreate = await serviceOutletComponentsPage.countDeleteButtons();

    await serviceOutletComponentsPage.clickOnCreateButton();

    await promise.all([
      serviceOutletUpdatePage.setOutletCodeInput('outletCode'),
      serviceOutletUpdatePage.setOutletNameInput('outletName'),
      serviceOutletUpdatePage.setTownInput('town'),
      serviceOutletUpdatePage.setParliamentaryConstituencyInput('parliamentaryConstituency'),
      serviceOutletUpdatePage.setGpsCoordinatesInput('gpsCoordinates'),
      serviceOutletUpdatePage.setOutletOpeningDateInput('2000-12-31'),
      serviceOutletUpdatePage.setRegulatorApprovalDateInput('2000-12-31'),
      serviceOutletUpdatePage.setOutletClosureDateInput('2000-12-31'),
      serviceOutletUpdatePage.setDateLastModifiedInput('2000-12-31'),
      serviceOutletUpdatePage.setLicenseFeePayableInput('5'),
      // serviceOutletUpdatePage.placeholderSelectLastOption(),
      serviceOutletUpdatePage.bankCodeSelectLastOption(),
      serviceOutletUpdatePage.outletTypeSelectLastOption(),
      serviceOutletUpdatePage.outletStatusSelectLastOption(),
      serviceOutletUpdatePage.countyNameSelectLastOption(),
      serviceOutletUpdatePage.subCountyNameSelectLastOption(),
    ]);

    await serviceOutletUpdatePage.save();
    expect(await serviceOutletUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await serviceOutletComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ServiceOutlet', async () => {
    const nbButtonsBeforeDelete = await serviceOutletComponentsPage.countDeleteButtons();
    await serviceOutletComponentsPage.clickOnLastDeleteButton();

    serviceOutletDeleteDialog = new ServiceOutletDeleteDialog();
    expect(await serviceOutletDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Service Outlet?');
    await serviceOutletDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(serviceOutletComponentsPage.title), 5000);

    expect(await serviceOutletComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
