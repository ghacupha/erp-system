import * as dayjs from 'dayjs';
import { IPayment } from 'app/entities/payments/payment/payment.model';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { CurrencyTypes } from 'app/entities/enumerations/currency-types.model';

export interface IInvoice {
  id?: number;
  invoiceNumber?: string;
  invoiceDate?: dayjs.Dayjs | null;
  invoiceAmount?: number | null;
  currency?: CurrencyTypes;
  conversionRate?: number;
  payment?: IPayment | null;
  dealer?: IDealer | null;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invoiceNumber?: string,
    public invoiceDate?: dayjs.Dayjs | null,
    public invoiceAmount?: number | null,
    public currency?: CurrencyTypes,
    public conversionRate?: number,
    public payment?: IPayment | null,
    public dealer?: IDealer | null
  ) {}
}

export function getInvoiceIdentifier(invoice: IInvoice): number | undefined {
  return invoice.id;
}
