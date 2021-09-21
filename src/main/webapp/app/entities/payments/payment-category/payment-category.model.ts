import { IPaymentLabel } from 'app/entities/payment-label/payment-label.model';
import { IPaymentCalculation } from 'app/entities/payments/payment-calculation/payment-calculation.model';
import { IPayment } from 'app/entities/payments/payment/payment.model';
import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';
import { CategoryTypes } from 'app/entities/enumerations/category-types.model';

export interface IPaymentCategory {
  id?: number;
  categoryName?: string;
  categoryDescription?: string | null;
  categoryType?: CategoryTypes;
  paymentLabels?: IPaymentLabel[] | null;
  paymentCalculations?: IPaymentCalculation[] | null;
  payments?: IPayment[] | null;
  placeholders?: IPlaceholder[] | null;
}

export class PaymentCategory implements IPaymentCategory {
  constructor(
    public id?: number,
    public categoryName?: string,
    public categoryDescription?: string | null,
    public categoryType?: CategoryTypes,
    public paymentLabels?: IPaymentLabel[] | null,
    public paymentCalculations?: IPaymentCalculation[] | null,
    public payments?: IPayment[] | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getPaymentCategoryIdentifier(paymentCategory: IPaymentCategory): number | undefined {
  return paymentCategory.id;
}
