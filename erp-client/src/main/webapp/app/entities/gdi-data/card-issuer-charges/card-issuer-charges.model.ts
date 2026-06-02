import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { ICardCategoryType } from 'app/entities/gdi/card-category-type/card-category-type.model';
import { ICardTypes } from 'app/entities/gdi/card-types/card-types.model';
import { ICardBrandType } from 'app/entities/gdi/card-brand-type/card-brand-type.model';
import { ICardClassType } from 'app/entities/gdi/card-class-type/card-class-type.model';
import { ICardCharges } from 'app/entities/gdi/card-charges/card-charges.model';

export interface ICardIssuerCharges {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  cardFeeChargeInLCY?: number;
  bankCode?: IInstitutionCode;
  cardCategory?: ICardCategoryType;
  cardType?: ICardTypes;
  cardBrand?: ICardBrandType;
  cardClass?: ICardClassType;
  cardChargeType?: ICardCharges;
}

export class CardIssuerCharges implements ICardIssuerCharges {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public cardFeeChargeInLCY?: number,
    public bankCode?: IInstitutionCode,
    public cardCategory?: ICardCategoryType,
    public cardType?: ICardTypes,
    public cardBrand?: ICardBrandType,
    public cardClass?: ICardClassType,
    public cardChargeType?: ICardCharges
  ) {}
}

export function getCardIssuerChargesIdentifier(cardIssuerCharges: ICardIssuerCharges): number | undefined {
  return cardIssuerCharges.id;
}
