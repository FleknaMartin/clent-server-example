package net.example.client.api.adapter;

import net.example.client.api.to.CreatePaymentResultTo;
import net.example.client.api.to.CreatePaymentTo;

public interface IPaymentServiceConnectorAdapter {

    CreatePaymentResultTo send(CreatePaymentTo payment);
}
