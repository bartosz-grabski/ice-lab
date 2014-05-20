
module FinancialNews {

  enum Currency {PLN, USD, EUR, CHF};


  interface NewsReceiver
  {
    void exchangeRate(Currency curr1, Currency curr2, float rate);   //np. (EUR, PLN, 4,14)
    void interestRate(Currency curr, float rate);   //np. (EUR, 1,2%)
  };


  interface NewsServer {
    void registerForNews (NewsReceiver* subscriber);
  };


};