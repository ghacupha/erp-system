jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICrbAccountHolderType, CrbAccountHolderType } from '../crb-account-holder-type.model';
import { CrbAccountHolderTypeService } from '../service/crb-account-holder-type.service';

import { CrbAccountHolderTypeRoutingResolveService } from './crb-account-holder-type-routing-resolve.service';

describe('CrbAccountHolderType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CrbAccountHolderTypeRoutingResolveService;
  let service: CrbAccountHolderTypeService;
  let resultCrbAccountHolderType: ICrbAccountHolderType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CrbAccountHolderTypeRoutingResolveService);
    service = TestBed.inject(CrbAccountHolderTypeService);
    resultCrbAccountHolderType = undefined;
  });

  describe('resolve', () => {
    it('should return ICrbAccountHolderType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbAccountHolderType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbAccountHolderType).toEqual({ id: 123 });
    });

    it('should return new ICrbAccountHolderType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbAccountHolderType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCrbAccountHolderType).toEqual(new CrbAccountHolderType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CrbAccountHolderType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCrbAccountHolderType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCrbAccountHolderType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
