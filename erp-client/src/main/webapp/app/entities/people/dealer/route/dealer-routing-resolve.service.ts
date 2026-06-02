import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDealer, Dealer } from '../dealer.model';
import { DealerService } from '../service/dealer.service';

@Injectable({ providedIn: 'root' })
export class DealerRoutingResolveService implements Resolve<IDealer> {
  constructor(protected service: DealerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDealer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dealer: HttpResponse<Dealer>) => {
          if (dealer.body) {
            return of(dealer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dealer());
  }
}
