import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBusinessStamp, BusinessStamp } from '../business-stamp.model';
import { BusinessStampService } from '../service/business-stamp.service';

@Injectable({ providedIn: 'root' })
export class BusinessStampRoutingResolveService implements Resolve<IBusinessStamp> {
  constructor(protected service: BusinessStampService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBusinessStamp> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((businessStamp: HttpResponse<BusinessStamp>) => {
          if (businessStamp.body) {
            return of(businessStamp.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BusinessStamp());
  }
}
