jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ChannelTypeService } from '../service/channel-type.service';
import { IChannelType, ChannelType } from '../channel-type.model';

import { ChannelTypeUpdateComponent } from './channel-type-update.component';

describe('ChannelType Management Update Component', () => {
  let comp: ChannelTypeUpdateComponent;
  let fixture: ComponentFixture<ChannelTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let channelTypeService: ChannelTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ChannelTypeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ChannelTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ChannelTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    channelTypeService = TestBed.inject(ChannelTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const channelType: IChannelType = { id: 456 };

      activatedRoute.data = of({ channelType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(channelType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ChannelType>>();
      const channelType = { id: 123 };
      jest.spyOn(channelTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ channelType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: channelType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(channelTypeService.update).toHaveBeenCalledWith(channelType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ChannelType>>();
      const channelType = new ChannelType();
      jest.spyOn(channelTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ channelType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: channelType }));
      saveSubject.complete();

      // THEN
      expect(channelTypeService.create).toHaveBeenCalledWith(channelType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ChannelType>>();
      const channelType = { id: 123 };
      jest.spyOn(channelTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ channelType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(channelTypeService.update).toHaveBeenCalledWith(channelType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
