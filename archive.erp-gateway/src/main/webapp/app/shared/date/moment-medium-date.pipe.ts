import {Pipe, PipeTransform} from "@angular/core";
import { Moment } from 'moment';

@Pipe({
  name: 'momentMediumDate',
})
export class MomentMediumDatePipe implements PipeTransform {
  transform(day: Moment | null | undefined): string {
    return day ? day.format('D MMM YYYY') : '';
  }
}
