To rebuild card.js.

1. Download https://github.com/melloware/card/tree/primefaces branch of code which has many bug fixes.

2. Execute "npm install"

3. Replace creditcard\node_modules\payment\lib\index.js with the contents of file .\payment.ts from this dir.

4. Execute "npm run compile" to build the new card.css and card.js files.
