import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import {
  ITransactionAccountPostingProcessType,
  TransactionAccountPostingProcessType,
} from '../transaction-account-posting-process-type.model';
import { TransactionAccountPostingProcessTypeService } from '../service/transaction-account-posting-process-type.service';

@Injectable({ providedIn: 'root' })
export class TransactionAccountPostingProcessTypeRoutingResolveService implements Resolve<ITransactionAccountPostingProcessType> {
  constructor(protected service: TransactionAccountPostingProcessTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransactionAccountPostingProcessType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((transactionAccountPostingProcessType: HttpResponse<TransactionAccountPostingProcessType>) => {
          if (transactionAccountPostingProcessType.body) {
            return of(transactionAccountPostingProcessType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TransactionAccountPostingProcessType());
  }
}
