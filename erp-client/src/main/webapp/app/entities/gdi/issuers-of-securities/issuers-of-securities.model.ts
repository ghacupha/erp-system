export interface IIssuersOfSecurities {
  id?: number;
  issuerOfSecuritiesCode?: string;
  issuerOfSecurities?: string;
  issuerOfSecuritiesDescription?: string | null;
}

export class IssuersOfSecurities implements IIssuersOfSecurities {
  constructor(
    public id?: number,
    public issuerOfSecuritiesCode?: string,
    public issuerOfSecurities?: string,
    public issuerOfSecuritiesDescription?: string | null
  ) {}
}

export function getIssuersOfSecuritiesIdentifier(issuersOfSecurities: IIssuersOfSecurities): number | undefined {
  return issuersOfSecurities.id;
}
