jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFiscalYear, FiscalYear } from '../fiscal-year.model';
import { FiscalYearService } from '../service/fiscal-year.service';

import { FiscalYearRoutingResolveService } from './fiscal-year-routing-resolve.service';

describe('FiscalYear routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FiscalYearRoutingResolveService;
  let service: FiscalYearService;
  let resultFiscalYear: IFiscalYear | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(FiscalYearRoutingResolveService);
    service = TestBed.inject(FiscalYearService);
    resultFiscalYear = undefined;
  });

  describe('resolve', () => {
    it('should return IFiscalYear returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFiscalYear = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFiscalYear).toEqual({ id: 123 });
    });

    it('should return new IFiscalYear if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFiscalYear = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFiscalYear).toEqual(new FiscalYear());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FiscalYear })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFiscalYear = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFiscalYear).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
