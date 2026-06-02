jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContractStatus, ContractStatus } from '../contract-status.model';
import { ContractStatusService } from '../service/contract-status.service';

import { ContractStatusRoutingResolveService } from './contract-status-routing-resolve.service';

describe('ContractStatus routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ContractStatusRoutingResolveService;
  let service: ContractStatusService;
  let resultContractStatus: IContractStatus | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ContractStatusRoutingResolveService);
    service = TestBed.inject(ContractStatusService);
    resultContractStatus = undefined;
  });

  describe('resolve', () => {
    it('should return IContractStatus returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultContractStatus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultContractStatus).toEqual({ id: 123 });
    });

    it('should return new IContractStatus if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultContractStatus = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultContractStatus).toEqual(new ContractStatus());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContractStatus })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultContractStatus = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultContractStatus).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
