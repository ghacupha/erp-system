jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPlaceholder, Placeholder } from '../placeholder.model';
import { PlaceholderService } from '../service/placeholder.service';

import { PlaceholderRoutingResolveService } from './placeholder-routing-resolve.service';

describe('Service Tests', () => {
  describe('Placeholder routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PlaceholderRoutingResolveService;
    let service: PlaceholderService;
    let resultPlaceholder: IPlaceholder | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PlaceholderRoutingResolveService);
      service = TestBed.inject(PlaceholderService);
      resultPlaceholder = undefined;
    });

    describe('resolve', () => {
      it('should return IPlaceholder returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPlaceholder = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPlaceholder).toEqual({ id: 123 });
      });

      it('should return new IPlaceholder if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPlaceholder = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPlaceholder).toEqual(new Placeholder());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Placeholder })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPlaceholder = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPlaceholder).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
