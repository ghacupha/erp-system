import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMerchantType, MerchantType } from '../merchant-type.model';
import { MerchantTypeService } from '../service/merchant-type.service';

@Injectable({ providedIn: 'root' })
export class MerchantTypeRoutingResolveService implements Resolve<IMerchantType> {
  constructor(protected service: MerchantTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMerchantType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((merchantType: HttpResponse<MerchantType>) => {
          if (merchantType.body) {
            return of(merchantType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MerchantType());
  }
}
