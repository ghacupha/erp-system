import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { ICardTypes } from 'app/entities/gdi/card-types/card-types.model';
import { ICardBrandType } from 'app/entities/gdi/card-brand-type/card-brand-type.model';
import { ICardCategoryType } from 'app/entities/gdi/card-category-type/card-category-type.model';
import { IBankTransactionType } from 'app/entities/gdi/bank-transaction-type/bank-transaction-type.model';
import { IChannelType } from 'app/entities/gdi/channel-type/channel-type.model';
import { ICardState } from 'app/entities/gdi-data/card-state/card-state.model';

export interface ICardUsageInformation {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  totalNumberOfLiveCards?: number;
  totalActiveCards?: number;
  totalNumberOfTransactionsDone?: number;
  totalValueOfTransactionsDoneInLCY?: number;
  bankCode?: IInstitutionCode;
  cardType?: ICardTypes;
  cardBrand?: ICardBrandType;
  cardCategoryType?: ICardCategoryType;
  transactionType?: IBankTransactionType;
  channelType?: IChannelType;
  cardState?: ICardState;
}

export class CardUsageInformation implements ICardUsageInformation {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public totalNumberOfLiveCards?: number,
    public totalActiveCards?: number,
    public totalNumberOfTransactionsDone?: number,
    public totalValueOfTransactionsDoneInLCY?: number,
    public bankCode?: IInstitutionCode,
    public cardType?: ICardTypes,
    public cardBrand?: ICardBrandType,
    public cardCategoryType?: ICardCategoryType,
    public transactionType?: IBankTransactionType,
    public channelType?: IChannelType,
    public cardState?: ICardState
  ) {}
}

export function getCardUsageInformationIdentifier(cardUsageInformation: ICardUsageInformation): number | undefined {
  return cardUsageInformation.id;
}
