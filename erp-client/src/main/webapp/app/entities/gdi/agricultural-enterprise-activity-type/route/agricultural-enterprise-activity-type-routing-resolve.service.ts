import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAgriculturalEnterpriseActivityType, AgriculturalEnterpriseActivityType } from '../agricultural-enterprise-activity-type.model';
import { AgriculturalEnterpriseActivityTypeService } from '../service/agricultural-enterprise-activity-type.service';

@Injectable({ providedIn: 'root' })
export class AgriculturalEnterpriseActivityTypeRoutingResolveService implements Resolve<IAgriculturalEnterpriseActivityType> {
  constructor(protected service: AgriculturalEnterpriseActivityTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAgriculturalEnterpriseActivityType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((agriculturalEnterpriseActivityType: HttpResponse<AgriculturalEnterpriseActivityType>) => {
          if (agriculturalEnterpriseActivityType.body) {
            return of(agriculturalEnterpriseActivityType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AgriculturalEnterpriseActivityType());
  }
}
