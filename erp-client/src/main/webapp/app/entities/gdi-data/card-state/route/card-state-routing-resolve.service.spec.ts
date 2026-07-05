jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICardState, CardState } from '../card-state.model';
import { CardStateService } from '../service/card-state.service';

import { CardStateRoutingResolveService } from './card-state-routing-resolve.service';

describe('CardState routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CardStateRoutingResolveService;
  let service: CardStateService;
  let resultCardState: ICardState | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CardStateRoutingResolveService);
    service = TestBed.inject(CardStateService);
    resultCardState = undefined;
  });

  describe('resolve', () => {
    it('should return ICardState returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardState = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardState).toEqual({ id: 123 });
    });

    it('should return new ICardState if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardState = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCardState).toEqual(new CardState());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CardState })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCardState = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCardState).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
