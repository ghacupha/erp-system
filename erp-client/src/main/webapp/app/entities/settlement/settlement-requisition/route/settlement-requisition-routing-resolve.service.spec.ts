jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISettlementRequisition, SettlementRequisition } from '../settlement-requisition.model';
import { SettlementRequisitionService } from '../service/settlement-requisition.service';

import { SettlementRequisitionRoutingResolveService } from './settlement-requisition-routing-resolve.service';

describe('SettlementRequisition routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SettlementRequisitionRoutingResolveService;
  let service: SettlementRequisitionService;
  let resultSettlementRequisition: ISettlementRequisition | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SettlementRequisitionRoutingResolveService);
    service = TestBed.inject(SettlementRequisitionService);
    resultSettlementRequisition = undefined;
  });

  describe('resolve', () => {
    it('should return ISettlementRequisition returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSettlementRequisition = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSettlementRequisition).toEqual({ id: 123 });
    });

    it('should return new ISettlementRequisition if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSettlementRequisition = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSettlementRequisition).toEqual(new SettlementRequisition());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SettlementRequisition })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSettlementRequisition = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSettlementRequisition).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
