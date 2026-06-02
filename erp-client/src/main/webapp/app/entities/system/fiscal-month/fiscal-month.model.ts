import * as dayjs from 'dayjs';
import { IFiscalYear } from 'app/entities/system/fiscal-year/fiscal-year.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IFiscalQuarter } from 'app/entities/system/fiscal-quarter/fiscal-quarter.model';

export interface IFiscalMonth {
  id?: number;
  monthNumber?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  fiscalMonthCode?: string;
  fiscalYear?: IFiscalYear;
  placeholders?: IPlaceholder[] | null;
  universallyUniqueMappings?: IUniversallyUniqueMapping[] | null;
  fiscalQuarter?: IFiscalQuarter | null;
}

export class FiscalMonth implements IFiscalMonth {
  constructor(
    public id?: number,
    public monthNumber?: number,
    public startDate?: dayjs.Dayjs,
    public endDate?: dayjs.Dayjs,
    public fiscalMonthCode?: string,
    public fiscalYear?: IFiscalYear,
    public placeholders?: IPlaceholder[] | null,
    public universallyUniqueMappings?: IUniversallyUniqueMapping[] | null,
    public fiscalQuarter?: IFiscalQuarter | null
  ) {}
}

export function getFiscalMonthIdentifier(fiscalMonth: IFiscalMonth): number | undefined {
  return fiscalMonth.id;
}
