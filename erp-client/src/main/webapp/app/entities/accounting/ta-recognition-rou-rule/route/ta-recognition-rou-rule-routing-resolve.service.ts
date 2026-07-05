import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITARecognitionROURule, TARecognitionROURule } from '../ta-recognition-rou-rule.model';
import { TARecognitionROURuleService } from '../service/ta-recognition-rou-rule.service';

@Injectable({ providedIn: 'root' })
export class TARecognitionROURuleRoutingResolveService implements Resolve<ITARecognitionROURule> {
  constructor(protected service: TARecognitionROURuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITARecognitionROURule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tARecognitionROURule: HttpResponse<TARecognitionROURule>) => {
          if (tARecognitionROURule.body) {
            return of(tARecognitionROURule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TARecognitionROURule());
  }
}
