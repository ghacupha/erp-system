export interface IIsicEconomicActivity {
  id?: number;
  businessEconomicActivityCode?: string;
  section?: string;
  sectionLabel?: string;
  division?: string;
  divisionLabel?: string;
  groupCode?: string | null;
  groupLabel?: string;
  classCode?: string;
  businessEconomicActivityType?: string | null;
  businessEconomicActivityTypeDescription?: string | null;
}

export class IsicEconomicActivity implements IIsicEconomicActivity {
  constructor(
    public id?: number,
    public businessEconomicActivityCode?: string,
    public section?: string,
    public sectionLabel?: string,
    public division?: string,
    public divisionLabel?: string,
    public groupCode?: string | null,
    public groupLabel?: string,
    public classCode?: string,
    public businessEconomicActivityType?: string | null,
    public businessEconomicActivityTypeDescription?: string | null
  ) {}
}

export function getIsicEconomicActivityIdentifier(isicEconomicActivity: IIsicEconomicActivity): number | undefined {
  return isicEconomicActivity.id;
}
