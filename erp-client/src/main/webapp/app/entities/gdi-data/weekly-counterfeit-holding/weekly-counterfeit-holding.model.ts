import * as dayjs from 'dayjs';

export interface IWeeklyCounterfeitHolding {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  dateConfiscated?: dayjs.Dayjs;
  serialNumber?: string;
  depositorsNames?: string;
  tellersNames?: string;
  dateSubmittedToCBK?: dayjs.Dayjs;
  remarks?: string | null;
}

export class WeeklyCounterfeitHolding implements IWeeklyCounterfeitHolding {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public dateConfiscated?: dayjs.Dayjs,
    public serialNumber?: string,
    public depositorsNames?: string,
    public tellersNames?: string,
    public dateSubmittedToCBK?: dayjs.Dayjs,
    public remarks?: string | null
  ) {}
}

export function getWeeklyCounterfeitHoldingIdentifier(weeklyCounterfeitHolding: IWeeklyCounterfeitHolding): number | undefined {
  return weeklyCounterfeitHolding.id;
}
