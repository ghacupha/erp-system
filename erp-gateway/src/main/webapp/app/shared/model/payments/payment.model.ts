import { Moment } from 'moment';
import { IInvoice } from 'app/shared/model/payments/invoice.model';
import { IDealer } from 'app/shared/model/dealers/dealer.model';

export interface IPayment {
  id?: number;
  paymentNumber?: string;
  paymentDate?: Moment;
  paymentAmount?: number;
  dealerName?: string;
  paymentCategory?: string;
  ownedInvoices?: IInvoice[];
  paymentCalculationId?: number;
  paymentRequisitionId?: number;
  dealers?: IDealer[];
  taxRuleId?: number;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public paymentNumber?: string,
    public paymentDate?: Moment,
    public paymentAmount?: number,
    public dealerName?: string,
    public paymentCategory?: string,
    public ownedInvoices?: IInvoice[],
    public paymentCalculationId?: number,
    public paymentRequisitionId?: number,
    public dealers?: IDealer[],
    public taxRuleId?: number
  ) {}
}
