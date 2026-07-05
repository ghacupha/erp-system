import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITALeaseRecognitionRule, TALeaseRecognitionRule } from '../ta-lease-recognition-rule.model';
import { TALeaseRecognitionRuleService } from '../service/ta-lease-recognition-rule.service';

@Injectable({ providedIn: 'root' })
export class TALeaseRecognitionRuleRoutingResolveService implements Resolve<ITALeaseRecognitionRule> {
  constructor(protected service: TALeaseRecognitionRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITALeaseRecognitionRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tALeaseRecognitionRule: HttpResponse<TALeaseRecognitionRule>) => {
          if (tALeaseRecognitionRule.body) {
            return of(tALeaseRecognitionRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TALeaseRecognitionRule());
  }
}
