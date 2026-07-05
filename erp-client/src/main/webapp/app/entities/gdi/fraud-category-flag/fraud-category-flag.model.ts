import { FlagCodes } from 'app/entities/enumerations/flag-codes.model';

export interface IFraudCategoryFlag {
  id?: number;
  fraudCategoryFlag?: FlagCodes;
  fraudCategoryTypeDetails?: string | null;
}

export class FraudCategoryFlag implements IFraudCategoryFlag {
  constructor(public id?: number, public fraudCategoryFlag?: FlagCodes, public fraudCategoryTypeDetails?: string | null) {}
}

export function getFraudCategoryFlagIdentifier(fraudCategoryFlag: IFraudCategoryFlag): number | undefined {
  return fraudCategoryFlag.id;
}
