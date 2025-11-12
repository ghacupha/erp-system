///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
import {Store} from "@ngrx/store";
import {State} from "../../../../store/global-store.definition";
import {Resolve} from "@angular/router";
import {Observable, of} from "rxjs";
import {
  dealerInvoiceSelected,
  dealerInvoiceSelectedDealer,
} from "../../../../store/selectors/dealer-invoice-worklows-status.selectors";
import {
  DEFAULT_DATE,
  DEFAULT_DESCRIPTION,
} from "../default-values.constants";
import {dealerAcquiredForInvoicedPayment} from "../../../../store/actions/dealer-invoice-workflows-status.actions";
import { IPayment, Payment } from '../payment.model';
import { IInvoice } from '../../invoice/invoice.model';
import { IDealer } from '../../../dealers/dealer/dealer.model';

@Injectable({ providedIn: 'root' })
export class DealerInvoicePaymentResolveService implements Resolve<IPayment>  {

  constructor(protected store: Store<State>) {}

  resolve(): Observable<IPayment> | Promise<IPayment> | IPayment {

    // UPDATE DEFAULT VALUES
    let payment: IPayment = {
      ...new Payment(),
      paymentDate: DEFAULT_DATE,
      description: DEFAULT_DESCRIPTION
    };

    const invoice: Observable<IInvoice> = this.store.select<IInvoice>(dealerInvoiceSelected);

    const dealer: Observable<IDealer> = this.store.select<IDealer>(dealerInvoiceSelectedDealer);

    // TODO ownedInvoices in the store

    // UPDATE VALUES FROM THE INVOICE
    invoice.subscribe(inv => {
      payment = {
        ...payment,
        invoicedAmount: inv.invoiceAmount,
        settlementCurrency: inv.currency,
        paymentAmount: inv.invoiceAmount,
        paymentLabels: [...(inv.paymentLabels ?? [])],
        placeholders: [...(inv.placeholders ?? [])],
      }
    });

    // UPDATE WITH VALUES FROM THE DEALER
    dealer.subscribe(dealr => {
      payment = {
        ...payment,
        dealerName: dealr.dealerName,
        paymentLabels: dealr.paymentLabels,
        placeholders: dealr.placeholders,
      };

      if (dealr.paymentLabels && dealr.placeholders) {
        this.store.dispatch(dealerAcquiredForInvoicedPayment({
          paymentLabels: dealr.paymentLabels,
          placeholders: dealr.placeholders,
        }));
      }
    });

    return of(payment)
  }


}
