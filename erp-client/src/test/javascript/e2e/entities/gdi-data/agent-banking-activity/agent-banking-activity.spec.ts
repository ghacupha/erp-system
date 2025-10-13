///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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

import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import {
  AgentBankingActivityComponentsPage,
  /* AgentBankingActivityDeleteDialog, */
  AgentBankingActivityUpdatePage,
} from './agent-banking-activity.page-object';

const expect = chai.expect;

describe('AgentBankingActivity e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let agentBankingActivityComponentsPage: AgentBankingActivityComponentsPage;
  let agentBankingActivityUpdatePage: AgentBankingActivityUpdatePage;
  /* let agentBankingActivityDeleteDialog: AgentBankingActivityDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load AgentBankingActivities', async () => {
    await navBarPage.goToEntity('agent-banking-activity');
    agentBankingActivityComponentsPage = new AgentBankingActivityComponentsPage();
    await browser.wait(ec.visibilityOf(agentBankingActivityComponentsPage.title), 5000);
    expect(await agentBankingActivityComponentsPage.getTitle()).to.eq('Agent Banking Activities');
    await browser.wait(
      ec.or(ec.visibilityOf(agentBankingActivityComponentsPage.entities), ec.visibilityOf(agentBankingActivityComponentsPage.noResult)),
      1000
    );
  });

  it('should load create AgentBankingActivity page', async () => {
    await agentBankingActivityComponentsPage.clickOnCreateButton();
    agentBankingActivityUpdatePage = new AgentBankingActivityUpdatePage();
    expect(await agentBankingActivityUpdatePage.getPageTitle()).to.eq('Create or edit a Agent Banking Activity');
    await agentBankingActivityUpdatePage.cancel();
  });

  /* it('should create and save AgentBankingActivities', async () => {
        const nbButtonsBeforeCreate = await agentBankingActivityComponentsPage.countDeleteButtons();

        await agentBankingActivityComponentsPage.clickOnCreateButton();

        await promise.all([
            agentBankingActivityUpdatePage.setReportingDateInput('2000-12-31'),
            agentBankingActivityUpdatePage.setAgentUniqueIdInput('agentUniqueId'),
            agentBankingActivityUpdatePage.setTerminalUniqueIdInput('terminalUniqueId'),
            agentBankingActivityUpdatePage.setTotalCountOfTransactionsInput('5'),
            agentBankingActivityUpdatePage.setTotalValueOfTransactionsInLCYInput('5'),
            agentBankingActivityUpdatePage.bankCodeSelectLastOption(),
            agentBankingActivityUpdatePage.branchCodeSelectLastOption(),
            agentBankingActivityUpdatePage.transactionTypeSelectLastOption(),
        ]);

        await agentBankingActivityUpdatePage.save();
        expect(await agentBankingActivityUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await agentBankingActivityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last AgentBankingActivity', async () => {
        const nbButtonsBeforeDelete = await agentBankingActivityComponentsPage.countDeleteButtons();
        await agentBankingActivityComponentsPage.clickOnLastDeleteButton();

        agentBankingActivityDeleteDialog = new AgentBankingActivityDeleteDialog();
        expect(await agentBankingActivityDeleteDialog.getDialogTitle())
            .to.eq('Are you sure you want to delete this Agent Banking Activity?');
        await agentBankingActivityDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(agentBankingActivityComponentsPage.title), 5000);

        expect(await agentBankingActivityComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
