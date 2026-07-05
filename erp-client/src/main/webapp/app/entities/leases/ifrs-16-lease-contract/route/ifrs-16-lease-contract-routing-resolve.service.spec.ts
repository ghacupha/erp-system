jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIFRS16LeaseContract, IFRS16LeaseContract } from '../ifrs-16-lease-contract.model';
import { IFRS16LeaseContractService } from '../service/ifrs-16-lease-contract.service';

import { IFRS16LeaseContractRoutingResolveService } from './ifrs-16-lease-contract-routing-resolve.service';

describe('IFRS16LeaseContract routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: IFRS16LeaseContractRoutingResolveService;
  let service: IFRS16LeaseContractService;
  let resultIFRS16LeaseContract: IIFRS16LeaseContract | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(IFRS16LeaseContractRoutingResolveService);
    service = TestBed.inject(IFRS16LeaseContractService);
    resultIFRS16LeaseContract = undefined;
  });

  describe('resolve', () => {
    it('should return IIFRS16LeaseContract returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIFRS16LeaseContract = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIFRS16LeaseContract).toEqual({ id: 123 });
    });

    it('should return new IIFRS16LeaseContract if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIFRS16LeaseContract = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultIFRS16LeaseContract).toEqual(new IFRS16LeaseContract());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as IFRS16LeaseContract })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultIFRS16LeaseContract = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultIFRS16LeaseContract).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
