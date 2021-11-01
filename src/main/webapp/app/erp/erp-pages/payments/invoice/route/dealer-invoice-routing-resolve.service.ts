import {Injectable} from "@angular/core";
import {InvoiceService} from "../service/invoice.service";
import {Resolve, Router} from "@angular/router";
import {Observable, of} from "rxjs";
import {IInvoice, Invoice} from "../invoice.model";
import {select, Store} from "@ngrx/store";
import {State} from "../../../../store/global-store.definition";
import {DEFAULT_CONVERSION_RATE, DEFAULT_CURRENCY} from "../../payment/default-values.constants";
import {IDealer} from "../../../dealers/dealer/dealer.model";
import {dealerInvoiceSelectedDealer} from "../../../../store/selectors/dealer-invoice-worklows-status.selectors";

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
        dealerId: dealer.id,
        paymentLabels: dealer.paymentLabels,
        placeholders: dealer.placeholders,
      }
    });

    return of(invoice);
  }
}
