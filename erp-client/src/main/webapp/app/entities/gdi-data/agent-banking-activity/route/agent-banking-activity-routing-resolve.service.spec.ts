jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAgentBankingActivity, AgentBankingActivity } from '../agent-banking-activity.model';
import { AgentBankingActivityService } from '../service/agent-banking-activity.service';

import { AgentBankingActivityRoutingResolveService } from './agent-banking-activity-routing-resolve.service';

describe('AgentBankingActivity routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AgentBankingActivityRoutingResolveService;
  let service: AgentBankingActivityService;
  let resultAgentBankingActivity: IAgentBankingActivity | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AgentBankingActivityRoutingResolveService);
    service = TestBed.inject(AgentBankingActivityService);
    resultAgentBankingActivity = undefined;
  });

  describe('resolve', () => {
    it('should return IAgentBankingActivity returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAgentBankingActivity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAgentBankingActivity).toEqual({ id: 123 });
    });

    it('should return new IAgentBankingActivity if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAgentBankingActivity = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAgentBankingActivity).toEqual(new AgentBankingActivity());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AgentBankingActivity })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAgentBankingActivity = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAgentBankingActivity).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
