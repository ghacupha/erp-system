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

import { LeaseContractComponentsPage, LeaseContractDeleteDialog, LeaseContractUpdatePage } from './lease-contract.page-object';

const expect = chai.expect;

describe('LeaseContract e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseContractComponentsPage: LeaseContractComponentsPage;
  let leaseContractUpdatePage: LeaseContractUpdatePage;
  let leaseContractDeleteDialog: LeaseContractDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseContracts', async () => {
    await navBarPage.goToEntity('lease-contract');
    leaseContractComponentsPage = new LeaseContractComponentsPage();
    await browser.wait(ec.visibilityOf(leaseContractComponentsPage.title), 5000);
    expect(await leaseContractComponentsPage.getTitle()).to.eq('Lease Contracts');
    await browser.wait(
      ec.or(ec.visibilityOf(leaseContractComponentsPage.entities), ec.visibilityOf(leaseContractComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LeaseContract page', async () => {
    await leaseContractComponentsPage.clickOnCreateButton();
    leaseContractUpdatePage = new LeaseContractUpdatePage();
    expect(await leaseContractUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Contract');
    await leaseContractUpdatePage.cancel();
  });

  it('should create and save LeaseContracts', async () => {
    const nbButtonsBeforeCreate = await leaseContractComponentsPage.countDeleteButtons();

    await leaseContractComponentsPage.clickOnCreateButton();

    await promise.all([
      leaseContractUpdatePage.setBookingIdInput('bookingId'),
      leaseContractUpdatePage.setLeaseTitleInput('leaseTitle'),
      leaseContractUpdatePage.setIdentifierInput('64c99148-3908-465d-8c4a-e510e3ade974'),
      leaseContractUpdatePage.setDescriptionInput('description'),
      leaseContractUpdatePage.setCommencementDateInput('2000-12-31'),
      leaseContractUpdatePage.setTerminalDateInput('2000-12-31'),
      // leaseContractUpdatePage.placeholderSelectLastOption(),
      // leaseContractUpdatePage.systemMappingsSelectLastOption(),
      // leaseContractUpdatePage.businessDocumentSelectLastOption(),
      // leaseContractUpdatePage.contractMetadataSelectLastOption(),
    ]);

    await leaseContractUpdatePage.save();
    expect(await leaseContractUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await leaseContractComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last LeaseContract', async () => {
    const nbButtonsBeforeDelete = await leaseContractComponentsPage.countDeleteButtons();
    await leaseContractComponentsPage.clickOnLastDeleteButton();

    leaseContractDeleteDialog = new LeaseContractDeleteDialog();
    expect(await leaseContractDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Lease Contract?');
    await leaseContractDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(leaseContractComponentsPage.title), 5000);

    expect(await leaseContractComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
