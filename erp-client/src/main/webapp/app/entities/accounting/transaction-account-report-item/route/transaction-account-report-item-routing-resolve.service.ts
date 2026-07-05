import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransactionAccountReportItem, TransactionAccountReportItem } from '../transaction-account-report-item.model';
import { TransactionAccountReportItemService } from '../service/transaction-account-report-item.service';

@Injectable({ providedIn: 'root' })
export class TransactionAccountReportItemRoutingResolveService implements Resolve<ITransactionAccountReportItem> {
  constructor(protected service: TransactionAccountReportItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransactionAccountReportItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((transactionAccountReportItem: HttpResponse<TransactionAccountReportItem>) => {
          if (transactionAccountReportItem.body) {
            return of(transactionAccountReportItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TransactionAccountReportItem());
  }
}
