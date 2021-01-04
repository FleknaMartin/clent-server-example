package net.example.server.api.servicefacade;

import net.example.server.api.to.CreateCardPaymentResultTo;
import net.example.server.api.to.CreateCardPaymentTo;
import org.springframework.transaction.annotation.Transactional;

public interface ICardPaymentServiceFacade {

    CreateCardPaymentResultTo createCardPayment(CreateCardPaymentTo createCardTransaction);
}
