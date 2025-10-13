///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
