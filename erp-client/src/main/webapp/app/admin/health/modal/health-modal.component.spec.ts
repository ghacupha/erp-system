///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { HealthModalComponent } from './health-modal.component';

describe('HealthModalComponent', () => {
  let comp: HealthModalComponent;
  let fixture: ComponentFixture<HealthModalComponent>;
  let mockActiveModal: NgbActiveModal;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [HealthModalComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(HealthModalComponent, '')
        .compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(HealthModalComponent);
    comp = fixture.componentInstance;
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('readableValue', () => {
    it('should return stringify value', () => {
      // GIVEN
      comp.health = undefined;

      // WHEN
      const result = comp.readableValue({ name: 'jhipster' });

      // THEN
      expect(result).toEqual('{"name":"jhipster"}');
    });

    it('should return string value', () => {
      // GIVEN
      comp.health = undefined;

      // WHEN
      const result = comp.readableValue('jhipster');

      // THEN
      expect(result).toEqual('jhipster');
    });

    it('should return storage space in an human readable unit (GB)', () => {
      // GIVEN
      comp.health = {
        key: 'diskSpace',
        value: {
          status: 'UP',
        },
      };

      // WHEN
      const result = comp.readableValue(1073741825);

      // THEN
      expect(result).toEqual('1.00 GB');
    });

    it('should return storage space in an human readable unit (MB)', () => {
      // GIVEN
      comp.health = {
        key: 'diskSpace',
        value: {
          status: 'UP',
        },
      };

      // WHEN
      const result = comp.readableValue(1073741824);

      // THEN
      expect(result).toEqual('1024.00 MB');
    });

    it('should return string value', () => {
      // GIVEN
      comp.health = {
        key: 'mail',
        value: {
          status: 'UP',
        },
      };

      // WHEN
      const result = comp.readableValue(1234);

      // THEN
      expect(result).toEqual('1234');
    });
  });

  describe('dismiss', () => {
    it('should call dismiss when dismiss modal is called', () => {
      // GIVEN
      const spy = jest.spyOn(mockActiveModal, 'dismiss');

      // WHEN
      comp.dismiss();

      // THEN
      expect(spy).toHaveBeenCalled();
    });
  });
});
