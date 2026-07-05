import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { IDealer } from 'app/entities/people/dealer/dealer.model';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';

export interface ILeaseTemplate {
  id?: number;
  templateTitle?: string;
  assetAccount?: ITransactionAccount | null;
  depreciationAccount?: ITransactionAccount | null;
  accruedDepreciationAccount?: ITransactionAccount | null;
  interestPaidTransferDebitAccount?: ITransactionAccount | null;
  interestPaidTransferCreditAccount?: ITransactionAccount | null;
  interestAccruedDebitAccount?: ITransactionAccount | null;
  interestAccruedCreditAccount?: ITransactionAccount | null;
  leaseRecognitionDebitAccount?: ITransactionAccount | null;
  leaseRecognitionCreditAccount?: ITransactionAccount | null;
  leaseRepaymentDebitAccount?: ITransactionAccount | null;
  leaseRepaymentCreditAccount?: ITransactionAccount | null;
  rouRecognitionCreditAccount?: ITransactionAccount | null;
  rouRecognitionDebitAccount?: ITransactionAccount | null;
  assetCategory?: IAssetCategory | null;
  serviceOutlet?: IServiceOutlet | null;
  mainDealer?: IDealer | null;
  leaseContracts?: IIFRS16LeaseContract[] | null;
}

export class LeaseTemplate implements ILeaseTemplate {
  constructor(
    public id?: number,
    public templateTitle?: string,
    public assetAccount?: ITransactionAccount | null,
    public depreciationAccount?: ITransactionAccount | null,
    public accruedDepreciationAccount?: ITransactionAccount | null,
    public interestPaidTransferDebitAccount?: ITransactionAccount | null,
    public interestPaidTransferCreditAccount?: ITransactionAccount | null,
    public interestAccruedDebitAccount?: ITransactionAccount | null,
    public interestAccruedCreditAccount?: ITransactionAccount | null,
    public leaseRecognitionDebitAccount?: ITransactionAccount | null,
    public leaseRecognitionCreditAccount?: ITransactionAccount | null,
    public leaseRepaymentDebitAccount?: ITransactionAccount | null,
    public leaseRepaymentCreditAccount?: ITransactionAccount | null,
    public rouRecognitionCreditAccount?: ITransactionAccount | null,
    public rouRecognitionDebitAccount?: ITransactionAccount | null,
    public assetCategory?: IAssetCategory | null,
    public serviceOutlet?: IServiceOutlet | null,
    public mainDealer?: IDealer | null,
    public leaseContracts?: IIFRS16LeaseContract[] | null
  ) {}
}

export function getLeaseTemplateIdentifier(leaseTemplate: ILeaseTemplate): number | undefined {
  return leaseTemplate.id;
}
