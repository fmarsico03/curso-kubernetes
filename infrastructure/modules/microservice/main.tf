resource "aws_security_group" "this" {
  name        = "${var.name}-sg"
  description = "SG for ${var.name}"
  vpc_id      = var.vpc_id

  dynamic "ingress" {
    for_each = var.allowed_ports
    content {
      description = "Port ${ingress.value}"
      from_port   = ingress.value
      to_port     = ingress.value
      protocol    = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
    }
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "microservice" {
  ami           = var.ami
  instance_type = var.instance_type

  vpc_security_group_ids = [aws_security_group.this.id]

  user_data = templatefile("${path.module}/user_data.sh", {
    name         = var.name
    repo_url     = var.repo_url
    compose_file = var.compose_file
  })

  tags = {
    Name = var.name
  }
}

output "public_ip" {
  value = aws_instance.microservice.public_ip
}
