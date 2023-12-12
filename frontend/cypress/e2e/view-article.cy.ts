it('View article', () => {
  cy.visit('/');
  cy.url().should('include', '/');
  let titleBefore = "";
  cy.get('poly-card:nth-child(2) article .title').then(($span) => {
    titleBefore = $span.get()[0].innerText;
  });
  let viewsBefore = 0;
  cy.get('poly-card:nth-child(2) article .views span').then(($span) => {
    viewsBefore = Number($span.get()[0].innerText);
  });
  cy.get('poly-card:nth-child(2) article > a').click();
  cy.get('h2.title').should((title) => {
    expect(title.get()[0].innerText, titleBefore);
  });
  cy.get('article:first-child .views span').should((views) => {
    expect(views.get()[0].innerText, `${viewsBefore + 1}`);
  });
});
