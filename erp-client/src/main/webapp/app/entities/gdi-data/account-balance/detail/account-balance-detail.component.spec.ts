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

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountBalanceDetailComponent } from './account-balance-detail.component';

describe('AccountBalance Management Detail Component', () => {
  let comp: AccountBalanceDetailComponent;
  let fixture: ComponentFixture<AccountBalanceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccountBalanceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ accountBalance: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AccountBalanceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AccountBalanceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accountBalance on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.accountBalance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
