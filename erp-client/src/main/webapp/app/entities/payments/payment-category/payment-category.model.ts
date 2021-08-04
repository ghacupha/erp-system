import { IPayment } from 'app/entities/payments/payment/payment.model';
import { IPaymentCalculation } from 'app/entities/payments/payment-calculation/payment-calculation.model';
import { CategoryTypes } from 'app/entities/enumerations/category-types.model';

export interface IPaymentCategory {
  id?: number;
  categoryName?: string;
  categoryDescription?: string | null;
  categoryType?: CategoryTypes;
  payment?: IPayment | null;
  paymentCalculations?: IPaymentCalculation[] | null;
}

export class PaymentCategory implements IPaymentCategory {
  constructor(
    public id?: number,
    public categoryName?: string,
    public categoryDescription?: string | null,
    public categoryType?: CategoryTypes,
    public payment?: IPayment | null,
    public paymentCalculations?: IPaymentCalculation[] | null
  ) {}
}

export function getPaymentCategoryIdentifier(paymentCategory: IPaymentCategory): number | undefined {
  return paymentCategory.id;
}
