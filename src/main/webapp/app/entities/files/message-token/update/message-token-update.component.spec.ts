jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MessageTokenService } from '../service/message-token.service';
import { IMessageToken, MessageToken } from '../message-token.model';

import { MessageTokenUpdateComponent } from './message-token-update.component';

describe('Component Tests', () => {
  describe('MessageToken Management Update Component', () => {
    let comp: MessageTokenUpdateComponent;
    let fixture: ComponentFixture<MessageTokenUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let messageTokenService: MessageTokenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MessageTokenUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MessageTokenUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MessageTokenUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      messageTokenService = TestBed.inject(MessageTokenService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const messageToken: IMessageToken = { id: 456 };

        activatedRoute.data = of({ messageToken });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(messageToken));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MessageToken>>();
        const messageToken = { id: 123 };
        jest.spyOn(messageTokenService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ messageToken });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: messageToken }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(messageTokenService.update).toHaveBeenCalledWith(messageToken);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MessageToken>>();
        const messageToken = new MessageToken();
        jest.spyOn(messageTokenService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ messageToken });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: messageToken }));
        saveSubject.complete();

        // THEN
        expect(messageTokenService.create).toHaveBeenCalledWith(messageToken);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MessageToken>>();
        const messageToken = { id: 123 };
        jest.spyOn(messageTokenService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ messageToken });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(messageTokenService.update).toHaveBeenCalledWith(messageToken);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
