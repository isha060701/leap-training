export class BookPage {
  addTitle(title: string) {
    cy.get("app-book-form input#title").type(title);
  }
  addAuthor(name: string) {
    cy.get("app-book-form input#author").type(name);
  }
  clickAddBook() {
    cy.get("button").contains("Add Book").click();
  }
  checkAddButtonDisabled() {
    cy.get("button").contains("Add Book").should("be.disabled");
  }
  checkAddButtonEnabled() {
    cy.get("button").contains("Add Book").should("not.be.disabled");
  }
}
