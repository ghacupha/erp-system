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

import {provideMockStore} from "@ngrx/store/testing";

jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { InvoiceService } from '../service/invoice.service';

import { InvoiceRoutingResolveService } from './invoice-routing-resolve.service';
import {initialState} from "../../../../store/global-store.definition";
import {LoggerTestingModule} from "ngx-logger/testing";
import { IInvoice, Invoice } from '../invoice.model';
import { ErpCommonModule } from '../../../../erp-common/erp-common.module';

describe('Service Tests', () => {
  describe('Invoice routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: InvoiceRoutingResolveService;
    let service: InvoiceService;
    let resultInvoice: IInvoice | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpCommonModule, HttpClientTestingModule, LoggerTestingModule],
        providers: [Router, ActivatedRouteSnapshot, provideMockStore({initialState})],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(InvoiceRoutingResolveService);
      service = TestBed.inject(InvoiceService);
      resultInvoice = undefined;
    });

    describe('resolve', () => {
      it('should return IInvoice returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInvoice = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInvoice).toEqual({ id: 123 });
      });

      it('should return new IInvoice if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInvoice = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultInvoice).toEqual(new Invoice());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Invoice })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInvoice = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInvoice).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
