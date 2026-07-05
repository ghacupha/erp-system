import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExcelReportExport, ExcelReportExport } from '../excel-report-export.model';
import { ExcelReportExportService } from '../service/excel-report-export.service';

@Injectable({ providedIn: 'root' })
export class ExcelReportExportRoutingResolveService implements Resolve<IExcelReportExport> {
  constructor(protected service: ExcelReportExportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExcelReportExport> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((excelReportExport: HttpResponse<ExcelReportExport>) => {
          if (excelReportExport.body) {
            return of(excelReportExport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExcelReportExport());
  }
}
