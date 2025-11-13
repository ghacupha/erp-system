///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
  AnticipatedMaturityPerioodComponentsPage,
  AnticipatedMaturityPerioodDeleteDialog,
  AnticipatedMaturityPerioodUpdatePage,
} from './anticipated-maturity-periood.page-object';

const expect = chai.expect;

describe('AnticipatedMaturityPeriood e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let anticipatedMaturityPerioodComponentsPage: AnticipatedMaturityPerioodComponentsPage;
  let anticipatedMaturityPerioodUpdatePage: AnticipatedMaturityPerioodUpdatePage;
  let anticipatedMaturityPerioodDeleteDialog: AnticipatedMaturityPerioodDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AnticipatedMaturityPerioods', async () => {
    await navBarPage.goToEntity('anticipated-maturity-periood');
    anticipatedMaturityPerioodComponentsPage = new AnticipatedMaturityPerioodComponentsPage();
    await browser.wait(ec.visibilityOf(anticipatedMaturityPerioodComponentsPage.title), 5000);
    expect(await anticipatedMaturityPerioodComponentsPage.getTitle()).to.eq('Anticipated Maturity Perioods');
    await browser.wait(
      ec.or(
        ec.visibilityOf(anticipatedMaturityPerioodComponentsPage.entities),
        ec.visibilityOf(anticipatedMaturityPerioodComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create AnticipatedMaturityPeriood page', async () => {
    await anticipatedMaturityPerioodComponentsPage.clickOnCreateButton();
    anticipatedMaturityPerioodUpdatePage = new AnticipatedMaturityPerioodUpdatePage();
    expect(await anticipatedMaturityPerioodUpdatePage.getPageTitle()).to.eq('Create or edit a Anticipated Maturity Periood');
    await anticipatedMaturityPerioodUpdatePage.cancel();
  });

  it('should create and save AnticipatedMaturityPerioods', async () => {
    const nbButtonsBeforeCreate = await anticipatedMaturityPerioodComponentsPage.countDeleteButtons();

    await anticipatedMaturityPerioodComponentsPage.clickOnCreateButton();

    await promise.all([
      anticipatedMaturityPerioodUpdatePage.setAnticipatedMaturityTenorCodeInput('anticipatedMaturityTenorCode'),
      anticipatedMaturityPerioodUpdatePage.setAniticipatedMaturityTenorTypeInput('aniticipatedMaturityTenorType'),
      anticipatedMaturityPerioodUpdatePage.setAnticipatedMaturityTenorDetailsInput('anticipatedMaturityTenorDetails'),
    ]);

    await anticipatedMaturityPerioodUpdatePage.save();
    expect(await anticipatedMaturityPerioodUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await anticipatedMaturityPerioodComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last AnticipatedMaturityPeriood', async () => {
    const nbButtonsBeforeDelete = await anticipatedMaturityPerioodComponentsPage.countDeleteButtons();
    await anticipatedMaturityPerioodComponentsPage.clickOnLastDeleteButton();

    anticipatedMaturityPerioodDeleteDialog = new AnticipatedMaturityPerioodDeleteDialog();
    expect(await anticipatedMaturityPerioodDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Anticipated Maturity Periood?'
    );
    await anticipatedMaturityPerioodDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(anticipatedMaturityPerioodComponentsPage.title), 5000);

    expect(await anticipatedMaturityPerioodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
