# Aplicativo SwissKnife
## Autor

- Igor Josafa Torres Barbosa - RA 72300095

## Descrição do Projeto

Trata-se da Sistematização da matéria de Programação para Dispositivos Móveis, do curso de Análise e Desenvolvimento de Sistemas do Centro Universitário de Brasília (CEUB).

Este projeto consiste no desenvolvimento de um aplicativo móvel para dispositivos Android, utilizando a linguagem de programação Kotlin. O app em questão se chama SwissKnife, sendo um aplicativo 2-em-1 com as seguintes funcionalidades:

- Calculadora: realiza cálculos de soma, subtração, multiplicação e divisão;

- Gerenciador de tarefas: registra e armazena as tarefas do usuário do aplicativo, juntamente com sua descrição, e com opção para notificação na data e hora agendada.

## Instalação do App

O app pode ser instalado em dispositivos móveis Android utilizando o arquivo SwissKnife.apk, presente na raiz do projeto. Para isso:

1. Permita a instalação de fontes desconhecidas no seu celular Android, em: Configurações -> Segurança -> Fontes desconhecidas.
2. Baixe o arquivo SwissKnife.apk e execute a instalação no seu aparelho.

Outra possibilidade de execução é através do Android Studio:

1. Clone o repositório https://github.com/igorjosafa/swiss-knife.git no seu computador.
2. Abra a pasta do projeto no Android Studio.
3. Sincronize as dependências através do Gradle.
4. Configure um emulador no Android Studio em AVD Manager.
5. Clique no botão Run na barra superior do Android Studio.

## Instruções de Uso

O vídeo Demonstracao-de-uso.mp4, presente na raiz do projeto, demonstra as possibilidades de uso do app, que serão descritas a seguir.

### 1. Calculadora

Ao entrar no app, selecione a opção "Calculadora" para ter acesso a esta funcionalidade.

1. Digite o primeiro número da operação, utilizando o ponto como divisor de milhares, caso necessário.
2. Modifique o sinal do número, caso necessário, clicando no botão "+/-" para alternar entre positivo e negativo.
3. Selecione uma das opções de operação: adição (+), subtração (-), multiplicação (x) ou divisão (÷).
4. Selecione o símbolo de igualdade (=) para obter o resultado ou selecione uma nova operação para continuar encadeando operações. À medida que as operações são encadeadas, será demonstrado o resultado parcial.
5. Clique em "CE" caso deseje limpar o cálculo feito.

### 2. Agendamento de Tarefas

Ao entrar no app, selecione a opção "Agenda" para ter acesso a esta funcionalidade.

Para adicionar uma tarefa:

1. Clique em "Add Task" para adicionar uma nova tarefa.
2. Digite o nome da tarefa, a sua descrição e, opcionalmente, marque uma data e uma hora para receber uma notificação desta tarefa.

As tarefas agendadas aparecerão na tela raiz desta funcionalidade, agrupadas por data, ou no final da lista caso não possua data.

Para modificar uma tarefa clique no nome da tarefa, edite os campos que deseja alterar e salve.

Para excluir uma tarefa clique no ícone de lixeira ao lado do nome da tarefa e confirme a exclusão.

## Arquitetura do algoritmo

### Calculadora

O algoritmo da calculadora foi dividido em uma camada de domínio, contendo a lógica de negócio, e uma camada de apresentação, contendo a lógica para criação, exibição e gerenciamento de estados das activities.

#### Domínio

Os casos de uso fazem parte do domínio da aplicação.

A calculadora possui uma interface que define o contrato para implementação dos casos de uso. Este contrato define a entrada de dois valores e a saída de um valor.

Os casos de uso que implementam essa interface são a adição, subtração, multiplicação e divisão.

#### Apresentação

A classe ViewModel faz uso das implementações dos casos de uso para retornar os valores de resultado de acordo com a operação escolhida pelo usuário na Activity. Esta classe também é responsável por gerenciar o ciclo de vida da activity.

A classe Activity cria a GUI da calculadora, com as teclas exibidas aos usuários e seus respectivos valores, de modo a permitir a realização de operações matemáticas.

### Agenda

O algoritmo da agenda é dividido entre uma camada de dados, uma camada de domínio e uma camada de apresentação.

#### Domínio

No domínio da aplicação são definida a classe de dados Task, que empacota os dados possíveis para uma determinada tarefa criada pelo usuário, sendo eles um id, um nome, uma descrição, uma data de criação, uma data de edição, um dia de agendamento e um horário para notificação.

Além disso é definida uma interface que define os tipos de operação que deverão ser efetuados no banco de dados, que são adição, recuperação, atualização e exclusão. Todas essas operações são feitas a partir de instâncias de tarefas.

#### Dados

A camada de dados é constituída pela classe DataBaseHelper que cria um banco de dados, define o esquema da tabela que armazena as tarefas e a cria e define as operações possíveis de serem feitas neste banco de dados.

Outra classe desta camada é a DataRepositoryImpl, que é a implementação da interface de operações possíveis para as instâncias de tarefas, definida no domínio. Esta classe define como cada função da interface interage com o banco de dados criado no DataBaseHelper.

#### Apresentação

A apresentação é composta pela classe TaskViewModel que instancia o banco de dados e utiliza as funções implementadas em DataRepositoryImpl para efetivamente fazer operações no banco de dados, manipulando as instâncias de tarefas.

Cada uma das telas da aplicação, seja a tela de apresentação das tarefas armazenadas, a de criação de novas tarefas, a de modificação de tarefas e a de exclusão de tarefas, utiliza uma instância do ViewModel para armazenar seu estado e fazer as operações no banco de dados, além de apresentar os dados das tarefas na tela do usuário.