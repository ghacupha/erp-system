jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICounterpartyType, CounterpartyType } from '../counterparty-type.model';
import { CounterpartyTypeService } from '../service/counterparty-type.service';

import { CounterpartyTypeRoutingResolveService } from './counterparty-type-routing-resolve.service';

describe('CounterpartyType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CounterpartyTypeRoutingResolveService;
  let service: CounterpartyTypeService;
  let resultCounterpartyType: ICounterpartyType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CounterpartyTypeRoutingResolveService);
    service = TestBed.inject(CounterpartyTypeService);
    resultCounterpartyType = undefined;
  });

  describe('resolve', () => {
    it('should return ICounterpartyType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCounterpartyType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCounterpartyType).toEqual({ id: 123 });
    });

    it('should return new ICounterpartyType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCounterpartyType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCounterpartyType).toEqual(new CounterpartyType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CounterpartyType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCounterpartyType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCounterpartyType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
