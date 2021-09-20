export interface IPaymentLabel {
  id?: number;
  description?: string;
  comments?: string | null;
  containingPaymentLabel?: IPaymentLabel | null;
}

export class PaymentLabel implements IPaymentLabel {
  constructor(
    public id?: number,
    public description?: string,
    public comments?: string | null,
    public containingPaymentLabel?: IPaymentLabel | null
  ) {}
}

export function getPaymentLabelIdentifier(paymentLabel: IPaymentLabel): number | undefined {
  return paymentLabel.id;
}
