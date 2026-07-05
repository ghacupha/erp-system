jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDepartmentType, DepartmentType } from '../department-type.model';
import { DepartmentTypeService } from '../service/department-type.service';

import { DepartmentTypeRoutingResolveService } from './department-type-routing-resolve.service';

describe('DepartmentType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DepartmentTypeRoutingResolveService;
  let service: DepartmentTypeService;
  let resultDepartmentType: IDepartmentType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DepartmentTypeRoutingResolveService);
    service = TestBed.inject(DepartmentTypeService);
    resultDepartmentType = undefined;
  });

  describe('resolve', () => {
    it('should return IDepartmentType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepartmentType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepartmentType).toEqual({ id: 123 });
    });

    it('should return new IDepartmentType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepartmentType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDepartmentType).toEqual(new DepartmentType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DepartmentType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDepartmentType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDepartmentType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
