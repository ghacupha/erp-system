import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExecutiveCategoryType, ExecutiveCategoryType } from '../executive-category-type.model';
import { ExecutiveCategoryTypeService } from '../service/executive-category-type.service';

@Injectable({ providedIn: 'root' })
export class ExecutiveCategoryTypeRoutingResolveService implements Resolve<IExecutiveCategoryType> {
  constructor(protected service: ExecutiveCategoryTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExecutiveCategoryType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((executiveCategoryType: HttpResponse<ExecutiveCategoryType>) => {
          if (executiveCategoryType.body) {
            return of(executiveCategoryType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExecutiveCategoryType());
  }
}
