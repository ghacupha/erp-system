import {Injectable} from '@angular/core';
import {Resolve} from '@angular/router';
import {IPayment, Payment} from '../payment.model';
import {Store} from '@ngrx/store';
import {State} from '../../../../store/global-store.definition';
import {Observable, of} from 'rxjs';
import {
  DEEFAULT_CONVERSION_RATE,
  DEFAULT_CURRENCY,
  DEFAULT_DATE, DEFAULT_DESCRIPTION, DEFAULT_DISBURSEMENT_COST,
  DEFAULT_INVOICE_AMOUNT,
  DEFAULT_TRANSACTION_AMOUNT, DEFAULT_VATABLE_AMOUNT
} from '../default-values.constants';
import {paymentToDealerInitiated} from '../../../../store/actions/dealer-workflows-status.actions';

@Injectable({ providedIn: 'root' })
export class DealerPaymentResolveService implements Resolve<IPayment> {
  constructor(
    protected store: Store<State>
  ) {}

  resolve(): Observable<IPayment> | Observable<never> {

    const payment: Payment = {
      paymentDate: DEFAULT_DATE,
      settlementCurrency: DEFAULT_CURRENCY,
      paymentAmount: DEFAULT_TRANSACTION_AMOUNT,
      invoicedAmount: DEFAULT_INVOICE_AMOUNT,
      disbursementCost: DEFAULT_DISBURSEMENT_COST,
      vatableAmount: DEFAULT_VATABLE_AMOUNT,
      conversionRate: DEEFAULT_CONVERSION_RATE,
      description: DEFAULT_DESCRIPTION
    }

    this.store.dispatch(paymentToDealerInitiated({dealerPayment: payment}))

    return of(payment);
  }
}
