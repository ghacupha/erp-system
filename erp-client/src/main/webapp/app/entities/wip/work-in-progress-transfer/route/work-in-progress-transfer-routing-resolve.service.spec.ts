jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IWorkInProgressTransfer, WorkInProgressTransfer } from '../work-in-progress-transfer.model';
import { WorkInProgressTransferService } from '../service/work-in-progress-transfer.service';

import { WorkInProgressTransferRoutingResolveService } from './work-in-progress-transfer-routing-resolve.service';

describe('WorkInProgressTransfer routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: WorkInProgressTransferRoutingResolveService;
  let service: WorkInProgressTransferService;
  let resultWorkInProgressTransfer: IWorkInProgressTransfer | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(WorkInProgressTransferRoutingResolveService);
    service = TestBed.inject(WorkInProgressTransferService);
    resultWorkInProgressTransfer = undefined;
  });

  describe('resolve', () => {
    it('should return IWorkInProgressTransfer returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWorkInProgressTransfer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWorkInProgressTransfer).toEqual({ id: 123 });
    });

    it('should return new IWorkInProgressTransfer if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWorkInProgressTransfer = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultWorkInProgressTransfer).toEqual(new WorkInProgressTransfer());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as WorkInProgressTransfer })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultWorkInProgressTransfer = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultWorkInProgressTransfer).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
