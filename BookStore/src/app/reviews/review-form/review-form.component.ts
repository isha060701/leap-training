import { Component, Input } from "@angular/core";
import { NgForm } from "@angular/forms";
import { Review } from "src/app/models/review";

@Component({
  selector: "app-review-form",
  templateUrl: "./review-form.component.html",
  styleUrls: ["./review-form.component.css"],
})
export class ReviewFormComponent {
  @Input() bookId: number = -1;
  review: Review = new Review("", -1);
  ngOnInit() {
    this.review = new Review("", this.bookId);
  }

  submit(form: NgForm)
  {
    console.log(this.review.content);
    form.resetForm();
  }
}
