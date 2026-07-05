jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CrbSubscriptionStatusTypeCodeService } from '../service/crb-subscription-status-type-code.service';

import { CrbSubscriptionStatusTypeCodeDeleteDialogComponent } from './crb-subscription-status-type-code-delete-dialog.component';

describe('CrbSubscriptionStatusTypeCode Management Delete Component', () => {
  let comp: CrbSubscriptionStatusTypeCodeDeleteDialogComponent;
  let fixture: ComponentFixture<CrbSubscriptionStatusTypeCodeDeleteDialogComponent>;
  let service: CrbSubscriptionStatusTypeCodeService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbSubscriptionStatusTypeCodeDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(CrbSubscriptionStatusTypeCodeDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CrbSubscriptionStatusTypeCodeDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CrbSubscriptionStatusTypeCodeService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
