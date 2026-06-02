jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CrbNatureOfInformationService } from '../service/crb-nature-of-information.service';
import { ICrbNatureOfInformation, CrbNatureOfInformation } from '../crb-nature-of-information.model';

import { CrbNatureOfInformationUpdateComponent } from './crb-nature-of-information-update.component';

describe('CrbNatureOfInformation Management Update Component', () => {
  let comp: CrbNatureOfInformationUpdateComponent;
  let fixture: ComponentFixture<CrbNatureOfInformationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbNatureOfInformationService: CrbNatureOfInformationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbNatureOfInformationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbNatureOfInformationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbNatureOfInformationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbNatureOfInformationService = TestBed.inject(CrbNatureOfInformationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbNatureOfInformation: ICrbNatureOfInformation = { id: 456 };

      activatedRoute.data = of({ crbNatureOfInformation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbNatureOfInformation));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbNatureOfInformation>>();
      const crbNatureOfInformation = { id: 123 };
      jest.spyOn(crbNatureOfInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbNatureOfInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbNatureOfInformation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbNatureOfInformationService.update).toHaveBeenCalledWith(crbNatureOfInformation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbNatureOfInformation>>();
      const crbNatureOfInformation = new CrbNatureOfInformation();
      jest.spyOn(crbNatureOfInformationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbNatureOfInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbNatureOfInformation }));
      saveSubject.complete();

      // THEN
      expect(crbNatureOfInformationService.create).toHaveBeenCalledWith(crbNatureOfInformation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbNatureOfInformation>>();
      const crbNatureOfInformation = { id: 123 };
      jest.spyOn(crbNatureOfInformationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbNatureOfInformation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbNatureOfInformationService.update).toHaveBeenCalledWith(crbNatureOfInformation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
