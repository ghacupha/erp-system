import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IRouModelMetadata } from 'app/entities/leases/rou-model-metadata/rou-model-metadata.model';

export interface IRouDepreciationEntry {
  id?: number;
  description?: string | null;
  depreciationAmount?: number;
  outstandingAmount?: number;
  rouAssetIdentifier?: string | null;
  rouDepreciationIdentifier?: string;
  sequenceNumber?: number | null;
  invalidated?: boolean | null;
  debitAccount?: ITransactionAccount;
  creditAccount?: ITransactionAccount;
  assetCategory?: IAssetCategory;
  leaseContract?: IIFRS16LeaseContract;
  rouMetadata?: IRouModelMetadata;
}

export class RouDepreciationEntry implements IRouDepreciationEntry {
  constructor(
    public id?: number,
    public description?: string | null,
    public depreciationAmount?: number,
    public outstandingAmount?: number,
    public rouAssetIdentifier?: string | null,
    public rouDepreciationIdentifier?: string,
    public sequenceNumber?: number | null,
    public invalidated?: boolean | null,
    public debitAccount?: ITransactionAccount,
    public creditAccount?: ITransactionAccount,
    public assetCategory?: IAssetCategory,
    public leaseContract?: IIFRS16LeaseContract,
    public rouMetadata?: IRouModelMetadata
  ) {
    this.invalidated = this.invalidated ?? false;
  }
}

export function getRouDepreciationEntryIdentifier(rouDepreciationEntry: IRouDepreciationEntry): number | undefined {
  return rouDepreciationEntry.id;
}
