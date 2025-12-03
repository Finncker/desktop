# 🏦 Finncker – Financial Tracker Desktop

**Slogan:** *A financial tracker to help people manage their finances.*

O **Finncker** é um aplicativo desktop moderno desenvolvido em **Java + JavaFX** para ajudar usuários a organizar suas finanças de forma simples, rápida e intuitiva. Ele permite registrar transações, controlar contas, visualizar gráficos e preparar previsões futuras — tudo com uma interface amigável e responsiva.

---

## ✨ Principais Funcionalidades

### 🔹 Registro de Transações
Cadastre receitas e despesas com detalhes como data, conta, categoria e descrição.

### 🔹 Gestão de Contas
Gerencie múltiplas fontes de dinheiro, como contas bancárias, carteira e reservas.

### 🔹 Organização por Categorias
Classifique transações para facilitar análises e relatórios.

### 🔹 Dashboard Visual e Intuitivo
Acompanhe saldo, fluxo de caixa e métricas importantes em um único lugar.

### 🔮 Roadmap com IA (Futuro)
- Categorização automática de transações  
- Previsão de saldo com base em históricos e padrões  
- Detecção de gastos incomuns

---

## 🧱 Arquitetura e Tecnologias

### 📌 Stack Tecnológico

| Categoria | Tecnologia | Versão / Detalhes |
|----------|------------|------------------|
| **Linguagem** | Java | JDK **24** |
| **Interface** | JavaFX | Versão **22** |
| **Build** | Gradle | Kotlin DSL (`build.gradle.kts`) |
| **Testes** | JUnit | Jupiter 5.10.2 |
| **Mocking** | Mockito | 5.7.0 |
| **Utilidades** | Lombok, Guava, SLF4J | Redução de boilerplate + logging |

---

## 🧩 Arquitetura MSCR

O projeto segue o padrão **MSCR**:

- **Model** → Entidades, enums e classes de domínio  
- **Service** → Regras de negócio  
- **Controller** → Comunicação com interface e FXML  
- **Resource** → Arquivos (CSS, FXML, ícones, fontes)

Estrutura simplificada:

```
src/
└── main/
    ├── java/
    │   └── com.github.finncker.desktop/
    │       ├── controller/
    │       ├── model/
    │       ├── service/
    │       └── Main.java
    └── resources/
        ├── css/
        ├── fxml/
        ├── images/
        └── style/
```

---

## 🚀 Como Executar o Projeto

### ✔ Pré-requisitos
- **JDK 24** instalado  
- Gradle via **wrapper** (`./gradlew`)

### ▶️ 1. Build da Aplicação
```
./gradlew clean build
```

### ▶️ 2. Executar a Aplicação
```
./gradlew run
```

---

## 🧪 Testes Unitários

O projeto possui uma suíte completa de testes unitários localizada em:

```
src/test/java/com/github/finncker/desktop/
```

A camada de testes segue a mesma estrutura da aplicação principal, separando testes por serviços e componentes específicos.

### 🔧 Tecnologias Utilizadas nos Testes

| Ferramenta | Uso |
|-----------|------|
| **JUnit Jupiter (JUnit 5)** | Framework principal para escrita e execução dos testes |
| **Mockito + Mockito JUnit Jupiter** | Criação de mocks para isolar dependências |
| **AssertJ** | Asserções mais legíveis e completas |
| **Lombok (para testes também)** | Reduz código repetitivo em classes auxiliares |
| **JUnit Platform Launcher** | Usado pelo Gradle para execução automatizada |

### 📁 Estrutura dos Testes

```
src/
└── test/
    └── java/
        └── com/github/finncker/desktop/
            ├── service/
            │   ├── AccountServiceTest.java
            │   ├── TransactionServiceTest.java
            │   └── CategoryServiceTest.java
            └── controller/
                └── TransactionsControllerTest.java
```

### ▶️ Executando os Testes

```
./gradlew test
```

### 📊 Relatórios de Teste

Localização dos relatórios:

```
build/reports/tests/test/index.html
```

---

## 👥 Equipe

Projeto desenvolvido pelos integrantes:  
Arthur Félix • Guilherme Caetano • Gustavo Santos • Heverton Borges  
João Lima • Maxsuel Santos • Pedro Almas  

---

## 📄 Licença
Projeto acadêmico livre para estudo e aprimoramento.

---

## ⭐ Contribuições
Pull Requests, Issues e Sugestões são bem-vindas!

---

## 🔗 Repositório
GitHub: www.github.com/Finncker/desktop
