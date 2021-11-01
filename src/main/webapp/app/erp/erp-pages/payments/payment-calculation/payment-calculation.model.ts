import {IPaymentLabel} from '../../payment-label/payment-label.model';
import {IPayment} from '../payment/payment.model';
import {IPaymentCategory} from '../payment-category/payment-category.model';
import {IPlaceholder} from '../../../../entities/erpService/placeholder/placeholder.model';

export interface IPaymentCalculation {
  id?: number;
  paymentExpense?: number | null;
  withholdingVAT?: number | null;
  withholdingTax?: number | null;
  paymentAmount?: number | null;
  paymentLabels?: IPaymentLabel[] | null;
  payment?: IPayment | null;
  paymentCategory?: IPaymentCategory | null;
  placeholders?: IPlaceholder[] | null;
}

export class PaymentCalculation implements IPaymentCalculation {
  constructor(
    public id?: number,
    public paymentExpense?: number | null,
    public withholdingVAT?: number | null,
    public withholdingTax?: number | null,
    public paymentAmount?: number | null,
    public paymentLabels?: IPaymentLabel[] | null,
    public payment?: IPayment | null,
    public paymentCategory?: IPaymentCategory | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getPaymentCalculationIdentifier(paymentCalculation: IPaymentCalculation): number | undefined {
  return paymentCalculation.id;
}
