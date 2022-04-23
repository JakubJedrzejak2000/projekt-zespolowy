provider "azurerm" {
  features {}
}


variable "resourcegroupname" {
  description = "Resource group name"
}

resource "azurerm_resource_group" "resource_group" {
  name     = var.resourcegroupname
  location = "West Europe"
}

resource "azurerm_app_service_plan" "service_plan" {
  name                = "service-plan-frontend-quote-service"
  location            = azurerm_resource_group.resource_group.location
  resource_group_name = azurerm_resource_group.resource_group.name

  sku {
    tier = "Standard"
    size = "S1"
  }
}

resource "azurerm_app_service" "frontend_app_service" {
  name                = "frontend-app-service-quote-service"
  location            = azurerm_resource_group.resource_group.location
  resource_group_name = azurerm_resource_group.resource_group.name
  app_service_plan_id = azurerm_app_service_plan.service_plan.id

  source_control {
    repo_url = "https://github.com/JakubJedrzejak2000/projekt-zespolowy"
    branch   = "frontend"
  }
}
