///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

import { IFraudCategoryFlag } from '../fraud-category-flag.model';

@Component({
  selector: 'jhi-fraud-category-flag-detail',
  templateUrl: './fraud-category-flag-detail.component.html',
})
export class FraudCategoryFlagDetailComponent implements OnInit {
  fraudCategoryFlag: IFraudCategoryFlag | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fraudCategoryFlag }) => {
      this.fraudCategoryFlag = fraudCategoryFlag;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
