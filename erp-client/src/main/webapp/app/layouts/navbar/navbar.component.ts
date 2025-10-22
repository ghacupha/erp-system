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

import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { VERSION } from 'app/app.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { Subject, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, map, switchMap, takeUntil } from 'rxjs/operators';
import { IReportMetadata } from 'app/erp/erp-reports/report-metadata/report-metadata.model';
import { ReportMetadataService } from 'app/erp/erp-reports/report-metadata/report-metadata.service';
import { NgbDropdown } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;

  reportSearchTerm = '';
  reportSearchResults: IReportMetadata[] = [];
  searchingReports = false;
  reportSearchError?: string;

  private readonly reportSearch$ = new Subject<string>();
  private readonly destroy$ = new Subject<void>();

  constructor(
    private loginService: LoginService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router,
    private readonly reportMetadataService: ReportMetadataService
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.registerReportSearch();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  onReportSearch(term: string): void {
    this.reportSearchTerm = term;
    this.reportSearch$.next(term);
  }

  clearReportSearch(): void {
    this.reportSearchTerm = '';
    this.reportSearchResults = [];
    this.reportSearchError = undefined;
    this.searchingReports = false;
    this.reportSearch$.next('');
  }

  navigateToReport(metadata: IReportMetadata, dropdown?: NgbDropdown): void {
    if (!metadata?.pagePath) {
      return;
    }
    this.router.navigate([metadata.pagePath]).then(navigated => {
      if (navigated) {
        this.clearReportSearch();
        dropdown?.close();
        this.collapseNavbar();
      }
    });
  }

  trackReportById(index: number, item: IReportMetadata): number | string {
    return item.id ?? item.pagePath ?? index;
  }

  private registerReportSearch(): void {
    this.reportSearch$
      .pipe(
        takeUntil(this.destroy$),
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(term => {
          const normalized = term.trim();
          if (normalized.length < 2) {
            this.searchingReports = false;
            return of({ results: [], error: undefined as string | undefined });
          }
          this.searchingReports = true;
          return this.reportMetadataService.search(normalized, 8).pipe(
            map(results => ({ results, error: undefined as string | undefined })),
            catchError(() => of({ results: [], error: 'Unable to search reports at the moment.' }))
          );
        })
      )
      .subscribe(({ results, error }) => {
        this.reportSearchError = error;
        this.searchingReports = false;
        this.reportSearchResults = (results ?? []).filter(report => report.active !== false);
      });
  }
}
