import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'size',
  standalone: true
})
export class SizePipe implements PipeTransform {

  transform(value: number): string {
    if (value < 1024) {
      return value + ' B';
    } else if (value >= 1024 && value < 1048576) {
      return (value / 1024).toFixed(2) + ' KB';
    } else {
      return (value / 1048576).toFixed(2) + ' MB';
    }
  }

}
