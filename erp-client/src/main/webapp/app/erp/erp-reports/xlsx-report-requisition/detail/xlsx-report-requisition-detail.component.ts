///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataUtils } from 'app/core/util/data-util.service';

import { IXlsxReportRequisition } from '../xlsx-report-requisition.model';
import { NGXLogger } from 'ngx-logger';

@Component({
  selector: 'jhi-xlsx-report-requisition-detail',
  templateUrl: './xlsx-report-requisition-detail.component.html',
})
export class XlsxReportRequisitionDetailComponent implements OnInit {
  xlsxReportRequisition: IXlsxReportRequisition | null = null;

  contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

  constructor(
    protected dataUtils: DataUtils,
    protected log: NGXLogger,
    protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ xlsxReportRequisition }) => {
      this.log.info(`Details component is initializing....`)
      this.log.info(`Updating the component with the data: ${xlsxReportRequisition.toString()}`)
      this.xlsxReportRequisition = xlsxReportRequisition;
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      this.log.info(`Details component is successfully updated with the data. Standby for rendering sequence`);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    // todo reconstitute the file, unlock with userPassword and convert back into base64String representation
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
