jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOutletType, OutletType } from '../outlet-type.model';
import { OutletTypeService } from '../service/outlet-type.service';

import { OutletTypeRoutingResolveService } from './outlet-type-routing-resolve.service';

describe('OutletType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OutletTypeRoutingResolveService;
  let service: OutletTypeService;
  let resultOutletType: IOutletType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(OutletTypeRoutingResolveService);
    service = TestBed.inject(OutletTypeService);
    resultOutletType = undefined;
  });

  describe('resolve', () => {
    it('should return IOutletType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOutletType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOutletType).toEqual({ id: 123 });
    });

    it('should return new IOutletType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOutletType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultOutletType).toEqual(new OutletType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OutletType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOutletType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOutletType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
