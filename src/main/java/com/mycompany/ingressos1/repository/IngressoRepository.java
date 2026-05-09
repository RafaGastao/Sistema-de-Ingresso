package com.mycompany.ingressos1.repository;

import com.mycompany.ingressos1.model.Ingresso;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IngressoRepository extends MongoRepository<Ingresso, String> {

    List<Ingresso> findByNomeCliente(String nomeCliente);

    List<Ingresso> findByClienteLogin(String clienteLogin);

    List<Ingresso> findByEventoId(String eventoId);

    Optional<Ingresso> findByCodigoQr(String codigoQr);

    boolean existsByEventoIdAndClienteLoginAndEstadoNot(String eventoId, String clienteLogin, com.mycompany.ingressos1.model.EstadoIngresso estado);

    long countByEventoId(String eventoId);

    long countByEventoIdAndEstado(String eventoId, com.mycompany.ingressos1.model.EstadoIngresso estado);
}
