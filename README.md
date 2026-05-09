# Sistema de Ingressos

Aplicacao web em Java com Spring Boot, Thymeleaf e MongoDB para cadastro de eventos, login de clientes, reserva de ingressos e controle administrativo por QR Code.

## Funcionalidades

- Login de cliente e administrador.
- Area do cliente com catalogo de eventos e lista de reservas.
- Cadastro administrativo de eventos com descricao, data, horario, local, estoque e valor.
- Reserva de ingressos Normal, Meia-entrada e VIP.
- Bloqueio de reserva quando nao ha ingressos disponiveis.
- Bloqueio de reserva duplicada ativa para o mesmo cliente e evento.
- Geracao de codigo unico por ingresso usando hash SHA-256.
- Geracao de QR Code para cada ingresso.
- Validacao administrativa de ingresso por codigo do QR Code.
- Controle de estados: RESERVADO, CONFIRMADO, CANCELADO e UTILIZADO.
- Relatorio simples por evento com ingressos reservados/confirmados, utilizados e disponiveis.

## Tecnologias

- Java 17
- Spring Boot 3.3.5
- Thymeleaf
- Spring Data MongoDB
- MongoDB Atlas
- ZXing para QR Code
- Maven

## Como executar

1. Instale o JDK 17.
2. Instale o Maven ou use o Maven embutido no Apache NetBeans.
3. Clone o repositorio.
4. Entre na pasta do projeto.
5. Confira a conexao do MongoDB em `src/main/resources/application.properties`.
6. Execute:

```bash
mvn spring-boot:run
```

Se estiver usando o Maven do NetBeans no Windows, um exemplo de comando e:

```powershell
$env:JAVA_HOME='C:\Program Files\Apache NetBeans\jdk'
& 'C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd' spring-boot:run
```

Depois acesse:

```text
http://localhost:8080
```

## Acessos

Administrador:

```text
login: admin
senha: 123
```

Cliente:

Crie uma conta em `/cadastro-usuario` e faca login normalmente.

## Fluxo de uso

1. O administrador acessa `/admin` e cadastra eventos.
2. O cliente acessa a area do cliente e visualiza os eventos.
3. O cliente reserva um ingresso para um evento disponivel.
4. O sistema reduz automaticamente a quantidade de ingressos disponiveis.
5. O cliente confirma o ingresso e visualiza o QR Code.
6. O administrador acessa `/admin/validar`, informa o codigo do QR Code e valida a entrada.
7. O sistema marca o ingresso como UTILIZADO e impede uso duplicado.

## Documento da atividade

O arquivo `DOCUMENTACAO_ABNT.md` contem a lista de requisitos funcionais e nao funcionais, alem dos diagramas de caso de uso e componentes em Mermaid.
