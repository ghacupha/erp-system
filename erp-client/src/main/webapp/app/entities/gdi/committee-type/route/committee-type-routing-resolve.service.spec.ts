jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICommitteeType, CommitteeType } from '../committee-type.model';
import { CommitteeTypeService } from '../service/committee-type.service';

import { CommitteeTypeRoutingResolveService } from './committee-type-routing-resolve.service';

describe('CommitteeType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CommitteeTypeRoutingResolveService;
  let service: CommitteeTypeService;
  let resultCommitteeType: ICommitteeType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CommitteeTypeRoutingResolveService);
    service = TestBed.inject(CommitteeTypeService);
    resultCommitteeType = undefined;
  });

  describe('resolve', () => {
    it('should return ICommitteeType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCommitteeType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCommitteeType).toEqual({ id: 123 });
    });

    it('should return new ICommitteeType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCommitteeType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCommitteeType).toEqual(new CommitteeType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CommitteeType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCommitteeType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCommitteeType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
