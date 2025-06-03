## Requisitos

Para rodar o projeto em sua máquina, é necessário ter as seguintes instalações:

- **Maven** 3.6.3 ou posterior
- **Java** 17 ou posterior
- **Javac** 17 (deve estar alinhado com a versão do Maven)
- **PostgreSQL** 11 ou posterior (recomendado: 12+)

**Observação:** Mesmo que você utilize uma versão posterior ao Java 17, o compilador Java (`javac`) precisa estar
apontando para a versão 17 para garantir compatibilidade com o Maven.

### 1. Clonando o Repositório

Para obter o código-fonte do projeto, execute:

```sh
git clone https://github.com/Kauan-Code19/userService.git
cd traduzzo_api
```
Caso queira clonar uma branch específica, utilize:
```sh
git clone --branch <nome-da-branch> https://github.com/Kauan-Code19/userService.git
cd traduzzo_api
```

### 2. Verificando e Instalando o Maven

Verifique se o Maven está instalado corretamente executando:

```sh
mvn -version
```

Se a versão for menor que a necessária, atualize ou instale o Maven:

**Ubuntu/Debian:**

```sh
sudo apt update && sudo apt install maven -y
```

**Fedora/RHEL:**

```sh
sudo dnf install maven -y
```

### 3. Verificando e Instalando o Java

Verifique se o Java está instalado corretamente executando:

```sh
java -version
```

Se a versão for menor que a necessária, atualize ou instale o Java conforme sua distribuição:

**Ubuntu/Debian:**

```sh
sudo apt update && sudo apt upgrade openjdk-* -y
sudo apt install openjdk-17-jdk -y
```

**Fedora/RHEL:**

```sh
sudo dnf update && sudo dnf install java-17-openjdk -y
```

### 4. Verificando e Configurando o `javac`

Após instalar o Java, é essencial garantir que o `javac` esteja alinhado com a versão utilizada pelo Maven.
Verifique a versão do `javac` executando:

```sh
javac -version
```

Se a versão do `javac` não corresponder ao Java configurado, atualize a alternativa do `javac` usando:

```sh
sudo update-alternatives --config javac
```

Escolha a opção que corresponda ao Java 17 (ou à versão utilizada pelo Maven).

Caso a opção do `javac` 17 não esteja disponível, adicione manualmente com o seguinte comando:

```sh
sudo update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/java-17-openjdk-amd64/bin/javac 100
```

Depois, execute novamente:

```sh
sudo update-alternatives --config javac
```

Selecione a opção correspondente ao Java 17.

E depois verifique a versão do javac novamente:
```sh
javac -version
```

### 5. Verificando e Instalando o PostgreSQL

Verifique se o PostgreSQL está instalado corretamente executando:

```sh
psql --version
```

Se a versão for menor que a necessária, atualize ou instale conforme sua distribuição:

**Ubuntu/Debian:**

```sh
sudo apt update
sudo apt install -y postgresql postgresql-contrib
```

**Fedora/RHEL:**

```sh
sudo dnf install -y postgresql-server postgresql-contrib
```

Após a instalação, verifique se o banco de dados já está inicializado:
```sh
ls /var/lib/pgsql/data
```

Se o diretório não existir ou estiver vazio, inicialize o banco de dados:
```sh
sudo postgresql-setup --initdb
```

Em seguida, ative e inicie o serviço:
```sh
sudo systemctl enable postgresql
sudo systemctl start postgresql
```

Verifique se o serviço está rodando:

```sh
sudo systemctl status postgresql
```

## Configuração do PostgreSQL

### Criando um usuário e banco de dados
Após instalar o PostgreSQL, siga os passos abaixo para criar um usuário e um banco de dados para a API:

1. Acesse o PostgreSQL como usuário `postgres`:

   ```sh
   sudo -i -u postgres
   psql
   ```

2. Crie um novo usuário:

   ```sql
   CREATE USER api_user WITH PASSWORD 'sua_senha';
   ```

3. Crie o banco de dados:

   ```sql
   CREATE DATABASE banco_db OWNER api_user;
   ```

4. Conceda as permissões necessárias:

   ```sql
   GRANT ALL PRIVILEGES ON DATABASE banco_db TO api_user;
   ```

5. Saia do PostgreSQL:

   ```sql
   \q
   exit
   ```

### Permitindo conexões remotas (se necessário)
Se precisar acessar o banco de dados remotamente, siga estas etapas:

1. Edite o arquivo de configuração `postgresql.conf` para permitir conexões remotas:

   ```sh
   sudo nano /etc/postgresql/<versao>/main/postgresql.conf
   ```

   Altere a linha:

   ```conf
   listen_addresses = 'localhost'
   ```

   Para:

   ```conf
   listen_addresses = '*'
   ```

2. Edite o arquivo `pg_hba.conf` para permitir conexões de IPs específicos:

   ```sh
   sudo nano /etc/postgresql/<versao>/main/pg_hba.conf
   ```

   Adicione a linha:

   ```conf
   host    all             all             0.0.0.0/0               md5
   ```

3. Reinicie o serviço PostgreSQL para aplicar as mudanças:

   ```sh
   sudo systemctl restart postgresql
   ```

## Execução da API

Para compilar e executar a API, utilize o Maven:

```sh
mvn clean install
mvn spring-boot:run
```

A API será executada na porta padrão **8080**. Se necessário, altere a porta no arquivo de propriedades:

```properties
server.port=8080
```
