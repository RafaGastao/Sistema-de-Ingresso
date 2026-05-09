package com.mycompany.ingressos1.repository;

import com.mycompany.ingressos1.model.Reserva;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservaRepository extends MongoRepository<Reserva, String> {

    List<Reserva> findByClienteLogin(String clienteLogin);

    Optional<Reserva> findByIngressoId(String ingressoId);
}
