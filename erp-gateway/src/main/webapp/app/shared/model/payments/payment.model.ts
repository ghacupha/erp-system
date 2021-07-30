import { Moment } from 'moment';
import { IInvoice } from 'app/shared/model/payments/invoice.model';
import { IDealer } from 'app/shared/model/dealers/dealer.model';

export interface IPayment {
  id?: number;
  paymentNumber?: string;
  paymentDate?: Moment;
  paymentAmount?: number;
  description?: string;
  ownedInvoices?: IInvoice[];
  paymentRequisitionId?: number;
  dealers?: IDealer[];
  taxRuleId?: number;
  paymentCategoryId?: number;
  paymentCalculationId?: number;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public paymentNumber?: string,
    public paymentDate?: Moment,
    public paymentAmount?: number,
    public description?: string,
    public ownedInvoices?: IInvoice[],
    public paymentRequisitionId?: number,
    public dealers?: IDealer[],
    public taxRuleId?: number,
    public paymentCategoryId?: number,
    public paymentCalculationId?: number
  ) {}
}
