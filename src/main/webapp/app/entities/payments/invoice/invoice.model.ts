import * as dayjs from 'dayjs';
import { IPaymentLabel } from 'app/entities/payment-label/payment-label.model';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { CurrencyTypes } from 'app/entities/enumerations/currency-types.model';

export interface IInvoice {
  id?: number;
  invoiceNumber?: string;
  invoiceDate?: dayjs.Dayjs | null;
  invoiceAmount?: number | null;
  currency?: CurrencyTypes;
  conversionRate?: number;
  paymentId?: number | null;
  dealerId?: number | null;
  fileUploadToken?: string | null;
  compilationToken?: string | null;
  paymentLabels?: IPaymentLabel[] | null;
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
    public paymentId?: number | null,
    public dealerId?: number | null,
    public fileUploadToken?: string | null,
    public compilationToken?: string | null,
    public paymentLabels?: IPaymentLabel[] | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getInvoiceIdentifier(invoice: IInvoice): number | undefined {
  return invoice.id;
}
