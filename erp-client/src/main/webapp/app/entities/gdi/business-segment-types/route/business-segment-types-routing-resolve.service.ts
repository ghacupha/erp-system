import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusinessSegmentTypes, BusinessSegmentTypes } from '../business-segment-types.model';
import { BusinessSegmentTypesService } from '../service/business-segment-types.service';

@Injectable({ providedIn: 'root' })
export class BusinessSegmentTypesRoutingResolveService implements Resolve<IBusinessSegmentTypes> {
  constructor(protected service: BusinessSegmentTypesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBusinessSegmentTypes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((businessSegmentTypes: HttpResponse<BusinessSegmentTypes>) => {
          if (businessSegmentTypes.body) {
            return of(businessSegmentTypes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BusinessSegmentTypes());
  }
}
