# FinTrack - Projeto Intermediario

Continuacao do FinTrack (gerenciador de financas pessoais).
Agora o sistema tem interface grafica com JavaFX e salva os dados num banco SQLite em vez do arquivo txt.

## Como rodar

- Precisa do JDK 21 ou mais novo.
- Pelo terminal: `mvn javafx:run`
- Pela IDE: rodar a classe `com.fintrack.app.Main`
- Testes: `mvn test`

O banco (`fintrack.db`) e criado sozinho na primeira vez que roda.

## O que tem no projeto

- **Generics:** classe `RepositorioGenerico<T>` (com `? extends T`, `? super T` e `?`)
- **JavaFX:** tela principal com tabela, tela de cadastro/edicao e tela de relatorio
- **FXML e CSS:** arquivos `.fxml` e `style.css` na pasta `resources/com/fintrack/view`
- **JDBC:** `Conexao` e `TransacaoDAO` (inserir, listar, atualizar, remover e commit/rollback)
- **JUnit 5:** testes do modelo, do repositorio, do DAO (banco em memoria) e do calculo de saldo
