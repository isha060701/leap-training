import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { BookPageComponent } from './books/book-page/book-page.component';
import { AboutPageComponent } from './about-page/about-page.component';

const routes:Routes=[
  {path:"",component:BookPageComponent},
  {path:"about",component:AboutPageComponent}
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports:[RouterModule]
})
export class AppRoutingModule { 
  
}
