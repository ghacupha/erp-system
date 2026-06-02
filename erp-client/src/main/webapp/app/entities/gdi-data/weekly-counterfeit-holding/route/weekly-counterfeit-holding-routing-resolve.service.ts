import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWeeklyCounterfeitHolding, WeeklyCounterfeitHolding } from '../weekly-counterfeit-holding.model';
import { WeeklyCounterfeitHoldingService } from '../service/weekly-counterfeit-holding.service';

@Injectable({ providedIn: 'root' })
export class WeeklyCounterfeitHoldingRoutingResolveService implements Resolve<IWeeklyCounterfeitHolding> {
  constructor(protected service: WeeklyCounterfeitHoldingService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWeeklyCounterfeitHolding> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((weeklyCounterfeitHolding: HttpResponse<WeeklyCounterfeitHolding>) => {
          if (weeklyCounterfeitHolding.body) {
            return of(weeklyCounterfeitHolding.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WeeklyCounterfeitHolding());
  }
}
