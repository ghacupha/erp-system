///
/// Erp System - Mark X No 12 (Kadar Series) Client 1.8.0
/// Copyright © 2021 - 2026 Edwin Njeru (mailnjeru@gmail.com)
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
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { LeaseTemplateComponentsPage, LeaseTemplateDeleteDialog, LeaseTemplateUpdatePage } from './lease-template.page-object';

const expect = chai.expect;

describe('LeaseTemplate e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let leaseTemplateComponentsPage: LeaseTemplateComponentsPage;
  let leaseTemplateUpdatePage: LeaseTemplateUpdatePage;
  let leaseTemplateDeleteDialog: LeaseTemplateDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LeaseTemplates', async () => {
    await navBarPage.goToEntity('lease-template');
    leaseTemplateComponentsPage = new LeaseTemplateComponentsPage();
    await browser.wait(ec.visibilityOf(leaseTemplateComponentsPage.title), 5000);
    expect(await leaseTemplateComponentsPage.getTitle()).to.eq('Lease Templates');
    await browser.wait(
      ec.or(ec.visibilityOf(leaseTemplateComponentsPage.entities), ec.visibilityOf(leaseTemplateComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LeaseTemplate page', async () => {
    await leaseTemplateComponentsPage.clickOnCreateButton();
    leaseTemplateUpdatePage = new LeaseTemplateUpdatePage();
    expect(await leaseTemplateUpdatePage.getPageTitle()).to.eq('Create or edit a Lease Template');
    await leaseTemplateUpdatePage.cancel();
  });

  it('should create and save LeaseTemplates', async () => {
    const nbButtonsBeforeCreate = await leaseTemplateComponentsPage.countDeleteButtons();

    await leaseTemplateComponentsPage.clickOnCreateButton();

    await promise.all([
      leaseTemplateUpdatePage.setTemplateTitleInput('templateTitle'),
      leaseTemplateUpdatePage.assetAccountSelectLastOption(),
      leaseTemplateUpdatePage.depreciationAccountSelectLastOption(),
      leaseTemplateUpdatePage.accruedDepreciationAccountSelectLastOption(),
      leaseTemplateUpdatePage.interestPaidTransferDebitAccountSelectLastOption(),
      leaseTemplateUpdatePage.interestPaidTransferCreditAccountSelectLastOption(),
      leaseTemplateUpdatePage.interestAccruedDebitAccountSelectLastOption(),
      leaseTemplateUpdatePage.interestAccruedCreditAccountSelectLastOption(),
      leaseTemplateUpdatePage.leaseRecognitionDebitAccountSelectLastOption(),
      leaseTemplateUpdatePage.leaseRecognitionCreditAccountSelectLastOption(),
      leaseTemplateUpdatePage.leaseRepaymentDebitAccountSelectLastOption(),
      leaseTemplateUpdatePage.leaseRepaymentCreditAccountSelectLastOption(),
      leaseTemplateUpdatePage.rouRecognitionCreditAccountSelectLastOption(),
      leaseTemplateUpdatePage.rouRecognitionDebitAccountSelectLastOption(),
      leaseTemplateUpdatePage.assetCategorySelectLastOption(),
      leaseTemplateUpdatePage.serviceOutletSelectLastOption(),
      leaseTemplateUpdatePage.mainDealerSelectLastOption(),
    ]);

    await leaseTemplateUpdatePage.save();
    expect(await leaseTemplateUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await leaseTemplateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last LeaseTemplate', async () => {
    const nbButtonsBeforeDelete = await leaseTemplateComponentsPage.countDeleteButtons();
    await leaseTemplateComponentsPage.clickOnLastDeleteButton();

    leaseTemplateDeleteDialog = new LeaseTemplateDeleteDialog();
    expect(await leaseTemplateDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Lease Template?');
    await leaseTemplateDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(leaseTemplateComponentsPage.title), 5000);

    expect(await leaseTemplateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
