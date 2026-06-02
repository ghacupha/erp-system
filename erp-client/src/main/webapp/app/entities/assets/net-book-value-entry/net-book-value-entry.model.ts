import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { IDepreciationPeriod } from 'app/entities/assets/depreciation-period/depreciation-period.model';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { IDepreciationMethod } from 'app/entities/assets/depreciation-method/depreciation-method.model';
import { IAssetRegistration } from 'app/entities/assets/asset-registration/asset-registration.model';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface INetBookValueEntry {
  id?: number;
  assetNumber?: string | null;
  assetTag?: string | null;
  assetDescription?: string | null;
  nbvIdentifier?: string;
  compilationJobIdentifier?: string | null;
  compilationBatchIdentifier?: string | null;
  elapsedMonths?: number | null;
  priorMonths?: number | null;
  usefulLifeYears?: number | null;
  netBookValueAmount?: number | null;
  previousNetBookValueAmount?: number | null;
  historicalCost?: number | null;
  serviceOutlet?: IServiceOutlet | null;
  depreciationPeriod?: IDepreciationPeriod | null;
  fiscalMonth?: IFiscalMonth | null;
  depreciationMethod?: IDepreciationMethod | null;
  assetRegistration?: IAssetRegistration | null;
  assetCategory?: IAssetCategory | null;
  placeholders?: IPlaceholder[] | null;
}

export class NetBookValueEntry implements INetBookValueEntry {
  constructor(
    public id?: number,
    public assetNumber?: string | null,
    public assetTag?: string | null,
    public assetDescription?: string | null,
    public nbvIdentifier?: string,
    public compilationJobIdentifier?: string | null,
    public compilationBatchIdentifier?: string | null,
    public elapsedMonths?: number | null,
    public priorMonths?: number | null,
    public usefulLifeYears?: number | null,
    public netBookValueAmount?: number | null,
    public previousNetBookValueAmount?: number | null,
    public historicalCost?: number | null,
    public serviceOutlet?: IServiceOutlet | null,
    public depreciationPeriod?: IDepreciationPeriod | null,
    public fiscalMonth?: IFiscalMonth | null,
    public depreciationMethod?: IDepreciationMethod | null,
    public assetRegistration?: IAssetRegistration | null,
    public assetCategory?: IAssetCategory | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getNetBookValueEntryIdentifier(netBookValueEntry: INetBookValueEntry): number | undefined {
  return netBookValueEntry.id;
}
