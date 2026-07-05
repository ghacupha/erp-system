import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILoanRestructureItem, LoanRestructureItem } from '../loan-restructure-item.model';
import { LoanRestructureItemService } from '../service/loan-restructure-item.service';

@Injectable({ providedIn: 'root' })
export class LoanRestructureItemRoutingResolveService implements Resolve<ILoanRestructureItem> {
  constructor(protected service: LoanRestructureItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILoanRestructureItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((loanRestructureItem: HttpResponse<LoanRestructureItem>) => {
          if (loanRestructureItem.body) {
            return of(loanRestructureItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LoanRestructureItem());
  }
}
