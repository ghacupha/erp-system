jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaseLiabilityScheduleReportItem, LeaseLiabilityScheduleReportItem } from '../lease-liability-schedule-report-item.model';
import { LeaseLiabilityScheduleReportItemService } from '../service/lease-liability-schedule-report-item.service';

import { LeaseLiabilityScheduleReportItemRoutingResolveService } from './lease-liability-schedule-report-item-routing-resolve.service';

describe('LeaseLiabilityScheduleReportItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LeaseLiabilityScheduleReportItemRoutingResolveService;
  let service: LeaseLiabilityScheduleReportItemService;
  let resultLeaseLiabilityScheduleReportItem: ILeaseLiabilityScheduleReportItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LeaseLiabilityScheduleReportItemRoutingResolveService);
    service = TestBed.inject(LeaseLiabilityScheduleReportItemService);
    resultLeaseLiabilityScheduleReportItem = undefined;
  });

  describe('resolve', () => {
    it('should return ILeaseLiabilityScheduleReportItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityScheduleReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseLiabilityScheduleReportItem).toEqual({ id: 123 });
    });

    it('should return new ILeaseLiabilityScheduleReportItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityScheduleReportItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLeaseLiabilityScheduleReportItem).toEqual(new LeaseLiabilityScheduleReportItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LeaseLiabilityScheduleReportItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityScheduleReportItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseLiabilityScheduleReportItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
