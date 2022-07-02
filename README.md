# Desafio Tecnico MeuTudo
Repositório Dedicado ao desafio Técnico da Meu Tudo de Backend

API desenvolvida com Spring Boot para um sistema semelhante a um sistema bancário

Primeiramente é válido dizer que essa aplicação foi toda desenvolvida seguintos os princípios do TDD, ou seja os testes que nela estão escritos, embora não sejam muitos foram escritos antes da implementação.

## Instalação e Configuração
Para instalar e rodar essa API é apenas necessário fazer o clone do projeto e importá-la como projeto maven existente na sua IDE favorita,
não são necessários configurações adicionais, visto que estou usando H2 como banco de dados pois embora ele seja bem simples e nesse caso baseado em arquivo,
o escolhi por acreditar que ele atenda o escopo além de que é possível facilmente trocar para um banco mais potente.

## Modelagem de Entidades
Por essa aplicação ter um escopo simples, procurei também modelar de forma simples e objetiva, temos apenas 2 entidades: Account e Transfer.

A Account representa uma conta de usuário do banco tendo apenas 5 atributos e o CPF como id.

Já A Transfer representa uma transferência realizada ou agendada entre 2 accounts, o emissor e o remetente, nesse caso utilizei um enum para marcar a
transferência como realizada, agendada e revertida, nesse último caso uma espécie de "soft delete" desse registro.

## Operações
Aqui é válido falar que a api conta com a documentação do Swagger logo na raiz do caminho, com isso fica bem mais fácil identificar seus endpoins.

### Consulta Saldo:
Endpoint: "accounts/balance/{cpf}"

Nessa primeira exigência do desafio fiquei na dúvida se a consulta seria apenas do saldo de uma account ou de um extrato das movimentações, até fiz essa pergunta a
recrutadora, ela acabou não me respodendo e seguindo com o desafio optei por ser apenas uma consulta de saldo da conta, escolhi o sentido literal da palavra saldo

![image](https://user-images.githubusercontent.com/39336736/176990660-5e06b874-6dda-4460-9ab3-a4b6cd914978.png)

### Transferência entre Contas:
Endpoint: "transfers"

Nessa exigência procurei utilizar Dto como em outros lugares para não expor a entidade e nesse caso para realizar algumas validações, assim como nessa operação
procurei desacoplar a regra de negócio do controller utilizando Services e deixando apenas o controller com a resposabilidade de controllar as requisições e
respostas do servidor.
Esse endpoint possui mais de um comportamento e para que ele funcione apenas como uma transferência simples é necessário enviar no parâmetro de "installments"
o número 1 que no caso indicaria que não está parcelando a transferência.

![image](https://user-images.githubusercontent.com/39336736/176990961-aa7cd689-4f34-46d8-9ab5-8e3ba5e9a1f4.png)

### Reverter uma Transferências:
Endpoint: "transfers/revert/{id}"

Nessa exigência tem o detalhe de poder reverter tanto uma transferência que foi realizada quanto como se fosse cancelar uma transferência que foi agendada,
novamente temos 2 comportamentos que nesse caso depende apenas se o id, que é o id da transferência, representa uma transferência marcada como realizada
ou como agendada.

Caso seja uma transferência que já foi realizada, o sistema faz um "soft delete" marca esse registro como revertido e obviamente reverte a transferência criando outra transferência "ao contrário" com os mesmos valores porém invertendo o emissor e o remetente;

Caso seja uma transferência que foi agendada, o sistema apenas realiza a exclusão do registro.

![image](https://user-images.githubusercontent.com/39336736/176991260-4d1f68b1-315b-4082-acc7-68072ef9356b.png)

### Programar uma Transferência Futura Parcelada
Endpoint: "transfers"

Como dito acima essa operação compartilha o memsmo endpoint de realizar uma transferência simples, porém ao acrescentar o número de parcelas ou "installments" esse endpoint assume esse comportamento que agenda transações futuras.

## Considerações Finais
Obrigado pela compreensão e atenção, um processo seletivo requer bastante trabalho e esforço de ambas as partes, dei o meu melhor aqui, procurei mostrar o que sei e usar do prazo que tinha para entregar o melhor.
Em caso de dúvidas e feedbacks, por favor entrem em contato, me coloco a disposição para batermos um papo legal sobre esse desafio e sobre a vaga.




