jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContractMetadata, ContractMetadata } from '../contract-metadata.model';
import { ContractMetadataService } from '../service/contract-metadata.service';

import { ContractMetadataRoutingResolveService } from './contract-metadata-routing-resolve.service';

describe('ContractMetadata routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ContractMetadataRoutingResolveService;
  let service: ContractMetadataService;
  let resultContractMetadata: IContractMetadata | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ContractMetadataRoutingResolveService);
    service = TestBed.inject(ContractMetadataService);
    resultContractMetadata = undefined;
  });

  describe('resolve', () => {
    it('should return IContractMetadata returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultContractMetadata = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultContractMetadata).toEqual({ id: 123 });
    });

    it('should return new IContractMetadata if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultContractMetadata = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultContractMetadata).toEqual(new ContractMetadata());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContractMetadata })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultContractMetadata = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultContractMetadata).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
