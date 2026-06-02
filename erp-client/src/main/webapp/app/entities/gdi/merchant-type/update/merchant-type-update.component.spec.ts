jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MerchantTypeService } from '../service/merchant-type.service';
import { IMerchantType, MerchantType } from '../merchant-type.model';

import { MerchantTypeUpdateComponent } from './merchant-type-update.component';

describe('MerchantType Management Update Component', () => {
  let comp: MerchantTypeUpdateComponent;
  let fixture: ComponentFixture<MerchantTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let merchantTypeService: MerchantTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MerchantTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(MerchantTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MerchantTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    merchantTypeService = TestBed.inject(MerchantTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const merchantType: IMerchantType = { id: 456 };

      activatedRoute.data = of({ merchantType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(merchantType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MerchantType>>();
      const merchantType = { id: 123 };
      jest.spyOn(merchantTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ merchantType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: merchantType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(merchantTypeService.update).toHaveBeenCalledWith(merchantType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MerchantType>>();
      const merchantType = new MerchantType();
      jest.spyOn(merchantTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ merchantType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: merchantType }));
      saveSubject.complete();

      // THEN
      expect(merchantTypeService.create).toHaveBeenCalledWith(merchantType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MerchantType>>();
      const merchantType = { id: 123 };
      jest.spyOn(merchantTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ merchantType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(merchantTypeService.update).toHaveBeenCalledWith(merchantType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
