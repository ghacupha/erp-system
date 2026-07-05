jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaseLiabilityScheduleItem, LeaseLiabilityScheduleItem } from '../lease-liability-schedule-item.model';
import { LeaseLiabilityScheduleItemService } from '../service/lease-liability-schedule-item.service';

import { LeaseLiabilityScheduleItemRoutingResolveService } from './lease-liability-schedule-item-routing-resolve.service';

describe('LeaseLiabilityScheduleItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LeaseLiabilityScheduleItemRoutingResolveService;
  let service: LeaseLiabilityScheduleItemService;
  let resultLeaseLiabilityScheduleItem: ILeaseLiabilityScheduleItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LeaseLiabilityScheduleItemRoutingResolveService);
    service = TestBed.inject(LeaseLiabilityScheduleItemService);
    resultLeaseLiabilityScheduleItem = undefined;
  });

  describe('resolve', () => {
    it('should return ILeaseLiabilityScheduleItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityScheduleItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseLiabilityScheduleItem).toEqual({ id: 123 });
    });

    it('should return new ILeaseLiabilityScheduleItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityScheduleItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLeaseLiabilityScheduleItem).toEqual(new LeaseLiabilityScheduleItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LeaseLiabilityScheduleItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseLiabilityScheduleItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseLiabilityScheduleItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
