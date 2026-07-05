jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRouAccountBalanceReportItem, RouAccountBalanceReportItem } from '../rou-account-balance-report-item.model';
import { RouAccountBalanceReportItemService } from '../service/rou-account-balance-report-item.service';

import { RouAccountBalanceReportItemRoutingResolveService } from './rou-account-balance-report-item-routing-resolve.service';

describe('RouAccountBalanceReportItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RouAccountBalanceReportItemRoutingResolveService;
  let service: RouAccountBalanceReportItemService;
  let resultRouAccountBalanceReportItem: IRouAccountBalanceReportItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RouAccountBalanceReportItemRoutingResolveService);
    service = TestBed.inject(RouAccountBalanceReportItemService);
    resultRouAccountBalanceReportItem = undefined;
  });

  describe('resolve', () => {
    it('should return IRouAccountBalanceReportItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouAccountBalanceReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouAccountBalanceReportItem).toEqual({ id: 123 });
    });

    it('should return new IRouAccountBalanceReportItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouAccountBalanceReportItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRouAccountBalanceReportItem).toEqual(new RouAccountBalanceReportItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RouAccountBalanceReportItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRouAccountBalanceReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRouAccountBalanceReportItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
