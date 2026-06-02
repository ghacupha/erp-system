import { CreditCardOwnershipTypes } from 'app/entities/enumerations/credit-card-ownership-types.model';

export interface ICreditCardOwnership {
  id?: number;
  creditCardOwnershipCategoryCode?: string;
  creditCardOwnershipCategoryType?: CreditCardOwnershipTypes;
  description?: string | null;
}

export class CreditCardOwnership implements ICreditCardOwnership {
  constructor(
    public id?: number,
    public creditCardOwnershipCategoryCode?: string,
    public creditCardOwnershipCategoryType?: CreditCardOwnershipTypes,
    public description?: string | null
  ) {}
}

export function getCreditCardOwnershipIdentifier(creditCardOwnership: ICreditCardOwnership): number | undefined {
  return creditCardOwnership.id;
}
