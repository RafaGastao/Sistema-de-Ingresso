# Sistema de Ingressos
<<<<<<< HEAD

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
=======
Sistema web desenvolvido em Java com Spring Boot para gerenciamento de eventos, reserva de ingressos, login de clientes e controle administrativo de entrada por QR Code.
## Funcionalidades
- Login de cliente e administrador.
- Cadastro de clientes.
- Área do cliente com catálogo de eventos.
- Cadastro administrativo de eventos.
- Reserva de ingressos por clientes autenticados.
- Controle da quantidade de ingressos disponíveis.
- Bloqueio de reserva duplicada para o mesmo evento.
- Geração de código único para cada ingresso.
- Geração e exibição de QR Code do ingresso.
- Pagamento/confirmação de ingresso.
- Cancelamento de reserva.
- Validação administrativa de QR Code.
- Controle de estados do ingresso:
  - `RESERVADO`
  - `PAGO`
  - `CANCELADO`
  - `UTILIZADO`
- Relatório simples de ingressos por evento.
## Tecnologias Utilizadas
- Java 17
- Spring Boot 3.3.5
- Spring MVC
- Thymeleaf
- Spring Data MongoDB
- MongoDB Atlas
- Maven
- ZXing para geração de QR Code
- HTML
- CSS
## Estrutura do Projeto
O projeto segue uma organização em camadas:
```text
src/main/java/com/mycompany/ingressos1
├── controller
├── model
├── repository
├── service
└── Ingressos1.java
```
## Camadas
- **model:** classes principais do sistema, como Evento, Ingresso, Cliente e Reserva.
- **controller:** controla as rotas da aplicação web.
- **service:** contém as regras de negócio.
- **repository:** realiza o acesso ao banco de dados MongoDB.
- **templates:** páginas HTML renderizadas pelo Thymeleaf.
- **static:** arquivos CSS e recursos estáticos.
## Pré-requisitos
Antes de executar o projeto, é necessário ter instalado:
- JDK 17 ou superior
- Maven
- Conta ou instância MongoDB configurada
Para verificar se o Java está instalado, execute:
```bash
java -version
```
Para verificar se o Maven está instalado, execute:
```bash
mvn -version
```
## Configuração do Banco de Dados
A aplicação utiliza MongoDB. A conexão fica no arquivo:
```text
src/main/resources/application.properties
```
Exemplo de configuração:
```properties
spring.application.name=Ingressos1
spring.data.mongodb.uri=mongodb+srv://usuario:senha@cluster.mongodb.net/ingressos_db?retryWrites=true&w=majority
server.port=8080
```
Substitua o usuário, a senha e o endereço do cluster pelos dados do seu MongoDB.
## Como Executar a Aplicação
Clone o repositório:
```bash
git clone https://github.com/RafaGastao/Sistema-de-Ingresso.git
```
Acesse a pasta do projeto:
```bash
cd Ingressos1
```
Execute a aplicação com Maven:
```bash
mvn spring-boot:run
```
Depois, abra o navegador em:
```text
http://localhost:8080
```
## Executando pelo NetBeans
Também é possível executar pelo Apache NetBeans:
1. Abra o NetBeans.
2. Clique em **File > Open Project**.
3. Selecione a pasta do projeto.
4. Aguarde o Maven carregar as dependências.
5. Execute a classe principal:
```text
Ingressos1.java
```
Acesse no navegador:
```text
http://localhost:8080
```
## Acesso ao Sistema
### Administrador
O sistema possui um administrador padrão:
```text
Login: admin
Senha: 123
```
Com esse acesso, é possível:

- cadastrar eventos;
- visualizar eventos cadastrados;
- consultar ingressos por evento;
- validar QR Code;
- acompanhar relatórios simples.
### Cliente
O cliente pode criar uma conta na tela de cadastro e depois fazer login normalmente.
Com o acesso de cliente, é possível:
- visualizar eventos disponíveis;
- reservar ingressos;
- consultar ingressos adquiridos;
- acompanhar o status da reserva;
- visualizar QR Code do ingresso.
## Fluxo Principal do Sistema
1. O administrador acessa o sistema.
2. O administrador cadastra um evento com nome, descrição, data, horário, local, quantidade de ingressos e valor.
3. O cliente cria uma conta e faz login.
4. O cliente acessa o catálogo de eventos.
5. O cliente reserva um ingresso.
6. O sistema reduz automaticamente a quantidade de ingressos disponíveis.
7. O cliente realiza o pagamento/confirmação do ingresso.
8. O sistema gera e exibe o QR Code.
9. O administrador valida o QR Code na entrada do evento.
10. O sistema marca o ingresso como utilizado.
## Estados do Ingresso
O ingresso pode possuir os seguintes estados:
| Estado | Descrição |
|---|---|
| RESERVADO | Ingresso reservado pelo cliente. |
| PAGO | Ingresso pago ou confirmado. |
| CANCELADO | Reserva ou ingresso cancelado. |
| UTILIZADO | Ingresso validado e utilizado na entrada do evento. |
## Geração e Validação de QR Code
Cada ingresso recebe um código único gerado por hash. Esse código é utilizado para gerar o QR Code do ingresso.
No momento da entrada do evento, o administrador pode validar o código do QR Code. O sistema verifica se o ingresso:
- existe;
- está pago;
- já foi utilizado;
- foi cancelado;
- ainda está apenas reservado.
Caso o ingresso esteja válido, ele é marcado como `UTILIZADO`.
## Porta da Aplicação
Utilizar a porta:
```text
8080
```
>>>>>>> b06425bca8fcd748385835366995781916ce4ea1
