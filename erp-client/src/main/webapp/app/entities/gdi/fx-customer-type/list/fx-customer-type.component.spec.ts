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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

import { FxCustomerTypeService } from '../service/fx-customer-type.service';

import { FxCustomerTypeComponent } from './fx-customer-type.component';

describe('FxCustomerType Management Component', () => {
  let comp: FxCustomerTypeComponent;
  let fixture: ComponentFixture<FxCustomerTypeComponent>;
  let service: FxCustomerTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FxCustomerTypeComponent],
      providers: [
        Router,
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(FxCustomerTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FxCustomerTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FxCustomerTypeService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.fxCustomerTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  it('should load a page', () => {
    // WHEN
    comp.loadPage(1);

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.fxCustomerTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  it('should calculate the sort attribute for an id', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalledWith(expect.objectContaining({ sort: ['id,desc'] }));
  });

  it('should calculate the sort attribute for a non-id attribute', () => {
    // INIT
    comp.ngOnInit();

    // GIVEN
    comp.predicate = 'name';

    // WHEN
    comp.loadPage(1);

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ sort: ['name,desc', 'id'] }));
  });
});
