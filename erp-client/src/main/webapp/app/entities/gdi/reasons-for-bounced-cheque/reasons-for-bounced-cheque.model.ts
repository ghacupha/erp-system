export interface IReasonsForBouncedCheque {
  id?: number;
  bouncedChequeReasonsTypeCode?: string;
  bouncedChequeReasonsType?: string | null;
}

export class ReasonsForBouncedCheque implements IReasonsForBouncedCheque {
  constructor(public id?: number, public bouncedChequeReasonsTypeCode?: string, public bouncedChequeReasonsType?: string | null) {}
}

export function getReasonsForBouncedChequeIdentifier(reasonsForBouncedCheque: IReasonsForBouncedCheque): number | undefined {
  return reasonsForBouncedCheque.id;
}
