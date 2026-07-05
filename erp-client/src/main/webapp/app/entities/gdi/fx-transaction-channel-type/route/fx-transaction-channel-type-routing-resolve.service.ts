import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFxTransactionChannelType, FxTransactionChannelType } from '../fx-transaction-channel-type.model';
import { FxTransactionChannelTypeService } from '../service/fx-transaction-channel-type.service';

@Injectable({ providedIn: 'root' })
export class FxTransactionChannelTypeRoutingResolveService implements Resolve<IFxTransactionChannelType> {
  constructor(protected service: FxTransactionChannelTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFxTransactionChannelType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fxTransactionChannelType: HttpResponse<FxTransactionChannelType>) => {
          if (fxTransactionChannelType.body) {
            return of(fxTransactionChannelType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FxTransactionChannelType());
  }
}
