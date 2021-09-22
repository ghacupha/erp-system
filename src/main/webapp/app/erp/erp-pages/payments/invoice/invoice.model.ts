import * as dayjs from 'dayjs';
import { IPaymentLabel } from 'app/entities/payment-label/payment-label.model';
import { IPayment } from 'app/entities/payments/payment/payment.model';
import { IDealer } from 'app/entities/dealers/dealer/dealer.model';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { CurrencyTypes } from 'app/entities/enumerations/currency-types.model';

export interface IInvoice {
  id?: number;
  invoiceNumber?: string;
  invoiceDate?: dayjs.Dayjs | null;
  invoiceAmount?: number | null;
  currency?: CurrencyTypes;
  conversionRate?: number;
  paymentLabels?: IPaymentLabel[] | null;
  payment?: IPayment | null;
  dealer?: IDealer | null;
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
    public payment?: IPayment | null,
    public dealer?: IDealer | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getInvoiceIdentifier(invoice: IInvoice): number | undefined {
  return invoice.id;
}
