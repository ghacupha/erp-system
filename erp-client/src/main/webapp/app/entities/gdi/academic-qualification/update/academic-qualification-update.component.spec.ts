jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AcademicQualificationService } from '../service/academic-qualification.service';
import { IAcademicQualification, AcademicQualification } from '../academic-qualification.model';

import { AcademicQualificationUpdateComponent } from './academic-qualification-update.component';

describe('AcademicQualification Management Update Component', () => {
  let comp: AcademicQualificationUpdateComponent;
  let fixture: ComponentFixture<AcademicQualificationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let academicQualificationService: AcademicQualificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AcademicQualificationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AcademicQualificationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AcademicQualificationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    academicQualificationService = TestBed.inject(AcademicQualificationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const academicQualification: IAcademicQualification = { id: 456 };

      activatedRoute.data = of({ academicQualification });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(academicQualification));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AcademicQualification>>();
      const academicQualification = { id: 123 };
      jest.spyOn(academicQualificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ academicQualification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: academicQualification }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(academicQualificationService.update).toHaveBeenCalledWith(academicQualification);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AcademicQualification>>();
      const academicQualification = new AcademicQualification();
      jest.spyOn(academicQualificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ academicQualification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: academicQualification }));
      saveSubject.complete();

      // THEN
      expect(academicQualificationService.create).toHaveBeenCalledWith(academicQualification);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AcademicQualification>>();
      const academicQualification = { id: 123 };
      jest.spyOn(academicQualificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ academicQualification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(academicQualificationService.update).toHaveBeenCalledWith(academicQualification);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
