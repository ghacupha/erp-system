export interface IFxReceiptPurposeType {
  id?: number;
  itemCode?: string;
  attribute1ReceiptPaymentPurposeCode?: string | null;
  attribute1ReceiptPaymentPurposeType?: string | null;
  attribute2ReceiptPaymentPurposeCode?: string | null;
  attribute2ReceiptPaymentPurposeDescription?: string | null;
  attribute3ReceiptPaymentPurposeCode?: string | null;
  attribute3ReceiptPaymentPurposeDescription?: string | null;
  attribute4ReceiptPaymentPurposeCode?: string | null;
  attribute4ReceiptPaymentPurposeDescription?: string | null;
  attribute5ReceiptPaymentPurposeCode?: string | null;
  attribute5ReceiptPaymentPurposeDescription?: string | null;
  lastChild?: string | null;
}

export class FxReceiptPurposeType implements IFxReceiptPurposeType {
  constructor(
    public id?: number,
    public itemCode?: string,
    public attribute1ReceiptPaymentPurposeCode?: string | null,
    public attribute1ReceiptPaymentPurposeType?: string | null,
    public attribute2ReceiptPaymentPurposeCode?: string | null,
    public attribute2ReceiptPaymentPurposeDescription?: string | null,
    public attribute3ReceiptPaymentPurposeCode?: string | null,
    public attribute3ReceiptPaymentPurposeDescription?: string | null,
    public attribute4ReceiptPaymentPurposeCode?: string | null,
    public attribute4ReceiptPaymentPurposeDescription?: string | null,
    public attribute5ReceiptPaymentPurposeCode?: string | null,
    public attribute5ReceiptPaymentPurposeDescription?: string | null,
    public lastChild?: string | null
  ) {}
}

export function getFxReceiptPurposeTypeIdentifier(fxReceiptPurposeType: IFxReceiptPurposeType): number | undefined {
  return fxReceiptPurposeType.id;
}
