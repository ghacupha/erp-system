jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAccountAttributeMetadata, AccountAttributeMetadata } from '../account-attribute-metadata.model';
import { AccountAttributeMetadataService } from '../service/account-attribute-metadata.service';

import { AccountAttributeMetadataRoutingResolveService } from './account-attribute-metadata-routing-resolve.service';

describe('AccountAttributeMetadata routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AccountAttributeMetadataRoutingResolveService;
  let service: AccountAttributeMetadataService;
  let resultAccountAttributeMetadata: IAccountAttributeMetadata | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AccountAttributeMetadataRoutingResolveService);
    service = TestBed.inject(AccountAttributeMetadataService);
    resultAccountAttributeMetadata = undefined;
  });

  describe('resolve', () => {
    it('should return IAccountAttributeMetadata returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountAttributeMetadata = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountAttributeMetadata).toEqual({ id: 123 });
    });

    it('should return new IAccountAttributeMetadata if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountAttributeMetadata = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAccountAttributeMetadata).toEqual(new AccountAttributeMetadata());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AccountAttributeMetadata })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountAttributeMetadata = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountAttributeMetadata).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
