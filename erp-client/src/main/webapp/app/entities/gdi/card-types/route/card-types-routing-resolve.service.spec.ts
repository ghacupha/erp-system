jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICardTypes, CardTypes } from '../card-types.model';
import { CardTypesService } from '../service/card-types.service';

import { CardTypesRoutingResolveService } from './card-types-routing-resolve.service';

describe('CardTypes routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CardTypesRoutingResolveService;
  let service: CardTypesService;
  let resultCardTypes: ICardTypes | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CardTypesRoutingResolveService);
    service = TestBed.inject(CardTypesService);
    resultCardTypes = undefined;
  });

  describe('resolve', () => {
    it('should return ICardTypes returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardTypes = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardTypes).toEqual({ id: 123 });
    });

    it('should return new ICardTypes if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardTypes = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCardTypes).toEqual(new CardTypes());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CardTypes })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardTypes = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardTypes).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
