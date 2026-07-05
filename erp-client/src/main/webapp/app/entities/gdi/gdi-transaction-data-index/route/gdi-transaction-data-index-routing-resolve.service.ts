import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGdiTransactionDataIndex, GdiTransactionDataIndex } from '../gdi-transaction-data-index.model';
import { GdiTransactionDataIndexService } from '../service/gdi-transaction-data-index.service';

@Injectable({ providedIn: 'root' })
export class GdiTransactionDataIndexRoutingResolveService implements Resolve<IGdiTransactionDataIndex> {
  constructor(protected service: GdiTransactionDataIndexService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGdiTransactionDataIndex> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gdiTransactionDataIndex: HttpResponse<GdiTransactionDataIndex>) => {
          if (gdiTransactionDataIndex.body) {
            return of(gdiTransactionDataIndex.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GdiTransactionDataIndex());
  }
}
