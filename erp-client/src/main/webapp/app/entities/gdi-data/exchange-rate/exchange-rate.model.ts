import * as dayjs from 'dayjs';
import { IInstitutionCode } from 'app/entities/gdi/institution-code/institution-code.model';
import { IIsoCurrencyCode } from 'app/entities/gdi/iso-currency-code/iso-currency-code.model';

export interface IExchangeRate {
  id?: number;
  businessReportingDay?: dayjs.Dayjs;
  buyingRate?: number;
  sellingRate?: number;
  meanRate?: number;
  closingBidRate?: number;
  closingOfferRate?: number;
  usdCrossRate?: number;
  institutionCode?: IInstitutionCode;
  currencyCode?: IIsoCurrencyCode;
}

export class ExchangeRate implements IExchangeRate {
  constructor(
    public id?: number,
    public businessReportingDay?: dayjs.Dayjs,
    public buyingRate?: number,
    public sellingRate?: number,
    public meanRate?: number,
    public closingBidRate?: number,
    public closingOfferRate?: number,
    public usdCrossRate?: number,
    public institutionCode?: IInstitutionCode,
    public currencyCode?: IIsoCurrencyCode
  ) {}
}

export function getExchangeRateIdentifier(exchangeRate: IExchangeRate): number | undefined {
  return exchangeRate.id;
}
