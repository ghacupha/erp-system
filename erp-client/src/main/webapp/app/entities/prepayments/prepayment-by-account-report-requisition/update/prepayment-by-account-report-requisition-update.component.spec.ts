jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PrepaymentByAccountReportRequisitionService } from '../service/prepayment-by-account-report-requisition.service';
import {
  IPrepaymentByAccountReportRequisition,
  PrepaymentByAccountReportRequisition,
} from '../prepayment-by-account-report-requisition.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';

import { PrepaymentByAccountReportRequisitionUpdateComponent } from './prepayment-by-account-report-requisition-update.component';

describe('PrepaymentByAccountReportRequisition Management Update Component', () => {
  let comp: PrepaymentByAccountReportRequisitionUpdateComponent;
  let fixture: ComponentFixture<PrepaymentByAccountReportRequisitionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prepaymentByAccountReportRequisitionService: PrepaymentByAccountReportRequisitionService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PrepaymentByAccountReportRequisitionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PrepaymentByAccountReportRequisitionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrepaymentByAccountReportRequisitionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prepaymentByAccountReportRequisitionService = TestBed.inject(PrepaymentByAccountReportRequisitionService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition = { id: 456 };
      const requestedBy: IApplicationUser = { id: 36586 };
      prepaymentByAccountReportRequisition.requestedBy = requestedBy;
      const lastAccessedBy: IApplicationUser = { id: 14232 };
      prepaymentByAccountReportRequisition.lastAccessedBy = lastAccessedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 8149 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy, lastAccessedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prepaymentByAccountReportRequisition });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prepaymentByAccountReportRequisition: IPrepaymentByAccountReportRequisition = { id: 456 };
      const requestedBy: IApplicationUser = { id: 54955 };
      prepaymentByAccountReportRequisition.requestedBy = requestedBy;
      const lastAccessedBy: IApplicationUser = { id: 8541 };
      prepaymentByAccountReportRequisition.lastAccessedBy = lastAccessedBy;

      activatedRoute.data = of({ prepaymentByAccountReportRequisition });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(prepaymentByAccountReportRequisition));
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
      expect(comp.applicationUsersSharedCollection).toContain(lastAccessedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentByAccountReportRequisition>>();
      const prepaymentByAccountReportRequisition = { id: 123 };
      jest.spyOn(prepaymentByAccountReportRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentByAccountReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prepaymentByAccountReportRequisition }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(prepaymentByAccountReportRequisitionService.update).toHaveBeenCalledWith(prepaymentByAccountReportRequisition);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentByAccountReportRequisition>>();
      const prepaymentByAccountReportRequisition = new PrepaymentByAccountReportRequisition();
      jest.spyOn(prepaymentByAccountReportRequisitionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentByAccountReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prepaymentByAccountReportRequisition }));
      saveSubject.complete();

      // THEN
      expect(prepaymentByAccountReportRequisitionService.create).toHaveBeenCalledWith(prepaymentByAccountReportRequisition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PrepaymentByAccountReportRequisition>>();
      const prepaymentByAccountReportRequisition = { id: 123 };
      jest.spyOn(prepaymentByAccountReportRequisitionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prepaymentByAccountReportRequisition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prepaymentByAccountReportRequisitionService.update).toHaveBeenCalledWith(prepaymentByAccountReportRequisition);
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
