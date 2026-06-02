import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IBankBranchCode } from 'app/entities/system/bank-branch-code/bank-branch-code.model';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';

export interface IAccountBalance {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  customerId?: string;
  accountContractNumber?: string;
  accruedInterestBalanceFCY?: number;
  accruedInterestBalanceLCY?: number;
  accountBalanceFCY?: number;
  accountBalanceLCY?: number;
  bankCode?: IInstitutionCode;
  branchId?: IBankBranchCode;
  currencyCode?: IIsoCurrencyCode;
}

export class AccountBalance implements IAccountBalance {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public customerId?: string,
    public accountContractNumber?: string,
    public accruedInterestBalanceFCY?: number,
    public accruedInterestBalanceLCY?: number,
    public accountBalanceFCY?: number,
    public accountBalanceLCY?: number,
    public bankCode?: IInstitutionCode,
    public branchId?: IBankBranchCode,
    public currencyCode?: IIsoCurrencyCode
  ) {}
}

export function getAccountBalanceIdentifier(accountBalance: IAccountBalance): number | undefined {
  return accountBalance.id;
}
