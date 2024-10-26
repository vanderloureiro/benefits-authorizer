# Benefits Authorizer

Autorizador de benefício do projeto take-home Cajú

### Stack
<ul>
    <li>Kotlin</li>
    <li>Spring Boot</li>
    <li>H2</li>
    <li>JUnit</li>
</ul>

### Execução

<b>Maven</b><br>
É possível rodar a aplicação entrando na pasta do projeto e executando o wrapper do maven com:

``./mvnw spring-boot:run``

<b>Docker</b><br>
Outra forma é através do Docker. Uma imagem docker pública foi disponibilizada no DockerHub:

``docker run -p 8080:8080 vanderloureiro/benefits-authorizer:latest``

### Acesso

Os endpoints estão documentos pelo Swagger. Após iniciar a aplicação, a documentação roda em:

``http://localhost:8080/swagger-ui/index.html``

### Banco de Dados

O projeto roda com um banco de dados relacional H2. A escolha foi pela simplicidade e portabilidade. 
Disponível acessa-lo pelo endereço:

``http://localhost:8080/h2-console``

Credenciais:<br>
<b>URL JDBC</b>: jdbc:h2:mem:mydb <br>
<b>User Name</b>: sa <br>
<b>password</b>: password<br>

Para facilitar testes, três usuários com saldos diferentes já são inseridos na base em cada inicialização. 
A base é apagada quando a aplicação encerra. 

## L4

Proposta de solução para problema de race condition

Há soluções como Lock Pessimista que são mais robustas, mas comprometem performance. 
Como detalhado que esse caso é uma pequena probabilidade de acontecer e o foco principal é performance, 
uma abordagem mais adequada seria com Lock Otimista.

A tabela de Account pode ter um campo de versão gerenciando seu ultimo estado. 
Caso a versão da escrita esteja diferente da versão de leitura, uma ação de retry e tratamento de exceção é feito.

### Considerações e melhorias

A organização de pacotes foi feita seguindo a abordagem de Package By Feature 
[(Link de artigo com mais detalhes)](https://medium.com/@vanderloureiro/desenvolvimento-modularizado-com-pacote-por-recurso-package-by-feature-b0b237fca8ef)

Há duplicidade de código nas versões L1 (RegisterTransactionService), L2 (RegisterTransactionFallbackService) e 
L3 (RegisterTransactionMerchantService). Como a versão final será a L3, as outras podem ser descartadas; se L1 e L2 
precisassem existir paralelamente, alguma abordagem de herança poderia ser usada.