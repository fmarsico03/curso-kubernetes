variable "name" {
  type = string
}

variable "ami" {
  type    = string
  default = "ami-0c7217cdde317cfec"
}

variable "instance_type" {
  type    = string
  default = "t3.micro"
}

variable "repo_url" {
  type = string
}

variable "compose_file" {
  type = string
}

variable "allowed_ports" {
  type = list(number)
}

variable "vpc_id" {
  type = string
}

variable "usuarios_url" {
  type    = string
  default = ""
}

variable "cursos_url" {
  type    = string
  default = ""
}


