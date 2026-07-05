import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAssetAdditionsReport, AssetAdditionsReport } from '../asset-additions-report.model';
import { AssetAdditionsReportService } from '../service/asset-additions-report.service';

@Injectable({ providedIn: 'root' })
export class AssetAdditionsReportRoutingResolveService implements Resolve<IAssetAdditionsReport> {
  constructor(protected service: AssetAdditionsReportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssetAdditionsReport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((assetAdditionsReport: HttpResponse<AssetAdditionsReport>) => {
          if (assetAdditionsReport.body) {
            return of(assetAdditionsReport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AssetAdditionsReport());
  }
}
