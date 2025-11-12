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

import {
  CrbSubscriptionStatusTypeCodeComponentsPage,
  CrbSubscriptionStatusTypeCodeDeleteDialog,
  CrbSubscriptionStatusTypeCodeUpdatePage,
} from './crb-subscription-status-type-code.page-object';

const expect = chai.expect;

describe('CrbSubscriptionStatusTypeCode e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let crbSubscriptionStatusTypeCodeComponentsPage: CrbSubscriptionStatusTypeCodeComponentsPage;
  let crbSubscriptionStatusTypeCodeUpdatePage: CrbSubscriptionStatusTypeCodeUpdatePage;
  let crbSubscriptionStatusTypeCodeDeleteDialog: CrbSubscriptionStatusTypeCodeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load CrbSubscriptionStatusTypeCodes', async () => {
    await navBarPage.goToEntity('crb-subscription-status-type-code');
    crbSubscriptionStatusTypeCodeComponentsPage = new CrbSubscriptionStatusTypeCodeComponentsPage();
    await browser.wait(ec.visibilityOf(crbSubscriptionStatusTypeCodeComponentsPage.title), 5000);
    expect(await crbSubscriptionStatusTypeCodeComponentsPage.getTitle()).to.eq('Crb Subscription Status Type Codes');
    await browser.wait(
      ec.or(
        ec.visibilityOf(crbSubscriptionStatusTypeCodeComponentsPage.entities),
        ec.visibilityOf(crbSubscriptionStatusTypeCodeComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create CrbSubscriptionStatusTypeCode page', async () => {
    await crbSubscriptionStatusTypeCodeComponentsPage.clickOnCreateButton();
    crbSubscriptionStatusTypeCodeUpdatePage = new CrbSubscriptionStatusTypeCodeUpdatePage();
    expect(await crbSubscriptionStatusTypeCodeUpdatePage.getPageTitle()).to.eq('Create or edit a Crb Subscription Status Type Code');
    await crbSubscriptionStatusTypeCodeUpdatePage.cancel();
  });

  it('should create and save CrbSubscriptionStatusTypeCodes', async () => {
    const nbButtonsBeforeCreate = await crbSubscriptionStatusTypeCodeComponentsPage.countDeleteButtons();

    await crbSubscriptionStatusTypeCodeComponentsPage.clickOnCreateButton();

    await promise.all([
      crbSubscriptionStatusTypeCodeUpdatePage.setSubscriptionStatusTypeCodeInput('subscriptionStatusTypeCode'),
      crbSubscriptionStatusTypeCodeUpdatePage.setSubscriptionStatusTypeInput('subscriptionStatusType'),
      crbSubscriptionStatusTypeCodeUpdatePage.setSubscriptionStatusTypeDescriptionInput('subscriptionStatusTypeDescription'),
    ]);

    await crbSubscriptionStatusTypeCodeUpdatePage.save();
    expect(await crbSubscriptionStatusTypeCodeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await crbSubscriptionStatusTypeCodeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last CrbSubscriptionStatusTypeCode', async () => {
    const nbButtonsBeforeDelete = await crbSubscriptionStatusTypeCodeComponentsPage.countDeleteButtons();
    await crbSubscriptionStatusTypeCodeComponentsPage.clickOnLastDeleteButton();

    crbSubscriptionStatusTypeCodeDeleteDialog = new CrbSubscriptionStatusTypeCodeDeleteDialog();
    expect(await crbSubscriptionStatusTypeCodeDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Crb Subscription Status Type Code?'
    );
    await crbSubscriptionStatusTypeCodeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(crbSubscriptionStatusTypeCodeComponentsPage.title), 5000);

    expect(await crbSubscriptionStatusTypeCodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
