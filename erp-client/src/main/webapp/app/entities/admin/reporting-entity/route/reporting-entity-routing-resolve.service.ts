import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportingEntity, ReportingEntity } from '../reporting-entity.model';
import { ReportingEntityService } from '../service/reporting-entity.service';

@Injectable({ providedIn: 'root' })
export class ReportingEntityRoutingResolveService implements Resolve<IReportingEntity> {
  constructor(protected service: ReportingEntityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReportingEntity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((reportingEntity: HttpResponse<ReportingEntity>) => {
          if (reportingEntity.body) {
            return of(reportingEntity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReportingEntity());
  }
}
