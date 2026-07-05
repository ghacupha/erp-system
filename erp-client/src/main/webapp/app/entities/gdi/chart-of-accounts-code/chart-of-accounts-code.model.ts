export interface IChartOfAccountsCode {
  id?: number;
  chartOfAccountsCode?: string;
  chartOfAccountsClass?: string;
  description?: string | null;
}

export class ChartOfAccountsCode implements IChartOfAccountsCode {
  constructor(
    public id?: number,
    public chartOfAccountsCode?: string,
    public chartOfAccountsClass?: string,
    public description?: string | null
  ) {}
}

export function getChartOfAccountsCodeIdentifier(chartOfAccountsCode: IChartOfAccountsCode): number | undefined {
  return chartOfAccountsCode.id;
}
