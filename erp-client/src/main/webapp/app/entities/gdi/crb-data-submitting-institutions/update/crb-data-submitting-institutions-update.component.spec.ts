jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CrbDataSubmittingInstitutionsService } from '../service/crb-data-submitting-institutions.service';
import { ICrbDataSubmittingInstitutions, CrbDataSubmittingInstitutions } from '../crb-data-submitting-institutions.model';

import { CrbDataSubmittingInstitutionsUpdateComponent } from './crb-data-submitting-institutions-update.component';

describe('CrbDataSubmittingInstitutions Management Update Component', () => {
  let comp: CrbDataSubmittingInstitutionsUpdateComponent;
  let fixture: ComponentFixture<CrbDataSubmittingInstitutionsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbDataSubmittingInstitutionsService: CrbDataSubmittingInstitutionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbDataSubmittingInstitutionsUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbDataSubmittingInstitutionsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbDataSubmittingInstitutionsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbDataSubmittingInstitutionsService = TestBed.inject(CrbDataSubmittingInstitutionsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbDataSubmittingInstitutions: ICrbDataSubmittingInstitutions = { id: 456 };

      activatedRoute.data = of({ crbDataSubmittingInstitutions });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbDataSubmittingInstitutions));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbDataSubmittingInstitutions>>();
      const crbDataSubmittingInstitutions = { id: 123 };
      jest.spyOn(crbDataSubmittingInstitutionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbDataSubmittingInstitutions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbDataSubmittingInstitutions }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbDataSubmittingInstitutionsService.update).toHaveBeenCalledWith(crbDataSubmittingInstitutions);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbDataSubmittingInstitutions>>();
      const crbDataSubmittingInstitutions = new CrbDataSubmittingInstitutions();
      jest.spyOn(crbDataSubmittingInstitutionsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbDataSubmittingInstitutions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbDataSubmittingInstitutions }));
      saveSubject.complete();

      // THEN
      expect(crbDataSubmittingInstitutionsService.create).toHaveBeenCalledWith(crbDataSubmittingInstitutions);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbDataSubmittingInstitutions>>();
      const crbDataSubmittingInstitutions = { id: 123 };
      jest.spyOn(crbDataSubmittingInstitutionsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbDataSubmittingInstitutions });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbDataSubmittingInstitutionsService.update).toHaveBeenCalledWith(crbDataSubmittingInstitutions);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
