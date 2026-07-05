jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICardUsageInformation, CardUsageInformation } from '../card-usage-information.model';
import { CardUsageInformationService } from '../service/card-usage-information.service';

import { CardUsageInformationRoutingResolveService } from './card-usage-information-routing-resolve.service';

describe('CardUsageInformation routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CardUsageInformationRoutingResolveService;
  let service: CardUsageInformationService;
  let resultCardUsageInformation: ICardUsageInformation | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CardUsageInformationRoutingResolveService);
    service = TestBed.inject(CardUsageInformationService);
    resultCardUsageInformation = undefined;
  });

  describe('resolve', () => {
    it('should return ICardUsageInformation returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardUsageInformation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardUsageInformation).toEqual({ id: 123 });
    });

    it('should return new ICardUsageInformation if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardUsageInformation = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCardUsageInformation).toEqual(new CardUsageInformation());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CardUsageInformation })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardUsageInformation = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardUsageInformation).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
