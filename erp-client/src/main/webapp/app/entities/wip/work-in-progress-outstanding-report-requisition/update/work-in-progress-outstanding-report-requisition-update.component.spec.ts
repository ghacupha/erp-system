jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { WorkInProgressOutstandingReportRequisitionService } from '../service/work-in-progress-outstanding-report-requisition.service';
import {
  IWorkInProgressOutstandingReportRequisition,
  WorkInProgressOutstandingReportRequisition,
} from '../work-in-progress-outstanding-report-requisition.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';

import { WorkInProgressOutstandingReportRequisitionUpdateComponent } from './work-in-progress-outstanding-report-requisition-update.component';

describe('WorkInProgressOutstandingReportRequisition Management Update Component', () => {
  let comp: WorkInProgressOutstandingReportRequisitionUpdateComponent;
  let fixture: ComponentFixture<WorkInProgressOutstandingReportRequisitionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workInProgressOutstandingReportRequisitionService: WorkInProgressOutstandingReportRequisitionService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [WorkInProgressOutstandingReportRequisitionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(WorkInProgressOutstandingReportRequisitionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkInProgressOutstandingReportRequisitionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workInProgressOutstandingReportRequisitionService = TestBed.inject(WorkInProgressOutstandingReportRequisitionService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition = { id: 456 };
      const requestedBy: IApplicationUser = { id: 24788 };
      workInProgressOutstandingReportRequisition.requestedBy = requestedBy;
      const lastAccessedBy: IApplicationUser = { id: 60101 };
      workInProgressOutstandingReportRequisition.lastAccessedBy = lastAccessedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 20858 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy, lastAccessedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workInProgressOutstandingReportRequisition });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const workInProgressOutstandingReportRequisition: IWorkInProgressOutstandingReportRequisition = { id: 456 };
      const requestedBy: IApplicationUser = { id: 43820 };
      workInProgressOutstandingReportRequisition.requestedBy = requestedBy;
      const lastAccessedBy: IApplicationUser = { id: 88258 };
      workInProgressOutstandingReportRequisition.lastAccessedBy = lastAccessedBy;

      activatedRoute.data = of({ workInProgressOutstandingReportRequisition });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(workInProgressOutstandingReportRequisition));
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
      expect(comp.applicationUsersSharedCollection).toContain(lastAccessedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WorkInProgressOutstandingReportRequisition>>();
      const workInProgressOutstandingReportRequisition = { id: 123 };
      jest.spyOn(workInProgressOutstandingReportRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workInProgressOutstandingReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workInProgressOutstandingReportRequisition }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(workInProgressOutstandingReportRequisitionService.update).toHaveBeenCalledWith(workInProgressOutstandingReportRequisition);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WorkInProgressOutstandingReportRequisition>>();
      const workInProgressOutstandingReportRequisition = new WorkInProgressOutstandingReportRequisition();
      jest.spyOn(workInProgressOutstandingReportRequisitionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workInProgressOutstandingReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workInProgressOutstandingReportRequisition }));
      saveSubject.complete();

      // THEN
      expect(workInProgressOutstandingReportRequisitionService.create).toHaveBeenCalledWith(workInProgressOutstandingReportRequisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WorkInProgressOutstandingReportRequisition>>();
      const workInProgressOutstandingReportRequisition = { id: 123 };
      jest.spyOn(workInProgressOutstandingReportRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workInProgressOutstandingReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workInProgressOutstandingReportRequisitionService.update).toHaveBeenCalledWith(workInProgressOutstandingReportRequisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackApplicationUserById', () => {
      it('Should return tracked ApplicationUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackApplicationUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
