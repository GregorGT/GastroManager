package com.gastromanager.models;

import java.io.Serializable;

public class PaymentInformationQuery implements Serializable {

        private String currency;
        private Double taxes;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Double getTaxes() {
            return taxes;
        }

        public void setTaxes(Double taxes) {
            this.taxes = taxes;
        }

}
