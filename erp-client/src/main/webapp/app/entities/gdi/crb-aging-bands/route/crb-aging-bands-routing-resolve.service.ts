import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICrbAgingBands, CrbAgingBands } from '../crb-aging-bands.model';
import { CrbAgingBandsService } from '../service/crb-aging-bands.service';

@Injectable({ providedIn: 'root' })
export class CrbAgingBandsRoutingResolveService implements Resolve<ICrbAgingBands> {
  constructor(protected service: CrbAgingBandsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICrbAgingBands> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((crbAgingBands: HttpResponse<CrbAgingBands>) => {
          if (crbAgingBands.body) {
            return of(crbAgingBands.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CrbAgingBands());
  }
}
