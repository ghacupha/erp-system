import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouAssetNBVReport, RouAssetNBVReport } from '../rou-asset-nbv-report.model';
import { RouAssetNBVReportService } from '../service/rou-asset-nbv-report.service';

@Injectable({ providedIn: 'root' })
export class RouAssetNBVReportRoutingResolveService implements Resolve<IRouAssetNBVReport> {
  constructor(protected service: RouAssetNBVReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouAssetNBVReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouAssetNBVReport: HttpResponse<RouAssetNBVReport>) => {
          if (rouAssetNBVReport.body) {
            return of(rouAssetNBVReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouAssetNBVReport());
  }
}
