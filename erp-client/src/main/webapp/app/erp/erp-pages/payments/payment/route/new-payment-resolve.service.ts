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


import {ActivatedRouteSnapshot, Resolve} from "@angular/router";
import {Store} from "@ngrx/store";
import {State} from "../../../../store/global-store.definition";
import {Observable, of} from "rxjs";
import {newPaymentButtonClicked} from "../../../../store/actions/update-menu-status.actions";
import {
  DEFAULT_CURRENCY,
  DEFAULT_DATE,
  DEFAULT_DESCRIPTION,
  DEFAULT_INVOICE_AMOUNT,
  DEFAULT_TRANSACTION_AMOUNT,
} from "../default-values.constants";
import {Injectable} from "@angular/core";
import { IPayment, Payment } from '../payment.model';

/**
 * Provides the New Payment form with default values
 */
@Injectable({ providedIn: 'root' })
export class NewPaymentResolveService implements Resolve<IPayment> {

  constructor(
    protected store: Store<State>
  ) {
  }


  /* eslint-disable-next-line */
  resolve(route: ActivatedRouteSnapshot): Observable<IPayment> | Observable<never> {

    const payment: Payment = {
      paymentDate: DEFAULT_DATE,
      settlementCurrency: DEFAULT_CURRENCY,
      paymentAmount: DEFAULT_TRANSACTION_AMOUNT,
      invoicedAmount: DEFAULT_INVOICE_AMOUNT,
      description: DEFAULT_DESCRIPTION
    }

    this.store.dispatch(newPaymentButtonClicked({newPayment: payment}))

    return of(payment);
  }
}
