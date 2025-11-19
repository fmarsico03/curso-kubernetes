terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

data "aws_vpc" "default" {
  default = true
}

module "msvc_cursos" {
  source        = "./modules/microservice"

  name          = "msvc-cursos"
  repo_url      = "https://github.com/fmarsico8/curso-kubernetes.git"
  compose_file  = "docker-compose-cursos.yaml"
  allowed_ports = [8082]
  vpc_id        = data.aws_vpc.default.id
}

module "msvc_usuarios" {
  source        = "./modules/microservice"

  name          = "msvc-usuarios"
  repo_url      = "https://github.com/fmarsico8/curso-kubernetes.git"
  compose_file  = "docker-compose-usuarios.yaml"
  allowed_ports = [8081]
  vpc_id        = data.aws_vpc.default.id
}
