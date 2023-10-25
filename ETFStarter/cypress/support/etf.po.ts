export class TablePageObject {
    checkBookTableHeader(tableHeader: string) {
      cy.get("app-root h2").should("contain.text", tableHeader);
    }
    getBookTableRows() {
      return cy.get("tbody > tr");
    }
    getBookTableFirstRowTitleColumn() {
      return cy.get("tbody > tr:nth-child(1) > :nth-child(1)").invoke("text");
    }
    getBookTableLastRowTitleColumn() {
      return cy.get("tbody > tr:last-child > :nth-child(1)").invoke("text");
    }
}