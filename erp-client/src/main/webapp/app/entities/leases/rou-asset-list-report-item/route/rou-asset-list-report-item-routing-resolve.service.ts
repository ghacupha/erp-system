import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouAssetListReportItem, RouAssetListReportItem } from '../rou-asset-list-report-item.model';
import { RouAssetListReportItemService } from '../service/rou-asset-list-report-item.service';

@Injectable({ providedIn: 'root' })
export class RouAssetListReportItemRoutingResolveService implements Resolve<IRouAssetListReportItem> {
  constructor(protected service: RouAssetListReportItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouAssetListReportItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouAssetListReportItem: HttpResponse<RouAssetListReportItem>) => {
          if (rouAssetListReportItem.body) {
            return of(rouAssetListReportItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouAssetListReportItem());
  }
}
