import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';

export interface IReportingEntity {
  id?: number;
  entityName?: string;
  reportingCurrency?: ISettlementCurrency | null;
  retainedEarningsAccount?: ITransactionAccount | null;
}

export class ReportingEntity implements IReportingEntity {
  constructor(
    public id?: number,
    public entityName?: string,
    public reportingCurrency?: ISettlementCurrency | null,
    public retainedEarningsAccount?: ITransactionAccount | null
  ) {}
}

export function getReportingEntityIdentifier(reportingEntity: IReportingEntity): number | undefined {
  return reportingEntity.id;
}
