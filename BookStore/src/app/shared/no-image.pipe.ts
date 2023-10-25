import { LocationStrategy } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'noImage'
})
export class NoImagePipe implements PipeTransform {

  constructor(private locationStrategy: LocationStrategy) {}
  transform(path: string): string {
  return path ? path
  : this.locationStrategy.getBaseHref() +
  'assets/images/NoImage.svg';
  }

  // transform(value: string): string{
  //   return value ? value : '/assets/images/NoImage.svg';
  // }

}
