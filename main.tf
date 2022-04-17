provider "azurerm" {
  features {

  }
}

resource "azurerm_resource_group" "example" {
  name     = "api-rg-pro"
  location = "West Europe"
}

variable "login" {
  description = "Administrator login for postgresql database"
}

variable "password" {
  description = "Aministrator password for postgresql database"

}

resource "azurerm_postgresql_server" "example" {
  name                = "moj-testowy-serwer-na-projekt-jakubj"
  location            = azurerm_resource_group.example.location
  resource_group_name = azurerm_resource_group.example.name

  sku_name = "B_Gen5_2"

  storage_mb                   = 5120
  backup_retention_days        = 7
  geo_redundant_backup_enabled = false
  auto_grow_enabled            = true

  administrator_login          = var.login
  administrator_login_password = var.password
  version                      = "11"
  ssl_enforcement_enabled      = true
}

resource "azurerm_postgresql_firewall_rule" "example" {
  name                = "office"
  resource_group_name = azurerm_resource_group.example.name
  server_name         = azurerm_postgresql_server.example.name
  start_ip_address    = "0.0.0.0"
  end_ip_address      = "255.255.255.255"
}

resource "azurerm_postgresql_database" "example" {
  name                = "exampledb"
  resource_group_name = azurerm_resource_group.example.name
  server_name         = azurerm_postgresql_server.example.name
  charset             = "UTF8"
  collation           = "English_United States.1252"

}

# Create the Linux App Service Plan
resource "azurerm_app_service_plan" "appserviceplan" {
  name                = "webapp-asp-frontend-quote-generator-jakubj"
  location            = azurerm_resource_group.example.location
  resource_group_name = azurerm_resource_group.example.name
  sku {
    tier = "Free"
    size = "F1"
  }
}
# Create the web app, pass in the App Service Plan ID, and deploy code from a public GitHub repo
resource "azurerm_app_service" "webapp" {
  name                = "webapp-frontend-quote-generator-jakubj"
  location            = azurerm_resource_group.example.location
  resource_group_name = azurerm_resource_group.example.name
  app_service_plan_id = azurerm_app_service_plan.appserviceplan.id
  source_control {
    repo_url           = "https://github.com/JakubJedrzejak2000/projekt-zespolowy"
    branch             = "master"
    manual_integration = true
    use_mercurial      = false
  }
}

locals {
  env_variables = {
    ENVIRONMENT       = "${azurerm_postgresql_database.example.server_name}.postgres.database.azure.com"
    DATABASE_NAME     = "postgres"
    DATABASE_USERNAME = "${azurerm_postgresql_server.example.administrator_login}@${azurerm_postgresql_database.example.server_name}"
    DATABASE_PASSWORd = var.password
  }
}

output "values" {
  value = azurerm_app_service.webapp
}

output "values2" {
  value = azurerm_app_service_plan.appserviceplan
}

output "databse" {
  value = azurerm_postgresql_database.example
}

output "dbserver" {
  sensitive = true
  value     = azurerm_postgresql_server.example
}

output "locals" {
  value = local.env_variables
}


resource "azurerm_container_group" "example" {
  name                = "example-containet-jakubj"
  location            = azurerm_resource_group.example.location
  resource_group_name = azurerm_resource_group.example.name
  ip_address_type     = "Public"
  # dns_name_label      = "aci-label"
  os_type             = "Linux"

  container {
    name   = "quote-generator"
    image  = "oskarskalski/quote-service:latest"
    cpu    = "0.5"
    memory = "1.5"

    ports {
      port     = 443
      protocol = "TCP"
    }
  }
}

output "docker" {
  value     = azurerm_container_group.example.container
  sensitive = true
}

resource "azurerm_app_service" "webappdockerfrontend" {
  depends_on = [
    azurerm_app_service.webapp
  ]
  name                = "docker-backend-quote-generator-jakubj"
  location            = azurerm_resource_group.example.location
  resource_group_name = azurerm_resource_group.example.name
  app_service_plan_id = azurerm_app_service_plan.appserviceplan.id
  site_config {
    scm_type  = "VSTSRM"
    always_on = "true"

    linux_fx_version = "DOCKER|20.101.165.47"
  }
  app_settings = local.env_variables
}