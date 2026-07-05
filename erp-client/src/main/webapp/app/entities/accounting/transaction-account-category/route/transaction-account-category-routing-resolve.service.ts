import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransactionAccountCategory, TransactionAccountCategory } from '../transaction-account-category.model';
import { TransactionAccountCategoryService } from '../service/transaction-account-category.service';

@Injectable({ providedIn: 'root' })
export class TransactionAccountCategoryRoutingResolveService implements Resolve<ITransactionAccountCategory> {
  constructor(protected service: TransactionAccountCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransactionAccountCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((transactionAccountCategory: HttpResponse<TransactionAccountCategory>) => {
          if (transactionAccountCategory.body) {
            return of(transactionAccountCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TransactionAccountCategory());
  }
}
