import {Injectable} from '@angular/core';
import {IPayment} from '../../../payments/payment/payment.model';
import {HttpResponse} from '@angular/common/http';
import {
  payDealerButtonClicked,
} from '../../../../store/actions/dealer-workflows-status.actions';
import {PaymentService} from '../../../payments/payment/service/payment.service';
import {IDealer} from '../dealer.model';
import {Store} from '@ngrx/store';
import {State} from '../../../../store/global-store.definition';
import {IPaymentCategory} from '../../../payments/payment-category/payment-category.model';

@Injectable({ providedIn: 'root' })
export class DealerCategoryService {

  constructor(protected paymentService: PaymentService, protected store: Store<State>) {
  }

  getDealerCategory(dealer: IDealer): void {
    this.paymentService.query({'dealerId.equals': dealer.id}).subscribe((pyts: HttpResponse<IPayment[]>) => {
      if (pyts.body?.length !== 0) {
        if (pyts.body) {
          const cat: IPaymentCategory | null | undefined= pyts.body[pyts.body.length - 1].paymentCategory
          if (cat) {
            this.store.dispatch(payDealerButtonClicked({selectedDealer: dealer, paymentDealerCategory: cat}));

            // let's conclude this business
            return;
          }
          this.store.dispatch(payDealerButtonClicked({selectedDealer: dealer, paymentDealerCategory: {}}));
        }
      }
    });
  }
}
