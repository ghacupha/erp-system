import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';

export interface IPaymentLabel {
  id?: number;
  description?: string;
  comments?: string | null;
  fileUploadToken?: string | null;
  compilationToken?: string | null;
  containingPaymentLabel?: IPaymentLabel | null;
  placeholders?: IPlaceholder[] | null;
}

export class PaymentLabel implements IPaymentLabel {
  constructor(
    public id?: number,
    public description?: string,
    public comments?: string | null,
    public fileUploadToken?: string | null,
    public compilationToken?: string | null,
    public containingPaymentLabel?: IPaymentLabel | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getPaymentLabelIdentifier(paymentLabel: IPaymentLabel): number | undefined {
  return paymentLabel.id;
}
