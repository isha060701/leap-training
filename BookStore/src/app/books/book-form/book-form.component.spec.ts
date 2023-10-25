import { ComponentFixture, TestBed } from "@angular/core/testing";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BookFormComponent } from "./book-form.component";
import { Book } from "src/app/models/book";

describe("BookFormComponent", () => {
  let component: BookFormComponent;
  let fixture: ComponentFixture<BookFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BookFormComponent],
      imports: [ReactiveFormsModule],
    }).compileComponents();

    fixture = TestBed.createComponent(BookFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  // it('should emit an event on click', () => {
  //   spyOn(component.createBook, 'emit'); // trigger the click
  //   const nativeElement = fixture.debugElement.nativeElement;
  //   const button = nativeElement.querySelector('button');
  //   button.dispatchEvent(new Event('click'));
  //   fixture.detectChanges(); // check the output event was triggered
  //   expect(component.createBook.emit).toHaveBeenCalled();
  // });

  it("should emit an event on click", () => {
    spyOn(component.createBook, "emit");
    const expected = new Book("The Silmarillion", "J R R Tolkien", "", -1);
    const form = fixture.debugElement.nativeElement.querySelector("form");
    component.bookForm.get("title")?.setValue(expected.title);
    component.bookForm.get("author")?.setValue(expected.author);
    form.dispatchEvent(new Event("ngSubmit"));
    expect(component.createBook.emit).toHaveBeenCalledWith(expected);
  });

  it("should validate content", () => {
    const titleCtrl = component.bookForm.get("title");
    const authorCtrl = component.bookForm.get("author");
    expect(component.bookForm.valid).toBeFalsy();
    expect(titleCtrl?.hasError("required")).toBeTruthy();
    expect(authorCtrl?.hasError("required")).toBeTruthy();
    titleCtrl?.setValue("The Inklings");
    authorCtrl?.setValue("Humphrey Carpenter");
    expect(component.bookForm.valid).toBeTruthy();
  });
});
