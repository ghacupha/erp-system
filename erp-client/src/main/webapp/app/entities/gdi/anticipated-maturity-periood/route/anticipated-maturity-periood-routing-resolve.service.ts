import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnticipatedMaturityPeriood, AnticipatedMaturityPeriood } from '../anticipated-maturity-periood.model';
import { AnticipatedMaturityPerioodService } from '../service/anticipated-maturity-periood.service';

@Injectable({ providedIn: 'root' })
export class AnticipatedMaturityPerioodRoutingResolveService implements Resolve<IAnticipatedMaturityPeriood> {
  constructor(protected service: AnticipatedMaturityPerioodService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAnticipatedMaturityPeriood> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((anticipatedMaturityPeriood: HttpResponse<AnticipatedMaturityPeriood>) => {
          if (anticipatedMaturityPeriood.body) {
            return of(anticipatedMaturityPeriood.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AnticipatedMaturityPeriood());
  }
}
