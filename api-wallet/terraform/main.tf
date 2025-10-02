terraform {
  required_providers {
    random = {
      source  = "hashicorp/random"
      version = "~> 3.6.2"
    }
  }
}

provider "aws" {
  region = local.aws-region
}

variable "account_id" {
  description = "AWS Account ID"
  default     = "932043840972"
}

resource "aws_sqs_queue" "deposit_queue" {
  name                        = "deposit_queue"
}

resource "aws_sqs_queue" "transfer_queue" {
  name                        = "transfer_queue"
}
