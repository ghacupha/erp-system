import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransactionAccountLedger, TransactionAccountLedger } from '../transaction-account-ledger.model';
import { TransactionAccountLedgerService } from '../service/transaction-account-ledger.service';

@Injectable({ providedIn: 'root' })
export class TransactionAccountLedgerRoutingResolveService implements Resolve<ITransactionAccountLedger> {
  constructor(protected service: TransactionAccountLedgerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransactionAccountLedger> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((transactionAccountLedger: HttpResponse<TransactionAccountLedger>) => {
          if (transactionAccountLedger.body) {
            return of(transactionAccountLedger.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TransactionAccountLedger());
  }
}
