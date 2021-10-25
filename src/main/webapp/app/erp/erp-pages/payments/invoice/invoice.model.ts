import * as dayjs from 'dayjs';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { CurrencyTypes } from 'app/entities/enumerations/currency-types.model';
import {IPaymentLabel} from "../../payment-label/payment-label.model";

export interface IInvoice {
  id?: number;
  invoiceNumber?: string;
  invoiceDate?: dayjs.Dayjs | null;
  invoiceAmount?: number | null;
  currency?: CurrencyTypes;
  conversionRate?: number;
  paymentLabels?: IPaymentLabel[] | null;
  paymentId?: number | null;
  dealerId?: number | null;
  placeholders?: IPlaceholder[] | null;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invoiceNumber?: string,
    public invoiceDate?: dayjs.Dayjs | null,
    public invoiceAmount?: number | null,
    public currency?: CurrencyTypes,
    public conversionRate?: number,
    public paymentLabels?: IPaymentLabel[] | null,
    public paymentId?: number | null,
    public dealerId?: number | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getInvoiceIdentifier(invoice: IInvoice): number | undefined {
  return invoice.id;
}
