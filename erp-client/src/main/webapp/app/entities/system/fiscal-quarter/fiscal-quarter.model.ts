import * as dayjs from 'dayjs';
import { IFiscalYear } from 'app/entities/system/fiscal-year/fiscal-year.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';

export interface IFiscalQuarter {
  id?: number;
  quarterNumber?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  fiscalQuarterCode?: string;
  fiscalYear?: IFiscalYear;
  placeholders?: IPlaceholder[] | null;
  universallyUniqueMappings?: IUniversallyUniqueMapping[] | null;
}

export class FiscalQuarter implements IFiscalQuarter {
  constructor(
    public id?: number,
    public quarterNumber?: number,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public fiscalQuarterCode?: string,
    public fiscalYear?: IFiscalYear,
    public placeholders?: IPlaceholder[] | null,
    public universallyUniqueMappings?: IUniversallyUniqueMapping[] | null
  ) {}
}

export function getFiscalQuarterIdentifier(fiscalQuarter: IFiscalQuarter): number | undefined {
  return fiscalQuarter.id;
}
