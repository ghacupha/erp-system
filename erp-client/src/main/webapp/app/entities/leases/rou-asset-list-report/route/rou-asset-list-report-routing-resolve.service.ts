import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouAssetListReport, RouAssetListReport } from '../rou-asset-list-report.model';
import { RouAssetListReportService } from '../service/rou-asset-list-report.service';

@Injectable({ providedIn: 'root' })
export class RouAssetListReportRoutingResolveService implements Resolve<IRouAssetListReport> {
  constructor(protected service: RouAssetListReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouAssetListReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouAssetListReport: HttpResponse<RouAssetListReport>) => {
          if (rouAssetListReport.body) {
            return of(rouAssetListReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouAssetListReport());
  }
}
