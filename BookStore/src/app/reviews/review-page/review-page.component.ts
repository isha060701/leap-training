import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-review-page',
  templateUrl: './review-page.component.html',
  styleUrls: ['./review-page.component.css']
})
export class ReviewPageComponent{
  constructor(private route:ActivatedRoute){}
  bookId=-1;

  ngOnInit(){
    this.bookId=this.route.snapshot.params['id'];
  }
}
