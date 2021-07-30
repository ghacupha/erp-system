import { CategoryTypes } from 'app/shared/model/enumerations/category-types.model';

export interface IPaymentCategory {
  id?: number;
  categoryName?: string;
  categoryDescription?: string;
  categoryType?: CategoryTypes;
  paymentId?: number;
}

export class PaymentCategory implements IPaymentCategory {
  constructor(
    public id?: number,
    public categoryName?: string,
    public categoryDescription?: string,
    public categoryType?: CategoryTypes,
    public paymentId?: number
  ) {}
}
