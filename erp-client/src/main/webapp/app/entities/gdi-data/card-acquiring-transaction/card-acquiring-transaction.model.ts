import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IChannelType } from 'app/entities/gdi/channel-type/channel-type.model';
import { ICardBrandType } from 'app/entities/gdi/card-brand-type/card-brand-type.model';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';
import { ICardCategoryType } from 'app/entities/gdi/card-category-type/card-category-type.model';

export interface ICardAcquiringTransaction {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  terminalId?: string;
  numberOfTransactions?: number;
  valueOfTransactionsInLCY?: number;
  bankCode?: IInstitutionCode;
  channelType?: IChannelType;
  cardBrandType?: ICardBrandType;
  currencyOfTransaction?: IIsoCurrencyCode;
  cardIssuerCategory?: ICardCategoryType;
}

export class CardAcquiringTransaction implements ICardAcquiringTransaction {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public terminalId?: string,
    public numberOfTransactions?: number,
    public valueOfTransactionsInLCY?: number,
    public bankCode?: IInstitutionCode,
    public channelType?: IChannelType,
    public cardBrandType?: ICardBrandType,
    public currencyOfTransaction?: IIsoCurrencyCode,
    public cardIssuerCategory?: ICardCategoryType
  ) {}
}

export function getCardAcquiringTransactionIdentifier(cardAcquiringTransaction: ICardAcquiringTransaction): number | undefined {
  return cardAcquiringTransaction.id;
}
