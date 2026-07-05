jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInstitutionStatusType, InstitutionStatusType } from '../institution-status-type.model';
import { InstitutionStatusTypeService } from '../service/institution-status-type.service';

import { InstitutionStatusTypeRoutingResolveService } from './institution-status-type-routing-resolve.service';

describe('InstitutionStatusType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InstitutionStatusTypeRoutingResolveService;
  let service: InstitutionStatusTypeService;
  let resultInstitutionStatusType: IInstitutionStatusType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(InstitutionStatusTypeRoutingResolveService);
    service = TestBed.inject(InstitutionStatusTypeService);
    resultInstitutionStatusType = undefined;
  });

  describe('resolve', () => {
    it('should return IInstitutionStatusType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInstitutionStatusType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInstitutionStatusType).toEqual({ id: 123 });
    });

    it('should return new IInstitutionStatusType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInstitutionStatusType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInstitutionStatusType).toEqual(new InstitutionStatusType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InstitutionStatusType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInstitutionStatusType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInstitutionStatusType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
