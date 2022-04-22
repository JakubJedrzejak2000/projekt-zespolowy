# Projekt Zespołowy/Budowa i Administracja w Chmurze - Projekt Zaliczeniowy "QuotesReader"

## Autorzy
Olga Bera 55242, Kordian Mikołajczyk 72133, Oskar Skalski 70904, Jakub Jędrzejak 71681

## Założenia
Aplikacja ma za zadanie generować cytaty zapisane w bazie danych w sposób losowy. Po wejściu na stronę główną zostanie wygenerowany cytat. Użytkownik może wygenerować kolejny po naciśnięciu odpowiedniego przycisku. Tak samo, po naciśnięciu odpowiedniego przycisku cytat zostanie przeczytany użytkownikowi na głos.
Uzytkownik ma możliwość zaproponowania własnego cytatu, który trafi do bazy danych dla tymczasowych cytatów. Następnie administrator będzie mógł rozpatrzyć i wrzucić cytat do głównej bazy danych, albo całkowicie odrzucić.

## Technologia


###### Wybrane technologie mogą ulec zmianie na przestrzeni pracy nad projektem.

### Języki programowania

* Frontend
  * HTML5
  * CSS3
  * JavaScript
  * Bootstrap
* Backend
  * Java
  * SpringBoot
  * Swagger
  * JUNIT 5
  * PostgreSQL
  * Mockito

### Technologie chmurowe

 * Azure Wev App
 * Azure Flexible Postgresql Database

### Narzędzia
 * Grafana
 * Prometheus


## Architektura
* Klient - Serwer


## Uruchomienie aplikacji lokalnie
Aby ułatwić uruchamianie aplikacji lokalnie na komputerze, stworzony został plik docker-compose. Wystarczy w katalogu z tym plikiem wpisać
```
docker-compose up
```
Plik docker-compose zawiera w sobie bazę danych, backend oraz Prometheusa wraz z Grafaną. W celu uruchomienia frontendu należy wpisać najpierw komendę
```
npm i
```
Zainstaluje ona wszelkie potrzebne do działania biblioteki. Komenda
```
npm run watch
```
Uruchomi aplikację

## Uruchomienie aplikacji w chmurze Azure'a

Aplikację można uruchomić w chmurze Azure'a. Aby to uzyskać, użyty został zarówno Terraform jak i Azure CLI. Terraform służ do zuploadowania frontendu oraz stworzenia grupy zasobów.
Zdeployowanie aplikacji przy pomocy Terraforma jest proste. Trzeba najpierw wpisać komendę
```
terraform init
```
Jeśli wszystko zakończyło się sukcesem, następną komendą będzie
```
terraform apply
```
Terraform zapyta użytkownika o dane, które ten musi podać. Następnie wystarczy jeszcze wpisać "yes" i aplikacja została zdeployowana.

Zdeployowanie bazy danych oraz frontendu odbywa się za pomocą komend azure cli.
Najpierw trzeba określić kilka zmiennych, które będą się powtarzac
```
export resource_group={nazwa grupy zasobow, którą podano wcześniej}
export user_password={haslo uzytkownika}
export user_login={login uzytkownika}
export plan_name={nazwa planu}
export webapp_name={nazwa aplikacji}
```
Po stworzeniu kilku zmiennych, należy rozpocząć od stworzenia bazy danych postgreSQL
```
az postgres flexible-server create \
--admin-password $user_password \
--admin-user $user_login \
--location eastus \
--name service-db \
--resource-group $resource_group \
--storage-size 32 \
--public-access all \
--sku-name Standard_B1ms \
--tier Burstable \
--public-access 0.0.0.0
```
Po udanym zdeployowaniu Bazy Danych, konsola wyświetli kilka przydatnych danych. Warto zapisać sobie nazwę bazy danych oraz hosta.
```
export databse_name={wartosc databaseName}
export host{wartosc host}
```

Następnym krokiem jest stworzenie Serivce Planu dla Web appa.
```
az appservice plan create \
--name $plan_name \
--resource-group $resource_group \
--location eastus \
--sku P1V2 \
--is-linux
```
Po udanym zdeployowaniu trzeba jeszcze stworzyć samą aplikację.
```
az webapp create \
--name $webapp_name \
--plan $plan_name \
--resource-group $resource_group \
--deployment-container-image-name oskarskalski/quote-service:latest
```
Aby aplikacja działa w pełni sprawnie należy dorzucić jeszcze zmienne środowiskowe.
```
az webapp config appsettings set \
--name $webapp_name \
--resource-group $resource_group \
--settings ENVIRONMENT=$host \
DATABASE_NAME=$database_name \
DATABASE_USERNAME=$user_login \
DATABASE_PASSWORD=$user_password
```
