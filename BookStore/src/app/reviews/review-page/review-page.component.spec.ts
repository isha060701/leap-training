import { ComponentFixture, TestBed } from "@angular/core/testing";

import { ReviewPageComponent } from "./review-page.component";
import { RouterTestingModule } from "@angular/router/testing";
import { Component, Input } from "@angular/core";

@Component({ selector: "app-review-form", template: "mock review form" })
class ReviewFormMockComponent {
  @Input() bookId: number = -1;
}
describe("ReviewPageComponent", () => {
  let component: ReviewPageComponent;
  let fixture: ComponentFixture<ReviewPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReviewPageComponent, ReviewFormMockComponent],
      imports: [RouterTestingModule],
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
