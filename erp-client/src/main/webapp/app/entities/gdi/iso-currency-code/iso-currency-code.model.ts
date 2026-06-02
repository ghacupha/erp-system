export interface IIsoCurrencyCode {
  id?: number;
  alphabeticCode?: string;
  numericCode?: string;
  minorUnit?: string;
  currency?: string;
  country?: string | null;
}

export class IsoCurrencyCode implements IIsoCurrencyCode {
  constructor(
    public id?: number,
    public alphabeticCode?: string,
    public numericCode?: string,
    public minorUnit?: string,
    public currency?: string,
    public country?: string | null
  ) {}
}

export function getIsoCurrencyCodeIdentifier(isoCurrencyCode: IIsoCurrencyCode): number | undefined {
  return isoCurrencyCode.id;
}
