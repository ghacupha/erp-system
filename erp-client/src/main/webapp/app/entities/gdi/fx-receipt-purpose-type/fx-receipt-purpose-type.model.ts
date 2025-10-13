///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

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
