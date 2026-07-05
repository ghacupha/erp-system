import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { ICreditCardOwnership } from 'app/entities/gdi/credit-card-ownership/credit-card-ownership.model';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';

export interface ICreditCardFacility {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  totalNumberOfActiveCreditCards?: number;
  totalCreditCardLimitsInCCY?: number;
  totalCreditCardLimitsInLCY?: number;
  totalCreditCardAmountUtilisedInCCY?: number;
  totalCreditCardAmountUtilisedInLcy?: number;
  totalNPACreditCardAmountInFCY?: number;
  totalNPACreditCardAmountInLCY?: number;
  bankCode?: IInstitutionCode;
  customerCategory?: ICreditCardOwnership;
  currencyCode?: IIsoCurrencyCode;
}

export class CreditCardFacility implements ICreditCardFacility {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public totalNumberOfActiveCreditCards?: number,
    public totalCreditCardLimitsInCCY?: number,
    public totalCreditCardLimitsInLCY?: number,
    public totalCreditCardAmountUtilisedInCCY?: number,
    public totalCreditCardAmountUtilisedInLcy?: number,
    public totalNPACreditCardAmountInFCY?: number,
    public totalNPACreditCardAmountInLCY?: number,
    public bankCode?: IInstitutionCode,
    public customerCategory?: ICreditCardOwnership,
    public currencyCode?: IIsoCurrencyCode
  ) {}
}

export function getCreditCardFacilityIdentifier(creditCardFacility: ICreditCardFacility): number | undefined {
  return creditCardFacility.id;
}
