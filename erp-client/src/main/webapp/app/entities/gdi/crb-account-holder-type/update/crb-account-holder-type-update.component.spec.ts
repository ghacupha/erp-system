jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CrbAccountHolderTypeService } from '../service/crb-account-holder-type.service';
import { ICrbAccountHolderType, CrbAccountHolderType } from '../crb-account-holder-type.model';

import { CrbAccountHolderTypeUpdateComponent } from './crb-account-holder-type-update.component';

describe('CrbAccountHolderType Management Update Component', () => {
  let comp: CrbAccountHolderTypeUpdateComponent;
  let fixture: ComponentFixture<CrbAccountHolderTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let crbAccountHolderTypeService: CrbAccountHolderTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbAccountHolderTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CrbAccountHolderTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CrbAccountHolderTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    crbAccountHolderTypeService = TestBed.inject(CrbAccountHolderTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const crbAccountHolderType: ICrbAccountHolderType = { id: 456 };

      activatedRoute.data = of({ crbAccountHolderType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(crbAccountHolderType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbAccountHolderType>>();
      const crbAccountHolderType = { id: 123 };
      jest.spyOn(crbAccountHolderTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbAccountHolderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbAccountHolderType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(crbAccountHolderTypeService.update).toHaveBeenCalledWith(crbAccountHolderType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbAccountHolderType>>();
      const crbAccountHolderType = new CrbAccountHolderType();
      jest.spyOn(crbAccountHolderTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbAccountHolderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: crbAccountHolderType }));
      saveSubject.complete();

      // THEN
      expect(crbAccountHolderTypeService.create).toHaveBeenCalledWith(crbAccountHolderType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CrbAccountHolderType>>();
      const crbAccountHolderType = { id: 123 };
      jest.spyOn(crbAccountHolderTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ crbAccountHolderType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(crbAccountHolderTypeService.update).toHaveBeenCalledWith(crbAccountHolderType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
