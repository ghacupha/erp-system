import {Injectable} from '@angular/core';
import {PaymentService} from '../../../payments/payment/service/payment.service';
import {IDealer} from '../dealer.model';
import {Store} from '@ngrx/store';
import {State} from '../../../../store/global-store.definition';
import {IPaymentCategory, PaymentCategory} from '../../../payments/payment-category/payment-category.model';
import {EMPTY, Observable, of} from "rxjs";
import { EntityArrayResponseType } from 'app/erp/erp-pages/payments/payment/service/payment.service';

@Injectable({ providedIn: 'root' })
export class DealerCategoryService {

  constructor(protected paymentService: PaymentService, protected store: Store<State>) {
  }

  getDealerCategory(dealer: IDealer): Observable<IPaymentCategory> {
    let cat: IPaymentCategory | null | undefined = {...new PaymentCategory()};
    this.paymentService.query({'dealerId.equals': dealer.id}).subscribe((pyts: EntityArrayResponseType) => {
      if (pyts.body?.length !== 0) {
        if (pyts.body) {
          if (pyts.body[pyts.body.length - 1].paymentCategory !== undefined) {
            if (cat) {
              cat = pyts.body[pyts.body.length - 1].paymentCategory
              // let's conclude this business
              return of(cat);
            }
          }
        }
      }
    });
    return EMPTY;
  }
}
