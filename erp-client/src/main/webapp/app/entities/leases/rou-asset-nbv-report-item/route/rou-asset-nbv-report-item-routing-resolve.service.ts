import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouAssetNBVReportItem, RouAssetNBVReportItem } from '../rou-asset-nbv-report-item.model';
import { RouAssetNBVReportItemService } from '../service/rou-asset-nbv-report-item.service';

@Injectable({ providedIn: 'root' })
export class RouAssetNBVReportItemRoutingResolveService implements Resolve<IRouAssetNBVReportItem> {
  constructor(protected service: RouAssetNBVReportItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouAssetNBVReportItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouAssetNBVReportItem: HttpResponse<RouAssetNBVReportItem>) => {
          if (rouAssetNBVReportItem.body) {
            return of(rouAssetNBVReportItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouAssetNBVReportItem());
  }
}
