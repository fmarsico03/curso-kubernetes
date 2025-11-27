terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region  = "us-east-1"
  profile = "terraform"
}

data "aws_vpc" "default" {
  default = true
}

module "msvc_cursos" {
  source        = "./modules/microservice"

  name          = "msvc-cursos"
  repo_url      = "https://github.com/fmarsico03/curso-kubernetes"
  compose_file  = "docker-compose-cursos.yaml"
  allowed_ports = [8082]
  vpc_id        = data.aws_vpc.default.id
}

module "msvc_usuarios" {
  source        = "./modules/microservice"

  name          = "msvc-usuarios"
  repo_url      = "https://github.com/fmarsico03/curso-kubernetes"
  compose_file  = "docker-compose-usuarios.yaml"
  allowed_ports = [8081]
  vpc_id        = data.aws_vpc.default.id
}

module "msvc_gateway" {
  source        = "./modules/microservice"

  name          = "msvc-gateway"
  repo_url      = "https://github.com/fmarsico03/curso-kubernetes"
  compose_file  = "docker-compose-gateway.yaml"
  allowed_ports = [8080]
  vpc_id        = data.aws_vpc.default.id
  usuarios_url = "${module.msvc_usuarios.private_ip}:8081"
  cursos_url   = "${module.msvc_cursos.private_ip}:8082"
}
