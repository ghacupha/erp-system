import * as dayjs from 'dayjs';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { IDepreciationMethod } from 'app/entities/assets/depreciation-method/depreciation-method.model';
import { IAssetRegistration } from 'app/entities/assets/asset-registration/asset-registration.model';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { IFiscalQuarter } from 'app/entities/system/fiscal-quarter/fiscal-quarter.model';
import { IFiscalYear } from 'app/entities/system/fiscal-year/fiscal-year.model';
import { IDepreciationJob } from 'app/entities/assets/depreciation-job/depreciation-job.model';
import { IDepreciationBatchSequence } from 'app/entities/assets/depreciation-batch-sequence/depreciation-batch-sequence.model';

export interface IDepreciationEntry {
  id?: number;
  postedAt?: dayjs.Dayjs | null;
  depreciationAmount?: number | null;
  assetNumber?: number | null;
  batchSequenceNumber?: number | null;
  processedItems?: string | null;
  totalItemsProcessed?: number | null;
  serviceOutlet?: IServiceOutlet | null;
  assetCategory?: IAssetCategory | null;
  depreciationMethod?: IDepreciationMethod | null;
  assetRegistration?: IAssetRegistration | null;
  depreciationPeriod?: IDepreciationPeriod | null;
  fiscalMonth?: IFiscalMonth | null;
  fiscalQuarter?: IFiscalQuarter | null;
  fiscalYear?: IFiscalYear | null;
  depreciationJob?: IDepreciationJob | null;
  depreciationBatchSequence?: IDepreciationBatchSequence | null;
}

export class DepreciationEntry implements IDepreciationEntry {
  constructor(
    public id?: number,
    public postedAt?: dayjs.Dayjs | null,
    public depreciationAmount?: number | null,
    public assetNumber?: number | null,
    public batchSequenceNumber?: number | null,
    public processedItems?: string | null,
    public totalItemsProcessed?: number | null,
    public serviceOutlet?: IServiceOutlet | null,
    public assetCategory?: IAssetCategory | null,
    public depreciationMethod?: IDepreciationMethod | null,
    public assetRegistration?: IAssetRegistration | null,
    public depreciationPeriod?: IDepreciationPeriod | null,
    public fiscalMonth?: IFiscalMonth | null,
    public fiscalQuarter?: IFiscalQuarter | null,
    public fiscalYear?: IFiscalYear | null,
    public depreciationJob?: IDepreciationJob | null,
    public depreciationBatchSequence?: IDepreciationBatchSequence | null
  ) {}
}

export function getDepreciationEntryIdentifier(depreciationEntry: IDepreciationEntry): number | undefined {
  return depreciationEntry.id;
}
