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
  WeeklyCounterfeitHoldingComponentsPage,
  WeeklyCounterfeitHoldingDeleteDialog,
  WeeklyCounterfeitHoldingUpdatePage,
} from './weekly-counterfeit-holding.page-object';

const expect = chai.expect;

describe('WeeklyCounterfeitHolding e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let weeklyCounterfeitHoldingComponentsPage: WeeklyCounterfeitHoldingComponentsPage;
  let weeklyCounterfeitHoldingUpdatePage: WeeklyCounterfeitHoldingUpdatePage;
  let weeklyCounterfeitHoldingDeleteDialog: WeeklyCounterfeitHoldingDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load WeeklyCounterfeitHoldings', async () => {
    await navBarPage.goToEntity('weekly-counterfeit-holding');
    weeklyCounterfeitHoldingComponentsPage = new WeeklyCounterfeitHoldingComponentsPage();
    await browser.wait(ec.visibilityOf(weeklyCounterfeitHoldingComponentsPage.title), 5000);
    expect(await weeklyCounterfeitHoldingComponentsPage.getTitle()).to.eq('Weekly Counterfeit Holdings');
    await browser.wait(
      ec.or(
        ec.visibilityOf(weeklyCounterfeitHoldingComponentsPage.entities),
        ec.visibilityOf(weeklyCounterfeitHoldingComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create WeeklyCounterfeitHolding page', async () => {
    await weeklyCounterfeitHoldingComponentsPage.clickOnCreateButton();
    weeklyCounterfeitHoldingUpdatePage = new WeeklyCounterfeitHoldingUpdatePage();
    expect(await weeklyCounterfeitHoldingUpdatePage.getPageTitle()).to.eq('Create or edit a Weekly Counterfeit Holding');
    await weeklyCounterfeitHoldingUpdatePage.cancel();
  });

  it('should create and save WeeklyCounterfeitHoldings', async () => {
    const nbButtonsBeforeCreate = await weeklyCounterfeitHoldingComponentsPage.countDeleteButtons();

    await weeklyCounterfeitHoldingComponentsPage.clickOnCreateButton();

    await promise.all([
      weeklyCounterfeitHoldingUpdatePage.setReportingDateInput('2000-12-31'),
      weeklyCounterfeitHoldingUpdatePage.setDateConfiscatedInput('2000-12-31'),
      weeklyCounterfeitHoldingUpdatePage.setSerialNumberInput('serialNumber'),
      weeklyCounterfeitHoldingUpdatePage.setDepositorsNamesInput('depositorsNames'),
      weeklyCounterfeitHoldingUpdatePage.setTellersNamesInput('tellersNames'),
      weeklyCounterfeitHoldingUpdatePage.setDateSubmittedToCBKInput('2000-12-31'),
      weeklyCounterfeitHoldingUpdatePage.setRemarksInput('remarks'),
    ]);

    await weeklyCounterfeitHoldingUpdatePage.save();
    expect(await weeklyCounterfeitHoldingUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await weeklyCounterfeitHoldingComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last WeeklyCounterfeitHolding', async () => {
    const nbButtonsBeforeDelete = await weeklyCounterfeitHoldingComponentsPage.countDeleteButtons();
    await weeklyCounterfeitHoldingComponentsPage.clickOnLastDeleteButton();

    weeklyCounterfeitHoldingDeleteDialog = new WeeklyCounterfeitHoldingDeleteDialog();
    expect(await weeklyCounterfeitHoldingDeleteDialog.getDialogTitle()).to.eq(
      'Are you sure you want to delete this Weekly Counterfeit Holding?'
    );
    await weeklyCounterfeitHoldingDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(weeklyCounterfeitHoldingComponentsPage.title), 5000);

    expect(await weeklyCounterfeitHoldingComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
