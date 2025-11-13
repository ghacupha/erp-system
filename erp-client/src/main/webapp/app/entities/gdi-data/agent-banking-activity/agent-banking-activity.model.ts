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

import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { IBankTransactionType } from 'app/entities/gdi/bank-transaction-type/bank-transaction-type.model';

export interface IAgentBankingActivity {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  agentUniqueId?: string;
  terminalUniqueId?: string;
  totalCountOfTransactions?: number;
  totalValueOfTransactionsInLCY?: number;
  bankCode?: IInstitutionCode;
  branchCode?: IBankBranchCode;
  transactionType?: IBankTransactionType;
}

export class AgentBankingActivity implements IAgentBankingActivity {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public agentUniqueId?: string,
    public terminalUniqueId?: string,
    public totalCountOfTransactions?: number,
    public totalValueOfTransactionsInLCY?: number,
    public bankCode?: IInstitutionCode,
    public branchCode?: IBankBranchCode,
    public transactionType?: IBankTransactionType
  ) {}
}

export function getAgentBankingActivityIdentifier(agentBankingActivity: IAgentBankingActivity): number | undefined {
  return agentBankingActivity.id;
}
