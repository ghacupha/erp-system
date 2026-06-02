jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CrbReportViewBandService } from '../service/crb-report-view-band.service';
import { ICrbReportViewBand, CrbReportViewBand } from '../crb-report-view-band.model';

import { CrbReportViewBandUpdateComponent } from './crb-report-view-band-update.component';

describe('CrbReportViewBand Management Update Component', () => {
  let comp: CrbReportViewBandUpdateComponent;
  let fixture: ComponentFixture<CrbReportViewBandUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbReportViewBandService: CrbReportViewBandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbReportViewBandUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbReportViewBandUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbReportViewBandUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbReportViewBandService = TestBed.inject(CrbReportViewBandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbReportViewBand: ICrbReportViewBand = { id: 456 };

      activatedRoute.data = of({ crbReportViewBand });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbReportViewBand));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbReportViewBand>>();
      const crbReportViewBand = { id: 123 };
      jest.spyOn(crbReportViewBandService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbReportViewBand });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbReportViewBand }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbReportViewBandService.update).toHaveBeenCalledWith(crbReportViewBand);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbReportViewBand>>();
      const crbReportViewBand = new CrbReportViewBand();
      jest.spyOn(crbReportViewBandService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbReportViewBand });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbReportViewBand }));
      saveSubject.complete();

      // THEN
      expect(crbReportViewBandService.create).toHaveBeenCalledWith(crbReportViewBand);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbReportViewBand>>();
      const crbReportViewBand = { id: 123 };
      jest.spyOn(crbReportViewBandService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbReportViewBand });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbReportViewBandService.update).toHaveBeenCalledWith(crbReportViewBand);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
