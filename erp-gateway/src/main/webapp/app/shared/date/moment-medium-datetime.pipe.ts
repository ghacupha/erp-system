import {Pipe, PipeTransform} from "@angular/core";
import {Moment} from "moment";

@Pipe({
  name: 'momentMediumDateTime'
})
export class MomentMediumDatetimePipe implements PipeTransform {
  transform(day: Moment | null | undefined): string {
    return day ? day.format('D MMM YYYY HH:mm:ss') : '';
  }
}
