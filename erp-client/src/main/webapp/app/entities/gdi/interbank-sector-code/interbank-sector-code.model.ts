export interface IInterbankSectorCode {
  id?: number;
  interbankSectorCode?: string;
  interbankSectorCodeDescription?: string | null;
}

export class InterbankSectorCode implements IInterbankSectorCode {
  constructor(public id?: number, public interbankSectorCode?: string, public interbankSectorCodeDescription?: string | null) {}
}

export function getInterbankSectorCodeIdentifier(interbankSectorCode: IInterbankSectorCode): number | undefined {
  return interbankSectorCode.id;
}
