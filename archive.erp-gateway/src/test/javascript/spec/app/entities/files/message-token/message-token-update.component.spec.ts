import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { MessageTokenUpdateComponent } from 'app/entities/files/message-token/message-token-update.component';
import { MessageTokenService } from 'app/entities/files/message-token/message-token.service';
import { MessageToken } from 'app/shared/model/files/message-token.model';

describe('Component Tests', () => {
  describe('MessageToken Management Update Component', () => {
    let comp: MessageTokenUpdateComponent;
    let fixture: ComponentFixture<MessageTokenUpdateComponent>;
    let service: MessageTokenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [MessageTokenUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(MessageTokenUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MessageTokenUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MessageTokenService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MessageToken(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new MessageToken();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
