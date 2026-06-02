import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISystemContentType, SystemContentType } from '../system-content-type.model';
import { SystemContentTypeService } from '../service/system-content-type.service';

@Injectable({ providedIn: 'root' })
export class SystemContentTypeRoutingResolveService implements Resolve<ISystemContentType> {
  constructor(protected service: SystemContentTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISystemContentType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((systemContentType: HttpResponse<SystemContentType>) => {
          if (systemContentType.body) {
            return of(systemContentType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SystemContentType());
  }
}
