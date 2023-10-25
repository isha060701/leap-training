export class AppPageObject {
  navigateToHomePage() {
    cy.visit("/");
  }
  checkTitle(title: string) {
    cy.get("app-root h1").should("contain.text", title);
  }
}
