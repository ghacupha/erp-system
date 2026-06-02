import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInterestCalcMethod, InterestCalcMethod } from '../interest-calc-method.model';
import { InterestCalcMethodService } from '../service/interest-calc-method.service';

@Injectable({ providedIn: 'root' })
export class InterestCalcMethodRoutingResolveService implements Resolve<IInterestCalcMethod> {
  constructor(protected service: InterestCalcMethodService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInterestCalcMethod> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((interestCalcMethod: HttpResponse<InterestCalcMethod>) => {
          if (interestCalcMethod.body) {
            return of(interestCalcMethod.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InterestCalcMethod());
  }
}
