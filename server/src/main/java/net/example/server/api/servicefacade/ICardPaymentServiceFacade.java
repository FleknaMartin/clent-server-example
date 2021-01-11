package net.example.server.api.servicefacade;

import net.example.server.api.to.CreateCardPaymentResultTo;
import net.example.server.api.to.CreateCardPaymentTo;

public interface ICardPaymentServiceFacade {

    CreateCardPaymentResultTo createCardPayment(CreateCardPaymentTo createCardTransaction);
}
