///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import {Injectable} from "@angular/core";
import {InvoiceService} from "../service/invoice.service";
import {Resolve, Router} from "@angular/router";
import {Observable, of} from "rxjs";
import {select, Store} from "@ngrx/store";
import {State} from "../../../../store/global-store.definition";
import {DEFAULT_CONVERSION_RATE, DEFAULT_CURRENCY} from "../../payment/default-values.constants";
import {dealerInvoiceSelectedDealer} from "../../../../store/selectors/dealer-invoice-worklows-status.selectors";
import { IInvoice, Invoice } from '../invoice.model';
import { IDealer } from '../../../dealers/dealer/dealer.model';

@Injectable({ providedIn: 'root' })
export class DealerInvoiceRoutingResolveService implements Resolve<IInvoice>  {

  constructor(protected service: InvoiceService, protected router: Router, protected store: Store<State>) {}

  resolve(): Observable<IInvoice> | Observable<never> {

    const invoiceDealer: Observable<IDealer> = this.store.pipe(select(dealerInvoiceSelectedDealer)).pipe();

    let invoice = {
      ...new Invoice(),
      currency: DEFAULT_CURRENCY,
      conversionRate: DEFAULT_CONVERSION_RATE,
    }

    invoiceDealer.subscribe(dealer => {
      invoice = {
        ...invoice,
        dealerName: dealer.dealerName,
        paymentLabels: dealer.paymentLabels,
        placeholders: dealer.placeholders,
      }
    });

    return of(invoice);
  }
}
