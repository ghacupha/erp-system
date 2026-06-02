import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepreciationMethod, DepreciationMethod } from '../depreciation-method.model';
import { DepreciationMethodService } from '../service/depreciation-method.service';

@Injectable({ providedIn: 'root' })
export class DepreciationMethodRoutingResolveService implements Resolve<IDepreciationMethod> {
  constructor(protected service: DepreciationMethodService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDepreciationMethod> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((depreciationMethod: HttpResponse<DepreciationMethod>) => {
          if (depreciationMethod.body) {
            return of(depreciationMethod.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DepreciationMethod());
  }
}
