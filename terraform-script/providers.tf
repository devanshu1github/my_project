terraform {
  required_providers {
      aws = {
      source = "hashicorp/aws"
      version = "~> 4.38"
    }
    kubernetes = {
      source = "hashicorp/kubernetes"
      version = "~> 2.15"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}
