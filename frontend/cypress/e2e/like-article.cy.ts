it('Like article', () => {
  cy.visit('/login');
  cy.url().should('include', '/login');
  cy.get('.form.nickname input').clear().type('admin');
  cy.get('.form.password input').clear().type('admincool');
  cy.get('.content .enter').click();
  cy.url().should('not.include', 'login');
  cy.url().should('include', '/');
  let titleBefore = "";
  cy.get('poly-card:nth-child(2) article .title').then(($span) => {
    titleBefore = $span.get()[0].innerText;
  });
  let likesBefore = 0;
  cy.get('poly-card:nth-child(2) article .rating span').then(($span) => {
    likesBefore = Number($span.get()[0].innerText);
  });
  cy.get('poly-card:nth-child(2) article .rating').click();
  let likesAfter = 0;
  cy.get('poly-card:nth-child(2) .rating span').should(($rating) => {
    expect($rating.get()[0].innerText, `${likesBefore + 1}`);
    likesAfter = Number($rating.get()[0].innerText);
  });
  cy.get('poly-card:nth-child(2) article > a').click();
  cy.get('h2.title').should((title) => {
    expect(title.get()[0].innerText, titleBefore);
  });
  cy.get('article:first-child .rating span').should((rating) => {
    expect(rating.get()[0].innerText, `${likesAfter}`);
  });
  cy.get('article:first-child .rating').click();
});
