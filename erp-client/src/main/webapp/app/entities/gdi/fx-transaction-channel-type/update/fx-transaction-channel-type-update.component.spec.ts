jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FxTransactionChannelTypeService } from '../service/fx-transaction-channel-type.service';
import { IFxTransactionChannelType, FxTransactionChannelType } from '../fx-transaction-channel-type.model';

import { FxTransactionChannelTypeUpdateComponent } from './fx-transaction-channel-type-update.component';

describe('FxTransactionChannelType Management Update Component', () => {
  let comp: FxTransactionChannelTypeUpdateComponent;
  let fixture: ComponentFixture<FxTransactionChannelTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let fxTransactionChannelTypeService: FxTransactionChannelTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FxTransactionChannelTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FxTransactionChannelTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FxTransactionChannelTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fxTransactionChannelTypeService = TestBed.inject(FxTransactionChannelTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const fxTransactionChannelType: IFxTransactionChannelType = { id: 456 };

      activatedRoute.data = of({ fxTransactionChannelType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(fxTransactionChannelType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxTransactionChannelType>>();
      const fxTransactionChannelType = { id: 123 };
      jest.spyOn(fxTransactionChannelTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxTransactionChannelType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fxTransactionChannelType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(fxTransactionChannelTypeService.update).toHaveBeenCalledWith(fxTransactionChannelType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxTransactionChannelType>>();
      const fxTransactionChannelType = new FxTransactionChannelType();
      jest.spyOn(fxTransactionChannelTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxTransactionChannelType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: fxTransactionChannelType }));
      saveSubject.complete();

      // THEN
      expect(fxTransactionChannelTypeService.create).toHaveBeenCalledWith(fxTransactionChannelType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FxTransactionChannelType>>();
      const fxTransactionChannelType = { id: 123 };
      jest.spyOn(fxTransactionChannelTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fxTransactionChannelType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fxTransactionChannelTypeService.update).toHaveBeenCalledWith(fxTransactionChannelType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
