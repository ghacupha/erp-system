jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILeaseContract, LeaseContract } from '../lease-contract.model';
import { LeaseContractService } from '../service/lease-contract.service';

import { LeaseContractRoutingResolveService } from './lease-contract-routing-resolve.service';

describe('LeaseContract routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: LeaseContractRoutingResolveService;
  let service: LeaseContractService;
  let resultLeaseContract: ILeaseContract | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(LeaseContractRoutingResolveService);
    service = TestBed.inject(LeaseContractService);
    resultLeaseContract = undefined;
  });

  describe('resolve', () => {
    it('should return ILeaseContract returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseContract = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseContract).toEqual({ id: 123 });
    });

    it('should return new ILeaseContract if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseContract = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultLeaseContract).toEqual(new LeaseContract());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as LeaseContract })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultLeaseContract = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultLeaseContract).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
