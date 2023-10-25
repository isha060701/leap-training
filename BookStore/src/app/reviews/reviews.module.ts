import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReviewsRoutingModule } from './reviews-routing.module';
import { ReviewPageComponent } from './review-page/review-page.component';
import { ReviewFormComponent } from './review-form/review-form.component';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    ReviewPageComponent,
    ReviewFormComponent
  ],
  imports: [
    CommonModule,
    ReviewsRoutingModule,
    FormsModule
  ]
})
export class ReviewsModule { }
