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
