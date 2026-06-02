jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RouAssetListReportService } from '../service/rou-asset-list-report.service';
import { IRouAssetListReport, RouAssetListReport } from '../rou-asset-list-report.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';

import { RouAssetListReportUpdateComponent } from './rou-asset-list-report-update.component';

describe('RouAssetListReport Management Update Component', () => {
  let comp: RouAssetListReportUpdateComponent;
  let fixture: ComponentFixture<RouAssetListReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rouAssetListReportService: RouAssetListReportService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RouAssetListReportUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RouAssetListReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RouAssetListReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rouAssetListReportService = TestBed.inject(RouAssetListReportService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const rouAssetListReport: IRouAssetListReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 5014 };
      rouAssetListReport.requestedBy = requestedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 54813 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rouAssetListReport });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rouAssetListReport: IRouAssetListReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 65871 };
      rouAssetListReport.requestedBy = requestedBy;

      activatedRoute.data = of({ rouAssetListReport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rouAssetListReport));
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouAssetListReport>>();
      const rouAssetListReport = { id: 123 };
      jest.spyOn(rouAssetListReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouAssetListReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouAssetListReport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rouAssetListReportService.update).toHaveBeenCalledWith(rouAssetListReport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouAssetListReport>>();
      const rouAssetListReport = new RouAssetListReport();
      jest.spyOn(rouAssetListReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouAssetListReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rouAssetListReport }));
      saveSubject.complete();

      // THEN
      expect(rouAssetListReportService.create).toHaveBeenCalledWith(rouAssetListReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RouAssetListReport>>();
      const rouAssetListReport = { id: 123 };
      jest.spyOn(rouAssetListReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rouAssetListReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rouAssetListReportService.update).toHaveBeenCalledWith(rouAssetListReport);
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
