import { IGdiMasterDataIndex } from 'app/entities/gdi/gdi-master-data-index/gdi-master-data-index.model';
import { UpdateFrequencyTypes } from 'app/entities/enumerations/update-frequency-types.model';
import { DatasetBehaviorTypes } from 'app/entities/enumerations/dataset-behavior-types.model';

export interface IGdiTransactionDataIndex {
  id?: number;
  datasetName?: string;
  databaseName?: string;
  updateFrequency?: UpdateFrequencyTypes;
  datasetBehavior?: DatasetBehaviorTypes;
  minimumDatarowsPerRequest?: number | null;
  maximumDataRowsPerRequest?: number | null;
  datasetDescription?: string | null;
  dataTemplateContentType?: string | null;
  dataTemplate?: string | null;
  masterDataItems?: IGdiMasterDataIndex[] | null;
}

export class GdiTransactionDataIndex implements IGdiTransactionDataIndex {
  constructor(
    public id?: number,
    public datasetName?: string,
    public databaseName?: string,
    public updateFrequency?: UpdateFrequencyTypes,
    public datasetBehavior?: DatasetBehaviorTypes,
    public minimumDatarowsPerRequest?: number | null,
    public maximumDataRowsPerRequest?: number | null,
    public datasetDescription?: string | null,
    public dataTemplateContentType?: string | null,
    public dataTemplate?: string | null,
    public masterDataItems?: IGdiMasterDataIndex[] | null
  ) {}
}

export function getGdiTransactionDataIndexIdentifier(gdiTransactionDataIndex: IGdiTransactionDataIndex): number | undefined {
  return gdiTransactionDataIndex.id;
}
