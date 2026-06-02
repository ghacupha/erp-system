export interface ISourcesOfFundsTypeCode {
  id?: number;
  sourceOfFundsTypeCode?: string;
  sourceOfFundsType?: string;
  sourceOfFundsTypeDetails?: string | null;
}

export class SourcesOfFundsTypeCode implements ISourcesOfFundsTypeCode {
  constructor(
    public id?: number,
    public sourceOfFundsTypeCode?: string,
    public sourceOfFundsType?: string,
    public sourceOfFundsTypeDetails?: string | null
  ) {}
}

export function getSourcesOfFundsTypeCodeIdentifier(sourcesOfFundsTypeCode: ISourcesOfFundsTypeCode): number | undefined {
  return sourcesOfFundsTypeCode.id;
}
