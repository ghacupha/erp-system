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

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { IReportFilterDefinition } from '../report-metadata/report-metadata.model';
import { LeaseRepaymentPeriodService } from 'app/entities/leases/lease-repayment-period/service/lease-repayment-period.service';
import { ILeaseRepaymentPeriod } from 'app/entities/leases/lease-repayment-period/lease-repayment-period.model';
import { LeaseLiabilityService } from 'app/entities/leases/lease-liability/service/lease-liability.service';
import { ILeaseLiability } from 'app/entities/leases/lease-liability/lease-liability.model';
import { FiscalMonthService } from 'app/entities/system/fiscal-month/service/fiscal-month.service';
import { IFiscalMonth } from 'app/entities/system/fiscal-month/fiscal-month.model';
import { PrepaymentAccountService } from 'app/entities/prepayments/prepayment-account/service/prepayment-account.service';
import { IPrepaymentAccount } from 'app/entities/prepayments/prepayment-account/prepayment-account.model';
import { TAAmortizationRuleService } from 'app/entities/accounting/ta-amortization-rule/service/ta-amortization-rule.service';
import { ITAAmortizationRule } from 'app/entities/accounting/ta-amortization-rule/ta-amortization-rule.model';

export interface ReportFilterOption<T = unknown> {
  value: unknown;
  label: string;
  description?: string | null;
  raw?: T;
}

@Injectable({ providedIn: 'root' })
export class ReportFilterOptionService {
  private readonly defaultPageSize = 100;

  constructor(
    private readonly leaseRepaymentPeriodService: LeaseRepaymentPeriodService,
    private readonly leaseLiabilityService: LeaseLiabilityService,
    private readonly fiscalMonthService: FiscalMonthService,
    private readonly prepaymentAccountService: PrepaymentAccountService,
    private readonly amortizationRuleService: TAAmortizationRuleService
  ) {}

  loadOptions(definition: IReportFilterDefinition, term?: string): Observable<ReportFilterOption[]> {
    const trimmedTerm = term?.trim();
    const source = definition.valueSource;
    switch (source) {
      case 'leasePeriods':
        return this.loadLeasePeriods(trimmedTerm);
      case 'leaseContracts':
        return this.loadLeaseContracts(trimmedTerm);
      case 'fiscalMonths':
        return this.loadFiscalMonths(trimmedTerm);
      case 'prepaymentAccounts':
        return this.loadPrepaymentAccounts(trimmedTerm);
      case 'amortizationRules':
        return this.loadAmortizationRules(trimmedTerm);
      default:
        return of([]);
    }
  }

  private loadLeasePeriods(term?: string): Observable<ReportFilterOption<ILeaseRepaymentPeriod>[]> {
    const request: Record<string, unknown> = { size: this.defaultPageSize, sort: ['id,desc'] };
    if (term) {
      request['periodCode.contains'] = term;
    }
    return this.leaseRepaymentPeriodService
      .query(request)
      .pipe(map((res: HttpResponse<ILeaseRepaymentPeriod[]>) => this.mapLeasePeriods(res.body ?? [])));
  }

  private mapLeasePeriods(periods: ILeaseRepaymentPeriod[]): ReportFilterOption<ILeaseRepaymentPeriod>[] {
    return periods.map(period => {
      const label = period.periodCode ?? (period.id ? `Period #${period.id}` : 'Lease period');
      const dateRange = this.buildDateRange(period.startDate, period.endDate);
      return {
        value: period.id ?? period.periodCode ?? label,
        label,
        description: dateRange ?? undefined,
        raw: period,
      };
    });
  }

  private loadLeaseContracts(term?: string): Observable<ReportFilterOption<ILeaseLiability>[]> {
    const request: Record<string, unknown> = { size: this.defaultPageSize, sort: ['id,desc'] };
    if (term) {
      request['leaseId.contains'] = term;
    }
    return this.leaseLiabilityService
      .query(request)
      .pipe(map((res: HttpResponse<ILeaseLiability[]>) => this.mapLeaseContracts(res.body ?? [])));
  }

  private mapLeaseContracts(liabilities: ILeaseLiability[]): ReportFilterOption<ILeaseLiability>[] {
    return liabilities.map(liability => {
      const label = liability.leaseId ?? (liability.id ? `Liability #${liability.id}` : 'Lease contract');
      const description = liability.leaseContract?.leaseTitle ?? null;
      const identifier = liability.id ?? liability.leaseId ?? label;
      return {
        value: identifier,
        label,
        description,
        raw: liability,
      };
    });
  }

  private loadFiscalMonths(term?: string): Observable<ReportFilterOption<IFiscalMonth>[]> {
    const request: Record<string, unknown> = { size: this.defaultPageSize, sort: ['id,desc'] };
    if (term) {
      request['fiscalMonthCode.contains'] = term;
    }
    return this.fiscalMonthService
      .query(request)
      .pipe(map((res: HttpResponse<IFiscalMonth[]>) => this.mapFiscalMonths(res.body ?? [])));
  }

  private mapFiscalMonths(months: IFiscalMonth[]): ReportFilterOption<IFiscalMonth>[] {
    return months.map(month => {
      const label = month.fiscalMonthCode ?? (month.id ? `Month #${month.id}` : 'Fiscal month');
      const period = this.buildDateRange(month.startDate, month.endDate);
      return {
        value: month.id ?? month.fiscalMonthCode ?? label,
        label,
        description: period ?? undefined,
        raw: month,
      };
    });
  }

  private loadPrepaymentAccounts(term?: string): Observable<ReportFilterOption<IPrepaymentAccount>[]> {
    const request: Record<string, unknown> = { size: this.defaultPageSize, sort: ['id,desc'] };
    if (term) {
      request['catalogueNumber.contains'] = term;
    }
    return this.prepaymentAccountService
      .query(request)
      .pipe(map((res: HttpResponse<IPrepaymentAccount[]>) => this.mapPrepaymentAccounts(res.body ?? [])));
  }

  private mapPrepaymentAccounts(accounts: IPrepaymentAccount[]): ReportFilterOption<IPrepaymentAccount>[] {
    return accounts.map(account => {
      const label = account.catalogueNumber ?? (account.id ? `Account #${account.id}` : 'Prepayment account');
      const description = account.particulars ?? account.notes ?? null;
      return {
        value: account.id ?? account.catalogueNumber ?? label,
        label,
        description,
        raw: account,
      };
    });
  }

  private loadAmortizationRules(term?: string): Observable<ReportFilterOption<ITAAmortizationRule>[]> {
    const request: Record<string, unknown> = { size: this.defaultPageSize, sort: ['id,desc'] };
    if (term) {
      request['name.contains'] = term;
    }
    return this.amortizationRuleService
      .query(request)
      .pipe(map((res: HttpResponse<ITAAmortizationRule[]>) => this.mapAmortizationRules(res.body ?? [])));
  }

  private mapAmortizationRules(rules: ITAAmortizationRule[]): ReportFilterOption<ITAAmortizationRule>[] {
    return rules.map(rule => {
      const label = rule.name ?? (rule.id ? `Rule #${rule.id}` : 'Amortization rule');
      const description = rule.identifier ?? null;
      return {
        value: rule.id ?? rule.identifier ?? label,
        label,
        description,
        raw: rule,
      };
    });
  }

  private buildDateRange(start?: dayjs.Dayjs, end?: dayjs.Dayjs): string | null {
    const formattedStart = start?.isValid() ? start.format('YYYY-MM-DD') : undefined;
    const formattedEnd = end?.isValid() ? end.format('YYYY-MM-DD') : undefined;
    if (formattedStart && formattedEnd) {
      return `${formattedStart} → ${formattedEnd}`;
    }
    if (formattedStart) {
      return formattedStart;
    }
    if (formattedEnd) {
      return formattedEnd;
    }
    return null;
  }
}
