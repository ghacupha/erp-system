jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AssetAdditionsReportService } from '../service/asset-additions-report.service';
import { IAssetAdditionsReport, AssetAdditionsReport } from '../asset-additions-report.model';
import { IApplicationUser } from 'app/entities/people/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/people/application-user/service/application-user.service';

import { AssetAdditionsReportUpdateComponent } from './asset-additions-report-update.component';

describe('AssetAdditionsReport Management Update Component', () => {
  let comp: AssetAdditionsReportUpdateComponent;
  let fixture: ComponentFixture<AssetAdditionsReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let assetAdditionsReportService: AssetAdditionsReportService;
  let applicationUserService: ApplicationUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AssetAdditionsReportUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AssetAdditionsReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AssetAdditionsReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    assetAdditionsReportService = TestBed.inject(AssetAdditionsReportService);
    applicationUserService = TestBed.inject(ApplicationUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ApplicationUser query and add missing value', () => {
      const assetAdditionsReport: IAssetAdditionsReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 72125 };
      assetAdditionsReport.requestedBy = requestedBy;

      const applicationUserCollection: IApplicationUser[] = [{ id: 31890 }];
      jest.spyOn(applicationUserService, 'query').mockReturnValue(of(new HttpResponse({ body: applicationUserCollection })));
      const additionalApplicationUsers = [requestedBy];
      const expectedCollection: IApplicationUser[] = [...additionalApplicationUsers, ...applicationUserCollection];
      jest.spyOn(applicationUserService, 'addApplicationUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ assetAdditionsReport });
      comp.ngOnInit();

      expect(applicationUserService.query).toHaveBeenCalled();
      expect(applicationUserService.addApplicationUserToCollectionIfMissing).toHaveBeenCalledWith(
        applicationUserCollection,
        ...additionalApplicationUsers
      );
      expect(comp.applicationUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const assetAdditionsReport: IAssetAdditionsReport = { id: 456 };
      const requestedBy: IApplicationUser = { id: 75423 };
      assetAdditionsReport.requestedBy = requestedBy;

      activatedRoute.data = of({ assetAdditionsReport });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(assetAdditionsReport));
      expect(comp.applicationUsersSharedCollection).toContain(requestedBy);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetAdditionsReport>>();
      const assetAdditionsReport = { id: 123 };
      jest.spyOn(assetAdditionsReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetAdditionsReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetAdditionsReport }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(assetAdditionsReportService.update).toHaveBeenCalledWith(assetAdditionsReport);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetAdditionsReport>>();
      const assetAdditionsReport = new AssetAdditionsReport();
      jest.spyOn(assetAdditionsReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetAdditionsReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: assetAdditionsReport }));
      saveSubject.complete();

      // THEN
      expect(assetAdditionsReportService.create).toHaveBeenCalledWith(assetAdditionsReport);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AssetAdditionsReport>>();
      const assetAdditionsReport = { id: 123 };
      jest.spyOn(assetAdditionsReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ assetAdditionsReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(assetAdditionsReportService.update).toHaveBeenCalledWith(assetAdditionsReport);
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
