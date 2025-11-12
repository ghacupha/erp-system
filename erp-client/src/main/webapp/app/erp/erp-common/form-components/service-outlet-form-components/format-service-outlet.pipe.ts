///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { Pipe, PipeTransform } from '@angular/core';
import { IServiceOutlet } from '../../../erp-granular/service-outlet/service-outlet.model';
import { ServiceOutletService } from '../../../erp-granular/service-outlet/service/service-outlet.service';

@Pipe({
  name: 'formatServiceOutlet'
})
export class FormatServiceOutletPipe implements PipeTransform {


  constructor(protected serviceOutletService: ServiceOutletService) {
  }

  transform(value: IServiceOutlet): string {

    let val: IServiceOutlet = value;

    if (value.id) {
      this.serviceOutletService.find(value.id).subscribe(res => {
        if (res.body) {
          val = res.body
        }
      });
    }

    let accountDetail = '';

    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (value) {
      if (val.outletName) {
        // eslint-disable-next-line @typescript-eslint/restrict-template-expressions
        accountDetail = `Id: ${val.id} | Dept. Name: ${val.outletName} | Outlet Code: ${val.outletCode}`;
      } else {
        accountDetail = `Id: ${val.id} | Outlet Code: ${val.outletCode}`;
      }
    }

    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    return value ? accountDetail : '';
  }
}
