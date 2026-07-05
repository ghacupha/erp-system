jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IParticularsOfOutlet, ParticularsOfOutlet } from '../particulars-of-outlet.model';
import { ParticularsOfOutletService } from '../service/particulars-of-outlet.service';

import { ParticularsOfOutletRoutingResolveService } from './particulars-of-outlet-routing-resolve.service';

describe('ParticularsOfOutlet routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ParticularsOfOutletRoutingResolveService;
  let service: ParticularsOfOutletService;
  let resultParticularsOfOutlet: IParticularsOfOutlet | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ParticularsOfOutletRoutingResolveService);
    service = TestBed.inject(ParticularsOfOutletService);
    resultParticularsOfOutlet = undefined;
  });

  describe('resolve', () => {
    it('should return IParticularsOfOutlet returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultParticularsOfOutlet = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultParticularsOfOutlet).toEqual({ id: 123 });
    });

    it('should return new IParticularsOfOutlet if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultParticularsOfOutlet = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultParticularsOfOutlet).toEqual(new ParticularsOfOutlet());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ParticularsOfOutlet })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultParticularsOfOutlet = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultParticularsOfOutlet).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
