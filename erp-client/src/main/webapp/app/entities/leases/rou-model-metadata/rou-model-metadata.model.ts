import * as dayjs from 'dayjs';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';

export interface IRouModelMetadata {
  id?: number;
  modelTitle?: string;
  modelVersion?: number;
  description?: string | null;
  leaseTermPeriods?: number;
  leaseAmount?: number;
  rouModelReference?: string;
  commencementDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
  hasBeenFullyAmortised?: boolean | null;
  hasBeenDecommissioned?: boolean | null;
  ifrs16LeaseContract?: IIFRS16LeaseContract;
  assetAccount?: ITransactionAccount;
  depreciationAccount?: ITransactionAccount;
  accruedDepreciationAccount?: ITransactionAccount;
  assetCategory?: IAssetCategory | null;
  documentAttachments?: IBusinessDocument[] | null;
}

export class RouModelMetadata implements IRouModelMetadata {
  constructor(
    public id?: number,
    public modelTitle?: string,
    public modelVersion?: number,
    public description?: string | null,
    public leaseTermPeriods?: number,
    public leaseAmount?: number,
    public rouModelReference?: string,
    public commencementDate?: dayjs.Dayjs | null,
    public expirationDate?: dayjs.Dayjs | null,
    public hasBeenFullyAmortised?: boolean | null,
    public hasBeenDecommissioned?: boolean | null,
    public ifrs16LeaseContract?: IIFRS16LeaseContract,
    public assetAccount?: ITransactionAccount,
    public depreciationAccount?: ITransactionAccount,
    public accruedDepreciationAccount?: ITransactionAccount,
    public assetCategory?: IAssetCategory | null,
    public documentAttachments?: IBusinessDocument[] | null
  ) {
    this.hasBeenFullyAmortised = this.hasBeenFullyAmortised ?? false;
    this.hasBeenDecommissioned = this.hasBeenDecommissioned ?? false;
  }
}

export function getRouModelMetadataIdentifier(rouModelMetadata: IRouModelMetadata): number | undefined {
  return rouModelMetadata.id;
}
