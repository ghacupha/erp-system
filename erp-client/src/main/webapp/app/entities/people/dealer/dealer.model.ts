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

import { IPaymentLabel } from 'app/entities/settlement/payment-label/payment-label.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IDealer {
  id?: number;
  dealerName?: string;
  taxNumber?: string | null;
  identificationDocumentNumber?: string | null;
  organizationName?: string | null;
  department?: string | null;
  position?: string | null;
  postalAddress?: string | null;
  physicalAddress?: string | null;
  accountName?: string | null;
  accountNumber?: string | null;
  bankersName?: string | null;
  bankersBranch?: string | null;
  bankersSwiftCode?: string | null;
  fileUploadToken?: string | null;
  compilationToken?: string | null;
  remarks?: string | null;
  otherNames?: string | null;
  paymentLabels?: IPaymentLabel[] | null;
  dealerGroup?: IDealer | null;
  placeholders?: IPlaceholder[] | null;
}

export class Dealer implements IDealer {
  constructor(
    public id?: number,
    public dealerName?: string,
    public taxNumber?: string | null,
    public identificationDocumentNumber?: string | null,
    public organizationName?: string | null,
    public department?: string | null,
    public position?: string | null,
    public postalAddress?: string | null,
    public physicalAddress?: string | null,
    public accountName?: string | null,
    public accountNumber?: string | null,
    public bankersName?: string | null,
    public bankersBranch?: string | null,
    public bankersSwiftCode?: string | null,
    public fileUploadToken?: string | null,
    public compilationToken?: string | null,
    public remarks?: string | null,
    public otherNames?: string | null,
    public paymentLabels?: IPaymentLabel[] | null,
    public dealerGroup?: IDealer | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getDealerIdentifier(dealer: IDealer): number | undefined {
  return dealer.id;
}
