export interface IInterestCalcMethod {
  id?: number;
  interestCalculationMethodCode?: string;
  interestCalculationMthodType?: string;
  interestCalculationMethodDetails?: string | null;
}

export class InterestCalcMethod implements IInterestCalcMethod {
  constructor(
    public id?: number,
    public interestCalculationMethodCode?: string,
    public interestCalculationMthodType?: string,
    public interestCalculationMethodDetails?: string | null
  ) {}
}

export function getInterestCalcMethodIdentifier(interestCalcMethod: IInterestCalcMethod): number | undefined {
  return interestCalcMethod.id;
}
