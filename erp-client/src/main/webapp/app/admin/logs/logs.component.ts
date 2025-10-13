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

import { Log, LoggersResponse, Level } from './log.model';
import { LogsService } from './logs.service';

@Component({
  selector: 'jhi-logs',
  templateUrl: './logs.component.html',
})
export class LogsComponent implements OnInit {
  loggers?: Log[];
  filteredAndOrderedLoggers?: Log[];
  filter = '';
  orderProp: keyof Log = 'name';
  ascending = true;

  constructor(private logsService: LogsService) {}

  ngOnInit(): void {
    this.findAndExtractLoggers();
  }

  changeLevel(name: string, level: Level): void {
    this.logsService.changeLevel(name, level).subscribe(() => this.findAndExtractLoggers());
  }

  filterAndSort(): void {
    this.filteredAndOrderedLoggers = this.loggers!.filter(
      logger => !this.filter || logger.name.toLowerCase().includes(this.filter.toLowerCase())
    ).sort((a, b) => {
      if (a[this.orderProp] < b[this.orderProp]) {
        return this.ascending ? -1 : 1;
      } else if (a[this.orderProp] > b[this.orderProp]) {
        return this.ascending ? 1 : -1;
      } else if (this.orderProp === 'level') {
        return a.name < b.name ? -1 : 1;
      }
      return 0;
    });
  }

  private findAndExtractLoggers(): void {
    this.logsService.findAll().subscribe((response: LoggersResponse) => {
      this.loggers = Object.entries(response.loggers).map(([key, logger]) => new Log(key, logger.effectiveLevel));
      this.filterAndSort();
    });
  }
}
